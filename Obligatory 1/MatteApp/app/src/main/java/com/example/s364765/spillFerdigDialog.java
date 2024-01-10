package com.example.s364765;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;

public class spillFerdigDialog extends DialogFragment {
    private SpillFerdigInterface callback;

    public interface SpillFerdigInterface {
        public void onHjemClick();


    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            callback = (SpillFerdigInterface) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException("Kallende klasse må implementere interfacet!");
        }
    }

    @Override
    public android.app.Dialog onCreateDialog(Bundle savedInstanceState) {
        return new
                AlertDialog.Builder(getActivity()).setTitle(R.string.spillFerdigDialogTittel).setPositiveButton(R.string.hjem, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        callback.onHjemClick();
                    }
                })
                .create();
    }
}

