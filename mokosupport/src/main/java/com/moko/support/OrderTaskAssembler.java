package com.moko.support;

import com.moko.support.entity.ConfigKeyEnum;
import com.moko.support.task.GetDeviceModelTask;
import com.moko.support.task.GetFirmwareVersionTask;
import com.moko.support.task.GetHardwareVersionTask;
import com.moko.support.task.GetManufacturerTask;
import com.moko.support.task.GetSoftwareVersionTask;
import com.moko.support.task.OrderTask;
import com.moko.support.task.SetPasswordTask;
import com.moko.support.task.WriteConfigTask;

import java.util.ArrayList;

public class OrderTaskAssembler {
    ///////////////////////////////////////////////////////////////////////////
    // READ
    ///////////////////////////////////////////////////////////////////////////

    public static OrderTask getManufacturer() {
        GetManufacturerTask getManufacturerTask = new GetManufacturerTask();
        return getManufacturerTask;
    }

    public static OrderTask getDeviceModel() {
        GetDeviceModelTask getDeviceModelTask = new GetDeviceModelTask();
        return getDeviceModelTask;
    }

    public static OrderTask getHardwareVersion() {
        GetHardwareVersionTask getHardwareVersionTask = new GetHardwareVersionTask();
        return getHardwareVersionTask;
    }

    public static OrderTask getFirmwareVersion() {
        GetFirmwareVersionTask getFirmwareVersionTask = new GetFirmwareVersionTask();
        return getFirmwareVersionTask;
    }

    public static OrderTask getSoftwareVersion() {
        GetSoftwareVersionTask getSoftwareVersionTask = new GetSoftwareVersionTask();
        return getSoftwareVersionTask;
    }

    public static OrderTask getBattery() {
        WriteConfigTask task = new WriteConfigTask();
        task.setData(ConfigKeyEnum.KEY_BATTERY);
        return task;
    }

    public static OrderTask getiBeaconUUID() {
        WriteConfigTask task = new WriteConfigTask();
        task.setData(ConfigKeyEnum.KEY_IBEACON_UUID);
        return task;
    }

    public static OrderTask getiBeaconMajor() {
        WriteConfigTask task = new WriteConfigTask();
        task.setData(ConfigKeyEnum.KEY_IBEACON_MAJOR);
        return task;
    }

    public static OrderTask getIBeaconMinor() {
        WriteConfigTask task = new WriteConfigTask();
        task.setData(ConfigKeyEnum.KEY_IBEACON_MINOR);
        return task;
    }

    public static OrderTask getMeasurePower() {
        WriteConfigTask task = new WriteConfigTask();
        task.setData(ConfigKeyEnum.KEY_MEASURE_POWER);
        return task;
    }

    public static OrderTask getTransmission() {
        WriteConfigTask task = new WriteConfigTask();
        task.setData(ConfigKeyEnum.KEY_TRANSMISSION);
        return task;
    }

    public static OrderTask getAdvInterval() {
        WriteConfigTask task = new WriteConfigTask();
        task.setData(ConfigKeyEnum.KEY_ADV_INTERVAL);
        return task;
    }

    public static OrderTask getAdvName() {
        WriteConfigTask task = new WriteConfigTask();
        task.setData(ConfigKeyEnum.KEY_ADV_NAME);
        return task;
    }

    public static OrderTask getScanInterval() {
        WriteConfigTask task = new WriteConfigTask();
        task.setData(ConfigKeyEnum.KEY_SCAN_INTERVAL);
        return task;
    }

    public static OrderTask getAlarmNotify() {
        WriteConfigTask task = new WriteConfigTask();
        task.setData(ConfigKeyEnum.KEY_ALARM_NOTIFY);
        return task;
    }

    public static OrderTask getAlarmRssi() {
        WriteConfigTask task = new WriteConfigTask();
        task.setData(ConfigKeyEnum.KEY_ALARM_RSSI);
        return task;
    }

