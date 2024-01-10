package com.example.s364765;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class Resultat extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Setter main activity til activity som vises da notifkasjonsvindu klikkes på
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}

