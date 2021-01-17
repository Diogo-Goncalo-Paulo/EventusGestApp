package com.eventusgest.modelo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;

public class DBHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "eventusgest";
    private static final int DB_VERSION = 5;

    private static final String TABLE_CREDENTIALS = "credentials";
    private static final String ID_CREDENTIAL = "id";
    private static final String UCID_CREDENTIAL = "ucid";
    private static final String ID_ENTITY_CREDENTIAL = "idEntity";
    private static final String ID_CURRENTAREA_CREDENTIAL = "idCurrentArea";
    private static final String ID_EVENT_CREDENTIAL = "idEvent";
    private static final String FLAGGED_CREDENTIAL = "flagged";
    private static final String BLOCKED_CREDENTIAL = "blocked";
    private static final String CARRIER_NAME = "carrierName";
    private static final String CARRIER_PHOTO = "carrierPhoto";
    private static final String CARRIER_INFO = "carrierInfo";
    private static final String ENTITY_NAME = "entityName";
    private static final String CARRIER_TYPE_NAME = "carrierTypeName";
    private static final String QR_CODE = "qrCode";
    private static final String ENTITY_TYPE_NAME = "entityTypeName";
    private static final String CURRENT_AREA_NAME = "currentAreaName";

    private static final String TABLE_MOVEMENTS = "movements";
    private static final String ID_MOVEMENT = "id";
    private static final String TIME = "time";
    private static final String ID_CREDENTIAL_MOV = "idCredential";
    private static final String ID_ACCESSPOINT = "idAccessPoint";
    private static final String ID_AREA_FROM = "idAreaFrom";
    private static final String ID_AREA_TO = "idAreaTo";
    private static final String ID_USER = "idUser";
    private static final String ID_EVENT_MOVEMENT = "idEvent";
    private static final String NAME_AREA_FROM = "nameAreaFrom";
    private static final String NAME_AREA_TO = "nameAreaTo";
    private static final String NAME_ACCESSPOINT = "nameAccessPoint";
    private static final String NAME_USER = "nameUser";
    private static final String NAME_CREDENTIAL = "nameCredential";
    private static final String LAST_MOVEMENT = "lastMovement";

    private final SQLiteDatabase db;

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        this.db = getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sqlCreateTableCredential =
                "CREATE TABLE " + TABLE_CREDENTIALS + " ( " +
                        ID_CREDENTIAL + " INTEGER PRIMARY KEY, " +
                        ID_ENTITY_CREDENTIAL + " INTEGER NOT NULL, " +
                        ID_CURRENTAREA_CREDENTIAL + " INTEGER NOT NULL, " +
                        ID_EVENT_CREDENTIAL + " INTEGER NOT NULL, " +
                        FLAGGED_CREDENTIAL + " INTEGER NOT NULL, " +
                        BLOCKED_CREDENTIAL + " INTEGER NOT NULL, " +
                        UCID_CREDENTIAL + " TEXT NOT NULL, " +
                        CARRIER_NAME + " TEXT, " +
                        CARRIER_PHOTO + " TEXT, " +
                        ENTITY_NAME + " TEXT, " +
                        CARRIER_TYPE_NAME + " TEXT, " +
                        CARRIER_INFO + " TEXT, " +
                        QR_CODE + " TEXT, " +
                        ENTITY_TYPE_NAME + " TEXT, " +
                        CURRENT_AREA_NAME + " TEXT); ";

        String sqlCreateTableMovement =
                "CREATE TABLE " + TABLE_MOVEMENTS + " ( " +
                        ID_MOVEMENT + " INTEGER PRIMARY KEY, " +
                        ID_CREDENTIAL_MOV + " INTEGER NOT NULL, " +
                        ID_ACCESSPOINT + " INTEGER NOT NULL, " +
                        ID_AREA_FROM + " INTEGER NOT NULL, " +
                        ID_AREA_TO + " INTEGER NOT NULL, " +
                        ID_USER + " INTEGER NOT NULL, " +
                        ID_EVENT_MOVEMENT + " INTEGER NOT NULL, " +
                        TIME + " TEXT NOT NULL, " +
                        NAME_AREA_FROM + " TEXT NOT NULL, " +
                        NAME_AREA_TO + " TEXT NOT NULL, " +
                        NAME_ACCESSPOINT + " TEXT NOT NULL, " +
                        NAME_USER + " TEXT NOT NULL, " +
                        NAME_CREDENTIAL + " TEXT NOT NULL, " +
                        LAST_MOVEMENT + " INTEGER NOT NULL); ";

        db.execSQL(sqlCreateTableMovement);
        db.execSQL(sqlCreateTableCredential);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sqlDropTableCredential = "DROP TABLE IF EXISTS " + TABLE_CREDENTIALS;
        db.execSQL(sqlDropTableCredential);
        String sqlCreateTableMovement = "DROP TABLE IF EXISTS " + TABLE_MOVEMENTS;
        db.execSQL(sqlCreateTableMovement);
        this.onCreate(db);
    }

    public Credential addCredentialDb (Credential credential) {
        ContentValues values = new ContentValues();
        values.put(ID_CREDENTIAL, credential.getId());
        values.put(ID_ENTITY_CREDENTIAL, credential.getIdEntity());
        values.put(ID_CURRENTAREA_CREDENTIAL, credential.getIdCurrentArea());
        values.put(ID_EVENT_CREDENTIAL, credential.getIdEvent());
        values.put(FLAGGED_CREDENTIAL, credential.getFlagged());
        values.put(BLOCKED_CREDENTIAL, credential.getBlocked());
        values.put(UCID_CREDENTIAL, credential.getUcid());
        values.put(CARRIER_NAME, credential.getCarrierName());
        values.put(CARRIER_PHOTO, credential.getCarrierPhoto());
        values.put(ENTITY_NAME, credential.getEntityName());
        values.put(CARRIER_TYPE_NAME, credential.getCarrierType());
        values.put(CARRIER_INFO, credential.getCarrierInfo());
        values.put(QR_CODE, credential.getQrCode());
        values.put(ENTITY_TYPE_NAME, credential.getEntityTypeName());
        values.put(CURRENT_AREA_NAME, credential.getCurrentAreaName());

        long id = this.db.insert(TABLE_CREDENTIALS, null, values);

        if(id > -1)
            credential.setID((int) id);
        return credential;
    }

    public void removeAllCredentials() {
        this.db.delete(TABLE_CREDENTIALS,null, null);
    }

    public ArrayList<Credential> getAllCredentialsDB() {
        ArrayList<Credential> credentials = new ArrayList<>();
        Cursor cursor = this.db.query(TABLE_CREDENTIALS, new String[]{ID_CREDENTIAL, ID_ENTITY_CREDENTIAL, ID_CURRENTAREA_CREDENTIAL, ID_EVENT_CREDENTIAL, FLAGGED_CREDENTIAL, BLOCKED_CREDENTIAL, UCID_CREDENTIAL, CARRIER_NAME, CARRIER_PHOTO, ENTITY_NAME, CARRIER_TYPE_NAME, CARRIER_INFO, QR_CODE, ENTITY_TYPE_NAME, CURRENT_AREA_NAME}, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                Credential auxCredential = new Credential(cursor.getInt(0), cursor.getInt(1), cursor.getInt(2), cursor.getInt(3), cursor.getInt(4), cursor.getInt(5), cursor.getString(6), cursor.getString(7), cursor.getString(10), cursor.getString(8), cursor.getString(9), cursor.getString(11), cursor.getString(12), cursor.getString(13), cursor.getString(14));
                credentials.add(auxCredential);
            }
            while (cursor.moveToNext());
        }

        return credentials;
    }

    public Movement addMovementDb (Movement movement) {
        ContentValues values = new ContentValues();
        values.put(ID_MOVEMENT, movement.getId());
        values.put(ID_CREDENTIAL_MOV, movement.getIdCredential());
        values.put(ID_ACCESSPOINT, movement.getIdAccessPoint());
        values.put(ID_AREA_FROM, movement.getIdAreaFrom());
        values.put(ID_AREA_TO, movement.getIdAreaTo());
        values.put(ID_USER, movement.getIdUser());
        values.put(ID_EVENT_MOVEMENT, movement.getIdEvent());
        values.put(TIME, movement.getTime());
        values.put(NAME_AREA_FROM, movement.getNameAreaFrom());
        values.put(NAME_AREA_TO, movement.getNameAreaTo());
        values.put(NAME_ACCESSPOINT, movement.getNameAccessPoint());
        values.put(NAME_USER, movement.getNameUser());
        values.put(NAME_CREDENTIAL, movement.getNameCredential());
        values.put(LAST_MOVEMENT, movement.getLastMovement());

        long id = this.db.insert(TABLE_MOVEMENTS, null, values);

        if(id > -1)
            movement.setId((int) id);
        return movement;
    }

    public void removeAllMovements() {
        this.db.delete(TABLE_MOVEMENTS,null, null);
    }

    public ArrayList<Movement> getAllMovementsDB() {
        ArrayList<Movement> movements = new ArrayList<>();
        Cursor cursor = this.db.query(TABLE_MOVEMENTS, new String[]{ID_MOVEMENT, ID_CREDENTIAL_MOV, ID_ACCESSPOINT, ID_AREA_FROM, ID_AREA_TO, ID_USER, TIME, NAME_AREA_FROM, NAME_AREA_TO, NAME_ACCESSPOINT, NAME_USER, NAME_CREDENTIAL}, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                Movement auxMovement = new Movement(cursor.getInt(0), cursor.getInt(1), cursor.getInt(2), cursor.getInt(3), cursor.getInt(4), cursor.getInt(5), cursor.getInt(6),cursor.getString(7),cursor.getString(8),cursor.getString(9),cursor.getString(10),cursor.getString(11),cursor.getString(11),cursor.getInt(12));
                movements.add(auxMovement);
            }
            while (cursor.moveToNext());
        }
        return movements;
    }
}
