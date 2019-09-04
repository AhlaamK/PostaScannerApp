package com.postaplus.postascannerapp;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

public class WaybillAdressActivity extends MasterActivity {
	TextView username,wbtxt,constxt,comptxt,addtxt,deldatetxt,deltimetxt;
	String wbill,consign,company,addr,deldate,deltime;
	TextView textView14,textView15;
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_wbilldetail);
		pref = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
		username=(TextView) findViewById(R.id.unametxt);
		Button back=(Button)findViewById(R.id.btnbck);
		username.setText(pref.getString("uname", ""));

		wbtxt=(TextView) findViewById(R.id.textwbill);
		constxt=(TextView) findViewById(R.id.textconsignee);
		comptxt=(TextView) findViewById(R.id.textcompany);
		addtxt=(TextView) findViewById(R.id.textaddr);
		deldatetxt=(TextView) findViewById(R.id.textdeldate);
		deltimetxt=(TextView) findViewById(R.id.textdeltime);
		textView14=(TextView) findViewById(R.id.textView14);
		textView15=(TextView) findViewById(R.id.textView15);
		wbill=getIntent().getExtras().getString("wbillno");
		System.out.println(wbill);
		wbtxt.setText(wbill);

		db=new DatabaseHandler(getBaseContext());
		SQLiteDatabase sqldb =db.getReadableDatabase();

		Cursor rr123 = sqldb.rawQuery("SELECT * FROM deliverydata WHERE Waybill='"+wbill.toString().trim()+"'", null);
		int c112=rr123.getCount();

		//String[] date_time=new String[c112];
		rr123.moveToFirst();
		if(c112>0)
		{

			consign=rr123.getString(rr123.getColumnIndex("Consignee"));
			company=rr123.getString(rr123.getColumnIndex("Company"));
			addr=rr123.getString(rr123.getColumnIndex("Address"));
			deldate= rr123.getString(rr123.getColumnIndex("DeliveryDate"));
			deltime= rr123.getString(rr123.getColumnIndex("DeliveryTime"));




		}
		rr123.close();

		db.close();


		constxt.setText(consign);
		comptxt.setText(company);
		addtxt.setText(addr);
		if(deldate!=null){
			if(!deldate.toString().isEmpty()&&!deldate.toString().equals("00:00-00:00")){
				SimpleDateFormat format = new SimpleDateFormat("dd-MMM-yyyy");
				String date = format.format(Date.parse(deldate));
				deldatetxt.setText(date);
			}else{
				deldatetxt.setText("");
				textView14.setVisibility(View.GONE);
			}

		}else{
			deldatetxt.setText("");
			textView14.setVisibility(View.GONE);
		}
		if(deltime!=null){
			if(deltime.toString()!=""&&!deltime.toString().equals("00:00-00:00")){
				deltimetxt.setText(deltime);

			}else {
				deltimetxt.setText("");
				textView15.setVisibility(View.GONE);

			}
		}else {
			deltimetxt.setText("");
			textView15.setVisibility(View.GONE);

		}







		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				v.startAnimation(AnimationUtils.loadAnimation(getBaseContext(), R.anim.image_click));

				// moveTaskToBack(true);
				WaybillAdressActivity.this.finish();


			}
		});
	}
}
