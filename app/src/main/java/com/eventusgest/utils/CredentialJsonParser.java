package com.eventusgest.utils;

import com.eventusgest.modelo.Credential;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class CredentialJsonParser {
    public static ArrayList<Credential> parserJsonCredentials(JSONArray response) {
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

                    String carrierName = null;
                    String carrierPhoto = null;
                    String carrierInfo = null;
                    String carrierTypeName = null;

                    if(credential.getString("carrier") != "null") {
                        JSONObject carrier = new JSONObject(credential.getString("carrier"));
                        carrierName = carrier.getString("name");
                        carrierPhoto = carrier.getString("photo");
                        carrierInfo = carrier.getString("info");
                        JSONObject carrierType = new JSONObject(carrier.getString("carrierType"));
                        carrierTypeName = carrierType.getString("name");
                    }

                    JSONObject entity = new JSONObject(credential.getString("entity"));
                    String entityName = entity.getString("name");

                    Credential c = new Credential(id, idEntity, idCurrentArea, idEvent, flagged, blocked, ucid, carrierName, carrierTypeName, carrierPhoto, entityName, carrierInfo);
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

            String carrierName = null;
            String carrierPhoto = null;
            String carrierInfo = null;
            String carrierTypeName = null;

            if(credential.getString("carrier") != "null") {
                JSONObject carrier = new JSONObject(credential.getString("carrier"));
                carrierName = carrier.getString("name");
                carrierPhoto = carrier.getString("photo");
                carrierInfo = carrier.getString("info");
                JSONObject carrierType = new JSONObject(carrier.getString("carrierType"));
                carrierTypeName = carrierType.getString("name");
            }

            JSONObject entity = new JSONObject(credential.getString("entity"));
            String entityName = entity.getString("name");

            Credential c = new Credential(id, idEntity, idCurrentArea, idEvent, flagged, blocked, ucid, carrierName, carrierTypeName, carrierPhoto, entityName, carrierInfo);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return auxCredential;
    }

}
