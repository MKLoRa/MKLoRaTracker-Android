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
import android.widget.ImageView;
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

import androidx.constraintlayout.widget.ConstraintLayout;
import butterknife.BindView;
import butterknife.ButterKnife;

public class ThreeAxisSensorActivity extends BaseActivity {

    private static final int OPTIONAL_PAYLOAD_MAC_ENABLE = 1;
    private static final int OPTIONAL_PAYLOAD_TIMESTAMP_ENABLE = 2;
    @BindView(R2.id.cb_3_axis_sensor_switch)
    CheckBox cb3AxisSensorSwitch;
    @BindView(R2.id.tv_3_axis_sample_rate)
    TextView tv3AxisSampleRate;
    @BindView(R2.id.tv_3_axis_g)
    TextView tv3AxisG;
    @BindView(R2.id.et_trigger_sensitivity)
    EditText etTriggerSensitivity;
    @BindView(R2.id.et_report_interval)
    EditText etReportInterval;
    @BindView(R2.id.cb_3_axis_mac)
    CheckBox cb3AxisMac;
    @BindView(R2.id.cb_3_axis_timestamp)
    CheckBox cb3AxisTimestamp;
    @BindView(R2.id.tv_3_axis_x)
    TextView tv3AxisX;
    @BindView(R2.id.tv_3_axis_y)
    TextView tv3AxisY;
    @BindView(R2.id.tv_3_axis_z)
    TextView tv3AxisZ;
    @BindView(R2.id.cl_3_axis_sensor_data)
    ConstraintLayout cl3AxisSensorData;
    @BindView(R2.id.iv_3_axis_sensor_data)
    ImageView iv3AxisSensorData;

    private boolean mReceiverTag = false;
    private boolean savedParamsError;