    public static OrderTask getVibrationIntansity() {
        WriteConfigTask task = new WriteConfigTask();
        task.setData(ConfigKeyEnum.KEY_VIBRATION_INTENSITY);
        return task;
    }

    public static OrderTask getVibrationCycle() {
        WriteConfigTask task = new WriteConfigTask();
        task.setData(ConfigKeyEnum.KEY_VIBRATION_CYCLE);
        return task;
    }

    public static OrderTask getVibrationDuration() {
        WriteConfigTask task = new WriteConfigTask();
        task.setData(ConfigKeyEnum.KEY_VIBRATION_DURATION);
        return task;
    }

    public static OrderTask getWarningRssi() {
        WriteConfigTask task = new WriteConfigTask();
        task.setData(ConfigKeyEnum.KEY_WARNING_RSSI);
        return task;
    }

    public static OrderTask getLoRaConnectable() {
        WriteConfigTask task = new WriteConfigTask();
        task.setData(ConfigKeyEnum.KEY_LORA_CONNECTABLE);
        return task;
    }

    public static OrderTask getScanWindow() {
        WriteConfigTask task = new WriteConfigTask();
        task.setData(ConfigKeyEnum.KEY_SCAN_WINDOW);
        return task;
    }

    public static OrderTask getConnectable() {
        WriteConfigTask task = new WriteConfigTask();
        task.setData(ConfigKeyEnum.KEY_CONNECTABLE);
        return task;
    }

    public static OrderTask getLowBattery() {
        WriteConfigTask task = new WriteConfigTask();
        task.setData(ConfigKeyEnum.KEY_LOW_POWER_PERCENT);
        return task;
    }

    public static OrderTask getDeviceInfoInterval() {
        WriteConfigTask task = new WriteConfigTask();
        task.setData(ConfigKeyEnum.KEY_DEVICE_INFO_INTERVAL);
        return task;
    }

    public static OrderTask getMacAddress() {
        WriteConfigTask task = new WriteConfigTask();
        task.setData(ConfigKeyEnum.KEY_DEVICE_MAC);
        return task;
    }

    public static OrderTask getFilterSwitchA() {
        WriteConfigTask task = new WriteConfigTask();
        task.setData(ConfigKeyEnum.KEY_FILTER_SWITCH_A);
        return task;
    }

    public static OrderTask getFilterRssiA() {
        WriteConfigTask task = new WriteConfigTask();
        task.setData(ConfigKeyEnum.KEY_FILTER_RSSI_A);
        return task;
    }

    public static OrderTask getFilterMacA() {
        WriteConfigTask task = new WriteConfigTask();
        task.setData(ConfigKeyEnum.KEY_FILTER_MAC_A);
        return task;
    }

    public static OrderTask getFilterNameA() {
        WriteConfigTask task = new WriteConfigTask();
        task.setData(ConfigKeyEnum.KEY_FILTER_ADV_NAME_A);
        return task;
    }

    public static OrderTask getFilterUUIDA() {
        WriteConfigTask task = new WriteConfigTask();
        task.setData(ConfigKeyEnum.KEY_FILTER_UUID_A);
        return task;
    }

    public static OrderTask getFilterAdvRawDataA() {
        WriteConfigTask task = new WriteConfigTask();
        task.setData(ConfigKeyEnum.KEY_FILTER_ADV_RAW_DATA_A);
        return task;
    }

    public static OrderTask getFilterMajorRangeA() {
        WriteConfigTask task = new WriteConfigTask();
        task.setData(ConfigKeyEnum.KEY_FILTER_MAJOR_RANGE_A);
        return task;
    }

    public static OrderTask getFilterMinorRangeA() {
        WriteConfigTask task = new WriteConfigTask();
        task.setData(ConfigKeyEnum.KEY_FILTER_MINOR_RANGE_A);
        return task;
    }

    public static OrderTask getFilterSwitchB() {
        WriteConfigTask task = new WriteConfigTask();
        task.setData(ConfigKeyEnum.KEY_FILTER_SWITCH_B);
        return task;
    }

