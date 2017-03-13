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
 * Clase que representa una alerta dentro de un fragmento.
 *
 * @author Jorge Cifuentes
 */
public class AlertDialogFragment extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final AppCompatActivity activity = (AppCompatActivity) getActivity();
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setTitle(R.string.game_over);
        alertDialogBuilder.setMessage(R.string.game_over_message);

        // Boton positivo.
        alertDialogBuilder.setPositiveButton(R.string.positive,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        Round round = new Round(RoundRepository.SIZE);
                        RoundRepository.get(getActivity()).addRound(round);

                        if (activity instanceof RoundListActivity)
                            ((RoundListActivity) activity).onRoundUpdated(round);
                        else
                            ((RoundActivity) activity).finish();
                        dialog.dismiss();

                    }
                }
        );

        // Boton negativo.
        alertDialogBuilder.setNegativeButton(R.string.negative, new DialogInterface.OnClickListener() {
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