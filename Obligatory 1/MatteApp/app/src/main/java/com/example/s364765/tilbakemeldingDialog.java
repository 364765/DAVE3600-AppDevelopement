package com.example.s364765;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;

public class tilbakemeldingDialog extends DialogFragment {
    private TilbakemeldingInterface callback;

    public interface TilbakemeldingInterface {
        public void onLukkClick();


    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            callback = (TilbakemeldingInterface) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException("Kallende klasse må implementere interfacet!");
        }
    }

    @Override
    public android.app.Dialog onCreateDialog(Bundle savedInstanceState) {
        return new
                AlertDialog.Builder(getActivity()).setTitle(R.string.feilTilbakemelding).setPositiveButton(R.string.lukk, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        callback.onLukkClick();
                    }
                })
                .create();
    }
}

