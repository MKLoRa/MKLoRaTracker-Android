package com.moko.support.loratracker;

import com.moko.ble.lib.task.OrderTask;
import com.moko.support.loratracker.entity.ParamsKeyEnum;
import com.moko.support.loratracker.task.GetFirmwareRevisionTask;
import com.moko.support.loratracker.task.GetHardwareRevisionTask;
import com.moko.support.loratracker.task.GetManufacturerNameTask;
import com.moko.support.loratracker.task.GetModelNumberTask;
import com.moko.support.loratracker.task.GetSoftwareRevisionTask;
import com.moko.support.loratracker.task.ParamsTask;
import com.moko.support.loratracker.task.SetPasswordTask;

import java.util.ArrayList;

public class OrderTaskAssembler {
    ///////////////////////////////////////////////////////////////////////////
    // READ
    ///////////////////////////////////////////////////////////////////////////

    public static OrderTask getManufacturer() {
        GetManufacturerNameTask getManufacturerTask = new GetManufacturerNameTask();
        return getManufacturerTask;
    }

    public static OrderTask getDeviceModel() {
        GetModelNumberTask getDeviceModelTask = new GetModelNumberTask();
        return getDeviceModelTask;
    }

    public static OrderTask getHardwareVersion() {
        GetHardwareRevisionTask getHardwareVersionTask = new GetHardwareRevisionTask();
        return getHardwareVersionTask;
    }

    public static OrderTask getFirmwareVersion() {
        GetFirmwareRevisionTask getFirmwareVersionTask = new GetFirmwareRevisionTask();
        return getFirmwareVersionTask;
    }

    public static OrderTask getSoftwareVersion() {
        GetSoftwareRevisionTask getSoftwareVersionTask = new GetSoftwareRevisionTask();
        return getSoftwareVersionTask;
    }

    public static OrderTask getBattery() {
        ParamsTask task = new ParamsTask();
        task.setData(ParamsKeyEnum.KEY_BATTERY);
        return task;
    }

    public static OrderTask getiBeaconUUID() {
        ParamsTask task = new ParamsTask();
        task.setData(ParamsKeyEnum.KEY_IBEACON_UUID);
        return task;
    }

    public static OrderTask getiBeaconMajor() {
        ParamsTask task = new ParamsTask();
        task.setData(ParamsKeyEnum.KEY_IBEACON_MAJOR);
        return task;
    }

    public static OrderTask getIBeaconMinor() {
        ParamsTask task = new ParamsTask();
        task.setData(ParamsKeyEnum.KEY_IBEACON_MINOR);
        return task;
    }

    public static OrderTask getMeasurePower() {
        ParamsTask task = new ParamsTask();
        task.setData(ParamsKeyEnum.KEY_MEASURE_POWER);
        return task;
    }

    public static OrderTask getTransmission() {
        ParamsTask task = new ParamsTask();
        task.setData(ParamsKeyEnum.KEY_TRANSMISSION);
        return task;
    }

    public static OrderTask getAdvInterval() {
        ParamsTask task = new ParamsTask();
        task.setData(ParamsKeyEnum.KEY_ADV_INTERVAL);
        return task;
    }

    public static OrderTask getAdvName() {
        ParamsTask task = new ParamsTask();
        task.setData(ParamsKeyEnum.KEY_ADV_NAME);
        return task;
    }

    public static OrderTask getScanInterval() {
        ParamsTask task = new ParamsTask();
        task.setData(ParamsKeyEnum.KEY_FILTER_VALID_INTERVAL);
        return task;
    }

    public static OrderTask getAlarmNotify() {
        ParamsTask task = new ParamsTask();
        task.setData(ParamsKeyEnum.KEY_ALARM_NOTIFY);
        return task;
    }

    public static OrderTask getAlarmRssi() {
        ParamsTask task = new ParamsTask();
        task.setData(ParamsKeyEnum.KEY_ALARM_RSSI);
        return task;
    }

    public static OrderTask getVibrationIntansity() {
        ParamsTask task = new ParamsTask();
        task.setData(ParamsKeyEnum.KEY_VIBRATION_INTENSITY);
        return task;
    }

    public static OrderTask getVibrationCycle() {
        ParamsTask task = new ParamsTask();
        task.setData(ParamsKeyEnum.KEY_VIBRATION_CYCLE);
        return task;
    }

    public static OrderTask getVibrationDuration() {
        ParamsTask task = new ParamsTask();
        task.setData(ParamsKeyEnum.KEY_VIBRATION_DURATION);
        return task;
    }

    public static OrderTask getWarningRssi() {
        ParamsTask task = new ParamsTask();
        task.setData(ParamsKeyEnum.KEY_WARNING_RSSI);
        return task;
    }

    public static OrderTask getLoRaConnectable() {
        ParamsTask task = new ParamsTask();
        task.setData(ParamsKeyEnum.KEY_NETWORK_STATUS);
        return task;
    }

    public static OrderTask getScanWindow() {
        ParamsTask task = new ParamsTask();
        task.setData(ParamsKeyEnum.KEY_SCAN_WINDOW);
        return task;
    }

