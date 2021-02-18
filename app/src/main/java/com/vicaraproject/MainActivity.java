package com.vicaraproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.RemoteViews;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
Switch swbt,wifiSwitch;
TextView txtbtstatus,txtwifistatus;
BluetoothAdapter bluetoothAdapter;
Intent serviceIntent;
RelativeLayout RLBT,RLWifi;
private  WifiManager wifiManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        swbt=(Switch) findViewById(R.id.swbluetooth);
        txtbtstatus=findViewById(R.id.bluetoothstatus);
        txtwifistatus=findViewById(R.id.status);
        RLBT=findViewById(R.id.RLbt);
        RLWifi=findViewById(R.id.RLwifi);
        wifiSwitch = findViewById(R.id.swwifi);
        wifiSwitch.setText("Wifi Turn ON/OFF");
        swbt.setText("Bluetooth turn ON/OFF");

        serviceIntent = new Intent(getApplicationContext(), Services.class);
        serviceIntent.putExtra("inputExtra", "Network status");

   bluetoothAdapter=BluetoothAdapter.getDefaultAdapter();

        RLBT.setBackgroundColor(Color.RED);
        RLWifi.setBackgroundColor(Color.RED);



 swbt.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
     @Override
     public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

         if (swbt.isChecked())
         {
             txtbtstatus.setText("Bluetooth Turn On");
             bluetoothAdapter.enable();
             Intent serviceIntent = new Intent(getApplicationContext(), Services.class);
             serviceIntent.putExtra("inputExtra", "Blutooth turn on");
             ContextCompat.startForegroundService(getApplicationContext(), serviceIntent);

             Toast.makeText(MainActivity.this, "status  bluetooth turn on", Toast.LENGTH_SHORT).show();

         }else
         {
             txtbtstatus.setText("Bluetooth Turn Off");

             bluetoothAdapter.disable();
             Intent serviceIntent = new Intent(getApplicationContext(), Services.class);
             serviceIntent.putExtra("inputExtra", "Bluetooth turn is OFF");
             ContextCompat.startForegroundService(getApplicationContext(), serviceIntent);

             Toast.makeText(MainActivity.this, "status  bluetooth turn off", Toast.LENGTH_SHORT).show();
         }
     }
 });

        wifiSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                if (isChecked)
                {
                    txtwifistatus.setText("Wifi Turn On");
                    wifiManager.setWifiEnabled(true);
                    Intent serviceIntent = new Intent(getApplicationContext(), Services.class);
                    serviceIntent.putExtra("inputExtra", "Wifi turn On");
                    ContextCompat.startForegroundService(getApplicationContext(), serviceIntent);

                    Toast.makeText(MainActivity.this," Wifi turn on", Toast.LENGTH_SHORT).show();
                }else
                {
                    txtwifistatus.setText("Wifi Turn On");
                    wifiManager.setWifiEnabled(false);
                    Intent serviceIntent = new Intent(getApplicationContext(), Services.class);
                    serviceIntent.putExtra("inputExtra", "Wifi turn Off");
                    ContextCompat.startForegroundService(getApplicationContext(), serviceIntent);

                }
            }
        });


    }






    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter intentFilter = new IntentFilter(WifiManager.WIFI_STATE_CHANGED_ACTION);
        IntentFilter intentFilter2 = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);


        registerReceiver(wifiStateReceiver, intentFilter);
        registerReceiver(ReceiverState,intentFilter2);
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(wifiStateReceiver);
        unregisterReceiver(ReceiverState);


    }

    private BroadcastReceiver wifiStateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int wifiStateExtra = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE,
                    WifiManager.WIFI_STATE_UNKNOWN);
            switch (wifiStateExtra) {
                case WifiManager.WIFI_STATE_ENABLED:
                    wifiSwitch.setChecked(true);
                    txtwifistatus.setText("Wifi turn ON");
                    break;
                case WifiManager.WIFI_STATE_DISABLED:
                    wifiSwitch.setChecked(false);
                    txtwifistatus.setText("Wifi turn OFF");
                    break;
            }
        }


    };
    private BroadcastReceiver ReceiverState = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String status = Networkutil.getConnectivityStatusString(context);
            if(status.isEmpty() || status.equals("No internet is available")) {
                status="No Internet Connection";
                RLBT.setBackgroundColor(Color.RED);
                RLWifi.setBackgroundColor(Color.RED);
                Toast.makeText(getApplicationContext(),"Internet Connection OFF",Toast.LENGTH_SHORT).show();

            }else {
                RLBT.setBackgroundColor(Color.GREEN);
                RLWifi.setBackgroundColor(Color.GREEN);
                Toast.makeText(getApplicationContext(),"Internet Connection ON",Toast.LENGTH_SHORT).show();

            }

            if (status.equals("Mobile Bluetooth enabled"))
            {
                swbt.setChecked(true);
            }else
            {
                swbt.setChecked(true);
            }
            Intent serviceIntent = new Intent(getApplicationContext(), Services.class);
            serviceIntent.putExtra("inputExtra", status);
            ContextCompat.startForegroundService(getApplicationContext(), serviceIntent);        }



    };






}