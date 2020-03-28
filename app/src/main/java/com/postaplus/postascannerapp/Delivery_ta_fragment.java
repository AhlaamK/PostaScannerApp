package com.postaplus.postascannerapp;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.content.ContentValues;
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
import android.util.Log;
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

import webservice.FuncClasses.CheckTranswaybill;
import webservice.WebService;



//fragment for transfer accept
public class Delivery_ta_fragment extends Fragment
{
   public static final String TAG = "TATAG";
   Button back,scan,transfer;
   String drivercode,route,routen,runsheet_res,date;
   ProgressBar Pb;
   View rootView;
   SharedPreferences pref;
   Long var1;
   public int SCANNER_REQUEST_CODE = 123;
   TableLayout  resulttab;
   TableRow tr;
   LayoutParams lp ;
   String waybill1="",attempt1="",rname1="",cname1="",error1="",phone="",area1="",company1="",civilid1="",serial1="",cardtype1="",deldate1="",deltime1="",amount1="",address1="";
   DatabaseHandler db;
   SQLiteDatabase sqldb;
   static boolean errored = false;
   String waybill;
   int count;
   Thread tta;
   BluetoothDevice _btDevice = null;
    CheckTranswaybill chktranswbllResponse;

   public FragmentActivity MYActivity;



   public void ScannerTAExecutions()
   {

       MYActivity = DeliveryActivity.TAActivity;


       route= MYActivity.getIntent().getExtras().getString("routecode");
       routen= MYActivity.getIntent().getExtras().getString("routename");
       waybill=DeliveryActivity.waybillFromScanner;
       System.out.println("TA Fragment selected");


       System.out.println("TA Fragment selected");

       rootView = DeliveryActivity.TArootView;
       if(resulttab == null) resulttab = (TableLayout) rootView.findViewById(R.id.resulttable1);
       if (Pb == null) Pb = (ProgressBar)rootView.findViewById(R.id.progressBar1);
       if (transfer == null) transfer=(Button)rootView.findViewById(R.id.buttontransfer);

       db=new DatabaseHandler(MYActivity.getBaseContext());
       sqldb = db.getReadableDatabase();
       Cursor c1 = sqldb.rawQuery("SELECT Username FROM logindata WHERE Loginstatus=1", null);
       c1.moveToFirst();
       if(c1.getCount()>0){
           drivercode=c1.getString(c1.getColumnIndex("Username"));
       }
       c1.close();
       db.close();

       new Task1().execute();

   }