    public static OrderTask getConnectable() {
        ParamsTask task = new ParamsTask();
        task.setData(ParamsKeyEnum.KEY_CONNECTABLE);
        return task;
    }

    public static OrderTask getLowBattery() {
        ParamsTask task = new ParamsTask();
        task.setData(ParamsKeyEnum.KEY_LOW_POWER_PERCENT);
        return task;
    }

    public static OrderTask getDeviceInfoInterval() {
        ParamsTask task = new ParamsTask();
        task.setData(ParamsKeyEnum.KEY_DEVICE_INFO_INTERVAL);
        return task;
    }

    public static OrderTask getMacAddress() {
        ParamsTask task = new ParamsTask();
        task.setData(ParamsKeyEnum.KEY_DEVICE_MAC);
        return task;
    }

    public static OrderTask getLBFilterSwitchA() {
        ParamsTask task = new ParamsTask();
        task.setData(ParamsKeyEnum.KEY_LOCATION_FILTER_SWITCH_A);
        return task;
    }

    public static OrderTask getLBFilterRssiA() {
        ParamsTask task = new ParamsTask();
        task.setData(ParamsKeyEnum.KEY_LOCATION_FILTER_RSSI_A);
        return task;
    }

    public static OrderTask getLBFilterMacA() {
        ParamsTask task = new ParamsTask();
        task.setData(ParamsKeyEnum.KEY_LOCATION_FILTER_MAC_A);
        return task;
    }

    public static OrderTask getLBFilterNameA() {
        ParamsTask task = new ParamsTask();
        task.setData(ParamsKeyEnum.KEY_LOCATION_FILTER_ADV_NAME_A);
        return task;
    }

    public static OrderTask getLBFilterUUIDA() {
        ParamsTask task = new ParamsTask();
        task.setData(ParamsKeyEnum.KEY_LOCATION_FILTER_UUID_A);
        return task;
    }

    public static OrderTask getLBFilterAdvRawDataA() {
        ParamsTask task = new ParamsTask();
        task.setData(ParamsKeyEnum.KEY_LOCATION_FILTER_ADV_RAW_DATA_A);
        return task;
    }

    public static OrderTask getLBFilterMajorRangeA() {
        ParamsTask task = new ParamsTask();
        task.setData(ParamsKeyEnum.KEY_LOCATION_FILTER_MAJOR_RANGE_A);
        return task;
    }

    public static OrderTask getLBFilterMinorRangeA() {
        ParamsTask task = new ParamsTask();
        task.setData(ParamsKeyEnum.KEY_LOCATION_FILTER_MINOR_RANGE_A);
        return task;
    }

    public static OrderTask getLBFilterSwitchB() {
        ParamsTask task = new ParamsTask();
        task.setData(ParamsKeyEnum.KEY_LOCATION_FILTER_SWITCH_B);
        return task;
    }

    public static OrderTask getLBFilterRssiB() {
        ParamsTask task = new ParamsTask();
        task.setData(ParamsKeyEnum.KEY_LOCATION_FILTER_RSSI_B);
        return task;
    }

    public static OrderTask getLBFilterMacB() {
        ParamsTask task = new ParamsTask();
        task.setData(ParamsKeyEnum.KEY_LOCATION_FILTER_MAC_B);
        return task;
    }

    public static OrderTask getLBFilterNameB() {
        ParamsTask task = new ParamsTask();
        task.setData(ParamsKeyEnum.KEY_LOCATION_FILTER_ADV_NAME_B);
        return task;
    }

    public static OrderTask getLBFilterUUIDB() {
        ParamsTask task = new ParamsTask();
        task.setData(ParamsKeyEnum.KEY_LOCATION_FILTER_UUID_B);
        return task;
    }

    public static OrderTask getLBFilterAdvRawDataB() {
        ParamsTask task = new ParamsTask();
        task.setData(ParamsKeyEnum.KEY_LOCATION_FILTER_ADV_RAW_DATA_B);
        return task;
    }

    public static OrderTask getLBFilterMajorRangeB() {
        ParamsTask task = new ParamsTask();
        task.setData(ParamsKeyEnum.KEY_LOCATION_FILTER_MAJOR_RANGE_B);
        return task;
    }

    public static OrderTask getLBFilterMinorRangeB() {
        ParamsTask task = new ParamsTask();
        task.setData(ParamsKeyEnum.KEY_LOCATION_FILTER_MINOR_RANGE_B);
        return task;
    }

    public static OrderTask getLBFilterABRelation() {
        ParamsTask task = new ParamsTask();
        task.setData(ParamsKeyEnum.KEY_LOCATION_FILTER_A_B_RELATION);
        return task;
    }

    public static OrderTask getLBFilterRepeat() {
        ParamsTask task = new ParamsTask();
        task.setData(ParamsKeyEnum.KEY_LOCATION_FILTER_REPEAT);
        return task;
    }

