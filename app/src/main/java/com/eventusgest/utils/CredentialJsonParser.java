package com.eventusgest.utils;

import com.eventusgest.modelo.Credential;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class CredentialJsonParser {
    public static ArrayList<Credential> parserJsonCredential(JSONArray response) {
        ArrayList<Credential> credentials = new ArrayList<>();

        if (response != null) {
            for (int i = 0; i < response.length(); i++) {
                try {
                    JSONObject credential = (JSONObject) response.get(i);
                    int id = credential.getInt("id");
                    String ucid = credential.getString("ucid");
                    int idEntity = credential.getInt("idEntity");
                    int idEvent = credential.getInt("idEvent");
                    int idCurrentArea = credential.getInt("idCurrentArea");
                    int flagged = credential.getInt("flagged");
                    int blocked = credential.getInt("blocked");


                    Credential c = new Credential(id, idEntity, idCurrentArea, idEvent, flagged, blocked, ucid);
                    credentials.add(c);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        return credentials;
    }

    public static Credential parserJsonCredential(String response) {
        Credential auxCredential = null;

        try {
            JSONObject credential = new JSONObject(response);
            int id = credential.getInt("id");
            String ucid = credential.getString("ucid");
            int idEntity = credential.getInt("idEntity");
            int idEvent = credential.getInt("idEvent");
            int idCurrentArea = credential.getInt("idCurrentArea");
            int flagged = credential.getInt("flagged");
            int blocked = credential.getInt("blocked");

            auxCredential = new Credential(id, idEntity, idCurrentArea, idEvent, flagged, blocked, ucid);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return auxCredential;
    }

}
