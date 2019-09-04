package com.postaplus.postascannerapp;

import android.app.ActionBar;
import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.onesignal.OSNotificationPayload;
import com.onesignal.OneSignal;

import koamtac.kdc.sdk.KDCData;
import koamtac.kdc.sdk.KDCReader;

public class MasterActivity extends Activity implements View.OnClickListener {
	//declare common method,variable etc in masterpage
	//SQLiteDatabase sqldb = null;
	//DatabaseHandler db;
	public static final String usrnam = "uname";
	public static final String MyPREFERENCES = "MyPrefs" ;
	SharedPreferences pref;
	public static SQLiteDatabase sqldb = null;
	public static DatabaseHandler db;
	public static final String METHOD_NAME1="CHECK_HOLDVALIDWAYBILL";
	public static final String METHOD_NAME2="CHECK_TRANSWAYBILL";
	public static final String METHOD_NAME3="CHECK_VALIDWAYBILL";
	public static final String METHOD_NAME4="GET_COURIERROUTE";
	public static final String METHOD_NAME5="GET_COURIERS";
	public static final String METHOD_NAME6="GET_EVENTS";
	public static final String METHOD_NAME7="GET_HOLDWAYBILLS";
	public static final String METHOD_NAME8="GET_ODOREADING";
	public static final String METHOD_NAME9="GET_OPENRST";
	public static final String METHOD_NAME10="GET_PAYTYPE";
	public static final String METHOD_NAME11="GET_PICKUP";
	public static final String METHOD_NAME12="GET_PICKUP_DT";
	public static final String METHOD_NAME13="GET_ROUTES";
	public static final String METHOD_NAME14="GET_RSTDETAIL";
	public static final String METHOD_NAME15="GET_SERVICE";
	public static final String METHOD_NAME16="SET_ACCEPTHOLD";
	public static final String METHOD_NAME17="SET_CANCELATION_HOLD";
	public static final String METHOD_NAME18="SET_COURIERROUTE";
	public static final String METHOD_NAME19="SET_COURIERROUTECLOSE";
	public static final String METHOD_NAME20="SET_DELIVERY";
	public static final String METHOD_NAME21="SET_DRIVERMETER";
	public static final String METHOD_NAME22="SET_PICKUPDETAILS";
	public static final String METHOD_NAME23="SET_PICKUP_RECVD";
	public static final String METHOD_NAME24="SET_TRANS_CONFIRM";
	public static final String METHOD_NAME25="SET_TRANS_HOLD";
	public static final String METHOD_NAME26="SET_TRANS_WC";
	public static final String METHOD_NAME27="SET_WAYBILLACK_IMG";
	public static final String METHOD_NAME28="SET_WC";
	public static final String METHOD_NAME29="USER_AUTHENTICATION";
	public static final String METHOD_NAME30="SET_DEVICE_STATUS";
	public static final String METHOD_NAME31="SET_FUEL_TRACK";
	public static final String METHOD_NAME32="GET_PICKUP_WAYBILLS_DT";
	public static final String METHOD_NAME33="SET_TRANS_PICKUP";
	public static final String METHOD_NAME34="CHECK_PICKUPWAYBILL";
	public static final String METHOD_NAME35="SET_TRANS_PICKUP_CONFIRM";
	public static final String METHOD_NAME36="GET_SCAN_WAYBILL_DT";
	public static final String METHOD_NAME37="SET_DELETE_HOLD";
	public static final String METHOD_NAME38="SET_ODO_FUEL_IMAGE";
	public static final String METHOD_NAME39="SET_AWB_EVENT";
	public static final String METHOD_NAME40="SET_SYNCH_CHANNEL";
	public static final String METHOD_NAME41="CLEAR_SYNCH_CHANNEL";
	public static final String METHOD_NAME42="SET_DELETE_INITIAL_SCAN_SINGLE";
	public static final String METHOD_NAME43="GET_TRANSWAYBILL_DT";
	public static final String METHOD_NAME44="CHECK_VHCLBARCODE";
	
	public static String NAMESPACE = "http://www.postaplus.net/";	
	//Reading ip from text file
	public static String URL = ScreenActivity.ipaddress;
	
	public String manufacturer,model;
	
	//public static String URL ="http://192.168.13.82/OpsCourierServ/Service.asmx";
	//public ip address
	//public static String URL = "http://213.132.228.254/OpsCourierServ/Service.asmx";
	
