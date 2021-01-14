package com.eventusgest.listeners;

import org.json.JSONArray;

public interface EventUserListener {
    void onGetEvents(JSONArray events);
}
