package com.postaplus.postascannerapp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.honeywell.aidc.AidcManager;
import com.honeywell.aidc.BarcodeFailureEvent;
import com.honeywell.aidc.BarcodeReadEvent;
import com.honeywell.aidc.BarcodeReader;
import com.honeywell.aidc.TriggerStateChangeEvent;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import DatabaseHandlers.DBFCS.TBLogin;
import DatabaseHandlers.DBFunctions;
import androidx.core.app.ActivityCompat;
import webservice.FuncClasses.Couriers;
import webservice.FuncClasses.Events;
import webservice.FuncClasses.HoldWayBills;
import webservice.FuncClasses.OpenRst;
import webservice.FuncClasses.PayType;
import webservice.FuncClasses.PickUpDt;
import webservice.FuncClasses.PickUpWaybillsDT;
import webservice.FuncClasses.Remarks;
import webservice.FuncClasses.Routes;
import webservice.FuncClasses.RstDetail;
import webservice.FuncClasses.ScanWaybillDt;
import webservice.FuncClasses.Service;
import webservice.FuncClasses.setPickUpDt;
import webservice.WebService;

import static android.os.AsyncTask.Status.PENDING;

//import static com.postaplus.postascannerapp.LoginActivity.acknumbr;
//import static com.postaplus.postascannerapp.LoginActivity.retrno;
//import static com.postaplus.postascannerapp.LoginActivity.rstnumbr;

public class HomeActivity extends MasterActivity implements BarcodeReader.BarcodeListener, BarcodeReader.TriggerListener {

    public static boolean errored = false;
    public static BarcodeReader barcodeReader;
    public static String retrno, rstnumbr, acknumbr, openrstERR;
    private static BarcodeReader barcodeReadernew;
    public AidcManager manager;
    String[] pickupno1, acname1, pickaddr1, pickarea1, contact_person1, pick_phone1, picktime1, pickuperror1;
    String[] waybillhold, cnamehold, phonehold, areahold, companyhold, civilidhold, serialhold, cardtypehold, deldatehold, deltimehold, amounthold, transcourierhold, addresshold;
    String[] attempt1, waybill1, rname1, cname1, phone, area1, company1, civilid1, serial1, cardtype1, deldate1, deltime1, amount1, error1, address1;
    String[] pickupno12, wbill12, paytype12, amount12, service12;
    boolean clear_channelstatus;
    GPSTracker gps;
    double latitude, longitude;
    String lati, longti, serialid, area;
    int count;
    boolean syncstatus, mstatus, calststus;
    String holdstatus, wc_transfer_status, deliverystatus, rootstatus;
    TextView username, routtxt, pendingtxt;
    ImageView delvr, logout, hold, transfer, pickup, sync, routec, fuel, summary;
    String drivercode, odometervalue, runsheet1, rte, rte1;
    //OpenRst runsheet;
    Context mContext;
    Routes[] routesresponse;
    ScanWaybillDt[] scanwbildtResponse;
    Events[] eventResponse;
    PickUpDt[] pickupdtResponse;
    PickUpWaybillsDT[] pickupwblldtResponse;
    HoldWayBills[] holddwaybillResponse;
    Couriers[] courierResponse;
    RstDetail[] rstdetailResponse;
    PayType[] paytypeResponse;
    Service[] serviceResponse;
    ProgressBar mPb;
    View rootView;
    Remarks[] pickupremarksResponse;
    SQLiteDatabase db1;
    String pick_status;
    HomeActivity _activity;
    setPickUpDt[] setpkpdtRequest;
    String AppVersion = "";
    TextView versionDisp;
    ActivityNotification actNoty = new ActivityNotification();
    Bitmap bitmap, bitmap1, bitmap2;
    List<String> waynillist = new ArrayList<String>();

