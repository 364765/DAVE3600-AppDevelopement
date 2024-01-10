package com.example.s364765;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.avtaleapp.R;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements AvtaleDialog.AvtaleInterface, SlettAvtaleDialog.SlettInterface {

    private Datakilde dataKilde;
    private ArrayAdapter<Avtaler> avtalerArrayAdapter;
    private List<Avtaler> avtaler;
    private static final int SEND_SMS_PERMISSION_REQUEST_CODE = 1;
    String CHANNEL_ID = "MinKanal";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Kaller på funksjon som lager avtale notifikasjons kanalen
        createNotificationChannel();

        dataKilde = new Datakilde(this);
        dataKilde.open();

        // Viewet med liste over avtaler
        ListView avtalerListView = findViewById(R.id.avtalerListView);

        avtaler = dataKilde.finnAlleAvtaler();

        // Legger in alle avtaler i list viewet
        avtalerArrayAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, avtaler);
        avtalerListView.setAdapter(avtalerArrayAdapter);


        // Ny avtale knapp
        Button leggtilButton = findViewById(R.id.avtaleBtn);
        leggtilButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AvtaleDialog avtaleDialog = new AvtaleDialog();
                avtaleDialog.show(getSupportFragmentManager(), "AvtaleDialog");
            }
        });


        // Venner knapp
        Button vennerButton = (Button) findViewById(R.id.vennerBtn);
        Intent i = new Intent(this, VennerActivity.class);
        vennerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(i);
            }
        });

        // Instillinger knapp
        Button preferanseknapp = findViewById(R.id.preferanserBtn);
        Intent intent = new Intent(this, SettingsActivity.class);
        preferanseknapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(intent);
            }
        });


        // onClick listener for avtale i liste som åpner dialog vindu med info
        avtalerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View v, int position, long id) {
                Avtaler avtale = avtaler.get(position);
                long avtaleID = avtale.getAvtaleID();

                SlettAvtaleDialog slettAvtaleDialog = new SlettAvtaleDialog(avtaleID, dataKilde);
                slettAvtaleDialog.show(getSupportFragmentManager(), "SlettAvtaleDialog");
            }
        });

        // Spør om tillatelse til å sende SMS
        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new
                            String[]{android.Manifest.permission.SEND_SMS},
                    SEND_SMS_PERMISSION_REQUEST_CODE);

        }

        // Lager minBroadcastReceiver og lytter etter signal
        BroadcastReceiver minBroadcastReceiver = new MinBroadcastReceiver();
        IntentFilter filter = new IntentFilter("com.example.service.SMS_TJENESTE_STARTET_SIGNAL");
        filter.addAction("com.example.service.SMS_TJENESTE_STARTET_SIGNAL");
        this.registerReceiver(minBroadcastReceiver, filter);


    }

    // Lager notifikasjons kanal som sendes da avtale påminnelse blir send ut
    private void createNotificationChannel() {
        CharSequence name = getString(R.string.channel_name);
        String description = getString(R.string.channel_description);
        int importance = NotificationManager.IMPORTANCE_DEFAULT;
        NotificationChannel channel = new
                NotificationChannel(CHANNEL_ID, name, importance);
        channel.setDescription(description);
        NotificationManager notificationManager =
                getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel);
    }


    @Override
    public void onAvbrytClick() {

    }

    // leggTilAvtale knapp i dialog
    @Override
    public void onLeggTilClick(String navn, String dato, String klokkeslett, String treffsted, String[] vennerListe) {
        if (!navn.isEmpty() && !dato.isEmpty() && !klokkeslett.isEmpty() && !treffsted.isEmpty()) {

            //Liste med id til venner i avtalen
            List<Long> vennIds = new ArrayList<>();

            // Liste med venner som allerede er lagret som kontakt
            List<Venner> lagredeVenner = dataKilde.finnAlleVenner();

            // Går gjennom vennerListe for hvert navn i avtalen
            for (String vennNavn : vennerListe) {
                // Bruker trim for å fjerne mellomrom/komma osv
                vennNavn = vennNavn.trim();
                // Går gjennom liste med venner som er lagret som kontakt
                for (Venner venn : lagredeVenner) {
                    String lagretVennNavn = venn.getNavn().trim();
                    // Sjekker om navnet i avtalen er likt navnet i liste med lagrede kontakter
                    if (vennNavn.equals(lagretVennNavn)) {
                        // Navnene matcher og id'n til navnet i avtalen blir lagret i vennIds liste
                        vennIds.add(venn.getId());
                        break;
                    }
                }
            }
            // Oppdaterer DB
            Avtaler nyAvtale = dataKilde.leggInnAvtale(navn, dato, klokkeslett, treffsted, vennIds);
            //dataKilde.leggInnAvtale(navn, dato, klokkeslett, treffsted, vennIds);
            avtalerArrayAdapter.add(nyAvtale);
        }
    }

    // SlettAvtale knapp i dialog
    @Override
    public void onSlettClick(long avtaleID) {
        dataKilde.slettAvtale(avtaleID);

        //Oppdaterer view
        ListView avtalerListView = findViewById(R.id.avtalerListView);
        avtaler = dataKilde.finnAlleAvtaler();
        avtalerArrayAdapter = new ArrayAdapter<>(getBaseContext(),
                android.R.layout.simple_list_item_1, avtaler);
        avtalerListView.setAdapter(avtalerArrayAdapter);
    }

    // Sjekker at tillatelse er gitt til å sende SMS
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == SEND_SMS_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] ==
                    PackageManager.PERMISSION_GRANTED) {
            } else {
                Toast.makeText(
                        this,
                        "SMS tillatelse ikke gitt. Du kan ikke sende SMS.",
                        Toast.LENGTH_SHORT
                ).show();
            }
        }

    }

}