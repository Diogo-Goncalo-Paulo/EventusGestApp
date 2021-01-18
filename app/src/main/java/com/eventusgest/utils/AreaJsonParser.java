package com.eventusgest.utils;

import com.eventusgest.modelo.Area;
import com.eventusgest.modelo.Credential;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class AreaJsonParser {
    public static ArrayList<Area> parserJsonAreas(JSONArray response) {
        ArrayList<Area> areas = new ArrayList<>();

        if (response != null) {
            for (int i = 0; i < response.length(); i++) {
                try {
                    JSONObject area = (JSONObject) response.get(i);
                    int id = area.getInt("id");
                    String name = area.getString("name");

                    Area a = new Area(id,name);
                    areas.add(a);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        return areas;
    }
}
