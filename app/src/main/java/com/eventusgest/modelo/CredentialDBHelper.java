package com.eventusgest.modelo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;

public class CredentialDBHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "eventusgest";
    private static final int DB_VERSION = 1;
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

    private final SQLiteDatabase db;

    public CredentialDBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        this.db = getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sqlCreateTableCredential =
                "CREATE TABLE " + TABLE_CREDENTIALS + " ( " +
                        ID_CREDENTIAL + " INTEGER PRIMARY KEY, " +
                        UCID_CREDENTIAL + " TEXT NOT NULL, " +
                        ID_ENTITY_CREDENTIAL + " INTEGER NOT NULL, " +
                        ID_CURRENTAREA_CREDENTIAL + " INTEGER NOT NULL, " +
                        ID_EVENT_CREDENTIAL + " INTEGER NOT NULL, " +
                        FLAGGED_CREDENTIAL + " INTEGER NOT NULL, " +
                        BLOCKED_CREDENTIAL + " INTEGER NOT NULL, " +
                        CARRIER_NAME + " TEXT, " +
                        CARRIER_INFO + " TEXT, " +
                        CARRIER_PHOTO + " TEXT, " +
                        ENTITY_NAME + " TEXT, " +
                        CARRIER_TYPE_NAME + " TEXT); ";
        db.execSQL(sqlCreateTableCredential);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sqlDropTableCredential = "DROP TABLE IF EXISTS " + TABLE_CREDENTIALS;
        db.execSQL(sqlDropTableCredential);
        this.onCreate(db);
    }

    public Credential addCredentialDb (Credential credential) {
        ContentValues values = new ContentValues();
        values.put(ID_CREDENTIAL, credential.getId());
        values.put(UCID_CREDENTIAL, credential.getUcid());
        values.put(ID_ENTITY_CREDENTIAL, credential.getIdEntity());
        values.put(ID_CURRENTAREA_CREDENTIAL, credential.getIdCurrentArea());
        values.put(ID_EVENT_CREDENTIAL, credential.getIdEvent());
        values.put(FLAGGED_CREDENTIAL, credential.getFlagged());
        values.put(BLOCKED_CREDENTIAL, credential.getBlocked());
        values.put(CARRIER_NAME, credential.getCarrierName());
        values.put(CARRIER_PHOTO, credential.getCarrierPhoto());
        values.put(CARRIER_INFO, credential.getCarrierInfo());
        values.put(ENTITY_NAME, credential.getEntityName());
        values.put(CARRIER_TYPE_NAME, credential.getCarrierType());

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
        Cursor cursor = this.db.query(TABLE_CREDENTIALS, new String[]{ID_CREDENTIAL, ID_ENTITY_CREDENTIAL, ID_CURRENTAREA_CREDENTIAL, ID_EVENT_CREDENTIAL, FLAGGED_CREDENTIAL, BLOCKED_CREDENTIAL, UCID_CREDENTIAL, CARRIER_NAME, CARRIER_PHOTO, ENTITY_NAME, CARRIER_TYPE_NAME, CARRIER_INFO}, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                Credential auxCredential = new Credential(cursor.getInt(0), cursor.getInt(1), cursor.getInt(2), cursor.getInt(3), cursor.getInt(4), cursor.getInt(5), cursor.getString(6), cursor.getString(7), cursor.getString(8), cursor.getString(9), cursor.getString(10), cursor.getString(11));
                credentials.add(auxCredential);
            }
            while (cursor.moveToNext());
        }
        return credentials;
    }
}
