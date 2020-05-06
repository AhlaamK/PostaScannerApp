package com.postaplus.postascannerapp;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ProgressBar;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TableRow.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import DatabaseHandlers.DBFCS.TBLogin;
import DatabaseHandlers.DBFunctions;
import webservice.FuncClasses.CheckValidWaybill;
import webservice.FuncClasses.OpenRst;
import webservice.WebService;

import static com.postaplus.postascannerapp.HomeActivity.barcodeReader;

/*import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.ksoap2.transport.ServiceConnection;*/


//fragment to create runsheet
public class Delivery_wc_fragment extends Fragment {
    public static final String TAG = "WCTAG";
    static boolean errored = false;
    //SoapObject response;
    public String waybill, wayBillCam;
    public int SCANNER_REQUEST_CODE = 123;
    //Thread twc;
    /*Webservice Function to retrive the waybilllist*/
    public String chkdata = "";
    public FragmentActivity MYActivity;
    //EditText tvScanResults;
    View rootView;
    Button back, scan, wc, clear, selectall, deselectall;
    TableLayout resulttab;
    TableRow tr;
    LayoutParams lp;
    TextView rnametxt, cnametxt, amounttxt, phnetxt, counttxt;
    CheckBox waybilltxt;
    DatabaseHandler db;
    SQLiteDatabase sqldb;
    boolean netstatus, clearholdstatus;
    String drivercode, route, routen, runsheet, date, runsheetNo;
    ProgressBar Pb;
    SharedPreferences pref;
    List<String> list;
    String waybill1 = "", rname1 = "", cname1 = "", error1 = "", phone = "", area1 = "", company1 = "", civilid1 = "", serial1 = "", cardtype1 = "", deldate1 = "", deltime1 = "", amount1 = "", adress1 = "", ShiperName = "", WABILLIdentity = "";
    CheckValidWaybill chkvaldwblResponse;
    OpenRst OpnRstCodes = null;
    int Flagcam = 0;
    ActivityNotification actNoty = new ActivityNotification();


    public void ScannerWCExecutions() {
        // Handle successful scan
        //String contents = intent.getStringExtra("SCAN_RESULT");
        //tvScanResults.setText(contents);


        System.out.println("pdata new value in chkdata is ");
        System.out.println(chkdata);

        MYActivity = DeliveryActivity.WCActivity;
        rootView = DeliveryActivity.WCrootView;

        route = MYActivity.getIntent().getExtras().getString("routecode");
        routen = MYActivity.getIntent().getExtras().getString("routename");


        if (Flagcam == 1) {
            waybill = wayBillCam;
            System.out.println("wayBillCam new value in chkdata is " + waybill);
        } else {
            waybill = DeliveryActivity.WaybillFromScanner;
        }

        if (Pb == null) Pb = rootView.findViewById(R.id.progressBar1);
        if (resulttab == null) resulttab = rootView.findViewById(R.id.resulttable1);
        if (counttxt == null) counttxt = rootView.findViewById(R.id.textcount);
        System.out.println("resulttab wcis " + resulttab);
        db = new DatabaseHandler(MYActivity.getBaseContext());
        sqldb = db.getReadableDatabase();

        Cursor c21 = sqldb.rawQuery("SELECT * FROM deliverydata WHERE Waybill='" + waybill + "'", null);
        Cursor c1 = sqldb.rawQuery("SELECT Username FROM logindata WHERE Loginstatus=1", null);
        c1.moveToFirst();
        if (c1.getCount() > 0) {
            drivercode = c1.getString(c1.getColumnIndex("Username"));

        }
        c1.close();

        int count1 = c21.getCount();
        c21.close();
        db.close();

        if (count1 > 0) {
            MYActivity.runOnUiThread(() -> Toast.makeText(MYActivity, "Already Scanned",
                    Toast.LENGTH_LONG).show());
        } else {
            MYActivity.runOnUiThread(() -> new Task().execute());
        }

    }

