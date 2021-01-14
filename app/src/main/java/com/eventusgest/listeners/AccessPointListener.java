package com.eventusgest.listeners;

import org.json.JSONArray;

public interface AccessPointListener {
    void onGetAccessPoints(JSONArray accessPoints);
}
