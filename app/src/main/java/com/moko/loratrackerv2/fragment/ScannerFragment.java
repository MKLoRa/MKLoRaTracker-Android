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
    private ArrayList<String> mScanWindowValues;
    private int mSelected;
    private int mScanWindowSelected;

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
                tvScanIntervalValue.setText(String.format("%ds", progress + 1));
                tvScanIntervalTips.setText(getString(R.string.storage_interval, progress + 1));
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
                tvWarningRange.setText(getString(R.string.warning_range, warningMax));
                if (warningMax >= -124) {
                    if (warningRssiValue >= warningMax) {
                        warningRssiValue = warningMax - 3;
                        tvWarningValue.setText(String.valueOf(warningRssiValue));
                        tvWarningTips.setText(getString(R.string.warning_tips, warningRssiValue));
                    }
                } else {
                    warningRssiValue = -127;
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
        mScanWindowValues = new ArrayList<>();
        mScanWindowValues.add("Off");
        mScanWindowValues.add("Low");
        mScanWindowValues.add("Medium");
        mScanWindowValues.add("Strong");
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
        BottomDialog dialog = new BottomDialog();
        dialog.setDatas(mScanWindowValues, mScanWindowSelected);
        dialog.setListener(value -> {
            mScanWindowSelected = value;
            tvScanWindow.setText(mScanWindowValues.get(value));
        });
        dialog.show(activity.getSupportFragmentManager());
    }

    public void saveParams() {
        final int scanIntervalProgress = sbScanInterval.getProgress() + 1;
        final int alarmTriggerRssiProgress = sbAlarmTriggerRssi.getProgress();
        int rssi = alarmTriggerRssiProgress - 127;
        List<OrderTask> orderTasks = new ArrayList<>();

        orderTasks.add(OrderTaskAssembler.setFilterValidInterval(scanIntervalProgress));
        orderTasks.add(OrderTaskAssembler.setAlarmNotify(mSelected));
        orderTasks.add(OrderTaskAssembler.setScanWindow(mScanWindowSelected));
        orderTasks.add(OrderTaskAssembler.setWarningRssi(warningRssiValue));
        orderTasks.add(OrderTaskAssembler.setAlarmTriggerRssi(rssi));

        LoRaTrackerMokoSupport.getInstance().sendOrder(orderTasks.toArray(new OrderTask[]{}));
    }

    public void setScanInterval(int time) {
        if (time <= 600)
            sbScanInterval.setProgress(time - 1);
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

    public void setScanWindow(int scanWindow) {
        if (scanWindow <= 3) {
            tvScanWindow.setText(mScanWindowValues.get(scanWindow));
            mScanWindowSelected = scanWindow;
        }
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
