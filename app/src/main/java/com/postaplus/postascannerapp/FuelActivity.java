package com.postaplus.postascannerapp;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.honeywell.aidc.BarcodeFailureEvent;
import com.honeywell.aidc.BarcodeReadEvent;
import com.honeywell.aidc.BarcodeReader;
import com.honeywell.aidc.ScannerUnavailableException;

import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import koamtac.kdc.sdk.KDCBarcodeDataReceivedListener;
import koamtac.kdc.sdk.KDCConnectionListener;
import koamtac.kdc.sdk.KDCConstants;
import koamtac.kdc.sdk.KDCData;
import koamtac.kdc.sdk.KDCDataReceivedListener;
import koamtac.kdc.sdk.KDCReader;

import static com.postaplus.postascannerapp.HomeActivity.barcodeReader;

public class FuelActivity extends MasterActivity
implements KDCConnectionListener,KDCDataReceivedListener,KDCBarcodeDataReceivedListener, BarcodeReader.BarcodeListener {
	TextView username;
	String drivercode,rname,rcode,contents="";
	ImageView odoimage,menuimg,recimage;
	Button sub,click,scan,recclick,back;
	EditText odonumber,recnotxt,recamounttxt;
	Bitmap photo,photo1; 
	String odoread,barcodevalue,recnumber,recamount;
	static boolean errored=false;
	String status;
	TextView result;
	SharedPreferences pref;
	Uri uriSavedImage=null;
	File imagefile1;
	DatabaseHandler db;
	public int SCANNER_REQUEST_CODE = 123;
	GPSTracker gps;
	double latitude,longitude;
	String lati,longti,date_time;
	//byte[] inputData=null;
	//byte[] inputData1=null;
	//KDC Parameters

	public static String WaybillFromScanner = "";
	public static String KDCScannerCallFrom = "";
	Thread ThrKdc;
	
	Resources _resources;
	BluetoothDevice _btDevice = null;
	static final byte[] TYPE_BT_OOB = "application/vnd.bluetooth.ep.oob".getBytes();
	Button _btnScan = null;

	//BluetoothDevice _btDevice;
	FuelActivity _activity;
	KDCData ScannerData;
	KDCReader _kdcReader;
	public String chkdata="";
	public String waybill;
	View rootView;
	public FuelActivity MYActivity;
	Context mContext;
	KDCTask KDCTaskExecutable = new KDCTask();
	String photoPath;
	int MY_REQUEST_CODE=10;


	public class KDCTask extends AsyncTask<Void, Void, String> {

		@Override
		protected String doInBackground(Void...arg0){

			if(ThrKdc!=null) {
				ThrKdc.run();
			}
			else {

				ThrKdc = new Thread() {
					@Override
					public void run() {
						_kdcReader = new KDCReader(null, _activity, _activity, null, null, null, _activity, false);
						MasterActivity.ScannerDevice = _kdcReader.GetBluetoothDevice();
						//_kdcReader.EnableBluetoothAutoPowerOn(false);
						//_kdcReader.EnableAutoReconnect(false);
						//_kdcReader.EnableBluetoothWakeupNull(false);
						_kdcReader.EnableBluetoothWakeupNull(true);
						if(isCancelled()) ThrKdc.interrupt();
					}
				};
				ThrKdc.start();
				if(isCancelled()) ThrKdc.interrupt();
			}
			return "";
		}
	}
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fuel);
      	System.out.println("fuelActivity Page");
		mContext=this;
      	imagefile1 = new File(Environment.getExternalStorageDirectory(), "Postaplus/Fuel&Odoimage");
		imagefile1.mkdirs();

		username=(TextView) findViewById(R.id.unametxt);
		pref = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
		username.setText(pref.getString("uname", ""));
		drivercode=username.getText().toString();

		rname= getIntent().getExtras().getString("routename");
		rcode=getIntent().getExtras().getString("routecode");

		back=(Button)findViewById(R.id.buttonback);
		click=(Button)findViewById(R.id.button1);
		recclick=(Button)findViewById(R.id.recieptbutton);
		result=(TextView)findViewById(R.id.textbarcode);
		odoimage=(ImageView)findViewById(R.id.imageodo);
		odonumber=(EditText)findViewById(R.id.odometertxt);

		recimage=(ImageView)findViewById(R.id.imagereceipt);
		recnotxt=(EditText)findViewById(R.id.recptnotxt);
		recamounttxt=(EditText)findViewById(R.id.recptamount);

		sub=(Button)findViewById(R.id.proceed);


		menuimg=(ImageView)findViewById(R.id.imageicon);
		//scan=(Button)findViewById(R.id.btnscan);

		//KDC Full Commands
	    _activity = this;

	    if (barcodeReader!=null)
		barcodeReader.addBarcodeListener(_activity);
		if (barcodeReader != null) {
			try {
				barcodeReader.claim();
				System.out.println("barcode onres:" + barcodeReader);
			} catch (ScannerUnavailableException e) {
				e.printStackTrace();
				Toast.makeText(this, "Scanner unavailable", Toast.LENGTH_SHORT).show();
			}
		}

	    _resources = getResources();
		if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
			//takePictureButton.setEnabled(false);
			ActivityCompat.requestPermissions(this, new String[] { Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE }, 0);
		}

	   /*  ThrKdc = new Thread(){
		    	@Override
		    	 public void run(){
		    		_kdcReader= new KDCReader(null, _activity, _activity, null, null, null, _activity, false);
		    		_btDevice = _kdcReader.GetBluetoothDevice();
					_kdcReader.EnableBluetoothWakeupNull(true);
		    	}
		    };
		    ThrKdc.start();*/

			KDCTaskExecutable.execute();
			System.out.println("FuelActivity KDCTask Executed");



		click.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				v.startAnimation(AnimationUtils.loadAnimation(getBaseContext(), R.anim.image_click));

			//	Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
				odoimage.setImageResource(R.drawable.odo);
				//imgcountarr[6]=1;
			/*	uriSavedImage=Uri.fromFile(new File(imagefile1,  "Fuelodo.PNG"));
				intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, uriSavedImage);
				startActivityForResult(intent, 0);*/

			/*	String imgpath = Environment.getExternalStorageDirectory().getAbsolutePath()+"/Postaplus/Fuel&Odoimage";
				//imagefile1.mkdirs();


				File imagefile1 = new File(imgpath);
				Uri imageFileUri = Uri.fromFile(new File(imagefile1,"Fuelodo.PNG")); // convert path to Uri

				Intent it = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
				it.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, imageFileUri);
				startActivityForResult(it, 0);*/

			/*	String fileName = imagefile1+"Fuelodo.PNG";
				ContentValues values = new ContentValues();
				values.put(MediaStore.EXTRA_OUTPUT, fileName);
				uriSavedImage = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

				Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				intent.putExtra(MediaStore.INTENT_ACTION_STILL_IMAGE_CAMERA, uriSavedImage);
				startActivityForResult(intent, 0);*/

				Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				uriSavedImage = Uri.fromFile(getOutputMediaFile());
				intent.putExtra(MediaStore.EXTRA_OUTPUT, uriSavedImage);

				startActivityForResult(intent, 0);

			}
		});
		/* back.setOnClickListener(new OnClickListener() {

		        @Override
		        public void onClick(View v) {
		        	v.startAnimation(AnimationUtils.loadAnimation(getBaseContext(), R.anim.image_click));

		        	// moveTaskToBack(true);
		        	FuelActivity.this.finish();
		        	Intent int1 = new Intent(FuelActivity.this,HomeActivity.class);

					int1.putExtra("route",rcode);
					int1.putExtra("route1",rname);

					startActivity(new Intent(int1));

		        }
		    });*/
		recclick.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				v.startAnimation(AnimationUtils.loadAnimation(getBaseContext(), R.anim.image_click));

				Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
				recimage.setImageResource(R.drawable.recp);
				//imgcountarr[6]=1;
				uriSavedImage=Uri.fromFile(new File(imagefile1,  "FuelReciept.PNG"));
				intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, uriSavedImage);
				startActivityForResult(intent, 0);


			}
		});
		/*scan.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				v.startAnimation(AnimationUtils.loadAnimation(getBaseContext(), R.anim.image_click));

				if (v.getId() == R.id.btnscan) {
					// go to fullscreen scan
					Intent intent = new Intent("com.google.zxing.client.android.SCAN");
					intent.putExtra("SCAN_MODE", "SCAN_MODE");
					startActivityForResult(intent, SCANNER_REQUEST_CODE);
				}


			}
		}); */
		sub.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {


				//tx.setText(num.getText().toString());
				v.startAnimation(AnimationUtils.loadAnimation(getBaseContext(), R.anim.image_click));

				if(null!=odoimage.getDrawable()&&odonumber.getText().length()!=0&&result.getText().length()!=0&&
						null!=recimage.getDrawable()&&recnotxt.getText().length()!=0&&recamounttxt.getText().length()!=0)
				{

					odoread=odonumber.getText().toString();
					recnumber=recnotxt.getText().toString();
					recamount=recamounttxt.getText().toString();

					//convert bitmap to bytearray
				/*	ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
					photo.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
					byte[] byteArray = byteArrayOutputStream.toByteArray();
					*/
					barcodevalue=result.getText().toString();
					//call the webservice for saving the value

				/*	ByteArrayOutputStream byteArrayOutputStream1 = new ByteArrayOutputStream();
					photo1.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream1);
					byte[] byteArray1 = byteArrayOutputStream1.toByteArray();*/

					SimpleDateFormat date1 = new SimpleDateFormat("yyyy-MMM-dd HH:mm:ss");
					date_time=date1.format(new Date());


				//	gps = new GPSTracker(FuelActivity.this);
					gps = new GPSTracker(mContext,FuelActivity.this);
					// check if GPS enabled
					if(gps.canGetLocation())
					{

						latitude = gps.getLatitude();
						longitude = gps.getLongitude();
						lati=String.valueOf(latitude);
						longti=String.valueOf(longitude);

					}
					else
					{
						// can't get location
						// GPS or Network is not enabled
						// Ask user to enable GPS/network in settings
						gps.showSettingsAlert();
					}
					db=new DatabaseHandler(getBaseContext());
					sqldb =db.getWritableDatabase();
					ContentValues values = new ContentValues();
					values.put("DATE_TIME", date_time);
					values.put("DRIVERCODE",drivercode);
					values.put("VehicleBarcode", barcodevalue);
					values.put("Odometer_Value", odoread);
					values.put("Odometer_picname","Fuelodo.PNG");
					values.put("Receipt_pic",recnumber);
					values.put("Reciept_no",recamount);
					values.put("Reciept_Amount","FuelReciept.PNG");
					values.put("Latitude", lati);
					values.put("Longtitude", longti);
					values.put("SyncStatus","0");

					sqldb.insertOrThrow("Fueldatatable", null, values);

						//status= WebService.insertfuelread(date_time,drivercode,barcodevalue,odoread,recnumber,recamount,lati,longti, MasterActivity.METHOD_NAME31);
						status= webservice.WebService.SET_FUEL_TRACK(date_time,drivercode,barcodevalue,odoread,recnumber,recamount,lati,longti);
						System.out.println("STATYS IS:"+status);

					if(!errored)
					{
					//if(status)
				//	{

						sqldb.execSQL("UPDATE Fueldatatable SET SyncStatus=1 WHERE SyncStatus=0 AND DRIVERCODE='"+drivercode+"'" );
						sqldb.execSQL("UPDATE Fueldatatable SET Fuelid='"+status+"' WHERE DRIVERCODE='"+drivercode+"'" );
						sqldb.execSQL("UPDATE Fueldatatable SET ImageSyncstatus=0 WHERE DRIVERCODE='"+drivercode+"'" );

						odoimage.setImageDrawable(null);
						recimage.setImageDrawable(null);
						odonumber.setText("");
						result.setText("");
						recnotxt.setText("");
						recamounttxt.setText("");

						Toast.makeText(getApplicationContext(),"Success",Toast.LENGTH_LONG).show();





				//	}

				//	else
				//	{
						//System.out.println("stage2");
						//Set Error message
					//	Toast.makeText(getApplicationContext(),"cannot saved, try again",
							//	Toast.LENGTH_LONG).show();
					//}
					db.close();
					}
					else
					{
						Toast.makeText(getApplicationContext(),"Connection Error,Please try after some time",
								Toast.LENGTH_LONG).show();
					}
					//db.close();

				}
				else if(null==odoimage.getDrawable())
				{
					Toast.makeText(getApplicationContext(),"Please take the pic of odometer",
							Toast.LENGTH_LONG).show();
				}
				else if(odonumber.getText().length()==0)
				{
					Toast.makeText(getApplicationContext(),"Enter the odometer value",
							Toast.LENGTH_LONG).show();
				}
				else if(result.getText().length()==0)
				{
					Toast.makeText(getApplicationContext(),"Please scan Barcode",
							Toast.LENGTH_LONG).show();
				}
				else if(null==recimage.getDrawable())
				{
					Toast.makeText(getApplicationContext(),"Please take pic of receipt",
							Toast.LENGTH_LONG).show();
				}
				else if(recnotxt.getText().length()==0)
				{
					Toast.makeText(getApplicationContext(),"Please enter receipt number",
							Toast.LENGTH_LONG).show();
				}
				else if(recamounttxt.getText().length()==0)
				{
					Toast.makeText(getApplicationContext(),"Please enter amount",
							Toast.LENGTH_LONG).show();
				}
				else
				{
					Toast.makeText(getApplicationContext(),"cannot proceed",
							Toast.LENGTH_LONG).show();
				}

			}
		});


}
/*	@Override
	public void onPause(){
		super.onPause();

		System.out.println("KDCReader on fuel actvity While Pause : " + _kdcReader);
		_kdcReader.Disconnect();
		if(ThrKdc!=null)ThrKdc.interrupt();
		KDCTaskExecutable.cancel(true);
		//if(!tsd.isInterrupted()) tsd.interrupt();
	}
	@Override
	public void onResume()
	{
		super.onResume();
		_activity = this;
		*//*tfuel = new Thread(){
			@Override
			public void run(){
				//_kdcReader= new KDCReader(MasterActivity.ScannerDevice, _activity, _activity, null, null, null, _activity, false);
				_kdcReader= new KDCReader(null, _activity, _activity, null, null, null, _activity, false);
			}
		};
		tfuel.start();*//*
	*//*	if(ThrKdc!=null)
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
		if(!KDCTaskExecutable.getStatus().equals(AsyncTask.Status.RUNNING) && !KDCTaskExecutable.getStatus().equals(AsyncTask.Status.FINISHED)){
			//KDCTaskExecutable.cancel(true);
			KDCTaskExecutable.execute();
			System.out.println("FuelActivity KDCTask Executed");
		}
		System.out.println("Resume activate in FuelActivity");
	}
	*/
	//Camera Scanner calling via volume button disabled

 /*@Override
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
    	
	    }*/
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		System.out.println("requestCode is"+requestCode+"RESULT_OK in fuel"+RESULT_OK+"ddta"+data);

		if (requestCode == 0 && resultCode == RESULT_OK)
		{
				
			
			if (data != null) 
			{

				//photo = (Bitmap) data.getExtras().get("data");

				//odoimage.setImageBitmap(photo); /* this is image view where you want to save image*/
				
				//Log.d("camera ---- > ", "" + data.getExtras().get("data"));
				/*Uri uri = data.getData();
			    InputStream iStream;
				try {
					iStream = getContentResolver().openInputStream(uri);
					 inputData = getBytes(iStream);
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
*/
				/*Bundle extras = data.getExtras();
				// Get the returned image from extra
				System.out.println("extras is"+extras);
				Bitmap bmp = (Bitmap) extras.get("data");

				System.out.println("bmp is"+bmp);*/
				System.out.println("imageView.setImageURI(file);"+uriSavedImage);
			//	getApplicationContext().getContentResolver().delete(data.getData(), null, null);
				//getExternalFilesDir(imagefile1.toString()).delete();
			}
		}
		if (requestCode == 1 && resultCode == RESULT_OK) 
		{
			if (data != null) 
			{

				//photo1 = (Bitmap) data.getExtras().get("data");

				//recimage.setImageBitmap(photo1); /* this is image view where you want to save image*/
				
				/*Uri uri = data.getData();
			    InputStream iStream1;
				try {
					iStream1 = getContentResolver().openInputStream(uri);
					 inputData1 = getBytes(iStream1);
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				*/

				System.out.println("resulting s;"+uriSavedImage+"data"+data);

				//getApplicationContext().getContentResolver().delete(data.getData(), null, null);
				//getExternalFilesDir(imagefile1.toString()).delete();
				//Log.d("camera ---- > ", "" + data.getExtras().get("data"));


			}
		}
		else if (requestCode == SCANNER_REQUEST_CODE) 
		{
				// Handle scan intent

				if (resultCode == Activity.RESULT_OK) {
					// Handle successful scan
					contents = data.getStringExtra("SCAN_RESULT");
					result.setText(contents);									
					
				} else if (resultCode == Activity.RESULT_CANCELED) {
					// Handle cancel
				}
			} else 
			{
				// Handle other intents
			}


		}


