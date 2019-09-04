package com.postaplus.postascannerapp;

import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

class PhoneStateChangeListener extends PhoneStateListener {
    public static boolean wasRinging;
    String incoming_nr;
    int prev_state=0;
    private static final String TAG = "CustomPhoneStateListener";

    @Override  
    public void onCallStateChanged(int state, String incomingNumber){


		//if(incomingNumber!=null&&incomingNumber.length()>0)
       // 	incoming_nr=incomingNumber;   

        switch(state){  
            case TelephonyManager.CALL_STATE_RINGING:  
                //    Log.d(TAG, "CALL_STATE_RINGING");
                    prev_state=state;  
                    break;  
            case TelephonyManager.CALL_STATE_OFFHOOK:  
         //   Log.d(TAG, "CALL_STATE_OFFHOOK");
            prev_state=state;  
            break;  
            case TelephonyManager.CALL_STATE_IDLE:  
          //      Log.d(TAG, "CALL_STATE_IDLE");
              //  NumberDatabase database=new NumberDatabase(mContext);  
                if((prev_state==TelephonyManager.CALL_STATE_OFFHOOK)){  
                    prev_state=state;  
             //       Log.d(TAG, "ANSWER");
                    //Answered Call which is ended  
                }  
                if((prev_state==TelephonyManager.CALL_STATE_RINGING)){  
                    prev_state=state;  
                 //   Log.d(TAG, "REJECT");
                    //Rejected or Missed call  
                }  
                break;  

        }  
    }

}