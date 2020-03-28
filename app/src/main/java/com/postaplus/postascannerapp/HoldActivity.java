package com.postaplus.postascannerapp;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
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
import com.honeywell.aidc.ScannerNotClaimedException;
import com.honeywell.aidc.ScannerUnavailableException;
import com.honeywell.aidc.TriggerStateChangeEvent;

import org.apache.commons.lang3.StringUtils;

import java.util.List;

import koamtac.kdc.sdk.KDCConstants;
import koamtac.kdc.sdk.KDCData;
import koamtac.kdc.sdk.KDCReader;
import webservice.FuncClasses.CheckHoldvalidwaybill;
import webservice.WebService;

import static com.postaplus.postascannerapp.HomeActivity.barcodeReader;

/*

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.ksoap2.transport.ServiceConnection;
*/

public class HoldActivity extends MasterActivity implements
		BarcodeReader.BarcodeListener,BarcodeReader.TriggerListener {
	private Spinner courierlist;

	private String[] drivcode, drivname;
	ProgressBar Pb;
	boolean cancelholdstatus;
	private int count;
	String waybill1 = "", rname1 = "", cname1 = "", error1 = "", phone = "", area1 = "", company1 = "", civilid1 = "", serial1 = "", cardtype1 = "", deldate1 = "", deltime1 = "", amount1 = "", addresshold1 = "";
	TableLayout resulttab;
	TableRow tr;
	LayoutParams lp;
	String transcourier, wbill, dcode, rname, rcode;
	TextView username, statusmsg;
	//SoapObject response;
	Button back, scan, cancelhold;
	GridView grid;
	List<String> list;
	public int SCANNER_REQUEST_CODE = 123;
	TextView waybilltxt, rnametxt, cnametxt, amounttxt, phonetxt, counttxt;
	Thread ThrKdc;
	CheckHoldvalidwaybill chkhldvldwblResponse;
	//KDC Parameters
	//
	public static String WaybillFromScanner = "";
	public static String KDCScannerCallFrom = "";

	Resources _resources;
	BluetoothDevice _btDevice = null;
	static final byte[] TYPE_BT_OOB = "application/vnd.bluetooth.ep.oob".getBytes();
	Button _btnScan = null;

	//BluetoothDevice _btDevice;
	HoldActivity _activity;
	//KDCData ScannerData;
	KDCReader _kdcReader;
	public String chkdata = "";
	public String waybill;
	View rootView;
	public HoldActivity MYActivity;
	//KDCTask KDCTaskExecutable = new KDCTask();
	public boolean isActivityActiveFlag = false;


	public class KDCTask extends AsyncTask<Void, Void, String> {

		@Override
		protected String doInBackground(Void... arg0) {

			if (ThrKdc != null) {
				ThrKdc.run();
			} else {

				ThrKdc = new Thread() {
					@Override
					public void run() {
						//_kdcReader = new KDCReader(null, _activity, _activity, null, null, null, _activity, false);
						MasterActivity.ScannerDevice = _kdcReader.GetBluetoothDevice();
						//_kdcReader.EnableBluetoothAutoPowerOn(false);
						//_kdcReader.EnableAutoReconnect(false);
						//_kdcReader.EnableBluetoothWakeupNull(false);
						_kdcReader.EnableBluetoothWakeupNull(true);
						if (isCancelled()) ThrKdc.interrupt();
					}
				};
				ThrKdc.start();
				if (isCancelled()) ThrKdc.interrupt();
			}
			return "";
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			/*Intent intent = new Intent(Intent.ACTION_MAIN);
			intent.addCategory(Intent.CATEGORY_HOME);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(intent);*/
			HoldActivity.this.finish();
			Intent int1 = new Intent(HoldActivity.this, HomeActivity.class);

			int1.putExtra("route", rcode);
			int1.putExtra("route1", rname);

			startActivity(new Intent(int1));

			return true;
	/*		if (!isActivityActiveFlag) {
				Toast.makeText(getApplicationContext(), "Please wait for scanner to connect",
						Toast.LENGTH_LONG).show();
				return false;
			} else {
                *//*Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);*//*
				if (_kdcReader != null) _kdcReader.Disconnect();
				if (ThrKdc != null) ThrKdc.interrupt();
				//KDCTaskExecutable.cancel(true);
				HoldActivity.this.finish();
				Intent int1 = new Intent(HoldActivity.this, HomeActivity.class);

				int1.putExtra("route", rcode);
				int1.putExtra("route1", rname);

				startActivity(new Intent(int1));

				return true;
			}*/

		}
		return false;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_hold);
		System.out.println("HoldActivity Page");

		username = (TextView) findViewById(R.id.unametxt);
		pref = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
		username.setText(pref.getString("uname", ""));
		dcode = username.getText().toString();

		rname = getIntent().getExtras().getString("routename");
		rcode = getIntent().getExtras().getString("routecode");


		courierlist = (Spinner) findViewById(R.id.spinnercourier);


		resulttab = (TableLayout) findViewById(R.id.resulttable1);
		statusmsg = (TextView) findViewById(R.id.textView1);
		Pb = (ProgressBar) findViewById(R.id.progressBar1);

		back = (Button) findViewById(R.id.btnbck);
		//scan=(Button)findViewById(R.id.btnscan);
		cancelhold = (Button) findViewById(R.id.buttoncancelhold);
		counttxt = (TextView) findViewById(R.id.textcount1);
		//KDC Full Commands
		_activity = this;

		_resources = getResources();
		if(barcodeReader!=null){
			barcodeReader.addBarcodeListener(this);
			barcodeReader.release();
		}

	   
	   /*  ThrKdc = new Thread(){
		    	@Override
		    	 public void run(){
		    		_kdcReader= new KDCReader(null, _activity, _activity, null, null, null, _activity, false);
		    		_btDevice = _kdcReader.GetBluetoothDevice();
					_kdcReader.EnableBluetoothWakeupNull(true);
		    	}
		    };
		    ThrKdc.start();
		*/

		//KDCTaskExecutable.execute();
		System.out.println("HoldActivity KDCTask Executed");


		db = new DatabaseHandler(getBaseContext());
		//open localdatabase in a read mode
		sqldb = db.getReadableDatabase();

		//select all values from eventtable and populate it in the spinner 
		Cursor c1 = sqldb.rawQuery("SELECT '' AS DRIVERCODE,'' AS DRIVERNAME UNION SELECT DRIVERCODE,DRIVERNAME  FROM courierdetails WHERE DRIVERCODE<>'" + dcode + "' ORDER BY DRIVERNAME ASC", null);
		int count = c1.getCount();


		drivcode = new String[count];
		drivname = new String[count];

		c1.moveToFirst();

		for (int i = 0; i < count; i++) {

			drivcode[i] = c1.getString(c1.getColumnIndex("DRIVERCODE"));
			drivname[i] = c1.getString(c1.getColumnIndex("DRIVERNAME"));
			// System.out.println("i="+i+eventname[i]);
			c1.moveToNext();
		}

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(HoldActivity.this, android.R.layout.simple_spinner_item, drivname);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		courierlist.setAdapter(adapter);

		// closing connection
		c1.close();
		//db.close();


		//to display the select couriers scanned holdwbill 
		courierlist.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
				db = new DatabaseHandler(getBaseContext());
				//open localdatabase in a read mode
				sqldb = db.getReadableDatabase();

				transcourier = String.valueOf(drivcode[position]);
				if (transcourier.isEmpty()) {
					Toast.makeText(_activity, "Please select any courier!", Toast.LENGTH_SHORT).show();

					return;
				}
				//System.out.println(transcourier);
				Cursor c21 = sqldb.rawQuery("SELECT * FROM Holdwaybilldata WHERE Transdriver_Code='" + transcourier + "' AND HOLD_Transfer_Status=0", null);
				int count1 = c21.getCount();
				String[] wbill21 = new String[count1];
				String[] rname21 = new String[count1];
				String[] cname21 = new String[count1];
				String[] amount21 = new String[count1];
				String[] phone21 = new String[count1];

				c21.moveToFirst();

				for (int i = count1; i >= 0; i--) {
					resulttab.removeAllViews();

				}


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

						tr = new TableRow(HoldActivity.this);

						if (Build.MODEL.contains("SM-N")) {
 							
 							/*lp = new LayoutParams(385,LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
 							
 							tr.setLayoutParams(lp);
 							lp.setMargins(0, 10, 40, 0);*/
							lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
							lp.setMargins(18, 2, 95, 2);
							tr.setLayoutParams(lp);
						} else {
							lp = new LayoutParams(200, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);

							tr.setLayoutParams(lp);
							lp.setMargins(0, 20, 70, 0);
						}

						waybilltxt = new TextView(HoldActivity.this);
						waybilltxt.setLayoutParams(lp);
						waybilltxt.setText(wbill21[i]);
						System.out.println("Wbll text is" + wbill21);

						rnametxt = new TextView(HoldActivity.this);
						rnametxt.setLayoutParams(lp);
						rnametxt.setText(rnam);
						System.out.println("rname text is" + rnam);

						cnametxt = new TextView(HoldActivity.this);
						cnametxt.setLayoutParams(lp);
						cnametxt.setText(cname21[i]);
						System.out.println("cnamet text is" + cname21);

						amounttxt = new TextView(HoldActivity.this);
						amounttxt.setLayoutParams(lp);
						if(amount21[i].matches("-1")){
							amounttxt.setText("0.000");
						}else
						amounttxt.setText(amount21[i]);
						System.out.println("amountt text is" + amount21.toString());

						phonetxt = new TextView(HoldActivity.this);
						phonetxt.setLayoutParams(lp);
						phonetxt.setText(phone21[i]);
						System.out.println("phonet text is" + phone21);


						tr.addView(waybilltxt);
						tr.addView(rnametxt);
						tr.addView(cnametxt);
						tr.addView(amounttxt);
						tr.addView(phonetxt);

						resulttab.addView(tr, new TableLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
						System.out.println("value are" + waybilltxt + rnametxt + cnametxt + amounttxt + phonetxt);
						counttxt.setText(String.valueOf(resulttab.getChildCount()));

						c21.moveToNext();

					}

					c21.close();

				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parentView) {

			}

		});


		//db.close();


		counttxt.setText(String.valueOf(resulttab.getChildCount()));


		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				v.startAnimation(AnimationUtils.loadAnimation(getBaseContext(), R.anim.image_click));
				if(barcodeReader!=null){
					barcodeReader.removeBarcodeListener(HoldActivity.this);
					barcodeReader.release();
				}
				HoldActivity.this.finish();
                Intent int1 = new Intent(HoldActivity.this, HomeActivity.class);

                int1.putExtra("route", rcode);
                int1.putExtra("route1", rname);

                startActivity(new Intent(int1));
