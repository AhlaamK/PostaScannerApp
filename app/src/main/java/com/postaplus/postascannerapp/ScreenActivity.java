package com.postaplus.postascannerapp;


import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.StrictMode;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.appindexing.Action;
import com.google.firebase.appindexing.FirebaseAppIndex;
import com.google.firebase.appindexing.FirebaseUserActions;
import com.google.firebase.appindexing.Indexable;
import com.google.firebase.appindexing.builders.Actions;
import com.google.firebase.appindexing.builders.Indexables;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class ScreenActivity extends Activity {
    public static String ipaddress = null;
    //public static String url = "http://132.145.29.167:1000/OpsCourierScannerService/OpsGCScanSrv.svc";
    public static String url = "http://10.0.0.20:1000/Courier";
    private static int SPLASH_TIME_OUT = 2800;
    String uname = null;
    ImageView logoapp;
    boolean netstatus, servicestatus;
    //public static String ipaddress = null;

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_screen);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        logoapp = findViewById(R.id.imagelogo);


    }


    @Override
    public void onStart() {
        super.onStart();
        Animation rotate = AnimationUtils.loadAnimation(this, R.anim.zoomin);
        checkPermissions(rotate);


        Indexable noteToIndex = Indexables.noteDigitalDocumentBuilder()
                .setName("Screen Page")
                .setText(Uri.parse("android-app://com.postaplus.postascannerapp/http/host/path")
                        .toString())
                .setUrl(Uri.parse("http://host/path").toString())
                .build();

        FirebaseAppIndex.getInstance().update(noteToIndex);
        FirebaseUserActions.getInstance().start(getAction());
    }

    @Override
    public void onStop() {
        FirebaseUserActions.getInstance().end(getAction());
        super.onStop();


    }

    public Action getAction() {
        return Actions.newView("ScreenActivity", "android-app://com.postaplus.postascannerapp/http/host/path");
    }

    private void getUrl(Animation rotate) {
        File ipAddressfile = new File(Environment.getExternalStorageDirectory(), "Postaplus/Data");
        ipAddressfile.mkdirs();
        File ipaddfile = new File(ipAddressfile, "ipaddress.txt");
        //File gpxfile = new File(file, sFileName);

        if (!ipaddfile.exists()) {

            try {
                FileWriter writer = new FileWriter(ipaddfile);
                ipaddress = "http://132.145.29.167:1000/Courier";
                writer.append(ipaddress);
                writer.flush();
                writer.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }

            StringBuilder text = new StringBuilder();

            try {

                BufferedReader br = new BufferedReader(new FileReader(ipaddfile));
                String line;

                while ((line = br.readLine()) != null) {
                    text.append(line);
                }

                br.close();
                ipaddress = text.toString();
            } catch (IOException e) {
                e.printStackTrace();
            }
            url = ipaddress + "/";
        } else {

            StringBuilder text = new StringBuilder();

            try {

                BufferedReader br = new BufferedReader(new FileReader(ipaddfile));
                String line;

                while ((line = br.readLine()) != null) {

                    text.append(line);
                }
                br.close();

                ipaddress = text.toString();

            } catch (IOException e) {
                ScreenActivity.this.finish();
                e.printStackTrace();
            }
            url = ipaddress + "/";

            System.out.println("url  screen" + url);
        }
        logoapp.startAnimation(rotate);
        //mp.start();
        new Handler().postDelayed(new Runnable() {
            public void run() {
                netstatus = isNetworkConnected();
                if (netstatus) {
                    servicestatus = isConnected();
                    if (servicestatus) {


                        Intent localIntent = new Intent(ScreenActivity.this, LoginActivity.class);
                        ScreenActivity.this.startActivity(new Intent(localIntent));

                        ScreenActivity.this.finish();
                    } else {
                        ScreenActivity.this.finish();
                        Toast.makeText(getApplicationContext(), "Connection Error", Toast.LENGTH_LONG).show();
                    }
                } else {
                    ScreenActivity.this.finish();
                    Toast.makeText(getApplicationContext(), "Please check your Internet Connection", Toast.LENGTH_LONG).show();
                }
            }


            boolean isConnected() {
                try {

                    ConnectivityManager cm = (ConnectivityManager) getSystemService
                            (Context.CONNECTIVITY_SERVICE);
                    assert cm != null;
                    NetworkInfo netInfo = cm.getActiveNetworkInfo();

                    if (netInfo != null && netInfo.isConnected()) {
                        boolean Connect = webservice.WebService.GET_SERVICE_STATUS(null);
                        if (Connect) return true;
                        else {
                            Toast.makeText(getBaseContext(), "Webservice not available",
                                    Toast.LENGTH_LONG).show();

                            Log.e("WebSer/Err", "WebService not available");
                            return false;

                        }

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                return false;

            }

            private boolean isNetworkConnected() {
                ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                assert cm != null;
                NetworkInfo ni = cm.getActiveNetworkInfo();
                return ni != null;
            }
        }, SPLASH_TIME_OUT);


    }

    private void checkPermissions(Animation rotate) {

        Dexter.withActivity(this)
                .withPermissions(
                        Manifest.permission.BLUETOOTH,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.CALL_PHONE,
                        Manifest.permission.CLEAR_APP_CACHE,
                        Manifest.permission.READ_CONTACTS,
                        Manifest.permission.CAMERA
                ).withListener(new MultiplePermissionsListener() {
            @Override
            public void onPermissionsChecked(MultiplePermissionsReport report) {
                getUrl(rotate);
            }

            @Override
            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {

            }

        }).check();

    }


}
