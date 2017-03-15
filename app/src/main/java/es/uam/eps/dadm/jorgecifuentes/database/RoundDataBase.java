package es.uam.eps.dadm.jorgecifuentes.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.UUID;

import es.uam.eps.dadm.jorgecifuentes.model.Round;
import es.uam.eps.dadm.jorgecifuentes.model.RoundRepository;

import static es.uam.eps.dadm.jorgecifuentes.database.RoundDataBaseSchema.RoundTable;
import static es.uam.eps.dadm.jorgecifuentes.database.RoundDataBaseSchema.UserTable;


/**
 * Created by jorgecf on 15/03/17.
 */

public class RoundDataBase implements RoundRepository {

    private final String DEBUG_TAG = "DEBUG";

    private static final String DATABASE_NAME = "er.db";
    private static final int DATABASE_VERSION = 1;

    private DatabaseHelper helper;
    private SQLiteDatabase db;


    private static class DatabaseHelper extends SQLiteOpenHelper {

        public DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        public void onCreate(SQLiteDatabase db) {
            createTable(db);
        }


        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + UserTable.NAME);
            db.execSQL("DROP TABLE IF EXISTS " + RoundTable.NAME);
            createTable(db);
        }


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
                UserTable.Cols.PLAYERNAME + " = ? AND " + UserTable.Cols.PLAYERPASSWORD + " = ?", // filtro (query en si)
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

        long id = this.db.insert(UserTable.NAME, null, values);

        if (id < 0)
            callback.onError("Error inserting new player named " + playername);
        else
            callback.onLogin(uuid);
    }

    @Override
    public void getRounds(String playeruuid, String orderByField, String group, RoundsCallback callback) {

    }

    @Override
    public void addRound(Round round, BooleanCallback callback) {

    }

    @Override
    public void updateRound(Round round, BooleanCallback callback) {

    }
}
