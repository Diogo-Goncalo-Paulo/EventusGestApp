package com.eventusgest.listeners;

import org.json.JSONArray;

public interface AreasLeftListener {
    void onGetEvents(JSONArray events);
    void onUpdatedEvent(String event);
}