    public static OrderTask getCTFilterSwitchA() {
        ParamsTask task = new ParamsTask();
        task.setData(ParamsKeyEnum.KEY_TRACKING_FILTER_SWITCH_A);
        return task;
    }

    public static OrderTask getCTFilterRssiA() {
        ParamsTask task = new ParamsTask();
        task.setData(ParamsKeyEnum.KEY_TRACKING_FILTER_RSSI_A);
        return task;
    }

    public static OrderTask getCTFilterMacA() {
        ParamsTask task = new ParamsTask();
        task.setData(ParamsKeyEnum.KEY_TRACKING_FILTER_MAC_A);
        return task;
    }

    public static OrderTask getCTFilterNameA() {
        ParamsTask task = new ParamsTask();
        task.setData(ParamsKeyEnum.KEY_TRACKING_FILTER_ADV_NAME_A);
        return task;
    }

    public static OrderTask getCTFilterUUIDA() {
        ParamsTask task = new ParamsTask();
        task.setData(ParamsKeyEnum.KEY_TRACKING_FILTER_UUID_A);
        return task;
    }

    public static OrderTask getCTFilterAdvRawDataA() {
        ParamsTask task = new ParamsTask();
        task.setData(ParamsKeyEnum.KEY_TRACKING_FILTER_ADV_RAW_DATA_A);
        return task;
    }

    public static OrderTask getCTFilterMajorRangeA() {
        ParamsTask task = new ParamsTask();
        task.setData(ParamsKeyEnum.KEY_TRACKING_FILTER_MAJOR_RANGE_A);
        return task;
    }

    public static OrderTask getCTFilterMinorRangeA() {
        ParamsTask task = new ParamsTask();
        task.setData(ParamsKeyEnum.KEY_TRACKING_FILTER_MINOR_RANGE_A);
        return task;
    }

    public static OrderTask getCTFilterSwitchB() {
        ParamsTask task = new ParamsTask();
        task.setData(ParamsKeyEnum.KEY_TRACKING_FILTER_SWITCH_B);
        return task;
    }

    public static OrderTask getCTFilterRssiB() {
        ParamsTask task = new ParamsTask();
        task.setData(ParamsKeyEnum.KEY_TRACKING_FILTER_RSSI_B);
        return task;
    }

    public static OrderTask getCTFilterMacB() {
        ParamsTask task = new ParamsTask();
        task.setData(ParamsKeyEnum.KEY_TRACKING_FILTER_MAC_B);
        return task;
    }

    public static OrderTask getCTFilterNameB() {
        ParamsTask task = new ParamsTask();
        task.setData(ParamsKeyEnum.KEY_TRACKING_FILTER_ADV_NAME_B);
        return task;
    }

    public static OrderTask getCTFilterUUIDB() {
        ParamsTask task = new ParamsTask();
        task.setData(ParamsKeyEnum.KEY_TRACKING_FILTER_UUID_B);
        return task;
    }

    public static OrderTask getCTFilterAdvRawDataB() {
        ParamsTask task = new ParamsTask();
        task.setData(ParamsKeyEnum.KEY_TRACKING_FILTER_ADV_RAW_DATA_B);
        return task;
    }

    public static OrderTask getCTFilterMajorRangeB() {
        ParamsTask task = new ParamsTask();
        task.setData(ParamsKeyEnum.KEY_TRACKING_FILTER_MAJOR_RANGE_B);
        return task;
    }

    public static OrderTask getCTFilterMinorRangeB() {
        ParamsTask task = new ParamsTask();
        task.setData(ParamsKeyEnum.KEY_TRACKING_FILTER_MINOR_RANGE_B);
        return task;
    }

    public static OrderTask getCTFilterABRelation() {
        ParamsTask task = new ParamsTask();
        task.setData(ParamsKeyEnum.KEY_TRACKING_FILTER_A_B_RELATION);
        return task;
    }

    public static OrderTask getCTFilterRepeat() {
        ParamsTask task = new ParamsTask();
        task.setData(ParamsKeyEnum.KEY_TRACKING_FILTER_REPEAT);
        return task;
    }

    public static OrderTask getReportLocationBeacons() {
        ParamsTask task = new ParamsTask();
        task.setData(ParamsKeyEnum.KEY_REPORT_LOCATION_BEACONS);
        return task;
    }

    public static OrderTask getTrackingOptionalPayload() {
        ParamsTask task = new ParamsTask();
        task.setData(ParamsKeyEnum.KEY_OPTIONAL_PAYLOAD_TRACKING);
        return task;
    }

    public static OrderTask getLoraMode() {
        ParamsTask task = new ParamsTask();
        task.setData(ParamsKeyEnum.KEY_LORA_MODE);
        return task;
    }

    public static OrderTask getLoraDevEUI() {
        ParamsTask task = new ParamsTask();
        task.setData(ParamsKeyEnum.KEY_LORA_DEV_EUI);
        return task;
    }

    public static OrderTask getLoraAppEUI() {
        ParamsTask task = new ParamsTask();
        task.setData(ParamsKeyEnum.KEY_LORA_APP_EUI);
        return task;
    }

