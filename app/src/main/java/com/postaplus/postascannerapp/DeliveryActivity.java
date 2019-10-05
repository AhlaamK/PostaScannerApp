package com.postaplus.postascannerapp;


import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.FragmentTransaction;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.honeywell.aidc.BarcodeFailureEvent;
import com.honeywell.aidc.BarcodeReadEvent;
import com.honeywell.aidc.BarcodeReader;
import com.honeywell.aidc.ScannerUnavailableException;
import com.honeywell.aidc.TriggerStateChangeEvent;

import org.apache.commons.lang3.StringUtils;

import TabsPagerAdapter.TabsPagerAdapter;
import koamtac.kdc.sdk.KDCConstants;
import koamtac.kdc.sdk.KDCData;
import koamtac.kdc.sdk.KDCReader;

import static com.postaplus.postascannerapp.HomeActivity.barcodeReader;


public class  DeliveryActivity extends FragmentActivity implements
        BarcodeReader.BarcodeListener,BarcodeReader.TriggerListener,
        ActionBar.TabListener {

    private ViewPager viewPager;
    private TabsPagerAdapter mAdapter;
    private ActionBar actionBar;

    android.support.v4.app.FragmentTransaction transaction;


    //
    public static String WaybillFromScanner = "";
    public static String KDCScannerCallFrom = "";
    public static View WCrootView;
    public static FragmentActivity WCActivity;
    public static View TArootView;
    public static FragmentActivity TAActivity;
    //KDCTask KDCTaskExecutable = new KDCTask();
    public boolean isActivityActiveFlag=false;
    static boolean DonotInterruptKDCScan = true;
    static int scandisconFlag=0;

    //public static
    String route,routen;

    Resources _resources;

    //final static  String btDevice ="00:19:01:47:D4:42";
//	BluetoothDevice _btDevice;
    static final byte[] TYPE_BT_OOB = "application/vnd.bluetooth.ep.oob".getBytes();
    Button _btnScan = null;

    //BluetoothDevice _btDevice;
    DeliveryActivity _activity;
    KDCData ScannerData;
    KDCReader _kdcReader;
    Thread ThrKdc;


    // Tab titles
    private String[] tabs = { "WC", "Print", "Transfer Accept" };
    SharedPreferences pref;

    TextView username;
    DatabaseHandler db;
    SQLiteDatabase sqldb;



/*	public class KDCTask extends AsyncTask<Void, Void, String> {

		@Override
		protected String doInBackground(Void...arg0){

			if(ThrKdc!=null) {
				ThrKdc.run();
			}
			else {

				ThrKdc = new Thread() {
					@Override
					public void run() {
					//	_kdcReader = new KDCReader(null, _activity, _activity, null, null, null, _activity, false);
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
	}*/

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {

            if(barcodeReader!=null){
                barcodeReader.release();
                barcodeReader.removeBarcodeListener(DeliveryActivity.this);
            }

            Intent intent = new Intent(DeliveryActivity.this, HomeActivity.class);
            intent.putExtra("route", route);
            intent.putExtra("route1", routen);

            startActivity(intent);
	/*		if(!isActivityActiveFlag)
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
				//KDCTaskExecutable.cancel(true);
				Intent intent = new Intent(DeliveryActivity.this, HomeActivity.class);
				intent.putExtra("route", route);
				intent.putExtra("route1", routen);

				startActivity(intent);

				return true;
			}

		}
		return false;*/
        }
        return false;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_delivery);
        ActionBar localActionBar = getActionBar();
        localActionBar.setCustomView(R.layout.actionbar_layout);
        localActionBar.setDisplayShowTitleEnabled(false);
        localActionBar.setDisplayShowCustomEnabled(true);
        localActionBar.setDisplayUseLogoEnabled(true);
        localActionBar.setDisplayShowHomeEnabled(false);
        localActionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#1c181c")));
        //localActionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM | ActionBar.DISPLAY_SHOW_HOME);
        System.out.println("Delivery Activity Page");

        //for networkonmainthreadexception
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        route = getIntent().getExtras().getString("routecode");
        routen = getIntent().getExtras().getString("routename");

        //KDC Full Commands
        _activity = this;

        _resources = getResources();
        if (barcodeReader!= null){
            barcodeReader.addBarcodeListener(this);

        }

	   
	     /*ThrKdc = new Thread(){
		    	@Override
		    	 public void run(){

					_kdcReader= new KDCReader(null, _activity, _activity, null, null, null, _activity, false);
		    		MasterActivity.ScannerDevice = _kdcReader.GetBluetoothDevice();
					_kdcReader.EnableBluetoothWakeupNull(true);

		    	}
		    };
		    ThrKdc.start();*/
        //new KDCTask().execute();
        //  KDCTaskExecutable.execute();

        //	String name= getIntent().getExtras().getString("uname");
        username=(TextView) findViewById(R.id.unametxt);


        pref = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        username.setText(pref.getString("uname", ""));


        // Initilization
        viewPager = (ViewPager) findViewById(R.id.pager);
        actionBar = getActionBar();
        mAdapter = new TabsPagerAdapter(getSupportFragmentManager());

        viewPager.setAdapter(mAdapter);
        actionBar.setHomeButtonEnabled(false);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        // Adding Tabs
        for (String tab_name : tabs) {
            actionBar.addTab(actionBar.newTab().setText(tab_name)
                    .setTabListener(this));
        }

        /**
         * on swiping the viewpager make respective tab selected
         * */
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                // on changing the page
                // make respected tab selected
                actionBar.setSelectedNavigationItem(position);
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
            }
        });
    }
    /*@Override
    public void onResume()
    {
        super.onResume();
        if(!isActivityActiveFlag) isActivityActiveFlag=false;
        _activity = this;
        *//*tda = new Thread(){
			@Override
			public void run(){
				//_kdcReader= new KDCReader(MasterActivity.ScannerDevice, _activity, _activity, null, null, null, _activity, false);
				_kdcReader= new KDCReader(null, _activity, _activity, null, null, null, _activity, false);
			}
		};
		tda.start();*//*
		System.out.println("THRKDC in Resume activated on deliveryactivity"+ThrKdc);
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
		//new KDCTask().execute();
      *//*  if(!KDCTaskExecutable.getStatus().equals(AsyncTask.Status.RUNNING) && !KDCTaskExecutable.getStatus().equals(AsyncTask.Status.FINISHED)){
            //KDCTaskExecutable.cancel(true);
            KDCTaskExecutable.execute();
            System.out.println("DeliveryActivity KDCTask Executed");
        }*//*

		System.out.println("Resume activate in deliveryactivity");
	}*/
    //@Override
