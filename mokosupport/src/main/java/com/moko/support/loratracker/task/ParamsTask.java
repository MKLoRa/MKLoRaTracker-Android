package com.moko.support.loratracker.task;

import android.text.TextUtils;

import com.moko.ble.lib.task.OrderTask;
import com.moko.ble.lib.utils.MokoUtils;
import com.moko.support.loratracker.entity.OrderCHAR;
import com.moko.support.loratracker.entity.ParamsKeyEnum;

import java.util.ArrayList;
import java.util.Calendar;

import androidx.annotation.IntRange;

public class ParamsTask extends OrderTask {
    public byte[] data;

    public ParamsTask() {
        super(OrderCHAR.CHAR_PARAMS, OrderTask.RESPONSE_TYPE_WRITE_NO_RESPONSE);
    }

    @Override
    public byte[] assemble() {
        return data;
    }

    public void setData(ParamsKeyEnum key) {
        switch (key) {
            case KEY_TIME_SYNC_INTERVAL:
            case KEY_CURRENT_TIME:

            case KEY_IBEACON_UUID:
            case KEY_IBEACON_MAJOR:
            case KEY_IBEACON_MINOR:
            case KEY_MEASURE_POWER:
            case KEY_ADV_INTERVAL:
            case KEY_TRANSMISSION:
            case KEY_ADV_NAME:

            case KEY_TIME:

            case KEY_SCAN_WINDOW:
            case KEY_CONNECTABLE:

            case KEY_LORA_MODE:
            case KEY_LORA_REGION:
            case KEY_LORA_DEV_EUI:
            case KEY_LORA_APP_EUI:
            case KEY_LORA_APP_KEY:
            case KEY_LORA_DEV_ADDR:
            case KEY_LORA_APP_SKEY:
            case KEY_LORA_NWK_SKEY:
            case KEY_LORA_CH:
            case KEY_LORA_DR:
            case KEY_LORA_ADR:
            case KEY_LORA_MESSAGE_TYPE:
            case KEY_NETWORK_STATUS:
            case KEY_NETWORK_CHECK_INTERVAL:
            case KEY_ALARM_RSSI:
            case KEY_ALARM_NOTIFY:

            case KEY_VIBRATION_INTENSITY:
            case KEY_VIBRATION_CYCLE:
            case KEY_VIBRATION_DURATION:

            case KEY_DEVICE_MAC:
            case KEY_BATTERY:
            case KEY_LOW_POWER_PERCENT:
            case KEY_LORA_REPORT_INTERVAL:

            case KEY_TRACKING_FILTER_RSSI_A:
            case KEY_TRACKING_FILTER_MAC_A:
            case KEY_TRACKING_FILTER_ADV_NAME_A:
            case KEY_TRACKING_FILTER_UUID_A:
            case KEY_TRACKING_FILTER_MAJOR_RANGE_A:
            case KEY_TRACKING_FILTER_MINOR_RANGE_A:
            case KEY_TRACKING_FILTER_ADV_RAW_DATA_A:

            case KEY_TRACKING_FILTER_RSSI_B:
            case KEY_TRACKING_FILTER_MAC_B:
            case KEY_TRACKING_FILTER_ADV_NAME_B:
            case KEY_TRACKING_FILTER_UUID_B:
            case KEY_TRACKING_FILTER_MAJOR_RANGE_B:
            case KEY_TRACKING_FILTER_MINOR_RANGE_B:
            case KEY_TRACKING_FILTER_ADV_RAW_DATA_B:

            case KEY_TRACKING_FILTER_SWITCH_A:
            case KEY_TRACKING_FILTER_SWITCH_B:
            case KEY_TRACKING_FILTER_A_B_RELATION:
            case KEY_TRACKING_FILTER_REPEAT:

            case KEY_FILTER_VALID_INTERVAL:
            case KEY_DEVICE_INFO_INTERVAL:
            case KEY_DEVICE_INFO:
            case KEY_WARNING_RSSI:

            case KEY_LOCATION_FILTER_RSSI_A:
            case KEY_LOCATION_FILTER_MAC_A:
            case KEY_LOCATION_FILTER_ADV_NAME_A:
            case KEY_LOCATION_FILTER_UUID_A:
            case KEY_LOCATION_FILTER_MAJOR_RANGE_A:
            case KEY_LOCATION_FILTER_MINOR_RANGE_A:
            case KEY_LOCATION_FILTER_ADV_RAW_DATA_A:

            case KEY_LOCATION_FILTER_RSSI_B:
            case KEY_LOCATION_FILTER_MAC_B:
            case KEY_LOCATION_FILTER_ADV_NAME_B:
            case KEY_LOCATION_FILTER_UUID_B:
            case KEY_LOCATION_FILTER_MAJOR_RANGE_B:
            case KEY_LOCATION_FILTER_MINOR_RANGE_B:
            case KEY_LOCATION_FILTER_ADV_RAW_DATA_B:

            case KEY_LOCATION_FILTER_SWITCH_A:
            case KEY_LOCATION_FILTER_SWITCH_B:
            case KEY_LOCATION_FILTER_A_B_RELATION:
            case KEY_LOCATION_FILTER_REPEAT:

            case KEY_OPTIONAL_PAYLOAD_TRACKING:
            case KEY_REPORT_LOCATION_BEACONS:

            case KEY_THREE_AXIS_SAMPLE_RATE:
            case KEY_THREE_AXIS_G:
            case KEY_THREE_AXIS_TRIGGER_SENSITIVITY:
            case KEY_THREE_AXIS_ENABLE:
            case KEY_THREE_AXIS_REPORT_INTERVAL:
            case KEY_THREE_AXIS_DATA_ENABLE:
            case KEY_OPTIONAL_PAYLOAD_THREE_AXIS:

            case KEY_SOS_ENABLE:
            case KEY_SOS_REPORT_INTERVAL:
            case KEY_OPTIONAL_PAYLOAD_SOS:

            case KEY_GPS_FUNCTION_STATUS:
            case KEY_GPS_FUNCTION_SWITCH:
            case KEY_GPS_SATELLITE_SEARCH_TIME:
            case KEY_GPS_REPORT_INTERVAL:
            case KEY_OPTIONAL_PAYLOAD_GPS:

            case KEY_LORA_UPLINK_DELL_TIME:
            case KEY_LORA_DUTY_CYCLE_ENABLE:
            case KEY_REPORT_LOCATION_ENABLE:
                createGetConfigData(key.getParamsKey());
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

    public void setFilterValidInterval(@IntRange(from = 1, to = 600) int seconds) {
        byte[] intervalBytes = MokoUtils.toByteArray(seconds, 2);
        data = new byte[]{
                (byte) 0xED,
                (byte) 0x01,
                (byte) ParamsKeyEnum.KEY_FILTER_VALID_INTERVAL.getParamsKey(),
                (byte) 0x02,
                intervalBytes[0],
                intervalBytes[1]
        };
    }

    public void setAlarmNotify(@IntRange(from = 0, to = 3) int notify) {
        data = new byte[]{
                (byte) 0xED,
                (byte) 0x01,
                (byte) ParamsKeyEnum.KEY_ALARM_NOTIFY.getParamsKey(),
                (byte) 0x01,
                (byte) notify
        };
    }

    public void setAlarmTirggerRssi(@IntRange(from = -127, to = 0) int rssi) {
        data = new byte[]{
                (byte) 0xED,
                (byte) 0x01,
                (byte) ParamsKeyEnum.KEY_ALARM_RSSI.getParamsKey(),
                (byte) 0x01,
                (byte) rssi,
        };

    }

    public void setTime() {
        Calendar calendar = Calendar.getInstance();
        long time = calendar.getTimeInMillis() / 1000;
        byte[] bytes = new byte[4];
        for (int i = 0; i < 4; ++i) {
            bytes[i] = (byte) (time >> 8 * (3 - i) & 255);
        }
        data = new byte[]{
                (byte) 0xED,
                (byte) 0x01,
                (byte) ParamsKeyEnum.KEY_TIME.getParamsKey(),
                (byte) 0x04,
                bytes[0],
                bytes[1],
                bytes[2],
                bytes[3],
        };
    }

    public void setLBFilterRssiA(@IntRange(from = -127, to = 0) int rssi) {
        data = new byte[]{
                (byte) 0xED,
                (byte) 0x01,
                (byte) ParamsKeyEnum.KEY_LOCATION_FILTER_RSSI_A.getParamsKey(),
                (byte) 0x01,
                (byte) rssi
        };
    }

    public void setLBFilterMacA(String mac, boolean isReverse) {
        if (TextUtils.isEmpty(mac)) {
            data = new byte[]{
                    (byte) 0xED,
                    (byte) 0x01,
                    (byte) ParamsKeyEnum.KEY_LOCATION_FILTER_MAC_A.getParamsKey(),
                    (byte) 0x01,
                    (byte) 0x00,
            };

        } else {
            byte[] macBytes = MokoUtils.hex2bytes(mac);
            int length = macBytes.length + 1;
            data = new byte[4 + length];
            data[0] = (byte) 0xED;
            data[1] = (byte) 0x01;
            data[2] = (byte) ParamsKeyEnum.KEY_LOCATION_FILTER_MAC_A.getParamsKey();
            data[3] = (byte) length;
            data[4] = (byte) (isReverse ? 0x02 : 0x01);
            for (int i = 0; i < macBytes.length; i++) {
                data[5 + i] = macBytes[i];
            }
        }
    }

    public void setLBFilterNameA(String name, boolean isReverse) {
        if (TextUtils.isEmpty(name)) {
            data = new byte[]{
                    (byte) 0xED,
                    (byte) 0x01,
                    (byte) ParamsKeyEnum.KEY_LOCATION_FILTER_ADV_NAME_A.getParamsKey(),
                    (byte) 0x01,
                    (byte) 0x00,
            };
        } else {
            byte[] nameBytes = name.getBytes();
            int length = nameBytes.length + 1;
            data = new byte[4 + length];
            data[0] = (byte) 0xED;
            data[1] = (byte) 0x01;
            data[2] = (byte) ParamsKeyEnum.KEY_LOCATION_FILTER_ADV_NAME_A.getParamsKey();
            data[3] = (byte) length;
            data[4] = (byte) (isReverse ? 0x02 : 0x01);
            for (int i = 0; i < nameBytes.length; i++) {
                data[5 + i] = nameBytes[i];
            }
        }
    }

    public void setLBFilterUUIDA(String uuid, boolean isReverse) {
        if (TextUtils.isEmpty(uuid)) {
            data = new byte[]{
                    (byte) 0xED,
                    (byte) 0x01,
                    (byte) ParamsKeyEnum.KEY_LOCATION_FILTER_UUID_A.getParamsKey(),
                    (byte) 0x01,
                    (byte) 0x00,
            };
        } else {
            byte[] uuidBytes = MokoUtils.hex2bytes(uuid);
            int length = uuidBytes.length + 1;
            data = new byte[4 + length];
            data[0] = (byte) 0xED;
            data[1] = (byte) 0x01;
            data[2] = (byte) ParamsKeyEnum.KEY_LOCATION_FILTER_UUID_A.getParamsKey();
            data[3] = (byte) length;
            data[4] = (byte) (isReverse ? 0x02 : 0x01);
            for (int i = 0; i < uuidBytes.length; i++) {
                data[5 + i] = uuidBytes[i];
            }
        }
    }

    public void setLBFilterMajorRangeA(@IntRange(from = 0, to = 1) int enable,
                                       @IntRange(from = 0, to = 65535) int majorMin,
                                       @IntRange(from = 0, to = 65535) int majorMax,
                                       boolean isReverse) {
        if (enable == 0) {
            data = new byte[]{
                    (byte) 0xED,
                    (byte) 0x01,
                    (byte) ParamsKeyEnum.KEY_LOCATION_FILTER_MAJOR_RANGE_A.getParamsKey(),
                    (byte) 0x01,
                    (byte) 0x00,
            };
        } else {
            byte[] majorMinBytes = MokoUtils.toByteArray(majorMin, 2);
            byte[] majorMaxBytes = MokoUtils.toByteArray(majorMax, 2);
            data = new byte[]{
                    (byte) 0xED,
                    (byte) 0x01,
                    (byte) ParamsKeyEnum.KEY_LOCATION_FILTER_MAJOR_RANGE_A.getParamsKey(),
                    (byte) 0x05,
                    (byte) (isReverse ? 0x02 : 0x01),
                    majorMinBytes[0],
                    majorMinBytes[1],
                    majorMaxBytes[0],
                    majorMaxBytes[1],
            };
        }
    }

    public void setLBFilterMinorRangeA(@IntRange(from = 0, to = 1) int enable,
                                       @IntRange(from = 0, to = 65535) int minorMin,
                                       @IntRange(from = 0, to = 65535) int minorMax,
                                       boolean isReverse) {
        if (enable == 0) {
            data = new byte[]{
                    (byte) 0xED,
                    (byte) 0x01,
                    (byte) ParamsKeyEnum.KEY_LOCATION_FILTER_MINOR_RANGE_A.getParamsKey(),
                    (byte) 0x01,
                    (byte) 0x00,
            };
        } else {
            byte[] minorMinBytes = MokoUtils.toByteArray(minorMin, 2);
            byte[] minorMaxBytes = MokoUtils.toByteArray(minorMax, 2);
            data = new byte[]{
                    (byte) 0xED,
                    (byte) 0x01,
                    (byte) ParamsKeyEnum.KEY_LOCATION_FILTER_MINOR_RANGE_A.getParamsKey(),
                    (byte) 0x05,
                    (byte) (isReverse ? 0x02 : 0x01),
                    minorMinBytes[0],
                    minorMinBytes[1],
                    minorMaxBytes[0],
                    minorMaxBytes[1],
            };
        }
    }

    public void setLBFilterRawDataA(ArrayList<String> filterRawDatas, boolean isReverse) {
        if (filterRawDatas == null || filterRawDatas.size() == 0) {
            data = new byte[]{
                    (byte) 0xED,
                    (byte) 0x01,
                    (byte) ParamsKeyEnum.KEY_LOCATION_FILTER_ADV_RAW_DATA_A.getParamsKey(),
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
            data[2] = (byte) ParamsKeyEnum.KEY_LOCATION_FILTER_ADV_RAW_DATA_A.getParamsKey();
            data[3] = (byte) length;
            data[4] = (byte) (isReverse ? 0x02 : 0x01);
            for (int i = 0; i < mRawDatas.length; i++) {
                data[5 + i] = mRawDatas[i];
            }
        }
    }

    public void setLBFilterSwitchA(@IntRange(from = 0, to = 1) int enable) {
        data = new byte[]{
                (byte) 0xED,
                (byte) 0x01,
                (byte) ParamsKeyEnum.KEY_LOCATION_FILTER_SWITCH_A.getParamsKey(),
                (byte) 0x01,
                (byte) enable,
        };
    }

    public void setLBFilterRssiB(@IntRange(from = -127, to = 0) int rssi) {
        data = new byte[]{
                (byte) 0xED,
                (byte) 0x01,
                (byte) ParamsKeyEnum.KEY_LOCATION_FILTER_RSSI_B.getParamsKey(),
                (byte) 0x01,
                (byte) rssi
        };
    }

    public void setLBFilterMacB(String mac, boolean isReverse) {
        if (TextUtils.isEmpty(mac)) {
            data = new byte[]{
                    (byte) 0xED,
                    (byte) 0x01,
                    (byte) ParamsKeyEnum.KEY_LOCATION_FILTER_MAC_B.getParamsKey(),
                    (byte) 0x01,
                    (byte) 0x00,
            };

        } else {
            byte[] macBytes = MokoUtils.hex2bytes(mac);
            int length = macBytes.length + 1;
            data = new byte[4 + length];
            data[0] = (byte) 0xED;
            data[1] = (byte) 0x01;
            data[2] = (byte) ParamsKeyEnum.KEY_LOCATION_FILTER_MAC_B.getParamsKey();
            data[3] = (byte) length;
            data[4] = (byte) (isReverse ? 0x02 : 0x01);
            for (int i = 0; i < macBytes.length; i++) {
                data[5 + i] = macBytes[i];
            }
        }
    }

    public void setLBFilterNameB(String name, boolean isReverse) {
        if (TextUtils.isEmpty(name)) {
            data = new byte[]{
                    (byte) 0xED,
                    (byte) 0x01,
                    (byte) ParamsKeyEnum.KEY_LOCATION_FILTER_ADV_NAME_B.getParamsKey(),
                    (byte) 0x01,
                    (byte) 0x00,
            };
        } else {
            byte[] nameBytes = name.getBytes();
            int length = nameBytes.length + 1;
            data = new byte[4 + length];
            data[0] = (byte) 0xED;
            data[1] = (byte) 0x01;
            data[2] = (byte) ParamsKeyEnum.KEY_LOCATION_FILTER_ADV_NAME_B.getParamsKey();
            data[3] = (byte) length;
            data[4] = (byte) (isReverse ? 0x02 : 0x01);
            for (int i = 0; i < nameBytes.length; i++) {
                data[5 + i] = nameBytes[i];
            }
        }
    }

    public void setLBFilterUUIDB(String uuid, boolean isReverse) {
        if (TextUtils.isEmpty(uuid)) {
            data = new byte[]{
                    (byte) 0xED,
                    (byte) 0x01,
                    (byte) ParamsKeyEnum.KEY_LOCATION_FILTER_UUID_B.getParamsKey(),
                    (byte) 0x01,
                    (byte) 0x00,
            };
        } else {
            byte[] uuidBytes = MokoUtils.hex2bytes(uuid);
            int length = uuidBytes.length + 1;
            data = new byte[4 + length];
            data[0] = (byte) 0xED;
            data[1] = (byte) 0x01;
            data[2] = (byte) ParamsKeyEnum.KEY_LOCATION_FILTER_UUID_B.getParamsKey();
            data[3] = (byte) length;
            data[4] = (byte) (isReverse ? 0x02 : 0x01);
            for (int i = 0; i < uuidBytes.length; i++) {
                data[5 + i] = uuidBytes[i];
            }
        }
    }

    public void setLBFilterMajorRangeB(@IntRange(from = 0, to = 1) int enable,
                                       @IntRange(from = 0, to = 65535) int majorMin,
                                       @IntRange(from = 0, to = 65535) int majorMax,
                                       boolean isReverse) {
        if (enable == 0) {
            data = new byte[]{
                    (byte) 0xED,
                    (byte) 0x01,
                    (byte) ParamsKeyEnum.KEY_LOCATION_FILTER_MAJOR_RANGE_B.getParamsKey(),
                    (byte) 0x01,
                    (byte) 0x00,
            };
        } else {
            byte[] majorMinBytes = MokoUtils.toByteArray(majorMin, 2);
            byte[] majorMaxBytes = MokoUtils.toByteArray(majorMax, 2);
            data = new byte[]{
                    (byte) 0xED,
                    (byte) 0x01,
                    (byte) ParamsKeyEnum.KEY_LOCATION_FILTER_MAJOR_RANGE_B.getParamsKey(),
                    (byte) 0x05,
                    (byte) (isReverse ? 0x02 : 0x01),
                    majorMinBytes[0],
                    majorMinBytes[1],
                    majorMaxBytes[0],
                    majorMaxBytes[1],
            };
        }
    }

    public void setLBFilterMinorRangeB(@IntRange(from = 0, to = 1) int enable,
                                       @IntRange(from = 0, to = 65535) int minorMin,
                                       @IntRange(from = 0, to = 65535) int minorMax,
                                       boolean isReverse) {
        if (enable == 0) {
            data = new byte[]{
                    (byte) 0xED,
                    (byte) 0x01,
                    (byte) ParamsKeyEnum.KEY_LOCATION_FILTER_MINOR_RANGE_B.getParamsKey(),
                    (byte) 0x01,
                    (byte) 0x00,
            };
        } else {
            byte[] minorMinBytes = MokoUtils.toByteArray(minorMin, 2);
            byte[] minorMaxBytes = MokoUtils.toByteArray(minorMax, 2);
            data = new byte[]{
                    (byte) 0xED,
                    (byte) 0x01,
                    (byte) ParamsKeyEnum.KEY_LOCATION_FILTER_MINOR_RANGE_B.getParamsKey(),
                    (byte) 0x05,
                    (byte) (isReverse ? 0x02 : 0x01),
                    minorMinBytes[0],
                    minorMinBytes[1],
                    minorMaxBytes[0],
                    minorMaxBytes[1],
            };
        }
    }

    public void setLBFilterRawDataB(ArrayList<String> filterRawDatas, boolean isReverse) {
        if (filterRawDatas == null || filterRawDatas.size() == 0) {
            data = new byte[]{
                    (byte) 0xED,
                    (byte) 0x01,
                    (byte) ParamsKeyEnum.KEY_LOCATION_FILTER_ADV_RAW_DATA_B.getParamsKey(),
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
            data[2] = (byte) ParamsKeyEnum.KEY_LOCATION_FILTER_ADV_RAW_DATA_B.getParamsKey();
            data[3] = (byte) length;
            data[4] = (byte) (isReverse ? 0x02 : 0x01);
            for (int i = 0; i < mRawDatas.length; i++) {
                data[5 + i] = mRawDatas[i];
            }
        }
    }

    public void setLBFilterSwitchB(@IntRange(from = 0, to = 1) int enable) {
        data = new byte[]{
                (byte) 0xED,
                (byte) 0x01,
                (byte) ParamsKeyEnum.KEY_LOCATION_FILTER_SWITCH_B.getParamsKey(),
                (byte) 0x01,
                (byte) enable,
        };
    }

    public void setLBFilterABRelation(@IntRange(from = 0, to = 1) int relation) {
        data = new byte[]{
                (byte) 0xED,
                (byte) 0x01,
                (byte) ParamsKeyEnum.KEY_LOCATION_FILTER_A_B_RELATION.getParamsKey(),
                (byte) 0x01,
                (byte) relation,
        };
    }

    public void setLBFilterRepeat(@IntRange(from = 0, to = 3) int repeat) {
        data = new byte[]{
                (byte) 0xED,
                (byte) 0x01,
                (byte) ParamsKeyEnum.KEY_LOCATION_FILTER_REPEAT.getParamsKey(),
                (byte) 0x01,
                (byte) repeat,
        };
    }

    public void setCTFilterRssiA(@IntRange(from = -127, to = 0) int rssi) {
        data = new byte[]{
                (byte) 0xED,
                (byte) 0x01,
                (byte) ParamsKeyEnum.KEY_TRACKING_FILTER_RSSI_A.getParamsKey(),
                (byte) 0x01,
                (byte) rssi
        };
    }

    public void setCTFilterMacA(String mac, boolean isReverse) {
        if (TextUtils.isEmpty(mac)) {
            data = new byte[]{
                    (byte) 0xED,
                    (byte) 0x01,
                    (byte) ParamsKeyEnum.KEY_TRACKING_FILTER_MAC_A.getParamsKey(),
                    (byte) 0x01,
                    (byte) 0x00,
            };

        } else {
            byte[] macBytes = MokoUtils.hex2bytes(mac);
            int length = macBytes.length + 1;
            data = new byte[4 + length];
            data[0] = (byte) 0xED;
            data[1] = (byte) 0x01;
            data[2] = (byte) ParamsKeyEnum.KEY_TRACKING_FILTER_MAC_A.getParamsKey();
            data[3] = (byte) length;
            data[4] = (byte) (isReverse ? 0x02 : 0x01);
            for (int i = 0; i < macBytes.length; i++) {
                data[5 + i] = macBytes[i];
            }
        }
    }

    public void setCTFilterNameA(String name, boolean isReverse) {
        if (TextUtils.isEmpty(name)) {
            data = new byte[]{
                    (byte) 0xED,
                    (byte) 0x01,
                    (byte) ParamsKeyEnum.KEY_TRACKING_FILTER_ADV_NAME_A.getParamsKey(),
                    (byte) 0x01,
                    (byte) 0x00,
            };
        } else {
            byte[] nameBytes = name.getBytes();
            int length = nameBytes.length + 1;
            data = new byte[4 + length];
            data[0] = (byte) 0xED;
            data[1] = (byte) 0x01;
            data[2] = (byte) ParamsKeyEnum.KEY_TRACKING_FILTER_ADV_NAME_A.getParamsKey();
            data[3] = (byte) length;
            data[4] = (byte) (isReverse ? 0x02 : 0x01);
            for (int i = 0; i < nameBytes.length; i++) {
                data[5 + i] = nameBytes[i];
            }
        }
    }

    public void setCTFilterUUIDA(String uuid, boolean isReverse) {
        if (TextUtils.isEmpty(uuid)) {
            data = new byte[]{
                    (byte) 0xED,
                    (byte) 0x01,
                    (byte) ParamsKeyEnum.KEY_TRACKING_FILTER_UUID_A.getParamsKey(),
                    (byte) 0x01,
                    (byte) 0x00,
            };
        } else {
            byte[] uuidBytes = MokoUtils.hex2bytes(uuid);
            int length = uuidBytes.length + 1;
            data = new byte[4 + length];
            data[0] = (byte) 0xED;
            data[1] = (byte) 0x01;
            data[2] = (byte) ParamsKeyEnum.KEY_TRACKING_FILTER_UUID_A.getParamsKey();
            data[3] = (byte) length;
            data[4] = (byte) (isReverse ? 0x02 : 0x01);
            for (int i = 0; i < uuidBytes.length; i++) {
                data[5 + i] = uuidBytes[i];
            }
        }
    }

    public void setCTFilterMajorRangeA(@IntRange(from = 0, to = 1) int enable,
                                       @IntRange(from = 0, to = 65535) int majorMin,
                                       @IntRange(from = 0, to = 65535) int majorMax,
                                       boolean isReverse) {
        if (enable == 0) {
            data = new byte[]{
                    (byte) 0xED,
                    (byte) 0x01,
                    (byte) ParamsKeyEnum.KEY_TRACKING_FILTER_MAJOR_RANGE_A.getParamsKey(),
                    (byte) 0x01,
                    (byte) 0x00,
            };
        } else {
            byte[] majorMinBytes = MokoUtils.toByteArray(majorMin, 2);
            byte[] majorMaxBytes = MokoUtils.toByteArray(majorMax, 2);
            data = new byte[]{
                    (byte) 0xED,
                    (byte) 0x01,
                    (byte) ParamsKeyEnum.KEY_TRACKING_FILTER_MAJOR_RANGE_A.getParamsKey(),
                    (byte) 0x05,
                    (byte) (isReverse ? 0x02 : 0x01),
                    majorMinBytes[0],
                    majorMinBytes[1],
                    majorMaxBytes[0],
                    majorMaxBytes[1],
            };
        }
    }

    public void setCTFilterMinorRangeA(@IntRange(from = 0, to = 1) int enable,
                                       @IntRange(from = 0, to = 65535) int minorMin,
                                       @IntRange(from = 0, to = 65535) int minorMax,
                                       boolean isReverse) {
        if (enable == 0) {
            data = new byte[]{
                    (byte) 0xED,
                    (byte) 0x01,
                    (byte) ParamsKeyEnum.KEY_TRACKING_FILTER_MINOR_RANGE_A.getParamsKey(),
                    (byte) 0x01,
                    (byte) 0x00,
            };
        } else {
            byte[] minorMinBytes = MokoUtils.toByteArray(minorMin, 2);
            byte[] minorMaxBytes = MokoUtils.toByteArray(minorMax, 2);
            data = new byte[]{
                    (byte) 0xED,
                    (byte) 0x01,
                    (byte) ParamsKeyEnum.KEY_TRACKING_FILTER_MINOR_RANGE_A.getParamsKey(),
                    (byte) 0x05,
                    (byte) (isReverse ? 0x02 : 0x01),
                    minorMinBytes[0],
                    minorMinBytes[1],
                    minorMaxBytes[0],
                    minorMaxBytes[1],
            };
        }
    }

    public void setCTFilterRawDataA(ArrayList<String> filterRawDatas, boolean isReverse) {
        if (filterRawDatas == null || filterRawDatas.size() == 0) {
            data = new byte[]{
                    (byte) 0xED,
                    (byte) 0x01,
                    (byte) ParamsKeyEnum.KEY_TRACKING_FILTER_ADV_RAW_DATA_A.getParamsKey(),
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
            data[2] = (byte) ParamsKeyEnum.KEY_TRACKING_FILTER_ADV_RAW_DATA_A.getParamsKey();
            data[3] = (byte) length;
            data[4] = (byte) (isReverse ? 0x02 : 0x01);
            for (int i = 0; i < mRawDatas.length; i++) {
                data[5 + i] = mRawDatas[i];
            }
        }
    }

    public void setCTFilterSwitchA(@IntRange(from = 0, to = 1) int enable) {
        data = new byte[]{
                (byte) 0xED,
                (byte) 0x01,
                (byte) ParamsKeyEnum.KEY_TRACKING_FILTER_SWITCH_A.getParamsKey(),
                (byte) 0x01,
                (byte) enable,
        };
    }

    public void setCTFilterRssiB(@IntRange(from = -127, to = 0) int rssi) {
        data = new byte[]{
                (byte) 0xED,
                (byte) 0x01,
                (byte) ParamsKeyEnum.KEY_TRACKING_FILTER_RSSI_B.getParamsKey(),
                (byte) 0x01,
                (byte) rssi
        };
    }

    public void setCTFilterMacB(String mac, boolean isReverse) {
        if (TextUtils.isEmpty(mac)) {
            data = new byte[]{
                    (byte) 0xED,
                    (byte) 0x01,
                    (byte) ParamsKeyEnum.KEY_TRACKING_FILTER_MAC_B.getParamsKey(),
                    (byte) 0x01,
                    (byte) 0x00,
            };

        } else {
            byte[] macBytes = MokoUtils.hex2bytes(mac);
            int length = macBytes.length + 1;
            data = new byte[4 + length];
            data[0] = (byte) 0xED;
            data[1] = (byte) 0x01;
            data[2] = (byte) ParamsKeyEnum.KEY_TRACKING_FILTER_MAC_B.getParamsKey();
            data[3] = (byte) length;
            data[4] = (byte) (isReverse ? 0x02 : 0x01);
            for (int i = 0; i < macBytes.length; i++) {
                data[5 + i] = macBytes[i];
            }
        }
    }

    public void setCTFilterNameB(String name, boolean isReverse) {
        if (TextUtils.isEmpty(name)) {
            data = new byte[]{
                    (byte) 0xED,
                    (byte) 0x01,
                    (byte) ParamsKeyEnum.KEY_TRACKING_FILTER_ADV_NAME_B.getParamsKey(),
                    (byte) 0x01,
                    (byte) 0x00,
            };
        } else {
            byte[] nameBytes = name.getBytes();
            int length = nameBytes.length + 1;
            data = new byte[4 + length];
            data[0] = (byte) 0xED;
            data[1] = (byte) 0x01;
            data[2] = (byte) ParamsKeyEnum.KEY_TRACKING_FILTER_ADV_NAME_B.getParamsKey();
            data[3] = (byte) length;
            data[4] = (byte) (isReverse ? 0x02 : 0x01);
            for (int i = 0; i < nameBytes.length; i++) {
                data[5 + i] = nameBytes[i];
            }
        }
    }

    public void setCTFilterUUIDB(String uuid, boolean isReverse) {
        if (TextUtils.isEmpty(uuid)) {
            data = new byte[]{
                    (byte) 0xED,
                    (byte) 0x01,
                    (byte) ParamsKeyEnum.KEY_TRACKING_FILTER_UUID_B.getParamsKey(),
                    (byte) 0x01,
                    (byte) 0x00,
            };
        } else {
            byte[] uuidBytes = MokoUtils.hex2bytes(uuid);
            int length = uuidBytes.length + 1;
            data = new byte[4 + length];
            data[0] = (byte) 0xED;
            data[1] = (byte) 0x01;
            data[2] = (byte) ParamsKeyEnum.KEY_TRACKING_FILTER_UUID_B.getParamsKey();
            data[3] = (byte) length;
            data[4] = (byte) (isReverse ? 0x02 : 0x01);
            for (int i = 0; i < uuidBytes.length; i++) {
                data[5 + i] = uuidBytes[i];
            }
        }
    }

    public void setCTFilterMajorRangeB(@IntRange(from = 0, to = 1) int enable,
                                       @IntRange(from = 0, to = 65535) int majorMin,
                                       @IntRange(from = 0, to = 65535) int majorMax,
                                       boolean isReverse) {
        if (enable == 0) {
            data = new byte[]{
                    (byte) 0xED,
                    (byte) 0x01,
                    (byte) ParamsKeyEnum.KEY_TRACKING_FILTER_MAJOR_RANGE_B.getParamsKey(),
                    (byte) 0x01,
                    (byte) 0x00,
            };
        } else {
            byte[] majorMinBytes = MokoUtils.toByteArray(majorMin, 2);
            byte[] majorMaxBytes = MokoUtils.toByteArray(majorMax, 2);
            data = new byte[]{
                    (byte) 0xED,
                    (byte) 0x01,
                    (byte) ParamsKeyEnum.KEY_TRACKING_FILTER_MAJOR_RANGE_B.getParamsKey(),
                    (byte) 0x05,
                    (byte) (isReverse ? 0x02 : 0x01),
                    majorMinBytes[0],
                    majorMinBytes[1],
                    majorMaxBytes[0],
                    majorMaxBytes[1],
            };
        }
    }

    public void setCTFilterMinorRangeB(@IntRange(from = 0, to = 1) int enable,
                                       @IntRange(from = 0, to = 65535) int minorMin,
                                       @IntRange(from = 0, to = 65535) int minorMax,
                                       boolean isReverse) {
        if (enable == 0) {
            data = new byte[]{
                    (byte) 0xED,
                    (byte) 0x01,
                    (byte) ParamsKeyEnum.KEY_TRACKING_FILTER_MINOR_RANGE_B.getParamsKey(),
                    (byte) 0x01,
                    (byte) 0x00,
            };
        } else {
            byte[] minorMinBytes = MokoUtils.toByteArray(minorMin, 2);
            byte[] minorMaxBytes = MokoUtils.toByteArray(minorMax, 2);
            data = new byte[]{
                    (byte) 0xED,
                    (byte) 0x01,
                    (byte) ParamsKeyEnum.KEY_TRACKING_FILTER_MINOR_RANGE_B.getParamsKey(),
                    (byte) 0x05,
                    (byte) (isReverse ? 0x02 : 0x01),
                    minorMinBytes[0],
                    minorMinBytes[1],
                    minorMaxBytes[0],
                    minorMaxBytes[1],
            };
        }
    }

    public void setCTFilterRawDataB(ArrayList<String> filterRawDatas, boolean isReverse) {
        if (filterRawDatas == null || filterRawDatas.size() == 0) {
            data = new byte[]{
                    (byte) 0xED,
                    (byte) 0x01,
                    (byte) ParamsKeyEnum.KEY_TRACKING_FILTER_ADV_RAW_DATA_B.getParamsKey(),
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
            data[2] = (byte) ParamsKeyEnum.KEY_TRACKING_FILTER_ADV_RAW_DATA_B.getParamsKey();
            data[3] = (byte) length;
            data[4] = (byte) (isReverse ? 0x02 : 0x01);
            for (int i = 0; i < mRawDatas.length; i++) {
                data[5 + i] = mRawDatas[i];
            }
        }
    }

    public void setCTFilterSwitchB(@IntRange(from = 0, to = 1) int enable) {
        data = new byte[]{
                (byte) 0xED,
                (byte) 0x01,
                (byte) ParamsKeyEnum.KEY_TRACKING_FILTER_SWITCH_B.getParamsKey(),
                (byte) 0x01,
                (byte) enable,
        };
    }

    public void setCTFilterABRelation(@IntRange(from = 0, to = 1) int relation) {
        data = new byte[]{
                (byte) 0xED,
                (byte) 0x01,
                (byte) ParamsKeyEnum.KEY_TRACKING_FILTER_A_B_RELATION.getParamsKey(),
                (byte) 0x01,
                (byte) relation,
        };
    }

    public void setCTFilterRepeat(@IntRange(from = 0, to = 3) int repeat) {
        data = new byte[]{
                (byte) 0xED,
                (byte) 0x01,
                (byte) ParamsKeyEnum.KEY_TRACKING_FILTER_REPEAT.getParamsKey(),
                (byte) 0x01,
                (byte) repeat,
        };
    }

    public void setTrackingOptionPayload(@IntRange(from = 0, to = 255) int payload) {
        data = new byte[]{
                (byte) 0xED,
                (byte) 0x01,
                (byte) ParamsKeyEnum.KEY_OPTIONAL_PAYLOAD_TRACKING.getParamsKey(),
                (byte) 0x01,
                (byte) payload,
        };
    }

    public void setReportLocationBeacons(@IntRange(from = 1, to = 4) int num) {
        data = new byte[]{
                (byte) 0xED,
                (byte) 0x01,
                (byte) ParamsKeyEnum.KEY_REPORT_LOCATION_BEACONS.getParamsKey(),
                (byte) 0x01,
                (byte) num,
        };
    }

    public void setAdvName(String advName) {
        byte[] advNameBytes = advName.getBytes();
        int length = advNameBytes.length;
        data = new byte[length + 4];
        data[0] = (byte) 0xED;
        data[1] = (byte) 0x01;
        data[2] = (byte) ParamsKeyEnum.KEY_ADV_NAME.getParamsKey();
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
        data[2] = (byte) ParamsKeyEnum.KEY_IBEACON_UUID.getParamsKey();
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
                (byte) ParamsKeyEnum.KEY_IBEACON_MAJOR.getParamsKey(),
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
                (byte) ParamsKeyEnum.KEY_IBEACON_MINOR.getParamsKey(),
                (byte) 0x02,
                minorBytes[0],
                minorBytes[1]
        };
    }

    public void setAdvInterval(int advInterval) {
        data = new byte[]{
                (byte) 0xED,
                (byte) 0x01,
                (byte) ParamsKeyEnum.KEY_ADV_INTERVAL.getParamsKey(),
                (byte) 0x01,
                (byte) advInterval
        };
    }

    public void setTransmission(int transmission) {
        data = new byte[]{
                (byte) 0xED,
                (byte) 0x01,
                (byte) ParamsKeyEnum.KEY_TRANSMISSION.getParamsKey(),
                (byte) 0x01,
                (byte) transmission
        };
    }

    public void setMeasurePower(int measurePower) {
        data = new byte[]{
                (byte) 0xED,
                (byte) 0x01,
                (byte) ParamsKeyEnum.KEY_MEASURE_POWER.getParamsKey(),
                (byte) 0x01,
                (byte) measurePower
        };
    }

    public void setConnectable(int connectionMode) {
        data = new byte[]{
                (byte) 0xED,
                (byte) 0x01,
                (byte) ParamsKeyEnum.KEY_CONNECTABLE.getParamsKey(),
                (byte) 0x01,
                (byte) connectionMode
        };
    }

    public void setLowBattery(int lowBattery) {
        data = new byte[]{
                (byte) 0xED,
                (byte) 0x01,
                (byte) ParamsKeyEnum.KEY_LOW_POWER_PERCENT.getParamsKey(),
                (byte) 0x01,
                (byte) lowBattery
        };
    }

    public void setDeviceInfoInterval(int interval) {
        data = new byte[]{
                (byte) 0xED,
                (byte) 0x01,
                (byte) ParamsKeyEnum.KEY_DEVICE_INFO_INTERVAL.getParamsKey(),
                (byte) 0x01,
                (byte) interval
        };
    }

    public void setClosePower() {
        data = new byte[]{
                (byte) 0xED,
                (byte) 0x01,
                (byte) ParamsKeyEnum.KEY_CLOSE.getParamsKey(),
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
        data[2] = (byte) ParamsKeyEnum.KEY_PASSWORD.getParamsKey();
        data[3] = (byte) length;
        for (int i = 0; i < passwordBytes.length; i++) {
            data[i + 4] = passwordBytes[i];
        }
    }

    public void reset() {
        data = new byte[]{
                (byte) 0xED,
                (byte) 0x01,
                (byte) ParamsKeyEnum.KEY_RESET.getParamsKey(),
                (byte) 0x01,
                (byte) 0x01
        };
    }


    public void setScanWinow(int scanWindow) {
        data = new byte[]{
                (byte) 0xED,
                (byte) 0x01,
                (byte) ParamsKeyEnum.KEY_SCAN_WINDOW.getParamsKey(),
                (byte) 0x01,
                (byte) scanWindow
        };
    }

    public void setLoraDevAddr(String devAddr) {
        byte[] rawDataBytes = MokoUtils.hex2bytes(devAddr);
        int length = rawDataBytes.length;
        data = new byte[4 + length];
        data[0] = (byte) 0xED;
        data[1] = (byte) 0x01;
        data[2] = (byte) ParamsKeyEnum.KEY_LORA_DEV_ADDR.getParamsKey();
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
        data[2] = (byte) ParamsKeyEnum.KEY_LORA_APP_SKEY.getParamsKey();
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
        data[2] = (byte) ParamsKeyEnum.KEY_LORA_NWK_SKEY.getParamsKey();
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
        data[2] = (byte) ParamsKeyEnum.KEY_LORA_DEV_EUI.getParamsKey();
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
        data[2] = (byte) ParamsKeyEnum.KEY_LORA_APP_EUI.getParamsKey();
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
        data[2] = (byte) ParamsKeyEnum.KEY_LORA_APP_KEY.getParamsKey();
        data[3] = (byte) length;
        for (int i = 0; i < length; i++) {
            data[i + 4] = rawDataBytes[i];
        }
    }

    public void setLoraUploadMode(int mode) {
        data = new byte[]{
                (byte) 0xED,
                (byte) 0x01,
                (byte) ParamsKeyEnum.KEY_LORA_MODE.getParamsKey(),
                (byte) 0x01,
                (byte) mode
        };
    }

    public void setLoraUploadInterval(int interval) {
        data = new byte[]{
                (byte) 0xED,
                (byte) 0x01,
                (byte) ParamsKeyEnum.KEY_LORA_REPORT_INTERVAL.getParamsKey(),
                (byte) 0x01,
                (byte) interval
        };
    }

    public void setLoraMessageType(int type) {
        data = new byte[]{
                (byte) 0xED,
                (byte) 0x01,
                (byte) ParamsKeyEnum.KEY_LORA_MESSAGE_TYPE.getParamsKey(),
                (byte) 0x01,
                (byte) type
        };
    }

    public void setLoraRegion(int region) {
        data = new byte[]{
                (byte) 0xED,
                (byte) 0x01,
                (byte) ParamsKeyEnum.KEY_LORA_REGION.getParamsKey(),
                (byte) 0x01,
                (byte) region
        };
    }

    public void setLoraCH(int ch1, int ch2) {
        data = new byte[]{
                (byte) 0xED,
                (byte) 0x01,
                (byte) ParamsKeyEnum.KEY_LORA_CH.getParamsKey(),
                (byte) 0x02,
                (byte) ch1,
                (byte) ch2
        };
    }


    public void setLoraDutyCycleEnable(@IntRange(from = 0, to = 1) int enable) {
        data = new byte[]{
                (byte) 0xED,
                (byte) 0x01,
                (byte) ParamsKeyEnum.KEY_LORA_DUTY_CYCLE_ENABLE.getParamsKey(),
                (byte) 0x01,
                (byte) enable
        };
    }

    public void setLoraDR(int dr1) {
        data = new byte[]{
                (byte) 0xED,
                (byte) 0x01,
                (byte) ParamsKeyEnum.KEY_LORA_DR.getParamsKey(),
                (byte) 0x01,
                (byte) dr1
        };
    }

    public void setLoraADR(int adr) {
        data = new byte[]{
                (byte) 0xED,
                (byte) 0x01,
                (byte) ParamsKeyEnum.KEY_LORA_ADR.getParamsKey(),
                (byte) 0x01,
                (byte) adr
        };
    }

    public void setLoraUplinkDellTime(@IntRange(from = 0, to = 1) int uplinkDellTime) {
        data = new byte[]{
                (byte) 0xED,
                (byte) 0x01,
                (byte) ParamsKeyEnum.KEY_LORA_UPLINK_DELL_TIME.getParamsKey(),
                (byte) 0x01,
                (byte) uplinkDellTime
        };
    }

    public void setLoraConnect() {
        data = new byte[]{
                (byte) 0xED,
                (byte) 0x01,
                (byte) ParamsKeyEnum.KEY_LORA_CONNECT.getParamsKey(),
                (byte) 0x01,
                (byte) 0x01
        };
    }

    public void setVibrationIntensity(@IntRange(from = 0, to = 100) int intensity) {
        data = new byte[]{
                (byte) 0xED,
                (byte) 0x01,
                (byte) ParamsKeyEnum.KEY_VIBRATION_INTENSITY.getParamsKey(),
                (byte) 0x01,
                (byte) intensity
        };
    }

    public void setVibrationDuration(@IntRange(from = 0, to = 255) int duration) {
        data = new byte[]{
                (byte) 0xED,
                (byte) 0x01,
                (byte) ParamsKeyEnum.KEY_VIBRATION_DURATION.getParamsKey(),
                (byte) 0x01,
                (byte) duration
        };
    }

    public void setVibrationCycle(@IntRange(from = 1, to = 600) int cycle) {
        byte[] cycleBytes = MokoUtils.toByteArray(cycle, 2);
        data = new byte[]{
                (byte) 0xED,
                (byte) 0x01,
                (byte) ParamsKeyEnum.KEY_VIBRATION_CYCLE.getParamsKey(),
                (byte) 0x02,
                cycleBytes[0],
                cycleBytes[1]
        };
    }

    public void setWarningRssi(@IntRange(from = -127, to = 0) int rssi) {
        data = new byte[]{
                (byte) 0xED,
                (byte) 0x01,
                (byte) ParamsKeyEnum.KEY_WARNING_RSSI.getParamsKey(),
                (byte) 0x01,
                (byte) rssi
        };
    }

    public void setSOSEnable(@IntRange(from = 0, to = 1) int enable) {
        data = new byte[]{
                (byte) 0xED,
                (byte) 0x01,
                (byte) ParamsKeyEnum.KEY_SOS_ENABLE.getParamsKey(),
                (byte) 0x01,
                (byte) enable
        };
    }

    public void setSOSReportInterval(@IntRange(from = 1, to = 10) int interval) {
        data = new byte[]{
                (byte) 0xED,
                (byte) 0x01,
                (byte) ParamsKeyEnum.KEY_SOS_REPORT_INTERVAL.getParamsKey(),
                (byte) 0x01,
                (byte) interval
        };
    }

    public void setSOSOptionalPayload(@IntRange(from = 0, to = 255) int payload) {
        data = new byte[]{
                (byte) 0xED,
                (byte) 0x01,
                (byte) ParamsKeyEnum.KEY_OPTIONAL_PAYLOAD_SOS.getParamsKey(),
                (byte) 0x01,
                (byte) payload
        };
    }

    public void setGPSEnable(@IntRange(from = 0, to = 1) int enable) {
        data = new byte[]{
                (byte) 0xED,
                (byte) 0x01,
                (byte) ParamsKeyEnum.KEY_GPS_FUNCTION_SWITCH.getParamsKey(),
                (byte) 0x01,
                (byte) enable
        };
    }

    public void setGPSReportInterval(@IntRange(from = 10, to = 200) int interval) {
        data = new byte[]{
                (byte) 0xED,
                (byte) 0x01,
                (byte) ParamsKeyEnum.KEY_GPS_REPORT_INTERVAL.getParamsKey(),
                (byte) 0x01,
                (byte) interval
        };
    }

    public void setGPSSearchTime(@IntRange(from = 1, to = 10) int time) {
        data = new byte[]{
                (byte) 0xED,
                (byte) 0x01,
                (byte) ParamsKeyEnum.KEY_GPS_SATELLITE_SEARCH_TIME.getParamsKey(),
                (byte) 0x01,
                (byte) time
        };
    }

    public void setGPSOptionalPayload(@IntRange(from = 0, to = 255) int payload) {
        data = new byte[]{
                (byte) 0xED,
                (byte) 0x01,
                (byte) ParamsKeyEnum.KEY_OPTIONAL_PAYLOAD_GPS.getParamsKey(),
                (byte) 0x01,
                (byte) payload
        };
    }

    public void set3AxisEnable(@IntRange(from = 0, to = 1) int enable) {
        data = new byte[]{
                (byte) 0xED,
                (byte) 0x01,
                (byte) ParamsKeyEnum.KEY_THREE_AXIS_ENABLE.getParamsKey(),
                (byte) 0x01,
                (byte) enable
        };
    }

    public void set3AxisDataEnable(@IntRange(from = 0, to = 1) int enable) {
        data = new byte[]{
                (byte) 0xED,
                (byte) 0x01,
                (byte) ParamsKeyEnum.KEY_THREE_AXIS_DATA_ENABLE.getParamsKey(),
                (byte) 0x01,
                (byte) enable
        };
    }

    public void set3AxisSampleRate(@IntRange(from = 0, to = 4) int sampleRate) {
        data = new byte[]{
                (byte) 0xED,
                (byte) 0x01,
                (byte) ParamsKeyEnum.KEY_THREE_AXIS_SAMPLE_RATE.getParamsKey(),
                (byte) 0x01,
                (byte) sampleRate
        };
    }

    public void set3AxisG(@IntRange(from = 0, to = 3) int g) {
        data = new byte[]{
                (byte) 0xED,
                (byte) 0x01,
                (byte) ParamsKeyEnum.KEY_THREE_AXIS_G.getParamsKey(),
                (byte) 0x01,
                (byte) g
        };
    }

    public void set3AxisTriggerSensitivity(@IntRange(from = 7, to = 255) int triggerSensitivity) {
        data = new byte[]{
                (byte) 0xED,
                (byte) 0x01,
                (byte) ParamsKeyEnum.KEY_THREE_AXIS_TRIGGER_SENSITIVITY.getParamsKey(),
                (byte) 0x01,
                (byte) triggerSensitivity
        };
    }

    public void set3AxisReportInterval(@IntRange(from = 1, to = 60) int reportInterval) {
        data = new byte[]{
                (byte) 0xED,
                (byte) 0x01,
                (byte) ParamsKeyEnum.KEY_THREE_AXIS_REPORT_INTERVAL.getParamsKey(),
                (byte) 0x01,
                (byte) reportInterval
        };
    }

    public void set3AxisOptionalPayload(@IntRange(from = 0, to = 255) int payload) {
        data = new byte[]{
                (byte) 0xED,
                (byte) 0x01,
                (byte) ParamsKeyEnum.KEY_OPTIONAL_PAYLOAD_THREE_AXIS.getParamsKey(),
                (byte) 0x01,
                (byte) payload
        };
    }

    public void setTimeSyncInterval(@IntRange(from = 0, to = 240) int timeSyncInterval) {
        data = new byte[]{
                (byte) 0xED,
                (byte) 0x01,
                (byte) ParamsKeyEnum.KEY_TIME_SYNC_INTERVAL.getParamsKey(),
                (byte) 0x01,
                (byte) timeSyncInterval
        };
    }

    public void setNetworkCheckInterval(@IntRange(from = 0, to = 240) int interval) {
        data = new byte[]{
                (byte) 0xED,
                (byte) 0x01,
                (byte) ParamsKeyEnum.KEY_NETWORK_CHECK_INTERVAL.getParamsKey(),
                (byte) 0x01,
                (byte) interval
        };
    }

    public void setReportLocationEnable(@IntRange(from = 0, to = 1) int enable) {
        data = new byte[]{
                (byte) 0xED,
                (byte) 0x01,
                (byte) ParamsKeyEnum.KEY_REPORT_LOCATION_ENABLE.getParamsKey(),
                (byte) 0x01,
                (byte) enable
        };
    }
}