    public static OrderTask getFilterRssiB() {
        WriteConfigTask task = new WriteConfigTask();
        task.setData(ConfigKeyEnum.KEY_FILTER_RSSI_B);
        return task;
    }

    public static OrderTask getFilterMacB() {
        WriteConfigTask task = new WriteConfigTask();
        task.setData(ConfigKeyEnum.KEY_FILTER_MAC_B);
        return task;
    }

    public static OrderTask getFilterNameB() {
        WriteConfigTask task = new WriteConfigTask();
        task.setData(ConfigKeyEnum.KEY_FILTER_ADV_NAME_B);
        return task;
    }

    public static OrderTask getFilterUUIDB() {
        WriteConfigTask task = new WriteConfigTask();
        task.setData(ConfigKeyEnum.KEY_FILTER_UUID_B);
        return task;
    }

    public static OrderTask getFilterAdvRawDataB() {
        WriteConfigTask task = new WriteConfigTask();
        task.setData(ConfigKeyEnum.KEY_FILTER_ADV_RAW_DATA_B);
        return task;
    }

    public static OrderTask getFilterMajorRangeB() {
        WriteConfigTask task = new WriteConfigTask();
        task.setData(ConfigKeyEnum.KEY_FILTER_MAJOR_RANGE_B);
        return task;
    }

    public static OrderTask getFilterMinorRangeB() {
        WriteConfigTask task = new WriteConfigTask();
        task.setData(ConfigKeyEnum.KEY_FILTER_MINOR_RANGE_B);
        return task;
    }

    public static OrderTask getFilterABRelation() {
        WriteConfigTask task = new WriteConfigTask();
        task.setData(ConfigKeyEnum.KEY_FILTER_A_B_RELATION);
        return task;
    }

    public static OrderTask getLoraMode() {
        WriteConfigTask task = new WriteConfigTask();
        task.setData(ConfigKeyEnum.KEY_LORA_MODE);
        return task;
    }

    public static OrderTask getLoraDevEUI() {
        WriteConfigTask task = new WriteConfigTask();
        task.setData(ConfigKeyEnum.KEY_LORA_DEV_EUI);
        return task;
    }

    public static OrderTask getLoraAppEUI() {
        WriteConfigTask task = new WriteConfigTask();
        task.setData(ConfigKeyEnum.KEY_LORA_APP_EUI);
        return task;
    }

    public static OrderTask getLoraAppKey() {
        WriteConfigTask task = new WriteConfigTask();
        task.setData(ConfigKeyEnum.KEY_LORA_APP_KEY);
        return task;
    }

    public static OrderTask getLoraDevAddr() {
        WriteConfigTask task = new WriteConfigTask();
        task.setData(ConfigKeyEnum.KEY_LORA_DEV_ADDR);
        return task;
    }

    public static OrderTask getLoraAppSKey() {
        WriteConfigTask task = new WriteConfigTask();
        task.setData(ConfigKeyEnum.KEY_LORA_APP_SKEY);
        return task;
    }

    public static OrderTask getLoraNwkSKey() {
        WriteConfigTask task = new WriteConfigTask();
        task.setData(ConfigKeyEnum.KEY_LORA_NWK_SKEY);
        return task;
    }

    public static OrderTask getLoraRegion() {
        WriteConfigTask task = new WriteConfigTask();
        task.setData(ConfigKeyEnum.KEY_LORA_REGION);
        return task;
    }

    public static OrderTask getLoraReportInterval() {
        WriteConfigTask task = new WriteConfigTask();
        task.setData(ConfigKeyEnum.KEY_LORA_REPORT_INTERVAL);
        return task;
    }

    public static OrderTask getLoraMessageType() {
        WriteConfigTask task = new WriteConfigTask();
        task.setData(ConfigKeyEnum.KEY_LORA_MESSAGE_TYPE);
        return task;
    }

