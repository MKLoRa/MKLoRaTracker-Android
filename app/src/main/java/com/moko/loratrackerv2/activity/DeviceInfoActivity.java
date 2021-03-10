package com.moko.loratrackerv2.activity;


import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.elvishew.xlog.XLog;
import com.moko.ble.lib.MokoConstants;
import com.moko.ble.lib.event.ConnectStatusEvent;
import com.moko.ble.lib.event.OrderTaskResponseEvent;
import com.moko.ble.lib.task.OrderTask;
import com.moko.ble.lib.task.OrderTaskResponse;
import com.moko.ble.lib.utils.MokoUtils;
import com.moko.loratrackerv2.AppConstants;
import com.moko.loratrackerv2.R;
import com.moko.loratrackerv2.R2;
import com.moko.loratrackerv2.dialog.AlertMessageDialog;
import com.moko.loratrackerv2.dialog.LoadingMessageDialog;
import com.moko.loratrackerv2.fragment.DeviceFragment;
import com.moko.loratrackerv2.fragment.LoRaFragment;
import com.moko.loratrackerv2.fragment.ScannerFragment;
import com.moko.loratrackerv2.fragment.SettingFragment;
import com.moko.loratrackerv2.service.DfuService;
import com.moko.loratrackerv2.utils.FileUtils;
import com.moko.loratrackerv2.utils.ToastUtils;
import com.moko.support.loratracker.LoRaTrackerMokoSupport;
import com.moko.support.loratracker.OrderTaskAssembler;
import com.moko.support.loratracker.entity.OrderCHAR;
import com.moko.support.loratracker.entity.ParamsKeyEnum;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import androidx.annotation.IdRes;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import butterknife.BindView;
import butterknife.ButterKnife;
import no.nordicsemi.android.dfu.DfuProgressListener;
import no.nordicsemi.android.dfu.DfuProgressListenerAdapter;
import no.nordicsemi.android.dfu.DfuServiceInitiator;
import no.nordicsemi.android.dfu.DfuServiceListenerHelper;

public class DeviceInfoActivity extends BaseActivity implements RadioGroup.OnCheckedChangeListener {
    public static final int REQUEST_CODE_SELECT_FIRMWARE = 0x10;

    @BindView(R2.id.frame_container)
    FrameLayout frameContainer;
    @BindView(R2.id.radioBtn_lora)
    RadioButton radioBtnLora;
    @BindView(R2.id.radioBtn_scanner)
    RadioButton radioBtnScanner;
    @BindView(R2.id.radioBtn_setting)
    RadioButton radioBtnSetting;
    @BindView(R2.id.radioBtn_device)
    RadioButton radioBtnDevice;
    @BindView(R2.id.rg_options)
    RadioGroup rgOptions;
    @BindView(R2.id.tv_title)
    TextView tvTitle;
    @BindView(R2.id.iv_save)
    ImageView ivSave;
    private FragmentManager fragmentManager;
    private LoRaFragment loraFragment;
    private ScannerFragment scannerFragment;
    private SettingFragment settingFragment;
    private DeviceFragment deviceFragment;
    public String mDeviceMac;
    public String mDeviceName;
    private String[] mUploadMode;
    private String[] mRegions;
    private int mSelectedRegion;
    private boolean mReceiverTag = false;
    private int disConnectType;

