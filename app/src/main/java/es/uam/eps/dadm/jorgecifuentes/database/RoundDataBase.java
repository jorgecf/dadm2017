package es.uam.eps.dadm.jorgecifuentes.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import es.uam.eps.dadm.jorgecifuentes.R;
import es.uam.eps.dadm.jorgecifuentes.model.Round;
import es.uam.eps.dadm.jorgecifuentes.model.RoundRepository;
import es.uam.eps.dadm.jorgecifuentes.model.Triplet;

import static es.uam.eps.dadm.jorgecifuentes.database.RoundDataBaseSchema.RoundTable;
import static es.uam.eps.dadm.jorgecifuentes.database.RoundDataBaseSchema.UserTable;
import static es.uam.eps.dadm.jorgecifuentes.database.RoundDataBaseSchema.ScoresTable;

/**
 * RoundRepository en forma de base de datos local de rondas.
 *
 * @author Jorge Cifuentes
 */
public class RoundDataBase implements RoundRepository {

    private final String DEBUG_TAG = "DEBUG";

    private static final String DATABASE_NAME = "er.db";
    private static final int DATABASE_VERSION = 2;
    private Context context;
    /**
     * @TODO darg como os comenté en clase, hay que llamar a close() en onDestroy() de la actividad que abre la conexión con la base de datos
     */
    private DatabaseHelper helper;
    private SQLiteDatabase db;

    public RoundDataBase(Context context) {
        this.context = context;
        this.helper = new DatabaseHelper(context);
    }

    /**
     * Clase auxiliar para el creado y actualizacion de la base de datos.
     */
    private static class DatabaseHelper extends SQLiteOpenHelper {

        public DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            createTable(db);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

            String[] tables = {UserTable.NAME, RoundTable.NAME, ScoresTable.NAME};

            for (String table : tables)
                db.execSQL("DROP TABLE IF EXISTS " + table);

            createTable(db);
        }

