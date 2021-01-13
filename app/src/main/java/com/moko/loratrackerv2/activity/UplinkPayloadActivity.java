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

public class UplinkPayloadActivity extends BaseActivity {

    // Location
    private static final int TRACKING_OPTIONAL_PAYLOAD_BATTERY_ENABLE = 1;
    private static final int TRACKING_OPTIONAL_PAYLOAD_HOST_MAC_ENABLE = 2;
    private static final int TRACKING_OPTIONAL_PAYLOAD_RAW_ENABLE = 4;
    // SOS
    private static final int SOS_OPTIONAL_PAYLOAD_TIMESTAMP_ENABLE = 1;
    private static final int SOS_OPTIONAL_PAYLOAD_HOST_MAC_ENABLE = 2;
    // GPS
    private static final int GPS_OPTIONAL_PAYLOAD_ALTITUDE_ENABLE = 1;
    private static final int GPS_OPTIONAL_PAYLOAD_TIMESTAMP_ENABLE = 2;
    private static final int GPS_OPTIONAL_PAYLOAD_SEARCH_MODE_ENABLE = 4;
    private static final int GPS_OPTIONAL_PAYLOAD_PDOP_ENABLE = 8;
    private static final int GPS_OPTIONAL_PAYLOAD_SATELLITES_NUMBER_ENABLE = 16;
    // 3-AXIS
    private static final int AXIS_OPTIONAL_PAYLOAD_HOST_MAC_ENABLE = 1;
    private static final int AXIS_OPTIONAL_PAYLOAD_TIMESTAMP_ENABLE = 2;

    @BindView(R2.id.et_device_info_report_interval)
    EditText etDeviceInfoReportInterval;
    @BindView(R2.id.cb_report_location_info)
    CheckBox cbReportLocationInfo;
    @BindView(R2.id.tv_reported_location_beacons)
    TextView tvReportedLocationBeacons;
    @BindView(R2.id.et_non_alarm_report_interval)
    EditText etNonAlarmReportInterval;
    @BindView(R.id.cb_tracking_mac)
    CheckBox cbTrackingMac;
    @BindView(R.id.cb_tracking_raw_data)
    CheckBox cbTrackingRawData;
    @BindView(R.id.cb_tracking_battery)
    CheckBox cbTrackingBattery;
    @BindView(R2.id.et_sos_report_interval)
    EditText etSosReportInterval;
    @BindView(R2.id.cb_sos_timestamp)
    CheckBox cbSosTimestamp;
    @BindView(R2.id.cb_sos_mac)
    CheckBox cbSosMac;
    @BindView(R2.id.tv_gps_report_interval)
    TextView tvGpsReportInterval;
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
    @BindView(R2.id.et_axis_report_interval)
    EditText etAxisReportInterval;
    @BindView(R2.id.cb_3_axis_mac)
    CheckBox cb3AxisMac;
    @BindView(R2.id.cb_3_axis_timestamp)
    CheckBox cb3AxisTimestamp;
    private boolean mReceiverTag = false;
    private boolean savedParamsError;

