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
import android.widget.TextView;

import com.moko.ble.lib.MokoConstants;
import com.moko.ble.lib.event.ConnectStatusEvent;
import com.moko.ble.lib.event.OrderTaskResponseEvent;
import com.moko.ble.lib.task.OrderTask;
import com.moko.ble.lib.task.OrderTaskResponse;
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

import butterknife.BindView;
import butterknife.ButterKnife;

public class GPSFucntionActivity extends BaseActivity {

    private static final int OPTIONAL_PAYLOAD_ALTITUDE_ENABLE = 1;
    private static final int OPTIONAL_PAYLOAD_TIMESTAMP_ENABLE = 2;
    private static final int OPTIONAL_PAYLOAD_SEARCH_MODE_ENABLE = 4;
    private static final int OPTIONAL_PAYLOAD_PDOP_ENABLE = 8;
    private static final int OPTIONAL_PAYLOAD_SATELLITES_NUMBER_ENABLE = 16;
    @BindView(R2.id.cb_gps_switch)
    CheckBox cbGpsSwitch;
    @BindView(R2.id.tv_gps_report_interval)
    TextView tvGpsReportInterval;
    @BindView(R2.id.et_search_time)
    EditText etSearchTime;
    @BindView(R2.id.cb_gps_altitude)
    CheckBox cbGpsAltitude;
    @BindView(R2.id.cb_gps_timestamp)
    CheckBox cbGpsTimestamp;
    @BindView(R2.id.cb_gps_pdop)
    CheckBox cbGpsPdop;
    @BindView(R2.id.cb_gps_satellites_number)
    CheckBox cbGpsSatellitesNumber;
    @BindView(R2.id.cb_gps_search_mode)
    CheckBox cbGpsSearchMode;


    private boolean mReceiverTag = false;
    private boolean savedParamsError;

