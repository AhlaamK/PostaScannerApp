package com.postaplus.postascannerapp;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TableRow.LayoutParams;
import android.widget.TextView;

public class SelectedPick_Activity extends MasterActivity {
	TextView username,picknotxt,waybilltxt,paytxt,amounttxt,servicetxt;
	TableLayout  resulttab;
	TableRow tr;
	LayoutParams lp ;
	String pickupno,paytype,servicetype,rname,rcode;
	String[] wbillarr,picknoarr,paytpearr,amountarr,servicearr,waybill1,pick1;
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_selectedpickupdetail);
		pref = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
		username=(TextView) findViewById(R.id.unametxt);
		Button back=(Button)findViewById(R.id.btnbck);
		username.setText(pref.getString("uname", ""));			
		resulttab=(TableLayout)findViewById(R.id.tlTable);
		
		pickupno=getIntent().getExtras().getString("pickno");
		//System.out.println("pick:"+pickupno);
		  back.setOnClickListener(new OnClickListener() {
		      	 
		        @Override
		        public void onClick(View v) {
		        	v.startAnimation(AnimationUtils.loadAnimation(getBaseContext(), R.anim.image_click));
		        	
		        	// moveTaskToBack(true); 
		   	       SelectedPick_Activity.this.finish();
		   	   
		        	
		        }
		    });
		
		
		 db=new DatabaseHandler(getBaseContext());
		 SQLiteDatabase sqldb =db.getReadableDatabase();
		//	Cursor rr121 = sqldb.rawQuery("SELECT Pickup_No FROM pickuphead WHERE Status='C'", null);
		//	int c11=rr121.getCount();


		//	String[] pickno=new String[c11];				

		//	rr121.moveToFirst();
		//	if(c11>0)
			//{

			//	for(int i=0;i<c11;i++)
			//	{
			//		pickno[i]=rr121.getString(rr121.getColumnIndex("Pickup_No"));
					//System.out.println(pickno[i]);
					
					//select data from pickupdetail have status completed
					Cursor rr123 = sqldb.rawQuery("SELECT * FROM pickupdetails WHERE Pickup_No='"+pickupno+"'", null);
					int c112=rr123.getCount();


					picknoarr=new String[c112];
					wbillarr=new String[c112];
					paytpearr=new String[c112];
					amountarr=new String[c112];
					servicearr=new String[c112];
					//String[] date_time=new String[c112];
					rr123.moveToFirst();
					if(c112>0)
					{
						//rr123.moveToFirst();
						for(int i1=0;i1<c112;i1++)
						{
							picknoarr[i1]=rr123.getString(rr123.getColumnIndex("Pickup_No"));
							wbillarr[i1]=rr123.getString(rr123.getColumnIndex("Waybill_Number"));
							paytpearr[i1]=rr123.getString(rr123.getColumnIndex("PayType"));

							amountarr[i1]=rr123.getString(rr123.getColumnIndex("Amount"));
							servicearr[i1]=rr123.getString(rr123.getColumnIndex("ServiceType"));
							
							//System.out.println(picknoarr[i1]+","+wbillarr[i1]+","+paytpearr[i1]+","+amountarr[i1]+","+servicearr[i1]);
							Cursor pi = sqldb.rawQuery("SELECT * FROM paytypedetails WHERE PayID='"+paytpearr[i1]+"'", null);
							if(pi.getCount()>0)
							{
								pi.moveToFirst();															
								paytype =pi.getString(pi.getColumnIndex("PayTYPE"));

							}
							pi.close();
							
							Cursor se = sqldb.rawQuery("SELECT * FROM servicedetails WHERE ServiceID='"+servicearr[i1]+"'", null);
							if(se.getCount()>0)
							{
								se.moveToFirst();															
								servicetype =se.getString(se.getColumnIndex("ServiceTYPE"));

							}
							se.close();
							
							
							
							
							
							rr123.moveToNext();				

							
 				
 						tr = new TableRow(SelectedPick_Activity.this);

 						if(Build.MODEL.contains("SM-N"))
 						{
 							
 							lp = new LayoutParams(385,LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);

 							tr.setLayoutParams(lp);
 							lp.setMargins(0, 10, 40, 0);
 						
 						}
 						else
 						{
 						/*lp = new LayoutParams(150,LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
 						
 						tr.setLayoutParams(lp);
 						lp.setMargins(0, 20, 10, 0);*/
							lp = new LayoutParams(200,LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
							lp.setMargins(0, 20, 70, 0);
							tr.setLayoutParams(lp);
 						}
 						
 						waybilltxt = new TextView(SelectedPick_Activity.this);
 						waybilltxt.setLayoutParams(lp);
 						waybilltxt.setText(wbillarr[i1]);
 						

 						picknotxt = new TextView(SelectedPick_Activity.this);
 						picknotxt.setLayoutParams(lp);
 						picknotxt.setText(picknoarr[i1]);
 						
 										
 						
 						
 						paytxt= new TextView(SelectedPick_Activity.this);
 						paytxt.setLayoutParams(lp);
 						paytxt.setText(paytype);
 						
 					/*	waybilltxt= new TextView(SelectedPick_Activity.this);
 						waybilltxt.setLayoutParams(lp);
 						waybilltxt.setText(paytype);*/
 						
 						amounttxt = new TextView(SelectedPick_Activity.this);
 						amounttxt.setLayoutParams(lp);
 						amounttxt.setText(amountarr[i1]);
 						
 						servicetxt= new TextView(SelectedPick_Activity.this);
 						servicetxt.setLayoutParams(lp);
 						servicetxt.setText(servicetype);
 						
 						tr.addView(picknotxt);
 						tr.addView(waybilltxt);
 						
 						tr.addView(paytxt);
 						tr.addView(amounttxt);
 						tr.addView(servicetxt);
 						
 						resulttab.addView(tr, new TableLayout.LayoutParams (LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));			
 						
 				}
 				}
					rr123.close();
			//		rr121.moveToNext();
 				//}
					
		//	}
 			
 		//	else
 		//	{
 		//		Toast.makeText(getApplicationContext(),"NO Data", Toast.LENGTH_LONG).show();
 		//	}
 			//c2.close();
		//	rr121.close();			
			
			
			db.close();
		
		
	}
}