/*	@Override
	
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onRestoreInstanceState(savedInstanceState);
		if(Build.MODEL.contains("SM-N9005"))
			{
			
		result.setText(contents);
		odoimage.setImageResource(R.drawable.odo);
		recimage.setImageResource(R.drawable.recp); 
			}
	}
	*/
	
	/*public byte[] getBytes(InputStream inputStream) throws IOException {
	      ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
	      int bufferSize = 1024;
	      byte[] buffer = new byte[bufferSize];

	      int len = 0;
	      while ((len = inputStream.read(buffer)) != -1) {
	        byteBuffer.write(buffer, 0, len);
	      }
	      return1 byteBuffer.toByteArray();
	    }*/
	 // KDC Connection Changed 
	  @Override
	  public void ConnectionChanged(BluetoothDevice device,int state){
		  //ToDo Auto-generated method stub
		  
		  Log.i("KDCReader", "KDC Fuel Activity connection changed block");
		  System.out.print("KDCReader Fuel Activity connection changed block");
		  System.out.print("State is "+state);
		  switch(state){
		  
		  case KDCConstants.CONNECTION_STATE_CONNECTED:
			  _activity.runOnUiThread(new Runnable(){
				  @Override
				  public void run(){
				  
				  Toast.makeText(_activity, "Scanner Connected", Toast.LENGTH_LONG).show();
				  }
			  	});
			  break;
		  
		  case KDCConstants.CONNECTION_STATE_LOST:
			  _activity.runOnUiThread(new Runnable(){
				  @Override
				  public void run(){
					  
					  Toast.makeText(_activity, "Scanner Connection Lost", Toast.LENGTH_LONG).show();
				  }
			  });
			  break;
		  }
	  }
	  // KDC DataReceived 
	  
	  
	  @Override
	  public void DataReceived(KDCData pData){
		  
		  //
	 }
	  
	 // Barcode DataReceived
	  @Override
	  public void BarcodeDataReceived(KDCData pData){
		 
		  Log.i("KDCReader", "KDC Fuel Activity BarCodeReceived Block");
		  System.out.print("KDCReader Fuel Activity  BarCodeReceived Block");
		  
		  		
		  if(pData != null){
			  
			  ScannerData = pData;
			  waybill = ScannerData.GetData();
			 // StartDeliveryActivity.WaybillFromScanner = ScannerData.GetData();
			  
			  if(Check_ValidWaybill(pData.GetData())==true)
			  {
				  
				  System.out.println(" Fuelactivity ID : ");
				 // System.out.println(R.id.WC_Frame);
				  System.out.println(" value for pdata is : ");
				  System.out.println(pData); 

				  _activity.runOnUiThread(new Runnable(){
					  @Override
					  public void run(){
						 if(waybill!=null){
							 //String contents = wbill;
							 //contents = data.getStringExtra("SCAN_RESULT");
											
								
								contents	=waybill;
								result.setText(contents);
								
							 // _activity.ScannerExecutions();
							
							
						 }
						 // wbilldata1=contents;			
						
					  }
				  });
				
			  }
			  else
			  {
				  
				  _activity.runOnUiThread(new Runnable(){
					  @Override
					  public void run(){
						  
						  Toast.makeText(_activity, "Invalid Waybill", Toast.LENGTH_LONG).show();
					  }
				  });
			  }
		  }
		  else
		  {
			  _activity.runOnUiThread(new Runnable(){
				  @Override
				  public void run(){
					  
					  Toast.makeText(_activity, "Invalid Waybill", Toast.LENGTH_LONG).show();
				  }
			  });
		  }
		  
	  }
	  
	  public static boolean Check_ValidWaybill (String s){
			
			if (s.length() == 10 || s.length() == 12)
			{
				return StringUtils.isNumeric(s) == true;
			}
			else if (s.length() == 18||s.length() == 15)
			{
				return StringUtils.isAlphanumeric(s) == true;
			}
			return false;
		}

	@Override
	public void onBarcodeEvent(final BarcodeReadEvent barcodeReadEvent) {

		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				if (barcodeReadEvent != null) {

					//ScannerData = barcodeReadEvent;
					waybill = barcodeReadEvent.getBarcodeData();
					// StartDeliveryActivity.WaybillFromScanner = ScannerData.GetData();

					if (Check_ValidWaybill(barcodeReadEvent.getBarcodeData()) == true) {

						System.out.println(" Fuelactivity ID : ");
						// System.out.println(R.id.WC_Frame);
						System.out.println(" value for barcodeReadEvent is : ");
						System.out.println(barcodeReadEvent);

						_activity.runOnUiThread(new Runnable() {
							@Override
							public void run() {
								if (waybill != null) {
									//String contents = wbill;
									//contents = data.getStringExtra("SCAN_RESULT");


									contents = waybill;
									result.setText(contents);

									// _activity.ScannerExecutions();


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
//	@SuppressWarnings("deprecation")
	/*private String getPath(Uri selectedImaeUri)
	{
		String[] projection = { MediaStore.Images.Media.DATA };

		Cursor cursor = managedQuery(selectedImaeUri, projection, null, null,
				null);

		if (cursor != null)
		{
			cursor.moveToFirst();

			int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

			return cursor.getString(columnIndex);
		}

		return selectedImaeUri.getPath();
	}
	private Bitmap decodeUri(Uri selectedImage) throws FileNotFoundException
	{
		BitmapFactory.Options o = new BitmapFactory.Options();

		o.inJustDecodeBounds = true;

		BitmapFactory.decodeStream(getContentResolver()
				.openInputStream(selectedImage), null, o);

		final int REQUIRED_SIZE = 72;

		int width_tmp = o.outWidth, height_tmp = o.outHeight;

		int scale = 1;

		while (true)
		{
			if (width_tmp / 2 < REQUIRED_SIZE || height_tmp / 2 < REQUIRED_SIZE)
			{
				break;
			}
			width_tmp /= 2;

			height_tmp /= 2;

			scale *= 2;
		}

		BitmapFactory.Options o2 = new BitmapFactory.Options();

		o2.inSampleSize = scale;

		Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver()
				.openInputStream(selectedImage), null, o2);

		return bitmap;
	}*/

	@Override
	public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
		if (requestCode == 0) {
			if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
					&& grantResults[1] == PackageManager.PERMISSION_GRANTED) {

			}
		}
	}
	private static File getOutputMediaFile(){
		File mediaStorageDir = new File(Environment.getExternalStorageDirectory(), "/Postaplus/Fuel&Odoimage");
		//Environment.getExternalStorageDirectory().getAbsolutePath()+"/Postaplus/Fuel&Odoimage"
		if (!mediaStorageDir.exists()){
			if (!mediaStorageDir.mkdirs()){
				return null;
			}
		}

	//	String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
		return new File(mediaStorageDir.getPath() + File.separator +
				"Fuelodo.PNG");
	}
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		InputMethodManager imm = (InputMethodManager)getSystemService(Context.
				INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
		return true;
	}
/*	@Override
	public void onBackPressed(){

		super.onBackPressed();
		barcodeReader.release();
		barcodeReader.removeBarcodeListener(_activity);
		Intent intent = new Intent(FuelActivity.this, HomeActivity.class);
		startActivity(intent);
		finish();
	}*/
}
