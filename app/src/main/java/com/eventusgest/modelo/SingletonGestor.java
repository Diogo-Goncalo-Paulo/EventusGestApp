package com.eventusgest.modelo;

import android.content.Context;

import java.util.ArrayList;

public class SingletonGestor {
    private static SingletonGestor instance = null;
    private ArrayList<Credential> credentials;
    private CredentialDBHelper credentialsDB = null;

    public static synchronized SingletonGestor getInstance(Context context) {
        if (instance == null) {
            instance = new SingletonGestor(context);
        }
        return instance;
    }

    public SingletonGestor(Context context) {
        //gerarFakeData();
        credentials = new ArrayList<>();
        credentialsDB = new CredentialDBHelper(context);
    }

    private void gerarFakeData () {
        credentials = new ArrayList<>();

        credentials.add(new Credential(1, 1, 1, 1, 0, 0, "dasqwe21"));
        credentials.add(new Credential(2, 1, 1, 1, 0, 0, "qqqqwe21"));
        credentials.add(new Credential(3, 1, 1, 1, 0, 0, "ewqs23fd"));
        credentials.add(new Credential(4, 1, 1, 1, 0, 0, "zserf234"));
        credentials.add(new Credential(5, 1, 1, 1, 0, 0, "vcxz1234"));
    }

    public Credential getCredential (int id) {
        for (Credential c : credentials)
            if(c.getId() == id)
                return c;
        return null;
    }

    public ArrayList<Credential> getAllCredentialsDB() {
        credentials = credentialsDB.getAllCredentialsDB();
        return credentials;
    }

    public void addCredentialBD (Credential credential) {
        credentialsDB.addCredentialDb(credential);
    }

    public ArrayList<Credential> getAllCredentials () {
        return new ArrayList<>(credentials);
    }
}
