package com.example.s364765;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.os.LocaleListCompat;

public class PreferanserActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferanser);

        //Initialiserer knappene
        Button norskButton = (Button) findViewById(R.id.norskButton);
        Button tyskButton = (Button) findViewById(R.id.tyskButton);
        Button femSpørsmål = (Button) findViewById(R.id.femSpørsmålButton);
        Button tiSpørsmål = (Button) findViewById(R.id.tiSpørsmålButton);
        Button femtenSpørsmål = (Button) findViewById(R.id.femtenSpørsmålButton);


        //Setter språk til Norsk
        norskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LocaleListCompat appLocale = LocaleListCompat.forLanguageTags("no-NO");
                AppCompatDelegate.setApplicationLocales(appLocale);

                SharedPreferences sharedPreferences = getSharedPreferences("MinePreferanser", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("valgtSpråk", "no-NO");
                editor.apply();
            }
        });

        //Setter språk til Tysk
        tyskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LocaleListCompat appLocale = LocaleListCompat.forLanguageTags("de-DE");
                AppCompatDelegate.setApplicationLocales(appLocale);


                SharedPreferences sharedPreferences = getSharedPreferences("MinePreferanser", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("valgtSpråk", "de-DE");
                editor.apply();
            }
        });


        //Setter antall spørsmål til 5
        femSpørsmål.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sharedPreferences = getSharedPreferences("MinePreferanser", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt("antallRegnestykker", 5);
                editor.apply();
            }
        });

        //Setter antall spørsmål til 10
        tiSpørsmål.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sharedPreferences = getSharedPreferences("MinePreferanser", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt("antallRegnestykker", 10);
                editor.apply();
            }
        });

        //Setter antall spørsmål til 15
        femtenSpørsmål.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sharedPreferences = getSharedPreferences("MinePreferanser", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt("antallRegnestykker", 15);
                editor.apply();
            }
        });


    }
}
