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
import androidx.fragment.app.FragmentActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
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


public class Transfer_wc_fragment extends Fragment {
	
	private Spinner courierlist;
	String wc_transfer_status;
	Button back,selectall,transfer_wc,deselectall;
	ProgressBar pg;
	DatabaseHandler db;
	SQLiteDatabase sqldb;
	TableLayout  resulttab;
	TableRow tr;
	LayoutParams lp ;
	CheckBox waybilltxt;
	TextView cnametxt,amounttxt,pendingtxt;
	String wbill,area,cname;
	String dcode,transcourier,rname,rcode;
	SharedPreferences pref;
	private int count;
	private String[] drivname,drivcode;
	String[] waybill1,cname11,phone,area11,company1,civilid1,serial1,cardtype1,deldate1,deltime1,amount1;
	public FragmentActivity MYActivity;
	 @Override
	    public View onCreateView(LayoutInflater inflater, ViewGroup container,
	            Bundle savedInstanceState) {
		 Log.e("transfr wc","*****");
	        View rootView = inflater.inflate(R.layout.acitivity_transfer_wc, container, false);
	        back=(Button)rootView.findViewById(R.id.btnbck);
	        courierlist = (Spinner)rootView.findViewById(R.id.spinnercourier);
	        pg = (ProgressBar)rootView.findViewById(R.id.progressBar1);
	        resulttab=(TableLayout)rootView.findViewById(R.id.tbrslt);
	        selectall=(Button) rootView.findViewById(R.id.btnselect);
	        deselectall=(Button) rootView.findViewById(R.id.btndeselect);
	      transfer_wc=(Button)rootView.findViewById(R.id.buttontransfer);
	       pendingtxt=(TextView)rootView.findViewById(R.id.pendingtext);
	      pref = this.getActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
		    dcode=pref.getString("uname", "");
		 System.out.println("dcode"+dcode);
	        //networkonmainthreadexception
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		    StrictMode.setThreadPolicy(policy);

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
			Log.e("transfr blck","*****");
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
			
			//open localdatabase in a read mode
 			sqldb = db.getReadableDatabase();
 			Cursor c2 = sqldb.rawQuery("SELECT * FROM deliverydata WHERE WC_Status='A' AND Drivercode='"+dcode+"' AND WC_Transfer_Status=0", null);
 			int count1=c2.getCount();
		 System.out.println("trans wc count value in transferwc"+count1);
 			//set the pending shipment count
 			pendingtxt.setText(String.valueOf(count1));		 			
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
 			
 			c2.moveToFirst();
 			for (int i = count1; i >=0; i--) {
 				resulttab.removeAllViews();

 			}
		 Log.e("db entered ","*****");
		System.out.println("waybill1"+waybill1+"cname11"+cname11+"phone"+phone);
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
 					
 						tr = new TableRow(getActivity());

 						//System.out.println(Build.MODEL);
 						if(Build.MODEL.contains("SM-N"))
 						{
							/*//lp = new LayoutParams(LayoutParams.WRAP_CONTENT);
							lp = new LayoutParams(385,LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
							//tr.setId(resulttab.getChildCount());
							tr.setLayoutParams(lp);
							lp.setMargins(0, 20, 95, 0);*/

							lp = new LayoutParams(LayoutParams.WRAP_CONTENT);
							//tr.setId(resulttab.getChildCount());
							tr.setLayoutParams(lp);
							lp.setMargins(0, 20, 70, 0);



						}
 						else
 						{
							lp = new LayoutParams(LayoutParams.WRAP_CONTENT);
							//tr.setId(resulttab.getChildCount());
							tr.setLayoutParams(lp);
							lp.setMargins(0, 20, 82, 0);

 						/*lp = new LayoutParams(150,LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
 						
 						tr.setLayoutParams(lp);
 						lp.setMargins(0, 20, 10, 0);*/
 						}
 						waybilltxt = new CheckBox(getActivity());
 						waybilltxt.setLayoutParams(lp);
 						waybilltxt.setText(waybill1[i]);


 						cnametxt = new TextView(getActivity());
 						cnametxt.setLayoutParams(lp);
 						cnametxt.setText(cname11[i]);


 						amounttxt = new TextView(getActivity());
 						amounttxt.setLayoutParams(lp);
					if(amount1[i].matches("-1")||amount1[i].matches("0")||amount1[i].matches("0.000")){
						amounttxt.setText("0.000");
					}else
						amounttxt.setText(amount1[i]);


					if(waybill1[i].equals("")) {
						//tr.removeView(waybilltxt);
						tr.setVisibility(View.GONE);
					}else
						tr.setVisibility(View.VISIBLE);
 						tr.addView(waybilltxt);
 						tr.addView(cnametxt);
 						tr.addView(amounttxt);
 						
 						resulttab.addView(tr, new TableLayout.LayoutParams (LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));			
 						c2.moveToNext();
            }
 				
 			}
 			/*else
 			{
 				Toast.makeText(getActivity().getApplicationContext(),"NO Data", Toast.LENGTH_LONG).show();
 			}*/
 			c2.close();

			
			
			db.close();

			selectall.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					v.startAnimation(AnimationUtils.loadAnimation(getActivity().getBaseContext(), R.anim.image_click));
					for (int i = 0; i < resulttab.getChildCount(); i++) {
						waybilltxt = (CheckBox)((TableRow)resulttab.getChildAt(i)).getChildAt(0);
						waybilltxt.setChecked(true);
						}


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
			
			transfer_wc.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					v.startAnimation(AnimationUtils.loadAnimation(getActivity().getBaseContext(), R.anim.image_click));
					db=new DatabaseHandler(getActivity().getBaseContext());
					sqldb =db.getWritableDatabase();
					transcourier = String.valueOf(drivcode[courierlist.getSelectedItemPosition()]);
					System.out.println("transcourier value in transferwc"+transcourier+"dcode is:"+dcode);
					if (transcourier.isEmpty())
					{
						Toast.makeText(getActivity(), "Please select any courier!", Toast.LENGTH_SHORT).show();

						return;
					}
					System.out.println("transcourier value table in transferwc"+resulttab.getChildCount());
					if (resulttab.getChildCount()==0)
					{
						Toast.makeText(getActivity(), "Please select waybill for transfer!", Toast.LENGTH_SHORT).show();

						return;
					}
					for (int i = 0; i < resulttab.getChildCount(); i++) {
						waybilltxt = (CheckBox)((TableRow)resulttab.getChildAt(i)).getChildAt(0);
						
						if(waybilltxt.isChecked()){
							waybill1[i]=waybilltxt.getText().toString();


							sqldb.execSQL("UPDATE deliverydata SET WC_Transfer_Status=1 WHERE Waybill='"+waybill1[i]+"'");
							sqldb.execSQL("UPDATE deliverydata SET Transdriver_Code='"+transcourier+"' WHERE Waybill='"+waybill1[i]+"'");
							//wc_transfer_status=WebService.settranswc(dcode,transcourier,waybill1[i],MasterActivity.METHOD_NAME26);

							System.out.println("dcode  val on tra"+dcode+"transcourier val"+transcourier);
							if(isNetworkConnected()){
								wc_transfer_status= webservice.WebService.SET_TRANS_WC(dcode,transcourier,waybill1[i]);
							}

							//wc_transfer_status= webservice.WebService.SET_TRANS_WC(dcode,transcourier,waybill1[i]);
							if(wc_transfer_status!=null) {
								if (wc_transfer_status.contains("TRANSFER")) {
									sqldb.execSQL("UPDATE deliverydata SET TransferStatus=1 WHERE Waybill='" + waybill1[i] + "'");

								}
							}
						}
						System.out.println("waybill for db:"+waybill1[i]);
						}
					if(isNetworkConnected()&& wc_transfer_status!=null) {
					if(wc_transfer_status.contains("TRANSFER")){
						
						db=new DatabaseHandler(getActivity().getBaseContext());
						sqldb = db.getReadableDatabase();
			 			Cursor c2 = sqldb.rawQuery("SELECT * FROM deliverydata WHERE WC_Status='A' AND Drivercode='"+dcode+"' AND WC_Transfer_Status=0", null);
			 			int count1=c2.getCount();
			 			//set the pending shipment count
			 			pendingtxt.setText(String.valueOf(count1));		 			
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
			 					
			 						tr = new TableRow(getActivity());

			 						if(Build.MODEL.contains("SM-N"))
			 						{
			 							/*lp = new LayoutParams(LayoutParams.WRAP_CONTENT);
			 							lp = new LayoutParams(255,LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
			 							//tr.setId(resulttab.getChildCount());
			 							tr.setLayoutParams(lp);
			 							lp.setMargins(0, 20, 95, 0);*/
										lp = new LayoutParams(LayoutParams.WRAP_CONTENT);
										//tr.setId(resulttab.getChildCount());
										tr.setLayoutParams(lp);
										lp.setMargins(0, 20, 70, 0);


			 						
			 						}
			 						else
			 						{
										lp = new LayoutParams(LayoutParams.WRAP_CONTENT);
										//tr.setId(resulttab.getChildCount());
										tr.setLayoutParams(lp);
										lp.setMargins(0, 20, 82, 0);

			 						/*lp = new LayoutParams(150,LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
			 						
			 						tr.setLayoutParams(lp);
			 						lp.setMargins(0, 20, 10, 0);*/
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
			 						//amounttxt.setText(amount1[i]);
			 						
			 						
			 						tr.addView(waybilltxt);
			 						tr.addView(cnametxt);
			 						tr.addView(amounttxt);
			 						
			 						resulttab.addView(tr, new TableLayout.LayoutParams (LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));			
			 						c2.moveToNext();
			            }
			 				
			 			}
			 			c2.close();
						
						
						
						db.close();
						
						
						
						
						Toast.makeText(getActivity().getApplicationContext(), wc_transfer_status, Toast.LENGTH_LONG).show();
					}
					else{
						Toast.makeText(getActivity().getApplicationContext(), wc_transfer_status, Toast.LENGTH_LONG).show();
					}
				}else {
						Toast toast = Toast.makeText(getActivity().getApplicationContext(),"You are in offline mode, Please sync once you connect to a network", Toast.LENGTH_LONG);
						toast.setGravity(Gravity.CENTER, 0, 0);
						toast.show();
				}
					db.close();
				}
			});
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
