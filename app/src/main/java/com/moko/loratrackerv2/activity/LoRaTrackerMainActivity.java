package com.moko.loratrackerv2.activity;

import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.elvishew.xlog.XLog;
import com.moko.ble.lib.MokoConstants;
import com.moko.ble.lib.event.ConnectStatusEvent;
import com.moko.ble.lib.event.OrderTaskResponseEvent;
import com.moko.ble.lib.task.OrderTask;
import com.moko.ble.lib.task.OrderTaskResponse;
import com.moko.loratrackerv2.AppConstants;
import com.moko.loratrackerv2.BuildConfig;
import com.moko.loratrackerv2.R;
import com.moko.loratrackerv2.R2;
import com.moko.loratrackerv2.adapter.BeaconListAdapter;
import com.moko.loratrackerv2.dialog.AlertMessageDialog;
import com.moko.loratrackerv2.dialog.LoadingDialog;
import com.moko.loratrackerv2.dialog.LoadingMessageDialog;
import com.moko.loratrackerv2.dialog.PasswordDialog;
import com.moko.loratrackerv2.dialog.ScanFilterDialog;
import com.moko.loratrackerv2.entity.BeaconInfo;
import com.moko.loratrackerv2.utils.BeaconInfoParseableImpl;
import com.moko.loratrackerv2.utils.SPUtiles;
import com.moko.loratrackerv2.utils.ToastUtils;
import com.moko.support.loratracker.LoRaTrackerMokoSupport;
import com.moko.support.loratracker.MokoBleScanner;
import com.moko.support.loratracker.OrderTaskAssembler;
import com.moko.support.loratracker.callback.MokoScanDeviceCallback;
import com.moko.support.loratracker.entity.DeviceInfo;
import com.moko.support.loratracker.entity.OrderCHAR;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class LoRaTrackerMainActivity extends BaseActivity implements MokoScanDeviceCallback, BaseQuickAdapter.OnItemChildClickListener {
    @BindView(R2.id.iv_refresh)
    ImageView ivRefresh;
    @BindView(R2.id.rv_devices)
    RecyclerView rvDevices;
    @BindView(R2.id.tv_device_num)
    TextView tvDeviceNum;
    @BindView(R2.id.rl_edit_filter)
    RelativeLayout rl_edit_filter;
    @BindView(R2.id.rl_filter)
    RelativeLayout rl_filter;
    @BindView(R2.id.tv_filter)
    TextView tv_filter;
    private boolean mReceiverTag = false;
    private HashMap<String, BeaconInfo> beaconInfoHashMap;
    private ArrayList<BeaconInfo> beaconInfos;
    private BeaconListAdapter adapter;
    private Animation animation = null;
    private MokoBleScanner mokoBleScanner;
    public Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loratracker_activity_main);
        ButterKnife.bind(this);
        LoRaTrackerMokoSupport.getInstance().init(getApplicationContext());
        mSavedPassword = SPUtiles.getStringValue(this, AppConstants.SP_KEY_SAVED_PASSWORD, "");
        beaconInfoHashMap = new HashMap<>();
        beaconInfos = new ArrayList<>();
        adapter = new BeaconListAdapter();
        adapter.replaceData(beaconInfos);
        adapter.setOnItemChildClickListener(this);
        adapter.openLoadAnimation();
        rvDevices.setLayoutManager(new LinearLayoutManager(this));
        DividerItemDecoration itemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        itemDecoration.setDrawable(ContextCompat.getDrawable(this, R.drawable.loratracker_shape_recycleview_divider));
        rvDevices.addItemDecoration(itemDecoration);
        rvDevices.setAdapter(adapter);
        mHandler = new Handler(Looper.getMainLooper());
        mokoBleScanner = new MokoBleScanner(this);
        EventBus.getDefault().register(this);
        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        registerReceiver(mReceiver, filter);
        mReceiverTag = true;
        if (!LoRaTrackerMokoSupport.getInstance().isBluetoothOpen()) {
            LoRaTrackerMokoSupport.getInstance().enableBluetooth();
        } else {
            if (animation == null) {
                startScan();
            }
        }
    }

    private void startScan() {
        if (!LoRaTrackerMokoSupport.getInstance().isBluetoothOpen()) {
            LoRaTrackerMokoSupport.getInstance().enableBluetooth();
            return;
        }
        animation = AnimationUtils.loadAnimation(this, R.anim.loratracker_rotate_refresh);
        ivRefresh.startAnimation(animation);
        beaconInfoParseable = new BeaconInfoParseableImpl();
        mokoBleScanner.startScanDevice(this);
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mokoBleScanner.stopScanDevice();
            }
        }, 1000 * 60);
    }


    private BeaconInfoParseableImpl beaconInfoParseable;
    public String filterName;
    public int filterRssi = -100;

    @Override
    public void onStartScan() {
        beaconInfoHashMap.clear();
        new Thread(() -> {
            while (animation != null) {
                runOnUiThread(() -> {
                    adapter.replaceData(beaconInfos);
                    tvDeviceNum.setText(String.format("DEVICE(%d)", beaconInfos.size()));
                });
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                updateDevices();
            }
        }).start();
    }

    @Override
    public void onScanDevice(DeviceInfo deviceInfo) {
        BeaconInfo beaconInfo = beaconInfoParseable.parseDeviceInfo(deviceInfo);
        if (beaconInfo == null)
            return;
        beaconInfoHashMap.put(beaconInfo.mac, beaconInfo);
    }

    @Override
    public void onStopScan() {
        ivRefresh.clearAnimation();
        animation = null;
    }

    private void updateDevices() {
        beaconInfos.clear();
        if (!TextUtils.isEmpty(filterName) || filterRssi != -100) {
            ArrayList<BeaconInfo> beaconInfosFilter = new ArrayList<>(beaconInfoHashMap.values());
            Iterator<BeaconInfo> iterator = beaconInfosFilter.iterator();
            while (iterator.hasNext()) {
                BeaconInfo beaconInfo = iterator.next();
                if (beaconInfo.rssi > filterRssi) {
                    if (TextUtils.isEmpty(filterName)) {
                        continue;
                    } else {
                        if (TextUtils.isEmpty(beaconInfo.name) && TextUtils.isEmpty(beaconInfo.mac)) {
                            iterator.remove();
                        } else if (TextUtils.isEmpty(beaconInfo.name) && beaconInfo.mac.toLowerCase().replaceAll(":", "").contains(filterName.toLowerCase())) {
                            continue;
                        } else if (TextUtils.isEmpty(beaconInfo.mac) && beaconInfo.name.toLowerCase().contains(filterName.toLowerCase())) {
                            continue;
                        } else if (!TextUtils.isEmpty(beaconInfo.name) && !TextUtils.isEmpty(beaconInfo.mac) && (beaconInfo.name.toLowerCase().contains(filterName.toLowerCase()) || beaconInfo.mac.toLowerCase().replaceAll(":", "").contains(filterName.toLowerCase()))) {
                            continue;
                        } else {
                            iterator.remove();
                        }
                    }
                } else {
                    iterator.remove();
                }
            }
            beaconInfos.addAll(beaconInfosFilter);
        } else {
            beaconInfos.addAll(beaconInfoHashMap.values());
        }
        Collections.sort(beaconInfos, (lhs, rhs) -> {
            if (lhs.rssi > rhs.rssi) {
                return -1;
            } else if (lhs.rssi < rhs.rssi) {
                return 1;
            }
            return 0;
        });
    }

    public void onRefresh(View view) {
        if (isWindowLocked())
            return;
        if (!LoRaTrackerMokoSupport.getInstance().isBluetoothOpen()) {
            LoRaTrackerMokoSupport.getInstance().enableBluetooth();
            return;
        }
        if (animation == null) {
            startScan();
        } else {
            mHandler.removeMessages(0);
            mokoBleScanner.stopScanDevice();
        }
    }

    public void onAbout(View view) {
        startActivity(new Intent(this, AboutActivity.class));
    }


    public void onFilter(View view) {
        if (animation != null) {
            mHandler.removeMessages(0);
            mokoBleScanner.stopScanDevice();
        }
        ScanFilterDialog scanFilterDialog = new ScanFilterDialog(this);
        scanFilterDialog.setFilterName(filterName);
        scanFilterDialog.setFilterRssi(filterRssi);
        scanFilterDialog.setOnScanFilterListener(new ScanFilterDialog.OnScanFilterListener() {
            @Override
            public void onDone(String filterName, int filterRssi) {
                LoRaTrackerMainActivity.this.filterName = filterName;
                LoRaTrackerMainActivity.this.filterRssi = filterRssi;
                if (!TextUtils.isEmpty(filterName) || filterRssi != -100) {
                    rl_filter.setVisibility(View.VISIBLE);
                    rl_edit_filter.setVisibility(View.GONE);
                    StringBuilder stringBuilder = new StringBuilder();
                    if (!TextUtils.isEmpty(filterName)) {
                        stringBuilder.append(filterName);
                        stringBuilder.append(";");
                    }
                    if (filterRssi != -100) {
                        stringBuilder.append(String.format("%sdBm", filterRssi + ""));
                        stringBuilder.append(";");
                    }
                    tv_filter.setText(stringBuilder.toString());
                } else {
                    rl_filter.setVisibility(View.GONE);
                    rl_edit_filter.setVisibility(View.VISIBLE);
                }
                if (isWindowLocked())
                    return;
                if (animation == null) {
                    startScan();
                }
            }
        });
        scanFilterDialog.setOnDismissListener(dialog -> {
            if (isWindowLocked())
                return;
            if (animation == null) {
                startScan();
            }
        });
        scanFilterDialog.show();
    }

    public void onFilterDelete(View view) {
        if (animation != null) {
            mHandler.removeMessages(0);
            mokoBleScanner.stopScanDevice();
        }
        rl_filter.setVisibility(View.GONE);
        rl_edit_filter.setVisibility(View.VISIBLE);
        filterName = "";
        filterRssi = -100;
        if (isWindowLocked())
            return;
        if (animation == null) {
            startScan();
        }
    }

    private String mPassword;
    private String mSavedPassword;

    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        if (!LoRaTrackerMokoSupport.getInstance().isBluetoothOpen()) {
            LoRaTrackerMokoSupport.getInstance().enableBluetooth();
            return;
        }
        BeaconInfo beaconInfo = (BeaconInfo) adapter.getItem(position);
        if (beaconInfo != null && beaconInfo.connectable == 1 && !isFinishing()) {
            if (animation != null) {
                mHandler.removeMessages(0);
                mokoBleScanner.stopScanDevice();
            }
            // show password
            final PasswordDialog dialog = new PasswordDialog(LoRaTrackerMainActivity.this);
            dialog.setData(mSavedPassword);
            dialog.setOnPasswordClicked(new PasswordDialog.PasswordClickListener() {
                @Override
                public void onEnsureClicked(String password) {
                    if (!LoRaTrackerMokoSupport.getInstance().isBluetoothOpen()) {
                        LoRaTrackerMokoSupport.getInstance().enableBluetooth();
                        return;
                    }
                    XLog.i(password);
                    mPassword = password;
                    if (animation != null) {
                        mHandler.removeMessages(0);
                        mokoBleScanner.stopScanDevice();
                    }
                    showLoadingProgressDialog();
                    ivRefresh.postDelayed(() -> LoRaTrackerMokoSupport.getInstance().connDevice(beaconInfo.mac), 500);
                }

                @Override
                public void onDismiss() {

                }
            });
            dialog.show();
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {

                @Override
                public void run() {
                    runOnUiThread(() -> dialog.showKeyboard());
                }
            }, 200);
        }
    }

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null) {
                String action = intent.getAction();
                if (BluetoothAdapter.ACTION_STATE_CHANGED.equals(action)) {
                    int blueState = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, 0);
                    switch (blueState) {
                        case BluetoothAdapter.STATE_TURNING_OFF:
                            if (animation != null) {
                                mHandler.removeMessages(0);
                                mokoBleScanner.stopScanDevice();
                                onStopScan();
                            }
                            break;
                        case BluetoothAdapter.STATE_ON:
                            if (animation == null) {
                                startScan();
                            }
                            break;
                    }
                }
            }
        }
    };

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onConnectStatusEvent(ConnectStatusEvent event) {
        String action = event.getAction();
        if (MokoConstants.ACTION_DISCONNECTED.equals(action)) {
            mPassword = "";
            dismissLoadingProgressDialog();
            dismissLoadingMessageDialog();
            ToastUtils.showToast(LoRaTrackerMainActivity.this, "Connection Failed");
            if (animation == null) {
                startScan();
            }
        }
        if (MokoConstants.ACTION_DISCOVER_SUCCESS.equals(action)) {
            dismissLoadingProgressDialog();
            showLoadingMessageDialog();
            mHandler.postDelayed(() -> {
                // open password notify and set passwrord
                List<OrderTask> orderTasks = new ArrayList<>();
                orderTasks.add(OrderTaskAssembler.setPassword(mPassword));
                LoRaTrackerMokoSupport.getInstance().sendOrder(orderTasks.toArray(new OrderTask[]{}));
            }, 500);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onOrderTaskResponseEvent(OrderTaskResponseEvent event) {
        final String action = event.getAction();
        if (MokoConstants.ACTION_ORDER_TIMEOUT.equals(action)) {
        }
        if (MokoConstants.ACTION_ORDER_FINISH.equals(action)) {
        }
        if (MokoConstants.ACTION_ORDER_RESULT.equals(action)) {
            OrderTaskResponse response = event.getResponse();
            OrderCHAR orderCHAR = (OrderCHAR) response.orderCHAR;
            int responseType = response.responseType;
            byte[] value = response.responseValue;
            switch (orderCHAR) {
                case CHAR_PASSWORD:
                    dismissLoadingMessageDialog();
                    if (value.length == 5) {
                        int header = value[0] & 0xFF;// 0xED
                        int flag = value[1] & 0xFF;// read or write
                        int cmd = value[2] & 0xFF;
                        if (header != 0xED)
                            return;
                        int length = value[3] & 0xFF;
                        if (flag == 0x01 && cmd == 0x01 && length == 0x01) {
                            int result = value[4] & 0xFF;
                            if (1 == result) {
                                mSavedPassword = mPassword;
                                SPUtiles.setStringValue(LoRaTrackerMainActivity.this, AppConstants.SP_KEY_SAVED_PASSWORD, mSavedPassword);
                                XLog.i("Success");
                                Intent i = new Intent(LoRaTrackerMainActivity.this, DeviceInfoActivity.class);
                                startActivityForResult(i, AppConstants.REQUEST_CODE_DEVICE_INFO);
                            }
                            if (0 == result) {
                                ToastUtils.showToast(LoRaTrackerMainActivity.this, "Password Error");
                                LoRaTrackerMokoSupport.getInstance().disConnectBle();
                            }
                        }
                    }
            }
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AppConstants.REQUEST_CODE_DEVICE_INFO) {
            if (resultCode == RESULT_OK) {
                if (animation == null) {
                    startScan();
                }
            }
        }
    }

    private LoadingDialog mLoadingDialog;

    private void showLoadingProgressDialog() {
        mLoadingDialog = new LoadingDialog();
        mLoadingDialog.show(getSupportFragmentManager());

    }

    private void dismissLoadingProgressDialog() {
        if (mLoadingDialog != null)
            mLoadingDialog.dismissAllowingStateLoss();
    }

    private LoadingMessageDialog mLoadingMessageDialog;

    private void showLoadingMessageDialog() {
        mLoadingMessageDialog = new LoadingMessageDialog();
        mLoadingMessageDialog.setMessage("Verifying..");
        mLoadingMessageDialog.show(getSupportFragmentManager());

    }

    private void dismissLoadingMessageDialog() {
        if (mLoadingMessageDialog != null)
            mLoadingMessageDialog.dismissAllowingStateLoss();
    }

    @Override
    public void onBackPressed() {
        if (BuildConfig.IS_LIBRARY) {
            finish();
        } else {
            AlertMessageDialog dialog = new AlertMessageDialog();
            dialog.setMessage(R.string.main_exit_tips);
            dialog.setOnAlertConfirmListener(() -> LoRaTrackerMainActivity.this.finish());
            dialog.show(getSupportFragmentManager());
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mReceiverTag) {
            mReceiverTag = false;
            unregisterReceiver(mReceiver);
        }
        EventBus.getDefault().unregister(this);
    }
}
