package com.postaplus.postascannerapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.StrictMode;

public class MyBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
    	StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
    	StrictMode.setThreadPolicy(policy); 
    	
    	//Intent startServiceIntent = new Intent(context, PostaNotificationService.class);
       // context.startService(startServiceIntent);
       // Log.i("Autostart", "started");
    }
}
