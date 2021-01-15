package com.moko.loratrackerv2.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.moko.loratrackerv2.R;
import com.moko.loratrackerv2.R2;
import com.moko.loratrackerv2.activity.DeviceInfoActivity;
import com.moko.support.loratracker.LoRaTrackerMokoSupport;
import com.moko.support.loratracker.OrderTaskAssembler;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoRaFragment extends Fragment {
    private static final String TAG = LoRaFragment.class.getSimpleName();
    @BindView(R2.id.tv_lora_info)
    TextView tvLoraInfo;
    @BindView(R2.id.tv_network_check)
    TextView tvNetworkCheck;
    @BindView(R2.id.et_time_sync_interval)
    EditText etTimeSyncInterval;
    @BindView(R2.id.tv_uplink_payload)
    TextView tvUplinkPayload;

    private DeviceInfoActivity activity;

    public LoRaFragment() {
    }


    public static LoRaFragment newInstance() {
        LoRaFragment fragment = new LoRaFragment();
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
        View view = inflater.inflate(R.layout.loratracker_fragment_lora, container, false);
        ButterKnife.bind(this, view);
        activity = (DeviceInfoActivity) getActivity();

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

    public void setLoRaInfo(String loraInfo) {
        tvLoraInfo.setText(loraInfo);
    }

    public void setNetworkCheck(int networkCheck) {
        String networkCheckDisPlay = "";
        switch (networkCheck) {
            case 0:
                networkCheckDisPlay = "Disconnected";
                break;
            case 1:
                networkCheckDisPlay = "Connecting";
                break;
            case 2:
                networkCheckDisPlay = "Connected";
                break;
        }
        tvNetworkCheck.setText(networkCheckDisPlay);
    }


    public boolean isValid() {
        final String timeSyncIntervalStr = etTimeSyncInterval.getText().toString();
        if (TextUtils.isEmpty(timeSyncIntervalStr))
            return false;
        final int timeSyncInterval = Integer.parseInt(timeSyncIntervalStr);
        if (timeSyncInterval > 240)
            return false;
        return true;
    }


    public void saveParams() {
        final String timeSyncIntervalStr = etTimeSyncInterval.getText().toString();
        final int timeSyncInterval = Integer.parseInt(timeSyncIntervalStr);
        LoRaTrackerMokoSupport.getInstance().sendOrder(
                OrderTaskAssembler.setTimeSyncInterval(timeSyncInterval));
    }

    public void setTimeSyncInterval(int interval) {
        etTimeSyncInterval.setText(String.valueOf(interval));
    }
}
