package es.uam.eps.dadm.jorgecifuentes.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import es.uam.eps.dadm.jorgecifuentes.model.Round;
import es.uam.eps.dadm.jorgecifuentes.model.RoundRepository;

import static es.uam.eps.dadm.jorgecifuentes.database.RoundDataBaseSchema.RoundTable;
import static es.uam.eps.dadm.jorgecifuentes.database.RoundDataBaseSchema.UserTable;

/**
 * RoundRepository en forma de base de datos local de rondas.
 *
 * @author Jorge Cifuentes
 */
public class RoundDataBase implements RoundRepository {

    private final String DEBUG_TAG = "DEBUG";

    private static final String DATABASE_NAME = "er.db";
    private static final int DATABASE_VERSION = 1;

    private DatabaseHelper helper;
    private SQLiteDatabase db;

    public RoundDataBase(Context context) {
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
            db.execSQL("DROP TABLE IF EXISTS " + UserTable.NAME);
            db.execSQL("DROP TABLE IF EXISTS " + RoundTable.NAME);
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
            try {
                db.execSQL(str1);
                db.execSQL(str2);
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
            callback.onError("A player with this name already exists!"); //TODO string
        } else {

            long id = this.db.insert(UserTable.NAME, null, values);
            if (id < 0)
                callback.onError("Error inserting new player named " + playername);
            else
                callback.onLogin(uuid);
        }
    }

    @Override
    public void getRounds(String playeruuid, String orderByField, String group, RoundsCallback callback) {

        List<Round> rounds = new ArrayList<>();
        RoundCursorWrapper cursor = queryRounds();

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Round round = cursor.getRound();

            if (round.getPlayerUUID().equals(playeruuid))
                rounds.add(round);
            cursor.moveToNext();
        }

        cursor.close();

        if (cursor.getCount() > 0)
            callback.onResponse(rounds);
        else
            callback.onError("No rounds found in database");
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

        long id = this.db.insert(RoundTable.NAME, null, values);

        if (callback != null)
            callback.onResponse(id >= 0);
    }

    @Override
    public void updateRound(Round round, BooleanCallback callback) {

        ContentValues values = this.getContentValues(round);

        long id = this.db.update(RoundTable.NAME, values, RoundTable.Cols.ROUNDUUID + " = ?", new String[]{round.getRoundUUID()});
        if (callback != null)
            callback.onResponse(id >= 0);
    }

    @Override
    public void removeRound(Round round, BooleanCallback callback) {
        long id = this.db.delete(RoundTable.NAME, RoundTable.Cols.ROUNDUUID + " = ?", new String[]{round.getRoundUUID()});

        if (callback != null)
            callback.onResponse(id >= 0);
    }

    @Override
    public void updateUser(String userUUID, String name, String password) { //TODO callback?

        ContentValues values = this.getContentValues(userUUID, name, password);

        long id = this.db.update(UserTable.NAME, values, UserTable.Cols.PLAYERUUID + " = ?", new String[]{userUUID}); //TODO que pasa con password???
        Log.d("DEBUG", "updateUser: " + id);
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

    private ContentValues getContentValues(String userUUID, String name, String password) {

        ContentValues values = new ContentValues();

/*
        String sql = "SELECT " + UserTable.Cols.PLAYERPASSWORD + " " +
                "FROM " + UserTable.NAME + " " +
                "WHERE " + UserTable.Cols.PLAYERUUID + "='" +
                userUUID + "';";

        Cursor cursor = db.rawQuery(sql, null);
        cursor.moveToFirst();
        password = cursor.getString(0);

*/
        if (name != null) values.put(UserTable.Cols.PLAYERNAME, name);
        if (password != null) values.put(UserTable.Cols.PLAYERPASSWORD, password);

        return values;
    }
}