package com.postaplus.postascannerapp;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TableRow.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;


public class Transfer_hold_fragment extends Fragment  {
	
	private Spinner courierlist;
	String holdstatus;
	Button back,selectall,transferhold,deselectall;
	TextView code;
	String dcode,transcourier,rname,rcode;
	ProgressBar pg;
	DatabaseHandler db;
	SQLiteDatabase sqldb;
	TableLayout  resulttab;
	TableRow tr;
	LayoutParams lp ;
	CheckBox waybilltxt;
	TextView cnametxt,amounttxt,counttxt;
	String wbill,area,cname;
	SharedPreferences pref;
	String[] waybill1,cname11,phone,area11,company1,civilid1,serial1,cardtype1,deldate1,deltime1,amount1;
	private String[] drivname,drivcode;

		
	 @Override
	    public View onCreateView(LayoutInflater inflater, ViewGroup container,
	            Bundle savedInstanceState) {
	 
	        final View rootView = inflater.inflate(R.layout.activity_transfer_hold, container, false);
	        back=(Button)rootView.findViewById(R.id.btnbck);
	        courierlist = (Spinner)rootView.findViewById(R.id.spinnercourier);
	      
	        pg = (ProgressBar)rootView.findViewById(R.id.progressBar1);
	        resulttab=(TableLayout)rootView.findViewById(R.id.tbrslt);
	        selectall=(Button) rootView.findViewById(R.id.btnselect);
	        deselectall=(Button) rootView.findViewById(R.id.btndeselect);
	        transferhold=(Button)rootView.findViewById(R.id.buttontransfer);
	        counttxt=(TextView)rootView.findViewById(R.id.textcount);

	        //networkonmainthreadexception
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		    StrictMode.setThreadPolicy(policy); 
		  
		    
		    pref = this.getActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
		    dcode=pref.getString("uname", "");
		    rname= getActivity().getIntent().getExtras().getString("routename");
			rcode=getActivity().getIntent().getExtras().getString("routecode");


		    db=new DatabaseHandler(getActivity().getBaseContext());
			//open localdatabase in a read mode
			sqldb = db.getReadableDatabase();		

			//select all values from eventtable and populate it in the spinner 
			Cursor c1 = sqldb.rawQuery("SELECT '' AS DRIVERCODE,'' AS DRIVERNAME UNION SELECT DRIVERCODE,DRIVERNAME  FROM courierdetails WHERE DRIVERCODE<>'"+dcode+"' ORDER BY DRIVERNAME ASC", null);
			int count=c1.getCount();


			drivcode=new String[count];
			drivname=new String[count];
			
			c1.moveToFirst();

			for(int i=0;i<count;i++)
			{

				drivcode[i] = c1.getString(c1.getColumnIndex("DRIVERCODE"));
				drivname[i] = c1.getString(c1.getColumnIndex("DRIVERNAME"));
				// System.out.println("i="+i+eventname[i]);
				c1.moveToNext();
			} 

			ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity().getBaseContext(), android.R.layout.simple_spinner_item, drivname);
			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

			courierlist.setAdapter(adapter);

