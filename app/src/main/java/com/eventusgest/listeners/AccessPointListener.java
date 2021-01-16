package com.eventusgest.listeners;

import android.content.Context;

import org.json.JSONArray;

public interface AccessPointListener {
    void onGetAccessPoints(JSONArray accessPoints, Context context);
    void onUpdatedAccessPoint(String accessPoint);
}