    public static OrderTask getLoraAppKey() {
        ParamsTask task = new ParamsTask();
        task.setData(ParamsKeyEnum.KEY_LORA_APP_KEY);
        return task;
    }

    public static OrderTask getLoraDevAddr() {
        ParamsTask task = new ParamsTask();
        task.setData(ParamsKeyEnum.KEY_LORA_DEV_ADDR);
        return task;
    }

    public static OrderTask getLoraAppSKey() {
        ParamsTask task = new ParamsTask();
        task.setData(ParamsKeyEnum.KEY_LORA_APP_SKEY);
        return task;
    }

    public static OrderTask getLoraNwkSKey() {
        ParamsTask task = new ParamsTask();
        task.setData(ParamsKeyEnum.KEY_LORA_NWK_SKEY);
        return task;
    }

    public static OrderTask getLoraRegion() {
        ParamsTask task = new ParamsTask();
        task.setData(ParamsKeyEnum.KEY_LORA_REGION);
        return task;
    }

    public static OrderTask getLoraReportInterval() {
        ParamsTask task = new ParamsTask();
        task.setData(ParamsKeyEnum.KEY_LORA_REPORT_INTERVAL);
        return task;
    }

    public static OrderTask getLoraMessageType() {
        ParamsTask task = new ParamsTask();
        task.setData(ParamsKeyEnum.KEY_LORA_MESSAGE_TYPE);
        return task;
    }

    public static OrderTask getLoraCH() {
        ParamsTask task = new ParamsTask();
        task.setData(ParamsKeyEnum.KEY_LORA_CH);
        return task;
    }

    public static OrderTask getLoraDutyCycleEnable() {
        ParamsTask task = new ParamsTask();
        task.setData(ParamsKeyEnum.KEY_LORA_DUTY_CYCLE_ENABLE);
        return task;
    }

    public static OrderTask getLoraDR() {
        ParamsTask task = new ParamsTask();
        task.setData(ParamsKeyEnum.KEY_LORA_DR);
        return task;
    }

    public static OrderTask getLoraADR() {
        ParamsTask task = new ParamsTask();
        task.setData(ParamsKeyEnum.KEY_LORA_ADR);
        return task;
    }

    public static OrderTask getLoraUplinkDellTime() {
        ParamsTask task = new ParamsTask();
        task.setData(ParamsKeyEnum.KEY_LORA_UPLINK_DELL_TIME);
        return task;
    }

    public static OrderTask getSOSEnable() {
        ParamsTask task = new ParamsTask();
        task.setData(ParamsKeyEnum.KEY_SOS_ENABLE);
        return task;
    }

    public static OrderTask getSOSReportInterval() {
        ParamsTask task = new ParamsTask();
        task.setData(ParamsKeyEnum.KEY_SOS_REPORT_INTERVAL);
        return task;
    }

    public static OrderTask getSOSOptionalPayload() {
        ParamsTask task = new ParamsTask();
        task.setData(ParamsKeyEnum.KEY_OPTIONAL_PAYLOAD_SOS);
        return task;
    }

    public static OrderTask getGPSEnable() {
        ParamsTask task = new ParamsTask();
        task.setData(ParamsKeyEnum.KEY_GPS_FUNCTION_SWITCH);
        return task;
    }

    public static OrderTask getGPSStatus() {
        ParamsTask task = new ParamsTask();
        task.setData(ParamsKeyEnum.KEY_GPS_FUNCTION_STATUS);
        return task;
    }

    public static OrderTask getGPSReportInterval() {
        ParamsTask task = new ParamsTask();
        task.setData(ParamsKeyEnum.KEY_GPS_REPORT_INTERVAL);
        return task;
    }

    public static OrderTask getGPSSatelliteSearchTime() {
        ParamsTask task = new ParamsTask();
        task.setData(ParamsKeyEnum.KEY_GPS_SATELLITE_SEARCH_TIME);
        return task;
    }

    public static OrderTask getGPSOptionalPayload() {
        ParamsTask task = new ParamsTask();
        task.setData(ParamsKeyEnum.KEY_OPTIONAL_PAYLOAD_GPS);
        return task;
    }

    public static OrderTask get3AxisEnable() {
        ParamsTask task = new ParamsTask();
        task.setData(ParamsKeyEnum.KEY_THREE_AXIS_ENABLE);
        return task;
    }

    public static OrderTask get3AxisDataEnable() {
        ParamsTask task = new ParamsTask();
        task.setData(ParamsKeyEnum.KEY_THREE_AXIS_DATA_ENABLE);
        return task;
    }

    public static OrderTask get3AxisSampleRate() {
        ParamsTask task = new ParamsTask();
        task.setData(ParamsKeyEnum.KEY_THREE_AXIS_SAMPLE_RATE);
        return task;
    }

    public static OrderTask get3AxisG() {
        ParamsTask task = new ParamsTask();
        task.setData(ParamsKeyEnum.KEY_THREE_AXIS_G);
        return task;
    }

