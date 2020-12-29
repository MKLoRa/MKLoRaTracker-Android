package com.moko.support.loratracker.callback;

import com.moko.support.loratracker.entity.DeviceInfo;

public interface MokoScanDeviceCallback {
    void onStartScan();

    void onScanDevice(DeviceInfo device);

    void onStopScan();
}
