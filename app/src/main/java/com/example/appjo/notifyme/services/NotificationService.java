package com.example.appjo.notifyme.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.text.method.NumberKeyListener;
import android.util.Log;

public class NotificationService extends NotificationListenerService {

    private String TAG = NotificationService.class.getSimpleName();
    private NotificationServiceReceiver notificationServiceReceiver;

    @Override
    public void onCreate(){
        super.onCreate();
        notificationServiceReceiver = new NotificationServiceReceiver();
        IntentFilter intentFilter = new IntentFilter();

        //this intent filter will help in calling the receiver with appropriate action
        intentFilter.addAction("com.example.appjo.notifyme.services.NOTIFICATION_SERVICE");
        registerReceiver(notificationServiceReceiver, intentFilter);
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        unregisterReceiver(notificationServiceReceiver);
    }

    @Override
    public void onListenerConnected(){
        Log.v(TAG, "Notification Service Listener Connected");
    }

    @Override
    public void onListenerDisconnected() {
        Log.v(TAG, "Notification Service Listener Disconnected");

    }

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {

    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
        Log.v("check", "SBN ---> " + sbn.getPackageName());
    }

     class NotificationServiceReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            try{
                if(intent.getStringExtra("command").equals("list")){
                    int i = 1;
                    for (StatusBarNotification statusBarNotification : NotificationService.this.getActiveNotifications()){
                        Intent intent1 = new Intent("com.example.appjo.notifyme.services.NOTIFICATION_SERVICE");
                        Log.v(TAG, "Package-Name" + statusBarNotification.getPackageName());
                        intent1.putExtra("notification_event", i + " " + statusBarNotification.getPackageName());
                        sendBroadcast(intent1);
                        i++;
                    }
                    Intent intent2 = new Intent("com.example.appjo.notifyme.services.NOTIFICATION_SERVICE");
                    intent2.putExtra("adapter_update_event", "update");
                    sendBroadcast(intent2);
                }
            }catch (NullPointerException e){
                e.printStackTrace();
            }

        }
    }
}
