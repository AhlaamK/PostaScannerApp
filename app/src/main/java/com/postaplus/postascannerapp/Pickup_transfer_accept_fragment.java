package com.postaplus.postascannerapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TableRow.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;



public class Pickup_transfer_accept_fragment extends Fragment {
	Button back,scan,transfer;
	String drivercode,route,routen,pick_res,date;
	ProgressBar Pb;
	SharedPreferences pref;
	Long var1;
	public int SCANNER_REQUEST_CODE = 123;
	TableLayout  resulttab;
	TableRow tr;
	LayoutParams lp ;
	//SoapObject response;
	String pickupno12,wbill12,paytype12,amount12,service12,msg="",paytype,servicetype;
	DatabaseHandler db;
	SQLiteDatabase sqldb;
	static boolean errored = false;
	String waybill;
	int count;
	TextView picnotxt;
	int flag=0;
	View rootView;
	// KDC Parameters
    public String chkdata="";

	public FragmentActivity MYActivity;
		
	public void ScannerPTAExecutions()
	{
		System.out.println(" pdata new value in chkdata is ");
		 System.out.println(chkdata);
		  
		MYActivity = PickupActivity.PTAActivity;
		rootView = PickupActivity.PTArootView;
		
		
		//routen= MYActivity.getIntent().getExtras().getString("routename");
		waybill= PickupActivity.WaybillFromScanner;
		
		if(db==null||getActivity()==null) db=new DatabaseHandler(getActivity().getBaseContext());
		 System.out.println("getActivity value is");
			System.out.println(getActivity());
			System.out.println("Db value before is");
			System.out.println(db);
		//open localdatabase in a read mode
		sqldb = db.getReadableDatabase();
		
		//select  loginstatus and routecode on resume activity 
		Cursor c1 = sqldb.rawQuery("SELECT Username FROM logindata WHERE Loginstatus=1", null);
		c1.moveToFirst();
		if(c1.getCount()>0){
			drivercode=c1.getString(c1.getColumnIndex("Username"));
		c1.close();
		System.out.println("Db value is");
		System.out.println(db);
		}
		
		if(count>0)
		{
		
			MYActivity.runOnUiThread(new Runnable(){
				@Override
				public void run(){
					
				}
			});

			//tr.addView(waybilltxt);
			//tr.addView(rnametxt);
			//tr.addView(cnametxt);
			//tr.addView(amounttxt);
		
		}
		else
		{
			
			new Task1().execute();	
		}
		

	}

	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
        View rootView = inflater.inflate(R.layout.activity_pickup_accept_frag, container, false);
        resulttab=(TableLayout)rootView.findViewById(R.id.tlTable);
		back=(Button)rootView.findViewById(R.id.btnbck);
		//scan=(Button)rootView.findViewById(R.id.btnscan);
			
