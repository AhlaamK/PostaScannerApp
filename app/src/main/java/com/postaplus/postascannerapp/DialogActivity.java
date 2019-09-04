package com.postaplus.postascannerapp;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.honeywell.aidc.BarcodeFailureEvent;
import com.honeywell.aidc.BarcodeReadEvent;
import com.honeywell.aidc.BarcodeReader;
import com.honeywell.aidc.ScannerUnavailableException;
import com.honeywell.aidc.TriggerStateChangeEvent;

import org.apache.commons.lang3.StringUtils;

import koamtac.kdc.sdk.KDCData;
import koamtac.kdc.sdk.KDCReader;
import webservice.WebService;

import static com.postaplus.postascannerapp.HomeActivity.barcodeReader;
import static com.postaplus.postascannerapp.MasterActivity.db;
import static com.postaplus.postascannerapp.MasterActivity.sqldb;

/**
 * Created by ahlaam.kazi on 2/15/2018.
 */

public class DialogActivity extends Activity implements View.OnClickListener ,BarcodeReader.BarcodeListener,BarcodeReader.TriggerListener {

    Button PICKUPbtn, DELIVERYbtn,Confirmbtn;
    String drivercode,route,routen;
    Thread ThrKdc;
    //KDC Parameters
    //
    public static String WaybillFromScanner = "";
    public static String KDCScannerCallFrom = "";

    Resources _resources;
    BluetoothDevice _btDevice = null;
    static final byte[] TYPE_BT_OOB = "application/vnd.bluetooth.ep.oob".getBytes();
    Button _btnScan = null;

    //BluetoothDevice _btDevice;
    DialogActivity _activity;
    KDCData ScannerData;
    KDCReader _kdcReader;
    public String chkdata="";
    public String waybill;
    View rootView;
    public DialogActivity MYActivity;
    //KDCTask KDCTaskExecutable = new KDCTask();
    public boolean isActivityActiveFlag=false;
    TableRow tr;
    TableRow.LayoutParams lp;
    public int SCANNER_REQUEST_CODE = 123;
    String wbill,dcode;
    TableRow trdialog;
    TableLayout resulttabledialog;
    TextView waybilltxt;
    TextView AWBTXT;
    public static BluetoothDevice ScannerDevices = null;
    TextView textView1,textalert,textchoose,waybilldialog;
    String UserNotyTrackResp;
    int count1 = 0;
    String[]  wbillarr;
    String ApprvalStatus, Attemptstatus,wbilldata1;
    // SQLiteDatabase sqldb = null;
    // DatabaseHandler db;
    int[] stopdelarr;
    public String FlagDeliveryMode = "NRML";
    String laststats;
    Button btncancel;

