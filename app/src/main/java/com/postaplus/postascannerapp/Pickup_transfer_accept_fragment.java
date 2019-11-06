package com.postaplus.postascannerapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TableRow.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;


public class Pickup_transfer_accept_fragment extends Fragment {
    static boolean errored = false;
    public int SCANNER_REQUEST_CODE = 123;
    public String chkdata = "";
    public FragmentActivity MYActivity;
    Button back, scan, transfer;
    ProgressBar Pb;
    SharedPreferences pref;
    Long var1;
    TableLayout resulttab;
    TableRow tr;
    LayoutParams lp;
    DatabaseHandler db;
    SQLiteDatabase sqldb;
    String waybill;
    int count;
    int flag = 0;
    View rootView;
    private String drivercode, route, routen, pick_res;
    private String msg = "";
    private TextView picnotxt;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_pickup_accept_frag, container, false);
        resulttab = rootView.findViewById(R.id.tlTable);
        back = rootView.findViewById(R.id.btnbck);
        Pb = rootView.findViewById(R.id.progressBar1);
        transfer = rootView.findViewById(R.id.buttonaccept);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        if (this.getActivity() != null && getActivity().getIntent().getExtras() != null) {
            pref = this.getActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
            route = getActivity().getIntent().getExtras().getString("routecode");
            routen = getActivity().getIntent().getExtras().getString("routename");
        }
        if (getActivity() != null && getActivity().getBaseContext() != null)
            db = new DatabaseHandler(getActivity().getBaseContext());
        if (db != null)
            sqldb = db.getReadableDatabase();
        Cursor c1 = sqldb.rawQuery("SELECT Username FROM logindata WHERE Loginstatus=1", null);
        c1.moveToFirst();
        if (c1.getCount() > 0) {
            drivercode = c1.getString(c1.getColumnIndex("Username"));
        }
        c1.close();
        db.close();

        back.setOnClickListener(v -> {
            if (getActivity() != null) {
                v.startAnimation(AnimationUtils.loadAnimation(getActivity().getBaseContext(), R.anim.image_click));
                getActivity().finish();
            }
        });
        transfer.setOnClickListener(v -> {
            if (getActivity() != null)
                v.startAnimation(AnimationUtils.loadAnimation(getActivity().getBaseContext(), R.anim.image_click));
            String[] picknoarr = new String[resulttab.getChildCount()];
            int flag = 0;
            for (int i = 0; i < resulttab.getChildCount(); i++) {
                picnotxt = (TextView) ((TableRow) resulttab.getChildAt(i)).getChildAt(0);
                picknoarr[i] = picnotxt.getText().toString();
                pick_res = webservice.WebService.SET_TRANS_PICKUP_CONFIRM(drivercode, picknoarr[i]);
                flag = 0;
                if (!errored) {
                    if (pick_res.contains("ACCEPTED")) {
                        flag = 1;
                    }
                } else {
                    Toast.makeText(getActivity(), "Connection Error", Toast.LENGTH_LONG).show();
                }
            }
            if (flag == 1) {
                Toast.makeText(getActivity(), pick_res, Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getActivity(), pick_res, Toast.LENGTH_LONG).show();
            }
        });
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        PickupActivity.KDCScannerCallFrom = "PTAFragment";
        MYActivity = PickupActivity.PTAActivity;

        if (getView() != null) {
            getView().setFocusableInTouchMode(true);
            getView().requestFocus();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == SCANNER_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                waybill = intent.getStringExtra("SCAN_RESULT");
                new Task1().execute();
            }
        }
    }


    public class Task1 extends AsyncTask<Void, Void, String> {
        public void onPreExecute() {
            Pb.setVisibility(View.VISIBLE);
            tr = new TableRow(getActivity());
            if (Build.MODEL.contains("SM-N")) {
                lp = new LayoutParams(385, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
                tr.setLayoutParams(lp);
                lp.setMargins(0, 10, 40, 0);

            } else {
                lp = new LayoutParams(200, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
                lp.setMargins(0, 20, 70, 0);
                tr.setLayoutParams(lp);
            }
        }

        @Override
        protected String doInBackground(Void... arg0) {
            return "";
        }


        @Override
        public void onPostExecute(String res) {
            if (flag == 1) {
                resulttab.addView(tr, new TableLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

            } else {
                if (getActivity() != null)
                    Toast.makeText(getActivity().getApplicationContext(), msg,
                            Toast.LENGTH_LONG).show();
            }
            Pb.setVisibility(View.INVISIBLE);
        }
    }
}