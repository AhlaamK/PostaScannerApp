package com.postaplus.postascannerapp;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import DatabaseHandlers.DBFCS.TBLogin;
import DatabaseHandlers.DBFunctions;


public class RouteActivity extends MasterActivity  {

	private Spinner myspinner;

	TextView username;
	Button ok,logout;
	private String[] routename,routecode;

	String routestatus,drivercode,routec,routen,area;
	boolean mstatus;
	static boolean errored=false;
	GPSTracker gps;
	double latitude,longitude;
	String lati,longti,serialid;
	Context mContext;



	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_route);
		mContext=this;

		System.out.println("Route Activity Page");
		username=(TextView) findViewById(R.id.unametxt);
		ok=(Button) findViewById(R.id.buttonok);
		myspinner = (Spinner)findViewById(R.id.listroute);
		logout=(Button)findViewById(R.id.buttonlogout);
		/*set the loginuser using sharedpreferece*/
		pref = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
		username.setText(pref.getString("uname", ""));

		drivercode=username.getText().toString();


		db=new DatabaseHandler(getBaseContext());
		//open localdatabase in a read mode
		sqldb = db.getReadableDatabase();

		//select all values from eventtable and populate it in the spinner
		Cursor c1 = sqldb.rawQuery("SELECT ROUTECODE,ROUTENAME  FROM routedata", null);
		int count=c1.getCount();

		Log.e("RouteActivity","Route Count : " + count);
		routename=new String[count];
		routecode=new String[count];

		c1.moveToFirst();
		if(count>0){
			for(int i=0;i<count;i++)
			{

				routecode[i] = c1.getString(c1.getColumnIndex("ROUTECODE"));
				routename[i] = c1.getString(c1.getColumnIndex("ROUTENAME"));
				System.out.println("Routecode and routeactivity");
				// System.out.println("i="+i+eventname[i]);
				c1.moveToNext();
			}
		}else{
			TBLogin ActiveUser = DBFunctions.GetLoggedUser(getBaseContext());
			CommonMethods.getroutes(getBaseContext(),ActiveUser.USER_NAME);
		}

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_spinner_item, routename);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		myspinner.setAdapter(adapter);

		// closing connection
		c1.close();

		logout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				v.startAnimation(AnimationUtils.loadAnimation(getBaseContext(), R.anim.image_click));

				TelephonyManager telephonyManager  =
						( TelephonyManager )getSystemService( Context.TELEPHONY_SERVICE );
				serialid= telephonyManager.getDeviceId();

				gps = new GPSTracker(mContext,RouteActivity.this);

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
				//mstatus=WebService.Setdevicestatus(drivercode,serialid,lati,longti,login,area,MasterActivity.METHOD_NAME30);
				mstatus= webservice.WebService.SET_DEVICE_STATUS(drivercode,serialid,lati,longti,String.valueOf(login),area="");
				//if(mstatus)
				//System.out.println("success");

				//stopService(new Intent(getBaseContext(), PostaNotificationService.class));

				//db=new DatabaseHandler(getBaseContext());
				//update loginstatus to ZERO when logout
				SQLiteDatabase db1 =db.getWritableDatabase();
				db1.execSQL("UPDATE logindata SET Loginstatus=0 WHERE Username='"+drivercode+"'" );

				pref = getSharedPreferences(LoginActivity.MyPREFERENCES, Context.MODE_PRIVATE);
				Editor editor = pref.edit();
				editor.clear();
				editor.commit();

				RouteActivity.this.finish();

				startActivity(new Intent(RouteActivity.this,LoginActivity.class));


			}
		});



		db.close();




		ok.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				v.startAnimation(AnimationUtils.loadAnimation(getBaseContext(), R.anim.image_click));
				//routecode
				if(routec==null)routec= String.valueOf(routecode[myspinner.getSelectedItemPosition()]);
				//routec= String.valueOf(routecode[myspinner.getSelectedItemPosition()]);

				//set root value to server database

				//routestatus=WebService.setcourierroute(drivercode,routec,METHOD_NAME18);
				routestatus= webservice.WebService.SET_COURIERROUTE(drivercode,routec);

				if(!errored)
				{

					if(routestatus.contains("True"))
					{

						//update root value in the local database
						db=new DatabaseHandler(getBaseContext());
						//open localdatabase in a read mode
						sqldb = db.getReadableDatabase();

						//select all values in the table and check count 
						Cursor c = sqldb.rawQuery("SELECT * FROM logindata WHERE Username='"+drivercode+"'", null);


						String route1="";
						Integer count =  c.getCount();

						//if count==0 insert the values else update the data
						if(count>0)
						{

							c.moveToFirst();

							sqldb.execSQL("UPDATE logindata SET Routecode="+routec+" WHERE Username='"+drivercode+"'" );

						}
						c.close();
						Cursor cc = sqldb.rawQuery("SELECT * FROM logindata WHERE Username='"+drivercode+"'", null);
						if(cc.getCount()>0)
						{
							cc.moveToFirst();
							route1 =cc.getString(cc.getColumnIndex("Routecode"));

							if(route1!=null)
							{
								int rout1=Integer.parseInt(route1);

								//select all values in the table and check count
								Cursor c1 = sqldb.rawQuery("SELECT * FROM routedata WHERE ROUTECODE="+rout1, null);

								if(c1.getCount()>0)
								{
									c1.moveToFirst();
								/*if (c1.moveToFirst()) 
								{

									do 
									{
										
										if(c1.getInt(c1.getColumnIndex("ROUTECODE"))==rout1){*/
									//whole data of column is fetched by getColumnIndex()
									routen =c1.getString(c1.getColumnIndex("ROUTENAME"));

											
										/*}


									}while(c1.moveToNext());}*/

								}
								c1.close();
							}
						}
						Intent int1 = new Intent(RouteActivity.this,HomeActivity.class);


						//pass the value of routecode  to HomeActivity Page
						int1.putExtra("route",route1);
						//pass the value of routename  to HomeActivity Page
						int1.putExtra("route1",routen);
						startActivity(new Intent(int1));
						cc.close();

						db.close();
					}
					else
					{
						Toast.makeText(getApplicationContext(),"Open Route found!!", Toast.LENGTH_LONG).show();
					}
				}
				else{
					Toast.makeText(getApplicationContext(), "Connection Error Occured", Toast.LENGTH_LONG).show();
				}
			}
		});

	}
}