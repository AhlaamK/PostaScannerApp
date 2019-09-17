package com.postaplus.postascannerapp;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Address;
import android.location.Geocoder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.IBinder;
import android.os.StrictMode;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Timer;

import webservice.FuncClasses.PickUp;
import webservice.WebService;

public class PostaNotificationService extends Service {
	String pickupno = "", acname = "", pickaddr = "", pickarea = "", contact_person = "", pick_phone = "", picktime = "", error = "";
	String[] pickupno1, acname1, pickaddr1, pickarea1, contact_person1, pick_phone1, picktime1, error1;
	int count;
	DatabaseHandler db;
	SQLiteDatabase sqldb = null;
	String uname = null;
	//SoapObject response;
	int login = 0;
	boolean netstatus, mstatus, servicestatus;
	private static long UPDATE_INTERVAL = (3 * 60) * 1000;  //default
	GPSTracker gps;
	double latitude, longitude;
	String lati, longti, serialid, area;
	Context mContext;
	private static Timer timer = new Timer();
	Handler handler = new Handler();
	Runnable runnableCode;
	PickUp[] pickupResponse;
	int PickupCount = 0;
	private int NOTIFICATION = 1; // Unique identifier for our notification

	ActivityNotification actNoty = new ActivityNotification();

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		//	Log.e("Servicebind","Service Working");
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();