    public static OrderTask get3AxisTriggerSensitivity() {
        ParamsTask task = new ParamsTask();
        task.setData(ParamsKeyEnum.KEY_THREE_AXIS_TRIGGER_SENSITIVITY);
        return task;
    }

    public static OrderTask get3AxisReportInterval() {
        ParamsTask task = new ParamsTask();
        task.setData(ParamsKeyEnum.KEY_THREE_AXIS_REPORT_INTERVAL);
        return task;
    }

    public static OrderTask get3AxisOptionalPayload() {
        ParamsTask task = new ParamsTask();
        task.setData(ParamsKeyEnum.KEY_OPTIONAL_PAYLOAD_THREE_AXIS);
        return task;
    }

    public static OrderTask getTimeSyncInterval() {
        ParamsTask task = new ParamsTask();
        task.setData(ParamsKeyEnum.KEY_TIME_SYNC_INTERVAL);
        return task;
    }

    public static OrderTask getNetworkInterval() {
        ParamsTask task = new ParamsTask();
        task.setData(ParamsKeyEnum.KEY_NETWORK_CHECK_INTERVAL);
        return task;
    }

    public static OrderTask getReportLocationEnable() {
        ParamsTask task = new ParamsTask();
        task.setData(ParamsKeyEnum.KEY_REPORT_LOCATION_ENABLE);
        return task;
    }


    ///////////////////////////////////////////////////////////////////////////
    // WRITE
    ///////////////////////////////////////////////////////////////////////////

    public static ParamsTask setWriteConfig(ParamsKeyEnum configKeyEnum) {
        ParamsTask writeConfigTask = new ParamsTask();
        writeConfigTask.setData(configKeyEnum);
        return writeConfigTask;
    }

    public static OrderTask setPassword(String password) {
        SetPasswordTask setPasswordTask = new SetPasswordTask();
        setPasswordTask.setData(password);
        return setPasswordTask;
    }

    public static ParamsTask setTime() {
        ParamsTask writeConfigTask = new ParamsTask();
        writeConfigTask.setTime();
        return writeConfigTask;
    }

    public static OrderTask setDeviceName(String deviceName) {
        ParamsTask writeConfigTask = new ParamsTask();
        writeConfigTask.setAdvName(deviceName);
        return writeConfigTask;
    }

    public static OrderTask setUUID(String uuid) {
        ParamsTask writeConfigTask = new ParamsTask();
        writeConfigTask.setUUID(uuid);
        return writeConfigTask;
    }

    public static OrderTask setMajor(int major) {
        ParamsTask writeConfigTask = new ParamsTask();
        writeConfigTask.setMajor(major);
        return writeConfigTask;
    }

    public static OrderTask setMinor(int minor) {
        ParamsTask writeConfigTask = new ParamsTask();
        writeConfigTask.setMinor(minor);
        return writeConfigTask;
    }

    public static OrderTask setAdvInterval(int advInterval) {
        ParamsTask writeConfigTask = new ParamsTask();
        writeConfigTask.setAdvInterval(advInterval);
        return writeConfigTask;
    }


    public static OrderTask setTransmission(int transmission) {
        ParamsTask writeConfigTask = new ParamsTask();
        writeConfigTask.setTransmission(transmission);
        return writeConfigTask;
    }

    public static OrderTask setMeasurePower(int measurePower) {
        ParamsTask writeConfigTask = new ParamsTask();
        writeConfigTask.setMeasurePower(measurePower);
        return writeConfigTask;
    }

    public static ParamsTask setFilterValidInterval(int seconds) {
        ParamsTask writeConfigTask = new ParamsTask();
        writeConfigTask.setFilterValidInterval(seconds);
        return writeConfigTask;
    }

    public static ParamsTask setAlarmNotify(int notify) {
        ParamsTask writeConfigTask = new ParamsTask();
        writeConfigTask.setAlarmNotify(notify);
        return writeConfigTask;
    }

    public static ParamsTask setWarningRssi(int rssi) {
        ParamsTask writeConfigTask = new ParamsTask();
        writeConfigTask.setWarningRssi(rssi);
        return writeConfigTask;
    }

    public static ParamsTask setAlarmTriggerRssi(int rssi) {
        ParamsTask writeConfigTask = new ParamsTask();
        writeConfigTask.setAlarmTirggerRssi(rssi);
        return writeConfigTask;
    }

    public static OrderTask setConnectionMode(int connectionMode) {
        ParamsTask writeConfigTask = new ParamsTask();
        writeConfigTask.setConnectable(connectionMode);
        return writeConfigTask;
    }

    public static OrderTask setLowBattery(int lowBattery) {
        ParamsTask writeConfigTask = new ParamsTask();
        writeConfigTask.setLowBattery(lowBattery);
        return writeConfigTask;
    }

    public static OrderTask setDeviceInfoInterval(int interval) {
        ParamsTask writeConfigTask = new ParamsTask();
        writeConfigTask.setDeviceInfoInterval(interval);
        return writeConfigTask;
    }

