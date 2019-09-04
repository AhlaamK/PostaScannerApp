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
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
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

/*import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.ksoap2.transport.ServiceConnection;*/

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
  // SoapObject response;
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
   //KDC Parameters
/*	KDCData ScannerData;
   KDCReader _kdcReader;
   DeliveryActivity _activity;
   Delivery_ta_fragment _fragment2;*/


   /*@Override
   public void onStart() {
       super.onStart();
      _kdcReader.Connect(_btDevice);
   }  */


   public void ScannerTAExecutions()
   {
       // Handle successful scan
       //String contents = intent.getStringExtra("SCAN_RESULT");
       //tvScanResults.setText(contents);
       MYActivity = DeliveryActivity.TAActivity;


       route= MYActivity.getIntent().getExtras().getString("routecode");
       routen= MYActivity.getIntent().getExtras().getString("routename");
       waybill=DeliveryActivity.WaybillFromScanner;
       System.out.println("TA Fragment selected");
       /*rootView = inflater.inflate(R.layout.activity_delivery_transfer, container, false);
       resulttab=(TableLayout)rootView.findViewById(R.id.resulttable1);
       back=(Button)rootView.findViewById(R.id.btnbck);
       scan=(Button)rootView.findViewById(R.id.btnscan);*/

       System.out.println("TA Fragment selected");

       //Initializations
       rootView = DeliveryActivity.TArootView;
       if(resulttab == null) resulttab = (TableLayout) rootView.findViewById(R.id.resulttable1);
       if (Pb == null) Pb = (ProgressBar)rootView.findViewById(R.id.progressBar1);
       if (transfer == null) transfer=(Button)rootView.findViewById(R.id.buttontransfer);

       db=new DatabaseHandler(MYActivity.getBaseContext());
       //open localdatabase in a read mode
       sqldb = db.getReadableDatabase();
       //select the username which has login status 1
       Cursor c1 = sqldb.rawQuery("SELECT Username FROM logindata WHERE Loginstatus=1", null);
       c1.moveToFirst();
       if(c1.getCount()>0){
           drivercode=c1.getString(c1.getColumnIndex("Username"));
       }
       c1.close();
       db.close();

       new Task1().execute();

       /*db=new DatabaseHandler(getActivity().getBaseContext());
       //open localdatabase in a read mode
       sqldb = db.getReadableDatabase();


       //check if the scanned wbill is in local table
       Cursor c21 = sqldb.rawQuery("SELECT * FROM deliverydata WHERE Waybill='"+waybill+"'", null);
       int count1=c21.getCount();
       //String[] wbill21=new String[count1];
       //String[] rname21=new String[count1];
       //String[] cname21=new String[count1];
       //String[] amount21=new String[count1];
       if(count1>0)
       {

           Toast.makeText(getActivity().getApplicationContext(),"Already Scanned",
                   Toast.LENGTH_LONG).show();
           //tr.addView(waybilltxt);
           //tr.addView(rnametxt);
           //tr.addView(cnametxt);
           //tr.addView(amounttxt);


       }
       else
       {
           new Task1().execute();
       }*/
   }

   @Override
   public View onCreateView(LayoutInflater inflater, ViewGroup container,
           Bundle savedInstanceState) {
       System.out.println("TA Fragment selected");
       rootView = inflater.inflate(R.layout.activity_delivery_transfer, container, false);
       resulttab=(TableLayout)rootView.findViewById(R.id.resulttable1);
       back=(Button)rootView.findViewById(R.id.btnbck);
     //  scan=(Button)rootView.findViewById(R.id.btnscan);

       Pb = (ProgressBar)rootView.findViewById(R.id.progressBar1);
       transfer=(Button)rootView.findViewById(R.id.buttontransfer);
       StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
       StrictMode.setThreadPolicy(policy);

       MYActivity = getActivity();

        /*_activity =new DeliveryActivity();
           _fragment2 = this;

           //KDC Reader
           //_kdcReader= new KDCReader(null, _fragment1, _fragment1, null, null, null, _fragment1, false);
            Thread t = new Thread(){
                   @Override
                    public void run(){
                       _kdcReader= new KDCReader(null, _fragment2, _fragment2, null, null, null, _fragment2, false);
                       _btDevice = _kdcReader.GetBluetoothDevice();
                   }
               };

              // _btDevice = _kdcReader.GetBluetoothDevice();
               //ConfigureSyncOptions();
               t.start();
               Log.w("KDCReader", "KDC Thread1 Started");
               System.out.println("KDC Thread2 Started");
               //Log.w("KDCReader Log", _activity.);
               System.out.println(_fragment2);*/


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



       //check if the scanned wbill is in local table
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


               /*Cursor cr = sqldb.rawQuery("SELECT ROUTENAME FROM routedata WHERE ROUTECODE='"+rname21[i]+"'", null);
               int crcount=cr.getCount();
               cr.moveToFirst();
                   String rnam=null;
               if(crcount>0){
                       rnam= cr.getString(cr.getColumnIndex("ROUTENAME"));
                   }
               cr.close();*/
                   //System.out.println("i="+i+waybill1[i]+" "+cname11[i]+" "+phone[i]+" "+area11[i]+" "+company1[i]+" "+civilid1[i]+" "+serial1[i]);

                       tr = new TableRow(getActivity());

                       if(Build.MODEL.contains("SM-N"))
                        {
                           // lp = new LayoutParams(420,LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
                      //      lp = new LayoutParams(560,LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
                      //      lp = new LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
                            //tr.setId(resulttab.getChildCount());
                           /* tr.setLayoutParams(lp);
                            lp.setMargins(0, 10, 40, 0);*/
                            lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
                            lp.setMargins(18, 2, 95, 2);
                            tr.setLayoutParams(lp);

                        }
                        else
                        {
                      /*  lp = new LayoutParams(150,LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);

                        tr.setLayoutParams(lp);
                        lp.setMargins(0, 20, 10, 0);*/

                            lp = new LayoutParams(200,LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);

                            tr.setLayoutParams(lp);
                            lp.setMargins(0, 20,40, 0);
                        }
                   TextView waybilltxt1 = new TextView(MYActivity);
                   waybilltxt1.setLayoutParams(lp);
                   waybilltxt1.setText(wbill21[i]);
                //  waybilltxt1.setText("1000001234456");

                   TextView rnametxt = new TextView(MYActivity);
                   //rnametxt.setLayoutParams(lp);
                   rnametxt.setPadding(0,20,70,0);
                   rnametxt.setText(rname21[i]);
               //    rnametxt.setText("lijo");

                   TextView cnametxt = new TextView(MYActivity);
                   // cnametxt.setLayoutParams(lp);
                   cnametxt.setPadding(0,20,70,0);
                   cnametxt.setText(cname21[i]);
               //   cnametxt.setText("joy");

                   TextView amounttxt = new TextView(MYActivity);
                   // amounttxt.setLayoutParams(lp);
                   amounttxt.setPadding(50,20,150,0);
                   amounttxt.setText(amount21[i]);
              //    amounttxt.setText("99.999");

                   tr.addView(waybilltxt1);
                   tr.addView(rnametxt);
                   tr.addView(cnametxt);
                   tr.addView(amounttxt);

                   resulttab.addView(tr, new TableLayout.LayoutParams (LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
                   //counttxt.setText(String.valueOf(resulttab.getChildCount()));

                   c21.moveToNext();

       }

           c21.close();
           db.close();



       }

       back.setOnClickListener(new OnClickListener() {

           @Override
           public void onClick(View v) {
               v.startAnimation(AnimationUtils.loadAnimation(getActivity().getBaseContext(), R.anim.image_click));
               DeliveryActivity.DonotInterruptKDCScan=false;
               // moveTaskToBack(true);
               getActivity().finish();




           }
       });


       transfer.setOnClickListener(new OnClickListener() {

           @SuppressWarnings("null")
           @Override
           public void onClick(View v) {
               v.startAnimation(AnimationUtils.loadAnimation(getActivity().getBaseContext(), R.anim.image_click));
               DeliveryActivity.DonotInterruptKDCScan=false;
               //response is in the format of runsheet or message
               //if response is runsheet save the runsheet in local database

               //runsheet_res=WebService.settransconfirm(drivercode,MasterActivity.METHOD_NAME24);
               runsheet_res= WebService.SET_TRANS_CONFIRM(drivercode);
               if(!errored)
               {
               if(!runsheet_res.contains("ALREADY SCANNED")&&!runsheet_res.contains("WAYBILL NOT READY")&&!runsheet_res.contains("NO"))
               {

                   //var1=Long.parseLong(runsheet_res);

               db=new DatabaseHandler(getActivity().getBaseContext());
               //open localdatabase in a read mode
               sqldb = db.getReadableDatabase();

               Cursor c = sqldb.rawQuery("SELECT Runsheetcode FROM logindata WHERE Username='"+drivercode+"'", null);
               c.moveToFirst();
               if(c.getCount()>0){
                   String runsheet1=c.getString(c.getColumnIndex("Runsheetcode"));
                   //Toast.makeText(getActivity().getApplicationContext(), runsheet_res+","+runsheet1, Toast.LENGTH_LONG).show();



                   if(runsheet1==null)
                   {
                       sqldb = db.getWritableDatabase();
                       // made changes added ' on 12aug17
                       sqldb.execSQL("UPDATE logindata SET Runsheetcode='"+runsheet_res+"'WHERE Username='"+drivercode+"'" );
                      // sqldb.execSQL("UPDATE logindata SET Runsheetcode="+runsheet_res+"WHERE Username='"+drivercode+"'" );
                       Toast.makeText(getActivity().getApplicationContext(), "Transfer confirmed", Toast.LENGTH_LONG).show();

                   }
                   else{
                       // if(runsheet1.equals(var1.toString()))
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
                //   sqldb = db.getReadableDatabase();

                   sqldb = db.getWritableDatabase();
               sqldb.execSQL("DELETE FROM deliverydata WHERE Drivercode <> '"+drivercode+"'");

               sqldb.execSQL("DELETE FROM TransferAcceptTemp WHERE Drivercode ='"+drivercode+"'");
                   sqldb = db.getReadableDatabase();

               Cursor c1 = sqldb.rawQuery("SELECT * FROM deliverydata WHERE Waybill='"+waybill1+"'", null);
               int count1=c1.getCount();

               if(count1>0){

                   //System.out.println(c1.getString(c1.getColumnIndex("Waybill")));
               //	System.out.println("update");
                   //c1.moveToNext();

                   sqldb =db.getWritableDatabase();

                   sqldb.execSQL("UPDATE deliverydata SET Drivercode='"+drivercode+"',Routecode='"+route
                           +"',Consignee='"+cname1+"',Telephone='"+phone+"',"+"Area='"+area1+"',"+"Company='"
                           +company1+"',CivilID='"+civilid1+"',Serial='"+serial1+"',CardType='"+cardtype1+"',DeliveryDate='"+deldate1+"',DeliveryTime='"
                           +deltime1+"',Amount='"+amount1+"',StopDelivery=0,WC_Status='A',WC_Transfer_Status=0,TransferStatus=0,Attempt_Status=0,Address='"+address1+"' WHERE Waybill='"+waybill1+"'");


                   /*Log.e("Drivercode", drivercode);
                   Log.e("Routecode",route);
                   Log.e("Waybill", waybill1);
                   Log.e("Consignee", cname1);
                   Log.e("Telephone",phone);
                   Log.e("Area", area1);
                   Log.e("Company",company1);
                   Log.e("CivilID",civilid1);
                   Log.e("Serial", serial1);
                   Log.e("CardType", cardtype1);
                   Log.e("DeliveryDate",deldate1);
                   Log.e("DeliveryTime", deltime1);
                   Log.e("Amount",amount1);*/



               }
               else{
                   c1.moveToLast();

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

                   /*Log.e("Drivercode", drivercode);
                   Log.e("Routecode",route);
                   Log.e("Waybill", waybill1);
                   Log.e("Consignee", cname1);
                   Log.e("Telephone",phone);
                   Log.e("Area", area1);
                   Log.e("Company",company1);
                   Log.e("CivilID",civilid1);
                   Log.e("Serial", serial1);
                   Log.e("CardType", cardtype1);
                   Log.e("DeliveryDate",deldate1);
                   Log.e("DeliveryTime", deltime1);
                   Log.e("Amount",amount1);*/
               }


               c1.close();

               db.close();
               }
               else{
                   //Toast.makeText(getActivity().getApplicationContext(), runsheet_res, Toast.LENGTH_LONG).show();

                   MYActivity.runOnUiThread(new Runnable(){
                       @Override
                       public void run(){

                           Toast.makeText(MYActivity,runsheet_res,
                                     Toast.LENGTH_LONG).show();
                       }
                   });
               }}
               else{
                   //Toast.makeText(getActivity().getApplicationContext(), "Connection Error", Toast.LENGTH_LONG).show();
                   MYActivity.runOnUiThread(new Runnable(){
                           @Override
                           public void run(){

                               Toast.makeText(MYActivity,"Connection Error",
                                         Toast.LENGTH_LONG).show();
                           }
                       });

               }
           }
       });



       DeliveryActivity.TArootView= rootView;
       System.out.println("TA View Instance ");
       System.out.println(DeliveryActivity.TArootView);

       DeliveryActivity.TAActivity = getActivity();
       System.out.println("TA Context Instance ");
       System.out.println(DeliveryActivity.TAActivity);
       return rootView;

       //return1 rootView;
   }

   //Return scan result
   @Override
      public void onActivityResult(int requestCode, int resultCode, Intent intent) {

          if (requestCode == SCANNER_REQUEST_CODE) {
              // Handle scan intent

              if (resultCode == Activity.RESULT_OK) {
                  // Handle successful scan
                  String contents = intent.getStringExtra("SCAN_RESULT");

                  waybill=contents;
                  new Task1().execute();



              } else if (resultCode == Activity.RESULT_CANCELED) {
                  // Handle cancel
              }
          } else {
              // Handle other intents
          }

      }

   @Override
   public void onResume() {
       super.onResume();
       MYActivity = DeliveryActivity.TAActivity;
       rootView = DeliveryActivity.TArootView;
       resulttab = (TableLayout) rootView.findViewById(R.id.resulttable1);
       //tr = new TableRow(MYActivity);

       DeliveryActivity.KDCScannerCallFrom = "TAFragment";
       /*MasterActivity.KDCScannerCallFrom = "TAFragment";
       System.out.println("TAFragment Block");
       System.out.println(_fragment2);

       System.out.println("KDCReader Value before calling"+_kdcReader); *//*

       *//*if(_kdcReader == null)
       {
           _kdcReader= new KDCReader(null, _fragment2, _fragment2, null, null, null, _fragment2, false);
           _btDevice = _kdcReader.GetBluetoothDevice();
       }


       System.out.println("KDCReader Value After calling"+_kdcReader);

       if(_kdcReader != null)
       {
           _kdcReader.Connect(_btDevice);
           System.out.println("TA Device Connected");
       }

       if(_kdcReader != null)
       {
           _kdcReader.Listen();
           System.out.println("TA Device Listen");
       }

       if(Pb == null)
           Pb = (ProgressBar)rootView.findViewById(R.id.progressBar1);*//*


       getView().setFocusableInTouchMode(true);
       getView().requestFocus();
       getView().setOnKeyListener(new View.OnKeyListener() {
           @Override
           public boolean onKey(View v, int keyCode, KeyEvent event) {

                int action = event.getAction();
                keyCode = event.getKeyCode();
                    switch (keyCode) {
                    case KeyEvent.KEYCODE_VOLUME_UP:
                        if (action == KeyEvent.ACTION_DOWN) {
                            Intent intent = new Intent("com.google.zxing.client.android.SCAN");
                            intent.putExtra("SCAN_MODE", "SCAN_MODE");
                            startActivityForResult(intent, SCANNER_REQUEST_CODE);
                        }
                        return true;
                    case KeyEvent.KEYCODE_VOLUME_DOWN:
                        if (action == KeyEvent.ACTION_DOWN) {
                            Intent intent = new Intent("com.google.zxing.client.android.SCAN");
                            intent.putExtra("SCAN_MODE", "SCAN_MODE");
                            startActivityForResult(intent, SCANNER_REQUEST_CODE);
                        }
                        return true;
                    default:
                        //return1 super.dispatchKeyEvent(event);
                    }
               return false;
           }
       });

      }*/

   }
   @Override
   public void onStop() {
       super.onStop();
/*        _kdcReader.Disconnect();
       System.out.println("KDC Reader Disconnect From TA");*/
   }
    //async task for getting hold waybill
    public class Task1 extends AsyncTask<Void, Void, String>
       {
        String response1 = "";
           public void onPreExecute()
             {
                 MYActivity.runOnUiThread(new Runnable() {
                     @Override
                     public void run() {

                         Pb.setVisibility(View.VISIBLE);
                     }
                 });

               // super.onPreExecute();

               tr = new TableRow(MYActivity);
               if(Build.MODEL.contains("SM-N"))
                   {
                      //  lp = new LayoutParams(420,LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
                   //    lp = new LayoutParams(560,LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
                   //    lp = new LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
                       //tr.setId(resulttab.getChildCount());
                     /*  tr.setLayoutParams(lp);
                       lp.setMargins(0, 10, 40, 0);*/
                       lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
                       lp.setMargins(18, 2, 95, 2);
                       tr.setLayoutParams(lp);

                   }
                   else
                   {
                   /*lp = new LayoutParams(150,LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);

                   tr.setLayoutParams(lp);
                   lp.setMargins(0, 20, 10, 0);*/

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
                   // TODO Auto-generated method stub

               //    response = null;
                   chktranswbllResponse = null;
                   System.out.println("Waybill from : " + waybill);
                   System.out.println("Driver Code : " + drivercode);

                   //Check point for null values
                   if (drivercode == null || drivercode.equals("") || waybill == null || waybill.equals(""))
                   {
                       MYActivity.runOnUiThread(new Runnable(){
                             @Override
                             public void run(){

                                 Toast.makeText(MYActivity,"Try again! Required Values Blank",
                                           Toast.LENGTH_LONG).show();
                             }
                         });
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
                    //   error1 = chktranswbllResponse.ErrMsg;
                       attempt1 = chktranswbllResponse.Attempt;
                       address1 = chktranswbllResponse.Address;

                       final TextView waybilltxt = new TextView(MYActivity);
                       waybilltxt.setLayoutParams(lp);
                       waybilltxt.setText(waybill1);
                       //waybilltxt.setText("100000123456");
                       //	System.out.println("sssss2");
                       final TextView rnametxt = new TextView(MYActivity);
                       //rnametxt.setLayoutParams(lp);
                      rnametxt.setPadding(0,20,70,0);
                       rnametxt.setText(rname1);
                       //          rnametxt.setText("lijo");

                       final TextView cnametxt = new TextView(MYActivity);
                     //  cnametxt.setLayoutParams(lp);
                      cnametxt.setPadding(0,20,70,0);
                       cnametxt.setText(cname1);
                       //         cnametxt.setText("joy");

                       final TextView amounttxt = new TextView(MYActivity);
                      // amounttxt.setLayoutParams(lp);
                       amounttxt.setPadding(50,20,150,0);
                       amounttxt.setText(amount1);
                       //    amounttxt.setText("99.999");

                       tr.addView(waybilltxt);
                       tr.addView(rnametxt);
                       tr.addView(cnametxt);
                       tr.addView(amounttxt);
                   }
                    error1 = chktranswbllResponse.ErrMsg;
                   /*SoapObject Request = new SoapObject(MasterActivity.NAMESPACE, MasterActivity.METHOD_NAME2);
                   Request.addProperty("DRIVERCODE",drivercode);
                   Request.addProperty("WAYBILL",waybill);



                   *//*Create waybill class *//*
                   Waybill_check wb = new Waybill_check();


                   *//* Set the route to be the argument of the web service method *//*
                   PropertyInfo pi = new PropertyInfo();
                   pi.setName(MasterActivity.METHOD_NAME2);
                   pi.setValue(wb);
                   pi.setType(wb.getClass());
                   Request.addProperty(pi);

                   *//* Set the web service envelope*//*
                   SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                   envelope.dotNet = true;
                   envelope.setOutputSoapObject(Request);
                   //envelope for setting the data
                   envelope.addMapping(MasterActivity.NAMESPACE, MasterActivity.METHOD_NAME2,new Waybill_check().getClass());
                   HttpTransportSE androidHttpTransport = new HttpTransportSE(MasterActivity.URL);
                   //	androidHttpTransport.debug = true;

                   *//* Call the web service and retrieve result*//*

                   try {
                       // Invoke web service
                       ServiceConnection WebCon = androidHttpTransport.getServiceConnection();
                       WebCon.disconnect();
                       androidHttpTransport.call(MasterActivity.SOAP_ACTION+MasterActivity.METHOD_NAME2, envelope);

                       response = (SoapObject)envelope.getResponse();
                   Log.i("myApp", Request.toString());
                       //System.out.println("check dddd" + response);
                       //envelope for passing the data
                       envelope.addMapping(MasterActivity.NAMESPACE, MasterActivity.METHOD_NAME2,new Waybill_check().getClass());
                       androidHttpTransport.call(MasterActivity.SOAP_ACTION+MasterActivity.METHOD_NAME2, envelope);
                   //	System.out.println("sssss");
                       //if error message is there print only the error message else print all data

                       if(response.toString().contains("ConsignName"))
                       {
                           //System.out.println("sssss1");
                           waybill1=response.getProperty(0).toString();

                           rname1=response.getProperty(1).toString();
                           cname1 =response.getProperty(2).toString();
                           phone =response.getProperty(3).toString();
                           area1=response.getProperty(4).toString();
                           company1=response.getProperty(5).toString();
                           civilid1=response.getProperty(6).toString();
                           serial1=response.getProperty(7).toString();
                           cardtype1=response.getProperty(8).toString();
                           deldate1=response.getProperty(9).toString();
                           deltime1=response.getProperty(10).toString();
                           amount1=response.getProperty(11).toString();
                           error1=  response.getProperty(12).toString();
                           attempt1 =  response.getProperty(13).toString();
                           address1=  response.getProperty(14).toString();

                           final TextView waybilltxt = new TextView(getActivity());
                           waybilltxt.setLayoutParams(lp);
                           waybilltxt.setText(waybill1);
                     //      waybilltxt.setText("100000123456");
                       //	System.out.println("sssss2");
                           final TextView rnametxt = new TextView(getActivity());
                           rnametxt.setLayoutParams(lp);
                           rnametxt.setText(rname1);
                 //          rnametxt.setText("lijo");

                           final TextView cnametxt = new TextView(getActivity());
                           cnametxt.setLayoutParams(lp);
                           cnametxt.setText(cname1);
                  //         cnametxt.setText("joy");

                           final TextView amounttxt = new TextView(getActivity());
                           amounttxt.setLayoutParams(lp);
                          amounttxt.setText(amount1);
                       //    amounttxt.setText("99.999");

                           tr.addView(waybilltxt);
                           tr.addView(rnametxt);
                           tr.addView(cnametxt);
                           tr.addView(amounttxt);
                           //System.out.println("sssss3");

                           //display the response in table

                           //clear text msg
                           //	statusmsg.setText(null);
                           //save the response in database

                       }
                       else
                       {
                           //System.out.println("sssss4");
                           error1=  response.getProperty(0).toString();
                           //statusmsg.setText(error1);
                       }
                       androidHttpTransport.reset();
                       //androidHttpTransport.getConnection().disconnect();
                       //System.out.println("sssss5");
                        WebCon.disconnect();*/
                   }
                   catch(Exception e)
                   {
                       //Toast.makeText(getActivity().getApplicationContext(),"Connection Error",
                           //	Toast.LENGTH_LONG).show();
                       e.printStackTrace();
                       Log.e("Delivery TA:", e.getMessage().toString());

                   }


               }
           @Override
           public void onPostExecute(String res)
           {
               if (chktranswbllResponse == null)
            //   if (response == null)
               {
                   Pb.setVisibility(View.INVISIBLE);
                   Toast.makeText(MYActivity.getApplicationContext(),"Please Try again!",
                           Toast.LENGTH_LONG).show();
                   return;
               }

               //System.out.println("stagepos");
           //    if(response.toString().contains("ConsignName"))
                   if(chktranswbllResponse.ErrMsg==null||chktranswbllResponse.ErrMsg=="")
               {

                   resulttab.addView(tr, new TableLayout.LayoutParams (LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
               //	System.out.print("stage1");
                   //counttxt.setText(String.valueOf(resulttab.getChildCount()));
                   db=new DatabaseHandler(MYActivity.getBaseContext());
                   //open localdatabase in a read mode

                   sqldb = db.getWritableDatabase();
                   sqldb.execSQL("DELETE FROM TransferAcceptTemp WHERE Drivercode <> '"+drivercode+"'");
               //	System.out.print("stage2");
                   //select all values in the table and check count
                   sqldb = db.getReadableDatabase();
                   Cursor c1 = sqldb.rawQuery("SELECT * FROM TransferAcceptTemp WHERE Waybill='"+waybill1+"'", null);
                   int count1=c1.getCount();
                   //System.out.print("stage3");
                   if(count1>0){
                   //	System.out.print("stage4");
                       //System.out.println(c1.getString(c1.getColumnIndex("Waybill")));
                   //	System.out.println("update");
                       //c1.moveToNext();

                       sqldb =db.getWritableDatabase();

                       sqldb.execSQL("UPDATE TransferAcceptTemp SET Drivercode='"+drivercode+"',Routecode='"+rname1
                               +"',Consignee='"+cname1+"',Telephone='"+phone+"',"+"Area='"+area1+"',"+"Company='"
                               +company1+"',CivilID='"+civilid1+"',Serial='"+serial1+"',CardType='"+cardtype1+"',DeliveryDate='"+deldate1+"',DeliveryTime='"
                               +deltime1+"',Amount='"+amount1+"',Transfer_Status='P',Address='"+address1+"',Attempt_Status='"+attempt1+"' WHERE Waybill='"+waybill1+"'");


                   /*	Log.e("Drivercode", drivercode);
                       Log.e("Routecode",route);
                       Log.e("Waybill", waybill1);
                       Log.e("Consignee", cname1);
                       Log.e("Telephone",phone);
                       Log.e("Area", area1);
                       Log.e("Company",company1);
                       Log.e("CivilID",civilid1);
                       Log.e("Serial", serial1);
                       Log.e("CardType", cardtype1);
                       Log.e("DeliveryDate",deldate1);
                       Log.e("DeliveryTime", deltime1);
                       Log.e("Amount",amount1);*/



                   }
                   else{
                   //	System.out.print("stage5");
                       c1.moveToLast();
                   //	System.out.print("stage6");
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
                       //System.out.print("stage7");
                       sqldb.insertOrThrow("TransferAcceptTemp", null, values);

                   /*	Log.e("Drivercode", drivercode);
                       Log.e("Routecode",route);
                       Log.e("Waybill", waybill1);
                       Log.e("Consignee", cname1);
                       Log.e("Telephone",phone);
                       Log.e("Area", area1);
                       Log.e("Company",company1);
                       Log.e("CivilID",civilid1);
                       Log.e("Serial", serial1);
                       Log.e("CardType", cardtype1);
                       Log.e("DeliveryDate",deldate1);
                       Log.e("DeliveryTime", deltime1);
                       Log.e("Amount",amount1);*/
                   }
                   c1.close();

                   db.close();
               }
               else
//                   if(!error1.contains("anyType"))
                       Toast.makeText(MYActivity.getApplicationContext(),error1,
                               Toast.LENGTH_LONG).show();

             //  Pb.setVisibility(View.INVISIBLE);
               MYActivity.runOnUiThread(new Runnable() {
                   @Override
                   public void run() {

                       Pb.setVisibility(View.INVISIBLE);
                   }
               });


           }

}

   /*// KDC Connection Changed
     @Override
     public void ConnectionChanged(BluetoothDevice device,int state){
         //ToDo Auto-generated method stub

         Log.i("KDCReader", "KDC TA Connection Changed Block");
         System.out.print("KDCReader TA Connection Changed Block");
         switch(state){

         case KDCConstants.CONNECTION_STATE_CONNECTED:
             _activity.runOnUiThread(new Runnable(){
                 @Override
                 public void run(){

                 Toast.makeText(getActivity(), "Scanner Connected", Toast.LENGTH_LONG).show();
                 }
                 });
             break;

         case KDCConstants.CONNECTION_STATE_LOST:
             _activity.runOnUiThread(new Runnable(){
                 @Override
                 public void run(){

                     Toast.makeText(getActivity(), "Scanner Connection Lost", Toast.LENGTH_LONG).show();
                 }
             });
             break;
         }
     }
     // KDC DataReceived

     @Override
     public void Komtac
     {

     }
     @Override
     public void DataReceived(KDCData pData){

         //
    }

    // Barcode DataReceived
     @Override
     public void BarcodeDataReceived(KDCData pData){
//		  if(tta.isAlive())
//		  {
         Log.i("KDCReader", "KDC TA BarCodeReceived Block");
         System.out.print("KDCReader TA BarCodeReceived Block");
       //  }
         System.out.print("Pdata value"+pData);
         System.out.print("Kdata value");
         if(pData != null){

             ScannerData = pData;

             if(Check_ValidWaybill(pData.GetData())==true)
             {
                     //test calling
                    //Toast.makeText(_activity, "Valid Waybill", Toast.LENGTH_LONG).show();
                 getActivity().runOnUiThread(new Runnable(){
                     @Override
                     public void run(){
                         System.out.print("Scanner TA before Calling");
                         ScannerTAExecutions();
                         System.out.print("Scanner TA after Calling");
                     }
                 });


             }
             else
             {

                 _activity.runOnUiThread(new Runnable(){
                     @Override
                     public void run(){

                         Toast.makeText(getActivity(), "Invalid Waybill", Toast.LENGTH_LONG).show();
                     }
                 });
             }
         }
         else
         {
             _activity.runOnUiThread(new Runnable(){
                 @Override
                 public void run(){

                     Toast.makeText(getActivity(), "Invalid Waybill", Toast.LENGTH_LONG).show();
                 }
             });
         }

     }

     public static boolean Check_ValidWaybill (String s){

           if (s.length() == 10 || s.length() == 12)
           {
               if (StringUtils.isNumeric(s) == true)
                       return1 true;
               else
                       return1 false;
           }
           else if (s.length() == 18)
           {
               if (StringUtils.isAlphanumeric(s) == true)
                   return1 true;
               else
                   return1 false;
           }
           return1 false;*/
//	 private void Sleep(int milliseconds) {
//			try {
//				Thread.sleep(milliseconds);
//			} catch (Exception e) {
//
//			}
//	    }


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