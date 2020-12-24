package com.moko.support.task;

import androidx.annotation.IntRange;
import android.text.TextUtils;

import com.moko.support.entity.ConfigKeyEnum;
import com.moko.support.entity.OrderType;
import com.moko.support.utils.MokoUtils;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * @Date 2018/1/20
 * @Author wenzheng.liu
 * @Description
 * @ClassPath com.moko.support.task.WriteConfigTask
 */
public class WriteConfigTask extends OrderTask {
    public byte[] data;

    public WriteConfigTask() {
        super(OrderType.WRITE_CONFIG, OrderTask.RESPONSE_TYPE_WRITE_NO_RESPONSE);
    }

    @Override
    public byte[] assemble() {
        return data;
    }

    public void setData(ConfigKeyEnum key) {
        switch (key) {
            case KEY_ADV_NAME:
            case KEY_IBEACON_UUID:
            case KEY_IBEACON_MAJOR:
            case KEY_IBEACON_MINOR:
            case KEY_ADV_INTERVAL:
            case KEY_MEASURE_POWER:
            case KEY_TRANSMISSION:
            case KEY_SCAN_INTERVAL:
            case KEY_ALARM_NOTIFY:
            case KEY_ALARM_RSSI:
            case KEY_LORA_CONNECTABLE:
            case KEY_SCAN_WINDOW:
            case KEY_CONNECTABLE:
            case KEY_BATTERY:
            case KEY_DEVICE_MAC:

            case KEY_FILTER_RSSI_A:
            case KEY_FILTER_MAC_A:
            case KEY_FILTER_ADV_NAME_A:
            case KEY_FILTER_UUID_A:
            case KEY_FILTER_MAJOR_RANGE_A:
            case KEY_FILTER_MINOR_RANGE_A:

            case KEY_FILTER_RSSI_B:
            case KEY_FILTER_MAC_B:
            case KEY_FILTER_ADV_NAME_B:
            case KEY_FILTER_UUID_B:
            case KEY_FILTER_MAJOR_RANGE_B:
            case KEY_FILTER_MINOR_RANGE_B:

            case KEY_FILTER_SWITCH_A:
            case KEY_FILTER_SWITCH_B:
            case KEY_FILTER_A_B_RELATION:

            case KEY_LORA_MODE:
            case KEY_LORA_DEV_EUI:
            case KEY_LORA_APP_EUI:
            case KEY_LORA_APP_KEY:
            case KEY_LORA_DEV_ADDR:
            case KEY_LORA_APP_SKEY:
            case KEY_LORA_NWK_SKEY:
            case KEY_LORA_REGION:
            case KEY_LORA_REPORT_INTERVAL:
            case KEY_LORA_MESSAGE_TYPE:
            case KEY_LORA_CH:
            case KEY_LORA_DR:
            case KEY_LORA_ADR:
            case KEY_VIBRATION_INTENSITY:
            case KEY_VIBRATION_CYCLE:
            case KEY_VIBRATION_DURATION:
            case KEY_WARNING_RSSI:
            case KEY_LOW_POWER_PERCENT:
            case KEY_DEVICE_INFO_INTERVAL:
                createGetConfigData(key.getConfigKey());
                break;
        }
    }

    private void createGetConfigData(int configKey) {
        data = new byte[]{
                (byte) 0xED,
                (byte) 0x00,
                (byte) configKey,
                (byte) 0x00
        };
    }

    public void setScanInterval(@IntRange(from = 1, to = 600) int seconds) {
        byte[] intervalBytes = MokoUtils.toByteArray(seconds, 2);
        data = new byte[]{
                (byte) 0xED,
                (byte) 0x01,
                (byte) ConfigKeyEnum.KEY_SCAN_INTERVAL.getConfigKey(),
                (byte) 0x02,
                intervalBytes[0],
                intervalBytes[1]
        };
    }

