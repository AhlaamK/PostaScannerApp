package com.postaplus.postascannerapp;

import android.app.Activity;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by ahlaam.kazi on 2/20/2018.
 */

public class ActivityNotification {

    public void setRouteCode(Activity activity, String sotre_Obj ){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences( activity);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("Routecode", sotre_Obj );
        editor.commit();
    }
    public String getRouteCode(Activity activity){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences( activity);
        String sync_Obj = sharedPreferences.getString("Routecode", "");
        return sync_Obj;
    }


    public void setRouteName(Activity activity, String sotre_Obj ){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences( activity);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("RouteName", sotre_Obj );
        editor.commit();
    }
    public String getRouteName(Activity activity){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences( activity);
        String sync_Obj = sharedPreferences.getString("RouteName", "");
        return sync_Obj;

        }

    public void setdrivercode(Activity activity, String sotre_Obj ){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences( activity);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("Username", sotre_Obj );
        editor.commit();
    }
    public String getdrivercode(Activity activity){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences( activity);
        String sync_Obj = sharedPreferences.getString("Username", "");
        return sync_Obj;

    }
}