    public static OrderTask changePassword(String password) {
        ParamsTask writeConfigTask = new ParamsTask();
        writeConfigTask.changePassword(password);
        return writeConfigTask;
    }

    public static OrderTask setReset() {
        ParamsTask writeConfigTask = new ParamsTask();
        writeConfigTask.reset();
        return writeConfigTask;
    }

    public static OrderTask setScanWindow(int scannerState, int startTime) {
        ParamsTask writeConfigTask = new ParamsTask();
        writeConfigTask.setScanWinow(scannerState, startTime);
        return writeConfigTask;
    }

    public static OrderTask closePower() {
        ParamsTask task = new ParamsTask();
        task.setClosePower();
        return task;
    }

    public static OrderTask setLBFilterRssiA(int rssi) {
        ParamsTask task = new ParamsTask();
        task.setLBFilterRssiA(rssi);
        return task;
    }

    public static OrderTask setLBFilterMacA(String mac, boolean isReverse) {
        ParamsTask task = new ParamsTask();
        task.setLBFilterMacA(mac, isReverse);
        return task;
    }

    public static OrderTask setLBFilterNameA(String name, boolean isReverse) {
        ParamsTask task = new ParamsTask();
        task.setLBFilterNameA(name, isReverse);
        return task;
    }

    public static OrderTask setLBFilterUUIDA(String uuid, boolean isReverse) {
        ParamsTask task = new ParamsTask();
        task.setLBFilterUUIDA(uuid, isReverse);
        return task;
    }

    public static OrderTask setLBFilterAdvRawDataA(ArrayList<String> filterRawDatas, boolean isReverse) {
        ParamsTask task = new ParamsTask();
        task.setLBFilterRawDataA(filterRawDatas, isReverse);
        return task;
    }

    public static OrderTask setLBFilterMajorRangeA(int enable, int majorMin, int majorMax, boolean isReverse) {
        ParamsTask task = new ParamsTask();
        task.setLBFilterMajorRangeA(enable, majorMin, majorMax, isReverse);
        return task;
    }

    public static OrderTask setLBFilterMinorRangeA(int enable, int majorMin, int majorMax, boolean isReverse) {
        ParamsTask task = new ParamsTask();
        task.setLBFilterMinorRangeA(enable, majorMin, majorMax, isReverse);
        return task;
    }

    public static OrderTask setLBFilterSwitchA(int enable) {
        ParamsTask task = new ParamsTask();
        task.setLBFilterSwitchA(enable);
        return task;
    }

    public static OrderTask setLBFilterRssiB(int rssi) {
        ParamsTask task = new ParamsTask();
        task.setLBFilterRssiB(rssi);
        return task;
    }

    public static OrderTask setLBFilterMacB(String mac, boolean isReverse) {
        ParamsTask task = new ParamsTask();
        task.setLBFilterMacB(mac, isReverse);
        return task;
    }

    public static OrderTask setLBFilterNameB(String name, boolean isReverse) {
        ParamsTask task = new ParamsTask();
        task.setLBFilterNameB(name, isReverse);
        return task;
    }

    public static OrderTask setLBFilterUUIDB(String uuid, boolean isReverse) {
        ParamsTask task = new ParamsTask();
        task.setLBFilterUUIDB(uuid, isReverse);
        return task;
    }

    public static OrderTask setLBFilterAdvRawDataB(ArrayList<String> filterRawDatas, boolean isReverse) {
        ParamsTask task = new ParamsTask();
        task.setLBFilterRawDataB(filterRawDatas, isReverse);
        return task;
    }

    public static OrderTask setLBFilterMajorRangeB(int enable, int majorMin, int majorMax, boolean isReverse) {
        ParamsTask task = new ParamsTask();
        task.setLBFilterMajorRangeB(enable, majorMin, majorMax, isReverse);
        return task;
    }

    public static OrderTask setLBFilterMinorRangeB(int enable, int majorMin, int majorMax, boolean isReverse) {
        ParamsTask task = new ParamsTask();
        task.setLBFilterMinorRangeB(enable, majorMin, majorMax, isReverse);
        return task;
    }

    public static OrderTask setLBFilterSwitchB(int enable) {
        ParamsTask task = new ParamsTask();
        task.setLBFilterSwitchB(enable);
        return task;
    }

    public static OrderTask setLBFilterABRelation(int relation) {
        ParamsTask task = new ParamsTask();
        task.setLBFilterABRelation(relation);
        return task;
    }

    public static OrderTask setLBFilterRepeat(int relation) {
        ParamsTask task = new ParamsTask();
        task.setLBFilterRepeat(relation);
        return task;
    }

    public static OrderTask setCTFilterRssiA(int rssi) {
        ParamsTask task = new ParamsTask();
        task.setCTFilterRssiA(rssi);
        return task;
    }

    public static OrderTask setCTFilterMacA(String mac, boolean isReverse) {
        ParamsTask task = new ParamsTask();
        task.setCTFilterMacA(mac, isReverse);
        return task;
    }

