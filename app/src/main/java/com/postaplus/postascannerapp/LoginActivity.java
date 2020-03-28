package com.postaplus.postascannerapp;


import android.Manifest;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.os.StrictMode;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.onesignal.OneSignal;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import DatabaseHandlers.DBFCS.TBLogin;
import DatabaseHandlers.DBFunctions;
import androidx.core.app.ActivityCompat;
import webservice.FuncClasses.Couriers;
import webservice.FuncClasses.Events;
import webservice.FuncClasses.HoldWayBills;
import webservice.FuncClasses.OpenRst;
import webservice.FuncClasses.PickUpDt;
import webservice.FuncClasses.PickUpWaybillsDT;
import webservice.FuncClasses.Remarks;
import webservice.FuncClasses.Routes;
import webservice.FuncClasses.RstDetail;
import webservice.FuncClasses.ScanWaybillDt;
import webservice.FuncClasses.Service;
import webservice.WebService;


public class LoginActivity extends MasterActivity {
    public static final String usrnam = "uname";
    static boolean errored = false;
    public String Username;
    public boolean loginStatus, mstatus;
    GPSTracker gps;
    double latitude, longitude;
    String lati, longti;
    int count;
    String[] waybill1, rname1, cname1, phone, area1, company1, civilid1, serial1, cardtype1, deldate1, deltime1, amount1, error1;
    String[] addresshold, waybillhold, cnamehold, phonehold, areahold, companyhold, civilidhold, serialhold, cardtypehold, deldatehold, deltimehold, amounthold, transcourierhold;
    String[] attempt1, pickupno1, acname1, pickaddr1, pickarea1, contact_person1, pick_phone1, picktime1, error11, address1;
    String[] pickupno12, wbill12, paytype12, amount12, service12;
    String rname = "";
    String route, odovalue;
    Button sumbitBtn;
    //TextView statusTV;
    EditText userNameET, passWordET;
    TextView versionDisp;
    ProgressBar webservicePG;
    String Password, serialid;
    String AppVersion = "";
    Events[] eventResponse;
    Service[] serviceResponse;
    webservice.FuncClasses.PayType[] paytypeResponse;
    Couriers[] courierResponse;
    RstDetail[] rstdetailResponse;
    ScanWaybillDt[] scanwbildtResponse;
    PickUpDt[] pickupdtResponse;
    PickUpWaybillsDT[] pickupwblldtResponse;
    HoldWayBills[] holddwaybillResponse;
    Routes[] routesresponse;
    Remarks[] pickupremarksResponse;
    Button clrbtn;
    ActivityNotification actNoty = new ActivityNotification();
    OpenRst runsheet;
    private String[] pickuperror1, routename, routecode, eventid, eventdesc, serviceID, serviceType, PayId, PayType, couriercode, couriername;
    private SharedPreferences loginPreferences;
    private Editor loginPrefsEditor;

    // public  static String retrno ,rstnumbr,acknumbr;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getActionBar().hide();

        loginPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
        loginPrefsEditor = loginPreferences.edit();

        String mVersion = "";
        try {
            mVersion = this.getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            mVersion = "";
        }

        AppVersion = mVersion;
        System.out.println("Application Version : " + AppVersion);
        versionDisp = (TextView) findViewById(R.id.versionTxt);
        System.out.println("versionDisp before login: " + versionDisp);
        versionDisp.setText("Version " + AppVersion);
        pref = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        System.out.println("Login Activity Page");
        //Name Text control
        userNameET = (EditText) findViewById(R.id.unameet);
        passWordET = (EditText) findViewById(R.id.paswrdet);
       /* userNameET.setText("TEST99");
        passWordET.setText("TEST99");*/