		Pb = (ProgressBar)rootView.findViewById(R.id.progressBar1);
		transfer=(Button)rootView.findViewById(R.id.buttonaccept);
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy); 


		pref = this.getActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
		//saving route name and driver code to a variable
		route= getActivity().getIntent().getExtras().getString("routecode");
		routen= getActivity().getIntent().getExtras().getString("routename");

		db=new DatabaseHandler(getActivity().getBaseContext());
		//open localdatabase in a read mode
		sqldb = db.getReadableDatabase();
		//select  loginstatus and routecode on resume activity 
		Cursor c1 = sqldb.rawQuery("SELECT Username FROM logindata WHERE Loginstatus=1", null);
		c1.moveToFirst();
		if(c1.getCount()>0){
			drivercode=c1.getString(c1.getColumnIndex("Username"));
		}
		c1.close();
		db.close();
	//	sqldb.close();
			
		
	    back.setOnClickListener(new OnClickListener() {
	      	 
	        @Override
	        public void onClick(View v) {
	        	v.startAnimation(AnimationUtils.loadAnimation(getActivity().getBaseContext(), R.anim.image_click));
	        	
	        	// moveTaskToBack(true); 
	   	       getActivity().finish();
	           
	        	
	        }
	    });
	    transfer.setOnClickListener(new OnClickListener() {
	      	 
	        @SuppressWarnings("null")
			@Override
	        public void onClick(View v) {
	        	v.startAnimation(AnimationUtils.loadAnimation(getActivity().getBaseContext(), R.anim.image_click));
	        	String[] picknoarr=new String[resulttab.getChildCount()];
					
	        	int flag=0;
	        	for (int i = 0; i < resulttab.getChildCount(); i++)
				{
						picnotxt = (TextView)((TableRow)resulttab.getChildAt(i)).getChildAt(0);
					
						picknoarr[i]=picnotxt.getText().toString();
						//System.out.println(picknoarr[i]);
					//	pick_res= WebService.setpickupconfirm(drivercode,picknoarr[i], MasterActivity.METHOD_NAME35);
				     	pick_res= webservice.WebService.SET_TRANS_PICKUP_CONFIRM(drivercode,picknoarr[i]);
	       
	        	flag=0;
	        	if(!errored)
	        	{
	        		if(pick_res.contains("ACCEPTED"))
	        		{
	        		flag=1;
	        		}
	        		else
	        		{
	        		flag=0;
	        		}
	        	}
	        	else
	        	{
					Toast.makeText(getActivity(), "Connection Error", Toast.LENGTH_LONG).show();
				}
	        }
	        if(flag==1){
	        	Toast.makeText(getActivity(), pick_res, Toast.LENGTH_LONG).show();
	        }
	        else{
	        	Toast.makeText(getActivity(), pick_res, Toast.LENGTH_LONG).show();
	        }
	        	
	        	
	        }
	    });
	    
	    
	    /*scan.setOnClickListener(new OnClickListener() {
	      	 
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
	    }); 
	    */
	    
	    
        return rootView;
    }
	
    //Return scan result 
    @Override
   	public void onActivityResult(int requestCode, int resultCode, Intent intent) {

   		if (requestCode == SCANNER_REQUEST_CODE) {
   			// Handle scan intent
   		
   			if (resultCode == Activity.RESULT_OK) {
   				// Handle successful scan
   				String contents = intent.getStringExtra("SCAN_RESULT");
   				
   				waybill=contents;
   				new Task1().execute();
   		      
   		        
   		       
   			} else if (resultCode == Activity.RESULT_CANCELED) {
   				// Handle cancel
   			}
   		} else {
   			// Handle other intents
   		}
   		
   	} 
		    		  
    @Override
	public void onResume() {
	    super.onResume();
	    PickupActivity.KDCScannerCallFrom = "PTAFragment";
	    MYActivity = PickupActivity.PTAActivity;

	    getView().setFocusableInTouchMode(true);
	    getView().requestFocus();
		//Camera Scanner via volume buttons disabled
/*	    getView().setOnKeyListener(new View.OnKeyListener() {
	        @Override
	        public boolean onKey(View v, int keyCode, KeyEvent event) {

	        	 int action = event.getAction();
	     	    keyCode = event.getKeyCode();
	     	        switch (keyCode) {
	     	        case KeyEvent.KEYCODE_VOLUME_UP:
	     	            if (action == KeyEvent.ACTION_DOWN) {
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
	     	            //return1 super.dispatchKeyEvent(event);
	     	        }
	            return false;
	        }
	    });*/
	}   
		
	 //async task for getting hold waybill
	 public class Task1 extends AsyncTask<Void, Void, String>
		{
		 String response1 = "";
			public void onPreExecute()
			  {
				Pb.setVisibility(View.VISIBLE);
				// super.onPreExecute();

				tr = new TableRow(getActivity());
				if(Build.MODEL.contains("SM-N"))
					{

						lp = new LayoutParams(385,LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);

						tr.setLayoutParams(lp);
						lp.setMargins(0, 10, 40, 0);

					}
					else
					{
				/*	lp = new LayoutParams(150,LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);

					tr.setLayoutParams(lp);
					lp.setMargins(0, 20, 10, 0);*/
						lp = new LayoutParams(200,LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
						lp.setMargins(0, 20, 70, 0);
						tr.setLayoutParams(lp);
					}



			  }
			    @Override
			protected String doInBackground(Void... arg0)
			{
			    	getpickupwaybill();
			       return "";
			}

			    private void getpickupwaybill() {
					// TODO Auto-generated method stub

					return;

			    	/*SoapObject Request = new SoapObject(MasterActivity.NAMESPACE, MasterActivity.METHOD_NAME34);
					Request.addProperty("DRIVERCODE",drivercode);
					Request.addProperty("WAYBILL",waybill);

					db=new DatabaseHandler(getActivity().getBaseContext());
					sqldb =db.getReadableDatabase();

					*//*Create waybill class *//*
					PickupdtClass pkt = new PickupdtClass();


					*//* Set the route to be the argument of the web service method *//*
					PropertyInfo pi = new PropertyInfo();
					pi.setName(MasterActivity.METHOD_NAME34);
					pi.setValue(pkt);
					pi.setType(pkt.getClass());
					Request.addProperty(pi);

					*//* Set the web service envelope*//*
					SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
					envelope.dotNet = true;
					envelope.setOutputSoapObject(Request);
					//envelope for setting the data
					envelope.addMapping(MasterActivity.NAMESPACE, MasterActivity.METHOD_NAME34,new PickupdtClass().getClass());
					HttpTransportSE androidHttpTransport = new HttpTransportSE(MasterActivity.URL);
					//	androidHttpTransport.debug = true;

					*//* Call the web service and retrieve result*//*

					try {
						// Invoke web service
						ServiceConnection WebCon = androidHttpTransport.getServiceConnection();
						WebCon.disconnect();
						androidHttpTransport.call(MasterActivity.SOAP_ACTION+ MasterActivity.METHOD_NAME34, envelope);

						SoapObject response = (SoapObject)envelope.getResponse();
						Log.i("myApp", Request.toString());
						//System.out.println("check dddd:" + response);
						//envelope for passing the data
						envelope.addMapping(MasterActivity.NAMESPACE, MasterActivity.METHOD_NAME34,new PickupdtClass().getClass());
						androidHttpTransport.call(MasterActivity.SOAP_ACTION+ MasterActivity.METHOD_NAME34, envelope);

						//if error message is there print only the error message else print all data

						flag=0;

						if(response.toString().contains("PAYTYPE"))
						{

						pickupno12=response.getProperty(0).toString();

						wbill12=response.getProperty(1).toString();
						paytype12 =response.getProperty(2).toString();
						amount12 =response.getProperty(3).toString();
						service12=response.getProperty(4).toString();

						Cursor pyi = sqldb.rawQuery("SELECT * FROM paytypedetails WHERE PayID='"+paytype12+"'", null);
						if(pyi.getCount()>0)
						{
							pyi.moveToFirst();
							paytype =pyi.getString(pyi.getColumnIndex("PayTYPE"));

						}
						pyi.close();

						Cursor se = sqldb.rawQuery("SELECT * FROM servicedetails WHERE ServiceID='"+service12+"'", null);
						if(se.getCount()>0)
						{
							se.moveToFirst();
							servicetype =se.getString(se.getColumnIndex("ServiceTYPE"));

						}
						se.close();

							picnotxt = new TextView(getActivity());
							picnotxt.setLayoutParams(lp);
							//picnotxt.setText(pickupno12);
							picnotxt.setText("12");
							final TextView waybilltxt = new TextView(getActivity());
							waybilltxt.setLayoutParams(lp);
							//waybilltxt.setText(wbill12);
							waybilltxt.setText("1000256891");


							final TextView paytypetxt = new TextView(getActivity());
							paytypetxt.setLayoutParams(lp);
							//paytypetxt.setText(paytype);
							paytypetxt.setText("fghj");

							final TextView amounttxt = new TextView(getActivity());
							amounttxt.setLayoutParams(lp);
							//amounttxt.setText(amount12);
							amounttxt.setText("120");

							final TextView servicetxt = new TextView(getActivity());
							servicetxt.setLayoutParams(lp);
							//servicetxt.setText(servicetype);
							servicetxt.setText("customer");

							tr.addView(picnotxt);
							tr.addView(waybilltxt);
							tr.addView(paytypetxt);
							tr.addView(amounttxt);
							tr.addView(servicetxt);

							flag=1;

							//display the response in table

							//clear text ms
							//	statusmsg.setText(null);
							//save the response in database

						}

						else
						{

							msg =  response.getProperty(0).toString();
							flag=0;
							//statusmsg.setText(error1);
						}
						androidHttpTransport.reset();
						//androidHttpTransport.getConnection().disconnect();
								WebCon.disconnect();

					}
					catch(Exception e)
					{
						//Toast.makeText(getActivity().getApplicationContext(),"Connection Error",
							//	Toast.LENGTH_LONG).show();
						e.printStackTrace();
						//Log.e("Pickup TA:", e.getMessage().toString());

					}
*/

				}
			@Override
			public void onPostExecute(String res)
			{
				//System.out.println("post");
				//System.out.println(flag);

				if(flag==1)
				{
					resulttab.addView(tr, new TableLayout.LayoutParams (LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

				}
				else
					{


						Toast.makeText(getActivity().getApplicationContext(),msg,
								Toast.LENGTH_LONG).show();


					}




				Pb.setVisibility(View.INVISIBLE);



			}

		}
}