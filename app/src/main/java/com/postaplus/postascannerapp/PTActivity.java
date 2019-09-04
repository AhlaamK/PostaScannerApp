package com.postaplus.postascannerapp;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;


public class PTActivity extends MasterActivity {
    TextView username,delvtxt,consignee,delvaddress,delvarcity,delvcnctperson,pickupno,shippername,phoneno;
    Button back;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ptdetail);
        pref = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        username=(TextView) findViewById(R.id.unametxt);
        Button back=(Button)findViewById(R.id.btnbck);
        username.setText(pref.getString("uname", ""));

        pickupno=(TextView) findViewById(R.id.textpickupno);
        shippername=(TextView) findViewById(R.id.textshippername);
        consignee=(TextView) findViewById(R.id.textconsignee);
        delvaddress=(TextView) findViewById(R.id.textdelvaddr);
        delvarcity=(TextView) findViewById(R.id.textdelvarcty);
        phoneno=(TextView) findViewById(R.id.textdelvphone);

        System.out.println("Value for pickupno in pt activity"+getIntent().getExtras().getString("pickupno"));
        pickupno.setText(getIntent().getExtras().getString("pickupno"));
       // shippername.setText(getIntent().getExtras().getString("shippername"));
       /* consignee.setText(getIntent().getExtras().getString("consignee"));
        delvaddress.setText(getIntent().getExtras().getString("deliveryadd"));
        delvarcity.setText(getIntent().getExtras().getString("deliverycity"));
        phoneno.setText(getIntent().getExtras().getString("deliverycontactno"));*/
        try{

            db=new DatabaseHandler(getBaseContext());
            //open localdatabase in a read mode
            sqldb = db.getReadableDatabase();
            Cursor c2 = sqldb.rawQuery("SELECT * FROM pickuphead WHERE Pickup_No='"+ getIntent().getExtras().getString("pickupno") +"'", null);
             int count=c2.getCount();
            c2.moveToFirst();
            System.out.println("Value for count in ptactivity"+count);
            System.out.println("Value for accountname in ptactivity"+c2.getString(c2.getColumnIndex("Account_Name")));

            shippername.setText(c2.getString(c2.getColumnIndex("Account_Name")));
            consignee.setText(c2.getString(c2.getColumnIndex("ConsigneeName")));
            delvaddress.setText(c2.getString(c2.getColumnIndex("DeliveryAddress")));
            delvarcity.setText(c2.getString(c2.getColumnIndex("DeliveryCity")));
            phoneno.setText(c2.getString(c2.getColumnIndex("DeliveryPhone")));
            c2.close();
            db.close();
            sqldb.close();
        }
        catch (Exception e)
        {
            Log.e("Pt activity", "PT activity is errored");
            e.printStackTrace();
        }

        back.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                v.startAnimation(AnimationUtils.loadAnimation(getBaseContext(), R.anim.image_click));

                // moveTaskToBack(true);
                PTActivity.this.finish();


            }
        });
    }
}