   @Override
   public View onCreateView(LayoutInflater inflater, ViewGroup container,
           Bundle savedInstanceState) {
       System.out.println("TA Fragment selected");
       rootView = inflater.inflate(R.layout.activity_delivery_transfer, container, false);
       resulttab=(TableLayout)rootView.findViewById(R.id.resulttable1);
       back=(Button)rootView.findViewById(R.id.btnbck);

       Pb = (ProgressBar)rootView.findViewById(R.id.progressBar1);
       transfer=(Button)rootView.findViewById(R.id.buttontransfer);
       StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
       StrictMode.setThreadPolicy(policy);

       MYActivity = getActivity();
       pref = this.getActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
       //saving route name and driver code to a variable
       route= getActivity().getIntent().getExtras().getString("routecode");
       routen= getActivity().getIntent().getExtras().getString("routename");

       db=new DatabaseHandler(getActivity().getBaseContext());
       //open localdatabase in a read mode
       sqldb = db.getReadableDatabase();
       //select the username which has login status 1
       Cursor c1 = sqldb.rawQuery("SELECT Username FROM logindata WHERE Loginstatus=1", null);
       c1.moveToFirst();
       if(c1.getCount()>0){
           drivercode=c1.getString(c1.getColumnIndex("Username"));
       }
       c1.close();



       Cursor c21 = sqldb.rawQuery("SELECT * FROM TransferAcceptTemp WHERE Transfer_Status='P'", null);
       int count1=c21.getCount();
       String[] wbill21=new String[count1];
       String[] rname21=new String[count1];
       String[] cname21=new String[count1];
       String[] amount21=new String[count1];


       c21.moveToFirst();

       if(count1>0)
       {
           for(int i=0;i<count1;i++)
               {

               wbill21[i] = c21.getString(c21.getColumnIndex("Waybill"));
               rname21[i]= c21.getString(c21.getColumnIndex("Routecode"));
               cname21[i]= c21.getString(c21.getColumnIndex("Consignee"));
               amount21[i]= c21.getString(c21.getColumnIndex("Amount"));

                       tr = new TableRow(getActivity());

                       if(Build.MODEL.contains("SM-N"))
                        {

                            lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
                            lp.setMargins(18, 2, 95, 2);
                            tr.setLayoutParams(lp);

                        }
                        else
                        {


                            lp = new LayoutParams(200,LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);

                            tr.setLayoutParams(lp);
                            lp.setMargins(0, 20,40, 0);
                        }
                   TextView waybilltxt1 = new TextView(MYActivity);
                   waybilltxt1.setLayoutParams(lp);
                   waybilltxt1.setText(wbill21[i]);

                   TextView rnametxt = new TextView(MYActivity);
                   rnametxt.setPadding(0,20,70,0);
                   rnametxt.setText(rname21[i]);

                   TextView cnametxt = new TextView(MYActivity);
                   cnametxt.setPadding(0,20,70,0);
                   cnametxt.setText(cname21[i]);

                   TextView amounttxt = new TextView(MYActivity);
                   amounttxt.setPadding(50,20,150,0);
                   amounttxt.setText(amount21[i]);

                   tr.addView(waybilltxt1);
                   tr.addView(rnametxt);
                   tr.addView(cnametxt);
                   tr.addView(amounttxt);

                   resulttab.addView(tr, new TableLayout.LayoutParams (LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

                   c21.moveToNext();

       }

           c21.close();
           db.close();



       }

       back.setOnClickListener(v -> {
           v.startAnimation(AnimationUtils.loadAnimation(getActivity().getBaseContext(), R.anim.image_click));
           DeliveryActivity.DonotInterruptKDCScan=false;
           getActivity().finish();
       });
       transfer.setOnClickListener(v -> {
           v.startAnimation(AnimationUtils.loadAnimation(getActivity().getBaseContext(), R.anim.image_click));
           DeliveryActivity.DonotInterruptKDCScan=false;
           runsheet_res= WebService.SET_TRANS_CONFIRM(drivercode);
           if(!errored)
           {
           if(!runsheet_res.contains("ALREADY SCANNED")&&!runsheet_res.contains("WAYBILL NOT READY")&&!runsheet_res.contains("NO"))
           {
           db=new DatabaseHandler(getActivity().getBaseContext());
           sqldb = db.getReadableDatabase();

           Cursor c = sqldb.rawQuery("SELECT Runsheetcode FROM logindata WHERE Username='"+drivercode+"'", null);
           c.moveToFirst();
           if(c.getCount()>0){
               String runsheet1=c.getString(c.getColumnIndex("Runsheetcode"));
               if(runsheet1==null)
               {
                   sqldb = db.getWritableDatabase();
                   sqldb.execSQL("UPDATE logindata SET Runsheetcode='"+runsheet_res+"'WHERE Username='"+drivercode+"'" );
                   Toast.makeText(getActivity().getApplicationContext(), "Transfer confirmed", Toast.LENGTH_LONG).show();

               }
               else{
                   System.out.println("runsheet1 is:"+runsheet1+"runsheet_res is:"+runsheet_res);
                   if(runsheet1.equals(runsheet_res))
                    {
                        Toast.makeText(getActivity().getApplicationContext(), "Transfer confirmed", Toast.LENGTH_LONG).show();
                    }
                    else {

                        Toast.makeText(getActivity().getApplicationContext(), "Runsheet not same,Transfer Error", Toast.LENGTH_LONG).show();
                    }
               }
           }
           c.close();

           for (int i = resulttab.getChildCount(); i >=0; i--) {
                resulttab.removeAllViews();

            }
               sqldb = db.getWritableDatabase();
           sqldb.execSQL("DELETE FROM deliverydata WHERE Drivercode <> '"+drivercode+"'");

           sqldb.execSQL("DELETE FROM TransferAcceptTemp WHERE Drivercode ='"+drivercode+"'");
               sqldb = db.getReadableDatabase();

           Cursor c11 = sqldb.rawQuery("SELECT * FROM deliverydata WHERE Waybill='"+waybill1+"'", null);
           int count11 = c11.getCount();

           if(count11 >0){


               sqldb =db.getWritableDatabase();

               sqldb.execSQL("UPDATE deliverydata SET Drivercode='"+drivercode+"',Routecode='"+route
                       +"',Consignee='"+cname1+"',Telephone='"+phone+"',"+"Area='"+area1+"',"+"Company='"
                       +company1+"',CivilID='"+civilid1+"',Serial='"+serial1+"',CardType='"+cardtype1+"',DeliveryDate='"+deldate1+"',DeliveryTime='"
                       +deltime1+"',Amount='"+amount1+"',StopDelivery=0,WC_Status='A',WC_Transfer_Status=0,TransferStatus=0,Attempt_Status=0,Address='"+address1+"' WHERE Waybill='"+waybill1+"'");



           }
           else{
               c11.moveToLast();

               sqldb =db.getWritableDatabase();
               ContentValues values = new ContentValues();
               values.put("Drivercode", drivercode);
               values.put("Routecode",route);
               values.put("Waybill", waybill1);
               values.put("Consignee", cname1);
               values.put("Telephone",phone);
               values.put("Area",area1);
               values.put("Company",company1);
               values.put("CivilID",civilid1);
               values.put("Serial", serial1);
               values.put("CardType", cardtype1);
               values.put("DeliveryDate",deldate1);
               values.put("DeliveryTime", deltime1);
               values.put("Amount",amount1);
               values.put("WC_Status","A");
               values.put("StopDelivery","0");
               values.put("WC_Transfer_Status","0");
               values.put("TransferStatus","0");
               values.put("Attempt_Status","0");
               values.put("Address",address1);

               sqldb.insertOrThrow("deliverydata", null, values);

           }


           c11.close();

           db.close();
           }
           else{
               MYActivity.runOnUiThread(new Runnable(){
                   @Override
                   public void run(){

                       Toast.makeText(MYActivity,runsheet_res,
                                 Toast.LENGTH_LONG).show();
                   }
               });
           }}
           else{
               MYActivity.runOnUiThread(new Runnable(){
                       @Override
                       public void run(){

                           Toast.makeText(MYActivity,"Connection Error",
                                     Toast.LENGTH_LONG).show();
                       }
                   });

           }
       });
       DeliveryActivity.TArootView= rootView;
       System.out.println("TA View Instance ");
       System.out.println(DeliveryActivity.TArootView);

       DeliveryActivity.TAActivity = getActivity();
       System.out.println("TA Context Instance ");
       System.out.println(DeliveryActivity.TAActivity);
       return rootView;
   }
   @Override
      public void onActivityResult(int requestCode, int resultCode, Intent intent) {

          if (requestCode == SCANNER_REQUEST_CODE) {
              if (resultCode == Activity.RESULT_OK) {
                  waybill= intent.getStringExtra("SCAN_RESULT");
                  new Task1().execute();
              }
          }
   }
   @Override
   public void onResume() {
       super.onResume();
       MYActivity = DeliveryActivity.TAActivity;
       rootView = DeliveryActivity.TArootView;
       resulttab = rootView.findViewById(R.id.resulttable1);
       DeliveryActivity.kdcscannercallfrom = "TAFragment";
   }
    public class Task1 extends AsyncTask<Void, Void, String>
       {
           public void onPreExecute()
             {
                 MYActivity.runOnUiThread(() -> Pb.setVisibility(View.VISIBLE));


               tr = new TableRow(MYActivity);
               if(Build.MODEL.contains("SM-N"))
                   {
                       lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
                       lp.setMargins(18, 2, 95, 2);
                       tr.setLayoutParams(lp);
                   }
                   else
                   {
                       lp = new LayoutParams(200,LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
                       lp.setMargins(0, 20, 40, 0);
                       tr.setLayoutParams(lp);
                   }
             }
               @Override
           protected String doInBackground(Void... arg0)
           {
                   gettranswaybill();
                  return "";
           }
               private void gettranswaybill() {
                   chktranswbllResponse = null;
                   if (drivercode == null || drivercode.equals("") || waybill == null || waybill.equals(""))
                   {
                       MYActivity.runOnUiThread(() -> Toast.makeText(MYActivity,"Try again! Required Values Blank",
                                 Toast.LENGTH_LONG).show());
                       return;
                   }
                try{
                  chktranswbllResponse = WebService.CHECK_TRANSWAYBILL(drivercode,waybill);
                    System.out.println("chktranswbllResponse in DA"+chktranswbllResponse);
                    if(chktranswbllResponse == null)return;

                   if(chktranswbllResponse.ErrMsg ==null||chktranswbllResponse.ErrMsg=="") {
                       waybill1 = chktranswbllResponse.WayBill;
                       rname1 = chktranswbllResponse.RouteName;
                       cname1 = chktranswbllResponse.ConsignName;
                       phone = chktranswbllResponse.PhoneNo;
                       area1 = chktranswbllResponse.Area;
                       company1 = chktranswbllResponse.Company;
                       civilid1 = chktranswbllResponse.CivilId;
                       serial1 = chktranswbllResponse.Serial;
                       cardtype1 = chktranswbllResponse.CardType;
                       deldate1 = chktranswbllResponse.DelDate;
                       deltime1 = chktranswbllResponse.DelTime;
                       amount1 = chktranswbllResponse.Amount;
                       error1 = chktranswbllResponse.ErrMsg;
                       attempt1 = chktranswbllResponse.Attempt;
                       address1 = chktranswbllResponse.Address;

                       final TextView waybilltxt = new TextView(MYActivity);
                       waybilltxt.setLayoutParams(lp);
                       waybilltxt.setText(waybill1);
                       final TextView rnametxt = new TextView(MYActivity);
                      rnametxt.setPadding(0,20,70,0);
                       rnametxt.setText(rname1);

                       final TextView cnametxt = new TextView(MYActivity);
                      cnametxt.setPadding(0,20,70,0);
                       cnametxt.setText(cname1);

                       final TextView amounttxt = new TextView(MYActivity);
                       amounttxt.setPadding(50,20,150,0);
                       amounttxt.setText(amount1);
                       tr.addView(waybilltxt);
                       tr.addView(rnametxt);
                       tr.addView(cnametxt);
                       tr.addView(amounttxt);
                   }
                    error1 = chktranswbllResponse.ErrMsg;

                   }
                   catch(Exception e)
                   {
                       e.printStackTrace();
                   }
               }
           @Override
           public void onPostExecute(String res)
           {
               if (chktranswbllResponse == null)
               {
                   Pb.setVisibility(View.INVISIBLE);
                   Toast.makeText(MYActivity.getApplicationContext(),"Please Try again!",
                           Toast.LENGTH_LONG).show();
                   return;
               }

                   if(chktranswbllResponse.ErrMsg==null||chktranswbllResponse.ErrMsg=="")
               {

                   resulttab.addView(tr, new TableLayout.LayoutParams (LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
                   db=new DatabaseHandler(MYActivity.getBaseContext());

                   sqldb = db.getWritableDatabase();
                   sqldb.execSQL("DELETE FROM TransferAcceptTemp WHERE Drivercode <> '"+drivercode+"'");
                   sqldb = db.getReadableDatabase();
                   Cursor c1 = sqldb.rawQuery("SELECT * FROM TransferAcceptTemp WHERE Waybill='"+waybill1+"'", null);
                   int count1=c1.getCount();
                   if(count1>0){
                       sqldb =db.getWritableDatabase();

                       sqldb.execSQL("UPDATE TransferAcceptTemp SET Drivercode='"+drivercode+"',Routecode='"+rname1
                               +"',Consignee='"+cname1+"',Telephone='"+phone+"',"+"Area='"+area1+"',"+"Company='"
                               +company1+"',CivilID='"+civilid1+"',Serial='"+serial1+"',CardType='"+cardtype1+"',DeliveryDate='"+deldate1+"',DeliveryTime='"
                               +deltime1+"',Amount='"+amount1+"',Transfer_Status='P',Address='"+address1+"',Attempt_Status='"+attempt1+"' WHERE Waybill='"+waybill1+"'");
                   }
                   else{
                       c1.moveToLast();
                       sqldb =db.getWritableDatabase();
                       ContentValues values = new ContentValues();
                       values.put("Drivercode", drivercode);
                       values.put("Routecode",rname1);
                       values.put("Waybill", waybill1);
                       values.put("Consignee", cname1);
                       values.put("Telephone",phone);
                       values.put("Area",area1);
                       values.put("Company",company1);
                       values.put("CivilID",civilid1);
                       values.put("Serial", serial1);
                       values.put("CardType", cardtype1);
                       values.put("DeliveryDate",deldate1);
                       values.put("DeliveryTime", deltime1);
                       values.put("Amount",amount1);
                       values.put("Transfer_Status","P");
                       values.put("Address",address1);
                       values.put("Attempt_Status",attempt1);
                       sqldb.insertOrThrow("TransferAcceptTemp", null, values);


                   }
                   c1.close();

                   db.close();
               }
               else
                       Toast.makeText(MYActivity.getApplicationContext(),error1,
                               Toast.LENGTH_LONG).show();
               MYActivity.runOnUiThread(() -> Pb.setVisibility(View.INVISIBLE));
           }
}
    public void startASycncScanerTAExecute() {
        new StartAsyncTask_ScanTA().execute();
    }
    public class StartAsyncTask_ScanTA extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {

            ScannerTAExecutions();
            return null;
        }
    }
}