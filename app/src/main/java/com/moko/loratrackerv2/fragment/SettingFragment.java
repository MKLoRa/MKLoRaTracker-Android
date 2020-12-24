package com.moko.loratrackerv2.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.moko.loratrackerv2.AppConstants;
import com.moko.loratrackerv2.R;
import com.moko.loratrackerv2.activity.DeviceInfoActivity;
import com.moko.loratrackerv2.activity.LoRaSettingActivity;
import com.moko.loratrackerv2.dialog.AlertMessageDialog;
import com.moko.loratrackerv2.dialog.BottomDialog;
import com.moko.loratrackerv2.dialog.ChangePasswordDialog;
import com.moko.loratrackerv2.dialog.ScanWindowDialog;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SettingFragment extends Fragment {
    private static final String TAG = SettingFragment.class.getSimpleName();
    @Bind(R.id.tv_change_password)
    TextView tvChangePassword;
    @Bind(R.id.tv_factory_reset)
    TextView tvFactoryReset;
    @Bind(R.id.tv_update_firmware)
    TextView tvUpdateFirmware;
    @Bind(R.id.tv_lora_setting)
    TextView tvLoraSetting;
    @Bind(R.id.tv_scan_window)
    TextView tvScanWindow;
    @Bind(R.id.iv_connectable)
    ImageView ivConnectable;
    @Bind(R.id.iv_power_off)
    ImageView ivPowerOff;
    @Bind(R.id.tv_lora_connectable)
    TextView tvLoRaConnectable;
    @Bind(R.id.tv_low_battery_value)
    TextView tvLowBatteryValue;
    @Bind(R.id.tv_low_battery_tips)
    TextView tvLowBatteryTips;
    @Bind(R.id.tv_device_info_interval_value)
    TextView tvDeviceInfoIntervalValue;
    private String[] loraConnectable;
    private DeviceInfoActivity activity;

    public SettingFragment() {
    }


    public static SettingFragment newInstance() {
        SettingFragment fragment = new SettingFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "onCreate: ");
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.i(TAG, "onCreateView: ");
        View view = inflater.inflate(R.layout.fragment_setting, container, false);
        ButterKnife.bind(this, view);
        activity = (DeviceInfoActivity) getActivity();
        loraConnectable = getResources().getStringArray(R.array.lora_connectable);
        createLowBatteryList();
        return view;
    }


    @Override
    public void onPause() {
        Log.i(TAG, "onPause: ");
        super.onPause();
    }

    @Override
    public void onDestroyView() {
        Log.i(TAG, "onDestroyView: ");
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void onDestroy() {
        Log.i(TAG, "onDestroy: ");
        super.onDestroy();
    }

    @OnClick({R.id.tv_change_password, R.id.tv_factory_reset, R.id.tv_update_firmware,
            R.id.tv_lora_setting, R.id.tv_scan_window, R.id.iv_connectable,
            R.id.iv_power_off, R.id.tv_low_battery_value, R.id.tv_device_info_interval_value})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_change_password:
                showChangePasswordDialog();
                break;
            case R.id.tv_factory_reset:
                showResetDialog();
                break;
            case R.id.tv_update_firmware:
                activity.chooseFirmwareFile();
                break;
            case R.id.tv_lora_setting:
                // LORA
                Intent intent = new Intent(getActivity(), LoRaSettingActivity.class);
                activity.startActivityForResult(intent, AppConstants.REQUEST_CODE_LORA_SETTING);
                break;
            case R.id.tv_scan_window:
                showBeaconScannerDialog();
                break;
            case R.id.iv_connectable:
                showConnectableDialog();
                break;
            case R.id.iv_power_off:
                showPowerOffDialog();
                break;
            case R.id.tv_low_battery_value:
                showLowBatteryDialog();
                break;
            case R.id.tv_device_info_interval_value:
                showIntervalDialog();
                break;
        }
    }


    private void showResetDialog() {
        AlertMessageDialog dialog = new AlertMessageDialog();
        dialog.setTitle("Factory Reset!");
        dialog.setMessage("After factory reset,all the data will be reseted to the factory values.");
        dialog.setConfirm("OK");
        dialog.setOnAlertConfirmListener(() -> {
            activity.reset();
        });
        dialog.show(activity.getSupportFragmentManager());
    }

    private void showChangePasswordDialog() {
        final ChangePasswordDialog dialog = new ChangePasswordDialog(getActivity());
        dialog.setOnPasswordClicked(password -> activity.changePassword(password));
        dialog.show();
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override

            public void run() {
                activity.runOnUiThread(() -> dialog.showKeyboard());
            }
        }, 200);
    }

    private void showBeaconScannerDialog() {
        final ScanWindowDialog dialog = new ScanWindowDialog(getActivity());
        dialog.setData(scannerState ? startTime : 0);
        dialog.setOnScanWindowClicked(scanMode -> {
            String scanModeStr = "";
            switch (scanMode) {
                case 4:
                    scanModeStr = "0";
                    break;
                case 0:
                    scanModeStr = "1/2";
                    break;
                case 1:
                    scanModeStr = "1/4";
                    break;
                case 2:
                    scanModeStr = "1/8";
                    break;
            }
            tvScanWindow.setText(String.format("Scan Window(%s)", scanModeStr));
            if (scanMode < 3) {
                scanMode += 2;
                activity.setScanWindow(1, scanMode);
            } else {
                activity.setScanWindow(0, 1);
            }
        });
        dialog.show();
    }

    private void showConnectableDialog() {
        AlertMessageDialog dialog = new AlertMessageDialog();
        dialog.setTitle("Warning!");
        if (connectState) {
            dialog.setMessage("Are you sure to make the device Unconnectable？");
        } else {
            dialog.setMessage("Are you sure to make the device connectable？");
        }
        dialog.setConfirm("OK");
        dialog.setOnAlertConfirmListener(() -> {
            int value = !connectState ? 1 : 0;
            activity.changeConnectState(value);
        });
        dialog.show(activity.getSupportFragmentManager());
    }

    private void showPowerOffDialog() {
        AlertMessageDialog dialog = new AlertMessageDialog();
        dialog.setTitle("Warning!");
        dialog.setMessage("Are you sure to turn off the device? Please make sure the device has a button to turn on!");
        dialog.setConfirm("OK");
        dialog.setOnAlertConfirmListener(() -> {
            activity.powerOff();
        });
        dialog.show(activity.getSupportFragmentManager());
    }

    private void showLowBatteryDialog() {
        BottomDialog dialog = new BottomDialog();
        dialog.setDatas(lowBatteryList, lowBatteryListIndex);
        dialog.setListener(value -> {
            lowBatteryListIndex = value;
            lowBatteryValue = (value + 1) * 10;
            tvLowBatteryValue.setText(String.format("%d%%", lowBatteryValue));
            tvLowBatteryTips.setText(getString(R.string.low_battery_tips, lowBatteryValue));
            activity.setLowBattery(lowBatteryValue);
        });
        dialog.show(activity.getSupportFragmentManager());
    }

    private void showIntervalDialog() {
        BottomDialog dialog = new BottomDialog();
        dialog.setDatas(intervalList, intervalListIndex);
        dialog.setListener(value -> {
            intervalListIndex = value;
            intervalValue = value + 2;
            tvDeviceInfoIntervalValue.setText(String.valueOf(intervalValue));
            activity.setDeviceInfoInterval(intervalValue);
        });
        dialog.show(activity.getSupportFragmentManager());
    }

    private boolean scannerState;
    private int startTime;

    public void setScanWindow(int scanner, int startTime) {
        scannerState = scanner == 1;
        this.startTime = startTime;
        String scanModeStr = "";
        switch (startTime) {
            case 2:
                scanModeStr = "1/2";
                break;
            case 3:
                scanModeStr = "1/4";
                break;
            case 4:
                scanModeStr = "1/8";
                break;
        }
        tvScanWindow.setText(scannerState ? String.format("Scan Window(%s)", scanModeStr)
                : "Scan Window(0ms/1000ms)");
    }

    private boolean connectState;

    public void setConnectable(int connectable) {
        connectState = connectable == 1;
        ivConnectable.setImageResource(connectable == 1 ? R.drawable.ic_checked : R.drawable.ic_unchecked);
    }

    public void setLoRaConnectable(int connectable) {
        tvLoRaConnectable.setText(loraConnectable[connectable]);
    }

    private int lowBatteryValue;
    private ArrayList<String> lowBatteryList;
    private int lowBatteryListIndex;

    public void setLowBattery(int lowBattery) {
        lowBatteryValue = lowBattery;
        lowBatteryListIndex = lowBattery / 10 - 1;
        tvLowBatteryValue.setText(String.format("%d%%", lowBatteryValue));
        tvLowBatteryTips.setText(getString(R.string.low_battery_tips, lowBatteryValue));
    }

    private void createLowBatteryList() {
        if (lowBatteryList == null) {
            lowBatteryList = new ArrayList<>();
        } else {
            lowBatteryList.clear();
        }
        for (int i = 10; i <= 60; i += 10) {
            String lowBatteryStr = String.format("%d%%", i);
            lowBatteryList.add(lowBatteryStr);
        }
    }

    private int intervalValue;
    private ArrayList<String> intervalList;
    private int intervalListIndex;

    public void setDeviceInfoInterval(int interval) {
        intervalValue = interval;
        intervalListIndex = interval - 2;
        tvDeviceInfoIntervalValue.setText(String.valueOf(intervalValue));
    }

    private void createIntervalList() {
        if (intervalList == null) {
            intervalList = new ArrayList<>();
        } else {
            intervalList.clear();
        }
        for (int i = 2; i <= 120; i++) {
            String intervalStr = String.valueOf(i);
            intervalList.add(intervalStr);
        }
    }
}