    private void checkwaybill() {
        if (drivercode == null || drivercode.equals("") || waybill == null || waybill.equals("") || route == null || route.equals("")) {
            MYActivity.runOnUiThread(() -> Toast.makeText(MYActivity, "Try again! Required Values Blank",
                    Toast.LENGTH_LONG).show());
            return;
        }
        try {
            chkvaldwblResponse = WebService.CHECK_VALIDWAYBILL(drivercode, waybill, route);
            if (chkvaldwblResponse == null) return;
            if (chkvaldwblResponse.ErrMsg == null || chkvaldwblResponse.ErrMsg == "") {

                waybill1 = chkvaldwblResponse.WayBill;

                rname1 = chkvaldwblResponse.RouteName;
                cname1 = chkvaldwblResponse.ConsignName;
                phone = chkvaldwblResponse.PhoneNo;
                area1 = chkvaldwblResponse.Area;
                company1 = chkvaldwblResponse.Company;
                civilid1 = chkvaldwblResponse.CivilId;
                serial1 = chkvaldwblResponse.Serial;
                cardtype1 = chkvaldwblResponse.CardType;
                deldate1 = chkvaldwblResponse.DelDate;
                deltime1 = chkvaldwblResponse.DelTime;
                amount1 = chkvaldwblResponse.Amount;
                //	error1=  chkvaldwblResponse.ErrMsg;
                adress1 = chkvaldwblResponse.Address;
                WABILLIdentity = chkvaldwblResponse.WAYBILLIdentifier;
                ShiperName = chkvaldwblResponse.ShipperName;

                waybilltxt = new CheckBox(MYActivity);
                waybilltxt.setLayoutParams(lp);
                waybilltxt.setText(waybill1);

                rnametxt = new TextView(MYActivity);
                //rnametxt.setLayoutParams(lp);
                rnametxt.setPadding(0, 20, 87, 0);
                rnametxt.setText(rname1);


                cnametxt = new TextView(MYActivity);
                cnametxt.setPadding(0, 20, 87, 0);
                cnametxt.setText(cname1);

                amounttxt = new TextView(MYActivity);
                //amounttxt.setLayoutParams(lp);
                amounttxt.setPadding(52, 20, 87, 0);
                amounttxt.setText(amount1);

                phnetxt = new TextView(MYActivity);
                //phnetxt.setLayoutParams(lp);
                phnetxt.setPadding(0, 20, 87, 0);
                phnetxt.setText(phone);

                tr.addView(waybilltxt);
                tr.addView(rnametxt);
                tr.addView(cnametxt);
                tr.addView(amounttxt);
                tr.addView(phnetxt);


            }
            error1 = chkvaldwblResponse.ErrMsg;
        } catch (Exception e) {
            Log.e("Delivery-wc:", e.getMessage().toString());
            //	Toast.makeText(getActivity().getApplicationContext(), "WebService Error", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }


    }


//	@Override
//	public void onStop() {
    //super.onStop();
       /* _kdcReader.Disconnect();
        System.out.println("KDC Reader Disconnect From WC");*/
    //}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        System.out.println(" saved instance value is ");
        System.out.println(savedInstanceState);
        rootView = inflater.inflate(R.layout.activity_delivery_wc, container, false);

        resulttab = (TableLayout) rootView.findViewById(R.id.resulttable1);
        back = (Button) rootView.findViewById(R.id.btnbck);
        //	scan=(Button)rootView.findViewById(R.id.btnscan);
        selectall = (Button) rootView.findViewById(R.id.buttonselect);
        deselectall = (Button) rootView.findViewById(R.id.buttondeselect);
        //tvScanResults=(EditText) rootView.findViewById(R.id.abnoedttxt);
        clear = (Button) rootView.findViewById(R.id.buttoncancel);
        Pb = (ProgressBar) rootView.findViewById(R.id.progressBar1);
        wc = (Button) rootView.findViewById(R.id.buttonWC);
        counttxt = (TextView) rootView.findViewById(R.id.textcount);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

		 /*_activity =new De liveryActivity();
		    _fragment1 = this;
		    _TAFragment = new Delivery_ta_fragment();
		  
		//KDC Reader
	    //_kdcReader= new KDCReader(null, _fragment1, _fragment1, null, null, null, _fragment1, false);
	     Thread t = new Thread(){
		    	@Override
		    	 public void run(){
		    		_kdcReader= new KDCReader(null, _fragment1, _fragment1, null, null, null, _fragment1, false);
		    		_btDevice = _kdcReader.GetBluetoothDevice();
		    	}
		    };
		    t.start();
		    
		    
		    Log.w("KDCReader", "KDC Thread1 Started");
			System.out.println("KDC Thread2 Started");
			//Log.w("KDCReader Log", _activity.);
			System.out.println(_fragment1);*/

        pref = this.getActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        //saving route name and driver code to a variable
        route = getActivity().getIntent().getExtras().getString("routecode");
        routen = getActivity().getIntent().getExtras().getString("routename");
        runsheetNo = getActivity().getIntent().getExtras().getString("rstnumbr");
        db = new DatabaseHandler(getActivity().getBaseContext());
        //open localdatabase in a read mode
        sqldb = db.getReadableDatabase();
        //select  loginstatus and routecode on resume activity
        Cursor c1 = sqldb.rawQuery("SELECT Username FROM logindata WHERE Loginstatus=1", null);
        c1.moveToFirst();
        if (c1.getCount() > 0) {
            drivercode = c1.getString(c1.getColumnIndex("Username"));

        }


        c1.close();


        //select  loginstatus and routecode on resume activity
        Cursor c = sqldb.rawQuery("SELECT Runsheetcode FROM logindata WHERE Username='" + drivercode + "'", null);
        c.moveToFirst();
        if (c.getCount() > 0) {
            String runsheet1 = c.getString(c.getColumnIndex("Runsheetcode"));
            System.out.println("runsheet1 is:" + runsheet1);
            //doubt ask sir
            if (runsheet1 != null) {
                //if (runsheet1 == null) {
                if (runsheet1.contains("NA")) {
                    runsheet = c.getString(c.getColumnIndex("Runsheetcode"));

                    db = new DatabaseHandler(getActivity().getBaseContext());
                    //open localdatabase in a read mode
                    sqldb = db.getReadableDatabase();


                    //check if the scanned wbill is in local table
                    Cursor c21 = sqldb.rawQuery("SELECT * FROM deliverydata WHERE WC_Status='P'", null);
                    int count1 = c21.getCount();
                    String[] wbill21 = new String[count1];
                    String[] rname21 = new String[count1];
                    String[] cname21 = new String[count1];
                    String[] amount21 = new String[count1];
                    String[] phone21 = new String[count1];

                    c21.moveToFirst();

                    if (count1 > 0) {
                        for (int i = 0; i < count1; i++) {

                            wbill21[i] = c21.getString(c21.getColumnIndex("Waybill"));
                            rname21[i] = c21.getString(c21.getColumnIndex("Routecode"));
                            cname21[i] = c21.getString(c21.getColumnIndex("Consignee"));
                            amount21[i] = c21.getString(c21.getColumnIndex("Amount"));
                            phone21[i] = c21.getString(c21.getColumnIndex("Telephone"));
                            sqldb = db.getReadableDatabase();
                            Cursor cr = sqldb.rawQuery("SELECT ROUTENAME FROM routedata WHERE ROUTECODE='" + rname21[i] + "'", null);
                            int crcount = cr.getCount();
                            cr.moveToFirst();
                            String rnam = null;
                            if (crcount > 0) {
                                rnam = cr.getString(cr.getColumnIndex("ROUTENAME"));
                            }
                            cr.close();
                            //System.out.println("i="+i+waybill1[i]+" "+cname11[i]+" "+phone[i]+" "+area11[i]+" "+company1[i]+" "+civilid1[i]+" "+serial1[i]);

                            tr = new TableRow(getActivity());


                            if (Build.MODEL.contains("SM-N")) {
                                System.out.println("Called phone note5 in oncreateview");

                                lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
                                //	lp = new LayoutParams(LayoutParams.WRAP_CONTENT);

                                // tr.setId((resulttab.getChildCount() - 1));
                                lp.setMargins(15, 2, 65, 2);
                                tr.setLayoutParams(lp);


                            } else {
                                System.out.println("Called phone normal oncreateview");
                                lp = new LayoutParams(282, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);

                                tr.setLayoutParams(lp);

                                lp.setMargins(0, 20, 37, 0);

                            }

                            CheckBox waybilltxt1 = new CheckBox(getActivity());
                            waybilltxt1.setLayoutParams(lp);
                            waybilltxt1.setText(wbill21[i]);
                            //	waybilltxt1.setText("101010203");

                            rnametxt = new TextView(getActivity());
                            //rnametxt.setLayoutParams(lp);
                            rnametxt.setPadding(0, 20, 87, 0);
                            rnametxt.setText(rnam);
                            //rnametxt.setText("lijo");

                            cnametxt = new TextView(getActivity());
                            //cnametxt.setPadding(0,20,87,0);
                            cnametxt.setPadding(0, 20, 87, 0);
                            cnametxt.setText(cname21[i]);
                            //cnametxt.setText("joy");

                            amounttxt = new TextView(getActivity());
                            //amounttxt.setLayoutParams(lp);
                            amounttxt.setPadding(52, 20, 87, 0);
                            amounttxt.setText(amount21[i]);
                            //	amounttxt.setText("1234");

                            phnetxt = new TextView(getActivity());
                            //phnetxt.setLayoutParams(lp);
                            phnetxt.setPadding(0, 20, 87, 0);
                            phnetxt.setText(phone21[i]);
                            //	phnetxt.setText("12345678");

                            tr.addView(waybilltxt1);
                            tr.addView(rnametxt);
                            tr.addView(cnametxt);
                            tr.addView(amounttxt);
                            tr.addView(phnetxt);
                            resulttab.addView(tr, new TableLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
                            //counttxt.setText(String.valueOf(resulttab.getChildCount()));

                            c21.moveToNext();

                        }

                        c21.close();
                        db.close();
                        //_kdcReader.Disconnect();


                    }
                } else {
                    //scan.setEnabled(false);

                    wc.setText("Add\nRST");
                }
            } else {
                //scan.setEnabled(false);

                wc.setText("Add\nRST");
            }
        }
        c.close();
        db.close();
        System.out.println("count:" + resulttab.getChildCount());
        counttxt.setText(String.valueOf(resulttab.getChildCount()));

        selectall.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                v.startAnimation(AnimationUtils.loadAnimation(getActivity().getBaseContext(), R.anim.image_click));
                for (int i = 0; i < resulttab.getChildCount(); i++) {
                    waybilltxt = (CheckBox) ((TableRow) resulttab.getChildAt(i)).getChildAt(0);
                    waybilltxt.setChecked(true);
                }


            }
        });
        deselectall.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                v.startAnimation(AnimationUtils.loadAnimation(getActivity().getBaseContext(), R.anim.image_click));
                for (int i = 0; i < resulttab.getChildCount(); i++) {
                    waybilltxt = (CheckBox) ((TableRow) resulttab.getChildAt(i)).getChildAt(0);
                    waybilltxt.setChecked(false);
                }


            }
        });
        back.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                v.startAnimation(AnimationUtils.loadAnimation(getActivity().getBaseContext(), R.anim.image_click));
                DeliveryActivity.DonotInterruptKDCScan = false;
                //DeliveryActivity.scandisconFlag=1;
                // moveTaskToBack(true);
                getActivity().finish();
                Intent int1 = new Intent(getActivity(), HomeActivity.class);

                int1.putExtra("route", route);
                int1.putExtra("route1", routen);

                startActivity(new Intent(int1));

            }
        });
	/*	scan.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				v.startAnimation(AnimationUtils.loadAnimation(getActivity().getBaseContext(), R.anim.image_click));

				if (v.getId() == R.id.btnscan) {
					// go to fullscreen scan
					Intent intent = new Intent("com.google.zxing.client.android.SCAN");
					intent.putExtra("SCAN_MODE", "SCAN_MODE");
					startActivityForResult(intent, SCANNER_REQUEST_CODE);
				}


			}
		}); */

	/*	 clear.setOnClickListener(new OnClickListener() {

		        @Override
		        public void onClick(View v) {
		        	v.startAnimation(AnimationUtils.loadAnimation(getActivity().getBaseContext(), R.anim.image_click));

		        	clearholdstatus=WebService.clearholddelvr(drivercode,MasterActivity.METHOD_NAME37);
		        	db=new DatabaseHandler(getActivity().getBaseContext());
					//open localdatabase in a read mode
					sqldb = db.getReadableDatabase();


		        	if(clearholdstatus){

						Cursor c1 = sqldb.rawQuery("SELECT * FROM deliverydata WHERE Drivercode='"+drivercode+"'", null);
						int count1=c1.getCount();

						if(count1>0){
							sqldb.execSQL("DELETE FROM deliverydata WHERE  Drivercode='"+drivercode+"'");
						}
		        		c1.close();

		        		for (int i = count1; i >=0; i--) {
							resulttab.removeAllViews();

						}
		        		counttxt.setText(String.valueOf(resulttab.getChildCount()));
		        		Toast.makeText(getActivity().getApplicationContext(), "Cancel Scanned Waybills", Toast.LENGTH_LONG).show();
		        	}
		        	else{
		        		Toast.makeText(getActivity().getApplicationContext(), "Cancel Error", Toast.LENGTH_LONG).show();
		        	}
		        	db.close();
		        }
		    });

		*/
        //clear one by one waybill
        clear.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                v.startAnimation(AnimationUtils.loadAnimation(getActivity().getBaseContext(), R.anim.image_click));
                MYActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        db = new DatabaseHandler(getActivity().getBaseContext());
                        //open localdatabase in a read mode
                        sqldb = db.getReadableDatabase();

                        String[] wbill = new String[resulttab.getChildCount()];
                        int flag = 0;
                        for (int i = 0; i < resulttab.getChildCount(); i++) {
                            waybilltxt = (CheckBox) ((TableRow) resulttab.getChildAt(i)).getChildAt(0);
                            if (waybilltxt.isChecked()) {
                                wbill[i] = waybilltxt.getText().toString();


                                //clearholdstatus=WebService.clearholddelvronebyone(drivercode,wbill[i],MasterActivity.METHOD_NAME42);
                                //	clearholdstatus= webservice.WebService.SET_DELETE_HOLD(drivercode);
                                clearholdstatus = WebService.SET_DELETE_INITIAL_SCAN_SINGLE(drivercode, wbill[i]);

                                if (clearholdstatus) {

                                    Cursor c1 = sqldb.rawQuery("SELECT * FROM deliverydata WHERE Waybill='" + wbill[i] + "'", null);
                                    int count1 = c1.getCount();

                                    if (count1 > 0) {
                                        sqldb = db.getWritableDatabase();
                                        sqldb.execSQL("DELETE FROM deliverydata WHERE  Waybill='" + wbill[i] + "'");
                                    }
                                    c1.close();

		        		/*for (int i = count1; i >=0; i--) {
							resulttab.removeAllViews();

						}*/
                                    counttxt.setText(String.valueOf(resulttab.getChildCount()));
                                    flag = 1;

                                    //Toast.makeText(getActivity().getApplicationContext(), "Cancel Scanned Waybills", Toast.LENGTH_LONG).show();
                                } else {
                                    flag = 0;
                                    //Toast.makeText(getActivity().getApplicationContext(), "Cancel Error", Toast.LENGTH_LONG).show();
                                }
                            }
                        }
                        if (flag == 1) {
                            //after deletion clear the delected wbill from the table and load the rest wbill
                            for (int i = resulttab.getChildCount(); i >= 0; i--) {
                                resulttab.removeAllViews();

                            }
                            sqldb = db.getReadableDatabase();

                            Cursor c21 = sqldb.rawQuery("SELECT * FROM deliverydata WHERE WC_Status='P'", null);
                            int count1 = c21.getCount();
                            String[] wbill21 = new String[count1];
                            String[] rname21 = new String[count1];
                            String[] cname21 = new String[count1];
                            String[] amount21 = new String[count1];
                            String[] phone21 = new String[count1];

                            c21.moveToFirst();

                            if (count1 > 0) {
                                for (int i = 0; i < count1; i++) {

                                    wbill21[i] = c21.getString(c21.getColumnIndex("Waybill"));
                                    rname21[i] = c21.getString(c21.getColumnIndex("Routecode"));
                                    cname21[i] = c21.getString(c21.getColumnIndex("Consignee"));
                                    amount21[i] = c21.getString(c21.getColumnIndex("Amount"));
                                    phone21[i] = c21.getString(c21.getColumnIndex("Telephone"));

                                    Cursor cr = sqldb.rawQuery("SELECT ROUTENAME FROM routedata WHERE ROUTECODE='" + rname21[i] + "'", null);
                                    int crcount = cr.getCount();
                                    cr.moveToFirst();
                                    String rnam = null;
                                    if (crcount > 0) {
                                        rnam = cr.getString(cr.getColumnIndex("ROUTENAME"));
                                    }
                                    cr.close();
                                    //System.out.println("i="+i+waybill1[i]+" "+cname11[i]+" "+phone[i]+" "+area11[i]+" "+company1[i]+" "+civilid1[i]+" "+serial1[i]);

                                    tr = new TableRow(getActivity());

                                    if (Build.MODEL.contains("SM-N")) {


                                        lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
                                        //	lp = new LayoutParams(LayoutParams.WRAP_CONTENT);
                                        tr.setId((resulttab.getChildCount()));
                                        // tr.setId((resulttab.getChildCount() - 1));
                                        lp.setMargins(15, 2, 65, 2);
                                        tr.setLayoutParams(lp);
                                    } else {
                                        lp = new LayoutParams(282, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);

                                        tr.setLayoutParams(lp);
                                        //lp.setMargins(20, 20, 0, 0);
                                        lp.setMargins(0, 20, 37, 0);
                                    }


                                    CheckBox waybilltxt1 = new CheckBox(getActivity());
                                    waybilltxt1.setLayoutParams(lp);
                                    waybilltxt1.setText(wbill21[i]);
                                    //	waybilltxt1.setText("10002929");

                                    rnametxt = new TextView(getActivity());
                                    //rnametxt.setLayoutParams(lp);
                                    rnametxt.setPadding(0, 20, 87, 0);
                                    rnametxt.setText(rnam);
                                    //	rnametxt.setText("lijo");

                                    cnametxt = new TextView(getActivity());
                                    cnametxt.setPadding(0, 20, 87, 0);
                                    cnametxt.setText(cname21[i]);
                                    //cnametxt.setText("Joy");

                                    amounttxt = new TextView(getActivity());
                                    //amounttxt.setLayoutParams(lp);
                                    amounttxt.setPadding(52, 20, 87, 0);
                                    amounttxt.setText(amount21[i]);
                                    //	amounttxt.setText("18282");

                                    phnetxt = new TextView(getActivity());
                                    //phnetxt.setLayoutParams(lp);
                                    phnetxt.setPadding(0, 20, 87, 0);
                                    phnetxt.setText(phone21[i]);
                                    //	phnetxt.setText("123456");

                                    tr.addView(waybilltxt1);
                                    tr.addView(rnametxt);
                                    tr.addView(cnametxt);
                                    tr.addView(amounttxt);
                                    tr.addView(phnetxt);
                                    resulttab.addView(tr, new TableLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));


                                    c21.moveToNext();

                                }

                                c21.close();
                            }
                            counttxt.setText(String.valueOf(resulttab.getChildCount()));
                            Toast.makeText(getActivity().getApplicationContext(), "Cancelled Selected Waybills", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(getActivity().getApplicationContext(), "Cancel Error", Toast.LENGTH_LONG).show();
                        }
                        db.close();

                    }
                });
            }
        });
        wc.setOnClickListener(v -> {
            v.startAnimation(AnimationUtils.loadAnimation(getActivity().getBaseContext(), R.anim.image_click));
            DeliveryActivity.DonotInterruptKDCScan = false;
            //	DeliveryActivity.scandisconFlag=1;

            System.out.println("table count in wc button" + resulttab.getChildCount());
            if (resulttab.getChildCount() == 0) {
                Toast.makeText(MYActivity.getApplicationContext(), "Please scan AWB!",
                        Toast.LENGTH_LONG).show();
                return;
            }

            //runsheet=WebService.setWcrunsheet(drivercode,MasterActivity.METHOD_NAME28);
            //if(isNetworkConnected()){
            OpnRstCodes = WebService.SET_WC(drivercode);
            /*}else {
                Toast.makeText(MYActivity.getApplicationContext(),"You are in ofline mode, You need to connect to internet for creating RST!",
                        Toast.LENGTH_LONG).show();
                return;
            }*/
            //OpnRstCodes= WebService.SET_WC(drivercode);
            if (OpnRstCodes == null) {
                Toast.makeText(MYActivity.getApplicationContext(), "Please Try again!",
                        Toast.LENGTH_LONG).show();
                return;
            }

            //Added for TC Task
            if (OpnRstCodes.ERRMSG != null) {
                if (!OpnRstCodes.ERRMSG.toString().isEmpty()) {
                    if (!OpnRstCodes.ERRMSG.toString().toUpperCase().equals("TRUE")) {
                        Toast.makeText(MYActivity.getApplicationContext(), OpnRstCodes.ERRMSG,
                                Toast.LENGTH_LONG).show();
                        return;
                    }
                }
            }
            runsheet = OpnRstCodes.RSTNO;
            db = new DatabaseHandler(getActivity().getApplicationContext());
            //open localdatabase in a read mode
            sqldb = db.getReadableDatabase();

            //select all values in the table and check count
/*Cursor c = sqldb.rawQuery("SELECT * FROM logindata WHERE Username='"+drivercode+"'", null);
c.moveToFirst();*/
            TBLogin ActiveUser = DBFunctions.GetLoggedUser(getActivity().getBaseContext());

            if (!errored) {
                if (ActiveUser.LOGIN_STATUS == 1) {
                    sqldb = db.getWritableDatabase();
                    sqldb.execSQL("UPDATE logindata SET Runsheetcode='" + runsheet + "'WHERE Username='" + drivercode + "'");

  /*Cursor cc2 = sqldb.rawQuery("SELECT Odometervalue FROM logindata WHERE Username='"+drivercode+"'", null);
  cc2.moveToFirst();

  Integer count111 =  cc2.getCount();
  String odometervalue=null;
  if(count111>0)
  {
   odometervalue=cc2.getString(cc2.getColumnIndex("Odometervalue"));

  }
  cc2.close();*/
                    //if odometer value not saved then go to odometer page else got to startdelivery page
//Ak cmnt 23/05/18
                    /*Intent int1 = new Intent(getActivity(),StartDeliveryActivity.class);

                    int1.putExtra("routecode",route);
                    int1.putExtra("routename",routen);
                    //int1.putExtra("runsheetcode",runsheet1);
                    startActivity(new Intent(int1));*/

                    Cursor c2 = sqldb.rawQuery("SELECT * FROM deliverydata", null);
                    int count1 = c2.getCount();
                    c2.moveToFirst();
                    for (int i = 0; i < count1; i++) {
                        sqldb = db.getWritableDatabase();
                        sqldb.execSQL("UPDATE deliverydata SET WC_Status='A'");

                        c2.moveToNext();
                    }

                    db.close();
                    if (wc.getText().toString().contains("Add\nRST")) {
                        if (barcodeReader != null) {
                            barcodeReader.release();
                            //barcodeReader.close();
                            //barcodeReader.removeBarcodeListener(getActivity());
                        }
                        System.out.println("realeasr" + barcodeReader);
                        Intent i = new Intent(getActivity(), StartDeliveryActivity.class);
                        i.putExtra("routecode", route);
                        i.putExtra("routename", routen);
                        i.putExtra("dcode", drivercode);
                        startActivity(i);
                        //getActivity().finish();
                    } else {
                        //barcodeReader.release();
                        if (barcodeReader != null) {
                            barcodeReader.release();
                            //barcodeReader.removeBarcodeListener();
                        }
                        Intent i = new Intent(getActivity(), DialogActivity.class);
                        i.putExtra("routecode", route);
                        i.putExtra("routename", routen);
                        i.putExtra("dcode", drivercode);
                        startActivity(i);

                    }


  /* if(ActiveUser.ODOMETER_VALUE== null)
  {
   Intent int1 = new Intent(getActivity(),OdometerActivity.class);

   int1.putExtra("routecode",route);
   int1.putExtra("routename",routen);
   int1.putExtra("typeodo","START");

   startActivity(new Intent(int1));
  }

  else
  {
   Intent int1 = new Intent(getActivity(),StartDeliveryActivity.class);

   int1.putExtra("routecode",route);
   int1.putExtra("routename",routen);
   //int1.putExtra("runsheetcode",runsheet1);
   startActivity(new Intent(int1));
  }*/

                }
            }



        });
        DeliveryActivity.WCrootView = rootView;
        System.out.println("WC View Instance ");
        System.out.println(DeliveryActivity.WCrootView);

        DeliveryActivity.WCActivity = getActivity();
        System.out.println("WC Context Instance ");
        System.out.println(DeliveryActivity.WCActivity);
        return rootView;

    }


    @Override
    public void onResume() {
        super.onResume();


        DeliveryActivity.KDCScannerCallFrom = "WCFragment";
	    /*MasterActivity.KDCScannerCallFrom = "WCFragment";
	    System.out.println("WCFragment Block");
	    System.out.println(_fragment1);

	    _activity =new DeliveryActivity();
	    _fragment1 = this;*/

        MYActivity = DeliveryActivity.WCActivity;

        //_kdcReader.GetBarcodeDataReceivedListener();

	    /*if(_kdcReader != null)
	    	_kdcReader.Connect(_btDevice);
	      */

        //DeliveryActivity.scandisconFlag=1;


        if (Flagcam == 0) {
            resulttab.removeAllViews();
            db = new DatabaseHandler(getActivity().getBaseContext());
            //open localdatabase in a read mode
            sqldb = db.getReadableDatabase();


            //check if the scanned wbill is in local table
            Cursor c21 = sqldb.rawQuery("SELECT * FROM deliverydata WHERE WC_Status='P'", null);
            int count1 = c21.getCount();
            String[] wbill21 = new String[count1];
            String[] rname21 = new String[count1];
            String[] cname21 = new String[count1];
            String[] amount21 = new String[count1];
            String[] phone21 = new String[count1];

            c21.moveToFirst();

            if (count1 > 0) {
                for (int i = 0; i < count1; i++) {

                    wbill21[i] = c21.getString(c21.getColumnIndex("Waybill"));
                    rname21[i] = c21.getString(c21.getColumnIndex("Routecode"));
                    cname21[i] = c21.getString(c21.getColumnIndex("Consignee"));
                    amount21[i] = c21.getString(c21.getColumnIndex("Amount"));
                    phone21[i] = c21.getString(c21.getColumnIndex("Telephone"));

                    Cursor cr = sqldb.rawQuery("SELECT ROUTENAME FROM routedata WHERE ROUTECODE='" + rname21[i] + "'", null);
                    int crcount = cr.getCount();
                    cr.moveToFirst();
                    String rnam = null;
                    if (crcount > 0) {
                        rnam = cr.getString(cr.getColumnIndex("ROUTENAME"));
                    }
                    cr.close();
                    //System.out.println("i="+i+waybill1[i]+" "+cname11[i]+" "+phone[i]+" "+area11[i]+" "+company1[i]+" "+civilid1[i]+" "+serial1[i]);

                    tr = new TableRow(this.getActivity());

                    if (Build.MODEL.contains("SM-N")) {

                        System.out.println("Called phone note5 onresume");

                        lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
                        //	lp = new LayoutParams(LayoutParams.WRAP_CONTENT);

                        // tr.setId((resulttab.getChildCount() - 1));
                        lp.setMargins(15, 2, 65, 2);
                        tr.setLayoutParams(lp);


                    } else {
                        System.out.println("Called phone normal onresume");
                        lp = new LayoutParams(282, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);

                        tr.setLayoutParams(lp);
                        //lp.setMargins(20, 20, 0, 0);
                        lp.setMargins(0, 20, 37, 0);
                    }

                    CheckBox waybilltxt1 = new CheckBox(getActivity());
                    waybilltxt1.setLayoutParams(lp);
                    waybilltxt1.setText(wbill21[i]);
                    // waybilltxt1.setText("101010203");

                    rnametxt = new TextView(getActivity());
                    //rnametxt.setLayoutParams(lp);
                    rnametxt.setPadding(0, 20, 87, 0);
                    rnametxt.setText(rnam);
                    //rnametxt.setText("lijo");

                    cnametxt = new TextView(getActivity());
                    cnametxt.setPadding(0, 20, 87, 0);
                    cnametxt.setText(cname21[i]);
                    //cnametxt.setText("joy");

                    amounttxt = new TextView(getActivity());
                    //amounttxt.setLayoutParams(lp);
                    amounttxt.setPadding(52, 20, 87, 0);
                    amounttxt.setText(amount21[i]);
                    // amounttxt.setText("1234");

                    phnetxt = new TextView(getActivity());
                    //phnetxt.setLayoutParams(lp);
                    phnetxt.setPadding(0, 20, 87, 0);
                    phnetxt.setText(phone21[i]);
                    // phnetxt.setText("12345678");

                    tr.addView(waybilltxt1);
                    tr.addView(rnametxt);
                    tr.addView(cnametxt);
                    tr.addView(amounttxt);
                    tr.addView(phnetxt);
                    resulttab.addView(tr, new TableLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
                    counttxt.setText(String.valueOf(resulttab.getChildCount()));
                    System.out.println("tr us" + resulttab.getChildCount());
                    c21.moveToNext();

                }

                c21.close();
                db.close();
            }
        } else {
            System.out.println("resulttab value in resume and call from camera " + resulttab);
        }
        getView().setFocusableInTouchMode(true);
        getView().requestFocus();

        // Camera scanner calling through volume buton disabled
		/*getView().setOnKeyListener(new OnKeyListener() {
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {

				int action = event.getAction();
				keyCode = event.getKeyCode();
				switch (keyCode) {
					case KeyEvent.KEYCODE_VOLUME_UP:
						if (action == KeyEvent.ACTION_DOWN) {
							Intent intent = new Intent("com.google.zxing.client.android.SCAN");
							intent.putExtra("SCAN_MODE", "SCAN_MODE");
							Flagcam=1;
							startActivityForResult(intent, SCANNER_REQUEST_CODE);

						}
						return true;
					case KeyEvent.KEYCODE_VOLUME_DOWN:
						if (action == KeyEvent.ACTION_DOWN) {
							Intent intent = new Intent("com.google.zxing.client.android.SCAN");
							intent.putExtra("SCAN_MODE", "SCAN_MODE");
							Flagcam=1;
							startActivityForResult(intent, SCANNER_REQUEST_CODE);
						}
						return true;
					default:
						//return1 super.dispatchKeyEvent(event);
				}
				return false;
			}
		});*/
    }

    //Return scan result
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {

        if (requestCode == SCANNER_REQUEST_CODE) {
            // Handle scan intent

            if (resultCode == Activity.RESULT_OK) {
                // Handle successful scan
                String contents = intent.getStringExtra("SCAN_RESULT");
                //tvScanResults.setText(contents);

                wayBillCam = contents;

                System.out.println(" Value before calling db object ");
                System.out.println(db);

                db = new DatabaseHandler(getActivity().getBaseContext());
                //open localdatabase in a read mode
                sqldb = db.getReadableDatabase();


                //check if the scanned wbill is in local table
                Cursor c21 = sqldb.rawQuery("SELECT * FROM deliverydata WHERE Waybill='" + wayBillCam + "'", null);
                int count1 = c21.getCount();
                //String[] wbill21=new String[count1];
                //String[] rname21=new String[count1];
                //String[] cname21=new String[count1];
                //String[] amount21=new String[count1];
                if (count1 > 0) {

                    Toast.makeText(getActivity().getApplicationContext(), "Already Scanned",
                            Toast.LENGTH_LONG).show();
                    //tr.addView(waybilltxt);
                    //tr.addView(rnametxt);
                    //tr.addView(cnametxt);
                    //tr.addView(amounttxt);

                } else {
                    //new Task().execute();

                    ScannerWCExecutions();
				/*	MYActivity.runOnUiThread(new Runnable() {
						@Override
						public void run() {
							ScannerWCExecutions();
						}
					}); */
                }

            } else if (resultCode == Activity.RESULT_CANCELED) {
                // Handle cancel
            }
        } else {
            // Handle other intents
        }

    }
/*	 public static float convertDpToPixel(float dp, Context context){
		 Resources resources = context.getResources();
		 DisplayMetrics metrics = resources.getDisplayMetrics();
		 float px = dp * ((float)metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT); // You can cache "((float)metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT)" to avoid re-calculation.
		 return1 px;
	 }*/

    public void startASycncScanerExecute() {
        new StartAsyncTask_ScanWC().execute();
    }

    public boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        return ni != null;
    }

    public class Task extends AsyncTask<Void, Void, String> {

        //String response = "";
        public void onPreExecute() {
            Pb.setVisibility(View.VISIBLE);
            //super.onPreExecute();

            System.out.println("before table row create");
            tr = new TableRow(MYActivity);
            tr.removeAllViews();
            System.out.println("after table row create");


            if (Build.MODEL.contains("SM-N")) {
                System.out.println("Called phone note5 preexecute");


                lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
                //	lp = new LayoutParams(LayoutParams.WRAP_CONTENT);

                // tr.setId((resulttab.getChildCount() - 1));
                lp.setMargins(0, 10, 0, 0);
                tr.setLayoutParams(lp);

            } else {
                System.out.println("Called phone normal preexecute");

                lp = new LayoutParams(282, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
                tr.setLayoutParams(lp);
                lp.setMargins(0, 20, 37, 0);
            }
        }

        @Override
        protected String doInBackground(Void... arg0) {
            MYActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    checkwaybill();
                }
            });
            //checkwaybill();

            return "";
        }

        @Override
        public void onPostExecute(String res) {

            if (chkvaldwblResponse == null) {
                Pb.setVisibility(View.INVISIBLE);
                /*Toast.makeText(MYActivity.getApplicationContext(),"Please Try again!",
                        Toast.LENGTH_LONG).show();*/
                Toast.makeText(MYActivity.getApplicationContext(), "Please Try again or check your internet connection!",
                        Toast.LENGTH_LONG).show();
                return;
            }

            //	if(response.toString().contains("ConsignName")){
            Log.e("DeliveryWC", "ChkErr MSG : " + chkvaldwblResponse.ErrMsg);
            if (chkvaldwblResponse.ErrMsg == null || chkvaldwblResponse.ErrMsg == "") {
                Log.e("DeliveryWC", "Enter to Grid Block");

                resulttab.addView(tr, new TableLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
                System.out.println("resultabb is:" + resulttab.getChildCount());
                Log.e("DeliveryWC", "Resluttab error : ");

                counttxt.setText(String.valueOf(resulttab.getChildCount()));


                db = new DatabaseHandler(MYActivity.getBaseContext());
                //open localdatabase in a read mode

                sqldb = db.getWritableDatabase();
                sqldb.execSQL("DELETE FROM deliverydata WHERE Drivercode <> '" + drivercode + "'");
                sqldb = db.getReadableDatabase();
                //select all values in the table and check count
                Cursor c1 = sqldb.rawQuery("SELECT * FROM deliverydata WHERE Waybill='" + waybill1 + "'", null);
                int count1 = c1.getCount();

                if (count1 > 0) {

                    sqldb = db.getWritableDatabase();

                    Log.e("WC", "AWBIDENTIFIER before : " + WABILLIdentity);


                    sqldb.execSQL("UPDATE deliverydata SET Drivercode='" + drivercode + "',Routecode='" + route
                            + "',Consignee='" + cname1 + "',Telephone='" + phone + "'," + "Area='" + area1 + "'," + "Company='"
                            + company1 + "',CivilID='" + civilid1 + "',Serial='" + serial1 + "',CardType='" + cardtype1 + "',DeliveryDate='" + deldate1 + "',DeliveryTime='"
                            + deltime1 + "',Amount='" + amount1 + "',StopDelivery=0,WC_Status='P',WC_Transfer_Status=0,TransferStatus=0,Attempt_Status=0,Address='" + adress1 + "', "
                            + "ShipperName='" + ShiperName + "', AWBIdentifier='" + WABILLIdentity + "' "
                            + "WHERE Waybill='" + waybill1 + "'");


				/*	Log.e("Drivercode", drivercode);
					Log.e("Routecode",route);
					Log.e("Waybill", waybill1);
					Log.e("Consignee", cname1);
					Log.e("Telephone",phone);
					Log.e("Area", area1);
					Log.e("Company",company1);
					Log.e("CivilID",civilid1);
					Log.e("Serial", serial1);
					Log.e("CardType", cardtype1);
					Log.e("DeliveryDate",deldate1);
					Log.e("DeliveryTime", deltime1);
					Log.e("Amount",amount1);*/


                } else {
                    c1.moveToLast();

                    sqldb = db.getWritableDatabase();
                    ContentValues values = new ContentValues();
                    values.put("Drivercode", drivercode);
                    values.put("Routecode", route);
                    values.put("Waybill", waybill1);
                    values.put("Consignee", cname1);
                    values.put("Telephone", phone);
                    values.put("Area", area1);
                    values.put("Company", company1);
                    values.put("CivilID", civilid1);
                    values.put("Serial", serial1);
                    values.put("CardType", cardtype1);
                    values.put("DeliveryDate", deldate1);
                    values.put("DeliveryTime", deltime1);
                    values.put("Amount", amount1);
                    values.put("WC_Status", "P");
                    values.put("StopDelivery", "0");
                    values.put("WC_Transfer_Status", "0");
                    values.put("TransferStatus", "0");
                    values.put("Attempt_Status", "0");
                    values.put("Address", adress1);
                    values.put("ShipperName", ShiperName);
                    values.put("AWBIdentifier", WABILLIdentity);
                    values.put("ShipperName", ShiperName);
                    sqldb.insertOrThrow("deliverydata", null, values);

				/*	Log.e("Drivercode", drivercode);
					Log.e("Routecode",route);
					Log.e("Waybill", waybill1);
					Log.e("Consignee", cname1);
					Log.e("Telephone",phone);
					Log.e("Area", area1);
					Log.e("Company",company1);
					Log.e("CivilID",civilid1);
					Log.e("Serial", serial1);
					Log.e("CardType", cardtype1);
					Log.e("DeliveryDate",deldate1);
					Log.e("DeliveryTime", deltime1);
					Log.e("Amount",amount1);*/
                }
                c1.close();

                db.close();
            } else {
                //if(!error1.contains("anyType"))
                Toast.makeText(MYActivity.getApplicationContext(), error1,
                        Toast.LENGTH_LONG).show();
            }

            Pb.setVisibility(View.INVISIBLE);

        }
    }

    public class StartAsyncTask_ScanWC extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {

            ScannerWCExecutions();
            return null;
        }
    }

}

