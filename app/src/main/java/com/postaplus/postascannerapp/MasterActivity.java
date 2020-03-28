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

	public static final String METHOD_NAME9="GET_OPENRST";

	public static final String METHOD_NAME22="SET_PICKUPDETAILS";

	public static final String METHOD_NAME44="CHECK_VHCLBARCODE";
	
	public static String URL = ScreenActivity.ipaddress;
	
	public String manufacturer,model;
	

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
				return;

			}else if(pushawbStrarray.length<=0){
			Toast toast = Toast.makeText(getApplicationContext(),"You do not have notifications", Toast.LENGTH_LONG);
			toast.setGravity(Gravity.CENTER, 0, 0);
			toast.show();

			return;
		}else{



				Intent intent=new Intent(getApplicationContext(),PushNotifyActivity.class);
					startActivity(intent);
			}


				}

		}
	}



