package com.eventusgest.listeners;

import com.eventusgest.modelo.Movement;

import org.json.JSONObject;

import java.util.ArrayList;

public interface ChangeMovementListener {
    void onUpdateMovement(JSONObject movement);
    void onDeleteMovement();
}
