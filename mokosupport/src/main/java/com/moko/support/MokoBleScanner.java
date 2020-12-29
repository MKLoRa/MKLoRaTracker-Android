package com.moko.support;

import android.Manifest;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.ParcelUuid;
import android.text.TextUtils;

import com.elvishew.xlog.XLog;
import com.moko.ble.lib.utils.MokoUtils;
import com.moko.support.callback.MokoScanDeviceCallback;
import com.moko.support.entity.DeviceInfo;
import com.moko.support.entity.OrderServices;

import java.util.ArrayList;
import java.util.List;

import androidx.core.content.ContextCompat;
import no.nordicsemi.android.support.v18.scanner.BluetoothLeScannerCompat;
import no.nordicsemi.android.support.v18.scanner.ScanCallback;
import no.nordicsemi.android.support.v18.scanner.ScanFilter;
import no.nordicsemi.android.support.v18.scanner.ScanResult;
import no.nordicsemi.android.support.v18.scanner.ScanSettings;

public final class MokoBleScanner {

    private MokoLeScanHandler mMokoLeScanHandler;
    private MokoScanDeviceCallback mMokoScanDeviceCallback;

    private Context mContext;

    public void startScanDevice(Context context) {
        mContext = context;
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            XLog.i("Start scan");
        }
        final BluetoothLeScannerCompat scanner = BluetoothLeScannerCompat.getScanner();
        ScanSettings settings = new ScanSettings.Builder()
                .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
                .build();
        List<ScanFilter> scanFilterList = new ArrayList<>();
        ScanFilter.Builder builder = new ScanFilter.Builder();
        builder.setServiceUuid(new ParcelUuid(OrderServices.SERVICE_ADV.getUuid()));
        scanFilterList.add(builder.build());
//        List<ScanFilter> scanFilterList = Collections.singletonList(new ScanFilter.Builder().build());
        mMokoLeScanHandler = new MokoLeScanHandler(mMokoScanDeviceCallback);
        scanner.startScan(scanFilterList, settings, mMokoLeScanHandler);
        mMokoScanDeviceCallback.onStartScan();
    }

    public void stopScanDevice() {
        if (mMokoLeScanHandler != null && mMokoScanDeviceCallback != null) {
            if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                XLog.i("End scan");
            }
            final BluetoothLeScannerCompat scanner = BluetoothLeScannerCompat.getScanner();
            scanner.stopScan(mMokoLeScanHandler);
            mMokoScanDeviceCallback.onStopScan();
            mMokoLeScanHandler = null;
            mMokoScanDeviceCallback = null;
        }
    }

    public static class MokoLeScanHandler extends ScanCallback {

        private MokoScanDeviceCallback callback;

        public MokoLeScanHandler(MokoScanDeviceCallback callback) {
            this.callback = callback;
        }

        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            if (result != null) {
                BluetoothDevice device = result.getDevice();
                byte[] scanRecord = result.getScanRecord().getBytes();
                String name = result.getScanRecord().getDeviceName();
                int rssi = result.getRssi();
                if (TextUtils.isEmpty(name) || scanRecord.length == 0 || rssi == 127) {
                    return;
                }
                DeviceInfo deviceInfo = new DeviceInfo();
                deviceInfo.name = name;
                deviceInfo.rssi = rssi;
                deviceInfo.mac = device.getAddress();
                String scanRecordStr = MokoUtils.bytesToHexString(scanRecord);
                deviceInfo.scanRecord = scanRecordStr;
                deviceInfo.scanResult = result;
                callback.onScanDevice(deviceInfo);
            }
        }
    }


    public void setMokoScanDeviceCallback(MokoScanDeviceCallback mokoScanDeviceCallback) {
        this.mMokoScanDeviceCallback = mokoScanDeviceCallback;
    }
}