    public static OrderTask getLoraCH() {
        WriteConfigTask task = new WriteConfigTask();
        task.setData(ConfigKeyEnum.KEY_LORA_CH);
        return task;
    }

    public static OrderTask getLoraDR() {
        WriteConfigTask task = new WriteConfigTask();
        task.setData(ConfigKeyEnum.KEY_LORA_DR);
        return task;
    }

    public static OrderTask getLoraADR() {
        WriteConfigTask task = new WriteConfigTask();
        task.setData(ConfigKeyEnum.KEY_LORA_ADR);
        return task;
    }

    ///////////////////////////////////////////////////////////////////////////
    // WRITE
    ///////////////////////////////////////////////////////////////////////////

    public static WriteConfigTask setWriteConfig(ConfigKeyEnum configKeyEnum) {
        WriteConfigTask writeConfigTask = new WriteConfigTask();
        writeConfigTask.setData(configKeyEnum);
        return writeConfigTask;
    }

    public static OrderTask setPassword(String password) {
        SetPasswordTask setPasswordTask = new SetPasswordTask();
        setPasswordTask.setData(password);
        return setPasswordTask;
    }

    public static WriteConfigTask setTime() {
        WriteConfigTask writeConfigTask = new WriteConfigTask();
        writeConfigTask.setTime();
        return writeConfigTask;
    }

    public static OrderTask setDeviceName(String deviceName) {
        WriteConfigTask writeConfigTask = new WriteConfigTask();
        writeConfigTask.setAdvName(deviceName);
        return writeConfigTask;
    }

    public static OrderTask setUUID(String uuid) {
        WriteConfigTask writeConfigTask = new WriteConfigTask();
        writeConfigTask.setUUID(uuid);
        return writeConfigTask;
    }

    public static OrderTask setMajor(int major) {
        WriteConfigTask writeConfigTask = new WriteConfigTask();
        writeConfigTask.setMajor(major);
        return writeConfigTask;
    }

    public static OrderTask setMinor(int minor) {
        WriteConfigTask writeConfigTask = new WriteConfigTask();
        writeConfigTask.setMinor(minor);
        return writeConfigTask;
    }

    public static OrderTask setAdvInterval(int advInterval) {
        WriteConfigTask writeConfigTask = new WriteConfigTask();
        writeConfigTask.setAdvInterval(advInterval);
        return writeConfigTask;
    }


    public static OrderTask setTransmission(int transmission) {
        WriteConfigTask writeConfigTask = new WriteConfigTask();
        writeConfigTask.setTransmission(transmission);
        return writeConfigTask;
    }

    public static OrderTask setMeasurePower(int measurePower) {
        WriteConfigTask writeConfigTask = new WriteConfigTask();
        writeConfigTask.setMeasurePower(measurePower);
        return writeConfigTask;
    }

    public static WriteConfigTask setScanInterval(int seconds) {
        WriteConfigTask writeConfigTask = new WriteConfigTask();
        writeConfigTask.setScanInterval(seconds);
        return writeConfigTask;
    }

    public static WriteConfigTask setAlarmNotify(int notify) {
        WriteConfigTask writeConfigTask = new WriteConfigTask();
        writeConfigTask.setAlarmNotify(notify);
        return writeConfigTask;
    }

    public static WriteConfigTask setWarningRssi(int rssi) {
        WriteConfigTask writeConfigTask = new WriteConfigTask();
        writeConfigTask.setWarningRssi(rssi);
        return writeConfigTask;
    }

    public static WriteConfigTask setAlarmTriggerRssi(int rssi) {
        WriteConfigTask writeConfigTask = new WriteConfigTask();
        writeConfigTask.setAlarmTirggerRssi(rssi);
        return writeConfigTask;
    }

    public static OrderTask setConnectionMode(int connectionMode) {
        WriteConfigTask writeConfigTask = new WriteConfigTask();
        writeConfigTask.setConnectable(connectionMode);
        return writeConfigTask;
    }

