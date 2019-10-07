package com.postaplus.postascannerapp;


import android.app.Dialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothDevice;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TableRow.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.honeywell.aidc.BarcodeFailureEvent;
import com.honeywell.aidc.BarcodeReadEvent;
import com.honeywell.aidc.BarcodeReader;
import com.honeywell.aidc.ScannerUnavailableException;
import com.honeywell.aidc.TriggerStateChangeEvent;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.ExecutionException;

import DatabaseHandlers.DBFCS.TBLogin;
import DatabaseHandlers.DBFunctions;
import koamtac.kdc.sdk.KDCData;
import koamtac.kdc.sdk.KDCReader;
import utils.Utils;
import webservice.FuncClasses.CheckValidPickupWaybill;
import webservice.FuncClasses.PickupHoldwaybills;
import webservice.FuncClasses.setPickUpDt;
import webservice.WebService;

import static com.postaplus.postascannerapp.HomeActivity.barcodeReader;

public class PickupUpdateActivity extends MasterActivity
        implements BarcodeReader.BarcodeListener, BarcodeReader.TriggerListener {
    GPSTracker gps;
    int rowid = -1;
    double latitude, longitude;
    int pickflag;
    TextView username, picknotxt, accnotxt, wbilltxt, paytxt, amounttxt, servicetxt, deltxt, payidtxt, serviceidtxt, counttxt, tagtxt,waybilldialog;
    EditText amountedt;
    Button back, scan, pickup, finishbtn;
    String drivercode, route, routen, waybill, waybillCam, pickupno, service, paytype, amount, serviceId, serviceType, payId, accountname, date_time, payIde, serviceIde;
    String[] serviceTypearr, serviceIdarr, payTypearr, payIdarr, wbilltabarr, amounttabarr, servicetabarr, paytabarr, remarkcode, remarkdesc;
    int count, count1, tablecount;
    Spinner servicespinner, paytypespinner, pickupremksspinner;

    TableLayout resulttab;
    TableRow tr;
    int i = 0;
    LayoutParams lp;
    static boolean errored = false;
    boolean flag = false;
    boolean mastreflag = false;
    String pick_status, pickstts, newpickStatus;
    public int SCANNER_REQUEST_CODE = 123;
    //KDC Parameters
    //
    public static String WaybillFromScanner = "";
    public static String KDCScannerCallFrom = "";

    Resources _resources;
    BluetoothDevice _btDevice = null;
    static final byte[] TYPE_BT_OOB = "application/vnd.bluetooth.ep.oob".getBytes();
    Button _btnScan = null;

    //BluetoothDevice _btDevice;
    PickupUpdateActivity _activity;
    KDCData ScannerData;
    KDCReader _kdcReader;
    public String chkdata = "";
    public String wbill;
    View rootView;
    //public PickupUpdateActivity MYActivity;
    Context mContext;
    Thread ThrKdc;
    String pickupfinishresponse, dvrcode, selectedspinner, label;
    //KDCTask KDCTaskExecutable = new KDCTask();
    public boolean isActivityActiveFlag = false;
    static boolean DonotInterruptKDCScan = true;
    String status = "PU";
    ProgressBar pb;
    public ProgressDialog mProgressDialog;

    setPickUpDt[] setpkpdtRequest;
    int Flagcam = 0;
    int countN = 0;
    ProgressDialog pd;
    boolean processClick;
    private long lastClickTime = 0;
    AsyncTask pickupTask;
    int minteger = 0;
    int intmax = 99, intmin = 1;
    Button decrease, increase;
    CheckBox cbmps,cbReference;
    LinearLayout tagcounter;
    int tagvalue;
    TextView masterawbtxt;
    String mastrawbstrng;
    ProgressBar Pb;
    String usercode, tagmps;
    String delwaybillpckp;
    ArrayList<String> Scannedwaybillarry = new ArrayList<String>();
    String[] scanawbArray;
    Button clearbtn;
    Boolean Pickpflag = false;
    PickupHoldwaybills[] pckpholdwaybillresp;
    ArrayList<PickupHoldwaybills> Pickphldwayblarr = null;
    PickupHoldwaybills pickphldwaybil;
    String waybill1 = "", rname1 = "", cname1 = "", error1 = "", phone = "", area1 = "", company1 = "", civilid1 = "", serial1 = "", cardtype1 = "", deldate1 = "", deltime1 = "", amount1 = "", addresshold1 = "", adress1 = "",
            awbidetnfr1 = "", ShiperName = "", attempt1 = "", laststa1 = "";
    CheckValidPickupWaybill chckvalidpickupwaybilresp;
    boolean createflag = false;
    String Scanrwabill;
    String ErrMesageRef;

    TableLayout resulttabledialog;
    boolean delvryflag=false;
    TextView textchoose,textalert;

    String ApprvalStatus, Attemptstatus;
    TableRow trdialog;
    String UserNotyTrackResp;
    String laststats;
    String[]  wbillarr;
    int[] stopdelarr;

    public void ScannerExecutions() {
        System.out.println(" pdata new value in chkdata is ");
        System.out.println(chkdata);
        //Inializations
        if (servicespinner == null) servicespinner = (Spinner) findViewById(R.id.servicespinner);
        if (paytypespinner == null) paytypespinner = (Spinner) findViewById(R.id.paytypespinner);
        if (servicetabarr == null) servicetabarr = new String[count];
        if (paytabarr == null) paytabarr = new String[count];
        if (amountedt == null) amountedt = (EditText) findViewById(R.id.amountet);
        if (amount == null) amount = amountedt.getText().toString();


    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (isActivityActiveFlag)
                PickupUpdateActivity.this.finish();
            Intent int1 = new Intent(PickupUpdateActivity.this, PickupActivity.class);

            int1.putExtra("route", route);
            int1.putExtra("route1", routen);

            startActivity(new Intent(int1));

            return true;
           /* if(!isActivityActiveFlag)
            {
                Toast.makeText(getApplicationContext(), "Please wait for scanner to connect",
                        Toast.LENGTH_LONG).show();
                return false;
            }
            else{
                *//*Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);*//*
                if(_kdcReader!=null) _kdcReader.Disconnect();
                if(ThrKdc!=null)ThrKdc.interrupt();
               // KDCTaskExecutable.cancel(true);
                PickupUpdateActivity.this.finish();
                Intent int1 = new Intent(PickupUpdateActivity.this,PickupActivity.class);

                int1.putExtra("route",route);
                int1.putExtra("route1",routen);

                startActivity(new Intent(int1));

                return true;
            }*/

        }
        return false;
    }

    public void onCreate(Bundle savedInstanceState) {

        Log.e("SpinOnCreate","OnCreateCalled");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pickup_update);
        mContext = this;

        pickstts = new String();
        pref = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        username = (TextView) findViewById(R.id.unametxt);
        picknotxt = (TextView) findViewById(R.id.textpickup);
        username.setText(pref.getString("uname", ""));
        drivercode = username.getText().toString();
        accnotxt = (TextView) findViewById(R.id.textaccname);
        servicespinner = (Spinner) findViewById(R.id.servicespinner);
        paytypespinner = (Spinner) findViewById(R.id.paytypespinner);
        amountedt = (EditText) findViewById(R.id.amountet);
        resulttab = (TableLayout) findViewById(R.id.tlTable);
        pickupremksspinner = (Spinner) findViewById(R.id.remarkspinner);
        counttxt = (TextView) findViewById(R.id.countText);

        route = getIntent().getExtras().getString("routecode");
        routen = getIntent().getExtras().getString("routename");

        back = (Button) findViewById(R.id.btnbck);
        //scan=(Button)findViewById(R.id.btnscan);
        pickup = (Button) findViewById(R.id.buttonpickup);
        finishbtn = (Button) findViewById(R.id.btnfinish);
        pb = (ProgressBar) findViewById(R.id.progressBarpckp);
        Pb = (ProgressBar) findViewById(R.id.progrespickp);
        Pb.setVisibility(View.INVISIBLE);
        clearbtn = (Button) findViewById(R.id.clearbtn);
        pickstts = "YES";
        processClick = true;
        //	pd = new ProgressDialog(PickupUpdateActivity.this);

        //mProgressDialog = new ProgressDialog(this);
        //mProgressDialog.setMessage("Loading........");
        //mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        //mProgressDialog.setCancelable(false);
        cbmps = (CheckBox) findViewById(R.id.cbmps);
        cbReference = (CheckBox)findViewById(R.id.cbReference);
        tagcounter = (LinearLayout) findViewById(R.id.tagcounter);
        tagcounter.setVisibility(View.GONE);
        pickupno = getIntent().getExtras().getString("pickno");
        picknotxt.setText(pickupno);
        //decrease=(Button)findViewById(R.id.decrease);
        //increase=(Button)findViewById(R.id.increase);
        masterawbtxt = (TextView) findViewById(R.id.masterawbtxt);

        //KDC Full Commands
        _activity = this;
        //mContext=this;

        _resources = getResources();
        Pickphldwayblarr = new ArrayList<>();

        barcodeReader.addBarcodeListener(_activity);

        if (barcodeReader != null) {
            try {
                barcodeReader.claim();
                System.out.println("barcode pickup onres:" + barcodeReader);
            } catch (ScannerUnavailableException e) {
                e.printStackTrace();
                Toast.makeText(this, "Scanner unavailable", Toast.LENGTH_SHORT).show();
            }

        }
        getpckpholdwaybill();

        db = new DatabaseHandler(getBaseContext());
        //open localdatabase in a read mode
        sqldb = db.getReadableDatabase();

        //select all values from eventtable and populate it in the spinner
        Cursor cr = sqldb.rawQuery("SELECT '-1' AS RemarkCode,'' AS RemarkDesc UNION SELECT RemarkCode,RemarkDesc  FROM pickupRemarks ORDER BY RemarkDesc ASC", null);
        int countRmk = cr.getCount();


        remarkcode = new String[countRmk];
        remarkdesc = new String[countRmk];
        //System.out.println("stage3");
        cr.moveToFirst();

        for (int i = 0; i < countRmk; i++) {
            System.out.println("Remarkdesc is" + cr.getString(cr.getColumnIndex("RemarkDesc")));

            remarkcode[i] = cr.getString(cr.getColumnIndex("RemarkCode"));
            remarkdesc[i] = cr.getString(cr.getColumnIndex("RemarkDesc"));

            // System.out.println("i="+i+eventname[i]);
            cr.moveToNext();
        }

        //Collections.sort(eventname);
        ArrayAdapter<String> adapterRemk = new ArrayAdapter<String>(PickupUpdateActivity.this, android.R.layout.simple_spinner_item, remarkdesc);
        adapterRemk.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        pickupremksspinner.setAdapter(adapterRemk);
        pickupremksspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //label = parent.getItemAtPosition(position).toString();
                label = String.valueOf(remarkcode[pickupremksspinner.getSelectedItemPosition()]);
                //	label = parent.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

                return;
            }
        });

        //	evspinner.setOnItemSelectedListener();
        // closing connection
        cr.close();
        db.close();


        db = new DatabaseHandler(getBaseContext());
        //open localdatabase in a read mode
        sqldb = db.getReadableDatabase();

        Cursor c = sqldb.rawQuery("SELECT Account_Name FROM pickuphead WHERE Pickup_No='" + pickupno + "'", null);
        count = c.getCount();
        if (count > 0) {
            c.moveToFirst();
            accountname = c.getString(c.getColumnIndex("Account_Name"));
        }
        //amount=getIntent().getExtras().getString("accno");
        accnotxt.setText(accountname);
        c.close();


        //select all values from servicetable and populate it in the spinner
        Cursor c1 = sqldb.rawQuery("SELECT 'NA' AS ServiceID,'NA' AS ServiceTYPE UNION SELECT ServiceID,ServiceTYPE  FROM servicedetails ", null);
        count = c1.getCount();


        serviceIdarr = new String[count];
        serviceTypearr = new String[count];

        c1.moveToFirst();

        for (int i = 0; i < count; i++) {

            serviceTypearr[i] = c1.getString(c1.getColumnIndex("ServiceTYPE"));
            serviceIdarr[i] = c1.getString(c1.getColumnIndex("ServiceID"));


            c1.moveToNext();
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(PickupUpdateActivity.this, android.R.layout.simple_spinner_item, serviceTypearr);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        servicespinner.setAdapter(adapter);
        servicespinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                serviceIde = String.valueOf(serviceIdarr[servicespinner.getSelectedItemPosition()]);
                System.out.println("serviceid is" + serviceIde);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

                return;
            }
        });

        // closing connection
        c1.close();

        //select all values from paytypedetails table and populate it in the spinner
        Cursor c2 = sqldb.rawQuery("SELECT 'NA' AS PayID,'NA' AS PayTYPE UNION SELECT PayID,PayTYPE  FROM paytypedetails ", null);
        count1 = c2.getCount();


        payIdarr = new String[count1];
        payTypearr = new String[count1];

        c2.moveToFirst();

        for (int i = 0; i < count1; i++) {

            payTypearr[i] = c2.getString(c2.getColumnIndex("PayTYPE"));
            payIdarr[i] = c2.getString(c2.getColumnIndex("PayID"));

            c2.moveToNext();
        }

        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(PickupUpdateActivity.this, android.R.layout.simple_spinner_item, payTypearr);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        paytypespinner.setAdapter(adapter1);
        paytypespinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                payIde = String.valueOf(payIdarr[paytypespinner.getSelectedItemPosition()]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

                return;
            }
        });


        // closing connection
        c2.close();

        db.close();
        //	sqldb.close();
        //For username
        db = new DatabaseHandler(getBaseContext());
        //open localdatabase in a read mode
        sqldb = db.getReadableDatabase();

        //select all values in the table and check count
        Cursor clog = sqldb.rawQuery("SELECT * FROM logindata", null);
        int countlog = clog.getCount();
        clog.moveToFirst();

        TBLogin ActiveLogin = DBFunctions.GetLoggedUser(getBaseContext());
        dvrcode = ActiveLogin.USER_NAME;

        //dvrcode=clog.getString(clog.getColumnIndex("Username"));

        clog.close();
        db.close();
        //	sqldb.close();
        //back button code
        back.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                v.startAnimation(AnimationUtils.loadAnimation(getBaseContext(), R.anim.image_click));
                DonotInterruptKDCScan = false;
                // moveTaskToBack(true);
                //_activity.finishActivity(0);
              //  barcodeReader.close();

                barcodeReader.removeBarcodeListener(_activity);
                barcodeReader.release();
               /* Intent int1 = new Intent(_activity, PickupActivity.class);
                int1.putExtra("route", route);
                int1.putExtra("route1", routen);
                startActivity(new Intent(int1));*/
                System.out.println("_Actv is:"+_activity);
               /* SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(_activity);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("HoneywellFlag", "0");
                editor.commit();*/
                PickupUpdateActivity.this.finish();
               // startActivity(new Intent(_activity, PickupActivity.class));


            }
        });

        clearbtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(AnimationUtils.loadAnimation(getBaseContext(), R.anim.image_click));
                DonotInterruptKDCScan = false;
                // moveTaskToBack(true);

                delwaybillpckp = WebService.SET_DELETE_WAYBILL_PICKUP(drivercode, pickupno, "");
                System.out.println("delway resp:" + delwaybillpckp);
                if (delwaybillpckp != null) {
                    resulttab.removeAllViews();

                    servicespinner.setSelection(0);
                    paytypespinner.setSelection(0);
                    amountedt.setText("0.000");
                    //PickupUpdateActivity.this.finish();
                    barcodeReader.removeBarcodeListener(PickupUpdateActivity.this);
                    barcodeReader.release();
                    PickupUpdateActivity.this.finish();

                    /*Intent int1 = new Intent(_activity, PickupActivity.class);
                    int1.putExtra("route", route);
                    int1.putExtra("route1", routen);
                    startActivity(new Intent(int1));*/

                } else {
                    Toast.makeText(getApplicationContext(), "No Waybills to delete", Toast.LENGTH_SHORT).show();
                    return;
                }

            }
        });

        finishbtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                v.startAnimation(AnimationUtils.loadAnimation(getBaseContext(), R.anim.image_click));
                pb.setVisibility(View.VISIBLE);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MMM-dd HH:mm:ss");
                date_time = sdf.format(new Date());

                //	System.out.println("value in finishhh button"+label+dvrcode);

                if (resulttab.getChildCount() > 0) {
                    Toast.makeText(getApplicationContext(), "Cannot finish, Please choose Pickup", Toast.LENGTH_SHORT).show();

                } else {

                    if (label.equals("-1")) {

                        Toast.makeText(getApplicationContext(), "Please select any Remarks", Toast.LENGTH_SHORT).show();
                    } else {
                        // moveTaskToBack(true);
                        System.out.println("value in finish button" + dvrcode + pickupno + "label value is" + label + date_time);

                        if (isNetworkConnected()) {
                            pickupfinishresponse = WebService.SET_PICKUP_FINISH(dvrcode, pickupno, label, date_time);
                            if (pickupfinishresponse.equals("TRUE")) {
                                db = new DatabaseHandler(getBaseContext());
                                //open localdatabase in a read mode
                                sqldb = db.getWritableDatabase();

                                //Update pickuphead table and check count
                                sqldb.execSQL("UPDATE pickuphead SET Status='C',TransferStatus=1,CodeRemark='" + label + "',AttemptDatetime='" + date_time + "' WHERE Pickup_No='" + pickupno + "'");

                                Toast.makeText(getApplicationContext(), "Submitted", Toast.LENGTH_SHORT).show();
                                db.close();
                                //back button code
                            } else {
                                db = new DatabaseHandler(getBaseContext());
                                //open localdatabase in a read mode
                                sqldb = db.getWritableDatabase();

                                //Update pickuphead table and check count
                                sqldb.execSQL("UPDATE pickuphead SET Status='C',TransferStatus=0,CodeRemark='" + label + "',AttemptDatetime='" + date_time + "' WHERE Pickup_No='" + pickupno + "'");

                                Toast.makeText(getApplicationContext(), "Submitted", Toast.LENGTH_SHORT).show();
                                pb.setVisibility(View.INVISIBLE);
                                db.close();
                                minteger = 0;
                            }
                        } else {
                            db = new DatabaseHandler(getBaseContext());
                            //open localdatabase in a read mode
                            sqldb = db.getWritableDatabase();

                            //Update pickuphead table and check count
                            sqldb.execSQL("UPDATE pickuphead SET Status='C',TransferStatus=0,CodeRemark='" + label + "',AttemptDatetime='" + date_time + "' WHERE Pickup_No='" + pickupno + "'");

                            Toast.makeText(getApplicationContext(), "Submitted", Toast.LENGTH_SHORT).show();
                            pb.setVisibility(View.INVISIBLE);
                            db.close();
                            minteger = 0;


                        }

                        _activity.finishActivity(0);

                        Intent int1 = new Intent(_activity, PickupActivity.class);
                        int1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        int1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        int1.putExtra("route", route);
                        int1.putExtra("route1", routen);

                        startActivity(new Intent(int1));
                    }

                }


            }
        });

        //scan button code
	/*	scan.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				v.startAnimation(AnimationUtils.loadAnimation(getBaseContext(), R.anim.image_click));
				if(servicespinner.getSelectedItemPosition()!=0&&paytypespinner.getSelectedItemPosition()!=0)

				{
					if(paytypespinner.getSelectedItem().equals("Cash"))
					{
						amount=amountedt.getText().toString();
						if(!amount.contains("0.000")||!amount.contains(""))
						{


							if (v.getId() == R.id.btnscan) 
							{
								// go to fullscreen scan
								Intent intent = new Intent("com.google.zxing.client.android.SCAN");
								intent.putExtra("SCAN_MODE", "SCAN_MODE");
								startActivityForResult(intent, SCANNER_REQUEST_CODE);
							}
						}
						else if(amount.contains("0.000")||amount.contains(null))
							
						{  _activity.runOnUiThread(new Runnable(){
							  @Override
							  public void run(){
								  
								  Toast.makeText(getApplicationContext(), "Please enter the amount", Toast.LENGTH_SHORT).show();
								  System.out.println("amount in pickup update on pickup");
									System.out.println(amount);	
							  }
						  });

							
							
							//Toast.makeText(getApplicationContext(), "Please enter the amount", Toast.LENGTH_SHORT).show();
						}
					}
					else if(!paytypespinner.getSelectedItem().equals(""))
					{
						if (v.getId() == R.id.btnscan) 
						{
							// go to fullscreen scan
							Intent intent = new Intent("com.google.zxing.client.android.SCAN");
							intent.putExtra("SCAN_MODE", "SCAN_MODE");
							startActivityForResult(intent, SCANNER_REQUEST_CODE);
						}
					}
				}

				else if (servicespinner.getSelectedItemPosition()==0)
				{
					 _activity.runOnUiThread(new Runnable(){
						  @Override
						  public void run(){
							  
							  Toast.makeText(getApplicationContext(), "Please Select Service Type", Toast.LENGTH_SHORT).show(); 				
							  System.out.println("servicespinner in pickup update on pickup");
								System.out.println(paytypespinner);	
						  }
					  });
					
					
					//Toast.makeText(getApplicationContext(), "Please Select Service Type", Toast.LENGTH_SHORT).show();
				}
				else if (paytypespinner.getSelectedItemPosition()==0)
				{
					 _activity.runOnUiThread(new Runnable(){
						  @Override
						  public void run(){
							  
							  Toast.makeText(getApplicationContext(), "Please Select Pay Type", Toast.LENGTH_SHORT).show();				
							  System.out.println("paytypespinner in pickup update on pickup");
								System.out.println(paytypespinner);
						  }
					  });
					
					//Toast.makeText(getApplicationContext(), "Please Select Pay Type", Toast.LENGTH_SHORT).show();
				}

			}
		}); */

        pickup.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                v.startAnimation(AnimationUtils.loadAnimation(getBaseContext(), R.anim.image_click));
                //	pd.show(PickupUpdateActivity.this, "Loading", "Wait while loading...");
                //	pb.setVisibility(View.VISIBLE);
				/*pickstts="YES";
				if(processClick)
				{
					pickup.setEnabled(false);
					pickup.setClickable(false);
					pickup.setVisibility(View.GONE);
					processClick=false;
				}*/
                //	final ProgressDialog pd = new ProgressDialog(PickupUpdateActivity.this);

                if (SystemClock.elapsedRealtime() - lastClickTime < 1000) {
                    return;
                }

              /*  if(resulttab.getChildCount()==0){
                    Toast.makeText(getApplicationContext(), "Please scan awbs to Pickup", Toast.LENGTH_SHORT).show();
                    return;
                }*/

                tablecount = resulttab.getChildCount();
                //wbilltabarr=new String[count];
                //amounttabarr=new String[count];
                //servicetabarr=new String[count];
                //paytabarr=new String[count];
                System.out.println("VALUES FROM  PICKUP " + tablecount + " " + drivercode + " " + pickupno);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MMM-dd HH:mm:ss");
                date_time = sdf.format(new Date());
                gps = new GPSTracker(mContext, PickupUpdateActivity.this);

                // check if GPS enabled
                if (gps.canGetLocation()) {

                    latitude = gps.getLatitude();
                    longitude = gps.getLongitude();

                    // \n is for new line
                    //  Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
                } else {
                    // can't get location
                    // GPS or Network is not enabled
                    // Ask user to enable GPS/network in settings
                    gps.showSettingsAlert();
                }
                pickflag = 0;

                setpkpdtRequest = new setPickUpDt[tablecount];
                for (int i = 0; i < tablecount; i++) {
                    setpkpdtRequest[i] = new setPickUpDt();

                }
                for (int i = 0; i < tablecount; i++) {
                    wbilltxt = (TextView) ((TableRow) resulttab.getChildAt(i)).getChildAt(1);
                    paytxt = (TextView) ((TableRow) resulttab.getChildAt(i)).getChildAt(2);
                    amounttxt = (TextView) ((TableRow) resulttab.getChildAt(i)).getChildAt(3);
                    servicetxt = (TextView) ((TableRow) resulttab.getChildAt(i)).getChildAt(4);
                    tagtxt = (TextView) ((TableRow) resulttab.getChildAt(i)).getChildAt(5);
                    System.out.println("tagtxt befre add" + tagtxt);
// tagval added to db tab

                    db = new DatabaseHandler(getBaseContext());
                    //open localdatabase in a read mode
                    sqldb = db.getReadableDatabase();
                    //select the payid from the paytypedetails table
                    Cursor c1 = sqldb.rawQuery("SELECT * FROM paytypedetails WHERE PayTYPE='" + paytxt.getText().toString() + "'", null);
                    if (c1.getCount() > 0) {
                        c1.moveToFirst();
                        if (payId == null) {
                            payId = c1.getString(c1.getColumnIndex("PayID"));
                            System.out.println("payId in pickup update on pickup");
                            System.out.println(payId);

                        }

                    }
                    c1.close();
                    Cursor c2 = sqldb.rawQuery("SELECT * FROM servicedetails WHERE ServiceTYPE='" + servicetxt.getText().toString() + "'", null);
                    if (c2.getCount() > 0) {
                        c2.moveToFirst();
                        if (serviceId == null) {
                            serviceId = c2.getString(c2.getColumnIndex("ServiceID"));
                            System.out.println("serviceId in pickup update on pickup");
                            System.out.println(serviceId);
                        }

                    }
                    c2.close();

                    System.out.println("WAYBILL IN PICKUP : " + wbilltxt.getText().toString());
                    System.out.println("Tag_value IN PICKUP : " + tagtxt.getText().toString());
                    System.out.println("mastrawbstrng IN PICKUP : " + mastrawbstrng);
                    sqldb = db.getWritableDatabase();
                    ContentValues values = new ContentValues();
                    values.put("Driver_Code", drivercode);
                    values.put("Pickup_No", pickupno);
                    values.put("Waybill_Number", wbilltxt.getText().toString());
                    values.put("PayType", payId);
                    values.put("Amount", amounttxt.getText().toString());
                    values.put("ServiceType", serviceId);
                    values.put("Date_Time", date_time);
                    values.put("Tag_value", tagtxt.getText().toString());
                    //values.put("Tag_value",mastrawbstrng);
                    System.out.println("tag value insert:" + tagtxt.getText().toString());
// mps


                    sqldb.insertOrThrow("pickupdetails", null, values);

                    setpkpdtRequest[i].DRIVERCODE = drivercode;
                    setpkpdtRequest[i].PICKUPNO = pickupno;
                    setpkpdtRequest[i].WAYBILL = wbilltxt.getText().toString();
                    setpkpdtRequest[i].PAYTYPE = payId;
                    setpkpdtRequest[i].AMOUNT = amounttxt.getText().toString();
                    setpkpdtRequest[i].SERVICE = serviceId;
                    setpkpdtRequest[i].DATETIMESTR = date_time;
                    setpkpdtRequest[i].STATUS = status;
                    setpkpdtRequest[i].TAGVAL = tagtxt.getText().toString();
                    //setpkpdtRequest[i].TAGVAL=masterawbtxt.toString();
                    System.out.println("tag value is req:" + tagtxt.getText().toString());
                }

                //pb.setVisibility(View.VISIBLE);

                //	mProgressDialog.show();


                //pickupTask = new pickupTask().execute();
                //new	pickupTask().execute();
                //pd.show(PickupUpdateActivity.this, "Loading", "Wait while loading...").setProgress(20);
                //	pd.setMessage("Loading Wait while loading..." );
                //	pd.show();
                System.out.println("called method webservice");

                pickupTask = new AsyncTask() {
                    @Override
                    protected Object doInBackground(Object[] objects) {
                        System.out.println("pickup pickupno update background finished" + pickupno);
                        //pick_status= WebService.SET_PICKUPDETAILS(drivercode,setpkpdtRequest);
                        pick_status = WebService.SET_PICKUPDETAILS(drivercode, pickupno);
                        System.out.println("pickup status update on pickup background finished" + pick_status);
                        //pick_status=newpickStatus;
                        return pick_status;
                    }
                };

                System.out.println("pickuptask status is:" + pickupTask.getStatus());
                if (pickupTask.getStatus() == AsyncTask.Status.PENDING) {
                    //task=new ProgressBarShow();
                    //	pd.setMessage("Wait while loading..." );
                    //	pd.show();
                    if (pickstts.contentEquals("YES")) {
                        pickstts = "NO";

                        //	pick_status= WebService.SET_PICKUPDETAILS(drivercode,setpkpdtRequest);

                        try {
                            pickupTask.execute().get();

                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        }
                    }

                }

                if (pickupTask.getStatus() == AsyncTask.Status.RUNNING) {
                    //task=new ProgressBarShow();
                    //pickupTask.execute();
                    System.out.println("pickup status update on pickuptask running" + pick_status);
                    //	pd.setMessage("Wait while loading..." );
                    //	pd.show();

                }


                //	pd.dismiss();


                System.out.println("pickup status update on pickup" + pick_status);


                System.out.println("pickuptask status beore finished is" + pickupTask.getStatus());
                if (pickupTask.getStatus() == AsyncTask.Status.RUNNING) {
                    // START NEW TASK HERE

                    //	Updated adding show/hide:
                    System.out.println("pickup status update on pickuptask running" + pick_status);
                    System.out.println("pickupTask finished");
                    System.out.println("pickuptask status after finished is" + pickupTask.getStatus());

                    if (!pick_status.equals("TRUE")) {

                        Runnable progressRunnable = new Runnable() {

                            @Override
                            public void run() {
                                //pd.cancel();
                                //	pd.dismiss();
                                //pd.hide();
                                Log.e("close block", "123123");
                            }
                        };

                        Handler pdCanceller = new Handler();
                        pdCanceller.postDelayed(progressRunnable, 1200);

                        Toast.makeText(getApplicationContext(), pick_status, Toast.LENGTH_SHORT).show();

                        lastClickTime = SystemClock.elapsedRealtime();
                        pickstts = "YES";

                        return;
                    }

                    System.out.println("pickup status in pickup update");
                    System.out.println(pick_status);
                    System.out.println(drivercode);
                    System.out.println(pickupno);
                    System.out.println(wbilltabarr);
                    System.out.print(payId);
                    System.out.println("amount is" + amounttabarr);
                    System.out.print(serviceId);
                    System.out.println(date_time);
                    System.out.println(METHOD_NAME22);
                    System.out.println("tag val for db is:" + tagvalue);


                    if (pick_status.equals("TRUE")) {

                        sqldb = db.getWritableDatabase();
                        sqldb.execSQL("UPDATE pickuphead SET Status='C' WHERE Pickup_No='" + pickupno + "'");
                        sqldb.execSQL("UPDATE pickuphead SET TransferStatus=2 WHERE Pickup_No='" + pickupno + "'");
                        sqldb.execSQL("UPDATE pickuphead SET Pickup_Date_Time='" + date_time + "' WHERE Pickup_No='" + pickupno + "'");

                        Toast.makeText(getApplicationContext(), "Submitted", Toast.LENGTH_SHORT).show();
                        for (int j = resulttab.getChildCount(); j >= 0; j--) {
                            resulttab.removeAllViews();
                            counttxt.setText(String.valueOf(resulttab.getChildCount()));
                            System.out.println("value of counttxt true in pickup update" + counttxt.getText().toString());
                        }


                        openDialog();

                        amountedt.setText("");
                        servicespinner.setSelection(0);
                        paytypespinner.setSelection(0);
                        picknotxt.setText("");
                        accnotxt.setText("");
                        //	pb.setVisibility(View.INVISIBLE);
                      //  _activity.finishActivity(0);
                       /* Intent int1 = new Intent(_activity, PickupActivity.class);

                        int1.putExtra("route", route);
                        int1.putExtra("route1", routen);

                        startActivity(new Intent(int1));*/
                        lastClickTime = SystemClock.elapsedRealtime();
                        pickstts = "YES";
                    } else {
                        Toast.makeText(getApplicationContext(), "Connection Error try after some time", Toast.LENGTH_SHORT).show();
                        pickstts = "YES";
                        //pb.setVisibility(View.INVISIBLE);
                        //	pd.dismiss();
                        lastClickTime = SystemClock.elapsedRealtime();
                    }
                    db.close();
                }
                //pb.setVisibility(View.INVISIBLE);
            }


        });
        cbmps.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {

                    //  cbmps.setChecked(false);
                    tagcounter.setVisibility(View.VISIBLE);
                    //mastrawbstrng=masterawbtxt.getText().toString();
                    System.out.println("mastrawbstrng is" + mastrawbstrng);

                    //masterawbtxt.setText("");
                    mastreflag = true;
                } else {

                    // cbmps.setChecked(false);
                    tagcounter.setVisibility(View.INVISIBLE);
                    masterawbtxt.setText("");
                    //tagvalue=0;
                    mastreflag = false;
                   /* getWindow().setSoftInputMode(
                            WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);*/
                }
            }
        });
    }

   /* @Override
    public void onResume() {
        super.onResume();
        if (!isActivityActiveFlag) isActivityActiveFlag = false;
        _activity = this;

        createflag = true;
        Log.e("SpinResume", "ResumeCalled");


    }*/

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        return ni != null;
    }

    private class pickupTask extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            System.out.println("pickup status update on pickup result preexecute");
        }

        @Override
        protected String doInBackground(String... strings) {

            //pick_status= WebService.SET_PICKUPDETAILS(drivercode,setpkpdtRequest);
            pick_status = WebService.SET_PICKUPDETAILS(drivercode, pickupno);
            System.out.println("pickup status update on pickup background finished" + pick_status);

            return pick_status;
        }

        @Override
        protected void onPostExecute(String s) {

            System.out.println("pickup status update on pickup result postexctve" + s);
            pick_status = s;
            super.onPostExecute(s);
        }


    }

    public void increaseInteger(View view) {
        minteger = minteger + 1;

        if (minteger > intmax) {
            increase.setClickable(false);
            System.out.println("intmax val is:" + intmax + "minteger in incremntal:" + minteger);
        } else {
            // increase.setClickable(true);
            decrease.setClickable(true);
        }
        //display(minteger);

    }
    @Override
    public void onBarcodeEvent(final BarcodeReadEvent event) {

        Log.e("OnBarcodeSpinner", "Called");

        _activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (event != null) {
                    wbill = event.getBarcodeData();
                    if(cbReference.isChecked()){
                        _activity.runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        if (event.getBarcodeData() != null) {
                                                            //String contents = intent.getStringExtra("SCAN_RESULT");
                                                            //tvScanResults.setText(contents);

                                                            //	waybill=wbill;
                                                            Scanrwabill = wbill;
                                                            if (delvryflag == true) {
                                                                new UserNotifyTrack(Scanrwabill).execute();
                                                            } else {
                                                                if (servicespinner.getSelectedItemPosition() == 0) {
                                                                    Toast.makeText(getApplicationContext(), "Please Select Service Type", Toast.LENGTH_SHORT).show();
                                                                    return;
                                                                } else if (amountedt.getText().toString().contains("0.000") && !payIde.contains("NA")) {
                                                                    Toast.makeText(getApplicationContext(), "Please enter Amount", Toast.LENGTH_SHORT).show();
                                                                    return;
                                                                }
                                                                System.out.println(" PickupUpdateactivity barcode wbill : " + wbill);

                                                                System.out.println(" PickupUpdateactivity barcode wbill : " + wbill);
                                                                //	String Masterbill = masterawbtxt.getText().toString();
                                                                System.out.println(" masterawbtxt barcode wbill : " + masterawbtxt.getText().toString());
                                                                String mastbill = "";
                                                                if (cbmps.isChecked() && masterawbtxt.getText().toString().equals("")) {
                                                                    masterawbtxt.setText(wbill);
                                                                    mastbill = wbill;

                                                                    // masterawbtxt.setText(wbill);
                                                                    Log.e("blck1", "1");

                                                                } else if (cbmps.isChecked() && !masterawbtxt.getText().toString().equals("")) {

                                                                    mastbill = masterawbtxt.getText().toString();

                                                                    Log.e("blck2", "2");
                                                                } else if (!cbmps.isChecked()) {
                                                                    mastbill = "";
                                                                    Log.e("blck3", "3");
                                                                }

                                                                System.out.println("wbill on barcode:" + wbill + "mastbill on barcode:" + mastbill);


                                                                new checkreferencewaybill(wbill, mastbill).execute();


                                                            }
                                                        }


                                                    }


                                                    // }


                                                }
                        );

                    }else if(!cbReference.isChecked()){
                        // StartDeliveryActivity.WaybillFromScanner = ScannerData.GetData();

                        if (Utils.checkValidWaybill(event.getBarcodeData()) == true) {

                            System.out.println(" PickupUpdateactivity ID : ");
                            // System.out.println(R.id.WC_Frame);
                            System.out.println("value for pdata is : " + event.getBarcodeData());
                            System.out.println(event);

                            _activity.runOnUiThread(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            if (wbill != null) {
                                                                //String contents = intent.getStringExtra("SCAN_RESULT");
                                                                //tvScanResults.setText(contents);

                                                                //	waybill=wbill;
                                                                Scanrwabill = wbill;
                                                                if (delvryflag == true) {
                                                                    new UserNotifyTrack(Scanrwabill).execute();
                                                                } else {
                                                                    if (servicespinner.getSelectedItemPosition() == 0) {
                                                                        Toast.makeText(getApplicationContext(), "Please Select Service Type", Toast.LENGTH_SHORT).show();
                                                                        return;
                                                                    } else if (amountedt.getText().toString().contains("0.000") && !payIde.contains("NA")) {
                                                                        Toast.makeText(getApplicationContext(), "Please enter Amount", Toast.LENGTH_SHORT).show();
                                                                        return;
                                                                    }
                                                                    System.out.println(" PickupUpdateactivity barcode wbill : " + wbill);

                                                                    System.out.println(" PickupUpdateactivity barcode wbill : " + wbill);
                                                                    //	String Masterbill = masterawbtxt.getText().toString();
                                                                    System.out.println(" masterawbtxt barcode wbill : " + masterawbtxt.getText().toString());
                                                                    String mastbill = "";
                                                                    if (cbmps.isChecked() && masterawbtxt.getText().toString().equals("")) {
                                                                        masterawbtxt.setText(wbill);
                                                                        mastbill = wbill;

                                                                        // masterawbtxt.setText(wbill);
                                                                        Log.e("blck1", "1");

                                                                    } else if (cbmps.isChecked() && !masterawbtxt.getText().toString().equals("")) {

                                                                        mastbill = masterawbtxt.getText().toString();

                                                                        Log.e("blck2", "2");
                                                                    } else if (!cbmps.isChecked()) {
                                                                        mastbill = "";
                                                                        Log.e("blck3", "3");
                                                                    }

                                                                    System.out.println("wbill on barcode:" + wbill + "mastbill on barcode:" + mastbill);


                                                                    new checkpickpwaybill(wbill, mastbill).execute();


                                                                }
                                                            }


                                                        }


                                                        // }


                                                    }
                            );

                        } else {

                            _activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {

                                    Toast.makeText(_activity, "Invalid Waybill", Toast.LENGTH_LONG).show();
                                }
                            });
                        }

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
    @Override
    public void onFailureEvent(BarcodeFailureEvent barcodeFailureEvent) {

    }

    @Override
    public void onTriggerEvent(TriggerStateChangeEvent triggerStateChangeEvent) {

    }

    public class checkpickpwaybill extends AsyncTask<Void, Void, String> {
        //String response = "";
        String Taskwabill = "";
        String TaskMasterwabill = "";
        CheckValidPickupWaybill CheckVPWaybillResp = null;

        public checkpickpwaybill(String TakWaybill, String Mastrwabill) {
            super();
            Taskwabill = TakWaybill;
            TaskMasterwabill = Mastrwabill;
            System.out.println("TaskMasterwabill onchckp" + TaskMasterwabill + "maaswreng" + masterawbtxt.getText().toString());

        }

        public void onPreExecute() {
            Pb.setVisibility(View.VISIBLE);
            Pickpflag = false;
            System.out.println("taskwaybill prexecute:" + Taskwabill);
            // super.onPreExecute();
            if (cbmps.isChecked()) {
                cbmps.setEnabled(false);
                cbmps.setChecked(true);
            }
        }

        @Override
        protected String doInBackground(Void... arg0) {
            CheckVPWaybillResp = checkwaybill(Taskwabill, TaskMasterwabill);

            return "";
        }

        @Override
        public void onPostExecute(String res) {
            //response=null;
            System.out.println("CheckVPWaybillResp pos: " + CheckVPWaybillResp.ErrMsg);
            if (CheckVPWaybillResp == null) {
                Pb.setVisibility(View.INVISIBLE);
                cbmps.setEnabled(true);
                Toast.makeText(PickupUpdateActivity.this, "Please Try again!",
                        Toast.LENGTH_LONG).show();
                return;
            } else if (CheckVPWaybillResp.ErrMsg != "") {
                cbmps.setEnabled(true);
                masterawbtxt.setText("");
                Toast.makeText(getApplicationContext(), CheckVPWaybillResp.ErrMsg,
                        Toast.LENGTH_LONG).show();
                Pb.setVisibility(View.INVISIBLE);
                return;
            }
            if (CheckVPWaybillResp.ErrMsg == null || CheckVPWaybillResp.ErrMsg == "")
            //	System.out.println("response : " + response.toString());
            {

                db = new DatabaseHandler(getBaseContext());
                //open localdatabase in a read mode
                sqldb = db.getReadableDatabase();
                Cursor cc = sqldb.rawQuery("SELECT Waybill FROM deliverydata WHERE Waybill='" + Taskwabill + "'", null);
                int wbcount = cc.getCount();
                System.out.println("wbcount " + wbcount);
                if (wbcount > 0) {
                    Log.e("reached ", "1231");
                    Toast.makeText(getApplicationContext(), "WayBill with Delivery", Toast.LENGTH_LONG).show();
                    Pb.setVisibility(View.INVISIBLE);
                    return;
                } else
                    System.out.println("mast onpost:" + TaskMasterwabill);
                //	masterawbtxt.setText(TaskMasterwabill);

                { 	/*if(cbmps.isChecked() ){
					cbmps.setEnabled(true);
				}*/
									/*if (cbmps.isChecked()) {
										if(masterawbtxt.getText().toString().equals("")){
										masterawbtxt.setText(waybill);
											mastreflag=true;
											flag=false;
										}
										flag=false;
									} else {*/
                    //mastreflag=true;
									/*if(mastreflag==true){
										masterawbtxt.setText(waybill);
										mastreflag=false;
										flag=true;
									}*/


                    //Pb.setVisibility(View.VISIBLE);
                    tr = new TableRow(PickupUpdateActivity.this);
                    if (Build.MODEL.contains("SM-N")) {
                        System.out.println("called smn barcodedatarecieved");
                        //	lp = new LayoutParams(385,LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
                        lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
                        tr.setId(resulttab.getChildCount());
                        //lp.setMargins(0, 10, 30, 0);
                        lp.setMargins(55, 2, 95, 2);
                        tr.setLayoutParams(lp);


                    } else {
						/*lp = new LayoutParams(150, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
							tr.setId(resulttab.getChildCount());
							tr.setLayoutParams(lp);
							lp.setMargins(0, 20, 10, 0);*/

                        lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
                        tr.setId(resulttab.getChildCount());
                        lp.setMargins(0, 20, 100, 0);
                        tr.setLayoutParams(lp);
                    }


                    deltxt = new TextView(PickupUpdateActivity.this);
                    deltxt.setLayoutParams(lp);
                    deltxt.setText("Delete");

                    wbilltxt = new TextView(PickupUpdateActivity.this);
                    wbilltxt.setLayoutParams(lp);
                    wbilltxt.setText(Taskwabill);


                    paytxt = new TextView(PickupUpdateActivity.this);
                    paytxt.setLayoutParams(lp);
                    paytxt.setText(paytype);

                    amounttxt = new TextView(PickupUpdateActivity.this);
                    amounttxt.setLayoutParams(lp);
                    amounttxt.setText(amount);

                    servicetxt = new TextView(PickupUpdateActivity.this);
                    servicetxt.setLayoutParams(lp);
                    servicetxt.setText(serviceType);

                    payidtxt = new TextView(PickupUpdateActivity.this);
                    //	payidtxt.setLayoutParams(lp);
                    payidtxt.setText(payIde);
                    System.out.println("payid txt is" + payIde);

                    serviceidtxt = new TextView(PickupUpdateActivity.this);
                    //	serviceidtxt.setLayoutParams(lp);
                    serviceidtxt.setText(serviceId);
                    System.out.println("serviceid txt is" + serviceIde);


                    // tag added for mps
                    tagtxt = new TextView(PickupUpdateActivity.this);
                    tagtxt.setLayoutParams(lp);
                    //tagtxt.setText(String.valueOf(tagvalue));
                    System.out.println("tagtxt on chck txt is" + mastrawbstrng);
                    tagtxt.setText(TaskMasterwabill);

											/*if(cbmps.isChecked()){
												tagtxt.setText(mastrawbstrng);
											}else{
												tagtxt.setText("");
											}*/

                    System.out.println("value of mastrawbstrng tables" + mastrawbstrng);
// condition added for MPS
                    if (cbmps.isChecked() && masterawbtxt.getText().toString().equals("")) {

                        //masterawbtxt.setText(Taskwabill);
                        //	tagtxt.setText(Taskwabill);
                        tr.addView(deltxt);
                        tr.addView(wbilltxt);
                        tr.addView(paytxt);
                        tr.addView(amounttxt);
                        tr.addView(servicetxt);
                        tr.addView(tagtxt);


                        System.out.println("value of wbilltxt in pickup update" + wbilltxt);
                        System.out.println("value of paytxt in pickup update" + paytxt);
                        System.out.println("value of amounttxt in pickup update" + amounttxt.getText());
                        System.out.print(amount);
                        System.out.println("value of servicetxt in pickup update" + servicetxt);

                        System.out.println("value of tagtext in pickup update" + tagtxt.getText().toString());
                        countN = resulttab.getChildCount() + 1;
                        System.out.println("value of counttxt in " + countN);
                        counttxt.setText(String.valueOf(countN));
                        System.out.println("value of counttxt in pickup update" + counttxt.getText().toString());

                        if (resulttab.getChildCount() == 0) {
                            resulttab.addView(tr, new TableLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
                        } else {
                            //	counttxt.setText(String.valueOf(resulttab.getChildCount()));
                            //	System.out.println("value of counttxt in pickup update"+counttxt.toString());

                            String wb;
                            for (int i = 0; i < resulttab.getChildCount(); i++) {

                                TextView wbill = (TextView) ((TableRow) resulttab.getChildAt(i)).getChildAt(1);
                                wb = wbill.getText().toString();


                                if (!Taskwabill.equals(wb)) {
                                    flag = true;

                                } else if (Taskwabill.equals(wb)) {
                                    flag = false;
                                    System.out.println(flag);

                                    break;

                                }
                            }
                            if (flag) {
                                resulttab.addView(tr, new TableLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
                            } else {
                                System.out.println("value of counttxt in else" + countN);
                                counttxt.setText(String.valueOf(countN - 1));
                                Toast.makeText(getApplicationContext(), "Duplicate WayBill not allowed,Please Scan again", Toast.LENGTH_LONG).show();
                            }

                        }
                        //	masterawbtxt.setText(waybill);
                    } else if (cbmps.isChecked() && !masterawbtxt.getText().toString().equals("")) {
                        // Check_mawb();
//check for mawb
												/*if(masterawbtxt.getText().toString().equals(waybill)){
													Toast.makeText(getApplicationContext(), "MasterAWB cannot be scanned",
															Toast.LENGTH_LONG).show();
													return ;
												}else*/
                        tr.addView(deltxt);
                        tr.addView(wbilltxt);
                        tr.addView(paytxt);
                        tr.addView(amounttxt);
                        tr.addView(servicetxt);
                        tr.addView(tagtxt);


                        System.out.println("value of wbilltxt in pickup update" + wbilltxt);
                        System.out.println("value of paytxt in pickup update" + paytxt);
                        System.out.println("value of amounttxt in pickup update" + amounttxt.getText());
                        System.out.print(amount);
                        System.out.println("value of servicetxt in pickup update" + servicetxt);

                        System.out.println("value of tagtext in pickup update" + tagtxt.getText().toString());
                        countN = resulttab.getChildCount() + 1;
                        System.out.println("value of counttxt in " + countN);
                        counttxt.setText(String.valueOf(countN));
                        System.out.println("value of counttxt in pickup update" + counttxt.getText().toString());

                        if (resulttab.getChildCount() == 0) {
                            resulttab.addView(tr, new TableLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
                        } else {
                            //	counttxt.setText(String.valueOf(resulttab.getChildCount()));
                            //	System.out.println("value of counttxt in pickup update"+counttxt.toString());

                            String wb;
                            for (int i = 0; i < resulttab.getChildCount(); i++) {

                                TextView wbill = (TextView) ((TableRow) resulttab.getChildAt(i)).getChildAt(1);
                                wb = wbill.getText().toString();
                                System.out.println("value of wb in wb update" + wb);

                                if (!Taskwabill.equals(wb)) {
                                    flag = true;

                                } else if (Taskwabill.equals(wb)) {
                                    flag = false;
                                    System.out.println(flag);

                                    break;

                                }
                            }
                            if (flag) {
                                resulttab.addView(tr, new TableLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
                            } else {
                                System.out.println("value of counttxt in else" + countN);
                                counttxt.setText(String.valueOf(countN - 1));
                                Toast.makeText(getApplicationContext(), "Duplicate WayBill not allowed,Please Scan again", Toast.LENGTH_LONG).show();


                            }

                        }
                    } else if (!cbmps.isChecked()) {
                        System.out.println("mastr unc:" + tagtxt.getText().toString() + "waybill:+" + Taskwabill);

												/*if(mastrawbstrng.equals(waybill)){
													Toast.makeText(getApplicationContext(), "MasterAWB cannot be added",
															Toast.LENGTH_LONG).show();
													return ;
												}else
*/
                        tr.addView(deltxt);
                        tr.addView(wbilltxt);
                        tr.addView(paytxt);
                        tr.addView(amounttxt);
                        tr.addView(servicetxt);
                        tr.addView(tagtxt);


                        System.out.println("value of wbilltxt in pickup update" + wbilltxt);
                        System.out.println("value of paytxt in pickup update" + paytxt);
                        System.out.println("value of amounttxt in pickup update" + amounttxt.getText());
                        System.out.print(amount);
                        System.out.println("value of servicetxt in pickup update" + servicetxt);

                        System.out.println("value of tagtext in pickup update" + tagtxt.getText());
                        countN = resulttab.getChildCount() + 1;
                        System.out.println("value of counttxt in " + countN);
                        counttxt.setText(String.valueOf(countN));
                        System.out.println("value of counttxt in pickup update" + counttxt.getText().toString());

                        if (resulttab.getChildCount() == 0) {
                            resulttab.addView(tr, new TableLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
                        } else {
                            //	counttxt.setText(String.valueOf(resulttab.getChildCount()));
                            //	System.out.println("value of counttxt in pickup update"+counttxt.toString());

                            String wb;
                            for (int i = 0; i < resulttab.getChildCount(); i++) {

                                TextView wbill = (TextView) ((TableRow) resulttab.getChildAt(i)).getChildAt(1);
                                wb = wbill.getText().toString();
                                System.out.println("wbb s" + wb);
                                System.out.println("wb:" + wb + "Taskwabillwaybill:+" + Taskwabill);
                                if (!Taskwabill.equals(wb)) {
                                    flag = true;

                                } else if (Taskwabill.equals(wb)) {
                                    flag = false;
                                    System.out.println(flag);

                                    break;

                                }
                            }
                            System.out.println("value of flag in else" + flag);
                            if (flag) {
                                resulttab.addView(tr, new TableLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
                            } else {
                                System.out.println("value of counttxt in else" + countN);
                                counttxt.setText(String.valueOf(countN - 1));
                                Toast.makeText(getApplicationContext(), "Duplicate WayBill not allowed,Please Scan again", Toast.LENGTH_LONG).show();
                            }

                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Please select Service and PayType", Toast.LENGTH_LONG).show();

                    }

                    tr.setOnClickListener(new OnClickListener() {

                        @Override
                        public void onClick(View v) {

                            System.out.println("Rowid:" + rowid + ",v.getID:" + v.getId());
                            TextView wbill = (TextView) ((TableRow) resulttab.getChildAt(v.getId())).getChildAt(1);

                            String delwb = wbill.getText().toString();
                            System.out.println("delwb:" + delwb);
                            System.out.println("wayvill on del:" + delwb);
                            if (rowid != v.getId()) {
                                rowid = v.getId();
                                Toast.makeText(getApplicationContext(), "Press again to delete the row", Toast.LENGTH_LONG).show();

                            } else if (rowid == v.getId()) {

                                resulttab.removeViewAt(v.getId());
                                counttxt.setText(String.valueOf(resulttab.getChildCount()));
                                System.out.println("value of counttxt in pickup update delete" + counttxt.getText().toString());
                                waybill = delwb;

                                System.out.println("value of delwb is:" + delwb);
                                delwaybillpckp = WebService.SET_DELETE_WAYBILL_PICKUP(drivercode, pickupno, delwb);
                                Toast.makeText(getApplicationContext(), "Row deleted successfully", Toast.LENGTH_LONG).show();
                                rowid = -1;
                                int tabcount = resulttab.getChildCount();
                                for (int i = 0; i < tabcount; i++) {
                                    tr = (TableRow) resulttab.getChildAt(i);
                                    tr.setId(i);
                                    counttxt.setText(String.valueOf(resulttab.getChildCount()));
                                    System.out.println("value of counttxt in pickup update delete" + counttxt.getText().toString());
                                }
                            }

                        }
                    });
                }
                cc.close();
                db.close();
//&&Masterwabill==mastrawbstrng
                System.out.println("Taskwabill" + Taskwabill + "TaskMasterwabill" + masterawbtxt.getText().toString() + "mas" + masterawbtxt.getText().toString());
                if (Taskwabill == Scanrwabill || Taskwabill==waybillCam) {
                    Pickpflag = true;
                    Pb.setVisibility(View.INVISIBLE);
                    cbmps.setEnabled(true);

                }

            }
            //Pb.setVisibility(View.INVISIBLE);
        }
    }

    private CheckValidPickupWaybill checkwaybill(String waybill, String Masterwabill) {


        System.out.println("DRIVERCODE:" + drivercode);
        System.out.println("WAYBILL cam:" + waybill);
        CheckValidPickupWaybill chckvalidpickupwaybilresp_chkwbl = null;

        if (drivercode == null || drivercode.equals("") || waybill == null || waybill.equals("")) {
            this.runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    Toast.makeText(getApplicationContext(), "Try again! Required Values Blank",
                            Toast.LENGTH_LONG).show();
                }
            });
            return null;
        }

        flag = false;
        serviceType = servicespinner.getSelectedItem().toString();
        paytype = paytypespinner.getSelectedItem().toString();
        amount = amountedt.getText().toString();
        mastrawbstrng = masterawbtxt.getText().toString();
        //serviceId=String.valueOf(serviceIdarr[servicespinner.getSelectedItemPosition()]);
        //payId=String.valueOf(payIdarr[paytypespinner.getSelectedItemPosition()]);
        System.out.println("value of service type in pickup update");
        System.out.println(serviceType);
        System.out.println("value of paytype in pickup update");
        System.out.println(paytype);
        System.out.println("value of amount in pickup update");
        System.out.println(amount);
        System.out.println("value of mastrawbstrng in pickup update" + mastrawbstrng);


        //	if(payIde.equals("-1") && serviceIde.equals(-1))
        //&&paytypespinner.getSelectedItemPosition()!=0)


        //	if ((servicespinner.getSelectedItemPosition() != 0 && Double.parseDouble(amount) == 0) || (servicespinner.getSelectedItemPosition() != 0 && paytypespinner.getSelectedItemPosition() != 0 && Double.parseDouble(amount) > 0.0)) {
        try {
/*
                if(tagmps==null){
                    tagmps="";
                }else{
                    tagmps=mastrawbstrng;
                }*/
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MMM-dd HH:mm:ss");
            date_time = sdf.format(new Date());
            //if(chckvalidpickupwaybilresp_chkwbl == null) return null;
            chckvalidpickupwaybilresp_chkwbl = WebService.CHECK_VALIDPICKUPWAYBILL(drivercode, waybill, usercode = drivercode, Masterwabill, serviceIde, pickupno, payIde, amountedt.getText().toString(), date_time);
            System.out.println("chckvalsp" + chckvalidpickupwaybilresp_chkwbl);
            // if(chckvalidpickupwaybilresp_chkwbl == null) return null;

            Log.e("PICKIPDT", "CHKWAYBILL Waybill " + chckvalidpickupwaybilresp_chkwbl.WayBill);
            Log.e("PICKIPDT", "CHKWAYBILL Waybill " + chckvalidpickupwaybilresp_chkwbl.AWBIdentifier);

            if (chckvalidpickupwaybilresp_chkwbl.ErrMsg == null || chckvalidpickupwaybilresp_chkwbl.ErrMsg == "") {

                waybill1 = chckvalidpickupwaybilresp_chkwbl.WayBill;
                rname1 = chckvalidpickupwaybilresp_chkwbl.RouteName;
                cname1 = chckvalidpickupwaybilresp_chkwbl.ConsignName;
                phone = chckvalidpickupwaybilresp_chkwbl.PhoneNo;
                area1 = chckvalidpickupwaybilresp_chkwbl.Area;
                company1 = chckvalidpickupwaybilresp_chkwbl.Company;
                civilid1 = chckvalidpickupwaybilresp_chkwbl.CivilId;
                serial1 = chckvalidpickupwaybilresp_chkwbl.Serial;
                cardtype1 = chckvalidpickupwaybilresp_chkwbl.CardType;
                deldate1 = chckvalidpickupwaybilresp_chkwbl.DelDate;
                deltime1 = chckvalidpickupwaybilresp_chkwbl.DelTime;
                amount1 = chckvalidpickupwaybilresp_chkwbl.Amount;
                adress1 = chckvalidpickupwaybilresp_chkwbl.Address;
                awbidetnfr1 = chckvalidpickupwaybilresp_chkwbl.AWBIdentifier;
                ShiperName = chckvalidpickupwaybilresp_chkwbl.ShipperName;
                attempt1 = chckvalidpickupwaybilresp_chkwbl.Attempt;
                error1 = chckvalidpickupwaybilresp_chkwbl.ErrMsg;
                laststa1 = chckvalidpickupwaybilresp_chkwbl.Last_Status;


            }
           /* Scannedwaybillarry.add(waybill);
            scanawbArray = new String[ Scannedwaybillarry.size() ];
            Scannedwaybillarry.toArray( scanawbArray );
            System.out.println("Scannedwaybillarry is"+Scannedwaybillarry+"scanawbArray arae:"+scanawbArray.toString());*/

        } catch (Exception e) {
        }

        return chckvalidpickupwaybilresp_chkwbl;
    }

    public void getpckpholdwaybill() {
        System.out.println("pckp called is:");
        try {
            pckpholdwaybillresp = WebService.GET_HOLDWAYBILLS_PICKUP(drivercode, pickupno);
            System.out.println("pckpholdwaybillresp is:" + pckpholdwaybillresp.length);
            if (pckpholdwaybillresp != null) {
                System.out.println("pickphldwsizeaybil  is:" + pckpholdwaybillresp[i].WAYBILL);
                for (PickupHoldwaybills pckphldwabOb : pckpholdwaybillresp) {
                    pickphldwaybil = new PickupHoldwaybills();
                    System.out.println("Inside loop is");
                    pickphldwaybil.TAGMTS = pckphldwabOb.TAGMTS;
                    pickphldwaybil.USERCODE = pckphldwabOb.USERCODE;
                    pickphldwaybil.PICKUPNO = pckphldwabOb.PICKUPNO;
                    pickphldwaybil.PAYTYPE = pckphldwabOb.PAYTYPE;
                    pickphldwaybil.STATUS = pckphldwabOb.STATUS;
                    pickphldwaybil.SERVICE = pckphldwabOb.SERVICE;
                    pickphldwaybil.DATETIMESTR = pckphldwabOb.DATETIMESTR;
                    pickphldwaybil.WAYBILL = pckphldwabOb.WAYBILL;
                    pickphldwaybil.AMOUNT = pckphldwabOb.AMOUNT;

                    Pickphldwayblarr.add(pickphldwaybil);
                }
                System.out.println("pckphldwabOb.WAYBILL:" + Pickphldwayblarr.get(i).WAYBILL + "pckp lenth" + Pickphldwayblarr.size());


            }
            addView();
        } catch (Exception e) {

        }
    }

    public void addView() {
        resulttab.removeAllViews();
        counttxt.setText(String.valueOf(Pickphldwayblarr.size()));
        for (int i = 0; i < Pickphldwayblarr.size(); i++) {

            tr = new TableRow(PickupUpdateActivity.this);

            lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
            tr.setId(resulttab.getChildCount());
            lp.setMargins(0, 20, 100, 0);
            tr.setLayoutParams(lp);
		/*	if (Build.MODEL.contains("SM-N")) {
				System.out.println("called smn barcodedatarecieved");

				lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
				tr.setId(resulttab.getChildCount());

				lp.setMargins(55, 2, 95, 2);
				tr.setLayoutParams(lp);


			} else {


				lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
				tr.setId(resulttab.getChildCount());
				lp.setMargins(0, 20, 100, 0);
				tr.setLayoutParams(lp);
			}*/
            System.out.println("pckparra size:" + Pickphldwayblarr.size());
            deltxt = new TextView(PickupUpdateActivity.this);
            deltxt.setLayoutParams(lp);
            deltxt.setText("Delete");

            wbilltxt = new TextView(PickupUpdateActivity.this);
            wbilltxt.setLayoutParams(lp);
            wbilltxt.setText(Pickphldwayblarr.get(i).WAYBILL);


            paytxt = new TextView(PickupUpdateActivity.this);
            paytxt.setLayoutParams(lp);
            paytxt.setText(Pickphldwayblarr.get(i).PAYTYPE);

            amounttxt = new TextView(PickupUpdateActivity.this);
            amounttxt.setLayoutParams(lp);
            amounttxt.setText(Pickphldwayblarr.get(i).AMOUNT);

            servicetxt = new TextView(PickupUpdateActivity.this);
            servicetxt.setLayoutParams(lp);
            servicetxt.setText(Pickphldwayblarr.get(i).SERVICE);
            System.out.println("amount frm funct is:" + Pickphldwayblarr.get(i).AMOUNT);
		/*payidtxt = new TextView(PickupUpdateActivity.this);
		//	payidtxt.setLayoutParams(lp);
		payidtxt.setText(Pickphldwayblarr.get(i).PAYTYPE);
		System.out.println("payid txt is" + payIde);*/

		/*serviceidtxt = new TextView(PickupUpdateActivity.this);
		//	serviceidtxt.setLayoutParams(lp);
		serviceidtxt.setText(serviceId);
		System.out.println("serviceid txt is" + serviceIde);*/

// tag added for mps
            tagtxt = new TextView(PickupUpdateActivity.this);
            tagtxt.setLayoutParams(lp);
            //tagtxt.setText(tagvalue);
            //tagtxt.setText(String.valueOf(tagvalue));
            System.out.println("mastrawbstrng tab txt is" + tagtxt);
            tagtxt.setText(Pickphldwayblarr.get(i).TAGMTS);

            tr.addView(deltxt);
            tr.addView(wbilltxt);
            tr.addView(paytxt);
            tr.addView(amounttxt);
            tr.addView(servicetxt);
            tr.addView(tagtxt);

            tr.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {

                    System.out.println("Rowid:" + rowid + "v.getID:" + v.getId());
                    TextView wbill = (TextView) ((TableRow) resulttab.getChildAt(v.getId())).getChildAt(1);

                    String delwb = wbill.getText().toString();
                    System.out.println("delwb:" + delwb);
                    System.out.println("wayvill on del:" + delwb);
                    if (rowid != v.getId()) {
                        rowid = v.getId();
                        Toast.makeText(getApplicationContext(), "Press again to delete the row", Toast.LENGTH_LONG).show();

                    } else if (rowid == v.getId()) {

                        resulttab.removeViewAt(v.getId());
                        counttxt.setText(String.valueOf(resulttab.getChildCount()));
                        System.out.println("value of counttxt in pickup update delete" + counttxt.getText().toString());
                        waybill = delwb;

                        System.out.println("value of delwb is:" + delwb);
                        delwaybillpckp = WebService.SET_DELETE_WAYBILL_PICKUP(drivercode, pickupno, delwb);
                        Toast.makeText(getApplicationContext(), "Row deleted successfully", Toast.LENGTH_LONG).show();
                        rowid = -1;
                        int tabcount = resulttab.getChildCount();
                        for (int i = 0; i < tabcount; i++) {
                            tr = (TableRow) resulttab.getChildAt(i);
                            tr.setId(i);
                            counttxt.setText(String.valueOf(resulttab.getChildCount()));
                            System.out.println("value of counttxt in pickup update delete" + counttxt.getText().toString());
                        }
                    }

                }
            });
            resulttab.addView(tr, new TableLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        }


    }

    public boolean validationcheck() {
        Log.e("Spinner Position", " " + String.valueOf(servicespinner.getSelectedItemPosition()));

        if (servicespinner.getSelectedItemPosition() == 0) {
            _activity.runOnUiThread(new Runnable() {
                public void run() {
                    Toast.makeText(_activity, "Please Select Service Type", Toast.LENGTH_LONG).show();
                }
            });
            return false;

        } else if (amountedt.getText().toString().contains("0.000") && !payIde.contains("NA")) {
            _activity.runOnUiThread(new Runnable() {
                public void run() {
                    Toast.makeText(_activity, "Please enter Amount", Toast.LENGTH_LONG).show();
                }
            });
            return false;
        }
        Log.e("val", "/*/*");
        return true;
    }



   /* @Override
    public void onBackPressed() {
        System.out.println("frag:"+getFragmentManager().getBackStackEntryCount());
        super.onBackPressed();
        barcodeReader.removeBarcodeListener(_activity);
        PickupUpdateActivity.this.finish();

    }*/

    @Override
    public void onPause() {
        super.onPause();
        if(barcodeReader!=null){
        barcodeReader.removeBarcodeListener(_activity);
        barcodeReader.release();
        _activity.finish();
    }

    }


    public void openDialog() {
        final Dialog dialog = new Dialog(PickupUpdateActivity.this); // Context, this, etc.
        dialog.setContentView(R.layout.dialog);
        dialog.setTitle("Select your choice");
        dialog.setCanceledOnTouchOutside(false);
        //  delvryflag=true;

        final Button Pickupaction = (Button) dialog.findViewById(R.id.pckupaction);
        final Button Deliveryaction = (Button) dialog.findViewById(R.id.deliveryaction);

        // scannningtable =(TableLayout)dialog.findViewById(R.id.scannningtable);
        resulttabledialog =(TableLayout)dialog.findViewById(R.id.resulttabledialog);
        textchoose =(TextView)dialog.findViewById(R.id.textchoose);
        textalert=(TextView)dialog.findViewById(R.id.textalert);
        final Button confirmaction=(Button)dialog.findViewById(R.id.confirmaction);
        final Button btncancel =(Button)dialog.findViewById(R.id.btncancel);
        //  scannningtable.setVisibility(View.GONE);

        dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey (DialogInterface dialog, int keyCode, KeyEvent event) {
                int action = event.getAction();
                int keyCodes = event.getKeyCode();
                System.out.println("keycode"+keyCodes+"keyevent"+action);

                switch (keyCodes) {
                    case KeyEvent.KEYCODE_VOLUME_UP:

                        if (action == KeyEvent.ACTION_DOWN) {
                            DonotInterruptKDCScan = true;
                            delvryflag=true;
                            Intent intent = new Intent("com.google.zxing.client.android.SCAN");
                            intent.putExtra("SCAN_MODE", "SCAN_MODE");
                            startActivityForResult(intent, SCANNER_REQUEST_CODE);
                            Flagcam = 1;

                        }

                        return true;
                    case KeyEvent.KEYCODE_VOLUME_DOWN:

                        if (action == KeyEvent.ACTION_DOWN) {
                            DonotInterruptKDCScan = true;
                            delvryflag=true;
                            Intent intent = new Intent("com.google.zxing.client.android.SCAN");
                            intent.putExtra("SCAN_MODE", "SCAN_MODE");
                            startActivityForResult(intent, SCANNER_REQUEST_CODE);
                            Flagcam = 1;
                        }

                        return true;
                    default:
                        return PickupUpdateActivity.super.dispatchKeyEvent(event);
                }

            }
        });

        Pickupaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Do your code here
                dialog.dismiss();
                delvryflag=false;
                if(_kdcReader!=null) _kdcReader.Disconnect();
                if(ThrKdc!=null)ThrKdc.interrupt();
               // KDCTaskExecutable.cancel(true);
                Intent int1 = new Intent(PickupUpdateActivity.this,PickupActivity.class);
                int1.putExtra("routecode",route);
                int1.putExtra("routename",routen);

                //startActivity(new Intent(int1));
                // new code
                startActivity(new Intent(int1));
                _activity.finish();
            }
        });

        confirmaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Do your code
                if(resulttabledialog.getChildCount()==0){
                    Toast.makeText(getApplicationContext(),"Please Scan Awb!",
                            Toast.LENGTH_LONG).show();
                    return;
                }
                delvryflag=false;
                if(_kdcReader!=null) _kdcReader.Disconnect();
                if(ThrKdc!=null)ThrKdc.interrupt();
                //KDCTaskExecutable.cancel(true);
                dialog.dismiss();
                Intent int1 = new Intent(PickupUpdateActivity.this,StartDeliveryActivity.class);
                int1.putExtra("routecode",route);
                int1.putExtra("routename",routen);
                startActivity(int1);

            }
        });

        btncancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Do your code here
                dialog.dismiss();
                delvryflag=false;

                if(!isActivityActiveFlag)
                {
                   // Toast.makeText(getApplicationContext(), "Please wait for scanner to connect",
                       //     Toast.LENGTH_LONG).show();

                }
                else{

                    if(_kdcReader!=null) _kdcReader.Disconnect();
                    if(ThrKdc!=null)ThrKdc.interrupt();
                   // KDCTaskExecutable.cancel(true);


                }
                Intent int1 = new Intent(PickupUpdateActivity.this,HomeActivity.class);
                int1.putExtra("routecode",route);
                int1.putExtra("routename",routen);

                //startActivity(new Intent(int1));
                // new code
                startActivity(new Intent(int1));
            }
        });
        Deliveryaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Do your code here
                delvryflag=true;

                dialog.setTitle("Please confirm");

                confirmaction.setVisibility(View.VISIBLE);
                btncancel.setVisibility(View.VISIBLE);
                Pickupaction.setVisibility(View.GONE);
                Deliveryaction.setVisibility(View.GONE);
                textalert.setVisibility(View.VISIBLE);


            }
        });

        dialog.show();
    }

    public class UserNotifyTrack extends AsyncTask<Void, Void, String> {

        String Taskdialwabill="";
        public UserNotifyTrack(String TakWaybill) {
            super();
            Taskdialwabill = TakWaybill;
            System.out.println("Taskdialwabill"+Taskdialwabill);


        }
        //String response = "";
        public void onPreExecute() {

            // super.onPreExecute();

            trdialog = new TableRow(PickupUpdateActivity.this);

            if (Build.MODEL.contains("SM-N")) {

				/*	lp = new LayoutParams(420,LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);

					tr.setLayoutParams(lp);
					lp.setMargins(0, 10, 40, 0);*/
                lp = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT);
                lp.setMargins(18, 2, 95, 2);
                trdialog.setLayoutParams(lp);

            } else {
                lp = new TableRow.LayoutParams(150, TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT);

                trdialog.setLayoutParams(lp);
                lp.setMargins(0, 20, 10, 0);
            }
            System.out.println("trdialog async:" + trdialog);
        }

        @Override
        protected String doInBackground(Void... arg0) {
            // usernotytrack();
            return "";
        }

		/*@Override
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
							UserNotyTrackResp = WebService.CUSTOMER_NOTIFY_TRACK(wbillarr[i], drivercode);
							System.out.println("wbillarr[i] are:" + wbillarr[i]);
							c2.moveToNext();
							if (UserNotyTrackResp == null) {

								Toast.makeText(PickupUpdateActivity.this, "Please Try again!",
										Toast.LENGTH_LONG).show();
								return;
							}
							if (UserNotyTrackResp.contains("TRUE"))

							{
								waybilldialog = new TextView(PickupUpdateActivity.this);
								// waybilltxt.setLayoutParams(lp);
								waybilldialog.setGravity(Gravity.CENTER_HORIZONTAL);
								waybilldialog.setText(wbill);
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
					// System.out.println("UserNotyTrackResp 2:" + UserNotyTrackResp);


				}else{
					Toast.makeText(PickupUpdateActivity.this, "Not in your Runsheet!",
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
                System.out.println("resulttabledialog dialog:" + resulttabledialog.getChildCount()+"Taskdialwabill"+Taskdialwabill);
             /*   if(resulttabledialog.getChildCount()>0) {
					TextView wbillres = null;
					for (int k = 0; k < resulttabledialog.getChildCount(); k++) {
						wbillres = (TextView) ((TableRow) resulttabledialog.getChildAt(k)).getChildAt(0);
						System.out.println("wbress:" + wbillres.getText().toString());
						if (Taskdialwabill.equals(wbillres.getText().toString())) {
							Toast.makeText(PickupUpdateActivity.this, "No duplicate waybills allowed!",
									Toast.LENGTH_LONG).show();
							return;
						}
					}

				}*/
                Cursor c2 = sqldb.rawQuery("SELECT * FROM deliverydata WHERE Waybill='" + Taskdialwabill + "' ", null);
                count1 = c2.getCount();
                //System.out.println("stage");
                c2.moveToFirst();
                wbillarr = new String[count1];

                stopdelarr = new int[count1];

                System.out.println("wbillarr dialog delv:" + wbillarr.toString() + "c2.getCount()" + c2.getCount());

                if (c2.getCount() > 0) {


                    for (int j = 0; j < count1; j++) {
                        //System.out.println("stage3");
                        wbillarr[j] = c2.getString(c2.getColumnIndex("Waybill"));

                        stopdelarr[j] = c2.getInt(c2.getColumnIndex("StopDelivery"));
                        laststats = c2.getString(c2.getColumnIndex("WC_Status"));

                        ApprvalStatus = c2.getString(c2.getColumnIndex("ApprovalStatus"));
                        Attemptstatus = c2.getString(c2.getColumnIndex("Attempt_Status"));

                        System.out.println("approval status is:" + ApprvalStatus + "Stopdlv : " + stopdelarr[j] + " Attpmpt " + Attemptstatus + "laststats" + laststats);
                        if (laststats.equals("C")) laststats = "DELIVERED";
                        else if (laststats.equals("A")) laststats = "WC";


                        //System.out.println("i="+i+wbillarr[i]+" "+consr[i]+" "+arear[i]+" "+phoner[i]+" "+compnyr[i]+" "+civilidr[i]+" "+stopdelarr[i]);
                        if (ApprvalStatus == null) ApprvalStatus = "";
                        //if (stopdelarr[i] == 0 || (stopdelarr[i] == 1 && ApprvalStatus.equals("APPROVED") && Attemptstatus.equals("0"))) {

                        System.out.println("Taskdialwabill pick:" + Taskdialwabill + "wbarr" + wbillarr[j]);
                        if (stopdelarr[j] == 0 || (stopdelarr[j] == 1 && ApprvalStatus.equals("APPROVED") && Attemptstatus.equals("0"))) {
                      //  if (wbillarr[j] != null && !laststats.equals("DELIVERED")) {
                            System.out.println("Taskdialwabill pick:" + Taskdialwabill + "wbarr" + wbillarr[j]);
                            if(resulttabledialog.getChildCount()>0) {
                                TextView wbillres = null;
                                for (int k = 0; k < resulttabledialog.getChildCount(); k++) {
                                    wbillres = (TextView) ((TableRow) resulttabledialog.getChildAt(k)).getChildAt(0);
                                    System.out.println("wbress:" + wbillres.getText().toString());
                                    if (Taskdialwabill.equals(wbillres.getText().toString())) {
                                        Toast.makeText(PickupUpdateActivity.this, "No duplicate waybills allowed!",
                                                Toast.LENGTH_LONG).show();
                                        return;
                                    }
                                }

                            }
                            UserNotyTrackResp = WebService.CUSTOMER_NOTIFY_TRACK(wbillarr[j], drivercode);
                            System.out.println("wbillarr[j] are:" + wbillarr[j]);
                            c2.moveToNext();
                            if (UserNotyTrackResp == null) {

                                Toast.makeText(PickupUpdateActivity.this, "Please Try again!",
                                        Toast.LENGTH_LONG).show();
                                return;
                            }
                            if (UserNotyTrackResp.contains("TRUE")) {
                                waybilldialog = new TextView(PickupUpdateActivity.this);
                                // waybilltxt.setLayoutParams(lp);
                                waybilldialog.setGravity(Gravity.CENTER_HORIZONTAL);
                                waybilldialog.setText(Taskdialwabill);
                                System.out.println("wbill text is" + wbillarr[j]);


                                trdialog.addView(waybilldialog);


                                resulttabledialog.addView(trdialog, new TableLayout.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));


                            } else if (UserNotyTrackResp.contains("INVALIDVALUE")) {
                                Toast.makeText(_activity, UserNotyTrackResp, Toast.LENGTH_LONG).show();
                                return;
                            } else {
                                Toast.makeText(_activity, "Not in your Runsheet", Toast.LENGTH_LONG).show();
                                return;
                            }


                            db.close();

                        } else {

                            Toast.makeText(_activity, "Already Delivered", Toast.LENGTH_LONG).show();
                            return;

                        }


                    }

                    db.close();
                    c2.close();
                    db.close();


                } else {
                    Log.e("cdcv", "12");
                    Toast.makeText(PickupUpdateActivity.this, "Not in your Runsheet!",
                            Toast.LENGTH_LONG).show();
                    return;
                }


            }catch (Exception e){

            }
        }
    }
    public class checkreferencewaybill extends AsyncTask<Void, Void, String> {
        //String response = "";
        String Taskwabill = "";
        String TaskMasterwabill = "";
        JSONObject checkValidReferenceWaybillResp = null;

        public checkreferencewaybill(String TakWaybill, String Mastrwabill) {
            super();
            Taskwabill = TakWaybill;
            TaskMasterwabill = Mastrwabill;
            System.out.println("TaskMasterwabill onchckp" + TaskMasterwabill + "maaswreng" + masterawbtxt.getText().toString());

        }

        public void onPreExecute() {
            Pb.setVisibility(View.VISIBLE);
            Pickpflag = false;
            System.out.println("taskwaybill prexecute:" + Taskwabill);
            // super.onPreExecute();
            if (cbmps.isChecked()) {
                cbmps.setEnabled(false);
                cbmps.setChecked(true);
            }
             if(cbReference.isChecked()){
                cbReference.setEnabled(false);
                cbReference.setChecked(true);
            }
        }

        @Override
        protected String doInBackground(Void... arg0) {
            checkValidReferenceWaybillResp = checkreferencewaybill(Taskwabill, TaskMasterwabill);

            return "";
        }

        @Override
        public void onPostExecute(String res) {

            if (checkValidReferenceWaybillResp == null) {
                Pb.setVisibility(View.INVISIBLE);
                cbmps.setEnabled(true);
                cbReference.setEnabled(true);
                Toast.makeText(PickupUpdateActivity.this, "Please Try again!",
                        Toast.LENGTH_LONG).show();
                return;
            } else if (!ErrMesageRef.isEmpty()|| !ErrMesageRef.equals("")) {
                cbmps.setEnabled(true);
                cbReference.setEnabled(true);

                masterawbtxt.setText("");
                Toast.makeText(getApplicationContext(),ErrMesageRef,
                        Toast.LENGTH_LONG).show();
                Pb.setVisibility(View.INVISIBLE);
                return;
            }
            if (ErrMesageRef == null || ErrMesageRef.isEmpty()||ErrMesageRef == "")
            //	System.out.println("response : " + response.toString());
            {

                db = new DatabaseHandler(getBaseContext());
                //open localdatabase in a read mode
                sqldb = db.getReadableDatabase();
                Cursor cc = sqldb.rawQuery("SELECT Waybill FROM deliverydata WHERE Waybill='" + Taskwabill + "'", null);
                int wbcount = cc.getCount();
                System.out.println("wbcount " + wbcount);
                if (wbcount > 0) {
                    Log.e("reached ", "1231");
                    Toast.makeText(getApplicationContext(), "WayBill with Delivery", Toast.LENGTH_LONG).show();
                } else
                    System.out.println("mast onpost:" + TaskMasterwabill);
                //	masterawbtxt.setText(TaskMasterwabill);

                { 	/*if(cbmps.isChecked() ){
					cbmps.setEnabled(true);
				}*/


                    //Pb.setVisibility(View.VISIBLE);
                    tr = new TableRow(PickupUpdateActivity.this);
                    if (Build.MODEL.contains("SM-N")) {
                        System.out.println("called smn barcodedatarecieved");
                        //	lp = new LayoutParams(385,LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
                        lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
                        tr.setId(resulttab.getChildCount());
                        //lp.setMargins(0, 10, 30, 0);
                        lp.setMargins(55, 2, 95, 2);
                        tr.setLayoutParams(lp);


                    } else {
						/*lp = new LayoutParams(150, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
							tr.setId(resulttab.getChildCount());
							tr.setLayoutParams(lp);
							lp.setMargins(0, 20, 10, 0);*/

                        lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
                        tr.setId(resulttab.getChildCount());
                        lp.setMargins(0, 20, 100, 0);
                        tr.setLayoutParams(lp);
                    }


                    deltxt = new TextView(PickupUpdateActivity.this);
                    deltxt.setLayoutParams(lp);
                    deltxt.setText("Delete");

                    wbilltxt = new TextView(PickupUpdateActivity.this);
                    wbilltxt.setLayoutParams(lp);
                    wbilltxt.setText(Taskwabill);


                    paytxt = new TextView(PickupUpdateActivity.this);
                    paytxt.setLayoutParams(lp);
                    paytxt.setText(paytype);

                    amounttxt = new TextView(PickupUpdateActivity.this);
                    amounttxt.setLayoutParams(lp);
                    amounttxt.setText(amount);

                    servicetxt = new TextView(PickupUpdateActivity.this);
                    servicetxt.setLayoutParams(lp);
                    servicetxt.setText(serviceType);

                    payidtxt = new TextView(PickupUpdateActivity.this);
                    //	payidtxt.setLayoutParams(lp);
                    payidtxt.setText(payIde);
                    System.out.println("payid txt is" + payIde);

                    serviceidtxt = new TextView(PickupUpdateActivity.this);
                    //	serviceidtxt.setLayoutParams(lp);
                    serviceidtxt.setText(serviceId);
                    System.out.println("serviceid txt is" + serviceIde);


                    // tag added for mps
                    tagtxt = new TextView(PickupUpdateActivity.this);
                    tagtxt.setLayoutParams(lp);
                    //tagtxt.setText(String.valueOf(tagvalue));
                    System.out.println("tagtxt on chck txt is" + mastrawbstrng);
                    tagtxt.setText(TaskMasterwabill);

											/*if(cbmps.isChecked()){
												tagtxt.setText(mastrawbstrng);
											}else{
												tagtxt.setText("");
											}*/

                    System.out.println("value of mastrawbstrng tables" + mastrawbstrng);
// condition added for MPS
                    if (cbmps.isChecked() && masterawbtxt.getText().toString().equals("")) {

                        //masterawbtxt.setText(Taskwabill);
                        //	tagtxt.setText(Taskwabill);
                        tr.addView(deltxt);
                        tr.addView(wbilltxt);
                        tr.addView(paytxt);
                        tr.addView(amounttxt);
                        tr.addView(servicetxt);
                        tr.addView(tagtxt);


                        System.out.println("value of wbilltxt in pickup update" + wbilltxt);
                        System.out.println("value of paytxt in pickup update" + paytxt);
                        System.out.println("value of amounttxt in pickup update" + amounttxt.getText());
                        System.out.print(amount);
                        System.out.println("value of servicetxt in pickup update" + servicetxt);

                        System.out.println("value of tagtext in pickup update" + tagtxt.getText().toString());
                        countN = resulttab.getChildCount() + 1;
                        System.out.println("value of counttxt in " + countN);
                        counttxt.setText(String.valueOf(countN));
                        System.out.println("value of counttxt in pickup update" + counttxt.getText().toString());

                        if (resulttab.getChildCount() == 0) {
                            resulttab.addView(tr, new TableLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
                        } else {
                            //	counttxt.setText(String.valueOf(resulttab.getChildCount()));
                            //	System.out.println("value of counttxt in pickup update"+counttxt.toString());

                            String wb;
                            for (int i = 0; i < resulttab.getChildCount(); i++) {

                                TextView wbill = (TextView) ((TableRow) resulttab.getChildAt(i)).getChildAt(1);
                                wb = wbill.getText().toString();


                                if (!Taskwabill.equals(wb)) {
                                    flag = true;

                                } else if (Taskwabill.equals(wb)) {
                                    flag = false;
                                    System.out.println(flag);

                                    break;

                                }
                            }
                            if (flag) {
                                resulttab.addView(tr, new TableLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
                            } else {
                                System.out.println("value of counttxt in else" + countN);
                                counttxt.setText(String.valueOf(countN - 1));
                                Toast.makeText(getApplicationContext(), "Duplicate WayBill not allowed,Please Scan again", Toast.LENGTH_LONG).show();
                            }

                        }
                        //	masterawbtxt.setText(waybill);
                    } else if (cbmps.isChecked() && !masterawbtxt.getText().toString().equals("")) {
                        // Check_mawb();
//check for mawb
												/*if(masterawbtxt.getText().toString().equals(waybill)){
													Toast.makeText(getApplicationContext(), "MasterAWB cannot be scanned",
															Toast.LENGTH_LONG).show();
													return ;
												}else*/
                        tr.addView(deltxt);
                        tr.addView(wbilltxt);
                        tr.addView(paytxt);
                        tr.addView(amounttxt);
                        tr.addView(servicetxt);
                        tr.addView(tagtxt);


                        System.out.println("value of wbilltxt in pickup update" + wbilltxt);
                        System.out.println("value of paytxt in pickup update" + paytxt);
                        System.out.println("value of amounttxt in pickup update" + amounttxt.getText());
                        System.out.print(amount);
                        System.out.println("value of servicetxt in pickup update" + servicetxt);

                        System.out.println("value of tagtext in pickup update" + tagtxt.getText().toString());
                        countN = resulttab.getChildCount() + 1;
                        System.out.println("value of counttxt in " + countN);
                        counttxt.setText(String.valueOf(countN));
                        System.out.println("value of counttxt in pickup update" + counttxt.getText().toString());

                        if (resulttab.getChildCount() == 0) {
                            resulttab.addView(tr, new TableLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
                        } else {
                            //	counttxt.setText(String.valueOf(resulttab.getChildCount()));
                            //	System.out.println("value of counttxt in pickup update"+counttxt.toString());

                            String wb;
                            for (int i = 0; i < resulttab.getChildCount(); i++) {

                                TextView wbill = (TextView) ((TableRow) resulttab.getChildAt(i)).getChildAt(1);
                                wb = wbill.getText().toString();
                                System.out.println("value of wb in wb update" + wb);

                                if (!Taskwabill.equals(wb)) {
                                    flag = true;

                                } else if (Taskwabill.equals(wb)) {
                                    flag = false;
                                    System.out.println(flag);

                                    break;

                                }
                            }
                            if (flag) {
                                resulttab.addView(tr, new TableLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
                            } else {
                                System.out.println("value of counttxt in else" + countN);
                                counttxt.setText(String.valueOf(countN - 1));
                                Toast.makeText(getApplicationContext(), "Duplicate WayBill not allowed,Please Scan again", Toast.LENGTH_LONG).show();


                            }

                        }
                    } else if (!cbmps.isChecked()) {
                        System.out.println("mastr unc:" + tagtxt.getText().toString() + "waybill:+" + Taskwabill);

												/*if(mastrawbstrng.equals(waybill)){
													Toast.makeText(getApplicationContext(), "MasterAWB cannot be added",
															Toast.LENGTH_LONG).show();
													return ;
												}else
*/
                        tr.addView(deltxt);
                        tr.addView(wbilltxt);
                        tr.addView(paytxt);
                        tr.addView(amounttxt);
                        tr.addView(servicetxt);
                        tr.addView(tagtxt);


                        System.out.println("value of wbilltxt in pickup update" + wbilltxt);
                        System.out.println("value of paytxt in pickup update" + paytxt);
                        System.out.println("value of amounttxt in pickup update" + amounttxt.getText());
                        System.out.print(amount);
                        System.out.println("value of servicetxt in pickup update" + servicetxt);

                        System.out.println("value of tagtext in pickup update" + tagtxt.getText());
                        countN = resulttab.getChildCount() + 1;
                        System.out.println("value of counttxt in " + countN);
                        counttxt.setText(String.valueOf(countN));
                        System.out.println("value of counttxt in pickup update" + counttxt.getText().toString());

                        if (resulttab.getChildCount() == 0) {
                            resulttab.addView(tr, new TableLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
                        } else {
                            //	counttxt.setText(String.valueOf(resulttab.getChildCount()));
                            //	System.out.println("value of counttxt in pickup update"+counttxt.toString());

                            String wb;
                            for (int i = 0; i < resulttab.getChildCount(); i++) {

                                TextView wbill = (TextView) ((TableRow) resulttab.getChildAt(i)).getChildAt(1);
                                wb = wbill.getText().toString();
                                System.out.println("wbb s" + wb);
                                System.out.println("wb:" + wb + "Taskwabillwaybill:+" + Taskwabill);
                                if (!Taskwabill.equals(wb)) {
                                    flag = true;

                                } else if (Taskwabill.equals(wb)) {
                                    flag = false;
                                    System.out.println(flag);

                                    break;

                                }
                            }
                            System.out.println("value of flag in else" + flag);
                            if (flag) {
                                resulttab.addView(tr, new TableLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
                            } else {
                                System.out.println("value of counttxt in else" + countN);
                                counttxt.setText(String.valueOf(countN - 1));
                                Toast.makeText(getApplicationContext(), "Duplicate WayBill not allowed,Please Scan again", Toast.LENGTH_LONG).show();
                            }

                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Please select Service and PayType", Toast.LENGTH_LONG).show();

                    }

                    tr.setOnClickListener(new OnClickListener() {

                        @Override
                        public void onClick(View v) {

                            System.out.println("Rowid:" + rowid + ",v.getID:" + v.getId());
                            TextView wbill = (TextView) ((TableRow) resulttab.getChildAt(v.getId())).getChildAt(1);

                            String delwb = wbill.getText().toString();
                            System.out.println("delwb:" + delwb);
                            System.out.println("wayvill on del:" + delwb);
                            if (rowid != v.getId()) {
                                rowid = v.getId();
                                Toast.makeText(getApplicationContext(), "Press again to delete the row", Toast.LENGTH_LONG).show();

                            } else if (rowid == v.getId()) {

                                resulttab.removeViewAt(v.getId());
                                counttxt.setText(String.valueOf(resulttab.getChildCount()));
                                System.out.println("value of counttxt in pickup update delete" + counttxt.getText().toString());
                                waybill = delwb;

                                System.out.println("value of delwb is:" + delwb);
                                delwaybillpckp = WebService.SET_DELETE_WAYBILL_PICKUP(drivercode, pickupno, delwb);
                                Toast.makeText(getApplicationContext(), "Row deleted successfully", Toast.LENGTH_LONG).show();
                                rowid = -1;
                                int tabcount = resulttab.getChildCount();
                                for (int i = 0; i < tabcount; i++) {
                                    tr = (TableRow) resulttab.getChildAt(i);
                                    tr.setId(i);
                                    counttxt.setText(String.valueOf(resulttab.getChildCount()));
                                    System.out.println("value of counttxt in pickup update delete" + counttxt.getText().toString());
                                }
                            }

                        }
                    });
                }
                cc.close();
                db.close();
//&&Masterwabill==mastrawbstrng
                System.out.println("Taskwabill" + Taskwabill + "TaskMasterwabill" + masterawbtxt.getText().toString() + "mas" + masterawbtxt.getText().toString());
                if (Taskwabill == Scanrwabill || Taskwabill==waybillCam) {
                    Pickpflag = true;
                    Pb.setVisibility(View.INVISIBLE);
                    cbmps.setEnabled(true);
                    cbReference.setEnabled(true);

                }

            }
            //Pb.setVisibility(View.INVISIBLE);
        }
    }

    private JSONObject checkreferencewaybill(String waybill, String Masterwabill) {


        System.out.println("DRIVERCODE:" + drivercode);
        System.out.println("WAYBILL cam:" + waybill);
        JSONObject checkValidRefernceResp = null;

        if (drivercode == null || drivercode.equals("") || waybill == null || waybill.equals("")) {
            this.runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    Toast.makeText(getApplicationContext(), "Try again! Required Values Blank",
                            Toast.LENGTH_LONG).show();
                }
            });
            return null;
        }

        flag = false;
        serviceType = servicespinner.getSelectedItem().toString();
        paytype = paytypespinner.getSelectedItem().toString();
        amount = amountedt.getText().toString();
        mastrawbstrng = masterawbtxt.getText().toString();
        //serviceId=String.valueOf(serviceIdarr[servicespinner.getSelectedItemPosition()]);
        //payId=String.valueOf(payIdarr[paytypespinner.getSelectedItemPosition()]);
        System.out.println("value of service type in pickup update");
        System.out.println(serviceType);
        System.out.println("value of paytype in pickup update");
        System.out.println(paytype);
        System.out.println("value of amount in pickup update");
        System.out.println(amount);
        System.out.println("value of mastrawbstrng in pickup update" + mastrawbstrng);




        try {

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MMM-dd HH:mm:ss");
            date_time = sdf.format(new Date());
            //if(chckvalidpickupwaybilresp_chkwbl == null) return null;
            checkValidRefernceResp = WebService.Check_ValidPickupReference(drivercode, waybill, usercode = drivercode, Masterwabill, serviceIde, pickupno, payIde, amountedt.getText().toString(), date_time);
            System.out.println("checkValidRefernceResp are:" + checkValidRefernceResp);


            // error1= checkValidRefernceResp.getString("ErrMsg");


            ErrMesageRef = checkValidRefernceResp.getString("ErrMsg");




          /*  if (checkValidRefernceResp.ErrMsg == null || checkValidRefernceResp.ErrMsg == "") {

                waybill1 = checkValidRefernceResp.WayBill;
                rname1 = checkValidRefernceResp.RouteName;
                cname1 = checkValidRefernceResp.ConsignName;
                phone = checkValidRefernceResp.PhoneNo;
                area1 = checkValidRefernceResp.Area;
                company1 = checkValidRefernceResp.Company;
                civilid1 = checkValidRefernceResp.CivilId;
                serial1 = checkValidRefernceResp.Serial;
                cardtype1 = checkValidRefernceResp.CardType;
                deldate1 = checkValidRefernceResp.DelDate;
                deltime1 = checkValidRefernceResp.DelTime;
                amount1 = checkValidRefernceResp.Amount;
                adress1 = checkValidRefernceResp.Address;
                awbidetnfr1 = checkValidRefernceResp.AWBIdentifier;
                ShiperName = checkValidRefernceResp.ShipperName;
                attempt1 = checkValidRefernceResp.Attempt;
                error1 = checkValidRefernceResp.ErrMsg;
                laststa1 = checkValidRefernceResp.Last_Status;


            }*/


        } catch (Exception e) {
        }

        return checkValidRefernceResp;
    }
}