    private ArrayList<String> mSampleRateValues;
    private int mSampleRateSelected;
    private ArrayList<String> mGValues;
    private int mGSelected;
    private boolean mSensorDataOpen;
    private boolean mIsBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loratracker_activity_three_axis_sensor);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        mSampleRateValues = new ArrayList<>();
        mSampleRateValues.add("1HZ");
        mSampleRateValues.add("10HZ");
        mSampleRateValues.add("25HZ");
        mSampleRateValues.add("50HZ");
        mSampleRateValues.add("100HZ");
        mGValues = new ArrayList<>();
        mGValues.add("±2g");
        mGValues.add("±4g");
        mGValues.add("±8g");
        mGValues.add("±16g");
        // 注册广播接收器
        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        registerReceiver(mReceiver, filter);
        mReceiverTag = true;
        showSyncingProgressDialog();
        List<OrderTask> orderTasks = new ArrayList<>();
        orderTasks.add(OrderTaskAssembler.get3AxisEnable());
        orderTasks.add(OrderTaskAssembler.get3AxisSampleRate());
        orderTasks.add(OrderTaskAssembler.get3AxisG());
        orderTasks.add(OrderTaskAssembler.get3AxisTriggerSensitivity());
        orderTasks.add(OrderTaskAssembler.get3AxisReportInterval());
        orderTasks.add(OrderTaskAssembler.get3AxisOptionalPayload());
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
        EventBus.getDefault().cancelEventDelivery(event);
        final String action = event.getAction();
        runOnUiThread(() -> {
            if (MokoConstants.ACTION_CURRENT_DATA.equals(action)) {
                OrderTaskResponse response = event.getResponse();
                OrderCHAR orderCHAR = (OrderCHAR) response.orderCHAR;
                int responseType = response.responseType;
                byte[] value = response.responseValue;
                switch (orderCHAR) {
                    case CHAR_THREE_AXIS:
                        final int length = value.length;
                        if (length != 5)
                            return;
                        int header = value[0] & 0xFF;
                        int flag = value[1] & 0xFF;
                        int cmd = value[2] & 0xFF;
                        int len = value[3] & 0xFF;
                        if (header == 0xED && flag == 0x02 && cmd == 0x03 && len == 0x06) {
                            byte[] xBytes = Arrays.copyOfRange(value, 4, 6);
                            byte[] yBytes = Arrays.copyOfRange(value, 6, 8);
                            byte[] zBytes = Arrays.copyOfRange(value, 8, 10);
                            tv3AxisX.setText(MokoUtils.toInt(xBytes));
                            tv3AxisY.setText(MokoUtils.toInt(yBytes));
                            tv3AxisZ.setText(MokoUtils.toInt(zBytes));
                        }
                        break;
                }
            }
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
                                    case KEY_THREE_AXIS_ENABLE:
                                    case KEY_THREE_AXIS_SAMPLE_RATE:
                                    case KEY_THREE_AXIS_G:
                                    case KEY_THREE_AXIS_TRIGGER_SENSITIVITY:
                                    case KEY_THREE_AXIS_REPORT_INTERVAL:
                                        if (result != 1) {
                                            savedParamsError = true;
                                        }
                                        break;
                                    case KEY_OPTIONAL_PAYLOAD_THREE_AXIS:
                                        if (result != 1) {
                                            savedParamsError = true;
                                        }
                                        if (savedParamsError) {
                                            ToastUtils.showToast(ThreeAxisSensorActivity.this, "Opps！Save failed. Please check the input characters and try again.");
                                        } else {
                                            AlertMessageDialog dialog = new AlertMessageDialog();
                                            dialog.setMessage("Saved Successfully！");
                                            dialog.setConfirm("OK");
                                            dialog.setCancelGone();
                                            dialog.show(getSupportFragmentManager());
                                        }
                                        break;
                                    case KEY_THREE_AXIS_DATA_ENABLE:
                                        if (mIsBack) {
                                            finish();
                                            return;
                                        }
                                        iv3AxisSensorData.setImageResource(mSensorDataOpen ?
                                                R.drawable.loratracker_ic_checked : R.drawable.loratracker_ic_unchecked);
                                        cl3AxisSensorData.setVisibility(mSensorDataOpen ? View.VISIBLE : View.GONE);
                                        break;
                                }
                            }
                            if (flag == 0x00) {
                                // read
                                switch (configKeyEnum) {
                                    case KEY_THREE_AXIS_ENABLE:
                                        if (length > 0) {
                                            int status = value[4] & 0xFF;
                                            cb3AxisSensorSwitch.setEnabled(status == 1);
                                        }
                                        break;
                                    case KEY_THREE_AXIS_SAMPLE_RATE:
                                        if (length > 0) {
                                            mSampleRateSelected = value[4] & 0xFF;
                                            tv3AxisSampleRate.setText(mSampleRateValues.get(mSampleRateSelected));
                                        }
                                        break;
                                    case KEY_THREE_AXIS_G:
                                        if (length > 0) {
                                            mGSelected = value[4] & 0xFF;
                                            tv3AxisG.setText(mGValues.get(mGSelected));
                                        }
                                        break;
                                    case KEY_THREE_AXIS_TRIGGER_SENSITIVITY:
                                        if (length > 0) {
                                            int triggerSensitivity = value[4] & 0xFF;
                                            etTriggerSensitivity.setText(String.valueOf(triggerSensitivity));
                                        }
                                        break;
                                    case KEY_THREE_AXIS_REPORT_INTERVAL:
                                        if (length > 0) {
                                            int reportInterval = value[4] & 0xFF;
                                            etReportInterval.setText(String.valueOf(reportInterval));
                                        }
                                        break;
                                    case KEY_OPTIONAL_PAYLOAD_THREE_AXIS:
                                        if (length > 0) {
                                            int optionalPayload = value[4] & 0xFF;
                                            cb3AxisMac.setChecked((optionalPayload & OPTIONAL_PAYLOAD_MAC_ENABLE)
                                                    == OPTIONAL_PAYLOAD_MAC_ENABLE);
                                            cb3AxisTimestamp.setChecked((optionalPayload & OPTIONAL_PAYLOAD_TIMESTAMP_ENABLE)
                                                    == OPTIONAL_PAYLOAD_TIMESTAMP_ENABLE);
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
        final String triggerSensitivityStr = etTriggerSensitivity.getText().toString();
        if (TextUtils.isEmpty(triggerSensitivityStr))
            return false;
        int triggerSensitivity = Integer.parseInt(triggerSensitivityStr);
        if (triggerSensitivity < 7 || triggerSensitivity > 255)
            return false;
        final String intervalStr = etReportInterval.getText().toString();
        if (TextUtils.isEmpty(intervalStr))
            return false;
        int interval = Integer.parseInt(intervalStr);
        if (interval < 1 || interval > 60)
            return false;
        return true;
    }


    private void saveParams() {
        final String triggerSensitivityStr = etTriggerSensitivity.getText().toString();
        final String intervalStr = etReportInterval.getText().toString();
        int triggerSensitivity = Integer.parseInt(triggerSensitivityStr);
        int interval = Integer.parseInt(intervalStr);
        List<OrderTask> orderTasks = new ArrayList<>();
        orderTasks.add(OrderTaskAssembler.set3AxisEnable(cb3AxisSensorSwitch.isChecked() ? 1 : 0));
        orderTasks.add(OrderTaskAssembler.set3AxisSampleRate(mSampleRateSelected));
        orderTasks.add(OrderTaskAssembler.set3AxisG(mGSelected));
        orderTasks.add(OrderTaskAssembler.set3AxisTriggerSensitivity(triggerSensitivity));
        orderTasks.add(OrderTaskAssembler.set3AxisReportInterval(interval));
        int mac = 0;
        int timestamp = 0;
        if (cb3AxisMac.isChecked())
            mac = OPTIONAL_PAYLOAD_MAC_ENABLE;
        if (cb3AxisTimestamp.isChecked())
            timestamp = OPTIONAL_PAYLOAD_TIMESTAMP_ENABLE;
        orderTasks.add(OrderTaskAssembler.set3AxisOptionalPayload(mac | timestamp));
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
                            ThreeAxisSensorActivity.this.setResult(RESULT_OK);
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
        back();
    }

    @Override
    public void onBackPressed() {
        back();
    }

    private void back() {
        if (mSensorDataOpen) {
            mIsBack = true;
            showSyncingProgressDialog();
            LoRaTrackerMokoSupport.getInstance().sendOrder(OrderTaskAssembler.set3AxisDataEnable(0));
            return;
        }
        finish();
    }

    public void onSelectSampleRate(View view) {
        BottomDialog dialog = new BottomDialog();
        dialog.setDatas(mSampleRateValues, mSampleRateSelected);
        dialog.setListener(value -> {
            mSampleRateSelected = value;
            tv3AxisSampleRate.setText(mSampleRateValues.get(value));
        });
        dialog.show(getSupportFragmentManager());
    }

    public void onSelectG(View view) {
        BottomDialog dialog = new BottomDialog();
        dialog.setDatas(mGValues, mGSelected);
        dialog.setListener(value -> {
            mGSelected = value;
            tv3AxisG.setText(mGValues.get(value));
        });
        dialog.show(getSupportFragmentManager());
    }

    public void onSensorDataSwitch(View view) {
        if (isWindowLocked())
            return;
        mSensorDataOpen = !mSensorDataOpen;
        showSyncingProgressDialog();
        LoRaTrackerMokoSupport.getInstance().sendOrder(OrderTaskAssembler.set3AxisDataEnable(mSensorDataOpen ? 1 : 0));
    }
}
