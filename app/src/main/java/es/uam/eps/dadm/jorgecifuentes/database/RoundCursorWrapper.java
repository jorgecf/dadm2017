package es.uam.eps.dadm.jorgecifuentes.database;

import android.database.Cursor;
import android.database.CursorWrapper;
import android.util.Log;

import es.uam.eps.dadm.jorgecifuentes.model.Round;
import es.uam.eps.dadm.jorgecifuentes.model.Utils;
import es.uam.eps.multij.ExcepcionJuego;

import static es.uam.eps.dadm.jorgecifuentes.database.RoundDataBaseSchema.UserTable;
import static es.uam.eps.dadm.jorgecifuentes.database.RoundDataBaseSchema.RoundTable;
import static es.uam.eps.dadm.jorgecifuentes.database.RoundDataBaseSchema.ScoresTable;

/**
 * Wrapper para Cursor que efectua operaciones con informacion de la base de datos,
 *
 * @author Jorge Cifuentes
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
        String board = getString(getColumnIndex(RoundTable.Cols.BOARD));
        String rounduuid = getString(getColumnIndex(RoundTable.Cols.ROUNDUUID));
        String date = getString(getColumnIndex(RoundTable.Cols.DATE));
        String title = getString(getColumnIndex(RoundTable.Cols.TITLE));
        String playeruuid = getString(getColumnIndex(UserTable.Cols.PLAYERUUID));

        Round round = new Round(playeruuid, playername);
        round.setDate(date);
        round.setTitle(title);

        round.setRoundUUID(rounduuid);

        try {
            round.getBoard().stringToTablero(board);
        } catch (ExcepcionJuego e) {
            Log.d("DEBUG", "Error turning string into tablero");
        }
        return round;
    }

    public Utils.Triplet<String, String, String> getScore() {
        String title = getString(getColumnIndex(RoundTable.Cols.TITLE));
        String b = getString(getColumnIndex(ScoresTable.Cols.BLACKSCORE));
        String w = getString(getColumnIndex(ScoresTable.Cols.WHITESCORE));

        return new Utils.Triplet<>(title, b, w);
    }

}