		//   _startService();
		StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
				.detectAll()  // or .detectAll() for all detectable problems
				.penaltyLog()
				.build());

		mContext = this;

	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {

		runnableCode = new Runnable() {
			@Override
			public void run() {
				try {
					Log.e("PostaService", "Service Working");
					boolean WebserStatus = WebService.GET_SERVICE_STATUS(null);
					if (WebserStatus) {
						// Do something here on the main thread
						doServiceWork();
						Log.d("Handlers", "Called on main thread");
						//	Log.e("Updateinterval",String.valueOf(UPDATE_INTERVAL));
					}

				} catch (Exception e) {
					Log.e("PostaService", "OnstartCmd try-catch block");
					e.printStackTrace();
				}


				handler.postDelayed(runnableCode, 180000);
			}
		};

		handler.post(runnableCode);
		//	startForeground(NOTIFICATION, mBuilder);
		return START_STICKY;
	}
  /*  private void _startService()
    {      
        timer.scheduleAtFixedRate(

                new TimerTask() {

                    public void run() {

                        doServiceWork();

                    }
                },1000,UPDATE_INTERVAL);
		*//*timer.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				doServiceWork();
			}
		}, 0, 5000);*//*

        Log.i(getClass().getSimpleName(), "FileScannerService Timer started....");
    }*/

	private void doServiceWork() {

		try {


			login = 0;
			db = new DatabaseHandler(getApplicationContext());
			sqldb = db.getReadableDatabase();

			Cursor c = sqldb.rawQuery("SELECT * FROM logindata WHERE Loginstatus=1", null);
			int count1 = c.getCount();

			if (count1 > 0) {

				c.moveToFirst();

				uname = c.getString(c.getColumnIndex("Username"));

				//String login=c.getString(c.getColumnIndex("Loginstatus"));
				login = c.getInt(c.getColumnIndex("Loginstatus"));


			}
			c.close();
			db.close();
			sqldb.close();
			if (login == 1) {

				netstatus = isNetworkConnected();
				if (netstatus) {


					new pickupTask().execute();

				} else {
					Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
				}
				//	 for(int i=0;i<count;i++){
				// if(pickupno!=null){
				// System.out.print("reci:"+pickupno);
				// }
				// }

			} else {

				netstatus = isNetworkConnected();
				if (netstatus) {
					new monitortask().execute();
					//servicestatus=isConnected();
					/*if(servicestatus)
					{

						new monitortask().execute();
					}
					else
					{

						Toast.makeText(getApplicationContext(), "Connection Error", Toast.LENGTH_LONG).show();
					}*/
				} else {
					Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
				}
				//	 for(int i=0;i<count;i++){
				// if(pickupno!=null){
				// System.out.print("reci:"+pickupno);
				// }
				// }

			}


		} catch (Exception e) {
			e.printStackTrace();
			//Log.e("PostaNotification:", e.getMessage().toString());
			// Toast.makeText(getApplicationContext(), "Connection ERROR", Toast.LENGTH_LONG).show();
		}

	}

	public boolean isConnected() {
		try {
			ConnectivityManager cm = (ConnectivityManager) getSystemService
					(Context.CONNECTIVITY_SERVICE);
			NetworkInfo netInfo = cm.getActiveNetworkInfo();

			if (netInfo != null && netInfo.isConnected()) {
				//Network is available but check if we can get access from the network.
			/*	URL url = new URL(MasterActivity.URL);
				HttpURLConnection urlc = (HttpURLConnection) url.openConnection();
				urlc.setRequestProperty("Connection", "close");
				urlc.setConnectTimeout(2000); // Timeout 2 seconds.
				urlc.connect();*/

				//Successful response.
				//Log.d("NO INTERNET", "NO INTERNET");
				//return1 urlc.getResponseCode() == 200;
				Boolean Connect = WebService.GET_SERVICE_STATUS(null);
				System.out.println("Connect result in postanotification service" + Connect);
				if (Connect) return true;
				else {
					Toast.makeText(getApplicationContext(), "WebService not available", Toast.LENGTH_LONG).show();
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
		NetworkInfo ni = cm.getActiveNetworkInfo();
		return ni != null;
	}

	private void _shutdownService() {
		if (timer != null) timer.cancel();
		Log.i(getClass().getSimpleName(), "Timer stopped...");
		timer = new Timer();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		//handler.removeCallbacks(runnableCode);
		//   _shutdownService();


	}

	public class pickupTask extends AsyncTask<Void, Void, String> {

		@Override
		protected String doInBackground(Void... arg0) {
			getpickup();

			Log.d("Handlers", "Called for getpickup");

			return "";
		}

		private void getpickup() {

			try {
				pickupResponse = WebService.GET_PICKUP(uname);
				//   if(pickupResponse!=null)  PickupCount=pickupResponse.length;
				PickupCount = 0;
				db = new DatabaseHandler(getBaseContext());

				for (PickUp puObv : pickupResponse) {
					System.out.println("response rerror value in getpickup is :" + puObv.ERR);
					System.out.println("response success value in getpickup is :" + puObv.ACC_NAME);
					System.out.println("response success value in getpickup is :" + puObv.PICK_NO);
					System.out.println("response success value in getpickup for pick_type is :" + puObv.IDENTIFIER);
					if (puObv.ERR == null) {
						Log.e("Postaservice/response", "Enter to pickup counter block");
						//To count pickup
						PickupCount += 1;
						//open localdatabase in a read mode

						sqldb = db.getWritableDatabase();
						sqldb.execSQL("DELETE FROM pickuphead WHERE Drivercode <> '" + uname + "'");
						sqldb = db.getReadableDatabase();
						//select all values in the table and check count
						Cursor c1 = sqldb.rawQuery("SELECT * FROM pickuphead WHERE Pickup_No='" + puObv.PICK_NO + "'", null);
						int count1 = c1.getCount();

						sqldb = db.getWritableDatabase();
						if (count1 > 0) {

							System.out.println("update");


							sqldb.execSQL("UPDATE pickuphead SET Drivercode='" + uname + "', Account_Name='" + puObv.ACC_NAME
									+ "',Pick_Address='" + puObv.PICK_ADD + "',Pickup_Area='" + puObv.PICK_AREA + "',Contact_Person='" + puObv.CONTACT_PERSON + "'," + "Pickup_Phone='" + puObv.PICK_PHONE + "'," + "Pickup_Time='"
									+ puObv.PICK_TIME + "',Status='P',TransferStatus=0,Pickup_Type='" + puObv.IDENTIFIER + "',ConsigneeName='" + puObv.CONSIGNEE_NAME + "',DeliveryAddress='" + puObv.DEL_ADD + "',DeliveryCity='" + puObv.DEL_CITY + "',DeliveryPhone='" + puObv.DEL_PHONE + "' WHERE Pickup_No='" + puObv.PICK_NO + "'");


						} else {
							c1.moveToLast();

							//sqldb =db.getWritableDatabase();
							ContentValues values = new ContentValues();
							values.put("Drivercode", uname);
							values.put("Pickup_No", puObv.PICK_NO);
							values.put("Pickup_Type", puObv.IDENTIFIER);
							values.put("Pickup_Time", puObv.PICK_TIME);
							values.put("Pickup_Phone", puObv.PICK_PHONE);
							values.put("Account_Name", puObv.ACC_NAME);
							values.put("Pick_Address", puObv.PICK_ADD);
							values.put("Pickup_Area", puObv.PICK_AREA);
							values.put("Contact_Person", puObv.CONTACT_PERSON);
							values.put("Status", "P");
							values.put("TransferStatus", "0");
							values.put("ConsigneeName", puObv.CONSIGNEE_NAME);
							values.put("DeliveryAddress", puObv.DEL_ADD);
							values.put("DeliveryCity", puObv.DEL_CITY);
							values.put("DeliveryPhone", puObv.DEL_PHONE);


							sqldb.insertOrThrow("pickuphead", null, values);

						}
						c1.close();
					}
				}
				db.close();
				sqldb.close();
				Log.e("Postaservice/response", String.valueOf(PickupCount));
			} catch (Exception e) {
				e.printStackTrace();
			}


		}

		@Override
		public void onPostExecute(String res) {

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

			gps = new GPSTracker(mContext, PostaNotificationService.this);
			System.out.println("latitude in postanotific postexec is" + gps.getLatitude());
			System.out.println("longitude is" + gps.getLongitude());
			// check if GPS enabled     
			if (gps.canGetLocation()) {

				latitude = gps.getLatitude();
				longitude = gps.getLongitude();
				lati = String.valueOf(latitude);
				longti = String.valueOf(longitude);

			} else {
				// can't get location
				// GPS or Network is not enabled
				// Ask user to enable GPS/network in settings
				gps.showSettingsAlert();
			}
			//get the area name
			Geocoder gcd = new Geocoder(getApplicationContext(), Locale.getDefault());
			List<Address> addresses;
			try {
				System.out.println("latitude in geocoder before" + gps.getLatitude());
				System.out.println("longitude is" + gps.getLongitude());

				addresses = gcd.getFromLocation(gps.getLatitude(), gps.getLongitude(), 1);
				System.out.println("addresses is" + addresses);
				System.out.println("latitude in geocoder after" + gps.getLatitude());
				System.out.println("longitude is" + gps.getLongitude());
				if (addresses != null && addresses.size() > 0) {
					Address address = addresses.get(0);
					// sending back first address line and locality
					area = address.getAddressLine(0) + "," + address.getAdminArea();
					// System.out.println(area+":"+address.getAdminArea());
				}


			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			//System.out.println(response.getPropertyCount());

			//mstatus=WebService.Setdevicestatus(uname,serialid,lati,longti,login,area,MasterActivity.METHOD_NAME30);
			mstatus = WebService.SET_DEVICE_STATUS(uname, serialid, lati, longti, String.valueOf(login), area = "");
			if (mstatus) System.out.println("success mstatus is:" + mstatus);


			//	if (response == null) return1;
			if (PickupCount <= 0) return;
			if (PickupCount > 0)
			//	if(response.toString().contains("PICK_NO"))
			{


				PendingIntent contentIntent = PendingIntent.getActivity(getApplicationContext(), 0,
						new Intent(getApplicationContext(), NotificationActivity.class), 0);


				NotificationManager mNotificationManager =
						(NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
				NotificationCompat.Builder mBuilder =
						new NotificationCompat.Builder(getApplicationContext())

								.setSmallIcon(R.drawable.postlogoapp)
								.setContentTitle("Pickup Notification")
								.setContentText("You have " + PickupCount + " pickup");
				mBuilder.setContentIntent(contentIntent);
				mBuilder.setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_VIBRATE | Notification.DEFAULT_SOUND);
				mBuilder.setAutoCancel(true);

				mNotificationManager.notify(0, mBuilder.build());

			}
		}
	}

	public class monitortask extends AsyncTask<Void, Void, String> {

		@Override
		protected String doInBackground(Void... arg0) {

			return "";
		}

		@Override
		public void onPostExecute(String res) {

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

			gps = new GPSTracker(mContext,PostaNotificationService.this);

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
					// System.out.println(area+":"+address.getAdminArea());
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			int login1=0;
			//mstatus=WebService.Setdevicestatus(uname,serialid,lati,longti,login1,area,MasterActivity.METHOD_NAME30);
			mstatus= WebService.SET_DEVICE_STATUS(uname,serialid,lati,longti,String.valueOf(login1),area="");
			System.out.println("value of mstatus in postanotification service"+mstatus);
			//if(mstatus)
			//System.out.println("success");

		}
	}


}

