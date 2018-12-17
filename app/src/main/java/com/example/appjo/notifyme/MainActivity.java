package com.example.appjo.notifyme;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appjo.notifyme.services.NotificationService;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private NotificationReceiver mReceiver;
    private RecyclerView mRecyclerView;
    private NotificationAdapter mAdapter;
    private SharedPreferences mPrefs;
    private static final int NOTIFICATION_ACCESS_CODE = 1500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        mRecyclerView = findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        mAdapter = new NotificationAdapter(this);
        mRecyclerView.setAdapter(mAdapter);
        mReceiver = new NotificationReceiver();
        IntentFilter filter = new IntentFilter();

        //this intent filter will help in calling the receiver with appropriate action
        filter.addAction("com.example.appjo.notifyme.services.NOTIFICATION_SERVICE");
        registerReceiver(mReceiver,filter);

        Intent intent = new Intent(
                "android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS");
        startActivity(intent);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mReceiver);
    }

    public void buttonClicked(View v){
        if(v.getId() == R.id.btnCreateNotify){
            NotificationManager nManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            NotificationCompat.Builder ncomp = new NotificationCompat.Builder(this);
            ncomp.setContentTitle("My Notification");
            ncomp.setContentText("Notification Listener Service Example");
            ncomp.setTicker("Notification Listener Service Example");
            ncomp.setSmallIcon(R.drawable.ic_launcher_foreground);
            ncomp.setAutoCancel(true);
            nManager.notify((int)System.currentTimeMillis(),ncomp.build());
        }
        else if(v.getId() == R.id.btnListNotify){
            Intent i = new Intent("com.example.appjo.notifyme.services.NOTIFICATION_SERVICE");
            i.putExtra("adapter_dataset_clean","clean");
            sendBroadcast(i);
        }
    }

    class NotificationReceiver extends BroadcastReceiver {

        private List<String> notifications = new ArrayList<>();
        @Override
        public void onReceive(Context context, Intent intent) {

            if (intent.getStringExtra("notification_event") != null){
                String data = intent.getStringExtra("notification_event");
                notifications.add(data);
            }else if (intent.getStringExtra("adapter_update_event") != null){
                if (intent.getStringExtra("adapter_update_event").equals("update")){
                    mAdapter.addNotifications(notifications);
                }
            }else if (intent.getStringExtra("adapter_dataset_clean") != null){
                if (intent.getStringExtra("adapter_dataset_clean").equals("clean")){
                    final int size = notifications.size();
                    if (size > 0) {
                        for (int i = 0; i < size; i++) {
                            notifications.remove(0);
                        }
                    }
                    Intent intent2 = new Intent("com.example.appjo.notifyme.services.NOTIFICATION_SERVICE");
                    intent2.putExtra("command","list");
                    sendBroadcast(intent2);
                }
            }
        }
    }
}