    int flag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_dialog);

        barcodeReader.addBarcodeListener(DialogActivity.this);

        System.out.println("barcoderader in dialog:"+barcodeReader);
        if(barcodeReader!= null){
            try {
                barcodeReader.claim();
            } catch (ScannerUnavailableException e) {
                e.printStackTrace();
            }
        }

        PICKUPbtn = (Button) findViewById(R.id.pckupaction);
        DELIVERYbtn = (Button) findViewById(R.id.deliveryaction);
        // resulttabledialog=(TableLayout)findViewById(R.id.resulttabledialog);
        resulttabledialog = (TableLayout)findViewById(R.id.resulttable1);
        btncancel=(Button)findViewById(R.id.btncancel);
        AWBTXT =(TextView)findViewById(R.id.AWBTXT);
        textView1=(TextView)findViewById(R.id.textView1);
        textalert=(TextView)findViewById(R.id.textalert);
        textchoose=(TextView)findViewById(R.id.textchoose);
        resulttabledialog=(TableLayout)findViewById(R.id.resulttabledialog);

        Confirmbtn=(Button) findViewById(R.id.confirmaction);
        route= getIntent().getExtras().getString("routecode");
        routen= getIntent().getExtras().getString("routename");
        dcode= getIntent().getExtras().getString("dcode");
        PICKUPbtn.setOnClickListener(this);
        DELIVERYbtn.setOnClickListener(this);
        Confirmbtn.setOnClickListener(this);
        btncancel.setOnClickListener(this);
        // setFinishOnTouchOutside(false);
        //  KDCTaskExecutable.execute();

        this.setFinishOnTouchOutside(false);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.pckupaction:

                if (barcodeReader != null) {
                    barcodeReader.release();
                    barcodeReader.removeBarcodeListener(DialogActivity.this);

                }
                Intent int1 = new Intent(DialogActivity.this,PickupActivity.class);
                int1.putExtra("routecode",route);
                int1.putExtra("routename",routen);
                //startActivity(new Intent(int1));
                // new code
                startActivity(new Intent(int1));
                this.finish();

                break;

            case R.id.deliveryaction:


                DELIVERYbtn.setBackgroundColor(Color.parseColor("#f0f0f0"));
                textalert.setVisibility(View.VISIBLE);
                PICKUPbtn.setVisibility(View.GONE);
                DELIVERYbtn.setVisibility(View.GONE);
                textchoose.setVisibility(View.GONE);
                Confirmbtn.setVisibility(View.VISIBLE);
                btncancel.setVisibility(View.VISIBLE);


                // AWBTXT.setText(waybill);

                System.out.println("AWBTXT "+waybill);


                break;
            case R.id.confirmaction:

                if(resulttabledialog.getChildCount()==0){
                    Toast.makeText(getApplicationContext(), "Please Scan Awb you wish to deliver", Toast.LENGTH_SHORT)
                            .show();
                    return;
                }else{

                    if (barcodeReader != null) {
                        barcodeReader.release();
                        barcodeReader.removeBarcodeListener(DialogActivity.this);


                    }
                    Intent int2 = new Intent(DialogActivity.this,StartDeliveryActivity.class);
                    // delvryflag=true;
                    int2.putExtra("routecode",route);
                    int2.putExtra("routename",routen);

                    //startActivity(new Intent(int1));
                    // new code
                    startActivity(new Intent(int2));
                    DialogActivity.this.finish();

                }


                break;

            case R.id.btncancel:


              /*  if (!isActivityActiveFlag) {
                    Toast.makeText(getApplicationContext(), "Please wait for scanner to connect",
                            Toast.LENGTH_LONG).show();

                } else {
                    if (_kdcReader != null) _kdcReader.Disconnect();
                    if (ThrKdc != null) ThrKdc.interrupt();
                    KDCTaskExecutable.cancel(true);
                }*/
                if (barcodeReader != null) {
                    barcodeReader.release();
                    barcodeReader.removeBarcodeListener(DialogActivity.this);

                }
                Intent int2 = new Intent(DialogActivity.this,HomeActivity.class);
                int2.putExtra("routecode",route);
                int2.putExtra("routename",routen);

                //startActivity(new Intent(int1));
                // new code
                startActivity(new Intent(int2));
                DialogActivity.this.finish();

                break;
        }

    }


    @Override
    public void onPause(){
        super.onPause();

        if (barcodeReader != null) {
            barcodeReader.release();
            barcodeReader.removeBarcodeListener(DialogActivity.this);

        }
        Log.e("reader","1213");
        if(!isActivityActiveFlag) isActivityActiveFlag=false;
	/*	ThrKdc.interrupt();
		_kdcReader.Disconnect();*/
        //if(!tsd.isInterrupted()) tsd.interrupt();

    }
    @Override
    public void onResume()
    {
        super.onResume();
        barcodeReader.addBarcodeListener(DialogActivity.this);
        try {
            barcodeReader.claim();
        } catch (ScannerUnavailableException e) {
            e.printStackTrace();
        }


        System.out.println("Resume activate in dialog");
    }
  /*  @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        int action = event.getAction();
        int keyCode = event.getKeyCode();

        switch (keyCode) {
            case KeyEvent.KEYCODE_VOLUME_UP:

                if (action == KeyEvent.ACTION_DOWN)
                {
                    Intent intent = new Intent("com.google.zxing.client.android.SCAN");
                    intent.putExtra("SCAN_MODE", "SCAN_MODE");
                    startActivityForResult(intent, SCANNER_REQUEST_CODE);
                }

                return true;
            case KeyEvent.KEYCODE_VOLUME_DOWN:

                if (action == KeyEvent.ACTION_DOWN) {
                    Intent intent = new Intent("com.google.zxing.client.android.SCAN");
                    intent.putExtra("SCAN_MODE", "SCAN_MODE");
                    startActivityForResult(intent, SCANNER_REQUEST_CODE);
                }

                return true;
            default:
                return super.dispatchKeyEvent(event);
        }

    }
*/

    @Override
    public void onBarcodeEvent(final BarcodeReadEvent honeywelbar) {
        DialogActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                String barcodeData = honeywelbar.getBarcodeData();
                // update UI to reflect the data

                System.out.println("barcodeData dialo is:" + barcodeData);


                if (barcodeData != null) {

                    //ScannerData = barcodeData;
                    waybill = barcodeData;
                    // StartDeliveryActivity.WaybillFromScanner = ScannerData.GetData();
                    System.out.println("barcodeData waybill is:" + waybill);
                    if (Check_ValidWaybill(waybill) == true) {

                        System.out.println("diaalog ID : ");
                        // System.out.println(R.id.WC_Frame);
                        System.out.println(" value for honeywelbar is : ");
                        System.out.println(honeywelbar);

                        DialogActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if(waybill!=null){
                                    //String contents = wbill;


                                    wbill=waybill;

                                    System.out.println("Value wbill in dialog"+wbill);
                                    new UserNotifyTrack(wbill).execute();

                                }


                            }
                        });

                    } else {

                        _activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                Toast.makeText(_activity, "Invalid Waybill", Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                } else {
                    _activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            Toast.makeText(_activity, "Invalid Waybill", Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        });
    }

    public static boolean Check_ValidWaybill (String s){

        if (s.length() == 10 || s.length() == 12)
        {
            return StringUtils.isNumeric(s) == true;
        }
        else if (s.length() == 18)
        {
            return StringUtils.isAlphanumeric(s) == true;
        }
        return false;
    }



    public class UserNotifyTrack extends AsyncTask<Void, Void, String> {

        String Taskwabill;
        public UserNotifyTrack(String TakWaybill) {
            super();
            Taskwabill = TakWaybill;

        }
        //String response = "";
        public void onPreExecute() {

            // super.onPreExecute();

            trdialog = new TableRow(DialogActivity.this);

            if (Build.MODEL.contains("SM-N")) {


                lp = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT);
                lp.setMargins(18, 2, 95, 2);
                trdialog.setLayoutParams(lp);

            } else {
                lp = new TableRow.LayoutParams(200, TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT);

                trdialog.setLayoutParams(lp);
                lp.setMargins(0, 5, 70, 0);
            }
            System.out.println("trdialog async:" + trdialog);
        }

        @Override
        protected String doInBackground(Void... arg0) {

            return "";
        }



      /*  @Override
        public void onPostExecute(String res) {
            //response=null;
            System.out.println("UserNotyTrackResp on post:" + UserNotyTrackResp);

            try {
                System.out.println("UserNotyTrackResp notfytrck1:" + UserNotyTrackResp);


                db = new DatabaseHandler(getBaseContext());
                System.out.println("db dialog:" + db);
                //open localdatabase in a read mode
                //open localdatabase in a read mode
                sqldb = db.getReadableDatabase();
                // Cursor c2 = sqldb.rawQuery("SELECT * FROM deliverydata WHERE Waybill='" + wbill + "' AND AWBIdentifier= '" + FlagDeliveryMode + "' ", null);

                Cursor c2 = sqldb.rawQuery("SELECT * FROM deliverydata WHERE Waybill='" + wbill+ "' ", null);
                count1 = c2.getCount();
                //System.out.println("stage");
                c2.moveToFirst();
                wbillarr = new String[count1];
                //laststats=new String[count1];
                stopdelarr = new int[count1];

                System.out.println("wbillarr dialog delv:" + wbillarr.toString()+"c2.getCount()"+c2.getCount() );

                if (c2.getCount() > 0) {


                    for (int i = 0; i < count1; i++) {
                        //System.out.println("stage3");
                        wbillarr[i] = c2.getString(c2.getColumnIndex("Waybill"));

                        stopdelarr[i] = c2.getInt(c2.getColumnIndex("StopDelivery"));
                        laststats=c2.getString(c2.getColumnIndex("WC_Status"));
                        System.out.println("laststats[i]"+laststats);

                        ApprvalStatus = c2.getString(c2.getColumnIndex("ApprovalStatus"));
                        Attemptstatus = c2.getString(c2.getColumnIndex("Attempt_Status"));

                        System.out.println("approval status is:" + ApprvalStatus + "Stopdlv : " + stopdelarr[i] + " Attpmpt " + Attemptstatus+"laststats"+laststats);
                        if(laststats.equals("C")) laststats = "DELIVERED";
                        else if(laststats.equals("A")) laststats = "WC";

                        //System.out.println("i="+i+wbillarr[i]+" "+consr[i]+" "+arear[i]+" "+phoner[i]+" "+compnyr[i]+" "+civilidr[i]+" "+stopdelarr[i]);
                        //  if (ApprvalStatus == null) ApprvalStatus = "";
                        //  if (stopdelarr[i] == 0 || (stopdelarr[i] == 1 && ApprvalStatus.equals("APPROVED") && Attemptstatus.equals("0"))) {


                        if (wbillarr[i] != null && ! laststats.equals("DELIVERED")) {
                            UserNotyTrackResp = WebService.CUSTOMER_NOTIFY_TRACK(wbillarr[i], dcode);
                            System.out.println("wbillarr[i] are:" + wbillarr[i]);
                            c2.moveToNext();
                            if (UserNotyTrackResp == null) {

                                Toast.makeText(DialogActivity.this, "Please Try again!",
                                        Toast.LENGTH_LONG).show();
                                return;
                            }
                            if (UserNotyTrackResp.contains("TRUE"))

                            {
                                waybilldialog = new TextView(DialogActivity.this);
                                // waybilltxt.setLayoutParams(lp);
                                waybilldialog.setGravity(Gravity.CENTER_HORIZONTAL);
                                waybilldialog.setText(Taskwabill);
                                System.out.println("wbill text is" + wbill);


                                trdialog.addView(waybilldialog);


                                resulttabledialog.addView(trdialog, new TableLayout.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));


                            } else if(UserNotyTrackResp.contains("INVALIDVALUE")){
                                Toast.makeText(_activity, UserNotyTrackResp, Toast.LENGTH_LONG).show();
                                return;
                            }else{
                                Toast.makeText(_activity, "Not in your Runsheet", Toast.LENGTH_LONG).show();
                                return;
                            }


                        } else {
                            if (stopdelarr[i] == 1 && ApprvalStatus.equals("APPROVED")) {
                                _activity.runOnUiThread(new Runnable() {
                                    public void run() {
                                        Toast.makeText(_activity, "Stopped Delivery", Toast.LENGTH_LONG).show();
                                    }
                                    // Toast.makeText(getApplicationContext(), "Stopped Delivery",
                                    //  Toast.LENGTH_LONG).show();
                                });

                            } else {
                                _activity.runOnUiThread(new Runnable() {
                                    public void run() {
                                        Toast.makeText(_activity, "Already Delivered", Toast.LENGTH_LONG).show();
                                    }
                                });
                                // Toast.makeText(StartDeliveryActivity.this, "Already Delivered",
                                //        Toast.LENGTH_LONG).show();
                            }
                        }

                *//*  if(!wbillarr[i].equals(null)){
                           UserNotyTrackResp = WebService.CUSTOMER_NOTIFY_TRACK(wbillarr[i], dcode);
                       } else {
                           _activity.runOnUiThread(new Runnable() {
                               public void run() {
                                   Toast.makeText(_activity, "Not in your Runsheet", Toast.LENGTH_LONG).show();
                               }
                           });

                       }*//*
                    }

                    db.close();



                    c2.close();
                    db.close();


                    //  UserNotyTrackResp = WebService.CUSTOMER_NOTIFY_TRACK(wbillarr[i], drivercode);
                    System.out.println("UserNotyTrackResp 2:" + UserNotyTrackResp);
                *//*    if (UserNotyTrackResp == null) {

                        Toast.makeText(DialogActivity.this, "Please Try again!",
                                Toast.LENGTH_LONG).show();
                        return;
                    }


                    if (UserNotyTrackResp.contains("TRUE"))

                    {
                        waybilldialog = new TextView(DialogActivity.this);
                        // waybilltxt.setLayoutParams(lp);
                        waybilldialog.setGravity(Gravity.CENTER_HORIZONTAL);
                        waybilldialog.setText(wbill);
                        System.out.println("wbill text is" + wbill);


                        trdialog.addView(waybilldialog);


                        resulttabledialog.addView(trdialog, new TableLayout.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));


                    } else if(UserNotyTrackResp.contains("INVALIDVALUE")){
                        Toast.makeText(_activity, UserNotyTrackResp, Toast.LENGTH_LONG).show();
                    }else{
                        Toast.makeText(_activity, "Not in your Runsheet", Toast.LENGTH_LONG).show();
                    }
*//*

                }else{
                    Toast.makeText(DialogActivity.this, "Not in your Runsheet!",
                            Toast.LENGTH_LONG).show();
                    return;
                }

            }catch (Exception e){

            }
        }*/

        @Override
        public void onPostExecute(String res) {
            //response=null;
            System.out.println("UserNotyTrackResp on post:" + UserNotyTrackResp);

            try {
                System.out.println("UserNotyTrackResp notfytrck1:" + UserNotyTrackResp);


                db = new DatabaseHandler(getBaseContext());
                System.out.println("db dialog:" + db);
                //open localdatabase in a read mode
                //open localdatabase in a read mode
                sqldb = db.getReadableDatabase();
                // Cursor c2 = sqldb.rawQuery("SELECT * FROM deliverydata WHERE Waybill='" + wbill + "' AND AWBIdentifier= '" + FlagDeliveryMode + "' ", null);
                System.out.println("wbill dialog on post:" + db+"wbill:"+wbill);
                Cursor c2 = sqldb.rawQuery("SELECT * FROM deliverydata WHERE Waybill='" + wbill+ "' ", null);

                count1 = c2.getCount();
                //System.out.println("stage");
                c2.moveToFirst();
                wbillarr = new String[count1];

                stopdelarr = new int[count1];

                System.out.println("wbillarr dialog delv:" + wbillarr.toString()+"c2.getCount()"+c2.getCount() );

                if (c2.getCount() > 0) {


                    for (int i = 0; i < count1; i++) {
                        //System.out.println("stage3");
                        wbillarr[i] = c2.getString(c2.getColumnIndex("Waybill"));

                        stopdelarr[i] = c2.getInt(c2.getColumnIndex("StopDelivery"));
                        laststats=c2.getString(c2.getColumnIndex("WC_Status"));

                        ApprvalStatus = c2.getString(c2.getColumnIndex("ApprovalStatus"));
                        Attemptstatus = c2.getString(c2.getColumnIndex("Attempt_Status"));

                        System.out.println("approval status is:" + ApprvalStatus + "Stopdlv : " + stopdelarr[i] + " Attpmpt " + Attemptstatus+"laststats"+laststats);
                        if(laststats.equals("C")) laststats = "DELIVERED";
                        else if(laststats.equals("A")) laststats = "WC";


                        //System.out.println("i="+i+wbillarr[i]+" "+consr[i]+" "+arear[i]+" "+phoner[i]+" "+compnyr[i]+" "+civilidr[i]+" "+stopdelarr[i]);
                        if (ApprvalStatus == null) ApprvalStatus = "";
                        //if (stopdelarr[i] == 0 || (stopdelarr[i] == 1 && ApprvalStatus.equals("APPROVED") && Attemptstatus.equals("0"))) {



                        if (wbillarr[i] != null && ! laststats.equals("DELIVERED")) {
                            System.out.println("Taskwabill:" + Taskwabill + "wbarr" + wbillarr[i]);

                            if(resulttabledialog.getChildCount()>0) {
                                TextView wbillres = null;
                                for (int k = 0; k < resulttabledialog.getChildCount(); k++) {
                                    wbillres = (TextView) ((TableRow) resulttabledialog.getChildAt(k)).getChildAt(0);
                                    System.out.println("wbresultdialog:" + wbillres.getText().toString());
                                    if (Taskwabill.equals(wbillres.getText().toString())) {
                                        Toast.makeText(DialogActivity.this, "No duplicate waybills allowed!",
                                                Toast.LENGTH_LONG).show();
                                        return;
                                    }
                                }

                            }
                            UserNotyTrackResp = WebService.CUSTOMER_NOTIFY_TRACK(wbillarr[i], dcode);
                            System.out.println("wbillarr[i] are:" + wbillarr[i]);
                            c2.moveToNext();
                            if (UserNotyTrackResp == null) {

                                Toast.makeText(DialogActivity.this, "Please Try again!",
                                        Toast.LENGTH_LONG).show();
                                return;
                            }
                            if (UserNotyTrackResp.contains("TRUE")) {
                                waybilldialog = new TextView(DialogActivity.this);
                                // waybilltxt.setLayoutParams(lp);
                                waybilldialog.setGravity(Gravity.CENTER_HORIZONTAL);
                                waybilldialog.setText(Taskwabill);
                                System.out.println("wbill dial text is" + wbillarr[i]);


                                trdialog.addView(waybilldialog);


                                resulttabledialog.addView(trdialog, new TableLayout.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));


                            } else if (UserNotyTrackResp.contains("INVALIDVALUE")) {
                                Toast.makeText(  DialogActivity.this, UserNotyTrackResp, Toast.LENGTH_LONG).show();
                                return;
                            } else {
                                Toast.makeText(  DialogActivity.this, "Not in your Runsheet", Toast.LENGTH_LONG).show();
                                return;
                            }


                        }else {

                            Toast.makeText(  DialogActivity.this, "Already Delivered", Toast.LENGTH_LONG).show();
                            return;



                        }


                    }

                    db.close();
                    c2.close();
                    db.close();


                }else{
                    Log.e("ccddv","12");
                    Toast.makeText(DialogActivity.this, "Not in your Runsheet!",
                            Toast.LENGTH_LONG).show();
                    return;
                }

            }catch (Exception e){

            }
        }
    }
    @Override
    public void onBackPressed()
    {
        //don't call super
        if (barcodeReader != null) {
            barcodeReader.release();
            barcodeReader.removeBarcodeListener(DialogActivity.this);
        }

    }
    @Override
    public void onFailureEvent(BarcodeFailureEvent honeywelbar) {

    }

    @Override
    public void onTriggerEvent(TriggerStateChangeEvent triggerStateChangeEvent) {

    }

}