        /**
         * Crea las tablas de la base de datos.
         *
         * @param db Base de datos SQLite.
         */
        private void createTable(SQLiteDatabase db) {

            String str1 = "CREATE TABLE " + UserTable.NAME + " ("
                    + "_id integer primary key autoincrement, "
                    + UserTable.Cols.PLAYERUUID + " TEXT UNIQUE, "
                    + UserTable.Cols.PLAYERNAME + " TEXT UNIQUE, "
                    + UserTable.Cols.PLAYERPASSWORD + " TEXT);";

            String str2 = "CREATE TABLE " + RoundTable.NAME + " ("
                    + "_id integer primary key autoincrement, "
                    + RoundTable.Cols.ROUNDUUID + " TEXT UNIQUE, "
                    + RoundTable.Cols.PLAYERUUID + " TEXT REFERENCES " + UserTable.Cols.PLAYERUUID + ", "
                    + RoundTable.Cols.DATE + " TEXT, "
                    + RoundTable.Cols.TITLE + " TEXT, "
                    + RoundTable.Cols.SIZE + " TEXT, "
                    + RoundTable.Cols.BOARD + " TEXT);";

            String str3 = "CREATE TABLE " + ScoresTable.NAME + " ("
                    + "_id integer primary key autoincrement, "
                    + ScoresTable.Cols.ROUNDUUID + " TEXT UNIQUE, " /* foreign keys ---> https://sqlite.org/foreignkeys.html  */
                    + ScoresTable.Cols.BLACKSCORE + " INTEGER, "
                    + ScoresTable.Cols.WHITESCORE + " INTEGER);";

            try {
                db.execSQL(str1);
                db.execSQL(str2);
                db.execSQL(str3);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public void open() throws SQLException {
        this.db = this.helper.getWritableDatabase();
    }


    @Override
    public void close() {
        this.db.close();
    }


    @Override
    public void login(String playername, String playerpassword, LoginRegisterCallback callback) {

        Log.d(DEBUG_TAG, "Login " + playername);

        Cursor cursor = this.db.query(
                UserTable.NAME, // tabla donde buscar
                new String[]{UserTable.Cols.PLAYERUUID}, // columna devuelta
                UserTable.Cols.PLAYERNAME + " = ? AND " + UserTable.Cols.PLAYERPASSWORD + " = ?", // filtro (query en si misma)
                new String[]{playername, playerpassword}, // sustitucion de los ?
                null,
                null,
                null);

        int count = cursor.getCount();
        String uuid = count == 1 && cursor.moveToFirst() ? cursor.getString(0) : "";
        cursor.close();

        if (count == 1)
            callback.onLogin(uuid);
        else
            callback.onError("Username or password incorrect.");
    }

    @Override
    public void register(String playername, String password, LoginRegisterCallback callback) {

        ContentValues values = new ContentValues();

        String uuid = UUID.randomUUID().toString();

        values.put(UserTable.Cols.PLAYERUUID, uuid);
        values.put(UserTable.Cols.PLAYERNAME, playername);
        values.put(UserTable.Cols.PLAYERPASSWORD, password);

        // Comprobamos si el nombre de usuario ya existe.
        Cursor cursor = this.db.query(
                UserTable.NAME, // tabla donde buscar
                new String[]{UserTable.Cols.PLAYERUUID}, // columna devuelta
                UserTable.Cols.PLAYERNAME + " = ?", // filtro (query en si misma)
                new String[]{playername}, // sustitucion de los ?
                null,
                null,
                null);


        if (cursor.getCount() != 0) {
            callback.onError(this.context.getString(R.string.username_already_exists));
        } else {

            long id = this.db.insert(UserTable.NAME, null, values);
            if (id < 0)
                callback.onError(this.context.getString(R.string.error_inserting_user) + playername);
            else
                callback.onLogin(uuid);
        }
    }

    @Override
    public void getRounds(@Nullable String playeruuid, @Nullable String orderByField, @Nullable String group, RoundsCallback<Round> callback) {

        // Obtenemos todas las rondas con su usuario asociado.
        List<Round> rounds = new ArrayList<>();
        RoundCursorWrapper cursor = queryRounds();

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Round round = cursor.getRound();

            if (playeruuid == null) {
                rounds.add(round);
            } else if (round.getPlayerUUID().equals(playeruuid)) {
                rounds.add(round);
            }

            cursor.moveToNext();
        }

        cursor.close();

        if (cursor.getCount() > 0)
            callback.onResponse(rounds);
        else
            callback.onError(this.context.getString(R.string.no_rounds_found));
    }

    @Override
    public void getScores(RoundsCallback<Triplet<String, String, String>> callback) {

        String query = "SELECT " +
                RoundTable.Cols.TITLE + ", " +
                ScoresTable.Cols.BLACKSCORE + ", " +
                ScoresTable.Cols.WHITESCORE + " " +
                "FROM " +
                RoundTable.NAME + " as r, " +
                ScoresTable.NAME + " as s " +
                "WHERE " +
                "r." + RoundTable.Cols.ROUNDUUID + " = " + "s." + ScoresTable.Cols.ROUNDUUID + " " +
                "ORDER BY " + ScoresTable.Cols.BLACKSCORE + " DESC" +
                ";";

        RoundCursorWrapper cursor = new RoundCursorWrapper(db.rawQuery(query, null));
        List<Triplet<String, String, String>> rounds = new ArrayList<>();

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            rounds.add(cursor.getScore());
            cursor.moveToNext();
        }

        cursor.close();

        if (cursor.getCount() > 0)
            callback.onResponse(rounds);
        else
            callback.onError(this.context.getString(R.string.no_rounds_found));

    }

