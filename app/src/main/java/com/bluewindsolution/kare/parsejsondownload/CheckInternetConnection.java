package com.bluewindsolution.kare.parsejsondownload;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.widget.Toast;

/**
 * Created by Pruxasin on 10/2/2559.
 */
public class CheckInternetConnection {
    Context context;

    public CheckInternetConnection(Context mainActivity) {
        this.context = mainActivity;
    }

    public boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        return (cm.getActiveNetworkInfo() != null);
    }

    public void showNotifications(String stToast, int length) {
        Toast.makeText(context, stToast, length).show();
    }
}
