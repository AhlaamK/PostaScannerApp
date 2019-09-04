package com.postaplus.postascannerapp;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
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


public class Transfer_pickup_fragment extends Fragment {

	private Spinner courierlist;
	Button back,selectall,transfer_pickup,deselectall;
	String dcode,transcourier,rname,rcode,paytype,servicetype,pickuptransferstatus;
	ProgressBar pg;
	DatabaseHandler db;
	SQLiteDatabase sqldb;
	TableLayout  resulttab;
	TableRow tr;
	LayoutParams lp ;
	static boolean errored;
	CheckBox picknotxt;
	TextView waybilltxt,accnotxt,wbilltxt,paytxt,amounttxt,servicetxt,counttxt;
	//String waybill,pickupno,service,paytype,amount,serviceId,serviceType,payId,accountname,date_time;
	SharedPreferences pref;
	String[] wbillarr,picknoarr,paytpearr,amountarr,servicearr,waybill1,pick1;
	private String[] drivname,drivcode;


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.activity_pickup_transfer, container, false);
		back=(Button)rootView.findViewById(R.id.btnbck);
		courierlist = (Spinner)rootView.findViewById(R.id.spinnercourier);

		pg = (ProgressBar)rootView.findViewById(R.id.progressBar1);
		resulttab=(TableLayout)rootView.findViewById(R.id.tlTable);
		selectall=(Button) rootView.findViewById(R.id.btnselect);
		deselectall=(Button) rootView.findViewById(R.id.btndeselect);
		transfer_pickup=(Button)rootView.findViewById(R.id.buttontransfer);
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
		//populate the driver code in spinner
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity().getBaseContext(), android.R.layout.simple_spinner_item, drivname);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		courierlist.setAdapter(adapter);

		// closing connection
		c1.close();

		//select the pickup number from pickup head where status=c
		//SQLiteDatabase db1 =db.getReadableDatabase();

		Cursor rr121 = sqldb.rawQuery("SELECT Pickup_No FROM pickuphead WHERE Status='C'", null);
		int c11=rr121.getCount();

		System.out.println("c11 s:"+c11+"rr121 is/:"+rr121);
		String[] pickno=new String[c11];
		System.out.println("pickno:"+pickno.toString());
		rr121.moveToFirst();
		if(c11>0)
		{

			for(int i=0;i<c11;i++)
			{
				pickno[i]=rr121.getString(rr121.getColumnIndex("Pickup_No"));
				//System.out.println(pickno[i]);
				System.out.println("pickno[i]:"+pickno[i]);
				//select data from pickupdetail have status completed
				Cursor rr123 = sqldb.rawQuery("SELECT DISTINCT Pickup_No,PayType,Amount,ServiceType FROM pickupdetails WHERE Pickup_No='"+pickno[i]+"'", null);
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
						//wbillarr[i1]=rr123.getString(rr123.getColumnIndex("Waybill_Number"));
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



						tr = new TableRow(getActivity());

						if(Build.MODEL.contains("SM-N"))
						{

							lp = new LayoutParams(420,LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);

							tr.setLayoutParams(lp);
							lp.setMargins(0, 10, 40, 0);

						}
						else
						{
 						/*lp = new LayoutParams(150,LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
 						
 						tr.setLayoutParams(lp);
 						lp.setMargins(0, 20, 10, 0);*/
							lp = new LayoutParams(LayoutParams.WRAP_CONTENT);
							lp.setMargins(0, 20, 115, 0);
							tr.setLayoutParams(lp);
						}
 						
 						/*waybilltxt = new TextView(getActivity());
 						waybilltxt.setLayoutParams(lp);
 						waybilltxt.setText(wbillarr[i1]);*/


						final CheckBox picknotxt = new CheckBox(getActivity());
						picknotxt.setLayoutParams(lp);
						picknotxt.setText(picknoarr[i1]);


						picknotxt.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								v.startAnimation(AnimationUtils.loadAnimation(getActivity().getBaseContext(), R.anim.image_click));
								//System.out.println(picknotxt.getText().toString());
								if(picknotxt.isChecked()){
									Intent int1 = new Intent(getActivity(),SelectedPick_Activity.class);

									int1.putExtra("pickno",picknotxt.getText().toString());
									//int1.putExtra("accno",acname);
									//int1.putExtra("routecode",route);
									//int1.putExtra("routename",routen);
									startActivity(new Intent(int1));
								}
							}
						});



						paytxt= new TextView(getActivity());
						paytxt.setLayoutParams(lp);

						System.out.println("paytyp is:"+paytype);
