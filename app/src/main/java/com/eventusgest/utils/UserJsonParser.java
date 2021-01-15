package com.eventusgest.utils;

import com.eventusgest.modelo.Credential;
import com.eventusgest.modelo.User;

import org.json.JSONException;
import org.json.JSONObject;

public class UserJsonParser {
    public static User parserJsonUser(String response) {
        User auxUser = null;

        try {
            JSONObject user = new JSONObject(response);
            int id = user.getInt("id");
            String username = user.getString("username");
            String displayName = user.getString("displayName");
            int currentEvent = user.getInt("currentEvent");
            JSONObject event = new JSONObject(user.getString("event"));
            String eventName = event.getString("name");

            String accessPointName = null;
            int idAccessPoint = 0;

            if(user.getString("accessPoint") != "null") {
                JSONObject accessPoint = new JSONObject(user.getString("accessPoint"));
                idAccessPoint = user.getInt("idAccessPoint");
                accessPointName = accessPoint.getString("name");
            }

            String role =  user.getString("role");;
            auxUser = new User(id, currentEvent, eventName, idAccessPoint, accessPointName, username, displayName, role);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return auxUser;
    }
}
