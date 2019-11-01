package com.postaplus.postascannerapp;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import DatabaseHandlers.DBFCS.TBLogin;
import DatabaseHandlers.DBFunctions;

/**
 * Created by ahlaam.kazi on 9/27/2016.
 */

public class Summary_ack_fragment extends Fragment {
    TextView txt18, txtcount1, txtcount2, txt20, txtcount3;
    View rootView;
    DatabaseHandler db;
    SQLiteDatabase sqldb;
    TableLayout resulttab;
    TableRow tr;
    TableRow.LayoutParams lp;
    TextView waybltxt, cnsgtxt, statustxt;
    String wbill1 = "", consignee1 = "", status1 = "";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        System.out.println(" saved instance value is ");
        System.out.println(savedInstanceState);
        rootView = inflater.inflate(R.layout.activity_summary_ack, container, false);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        txtcount1 = (TextView) rootView.findViewById(R.id.textcount1);
        txtcount2 = (TextView) rootView.findViewById(R.id.textcount2);
        txtcount3 = (TextView) rootView.findViewById(R.id.textcount3);
        txt18 = (TextView) rootView.findViewById(R.id.textView18);
        txt20 = (TextView) rootView.findViewById(R.id.textView20);
        resulttab = (TableLayout) rootView.findViewById(R.id.resulttabl2);


        TBLogin ActiveLogin = DBFunctions.GetLoggedUser(getActivity().getBaseContext());

        db = new DatabaseHandler(getActivity().getBaseContext());
        //open localdatabase in a read mode
        sqldb = db.getReadableDatabase();

        // To Show the Total of waybills
        Cursor c1 = sqldb.rawQuery("SELECT * FROM deliverydata WHERE awbidentifier  = 'ACK'", null);
        int AwbTTL = c1.getCount();
        c1.close();
        txtcount1.setText(String.valueOf(AwbTTL));


        // To Show the Pending wabills
        c1 = sqldb.rawQuery("SELECT * FROM deliverydata WHERE awbidentifier  = 'ACK' AND (StopDelivery = 0 OR (WC_Status = 'SD' AND StopDelivery = 1)) ", null);
        //AND WC_Status='A'
        int AWBPND = c1.getCount();
        txtcount2.setText(String.valueOf(AWBPND));

        if (AWBPND > 0) {
            c1.moveToFirst();
            for (int i = 0; i < AWBPND; i++) {
                wbill1 = c1.getString(c1.getColumnIndex("Waybill"));
                consignee1 = c1.getString(c1.getColumnIndex("Consignee"));
                //status1= c1.getString(c1.getColumnIndex("Status"));

                Cursor c2 = sqldb.rawQuery("SELECT * FROM wbilldata WHERE waybill = '" + wbill1 + "' ORDER BY id DESC LIMIT 1 ", null);
                if (c2.getCount() > 0) {
                    c2.moveToFirst();
                    status1 = c2.getString(c2.getColumnIndex("Eventcode"));
                } else {
                    status1 = c1.getString(c1.getColumnIndex("WC_Status"));
                    if (status1.equals("A")) status1 = "ACK";
                    else if (status1.equals("C")) status1 = "DELIVERED";
                    else status1 = c1.getString(c1.getColumnIndex("Last_Status"));
                }
                c2.close();

                tr = new TableRow(getActivity());
                if (Build.MODEL.contains("SM-N")) {

                    lp = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT);
                    lp.setMargins(15, 2, 95, 2);
                    tr.setLayoutParams(lp);
                   // lp.setMargins(0, 10, 40, 0);

                }else{
                    lp = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT);
                    lp.setMargins(0,20,115,0);
                    tr.setLayoutParams(lp);
                }

                waybltxt = new TextView(getActivity());
                waybltxt.setLayoutParams(lp);
                waybltxt.setText(wbill1);

                cnsgtxt = new TextView(getActivity());
                cnsgtxt.setLayoutParams(lp);
                cnsgtxt.setText(consignee1);

                statustxt = new TextView(getActivity());
                statustxt.setLayoutParams(lp);
                statustxt.setText(status1);


                tr.addView(waybltxt);
                tr.addView(cnsgtxt);
                tr.addView(statustxt);
                resulttab.addView(tr, new TableLayout.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));

                c1.moveToNext();
            }
        }
        c1.close();


        // To show the completed waybills
        c1 = sqldb.rawQuery("SELECT * FROM deliverydata WHERE awbidentifier  = 'ACK' AND StopDelivery = 1 AND WC_Status<>'SD'  ", null);
        int AWBDELIVERED = c1.getCount();
        txtcount3.setText(String.valueOf(AWBDELIVERED));


        if (AWBDELIVERED > 0) {
            c1.moveToFirst();
            for (int i = 0; i < AWBDELIVERED; i++) {
                wbill1 = c1.getString(c1.getColumnIndex("Waybill"));
                consignee1 = c1.getString(c1.getColumnIndex("Consignee"));
                //   status1= c1.getString(c1.getColumnIndex("Status"));

                Cursor c2 = sqldb.rawQuery("SELECT * FROM wbilldata WHERE waybill = '" + wbill1 + "' ORDER BY id DESC LIMIT 1 ", null);
                if (c2.getCount() > 0) {
                    c2.moveToFirst();
                    status1 = c2.getString(c2.getColumnIndex("Eventcode"));
                } else {
                    status1 = c1.getString(c1.getColumnIndex("WC_Status"));
                    if (status1.equals("C")) status1 = "DELIVERED";
                    else if (status1.equals("A")) status1 = "ACK";
                    else status1 = c1.getString(c1.getColumnIndex("Last_Status"));
                }
                c2.close();

                tr = new TableRow(getActivity());
                if (Build.MODEL.contains("SM-N")) {

                    lp = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT);
                    lp.setMargins(15, 2, 95, 2);
                    tr.setLayoutParams(lp);
                   // lp.setMargins(0, 10, 40, 0);

                }else{
                    lp = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT);
                    lp.setMargins(0,20,115,0);
                    tr.setLayoutParams(lp);
                }

                waybltxt = new TextView(getActivity());
                waybltxt.setLayoutParams(lp);
                waybltxt.setText(wbill1);

                cnsgtxt = new TextView(getActivity());
                cnsgtxt.setLayoutParams(lp);
                cnsgtxt.setText(consignee1);

                statustxt = new TextView(getActivity());
                statustxt.setLayoutParams(lp);
                statustxt.setText(status1);


                tr.addView(waybltxt);
                tr.addView(cnsgtxt);
                tr.addView(statustxt);
                resulttab.addView(tr, new TableLayout.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));

                c1.moveToNext();
            }
        }
        c1.close();


        db.close();
        return rootView;
    }
}