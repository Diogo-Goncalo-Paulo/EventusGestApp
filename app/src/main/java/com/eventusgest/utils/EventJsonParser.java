package com.eventusgest.utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class EventJsonParser {
    public static String[] parserJsonEventNames(JSONArray response) {
        String[] events = new String[response.length()];

        if (response != null) {
            for (int i = 0; i < response.length(); i++) {
                try {
                    JSONObject event = (JSONObject) response.get(i);
                    String name = event.getString("name");
                    events[i] = name;

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        return events;
    }
}
