package ru.av3969.stickerscollector.ui.editcoll;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

public class AlertDialogFragment extends DialogFragment {
    public AlertDialogFragment() {
    }

    public static AlertDialogFragment newInstance() {
        AlertDialogFragment dialogFragment = new AlertDialogFragment();
        return dialogFragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setTitle("Test title");
        alertDialogBuilder.setMessage("Some stickers have negative balance");
        alertDialogBuilder.setPositiveButton("Ok", (dialog, which) -> dialog.dismiss());
        return alertDialogBuilder.create();
    }
}