    public static OrderTask setLowBattery(int lowBattery) {
        WriteConfigTask writeConfigTask = new WriteConfigTask();
        writeConfigTask.setLowBattery(lowBattery);
        return writeConfigTask;
    }

    public static OrderTask setDeviceInfoInterval(int interval) {
        WriteConfigTask writeConfigTask = new WriteConfigTask();
        writeConfigTask.setDeviceInfoInterval(interval);
        return writeConfigTask;
    }

    public static OrderTask changePassword(String password) {
        WriteConfigTask writeConfigTask = new WriteConfigTask();
        writeConfigTask.changePassword(password);
        return writeConfigTask;
    }

    public static OrderTask setReset() {
        WriteConfigTask writeConfigTask = new WriteConfigTask();
        writeConfigTask.reset();
        return writeConfigTask;
    }

    public static OrderTask setScanWindow(int scannerState, int startTime) {
        WriteConfigTask writeConfigTask = new WriteConfigTask();
        writeConfigTask.setScanWinow(scannerState, startTime);
        return writeConfigTask;
    }

    public static OrderTask closePower() {
        WriteConfigTask task = new WriteConfigTask();
        task.setClosePower();
        return task;
    }

    public static OrderTask setFilterRssiA(int rssi) {
        WriteConfigTask task = new WriteConfigTask();
        task.setFilterRssiA(rssi);
        return task;
    }

    public static OrderTask setFilterMacA(String mac, boolean isReverse) {
        WriteConfigTask task = new WriteConfigTask();
        task.setFilterMacA(mac, isReverse);
        return task;
    }

    public static OrderTask setFilterNameA(String name, boolean isReverse) {
        WriteConfigTask task = new WriteConfigTask();
        task.setFilterNameA(name, isReverse);
        return task;
    }

    public static OrderTask setFilterUUIDA(String uuid, boolean isReverse) {
        WriteConfigTask task = new WriteConfigTask();
        task.setFilterUUIDA(uuid, isReverse);
        return task;
    }

    public static OrderTask setFilterAdvRawDataA(ArrayList<String> filterRawDatas, boolean isReverse) {
        WriteConfigTask task = new WriteConfigTask();
        task.setFilterRawDataA(filterRawDatas, isReverse);
        return task;
    }

    public static OrderTask setFilterMajorRangeA(int enable, int majorMin, int majorMax, boolean isReverse) {
        WriteConfigTask task = new WriteConfigTask();
        task.setFilterMajorRangeA(enable, majorMin, majorMax, isReverse);
        return task;
    }

    public static OrderTask setFilterMinorRangeA(int enable, int majorMin, int majorMax, boolean isReverse) {
        WriteConfigTask task = new WriteConfigTask();
        task.setFilterMinorRangeA(enable, majorMin, majorMax, isReverse);
        return task;
    }

    public static OrderTask setFilterSwitchA(int enable) {
        WriteConfigTask task = new WriteConfigTask();
        task.setFilterSwitchA(enable);
        return task;
    }

    public static OrderTask setFilterRssiB(int rssi) {
        WriteConfigTask task = new WriteConfigTask();
        task.setFilterRssiB(rssi);
        return task;
    }

    public static OrderTask setFilterMacB(String mac, boolean isReverse) {
        WriteConfigTask task = new WriteConfigTask();
        task.setFilterMacB(mac, isReverse);
        return task;
    }

    public static OrderTask setFilterNameB(String name, boolean isReverse) {
        WriteConfigTask task = new WriteConfigTask();
        task.setFilterNameB(name, isReverse);
        return task;
    }

    public static OrderTask setFilterUUIDB(String uuid, boolean isReverse) {
        WriteConfigTask task = new WriteConfigTask();
        task.setFilterUUIDB(uuid, isReverse);
        return task;
    }

    public static OrderTask setFilterAdvRawDataB(ArrayList<String> filterRawDatas, boolean isReverse) {
        WriteConfigTask task = new WriteConfigTask();
        task.setFilterRawDataB(filterRawDatas, isReverse);
        return task;
    }