    private boolean savedParamsError;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loratracker_activity_device_info);
        ButterKnife.bind(this);
        fragmentManager = getFragmentManager();
        initFragment();
        radioBtnLora.setChecked(true);
        tvTitle.setText(R.string.title_lora);
        rgOptions.setOnCheckedChangeListener(this);
        mUploadMode = getResources().getStringArray(R.array.upload_mode);
        mRegions = getResources().getStringArray(R.array.region);
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
            // sync time after connect success;
            orderTasks.add(OrderTaskAssembler.setTime());
            // get lora params
            orderTasks.add(OrderTaskAssembler.getLoraRegion());
            orderTasks.add(OrderTaskAssembler.getLoraMode());
            orderTasks.add(OrderTaskAssembler.getLoRaConnectable());
            orderTasks.add(OrderTaskAssembler.getTimeSyncInterval());
            LoRaTrackerMokoSupport.getInstance().sendOrder(orderTasks.toArray(new OrderTask[]{}));
        }
    }

    private void initFragment() {
        loraFragment = LoRaFragment.newInstance();
        scannerFragment = ScannerFragment.newInstance();
        settingFragment = SettingFragment.newInstance();
        deviceFragment = DeviceFragment.newInstance();
        fragmentManager.beginTransaction()
                .add(R.id.frame_container, loraFragment)
                .add(R.id.frame_container, scannerFragment)
                .add(R.id.frame_container, settingFragment)
                .add(R.id.frame_container, deviceFragment)
                .show(loraFragment)
                .hide(scannerFragment)
                .hide(settingFragment)
                .hide(deviceFragment)
                .commit();
    }

    @Subscribe(threadMode = ThreadMode.POSTING, priority = 100)
    public void onConnectStatusEvent(ConnectStatusEvent event) {
        EventBus.getDefault().cancelEventDelivery(event);
        final String action = event.getAction();
        runOnUiThread(() -> {
            if (MokoConstants.ACTION_DISCONNECTED.equals(action)) {
                showDisconnectDialog();
            }
            if (MokoConstants.ACTION_DISCOVER_SUCCESS.equals(action)) {
            }
        });

    }

    @Subscribe(threadMode = ThreadMode.POSTING, priority = 100)
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
                    case CHAR_DISCONNECTED_NOTIFY:
                        final int length = value.length;
                        if (length != 5)
                            return;
                        int header = value[0] & 0xFF;
                        int flag = value[1] & 0xFF;
                        int cmd = value[2] & 0xFF;
                        int len = value[3] & 0xFF;
                        int type = value[4] & 0xFF;
                        if (header == 0xED && flag == 0x02 && cmd == 0x02 && len == 0x01) {
                            disConnectType = type;
                            if (type == 1) {
                                // valid password timeout
                            } else if (type == 2) {
                                // change password success
                            } else if (type == 3) {
                                // reset success
                            } else if (type == 4) {
                                // no data exchange timeout
                            } else if (type == 5) {
                                // close device
                            }
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
                    case CHAR_MODEL_NUMBER:
                        String productModel = new String(value);
                        deviceFragment.setProductModel(productModel);
                        break;
                    case CHAR_SOFTWARE_REVISION:
                        String softwareVersion = new String(value);
                        deviceFragment.setSoftwareVersion(softwareVersion);
                        break;
                    case CHAR_FIRMWARE_REVISION:
                        String firmwareVersion = new String(value);
                        deviceFragment.setFirmwareVersion(firmwareVersion);
                        break;
                    case CHAR_HARDWARE_REVISION:
                        String hardwareVersion = new String(value);
                        deviceFragment.setHardwareVersion(hardwareVersion);
                        break;
                    case CHAR_MANUFACTURER_NAME:
                        String manufacture = new String(value);
                        deviceFragment.setManufacture(manufacture);
                        break;
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
                                    case KEY_TIME:
                                        if (result == 1)
                                            ToastUtils.showToast(DeviceInfoActivity.this, "Time sync completed!");
                                    case KEY_FILTER_VALID_INTERVAL:
                                    case KEY_ALARM_NOTIFY:
                                    case KEY_SCAN_WINDOW:
                                    case KEY_WARNING_RSSI:
                                        if (result != 1) {
                                            savedParamsError = true;
                                        }
                                        break;
                                    case KEY_ALARM_RSSI:
                                    case KEY_TIME_SYNC_INTERVAL:
                                    case KEY_CONNECTABLE:
                                    case KEY_LOW_POWER_PERCENT:
                                        if (result != 1) {
                                            savedParamsError = true;
                                        }
                                        if (savedParamsError) {
                                            ToastUtils.showToast(DeviceInfoActivity.this, "Opps！Save failed. Please check the input characters and try again.");
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
                                    case KEY_LORA_REGION:
                                        if (length > 0) {
                                            final int region = value[4] & 0xFF;
                                            mSelectedRegion = region;
                                        }
                                        break;
                                    case KEY_LORA_MODE:
                                        if (length > 0) {
                                            final int mode = value[4];
                                            String loraInfo = String.format("%s/%s/ClassA",
                                                    mUploadMode[mode - 1], mRegions[mSelectedRegion]);
                                            loraFragment.setLoRaInfo(loraInfo);
                                        }
                                        break;
                                    case KEY_NETWORK_STATUS:
                                        if (length > 0) {
                                            int connectable = value[4];
                                            loraFragment.setNetworkCheck(connectable);
                                        }
                                        break;
                                    case KEY_TIME_SYNC_INTERVAL:
                                        if (length > 0) {
                                            int interval = value[4] & 0xFF;
                                            loraFragment.setTimeSyncInterval(interval);
                                        }
                                        break;
                                    case KEY_ADV_NAME:
                                        if (length > 0) {
                                            byte[] rawDataBytes = Arrays.copyOfRange(value, 4, 4 + length);
                                            final String deviceName = new String(rawDataBytes);
                                            mDeviceName = deviceName;
                                            settingFragment.setDeviceName(deviceName);
                                        }
                                        break;
                                    case KEY_FILTER_VALID_INTERVAL:
                                        if (length > 0) {
                                            byte[] rawDataBytes = Arrays.copyOfRange(value, 4, 4 + length);
                                            final int scannInterval = MokoUtils.toInt(rawDataBytes);
                                            scannerFragment.setScanInterval(scannInterval);
                                        }
                                        break;
                                    case KEY_ALARM_NOTIFY:
                                        if (length > 0) {
                                            int notify = value[4] & 0xFF;
                                            scannerFragment.setAlarmNotify(notify);
                                        }
                                        break;
                                    case KEY_ALARM_RSSI:
                                        if (length > 0) {
                                            int rssi = value[4];
                                            scannerFragment.setAlarmTriggerRssi(rssi);
                                        }
                                        break;
                                    case KEY_WARNING_RSSI:
                                        if (length > 0) {
                                            int rssi = value[4];
                                            scannerFragment.setWarningRssi(rssi);
                                        }
                                        break;
                                    case KEY_SCAN_WINDOW:
                                        if (length > 0) {
                                            int scanWindow = value[4] & 0xFF;
                                            scannerFragment.setScanWindow(scanWindow);
                                        }
                                        break;
                                    case KEY_CONNECTABLE:
                                        if (length > 0) {
                                            int connectable = value[4] & 0xFF;
                                            settingFragment.setConnectable(connectable);
                                        }
                                        break;
                                    case KEY_LOW_POWER_PERCENT:
                                        if (length > 0) {
                                            int lowBattery = value[4] & 0xFF;
                                            settingFragment.setLowBattery(lowBattery);
                                        }
                                        break;
                                    case KEY_GPS_FUNCTION_STATUS:
                                        if (length > 0) {
                                            int status = value[4] & 0xFF;
                                            settingFragment.setGpsFunction(status);
                                        }
                                        break;
                                    case KEY_DEVICE_MAC:
                                        if (length > 0) {
                                            byte[] macBytes = Arrays.copyOfRange(value, 4, 4 + length);
                                            StringBuffer stringBuffer = new StringBuffer();
                                            for (int i = 0, l = macBytes.length; i < l; i++) {
                                                stringBuffer.append(MokoUtils.byte2HexString(macBytes[i]));
                                                if (i < (l - 1))
                                                    stringBuffer.append(":");
                                            }
                                            mDeviceMac = stringBuffer.toString();
                                            deviceFragment.setMacAddress(stringBuffer.toString());
                                        }
                                        break;
                                    case KEY_BATTERY:
                                        if (length > 0) {
                                            int battery = value[4] & 0xFF;
                                            deviceFragment.setBatteryValtage(battery);
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

    private void showDisconnectDialog() {
        if (disConnectType == 2) {
            AlertMessageDialog dialog = new AlertMessageDialog();
            dialog.setTitle("Change Password");
            dialog.setMessage("Password changed successfully!Please reconnect the device.");
            dialog.setConfirm("OK");
            dialog.setCancelGone();
            dialog.setOnAlertConfirmListener(() -> {
                setResult(RESULT_OK);
                finish();
            });
            dialog.show(getSupportFragmentManager());
        } else if (disConnectType == 3) {
            AlertMessageDialog dialog = new AlertMessageDialog();
            dialog.setTitle("Factory Reset");
            dialog.setMessage("Factory reset successfully!Please reconnect the device.");
            dialog.setConfirm("OK");
            dialog.setCancelGone();
            dialog.setOnAlertConfirmListener(() -> {
                setResult(RESULT_OK);
                finish();
            });
            dialog.show(getSupportFragmentManager());
        } else if (disConnectType == 4) {
            AlertMessageDialog dialog = new AlertMessageDialog();
            dialog.setMessage("No data communication for 2 minutes, the device is disconnected.");
            dialog.setConfirm("OK");
            dialog.setCancelGone();
            dialog.setOnAlertConfirmListener(() -> {
                setResult(RESULT_OK);
                finish();
            });
            dialog.show(getSupportFragmentManager());
        } else if (disConnectType == 1) {
            AlertMessageDialog dialog = new AlertMessageDialog();
            dialog.setMessage("The device disconnected!");
            dialog.setConfirm("OK");
            dialog.setCancelGone();
            dialog.setOnAlertConfirmListener(() -> {
                setResult(RESULT_OK);
                finish();
            });
            dialog.show(getSupportFragmentManager());
        } else {
            if (LoRaTrackerMokoSupport.getInstance().isBluetoothOpen() && !isUpgrade) {
                AlertMessageDialog dialog = new AlertMessageDialog();
                dialog.setTitle("Dismiss");
                dialog.setMessage("The device disconnected!");
                dialog.setConfirm("Exit");
                dialog.setCancelGone();
                dialog.setOnAlertConfirmListener(() -> {
                    setResult(RESULT_OK);
                    finish();
                });
                dialog.show(getSupportFragmentManager());
            }
        }
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
                            AlertDialog.Builder builder = new AlertDialog.Builder(DeviceInfoActivity.this);
                            builder.setTitle("Dismiss");
                            builder.setCancelable(false);
                            builder.setMessage("The current system of bluetooth is not available!");
                            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    DeviceInfoActivity.this.setResult(RESULT_OK);
                                    finish();
                                }
                            });
                            builder.show();
                            break;

                    }
                }
            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_SELECT_FIRMWARE) {
            if (resultCode == RESULT_OK) {
                //得到uri，后面就是将uri转化成file的过程。
                Uri uri = data.getData();
                String firmwareFilePath = FileUtils.getPath(this, uri);
                if (TextUtils.isEmpty(firmwareFilePath))
                    return;
                final File firmwareFile = new File(firmwareFilePath);
                if (firmwareFile.exists()) {
                    final DfuServiceInitiator starter = new DfuServiceInitiator(mDeviceMac)
                            .setDeviceName(mDeviceName)
                            .setKeepBond(false)
                            .setDisableNotification(true);
                    starter.setZip(null, firmwareFilePath);
                    starter.start(this, DfuService.class);
                    showDFUProgressDialog("Waiting...");
                } else {
                    Toast.makeText(this, "file is not exists!", Toast.LENGTH_SHORT).show();
                }
            }
        } else if (requestCode == AppConstants.REQUEST_CODE_LORA_SETTING) {
            if (resultCode == RESULT_OK) {
                ivSave.postDelayed(() -> {
                    showSyncingProgressDialog();
                    List<OrderTask> orderTasks = new ArrayList<>();
                    // setting
                    orderTasks.add(OrderTaskAssembler.getLoraRegion());
                    orderTasks.add(OrderTaskAssembler.getLoraMode());
                    orderTasks.add(OrderTaskAssembler.getLoRaConnectable());
                    LoRaTrackerMokoSupport.getInstance().sendOrder(orderTasks.toArray(new OrderTask[]{}));
                }, 500);
            }
        } else if (requestCode == AppConstants.REQUEST_CODE_ADV) {
            if (resultCode == RESULT_OK) {
                ivSave.postDelayed(() -> {
                    showSyncingProgressDialog();
                    // setting
                    LoRaTrackerMokoSupport.getInstance().sendOrder(OrderTaskAssembler.getAdvName());
                }, 500);
            }
        }
    }

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

    public void onSave(View view) {
        if (radioBtnLora.isChecked()) {
            if (loraFragment.isValid()) {
                showSyncingProgressDialog();
                loraFragment.saveParams();
            } else {
                ToastUtils.showToast(this, "Opps！Save failed. Please check the input characters and try again.");
            }
        }
        if (radioBtnScanner.isChecked()) {
            showSyncingProgressDialog();
            scannerFragment.saveParams();
        }
    }

    private void back() {
        LoRaTrackerMokoSupport.getInstance().disConnectBle();
//        mIsClose = false;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            back();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
        if (checkedId == R.id.radioBtn_lora) {
            showLoRaAndGetData();
        } else if (checkedId == R.id.radioBtn_scanner) {
            showScannerAndGetData();
        } else if (checkedId == R.id.radioBtn_setting) {
            showSettingAndGetData();
        } else if (checkedId == R.id.radioBtn_device) {
            showDeviceAndGetData();
        }
    }

    private void showDeviceAndGetData() {
        tvTitle.setText(R.string.title_device);
        ivSave.setVisibility(View.GONE);
        fragmentManager.beginTransaction()
                .hide(loraFragment)
                .hide(scannerFragment)
                .hide(settingFragment)
                .show(deviceFragment)
                .commit();
        showSyncingProgressDialog();
        List<OrderTask> orderTasks = new ArrayList<>();
        // device
        orderTasks.add(OrderTaskAssembler.getBattery());
        orderTasks.add(OrderTaskAssembler.getMacAddress());
        orderTasks.add(OrderTaskAssembler.getDeviceModel());
        orderTasks.add(OrderTaskAssembler.getSoftwareVersion());
        orderTasks.add(OrderTaskAssembler.getFirmwareVersion());
        orderTasks.add(OrderTaskAssembler.getHardwareVersion());
        orderTasks.add(OrderTaskAssembler.getManufacturer());
        LoRaTrackerMokoSupport.getInstance().sendOrder(orderTasks.toArray(new OrderTask[]{}));
    }

    private void showSettingAndGetData() {
        tvTitle.setText(R.string.title_setting);
        ivSave.setVisibility(View.GONE);
        fragmentManager.beginTransaction()
                .hide(loraFragment)
                .hide(scannerFragment)
                .show(settingFragment)
                .hide(deviceFragment)
                .commit();
        showSyncingProgressDialog();
        List<OrderTask> orderTasks = new ArrayList<>();
        // setting
        orderTasks.add(OrderTaskAssembler.getAdvName());
        orderTasks.add(OrderTaskAssembler.getConnectable());
        orderTasks.add(OrderTaskAssembler.getLowBattery());
        orderTasks.add(OrderTaskAssembler.getGPSStatus());
        LoRaTrackerMokoSupport.getInstance().sendOrder(orderTasks.toArray(new OrderTask[]{}));
    }

    private void showScannerAndGetData() {
        tvTitle.setText(R.string.title_scanner);
        ivSave.setVisibility(View.VISIBLE);
        fragmentManager.beginTransaction()
                .hide(loraFragment)
                .show(scannerFragment)
                .hide(settingFragment)
                .hide(deviceFragment)
                .commit();
        showSyncingProgressDialog();
        List<OrderTask> orderTasks = new ArrayList<>();
        // scanner
        orderTasks.add(OrderTaskAssembler.getScanInterval());
        orderTasks.add(OrderTaskAssembler.getAlarmNotify());
        orderTasks.add(OrderTaskAssembler.getAlarmRssi());
        orderTasks.add(OrderTaskAssembler.getWarningRssi());
        orderTasks.add(OrderTaskAssembler.getScanWindow());
        LoRaTrackerMokoSupport.getInstance().sendOrder(orderTasks.toArray(new OrderTask[]{}));
    }

    private void showLoRaAndGetData() {
        tvTitle.setText(R.string.title_lora);
        ivSave.setVisibility(View.VISIBLE);
        fragmentManager.beginTransaction()
                .show(loraFragment)
                .hide(scannerFragment)
                .hide(settingFragment)
                .hide(deviceFragment)
                .commit();
        showSyncingProgressDialog();
        List<OrderTask> orderTasks = new ArrayList<>();
        // get lora params
        orderTasks.add(OrderTaskAssembler.getLoraRegion());
        orderTasks.add(OrderTaskAssembler.getLoraMode());
        orderTasks.add(OrderTaskAssembler.getLoRaConnectable());
        orderTasks.add(OrderTaskAssembler.getTimeSyncInterval());
        LoRaTrackerMokoSupport.getInstance().sendOrder(orderTasks.toArray(new OrderTask[]{}));
    }


    public void changePassword(String password) {
        showSyncingProgressDialog();
        LoRaTrackerMokoSupport.getInstance().sendOrder(OrderTaskAssembler.changePassword(password));
    }

    public void reset() {
        showSyncingProgressDialog();
        LoRaTrackerMokoSupport.getInstance().sendOrder(OrderTaskAssembler.setReset());
    }

    public void changeConnectState(int connectState) {
        showSyncingProgressDialog();
        LoRaTrackerMokoSupport.getInstance().sendOrder(OrderTaskAssembler.setConnectionMode(connectState), OrderTaskAssembler.getConnectable());
    }

    public void powerOff() {
        showSyncingProgressDialog();
        LoRaTrackerMokoSupport.getInstance().sendOrder(OrderTaskAssembler.closePower());
    }

    public void setLowBattery(int lowBattery) {
        showSyncingProgressDialog();
        LoRaTrackerMokoSupport.getInstance().sendOrder(OrderTaskAssembler.setLowBattery(lowBattery));
    }

