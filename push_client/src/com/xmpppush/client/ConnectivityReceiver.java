package com.xmpppush.client;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

public class ConnectivityReceiver extends BroadcastReceiver {

    private static final String LOGTAG = CommonUtil
            .makeLogTag(ConnectivityReceiver.class);

    private NotificationService notificationService;

    public ConnectivityReceiver(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(LOGTAG, "ConnectivityReceiver.onReceive()...");
        String action = intent.getAction();
        Log.d(LOGTAG, "action=" + action);

        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo != null) {
            Log.d(LOGTAG, "Network Type  = " + networkInfo.getTypeName());
            Log.d(LOGTAG, "Network State = " + networkInfo.getState());
            if (networkInfo.isConnected()) {
                Log.i(LOGTAG, "Network connected");
                notificationService.connect();
            }
        } else {
            Log.e(LOGTAG, "Network unavailable");
            notificationService.disconnect();
        }
    }

}
