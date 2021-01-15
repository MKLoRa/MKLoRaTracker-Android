package com.moko.loratrackerv2.activity;


import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.moko.ble.lib.MokoConstants;
import com.moko.ble.lib.event.ConnectStatusEvent;
import com.moko.ble.lib.event.OrderTaskResponseEvent;
import com.moko.ble.lib.task.OrderTask;
import com.moko.ble.lib.task.OrderTaskResponse;
import com.moko.ble.lib.utils.MokoUtils;
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
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class VibrationSettingActivity extends BaseActivity {


    @BindView(R2.id.tv_vibration_intensity)
    TextView tvVibrationIntensity;
    @BindView(R2.id.et_vibration_cycle)
    EditText etVibrationCycle;
    @BindView(R2.id.et_vibration_duration)
    EditText etVibrationDuration;
    private boolean mReceiverTag = false;
    private boolean savedParamsError;

    private ArrayList<String> mValues;
    private int mSelected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loratracker_activity_vibration_setting);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        mValues = new ArrayList<>();
        mValues.add("Low");
        mValues.add("Medium");
        mValues.add("Strong");

        // 注册广播接收器
        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        registerReceiver(mReceiver, filter);
        mReceiverTag = true;
        showSyncingProgressDialog();
        List<OrderTask> orderTasks = new ArrayList<>();
        orderTasks.add(OrderTaskAssembler.getVibrationIntansity());
        orderTasks.add(OrderTaskAssembler.getVibrationCycle());
        orderTasks.add(OrderTaskAssembler.getVibrationDuration());
        LoRaTrackerMokoSupport.getInstance().sendOrder(orderTasks.toArray(new OrderTask[]{}));
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
        final String action = event.getAction();
        runOnUiThread(() -> {
            if (MokoConstants.ACTION_ORDER_TIMEOUT.equals(action)) {
            }
            if (MokoConstants.ACTION_ORDER_FINISH.equals(action)) {
                dismissSyncProgressDialog();
            }
            if (MokoConstants.ACTION_ORDER_RESULT.equals(action)) {
                EventBus.getDefault().cancelEventDelivery(event);
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
                                    case KEY_VIBRATION_INTENSITY:
                                    case KEY_VIBRATION_DURATION:
                                        if (result != 1) {
                                            savedParamsError = true;
                                        }
                                        break;
                                    case KEY_VIBRATION_CYCLE:
                                        if (result != 1) {
                                            savedParamsError = true;
                                        }
                                        if (savedParamsError) {
                                            ToastUtils.showToast(VibrationSettingActivity.this, "Opps！Save failed. Please check the input characters and try again.");
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
                                    case KEY_VIBRATION_INTENSITY:
                                        if (length > 0) {
                                            int intensity = value[4] & 0xFF;
                                            switch (intensity) {
                                                case 10:
                                                    mSelected = 0;
                                                    tvVibrationIntensity.setText(mValues.get(0));
                                                    break;
                                                case 50:
                                                    mSelected = 1;
                                                    tvVibrationIntensity.setText(mValues.get(1));
                                                    break;
                                                case 100:
                                                    mSelected = 2;
                                                    tvVibrationIntensity.setText(mValues.get(2));
                                                    break;
                                            }
                                        }
                                        break;
                                    case KEY_VIBRATION_DURATION:
                                        if (length > 0) {
                                            int duration = value[4] & 0xFF;
                                            etVibrationDuration.setText(String.valueOf(duration));
                                        }
                                        break;
                                    case KEY_VIBRATION_CYCLE:
                                        if (length > 0) {
                                            byte[] cycleBytes = Arrays.copyOfRange(value, 4, 4 + length);
                                            int cycle = MokoUtils.toInt(cycleBytes);
                                            if (cycle >= 1 && cycle <= 600) {
                                                etVibrationCycle.setText(String.valueOf(cycle));
                                            }
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

    public void onSave(View view) {
        if (isValid()) {
            if (isDurationLessThanCycle()) {
                showSyncingProgressDialog();
                saveParams();
            }
        } else {
            ToastUtils.showToast(this, "Opps！Save failed. Please check the input characters and try again.");
        }
    }

    private boolean isValid() {
        final String durationStr = etVibrationDuration.getText().toString();
        final String cycleStr = etVibrationCycle.getText().toString();
        if (TextUtils.isEmpty(durationStr))
            return false;
        if (TextUtils.isEmpty(cycleStr))
            return false;
        int duration = Integer.parseInt(durationStr);
        if (duration > 10)
            return false;
        if (TextUtils.isEmpty(cycleStr))
            return false;
        int cycle = Integer.parseInt(cycleStr);
        if (cycle < 1 || cycle > 600)
            return false;
        return true;
    }

    public boolean isDurationLessThanCycle() {
        final String durationStr = etVibrationDuration.getText().toString();
        final String cycleStr = etVibrationCycle.getText().toString();
        int duration = Integer.parseInt(durationStr);
        int cycle = Integer.parseInt(cycleStr);
        if (duration > cycle) {
            AlertMessageDialog dialog = new AlertMessageDialog();
            dialog.setCancelGone();
            dialog.setMessage("Vibration Cycle should be no less than Duration of  Vibration");
            dialog.setConfirm("OK");
            dialog.show(getSupportFragmentManager());
            return false;
        }
        return true;
    }


    private void saveParams() {
        int intensity = 0;
        switch (mSelected) {
            case 0:
                intensity = 10;
                break;
            case 1:
                intensity = 50;
                break;
            case 2:
                intensity = 100;
                break;
        }
        final String durationStr = etVibrationDuration.getText().toString();
        final String cycleStr = etVibrationCycle.getText().toString();
        int duration = Integer.parseInt(durationStr);
        int cycle = Integer.parseInt(cycleStr);
        List<OrderTask> orderTasks = new ArrayList<>();
        orderTasks.add(OrderTaskAssembler.setVibrationIntensity(intensity));
        orderTasks.add(OrderTaskAssembler.setVibrationDuration(duration));
        orderTasks.add(OrderTaskAssembler.setVibrationCycle(cycle));
        LoRaTrackerMokoSupport.getInstance().sendOrder(orderTasks.toArray(new OrderTask[]{}));
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
                            VibrationSettingActivity.this.setResult(RESULT_OK);
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

    public void onSelectVibrationIntensity(View view) {
        BottomDialog dialog = new BottomDialog();
        dialog.setDatas(mValues, mSelected);
        dialog.setListener(value -> {
            tvVibrationIntensity.setText(mValues.get(value));
            mSelected = value;
        });
        dialog.show(getSupportFragmentManager());
    }
}