    public static OrderTask setCTFilterNameA(String name, boolean isReverse) {
        ParamsTask task = new ParamsTask();
        task.setCTFilterNameA(name, isReverse);
        return task;
    }

    public static OrderTask setCTFilterUUIDA(String uuid, boolean isReverse) {
        ParamsTask task = new ParamsTask();
        task.setCTFilterUUIDA(uuid, isReverse);
        return task;
    }

    public static OrderTask setCTFilterAdvRawDataA(ArrayList<String> filterRawDatas, boolean isReverse) {
        ParamsTask task = new ParamsTask();
        task.setCTFilterRawDataA(filterRawDatas, isReverse);
        return task;
    }

    public static OrderTask setCTFilterMajorRangeA(int enable, int majorMin, int majorMax, boolean isReverse) {
        ParamsTask task = new ParamsTask();
        task.setCTFilterMajorRangeA(enable, majorMin, majorMax, isReverse);
        return task;
    }

    public static OrderTask setCTFilterMinorRangeA(int enable, int majorMin, int majorMax, boolean isReverse) {
        ParamsTask task = new ParamsTask();
        task.setCTFilterMinorRangeA(enable, majorMin, majorMax, isReverse);
        return task;
    }

    public static OrderTask setCTFilterSwitchA(int enable) {
        ParamsTask task = new ParamsTask();
        task.setCTFilterSwitchA(enable);
        return task;
    }

    public static OrderTask setCTFilterRssiB(int rssi) {
        ParamsTask task = new ParamsTask();
        task.setCTFilterRssiB(rssi);
        return task;
    }

    public static OrderTask setCTFilterMacB(String mac, boolean isReverse) {
        ParamsTask task = new ParamsTask();
        task.setCTFilterMacB(mac, isReverse);
        return task;
    }

    public static OrderTask setCTFilterNameB(String name, boolean isReverse) {
        ParamsTask task = new ParamsTask();
        task.setCTFilterNameB(name, isReverse);
        return task;
    }

    public static OrderTask setCTFilterUUIDB(String uuid, boolean isReverse) {
        ParamsTask task = new ParamsTask();
        task.setCTFilterUUIDB(uuid, isReverse);
        return task;
    }

    public static OrderTask setCTFilterAdvRawDataB(ArrayList<String> filterRawDatas, boolean isReverse) {
        ParamsTask task = new ParamsTask();
        task.setCTFilterRawDataB(filterRawDatas, isReverse);
        return task;
    }

    public static OrderTask setCTFilterMajorRangeB(int enable, int majorMin, int majorMax, boolean isReverse) {
        ParamsTask task = new ParamsTask();
        task.setCTFilterMajorRangeB(enable, majorMin, majorMax, isReverse);
        return task;
    }

    public static OrderTask setCTFilterMinorRangeB(int enable, int majorMin, int majorMax, boolean isReverse) {
        ParamsTask task = new ParamsTask();
        task.setCTFilterMinorRangeB(enable, majorMin, majorMax, isReverse);
        return task;
    }

    public static OrderTask setCTFilterSwitchB(int enable) {
        ParamsTask task = new ParamsTask();
        task.setCTFilterSwitchB(enable);
        return task;
    }

    public static OrderTask setCTFilterABRelation(int relation) {
        ParamsTask task = new ParamsTask();
        task.setCTFilterABRelation(relation);
        return task;
    }

    public static OrderTask setCTFilterRepeat(int relation) {
        ParamsTask task = new ParamsTask();
        task.setCTFilterRepeat(relation);
        return task;
    }

    public static OrderTask setTrackingOptionPayload(int payload) {
        ParamsTask task = new ParamsTask();
        task.setTrackingOptionPayload(payload);
        return task;
    }

    public static OrderTask setReportLocationBeacons(int num) {
        ParamsTask task = new ParamsTask();
        task.setReportLocationBeacons(num);
        return task;
    }

    public static OrderTask setLoraDevAddr(String devAddr) {
        ParamsTask task = new ParamsTask();
        task.setLoraDevAddr(devAddr);
        return task;
    }

    public static OrderTask setLoraNwkSKey(String nwkSKey) {
        ParamsTask task = new ParamsTask();
        task.setLoraNwkSKey(nwkSKey);
        return task;
    }

    public static OrderTask setLoraAppSKey(String appSKey) {
        ParamsTask task = new ParamsTask();
        task.setLoraAppSKey(appSKey);
        return task;
    }

    public static OrderTask setLoraAppEui(String appEui) {
        ParamsTask task = new ParamsTask();
        task.setLoraAppEui(appEui);
        return task;
    }

    public static OrderTask setLoraDevEui(String devEui) {
        ParamsTask task = new ParamsTask();
        task.setLoraDevEui(devEui);
        return task;
    }

    public static OrderTask setLoraAppKey(String appKey) {
        ParamsTask task = new ParamsTask();
        task.setLoraAppKey(appKey);
        return task;
    }

    public static OrderTask setLoraUploadMode(int mode) {
        ParamsTask task = new ParamsTask();
        task.setLoraUploadMode(mode);
        return task;
    }

