package com.postaplus.postascannerapp;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TableRow.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

//import org.ksoap2.serialization.SoapObject;

import java.text.SimpleDateFormat;
import java.util.Date;

public class NotificationActivity extends MasterActivity {
	String date="",pickupno="",acname="",pickaddr="",pickarea="",contact_person="",pick_phone="",picktime="",error="";
	String[] pickupno1,acname1,pickaddr1,pickarea1,contact_person1,pick_phone1,picktime1,error1;
	int count;
	static boolean errored=false;
	TableLayout  resulttab;
	TableRow tr;
	LayoutParams lp ;
	String uname=null;
	//SoapObject response;
	TextView username,rnametxt,cnametxt,counttxt;
	Button selectall,accept,reject,close;
	CheckBox ch;
	CheckBox chboxpickup;
	boolean acceptstatus;
	String rcode,rname;
	
	    @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.activity_list);
	       		
			
			
			pref = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
			username=(TextView) findViewById(R.id.unametxt);
			username.setText(pref.getString("uname", ""));
			uname=username.getText().toString();
			counttxt=(TextView) findViewById(R.id.textcount);
			
			//String pick=getIntent().getExtras().getString("Pickupno");
			
			resulttab=(TableLayout)findViewById(R.id.table2);
			 selectall=(Button) findViewById(R.id.buttonselect);
			 accept=(Button) findViewById(R.id.buttonaccept);
			// reject=(Button) findViewById(R.id.buttonreject);
			close=(Button) findViewById(R.id.buttonclose);
					
			
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MMM-dd HH:mm:ss");
			date=sdf.format(new Date());
			
			counttxt.setText(String.valueOf(resulttab.getChildCount()));
			
			new pickupTask().execute();
			
			selectall.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					v.startAnimation(AnimationUtils.loadAnimation(getBaseContext(), R.anim.image_click));
					for (int i = 0; i < resulttab.getChildCount(); i++) {
						ch = (CheckBox)((TableRow)resulttab.getChildAt(i)).getChildAt(0);
						  ch.setChecked(true);
						}


				}
			});
	    
			accept.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					v.startAnimation(AnimationUtils.loadAnimation(getBaseContext(), R.anim.image_click));
					db = new DatabaseHandler(getBaseContext());
					sqldb = db.getWritableDatabase();
					String[] PickupNO = new String[resulttab.getChildCount()];
					for (int j = 0; j < resulttab.getChildCount(); j++) {
						ch = (CheckBox) ((TableRow) resulttab.getChildAt(j)).getChildAt(0);
					//	ch = (CheckBox) ((TableRow) resulttab.getChildAt(i)).getChildAt(i);
						if (ch.isChecked()) {
							if (ch.isChecked()) {
								PickupNO[j] = ch.getText().toString();


								//acceptstatus=WebService.setnotpickup(uname,pickupno1[i],date,METHOD_NAME23);
							//	acceptstatus = webservice.WebService.SET_PICKUP_RECVD(uname, pickupno1[i], date);
								acceptstatus = webservice.WebService.SET_PICKUP_RECVD(uname, PickupNO[j], date);

							}

							if (!errored) {
								if (acceptstatus) {
									sqldb.execSQL("UPDATE pickuphead SET Status='A' WHERE Pickup_No='" + PickupNO[j] + "'");
									sqldb.execSQL("UPDATE pickuphead SET TransferStatus=1 WHERE Pickup_No='" + PickupNO[j] + "'");
									sqldb.execSQL("UPDATE pickuphead SET Accept_Date_Time='" + date + "' WHERE Pickup_No='" + PickupNO[j] + "'");



								/*	sqldb.execSQL("UPDATE pickuphead SET Status='A' WHERE Pickup_No='" + pickupno1[j] + "'");
									sqldb.execSQL("UPDATE pickuphead SET TransferStatus=1 WHERE Pickup_No='" + pickupno1[j] + "'");
									sqldb.execSQL("UPDATE pickuphead SET Accept_Date_Time='" + date + "' WHERE Pickup_No='" + pickupno1[j] + "'");
*/
								}
							} else {
								Toast.makeText(getApplicationContext(), "Connection Error", Toast.LENGTH_LONG).show();
							}
						} else {
							Toast.makeText(getApplicationContext(), "Please select atleast 1 Pickup", Toast.LENGTH_LONG).show();

						}
					}
					if (ch.isChecked() == true) {
						if (acceptstatus) {

							Toast.makeText(getApplicationContext(), "Accepted", Toast.LENGTH_LONG).show();
							resulttab.removeView(tr);
							NotificationActivity.this.finish();
							Intent int1 = new Intent(NotificationActivity.this, PickupActivity.class);
							int1.putExtra("route", rcode);
							int1.putExtra("route1", rname);

							startActivity(new Intent(int1));

						}
						db.close();
						//	sqldb.close();
					/*NotificationActivity.this.finish();
					Intent int1 = new Intent(NotificationActivity.this,PickupActivity.class);
					int1.putExtra("route",rcode);
					int1.putExtra("route1",rname);

					startActivity(new Intent(int1));*/

					}

				}
				
			});

			close.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					v.startAnimation(AnimationUtils.loadAnimation(getBaseContext(), R.anim.image_click));
					NotificationActivity.this.finish();
					Intent int1 = new Intent(NotificationActivity.this,PickupActivity.class);
					int1.putExtra("route",rcode);
					int1.putExtra("route1",rname);

					startActivity(new Intent(int1));

				}
			});
	    
	    
	    
	    }
	 
	    public class pickupTask extends AsyncTask<Void, Void, String>
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
				
				 				
				 db=new DatabaseHandler(getBaseContext());
					//open localdatabase in a read mode
					sqldb = db.getReadableDatabase();
					
					Cursor c2 = sqldb.rawQuery("SELECT * FROM pickuphead WHERE Status='P'", null);
					count=c2.getCount();
					System.out.println("stage");

					pickupno1=new String[count];
					acname1=new String[count];
					pickaddr1=new String[count];
					pickarea1=new String[count];
					contact_person1=new String[count];
					pick_phone1=new String[count];
					picktime1=new String[count];
					
					System.out.println("stage3");
					c2.moveToFirst();
					for (int i = count; i >=0; i--) 
					{
						resulttab.removeAllViews();

					}


					if(c2.getCount()>0)
					{


						for(int i=0;i<count;i++)
						{

							pickupno1[i] = c2.getString(c2.getColumnIndex("Pickup_No"));
							acname1[i] = c2.getString(c2.getColumnIndex("Account_Name"));
							pickaddr1[i]= c2.getString(c2.getColumnIndex("Pick_Address"));
							pickarea1[i]= c2.getString(c2.getColumnIndex("Pickup_Area"));
							contact_person1[i]= c2.getString(c2.getColumnIndex("Contact_Person"));
							pick_phone1[i]= c2.getString(c2.getColumnIndex("Pickup_Phone"));
							picktime1[i]= c2.getString(c2.getColumnIndex("Pickup_Time"));
							
							//System.out.println("i="+i+pickupno1[i]+" "+acname1[i]+" "+pickaddr1[i]+" "+pickarea1[i]+" "+contact_person1[i]+" "+pick_phone1[i]+" "+picktime1[i]);

							
		            	//create table row
						tr = new TableRow(NotificationActivity.this);
						if(Build.MODEL.contains("SM-N"))
 						{
 							
 							lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
							lp.setMargins(5, 2, 35, 2);
 							tr.setLayoutParams(lp);
 							//lp.setMargins(0, 10, 40, 0);
 						
 						}
 						else
 						{
 					/*	lp = new LayoutParams(150,LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
 						
 						tr.setLayoutParams(lp);
 						lp.setMargins(0, 20, 10, 0);*/

							lp = new LayoutParams(LayoutParams.WRAP_CONTENT);
							lp.setMargins(0, 20,40, 0);
							tr.setLayoutParams(lp);
 						}				
					
							
						 ch = new CheckBox(NotificationActivity.this);
						 ch.setLayoutParams(lp);
							//ch.setWidth(300);
						 ch.setText(pickupno1[i]);
							
					
					rnametxt = new TextView(NotificationActivity.this);
					rnametxt.setLayoutParams(lp);
					rnametxt.setText(acname1[i]);
					
					
					cnametxt = new TextView(NotificationActivity.this);
					cnametxt.setLayoutParams(lp);
					cnametxt.setText(pickarea1[i]);
									
					
					//tr.addView(ch);
					tr.addView(ch);
					tr.addView(rnametxt);
					tr.addView(cnametxt);
					tr.setId(i);
					
					
				
					resulttab.addView(tr, new TableLayout.LayoutParams (LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
					c2.moveToNext();
					counttxt.setText(String.valueOf(resulttab.getChildCount()));
				 }
					
				
			}
					c2.close();
					db.close();
			
		}
		}
	    }
	   
		

