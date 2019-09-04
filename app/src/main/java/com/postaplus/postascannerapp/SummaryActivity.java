package com.postaplus.postascannerapp;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import TabsPagerAdapter.TabsPagerAdapter_Summary;


/**
 * Created by ahlaam.kazi on 9/27/2016.
 */

public class SummaryActivity extends FragmentActivity implements ActionBar.TabListener {


    private ViewPager viewPager;
    private TabsPagerAdapter_Summary mAdapter;
    private ActionBar actionBar;



    // Tab titles
    private String[] tabs = { "WC", "Pickup", "Ack","Return" };
    SharedPreferences pref;
    TextView username,textViewPushicon;
    ImageView imageViewPushicon;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_summary);
        ActionBar localActionBar = getActionBar();
        localActionBar.setCustomView(R.layout.actionbar_layout);
        localActionBar.setDisplayShowTitleEnabled(false);
        localActionBar.setDisplayShowCustomEnabled(true);
        localActionBar.setDisplayUseLogoEnabled(true);
        localActionBar.setDisplayShowHomeEnabled(false);
        localActionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#1c181c")));
        //localActionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM | ActionBar.DISPLAY_SHOW_HOME);
        System.out.println("Summary Activity Page");

        //for networkonmainthreadexception
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        username=(TextView) findViewById(R.id.unametxt);

        pref = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        username.setText(pref.getString("uname", ""));

        imageViewPushicon = (ImageView)findViewById(R.id.imageViewPushicon);
        imageViewPushicon.setVisibility(View.GONE);
        // Initilization
        viewPager = (ViewPager) findViewById(R.id.pager);
        actionBar = getActionBar();
        mAdapter = new TabsPagerAdapter_Summary(getSupportFragmentManager());

        viewPager.setAdapter(mAdapter);
        actionBar.setHomeButtonEnabled(false);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        // Adding Tabs
        for (String tab_name : tabs) {
            actionBar.addTab(actionBar.newTab().setText(tab_name)
                    .setTabListener(this));
        }

        /**
         * on swiping the viewpager make respective tab selected
         * */
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                // on changing the page
                // make respected tab selected
                actionBar.setSelectedNavigationItem(position);
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
            }
        });
    }
    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {


    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
        // on tab selected
        // show respected fragment view
        viewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {


    }

    }
