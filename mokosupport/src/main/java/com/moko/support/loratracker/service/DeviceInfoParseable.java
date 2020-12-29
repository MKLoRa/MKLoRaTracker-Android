package com.moko.support.loratracker.service;

import com.moko.support.loratracker.entity.DeviceInfo;

public interface DeviceInfoParseable<T> {
    T parseDeviceInfo(DeviceInfo deviceInfo);
}