//    public void setDeviceInfoInterval(int interval) {
//        showSyncingProgressDialog();
//        LoRaTrackerMokoSupport.getInstance().sendOrder(OrderTaskAssembler.setDeviceInfoInterval(interval));
//    }

    public void chooseFirmwareFile() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");//设置类型，我这里是任意类型，任意后缀的可以这样写。
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        try {
            startActivityForResult(Intent.createChooser(intent, "select file first!"), REQUEST_CODE_SELECT_FIRMWARE);
        } catch (ActivityNotFoundException ex) {
            ToastUtils.showToast(this, "install file manager app");
        }
    }

    private ProgressDialog mDFUDialog;

    private void showDFUProgressDialog(String tips) {
        mDFUDialog = new ProgressDialog(DeviceInfoActivity.this);
        mDFUDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDFUDialog.setCanceledOnTouchOutside(false);
        mDFUDialog.setCancelable(false);
        mDFUDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mDFUDialog.setMessage(tips);
        if (!isFinishing() && mDFUDialog != null && !mDFUDialog.isShowing()) {
            mDFUDialog.show();
        }
    }

    private void dismissDFUProgressDialog() {
        mDeviceConnectCount = 0;
        if (!isFinishing() && mDFUDialog != null && mDFUDialog.isShowing()) {
            mDFUDialog.dismiss();
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(DeviceInfoActivity.this);
        builder.setTitle("Dismiss");
        builder.setCancelable(false);
        builder.setMessage("The device disconnected!");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                isUpgrade = false;
                DeviceInfoActivity.this.setResult(RESULT_OK);
                finish();
            }
        });
        builder.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        DfuServiceListenerHelper.registerProgressListener(this, mDfuProgressListener);
    }

    @Override
    protected void onPause() {
        super.onPause();
        DfuServiceListenerHelper.unregisterProgressListener(this, mDfuProgressListener);
    }

    private int mDeviceConnectCount;
    private boolean isUpgrade;

    private final DfuProgressListener mDfuProgressListener = new DfuProgressListenerAdapter() {
        @Override
        public void onDeviceConnecting(String deviceAddress) {
            XLog.w("onDeviceConnecting...");
            mDeviceConnectCount++;
            if (mDeviceConnectCount > 3) {
                Toast.makeText(DeviceInfoActivity.this, "Error:DFU Failed", Toast.LENGTH_SHORT).show();
                dismissDFUProgressDialog();
                final LocalBroadcastManager manager = LocalBroadcastManager.getInstance(DeviceInfoActivity.this);
                final Intent abortAction = new Intent(DfuService.BROADCAST_ACTION);
                abortAction.putExtra(DfuService.EXTRA_ACTION, DfuService.ACTION_ABORT);
                manager.sendBroadcast(abortAction);
            }

        }

        @Override
        public void onDeviceDisconnecting(String deviceAddress) {
            XLog.w("onDeviceDisconnecting...");
        }

        @Override
        public void onDfuProcessStarting(String deviceAddress) {
            isUpgrade = true;
            mDFUDialog.setMessage("DfuProcessStarting...");
        }


        @Override
        public void onEnablingDfuMode(String deviceAddress) {
            mDFUDialog.setMessage("EnablingDfuMode...");
        }

        @Override
        public void onFirmwareValidating(String deviceAddress) {
            mDFUDialog.setMessage("FirmwareValidating...");
        }

        @Override
        public void onDfuCompleted(String deviceAddress) {
            mDeviceConnectCount = 0;
            if (!isFinishing() && mDFUDialog != null && mDFUDialog.isShowing()) {
                mDFUDialog.dismiss();
            }
            AlertDialog.Builder builder = new AlertDialog.Builder(DeviceInfoActivity.this);
            builder.setTitle("Update Firmware");
            builder.setCancelable(false);
            builder.setMessage("Update firmware successfully!\nPlease reconnect the device.");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    isUpgrade = false;
                    DeviceInfoActivity.this.setResult(RESULT_OK);
                    finish();
                }
            });
            builder.show();
        }

        @Override
        public void onDfuAborted(String deviceAddress) {
            mDFUDialog.setMessage("DfuAborted...");
        }

        @Override
        public void onProgressChanged(String deviceAddress, int percent, float speed, float avgSpeed, int currentPart, int partsTotal) {
            mDFUDialog.setMessage("Progress:" + percent + "%");
        }

        @Override
        public void onError(String deviceAddress, int error, int errorType, String message) {
            ToastUtils.showToast(DeviceInfoActivity.this, "Opps!DFU Failed. Please try again!");
            XLog.i("Error:" + message);
            dismissDFUProgressDialog();
        }
    };

    public void onChangePassword(View view) {
        settingFragment.showChangePasswordDialog();
    }

    public void onFactoryReset(View view) {
        settingFragment.showResetDialog();
    }

    public void onUpdateFirmware(View view) {
        chooseFirmwareFile();
    }

    public void onScanWindow(View view) {
        scannerFragment.showBeaconScannerDialog();
    }

    public void onConnectable(View view) {
        settingFragment.showConnectableDialog();
    }

    public void onPowerOff(View view) {
        settingFragment.showPowerOffDialog();
    }

    public void onLowBatteryValue(View view) {
        settingFragment.showLowBatteryDialog();
    }

    public void onLBFilterOptions(View view) {
        startActivity(new Intent(this, FilterLBOptionsActivity.class));
    }

    public void onCTFilterOptions(View view) {
        startActivity(new Intent(this, FilterCTOptionsActivity.class));
    }

    public void onWarningValue(View view) {
        scannerFragment.showWarningRssiDialog();
    }

    public void onAdvInfo(View view) {
        Intent intent = new Intent(this, AdvInfoActivity.class);
        startActivityForResult(intent, AppConstants.REQUEST_CODE_ADV);
    }

    public void onVibrationSetting(View view) {
        startActivity(new Intent(this, VibrationSettingActivity.class));
    }

    public void onSOSFunction(View view) {
        startActivity(new Intent(this, SOSFucntionActivity.class));
    }

    public void onGPSFunction(View view) {
        startActivity(new Intent(this, GPSFucntionActivity.class));
    }

    public void on3AxisSensor(View view) {
        startActivity(new Intent(this, ThreeAxisSensorActivity.class));
    }

    public void onAlarmNotify(View view) {
        scannerFragment.showAlarmNotifyDialog();
    }

    public void onLoraSetting(View view) {
        // LORA
        Intent intent = new Intent(this, LoRaSettingActivity.class);
        startActivityForResult(intent, AppConstants.REQUEST_CODE_LORA_SETTING);
    }

    public void onNetworkCheck(View view) {
        startActivity(new Intent(this, NetworkCheckActivity.class));
    }

    public void onUplinkPayload(View view) {
        startActivity(new Intent(this, UplinkPayloadActivity.class));
    }
}
