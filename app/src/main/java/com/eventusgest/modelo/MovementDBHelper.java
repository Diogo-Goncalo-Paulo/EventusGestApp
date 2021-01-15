package com.eventusgest.modelo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class MovementDBHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "eventusgest";
    private static final int DB_VERSION = 1;
    private static final String TABLE_MOVEMENTS = "movements";
    private static final String ID_MOVEMENT = "id";
    private static final String TIME = "time";
    private static final String ID_CREDENTIAL = "idCredential";
    private static final String ID_ACCESSPOINT = "idAccessPoint";
    private static final String ID_AREA_FROM = "idAreaFrom";
    private static final String ID_AREA_TO = "idAreaTo";
    private static final String ID_USER = "idUser";


    private final SQLiteDatabase db;

    public MovementDBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        this.db = getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sqlCreateTableMovement =
                "CREATE TABLE " + TABLE_MOVEMENTS + " ( " +
                        ID_MOVEMENT + " INTEGER PRIMARY KEY, " +
                        TIME + " INTEGER NOT NULL, " +
                        ID_CREDENTIAL + " INTEGER NOT NULL, " +
                        ID_ACCESSPOINT + " INTEGER NOT NULL, " +
                        ID_AREA_FROM + " INTEGER NOT NULL, " +
                        ID_AREA_TO + " INTEGER NOT NULL, " +
                        ID_USER + " INTEGER NOT NULL);";
        db.execSQL(sqlCreateTableMovement);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sqlCreateTableMovement = "DROP TABLE IF EXISTS " + TABLE_MOVEMENTS;
        db.execSQL(sqlCreateTableMovement);
        this.onCreate(db);
    }

    public Movement addMovementDb (Movement movement) {
        ContentValues values = new ContentValues();
        values.put(ID_MOVEMENT, movement.getId());
        values.put(TIME, movement.getTime());
        values.put(ID_CREDENTIAL, movement.getIdCredential());
        values.put(ID_ACCESSPOINT, movement.getIdAccessPoint());
        values.put(ID_AREA_FROM, movement.getIdAreaFrom());
        values.put(ID_AREA_TO, movement.getIdAreaTo());
        values.put(ID_USER, movement.getIdUser());

        long id = this.db.insert(TABLE_MOVEMENTS, null, values);

        if(id > -1)
            movement.setId((int) id);
        return movement;
    }

    public Movement editMovementDb (Movement movement) {
        ContentValues values = new ContentValues();
        values.put(ID_MOVEMENT, movement.getId());
        values.put(TIME, movement.getTime());
        values.put(ID_CREDENTIAL, movement.getIdCredential());
        values.put(ID_ACCESSPOINT, movement.getIdAccessPoint());
        values.put(ID_AREA_FROM, movement.getIdAreaFrom());
        values.put(ID_AREA_TO, movement.getIdAreaTo());
        values.put(ID_USER, movement.getIdUser());

        this.db.update(TABLE_MOVEMENTS, values, ID_MOVEMENT+"="+movement.getId(),null);
        return movement;
    }

    public void removeAllMovements() {
        this.db.delete(TABLE_MOVEMENTS,null, null);
    }

    public ArrayList<Movement> getAllMovementsDB() {
        ArrayList<Movement> movements = new ArrayList<>();
        Cursor cursor = this.db.query(TABLE_MOVEMENTS, new String[]{ID_MOVEMENT,TIME , ID_CREDENTIAL, ID_ACCESSPOINT, ID_AREA_FROM, ID_AREA_TO, ID_USER}, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                Movement auxMovement = new Movement(cursor.getInt(0), cursor.getInt(1), cursor.getInt(2), cursor.getInt(3), cursor.getInt(4), cursor.getInt(5), cursor.getInt(6));
                movements.add(auxMovement);
            }
            while (cursor.moveToNext());
        }
        return movements;
    }
}
