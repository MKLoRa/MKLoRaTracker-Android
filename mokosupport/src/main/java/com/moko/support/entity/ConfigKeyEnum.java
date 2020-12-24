package com.moko.support.entity;


import java.io.Serializable;

public enum ConfigKeyEnum implements Serializable {

    KEY_IBEACON_UUID(0x20),
    KEY_IBEACON_MAJOR(0x21),
    KEY_IBEACON_MINOR(0x22),
    KEY_MEASURE_POWER(0x23),
    KEY_TRANSMISSION(0x24),
    KEY_ADV_INTERVAL(0x25),
    KEY_ADV_NAME(0x26),

    KEY_PASSWORD(0x27),

    KEY_SCAN_INTERVAL(0x28),
    KEY_SCAN_WINDOW(0x29),
    KEY_CONNECTABLE(0x2A),
    KEY_ALARM_RSSI(0x2B),
    KEY_LORA_REPORT_INTERVAL(0x2C),
    KEY_ALARM_NOTIFY(0x2D),

    KEY_LORA_MODE(0x2E),
    KEY_LORA_REGION(0x2F),
    KEY_LORA_DEV_EUI(0x30),
    KEY_LORA_APP_EUI(0x31),
    KEY_LORA_APP_KEY(0x32),
    KEY_LORA_DEV_ADDR(0x33),
    KEY_LORA_APP_SKEY(0x34),
    KEY_LORA_NWK_SKEY(0x35),
    KEY_LORA_CH(0x36),
    KEY_LORA_DR(0x37),
    KEY_LORA_ADR(0x38),
    KEY_LORA_MESSAGE_TYPE(0x39),

    KEY_VIBRATION_INTENSITY(0x3A),
    KEY_VIBRATION_DURATION(0x3B),
    KEY_VIBRATION_CYCLE(0x3C),
    KEY_VIBRATION_NUMBER(0x3D),

    KEY_TIME(0x3E),
    KEY_LORA_CONNECTABLE(0x3F),
    KEY_DEVICE_MAC(0x40),
    KEY_BATTERY(0x41),

    KEY_FILTER_RSSI_A(0x42),
    KEY_FILTER_ADV_NAME_A(0x43),
    KEY_FILTER_MAC_A(0x44),
    KEY_FILTER_MAJOR_RANGE_A(0x45),
    KEY_FILTER_MINOR_RANGE_A(0x46),
    KEY_FILTER_UUID_A(0x47),
    KEY_FILTER_ADV_RAW_DATA_A(0x48),

    KEY_FILTER_RSSI_B(0x49),
    KEY_FILTER_ADV_NAME_B(0x4A),
    KEY_FILTER_MAC_B(0x4B),
    KEY_FILTER_MAJOR_RANGE_B(0x4C),
    KEY_FILTER_MINOR_RANGE_B(0x4D),
    KEY_FILTER_UUID_B(0x4E),
    KEY_FILTER_ADV_RAW_DATA_B(0x4F),

    KEY_FILTER_SWITCH_A(0x50),
    KEY_FILTER_SWITCH_B(0x51),
    KEY_FILTER_A_B_RELATION(0x52),

    KEY_DEVICE_INFO_INTERVAL(0X53),
    KEY_WARNING_RSSI(0x54),
    KEY_WARNING_NUMBER(0x55),
    KEY_LOW_POWER_PERCENT(0x56),

    KEY_CH_DR_RESET(0x57),
    KEY_LORA_CONNECT(0x58),
    KEY_CLOSE(0x59),
    KEY_RESET(0x5A),
    ;

    private int configKey;

    ConfigKeyEnum(int configKey) {
        this.configKey = configKey;
    }


    public int getConfigKey() {
        return configKey;
    }

    public static ConfigKeyEnum fromConfigKey(int configKey) {
        for (ConfigKeyEnum configKeyEnum : ConfigKeyEnum.values()) {
            if (configKeyEnum.getConfigKey() == configKey) {
                return configKeyEnum;
            }
        }
        return null;
    }
}