/*				if (!isActivityActiveFlag) {
					Toast.makeText(getApplicationContext(), "Please wait for scanner to connect",
							Toast.LENGTH_LONG).show();
					return;
				} else {
					// moveTaskToBack(true);
					HoldActivity.this.finish();
					Intent int1 = new Intent(HoldActivity.this, HomeActivity.class);

					int1.putExtra("route", rcode);
					int1.putExtra("route1", rname);

					startActivity(new Intent(int1));
				}*/


			}
		});
			   /*scan.setOnClickListener(new OnClickListener() {
			      	 
			        @Override
			        public void onClick(View v) {
			        	v.startAnimation(AnimationUtils.loadAnimation(getBaseContext(), R.anim.image_click));
			        	if(courierlist.getSelectedItemPosition()!=0)
			        	{
			        	if (v.getId() == R.id.btnscan) 
			        	{
			    			// go to fullscreen scan
			    			Intent intent = new Intent("com.google.zxing.client.android.SCAN");
			    			intent.putExtra("SCAN_MODE", "SCAN_MODE");
			    			startActivityForResult(intent, SCANNER_REQUEST_CODE);
			    		}
			           
			        	}
			        	else if(courierlist.getSelectedItemPosition()==0){
			        		Toast.makeText(getApplicationContext(), "Please Select Courier", Toast.LENGTH_SHORT).show();
			        	}
			        }
			    });*/

		cancelhold.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				v.startAnimation(AnimationUtils.loadAnimation(getBaseContext(), R.anim.image_click));
				transcourier = String.valueOf(drivcode[courierlist.getSelectedItemPosition()]);
				//cancelholdstatus= WebService.cancelhold(dcode,transcourier,METHOD_NAME17);
				cancelholdstatus = WebService.SET_CANCELATION_HOLD(dcode, transcourier);
				db = new DatabaseHandler(getBaseContext());
				//open localdatabase in a read mode
				sqldb = db.getReadableDatabase();


				if (cancelholdstatus) {

					Cursor c1 = sqldb.rawQuery("SELECT * FROM Holdwaybilldata WHERE Transdriver_Code='" + transcourier + "' AND Drivercode='" + dcode + "'", null);
					int count1 = c1.getCount();

					if (count1 > 0) {
						sqldb = db.getWritableDatabase();
						sqldb.execSQL("DELETE FROM Holdwaybilldata WHERE Transdriver_Code='" + transcourier + "'AND Drivercode='" + dcode + "'");
					}
					c1.close();
					for (int i = count1; i >= 0; i--) {
						resulttab.removeAllViews();

					}


					counttxt.setText(String.valueOf(resulttab.getChildCount()));
					Toast.makeText(getApplicationContext(), "Cancel Selected Hold Waybill", Toast.LENGTH_LONG).show();
				} else {
					Toast.makeText(getApplicationContext(), "Cancel Error", Toast.LENGTH_LONG).show();
				}
				//db.close();
			}
		});
		db.close();
	}


	@Override
	public void onResume() {  //will always? be called before app becomes visible?
		super.onResume();
		if (barcodeReader != null) {
			try {
				barcodeReader.claim();
				System.out.println("barcode onres:" + barcodeReader);
			} catch (ScannerUnavailableException e) {
				e.printStackTrace();
				Toast.makeText(this, "Scanner unavailable", Toast.LENGTH_SHORT).show();
			}
		}

	}
	/*@Override
	public void onResume()
	{
		super.onResume();
		if(!isActivityActiveFlag) isActivityActiveFlag=false;
		_activity = this;
		*//*thold = new Thread(){
			@Override
			public void run(){
				//_kdcReader= new KDCReader(MasterActivity.ScannerDevice, _activity, _activity, null, null, null, _activity, false);
				_kdcReader= new KDCReader(null, _activity, _activity, null, null, null, _activity, false);
			}
		};
		thold.start();*//*
		*//*if(ThrKdc!=null)
			ThrKdc.run();
		else{
			ThrKdc = new Thread(){
				@Override
				public void run(){
					//_kdcReader= new KDCReader(MasterActivity.ScannerDevice, _activity, _activity, null, null, null, _activity, false);
					_kdcReader= new KDCReader(null, _activity, _activity, null, null, null, _activity, false);
					_kdcReader.EnableBluetoothWakeupNull(true);
				}
			};
			ThrKdc.start();
		}*//*

		*//*if(!KDCTaskExecutable.getStatus().equals(AsyncTask.Status.RUNNING) && !KDCTaskExecutable.getStatus().equals(AsyncTask.Status.FINISHED)){
			//KDCTaskExecutable.cancel(true);
			KDCTaskExecutable.execute();
			System.out.println("HoldActivity KDCTask Executed");
		}*//*

		System.out.println("Resume activate in holdactivity");
	}*/

	// Camera Scanner via volume buttons disabled
