package com.moko.loratrackerv2.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import com.moko.ble.lib.task.OrderTask;
import com.moko.loratrackerv2.R;
import com.moko.loratrackerv2.R2;
import com.moko.loratrackerv2.activity.DeviceInfoActivity;
import com.moko.loratrackerv2.dialog.BottomDialog;
import com.moko.loratrackerv2.dialog.ScanWindowDialog;
import com.moko.support.loratracker.LoRaTrackerMokoSupport;
import com.moko.support.loratracker.OrderTaskAssembler;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ScannerFragment extends Fragment {
    private static final String TAG = ScannerFragment.class.getSimpleName();
    @BindView(R2.id.sb_scan_interval)
    SeekBar sbScanInterval;
    @BindView(R2.id.tv_scan_interval_value)
    TextView tvScanIntervalValue;
    @BindView(R2.id.tv_scan_interval_tips)
    TextView tvScanIntervalTips;
    @BindView(R2.id.tv_alarm_notify)
    TextView tvAlarmNotify;
    @BindView(R2.id.sb_alarm_trigger_rssi)
    SeekBar sbAlarmTriggerRssi;
    @BindView(R2.id.tv_alarm_trigger_rssi_value)
    TextView tvAlarmTriggerRssiValue;
    @BindView(R2.id.tv_alarm_trigger_rssi_tips)
    TextView tvAlarmTriggerRssiTips;
    @BindView(R2.id.tv_warning_range)
    TextView tvWarningRange;
    @BindView(R2.id.tv_warning_value)
    TextView tvWarningValue;
    @BindView(R2.id.tv_warning_tips)
    TextView tvWarningTips;
    @BindView(R2.id.tv_scan_window)
    TextView tvScanWindow;

    private DeviceInfoActivity activity;

    private ArrayList<String> mValues;
    private int mSelected;

    public ScannerFragment() {
    }


    public static ScannerFragment newInstance() {
        ScannerFragment fragment = new ScannerFragment();
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
        View view = inflater.inflate(R.layout.loratracker_fragment_scanner, container, false);
        ButterKnife.bind(this, view);
        activity = (DeviceInfoActivity) getActivity();
        sbScanInterval.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tvScanIntervalValue.setText(String.format("%ds", progress));
                tvScanIntervalTips.setText(getString(R.string.storage_interval, progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        sbAlarmTriggerRssi.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int rssi = progress - 127;
                tvAlarmTriggerRssiValue.setText(String.format("%dBm", rssi));
                tvAlarmTriggerRssiTips.setText(getString(R.string.alarm_trigger_rssi, rssi));
                warningMax = rssi;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (warningMax >= -124) {
                    if (warningRssiValue >= warningMax) {
                        warningRssiValue = warningMax - 3;
                        tvWarningRange.setText(getString(R.string.warning_range, warningMax));
                        tvWarningValue.setText(String.valueOf(warningRssiValue));
                        tvWarningTips.setText(getString(R.string.warning_tips, warningRssiValue));
                    }
                } else {
                    warningRssiValue = -127;
                    tvWarningRange.setText(getString(R.string.warning_range, warningMax));
                    tvWarningValue.setText(String.valueOf(warningRssiValue));
                    tvWarningTips.setText(getString(R.string.warning_tips, warningRssiValue));
                }
            }
        });
        mValues = new ArrayList<>();
        mValues.add("Off");
        mValues.add("Light");
        mValues.add("Vibration");
        mValues.add("Light+Vibration");
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

    public void showWarningRssiDialog() {
        createWarningRssiList();
        BottomDialog dialog = new BottomDialog();
        dialog.setDatas(warningRssiList, warningRssiListIndex);
        dialog.setListener(value -> {
            warningRssiListIndex = value;
            warningRssiValue = -(value - warningMax);
            tvWarningValue.setText(String.valueOf(warningRssiValue));
            tvWarningTips.setText(getString(R.string.warning_tips, warningRssiValue));
        });
        dialog.show(activity.getSupportFragmentManager());
    }

    public void showBeaconScannerDialog() {
        final ScanWindowDialog dialog = new ScanWindowDialog(getActivity());
        dialog.setData(scannerState ? startTime : 0);
        dialog.setOnScanWindowClicked(scanMode -> {
            String scanModeStr = "";
            switch (scanMode) {
                case 4:
                    scanModeStr = "0";
                    break;
                case 0:
                    scanModeStr = "1";
                    break;
                case 1:
                    scanModeStr = "1/2";
                    break;
                case 2:
                    scanModeStr = "1/4";
                    break;
                case 3:
                    scanModeStr = "1/8";
                    break;
            }
            tvScanWindow.setText(String.format("Scan Window(%s)", scanModeStr));
            if (scanMode < 4) {
                scanMode += 1;
                activity.setScanWindow(1, scanMode);
            } else {
                activity.setScanWindow(0, 1);
            }
        });
        dialog.show();
    }

    public void saveParams() {
        final int scanIntervalProgress = sbScanInterval.getProgress();
        final int alarmTriggerRssiProgress = sbAlarmTriggerRssi.getProgress();
        int rssi = alarmTriggerRssiProgress - 127;
        List<OrderTask> orderTasks = new ArrayList<>();

        orderTasks.add(OrderTaskAssembler.setFilterValidInterval(scanIntervalProgress));
        orderTasks.add(OrderTaskAssembler.setAlarmNotify(mSelected));
        orderTasks.add(OrderTaskAssembler.setWarningRssi(warningRssiValue));
        orderTasks.add(OrderTaskAssembler.setAlarmTriggerRssi(rssi));

        LoRaTrackerMokoSupport.getInstance().sendOrder(orderTasks.toArray(new OrderTask[]{}));
    }

    public void setScanInterval(int time) {
        if (time <= 600)
            sbScanInterval.setProgress(time);
    }

    private int warningMax;
    private int warningRssiValue;
    private ArrayList<String> warningRssiList;
    private int warningRssiListIndex;

    public void setAlarmTriggerRssi(int rssi) {
        int progress = rssi + 127;
        if (progress >= 0 && progress <= 127) {
            sbAlarmTriggerRssi.setProgress(progress);
            int value = progress - 127;
            tvAlarmTriggerRssiValue.setText(String.format("%dBm", value));
            tvAlarmTriggerRssiTips.setText(getString(R.string.alarm_trigger_rssi, value));
            warningMax = rssi;
            tvWarningRange.setText(getString(R.string.warning_range, rssi));
        }
    }

    private void createWarningRssiList() {
        if (warningRssiList == null) {
            warningRssiList = new ArrayList<>();
        } else {
            warningRssiList.clear();
        }
        int warningRssiMax = warningMax + 127;
        for (int i = 0; i <= warningRssiMax; i++) {
            String rssiStr = String.valueOf(i - 127);
            warningRssiList.add(0, rssiStr);
        }
        warningRssiListIndex = Math.abs(warningRssiValue) + warningMax;
    }

    public void setWarningRssi(int rssi) {
        warningRssiValue = rssi;
        warningRssiListIndex = Math.abs(rssi) + warningMax;
        tvWarningValue.setText(String.valueOf(warningRssiValue));
        tvWarningTips.setText(getString(R.string.warning_tips, warningRssiValue));
    }

    public void setAlarmNotify(int alarmNotify) {
        if (alarmNotify <= 3) {
            tvAlarmNotify.setText(mValues.get(alarmNotify));
            mSelected = alarmNotify;
        }
    }

    private boolean scannerState;
    private int startTime;

    public void setScanWindow(int scanner, int startTime) {
        scannerState = scanner == 1;
        this.startTime = startTime;
        String scanModeStr = "";
        switch (startTime) {
            case 1:
                scanModeStr = "1";
                break;
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
                : "Scan Window(0)");
    }

    public void showAlarmNotifyDialog() {
        BottomDialog dialog = new BottomDialog();
        dialog.setDatas(mValues, mSelected);
        dialog.setListener(value -> {
            mSelected = value;
            tvAlarmNotify.setText(mValues.get(value));
        });
        dialog.show(activity.getSupportFragmentManager());
    }
}
