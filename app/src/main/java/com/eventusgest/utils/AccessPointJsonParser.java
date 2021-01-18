package com.eventusgest.utils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.eventusgest.modelo.AccessPoint;
import com.eventusgest.modelo.Area;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class AccessPointJsonParser {
    public static AccessPoint parserJsonAccesspoint(JSONObject response) {
        AccessPoint accessPoint = null;
        if (response != null) {
            try {
                JSONObject accesspoint = (JSONObject) response;
                int id = accesspoint.getInt("id");
                String name = accesspoint.getString("name");

                JSONArray arr = accesspoint.getJSONArray("areas");
                int[] idAreas = new int[arr.length()];

                for(int i = 0; i < arr.length(); i++){
                    idAreas[i] = arr.getInt(i);
                }


                accessPoint = new AccessPoint(id,name,idAreas);

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        return accessPoint;
    }


}
