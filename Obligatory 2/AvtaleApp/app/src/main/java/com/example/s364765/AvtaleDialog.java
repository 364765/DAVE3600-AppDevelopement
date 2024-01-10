package com.example.s364765;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.fragment.app.DialogFragment;

import com.example.avtaleapp.R;


public class AvtaleDialog extends DialogFragment {
    private AvtaleDialog.AvtaleInterface callback;

    public interface AvtaleInterface {
        void onLeggTilClick(String navn, String dato, String klokkeslett, String treffsted, String[] venner);

        void onAvbrytClick();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            callback = (AvtaleDialog.AvtaleInterface) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException("Kallende klasse må implementere interfacet!");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        // Bruker LayoutInflater til å bruke avtale_dialog.xml som layout i dialog vindu
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.avtale_dialog, null);

        EditText dialogInputAvtaleNavn = view.findViewById(R.id.dialogInputAvtaleNavn);
        EditText dialogInputDato = view.findViewById(R.id.dialogInputDato);
        EditText dialogInputKlokkeslett = view.findViewById(R.id.dialogInputKlokkeslett);
        EditText dialogInputTreffsted = view.findViewById(R.id.dialogInputTreffsted);
        EditText dialogInputVenner = view.findViewById(R.id.dialogInputVenner);

        // Setter viewet
        builder.setView(view)
                .setTitle("Legg til avtale")
                .setPositiveButton("Legg til", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String navn = dialogInputAvtaleNavn.getText().toString();
                        String dato = dialogInputDato.getText().toString();
                        String klokkeslett = dialogInputKlokkeslett.getText().toString();
                        String treffsted = dialogInputTreffsted.getText().toString();
                        String venner = dialogInputVenner.getText().toString();

                        // Fjerner "," og legger i liste
                        String[] vennerListe = venner.split(",");

                        callback.onLeggTilClick(navn, dato, klokkeslett, treffsted, vennerListe);
                    }
                })
                .setNegativeButton("Avbryt", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        callback.onAvbrytClick();
                    }
                });
        return builder.create();
    }
}