//added because if null layout shifts
						if(paytype == null){
							paytxt.setText("   NA        ");
						}else paytxt.setText(paytype);



						amounttxt = new TextView(getActivity());
						amounttxt.setLayoutParams(lp);
						amounttxt.setText(amountarr[i1]);

						servicetxt= new TextView(getActivity());
						servicetxt.setLayoutParams(lp);
						servicetxt.setText(servicetype);

						tr.addView(picknotxt);
						//tr.addView(waybilltxt);

						tr.addView(paytxt);
						tr.addView(amounttxt);
						tr.addView(servicetxt);

						resulttab.addView(tr, new TableLayout.LayoutParams (LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
						counttxt.setText(String.valueOf(resulttab.getChildCount()));
					}
				}
				rr123.close();
				rr121.moveToNext();
			}

		}

		else
		{
			Toast.makeText(getActivity().getApplicationContext(),"NO Data", Toast.LENGTH_LONG).show();
		}
		//c2.close();
		rr121.close();


		db.close();
		selectall.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				v.startAnimation(AnimationUtils.loadAnimation(getActivity().getBaseContext(), R.anim.image_click));
				for (int i = 0; i < resulttab.getChildCount(); i++) {
					picknotxt = (CheckBox)((TableRow)resulttab.getChildAt(i)).getChildAt(0);
					picknotxt.setChecked(true);

				}


			}
		});




		//trasfer the selceted pickup to selected courier
		transfer_pickup.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				v.startAnimation(AnimationUtils.loadAnimation(getActivity().getBaseContext(), R.anim.image_click));
				db=new DatabaseHandler(getActivity().getBaseContext());
				sqldb =db.getWritableDatabase();
				transcourier = String.valueOf(drivcode[courierlist.getSelectedItemPosition()]);
				int flag=0;
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
				picknoarr=new String[resulttab.getChildCount()];
				for (int i = 0; i < resulttab.getChildCount(); i++)
				{
					picknotxt = (CheckBox)((TableRow)resulttab.getChildAt(i)).getChildAt(0);
					if(picknotxt.isChecked())
					{
						picknoarr[i]=picknotxt.getText().toString();
						System.out.println(picknoarr[i]);

						//pickuptransferstatus=WebService.settranspick(dcode,transcourier,picknoarr[i],MasterActivity.METHOD_NAME33);
						pickuptransferstatus= webservice.WebService.SET_TRANS_PICKUP(dcode,transcourier,picknoarr[i]);
						flag=0;
						if(!errored){
							if(pickuptransferstatus.contains("TRANSFERED"))
							{
								flag=1;
								sqldb.execSQL("UPDATE pickuphead SET TransferStatus=2 WHERE Pickup_No='"+picknoarr[i]+"'");

							}
							else{
								flag=0;
							}
						}
						else{
							Toast.makeText(getActivity(), "Connection Error", Toast.LENGTH_LONG).show();
						}
					}
				}
				if(flag==1)
				{

					Toast.makeText(getActivity(), "Tranferred", Toast.LENGTH_LONG).show();

				}
				else{
					Toast.makeText(getActivity(), "INVALID PICKUP", Toast.LENGTH_LONG).show();
				}
				db.close();


			}
		});

		deselectall.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				v.startAnimation(AnimationUtils.loadAnimation(getActivity().getBaseContext(), R.anim.image_click));
				for (int i = 0; i < resulttab.getChildCount(); i++) {
					picknotxt = (CheckBox)((TableRow)resulttab.getChildAt(i)).getChildAt(0);
					picknotxt.setChecked(false);
				}


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

}
