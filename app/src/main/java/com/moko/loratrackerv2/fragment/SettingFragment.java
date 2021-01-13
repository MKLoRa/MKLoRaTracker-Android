package com.moko.loratrackerv2.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.moko.loratrackerv2.R;
import com.moko.loratrackerv2.R2;
import com.moko.loratrackerv2.activity.DeviceInfoActivity;
import com.moko.loratrackerv2.dialog.AlertMessageDialog;
import com.moko.loratrackerv2.dialog.BottomDialog;
import com.moko.loratrackerv2.dialog.ChangePasswordDialog;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SettingFragment extends Fragment {
    private static final String TAG = SettingFragment.class.getSimpleName();
    @BindView(R2.id.iv_connectable)
    ImageView ivConnectable;
    @BindView(R2.id.tv_low_battery_value)
    TextView tvLowBatteryValue;
    @BindView(R2.id.tv_low_battery_tips)
    TextView tvLowBatteryTips;
    @BindView(R2.id.tv_adv_name)
    TextView tvAdvName;
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
        View view = inflater.inflate(R.layout.loratracker_fragment_setting, container, false);
        ButterKnife.bind(this, view);
        activity = (DeviceInfoActivity) getActivity();
        createLowBatteryList();
        return view;
    }


    @Override
    public void onPause() {
        Log.i(TAG, "onPause: ");
        super.onPause();
    }

    @Override
    public void onDestroy() {
        Log.i(TAG, "onDestroy: ");
        super.onDestroy();
    }

    public void setDeviceName(String deviceName) {
        tvAdvName.setText(deviceName);
    }

    public void showResetDialog() {
        AlertMessageDialog dialog = new AlertMessageDialog();
        dialog.setTitle("Factory Reset!");
        dialog.setMessage("After factory reset,all the data will be reseted to the factory values.");
        dialog.setConfirm("OK");
        dialog.setOnAlertConfirmListener(() -> {
            activity.reset();
        });
        dialog.show(activity.getSupportFragmentManager());
    }

    public void showChangePasswordDialog() {
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


    public void showConnectableDialog() {
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

    public void showPowerOffDialog() {
        AlertMessageDialog dialog = new AlertMessageDialog();
        dialog.setTitle("Warning!");
        dialog.setMessage("Are you sure to turn off the device? Please make sure the device has a button to turn on!");
        dialog.setConfirm("OK");
        dialog.setOnAlertConfirmListener(() -> {
            activity.powerOff();
        });
        dialog.show(activity.getSupportFragmentManager());
    }

    public void showLowBatteryDialog() {
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

    private boolean connectState;

    public void setConnectable(int connectable) {
        connectState = connectable == 1;
        ivConnectable.setImageResource(connectable == 1 ? R.drawable.loratracker_ic_checked : R.drawable.loratracker_ic_unchecked);
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
}