    private ArrayList<String> mValues;
    private int mSelected;
    private int mLoraReportInterval;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loratracker_activity_gps_function);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        mValues = new ArrayList<>();
        for (int i = 1; i <= 20; i++) {
            mValues.add(String.valueOf(i * 10));
        }
        // 注册广播接收器
        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        registerReceiver(mReceiver, filter);
        mReceiverTag = true;
        showSyncingProgressDialog();
        List<OrderTask> orderTasks = new ArrayList<>();
        orderTasks.add(OrderTaskAssembler.getGPSStatus());
        orderTasks.add(OrderTaskAssembler.getGPSEnable());
        orderTasks.add(OrderTaskAssembler.getLoraReportInterval());
        orderTasks.add(OrderTaskAssembler.getGPSReportInterval());
        orderTasks.add(OrderTaskAssembler.getGPSOptionalPayload());
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
                                    case KEY_GPS_FUNCTION_SWITCH:
                                    case KEY_GPS_REPORT_INTERVAL:
                                    case KEY_GPS_SATELLITE_SEARCH_TIME:
                                        if (result != 1) {
                                            savedParamsError = true;
                                        }
                                        break;
                                    case KEY_OPTIONAL_PAYLOAD_GPS:
                                        if (result != 1) {
                                            savedParamsError = true;
                                        }
                                        if (savedParamsError) {
                                            ToastUtils.showToast(GPSFucntionActivity.this, "Opps！Save failed. Please check the input characters and try again.");
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
                                    case KEY_GPS_FUNCTION_STATUS:
                                        if (length > 0) {
                                            int status = value[4] & 0xFF;
                                            cbGpsSwitch.setEnabled(status == 1);
                                        }
                                        break;
                                    case KEY_GPS_FUNCTION_SWITCH:
                                        if (length > 0) {
                                            int enable = value[4] & 0xFF;
                                            cbGpsSwitch.setChecked(enable == 1);
                                        }
                                        break;
                                    case KEY_LORA_REPORT_INTERVAL:
                                        if (length > 0) {
                                            mLoraReportInterval = value[4] & 0xFF;
                                        }
                                        break;
                                    case KEY_GPS_REPORT_INTERVAL:
                                        if (length > 0) {
                                            int gpsReportInterval = value[4] & 0xFF;
                                            tvGpsReportInterval.setText(gpsReportInterval * mLoraReportInterval);
                                            mSelected = gpsReportInterval / 10 - 1;
                                        }
                                        break;
                                    case KEY_GPS_SATELLITE_SEARCH_TIME:
                                        if (length > 0) {
                                            int searchTime = value[4] & 0xFF;
                                            etSearchTime.setText(String.valueOf(searchTime));
                                        }
                                        break;
                                    case KEY_OPTIONAL_PAYLOAD_GPS:
                                        if (length > 0) {
                                            int optionalPayload = value[4] & 0xFF;
                                            cbGpsAltitude.setChecked((optionalPayload & OPTIONAL_PAYLOAD_ALTITUDE_ENABLE)
                                                    == OPTIONAL_PAYLOAD_ALTITUDE_ENABLE);
                                            cbGpsTimestamp.setChecked((optionalPayload & OPTIONAL_PAYLOAD_TIMESTAMP_ENABLE)
                                                    == OPTIONAL_PAYLOAD_TIMESTAMP_ENABLE);
                                            cbGpsSearchMode.setChecked((optionalPayload & OPTIONAL_PAYLOAD_SEARCH_MODE_ENABLE)
                                                    == OPTIONAL_PAYLOAD_SEARCH_MODE_ENABLE);
                                            cbGpsPdop.setChecked((optionalPayload & OPTIONAL_PAYLOAD_PDOP_ENABLE)
                                                    == OPTIONAL_PAYLOAD_PDOP_ENABLE);
                                            cbGpsSatellitesNumber.setChecked((optionalPayload & OPTIONAL_PAYLOAD_SATELLITES_NUMBER_ENABLE)
                                                    == OPTIONAL_PAYLOAD_SATELLITES_NUMBER_ENABLE);
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
        final String intervalStr = etSearchTime.getText().toString();
        if (TextUtils.isEmpty(intervalStr))
            return false;
        int interval = Integer.parseInt(intervalStr);
        if (interval < 1 || interval > 10)
            return false;
        return true;
    }


    private void saveParams() {
        final String searchTimeStr = etSearchTime.getText().toString();
        int searchTime = Integer.parseInt(searchTimeStr);
        List<OrderTask> orderTasks = new ArrayList<>();
        final int reportInterval = (mSelected + 1) * 10;
        orderTasks.add(OrderTaskAssembler.setGPSEnable(cbGpsSwitch.isChecked() ? 1 : 0));
        orderTasks.add(OrderTaskAssembler.setGPSReportInterval(reportInterval));
        orderTasks.add(OrderTaskAssembler.setGPSSearchTime(searchTime));
        int altitude = 0;
        int timestamp = 0;
        int searchMode = 0;
        int pdop = 0;
        int numberOfSatellites = 0;
        if (cbGpsAltitude.isChecked())
            altitude = OPTIONAL_PAYLOAD_ALTITUDE_ENABLE;
        if (cbGpsTimestamp.isChecked())
            timestamp = OPTIONAL_PAYLOAD_TIMESTAMP_ENABLE;
        if (cbGpsSearchMode.isChecked())
            searchMode = OPTIONAL_PAYLOAD_SEARCH_MODE_ENABLE;
        if (cbGpsPdop.isChecked())
            pdop = OPTIONAL_PAYLOAD_PDOP_ENABLE;
        if (cbGpsSatellitesNumber.isChecked())
            numberOfSatellites = OPTIONAL_PAYLOAD_SATELLITES_NUMBER_ENABLE;
        orderTasks.add(OrderTaskAssembler.setGPSOptionalPayload(
                altitude | timestamp | searchMode | pdop | numberOfSatellites));
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
                            GPSFucntionActivity.this.setResult(RESULT_OK);
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

    public void onSelectGPSReportInterval(View view) {
        BottomDialog dialog = new BottomDialog();
        dialog.setDatas(mValues, mSelected);
        dialog.setListener(value -> {
            mSelected = value;
            int gpsReportInterval = (mSelected + 1) * 10 * mLoraReportInterval;
            tvGpsReportInterval.setText(String.valueOf(gpsReportInterval));
        });
        dialog.show(getSupportFragmentManager());
    }
}