    public void setAlarmNotify(@IntRange(from = 0, to = 3) int notify) {
        data = new byte[]{
                (byte) 0xED,
                (byte) 0x01,
                (byte) ConfigKeyEnum.KEY_ALARM_NOTIFY.getConfigKey(),
                (byte) 0x01,
                (byte) notify
        };
    }

    public void setAlarmTirggerRssi(@IntRange(from = -127, to = 0) int rssi) {
        data = new byte[]{
                (byte) 0xED,
                (byte) 0x01,
                (byte) ConfigKeyEnum.KEY_ALARM_RSSI.getConfigKey(),
                (byte) 0x01,
                (byte) rssi,
        };

    }

    public void setTime() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int date = calendar.get(Calendar.DATE);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        int second = calendar.get(Calendar.SECOND);
        byte[] yearBytes = MokoUtils.toByteArray(year, 2);
        data = new byte[]{
                (byte) 0xED,
                (byte) 0x01,
                (byte) ConfigKeyEnum.KEY_TIME.getConfigKey(),
                (byte) 0x07,
                yearBytes[0],
                yearBytes[1],
                (byte) month,
                (byte) date,
                (byte) hour,
                (byte) minute,
                (byte) second,
        };
    }

    public void setFilterRssiA(@IntRange(from = -127, to = 0) int rssi) {
        data = new byte[]{
                (byte) 0xED,
                (byte) 0x01,
                (byte) ConfigKeyEnum.KEY_FILTER_RSSI_A.getConfigKey(),
                (byte) 0x01,
                (byte) rssi
        };
    }

    public void setFilterMacA(String mac, boolean isReverse) {
        if (TextUtils.isEmpty(mac)) {
            data = new byte[]{
                    (byte) 0xED,
                    (byte) 0x01,
                    (byte) ConfigKeyEnum.KEY_FILTER_MAC_A.getConfigKey(),
                    (byte) 0x01,
                    (byte) 0x00,
            };

        } else {
            byte[] macBytes = MokoUtils.hex2bytes(mac);
            int length = macBytes.length + 1;
            data = new byte[4 + length];
            data[0] = (byte) 0xED;
            data[1] = (byte) 0x01;
            data[2] = (byte) ConfigKeyEnum.KEY_FILTER_MAC_A.getConfigKey();
            data[3] = (byte) length;
            data[4] = (byte) (isReverse ? 0x02 : 0x01);
            for (int i = 0; i < macBytes.length; i++) {
                data[5 + i] = macBytes[i];
            }
        }
    }

    public void setFilterNameA(String name, boolean isReverse) {
        if (TextUtils.isEmpty(name)) {
            data = new byte[]{
                    (byte) 0xED,
                    (byte) 0x01,
                    (byte) ConfigKeyEnum.KEY_FILTER_ADV_NAME_A.getConfigKey(),
                    (byte) 0x01,
                    (byte) 0x00,
            };
        } else {
            byte[] nameBytes = name.getBytes();
            int length = nameBytes.length + 1;
            data = new byte[4 + length];
            data[0] = (byte) 0xED;
            data[1] = (byte) 0x01;
            data[2] = (byte) ConfigKeyEnum.KEY_FILTER_ADV_NAME_A.getConfigKey();
            data[3] = (byte) length;
            data[4] = (byte) (isReverse ? 0x02 : 0x01);
            for (int i = 0; i < nameBytes.length; i++) {
                data[5 + i] = nameBytes[i];
            }
        }
    }

    public void setFilterUUIDA(String uuid, boolean isReverse) {
        if (TextUtils.isEmpty(uuid)) {
            data = new byte[]{
                    (byte) 0xED,
                    (byte) 0x01,
                    (byte) ConfigKeyEnum.KEY_FILTER_UUID_A.getConfigKey(),
                    (byte) 0x01,
                    (byte) 0x00,
            };
        } else {
            byte[] uuidBytes = MokoUtils.hex2bytes(uuid);
            int length = uuidBytes.length + 1;
            data = new byte[4 + length];
            data[0] = (byte) 0xED;
            data[1] = (byte) 0x01;
            data[2] = (byte) ConfigKeyEnum.KEY_FILTER_UUID_A.getConfigKey();
            data[3] = (byte) length;
            data[4] = (byte) (isReverse ? 0x02 : 0x01);
            for (int i = 0; i < uuidBytes.length; i++) {
                data[5 + i] = uuidBytes[i];
            }
        }
    }

    public void setFilterMajorRangeA(@IntRange(from = 0, to = 1) int enable,
                                     @IntRange(from = 0, to = 65535) int majorMin,
                                     @IntRange(from = 0, to = 65535) int majorMax,
                                     boolean isReverse) {
        if (enable == 0) {
            data = new byte[]{
                    (byte) 0xED,
                    (byte) 0x01,
                    (byte) ConfigKeyEnum.KEY_FILTER_MAJOR_RANGE_A.getConfigKey(),
                    (byte) 0x01,
                    (byte) 0x00,
            };
        } else {
            byte[] majorMinBytes = MokoUtils.toByteArray(majorMin, 2);
            byte[] majorMaxBytes = MokoUtils.toByteArray(majorMax, 2);
            data = new byte[]{
                    (byte) 0xED,
                    (byte) 0x01,
                    (byte) ConfigKeyEnum.KEY_FILTER_MAJOR_RANGE_A.getConfigKey(),
                    (byte) 0x05,
                    (byte) (isReverse ? 0x02 : 0x01),
                    majorMinBytes[0],
                    majorMinBytes[1],
                    majorMaxBytes[0],
                    majorMaxBytes[1],
            };
        }
    }

    public void setFilterMinorRangeA(@IntRange(from = 0, to = 1) int enable,
                                     @IntRange(from = 0, to = 65535) int minorMin,
                                     @IntRange(from = 0, to = 65535) int minorMax,
                                     boolean isReverse) {
        if (enable == 0) {
            data = new byte[]{
                    (byte) 0xED,
                    (byte) 0x01,
                    (byte) ConfigKeyEnum.KEY_FILTER_MINOR_RANGE_A.getConfigKey(),
                    (byte) 0x01,
                    (byte) 0x00,
            };
        } else {
            byte[] minorMinBytes = MokoUtils.toByteArray(minorMin, 2);
            byte[] minorMaxBytes = MokoUtils.toByteArray(minorMax, 2);
            data = new byte[]{
                    (byte) 0xED,
                    (byte) 0x01,
                    (byte) ConfigKeyEnum.KEY_FILTER_MINOR_RANGE_A.getConfigKey(),
                    (byte) 0x05,
                    (byte) (isReverse ? 0x02 : 0x01),
                    minorMinBytes[0],
                    minorMinBytes[1],
                    minorMaxBytes[0],
                    minorMaxBytes[1],
            };
        }
    }

    public void setFilterRawDataA(ArrayList<String> filterRawDatas, boolean isReverse) {
        if (filterRawDatas == null || filterRawDatas.size() == 0) {
            data = new byte[]{
                    (byte) 0xED,
                    (byte) 0x01,
                    (byte) ConfigKeyEnum.KEY_FILTER_ADV_RAW_DATA_A.getConfigKey(),
                    (byte) 0x01,
                    (byte) 0x00,
            };
        } else {
            StringBuffer stringBuffer = new StringBuffer();
            for (String rawData : filterRawDatas) {
                stringBuffer.append(rawData);
            }
            byte[] mRawDatas = MokoUtils.hex2bytes(stringBuffer.toString());
            final int length = mRawDatas.length + 1;
            data = new byte[length + 4];
            data[0] = (byte) 0xED;
            data[1] = (byte) 0x01;
            data[2] = (byte) ConfigKeyEnum.KEY_FILTER_ADV_RAW_DATA_A.getConfigKey();
            data[3] = (byte) length;
            data[4] = (byte) (isReverse ? 0x02 : 0x01);
            for (int i = 0; i < mRawDatas.length; i++) {
                data[5 + i] = mRawDatas[i];
            }
        }
    }

    public void setFilterSwitchA(@IntRange(from = 0, to = 1) int enable) {
        data = new byte[]{
                (byte) 0xED,
                (byte) 0x01,
                (byte) ConfigKeyEnum.KEY_FILTER_SWITCH_A.getConfigKey(),
                (byte) 0x01,
                (byte) enable,
        };
    }

    public void setFilterRssiB(@IntRange(from = -127, to = 0) int rssi) {
        data = new byte[]{
                (byte) 0xED,
                (byte) 0x01,
                (byte) ConfigKeyEnum.KEY_FILTER_RSSI_B.getConfigKey(),
                (byte) 0x01,
                (byte) rssi
        };
    }

    public void setFilterMacB(String mac, boolean isReverse) {
        if (TextUtils.isEmpty(mac)) {
            data = new byte[]{
                    (byte) 0xED,
                    (byte) 0x01,
                    (byte) ConfigKeyEnum.KEY_FILTER_MAC_B.getConfigKey(),
                    (byte) 0x01,
                    (byte) 0x00,
            };

        } else {
            byte[] macBytes = MokoUtils.hex2bytes(mac);
            int length = macBytes.length + 1;
            data = new byte[4 + length];
            data[0] = (byte) 0xED;
            data[1] = (byte) 0x01;
            data[2] = (byte) ConfigKeyEnum.KEY_FILTER_MAC_B.getConfigKey();
            data[3] = (byte) length;
            data[4] = (byte) (isReverse ? 0x02 : 0x01);
            for (int i = 0; i < macBytes.length; i++) {
                data[5 + i] = macBytes[i];
            }
        }
    }

    public void setFilterNameB(String name, boolean isReverse) {
        if (TextUtils.isEmpty(name)) {
            data = new byte[]{
                    (byte) 0xED,
                    (byte) 0x01,
                    (byte) ConfigKeyEnum.KEY_FILTER_ADV_NAME_B.getConfigKey(),
                    (byte) 0x01,
                    (byte) 0x00,
            };
        } else {
            byte[] nameBytes = name.getBytes();
            int length = nameBytes.length + 1;
            data = new byte[4 + length];
            data[0] = (byte) 0xED;
            data[1] = (byte) 0x01;
            data[2] = (byte) ConfigKeyEnum.KEY_FILTER_ADV_NAME_B.getConfigKey();
            data[3] = (byte) length;
            data[4] = (byte) (isReverse ? 0x02 : 0x01);
            for (int i = 0; i < nameBytes.length; i++) {
                data[5 + i] = nameBytes[i];
            }
        }
    }

    public void setFilterUUIDB(String uuid, boolean isReverse) {
        if (TextUtils.isEmpty(uuid)) {
            data = new byte[]{
                    (byte) 0xED,
                    (byte) 0x01,
                    (byte) ConfigKeyEnum.KEY_FILTER_UUID_B.getConfigKey(),
                    (byte) 0x01,
                    (byte) 0x00,
            };
        } else {
            byte[] uuidBytes = MokoUtils.hex2bytes(uuid);
            int length = uuidBytes.length + 1;
            data = new byte[4 + length];
            data[0] = (byte) 0xED;
            data[1] = (byte) 0x01;
            data[2] = (byte) ConfigKeyEnum.KEY_FILTER_UUID_B.getConfigKey();
            data[3] = (byte) length;
            data[4] = (byte) (isReverse ? 0x02 : 0x01);
            for (int i = 0; i < uuidBytes.length; i++) {
                data[5 + i] = uuidBytes[i];
            }
        }
    }

    public void setFilterMajorRangeB(@IntRange(from = 0, to = 1) int enable,
                                     @IntRange(from = 0, to = 65535) int majorMin,
                                     @IntRange(from = 0, to = 65535) int majorMax,
                                     boolean isReverse) {
        if (enable == 0) {
            data = new byte[]{
                    (byte) 0xED,
                    (byte) 0x01,
                    (byte) ConfigKeyEnum.KEY_FILTER_MAJOR_RANGE_B.getConfigKey(),
                    (byte) 0x01,
                    (byte) 0x00,
            };
        } else {
            byte[] majorMinBytes = MokoUtils.toByteArray(majorMin, 2);
            byte[] majorMaxBytes = MokoUtils.toByteArray(majorMax, 2);
            data = new byte[]{
                    (byte) 0xED,
                    (byte) 0x01,
                    (byte) ConfigKeyEnum.KEY_FILTER_MAJOR_RANGE_B.getConfigKey(),
                    (byte) 0x05,
                    (byte) (isReverse ? 0x02 : 0x01),
                    majorMinBytes[0],
                    majorMinBytes[1],
                    majorMaxBytes[0],
                    majorMaxBytes[1],
            };
        }
    }

    public void setFilterMinorRangeB(@IntRange(from = 0, to = 1) int enable,
                                     @IntRange(from = 0, to = 65535) int minorMin,
                                     @IntRange(from = 0, to = 65535) int minorMax,
                                     boolean isReverse) {
        if (enable == 0) {
            data = new byte[]{
                    (byte) 0xED,
                    (byte) 0x01,
                    (byte) ConfigKeyEnum.KEY_FILTER_MINOR_RANGE_B.getConfigKey(),
                    (byte) 0x01,
                    (byte) 0x00,
            };
        } else {
            byte[] minorMinBytes = MokoUtils.toByteArray(minorMin, 2);
            byte[] minorMaxBytes = MokoUtils.toByteArray(minorMax, 2);
            data = new byte[]{
                    (byte) 0xED,
                    (byte) 0x01,
                    (byte) ConfigKeyEnum.KEY_FILTER_MINOR_RANGE_B.getConfigKey(),
                    (byte) 0x05,
                    (byte) (isReverse ? 0x02 : 0x01),
                    minorMinBytes[0],
                    minorMinBytes[1],
                    minorMaxBytes[0],
                    minorMaxBytes[1],
            };
        }
    }

    public void setFilterRawDataB(ArrayList<String> filterRawDatas, boolean isReverse) {
        if (filterRawDatas == null || filterRawDatas.size() == 0) {
            data = new byte[]{
                    (byte) 0xED,
                    (byte) 0x01,
                    (byte) ConfigKeyEnum.KEY_FILTER_ADV_RAW_DATA_B.getConfigKey(),
                    (byte) 0x01,
                    (byte) 0x00,
            };
        } else {
            StringBuffer stringBuffer = new StringBuffer();
            for (String rawData : filterRawDatas) {
                stringBuffer.append(rawData);
            }
            byte[] mRawDatas = MokoUtils.hex2bytes(stringBuffer.toString());
            final int length = mRawDatas.length + 1;
            data = new byte[length + 4];
            data[0] = (byte) 0xED;
            data[1] = (byte) 0x01;
            data[2] = (byte) ConfigKeyEnum.KEY_FILTER_ADV_RAW_DATA_B.getConfigKey();
            data[3] = (byte) length;
            data[4] = (byte) (isReverse ? 0x02 : 0x01);
            for (int i = 0; i < mRawDatas.length; i++) {
                data[5 + i] = mRawDatas[i];
            }
        }
    }

    public void setFilterSwitchB(@IntRange(from = 0, to = 1) int enable) {
        data = new byte[]{
                (byte) 0xED,
                (byte) 0x01,
                (byte) ConfigKeyEnum.KEY_FILTER_SWITCH_B.getConfigKey(),
                (byte) 0x01,
                (byte) enable,
        };
    }

    public void setFilterABRelation(@IntRange(from = 0, to = 1) int relation) {
        data = new byte[]{
                (byte) 0xED,
                (byte) 0x01,
                (byte) ConfigKeyEnum.KEY_FILTER_A_B_RELATION.getConfigKey(),
                (byte) 0x01,
                (byte) relation,
        };
    }

    public void setAdvName(String advName) {
        byte[] advNameBytes = advName.getBytes();
        int length = advNameBytes.length;
        data = new byte[length + 4];
        data[0] = (byte) 0xED;
        data[1] = (byte) 0x01;
        data[2] = (byte) ConfigKeyEnum.KEY_ADV_NAME.getConfigKey();
        data[3] = (byte) length;
        for (int i = 0; i < advNameBytes.length; i++) {
            data[i + 4] = advNameBytes[i];
        }
    }

    public void setUUID(String uuid) {
        byte[] uuidBytes = MokoUtils.hex2bytes(uuid);
        data = new byte[20];
        data[0] = (byte) 0xED;
        data[1] = (byte) 0x01;
        data[2] = (byte) ConfigKeyEnum.KEY_IBEACON_UUID.getConfigKey();
        data[3] = (byte) 0x10;
        for (int i = 0; i < uuidBytes.length; i++) {
            data[i + 4] = uuidBytes[i];
        }
    }

    public void setMajor(int major) {
        byte[] majorBytes = MokoUtils.toByteArray(major, 2);
        data = new byte[]{
                (byte) 0xED,
                (byte) 0x01,
                (byte) ConfigKeyEnum.KEY_IBEACON_MAJOR.getConfigKey(),
                (byte) 0x02,
                majorBytes[0],
                majorBytes[1]
        };
    }

    public void setMinor(int minor) {
        byte[] minorBytes = MokoUtils.toByteArray(minor, 2);
        data = new byte[]{
                (byte) 0xED,
                (byte) 0x01,
                (byte) ConfigKeyEnum.KEY_IBEACON_MINOR.getConfigKey(),
                (byte) 0x02,
                minorBytes[0],
                minorBytes[1]
        };
    }

    public void setAdvInterval(int advInterval) {
        data = new byte[]{
                (byte) 0xED,
                (byte) 0x01,
                (byte) ConfigKeyEnum.KEY_ADV_INTERVAL.getConfigKey(),
                (byte) 0x01,
                (byte) advInterval
        };
    }

    public void setTransmission(int transmission) {
        data = new byte[]{
                (byte) 0xED,
                (byte) 0x01,
                (byte) ConfigKeyEnum.KEY_TRANSMISSION.getConfigKey(),
                (byte) 0x01,
                (byte) transmission
        };
    }

    public void setMeasurePower(int measurePower) {
        data = new byte[]{
                (byte) 0xED,
                (byte) 0x01,
                (byte) ConfigKeyEnum.KEY_MEASURE_POWER.getConfigKey(),
                (byte) 0x01,
                (byte) measurePower
        };
    }

    public void setConnectable(int connectionMode) {
        data = new byte[]{
                (byte) 0xED,
                (byte) 0x01,
                (byte) ConfigKeyEnum.KEY_CONNECTABLE.getConfigKey(),
                (byte) 0x01,
                (byte) connectionMode
        };
    }

    public void setLowBattery(int lowBattery) {
        data = new byte[]{
                (byte) 0xED,
                (byte) 0x01,
                (byte) ConfigKeyEnum.KEY_LOW_POWER_PERCENT.getConfigKey(),
                (byte) 0x01,
                (byte) lowBattery
        };
    }

    public void setDeviceInfoInterval(int interval) {
        data = new byte[]{
                (byte) 0xED,
                (byte) 0x01,
                (byte) ConfigKeyEnum.KEY_DEVICE_INFO_INTERVAL.getConfigKey(),
                (byte) 0x01,
                (byte) interval
        };
    }

    public void setClosePower() {
        data = new byte[]{
                (byte) 0xED,
                (byte) 0x01,
                (byte) ConfigKeyEnum.KEY_CLOSE.getConfigKey(),
                (byte) 0x01,
                (byte) 0x01
        };
    }

    public void changePassword(String password) {
        byte[] passwordBytes = password.getBytes();
        int length = passwordBytes.length;
        data = new byte[length + 4];
        data[0] = (byte) 0xED;
        data[1] = (byte) 0x01;
        data[2] = (byte) ConfigKeyEnum.KEY_PASSWORD.getConfigKey();
        data[3] = (byte) length;
        for (int i = 0; i < passwordBytes.length; i++) {
            data[i + 4] = passwordBytes[i];
        }
    }

    public void reset() {
        data = new byte[]{
                (byte) 0xED,
                (byte) 0x01,
                (byte) ConfigKeyEnum.KEY_RESET.getConfigKey(),
                (byte) 0x01,
                (byte) 0x01
        };
    }


    public void setScanWinow(int scannerState, int startTime) {
        data = new byte[]{
                (byte) 0xED,
                (byte) 0x01,
                (byte) ConfigKeyEnum.KEY_SCAN_WINDOW.getConfigKey(),
                (byte) 0x02,
                (byte) scannerState,
                (byte) startTime
        };
    }

    public void setLoraDevAddr(String devAddr) {
        byte[] rawDataBytes = MokoUtils.hex2bytes(devAddr);
        int length = rawDataBytes.length;
        data = new byte[4 + length];
        data[0] = (byte) 0xED;
        data[1] = (byte) 0x01;
        data[2] = (byte) ConfigKeyEnum.KEY_LORA_DEV_ADDR.getConfigKey();
        data[3] = (byte) length;
        for (int i = 0; i < length; i++) {
            data[i + 4] = rawDataBytes[i];
        }
    }

    public void setLoraAppSKey(String appSkey) {
        byte[] rawDataBytes = MokoUtils.hex2bytes(appSkey);
        int length = rawDataBytes.length;
        data = new byte[4 + length];
        data[0] = (byte) 0xED;
        data[1] = (byte) 0x01;
        data[2] = (byte) ConfigKeyEnum.KEY_LORA_APP_SKEY.getConfigKey();
        data[3] = (byte) length;
        for (int i = 0; i < length; i++) {
            data[i + 4] = rawDataBytes[i];
        }
    }

    public void setLoraNwkSKey(String nwkSkey) {
        byte[] rawDataBytes = MokoUtils.hex2bytes(nwkSkey);
        int length = rawDataBytes.length;
        data = new byte[4 + length];
        data[0] = (byte) 0xED;
        data[1] = (byte) 0x01;
        data[2] = (byte) ConfigKeyEnum.KEY_LORA_NWK_SKEY.getConfigKey();
        data[3] = (byte) length;
        for (int i = 0; i < length; i++) {
            data[i + 4] = rawDataBytes[i];
        }
    }

    public void setLoraDevEui(String devEui) {
        byte[] rawDataBytes = MokoUtils.hex2bytes(devEui);
        int length = rawDataBytes.length;
        data = new byte[4 + length];
        data[0] = (byte) 0xED;
        data[1] = (byte) 0x01;
        data[2] = (byte) ConfigKeyEnum.KEY_LORA_DEV_EUI.getConfigKey();
        data[3] = (byte) length;
        for (int i = 0; i < length; i++) {
            data[i + 4] = rawDataBytes[i];
        }
    }

    public void setLoraAppEui(String appEui) {
        byte[] rawDataBytes = MokoUtils.hex2bytes(appEui);
        int length = rawDataBytes.length;
        data = new byte[4 + length];
        data[0] = (byte) 0xED;
        data[1] = (byte) 0x01;
        data[2] = (byte) ConfigKeyEnum.KEY_LORA_APP_EUI.getConfigKey();
        data[3] = (byte) length;
        for (int i = 0; i < length; i++) {
            data[i + 4] = rawDataBytes[i];
        }
    }

    public void setLoraAppKey(String appKey) {
        byte[] rawDataBytes = MokoUtils.hex2bytes(appKey);
        int length = rawDataBytes.length;
        data = new byte[4 + length];
        data[0] = (byte) 0xED;
        data[1] = (byte) 0x01;
        data[2] = (byte) ConfigKeyEnum.KEY_LORA_APP_KEY.getConfigKey();
        data[3] = (byte) length;
        for (int i = 0; i < length; i++) {
            data[i + 4] = rawDataBytes[i];
        }
    }

    public void setLoraUploadMode(int mode) {
        data = new byte[]{
                (byte) 0xED,
                (byte) 0x01,
                (byte) ConfigKeyEnum.KEY_LORA_MODE.getConfigKey(),
                (byte) 0x01,
                (byte) mode
        };
    }

    public void setLoraUploadInterval(int interval) {
        data = new byte[]{
                (byte) 0xED,
                (byte) 0x01,
                (byte) ConfigKeyEnum.KEY_LORA_REPORT_INTERVAL.getConfigKey(),
                (byte) 0x01,
                (byte) interval
        };
    }

    public void setLoraMessageType(int type) {
        data = new byte[]{
                (byte) 0xED,
                (byte) 0x01,
                (byte) ConfigKeyEnum.KEY_LORA_MESSAGE_TYPE.getConfigKey(),
                (byte) 0x01,
                (byte) type
        };
    }

    public void setLoraRegion(int region) {
        data = new byte[]{
                (byte) 0xED,
                (byte) 0x01,
                (byte) ConfigKeyEnum.KEY_LORA_REGION.getConfigKey(),
                (byte) 0x01,
                (byte) region
        };
    }

    public void setLoraCH(int ch1, int ch2) {
        data = new byte[]{
                (byte) 0xED,
                (byte) 0x01,
                (byte) ConfigKeyEnum.KEY_LORA_CH.getConfigKey(),
                (byte) 0x02,
                (byte) ch1,
                (byte) ch2
        };
    }

    public void setLoraDR(int dr1) {
        data = new byte[]{
                (byte) 0xED,
                (byte) 0x01,
                (byte) ConfigKeyEnum.KEY_LORA_DR.getConfigKey(),
                (byte) 0x01,
                (byte) dr1
        };
    }

    public void setLoraADR(int adr) {
        data = new byte[]{
                (byte) 0xED,
                (byte) 0x01,
                (byte) ConfigKeyEnum.KEY_LORA_ADR.getConfigKey(),
                (byte) 0x01,
                (byte) adr
        };
    }

    public void setLoraConnect() {
        data = new byte[]{
                (byte) 0xED,
                (byte) 0x01,
                (byte) ConfigKeyEnum.KEY_LORA_CONNECT.getConfigKey(),
                (byte) 0x01,
                (byte) 0x01
        };
    }

    public void setVibrationIntensity(@IntRange(from = 0, to = 100) int intensity) {
        data = new byte[]{
                (byte) 0xED,
                (byte) 0x01,
                (byte) ConfigKeyEnum.KEY_VIBRATION_INTENSITY.getConfigKey(),
                (byte) 0x01,
                (byte) intensity
        };
    }

    public void setVibrationDuration(@IntRange(from = 0, to = 255) int duration) {
        data = new byte[]{
                (byte) 0xED,
                (byte) 0x01,
                (byte) ConfigKeyEnum.KEY_VIBRATION_DURATION.getConfigKey(),
                (byte) 0x01,
                (byte) duration
        };
    }

    public void setVibrationCycle(@IntRange(from = 1, to = 600) int cycle) {
        byte[] cycleBytes = MokoUtils.toByteArray(cycle, 2);
        data = new byte[]{
                (byte) 0xED,
                (byte) 0x01,
                (byte) ConfigKeyEnum.KEY_VIBRATION_CYCLE.getConfigKey(),
                (byte) 0x02,
                cycleBytes[0],
                cycleBytes[1]
        };
    }

    public void setWarningRssi(@IntRange(from = -127, to = 0) int rssi) {
        data = new byte[]{
                (byte) 0xED,
                (byte) 0x01,
                (byte) ConfigKeyEnum.KEY_WARNING_RSSI.getConfigKey(),
                (byte) 0x01,
                (byte) rssi
        };
    }
}
