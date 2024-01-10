package com.example.s364765;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;


public class MinBroadcastReceiver extends BroadcastReceiver {
    public MinBroadcastReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent periodisk = new Intent(context, MinPeriodisk.class);
        context.startService(periodisk);
        Log.d("MinBroadcastReceiver", "Kaller på MinPeriodisk");
    }
}