			// closing connection
			c1.close();
			db.close();
			selectall.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					v.startAnimation(AnimationUtils.loadAnimation(getActivity().getBaseContext(), R.anim.image_click));
					Log.e("u clicked !!!","******");
				for (int i = 0; i < resulttab.getChildCount(); i++) {
						waybilltxt = (CheckBox)((TableRow)resulttab.getChildAt(i)).getChildAt(0);
						waybilltxt.setChecked(true);
						}


				}
			});

			transferhold.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					v.startAnimation(AnimationUtils.loadAnimation(getActivity().getBaseContext(), R.anim.image_click));


					db = new DatabaseHandler(getActivity().getBaseContext());
					sqldb = db.getWritableDatabase();
					transcourier = String.valueOf(drivcode[courierlist.getSelectedItemPosition()]);
					if (transcourier.isEmpty()) {
						Toast.makeText(getActivity(), "Please select any courier!", Toast.LENGTH_SHORT).show();

						return;
					}
					System.out.println("transcourier value table in transferwc" + resulttab.getChildCount());
					if (resulttab.getChildCount() == 0) {
						Toast.makeText(getActivity(), "Please select waybill for transfer!", Toast.LENGTH_SHORT).show();

						return;
					}

					for (int i = 0; i < resulttab.getChildCount(); i++) {
						waybilltxt = (CheckBox) ((TableRow) resulttab.getChildAt(i)).getChildAt(0);

						if (waybilltxt.isChecked()) {
							waybill1[i] = waybilltxt.getText().toString();
							sqldb.execSQL("UPDATE Holdwaybilldata SET HOLD_Transfer_Status=1 WHERE Waybill='" + waybill1[i] + "'");
							if (isNetworkConnected()) {
								holdstatus = webservice.WebService.SET_TRANS_HOLD(dcode, transcourier, waybill1[i]);
							}

							//holdstatus=WebService.settranshold(dcode,transcourier,waybill1[i],MasterActivity.METHOD_NAME25);
							//aK holdstatus= webservice.WebService.SET_TRANS_HOLD(dcode,transcourier,waybill1[i]);
							System.out.println("holdstatus value in transhold" + holdstatus);
							if (holdstatus != null) {
								if (holdstatus.contains("TRANSFER")) {
									sqldb.execSQL("UPDATE Holdwaybilldata SET TransferStatus=1 WHERE Waybill='" + waybill1[i] + "'");

								}
							}
							//else
							//	sqldb.execSQL("UPDATE Holdwaybilldata SET TransferStatus=0 WHERE Waybill='" + waybill1[i] + "'");
						}
					}
					//if (holdstatus != null)
						if (isNetworkConnected()){
						if (holdstatus.contains("TRANSFER")) {
							System.out.println("holdstatus value in transhold" + holdstatus);
							transcourier = String.valueOf(drivcode[courierlist.getSelectedItemPosition()]);
							db = new DatabaseHandler(getActivity().getBaseContext());
							//open localdatabase in a read mode
							sqldb = db.getReadableDatabase();
							Cursor c2 = sqldb.rawQuery("SELECT * FROM Holdwaybilldata WHERE Transdriver_Code='" + transcourier + "' AND Drivercode='" + dcode + "' AND HOLD_Transfer_Status=0", null);
							int count1 = c2.getCount();

							waybill1 = new String[count1];

							cname11 = new String[count1];
							phone = new String[count1];
							area11 = new String[count1];
							company1 = new String[count1];
							civilid1 = new String[count1];
							serial1 = new String[count1];
							cardtype1 = new String[count1];
							deldate1 = new String[count1];
							deltime1 = new String[count1];
							amount1 = new String[count1];
							System.out.println("count1:" + count1);
							c2.moveToFirst();
							for (int i = count1; i >= 0; i--) {
								resulttab.removeAllViews();

							}


							if (count1 > 0) {


								for (int i = 0; i < count1; i++) {

									waybill1[i] = c2.getString(c2.getColumnIndex("Waybill"));
									cname11[i] = c2.getString(c2.getColumnIndex("Consignee"));
									phone[i] = c2.getString(c2.getColumnIndex("Telephone"));
									area11[i] = c2.getString(c2.getColumnIndex("Area"));
									company1[i] = c2.getString(c2.getColumnIndex("Company"));
									civilid1[i] = c2.getString(c2.getColumnIndex("CivilID"));
									serial1[i] = c2.getString(c2.getColumnIndex("Serial"));
									cardtype1[i] = c2.getString(c2.getColumnIndex("CardType"));
									deldate1[i] = c2.getString(c2.getColumnIndex("DeliveryDate"));
									deltime1[i] = c2.getString(c2.getColumnIndex("DeliveryTime"));
									amount1[i] = c2.getString(c2.getColumnIndex("Amount"));

									//System.out.println("i="+i+waybill1[i]+" "+cname11[i]+" "+phone[i]+" "+area11[i]+" "+company1[i]+" "+civilid1[i]+" "+serial1[i]);
									c2.moveToNext();
									tr = new TableRow(getActivity());
									System.out.println("i is" + i);
									Log.e("amnt", amount1[i]);
									if (Build.MODEL.contains("SM-N")) {
										//lp = new LayoutParams(300,LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
										//lp = new LayoutParams(385,LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
										lp = new LayoutParams(LayoutParams.WRAP_CONTENT);
										//tr.setId(resulttab.getChildCount());
										tr.setLayoutParams(lp);
										lp.setMargins(0, 20, 70, 0);

									} else {
			 					/*	lp = new LayoutParams(150,LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
			 						
			 						tr.setLayoutParams(lp);
			 						lp.setMargins(0, 20, 10, 0);*/

										lp = new LayoutParams(LayoutParams.WRAP_CONTENT);
										//tr.setId(resulttab.getChildCount());
										tr.setLayoutParams(lp);
										lp.setMargins(0, 20, 82, 0);
									}

									waybilltxt = new CheckBox(getActivity());
									waybilltxt.setLayoutParams(lp);
									waybilltxt.setText(waybill1[i]);


									cnametxt = new TextView(getActivity());
									cnametxt.setLayoutParams(lp);
									cnametxt.setText(cname11[i]);


									amounttxt = new TextView(getActivity());
									amounttxt.setLayoutParams(lp);
									if (amount1[i].matches("-1")) {
										amounttxt.setText("0.000");
									} else
										amounttxt.setText(amount1[i]);


									tr.addView(waybilltxt);
									tr.addView(cnametxt);
									tr.addView(amounttxt);

									resulttab.addView(tr, new TableLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
									counttxt.setText(String.valueOf(resulttab.getChildCount()));
								}

							}

							c2.close();

							Toast.makeText(getActivity().getApplicationContext(), "Submitted Successfully", Toast.LENGTH_LONG).show();
						} else {
							Toast.makeText(getActivity().getApplicationContext(), "Transfer error", Toast.LENGTH_LONG).show();
						}

				}else
					{
						Toast toast = Toast.makeText(getActivity().getApplicationContext(),"You are in offline mode, Please sync once you connect to a network", Toast.LENGTH_LONG);
						toast.setGravity(Gravity.CENTER, 0, 0);
						toast.show();

					}
					db.close();
				}
			});
			
			deselectall.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					v.startAnimation(AnimationUtils.loadAnimation(getActivity().getBaseContext(), R.anim.image_click));
					for (int i = 0; i < resulttab.getChildCount(); i++) {
						waybilltxt = (CheckBox)((TableRow)resulttab.getChildAt(i)).getChildAt(0);
						waybilltxt.setChecked(false);
						}


				}
			});
			 courierlist.setOnItemSelectedListener(new OnItemSelectedListener() {
			        @Override
			        public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
			        	transcourier = String.valueOf(drivcode[position]);

				        	//open localdatabase in a read mode
				 			sqldb = db.getReadableDatabase();
				 			Cursor c2 = sqldb.rawQuery("SELECT * FROM Holdwaybilldata WHERE Transdriver_Code='"+transcourier+"' AND Drivercode='"+dcode+"' AND HOLD_Transfer_Status=0", null);
				 			int count1=c2.getCount();
				 					 			
				 			waybill1=new String[count1];
				 			
				 			cname11=new String[count1];
				 			phone=new String[count1];
				 			area11=new String[count1];
				 			company1=new String[count1];
				 			civilid1=new String[count1];
				 			serial1=new String[count1];
				 			cardtype1=new String[count1];
				 			deldate1=new String[count1];
				 			deltime1=new String[count1];
				 			amount1=new String[count1];
						Log.e("amount1",amount1.toString());
				 			c2.moveToFirst();
				 			for (int i = count1; i >=0; i--) {
				 				resulttab.removeAllViews();

				 			}


				 			if(count1>0)
				 			{


				 				for(int i=0;i<count1;i++)
				 				{

				 					waybill1[i] = c2.getString(c2.getColumnIndex("Waybill"));
				 					cname11[i]= c2.getString(c2.getColumnIndex("Consignee"));
				 					phone[i]= c2.getString(c2.getColumnIndex("Telephone"));
				 					area11[i]= c2.getString(c2.getColumnIndex("Area"));
				 					company1[i]= c2.getString(c2.getColumnIndex("Company"));
				 					civilid1[i]= c2.getString(c2.getColumnIndex("CivilID"));
				 					serial1[i]=c2.getString(c2.getColumnIndex("Serial"));
						 			cardtype1[i]=c2.getString(c2.getColumnIndex("CardType"));
						 			deldate1[i]=c2.getString(c2.getColumnIndex("DeliveryDate"));
						 			deltime1[i]=c2.getString(c2.getColumnIndex("DeliveryTime"));
						 			amount1[i]=c2.getString(c2.getColumnIndex("Amount"));
				 					
				 					//System.out.println("i="+i+waybill1[i]+" "+cname11[i]+" "+phone[i]+" "+area11[i]+" "+company1[i]+" "+civilid1[i]+" "+serial1[i]);
				 					c2.moveToNext();
				 						tr = new TableRow(getActivity());
									Log.e("amnts",amount1[i]);

				 						if(Build.MODEL.contains("SM-N"))
				 						{
				 							//lp = new LayoutParams(300,LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
				 							//lp = new LayoutParams(385,LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
											lp = new LayoutParams(LayoutParams.WRAP_CONTENT);
				 							//tr.setId(resulttab.getChildCount());
				 							tr.setLayoutParams(lp);
				 							lp.setMargins(0, 20, 70, 0);
				 						
				 						}
				 						else
				 						{
				 						/*lp = new LayoutParams(150,LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
				 						
				 						tr.setLayoutParams(lp);
				 						lp.setMargins(0, 20, 10, 0);*/

											lp = new LayoutParams(LayoutParams.WRAP_CONTENT);
											//tr.setId(resulttab.getChildCount());
											tr.setLayoutParams(lp);
											lp.setMargins(0, 20, 82, 0);
				 						}
				 						
				 						waybilltxt = new CheckBox(getActivity());
				 						waybilltxt.setLayoutParams(lp);
				 						waybilltxt.setText(waybill1[i]);


				 						cnametxt = new TextView(getActivity());
				 						cnametxt.setLayoutParams(lp);
				 						cnametxt.setText(cname11[i]);


				 						amounttxt = new TextView(getActivity());
				 						amounttxt.setLayoutParams(lp);
									if(amount1[i].matches("-1")){
										amounttxt.setText("0.000");
									}else
				 						amounttxt.setText(amount1[i]);
				 						
				 						
				 						tr.addView(waybilltxt);
				 						tr.addView(cnametxt);
				 						tr.addView(amounttxt);

				 						resulttab.addView(tr, new TableLayout.LayoutParams (LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));			
				 						counttxt.setText(String.valueOf(resulttab.getChildCount()));
				            }


				 			}
				 			else
				 			{
				 				//Toast.makeText(getActivity().getApplicationContext(),"NO Data", Toast.LENGTH_LONG).show();
				 			}
				 			c2.close();
			        }

			        @Override
			        public void onNothingSelected(AdapterView<?> parentView) {

			        }


			    });
			
			
			
			db.close();
	        
	        back.setOnClickListener(new OnClickListener() {
		      	 
		        @Override
		        public void onClick(View v) {
		        	v.startAnimation(AnimationUtils.loadAnimation(getActivity().getBaseContext(), R.anim.image_click));
		        	
		        	// moveTaskToBack(true); 
		   	       getActivity().finish();
		   	    Intent int1 = new Intent(getActivity(),HomeActivity.class);

				int1.putExtra("route",rcode);
				int1.putExtra("route1",rname);

				startActivity(new Intent(int1));
		        	
		        }
		    });



	        return rootView;
	 }

	public boolean isNetworkConnected() {
		ConnectivityManager cm = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo ni = cm.getActiveNetworkInfo();
		return ni != null;
	}


}