/*	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		int action = event.getAction();
		int keyCode = event.getKeyCode();

		switch (keyCode) {
			case KeyEvent.KEYCODE_VOLUME_UP:
				if (courierlist.getSelectedItemPosition() != 0) {
					if (action == KeyEvent.ACTION_DOWN) {
						Intent intent = new Intent("com.google.zxing.client.android.SCAN");
						intent.putExtra("SCAN_MODE", "SCAN_MODE");
						startActivityForResult(intent, SCANNER_REQUEST_CODE);
					}
				} else if (courierlist.getSelectedItemPosition() == 0) {
					Toast.makeText(getApplicationContext(), "Please Select Courier", Toast.LENGTH_SHORT).show();
				}
				return true;
			case KeyEvent.KEYCODE_VOLUME_DOWN:
				if (courierlist.getSelectedItemPosition() != 0) {
					if (action == KeyEvent.ACTION_DOWN) {
						Intent intent = new Intent("com.google.zxing.client.android.SCAN");
						intent.putExtra("SCAN_MODE", "SCAN_MODE");
						startActivityForResult(intent, SCANNER_REQUEST_CODE);
					}
				} else if (courierlist.getSelectedItemPosition() == 0) {
					Toast.makeText(getApplicationContext(), "Please Select Courier", Toast.LENGTH_SHORT).show();
				}
				return true;
			default:
				return super.dispatchKeyEvent(event);
		}

	}*/

	//Return scan result
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {

		if (requestCode == SCANNER_REQUEST_CODE) {
			// Handle scan intent

			if (resultCode == Activity.RESULT_OK) {
				// Handle successful scan
				String contents = intent.getStringExtra("SCAN_RESULT");

				wbill = contents;
				transcourier = String.valueOf(drivcode[courierlist.getSelectedItemPosition()]);
				new checkholdwaybill().execute();


			} else if (resultCode == Activity.RESULT_CANCELED) {
				// Handle cancel
			}
		} else {
			// Handle other intents
		}

	}

	//check hol_waybill async
	public class checkholdwaybill extends AsyncTask<Void, Void, String> {
		//String response = "";
		public void onPreExecute() {
			Pb.setVisibility(View.VISIBLE);
			// super.onPreExecute();

			tr = new TableRow(HoldActivity.this);

			if (Build.MODEL.contains("SM-N")) {
					
				/*	lp = new LayoutParams(420,LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
					
					tr.setLayoutParams(lp);
					lp.setMargins(0, 10, 40, 0);*/
				lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
				lp.setMargins(18, 2, 95, 2);
				tr.setLayoutParams(lp);

			} else {
				lp = new LayoutParams(200,LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);

				tr.setLayoutParams(lp);
				//lp.setMargins(0, 20, 5, 0);
				lp.setMargins(0,20,70, 0);

			}
		}

		@Override
		protected String doInBackground(Void... arg0) {
			checkwaybill();
			return "";
		}

		private void checkwaybill() {
			// TODO Auto-generated method stub
			try {

				chkhldvldwblResponse = WebService.CHECK_HOLDVALIDWAYBILL(dcode, wbill, rcode, transcourier);
				if (chkhldvldwblResponse == null) return;
				//if error message is there print only the error message else print all data

				if (chkhldvldwblResponse.ErrMsg == null) {

					waybill1 = chkhldvldwblResponse.WayBill;

					rname1 = chkhldvldwblResponse.RouteName;
					cname1 = chkhldvldwblResponse.ConsignName;
					phone = chkhldvldwblResponse.PhoneNo;
					area1 = chkhldvldwblResponse.Area;
					company1 = chkhldvldwblResponse.Company;
					civilid1 = chkhldvldwblResponse.CivilId;
					serial1 = chkhldvldwblResponse.Serial;
					cardtype1 = chkhldvldwblResponse.CardType;
					deldate1 = chkhldvldwblResponse.DelDate;
					deltime1 = chkhldvldwblResponse.DelTime;
					amount1 = chkhldvldwblResponse.Amount;
					addresshold1 = chkhldvldwblResponse.Address;


					waybilltxt = new TextView(HoldActivity.this);
					waybilltxt.setLayoutParams(lp);
					waybilltxt.setText(waybill1);
					System.out.println("Waybill text is" + waybill1);

					rnametxt = new TextView(HoldActivity.this);
					rnametxt.setLayoutParams(lp);
					rnametxt.setText(rname1);
					System.out.println("rnametxt  is" + rname1);


					cnametxt = new TextView(HoldActivity.this);
					cnametxt.setLayoutParams(lp);
					cnametxt.setText(cname1);
					System.out.println("cnametxt  is" + cname1);

					amounttxt = new TextView(HoldActivity.this);
					amounttxt.setLayoutParams(lp);
					if(amount1.matches("-1")){
						amounttxt.setText("0.000");
					}else
						amounttxt.setText(amount1);
					System.out.println("amounttxt  is" + amount1);

					phonetxt = new TextView(HoldActivity.this);
					phonetxt.setLayoutParams(lp);
					phonetxt.setText(phone);
					System.out.println("phone  is" + phone);

					tr.addView(waybilltxt);
					tr.addView(rnametxt);
					tr.addView(cnametxt);
					tr.addView(amounttxt);
					tr.addView(phonetxt);

					//display the response in table

					//clear text msg
					//	statusmsg.setText(null);
					//save the response in database

				}

				error1 = chkhldvldwblResponse.ErrMsg;
			} catch (Exception e) {
				//Log.e("HoldActivity:", e.getMessage().toString());
				//Toast.makeText(getApplicationContext(), e.getMessage().toString(), Toast.LENGTH_LONG).show();
				e.printStackTrace();
			}


		}

		@Override
		public void onPostExecute(String res) {
			//response=null;
			if (chkhldvldwblResponse == null) {
			/*	Pb.setVisibility(View.INVISIBLE);
				Toast.makeText(MYActivity.getApplicationContext(), "Please Try again!",
						Toast.LENGTH_LONG).show();*/

				Toast.makeText(HoldActivity.this, "Please Try again!",
						Toast.LENGTH_LONG).show();
				return;
			}
			//	System.out.println("response : " + response.toString());
			if (chkhldvldwblResponse.ErrMsg == null)
			//	System.out.println("response : " + response.toString());
			{


				resulttab.addView(tr, new TableLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
				//resulttab.addView(tr,new TableLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
				counttxt.setText(String.valueOf(resulttab.getChildCount()));
				//amount1=response.getProperty(11).toString();
				//amounttxt.setText(amount1);


				db = new DatabaseHandler(getBaseContext());
				//open localdatabase in a read mode

				sqldb = db.getWritableDatabase();
				sqldb.execSQL("DELETE FROM Holdwaybilldata WHERE Drivercode <> '" + dcode + "'");
				//select all values in the table and check count
				sqldb = db.getReadableDatabase();
				Cursor c1 = sqldb.rawQuery("SELECT * FROM Holdwaybilldata WHERE Waybill='" + waybill1 + "'", null);
				int count1 = c1.getCount();

				if (count1 > 0) {

					//System.out.println(c1.getString(c1.getColumnIndex("Waybill")));
					System.out.println("update");
					//c1.moveToNext();

					sqldb = db.getWritableDatabase();

					sqldb.execSQL("UPDATE Holdwaybilldata SET Drivercode='" + dcode + "',Routecode='" + rcode + "',Transdriver_Code='" + transcourier
							+ "',Consignee='" + cname1 + "',Telephone='" + phone + "'," + "Area='" + area1 + "'," + "Company='"
							+ company1 + "',CivilID='" + civilid1 + "',Serial='" + serial1 + "',CardType='" + cardtype1 + "',DeliveryDate='" + deldate1 + "',DeliveryTime='"
							+ deltime1 + "',Amount='" + amount1 + "',Address='" + addresshold1 + "',HOLD_Transfer_Status=0,TransferStatus=0 WHERE Waybill='" + waybill1 + "'");


					/*Log.e("Drivercode", dcode);
					Log.e("Routecode",rcode);
					Log.e("Transdriver_Code",transcourier);
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
					values.put("Drivercode", dcode);
					values.put("Routecode", rcode);
					values.put("Transdriver_Code", transcourier);
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
					values.put("Address", addresshold1);
					values.put("HOLD_Transfer_Status", "0");
					values.put("TransferStatus", "0");


					sqldb.insertOrThrow("Holdwaybilldata", null, values);

				/* Log.e("Drivercode", dcode);
					Log.e("Routecode",rcode);
					Log.e("Transdriver_Code",transcourier);
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
			}
			//	if(!error1.contains("anyType")||error1!="")
			else {
				Toast.makeText(getApplicationContext(), error1, Toast.LENGTH_LONG).show();
			}


			Pb.setVisibility(View.INVISIBLE);


		}
	}

	// KDC Connection Changed
	// @Override
	public void ConnectionChanged(BluetoothDevice device, int state) {
		//ToDo Auto-generated method stub

		Log.i("KDCReader", "KDC Hold Activity connection changed block");
		System.out.print("KDCReader Hold Activity connection changed block");
		System.out.print("State is " + state);
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
	// KDC DataReceived


	//@Override
	public void DataReceived(KDCData pData) {

		//
	}

	// Barcode DataReceived
	// @Override
	public void BarcodeDataReceived(KDCData pData) {

		Log.i("KDCReader", "KDC Hold Activity BarCodeReceived Block");
		System.out.print("KDCReader Hold Activity  BarCodeReceived Block");


		if (pData != null) {

			ScannerData = pData;
			waybill = ScannerData.GetData();
			// StartDeliveryActivity.waybillFromScanner = ScannerData.GetData();

			if (Check_ValidWaybill(pData.GetData()) == true) {

				System.out.println(" Holdactivity ID : ");
				// System.out.println(R.id.WC_Frame);
				System.out.println(" value for pdata is : ");
				System.out.println(pData);

				_activity.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						if (waybill != null) {
							//String contents = wbill;


							wbill = waybill;
							transcourier = String.valueOf(drivcode[courierlist.getSelectedItemPosition()]);
							// _activity.ScannerExecutions();
							System.out.println("Value wbill in hold" + wbill);
							if (transcourier.isEmpty()) {
								Toast.makeText(_activity, "Please select any courier!", Toast.LENGTH_SHORT).show();

								return;
							}
							new checkholdwaybill().execute();
						}
						// wbilldata1=contents;

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

	public static boolean Check_ValidWaybill(String s) {

		if (s.length() == 10 || s.length() == 12) {
			return StringUtils.isNumeric(s) == true;
		} else if (s.length() == 18) {
			return StringUtils.isAlphanumeric(s) == true;
		}
		return false;
	}

	@Override
	public void onBarcodeEvent(final BarcodeReadEvent event) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				String barcodeData = event.getBarcodeData();
				// update UI to reflect the data

				System.out.println("barcodeData is:" + barcodeData);


				if (barcodeData != null) {

					//ScannerData = barcodeData;
					waybill = barcodeData;
					// StartDeliveryActivity.waybillFromScanner = ScannerData.GetData();

					if (Check_ValidWaybill(event.getBarcodeData()) == true) {

						System.out.println(" Holdactivity ID : ");
						// System.out.println(R.id.WC_Frame);
						System.out.println(" value for barcodeData is : ");
						System.out.println(barcodeData);

						_activity.runOnUiThread(new Runnable() {
							@Override
							public void run() {
								if (waybill != null) {
									//String contents = wbill;


									wbill = waybill;
									transcourier = String.valueOf(drivcode[courierlist.getSelectedItemPosition()]);
									// _activity.ScannerExecutions();
									System.out.println("Value wbill in hold" + wbill);
									if (transcourier.isEmpty()) {
										Toast.makeText(_activity, "Please select any courier!", Toast.LENGTH_SHORT).show();

										return;
									}
									new checkholdwaybill().execute();
								}
								// wbilldata1=contents;

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

	@Override
	public void onFailureEvent(BarcodeFailureEvent barcodeFailureEvent) {

	}

	@Override
	public void onTriggerEvent(TriggerStateChangeEvent event) {
		try {
			// only handle trigger presses
			// turn on/off aimer, illumination and decoding
			barcodeReader.aim(event.getState());
			barcodeReader.light(event.getState());
			barcodeReader.decode(event.getState());


		} catch (ScannerNotClaimedException e) {
			e.printStackTrace();
			Toast.makeText(this, "Scanner is not claimed", Toast.LENGTH_SHORT).show();
		} catch (ScannerUnavailableException e) {
			e.printStackTrace();
			Toast.makeText(this, "Scanner unavailable", Toast.LENGTH_SHORT).show();
		}
	}
	@Override
	public void onPause() {
		super.onPause();
		if(barcodeReader!=null){
		barcodeReader.removeBarcodeListener(this);
		barcodeReader.release();
		}
		_activity.finish();
	}

}