    OpenRst runsheet;
    boolean Connected;
    int oc1 = 0, oc = 0, fc = 0, c = 0, c1 = 0, c2 = 0, c3 = 0, c4 = 0;
    private String[] routename, routecode, eventid, eventdesc, serviceID, serviceType, PayId, PayType, couriercode, couriername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_home);

        String mVersion = "";
        try {
            mVersion = this.getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            mVersion = "";
        }
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .detectAll() // or .detectAll() for all detectable problems
                .penaltyLog()
                .build());

        AppVersion = mVersion;
        System.out.println("versionDisp before home: " + versionDisp);
        System.out.println("Application Version in home: " + AppVersion);
        versionDisp = (TextView) findViewById(R.id.vrnTxt);
        System.out.println("versionDisp in home: " + versionDisp);
        versionDisp.setText("Version " + AppVersion);

        mContext = this;
        System.out.println("Home Activity Page");
        //Save the value of route in a variable
        //route code


        rte = actNoty.getRouteCode(HomeActivity.this);
        rte1 = actNoty.getRouteName(HomeActivity.this);
        System.out.println("rrrr:" + rte + "," + rte1);

        pref = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);

        username = (TextView) findViewById(R.id.unametxt);
        routtxt = (TextView) findViewById(R.id.textroute);
        delvr = (ImageView) findViewById(R.id.imagedelivery);
        sync = (ImageView) findViewById(R.id.imagesync);
        logout = (ImageView) findViewById(R.id.imagelogout);
        routec = (ImageView) findViewById(R.id.imagerouteclose);
        hold = (ImageView) findViewById(R.id.imagehold);
        fuel = (ImageView) findViewById(R.id.imagefuel);
        pickup = (ImageView) findViewById(R.id.imagepickup);
        transfer = (ImageView) findViewById(R.id.imagetransfer);
        pendingtxt = (TextView) findViewById(R.id.pendingtext);
        summary = (ImageView) findViewById(R.id.imagesummary);
        mPb = (ProgressBar) findViewById(R.id.progressBarMain);

        username.setText(pref.getString("uname", ""));

        drivercode = username.getText().toString();
        actNoty.setdrivercode(HomeActivity.this, drivercode);
        System.out.println("drivercode home :" + drivercode);

        routtxt.setText("Route:" + rte1);

        _activity = this;

        GetOPENRRST();
        AidcManager.create(this, new AidcManager.CreatedCallback() {

                    @Override
                    public void onCreated(AidcManager aidcManager) {
                        manager = aidcManager;
                        barcodeReader = manager.createBarcodeReader();

                        System.out.println("manager in ocree:" + manager + "barcode is:" + barcodeReader);

                        try {
                            if (barcodeReader != null) {
                                //barcodeReader.claim();

                                System.out.println("barcodeReader in try:" + barcodeReader);
                            }
                            Map<String, Object> properties = new HashMap<String, Object>();
                            properties.put(BarcodeReader.PROPERTY_CODE_128_ENABLED, true);
                            properties.put(BarcodeReader.PROPERTY_GS1_128_ENABLED, true);
                            properties.put(BarcodeReader.PROPERTY_QR_CODE_ENABLED, true);
                            properties.put(BarcodeReader.PROPERTY_CODE_39_ENABLED, true);
                            properties.put(BarcodeReader.PROPERTY_DATAMATRIX_ENABLED, true);
                            properties.put(BarcodeReader.PROPERTY_UPC_A_ENABLE, true);
                            properties.put(BarcodeReader.PROPERTY_EAN_13_ENABLED, true);
                            properties.put(BarcodeReader.PROPERTY_AZTEC_ENABLED, true);
                            properties.put(BarcodeReader.PROPERTY_CODABAR_ENABLED, true);
                            properties.put(BarcodeReader.PROPERTY_INTERLEAVED_25_ENABLED, true);
                            properties.put(BarcodeReader.PROPERTY_PDF_417_ENABLED, false);
                            properties.put(BarcodeReader.TRIGGER_CONTROL_MODE_AUTO_CONTROL, true);
                            properties.put(BarcodeReader.PROPERTY_CODE_39_MAXIMUM_LENGTH, 10);
                            properties.put(BarcodeReader.PROPERTY_CENTER_DECODE, true);
                            properties.put(BarcodeReader.PROPERTY_NOTIFICATION_BAD_READ_ENABLED, true);

                            barcodeReader.addBarcodeListener(_activity);
                            barcodeReader.addTriggerListener(_activity);


                            System.out.println("barcodeReader in finish" + barcodeReader);
                        } catch (Exception e) {
                        }


                    }

                }
        );


        db = new DatabaseHandler(getBaseContext());
        sqldb = db.getReadableDatabase();
        Cursor c2 = sqldb.rawQuery("SELECT * FROM deliverydata WHERE Attempt_Status=0 AND Drivercode='" + drivercode + "'", null);
        int count1 = c2.getCount();
        c2.moveToFirst();
        //set the pending shipment count
        System.out.println("Number of shipment count is" + count1);
        pendingtxt.setText(String.valueOf(count1));
        //System.out.println(count1);
        c2.close();
        db.close();
        //System.out.println("Home stage2");


        delvr.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                v.startAnimation(AnimationUtils.loadAnimation(getBaseContext(), R.anim.image_click));
                try {
                    if (isNetworkConnected()) {
                        Connected = WebService.GET_SERVICE_STATUS(null);

                        GetOPENRRST();

                    }
                    System.out.println("Drivercode value in homeactivity is");
                    System.out.println(drivercode);
                    System.out.println("METHOD_NAME9 value in homeactivity is");
                    System.out.println(METHOD_NAME9);


                    System.out.println("rstnumbr are:" + rstnumbr);

                    if (rstnumbr != null && acknumbr != null && retrno != null && rstnumbr.equals("NA") && acknumbr.equals("NA") && retrno.equals("NA")) {
                        Intent int1 = new Intent(HomeActivity.this, DeliveryActivity.class);

                        int1.putExtra("routecode", rte);
                        int1.putExtra("routename", rte1);
                        sqldb = db.getWritableDatabase();
                        sqldb.execSQL("UPDATE logindata SET Runsheetcode='" + rstnumbr + "' WHERE Username='" + drivercode + "'");
                        ;
                        HomeActivity.this.startActivity(new Intent(int1));
                        return;

                    } else {


                        TBLogin ActiveLogin = DBFunctions.GetLoggedUser(HomeActivity.this);
                        db = new DatabaseHandler(HomeActivity.this);
                        System.out.println("Actv stts" + ActiveLogin.LOGIN_STATUS);
                        if (ActiveLogin.LOGIN_STATUS == 1) {
                            sqldb = db.getWritableDatabase();

                            sqldb.execSQL("UPDATE logindata SET Runsheetcode='" + rstnumbr + "' WHERE Username='" + drivercode + "'");
                        }
                        if (ActiveLogin.ODOMETER_VALUE == null) {

                            odometervalue = WebService.GET_ODOREADING(drivercode);
                            sqldb = db.getWritableDatabase();
                            if (odometervalue != null) {
                                if (odometervalue.equals("NO READING")) {
                                    sqldb.execSQL("UPDATE logindata SET Odometervalue=" + null + " WHERE Username='" + drivercode + "'");
                                }

                            } else {
                                sqldb.execSQL("UPDATE logindata SET Odometervalue=" + null + " WHERE Username='" + drivercode + "'");
                            }
                        }

                    }


                    sqldb = db.getReadableDatabase();
                    Cursor cc2 = sqldb.rawQuery("SELECT Odometervalue FROM logindata WHERE Username='" + drivercode + "'", null);
                    cc2.moveToFirst();

                    Integer count111 = cc2.getCount();

                    if (count111 > 0) {
                        odometervalue = cc2.getString(cc2.getColumnIndex("Odometervalue"));

                    }
                    cc2.close();
                    db.close();


                    System.out.println("strt are:" + rstnumbr);
                    Intent int1 = new Intent(HomeActivity.this, StartDeliveryActivity.class);

                    int1.putExtra("routecode", rte);
                    int1.putExtra("routename", rte1);
                    int1.putExtra("runsheetcode", rstnumbr);
                    startActivity(new Intent(int1));
                } catch (Exception e) {

                    e.printStackTrace();


                }

            }

        });

        pickup.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                try {
                    mPb.setVisibility(View.VISIBLE);
                    v.startAnimation(AnimationUtils.loadAnimation(getBaseContext(), R.anim.image_click));
                    Intent int1 = new Intent(HomeActivity.this, PickupActivity.class);
                    int1.putExtra("routecode", rte);
                    int1.putExtra("routename", rte1);
                    startActivity(new Intent(int1));
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {

                }
            }
        });
        fuel.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                try {
                    mPb.setVisibility(View.VISIBLE);
                    v.startAnimation(AnimationUtils.loadAnimation(getBaseContext(), R.anim.image_click));
                    Intent int1 = new Intent(HomeActivity.this, FuelActivity.class);
                    int1.putExtra("routecode", rte);
                    int1.putExtra("routename", rte1);
                    startActivity(new Intent(int1));
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {

                }

            }
        });


        hold.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                try {
                    mPb.setVisibility(View.VISIBLE);
                    v.startAnimation(AnimationUtils.loadAnimation(getBaseContext(), R.anim.image_click));
                    Intent int1 = new Intent(HomeActivity.this, HoldActivity.class);

                    int1.putExtra("routecode", rte);
                    int1.putExtra("routename", rte1);

                    startActivity(new Intent(int1));
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {

                }

            }
        });

        transfer.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                try {
                    mPb.setVisibility(View.VISIBLE);
                    v.startAnimation(AnimationUtils.loadAnimation(getBaseContext(), R.anim.image_click));
                    Intent int1 = new Intent(HomeActivity.this, TransferActivity.class);

                    int1.putExtra("routecode", rte);
                    int1.putExtra("routename", rte1);

                    startActivity(new Intent(int1));
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {

                }

            }
        });
        //syn code
        sync.setOnClickListener(syncButtonClicked());


        routec.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                v.startAnimation(AnimationUtils.loadAnimation(getBaseContext(), R.anim.image_click));
                mPb.setVisibility(View.VISIBLE);
                db = new DatabaseHandler(getBaseContext());

                SQLiteDatabase db12 = db.getReadableDatabase();


                Cursor fuelc = db12.rawQuery("SELECT * FROM Fueldatatable WHERE SyncStatus=1 AND ImageSyncstatus=0 AND Drivercode='" + drivercode + "'", null);
                int fc = fuelc.getCount();
                fuelc.close();
                if (fc > 0) {

                    Toast.makeText(getApplicationContext(), "Fuelpage image ack pending", Toast.LENGTH_LONG).show();
                    return;
                }


                Cursor rbc = db12.rawQuery("SELECT Waybill FROM deliverydata WHERE Attempt_Status<>1 ", null);
                int c = rbc.getCount();
                rbc.close();

                if (c > 0) {
                    Toast.makeText(getApplicationContext(), " Pending Waybills are there", Toast.LENGTH_LONG).show();
                    Intent int1 = new Intent(HomeActivity.this, Route_Close_Activity.class);

                    int1.putExtra("route", rte);
                    int1.putExtra("route1", rte1);
                    startActivity(new Intent(int1));
                    return;
                }

                //cursor to check if pending pickups are there
                Cursor rbc11 = db12.rawQuery("SELECT Pickup_No FROM pickuphead WHERE Status<>'C'", null);
                int c1 = rbc11.getCount();
                rbc11.close();

                int c12 = 0;
                if (c1 > 0) {
                    Toast.makeText(getApplicationContext(), "Pending Pickups are there", Toast.LENGTH_LONG).show();
                    return;
                } else {
                    Cursor rbc121 = db12.rawQuery("SELECT Pickup_No FROM pickuphead WHERE Status='C'", null);
                    c12 = rbc121.getCount();
                    rbc121.close();

                    if (c12 > 0) {
                        Toast.makeText(getApplicationContext(), "Completed Pickups to be finished from dispatcher side", Toast.LENGTH_LONG).show();
                        return;
                    }
                    //rbc121.close();
                }


                //cursor to check if image sync pending
                Cursor rbc112 = db12.rawQuery("SELECT * FROM wbillimagesdata WHERE TransferStatus=0 AND Drivercode='" + drivercode + "'", null);
                int c2 = rbc112.getCount();
                rbc112.close();

                if (c2 > 0) {
                    Toast.makeText(getApplicationContext(), "Route cannot close now Sync image Pending ", Toast.LENGTH_SHORT).show();
                    mPb.setVisibility(View.INVISIBLE);
                    return;
                }


                if (c <= 0 && c1 <= 0 && c12 <= 0 && c2 <= 0 && fc <= 0) {
                    rootstatus = WebService.SET_COURIERROUTECLOSE(drivercode, rte);
                    SQLiteDatabase db1 = db.getWritableDatabase();
                    if (rootstatus.equals("TRUE")) {
                        db1.execSQL("UPDATE logindata SET Loginstatus=0,Routecode=" + null + ",Runsheetcode=" + null + ",Odometervalue=" + null + " WHERE Username='" + drivercode + "'");
                        db.close();
                        Toast.makeText(getApplicationContext(), "Route closed", Toast.LENGTH_SHORT).show();

                        TelephonyManager telephonyManager =
                                (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                        if (ActivityCompat.checkSelfPermission(HomeActivity.this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                            return;
                        }
                        assert telephonyManager != null;
                        serialid = telephonyManager.getDeviceId();

                        gps = new GPSTracker(mContext, HomeActivity.this);

                        // check if GPS enabled
                        if (gps.canGetLocation()) {
                            System.out.println("latitude in home activity is" + gps.getLatitude());
                            System.out.println("longitude is" + gps.getLongitude());
                            latitude = gps.getLatitude();
                            longitude = gps.getLongitude();
                            lati = String.valueOf(latitude);
                            longti = String.valueOf(longitude);

                        } else {
                            // can't get location
                            // GPS or Network is not enabled
                            // Ask user to enable GPS/network in settings
                            gps.showSettingsAlert();
                        }
                        Geocoder gcd = new Geocoder(getApplicationContext(), Locale.getDefault());
                        List<Address> addresses;
                        try {
                            addresses = gcd.getFromLocation(gps.getLatitude(), gps.getLongitude(), 1);

                            if (addresses != null && addresses.size() > 0) {

                                Address address = addresses.get(0);
                                // sending back first address line and locality
                                area = address.getAddressLine(0) + "," + address.getAdminArea();
                                //System.out.println(area);
                            }
                        } catch (IOException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                        int login = 0;


                        //	mstatus=WebService.Setdevicestatus(drivercode,serialid,lati,longti,login,area,MasterActivity.METHOD_NAME30);
                        mstatus = WebService.SET_DEVICE_STATUS(drivercode, serialid, lati, longti, String.valueOf(login), area = "");
                        //if(mstatus)
                        //System.out.println("success");


                        //Intent intent = new Intent(getBaseContext(), PostaNotificationService.class);
                        //stopService(intent);

                        pref = getSharedPreferences(LoginActivity.MyPREFERENCES, Context.MODE_PRIVATE);
                        Editor editor = pref.edit();
                        editor.clear();
                        editor.commit();
                        clearApplicationData();
                        HomeActivity.this.finish();

                        startActivity(new Intent(HomeActivity.this, LoginActivity.class));
                        return;
                    } else {
                        db.close();
                        //	Toast.makeText(getApplicationContext(), "Route cannot close now", Toast.LENGTH_SHORT).show();
                        Toast.makeText(getApplicationContext(), rootstatus, Toast.LENGTH_SHORT).show();
                        mPb.setVisibility(View.INVISIBLE);
                        return;
                    }
                } else
                    Toast.makeText(getApplicationContext(), "Route cannot close now", Toast.LENGTH_SHORT).show();
                mPb.setVisibility(View.INVISIBLE);
                db.close();
            }

            public void clearApplicationData() {
                // TODO Auto-generated method stub
                File cache = getCacheDir();
                File appDir = new File(cache.getParent());
                if (appDir.exists()) {
                    String[] children = appDir.list();
                    for (String s : children) {
                        if (!s.equals("lib")) {
                            deleteDir(new File(appDir, s));
                            Log.i("TAG", " File /data/data/com.post.postaplusandroidapp/" + s + " DELETED ***");
                        }
                    }
                }
            }

            public boolean deleteDir(File dir) {
                if (dir != null && dir.isDirectory()) {
                    String[] children = dir.list();
                    for (int i = 0; i < children.length; i++) {
                        boolean success = deleteDir(new File(dir, children[i]));
                        if (!success) {
                            return false;
                        }
                    }
                }
                return dir.delete();
            }


        });

        summary.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    mPb.setVisibility(View.VISIBLE);
                    v.startAnimation(AnimationUtils.loadAnimation(getBaseContext(), R.anim.image_click));
                    Intent int1 = new Intent(HomeActivity.this, SummaryActivity.class);

                    int1.putExtra("routecode", rte);
                    int1.putExtra("routename", rte1);

                    startActivity(new Intent(int1));
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                }

            }
        });

        logout.setOnClickListener(new OnClickListener() {

            @SuppressLint("MissingPermission")
            @Override
            public void onClick(View v) {
                try {
                    mPb.setVisibility(View.VISIBLE);
                    v.startAnimation(AnimationUtils.loadAnimation(getBaseContext(), R.anim.image_click));
                    //Intent intent = new Intent(HomeActivity.this, PostaNotificationService.class);

                    TelephonyManager telephonyManager =
                            (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                    assert telephonyManager != null;
                    serialid = telephonyManager.getDeviceId();

                    gps = new GPSTracker(mContext, HomeActivity.this);

                    // check if GPS enabled
                    if (gps.canGetLocation()) {

                        latitude = gps.getLatitude();
                        longitude = gps.getLongitude();
                        lati = String.valueOf(latitude);
                        longti = String.valueOf(longitude);

                    } else {
                        // can't get location
                        // GPS or Network is not enabled
                        // Ask user to enable GPS/network in settings
                        gps.showSettingsAlert();
                    }
                    Geocoder gcd = new Geocoder(getApplicationContext(), Locale.getDefault());
                    List<Address> addresses;
                    try {
                        addresses = gcd.getFromLocation(gps.getLatitude(), gps.getLongitude(), 1);
                        System.out.println("lati are:" + lati + "longti aa:" + longti);
                        if (addresses != null && addresses.size() > 0) {
                            Address address = addresses.get(0);
                            // sending back first address line and locality
                            area = address.getAddressLine(0) + "," + address.getAdminArea();
                            //System.out.println(area);
                        }
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    int login = 0;
                    //System.out.println(response.getPropertyCount());
                    //		mstatus=WebService.Setdevicestatus(drivercode,serialid,lati,longti,login,area,MasterActivity.METHOD_NAME30);
                    mstatus = WebService.SET_DEVICE_STATUS(drivercode, serialid, lati, longti, String.valueOf(login), area = "");
                    //if(mstatus)
                    //System.out.println("success");


                    //stopService(new Intent(getBaseContext(), PostaNotificationService.class));


                    db = new DatabaseHandler(getBaseContext());
                    //update loginstatus to ZERO when logout
                    SQLiteDatabase db1 = db.getWritableDatabase();
                    db1.execSQL("UPDATE logindata SET Loginstatus=0 WHERE Username='" + drivercode + "'");
                    db.close();

                    pref = getSharedPreferences(LoginActivity.MyPREFERENCES, Context.MODE_PRIVATE);
                    Editor editor = pref.edit();
                    editor.clear();
                    editor.commit();
                    clearcashData(HomeActivity.this);
                    // moveTaskToBack(true);
                    HomeActivity.this.finish();

                    startActivity(new Intent(HomeActivity.this, LoginActivity.class));


                    //db.close();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    //mPb.setVisibility(View.INVISIBLE);
                }

            }

            //method to clear the cash content
            public void clearcashData(Context context) {
                File dir = context.getCacheDir();
                if (dir != null && dir.isDirectory()) {
                    deleteDir(dir);
                }
            }

            public boolean deleteDir(File dir) {
                if (dir != null && dir.isDirectory()) {
                    String[] children = dir.list();
                    for (int i = 0; i < children.length; i++) {
                        boolean success = deleteDir(new File(dir, children[i]));
                        if (!success) {
                            return false;
                        }
                    }
                }

                // The directory is now empty so delete it
                return dir.delete();
            }
        });

    }

    private OnClickListener syncButtonClicked() {
        return new OnClickListener() {


            @Override
            public void onClick(final View v) {
                _activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            v.startAnimation(AnimationUtils.loadAnimation(getBaseContext(), R.anim.image_click));
                            clearcashData(HomeActivity.this);
                            boolean serviceStatusAsynch = WebService.GET_SERVICE_STATUS_ASYNCH(drivercode);
                            if (!serviceStatusAsynch) {
                                Toast.makeText(HomeActivity.this, "The status of service not available, try later",
                                        Toast.LENGTH_LONG).show();
                                return;
                            }
                            new Async_SyncTask().execute();
                            onPostExecute();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                });
            }


            public void clearcashData(Context context) {
                File dir = context.getCacheDir();
                if (dir != null && dir.isDirectory()) {
                    deleteDir(dir);
                }
            }

            public boolean deleteDir(File dir) {
                if (dir != null && dir.isDirectory()) {
                    String[] children = dir.list();
                    for (int i = 0; i < children.length; i++) {
                        boolean success = deleteDir(new File(dir, children[i]));
                        if (!success) {
                            return false;
                        }
                    }
                }

                return dir.delete();
            }


            private void Loader_from_Local_wbillhold_transfer() {
                // TODO Auto-generated method stub
                System.out.println("Loader_from_Local_wbillhold_transfer()");

                db = new DatabaseHandler(getBaseContext());
                SQLiteDatabase db1 = db.getReadableDatabase();

                Cursor rr1 = db1.rawQuery("SELECT * FROM Holdwaybilldata WHERE TransferStatus=0 AND Drivercode='" + drivercode + "' AND HOLD_Transfer_Status=1", null);
                int c1 = rr1.getCount();


                String[] trancourier = new String[c1];
                String[] wbill1 = new String[c1];


                rr1.moveToFirst();
                if (c1 > 0) {

                    for (int i = 0; i < c1; i++) {
                        trancourier[i] = rr1.getString(rr1.getColumnIndex("Transdriver_Code"));

                        wbill1[i] = rr1.getString(rr1.getColumnIndex("Waybill"));


                        rr1.moveToNext();
                        //holdstatus=WebService.settranshold(drivercode,trancourier[i],wbill1[i],METHOD_NAME25);
                        holdstatus = WebService.SET_TRANS_HOLD(drivercode, trancourier[i], wbill1[i]);

                        if (holdstatus == null) {

                            Toast.makeText(HomeActivity.this, "Please Try again!",
                                    Toast.LENGTH_LONG).show();
                            return;
                        }
                        if (holdstatus.contains("TRANSFER")) {
                            db1 = db.getWritableDatabase();
                            db1.execSQL("UPDATE Holdwaybilldata SET TransferStatus=1 WHERE Waybill='" + wbill1[i] + "'");
                        }

                    }
                }
                rr1.close();
                db1.close();
            }

            private void Loader_from_Local_wbillevent() {
                // TODO Auto-generated method stub
                System.out.println("Loader_from_Local_wbillevent()");

                db = new DatabaseHandler(getBaseContext());
                SQLiteDatabase db1 = db.getReadableDatabase();

                Cursor rr = db1.rawQuery("SELECT * FROM wbilldata WHERE TransferStatus=0", null);
                int c1 = rr.getCount();
                String[] evname = new String[c1];
                String[] rname = new String[c1];
                String[] dcode = new String[c1];
                String[] wbill1 = new String[c1];
                String[] eventcode1 = new String[c1];
                String[] date_time = new String[c1];
                String[] lat = new String[c1];
                String[] long11 = new String[c1];
                rr.moveToFirst();
                int flag = 0;
                if (c1 > 0) {

                    for (int i = 0; i < c1; i++) {
                        evname[i] = rr.getString(rr.getColumnIndex("Event_note"));
                        rname[i] = rr.getString(rr.getColumnIndex("Reciever_Name"));
                        dcode[i] = rr.getString(rr.getColumnIndex("Drivercode"));
                        wbill1[i] = rr.getString(rr.getColumnIndex("Waybill"));
                        eventcode1[i] = rr.getString(rr.getColumnIndex("Eventcode"));
                        date_time[i] = rr.getString(rr.getColumnIndex("Date_Time"));
                        lat[i] = rr.getString(rr.getColumnIndex("Latitude"));
                        long11[i] = rr.getString(rr.getColumnIndex("Longtitude"));
                        sqldb = db.getReadableDatabase();
                        Cursor wd = sqldb.rawQuery("SELECT * FROM DeliveryData WHERE waybill = '" + wbill1[i] + "'", null);
                        int wdcount = wd.getCount();
                        String IsCODCollected = "N";
                        String WaybillIdentifier = "NRML";
                        String BarcodeIdentifier = "N";
                        wd.moveToFirst();
                        if (wdcount > 0) {
                            IsCODCollected = wd.getString(wd.getColumnIndex("IsCollectedCOD"));
                            WaybillIdentifier = wd.getString(wd.getColumnIndex("AWBIdentifier"));
                            BarcodeIdentifier = wd.getString(wd.getColumnIndex("BarcodeIdentifier"));
                            System.out.println("Barcodeidentifier is" + wd.getString(wd.getColumnIndex("BarcodeIdentifier")));
                        }
                        wd.close();
                        waynillist.add(wbill1[i]);
                        waynillist = new ArrayList<String>(new LinkedHashSet<String>(waynillist));
                        System.out.println("waylist in home:" + waynillist);
                        deliverystatus = WebService.SET_DELIVERY(rname[i], dcode[i], waynillist, eventcode1[i], lat[i], long11[i], date_time[i], evname[i], WaybillIdentifier, IsCODCollected, BarcodeIdentifier);
                        //	deliverystatus=WebService.setdelivery(rname[i],dcode[i],wbill1[i],eventcode1[i],lat[i],long11[i],date_time[i],evname[i],METHOD_NAME20);
                        //	AK	//deliverystatus = WebService.SET_DELIVERY(rname[i], dcode[i], wbill1[i], eventcode1[i], lat[i], long11[i], date_time[i], evname[i], WaybillIdentifier, IsCODCollected, BarcodeIdentifier);
                        //System.out.println("stage1");
                        if (deliverystatus.contains("True")) {
                            db1 = db.getWritableDatabase();
                            sqldb = db.getWritableDatabase();
                            sqldb.execSQL("UPDATE deliverydata SET WC_Status='C' WHERE Waybill='" + wbill1[i] + "'");
                            db1.execSQL("UPDATE wbilldata SET TransferStatus=1 WHERE Waybill='" + wbill1[i] + "'");
                            sqldb.execSQL("UPDATE deliverydata SET Attempt_Status=1 WHERE Waybill='" + wbill1[i] + "'");
                            //flag=1;
                            if ((eventcode1[i].contains("SD")) || (eventcode1[i].contains("NT")) || eventcode1[i].contains("MR") || eventcode1[i].contains("DELIVERED")) {
                                db1 = db.getWritableDatabase();
                                sqldb = db.getWritableDatabase();
                                //System.out.println("stage3");
                                sqldb.execSQL("UPDATE deliverydata SET StopDelivery=1 WHERE Waybill='" + wbill1[i] + "'");
                                db1.execSQL("UPDATE deliverydata SET ApprovalStatus='APPROVED' WHERE Waybill='" + wbill1[i] + "'");
                            }
                        } else if (deliverystatus.contains("REQ")) {
                            //System.out.println("stage4");
                            db1 = db.getWritableDatabase();
                            flag = 1;
                            db1.execSQL("UPDATE deliverydata SET ApprovalStatus='REQ' WHERE Waybill='" + wbill1[i] + "'");
                        }
                        rr.moveToNext();
                    }
                }
                if (flag == 1) {
                    //System.out.println("stage5");
                    Toast.makeText(getApplicationContext(), "REQUESTED WAYBILL NOT APPROVED TO UPDATE", Toast.LENGTH_LONG).show();
                }
                rr.close();
                db1.close();
            }


        };
    }

    public void getroutes() {

        try {
            routesresponse = WebService.GET_ROUTES(drivercode);
            if (routesresponse == null) {

                Toast.makeText(HomeActivity.this, "Please Try again!",
                        Toast.LENGTH_LONG).show();
                return;
            }
            for (Routes routesOb : routesresponse) {
                sqldb = db.getReadableDatabase();
                Cursor rou = sqldb.rawQuery("SELECT * FROM routedata WHERE ROUTECODE='" + routesOb.RouteCode + "'", null);
                sqldb = db.getWritableDatabase();
                if (rou.getCount() > 0) {
                    //	sqldb = db.getWritableDatabase();
                    sqldb.execSQL("UPDATE routedata SET ROUTENAME='" + routesOb.RouteName + "' WHERE ROUTECODE='" + routesOb.RouteCode + "'");
                } else {
                    //	sqldb = db.getWritableDatabase();
                    String sql = "INSERT  INTO routedata (ROUTECODE, ROUTENAME) "

                            + "VALUES (" + routesOb.RouteCode + ", '"
                            + routesOb.RouteName + "')";
                    sqldb.execSQL(sql);
                    //sqldb.insertOrThrow("routedata", null, values);
                }
                rou.close();
            }
            db.close();
        } catch (Exception e) {
            Log.e("GetRoutes:", "Get Routes in Home activity is errored");
            e.printStackTrace();
        }

    }

    //get data from hold delivery table
    public void getdeliveryholddetail() {
        try {
            scanwbildtResponse = WebService.GET_SCAN_WAYBILL_DT(drivercode);
            db = new DatabaseHandler(getBaseContext());
            //open localdatabase in a read mode

            sqldb = db.getWritableDatabase();
            //select all values in the table and check count
            sqldb.execSQL("DELETE FROM deliverydata WHERE Drivercode <> '" + drivercode + "'");
            for (ScanWaybillDt scwbldtOb : scanwbildtResponse) {
                if (scwbldtOb.ErrMsg == null || scwbldtOb.ErrMsg == "") {
                    sqldb = db.getReadableDatabase();
                    Log.e("Synch", "DelvHoldDetails Bolck Called");
                    Cursor c1 = sqldb.rawQuery("SELECT * FROM deliverydata WHERE Waybill='" + scwbldtOb.WayBill + "'", null);
                    int count1 = c1.getCount();

                    if (count1 > 0) {

                        //System.out.println(c1.getString(c1.getColumnIndex("Waybill")));

                        //c1.moveToNext();

                        sqldb = db.getWritableDatabase();

                        sqldb.execSQL("UPDATE deliverydata SET Drivercode='" + drivercode + "',Routecode=" + scwbldtOb.RouteName
                                + ",Consignee='" + scwbldtOb.ConsignName + "',Telephone='" + scwbldtOb.PhoneNo + "'," + "Area='" + scwbldtOb.Area + "'," + "Company='"
                                + scwbldtOb.Company + "',CivilID='" + scwbldtOb.CivilId + "',Serial='" + scwbldtOb.Serial + "',CardType='" + scwbldtOb.CardType + "',DeliveryDate='" + scwbldtOb.DelDate + "',DeliveryTime='"
                                + scwbldtOb.DelTime + "',Amount='" + scwbldtOb.Amount + "',StopDelivery=0,WC_Status='P',WC_Transfer_Status=0,TransferStatus=0,Attempt_Status='" + scwbldtOb.Attempt + "',Address='" + scwbldtOb.Address + "' WHERE Waybill='" + scwbldtOb.WayBill + "'");


                    } else {
                        c1.moveToLast();

                        sqldb = db.getWritableDatabase();
                        ContentValues values = new ContentValues();
                        values.put("Drivercode", drivercode);
                        values.put("Routecode", scwbldtOb.RouteName);
                        values.put("Waybill", scwbldtOb.WayBill);
                        values.put("Consignee", scwbldtOb.ConsignName);
                        values.put("Telephone", scwbldtOb.PhoneNo);
                        values.put("Area", scwbldtOb.Area);
                        values.put("Company", scwbldtOb.Company);
                        values.put("CivilID", scwbldtOb.CivilId);
                        values.put("Serial", scwbldtOb.Serial);
                        values.put("CardType", scwbldtOb.CardType);
                        values.put("DeliveryDate", scwbldtOb.DelDate);
                        values.put("DeliveryTime", scwbldtOb.DelTime);
                        values.put("Amount", scwbldtOb.Amount);
                        values.put("WC_Status", "P");
                        values.put("StopDelivery", "0");
                        values.put("WC_Transfer_Status", "0");
                        values.put("TransferStatus", "0");
                        values.put("Attempt_Status", scwbldtOb.Attempt);
                        values.put("Address", scwbldtOb.Address);

                        sqldb.insertOrThrow("deliverydata", null, values);

                    }
                    c1.close();
                }
            }
        } catch (Exception e) {
            Log.e("Get ScanWBll detail:", "Get Scan Waybill in Home activity is errored");

            e.printStackTrace();
        } finally {
            db.close();
        }

    }

    //function to get event
    public void getevent() {
        // TODO Auto-generated method stub
        try {

            eventResponse = WebService.GET_EVENTS();
            if (eventResponse != null) {
                db = new DatabaseHandler(getBaseContext());
                //  sqldb = db.getWritableDatabase();

                for (Events evOb : eventResponse) {

                    sqldb = db.getReadableDatabase();
                    Cursor courier = sqldb.rawQuery("SELECT * FROM eventdata WHERE EVENTCODE='" + evOb.EVENTCODE + "'", null);
                    sqldb = db.getWritableDatabase();
                    if (courier.getCount() > 0) {

                        sqldb.execSQL("UPDATE eventdata SET EVENTDESC='" + evOb.EVENTDESC + "' WHERE EVENTCODE='" + evOb.EVENTCODE + "'");
                    } else {

                        String sql = "INSERT  INTO eventdata (EVENTCODE, EVENTDESC) "

                                + "VALUES ('" + evOb.EVENTCODE + "', '"
                                + evOb.EVENTDESC + "')";
                        sqldb.execSQL(sql);
                    }
                    courier.close();


                }
                db.close();
            }

        } catch (Exception e) {
            Log.e("Get-event:", "Get Event in login activity is errored");
            //Toast.makeText(getApplicationContext(),e.getMessage().toString(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    private void getpickupRemarks() {

        try {

            pickupremarksResponse = WebService.GET_PICKUP_REMARK(drivercode);

            if (pickupremarksResponse == null) {

                Toast.makeText(HomeActivity.this, "Please Try again!",
                        Toast.LENGTH_LONG).show();
                return;
            }
            db = new DatabaseHandler(getBaseContext());
            //  sqldb = db.getWritableDatabase();

            for (Remarks remOb : pickupremarksResponse) {

                sqldb = db.getReadableDatabase();
                Cursor remk = sqldb.rawQuery("SELECT * FROM pickupRemarks WHERE RemarkCode='" + remOb.REMARKCODE + "'", null);
                sqldb = db.getWritableDatabase();
                if (remk.getCount() > 0) {
                    sqldb.execSQL("UPDATE pickupRemarks SET RemarkDesc='" + remOb.REMARKDESC + "' WHERE RemarkCode='" + remOb.REMARKCODE + "'");
                } else {

                    String sql = "INSERT  INTO pickupRemarks (RemarkCode, RemarkDesc) "

                            + "VALUES ('" + remOb.REMARKCODE + "', '"
                            + remOb.REMARKDESC + "')";
                    sqldb.execSQL(sql);
                }
                remk.close();


            }
            db.close();


        } catch (Exception e) {
            Log.e("Get-remarks:", "Get remarkspickup in login activity is errored");
            //Toast.makeText(getApplicationContext(),e.getMessage().toString(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    //to get the scanned transfer accept waybill before accepting
    private void gettranswaybillacceptdt() {
        // TODO Auto-generated method stub
        return;
			/*System.out.println("gettranswaybillacceptdt" );

			SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME43);
			Request.addProperty("DRIVERCODE2",drivercode);
			System.out.println(" value of DRIVERCODE2" );
			System.out.println(drivercode);

			*//*Create waybill class *//*
			Waybill_check wb = new Waybill_check();


			*//* Set the route to be the argument of the web service method *//*
			PropertyInfo pi = new PropertyInfo();
			pi.setName(METHOD_NAME43);
			pi.setValue(wb);
			pi.setType(wb.getClass());
			Request.addProperty(pi);

			*//* Set the web service envelope*//*
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
			envelope.dotNet = true;
			envelope.setOutputSoapObject(Request);
			//envelope for setting the data
			envelope.addMapping(NAMESPACE, METHOD_NAME43,new Waybill_check().getClass());
			HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
			//	androidHttpTransport.debug = true;

			*//* Call the web service and retrieve result*//*


			try {
				ServiceConnection WebCon = androidHttpTransport.getServiceConnection();
				WebCon.disconnect();
				androidHttpTransport.call(SOAP_ACTION+METHOD_NAME43, envelope);
				System.out.println("Value of SOAP_ACTION+METHOD_NAME43 in home activity");
				System.out.println(SOAP_ACTION+METHOD_NAME43);
				System.out.println("Value of envelope in home activity");
				System.out.println(envelope);
				System.out.println("androidHttpTransport value in home activity");
				System.out.println(androidHttpTransport);
				SoapObject response = (SoapObject)envelope.getResponse();
				Log.i("myApp", Request.toString());
				//System.out.println("check dddd" + response);
				//envelope for passing the data
				envelope.addMapping(NAMESPACE, METHOD_NAME43,new Waybill_check().getClass());
				androidHttpTransport.call(SOAP_ACTION+METHOD_NAME43, envelope);

				Waybill_check[] waybills=new Waybill_check[response.getPropertyCount()];
				count=response.getPropertyCount();
				waybill1=new String[count];
				rname1=new String[count];
				cname1 =new String[count];
				phone =new String[count];
				area1=new String[count];
				company1=new String[count];
				civilid1=new String[count];
				serial1=new String[count];
				cardtype1=new String[count];
				deldate1=new String[count];
				deltime1=new String[count];
				amount1=new String[count];
				attempt1=new String[count];
				error1= new String[count];
				address1= new String[count];

				db=new DatabaseHandler(getBaseContext());
				//open localdatabase in a read mode
				sqldb = db.getReadableDatabase();
				//select all values in the table and check count
				sqldb.execSQL("DELETE FROM TransferAcceptTemp WHERE Drivercode <> '"+drivercode+"'");
				if(response.toString().contains("ConsignName"))
				{
					for (int i = 0; i < response.getPropertyCount(); i++)
					{

						SoapObject pii = (SoapObject)response.getProperty(i);

						wb.wbill=pii.getProperty(0).toString();

						wb.routename=pii.getProperty(1).toString();
						wb.cname =pii.getProperty(2).toString();
						wb.phno =pii.getProperty(3).toString();
						wb.area=pii.getProperty(4).toString();
						wb.company=pii.getProperty(5).toString();
						wb.civilid=pii.getProperty(6).toString();
						wb.serial=pii.getProperty(7).toString();
						wb.cardtype=pii.getProperty(8).toString();
						wb.del_date=pii.getProperty(9).toString();
						wb.del_time=pii.getProperty(10).toString();
						wb.amount=pii.getProperty(11).toString();
						wb.error= pii.getProperty(12).toString();
						wb.attempt= pii.getProperty(13).toString();
						wb.address= pii.getProperty(14).toString();
						waybills[i]=wb;

						waybill1[i]=waybills[i].wbill;
						rname1[i]=waybills[i].routename;
						cname1[i]=waybills[i].cname;
						phone[i]=waybills[i].phno;
						area1[i]=waybills[i].area;
						company1[i]=waybills[i].company;
						civilid1[i]=waybills[i].civilid;
						serial1[i]=waybills[i].serial;
						cardtype1[i]=waybills[i].cardtype;
						deldate1[i]=waybills[i].del_date;
						deltime1[i]=waybills[i].del_time;
						amount1[i]=waybills[i].amount;
						error1[i]= waybills[i].error;
						attempt1[i]=waybills[i].attempt;
						address1[i]=waybills[i].address;



						Cursor c1 = sqldb.rawQuery("SELECT * FROM TransferAcceptTemp WHERE Waybill='"+waybill1[i]+"'",null);
						int count1=c1.getCount();

						if(count1>0){

							//System.out.println(c1.getString(c1.getColumnIndex("Waybill")));

							//c1.moveToNext();

							sqldb =db.getWritableDatabase();

							sqldb.execSQL("UPDATE TransferAcceptTemp SET Drivercode='"+drivercode+"',Routecode="+rname1[i]
									+",Consignee='"+cname1[i]+"',Telephone='"+phone[i]+"',"+"Area='"+area1[i]+"',"+"Company='"
									+company1[i]+"',CivilID='"+civilid1[i]+"',Serial='"+serial1[i]+"',CardType='"+cardtype1[i]+"',DeliveryDate='"+deldate1[i]+"',DeliveryTime='"
									+deltime1[i]+"',Amount='"+amount1[i]+"',Transfer_Status='P',Address='"+address1[i]+"',Attempt_Status='"+attempt1[i]+"' WHERE Waybill='"+waybill1[i]+"'");




						}
						else{
							c1.moveToLast();

							sqldb =db.getWritableDatabase();
							ContentValues values = new ContentValues();
							values.put("Drivercode", drivercode);
							values.put("Routecode",rname1[i]);
							values.put("Waybill",waybill1[i]);
							values.put("Consignee",cname1[i]);
							values.put("Telephone",phone[i]);
							values.put("Area",area1[i]);
							values.put("Company",company1[i]);
							values.put("CivilID",civilid1[i]);
							values.put("Serial", serial1[i]);
							values.put("CardType",cardtype1[i]);
							values.put("DeliveryDate",deldate1[i]);
							values.put("DeliveryTime", deltime1[i]);
							values.put("Amount",amount1[i]);
							values.put("Transfer_Status","P");
							values.put("Address",address1[i]);
							values.put("Attempt_Status",attempt1[i]);

							sqldb.insertOrThrow("TransferAcceptTemp", null, values);

						}
						c1.close();




					}
					db.close();
				}
				androidHttpTransport.reset();
				//	db.close();
				WebCon.disconnect();
				//androidHttpTransport.getConnection().disconnect();
			}
			catch(Exception e)
			{
				//Log.e("Home:delivery:", e.getMessage().toString());
				e.printStackTrace();
			}*/
    }

    //sync pickup from server
    private void getpickupddeatils() {
        // TODO Auto-generated method stub
        //function to get the pickupdetails
        db = new DatabaseHandler(getBaseContext());
        try {
            pickupdtResponse = WebService.GET_PICKUP_DT(drivercode);
            //System.out.println("value of pickupdt response in getpickupdt");

            //	db = new DatabaseHandler(getBaseContext());
            //open localdatabase in a read mode

            sqldb = db.getWritableDatabase();
            sqldb.execSQL("DELETE FROM pickuphead WHERE Drivercode <> '" + drivercode + "'");
            sqldb = db.getReadableDatabase();
            for (PickUpDt pkdtOb : pickupdtResponse) {
                sqldb = db.getWritableDatabase();

                //System.out.println("value of pickupdt response in getpickupdt :"+pkdtOb.ACC_NAME);
                if ((pkdtOb.ERR).contains("PENDING")) {
                    //System.out.println("PENDING\n------------");

                    sqldb = db.getReadableDatabase();
                    //select all values in the table and check count
                    Cursor c1 = sqldb.rawQuery("SELECT * FROM pickuphead WHERE Pickup_No='" + pkdtOb.PICK_NO + "'", null);
                    int count1 = c1.getCount();

                    if (count1 > 0) {

                        System.out.println("update");

                        sqldb = db.getWritableDatabase();

                        sqldb.execSQL("UPDATE pickuphead SET Drivercode='" + drivercode + "', Account_Name='" + pkdtOb.ACC_NAME
                                + "',Pick_Address='" + pkdtOb.PICK_ADD + "',Pickup_Area='" + pkdtOb.PICK_AREA + "',Contact_Person='" + pkdtOb.CONTACT_PERSON + "'," + "Pickup_Phone='" + pkdtOb.PICK_PHONE + "'," + "Pickup_Time='"
                                + pkdtOb.PICK_TIME + "',Status='A',TransferStatus=0,Pickup_Type='" + pkdtOb.IDENTIFIER + "',ConsigneeName='" + pkdtOb.CONSIGNEE_NAME + "',DeliveryAddress='" + pkdtOb.DEL_ADD + "',DeliveryCity='" + pkdtOb.DEL_CITY + "',DeliveryPhone='" + pkdtOb.DEL_PHONE + "' WHERE Pickup_No='" + pkdtOb.PICK_NO + "'");


                    } else {
                        c1.moveToLast();

                        System.out.println("pickup no and Accountname in get pickupdt: " + pkdtOb.PICK_NO + pkdtOb.ACC_NAME);
                        sqldb = db.getWritableDatabase();
                        ContentValues values = new ContentValues();
                        values.put("Drivercode", drivercode);
                        values.put("Pickup_No", pkdtOb.PICK_NO);
                        values.put("Account_Name", pkdtOb.ACC_NAME);
                        values.put("Pick_Address", pkdtOb.PICK_ADD);
                        values.put("Pickup_Area", pkdtOb.PICK_AREA);
                        values.put("Contact_Person", pkdtOb.CONTACT_PERSON);
                        values.put("Pickup_Phone", pkdtOb.PICK_PHONE);
                        values.put("Pickup_Time", pkdtOb.PICK_TIME);
                        values.put("Status", "A");
                        values.put("TransferStatus", "0");
                        values.put("Pickup_Type", pkdtOb.IDENTIFIER);
                        values.put("ConsigneeName", pkdtOb.CONSIGNEE_NAME);
                        values.put("DeliveryAddress", pkdtOb.DEL_ADD);
                        values.put("DeliveryCity", pkdtOb.DEL_CITY);
                        values.put("DeliveryPhone", pkdtOb.DEL_PHONE);

                        sqldb.insertOrThrow("pickuphead", null, values);

                    }
                    c1.close();
                }

                //if err message completed call GET_PICKUP_WAYBILLS_DT
                else if ((pkdtOb.ERR).equals("COMPLETED")) {

                    sqldb = db.getReadableDatabase();
                    //System.out.println("ERR=COMPLETED\n______________");
                    //if data is not in the table insert/update
                    Cursor c11 = sqldb.rawQuery("SELECT * FROM pickuphead WHERE Pickup_No='" + pkdtOb.PICK_NO + "'", null);
                    int count1 = c11.getCount();

                    if (count1 > 0) {

                        System.out.println("update");

                        sqldb = db.getWritableDatabase();


                        sqldb.execSQL("UPDATE pickuphead SET Drivercode='" + drivercode + "', Account_Name='" + pkdtOb.ACC_NAME
                                + "',Pick_Address='" + pkdtOb.PICK_ADD + "',Pickup_Area='" + pkdtOb.PICK_AREA + "',Contact_Person='" + pkdtOb.CONTACT_PERSON + "'," + "Pickup_Phone='" + pkdtOb.PICK_PHONE + "'," + "Pickup_Time='"
                                + pkdtOb.PICK_TIME + "',Status='C',TransferStatus=2 WHERE Pickup_No='" + pkdtOb.PICK_NO + "'");


                    } else {
                        c11.moveToLast();

                        sqldb = db.getWritableDatabase();
                        ContentValues values = new ContentValues();
                        values.put("Drivercode", drivercode);
                        values.put("Pickup_No", pkdtOb.PICK_NO);
                        values.put("Account_Name", pkdtOb.ACC_NAME);
                        values.put("Pick_Address", pkdtOb.PICK_ADD);

                        values.put("Pickup_Area", pkdtOb.PICK_AREA);
                        values.put("Contact_Person", pkdtOb.CONTACT_PERSON);
                        values.put("Pickup_Phone", pkdtOb.PICK_PHONE);
                        values.put("Pickup_Time", pkdtOb.PICK_TIME);
                        values.put("Status", "C");
                        values.put("TransferStatus", "2");


                        sqldb.insertOrThrow("pickuphead", null, values);

                    }
                    c11.close();
                    getpickupwbilldetails(pkdtOb.PICK_NO);

                }

                //if error contain finished message delete the pickup from pickup detail table
                else if ((pkdtOb.ERR).equals("FINISHED")) {

                    sqldb = db.getWritableDatabase();
                    //System.out.println("ERR=FINISHED\n______________");
                    sqldb.execSQL("DELETE FROM pickuphead WHERE Pickup_No='" + pkdtOb.PICK_NO + "'");
                    sqldb.execSQL("DELETE FROM pickupdetails WHERE Pickup_No='" + pkdtOb.PICK_NO + "'");

                }
            }
            db.close();
        } catch (Exception e) {
            Log.e("Get Pickup detail:", "Get Pickup Detail in homeactivity is errored");
            e.printStackTrace();
        } finally {
            db.close();
            //	sqldb.close();
        }

    }

    //get the pickup wbill details
    private void getpickupwbilldetails(String pickno) {
        // TODO Auto-generated method stub
        try {
            pickupwblldtResponse = WebService.GET_PICKUP_WAYBILLS_DT(drivercode, pickno);
            sqldb = db.getReadableDatabase();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MMM-dd HH:mm:ss");
            String date_time = sdf.format(new Date());
            for (PickUpWaybillsDT pkpwbldtObv : pickupwblldtResponse) {

                //select all values in the table and check count
                Cursor c1 = sqldb.rawQuery("SELECT * FROM pickupdetails WHERE Waybill_Number='" + pkpwbldtObv.WAYBILL + "'", null);
                int count1 = c1.getCount();

                if (count1 > 0) {

                    System.out.println("update");

                    sqldb = db.getWritableDatabase();


                    sqldb.execSQL("UPDATE pickupdetails SET Driver_Code='" + drivercode + "', Pickup_No='" + pkpwbldtObv.PICKUPNO
                            + "',Pickup_No='" + pkpwbldtObv.PICKUPNO + "',PayType='" + pkpwbldtObv.PAYTYPE + "',Amount='" + pkpwbldtObv.AMOUNT + "'," + "ServiceType='" + pkpwbldtObv.SERVICE + "'," + "Date_Time='"
                            + date_time + "' WHERE Waybill_Number='" + pkpwbldtObv.WAYBILL + "'");


                } else {
                    c1.moveToLast();

                    sqldb = db.getWritableDatabase();

                    ContentValues values = new ContentValues();
                    values.put("Driver_Code", drivercode);
                    values.put("Pickup_No", pkpwbldtObv.PICKUPNO);
                    values.put("Waybill_Number", pkpwbldtObv.WAYBILL);
                    values.put("PayType", pkpwbldtObv.PAYTYPE);
                    values.put("Amount", pkpwbldtObv.AMOUNT);
                    values.put("ServiceType", pkpwbldtObv.SERVICE);
                    values.put("Date_Time", date_time);
                    //values.put("Tag_value",);


                    sqldb.insertOrThrow("pickupdetails", null, values);

                }
                sqldb = db.getWritableDatabase();
                sqldb.execSQL("UPDATE pickuphead SET Status='C' WHERE Pickup_No='" + pickno + "'");
                sqldb.execSQL("UPDATE pickuphead SET TransferStatus=2 WHERE Pickup_No='" + pickno + "'");
                c1.close();


            }
            db.close();
            //	sqldb.close();
        } catch (Exception e) {
            Log.e("Get Pickup Wbll detail:", "Get Pickup Waybil detail in login activity is errored");
            e.printStackTrace();
        }

    }


    //function to get the hold wbill data from server
    private void getholdwaybilldetail() {
        // TODO Auto-generated method stub
        try {
            holddwaybillResponse = WebService.GET_HOLDWAYBILLS(drivercode);
            if (holddwaybillResponse == null) {
                return;
            } else
                db = new DatabaseHandler(getBaseContext());
            sqldb = db.getWritableDatabase();
            sqldb.execSQL("DELETE FROM Holdwaybilldata WHERE Drivercode <> '" + drivercode + "'");
            for (HoldWayBills hldwbllOb : holddwaybillResponse) {
                sqldb = db.getReadableDatabase();
                //select all values in the table and check count
                Cursor c1 = sqldb.rawQuery("SELECT * FROM Holdwaybilldata WHERE Waybill='" + hldwbllOb.Waybill + "'", null);
                int count1 = c1.getCount();

                if (count1 > 0) {

                    //System.out.println(c1.getString(c1.getColumnIndex("Waybill")));

                    //c1.moveToNext();

                    sqldb = db.getWritableDatabase();

                    sqldb.execSQL("UPDATE Holdwaybilldata SET Drivercode='" + drivercode + "',Routecode='" + rte + "',Transdriver_Code='" + hldwbllOb.TrnsDrvr
                            + "',Consignee='" + hldwbllOb.Consignee + "',Telephone='" + hldwbllOb.Phno + "'," + "Area='" + hldwbllOb.Area + "'," + "Company='"
                            + hldwbllOb.Company + "',CivilID='" + hldwbllOb.CivilId + "',Serial='" + hldwbllOb.Serial + "',CardType='" + hldwbllOb.CardType + "',DeliveryDate='" + hldwbllOb.DelDate + "',DeliveryTime='"
                            + hldwbllOb.DelTime + "',Amount='" + hldwbllOb.Amount + "',Address='" + hldwbllOb.Address + "',HOLD_Transfer_Status=0,TransferStatus=0 WHERE Waybill='" + hldwbllOb.Waybill + "'");


                } else {
                    c1.moveToLast();

                    sqldb = db.getWritableDatabase();
                    ContentValues values = new ContentValues();
                    values.put("Drivercode", drivercode);
                    values.put("Routecode", rte);
                    values.put("Transdriver_Code", hldwbllOb.TrnsDrvr);
                    values.put("Waybill", hldwbllOb.Waybill);
                    values.put("Consignee", hldwbllOb.Consignee);
                    values.put("Telephone", hldwbllOb.Phno);
                    values.put("Area", hldwbllOb.Area);
                    values.put("Company", hldwbllOb.Company);
                    values.put("CivilID", hldwbllOb.CivilId);
                    values.put("Serial", hldwbllOb.Serial);
                    values.put("CardType", hldwbllOb.CardType);
                    values.put("DeliveryDate", hldwbllOb.DelDate);
                    values.put("DeliveryTime", hldwbllOb.DelTime);
                    values.put("Amount", hldwbllOb.Amount);
                    values.put("Address", hldwbllOb.Address);
                    values.put("HOLD_Transfer_Status", "0");
                    values.put("TransferStatus", "0");


                    sqldb.insertOrThrow("Holdwaybilldata", null, values);

                }
                c1.close();


            }
            db.close();
            //	sqldb.close();
        } catch (Exception e) {
            Log.e("Get Hold Wbll detail:", "Get Hold Waybill in login activity is errored");
            e.printStackTrace();

        }


    }

    //function to get the driver details
    private void getcouriers() {

        try {

            courierResponse = WebService.GET_COURIERS(drivercode);
            if (courierResponse == null) {

                Toast.makeText(HomeActivity.this, "Please Try again!",
                        Toast.LENGTH_LONG).show();
                return;
            }
            db = new DatabaseHandler(getBaseContext());
            for (Couriers corObv : courierResponse) {
                sqldb = db.getReadableDatabase();
                Cursor courier = sqldb.rawQuery("SELECT * FROM courierdetails WHERE DRIVERCODE='" + corObv.Driver_Code + "'", null);
                sqldb = db.getWritableDatabase();
                if (courier.getCount() > 0) {
                    sqldb.execSQL("UPDATE courierdetails SET DRIVERNAME='" + corObv.Driver_Name + "' WHERE DRIVERCODE='" + corObv.Driver_Code + "'");
                } else {

                    String sql = "INSERT  INTO courierdetails (DRIVERCODE, DRIVERNAME) "

                            + "VALUES ('" + corObv.Driver_Code + "', '"
                            + corObv.Driver_Name + "')";
                    sqldb.execSQL(sql);
                }
                courier.close();
            }
            db.close();
            //	sqldb.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    //sync delivery data from server
    private void getdeliverydetail() {
        // TODO Auto-generated method stub
        try {
            rstdetailResponse = WebService.GET_RSTDETAIL(drivercode);
            db = new DatabaseHandler(getBaseContext());
            //open localdatabase in a read mode
            sqldb = db.getWritableDatabase();
            //select all values in the table and check count
            sqldb.execSQL("DELETE FROM deliverydata WHERE Drivercode <> '" + drivercode + "'");
            for (RstDetail rstdtevOb : rstdetailResponse) {
                if (rstdtevOb.ErrMsg == null || rstdtevOb.ErrMsg == "") {
                    System.out.println("AWVIdentifier in home activity" + rstdtevOb.AWBIdentifier);
                    Log.e("Synch", "DelvDetails Bolck Called");
                    sqldb = db.getReadableDatabase();
					/*Cursor c11 = sqldb.rawQuery("SELECT 1 FROM deliverydata WHERE Waybill='" + rstdtevOb.WayBill + "' ", null);
					int count11 = c11.getCount();
*/
                    Cursor c1 = sqldb.rawQuery("SELECT * FROM deliverydata WHERE Waybill='" + rstdtevOb.WayBill + "'", null);
                    int count1 = c1.getCount();
                    //System.out.println("AWB Count : " + c1.getCount());
                    //	System.out.println("AWB Identity : " + rstdtevOb.AWBIdentifier);
                    if (count1 > 0) {

                        //System.out.println(c1.getString(c1.getColumnIndex("Waybill")));

                        //c1.moveToNext();
                        sqldb = db.getWritableDatabase();
                        String UpdateFields = "";
                        if (rstdtevOb.Last_Status.equals("WC") || rstdtevOb.Last_Status.equals("ACKW") || rstdtevOb.Last_Status.equals("RTW"))
                            UpdateFields = ",StopDelivery=0,WC_Status='A',WC_Transfer_Status=0,TransferStatus=0,Attempt_Status=0";
                        else if (rstdtevOb.Last_Status.equals("DELIVERED") || rstdtevOb.Last_Status.equals("ACKC") || rstdtevOb.Last_Status.equals("RTC") || rstdtevOb.Last_Status.equals("SD")) {
                            //System.out.println("wc status while delivered");
                            if (rstdtevOb.Last_Status.equals("SD"))
                                UpdateFields = ",StopDelivery=1,WC_Status='SD',WC_Transfer_Status=1,TransferStatus=0,Attempt_Status=1, ApprovalStatus='APPROVED'";
                            else
                                UpdateFields = ",StopDelivery=1,WC_Status='C',WC_Transfer_Status=1,TransferStatus=0,Attempt_Status=1, ApprovalStatus=''";
                        } else
                            UpdateFields = ",StopDelivery=0,WC_Status='C',WC_Transfer_Status=1,TransferStatus=0,Attempt_Status=1";
                        // System.out.println("wc status while notdelivered");
                        sqldb.execSQL("UPDATE deliverydata SET Drivercode='" + drivercode + "',Routecode=" + rstdtevOb.RouteName
                                + ",Consignee='" + rstdtevOb.ConsignName + "',Telephone='" + rstdtevOb.PhoneNo + "'," + "Area='" + rstdtevOb.Area + "'," + "Company='"
                                + rstdtevOb.Company + "',CivilID='" + rstdtevOb.CivilId + "',Serial='" + rstdtevOb.Serial + "',CardType='" + rstdtevOb.CardType + "',DeliveryDate='" + rstdtevOb.DelDate + "',DeliveryTime='"
                                + rstdtevOb.DelTime + "',Amount='" + rstdtevOb.Amount + "',Address='" + rstdtevOb.Address + "',ShipperName='" + rstdtevOb.ShipperName + "',AWBIdentifier='" + rstdtevOb.AWBIdentifier + "',Last_Status='" + rstdtevOb.Last_Status + "' " + UpdateFields + "  WHERE Waybill='" + rstdtevOb.WayBill + "'");

                        //System.out.println("Updated AWB : " + rstdtevOb.WayBill);

                    } else {
                        c1.moveToLast();

                        sqldb = db.getWritableDatabase();
                        ContentValues values = new ContentValues();
                        values.put("Drivercode", drivercode);
                        values.put("Routecode", rstdtevOb.RouteName);
                        values.put("Waybill", rstdtevOb.WayBill);
                        values.put("Consignee", rstdtevOb.ConsignName);
                        values.put("Telephone", rstdtevOb.PhoneNo);
                        values.put("Area", rstdtevOb.Area);
                        values.put("Company", rstdtevOb.Company);
                        values.put("CivilID", rstdtevOb.CivilId);
                        values.put("Serial", rstdtevOb.Serial);
                        values.put("CardType", rstdtevOb.CardType);
                        values.put("DeliveryDate", rstdtevOb.DelDate);
                        values.put("DeliveryTime", rstdtevOb.DelTime);
                        values.put("Amount", rstdtevOb.Amount);
                        System.out.println("wc status : " + rstdtevOb.Last_Status.equals("WC"));
                        if (rstdtevOb.Last_Status.equals("WC") || rstdtevOb.Last_Status.equals("ACKW") || rstdtevOb.Last_Status.equals("RTW")) {
                            values.put("WC_Status", "A");
                            System.out.println("wc status while wc");
                            values.put("Attempt_Status", "0");
                            values.put("WC_Transfer_Status", "0");
                        } else if (rstdtevOb.Last_Status.equals("DELIVERED") || rstdtevOb.Last_Status.equals("ACKC") || rstdtevOb.Last_Status.equals("RTC") || rstdtevOb.Last_Status.equals("SD")) {

                            if (rstdtevOb.Last_Status.equals("SD")) {
                                values.put("ApprovalStatus", "APPROVED");
                                values.put("WC_Status", "SD");
                            } else {
                                values.put("ApprovalStatus", "");
                                values.put("WC_Status", "C");
                            }
                            values.put("StopDelivery", "1");
                            values.put("Attempt_Status", "1");
                            values.put("WC_Transfer_Status", "1");
                        } else {
                            System.out.println("wc status while notwc");
                            values.put("WC_Status", "C");
                            values.put("Attempt_Status", "1");
                            values.put("WC_Transfer_Status", "0");
                        }


                        values.put("Address", rstdtevOb.Address);
                        values.put("ShipperName", rstdtevOb.ShipperName);
                        values.put("AWBIdentifier", rstdtevOb.AWBIdentifier);
                        values.put("Last_Status", rstdtevOb.Last_Status);

                        sqldb.insertOrThrow("deliverydata", null, values);
                        System.out.println("Inserted AWB : " + rstdtevOb.WayBill);

                    }
                    c1.close();


					/*}
					c11.close();*/

                }
            }
        } catch (Exception e) {
            Log.e("Get Dlvry detail:", "Get Delivery detail in login activity is errored");
            e.printStackTrace();
        } finally {
            db.close();
            //	sqldb.close();
        }


    }

    private void getpaytype() {
        // TODO Auto-generated method stub
        paytypeResponse = WebService.GET_PAYTYPE();
        db = new DatabaseHandler(getBaseContext());
        try {
            for (webservice.FuncClasses.PayType pytpObv : paytypeResponse) {
                System.out.println("paytype service is working" + pytpObv.PAYTYPE);
                sqldb = db.getReadableDatabase();
                Cursor courier = sqldb.rawQuery("SELECT * FROM paytypedetails WHERE PayID='" + pytpObv.PAYID + "'", null);
                sqldb = db.getWritableDatabase();
                if (courier.getCount() > 0) {
                    sqldb.execSQL("UPDATE paytypedetails SET PayTYPE='" + pytpObv.PAYTYPE + "' WHERE PayID='" + pytpObv.PAYID + "'");
                } else {
                    String sql = "INSERT INTO paytypedetails (PayID, PayTYPE) "

                            + "VALUES ('" + pytpObv.PAYID + "', '"
                            + pytpObv.PAYTYPE + "')";
                    sqldb.execSQL(sql);
                }
                courier.close();
            }
            db.close();
            //		sqldb.close();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void getservice() {
        // TODO Auto-generated method stub

        try {
            serviceResponse = WebService.GET_SERVICE();
            if (serviceResponse == null) {

                Toast.makeText(HomeActivity.this, "Please Try again!",
                        Toast.LENGTH_LONG).show();
                return;
            }
            db = new DatabaseHandler(getBaseContext());
            for (Service serObv : serviceResponse) {
                //System.out.println("Get service is working" + serObv.SERVICEID);
                sqldb = db.getReadableDatabase();
                Cursor courier = sqldb.rawQuery("SELECT * FROM servicedetails WHERE ServiceID='" + serObv.SERVICEID + "'", null);
                sqldb = db.getWritableDatabase();
                if (courier.getCount() > 0) {
                    sqldb.execSQL("UPDATE servicedetails SET ServiceTYPE='" + serObv.SERVICETYPE + "' WHERE ServiceID='" + serObv.SERVICEID + "'");
                } else {

                    String sql = "INSERT  INTO servicedetails (ServiceID, ServiceTYPE) "

                            + "VALUES ('" + serObv.SERVICEID + "', '"
                            + serObv.SERVICETYPE + "')";
                    sqldb.execSQL(sql);
                }
                courier.close();
            }
            db.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


	/*public class getroutetask extends AsyncTask<Void, Void, String>
	{

		String response = "";
		public void onPreExecute()
		{
			super.onPreExecute();
			mPb.setVisibility(View.VISIBLE);
		}
		@Override
		protected String doInBackground(Void... arg0)
		{


			return "";
		}



		@Override
		public void onPostExecute(String res)
		{



		}
	}*/

    public void onResume() {
        super.onResume();
        if (pushWaybillCount == 0) {
            textViewPushicon.setText(String.valueOf(""));
        } else {
            textViewPushicon.setText(String.valueOf(pushWaybillCount));

        }
        mPb.setVisibility(View.INVISIBLE);
        routtxt.setText("Route:" + rte1);
        //	flagpush=0;

    }

    public void onPostExecute() {

        mPb.postDelayed(new Runnable() {
            @Override
            public void run() {
                //Do something after 100ms
                mPb.setVisibility(View.INVISIBLE);

                mPb.postDelayed(this, 15000);
            }
        }, 15000);

    }

    @Override
    public void onBarcodeEvent(BarcodeReadEvent barcodeReadEvent) {

    }

    @Override
    public void onFailureEvent(BarcodeFailureEvent barcodeFailureEvent) {

    }

    @Override
    public void onTriggerEvent(TriggerStateChangeEvent triggerStateChangeEvent) {

    }

    /*@Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        startActivity(intent);
        finish();
        System.exit(0);
    }*/
    @Override
    public void onPause() {
        super.onPause();
        if (barcodeReader != null) {
            barcodeReader.removeBarcodeListener(HomeActivity.this);
            barcodeReader.release();
        }
        // _activity.finish();
    }

    @Override
    public void onBackPressed() {
		/*Intent intent = new Intent(Intent.ACTION_MAIN);
		intent.addCategory(Intent.CATEGORY_HOME);
		startActivity(intent);
		finish();*/
        //System.exit(0);
        if (barcodeReader != null) {
            barcodeReader.removeBarcodeListener(HomeActivity.this);
            barcodeReader.release();
        }
    }


    private void GetOPENRRST() {
        System.out.println("runshhett call:");

        runsheet = WebService.GET_OPENRST(drivercode);
        //if no open runsheet go to delivery page else go to odometer page
        if (runsheet != null) {
            System.out.println("runshhett strn:" + runsheet.RSTNO);

            retrno = runsheet.RTNO;
            rstnumbr = runsheet.RSTNO;
            acknumbr = runsheet.ACKNO;
            openrstERR = runsheet.ERRMSG;

        }

    }

    public void Loader_from_server() {
        // TODO Auto-generated method stub
        System.out.println("Loader_from_server()");
        //new getroutetask().execute();
        //Ak cmntd onpost
        //onPostExecute();
//mPb.setVisibility(View.VISIBLE);

        getroutes();
        //GetOPENRRST();
        // Disable get events on homescreen
        getevent();
        getpickupRemarks();
        getservice();
        getpaytype();
        getdeliverydetail();
        getdeliveryholddetail();
        getcouriers();
        getholdwaybilldetail();
        getpickupddeatils();
        gettranswaybillacceptdt();


        _activity.runOnUiThread(new Runnable() {
            public void run() {
                onPostExecute();
                //mPb.setVisibility(View.VISIBLE);
                //Toast.makeText(_activity, "Sync done Successfully from server to device", Toast.LENGTH_SHORT).show();
            }
        });

        //Toast.makeText(getApplicationContext(), "Sync done Successfully from server to device", Toast.LENGTH_SHORT).show();
        //mPb.setVisibility(View.INVISIBLE);

    }

    //update the call status
    public void Loader_from_Local_callstatus() {
        System.out.println("Loader_from_Local_callstatus()");
        db = new DatabaseHandler(getBaseContext());
        SQLiteDatabase db1 = db.getReadableDatabase();


        gps = new GPSTracker(mContext, HomeActivity.this);

        // check if GPS enabled
        if (gps.canGetLocation()) {

            latitude = gps.getLatitude();
            longitude = gps.getLongitude();
            lati = String.valueOf(latitude);
            longti = String.valueOf(latitude);
            // \n is for new line
            //  Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
        } else {
            // can't get location
            // GPS or Network is not enabled
            // Ask user to enable GPS/network in settings
            gps.showSettingsAlert();
        }
        Cursor rbc = db1.rawQuery("SELECT DRIVERNAME FROM courierdetails WHERE DRIVERCODE='" + drivercode + "'", null);
        int c = rbc.getCount();
        String drivername = null;
        if (c > 0) {
            rbc.moveToFirst();
            drivername = rbc.getString(rbc.getColumnIndex("DRIVERNAME"));
        }
        rbc.close();
        Cursor calcu = db1.rawQuery("SELECT * FROM deliverydata WHERE SyncCallstatus=0 AND Drivercode='" + drivercode + "'", null);

        int oc1 = calcu.getCount();
        //System.out.println(oc1);
        String[] wbill = new String[oc1];
        String[] phone = new String[oc1];
        //SimpleDateFormat date1 = new SimpleDateFormat("yyyy-MMM-dd HH:mm:ss");
        String[] date_time = new String[oc1];
        calcu.moveToFirst();

        if (oc1 > 0) {
            //System.out.println("stage1");
            for (int i = 0; i < oc1; i++) {
                //System.out.println("stage2");
                wbill[i] = calcu.getString(calcu.getColumnIndex("Waybill"));
                //	System.out.println("WBILL:"+wbill[i]);
                phone[i] = calcu.getString(calcu.getColumnIndex("Telephone"));
                //	System.out.println("PHONE:"+phone[i]);


                date_time[i] = calcu.getString(calcu.getColumnIndex("CallTime"));


                //System.out.println("stage3");
                //	calststus=WebService.setcallstatus(wbill[i],"CU-CALL",drivername,"CALL TO CUSTOMER @ " +phone[i],date_time[i],lati,longti,MasterActivity.METHOD_NAME39);
                calststus = WebService.SET_AWB_EVENT(wbill[i], "CU-CALL", drivercode, "CALL TO CUSTOMER @ " + phone[i], date_time[i], lati, longti);
                //System.out.println("stage4");
                //System.out.println("WBILL:"+wbill[i]);

                db1 = db.getWritableDatabase();
                db1.execSQL("UPDATE deliverydata SET Callstatus=1 WHERE Waybill='" + wbill[i] + "'");
                db1.execSQL("UPDATE deliverydata SET SyncCallstatus=0 WHERE Waybill='" + wbill[i] + "'");
                if (calststus) {
                    //flag=1;
                    db1.execSQL("UPDATE deliverydata SET SyncCallstatus=1,Latitude='" + latitude + "',Longtitude='" + longitude + "'WHERE Waybill='" + wbill[i] + "'");
                } else {
                    //flag=0;
                    Log.e("Message:", "Error");
                }
                calcu.moveToNext();
            }
        }
        calcu.close();
        db1.close();
    }

    //function to sync fuel image
    private void Loader_from_Local_fuelimages() {
        // TODO Auto-generated method stub
        System.out.println("Loader_from_Local_fuelimages()");

        db = new DatabaseHandler(getBaseContext());
        SQLiteDatabase db1 = db.getReadableDatabase();

        Cursor rbc = db1.rawQuery("SELECT * FROM Fueldatatable WHERE SyncStatus=1 AND ImageSyncstatus=0 AND Drivercode='" + drivercode + "'", null);

        //System.out.println("stage1");
        int c = rbc.getCount();
        String fid = null;

        //String[] dcode1=new String[c];


        rbc.moveToFirst();
        //	System.out.println("stage2");
        if (c > 0) {
            //	System.out.println("stage3");

            fid = rbc.getString(rbc.getColumnIndex("Fuelid"));
            //dcode1[i]=rbc.getString(rbc.getColumnIndex("DRIVERCODE"));


            //System.out.println("i:"+fid);
            //System.out.println(dcode1[i]);


            byte[] byteArray1 = null;
            byte[] byteArray2 = null;

            File sdCardRoot = Environment.getExternalStorageDirectory();
            File yourDir = new File(sdCardRoot, "Postaplus/Fuel&Odoimage");
            for (File f : yourDir.listFiles()) {
                if (f.isFile())
                    if (f.getName().contains("Fuelodo")) {
                        System.out.println("Fuelodo");


                        ByteArrayOutputStream baos = new ByteArrayOutputStream();

                        FileInputStream fis;

                        try {
                            fis = new FileInputStream(new File(yourDir + "/" + f.getName()));


                            byte[] buf = new byte[1024];
                            int n;
                            while (-1 != (n = fis.read(buf)))
                                baos.write(buf, 0, n);

                            //byteArray1 = baos.toByteArray();
                            BitmapFactory.Options options = new BitmapFactory.Options();
                            options.inSampleSize = 24;
                            bitmap1 = BitmapFactory.decodeFile(f.getAbsoluteFile().toString(), options);
                            //bitmap1.compress(Bitmap.CompressFormat.JPEG, 100,baos);
                            System.out.println("bitmaps odo kb in sync is :" + bitmap1.getByteCount() / 1024);

                        } catch (FileNotFoundException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        } catch (IOException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }


                    }

                if (f.getName().contains("FuelReciept")) {
                    System.out.println("FuelReciept");


                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    FileInputStream fis;

                    try {
                        fis = new FileInputStream(new File(yourDir + "/" + f.getName()));
                        byte[] buf = new byte[1024];
                        int n;
                        while (-1 != (n = fis.read(buf)))
                            baos.write(buf, 0, n);

                        byteArray2 = baos.toByteArray();
                        BitmapFactory.Options options = new BitmapFactory.Options();
                        //	options.inSampleSize =24;
                        options.inSampleSize = 4;
                        bitmap2 = BitmapFactory.decodeFile(f.getAbsoluteFile().toString(), options);
                        //bitmap2.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                        System.out.println("bitmaps recpt kb in sync is :" + bitmap2.getByteCount() / 1024);

                    } catch (FileNotFoundException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
            //	syncstatus=WebService.syncfuelimage(fid,byteArray1,"fuel",byteArray2,"START",METHOD_NAME38);
            syncstatus = WebService.SET_ODO_FUEL_IMAGE(fid, bitmap1, "fuel", bitmap2, "START");

            if (syncstatus) {
                db1 = db.getWritableDatabase();
                db1.execSQL("UPDATE Fueldatatable SET ImageSyncstatus=1 WHERE DRIVERCODE='" + drivercode + "'");
            }


        }
        db1.close();
        rbc.close();
        if (syncstatus) {
            _activity.runOnUiThread(new Runnable() {
                public void run() {
                    Toast.makeText(getApplicationContext(), "Fuel image ack done Successfully", Toast.LENGTH_SHORT).show();
                }
            });


        } else {
            _activity.runOnUiThread(new Runnable() {
                public void run() {
                    Toast.makeText(getApplicationContext(), "Fuel Image synch was  unsucessful", Toast.LENGTH_SHORT).show();
                    Toast.makeText(getApplicationContext(), "Fuel image ack done Successfully", Toast.LENGTH_SHORT).show();
                }
            });


        }


    }

    //function to sync wbill ack images
    private void Loader_from_Local_wbillimages() {
        System.out.println("Loader_from_Local_wbillimages()");
        db = new DatabaseHandler(getBaseContext());
        db1 = db.getReadableDatabase();

        Cursor rbc = db1.rawQuery("SELECT * FROM wbillimagesdata WHERE TransferStatus=0 AND Drivercode='" + drivercode + "'", null);

        File sdCardRoot = Environment.getExternalStorageDirectory();
        File yourDir = new File(sdCardRoot, "Postaplus/Wbill_ackimage");
        // File newFile = new File(sdCardRoot, "Postaplus/Backup_Wbill_ackimage");
        System.out.println("yourdir wbillimage" + yourDir);
        //System.out.println("stage1");
        int c = rbc.getCount();
        String[] id = new String[c];
        String[] wbill1 = new String[c];
        String[] dcode1 = new String[c];
        String[] image1 = new String[c];
        String[] transfer1 = new String[c];
        String eventcode1 = null, runcode11 = null;
        rbc.moveToFirst();
        //	System.out.println("stage2");
        System.out.println("count in local wbillimage" + c);
        if (c > 0) {
            //	System.out.println("stage3");
            for (int i = 0; i < c; i++) {
                //	System.out.println("stage4");
                //id[i]=rbc.getString(rbc.getColumnIndex("ID"));
                wbill1[i] = rbc.getString(rbc.getColumnIndex("Waybill"));
                dcode1[i] = rbc.getString(rbc.getColumnIndex("Drivercode"));
                image1[i] = rbc.getString(rbc.getColumnIndex("Image_filename"));
                transfer1[i] = rbc.getString(rbc.getColumnIndex("TransferStatus"));

                //System.out.println("i:"+i+wbill1[i]);
                //System.out.println(dcode1[i]);
                //System.out.println(image1[i]);
                //System.out.println(transfer1[i]);
                //eventcode
                Cursor eventcc = db1.rawQuery("SELECT Eventcode FROM wbilldata WHERE Waybill='" + wbill1[i] + "'", null);
                int eventcount = eventcc.getCount();
                eventcc.moveToFirst();
                if (eventcount > 0) {

                    eventcode1 = eventcc.getString(eventcc.getColumnIndex("Eventcode"));
                }
                //System.out.println("i:Eventcode:"+eventcode1);
                eventcc.close();

                //runsheet code
                Cursor runc = db1.rawQuery("SELECT Runsheetcode FROM logindata WHERE Username='" + drivercode + "'", null);
                runc.moveToFirst();

                Integer count1 = runc.getCount();

                if (count1 > 0) {
                    runcode11 = runc.getString(runc.getColumnIndex("Runsheetcode"));

                }
                runc.close();
                System.out.println("iww:Runsheetcode:" + runcode11);


                for (File f : yourDir.listFiles()) {
                    System.out.println("isfile in wbillimage" + f.isFile());
                    System.out.println("fgetame in wbillimage" + f.getName());
                    if (f.isFile())
                        if (f.getName().contains(wbill1[i]) && !f.getName().contains(wbill1[i] + "_" + "sign")) {
                            System.out.println("wbill without sign");
									/*BitmapFactory.Options options = new BitmapFactory.Options();
									options.inPreferredConfig = Bitmap.Config.ARGB_8888;
									Bitmap bitmap = BitmapFactory.decodeFile(yourDir+"/"+f.getName(), options);
									//	System.out.println("bitmap:"+bitmap);
									//convert bitmap to bytearray
									ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
									bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
									byte[] byteArray = byteArrayOutputStream.toByteArray();*/

                            //System.out.println("bytearr:"+byteArray);


                            ByteArrayOutputStream baos = new ByteArrayOutputStream();


                            FileInputStream fis;
                            byte[] byteArray = null;
                            try {
                                fis = new FileInputStream(new File(yourDir + "/" + f.getName()));
                                System.out.println("fis in sync is :" + fis + "ff" + f.getPath());
                                byte[] buf = new byte[1024];
                                int n;
                                while (-1 != (n = fis.read(buf)))
                                    baos.write(buf, 0, n);

                                BitmapFactory.Options options = new BitmapFactory.Options();
                                options.inSampleSize = 1;
                                //withdot sampl
                                bitmap = BitmapFactory.decodeFile(f.getAbsoluteFile().toString());
                                // bitmap = BitmapFactory.decodeFile(f.getAbsoluteFile().toString(),options);
                                System.out.println("bitmaps kb in sync is :" + bitmap.getByteCount() / 1024);
                                File resource = new File(f.getAbsoluteFile().toString());
                                //bitmap.compress(Bitmap.CompressFormat.JPEG,100, baos);


                                //	int size = bitmap.getRowBytes() * bitmap.getHeight();
										/*int size = bitmap.getWidth() * bitmap.getHeight();
										ByteBuffer byteBuffer = ByteBuffer.allocate(size);
										bitmap.copyPixelsToBuffer(byteBuffer);
										byteArray = byteBuffer.array();*/


                                ///	byteArray = baos.toByteArray();
										/*if(byteArray.length>2500000){
											byteArray = ;
											Log.e("byteArray/suc",byteArray.toString());
										}else{
											System.out.println("byteArray err is :");
											byteArray = baos.toByteArray();
										}
*/      // byteArray=f.getAbsoluteFile().getName().getBytes();
                                System.out.println("resource fil in sync is :" + resource);

                            } catch (FileNotFoundException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            } catch (IOException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }

                            //	System.out.println("byteArray in sync is :"+byteArray.length);

                            //syncstatus=WebService.syncimage(wbill1[i],runcode11,eventcode1,byteArray,drivercode,"ACK",METHOD_NAME27);
                            syncstatus = WebService.SET_WAYBILLACK_IMG(wbill1[i], runcode11, eventcode1, bitmap, drivercode, "ACK");
                            System.out.println("syncstatus in sync is :" + syncstatus + "bytearray :" + byteArray);
                            if (syncstatus) {
                                db1 = db.getWritableDatabase();
                                db1.execSQL("UPDATE wbillimagesdata SET TransferStatus=1 WHERE Waybill='" + wbill1[i] + "'");
									/*		File dir = new File(Environment.getExternalStorageDirectory()+"/Postaplus/Wbill_ackimage");
											File dir1 = new File(Environment.getExternalStorageDirectory()+"/Postaplus/Backup_Wbill_ackimage");

											try {
												copyDirectoryOneLocationToAnotherLocation(dir,dir1);
											} catch (IOException e) {
												// TODO Auto-generated catch block
												e.printStackTrace();
											}

										*/
                            }

                        } else if (f.getName().contains(wbill1[i] + "_" + "sign")) {
                            System.out.println("wbill with sign");
								/*BitmapFactory.Options options = new BitmapFactory.Options();
								options.inPreferredConfig = Bitmap.Config.ARGB_8888;
								Bitmap bitmap = BitmapFactory.decodeFile(yourDir+"/"+f.getName(), options);
								//	System.out.println("bitmap:"+bitmap);
								//convert bitmap to bytearray
								ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
								bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
								byte[] byteArray = byteArrayOutputStream.toByteArray();*/

                            //System.out.println("bytearr:"+byteArray);
                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
                            FileInputStream fis;
                            byte[] byteArray = null;
                            try {
                                fis = new FileInputStream(new File(yourDir + "/" + f.getName()));
                                byte[] buf = new byte[1024];
                                int n;
                                while (-1 != (n = fis.read(buf)))
                                    baos.write(buf, 0, n);

                                //byteArray = baos.toByteArray();

                                BitmapFactory.Options options = new BitmapFactory.Options();
                                options.inSampleSize = 1;
                                //without samppling							//bitmap = BitmapFactory.decodeFile(f.getAbsoluteFile().toString()]);
                                bitmap = BitmapFactory.decodeFile(f.getAbsoluteFile().toString(), options);
                                System.out.println("bitmaps kb sing in sync is :" + bitmap.getByteCount() / 1024);
                                //bitmap.compress(Bitmap.CompressFormat.JPEG,100, baos);
                                //System.out.println("bitmaps new kb sing in sync is :"+bitmap.getByteCount()/1024);
                            } catch (FileNotFoundException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            } catch (IOException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }


                            //syncstatus=WebService.syncimage(wbill1[i],runcode11,eventcode1,byteArray,drivercode,"SIGN",METHOD_NAME27);
                            syncstatus = WebService.SET_WAYBILLACK_IMG(wbill1[i], runcode11, eventcode1, bitmap, drivercode, "SIGN");
                            if (syncstatus) {
                                db1 = db.getWritableDatabase();
                                db1.execSQL("UPDATE wbillimagesdata SET TransferStatus=1 WHERE Waybill='" + wbill1[i] + "'");


                            }
                        }

                }
                rbc.moveToNext();


            }
        }
        rbc.close();
        db1.close();

        if (syncstatus) {
            _activity.runOnUiThread(new Runnable() {
                public void run() {
                    Toast.makeText(_activity, "WaybillImage ACK done Successfully", Toast.LENGTH_SHORT).show();
                }
            });
            //Toast.makeText(getApplicationContext(), "WaybillImage ACK done Successfully", Toast.LENGTH_SHORT).show();
            //mPb.setVisibility(View.INVISIBLE);

            File dir = new File(Environment.getExternalStorageDirectory() + "/Postaplus/Wbill_ackimage");
            File dir1 = new File(Environment.getExternalStorageDirectory() + "/Postaplus/Backup_Wbill_ackimage");

            try {
                copyDirectoryOneLocationToAnotherLocation(dir, dir1);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            //File dir = new File(Environment.getExternalStorageDirectory()+"/Postaplus/Wbill_ackimage");
            //System.out.println("stage2");


            if (dir.isDirectory()) {
                String[] children = dir.list();
                for (int i = 0; i < children.length; i++) {
                    new File(dir, children[i]).delete();

                }
            }


        } else {
            //Toast.makeText(getApplicationContext(), "Image Sync Error", Toast.LENGTH_SHORT).show();
            _activity.runOnUiThread(new Runnable() {
                public void run() {
                    Toast.makeText(getApplicationContext(), "Image synch was unsucessful", Toast.LENGTH_SHORT).show();
                    //Toast.makeText(_activity, "WaybillImage ACK done Successfully", Toast.LENGTH_SHORT).show();
                }
            });

            //mPb.setVisibility(View.INVISIBLE);
        }
/*

				if(errored){
					Toast.makeText(getApplicationContext(), "WaybillImage ACK ERROR", Toast.LENGTH_SHORT).show();
					mPb.setVisibility(View.VISIBLE);
				}
*/


        //db.close();
    }

    //functiom to sync wbill hold transfer
    private void Loader_from_Local_wbillhold_transfer() {
        // TODO Auto-generated method stub
        System.out.println("Loader_from_Local_wbillhold_transfer()");

        db = new DatabaseHandler(getBaseContext());
        SQLiteDatabase db1 = db.getReadableDatabase();

        Cursor rr1 = db1.rawQuery("SELECT * FROM Holdwaybilldata WHERE TransferStatus=0 AND Drivercode='" + drivercode + "' AND HOLD_Transfer_Status=1", null);
        int c1 = rr1.getCount();


        String[] trancourier = new String[c1];
        String[] wbill1 = new String[c1];


        rr1.moveToFirst();
        if (c1 > 0) {

            for (int i = 0; i < c1; i++) {
                trancourier[i] = rr1.getString(rr1.getColumnIndex("Transdriver_Code"));

                wbill1[i] = rr1.getString(rr1.getColumnIndex("Waybill"));


                rr1.moveToNext();
                //holdstatus=WebService.settranshold(drivercode,trancourier[i],wbill1[i],METHOD_NAME25);
                holdstatus = WebService.SET_TRANS_HOLD(drivercode, trancourier[i], wbill1[i]);

                if (holdstatus == null) {

                    Toast.makeText(HomeActivity.this, "Please Try again!",
                            Toast.LENGTH_LONG).show();
                    return;
                }
                if (holdstatus.contains("TRANSFER")) {
                    db1 = db.getWritableDatabase();
                    db1.execSQL("UPDATE Holdwaybilldata SET TransferStatus=1 WHERE Waybill='" + wbill1[i] + "'");
                }

            }
        }
        rr1.close();
        db1.close();
    }

    // functiom to sync wbillWC_Transfer
    private void Loader_from_Local_wbillwc_transfer() {
        // TODO Auto-generated method stub
        System.out.println("Loader_from_Local_wbillwc_transfer()");

        db = new DatabaseHandler(getBaseContext());
        SQLiteDatabase db1 = db.getReadableDatabase();

        Cursor rr12 = db1.rawQuery("SELECT * FROM deliverydata WHERE TransferStatus=0 AND Drivercode='" + drivercode + "' AND WC_Transfer_Status=1", null);
        int c1 = rr12.getCount();


        String[] trancourier = new String[c1];
        String[] wbill1 = new String[c1];


        rr12.moveToFirst();
        if (c1 > 0) {

            for (int i = 0; i < c1; i++) {
                trancourier[i] = rr12.getString(rr12.getColumnIndex("Transdriver_Code"));

                wbill1[i] = rr12.getString(rr12.getColumnIndex("Waybill"));


                rr12.moveToNext();
                //	wc_transfer_status=WebService.settranswc(drivercode,trancourier[i],wbill1[i],METHOD_NAME26);
                System.out.println("trans hme" + trancourier[i]);
                if (trancourier[i] != null) {
                    wc_transfer_status = WebService.SET_TRANS_WC(drivercode, trancourier[i], wbill1[i]);
                }
                if (wc_transfer_status != null) {
                    if (wc_transfer_status.contains("TRANSFER")) {
                        db1 = db.getWritableDatabase();
                        db1.execSQL("UPDATE deliverydata SET TransferStatus=1 WHERE Waybill='" + wbill1[i] + "'");

                    }
                }
            }
        }
        rr12.close();
        db1.close();
    }

    // function for pickup sync
    private void Loader_from_Local_Pickup() {
        // TODO Auto-generated method stub
        System.out.println("Loader_from_Local_Pickup");
        db = new DatabaseHandler(getBaseContext());
        SQLiteDatabase db1 = db.getReadableDatabase();

        Cursor rr121 = db1.rawQuery("SELECT Pickup_No FROM pickuphead WHERE TransferStatus<>2 AND Status='C'", null);
        int c11 = rr121.getCount();


        String[] pickno = new String[c11];

        rr121.moveToFirst();
        if (c11 > 0) {

            setpkpdtRequest = new setPickUpDt[c11];
            for (int i = 0; i < c11; i++) {
                setpkpdtRequest[i] = new setPickUpDt();

            }


            for (int i = 0; i < c11; i++) {
                setpkpdtRequest[i].PICKUPNO = rr121.getString(rr121.getColumnIndex("Pickup_No"));
                //System.out.println(pickno[i]);

                //select data from pickupdetail have status completed
                Cursor rr123 = db1.rawQuery("SELECT * FROM pickupdetails WHERE Pickup_No='" + setpkpdtRequest[i].PICKUPNO + "'", null);
                int c112 = rr123.getCount();


                if (c112 > 0) {
                    rr123.moveToFirst();
                    for (int i1 = 0; i1 < c112; i1++) {
                        String status = "PU";
                        setpkpdtRequest[i1].PICKUPNO = rr123.getString(rr123.getColumnIndex("Pickup_No"));
                        setpkpdtRequest[i1].WAYBILL = rr123.getString(rr123.getColumnIndex("Waybill_Number"));
                        setpkpdtRequest[i1].PAYTYPE = rr123.getString(rr123.getColumnIndex("PayType"));
                        setpkpdtRequest[i1].AMOUNT = rr123.getString(rr123.getColumnIndex("Amount"));
                        setpkpdtRequest[i1].SERVICE = rr123.getString(rr123.getColumnIndex("ServiceType"));
                        setpkpdtRequest[i1].DATETIMESTR = rr123.getString(rr123.getColumnIndex("Date_Time"));
                        setpkpdtRequest[i1].STATUS = status;
                        //setpkpdtRequest[i1].TAGVAL=rr123.getString(rr123.getColumnIndex("Tag_value"));
                        rr123.moveToNext();
                        //	pick_status=WebService.setpickupdetails(drivercode,pickno1[i1],wbill1[i1],paytype[i1],amount[i1],service[i1],date_time[i1],METHOD_NAME22);

                        //		pick_status = WebService.SET_PICKUPDETAILS(drivercode, pickno1[i1], wbill1[i1], paytype[i1], amount[i1], service[i1], date_time[i1],status);
									/*if (pick_status.equals("TRUE")) {
									db1 = db.getWritableDatabase();
									db1.execSQL("UPDATE pickuphead SET Status='C',TransferStatus=2 WHERE Pickup_No='" + setpkpdtRequest[i1].PICKUPNO + "'");

								}*/
                    }
                    //pick_status = WebService.SET_PICKUPDETAILS(drivercode, setpkpdtRequest);
                    pick_status = WebService.SET_PICKUPDETAILS(drivercode, setpkpdtRequest[0].PICKUPNO);
                    if (pick_status != null) {
                        if (pick_status.equals("TRUE")) {
                            db1 = db.getWritableDatabase();
                            db1.execSQL("UPDATE pickuphead SET TransferStatus=2 WHERE Pickup_No='" + setpkpdtRequest[i].PICKUPNO + "'");

                        }
                    }
                }
                rr121.moveToNext();
                rr123.close();
            }
        }

        rr121.close();
        db1.close();

    }

    // function for set delivery/waybill event
    private void Loader_from_Local_wbillevent() {
        // TODO Auto-generated method stub
        System.out.println("Loader_from_Local_wbillevent()");

        db = new DatabaseHandler(getBaseContext());
        SQLiteDatabase db1 = db.getReadableDatabase();

        Cursor rr = db1.rawQuery("SELECT * FROM wbilldata WHERE TransferStatus=0", null);
        int c1 = rr.getCount();
        String[] evname = new String[c1];
        String[] rname = new String[c1];
        String[] dcode = new String[c1];
        String[] wbill1 = new String[c1];
        String[] eventcode1 = new String[c1];
        String[] date_time = new String[c1];
        String[] lat = new String[c1];
        String[] long11 = new String[c1];
        rr.moveToFirst();
        int flag = 0;
        if (c1 > 0) {

            for (int i = 0; i < c1; i++) {
                evname[i] = rr.getString(rr.getColumnIndex("Event_note"));
                rname[i] = rr.getString(rr.getColumnIndex("Reciever_Name"));
                dcode[i] = rr.getString(rr.getColumnIndex("Drivercode"));
                wbill1[i] = rr.getString(rr.getColumnIndex("Waybill"));
                eventcode1[i] = rr.getString(rr.getColumnIndex("Eventcode"));
                date_time[i] = rr.getString(rr.getColumnIndex("Date_Time"));
                lat[i] = rr.getString(rr.getColumnIndex("Latitude"));
                long11[i] = rr.getString(rr.getColumnIndex("Longtitude"));
                sqldb = db.getReadableDatabase();
                Cursor wd = sqldb.rawQuery("SELECT * FROM deliverydata WHERE waybill = '" + wbill1[i] + "'", null);
                int wdcount = wd.getCount();
                System.out.println("wdcount are:" + wdcount);
                String IsCODCollected = "N";
                String WaybillIdentifier = "NRML";
                String BarcodeIdentifier = "N";
                wd.moveToFirst();
                if (wdcount > 0 && IsCODCollected == null) {
                    IsCODCollected = wd.getString(wd.getColumnIndex("IsCollectedCOD"));
                    WaybillIdentifier = wd.getString(wd.getColumnIndex("AWBIdentifier"));
                    BarcodeIdentifier = wd.getString(wd.getColumnIndex("BarcodeIdentifier"));
                    System.out.println("Barcodeidentifier is" + wd.getString(wd.getColumnIndex("BarcodeIdentifier")));
                }
                wd.close();
                waynillist.add(wbill1[i]);
                waynillist = new ArrayList<String>(new LinkedHashSet<String>(waynillist));
                System.out.println("waylist in home:" + waynillist);
                deliverystatus = WebService.SET_DELIVERY(rname[i], dcode[i], waynillist, eventcode1[i], lat[i], long11[i], date_time[i], evname[i], WaybillIdentifier, IsCODCollected, BarcodeIdentifier);
                //	deliverystatus=WebService.setdelivery(rname[i],dcode[i],wbill1[i],eventcode1[i],lat[i],long11[i],date_time[i],evname[i],METHOD_NAME20);
                //	AK	//deliverystatus = WebService.SET_DELIVERY(rname[i], dcode[i], wbill1[i], eventcode1[i], lat[i], long11[i], date_time[i], evname[i], WaybillIdentifier, IsCODCollected, BarcodeIdentifier);
                //System.out.println("stage1");
                if (deliverystatus.contains("True")) {
                    db1 = db.getWritableDatabase();
                    sqldb = db.getWritableDatabase();
                    waynillist = new ArrayList<String>();
                    sqldb.execSQL("UPDATE deliverydata SET WC_Status='C' WHERE Waybill='" + wbill1[i] + "'");
                    db1.execSQL("UPDATE wbilldata SET TransferStatus=1 WHERE Waybill='" + wbill1[i] + "'");
                    sqldb.execSQL("UPDATE deliverydata SET Attempt_Status=1 WHERE Waybill='" + wbill1[i] + "'");
                    //flag=1;
                    if ((eventcode1[i].contains("SD")) || (eventcode1[i].contains("NT")) || eventcode1[i].contains("MR") || eventcode1[i].contains("DELIVERED")) {
                        db1 = db.getWritableDatabase();
                        sqldb = db.getWritableDatabase();
                        //System.out.println("stage3");
                        sqldb.execSQL("UPDATE deliverydata SET StopDelivery=1 WHERE Waybill='" + wbill1[i] + "'");
                        db1.execSQL("UPDATE deliverydata SET ApprovalStatus='APPROVED' WHERE Waybill='" + wbill1[i] + "'");
                    }
                } else if (deliverystatus.contains("REQ")) {
                    //System.out.println("stage4");
                    db1 = db.getWritableDatabase();
                    flag = 1;
                    db1.execSQL("UPDATE deliverydata SET ApprovalStatus='REQ' WHERE Waybill='" + wbill1[i] + "'");
                } else {
                    this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), deliverystatus, Toast.LENGTH_LONG).show();
                        }
                    });
                }
                //	else Toast.makeText(getApplicationContext(), deliverystatus, Toast.LENGTH_LONG).show();
                rr.moveToNext();
            }
        }
        if (flag == 1) {
            //System.out.println("stage5");
            Toast.makeText(getApplicationContext(), "REQUESTED WAYBILL NOT APPROVED TO UPDATE", Toast.LENGTH_LONG).show();
        }
        rr.close();
        db1.close();
    }

    public void copyDirectoryOneLocationToAnotherLocation(File sourceLocation, File targetLocation)
            throws IOException {

        if (sourceLocation.isDirectory()) {
            if (!targetLocation.exists()) {
                targetLocation.mkdir();
            }

            String[] children = sourceLocation.list();
            for (int i = 0; i < sourceLocation.listFiles().length; i++) {

                copyDirectoryOneLocationToAnotherLocation(new File(sourceLocation, children[i]),
                        new File(targetLocation, children[i]));
            }
        } else {

            InputStream in = new FileInputStream(sourceLocation);

            OutputStream out = new FileOutputStream(targetLocation);

            // Copy the bits from instream to outstream
            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            in.close();
            out.close();
        }

    }

    public boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        return ni != null;
    }

    private class AsyncLoaderfromServer extends AsyncTask<Void, String, Void> {


        @Override
        protected Void doInBackground(Void... voids) {
            Loader_from_server();
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            _activity.runOnUiThread(new Runnable() {
                public void run() {
                    mPb.setVisibility(View.VISIBLE);

                }
            });


        }


        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            //Loader_from_server();
            //this method will be running on UI thread
            _activity.runOnUiThread(new Runnable() {
                public void run() {
					/*mPb.setVisibility(View.VISIBLE);
					mPb.postDelayed(new Runnable() {
						@Override
						public void run() {
							//Do something after 100ms
							//mPb.setVisibility(View.INVISIBLE);
							mPb.postDelayed(this, 15000);
						}
					}, 15000);
*/
                    //	System.out.println("AsyncLoaderfromServer val on post:"+ new AsyncLoaderfromServer().getStatus());
                    if (new AsyncLoaderfromServer().getStatus() == PENDING) {
                        mPb.setVisibility(View.VISIBLE);
                    } else
                        mPb.setVisibility(View.INVISIBLE);
                    Toast.makeText(_activity, "Sync done Successfully from server to device", Toast.LENGTH_SHORT).show();
                }
            });

        }

    }

    // Async for call status
    public class Async_Call_L_S extends AsyncTask<Void, String, Void> {


        @Override
        protected Void doInBackground(Void... voids) {
            Loader_from_Local_callstatus();
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //mPb.setVisibility(View.VISIBLE);


        }


        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            //Loader_from_server();
            //this method will be running on UI thread
            //mPb.setVisibility(View.VISIBLE);


        }

    }

    // Async for fuel imges
    public class Async_FuelImg_L_S extends AsyncTask<Void, String, Void> {


        @Override
        protected Void doInBackground(Void... voids) {
            Loader_from_Local_fuelimages();
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //	mPb.setVisibility(View.VISIBLE);

        }


        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            //Loader_from_server();
            //this method will be running on UI thread
            //mPb.setVisibility(View.INVISIBLE);

        }

    }

    // Async for WbillImages ack
    public class Async_WbillImg_L_S extends AsyncTask<Void, String, Void> {


        @Override
        protected Void doInBackground(Void... voids) {
            Loader_from_Local_wbillimages();
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //	mPb.setVisibility(View.VISIBLE);

        }


        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            //	mPb.setVisibility(View.INVISIBLE);
            //Loader_from_server();
            //this method will be running on UI thread


        }

    }

    // Async for WbillImages ack
    public class Async_wbillhold_L_S extends AsyncTask<Void, String, Void> {


        @Override
        protected Void doInBackground(Void... voids) {
            Loader_from_Local_wbillhold_transfer();
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //mPb.setVisibility(View.VISIBLE);

        }


        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            //Loader_from_server();
            //this method will be running on UI thread
            //	mPb.setVisibility(View.INVISIBLE);

        }

    }

    // Async for Wbillwc_transfer
    public class Async_wbillwc_L_S extends AsyncTask<Void, String, Void> {


        @Override
        protected Void doInBackground(Void... voids) {
            Loader_from_Local_wbillwc_transfer();
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            //mPb.setVisibility(View.VISIBLE);
        }


        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            //Loader_from_server();
            //this method will be running on UI thread
            //mPb.setVisibility(View.INVISIBLE);

        }

    }

    // Async for Pickup
    public class Async_Pickup_L_S extends AsyncTask<Void, String, Void> {


        @Override
        protected Void doInBackground(Void... voids) {
            Loader_from_Local_Pickup();
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //mPb.setVisibility(View.VISIBLE);

        }


        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            //Loader_from_server();
            //this method will be running on UI thread
            //	mPb.setVisibility(View.INVISIBLE);
            //Toast.makeText(getApplicationContext(), "Sync done successfully to server and the channel closed", Toast.LENGTH_LONG).show();
        }

    }

    // Async for set delivery/waybill event
    public class Async_Local_wbillevent extends AsyncTask<Void, String, Void> {


        @Override
        protected Void doInBackground(Void... voids) {
            Loader_from_Local_wbillevent();
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //mPb.setVisibility(View.VISIBLE);

        }


        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            //Loader_from_server();
            //this method will be running on UI thread
            //	mPb.setVisibility(View.INVISIBLE);
            //Toast.makeText(getApplicationContext(), "Sync done successfully to server and the channel closed", Toast.LENGTH_LONG).show();
        }

    }

    public class Async_SyncTask extends AsyncTask<Void, String, Boolean> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            mPb.setVisibility(View.VISIBLE);
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            db = new DatabaseHandler(getBaseContext());
            SQLiteDatabase db1 = db.getReadableDatabase();

            //sync call status
            Cursor calcu = db1.rawQuery("SELECT * FROM deliverydata WHERE SyncCallstatus=0 AND Drivercode='" + drivercode + "'", null);
            oc1 = calcu.getCount();
            if (oc1 > 0) {

                //	Loader_from_Local_callstatus();

                new Async_Call_L_S().execute();


            }
            System.out.println("async is runinng" + new Async_Call_L_S().getStatus());
            //AK 3Sept cmntd
            //calcu.close();


            //sync odoimage
					/*Cursor odoc = db1.rawQuery("SELECT * FROM logindata WHERE OdometerimgSyncstatus=0 AND Username='"+drivercode+"'", null);
					oc=odoc.getCount();
					if(oc>0){

						Loader_from_Local_odoimages();
					}
					odoc.close();*/


            //sync fuelimages
            Cursor fuelc = db1.rawQuery("SELECT * FROM Fueldatatable WHERE SyncStatus=1 AND ImageSyncstatus=0 AND Drivercode='" + drivercode + "'", null);
            fc = fuelc.getCount();
            if (fc > 0) {

                //	Loader_from_Local_fuelimages();

                new Async_FuelImg_L_S().execute();
            }
            fuelc.close();


            //wbillimage data sync from local to server
            Cursor rbc = db1.rawQuery("SELECT * FROM wbillimagesdata WHERE TransferStatus=0 AND Drivercode='" + drivercode + "'", null);
            c = rbc.getCount();
            if (c > 0) {

                //Loader_from_Local_wbillimages();

                new Async_WbillImg_L_S().execute();
            }
            rbc.close();

            //wbill event data sync from local to server
            Cursor rbc1 = db1.rawQuery("SELECT * FROM wbilldata WHERE TransferStatus=0 AND Drivercode='" + drivercode + "'", null);
            c1 = rbc1.getCount();
            if (c1 > 0) {

                // commented on 12APR18 as sync may create isue as per stag apk
                // Loader_from_Local_wbillevent();

                new Async_Local_wbillevent().execute();
            }
            rbc1.close();

            //Holdwaybilldata_transfer_from_hold data sync from local to server
            Cursor rbc2 = db1.rawQuery("SELECT * FROM Holdwaybilldata WHERE TransferStatus=0 AND Drivercode='" + drivercode + "' AND HOLD_Transfer_Status=1", null);
            c2 = rbc2.getCount();
            System.out.println("transfer waybil:" + rbc2.getCount());
            if (c2 > 0) {

                //Loader_from_Local_wbillhold_transfer();
                new Async_wbillhold_L_S().execute();
            }
            rbc2.close();

            //Transfer_wc data sync from local to server
            Cursor rbc3 = db1.rawQuery("SELECT * FROM deliverydata WHERE TransferStatus=0 AND Drivercode='" + drivercode + "' AND WC_Transfer_Status=1", null);
            c3 = rbc3.getCount();
            System.out.println("transfer wc waybil:" + rbc2.getCount());
            if (c3 > 0) {

                //Loader_from_Local_wbillwc_transfer();
                new Async_wbillwc_L_S().execute();
            }
            rbc3.close();

            //Pickup data sync from local to server
            Cursor rbc4 = db1.rawQuery("SELECT Pickup_No FROM pickuphead WHERE TransferStatus<>2 AND Status='C'", null);
            c4 = rbc4.getCount();
            if (c4 > 0) {

                //Loader_from_Local_Pickup();
                new Async_Pickup_L_S().execute();
            }
            rbc4.close();
            db1.close();
            //local to server finish
            System.out.println("async after runinng" + new Async_Call_L_S().getStatus());

            //Server TO LOCAL
            //Loader_from_server();

            //new AsyncLoaderfromServer().execute();
            new AsyncLoaderfromServer().execute();

            //	clear_channelstatus=WebService.clearchannelstatus(drivercode,MasterActivity.METHOD_NAME41);
            clear_channelstatus = WebService.CLEAR_SYNCH_CHANNEL(drivercode);
            return clear_channelstatus;

        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            mPb.setVisibility(View.GONE);

            // TODO: 2019-11-02 what is the getStatus
//            if (result) {
//                if (new Async_SyncTask().getStatus() == PENDING) {
//                    mPb.setVisibility(View.VISIBLE);
//                } else
//                    mPb.setVisibility(View.INVISIBLE);
//                Toast.makeText(getApplicationContext(), "Sync done successfully to server and the channel closed", Toast.LENGTH_LONG).show();
//            }
            if (result)
                Toast.makeText(getApplicationContext(), "Sync channel cleared", Toast.LENGTH_LONG).show();
            else
                Toast.makeText(getApplicationContext(), "Channel cannot close now", Toast.LENGTH_LONG).show();

        }


    }

}




