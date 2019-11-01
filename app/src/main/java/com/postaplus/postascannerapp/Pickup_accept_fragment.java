package com.postaplus.postascannerapp;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TableRow.LayoutParams;
import android.widget.TextView;

/*import org.ksoap2.serialization.SoapObject;*/


public class Pickup_accept_fragment extends Fragment {
	
	String dcode,date="",pickupno="",acname="",pickaddr="",pickarea="",contact_person="",pick_phone="",picktime="",route,routen,callPhone;
	String[] pickupno1,acname1,pickaddr1,pickarea1,contact_person1,pick_phone1,picktime1,pickup_type,consignee,deladd,delcity,phoneno;
	int count;
	TableLayout  resulttab;
	TableRow tr;
	LayoutParams lp ;
	//SoapObject response;
	TextView counttxt;
	DatabaseHandler db;
	SQLiteDatabase sqldb;
	SharedPreferences pref;
	Context mContext;
	String phonecall;
    FragmentActivity _activity;

	//KDC
/*	public FragmentActivity MYActivity;
	View rootView;
	String waybill;
	public void ScannerPAExecutions()
	{
		// Handle successful scan
		//String contents = intent.getStringExtra("SCAN_RESULT");
		//tvScanResults.setText(contents);
		MYActivity = PickupActivity.PAActivity;
					
		
		route= MYActivity.getIntent().getExtras().getString("routecode");
		routen= MYActivity.getIntent().getExtras().getString("routename");
		waybill=PickupActivity.WaybillFromScanner;
		System.out.println("PA Fragment selected");
        rootView = inflater.inflate(R.layout.activity_delivery_transfer, container, false);
        resulttab=(TableLayout)rootView.findViewById(R.id.resulttable1);
		back=(Button)rootView.findViewById(R.id.btnbck);
		scan=(Button)rootView.findViewById(R.id.btnscan);
		
		System.out.println("PA Fragment selected");
			
		//Initializations
		rootView = PickupActivity.PArootView;
		
		
		
		new pickupdetailTask().execute();
		
		
	}*/
	@Override
	public void onResume() {
		super.onResume();

		new pickupdetailTask().execute();
	}
	 @Override
	    public View onCreateView(LayoutInflater inflater, ViewGroup container,
	            Bundle savedInstanceState) {
		 StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
			StrictMode.setThreadPolicy(policy);
			View rootView = inflater.inflate(R.layout.activity_pickup_accept, container, false);

			pref = this.getActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
			//saving route name and driver code to a variable
			route= getActivity().getIntent().getExtras().getString("routecode");
			routen= getActivity().getIntent().getExtras().getString("routename");

			 pref = this.getActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
			    dcode=pref.getString("uname", "");

			resulttab=(TableLayout)rootView.findViewById(R.id.tlTable);
        counttxt=(TextView)rootView.findViewById(R.id.textcount);

         _activity = this.getActivity();

//display no.of pickups
            counttxt.setText(String.valueOf(resulttab.getChildCount()));

		 SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
		 SharedPreferences.Editor editor = prefs.edit();
		 editor.putString("HoneywellFlag", "2");
		 editor.commit();
			new pickupdetailTask().execute();

	        return rootView;
	 }
	 public class pickupdetailTask extends AsyncTask<Void, Void, String>
		{
			public void onPreExecute()
			{

				super.onPreExecute();

			}
			@Override
			protected String doInBackground(Void... arg0)
			{

				return "";
			}

