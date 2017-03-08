package es.uam.eps.dadm.jorgecifuentes.controller;


import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import es.uam.eps.dadm.jorgecifuentes.R;
import es.uam.eps.dadm.jorgecifuentes.activities.RoundActivity;
import es.uam.eps.dadm.jorgecifuentes.activities.RoundListActivity;
import es.uam.eps.dadm.jorgecifuentes.model.Round;
import es.uam.eps.dadm.jorgecifuentes.model.RoundRepository;

/**
 * Created by jorgecf on 28/02/17.
 */

public class AlertDialogFragment extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final AppCompatActivity activity = (AppCompatActivity) getActivity();
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setTitle(R.string.game_over);
        alertDialogBuilder.setMessage(R.string.game_over_message);


        alertDialogBuilder.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        Round round = new Round();
                        RoundRepository.get(getActivity()).addRound(round);

                        if (activity instanceof RoundListActivity)
                            ((RoundListActivity) activity).onRoundUpdated(round);
                        else
                            ((RoundActivity) activity).finish();
                        dialog.dismiss();

                    }
                }
        );


        alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if (activity instanceof RoundActivity)
                            activity.finish();
                        dialog.dismiss();
                    }
                }

        );

        return alertDialogBuilder.create();
    }
}