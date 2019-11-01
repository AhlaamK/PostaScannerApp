package com.postaplus.postascannerapp;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.os.Bundle;
import androidx.core.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TableRow.LayoutParams;
import android.widget.TextView;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import webservice.*;

public class Route_Close_Activity extends MasterActivity {
	TableLayout resulttab;
	TableRow tr;
	LayoutParams lp;
	TextView username, tv, tv1;
	GPSTracker gps;
	double latitude, longitude;
	String lati, longti, serialid;
	String drivercode, rte1, rte, area;
	String[] wbill, pickupno;
	Button logout, back;
	boolean mstatus;
	Context mContext;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_route_close);
		mContext = this;

		//tv = new TextView(this);
		//tv1=new TextView(this);
		username = (TextView) findViewById(R.id.unametxt);
		pref = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
		username.setText(pref.getString("uname", ""));
		drivercode = username.getText().toString();
		resulttab = (TableLayout) findViewById(R.id.resulttable1);
		logout = (Button) findViewById(R.id.buttonlogout);
		back = (Button) findViewById(R.id.buttonback);

		//route code
		rte = getIntent().getExtras().getString("route");
		//route name
		rte1 = getIntent().getExtras().getString("route1");

		db = new DatabaseHandler(getBaseContext());
		sqldb = db.getReadableDatabase();
		Cursor rbc = sqldb.rawQuery("SELECT Waybill FROM deliverydata WHERE Attempt_Status<>1", null);
		int c = rbc.getCount();
		wbill = new String[c];
		rbc.moveToFirst();

		if (c > 0) {
			//	tv.setText("PENDING WAYBILL");
			for (int i = 0; i < c; i++) {


				wbill[i] = rbc.getString(rbc.getColumnIndex("Waybill"));

				tr = new TableRow(Route_Close_Activity.this);


				if (Build.MODEL.contains("SM-N")) {

					lp = new LayoutParams(340, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
					//tr.setId(resulttab.getChildCount());
					tr.setLayoutParams(lp);
					lp.setMargins(0, 10, 40, 0);

				} else {
					lp = new LayoutParams(150, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
					//tr.setId(resulttab.getChildCount());
					tr.setLayoutParams(lp);
					lp.setMargins(0, 20, 10, 0);

					lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
					//tr.setId(resulttab.getChildCount());
					lp.setMargins(0, 20, 70, 0);
					tr.setLayoutParams(lp);
				}


				final TextView waybilltxt = new TextView(Route_Close_Activity.this);
				waybilltxt.setLayoutParams(lp);
				waybilltxt.setText(wbill[i]);

				tr.addView(waybilltxt);

				rbc.moveToNext();
				resulttab.addView(tr, new TableLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

			}
		}
		rbc.close();


		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				v.startAnimation(AnimationUtils.loadAnimation(getBaseContext(), R.anim.image_click));

				// moveTaskToBack(true);
				Route_Close_Activity.this.finish();
				Intent int1 = new Intent(Route_Close_Activity.this, HomeActivity.class);

				int1.putExtra("route", rte);
				int1.putExtra("route1", rte1);

				startActivity(new Intent(int1));

			}
		});

		logout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				v.startAnimation(AnimationUtils.loadAnimation(getBaseContext(), R.anim.image_click));
				TelephonyManager telephonyManager =
						(TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
				if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
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
				
				gps = new GPSTracker(mContext,Route_Close_Activity.this);

				// check if GPS enabled     
				if(gps.canGetLocation())
				{

					latitude = gps.getLatitude();
					longitude = gps.getLongitude();	
					lati=String.valueOf(latitude);
					longti=String.valueOf(longitude);

				}else
				{
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
	                    area = address.getAddressLine(0)+ "," +address.getAdminArea();
	                    //System.out.println(area);
	                }
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	   			int login=0;
				//System.out.println(response.getPropertyCount());
			//	mstatus=WebService.Setdevicestatus(drivercode,serialid,lati,longti,login,area,MasterActivity.METHOD_NAME30);
				mstatus= WebService.SET_DEVICE_STATUS(drivercode,serialid,lati,longti,String.valueOf(login),area="");
				if(mstatus)
					System.out.println("success");
				
				
				
				//stopService(new Intent(getBaseContext(), PostaNotificationService.class));
				
				//db=new DatabaseHandler(getBaseContext());
				//update loginstatus to ZERO when logout
				SQLiteDatabase db1 =db.getWritableDatabase();
				db1.execSQL("UPDATE logindata SET Loginstatus=0 WHERE Username='"+drivercode+"'" );

				pref = getSharedPreferences(LoginActivity.MyPREFERENCES, Context.MODE_PRIVATE);
				Editor editor = pref.edit();
				editor.clear();
				editor.commit();
				
				Route_Close_Activity.this.finish();

				startActivity(new Intent(Route_Close_Activity.this,LoginActivity.class));

				
			}
		});
		
		db.close();
		
}
}
