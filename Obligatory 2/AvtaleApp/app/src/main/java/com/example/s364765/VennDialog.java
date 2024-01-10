package com.example.s364765;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.view.View;

import androidx.fragment.app.DialogFragment;

import com.example.avtaleapp.R;

public class VennDialog extends DialogFragment {
    private LeggTilInterface callback;

    public interface LeggTilInterface {
        void onLeggTilClick(String navn, String telefonNr);

        void onAvbrytClick();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            callback = (LeggTilInterface) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException("Kallende klasse må implementere interfacet!");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        // Bruker LayoutInflater til å bruke venn_dialog.xml som layout i dialog vindu
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.venn_dialog, null);

        EditText dialogInputNavn = view.findViewById(R.id.dialogInputNavn);
        EditText dialogInputTelefonNr = view.findViewById(R.id.dialogInputTelefonNr);

        // Setter viewet
        builder.setView(view)
                .setTitle("Legg til venn")
                .setPositiveButton("Legg til", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String navn = dialogInputNavn.getText().toString();
                        String telefonNr = dialogInputTelefonNr.getText().toString();
                        callback.onLeggTilClick(navn, telefonNr);
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




