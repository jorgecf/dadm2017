package es.uam.eps.dadm.jorgecifuentes.database;

import android.database.Cursor;
import android.database.CursorWrapper;
import android.util.Log;

import es.uam.eps.dadm.jorgecifuentes.model.Round;
import es.uam.eps.dadm.jorgecifuentes.model.TableroReversi;
import es.uam.eps.multij.ExcepcionJuego;

import static es.uam.eps.dadm.jorgecifuentes.database.RoundDataBaseSchema.UserTable;
import static es.uam.eps.dadm.jorgecifuentes.database.RoundDataBaseSchema.RoundTable;

/**
 * Created by jorgecf on 15/03/17.
 */

public class RoundCursorWrapper extends CursorWrapper {

    /**
     * Creates a cursor wrapper.
     *
     * @param cursor The underlying cursor to wrap.
     */
    public RoundCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Round getRound() {

        String playername = getString(getColumnIndex(UserTable.Cols.PLAYERNAME));
        String board = getString(getColumnIndex(RoundTable.Cols.BOARD)); //TODO probar
        //TODO date size etc

        Round round = new Round();
        round.setFirstPlayerName("random");
        round.setSecondPlayerName("playername");

        try {
            round.getBoard().stringToTablero(board);
        } catch (ExcepcionJuego e) {
            Log.d("DEBUG", "Error turning string into tablero");
        }
        return round;
    }

}
