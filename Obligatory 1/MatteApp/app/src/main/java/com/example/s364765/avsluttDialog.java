package com.example.s364765;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;

public class avsluttDialog extends DialogFragment {
    private AvsluttInterface callback;

    public interface AvsluttInterface {
        public void onJaClick();

        public void onNeiClick();

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            callback = (AvsluttInterface) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException("Kallende klasse må implementere interfacet!");
        }
    }

    @Override
    public android.app.Dialog onCreateDialog(Bundle savedInstanceState) {
        return new
                AlertDialog.Builder(getActivity()).setTitle(R.string.avsluttDialogTittel).setPositiveButton(R.string.avsluttOk, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        callback.onJaClick();
                    }
                }).setNegativeButton(R.string.avsluttIkkeOk, new
                        DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                callback.onNeiClick();
                            }
                        })
                .create();
    }
}
