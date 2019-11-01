package com.postaplus.postascannerapp;

/**
 * Created by ahlaam.kazi on 9/27/2016.
 */
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

public class Summary_pickup_fragment extends Fragment {

    TextView txt18, txtcount1, txtcount2, txt20, txtcount3;
    View rootView;
    DatabaseHandler db;
    SQLiteDatabase sqldb;
    TableLayout resulttab;
    TableRow tr;
    TableRow.LayoutParams lp;
    TextView pickptxt, acctxt, statustxt;
    String pckp1 = "", acc1 = "", status1 = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        System.out.println(" saved instance value is ");
        System.out.println(savedInstanceState);
        rootView = inflater.inflate(R.layout.activity_summary_pickup, container, false);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        txtcount1 = (TextView) rootView.findViewById(R.id.textcount1);
        txtcount2 = (TextView) rootView.findViewById(R.id.textcount2);
        txtcount3 = (TextView) rootView.findViewById(R.id.textcount3);
        txt18 = (TextView) rootView.findViewById(R.id.textView18);
        txt20 = (TextView) rootView.findViewById(R.id.textView20);
        resulttab = (TableLayout) rootView.findViewById(R.id.resulttabl3);


        TBLogin ActiveLogin = DBFunctions.GetLoggedUser(getActivity().getBaseContext());

        db = new DatabaseHandler(getActivity().getBaseContext());
        //open localdatabase in a read mode
        sqldb = db.getReadableDatabase();

        // To Show the Total of pickups
        Cursor c1 = sqldb.rawQuery("SELECT * FROM pickuphead WHERE Status<>'P' ", null);
        int PCKPTTL = c1.getCount();
        c1.close();
        txtcount1.setText(String.valueOf(PCKPTTL));

        /*if (PCKPTTL > 0) {
            c1.moveToFirst();
            for (int i = 0; i < PCKPTTL; i++) {
                pckp1 = c1.getString(c1.getColumnIndex("Pickup_No"));
                acc1 = c1.getString(c1.getColumnIndex("Account_Name"));
                status1 = c1.getString(c1.getColumnIndex("Status"));

                tr = new TableRow(getActivity());
                if (Build.MODEL.contains("SM-N")) {

                    lp = new TableRow.LayoutParams(411, TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT);

                    tr.setLayoutParams(lp);
                    lp.setMargins(0, 10, 75, 0);

                }

                pickptxt = new TextView(getActivity());
                pickptxt.setLayoutParams(lp);
                pickptxt.setText(pckp1);


                acctxt = new TextView(getActivity());
                acctxt.setLayoutParams(lp);
                acctxt.setText(acc1);


                statustxt = new TextView(getActivity());
                statustxt.setLayoutParams(lp);
                statustxt.setText(status1);


                tr.addView(pickptxt);
                tr.addView(acctxt);
                tr.addView(statustxt);

                resulttab.addView(tr, new TableLayout.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));

                c1.moveToNext();
            }
        }*/

        // To Show the Pending Pickups
        c1 = sqldb.rawQuery("SELECT * FROM pickuphead WHERE Status = 'A'", null);
        int PCKPPND = c1.getCount();
        txtcount2.setText(String.valueOf(PCKPPND));

        if (PCKPPND > 0) {
            c1.moveToFirst();
            for (int i = 0; i < PCKPPND; i++) {
                pckp1 = c1.getString(c1.getColumnIndex("Pickup_No"));
                acc1 = c1.getString(c1.getColumnIndex("Account_Name"));
                status1 = c1.getString(c1.getColumnIndex("Status"));

                if (status1.equals("A")) status1 = "PENDING";
                else if (status1.equals("C")) status1 = "FINISHED";
                else status1 = "UNKNOWN";

                tr = new TableRow(getActivity());
                if (Build.MODEL.contains("SM-N")) {

                    lp = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT);
                    lp.setMargins(15, 2, 95, 2);
                    tr.setLayoutParams(lp);
                   // lp.setMargins(0, 10, 95, 0);

                }else{

                    lp = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT);
                    lp.setMargins(0,20,87,0);
                    tr.setLayoutParams(lp);
                    // lp.setMargins(0, 10, 95, 0);
                }

                pickptxt = new TextView(getActivity());
                pickptxt.setLayoutParams(lp);
                pickptxt.setText(pckp1);


                acctxt = new TextView(getActivity());
                acctxt.setLayoutParams(lp);
                acctxt.setText(acc1);


                statustxt = new TextView(getActivity());
                statustxt.setLayoutParams(lp);
                statustxt.setText(status1);


                tr.addView(pickptxt);
                tr.addView(acctxt);
                tr.addView(statustxt);

                resulttab.addView(tr, new TableLayout.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));

                c1.moveToNext();


            }
        }
        c1.close();

        // To Show the Finished Pickups
        c1 = sqldb.rawQuery("SELECT * FROM pickuphead WHERE Status = 'C'", null);
        int PCKFNSHD = c1.getCount();
        txtcount3.setText(String.valueOf(PCKFNSHD));

        if (PCKFNSHD > 0) {
            c1.moveToFirst();
            for (int i = 0; i < PCKFNSHD; i++) {
                pckp1 = c1.getString(c1.getColumnIndex("Pickup_No"));
                acc1 = c1.getString(c1.getColumnIndex("Account_Name"));
                status1= c1.getString(c1.getColumnIndex("Status"));

                if (status1.equals("C")) status1 = "FINISHED";
                else if (status1.equals("A")) status1 = "PENDING";
                else status1 = "UNKNOWN";


                tr = new TableRow(getActivity());
                if (Build.MODEL.contains("SM-N")) {

                    lp = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT);
                    lp.setMargins(15, 2, 95, 2);
                    tr.setLayoutParams(lp);
                   // lp.setMargins(0, 10, 95, 0);

                }else{

                    lp = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT);
                    lp.setMargins(0,20,87,0);
                    tr.setLayoutParams(lp);
                    // lp.setMargins(0, 10, 95, 0);
                }

                pickptxt = new TextView(getActivity());
                pickptxt.setLayoutParams(lp);
                pickptxt.setText(pckp1);


                acctxt = new TextView(getActivity());
                acctxt.setLayoutParams(lp);
                acctxt.setText(acc1);


                statustxt = new TextView(getActivity());
                statustxt.setLayoutParams(lp);
                statustxt.setText(status1);


                tr.addView(pickptxt);
                tr.addView(acctxt);
                tr.addView(statustxt);

                resulttab.addView(tr, new TableLayout.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));

                c1.moveToNext();


            }
            c1.close();
        }

        db.close();
        return rootView;
}}