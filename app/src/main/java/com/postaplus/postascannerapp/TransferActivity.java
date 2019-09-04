package com.postaplus.postascannerapp;


import android.app.ActionBar;
import android.app.ActionBar.Tab;
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

import TabsPagerAdapter.TabsPagerAdapter_Transfer;


public class TransferActivity extends FragmentActivity implements
ActionBar.TabListener {  
	
private ViewPager viewPager;
private TabsPagerAdapter_Transfer mAdapter;
private ActionBar actionBar;
// Tab titles
private String[] tabs = { "FWC", "FHold","Transfer Pickup"};
	SharedPreferences pref;
	TextView username;
	ImageView imageViewPushicon;

	
		
@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfer);
        ActionBar localActionBar = getActionBar();
		localActionBar.setCustomView(R.layout.actionbar_layout);
		localActionBar.setDisplayShowTitleEnabled(false);
		localActionBar.setDisplayShowCustomEnabled(true);
		localActionBar.setDisplayUseLogoEnabled(false);
		localActionBar.setDisplayShowHomeEnabled(false);
		localActionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#1c181c")));
    imageViewPushicon = (ImageView)findViewById(R.id.imageViewPushicon);
    imageViewPushicon.setVisibility(View.GONE);
		//for networkonmainthreadexception
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
	    StrictMode.setThreadPolicy(policy); 
				
		username=(TextView) findViewById(R.id.unametxt);
			
		pref = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
		username.setText(pref.getString("uname", ""));
		
	 // Initilization
      viewPager = (ViewPager) findViewById(R.id.pager);
      actionBar = getActionBar();
      mAdapter = new TabsPagerAdapter_Transfer(getSupportFragmentManager());

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
  public void onTabReselected(Tab tab, FragmentTransaction ft) {
  }

  @Override
  public void onTabSelected(Tab tab, FragmentTransaction ft) {
      // on tab selected
      // show respected fragment view
      viewPager.setCurrentItem(tab.getPosition());
  }

  @Override
  public void onTabUnselected(Tab tab, FragmentTransaction ft) {
  }

	     
}