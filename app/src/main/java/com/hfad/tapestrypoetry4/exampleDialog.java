package com.hfad.tapestrypoetry4;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class exampleDialog extends DialogFragment {

    private String ans;
    private String thesaurus;

    public exampleDialog(String thesaurus, String ans) {
        this.ans=ans;
        this.thesaurus=thesaurus;

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(thesaurus).setMessage(ans).setPositiveButton("Okay,Got it!", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        return builder.create();
    }
}
