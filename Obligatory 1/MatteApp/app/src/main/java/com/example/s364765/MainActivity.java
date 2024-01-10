package com.example.s364765;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Preferanser knapp med listener
        Button preferanserButton = (Button) findViewById(R.id.preferanserButton);
        Intent i = new Intent(this, PreferanserActivity.class);
        preferanserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(i);
            }
        });

        //Start spill knapp med listener
        Button spillButton = (Button) findViewById(R.id.spillButton);
        Intent k = new Intent(this, SpillActivity.class);
        spillButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(k);
            }
        });


        //Info knapp med listener
        Button infoButton = (Button) findViewById(R.id.infoButton);
        Intent l = new Intent(this, InfoActivity.class);
        infoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(l);
            }
        });
    }

    //Setter default språk shared preferences
    @Override
    protected void onStart() {
        super.onStart();

        SharedPreferences sharedPreferences = getSharedPreferences("MinePreferanser", Context.MODE_PRIVATE);
        String valgtSpråk = sharedPreferences.getString("valgtSpråk", "");
    }
}