        //Button to trigger web service invocation
        sumbitBtn = findViewById(R.id.submit_btn);
        clrbtn = (Button) findViewById(R.id.clrdatabtn);
        //Display progress bar until web service invocation completes
        webservicePG = (ProgressBar) findViewById(R.id.progressBar1);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        //locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000L,1.0f, this);
        boolean isGPS = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (!isGPS)
            startActivityForResult(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS), 0);

        TelephonyManager telephonyManager =
                (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        System.out.println("serila:" + telephonyManager.getDeviceId());

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        serialid = telephonyManager.getDeviceId();

// Clearing cache data
        clrbtn.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {

                clearcashData(LoginActivity.this);
                Toast.makeText(getBaseContext(), "Data Cleared", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getApplicationContext(), ScreenActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("EXIT", true);
                startActivity(intent);

            }
        });


    }

    public void onLoginButtonClick(View v) {
        //Check if text controls are not empty
        if (userNameET.getText().length() != 0 && !userNameET.getText().toString().equals("")) {
            if (passWordET.getText().length() != 0 && !passWordET.getText().toString().equals("")) {
                Username = userNameET.getText().toString();
                Password = passWordET.getText().toString();

                System.out.println("username on logn" + Username);

                //Create instance for AsyncCallWS
                AsyncCallWS task = new AsyncCallWS();
                //Call execute
                task.execute();


            }
            //If Password text control is empty
            else {
                Toast.makeText(getBaseContext(), "Please enter Password",
                        Toast.LENGTH_LONG).show();
            }
            //If Username text control is empty
        } else {
            Toast.makeText(getBaseContext(), "Please enter Username",
                    Toast.LENGTH_LONG).show();
        }
    }


    //To clear cache data method
    public void clearcashData(Context context) {
        File dir = context.getCacheDir();
        System.out.println("dir value is" + dir);
        File appDir = new File(dir.getParent());
        System.out.println("appdir value is" + appDir);
        if (appDir.exists()) {
            String[] children = appDir.list();
            for (String s : children) {
                if (!s.equals("lib")) {
                    deleteDir(new File(appDir, s));
                    //  Log.i("TAG", "data/user/0/com.postaplus.postascannerapp/" + s +" DELETED");
                    Log.i("TAG", "/data/user/0/com.postaplus.postascannerapp" + s + " DELETED");
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

    @Override
    protected void onResume() {
        super.onResume();

        pref = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);


      /*  db = new DatabaseHandler(getBaseContext());
        //open localdatabase in a read mode
        sqldb = db.getReadableDatabase();

        //select  loginstatus and routecode on resume activity
        Cursor c = sqldb.rawQuery("SELECT * FROM logindata WHERE Loginstatus=1", null);
        int count1 = c.getCount();*/
        // DBFunctions.GetLoggedUser(getBaseContext());
        TBLogin ActiveLogin = DBFunctions.GetLoggedUser(getBaseContext());
        if (ActiveLogin != null) {
            //  if (count1 > 0) {
            /*c.moveToFirst();

            String uname = c.getString(c.getColumnIndex("Username"));

            int login = c.getInt(c.getColumnIndex("Loginstatus"));
            String route1 = c.getString(c.getColumnIndex("Routecode"));

            c.close();
            db.close();*/

            //check if user logged in or not
            if (ActiveLogin.LOGIN_STATUS == 1) {
                Intent intent = new Intent(getBaseContext(), PostaNotificationService.class);
                startService(intent);


                Toast.makeText(getBaseContext(), "You logged in..",
                        Toast.LENGTH_SHORT).show();


                if ((ActiveLogin.ROUTE_CODE != null) && (!ActiveLogin.ROUTE_CODE.equals("No Route"))) {
                    //System.out.println("resume2");
                    db = new DatabaseHandler(getBaseContext());
                    //open localdatabase in a read mode
                    sqldb = db.getReadableDatabase();

                    //int rout1 = Integer.parseInt(ActiveLogin.ROUTE_CODE);

                    //select all values in the table and check count
                    Cursor c1 = sqldb.rawQuery("SELECT * FROM routedata WHERE ROUTECODE=" + ActiveLogin.ROUTE_CODE, null);
                    if (c1.getCount() > 0) {
                        c1.moveToFirst();
                        rname = c1.getString(c1.getColumnIndex("ROUTENAME"));

                    }
                    c1.close();
                    //pass value of route name and code to HomeActivity
                    Intent i = new Intent(this, HomeActivity.class);
                    i.putExtra("route1", String.valueOf(rname));
                    i.putExtra("route", ActiveLogin.ROUTE_CODE);
                    actNoty.setRouteName(this, String.valueOf(rname));
                    actNoty.setRouteCode(this, ActiveLogin.ROUTE_CODE);
                    //actNoty.setdrivercode(this,Username);
                    OneSignal.sendTag("username", Username);

                    startActivity(i);
                    db.close();
                } else {

                    Toast.makeText(getBaseContext(), "You logged in..",
                            Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(this, RouteActivity.class);
                    startActivity(i);
                }
            }
        } else {
            // c.close();
            //   db.close();
            Loader_from_server();
        }
        //    db.close();

        //	}
    }


    //sync automatically from server
    private void Loader_from_server() {
        // TODO Auto-generated method stub
        new getroutetask().execute();
    }

    // Hiding of soft keyboard when touched anywhere on screen
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.
                INPUT_METHOD_SERVICE);
        if (imm != null && getCurrentFocus() != null) {
            IBinder windowToken = getCurrentFocus().getWindowToken();
            if (windowToken != null)
                imm.hideSoftInputFromWindow(windowToken, 0);
        }
        return true;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            //System.exit(0);
            return true;

        }
        return false;
    }

    public class getroutetask extends AsyncTask<Void, Void, String> {

        String response = "";

        public void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... arg0) {


//            getroutes();
            getevent();
            getservice();
            getpaytype();
            getpickupRemarks();

            //   GetOPENRRST();

            //getcouriers();
            //getdeliverydetail();

            return "";
        }


        //function to get paytype
        private void getpaytype() {

            paytypeResponse = WebService.GET_PAYTYPE();
            if (paytypeResponse != null) {
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
                    //      sqldb.close();

                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    db.close();
                    //    sqldb.close;
                }
            }
        }

        //function to get services
        private void getservice() {
            try {
                serviceResponse = WebService.GET_SERVICE();
                if (serviceResponse == null) {

                    Toast.makeText(LoginActivity.this, R.string.service_empty_hint,
                            Toast.LENGTH_LONG).show();
                    return;
                }
                db = new DatabaseHandler(getBaseContext());
                for (Service serObv : serviceResponse) {
                    System.out.println("Get service is working" + serObv.SERVICEID);
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
                //      sqldb.close();
                db.close();

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                db.close();
            }
        }

        //function to get event
        private void getevent() {

            try {

                eventResponse = WebService.GET_EVENTS();
                if (eventResponse != null) {
                    db = new DatabaseHandler(getBaseContext());
                    //  sqldb = db.getWritableDatabase();

                    for (Events evOb : eventResponse) {

                        sqldb = db.getReadableDatabase();
                        String eventCode = evOb.EVENTCODE;
                        String eventDesc = evOb.EVENTDESC;
                        eventCode = eventCode.replaceAll("'","");
                        eventDesc = eventDesc.replaceAll("'","");
                        Cursor courier = sqldb.rawQuery("SELECT * FROM eventdata WHERE EVENTCODE='" + eventCode + "'", null);
                        sqldb = db.getWritableDatabase();
                        if (courier.getCount() > 0) {
                            sqldb.execSQL("UPDATE eventdata SET EVENTDESC='" + eventDesc + "' WHERE EVENTCODE='" + eventCode + "'");
                        } else {

                            String sql = "INSERT  INTO eventdata (EVENTCODE, EVENTDESC) "

                                    + "VALUES ('" + eventCode + "', '"
                                    + eventDesc + "')";
                            sqldb.execSQL(sql);
                        }
                        courier.close();


                    }
                    db.close();
                    //    sqldb.close();


                }
            } catch (Exception e) {
                Log.e("Get-event:", "Get Event in login activity is errored");
                //Toast.makeText(getApplicationContext(),e.getMessage().toString(), Toast.LENGTH_LONG).show();
                e.printStackTrace();
            } finally {
                if (db != null)
                    db.close();
            }
        }

        //function to get pickupremarks
        private void getpickupRemarks() {

            try {

                pickupremarksResponse = WebService.GET_PICKUP_REMARK(Username);

                if (pickupremarksResponse == null) {

                    Toast.makeText(LoginActivity.this, "Please Try again!",
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
                //  db.close();


            } catch (Exception e) {
                Log.e("Get-remarks:", "Get remarkpickup in login activity is errored");
                //Toast.makeText(getApplicationContext(),e.getMessage().toString(), Toast.LENGTH_LONG).show();
                e.printStackTrace();
            } finally {
                db.close();
            }
        }

        @Override
        public void onPostExecute(String res) {


            //  Toast.makeText(getApplicationContext(), "Sync done Successfully", Toast.LENGTH_SHORT).show();
        }
    }

    /*public class routetask extends AsyncTask<Void, Void, String> {

        String response = "";

        public void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... arg0) {


            //   getroutes();

            return "";
        }
    }*/


    /*public class deliverytask extends AsyncTask<Void, Void, String> {

        String response = "";

        public void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... arg0) {


            getdeliverydetail();

            return "";
        }


        //function to get the detail of delivery table from the server

    }*/


    /*public class deliveryholdtask extends AsyncTask<Void, Void, String> {

        String response = "";

        public void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... arg0) {


            getdeliveryholddetail();

            return "";
        }


        //get data from hold delivery table




    }*/

    /*public class deliverytranferaccepttask extends AsyncTask<Void, Void, String> {

        String response = "";

        public void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... arg0) {


            gettranswaybillacceptdt();

            return "";
        }


        //to get the scanned transfer accept waybill before accepting

    }*/


    /*public class pickuptask extends AsyncTask<Void, Void, String> {

        String response = "";

        public void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... arg0) {


            getpickupddeatils();

            return "";
        }



    }*/


    /*public class holdtask extends AsyncTask<Void, Void, String> {

        String response = "";

        public void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... arg0) {


            getholdwaybilldetail();

            return "";
        }


        //function to get the detail of delivery table from the server




        //function to get routes

    }*/

  /*  public void getroutes() {
        try {
            routesresponse = WebService.GET_ROUTES(Username);
            for (Routes routesOb : routesresponse) {
                sqldb = db.getReadableDatabase();
                Cursor rou = sqldb.rawQuery("SELECT * FROM routedata WHERE ROUTECODE='" + routesOb.RouteCode + "'", null);
                sqldb = db.getWritableDatabase();
                if (rou.getCount() > 0) {
                    sqldb.execSQL("UPDATE routedata SET ROUTENAME='" + routesOb.RouteName + "' WHERE ROUTECODE='" + routesOb.RouteCode + "'");
                } else {

                    String sql = "INSERT or replace INTO routedata (ROUTECODE, ROUTENAME) "

                            + "VALUES (" + routesOb.RouteCode + ", '"
                            + routesOb.RouteName + "')";
                    sqldb.execSQL(sql);
                    //sqldb.insertOrThrow("routedata", null, values);
                }
                rou.close();
            }
            db.close();
        } catch (Exception e) {
            Log.e("GetRoutes:", "Get Routes in login activity is errored");
            e.printStackTrace();
        }

    }*/

    private class AsyncCallWS extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... params) {
            //Call Web Method
/*
			PackageManager manager = getPackageManager();
			PackageInfo info = manager.getPackageInfo(getPackageName(),0);
			String version = info.versionName;

			String versionName = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;*/
            //     loginStatus=SoapService.invokeLoginWS(Username,Password,serialid,AppVersion);

            //		loginStatus = WebService.invokeLoginWS(Username,Password,serialid,AppVersion,METHOD_NAME29);


            System.out.println("loginstatus sharedprefrences  is " + loginPreferences.getString("username", Username));
            if (!loginPreferences.getString("username", Username).contentEquals(Username)) {
                System.out.println("loginstatus sharedprefrences  is not matching" + Username);
                clearcashData(LoginActivity.this);
            }

            loginStatus = WebService.invokeLoginWS(Username, Password, serialid, AppVersion);


            System.out.println("loginstatus  is " + loginStatus);
            System.out.println("Username in webserv is");
            System.out.println(Username);
            System.out.println("Password in webserv is");
            System.out.println(Password);
            System.out.println("serialid in webserv is");
            System.out.println(serialid);
            System.out.println("Version in webserv is" + AppVersion);
            /*System.out.println("V1.15");
            System.out.println("METHOD_NAME29 in webserv is");*/
            //  System.out.println(METHOD_NAME29);


            return null;

        }

        @Override
        //Once WebService returns response
        protected void onPostExecute(Void result) {
            //Make Progress Bar invisible
            webservicePG.setVisibility(View.INVISIBLE);

            //save the username in sharedpreference
            Editor editor = pref.edit();
            String usrnam1 = Username;

            editor.putString("uname", usrnam1);

            editor.commit();
            //      editor.apply();

            //Error status is false
            if (!errored) {

                //Based on Boolean value returned from WebService
                if (loginStatus) {

                    loginPrefsEditor.putString("username", Username);
                    loginPrefsEditor.commit();

                    Intent intent = new Intent(getBaseContext(), PostaNotificationService.class);
                    startService(intent);

                    //Log.e("LoginActivity","CourierTask Called");
                    LoginActivitySYNCH task11 = new LoginActivitySYNCH();
                    task11.execute();

                    //Log.e("LoginActivity","RouteTask Called");
                    /*routetask task12 = new routetask();
                    task12.execute();*/

                    /*deliverytask task1 = new deliverytask();
                    task1.execute();

                    holdtask task2 = new holdtask();
                    task2.execute();

                    pickuptask task3 = new pickuptask();
                    task3.execute();
                    //call webservice to check if route present or not

                    deliveryholdtask task4 = new deliveryholdtask();
                    task4.execute();

                    deliverytranferaccepttask task5 = new deliverytranferaccepttask();
                    task5.execute();*/

                    //  route = WebService.checkroute(Username, METHOD_NAME4);
                    route = WebService.GET_COURIERROUTE(Username);

                    if (!route.equals("No Route")) {
                        Log.e("Entered route", route);
                        //get route name from local database
                        db = new DatabaseHandler(getBaseContext());
                        //open localdatabase in a read mode
                        sqldb = db.getReadableDatabase();

                        int rout1 = Integer.parseInt(route);

                        //select all values in the table and check count
                        Cursor c1 = sqldb.rawQuery("SELECT * FROM routedata WHERE ROUTECODE=" + rout1, null);
                        if (c1.getCount() > 0) {
                            c1.moveToFirst();
                            rname = c1.getString(c1.getColumnIndex("ROUTENAME"));

                        }
                        c1.close();
                        db.close();

                        db = new DatabaseHandler(getBaseContext());
                        //open localdatabase in a read mode
                        sqldb = db.getReadableDatabase();

                        //select all values in the table and check count
                        Cursor c = sqldb.rawQuery("SELECT * FROM logindata WHERE Username='" + Username + "'", null);
                        c.moveToFirst();

                        Integer count = c.getCount();
                        //if count==0 insert the values else update the data
                        c.close();
                        if (count > 0) {
                            sqldb = db.getWritableDatabase();
                            sqldb.execSQL("UPDATE logindata SET Loginstatus=1, Routecode=" + route + " WHERE Username='" + Username + "'");

							/*//count the total number of entries
							sqldb.execSQL("UPDATE logindata SET Loginstatus=1 WHERE Username='"+Username+"'" );*/
                        } else {
                            sqldb = db.getWritableDatabase();
                            ContentValues values = new ContentValues();
                            values.put("Username", Username);

                            values.put("Loginstatus", loginStatus);
                            values.put("Routecode", route);
                            sqldb.insertOrThrow("logindata", null, values);
                        }
                        //   c.close();
                        db.close();

                        Intent intObj = new Intent(LoginActivity.this, HomeActivity.class);
                        intObj.putExtra("route1", String.valueOf(rname));
                        intObj.putExtra("route", String.valueOf(route));
                        actNoty.setRouteName(LoginActivity.this, rname);
                        actNoty.setRouteCode(LoginActivity.this, route);
                        //actNoty.setdrivercode(LoginActivity.this,Username);
                        //Navigate to Home Screen
                        OneSignal.sendTag("username", Username);
                        startActivity(intObj);
                        return;
                    } else {
                        db = new DatabaseHandler(getBaseContext());
                        //open localdatabase in a read mode
                        sqldb = db.getReadableDatabase();

                        //select all values in the table and check count
                        Cursor c = sqldb.rawQuery("SELECT * FROM logindata WHERE Username='" + Username + "'", null);
                        c.moveToFirst();

                        Integer count = c.getCount();
                        //if count==0 insert the values else update the data
                        c.close();
                        route = null;
                        if (count > 0) {
                            sqldb = db.getWritableDatabase();
                            sqldb.execSQL("UPDATE logindata SET Loginstatus=1, Routecode=" + null + " WHERE Username='" + Username + "'");

							/*//count the total number of entries
							sqldb.execSQL("UPDATE logindata SET Loginstatus=1 WHERE Username='"+Username+"'" );*/
                        } else {
                            sqldb = db.getWritableDatabase();
                            ContentValues values = new ContentValues();
                            values.put("Username", Username);

                            values.put("Loginstatus", loginStatus);
                            values.put("Routecode", route);
                            sqldb.insertOrThrow("logindata", null, values);
                        }

                        db.close();
                        //  sqldb.close();
                        Intent intObj = new Intent(LoginActivity.this, RouteActivity.class);
                        //Navigate to Route Activity
                        startActivity(intObj);
                        return;
                    }
                } else {

                    System.out.println("loginstatus after  is " + loginStatus);
                    Toast.makeText(getBaseContext(), "Please check Username/ Password",
                            Toast.LENGTH_LONG).show();

                    return;
                }
                //Error status is true
            } else {
                Toast.makeText(getBaseContext(), "Error occured in invoking webservice",
                        Toast.LENGTH_LONG).show();
                return;
            }
/*			//Re-initialize Error Status to False
			errored = false;


			db=new DatabaseHandler(getBaseContext());
			//open localdatabase in a read mode
			sqldb = db.getReadableDatabase();

			//select all values in the table and check count
			Cursor c = sqldb.rawQuery("SELECT * FROM logindata WHERE Username='"+Username+"'", null);
			c.moveToFirst();

			Integer count =  c.getCount();
			//if count==0 insert the values else update the data

			if(count>0){
				if(route.contains("No Route"))
				{
					sqldb.execSQL("UPDATE logindata SET Routecode="+null+" WHERE Username='"+Username+"'");
				}
				else
				{
					sqldb.execSQL("UPDATE logindata SET Routecode="+route+" WHERE Username='"+Username+"'" );
				}
				//count the total number of entries
				sqldb.execSQL("UPDATE logindata SET Loginstatus=1 WHERE Username='"+Username+"'" );
			}


			else {
				sqldb =db.getWritableDatabase();
				ContentValues values = new ContentValues();
				values.put("Username", Username);

				values.put("Loginstatus",loginStatus);
				values.put("Routecode",route);
				sqldb.insertOrThrow("logindata", null, values);
			}
			db.close();*/
        }

        @Override
        //Make Progress Bar visible
        protected void onPreExecute() {
            webservicePG.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }
    }

    public class LoginActivitySYNCH extends AsyncTask<Void, Void, String> {

        String response = "";

        public void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... arg0) {


            getcouriers();

            CommonMethods.getroutes(getBaseContext(), Username);

            getdeliverydetail();

            getdeliveryholddetail();

            gettranswaybillacceptdt();

            getpickupddeatils();

            getholdwaybilldetail();


            //  GetOPENRRST();

            return "";
        }

        private void getcouriers() {
            db = new DatabaseHandler(getBaseContext());
            try {

                courierResponse = WebService.GET_COURIERS(Username);
                if (courierResponse == null) {

                    Toast.makeText(LoginActivity.this, "Please Try again!",
                            Toast.LENGTH_LONG).show();
                    return;
                }
                //  db = new DatabaseHandler(getBaseContext());
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
                    sqldb.close();
                }
                //  db.close();

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                db.close();
                //  sqldb.close();

            }


        }



        private void getdeliverydetail() {
            // TODO Auto-generated method stub
            try {
                rstdetailResponse = WebService.GET_RSTDETAIL(Username);
                db = new DatabaseHandler(getBaseContext());
                //open localdatabase in a read mode
                sqldb = db.getWritableDatabase();
                //select all values in the table and check count
                sqldb.execSQL("DELETE FROM deliverydata WHERE Drivercode <> '" + Username + "'");
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
                        System.out.println("AWB Count : " + c1.getCount());
                        System.out.println("AWB Identity : " + rstdtevOb.AWBIdentifier);
                        if (count1 > 0) {

                            //System.out.println(c1.getString(c1.getColumnIndex("Waybill")));

                            //c1.moveToNext();
                            sqldb = db.getWritableDatabase();
                            String UpdateFields = "";
                            if (rstdtevOb.Last_Status.equals("WC") || rstdtevOb.Last_Status.equals("ACKW") || rstdtevOb.Last_Status.equals("RTW"))
                                UpdateFields = ",StopDelivery=0,WC_Status='A',WC_Transfer_Status=0,TransferStatus=0,Attempt_Status=0";
                            else if (rstdtevOb.Last_Status.equals("DELIVERED") || rstdtevOb.Last_Status.equals("ACKC") || rstdtevOb.Last_Status.equals("RTC") || rstdtevOb.Last_Status.equals("SD")) {
                                System.out.println("wc status while delivered");
                                if (rstdtevOb.Last_Status.equals("SD"))
                                    UpdateFields = ",StopDelivery=1,WC_Status='SD',WC_Transfer_Status=1,TransferStatus=0,Attempt_Status=1, ApprovalStatus='APPROVED'";
                                else
                                    UpdateFields = ",StopDelivery=1,WC_Status='C',WC_Transfer_Status=1,TransferStatus=0,Attempt_Status=1, ApprovalStatus=''";
                            } else
                                UpdateFields = ",StopDelivery=0,WC_Status='C',WC_Transfer_Status=1,TransferStatus=0,Attempt_Status=1";
                            System.out.println("wc status while notdelivered");
                            sqldb.execSQL("UPDATE deliverydata SET Drivercode='" + Username + "',Routecode=" + rstdtevOb.RouteName
                                    + ",Consignee='" + rstdtevOb.ConsignName + "',Telephone='" + rstdtevOb.PhoneNo + "'," + "Area='" + rstdtevOb.Area + "'," + "Company='"
                                    + rstdtevOb.Company + "',CivilID='" + rstdtevOb.CivilId + "',Serial='" + rstdtevOb.Serial + "',CardType='" + rstdtevOb.CardType + "',DeliveryDate='" + rstdtevOb.DelDate + "',DeliveryTime='"
                                    + rstdtevOb.DelTime + "',Amount='" + rstdtevOb.Amount + "',Address='" + rstdtevOb.Address + "',ShipperName='" + rstdtevOb.ShipperName + "',AWBIdentifier='" + rstdtevOb.AWBIdentifier + "',Last_Status='" + rstdtevOb.Last_Status + "' " + UpdateFields + "  WHERE Waybill='" + rstdtevOb.WayBill + "'");
                            System.out.println("Updated AWB : " + rstdtevOb.WayBill);


                        } else {
                            c1.moveToLast();

                            sqldb = db.getWritableDatabase();
                            ContentValues values = new ContentValues();
                            values.put("Drivercode", Username);
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
                                values.put("WC_Status", "C");
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

        public void getdeliveryholddetail() {
            // TODO Auto-generated method stub
            try {
                scanwbildtResponse = WebService.GET_SCAN_WAYBILL_DT(Username);
                db = new DatabaseHandler(getBaseContext());
                //open localdatabase in a read mode
                sqldb = db.getWritableDatabase();
                //select all values in the table and check count
                sqldb.execSQL("DELETE FROM deliverydata WHERE Drivercode <> '" + Username + "'");
                sqldb = db.getReadableDatabase();
                for (ScanWaybillDt scwbldtOb : scanwbildtResponse) {
                    if (scwbldtOb.ErrMsg == null || scwbldtOb.ErrMsg == "") {
                        Cursor c1 = sqldb.rawQuery("SELECT * FROM deliverydata WHERE Waybill='" + scwbldtOb.WayBill + "'", null);
                        int count1 = c1.getCount();
                        if (count1 > 0) {
                            sqldb = db.getWritableDatabase();
                            sqldb.execSQL("UPDATE deliverydata SET Drivercode='" + Username + "',Routecode=" + scwbldtOb.RouteName
                                    + ",Consignee='" + scwbldtOb.ConsignName + "',Telephone='" + scwbldtOb.PhoneNo + "'," + "Area='" + scwbldtOb.Area + "'," + "Company='"
                                    + scwbldtOb.Company + "',CivilID='" + scwbldtOb.CivilId + "',Serial='" + scwbldtOb.Serial + "',CardType='" + scwbldtOb.CardType + "',DeliveryDate='" + scwbldtOb.DelDate + "',DeliveryTime='"
                                    + scwbldtOb.DelTime + "',Amount='" + scwbldtOb.Amount + "',StopDelivery=0,WC_Status='P',WC_Transfer_Status=0,TransferStatus=0,Attempt_Status='" + scwbldtOb.Attempt + "',Address='" + scwbldtOb.Address + "' WHERE Waybill='" + scwbldtOb.WayBill + "'");
                        } else {
                            c1.moveToLast();

                            sqldb = db.getWritableDatabase();
                            ContentValues values = new ContentValues();
                            values.put("Drivercode", Username);
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
                Log.e("Get ScanWBll detail:", "Get Scan Waybill in login activity is errored");

                e.printStackTrace();
            } finally {
                db.close();
                //       sqldb.close();
            }


        }

        private void gettranswaybillacceptdt() {
            // TODO Auto-generated method stub
            return;

            /*System.out.println("gettranswaybillacceptdt");

            SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME43);
            Request.addProperty("DRIVERCODE2", Username);

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
            envelope.addMapping(NAMESPACE, METHOD_NAME43, new Waybill_check().getClass());
            HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
            //	androidHttpTransport.debug = true;

			*//* Call the web service and retrieve result*//*


            try {
                ServiceConnection WebCon = androidHttpTransport.getServiceConnection();

                androidHttpTransport.call(SOAP_ACTION + METHOD_NAME43, envelope);
                SoapObject response = (SoapObject) envelope.getResponse();
                Log.i("myApp", Request.toString());
                //System.out.println("check dddd" + response);
                //envelope for passing the data
                envelope.addMapping(NAMESPACE, METHOD_NAME43, new Waybill_check().getClass());
                androidHttpTransport.call(SOAP_ACTION + METHOD_NAME43, envelope);

                Waybill_check[] waybills = new Waybill_check[response.getPropertyCount()];
                count = response.getPropertyCount();
                waybill1 = new String[count];
                rname1 = new String[count];
                cname1 = new String[count];
                phone = new String[count];
                area1 = new String[count];
                company1 = new String[count];
                civilid1 = new String[count];
                serial1 = new String[count];
                cardtype1 = new String[count];
                deldate1 = new String[count];
                deltime1 = new String[count];
                amount1 = new String[count];
                attempt1 = new String[count];
                error1 = new String[count];
                address1 = new String[count];

                db = new DatabaseHandler(getBaseContext());
                //open localdatabase in a read mode
                sqldb = db.getReadableDatabase();
                //select all values in the table and check count
                sqldb.execSQL("DELETE FROM TransferAcceptTemp WHERE Drivercode <> '" + Username + "'");
                if (response.toString().contains("ConsignName")) {
                    for (int i = 0; i < response.getPropertyCount(); i++) {

                        SoapObject pii = (SoapObject) response.getProperty(i);

                        wb.wbill = pii.getProperty(0).toString();

                        wb.routename = pii.getProperty(1).toString();
                        wb.cname = pii.getProperty(2).toString();
                        wb.phno = pii.getProperty(3).toString();
                        wb.area = pii.getProperty(4).toString();
                        wb.company = pii.getProperty(5).toString();
                        wb.civilid = pii.getProperty(6).toString();
                        wb.serial = pii.getProperty(7).toString();
                        wb.cardtype = pii.getProperty(8).toString();
                        wb.del_date = pii.getProperty(9).toString();
                        wb.del_time = pii.getProperty(10).toString();
                        wb.amount = pii.getProperty(11).toString();
                        wb.error = pii.getProperty(12).toString();
                        wb.attempt = pii.getProperty(13).toString();
                        wb.address = pii.getProperty(14).toString();
                        waybills[i] = wb;

                        waybill1[i] = waybills[i].wbill;
                        rname1[i] = waybills[i].routename;
                        cname1[i] = waybills[i].cname;
                        phone[i] = waybills[i].phno;
                        area1[i] = waybills[i].area;
                        company1[i] = waybills[i].company;
                        civilid1[i] = waybills[i].civilid;
                        serial1[i] = waybills[i].serial;
                        cardtype1[i] = waybills[i].cardtype;
                        deldate1[i] = waybills[i].del_date;
                        deltime1[i] = waybills[i].del_time;
                        amount1[i] = waybills[i].amount;
                        error1[i] = waybills[i].error;
                        attempt1[i] = waybills[i].attempt;
                        address1[i] = waybills[i].address;


                        Cursor c1 = sqldb.rawQuery("SELECT * FROM TransferAcceptTemp WHERE Waybill='" + waybill1[i] + "'", null);
                        int count1 = c1.getCount();

                        if (count1 > 0) {

                            //System.out.println(c1.getString(c1.getColumnIndex("Waybill")));

                            //c1.moveToNext();

                            sqldb = db.getWritableDatabase();

                            sqldb.execSQL("UPDATE TransferAcceptTemp SET Drivercode='" + Username + "',Routecode=" + rname1[i]
                                    + ",Consignee='" + cname1[i] + "',Telephone='" + phone[i] + "'," + "Area='" + area1[i] + "'," + "Company='"
                                    + company1[i] + "',CivilID='" + civilid1[i] + "',Serial='" + serial1[i] + "',CardType='" + cardtype1[i] + "',DeliveryDate='" + deldate1[i] + "',DeliveryTime='"
                                    + deltime1[i] + "',Amount='" + amount1[i] + "',Transfer_Status='P',Address='" + address1[i] + "',Attempt_Status='" + attempt1[i] + "' WHERE Waybill='" + waybill1[i] + "'");


                        } else {
                            c1.moveToLast();

                            sqldb = db.getWritableDatabase();
                            ContentValues values = new ContentValues();
                            values.put("Drivercode", Username);
                            values.put("Routecode", rname1[i]);
                            values.put("Waybill", waybill1[i]);
                            values.put("Consignee", cname1[i]);
                            values.put("Telephone", phone[i]);
                            values.put("Area", area1[i]);
                            values.put("Company", company1[i]);
                            values.put("CivilID", civilid1[i]);
                            values.put("Serial", serial1[i]);
                            values.put("CardType", cardtype1[i]);
                            values.put("DeliveryDate", deldate1[i]);
                            values.put("DeliveryTime", deltime1[i]);
                            values.put("Amount", amount1[i]);
                            values.put("Transfer_Status", "P");
                            values.put("Address", address1[i]);
                            values.put("Attempt_Status", attempt1[i]);

                            sqldb.insertOrThrow("TransferAcceptTemp", null, values);

                        }
                        c1.close();


                    }
                }
                androidHttpTransport.reset();
                db.close();
                WebCon.disconnect();
                //androidHttpTransport.getConnection().disconnect();
            } catch (Exception e) {
                //Log.e("Home:delivery:", e.getMessage().toString());
                e.printStackTrace();
            }*/
        }

        private void getpickupddeatils() {
            // TODO Auto-generated method stub

            //function to get the pickupdetails
            db = new DatabaseHandler(getBaseContext());
            try {
                pickupdtResponse = WebService.GET_PICKUP_DT(Username);
                //     db = new DatabaseHandler(getBaseContext());
                //open localdatabase in a read mode
                sqldb = db.getWritableDatabase();
                sqldb.execSQL("DELETE FROM pickuphead WHERE Drivercode <> '" + Username + "'");
                sqldb = db.getReadableDatabase();
                for (PickUpDt pkdtOb : pickupdtResponse) {

                    Log.e("PICKUPDT", "DBVAL : " + db);
                    sqldb = db.getReadableDatabase();

                    if (pkdtOb.ERR != null && (pkdtOb.ERR).contains("PENDING")) {
                        //System.out.println("PENDING\n------------");


                        //select all values in the table and check count
                        Cursor cp = sqldb.rawQuery("SELECT * FROM pickuphead WHERE Pickup_No='" + pkdtOb.PICK_NO + "'", null);
                        int count1 = cp.getCount();
                        //  c1.moveToFirst();
                        if (count1 > 0) {

                            System.out.println("update");

                            sqldb = db.getWritableDatabase();

                            sqldb.execSQL("UPDATE pickuphead SET Drivercode='" + Username + "', Account_Name='" + pkdtOb.ACC_NAME
                                    + "',Pick_Address='" + pkdtOb.PICK_ADD + "',Pickup_Area='" + pkdtOb.PICK_AREA + "',Contact_Person='" + pkdtOb.CONTACT_PERSON + "'," + "Pickup_Phone='" + pkdtOb.PICK_PHONE + "'," + "Pickup_Time='"
                                    + pkdtOb.PICK_TIME + "',Status='A',TransferStatus=0,Pickup_Type='" + pkdtOb.IDENTIFIER + "',ConsigneeName='" + pkdtOb.CONSIGNEE_NAME + "',DeliveryAddress='" + pkdtOb.DEL_ADD + "',DeliveryCity='" + pkdtOb.DEL_CITY + "',DeliveryPhone='" + pkdtOb.DEL_PHONE + "' WHERE Pickup_No='" + pkdtOb.PICK_NO + "'");


                        } else {
                            cp.moveToLast();

                            sqldb = db.getWritableDatabase();
                            ContentValues values = new ContentValues();
                            values.put("Drivercode", Username);
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
                        cp.close();
                    }

                    //if err message completed call GET_PICKUP_WAYBILLS_DT
                    else if (pkdtOb.ERR != null && (pkdtOb.ERR).contains("COMPLETED")) {
                        //System.out.println("ERR=COMPLETED\n______________");
                        //if data is not in the table insert/update
                        Cursor c11 = sqldb.rawQuery("SELECT * FROM pickuphead WHERE Pickup_No='" + pkdtOb.PICK_NO + "'", null);
                        int count1 = c11.getCount();

                        if (count1 > 0) {

                            System.out.println("update");

                            sqldb = db.getWritableDatabase();


                            sqldb.execSQL("UPDATE pickuphead SET Drivercode='" + Username + "', Account_Name='" + pkdtOb.ACC_NAME
                                    + "',Pick_Address='" + pkdtOb.PICK_ADD + "',Pickup_Area='" + pkdtOb.PICK_AREA + "',Contact_Person='" + pkdtOb.CONTACT_PERSON + "'," + "Pickup_Phone='" + pkdtOb.PICK_PHONE + "'," + "Pickup_Time='"
                                    + pkdtOb.PICK_TIME + "',Status='C',TransferStatus=2 WHERE Pickup_No='" + pkdtOb.PICK_NO + "'");


                        } else {
                            c11.moveToLast();

                            sqldb = db.getWritableDatabase();
                            ContentValues values = new ContentValues();
                            values.put("Drivercode", Username);
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
                    else if (pkdtOb.ERR != null && (pkdtOb.ERR).contains("FINISHED")) {

                        //System.out.println("ERR=FINISHED\n______________");
                        sqldb = db.getWritableDatabase();

                        sqldb.execSQL("DELETE FROM pickuphead WHERE Pickup_No='" + pkdtOb.PICK_NO + "'");
                        sqldb.execSQL("DELETE FROM pickupdetails WHERE Pickup_No='" + pkdtOb.PICK_NO + "'");

                    }
                }
                //  db.close();
            } catch (Exception e) {
                Log.e("Get Pickup detail:", "Get PickupDetail in login activity is errored");
                e.printStackTrace();
            } finally {
                //        sqldb.close();
                db.close();
                //      sqldb.close();
               /* if (sqldb != null && sqldb.isOpen()) {
                    sqldb.close();
                   // db.close();
                }*/
            }

        }

        //get the pickup wbill details
        private void getpickupwbilldetails(String pickno) {
            // TODO Auto-generated method stub
            db = new DatabaseHandler(getBaseContext());
            try {
                pickupwblldtResponse = WebService.GET_PICKUP_WAYBILLS_DT(Username, pickno);
                if (pickupwblldtResponse != null) {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MMM-dd HH:mm:ss");
                    String date_time = sdf.format(new Date());
                    for (PickUpWaybillsDT pkpwbldtObv : pickupwblldtResponse) {

                        sqldb = db.getReadableDatabase();
                        //select all values in the table and check count
                        Cursor c1 = sqldb.rawQuery("SELECT * FROM pickupdetails WHERE Waybill_Number='" + pkpwbldtObv.WAYBILL + "'", null);
                        int count1 = c1.getCount();

                        if (count1 > 0) {

                            System.out.println("update");

                            sqldb = db.getWritableDatabase();


                            sqldb.execSQL("UPDATE pickupdetails SET Driver_Code='" + Username + "', Pickup_No='" + pkpwbldtObv.PICKUPNO
                                    + "',Pickup_No='" + pkpwbldtObv.PICKUPNO + "',PayType='" + pkpwbldtObv.PAYTYPE + "',Amount='" + pkpwbldtObv.AMOUNT + "'," + "ServiceType='" + pkpwbldtObv.SERVICE + "'," + "Date_Time='"
                                    + date_time + "' WHERE Waybill_Number='" + pkpwbldtObv.WAYBILL + "'");


                        } else {
                            c1.moveToLast();

                            sqldb = db.getWritableDatabase();

                            ContentValues values = new ContentValues();
                            values.put("Driver_Code", Username);
                            values.put("Pickup_No", pkpwbldtObv.PICKUPNO);
                            values.put("Waybill_Number", pkpwbldtObv.WAYBILL);
                            values.put("PayType", pkpwbldtObv.PAYTYPE);
                            values.put("Amount", pkpwbldtObv.AMOUNT);
                            values.put("ServiceType", pkpwbldtObv.SERVICE);
                            values.put("Date_Time", date_time);


                            sqldb.insertOrThrow("pickupdetails", null, values);

                        }
                        sqldb = db.getWritableDatabase();
                        sqldb.execSQL("UPDATE pickuphead SET Status='C' WHERE Pickup_No='" + pickno + "'");
                        sqldb.execSQL("UPDATE pickuphead SET TransferStatus=2 WHERE Pickup_No='" + pickno + "'");
                        c1.close();


                    }
                    db.close();
                }
            } catch (Exception e) {
                Log.e("Get Pickup Wbll detail:", "Get Pickup Waybil detail in login activity is errored");
                e.printStackTrace();
            } finally {
                db.close();
                //     sqldb.close();
            }

        }

        private void getholdwaybilldetail() {
            try {
                holddwaybillResponse = WebService.GET_HOLDWAYBILLS(Username);
                if (holddwaybillResponse == null) {
                    return;
                } else
                    db = new DatabaseHandler(getBaseContext());
                sqldb = db.getWritableDatabase();
                sqldb.execSQL("DELETE FROM Holdwaybilldata WHERE Drivercode <> '" + Username + "'");
                sqldb = db.getReadableDatabase();
                for (HoldWayBills hldwbllOb : holddwaybillResponse) {

                    //select all values in the table and check count
                    Cursor c1 = sqldb.rawQuery("SELECT * FROM Holdwaybilldata WHERE Waybill='" + hldwbllOb.Waybill + "'", null);
                    int count1 = c1.getCount();

                    if (count1 > 0) {

                        //System.out.println(c1.getString(c1.getColumnIndex("Waybill")));

                        //c1.moveToNext();

                        sqldb = db.getWritableDatabase();

                        sqldb.execSQL("UPDATE Holdwaybilldata SET Drivercode='" + Username + "',Routecode='" + route + "',Transdriver_Code='" + hldwbllOb.TrnsDrvr
                                + "',Consignee='" + hldwbllOb.Consignee + "',Telephone='" + hldwbllOb.Phno + "'," + "Area='" + hldwbllOb.Area + "'," + "Company='"
                                + hldwbllOb.Company + "',CivilID='" + hldwbllOb.CivilId + "',Serial='" + hldwbllOb.Serial + "',CardType='" + hldwbllOb.CardType + "',DeliveryDate='" + hldwbllOb.DelDate + "',DeliveryTime='"
                                + hldwbllOb.DelTime + "',Amount='" + hldwbllOb.Amount + "',Address='" + hldwbllOb.Address + "',HOLD_Transfer_Status=0,TransferStatus=0 WHERE Waybill='" + hldwbllOb.Waybill + "'");


                    } else {
                        c1.moveToLast();

                        sqldb = db.getWritableDatabase();
                        ContentValues values = new ContentValues();
                        values.put("Drivercode", Username);
                        values.put("Routecode", route);
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
                //     sqldb.close();
            } catch (Exception e) {
                Log.e("Get Hold Wbll detail:", "Get Hold Waybill in login activity is errored");
                e.printStackTrace();

            }


        }
    }


  /*  private void GetOPENRRST() {


        runsheet = WebService.GET_OPENRST(Username);
        //if no open runsheet go to delivery page else go to odometer page
        System.out.println("runshhett strn:"+runsheet.RSTNO);

        retrno= runsheet.RTNO;
        rstnumbr= runsheet.RSTNO;
        acknumbr= runsheet.ACKNO;



    }*/


}