			@Override
			public void onPostExecute(String res)
			{


				db=new DatabaseHandler(getActivity().getBaseContext());
				//open localdatabase in a read mode
				sqldb = db.getReadableDatabase();
				Cursor c2 = sqldb.rawQuery("SELECT * FROM pickuphead WHERE Status='A'", null);
				count=c2.getCount();


				pickupno1=new String[count];
				pickup_type=new String[count];
				picktime1=new String[count];
				pick_phone1=new String[count];
				acname1=new String[count];
				pickaddr1=new String[count];
				pickarea1=new String[count];
				contact_person1=new String[count];
				consignee=new String[count];
				deladd=new String[count];
				delcity=new String[count];
				phoneno=new String[count];

				System.out.println("Pickupno and pickuptime in first calling: "+pickupno1+picktime1);

				c2.moveToFirst();
				for (int i = count; i >=0; i--) 
				{
					resulttab.removeAllViews();

				}


				if(c2.getCount()>0)
				{


					for(int i=0;i<count;i++)
					{
							System.out.println("Pickupno and pickuptime: "+pickupno1[i]+picktime1[i]);
						pickupno1[i] = c2.getString(c2.getColumnIndex("Pickup_No"));
						System.out.println("Pickup type is"+pickup_type[i]);
						pickup_type[i]=c2.getString(c2.getColumnIndex("Pickup_Type"));
						picktime1[i]= c2.getString(c2.getColumnIndex("Pickup_Time"));
						pick_phone1[i]= c2.getString(c2.getColumnIndex("Pickup_Phone"));
						acname1[i] = c2.getString(c2.getColumnIndex("Account_Name"));
						pickaddr1[i]= c2.getString(c2.getColumnIndex("Pick_Address"));
						pickarea1[i]= c2.getString(c2.getColumnIndex("Pickup_Area"));
						contact_person1[i]= c2.getString(c2.getColumnIndex("Contact_Person"));
						consignee[i]= c2.getString(c2.getColumnIndex("ConsigneeName"));
						deladd[i]=c2.getString(c2.getColumnIndex("DeliveryAddress"));
						delcity[i]=c2.getString(c2.getColumnIndex("DeliveryCity"));
						phoneno[i]=c2.getString(c2.getColumnIndex("DeliveryPhone"));

						System.out.println("consignee namee is"+consignee[i]);
						System.out.println("consignee namee is"+deladd[i]);
						System.out.println("consignee namee is"+delcity[i]);
						System.out.println("consignee namee is"+phoneno[i]);

						//System.out.println("i="+i+pickupno1[i]+" "+acname1[i]+" "+pickaddr1[i]+" "+pickarea1[i]+" "+contact_person1[i]+" "+pick_phone1[i]+" "+picktime1[i]);


						//create table row
						tr = new TableRow(getActivity());
						if(Build.MODEL.contains("SM-N"))
 						{
 							
 							lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
							//lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
							lp.setMargins(80, 1,25, 1);
							//lp.setMargins(0, 10, 30, 0);
							//lp.setMargins(25, 2, 95, 2);
 							tr.setLayoutParams(lp);

 							//lp.setMargins(0, 10, 30, 0);
 						
 						}
 						else
 						{
 						/*lp = new LayoutParams(150,LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
 						
 						tr.setLayoutParams(lp);
 						lp.setMargins(0, 20, 10, 0);*/

							lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);

							tr.setLayoutParams(lp);
							lp.setMargins(30, 20,70, 0);
 						}					


						final TextView picktxt = new TextView(getActivity());
						picktxt.setLayoutParams(lp);
						picktxt.setText(pickupno1[i]);

						TextView picktimetxt = new TextView(getActivity());
						picktimetxt.setLayoutParams(lp);
						picktimetxt.setText(picktime1[i]);

						 final TextView pttxt = new TextView(getActivity());
						pttxt.setLayoutParams(lp);
						pttxt.setText(pickup_type[i]);


						TextView pickphonetxt = new TextView(getActivity());
						pickphonetxt.setLayoutParams(lp);
						pickphonetxt.setText(pick_phone1[i]);
						System.out.println("pickupphone is:"+pick_phone1[i]);
						callPhone=pickphonetxt.getText().toString();
						System.out.println("pickupphone call:"+callPhone);


						final TextView acnametxt = new TextView(getActivity());
						acnametxt.setLayoutParams(lp);
						acnametxt.setText(acname1[i]);
						acname=acnametxt.getText().toString();

						TextView pickaddtxt = new TextView(getActivity());
						pickaddtxt.setLayoutParams(lp);
						pickaddtxt.setWidth(300);
						//pickaddtxt.setLines(10);
						pickaddtxt.setText(pickaddr1[i]);

						TextView pickareatxt = new TextView(getActivity());
						pickareatxt.setLayoutParams(lp);
						pickareatxt.setText(pickarea1[i]);

						TextView contpersontxt = new TextView(getActivity());
						contpersontxt.setLayoutParams(lp);
						contpersontxt.setText(contact_person1[i]);

						/*final TextView consigneetxt = new TextView(getActivity());
						consigneetxt.setText(consignee[i]);

						final TextView delvaddtxt = new TextView(getActivity());
						consigneetxt.setText(deladd[i]);

						final TextView delvcitytxt = new TextView(getActivity());
						consigneetxt.setText(delcity[i]);

						final TextView delcontnotxt = new TextView(getActivity());
						consigneetxt.setText(phoneno[i]);*/

							/*	consignee=consignee[i];
								deladd
								delcity
								phoneno*/


						picktxt.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								v.startAnimation(AnimationUtils.loadAnimation(getActivity().getBaseContext(), R.anim.image_click));

								//_activity.finishActivity(0);
								//_activity.finishAndRemoveTask();

								Intent int1 = new Intent(_activity,PickupUpdateActivity.class);
								int1.putExtra("pickno",picktxt.getText().toString());
							//	int1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
							//	int1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
							//	int1.putExtra("accnname",acnametxt.getText().toString());
                                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
                                SharedPreferences.Editor editor = prefs.edit();
                                editor.putString("HoneywellFlag", "1");
                                editor.commit();
								startActivity(new Intent(int1));

							}
						});
// Code for calling PTActivity
						pttxt.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								if(pttxt.getText().equals("TD")) {
									v.startAnimation(AnimationUtils.loadAnimation(getActivity().getBaseContext(), R.anim.image_click));

									_activity.finishActivity(0);

									Intent int1 = new Intent(_activity, PTActivity.class);
									int1.putExtra("pickupno",picktxt.getText().toString());
									int1.putExtra("shippername",acnametxt.getText().toString());

									startActivity(new Intent(int1));

								}
							}
						});

						final int finalI = i;
						pickphonetxt.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								v.startAnimation(AnimationUtils.loadAnimation(getActivity().getBaseContext(), R.anim.image_click));
								String uri = "tel:" + pick_phone1[finalI];

								System.out.println("phone number is:"+uri);

							//	mContext = getApplicationContext();
								PhoneStateChangeListener phoneListener = new PhoneStateChangeListener();
							//	TelephonyManager telephonyManager = (TelephonyManager) mContext.getSystemService(mContext.TELEPHONY_SERVICE);
								TelephonyManager telephonyManager = (TelephonyManager) getContext().getSystemService(Context.TELEPHONY_SERVICE);
								telephonyManager.listen(phoneListener, PhoneStateListener.LISTEN_CALL_STATE);
								//telephonyManager.listen(callStateListener,PhoneStateListener.LISTEN_CALL_STATE);
								Intent phoneCallIntent = new Intent(Intent.ACTION_CALL);
								phoneCallIntent.setData(Uri.parse(uri));

								if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {

									startActivity(phoneCallIntent);
								}
							}
						});

						//tr.addView(ch);
						tr.addView(picktxt);
						tr.addView(pttxt);
						tr.addView(picktimetxt);
						tr.addView(pickphonetxt);
						tr.addView(acnametxt);
						tr.addView(pickaddtxt);	
						tr.addView(pickareatxt);
						tr.addView(contpersontxt);





						resulttab.addView(tr, new TableLayout.LayoutParams (LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
						counttxt.setText(String.valueOf(resulttab.getChildCount()));
						c2.moveToNext();
					}


				}
				c2.close();
				db.close();
			//	sqldb.close();

			}
		}


	}

	
