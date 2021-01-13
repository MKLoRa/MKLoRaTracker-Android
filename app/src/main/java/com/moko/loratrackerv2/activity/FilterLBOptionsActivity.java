package com.moko.loratrackerv2.activity;


import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.moko.ble.lib.MokoConstants;
import com.moko.ble.lib.event.ConnectStatusEvent;
import com.moko.ble.lib.event.OrderTaskResponseEvent;
import com.moko.ble.lib.task.OrderTask;
import com.moko.ble.lib.task.OrderTaskResponse;
import com.moko.loratrackerv2.AppConstants;
import com.moko.loratrackerv2.R;
import com.moko.loratrackerv2.R2;
import com.moko.loratrackerv2.dialog.AlertMessageDialog;
import com.moko.loratrackerv2.dialog.BottomDialog;
import com.moko.loratrackerv2.dialog.LoadingMessageDialog;
import com.moko.loratrackerv2.utils.ToastUtils;
import com.moko.support.loratracker.LoRaTrackerMokoSupport;
import com.moko.support.loratracker.OrderTaskAssembler;
import com.moko.support.loratracker.entity.OrderCHAR;
import com.moko.support.loratracker.entity.ParamsKeyEnum;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import butterknife.BindView;
import butterknife.ButterKnife;

public class FilterLBOptionsActivity extends BaseActivity {

    @BindView(R2.id.tv_condition_a)
    TextView tvConditionA;
    @BindView(R2.id.tv_condition_b)
    TextView tvConditionB;
    @BindView(R2.id.tv_condition_a_title)
    TextView tvConditionATitle;
    @BindView(R2.id.tv_condition_b_title)
    TextView tvConditionBTitle;
    @BindView(R2.id.tv_relation)
    TextView tvRelation;
    @BindView(R2.id.tv_repeat)
    TextView tvRepeat;
    @BindView(R2.id.tv_title)
    TextView tvTitle;
    private boolean mReceiverTag = false;
    private boolean savedParamsError;
    private ArrayList<String> mValues;
    private int mSelected;
    private ArrayList<String> mRepeatValues;
    private int mRepeatSelected;

