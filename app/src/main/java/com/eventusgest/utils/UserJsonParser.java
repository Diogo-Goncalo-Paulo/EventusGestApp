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
            String eventName = user.getString("event.name");
            int idAccessPoint = user.getInt("idAccessPoint");
            String accessPointName = user.getString("accessPoint.name");
            String role = "bruh";

            auxUser = new User(id, currentEvent, eventName, idAccessPoint, accessPointName, username, displayName, role);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return auxUser;
    }
}
