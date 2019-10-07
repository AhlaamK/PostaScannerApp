package com.postaplus.postascannerapp;

import android.app.ActionBar;
import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TableRow;
import android.widget.TableRow.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.honeywell.aidc.BarcodeFailureEvent;
import com.honeywell.aidc.BarcodeReadEvent;
import com.honeywell.aidc.BarcodeReader;
import com.honeywell.aidc.TriggerStateChangeEvent;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import koamtac.kdc.sdk.KDCConstants;
import koamtac.kdc.sdk.KDCData;
import koamtac.kdc.sdk.KDCReader;

import static com.postaplus.postascannerapp.HomeActivity.barcodeReader;

public class OdometerActivity extends MasterActivity
implements BarcodeReader.BarcodeListener,BarcodeReader.TriggerListener {
	String contents="",contents1="";
	File imagefile1;
	ImageView odoimage,menuimg;
	Button sub,click,scan;
	EditText odonumber;
	boolean syncstatus,mstatus;
	Bitmap photo; 
	String route,routen,drivercode,odoread,barcodevalue,time_id,rootstatus;
	static boolean errored;
	String odo_status="";
	String status,type,serialid,lati,longti,area;
	GPSTracker gps;
	double latitude,longitude;
	TextView username,result;
	SharedPreferences pref;
	SQLiteDatabase db1 = null;
	DatabaseHandler db;
	Uri uriSavedImage=null;
	public int SCANNER_REQUEST_CODE = 123;
	TableRow tr;
	LayoutParams lp ;
	Thread ThrKdc;
	Bitmap bitmap1;
	Bitmap bitmap2=null;
	//KDC Parameters
		//
		public static String WaybillFromScanner = "";
		public static String KDCScannerCallFrom = "";
		//public static View WCrootView;
		Context mContext;
		Resources _resources;
		BluetoothDevice _btDevice = null;
		static final byte[] TYPE_BT_OOB = "application/vnd.bluetooth.ep.oob".getBytes();
		Button _btnScan = null;

		//BluetoothDevice _btDevice;
		OdometerActivity _activity;
		KDCData ScannerData;
		KDCReader _kdcReader;
		public String chkdata="";
		public String waybill;
		View rootView;
		public OdometerActivity MYActivity;

		public void ScannerExecutions(){
			drivercode=username.getText().toString();
			route= MYActivity.getIntent().getExtras().getString("routecode");
			routen= MYActivity.getIntent().getExtras().getString("routename");
			//waybill=OdometerActivity.WaybillFromScanner;
			//Initializations
			
			if (route == null||getIntent()==null)route= getIntent().getExtras().getString("routecode");
		}
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_odometer);
		 mContext=this;
		ActionBar localActionBar = getActionBar();
		localActionBar.setCustomView(R.layout.wc_actionbar);
		localActionBar.setDisplayShowTitleEnabled(false);
		localActionBar.setDisplayShowCustomEnabled(true);
		localActionBar.setDisplayUseLogoEnabled(false
				);
		localActionBar.setDisplayShowHomeEnabled(false);
		localActionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#1c181c")));
		System.out.println("Odometer Activity Page");
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);
		//KDC Full Commands
	    _activity = this;
	    
	    _resources = getResources();

		barcodeReader.addBarcodeListener(_activity);

		pref = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
		username=(TextView) findViewById(R.id.unametxt);
		username.setText(pref.getString("uname", ""));
		drivercode=username.getText().toString();

		route= getIntent().getExtras().getString("routecode");
		routen= getIntent().getExtras().getString("routename");

		type= getIntent().getExtras().getString("typeodo");
		
		
		odoimage=(ImageView)findViewById(R.id.imageodo);
		sub=(Button)findViewById(R.id.proceed);
		odonumber=(EditText)findViewById(R.id.odometertxt);
		click=(Button)findViewById(R.id.button1);
		menuimg=(ImageView)findViewById(R.id.imageicon);
	//	scan=(Button)findViewById(R.id.btnscan);
		result=(TextView)findViewById(R.id.textbarcode);
		
		imagefile1 = new File(Environment.getExternalStorageDirectory(), "Postaplus/Fuel&Odoimage");
		imagefile1.mkdirs();

		//save odometer value to login database
		db=new DatabaseHandler(getBaseContext());
		//update loginstatus to ZERO when logout
		 db1 =db.getWritableDatabase();
		
		//button to take the picture of odometer
		click.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				v.startAnimation(AnimationUtils.loadAnimation(getBaseContext(), R.anim.image_click));
				
				Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
				odoimage.setImageResource(R.drawable.odo); 	
				//imgcountarr[6]=1;
				SimpleDateFormat date11 = new SimpleDateFormat("HHmm");
				time_id=date11.format(new Date());
				uriSavedImage=Uri.fromFile(new File(imagefile1,  time_id+"ODO.PNG"));
				
				
				
				intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, uriSavedImage);
				startActivityForResult(intent, 0);


			}
		});

		sub.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {


				//tx.setText(num.getText().toString());
				v.startAnimation(AnimationUtils.loadAnimation(getBaseContext(), R.anim.image_click));

				if(null!=odoimage.getDrawable()&&odonumber.getText().length()!=0&&(result.getText().length()!=0 || contents != null))
				{
					
					odoread=odonumber.getText().toString();

					
					
					//convert bitmap to bytearray
				/*	ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();  
					photo.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
					byte[] byteArray = byteArrayOutputStream.toByteArray();*/
				
					barcodevalue=result.getText().toString();
					//call the webservice for saving the value 
					
					if(type.contains("START"))
					{
						System.out.println("START");
						
						//if the type is start save the start value and go to start delivery page
					//	status=WebService.insertodometer(drivercode,odoread,barcodevalue,"START",METHOD_NAME21);
						status= webservice.WebService.SET_DRIVERMETER(drivercode,odoread,barcodevalue,"START");
						
					if(!errored)
					{
					//if(status){
						db1.execSQL("UPDATE logindata SET Odometervalue='"+odoread+"'WHERE Username='"+drivercode+"'" );
						db1.execSQL("UPDATE logindata SET OdometerimgSyncstatus=0 WHERE Username='"+drivercode+"'" );
						db1.execSQL("UPDATE logindata SET Odometerid='"+time_id+"' WHERE Username='"+drivercode+"'" );
						db1.execSQL("UPDATE logindata SET OdometerFileno='"+status+"' WHERE Username='"+drivercode+"'");
						Intent in1 = new Intent(OdometerActivity.this,StartDeliveryActivity.class);
						in1.putExtra("routecode",route);
						in1.putExtra("routename",routen);
						startActivity(in1);
					
					//}
					

					}
					else
					{
						Toast.makeText(MYActivity.getBaseContext(),"Connection Error,Please try after some time", 
								Toast.LENGTH_LONG).show();
					}
					}
						else if(type.contains("END"))
						{
							System.out.println("END");
							//if the type is end save the end value and route close
					//	status=WebService.insertodometer(drivercode,odoread,barcodevalue,"END",METHOD_NAME21);
							status= webservice.WebService.SET_DRIVERMETER(drivercode,odoread,barcodevalue,"END");
					
						
					if(!errored)
					{
					//if(status){
						db1.execSQL("UPDATE logindata SET EndOdometervalue='"+odoread+"'WHERE Username='"+drivercode+"'" );
						db1.execSQL("UPDATE logindata SET OdometerimgSyncstatus=0 WHERE Username='"+drivercode+"'" );
						db1.execSQL("UPDATE logindata SET Odometerid='"+time_id+"' WHERE Username='"+drivercode+"'" );
						db1.execSQL("UPDATE logindata SET OdometerFileno='"+status+"' WHERE Username='"+drivercode+"'");
						
						
						SQLiteDatabase db1 =db.getReadableDatabase();

						Cursor rbc = db1.rawQuery("SELECT * FROM logindata WHERE OdometerimgSyncstatus=0 AND Username='"+drivercode+"'", null);

						//System.out.println("stage1");
						int c=rbc.getCount();
						String odoid=null;
						String odofileno=null;
						//String[] dcode1=new String[c];
						
						
						
						rbc.moveToFirst();
						//	System.out.println("stage2");
						if(c>0){
							//	System.out.println("stage3");
						
								odoid=rbc.getString(rbc.getColumnIndex("Odometerid"));						
								odofileno=rbc.getString(rbc.getColumnIndex("OdometerFileno"));				
								

								//System.out.println("i:"+odoid);
								//System.out.println(odofileno);				
													
								
								byte[] byteArray1 = null;
								byte[] byteArray2 = null;
								
								File sdCardRoot = Environment.getExternalStorageDirectory();
								File yourDir = new File(sdCardRoot, "Postaplus/Fuel&Odoimage");
								for (File f : yourDir.listFiles()) 
								{
									if (f.isFile())
										if(f.getName().contains(odoid))
										{
											System.out.println("odoimage");
											
											ByteArrayOutputStream baos = new ByteArrayOutputStream();
											FileInputStream fis;
											
											try {
												fis = new FileInputStream(new File(yourDir+"/"+f.getName()));
												byte[] buf = new byte[1024];
												int n;
												while (-1 != (n = fis.read(buf)))
												    baos.write(buf, 0, n);

												//byteArray1 = baos.toByteArray();
												BitmapFactory.Options options=new BitmapFactory.Options();
												options.inSampleSize =23;
												bitmap1 = BitmapFactory.decodeFile(f.getAbsoluteFile().toString(),options);
												System.out.println("bitmaps odoactv kb in sync is :"+bitmap1.getByteCount()/1024);
											
											} catch (FileNotFoundException e) {
												// TODO Auto-generated catch block
												e.printStackTrace();
											} catch (IOException e) {
												// TODO Auto-generated catch block
												e.printStackTrace();
											}
										}
										 
									
								}
							System.out.println("Called syncstatus from odometer activity");
										//	syncstatus=WebService.syncfuelimage(odofileno,byteArray1,"odo",byteArray2,"END",METHOD_NAME38);
											syncstatus= webservice.WebService.SET_ODO_FUEL_IMAGE(odofileno,bitmap1,"odo",bitmap2,"END");
										
									
										

								
								
								if(!errored)
								{
									if(syncstatus)
									{
										db1.execSQL("UPDATE logindata SET OdometerimgSyncstatus=1 WHERE Username='"+drivercode+"'" );
										
									//	rootstatus=WebService.routeclose(drivercode,route,METHOD_NAME19);
										rootstatus= webservice.WebService.SET_COURIERROUTECLOSE(drivercode,route);

										if(!errored)
										{
											//update loginstatus to ZERO when logout
											//SQLiteDatabase db1 =db.getWritableDatabase();
											if(rootstatus.equals("TRUE"))
											{
												db1.execSQL("UPDATE logindata SET Loginstatus=0,Routecode="+null+",Runsheetcode="+null+",Odometervalue="+null+" WHERE Username='"+drivercode+"'");

												Toast.makeText(getApplicationContext(), "Route closed", Toast.LENGTH_SHORT).show();

												TelephonyManager telephonyManager  =  
														( TelephonyManager )getSystemService( Context.TELEPHONY_SERVICE );
												serialid= telephonyManager.getDeviceId();

												gps = new GPSTracker(mContext,OdometerActivity.this);

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
												}catch (IOException e) {
													// TODO Auto-generated catch block
													e.printStackTrace();
												}
												int login=0;

											//	mstatus=WebService.Setdevicestatus(drivercode,serialid,lati,longti,login,area,METHOD_NAME30);
												mstatus= webservice.WebService.SET_DEVICE_STATUS(drivercode,serialid,lati,longti,String.valueOf(login),area="");
												if(mstatus)
													//System.out.println("success");



												//Intent intent = new Intent(getBaseContext(), PostaNotificationService.class);
												//stopService(intent);

												pref = getSharedPreferences(LoginActivity.MyPREFERENCES, Context.MODE_PRIVATE);
												Editor editor = pref.edit();
												editor.clear();
												editor.commit();
												clearApplicationData();
												OdometerActivity.this.finish();

												startActivity(new Intent(OdometerActivity.this,LoginActivity.class));
											}

											else{
												 _activity.runOnUiThread(new Runnable(){
													  @Override
													  public void run(){
														  
													//	Toast.makeText(MYActivity.getApplicationContext(), "Route cannot close now", Toast.LENGTH_SHORT).show();
														  Toast.makeText(MYActivity.getApplicationContext(), rootstatus, Toast.LENGTH_SHORT).show();
													  }
												  });
												
												//Toast.makeText(MYActivity.getApplicationContext(), "Route cannot close now", Toast.LENGTH_SHORT).show();
											}
											
										}
										else
										{
											Toast.makeText(MYActivity.getApplicationContext(), "Connection ERROR", Toast.LENGTH_SHORT).show();
										}
										
										
									}
								}
								else
								{
									
									//error go to home page
									Intent in1 = new Intent(OdometerActivity.this,HomeActivity.class);
									in1.putExtra("route",route);
									in1.putExtra("route1",routen);
									startActivity(in1);
									Toast.makeText(MYActivity.getApplicationContext(), "Connection ERROR", Toast.LENGTH_SHORT).show();
								}



							
						}
						rbc.close();			
					/*	if(syncstatus)
						{
							Toast.makeText(getApplicationContext(), "ODO image ack done Successfully", Toast.LENGTH_SHORT).show();

						}
					
						else
						{
							Toast.makeText(getApplicationContext(), "odo image ack error", Toast.LENGTH_SHORT).show();

						}*/
						
						
					//}
					

					}
					else
					{
						Toast.makeText(MYActivity.getBaseContext(),"Connection Error,Please try after some time", 
								Toast.LENGTH_LONG).show();
					}
					}
					db.close();
					
				}
				else if(null==odoimage.getDrawable())
				{
					/*MYActivity.runOnUiThread(new Runnable(){
					  @Override
					  public void run(){
						  
						  Toast.makeText(MYActivity.getApplicationContext(),"Please take the pic of odometer", 
							Toast.LENGTH_LONG).show();
					  }
				  });*/
					
					
					Toast.makeText(MYActivity.getApplicationContext(),"Please take the pic of odometer", 
							Toast.LENGTH_LONG).show();
				}
				else if(odonumber.getText().length()==0)
				{
					Toast.makeText(MYActivity.getApplicationContext(),"Enter the odometer value", 
							Toast.LENGTH_LONG).show();
				}  
				else if(result.getText().length()==0)
				{
					/*MYActivity.runOnUiThread(new Runnable(){
						  @Override
						  public void run(){
							  
							  Toast.makeText(MYActivity.getApplicationContext(),"Please scan Barcode", 
							Toast.LENGTH_LONG).show();
						  }
					  });*/
					Toast.makeText(getApplication().getApplicationContext(),"Please scan Barcode", 
							Toast.LENGTH_LONG).show();
				}  
				else
				{
					Toast.makeText(MYActivity.getApplicationContext(),"cannot proceed", 
							Toast.LENGTH_LONG).show();
				}

			}

			private void clearApplicationData() {
				// TODO Auto-generated method stub
				File cache = getCacheDir();
			       File appDir = new File(cache.getParent());
			       if (appDir.exists()) {
			           String[] children = appDir.list();
			           for (String s : children) {
			               if (!s.equals("lib")) {
			                   deleteDir(new File(appDir, s));Log.i("TAG", " File /data/data/com.post.postaplusandroidapp/" + s + " DELETED ***");
			               }
			           }
			       }
			}

			public boolean deleteDir(File dir) 
			   {
			       if (dir != null && dir.isDirectory()) {
			           String[] children = dir.list();
			           for (int i = 0; i < children.length; i++) {
			               boolean success = deleteDir(new File(dir, children[i]));
			               if (!success) {
			                   return false;
			               }
			           }
			       }
			       return dir.delete();
			   }
		});
	/*	scan.setOnClickListener(new OnClickListener() {

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
		}); 
*/
		//back to delivery page
		menuimg.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				v.startAnimation(AnimationUtils.loadAnimation(getBaseContext(), R.anim.image_click));

				OdometerActivity.this.finish();
				Intent intent = new Intent(OdometerActivity.this, DeliveryActivity.class);
				intent.putExtra("routecode",route);
				intent.putExtra("routename",routen);

				startActivity(intent);
			}
		});

	}
	/*@Override
	public void onPause(){
		super.onPause();
		System.out.println("KDCReader on Start Delivery While Pause : " + _kdcReader);
		_kdcReader.Disconnect();
      ThrKdc.interrupt();
	//	if(!tod.isInterrupted()) tod.interrupt();
	}*/
	/*@Override
	public void onResume()
	{
		super.onResume();
		_activity = this;
		 *//*tod = new Thread(){
			@Override
			public void run(){
				//_kdcReader= new KDCReader(MasterActivity.ScannerDevice, _activity, _activity, null, null, null, _activity, false);
				_kdcReader= new KDCReader(null, _activity, _activity, null, null, null, _activity, false);
			}
		};
		tod.start();*//*
		if(ThrKdc!=null)
			ThrKdc.run();
		else{
			ThrKdc = new Thread(){
				@Override
				public void run(){
					//_kdcReader= new KDCReader(MasterActivity.ScannerDevice, _activity, _activity, null, null, null, _activity, false);
					_kdcReader= new KDCReader(null, _activity, _activity, null, null, null, _activity, false);
				}
			};
			ThrKdc.start();
		}
		System.out.println("Resume activate in startdeliveryactivity");
	}*/
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
		//odoimage.setImageResource(R.drawable.odo); 	
		//result.setText(contents);	

		
	}
	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onRestoreInstanceState(savedInstanceState);
		if(Build.MODEL.contains("SM-N"))
			{
				
		odoimage.setImageResource(R.drawable.odo); 	
		result.setText(contents);	
			}
	}
	@Override
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
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 0 && resultCode == RESULT_OK) 
		{
			if (data != null) 
			{

				//photo = (Bitmap) data.getExtras().get("data");

				//odoimage.setImageBitmap(photo); /* this is image view where you want to save image*/
				getApplicationContext().getContentResolver().delete(data.getData(), null, null);
			//	Log.d("camera ---- > ", "" + data.getExtras().get("data"));


			}
		}
		
		else if (requestCode == SCANNER_REQUEST_CODE) 
		{
				// Handle scan intent

				if (resultCode == Activity.RESULT_OK) {
					// Handle successful scan
					contents = data.getStringExtra("SCAN_RESULT");
				//	odo_status=WebService.checkvalidodometer(drivercode, contents, METHOD_NAME44);
					odo_status= webservice.WebService.CHECK_VHCLBARCODE(drivercode, contents);
					/*System.out.println("drivercode value in odo actv is");
					System.out.println(drivercode);
					System.out.println("contents value in odo actv is");
					System.out.println(contents);
					System.out.println("METHOD_NAME44 value in odo actv is");
					System.out.println(METHOD_NAME44);
					System.out.println("odo_status value in odo actv is");
					System.out.println(odo_status);*/
					
					if(odo_status.equals("VALID"))
					{
					result.setText(contents);	
					
					}
					else
					{
						Toast.makeText(getBaseContext(),"Not A Valid Barcode!!", Toast.LENGTH_LONG).show();
					}
					
				} else if (resultCode == Activity.RESULT_CANCELED) {
					// Handle cancel
				}
			} else 
			{
				// Handle other intents
			}

		}
	 // KDC Connection Changed 
	//  @Override
	  public void ConnectionChanged(BluetoothDevice device,int state){
		  //ToDo Auto-generated method stub
		  
		  Log.i("KDCReader", "KDC Odometer Activity connection changed block");
		  System.out.print("KDCReader Odometer Activity connection changed block");
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
	  
	  
	 // @Override
	  public void DataReceived(KDCData pData){
		  
		  //
	 }
	  
	 // Barcode DataReceived
	 // @Override
	  public void BarcodeDataReceived(KDCData pData){
		 
		  Log.i("KDCReader", "KDC Odometer Activity BarCodeReceived Block");
		  System.out.print("KDCReader Odometer Activity  BarCodeReceived Block");
		  
		  		
		  if(pData != null){
			 
			  ScannerData = pData;
			  waybill=ScannerData.GetData();	
			  // if(checkValidWaybill(pData.GetData())==true)
			  {
				  
				  System.out.println(" OdometerActivity ID : ");
				 // System.out.println(R.id.WC_Frame);
				  System.out.println(" value for pdata is : ");
				  System.out.println(pData); 
				  
	  
				//if(MYActivity==null) return1;
				//else
				  _activity.runOnUiThread(new Runnable(){
					  @Override
					  public void run(){
						  
						  result=(TextView)findViewById(R.id.textbarcode);
						  contents = waybill;
						  
					//	  odo_status=WebService.checkvalidodometer(drivercode, contents, METHOD_NAME44);
						  odo_status= webservice.WebService.CHECK_VHCLBARCODE(drivercode, contents);
						  	System.out.println("drivercode value in odo actv is");
							System.out.println(drivercode);
							System.out.println("contents value in odo actv is");
							System.out.println(contents);
							System.out.println("METHOD_NAME44 value in odo actv is");
							System.out.println(METHOD_NAME44);
							System.out.println("odo_status value in odo actv is");
							System.out.println(odo_status);
							if(odo_status.equals("VALID"))
							{
								
								
								if(Build.MODEL.contains("SM-N"))
								{ 
									lp = new LayoutParams(255,LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
									//tr.setLayoutParams(lp);
									lp.setMargins(0, 20, 70, 0);
									
									result.setText(contents);
									  result.setVisibility(View.VISIBLE);
									  
									  _activity.runOnUiThread(new Runnable(){
									  @Override
									  public void run(){
										 Toast.makeText(getBaseContext()," Scan Sucessful!!", Toast.LENGTH_LONG).show();
										 
										 
									  }
									  
									  
										});
									  
									  //	Toast.makeText(getBaseContext()," Image Captured!!", Toast.LENGTH_LONG).show();	
								
								}
								/*_activity.runOnUiThread(new Runnable(){
									  @Override
									  public void run(){
										 
										  result.setText(contents);
										  result.setVisibility(View.VISIBLE);
										  	Toast.makeText(getBaseContext()," Valid Barcode!!", Toast.LENGTH_LONG).show();
										 
									  }
									  
									  
										});
								*/
								
								/*System.out.println("Barcode Txt Box : ");
								System.out.println(result);
								result.setText(contents);
								//result.setText(String.valueOf(ScannerData.GetData()));
							    result.setVisibility(View.VISIBLE);
								String obja = result.getText().toString();
								System.out.println("Barcode txt get value : ");
								System.out.println(obja);
							     
							     */
								/*//back to delivery page
								menuimg.setOnClickListener(new OnClickListener() {

									@Override
									public void onClick(View v) {
										v.startAnimation(AnimationUtils.loadAnimation(getBaseContext(), R.anim.image_click));

										OdometerActivity.this.finish();
										Intent intent = new Intent(OdometerActivity.this, DeliveryActivity.class);
										intent.putExtra("routecode",route);
										intent.putExtra("routename",routen);

										startActivity(intent);
									}
								});
*/
							
								 
							}
							else
							{ 
								_activity.runOnUiThread(new Runnable(){
									  @Override
									  public void run(){
										 
								
										  	Toast.makeText(getBaseContext(),"Not A Valid Barcode!!", Toast.LENGTH_LONG).show();
						
									  			}
										});
							}
					  }
				  });
				  }}
				 
				  
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


	  
	 /* public static boolean checkValidWaybill (String s){
			
			if (s.length() == 10 || s.length() == 12)
			{
				if (StringUtils.isNumeric(s) == true) 
						return1 true;
				else
						return1 false;
			}
			else if (s.length() == 18||s.length() == 15)
			{
				if (StringUtils.isAlphanumeric(s) == true) 
					return1 true;
				else
					return1 false;
			}
			return1 false;
		}*/


	@Override
	public void onBarcodeEvent(BarcodeReadEvent barcodevent) {

		System.out.print("barcodevent Odometer Activity  BarCodeReceived Block");


		if(barcodevent != null){


			waybill=barcodevent.getBarcodeData();
			// if(checkValidWaybill(pData.GetData())==true)
			{

				System.out.println(" OdometerActivity ID : ");
				// System.out.println(R.id.WC_Frame);
				System.out.println(" value for barcodevent is : ");
				System.out.println(barcodevent);


				//if(MYActivity==null) return1;
				//else
				_activity.runOnUiThread(new Runnable(){
					@Override
					public void run(){

						result=(TextView)findViewById(R.id.textbarcode);
						contents = waybill;

						//	  odo_status=WebService.checkvalidodometer(drivercode, contents, METHOD_NAME44);
						odo_status= webservice.WebService.CHECK_VHCLBARCODE(drivercode, contents);
						System.out.println("drivercode value in odo actv is");
						System.out.println(drivercode);
						System.out.println("contents value in odo actv is");
						System.out.println(contents);
						System.out.println("METHOD_NAME44 value in odo actv is");
						System.out.println(METHOD_NAME44);
						System.out.println("odo_status value in odo actv is");
						System.out.println(odo_status);
						if(odo_status.equals("VALID"))
						{


							if(Build.MODEL.contains("SM-N"))
							{
								lp = new LayoutParams(255,LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
								//tr.setLayoutParams(lp);
								lp.setMargins(0, 20, 70, 0);

								result.setText(contents);
								result.setVisibility(View.VISIBLE);

								_activity.runOnUiThread(new Runnable(){
									@Override
									public void run(){
										Toast.makeText(getBaseContext()," Scan Sucessful!!", Toast.LENGTH_LONG).show();


									}


								});

								//	Toast.makeText(getBaseContext()," Image Captured!!", Toast.LENGTH_LONG).show();

							}



						}
						else
						{
							_activity.runOnUiThread(new Runnable(){
								@Override
								public void run(){


									Toast.makeText(getBaseContext(),"Not A Valid Barcode!!", Toast.LENGTH_LONG).show();

								}
							});
						}
					}
				});
			}}


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

	@Override
	public void onFailureEvent(BarcodeFailureEvent barcodeFailureEvent) {

	}

	@Override
	public void onTriggerEvent(TriggerStateChangeEvent triggerStateChangeEvent) {

	}
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		InputMethodManager imm = (InputMethodManager)getSystemService(Context.
				INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
		return true;
	}
	@Override
	public void onPause() {
		super.onPause();
		barcodeReader.removeBarcodeListener(_activity);
		barcodeReader.release();
		_activity.finish();
	}
	}
	  

