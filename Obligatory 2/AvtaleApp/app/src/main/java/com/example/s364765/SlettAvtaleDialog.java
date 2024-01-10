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

import java.util.List;

public class SlettAvtaleDialog extends DialogFragment {
    private SlettAvtaleDialog.SlettInterface callback;
    private long avtaleID;
    private Datakilde dataKilde;

    public SlettAvtaleDialog(long avtaleID, Datakilde datakilde) {
        this.avtaleID = avtaleID;
        this.dataKilde = datakilde;
    }


    public interface SlettInterface {
        void onSlettClick(long avtaleID);

        void onAvbrytClick();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            callback = (SlettAvtaleDialog.SlettInterface) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException("Kallende klasse må implementere interfacet!");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        // Bruker LayoutInflater til å bruke slettavtale_dialog.xml som layout i dialog vindu
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.slettavtale_dialog, null);

        TextView navnTextView = view.findViewById(R.id.dialogAvtaleNavn);
        TextView datoTextView = view.findViewById(R.id.dialogAvtaleDato);
        TextView klokkeslettTextView = view.findViewById(R.id.dialogAvtaleKlokkeslett);
        TextView treffstedTextView = view.findViewById(R.id.dialogAvtaleTreffsted);
        TextView avtaleVennerTextView = view.findViewById(R.id.avtaleVenner);

        // Finner alle venner som er med i avtale i DB og legger i liste
        List<Venner> vennerIAvtale = dataKilde.finnVennerForAvtale(avtaleID);

        // String builder for å legge venners navn  i String
        StringBuilder avtaleVenner = new StringBuilder("Venner: ");
        for (Venner venn : vennerIAvtale) {
            avtaleVenner.append(venn.getNavn()).append(", ");
        }

        // Fjerner komma på siste venn og setter til text view
        if (avtaleVenner.length() > 2) {
            avtaleVenner.setLength(avtaleVenner.length() - 2);
        }

        // Legger in string med venner i dialog vindu
        avtaleVennerTextView.setText(avtaleVenner.toString());

        // Finner avtalen i DB og fyller dialog med info
        Avtaler avtale = dataKilde.finnAvtaleMedID(avtaleID);

        navnTextView.setText("Avtale navn: " + avtale.getAvtaleNavn());
        datoTextView.setText("Dato: " + avtale.getAvtaleDato());
        klokkeslettTextView.setText("Klokkeslett: " + avtale.getAvtaleKlokkeslett());
        treffstedTextView.setText("Treffsted: " + avtale.getAvtaleTreffsted());

        builder.setView(view)
                .setTitle("Avtale info")
                .setPositiveButton("Slett", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        callback.onSlettClick(avtaleID);
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


