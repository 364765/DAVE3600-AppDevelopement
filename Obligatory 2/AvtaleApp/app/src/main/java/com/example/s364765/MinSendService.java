package com.example.s364765;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.preference.PreferenceManager;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class MinSendService extends Service {
    private Datakilde dataKilde;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("MinSendService", "Sjekker DB for avtaler");
        sjekkDBForAvtaler();
        return super.onStartCommand(intent, flags, startId);
    }

    private void sjekkDBForAvtaler() {
        dataKilde = new Datakilde(this);
        dataKilde.open();
        // Henter dagens dato og formaterer den
        Calendar dagensDato = Calendar.getInstance();
        SimpleDateFormat datoFormat = new SimpleDateFormat("dd.MM.yy", Locale.getDefault());
        String datoIDag = datoFormat.format(dagensDato.getTime());

        // Lager liste med avtaler hvor dato er dagens dato
        List<Avtaler> avtalerMedDagensDato = dataKilde.finnAvtaleMedDato(datoIDag);

        // Går gjennom lista og sender påminnelse til venner som er inkludert i gjeldende avtaler
        for (Avtaler avtale : avtalerMedDagensDato) {
            long avtaleID = avtale.getAvtaleID();
            List<Venner> vennerForAvtale = dataKilde.finnVennerForAvtale(avtaleID);
            for (Venner venn : vennerForAvtale) {
                long vennID = venn.getId();

                Log.d("MinSendService", "Sender SMS påminnelse");
                sendPåminnelseSMS(avtaleID, vennID);
            }
        }
    }

    private void sendPåminnelseSMS(long avtaleID, long vennID) {
        // Henter sms melding innhold
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String smsMelding = sharedPreferences.getString("smsMelding", "");
        System.out.println("smsMelding: " + smsMelding);

        // Finner venn og tlfNr
        Venner venn = dataKilde.finnVennMedID(vennID);
        String vennTlf = venn.getTelefonNr();


        // Kaller på minService som sender notifikasjon
        Intent notifikasjon = new Intent(this, MinService.class);
        startService(notifikasjon);


        // Sender sms
        if (!vennTlf.isEmpty() && !smsMelding.isEmpty()) {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(vennTlf, null, smsMelding, null, null);
            Log.d("MinSendService", "SMS sendt til " + venn.getNavn() + " (" + vennTlf + ")");
            Toast.makeText(this, "SMS sendt til " + venn.getNavn() + " (" + vennTlf + "), innhold: " + smsMelding, Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "Feil ved sending av SMS til " + venn.getNavn() + " (" + vennTlf + ")",
                    Toast.LENGTH_SHORT).show();
        }
    }


}




