package com.example.s364765;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.fragment.app.DialogFragment;

public class LagreDialog extends DialogFragment {
    private LagreDialog.LagreInterface callback;


    public interface LagreInterface {
        void onAvbrytClick();

        void onLagreClick(String navn, String beskrivelse, String adresse, String latitude, String longitude);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            callback = (LagreDialog.LagreInterface) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException("Kallende klasse må implementere interfacet!");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        // Bruker LayoutInflater til å bruke lagre_dialog.xml som layout i dialog vindu
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.lagre_dialog, null);

        // Henter verdier fra dialog input
        EditText dialogStedNavn = view.findViewById(R.id.dialogStedNavn);
        EditText dialogBeskrivelse = view.findViewById(R.id.dialogBeskrivelse);

        // Henter verdier fra bundle som ble laget i MapsActivity
        String adresse = getArguments().getString("adresse");
        String latitude = getArguments().getString("latitude");
        String longitude = getArguments().getString("longitude");


        // Setter viewet
        builder.setView(view)
                .setTitle("Legg til nytt sted - " + adresse)
                .setPositiveButton("Legg til", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String stedNavn = dialogStedNavn.getText().toString();
                        String beskrivelse = dialogBeskrivelse.getText().toString();

                        // Utfører onLagreClick metoden i MapsActivity
                        callback.onLagreClick(stedNavn, beskrivelse, adresse, latitude, longitude);
                        Log.d("Dialog values: ", stedNavn + beskrivelse + adresse + latitude + longitude);

                    }

                })
                .setNegativeButton("Avbryt", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // Utfører onAvbrytClick i MpasActivity
                        callback.onAvbrytClick();
                    }
                });
        return builder.create();
    }
}