    public static OrderTask setFilterMajorRangeB(int enable, int majorMin, int majorMax, boolean isReverse) {
        WriteConfigTask task = new WriteConfigTask();
        task.setFilterMajorRangeB(enable, majorMin, majorMax, isReverse);
        return task;
    }

    public static OrderTask setFilterMinorRangeB(int enable, int majorMin, int majorMax, boolean isReverse) {
        WriteConfigTask task = new WriteConfigTask();
        task.setFilterMinorRangeB(enable, majorMin, majorMax, isReverse);
        return task;
    }

    public static OrderTask setFilterSwitchB(int enable) {
        WriteConfigTask task = new WriteConfigTask();
        task.setFilterSwitchB(enable);
        return task;
    }

    public static OrderTask setFilterABRelation(int relation) {
        WriteConfigTask task = new WriteConfigTask();
        task.setFilterABRelation(relation);
        return task;
    }

    public static OrderTask setLoraDevAddr(String devAddr) {
        WriteConfigTask task = new WriteConfigTask();
        task.setLoraDevAddr(devAddr);
        return task;
    }

    public static OrderTask setLoraNwkSKey(String nwkSKey) {
        WriteConfigTask task = new WriteConfigTask();
        task.setLoraNwkSKey(nwkSKey);
        return task;
    }

    public static OrderTask setLoraAppSKey(String appSKey) {
        WriteConfigTask task = new WriteConfigTask();
        task.setLoraAppSKey(appSKey);
        return task;
    }

    public static OrderTask setLoraAppEui(String appEui) {
        WriteConfigTask task = new WriteConfigTask();
        task.setLoraAppEui(appEui);
        return task;
    }

    public static OrderTask setLoraDevEui(String devEui) {
        WriteConfigTask task = new WriteConfigTask();
        task.setLoraDevEui(devEui);
        return task;
    }

    public static OrderTask setLoraAppKey(String appKey) {
        WriteConfigTask task = new WriteConfigTask();
        task.setLoraAppKey(appKey);
        return task;
    }

    public static OrderTask setLoraUploadMode(int mode) {
        WriteConfigTask task = new WriteConfigTask();
        task.setLoraUploadMode(mode);
        return task;
    }

    public static OrderTask setLoraUploadInterval(int interval) {
        WriteConfigTask task = new WriteConfigTask();
        task.setLoraUploadInterval(interval);
        return task;
    }

    public static OrderTask setLoraMessageType(int type) {
        WriteConfigTask task = new WriteConfigTask();
        task.setLoraMessageType(type);
        return task;
    }

    public static OrderTask setLoraRegion(int region) {
        WriteConfigTask task = new WriteConfigTask();
        task.setLoraRegion(region);
        return task;
    }

    public static OrderTask setLoraCH(int ch1, int ch2) {
        WriteConfigTask task = new WriteConfigTask();
        task.setLoraCH(ch1, ch2);
        return task;
    }

    public static OrderTask setLoraDR(int dr1) {
        WriteConfigTask task = new WriteConfigTask();
        task.setLoraDR(dr1);
        return task;
    }

    public static OrderTask setLoraADR(int adr) {
        WriteConfigTask task = new WriteConfigTask();
        task.setLoraADR(adr);
        return task;
    }

    public static OrderTask setLoraConnect() {
        WriteConfigTask task = new WriteConfigTask();
        task.setLoraConnect();
        return task;
    }

    public static OrderTask setVibrationIntensity(int intensity) {
        WriteConfigTask task = new WriteConfigTask();
        task.setVibrationIntensity(intensity);
        return task;
    }

    public static OrderTask setVibrationDuration(int duration) {
        WriteConfigTask task = new WriteConfigTask();
        task.setVibrationDuration(duration);
        return task;
    }

    public static OrderTask setVibrationCycle(int cycle) {
        WriteConfigTask task = new WriteConfigTask();
        task.setVibrationCycle(cycle);
        return task;
    }
}