	public static String SOAP_ACTION = "http://www.postaplus.net/";
	
	
	//KDC Parameters
	public static BluetoothDevice ScannerDevice = null;
	KDCData ScannerData;
	KDCReader _kdcReader;
	MasterActivity _activity;
	public static String KDCScannerCallFrom = "";
	public static String WaybillFromScanner = "";
	public static  String updatedPushWaybill;
	public static  int pushWaybillCount=0;
	TextView textViewPushicon;
	ImageView imageViewPushicon;
	public static String[] pushawbList = null;
	public static String[] pushawbStrarray;
	public static int flagpush=0;


	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
		ActionBar localActionBar = getActionBar();
		localActionBar.setCustomView(R.layout.actionbar_layout);
		localActionBar.setDisplayShowTitleEnabled(false);
		localActionBar.setDisplayShowCustomEnabled(true);
		localActionBar.setDisplayUseLogoEnabled(false);
		localActionBar.setDisplayShowHomeEnabled(false);
		localActionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#1c181c")));
		textViewPushicon = (TextView)localActionBar.getCustomView().findViewById(R.id.textViewPushicon);
		imageViewPushicon = (ImageView) localActionBar.getCustomView().findViewById(R.id.imageViewPushicon);
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy); 
		//System.out.println(URL);
		/*if(pushWaybillCount==0)textViewPushicon.setText(String.valueOf(""));
		else textViewPushicon.setText(String.valueOf(pushWaybillCount));*/
		if(pushWaybillCount==0){
			textViewPushicon.setText(String.valueOf(""));
		}else{
			textViewPushicon.setText(String.valueOf(pushWaybillCount));

		}
		 manufacturer = Build.MANUFACTURER;
	     model = Build.MODEL;
	     
	     _activity = this;

		imageViewPushicon.setOnClickListener(this);


		OneSignal.startInit(this).inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)

				.setNotificationReceivedHandler(notification -> {
					OSNotificationPayload payload = notification.payload;
					Log.e("data", payload.body);

				}).setNotificationOpenedHandler(result -> {
			Intent intent = new Intent(this, HomeActivity.class);
			this.startActivity(intent);

		}).init();
	     
	   //KDC Reader
	     /*Thread t = new Thread(){
		    	@Override
		    	 public void run(){
		    		_kdcReader= new KDCReader(null, null, null, null, null, null, null, false);
		    	}
		    };
		    t.start();*/

		    Log.w("KDCReader", "KDC Thread1 Started");
			System.out.println("KDC Thread2 Started");
			//Log.w("KDCReader Log", _activity.);
			System.out.println(_activity);
	}

	@Override
	protected void onResume() {
		super.onResume();
		OneSignal.startInit(this).inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
				.setNotificationReceivedHandler(notification -> {
					OSNotificationPayload payload = notification.payload;
					Log.e("data1", payload.body);
					if(payload.body.contains(":")){
						String[] pushwaybillString = payload.body.split(":");
						//updatedPushWaybill = pushwaybillString[1].toString();
						pushWaybillCount++;

						if(pushWaybillCount==1){
							updatedPushWaybill = pushwaybillString[1].toString().trim();

						}else if(pushWaybillCount>1){

							if (updatedPushWaybill.contains(pushwaybillString[1].toString().trim())) {
								pushWaybillCount= pushWaybillCount-1;
								return;
							}else{
								updatedPushWaybill = updatedPushWaybill+","+pushwaybillString[1].toString().trim();

							}


						}
						 pushawbList =updatedPushWaybill.split(",");
						 pushawbStrarray = new String[pushWaybillCount];
						//pushawbStrarray = new HashSet<String>(Arrays.asList(pushawbStrarray)).toArray(new String[0]);
						for(int i =0; i< pushWaybillCount;i++){

							pushawbStrarray[i]=pushawbList[i].toString();

						}

						textViewPushicon.setText(String.valueOf(pushWaybillCount));

					}
				}).setNotificationOpenedHandler(result -> {
			Intent intent = new Intent(this, HomeActivity.class);
			this.startActivity(intent);

		}).init();

	}

	@Override
	public void onClick(View v) {
		if(v.getId()==R.id.imageViewPushicon)
		{
			if(pushawbStrarray == null){
				Toast toast = Toast.makeText(getApplicationContext(),"You do not have notifications", Toast.LENGTH_LONG);
				toast.setGravity(Gravity.CENTER, 0, 0);
				toast.show();
				/*if(pushWaybillCount==0)textViewPushicon.setText(String.valueOf(""));
				else textViewPushicon.setText(String.valueOf(pushWaybillCount));*/
				return;

			}else if(pushawbStrarray.length<=0){
			Toast toast = Toast.makeText(getApplicationContext(),"You do not have notifications", Toast.LENGTH_LONG);
			toast.setGravity(Gravity.CENTER, 0, 0);
			toast.show();
			/*	if(pushWaybillCount==0)textViewPushicon.setText(String.valueOf(""));
				else textViewPushicon.setText(String.valueOf(pushWaybillCount));*/
			return;
		}else{


				/* if(pushawbStrarray.length>0&&flagpush==0){*/
			/*	if(flagpush==1){
					Toast toast = Toast.makeText(getApplicationContext(),"Please sync to see updated shipments", Toast.LENGTH_LONG);
					toast.setGravity(Gravity.CENTER, 0, 0);
					toast.show();
				}else if(flagpush==1&&pushawbStrarray.length>0){}*/
				Intent intent=new Intent(getApplicationContext(),PushNotifyActivity.class);
					startActivity(intent);
			}


				}

		}
	}



