package com.postaplus.postascannerapp;

import android.app.Application;
import android.content.Intent;
import android.util.Log;

import com.onesignal.OSNotificationPayload;
import com.onesignal.OneSignal;

import static com.postaplus.postascannerapp.MasterActivity.pushWaybillCount;
import static com.postaplus.postascannerapp.MasterActivity.pushawbList;
import static com.postaplus.postascannerapp.MasterActivity.pushawbStrarray;
import static com.postaplus.postascannerapp.MasterActivity.updatedPushWaybill;

public class ApplicationClass extends Application {
    private static ApplicationClass applicationClass;
    public static final String TAG = ApplicationClass.class
            .getSimpleName();
    public static synchronized ApplicationClass getInstance() {
        return applicationClass;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        OneSignal.startInit(this)
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                .unsubscribeWhenNotificationsAreDisabled(true)
                .setNotificationReceivedHandler(notification -> {
                    OSNotificationPayload payload = notification.payload;
                    Log.e("data1", payload.body);
                    if(payload.body.contains(":")){
                        String[] pushwaybillString = payload.body.split(":");
                        //updatedPushWaybill = pushwaybillString[1].toString();
                        pushWaybillCount++;

                        if(pushWaybillCount==1){
                            updatedPushWaybill = pushwaybillString[1].toString().trim();

                        }else if(pushWaybillCount>1){

                            if (updatedPushWaybill.contains(pushwaybillString[1].toString().trim())) {
                                pushWaybillCount= pushWaybillCount-1;
                                return;
                            }else{
                                updatedPushWaybill = updatedPushWaybill+","+pushwaybillString[1].toString().trim();

                            }


                        }
                        pushawbList =updatedPushWaybill.split(",");
                        pushawbStrarray = new String[pushWaybillCount];
                        //pushawbStrarray = new HashSet<String>(Arrays.asList(pushawbStrarray)).toArray(new String[0]);
                        for(int i =0; i< pushWaybillCount;i++){

                            pushawbStrarray[i]=pushawbList[i].toString();

                        }

                       // textViewPushicon.setText(String.valueOf(pushWaybillCount));

                    }
                }).setNotificationOpenedHandler(result -> {

            Intent intent = new Intent(this, ScreenActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            this.startActivity(intent);

        }).init();
        // OneSignal Initialization
     /*   OneSignal.startInit(this)
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                .unsubscribeWhenNotificationsAreDisabled(true)
                .setNotificationReceivedHandler(notification -> {
                    OSNotificationPayload payload = notification.payload;
                    Log.e("data", payload.body);

                })

        .init();*/

    }
}
