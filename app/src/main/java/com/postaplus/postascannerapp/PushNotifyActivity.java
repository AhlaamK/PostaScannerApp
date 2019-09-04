package com.postaplus.postascannerapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;

public class PushNotifyActivity extends MasterActivity implements View.OnClickListener {
    TextView textView_pushawb,pushWaybilltxt,username;
    TableRow trdialog;
    TableLayout resulttabledialog;
    TableRow.LayoutParams lp;
    SharedPreferences pref;
    ImageView imageViewPushicon;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_push_notify);
        textView_pushawb = (TextView)findViewById(R.id.textView_pushawb);
        textView_pushawb.setTextColor(Color.parseColor("#0000EE"));
        textView_pushawb.setPaintFlags(textView_pushawb.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        textView_pushawb.setOnClickListener(this);
        resulttabledialog=(TableLayout)findViewById(R.id.resulttabledialog);
       // textViewPushicon.setText(String.valueOf(pushWaybillCount));

        username=(TextView) findViewById(R.id.unametxt);
        pref = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        username.setText(pref.getString("uname", ""));
        trdialog = new TableRow(PushNotifyActivity.this);
       /* imageViewPushicon = (ImageView) findViewById(R.id.imageViewPushicon);
        imageViewPushicon.setVisibility(View.GONE);*/
        imageViewPushicon = (ImageView) findViewById(R.id.imageViewPushicon);
        imageViewPushicon.setVisibility(View.GONE);

        textViewPushicon = (TextView)findViewById(R.id.textViewPushicon);
        textViewPushicon.setVisibility(View.GONE);
        callDialogAlert();
       /* AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Please ensure you synch once to see updated shipments!")
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //do things
                        Intent intent=new Intent(getApplicationContext(),HomeActivity.class);
                        startActivity(intent);
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();*/
        if (Build.MODEL.contains("SM-N")) {


            lp = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT);
            lp.setMargins(18, 2, 95, 2);
            trdialog.setLayoutParams(lp);

        } else {
            lp = new TableRow.LayoutParams(200, TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT);

            trdialog.setLayoutParams(lp);
            lp.setMargins(0, 5, 70, 0);
        }
       /* if(pushawbStrarray  == null|| pushawbStrarray.length==0){
            Toast toast = Toast.makeText(getApplicationContext(),"You do not have notifications", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
            return;
        }*/


        for(int i = 0;i< pushawbStrarray.length;i++){
            trdialog = new TableRow(PushNotifyActivity.this);

            pushWaybilltxt = new TextView(PushNotifyActivity.this);
            pushWaybilltxt.setGravity(Gravity.CENTER_HORIZONTAL);
            pushWaybilltxt.setText(pushawbStrarray[i].toString());
            pushWaybilltxt.setTextColor(Color.parseColor("#0000EE"));
            pushWaybilltxt.setPaintFlags(pushWaybilltxt.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
            pushWaybilltxt.setOnClickListener(this);
            trdialog.addView(pushWaybilltxt);

            resulttabledialog.addView(trdialog, new TableLayout.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));

        }





    }

    @Override
    public void onClick(View v) {
        TextView awbnotifyText = (TextView)v;
        String awbpushText = awbnotifyText.getText().toString();
        View row = (View) v.getParent();



        List<String> list = new ArrayList<String>(Arrays.asList(pushawbStrarray));
        list = new ArrayList<String>(new LinkedHashSet<String>(list));
        list.remove(awbpushText.trim());
        pushawbStrarray = list.toArray(new String[0]);
        updatedPushWaybill= updatedPushWaybill.replace(awbpushText.trim(),"");
        updatedPushWaybill= updatedPushWaybill.replace(",,",",");
        updatedPushWaybill = updatedPushWaybill.replaceAll(", $", "");
        pushWaybillCount= pushWaybillCount-1;
        if(pushWaybillCount==0)textViewPushicon.setText(String.valueOf(""));
else textViewPushicon.setText(String.valueOf(pushWaybillCount));

        resulttabledialog.removeView(row);
        resulttabledialog.invalidate();
        Intent intent=new Intent(getApplicationContext(),WaybillAdressActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("wbillno", awbnotifyText.getText().toString());
        startActivity(intent);
        finish();

    }
    public void callDialogAlert(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Confirm if you have synched the Shipments");
        builder.setMessage("Choose YES to continue or NO to synch them!");
        builder.setCancelable(false);

        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                // Do nothing but close the dialog

                dialog.dismiss();
            }
        });

        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                Intent intent=new Intent(getApplicationContext(),HomeActivity.class);
                startActivity(intent);
                finish();
                // Do nothing
                dialog.dismiss();
            }
        });

        AlertDialog alert = builder.create();
        alert.show();
    }


}
