package com.example.s364765;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SpillActivity extends AppCompatActivity implements avsluttDialog.AvsluttInterface, spillFerdigDialog.SpillFerdigInterface, tilbakemeldingDialog.TilbakemeldingInterface {
    List<String> regnestykker;
    List<String> fasit;
    List<String> blandedeRegnestykker;
    TextView regnestykkeTextView;
    TextView svarTextView;
    TextView tilbakemeldingTextView;
    TextView nåværendeRegnestykkeTextView;
    TextView antallRegnestykkerTextView;

    String feilTilbakemelding;
    String riktigTilbakemelding;
    String regnestykkeTittel;
    private SharedPreferences sharedPreferences;
    private Set<String> brukteRegnestykker = new HashSet<>();
    private int antallRegnestykkerGrense;
    private int nåværendeRegnestykkeIndex = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spill);

        sharedPreferences = getSharedPreferences("MinePreferanser", Context.MODE_PRIVATE);

        regnestykker = Arrays.asList(getResources().getStringArray(R.array.regnestykker));
        fasit = Arrays.asList(getResources().getStringArray(R.array.fasit));
        blandedeRegnestykker = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.regnestykker)));

        regnestykkeTextView = findViewById(R.id.randomRegnestykke);
        svarTextView = findViewById(R.id.svar);
        tilbakemeldingTextView = findViewById(R.id.tilbakemelding);
        nåværendeRegnestykkeTextView = findViewById(R.id.nåverendeRegnestykke);
        antallRegnestykkerTextView = findViewById(R.id.totaleRegnestykker);


        feilTilbakemelding = getResources().getString(R.string.feilTilbakemelding);
        riktigTilbakemelding = getResources().getString(R.string.riktigTilbakemelding);
        regnestykkeTittel = getResources().getString(R.string.regnestykkeTeller);

        randomRegnestykke();
        tallKnapper();
        slettKnapp();


    }

    /*----------------------Hente og generere regnestykke-------------------------------------*/
    private void randomRegnestykke() {
        //Henter antall spørsmål fra SharedPreferences
        sharedPreferences = getSharedPreferences("MinePreferanser", Context.MODE_PRIVATE);
        antallRegnestykkerGrense = sharedPreferences.getInt("antallRegnestykker", 5);

        //Blander regnestykkene
        Collections.shuffle(blandedeRegnestykker);

        //Sjekker om regnestykke index er under shared preferences grense
        if (nåværendeRegnestykkeIndex < antallRegnestykkerGrense) {

            //Oppdaterer regnestykket man er på og totale regnestykker views
            int regnestykkeViewInt = nåværendeRegnestykkeIndex + 1;
            nåværendeRegnestykkeTextView.setText(String.valueOf(regnestykkeTittel + " " + regnestykkeViewInt) + "/");
            antallRegnestykkerTextView.setText(String.valueOf(antallRegnestykkerGrense));

            // Henter neste regnestykke
            String randomRegnestykke = nesteRegnestykke(blandedeRegnestykker);
            regnestykkeTextView.setText(randomRegnestykke + " = ");

            //Sjekker index til regnestykke og henter fasit svaret som ligger på samme index
            int regnestykkeIndex = regnestykker.indexOf(randomRegnestykke);
            String riktigSvar = fasit.get(regnestykkeIndex);

            //Listener på svar knappen for å kalle på fasit sjekk
            Button svarButton = (Button) findViewById(R.id.svarButton);
            svarButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    sjekkFasit(riktigSvar);
                }
            });

        } else {
            // Grensen for antall regnestykker i SharedPreferences er nådd
            regnestykkeGrenseNådd();
        }
    }

    //Henter nytt regnestykke
    private String nesteRegnestykke(List<String> blandedeRegnestykker) {
        for (String regnestykke : blandedeRegnestykker) {
            //Sjekker og oppdaterer liste med brukte regnestykker
            if (!brukteRegnestykker.contains(regnestykke)) {
                brukteRegnestykker.add(regnestykke);
                nåværendeRegnestykkeIndex++;
                return regnestykke;
            }
        }
        return "";
    }

    /*----------------------Fasit sjekk----------------------------------------*/
    private void sjekkFasit(String riktigSvar) {
        String brukerSvar = svarTextView.getText().toString();

        //Sjekker om svar er riktig og oppdaterer tilbakemelding/svar felt
        if (brukerSvar.equals(riktigSvar)) {
            tilbakemeldingTextView.setText(riktigTilbakemelding);
            tilbakemeldingTextView.setTextColor(Color.parseColor("#008000"));

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    tilbakemeldingTextView.setText("");
                    svarTextView.setText("");
                    randomRegnestykke();
                }
            }, 3000);
        } else {
            tilbakemelding();
            svarTextView.setText("");
        }
    }

    /*----------------------Tall knapper----------------------------------------*/
    private void tallKnapper() {
        //Løkke for å finne valgt knapp etter id
        for (int i = 1; i <= 10; i++) {
            int knappId = getResources().getIdentifier("knapp" + i, "id", getPackageName());
            Button knapp = findViewById(knappId);
            tallKnapperListeners(knapp, i);
        }
    }

    private void tallKnapperListeners(Button knapp, int tall) {
        knapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tall == 10) {
                    svarTextView.append("0");
                } else {
                    svarTextView.append(String.valueOf(tall));
                }
            }
        });
    }

    /*----------------------Slett funksjoner----------------------------------------*/
    private void slettKnapp() {
        Button slett = (Button) findViewById(R.id.slettButton);
        slett.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                slettTall();
            }
        });
    }

    private void slettTall() {
        String nåværendeSvar = svarTextView.getText().toString();

        // Sjekker om svar er tomt
        if (!nåværendeSvar.isEmpty()) {
            //Sletter siste tall
            nåværendeSvar = nåværendeSvar.substring(0, nåværendeSvar.length() - 1);

            //Oppdaterer view
            svarTextView.setText(nåværendeSvar);
        }
    }

    /*--------------Innskrevet tall/regnestykke teller forblir det samme ved rotasjon--------------------*/
    @Override
    protected void onSaveInstanceState(Bundle outstate) {
        super.onSaveInstanceState(outstate);
        TextView textView = (TextView) findViewById(R.id.svar);
        outstate.putString("antall", textView.getText().toString());

        TextView textView1 = (TextView) findViewById(R.id.nåverendeRegnestykke);
        outstate.putString("nåStykke", textView1.getText().toString());
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        TextView tw = (TextView) findViewById(R.id.svar);
        tw.setText(savedInstanceState.getString("antall"));

        TextView tw1 = (TextView) findViewById(R.id.nåverendeRegnestykke);
        tw1.setText(savedInstanceState.getString("nåStykke"));
    }


    /*----------------------Dialoger----------------------------------------*/
    //Avslutt dialog
    @Override
    public void onJaClick() {
        finish();
    }

    @Override
    public void onNeiClick() {
        return;
    }

    public void onBackPressed() {
        DialogFragment dialog = new avsluttDialog();
        dialog.show(getSupportFragmentManager(), "AvsluttDialog");
    }


    //Spill ferdig dialog
    @Override
    public void onHjemClick() {
        finish();
    }


    private void regnestykkeGrenseNådd() {
        DialogFragment grenseDialog = new spillFerdigDialog();
        grenseDialog.show(getSupportFragmentManager(), "SpillFerdigDialog");
        regnestykkeTextView.setText("");
    }


    //Tilbakemelding dialog

    @Override
    public void onLukkClick() {
        return;
    }

    private void tilbakemelding() {
        DialogFragment tilbakemeldingDialog = new tilbakemeldingDialog();
        tilbakemeldingDialog.show(getSupportFragmentManager(), "TilbakemeldingDialog");

    }


}