    private boolean isFilterAEnable;
    private boolean isFilterBEnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loratracker_activity_filter_relation);
        ButterKnife.bind(this);
        tvTitle.setText("Location Beacon Filter Options");
        tvConditionATitle.setText("Location Beacon Filter Condition A");
        tvConditionBTitle.setText("Location Beacon Filter Condition B");
        EventBus.getDefault().register(this);
        // 注册广播接收器
        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        registerReceiver(mReceiver, filter);
        mReceiverTag = true;
        if (!LoRaTrackerMokoSupport.getInstance().isBluetoothOpen()) {
            LoRaTrackerMokoSupport.getInstance().enableBluetooth();
        } else {
            showSyncingProgressDialog();
            List<OrderTask> orderTasks = new ArrayList<>();
            orderTasks.add(OrderTaskAssembler.getLBFilterSwitchA());
            orderTasks.add(OrderTaskAssembler.getLBFilterSwitchB());
            orderTasks.add(OrderTaskAssembler.getLBFilterABRelation());
            orderTasks.add(OrderTaskAssembler.getLBFilterRepeat());
            LoRaTrackerMokoSupport.getInstance().sendOrder(orderTasks.toArray(new OrderTask[]{}));
        }
        mValues = new ArrayList<>();
        mValues.add("And");
        mValues.add("Or");
        mRepeatValues = new ArrayList<>();
        mRepeatValues.add("No");
        mRepeatValues.add("MAC");
        mRepeatValues.add("MAC+Data Type");
        mRepeatValues.add("MAC+Raw Data");
    }

    @Subscribe(threadMode = ThreadMode.POSTING, priority = 200)
    public void onConnectStatusEvent(ConnectStatusEvent event) {
        final String action = event.getAction();
        runOnUiThread(() -> {
            if (MokoConstants.ACTION_DISCONNECTED.equals(action)) {
                setResult(RESULT_OK);
                finish();
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.POSTING, priority = 200)
    public void onOrderTaskResponseEvent(OrderTaskResponseEvent event) {
        EventBus.getDefault().cancelEventDelivery(event);
        final String action = event.getAction();
        runOnUiThread(() -> {
            if (MokoConstants.ACTION_ORDER_TIMEOUT.equals(action)) {
            }
            if (MokoConstants.ACTION_ORDER_FINISH.equals(action)) {
                dismissSyncProgressDialog();
            }
            if (MokoConstants.ACTION_ORDER_RESULT.equals(action)) {
                OrderTaskResponse response = event.getResponse();
                OrderCHAR orderCHAR = (OrderCHAR) response.orderCHAR;
                int responseType = response.responseType;
                byte[] value = response.responseValue;
                switch (orderCHAR) {
                    case CHAR_PARAMS:
                        if (value.length >= 4) {
                            int header = value[0] & 0xFF;// 0xED
                            int flag = value[1] & 0xFF;// read or write
                            int cmd = value[2] & 0xFF;
                            if (header != 0xED)
                                return;
                            ParamsKeyEnum configKeyEnum = ParamsKeyEnum.fromParamKey(cmd);
                            if (configKeyEnum == null) {
                                return;
                            }
                            int length = value[3] & 0xFF;
                            if (flag == 0x01) {
                                // write
                                int result = value[4] & 0xFF;
                                switch (configKeyEnum) {
                                    case KEY_LOCATION_FILTER_REPEAT:
                                    case KEY_LOCATION_FILTER_A_B_RELATION:
                                        if (result != 1) {
                                            savedParamsError = true;
                                        }
                                        if (savedParamsError) {
                                            ToastUtils.showToast(FilterLBOptionsActivity.this, "Opps！Save failed. Please check the input characters and try again.");
                                        } else {
                                            AlertMessageDialog dialog = new AlertMessageDialog();
                                            dialog.setMessage("Saved Successfully！");
                                            dialog.setConfirm("OK");
                                            dialog.setCancelGone();
                                            dialog.show(getSupportFragmentManager());
                                        }
                                        break;
                                }
                            }
                            if (flag == 0x00) {
                                // read
                                switch (configKeyEnum) {
                                    case KEY_LOCATION_FILTER_SWITCH_A:
                                        if (length == 1) {
                                            final int enable = value[4] & 0xFF;
                                            tvConditionA.setText(enable == 0 ? "OFF" : "ON");
                                            isFilterAEnable = enable == 1;
                                        }
                                        break;
                                    case KEY_LOCATION_FILTER_SWITCH_B:
                                        if (length == 1) {
                                            final int enable = value[4] & 0xFF;
                                            tvConditionB.setText(enable == 0 ? "OFF" : "ON");
                                            isFilterBEnable = enable == 1;
                                            if (isFilterAEnable && isFilterBEnable)
                                                tvRelation.setEnabled(true);
                                        }
                                        break;
                                    case KEY_LOCATION_FILTER_A_B_RELATION:
                                        if (length == 1) {
                                            final int relation = value[4] & 0xFF;
                                            tvRelation.setText(relation == 0 ? "And" : "Or");
                                            mSelected = relation;
                                        }
                                        break;
                                    case KEY_LOCATION_FILTER_REPEAT:
                                        if (length == 1) {
                                            final int repeat = value[4] & 0xFF;
                                            tvRepeat.setText(mRepeatValues.get(repeat));
                                            mRepeatSelected = repeat;
                                        }
                                        break;
                                }
                            }
                        }
                        break;
                }
            }
        });
    }


    private BroadcastReceiver mReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {

            if (intent != null) {
                String action = intent.getAction();
                if (BluetoothAdapter.ACTION_STATE_CHANGED.equals(action)) {
                    int blueState = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, 0);
                    switch (blueState) {
                        case BluetoothAdapter.STATE_TURNING_OFF:
                            dismissSyncProgressDialog();
                            FilterLBOptionsActivity.this.setResult(RESULT_OK);
                            finish();
                            break;
                    }
                }
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mReceiverTag) {
            mReceiverTag = false;
            // 注销广播
            unregisterReceiver(mReceiver);
        }
        EventBus.getDefault().unregister(this);
    }

    private LoadingMessageDialog mLoadingMessageDialog;

    public void showSyncingProgressDialog() {
        mLoadingMessageDialog = new LoadingMessageDialog();
        mLoadingMessageDialog.setMessage("Syncing..");
        mLoadingMessageDialog.show(getSupportFragmentManager());

    }

    public void dismissSyncProgressDialog() {
        if (mLoadingMessageDialog != null)
            mLoadingMessageDialog.dismissAllowingStateLoss();
    }

    public void onBack(View view) {
        finish();
    }

    public void onRelation(View view) {
        BottomDialog dialog = new BottomDialog();
        dialog.setDatas(mValues, mSelected);
        dialog.setListener(value -> {
            tvRelation.setText(value == 0 ? "And" : "Or");
            mSelected = value;
            showSyncingProgressDialog();
            LoRaTrackerMokoSupport.getInstance().sendOrder(OrderTaskAssembler.setLBFilterABRelation(value));
        });
        dialog.show(getSupportFragmentManager());
    }

    public void onRepeat(View view) {
        BottomDialog dialog = new BottomDialog();
        dialog.setDatas(mRepeatValues, mRepeatSelected);
        dialog.setListener(value -> {
            tvRepeat.setText(mRepeatValues.get(value));
            mRepeatSelected = value;
            showSyncingProgressDialog();
            LoRaTrackerMokoSupport.getInstance().sendOrder(OrderTaskAssembler.setLBFilterRepeat(value));
        });
        dialog.show(getSupportFragmentManager());
    }

    public void onLBFilterA(View view) {
        startActivityForResult(new Intent(this, FilterLBOptionsAActivity.class), AppConstants.REQUEST_CODE_FILTER);
    }

    public void onLBFilterB(View view) {
        startActivityForResult(new Intent(this, FilterLBOptionsBActivity.class), AppConstants.REQUEST_CODE_FILTER);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AppConstants.REQUEST_CODE_FILTER) {
            showSyncingProgressDialog();
            List<OrderTask> orderTasks = new ArrayList<>();
            orderTasks.add(OrderTaskAssembler.getLBFilterSwitchA());
            orderTasks.add(OrderTaskAssembler.getLBFilterSwitchB());
            orderTasks.add(OrderTaskAssembler.getLBFilterABRelation());
            LoRaTrackerMokoSupport.getInstance().sendOrder(orderTasks.toArray(new OrderTask[]{}));
        }
    }
}