    /**
     * Selecciona las rondas de la base de datos y las devuelve como RoundCursorWrapper.
     *
     * @return RoundCursorWrapper creado.
     */
    private RoundCursorWrapper queryRounds() {

        String sql = "SELECT " + UserTable.Cols.PLAYERNAME + ", " +
                UserTable.Cols.PLAYERUUID + ", " +
                RoundTable.Cols.ROUNDUUID + ", " +
                RoundTable.Cols.DATE + ", " +
                RoundTable.Cols.TITLE + ", " +
                RoundTable.Cols.SIZE + ", " +
                RoundTable.Cols.BOARD + " " +
                "FROM " + UserTable.NAME + " AS p, " +
                RoundTable.NAME + " AS r " +
                "WHERE " + "p." + UserTable.Cols.PLAYERUUID + "=" +
                "r." + RoundTable.Cols.PLAYERUUID + ";";

        Cursor cursor = db.rawQuery(sql, null);
        return new RoundCursorWrapper(cursor);
    }


    @Override
    public void addRound(Round round, BooleanCallback callback) {

        ContentValues values = this.getContentValues(round);
        ContentValues score = this.getContentScores(round);

        // Creamos la ronda en la tabla de rondas.
        long id = this.db.insert(RoundTable.NAME, null, values);

        // Tambien creamos su entrada en la tabla de scores.
        long id2 = this.db.insert(ScoresTable.NAME, null, score);

        if (callback != null)
            callback.onResponse(id >= 0 && id2 >= 0);
    }

    @Override
    public void updateRound(Round round, BooleanCallback callback) {

        ContentValues values = this.getContentValues(round);
        ContentValues score = this.getContentScores(round);

        String[] whereArgs = new String[]{round.getRoundUUID()};

        // Actualizamos el contenido de la ronda.
        long id = this.db.update(RoundTable.NAME, values, RoundTable.Cols.ROUNDUUID + " = ?", whereArgs);

        // Actualizamos el score.
        long id2 = this.db.update(ScoresTable.NAME, score, ScoresTable.Cols.ROUNDUUID + " = ?", whereArgs);

        if (callback != null)
            callback.onResponse(id >= 0 && id2 >= 0);

    }

    @Override
    public void removeRound(Round round, BooleanCallback callback) {

        String[] whereArgs = new String[]{round.getRoundUUID()};

        // Borramos la ronda.
        long id = this.db.delete(RoundTable.NAME, RoundTable.Cols.ROUNDUUID + " = ?", whereArgs);

        // Borramos su entrada en la tabla de scores.
        long id2 = this.db.delete(ScoresTable.NAME, ScoresTable.Cols.ROUNDUUID + " = ?", whereArgs);

        if (callback != null)
            callback.onResponse(id >= 0 && id2 >= 0);
    }

    @Override
    public void updateUser(String userUUID, String name, String password, BooleanCallback callback) {

        ContentValues values = this.getContentValues(name, password);

        long id;
        try {
            id = this.db.update(UserTable.NAME, values, UserTable.Cols.PLAYERUUID + " = ?", new String[]{userUUID});
        } catch (SQLiteConstraintException e) {
            Log.d("[debug]", "id already exists " + name + ", " + password);
            id = -1;
        }

        if (callback != null)
            callback.onResponse(id > 0);
    }

    private ContentValues getContentValues(Round round) {

        ContentValues values = new ContentValues();

        values.put(RoundTable.Cols.PLAYERUUID, round.getPlayerUUID());
        values.put(RoundTable.Cols.ROUNDUUID, round.getRoundUUID());
        values.put(RoundTable.Cols.DATE, round.getDate());
        values.put(RoundTable.Cols.TITLE, round.getTitle());
        values.put(RoundTable.Cols.SIZE, round.getSize());
        values.put(RoundTable.Cols.BOARD, round.getBoard().tableroToString());

        return values;
    }

    private ContentValues getContentScores(Round round) {

        ContentValues values = new ContentValues();

        values.put(ScoresTable.Cols.ROUNDUUID, round.getRoundUUID());
        values.put(ScoresTable.Cols.BLACKSCORE, round.getBlackScore());
        values.put(ScoresTable.Cols.WHITESCORE, round.getWhiteScore());

        return values;
    }

    private ContentValues getContentValues(String name, String password) {

        ContentValues values = new ContentValues();

        if (name != null) values.put(UserTable.Cols.PLAYERNAME, name);
        if (password != null) values.put(UserTable.Cols.PLAYERPASSWORD, password);

        return values;
    }
}