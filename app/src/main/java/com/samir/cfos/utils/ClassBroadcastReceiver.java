package com.samir.cfos.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.widget.Toast;

/**
 * created by SAMIR SHRESTHA on 1/23/2020  at 5:22 PM
 */

public class ClassBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if(ConnectivityManager.CONNECTIVITY_ACTION.equals(intent.getAction())){
            boolean noConnectivity = intent.getBooleanExtra(
                    ConnectivityManager.EXTRA_NO_CONNECTIVITY, false
            );
            if(noConnectivity){
                Toast.makeText(context, "Network Disconnected", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "Network Connected", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