/*	public void onPause(){
		super.onPause();
		if(!isActivityActiveFlag) isActivityActiveFlag=false;
		System.out.println("KDCReader on Start Delivery While Pause : " + _kdcReader);
		if(!DonotInterruptKDCScan){
			if(ThrKdc!=null) {
				if(_kdcReader!=null)_kdcReader.Disconnect();
				if(ThrKdc!=null)ThrKdc.interrupt();
			//	KDCTaskExecutable.cancel(true);
				System.out.println("THRKDC in pause activated on deliveryactivity"+ThrKdc);
		}
		}else{
			if(scandisconFlag==1)
			{
				DonotInterruptKDCScan = true;
			}

			else  DonotInterruptKDCScan = false;
		}


	//	if(!tda.isInterrupted()) tda.interrupt();
	}*/
    @Override
    public void onTabReselected(Tab tab, FragmentTransaction ft) {
        System.out.println("Tab one is"+tab);

    }

    @Override
    public void onTabSelected(Tab tab, FragmentTransaction ft) {
        // on tab selected
        // show respected fragment view
        viewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(Tab tab, FragmentTransaction ft) {
        System.out.println("Tab three is"+tab);

    }

    // KDC Connection Changed
//  @Override
    public void ConnectionChanged(final BluetoothDevice device, int state){
        //ToDo Auto-generated method stub

        Log.i("KDCReader", "KDC WC Connection Changed Block");
        System.out.print("KDCReader WC Connection Changed Block");
        System.out.print("State is "+state);
        System.out.print("kdc reader device is "+device);
        switch(state){

            case KDCConstants.CONNECTION_STATE_CONNECTED:
                _activity.runOnUiThread(new Runnable(){
                    @Override
                    public void run(){


                        Toast.makeText(_activity, "Scanner Connected", Toast.LENGTH_LONG).show();
                        isActivityActiveFlag=true;
                    }
                });
                break;

            case KDCConstants.CONNECTION_STATE_LOST:
                _activity.runOnUiThread(new Runnable(){
                    @Override
                    public void run(){

                        Toast.makeText(_activity, "Scanner Connection Lost", Toast.LENGTH_LONG).show();
                        isActivityActiveFlag=true;
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
//  @Override
 /* public void BarcodeDataReceived(KDCData pData){
	 
	  Log.i("KDCReader", "KDC WC BarCodeReceived Block");
	  System.out.print("KDCReader WC BarCodeReceived Block");
	  
	  		
	  if(pData != null){
		  
		  ScannerData = pData;
		  DeliveryActivity.WaybillFromScanner = ScannerData.GetData();
		  
		  if(Check_ValidWaybill(pData.GetData())==true)
		  {
			  
			  System.out.println(" - WC Constant ID : ");
	//		  System.out.println(R.id.WC_Frame);
			  System.out.println(" value for pdata is : ");
			  System.out.println(pData); 
//			  Delivery_wc_fragment fragment = (Delivery_wc_fragment)getSupportFragmentManager().findFragmentById(R.id.buttonWC);
//			  fragment.ScannerWCExecutions(); 
			 // Delivery_wc_fragment frag;
//			  FragmentManager fm1 = DeliveryActivity.this
//			      .getSupportFragmentManager();
//			  android.support.v4.app.FragmentTransaction ft1 = fm1.beginTransaction();
			//  frag = new Delivery_wc_fragment();
			//  ft1.replace(R.id.WC_Frame, frag);
			//  ft1.commit();
			   
			// frag.ScannerWCExecutions();
	
//			 Delivery_wc_fragment fragment = (Delivery_wc_fragment) getSupportFragmentManager().findFragmentById(R.id.WC_Frame); 
//			 fragment.ScannerWCExecutions();; 
			  
			  if(KDCScannerCallFrom=="WCFragment")
			  {
			  _activity.runOnUiThread(new Runnable(){
				  @Override
				  public void run(){
					  
					  Delivery_wc_fragment fragment = new Delivery_wc_fragment();
						 //getSupportFragmentManager().beginTransaction().replace(R.id.WC_Frame, fragment).commit();
						 System.out.println(" value for frag is : ");
						  System.out.println(fragment);
						  //fragment.chkdata=.GetData();
						 fragment.ScannerWCExecutions();
					
					
				  }
			  });
			  } 
			  else if(KDCScannerCallFrom=="TAFragment")
			  {
			  _activity.runOnUiThread(new Runnable(){
				  @Override
				  public void run(){
					  
					 				
						 Delivery_ta_fragment fragment2 = new Delivery_ta_fragment();
						 //getSupportFragmentManager().beginTransaction().replace(R.id.WC_Frame, fragment).commit();
						 System.out.println(" value for frag is : ");
						  System.out.println(fragment2);
						  //fragment.chkdata=.GetData();
						 fragment2.ScannerTAExecutions();  
					  
				  }
			  });
			  }
			 *//*Delivery_wc_fragment fragment = new Delivery_wc_fragment();
			 //getSupportFragmentManager().beginTransaction().replace(R.id.WC_Frame, fragment).commit();
			 System.out.println(" value for frag is : ");
			  System.out.println(fragment);
			  fragment.chkdata=pData.GetData();
			 fragment.ScannerWCExecutions();*//*

     *//* if(Delivery_wc_fragment.TAG=="Delivery_wc_fragment")
			  {
				    _activity.runOnUiThread(new Runnable(){
					  @Override
					  public void run(){
						  Delivery_wc_fragment fragment = (Delivery_wc_fragment)getSupportFragmentManager().findFragmentById(R.id.buttonWC);
						  fragment.ScannerWCExecutions();  
					  }
				  });
			  }*//*
//			  Delivery_wc_fragment fragment = (Delivery_wc_fragment)getSupportFragmentManager().findFragmentById(R.id.buttonWC);
//			  fragment.ScannerWCExecutions();  
			  
			  //Delivery_wc_fragment instanceFragment = ((Delivery_wc_fragment) getSupportFragmentManager().findFragmentByTag("Frag_WC_tag"));
			//   inst=manager.findFragmentById(R.id.WC_Frame);
//			  Delivery_wc_fragment fragment = (Delivery_wc_fragment)manager.findFragmentById(R.id.WC_Frame);
//			  fragment.ScannerWCExecutions();  
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
	  
  }*/

    public static boolean Check_ValidWaybill (String s){

        if (s.length() == 10 || s.length() == 12)
        {
            return StringUtils.isNumeric(s) == true;
        }
        else if (s.length() == 18)
        {
            return StringUtils.isAlphanumeric(s) == true;
        }
        return false;
    }
    @Override
    public void onBarcodeEvent(final BarcodeReadEvent event) {
        DeliveryActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                String barcodeData = event.getBarcodeData();
                // update UI to reflect the data

                System.out.println("barcodeData del is:" + barcodeData);


                if (barcodeData != null) {


                    DeliveryActivity.WaybillFromScanner = event.getBarcodeData();

                    if (Check_ValidWaybill(event.getBarcodeData()) == true) {

                        System.out.println(" - WC Constant ID : ");
                        //		  System.out.println(R.id.WC_Frame);
                        System.out.println(" value for barcodeData is : ");
                        System.out.println(barcodeData);


                        if (KDCScannerCallFrom == "WCFragment") {
                            _activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {

                                    final Delivery_wc_fragment fragment = new Delivery_wc_fragment();
                                    //getSupportFragmentManager().beginTransaction().replace(R.id.WC_Frame, fragment).commit();
                                    System.out.println(" value for frag is : ");
                                    System.out.println(fragment);
                                    //fragment.chkdata=.GetData();

                                    fragment.startASycncScanerExecute();




                                }
                            });


                        } else if (KDCScannerCallFrom == "TAFragment") {
                            _activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {


                                    Delivery_ta_fragment fragment2 = new Delivery_ta_fragment();
                                    //getSupportFragmentManager().beginTransaction().replace(R.id.WC_Frame, fragment).commit();
                                    System.out.println(" value for frag is : ");
                                    System.out.println(fragment2);
                                    //fragment.chkdata=.GetData();
                                    //  fragment2.ScannerTAExecutions();
                                    fragment2.startASycncScanerTAExecute();

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
    @Override
    public void onResume(){  //will always? be called before app becomes visible?
        super.onResume();
        if ( barcodeReader!= null) {
            try {
                barcodeReader.claim();
                System.out.println("barcode onres:"+barcodeReader);
            } catch (ScannerUnavailableException e) {
                e.printStackTrace();
                Toast.makeText(this, "Scanner unavailable", Toast.LENGTH_SHORT).show();
            }
        }
    }
  /*  @Override
    public void onPause() {
        super.onPause();
        if (barcodeReader!= null){
            barcodeReader.removeBarcodeListener(DeliveryActivity.this);
            barcodeReader.release();
            barcodeReader.removeBarcodeListener(_activity);
        }
     //  _activity.finish();
    }*/
   /* public void onPause(){
        super.onPause();
        if(scandisconFlag==1)
        {
            DonotInterruptKDCScan = true;
        }

        else  DonotInterruptKDCScan = false;
    }*/

    @Override
    public void onStop(){
        super.onStop();
        if (barcodeReader!= null){
            //  _activity.runOnUiThread(new Runnable() {
            //    @Override
            // public void run() {

            barcodeReader.removeBarcodeListener(_activity);
            barcodeReader.release();
        }
        // });

        //  }
        //_activity.finish();
    }


}