    public static OrderTask setLoraUploadInterval(int interval) {
        ParamsTask task = new ParamsTask();
        task.setLoraUploadInterval(interval);
        return task;
    }

    public static OrderTask setLoraMessageType(int type) {
        ParamsTask task = new ParamsTask();
        task.setLoraMessageType(type);
        return task;
    }

    public static OrderTask setLoraRegion(int region) {
        ParamsTask task = new ParamsTask();
        task.setLoraRegion(region);
        return task;
    }

    public static OrderTask setLoraCH(int ch1, int ch2) {
        ParamsTask task = new ParamsTask();
        task.setLoraCH(ch1, ch2);
        return task;
    }

    public static OrderTask setLoraDutyCycleEnable(int enable) {
        ParamsTask task = new ParamsTask();
        task.setLoraDutyCycleEnable(enable);
        return task;
    }

    public static OrderTask setLoraDR(int dr1) {
        ParamsTask task = new ParamsTask();
        task.setLoraDR(dr1);
        return task;
    }

    public static OrderTask setLoraADR(int adr) {
        ParamsTask task = new ParamsTask();
        task.setLoraADR(adr);
        return task;
    }

    public static OrderTask setLoraUplinkDellTime(int uplinkDellTime) {
        ParamsTask task = new ParamsTask();
        task.setLoraUplinkDellTime(uplinkDellTime);
        return task;
    }

    public static OrderTask setLoraConnect() {
        ParamsTask task = new ParamsTask();
        task.setLoraConnect();
        return task;
    }

    public static OrderTask setVibrationIntensity(int intensity) {
        ParamsTask task = new ParamsTask();
        task.setVibrationIntensity(intensity);
        return task;
    }

    public static OrderTask setVibrationDuration(int duration) {
        ParamsTask task = new ParamsTask();
        task.setVibrationDuration(duration);
        return task;
    }

    public static OrderTask setVibrationCycle(int cycle) {
        ParamsTask task = new ParamsTask();
        task.setVibrationCycle(cycle);
        return task;
    }

    public static OrderTask setSOSEnable(int enable) {
        ParamsTask task = new ParamsTask();
        task.setSOSEnable(enable);
        return task;
    }

    public static OrderTask setSOSReportInterval(int interval) {
        ParamsTask task = new ParamsTask();
        task.setSOSReportInterval(interval);
        return task;
    }

    public static OrderTask setSOSOptionalPayload(int payload) {
        ParamsTask task = new ParamsTask();
        task.setSOSOptionalPayload(payload);
        return task;
    }

    public static OrderTask setGPSEnable(int enable) {
        ParamsTask task = new ParamsTask();
        task.setGPSEnable(enable);
        return task;
    }

    public static OrderTask setGPSReportInterval(int interval) {
        ParamsTask task = new ParamsTask();
        task.setGPSReportInterval(interval);
        return task;
    }

    public static OrderTask setGPSSearchTime(int interval) {
        ParamsTask task = new ParamsTask();
        task.setGPSSearchTime(interval);
        return task;
    }

    public static OrderTask setGPSOptionalPayload(int payload) {
        ParamsTask task = new ParamsTask();
        task.setGPSOptionalPayload(payload);
        return task;
    }

    public static OrderTask set3AxisEnable(int enable) {
        ParamsTask task = new ParamsTask();
        task.set3AxisEnable(enable);
        return task;
    }

    public static OrderTask set3AxisDataEnable(int enable) {
        ParamsTask task = new ParamsTask();
        task.set3AxisDataEnable(enable);
        return task;
    }

    public static OrderTask set3AxisSampleRate(int sampleRate) {
        ParamsTask task = new ParamsTask();
        task.set3AxisSampleRate(sampleRate);
        return task;
    }

    public static OrderTask set3AxisG(int g) {
        ParamsTask task = new ParamsTask();
        task.set3AxisG(g);
        return task;
    }

    public static OrderTask set3AxisTriggerSensitivity(int triggerSensitivity) {
        ParamsTask task = new ParamsTask();
        task.set3AxisTriggerSensitivity(triggerSensitivity);
        return task;
    }

    public static OrderTask set3AxisReportInterval(int reportInterval) {
        ParamsTask task = new ParamsTask();
        task.set3AxisReportInterval(reportInterval);
        return task;
    }

    public static OrderTask set3AxisOptionalPayload(int payload) {
        ParamsTask task = new ParamsTask();
        task.set3AxisOptionalPayload(payload);
        return task;
    }

    public static OrderTask setTimeSyncInterval(int timeSyncInterval) {
        ParamsTask task = new ParamsTask();
        task.setTimeSyncInterval(timeSyncInterval);
        return task;
    }

    public static OrderTask setNetworkCheckInterval(int interval) {
        ParamsTask task = new ParamsTask();
        task.setNetworkCheckInterval(interval);
        return task;
    }

    public static OrderTask setReportLocationEnable(int interval) {
        ParamsTask task = new ParamsTask();
        task.setReportLocationEnable(interval);
        return task;
    }
}
