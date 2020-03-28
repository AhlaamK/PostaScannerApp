package com.postaplus.postascannerapp;


import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.FragmentTransaction;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.honeywell.aidc.BarcodeFailureEvent;
import com.honeywell.aidc.BarcodeReadEvent;
import com.honeywell.aidc.BarcodeReader;
import com.honeywell.aidc.ScannerUnavailableException;
import com.honeywell.aidc.TriggerStateChangeEvent;

import org.apache.commons.lang3.StringUtils;

import TabsPagerAdapter.TabsPagerDeliveryAdapter;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager.widget.ViewPager;
import koamtac.kdc.sdk.KDCConstants;
import koamtac.kdc.sdk.KDCData;
import koamtac.kdc.sdk.KDCReader;

import static com.postaplus.postascannerapp.HomeActivity.barcodeReader;


public class DeliveryActivity extends FragmentActivity implements
        BarcodeReader.BarcodeListener, BarcodeReader.TriggerListener,
        ActionBar.TabListener {

    static final byte[] TYPE_BT_OOB = "application/vnd.bluetooth.ep.oob".getBytes();
    public static String waybillFromScanner = "";
    public static String kdcscannercallfrom = "";
    public static View WCrootView;
    public static FragmentActivity WCActivity;
    public static View TArootView;
    public static FragmentActivity TAActivity;
    static boolean DonotInterruptKDCScan = true;
    public boolean isActivityActiveFlag = false;
    String route, routen;
    Resources _resources;
    Button _btnScan = null;
    DeliveryActivity _activity;
    KDCData ScannerData;
    KDCReader _kdcReader;
    Thread ThrKdc;
    SharedPreferences pref;
    TextView username;
    DatabaseHandler db;
    SQLiteDatabase sqldb;
    private ViewPager viewPager;
    private TabsPagerDeliveryAdapter mAdapter;
    private ActionBar actionBar;
    private String[] tabs = {"WC", "Print", "Transfer Accept"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_delivery);
        ActionBar localActionBar = getActionBar();
        localActionBar.setCustomView(R.layout.actionbar_layout);
        localActionBar.setDisplayShowTitleEnabled(false);
        localActionBar.setDisplayShowCustomEnabled(true);
        localActionBar.setDisplayUseLogoEnabled(true);
        localActionBar.setDisplayShowHomeEnabled(false);
        localActionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#1c181c")));

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        route = getIntent().getExtras().getString("routecode");
        routen = getIntent().getExtras().getString("routename");

        _activity = this;

        _resources = getResources();
        if (barcodeReader != null) {
            barcodeReader.addBarcodeListener(this);

        }
        username = findViewById(R.id.unametxt);


        pref = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        username.setText(pref.getString("uname", ""));


        viewPager = findViewById(R.id.pager);
        actionBar = getActionBar();
        mAdapter = new TabsPagerDeliveryAdapter(getSupportFragmentManager());

        viewPager.setAdapter(mAdapter);
        actionBar.setHomeButtonEnabled(false);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        for (String tab_name : tabs) {
            actionBar.addTab(actionBar.newTab().setText(tab_name)
                    .setTabListener(this));
        }


        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                actionBar.setSelectedNavigationItem(position);
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if (barcodeReader != null) {
            try {
                barcodeReader.claim();
            } catch (ScannerUnavailableException e) {
                e.printStackTrace();
                Toast.makeText(this, "Scanner unavailable", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (barcodeReader != null) {
            barcodeReader.removeBarcodeListener(_activity);
            barcodeReader.release();
        }

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {

            if (barcodeReader != null) {
                barcodeReader.release();
                barcodeReader.removeBarcodeListener(DeliveryActivity.this);
            }

            Intent intent = new Intent(DeliveryActivity.this, HomeActivity.class);
            intent.putExtra("route", route);
            intent.putExtra("route1", routen);

            startActivity(intent);

        }
        return false;
    }

    @Override
    public void onTabReselected(Tab tab, FragmentTransaction ft) {
    }

    @Override
    public void onTabSelected(Tab tab, FragmentTransaction ft) {
        viewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(Tab tab, FragmentTransaction ft) {
    }

    @Override
    public void onBarcodeEvent(final BarcodeReadEvent event) {
        DeliveryActivity.this.runOnUiThread(() -> {
            String barcodeData = event.getBarcodeData();
            if (barcodeData != null) {
                waybillFromScanner = event.getBarcodeData();
                if (Check_ValidWaybill(event.getBarcodeData())) {
                    if (kdcscannercallfrom.equals("WCFragment")) {
                        _activity.runOnUiThread(() -> {
                            final Delivery_wc_fragment fragment = new Delivery_wc_fragment();
                            fragment.startASycncScanerExecute();
                        });
                    } else if (kdcscannercallfrom.equals("TAFragment")) {
                        _activity.runOnUiThread(() -> {
                            Delivery_ta_fragment fragment2 = new Delivery_ta_fragment();
                            fragment2.startASycncScanerTAExecute();

                        });
                    }
                } else {

                    _activity.runOnUiThread(() -> Toast.makeText(_activity, "Invalid Waybill", Toast.LENGTH_LONG).show());
                }
            } else {
                _activity.runOnUiThread(() -> Toast.makeText(_activity, "Invalid Waybill", Toast.LENGTH_LONG).show());
            }
        });


    }

    @Override
    public void onFailureEvent(BarcodeFailureEvent barcodeFailureEvent) {

    }

    @Override
    public void onTriggerEvent(TriggerStateChangeEvent triggerStateChangeEvent) {

    }

    public void ConnectionChanged(final BluetoothDevice device, int state) {
        switch (state) {

            case KDCConstants.CONNECTION_STATE_CONNECTED:
                _activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {


                        Toast.makeText(_activity, "Scanner Connected", Toast.LENGTH_LONG).show();
                        isActivityActiveFlag = true;
                    }
                });
                break;

            case KDCConstants.CONNECTION_STATE_LOST:
                _activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        Toast.makeText(_activity, "Scanner Connection Lost", Toast.LENGTH_LONG).show();
                        isActivityActiveFlag = true;
                    }
                });
                break;
        }
    }

    public void DataReceived(KDCData pData) {
    }

    public static boolean Check_ValidWaybill(String s) {

        if (s.length() == 10 || s.length() == 12) {
            return StringUtils.isNumeric(s);
        } else if (s.length() == 18) {
            return StringUtils.isAlphanumeric(s);
        }
        return false;
    }


}
