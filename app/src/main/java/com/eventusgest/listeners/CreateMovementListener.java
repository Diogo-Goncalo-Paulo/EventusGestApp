package com.eventusgest.listeners;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONObject;

public interface CreateMovementListener {
    void onSearchUCID(String UCID);
    void onGetAccessPoint(JSONObject accessPoint);
    void onGetArea(JSONArray area);
    void onCreateMovement();
}
