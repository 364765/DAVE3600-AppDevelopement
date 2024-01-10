package com.example.s364765;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;

import com.example.avtaleapp.R;

public class SlettVennDialog extends DialogFragment {
    private SlettVennDialog.SlettInterface callback;
    private long vennID;
    private Datakilde dataKilde;

    public SlettVennDialog(long vennID, Datakilde datakilde) {
        this.vennID = vennID;
        this.dataKilde = datakilde;
    }


    public interface SlettInterface {
        void onSlettClick(long vennID);

        void onAvbrytClick();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            callback = (SlettVennDialog.SlettInterface) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException("Kallende klasse må implementere interfacet!");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        // Bruker LayoutInflater til å bruke slettvenn_dialog.xml som layout i dialog vindu
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.slettvenn_dialog, null);

        TextView navnTextView = view.findViewById(R.id.dialogNavn);
        TextView tlfTextView = view.findViewById(R.id.dialogTelefonNr);

        // Henter info fra DB om venn med id som trykkes på i listen
        Venner venn = dataKilde.finnVennMedID(vennID);

        navnTextView.setText("Navn: " + venn.getNavn());
        tlfTextView.setText("Telefon nummer: " + venn.getTelefonNr());

        // Setter viewet
        builder.setView(view)
                .setTitle("Kontakt info")
                .setPositiveButton("Slett", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        callback.onSlettClick(vennID);
                    }
                })
                .setNegativeButton("Tilbake", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        callback.onAvbrytClick();
                    }
                });
        return builder.create();
    }
}


