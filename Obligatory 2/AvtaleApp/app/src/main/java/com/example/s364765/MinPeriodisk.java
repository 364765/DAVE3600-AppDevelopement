package com.example.s364765;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.preference.PreferenceManager;

import java.util.Calendar;

public class MinPeriodisk extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Henter smsTidspunkt fra sharedPreferences
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String tidspunkt = sharedPreferences.getString("smsTidspunkt", "");

        // Formaterer tidspunktSMS og setter verdier
        String[] tidspunktSMS = tidspunkt.split(":");
        int ønsketTime = Integer.parseInt(tidspunktSMS[0]);
        int ønsketMinutt = Integer.parseInt(tidspunktSMS[1]);

        // Finner tidspunkt ved hjelp av Calendar
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, ønsketTime);
        calendar.set(Calendar.MINUTE, ønsketMinutt);
        calendar.set(Calendar.SECOND, 0);

        // Kaller på MinSendService
        Intent i = new Intent(this, MinSendService.class);
        PendingIntent pintent = PendingIntent.getService(this, 0, i, PendingIntent.FLAG_IMMUTABLE);

        // Setter alarm som vil utføre kallet på MinSendService
        AlarmManager alarm = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarm.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pintent);
        Log.d("MinPeriodisk", "Setter alarm");

        return super.onStartCommand(intent, flags, startId);
    }
}
