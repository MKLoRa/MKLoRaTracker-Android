package com.moko.loratrackerv2.activity;


import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

import com.moko.ble.lib.MokoConstants;
import com.moko.ble.lib.event.ConnectStatusEvent;
import com.moko.ble.lib.event.OrderTaskResponseEvent;
import com.moko.ble.lib.task.OrderTask;
import com.moko.ble.lib.task.OrderTaskResponse;
import com.moko.loratrackerv2.R;
import com.moko.loratrackerv2.R2;
import com.moko.loratrackerv2.dialog.AlertMessageDialog;
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

import butterknife.BindView;
import butterknife.ButterKnife;

public class SOSFucntionActivity extends BaseActivity {

    private static final int OPTIONAL_PAYLOAD_TIMESTAMP_ENABLE = 1;
    private static final int OPTIONAL_PAYLOAD_MAC_ENABLE = 2;

    @BindView(R2.id.cb_sos_switch)
    CheckBox cbSosSwitch;
    @BindView(R2.id.et_sos_report_interval)
    EditText etSosReportInterval;
    @BindView(R2.id.cb_sos_timestamp)
    CheckBox cbSosTimestamp;
    @BindView(R2.id.cb_sos_mac)
    CheckBox cbSosMac;
    private boolean mReceiverTag = false;
    private boolean savedParamsError;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loratracker_activity_sos_function);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);

        // 注册广播接收器
        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        registerReceiver(mReceiver, filter);
        mReceiverTag = true;
        showSyncingProgressDialog();
        List<OrderTask> orderTasks = new ArrayList<>();
        orderTasks.add(OrderTaskAssembler.getSOSEnable());
        orderTasks.add(OrderTaskAssembler.getSOSReportInterval());
        orderTasks.add(OrderTaskAssembler.getSOSOptionalPayload());
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
                                    case KEY_SOS_ENABLE:
                                    case KEY_SOS_REPORT_INTERVAL:
                                        if (result != 1) {
                                            savedParamsError = true;
                                        }
                                        break;
                                    case KEY_OPTIONAL_PAYLOAD_SOS:
                                        if (result != 1) {
                                            savedParamsError = true;
                                        }
                                        if (savedParamsError) {
                                            ToastUtils.showToast(SOSFucntionActivity.this, "Opps！Save failed. Please check the input characters and try again.");
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
                                    case KEY_SOS_ENABLE:
                                        if (length > 0) {
                                            int enable = value[4] & 0xFF;
                                            cbSosSwitch.setChecked(enable == 1);
                                        }
                                        break;
                                    case KEY_SOS_REPORT_INTERVAL:
                                        if (length > 0) {
                                            int interval = value[4] & 0xFF;
                                            etSosReportInterval.setText(String.valueOf(interval));
                                        }
                                        break;
                                    case KEY_OPTIONAL_PAYLOAD_SOS:
                                        if (length > 0) {
                                            int optionalPayload = value[4] & 0xFF;
                                            cbSosTimestamp.setChecked((optionalPayload & OPTIONAL_PAYLOAD_TIMESTAMP_ENABLE) == OPTIONAL_PAYLOAD_TIMESTAMP_ENABLE);
                                            cbSosMac.setChecked((optionalPayload & OPTIONAL_PAYLOAD_MAC_ENABLE) == OPTIONAL_PAYLOAD_MAC_ENABLE);
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
            showSyncingProgressDialog();
            saveParams();
        } else {
            ToastUtils.showToast(this, "Opps！Save failed. Please check the input characters and try again.");
        }
    }

    private boolean isValid() {
        final String intervalStr = etSosReportInterval.getText().toString();
        if (TextUtils.isEmpty(intervalStr))
            return false;
        int interval = Integer.parseInt(intervalStr);
        if (interval < 1 || interval > 10)
            return false;
        return true;
    }


    private void saveParams() {
        final String intervalStr = etSosReportInterval.getText().toString();
        int interval = Integer.parseInt(intervalStr);
        List<OrderTask> orderTasks = new ArrayList<>();
        orderTasks.add(OrderTaskAssembler.setSOSEnable(cbSosSwitch.isChecked() ? 1 : 0));
        orderTasks.add(OrderTaskAssembler.setSOSReportInterval(interval));
        int timestamp = 0;
        int mac = 0;
        if (cbSosTimestamp.isChecked())
            timestamp = OPTIONAL_PAYLOAD_TIMESTAMP_ENABLE;
        if (cbSosMac.isChecked())
            mac = OPTIONAL_PAYLOAD_MAC_ENABLE;
        orderTasks.add(OrderTaskAssembler.setSOSOptionalPayload(timestamp | mac));
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
                            SOSFucntionActivity.this.setResult(RESULT_OK);
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
}