    private ArrayList<String> mReportBeaconsValues;
    private int mReportBeaconsSelected;
    private ArrayList<String> mGPSValues;
    private int mGPSSelected;
    private int mLoraReportInterval;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loratracker_activity_uplink_payload);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        mReportBeaconsValues = new ArrayList<>();
        mReportBeaconsValues.add("1");
        mReportBeaconsValues.add("2");
        mReportBeaconsValues.add("3");
        mReportBeaconsValues.add("4");
        mGPSValues = new ArrayList<>();
        for (int i = 1; i <= 20; i++) {
            mGPSValues.add(String.valueOf(i * 10));
        }
        // 注册广播接收器
        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        registerReceiver(mReceiver, filter);
        mReceiverTag = true;
        showSyncingProgressDialog();
        List<OrderTask> orderTasks = new ArrayList<>();
        orderTasks.add(OrderTaskAssembler.getDeviceInfoInterval());
        orderTasks.add(OrderTaskAssembler.getReportLocationEnable());
        orderTasks.add(OrderTaskAssembler.getReportLocationBeacons());
        orderTasks.add(OrderTaskAssembler.getLoraReportInterval());
        orderTasks.add(OrderTaskAssembler.getTrackingOptionalPayload());
        orderTasks.add(OrderTaskAssembler.getSOSReportInterval());
        orderTasks.add(OrderTaskAssembler.getSOSOptionalPayload());
        orderTasks.add(OrderTaskAssembler.getGPSReportInterval());
        orderTasks.add(OrderTaskAssembler.getGPSOptionalPayload());
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
                                    case KEY_DEVICE_INFO_INTERVAL:
                                    case KEY_REPORT_LOCATION_ENABLE:
                                    case KEY_REPORT_LOCATION_BEACONS:
                                    case KEY_LORA_REPORT_INTERVAL:
                                    case KEY_OPTIONAL_PAYLOAD_TRACKING:
                                    case KEY_SOS_REPORT_INTERVAL:
                                    case KEY_OPTIONAL_PAYLOAD_SOS:
                                    case KEY_GPS_REPORT_INTERVAL:
                                    case KEY_OPTIONAL_PAYLOAD_GPS:
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
                                            ToastUtils.showToast(UplinkPayloadActivity.this, "Opps！Save failed. Please check the input characters and try again.");
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
                                    case KEY_DEVICE_INFO_INTERVAL:
                                        if (length > 0) {
                                            int interval = value[4] & 0xFF;
                                            etDeviceInfoReportInterval.setText(String.valueOf(interval));
                                        }
                                        break;
                                    case KEY_REPORT_LOCATION_ENABLE:
                                        if (length > 0) {
                                            int enable = value[4] & 0xFF;
                                            cbReportLocationInfo.setChecked(enable == 1);
                                        }
                                        break;
                                    case KEY_REPORT_LOCATION_BEACONS:
                                        if (length > 0) {
                                            int num = value[4] & 0xFF;
                                            mReportBeaconsSelected = num - 1;
                                        }
                                        break;
                                    case KEY_LORA_REPORT_INTERVAL:
                                        if (length > 0) {
                                            int interval = value[4] & 0xFF;
                                            mLoraReportInterval = interval;
                                            etNonAlarmReportInterval.setText(String.valueOf(interval));
                                        }
                                        break;
                                    case KEY_OPTIONAL_PAYLOAD_TRACKING:
                                        if (length > 0) {
                                            int optionalPayload = value[4] & 0xFF;
                                            cbTrackingBattery.setChecked((optionalPayload & TRACKING_OPTIONAL_PAYLOAD_BATTERY_ENABLE)
                                                    == TRACKING_OPTIONAL_PAYLOAD_BATTERY_ENABLE);
                                            cbTrackingMac.setChecked((optionalPayload & TRACKING_OPTIONAL_PAYLOAD_HOST_MAC_ENABLE)
                                                    == TRACKING_OPTIONAL_PAYLOAD_HOST_MAC_ENABLE);
                                            cbTrackingRawData.setChecked((optionalPayload & TRACKING_OPTIONAL_PAYLOAD_RAW_ENABLE)
                                                    == TRACKING_OPTIONAL_PAYLOAD_RAW_ENABLE);

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
                                            cbSosTimestamp.setChecked((optionalPayload & SOS_OPTIONAL_PAYLOAD_TIMESTAMP_ENABLE)
                                                    == SOS_OPTIONAL_PAYLOAD_TIMESTAMP_ENABLE);
                                            cbSosMac.setChecked((optionalPayload & SOS_OPTIONAL_PAYLOAD_HOST_MAC_ENABLE)
                                                    == SOS_OPTIONAL_PAYLOAD_HOST_MAC_ENABLE);
                                        }
                                        break;
                                    case KEY_GPS_REPORT_INTERVAL:
                                        if (length > 0) {
                                            int gpsReportInterval = value[4] & 0xFF;
                                            tvGpsReportInterval.setText(gpsReportInterval * mLoraReportInterval);
                                            mGPSSelected = gpsReportInterval / 10 - 1;
                                        }
                                        break;
                                    case KEY_OPTIONAL_PAYLOAD_GPS:
                                        if (length > 0) {
                                            int optionalPayload = value[4] & 0xFF;
                                            cbGpsAltitude.setChecked((optionalPayload & GPS_OPTIONAL_PAYLOAD_ALTITUDE_ENABLE)
                                                    == GPS_OPTIONAL_PAYLOAD_ALTITUDE_ENABLE);
                                            cbGpsTimestamp.setChecked((optionalPayload & GPS_OPTIONAL_PAYLOAD_TIMESTAMP_ENABLE)
                                                    == GPS_OPTIONAL_PAYLOAD_TIMESTAMP_ENABLE);
                                            cbGpsSearchMode.setChecked((optionalPayload & GPS_OPTIONAL_PAYLOAD_SEARCH_MODE_ENABLE)
                                                    == GPS_OPTIONAL_PAYLOAD_SEARCH_MODE_ENABLE);
                                            cbGpsPdop.setChecked((optionalPayload & GPS_OPTIONAL_PAYLOAD_PDOP_ENABLE)
                                                    == GPS_OPTIONAL_PAYLOAD_PDOP_ENABLE);
                                            cbGpsSatellitesNumber.setChecked((optionalPayload & GPS_OPTIONAL_PAYLOAD_SATELLITES_NUMBER_ENABLE)
                                                    == GPS_OPTIONAL_PAYLOAD_SATELLITES_NUMBER_ENABLE);
                                        }
                                        break;
                                    case KEY_THREE_AXIS_REPORT_INTERVAL:
                                        if (length > 0) {
                                            int reportInterval = value[4] & 0xFF;
                                            etAxisReportInterval.setText(String.valueOf(reportInterval));
                                        }
                                        break;
                                    case KEY_OPTIONAL_PAYLOAD_THREE_AXIS:
                                        if (length > 0) {
                                            int optionalPayload = value[4] & 0xFF;
                                            cb3AxisMac.setChecked((optionalPayload & AXIS_OPTIONAL_PAYLOAD_HOST_MAC_ENABLE)
                                                    == AXIS_OPTIONAL_PAYLOAD_HOST_MAC_ENABLE);
                                            cb3AxisTimestamp.setChecked((optionalPayload & AXIS_OPTIONAL_PAYLOAD_TIMESTAMP_ENABLE)
                                                    == AXIS_OPTIONAL_PAYLOAD_TIMESTAMP_ENABLE);
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
        final String deviceIntervalStr = etDeviceInfoReportInterval.getText().toString();
        if (TextUtils.isEmpty(deviceIntervalStr))
            return false;
        final int deviceInterval = Integer.parseInt(deviceIntervalStr);
        if (deviceInterval < 2 || deviceInterval > 120)
            return false;
        final String loraIntervalStr = etNonAlarmReportInterval.getText().toString();
        if (TextUtils.isEmpty(loraIntervalStr))
            return false;
        final int loraInterval = Integer.parseInt(loraIntervalStr);
        if (loraInterval < 1 || loraInterval > 60)
            return false;
        final String sosIntervalStr = etSosReportInterval.getText().toString();
        if (TextUtils.isEmpty(sosIntervalStr))
            return false;
        final int sosInterval = Integer.parseInt(sosIntervalStr);
        if (sosInterval < 1 || sosInterval > 10)
            return false;
        final String axisIntervalStr = etAxisReportInterval.getText().toString();
        if (TextUtils.isEmpty(axisIntervalStr))
            return false;
        int axisInterval = Integer.parseInt(axisIntervalStr);
        if (axisInterval < 1 || axisInterval > 60)
            return false;
        return true;
    }


    private void saveParams() {
        final String deviceIntervalStr = etDeviceInfoReportInterval.getText().toString();
        final String loraIntervalStr = etNonAlarmReportInterval.getText().toString();
        final String sosIntervalStr = etSosReportInterval.getText().toString();
        final String axisIntervalStr = etAxisReportInterval.getText().toString();
        final int sosInterval = Integer.parseInt(sosIntervalStr);
        final int deviceInterval = Integer.parseInt(deviceIntervalStr);
        final int loraInterval = Integer.parseInt(loraIntervalStr);
        final int axisInterval = Integer.parseInt(axisIntervalStr);
        List<OrderTask> orderTasks = new ArrayList<>();
        // device info
        orderTasks.add(OrderTaskAssembler.setDeviceInfoInterval(deviceInterval));
        // location and tracking
        orderTasks.add(OrderTaskAssembler.setReportLocationEnable(cbReportLocationInfo.isChecked() ? 1 : 0));
        orderTasks.add(OrderTaskAssembler.setReportLocationBeacons(mReportBeaconsSelected + 1));
        orderTasks.add(OrderTaskAssembler.setLoraUploadInterval(loraInterval));
        int trackingBattery = 0;
        int trackingHostMac = 0;
        int trackingRawData = 0;
        if (cbTrackingBattery.isChecked())
            trackingBattery = TRACKING_OPTIONAL_PAYLOAD_BATTERY_ENABLE;
        if (cbTrackingMac.isChecked())
            trackingHostMac = TRACKING_OPTIONAL_PAYLOAD_HOST_MAC_ENABLE;
        if (cbTrackingRawData.isChecked())
            trackingRawData = TRACKING_OPTIONAL_PAYLOAD_RAW_ENABLE;
        orderTasks.add(OrderTaskAssembler.setTrackingOptionPayload(trackingBattery | trackingHostMac | trackingRawData));
        // sos
        orderTasks.add(OrderTaskAssembler.setSOSReportInterval(sosInterval));
        int sosTimestamp = 0;
        int sosHostMac = 0;
        if (cbSosTimestamp.isChecked())
            sosTimestamp = SOS_OPTIONAL_PAYLOAD_TIMESTAMP_ENABLE;
        if (cbSosMac.isChecked())
            sosHostMac = SOS_OPTIONAL_PAYLOAD_HOST_MAC_ENABLE;
        orderTasks.add(OrderTaskAssembler.setSOSOptionalPayload(sosTimestamp | sosHostMac));
        // gps
        final int gpsInterval = (mGPSSelected + 1) * 10;
        orderTasks.add(OrderTaskAssembler.setGPSReportInterval(gpsInterval));
        int gpsAltitude = 0;
        int gpsTimestamp = 0;
        int gpsSearchMode = 0;
        int gpsPdop = 0;
        int gpsNumberOfSatellites = 0;
        if (cbGpsAltitude.isChecked())
            gpsAltitude = GPS_OPTIONAL_PAYLOAD_ALTITUDE_ENABLE;
        if (cbGpsTimestamp.isChecked())
            gpsTimestamp = GPS_OPTIONAL_PAYLOAD_TIMESTAMP_ENABLE;
        if (cbGpsSearchMode.isChecked())
            gpsSearchMode = GPS_OPTIONAL_PAYLOAD_SEARCH_MODE_ENABLE;
        if (cbGpsPdop.isChecked())
            gpsPdop = GPS_OPTIONAL_PAYLOAD_PDOP_ENABLE;
        if (cbGpsSatellitesNumber.isChecked())
            gpsNumberOfSatellites = GPS_OPTIONAL_PAYLOAD_SATELLITES_NUMBER_ENABLE;
        orderTasks.add(OrderTaskAssembler.setGPSOptionalPayload(
                gpsAltitude | gpsTimestamp | gpsSearchMode | gpsPdop | gpsNumberOfSatellites));
        // 3-Axis
        orderTasks.add(OrderTaskAssembler.set3AxisReportInterval(axisInterval));
        int mac = 0;
        int timestamp = 0;
        if (cb3AxisMac.isChecked())
            mac = AXIS_OPTIONAL_PAYLOAD_HOST_MAC_ENABLE;
        if (cb3AxisTimestamp.isChecked())
            timestamp = AXIS_OPTIONAL_PAYLOAD_TIMESTAMP_ENABLE;
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
                            UplinkPayloadActivity.this.setResult(RESULT_OK);
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

    public void onReportedLocationBeacons(View view) {
        BottomDialog dialog = new BottomDialog();
        dialog.setDatas(mReportBeaconsValues, mReportBeaconsSelected);
        dialog.setListener(value -> {
            mReportBeaconsSelected = value;
            tvReportedLocationBeacons.setText(mReportBeaconsValues.get(value));
        });
        dialog.show(getSupportFragmentManager());
    }

    public void onSelectGPSReportInterval(View view) {
        BottomDialog dialog = new BottomDialog();
        dialog.setDatas(mGPSValues, mGPSSelected);
        dialog.setListener(value -> {
            mGPSSelected = value;
            int gpsReportInterval = (mGPSSelected + 1) * 10 * mLoraReportInterval;
            tvGpsReportInterval.setText(String.valueOf(gpsReportInterval));
        });
        dialog.show(getSupportFragmentManager());
    }
}
