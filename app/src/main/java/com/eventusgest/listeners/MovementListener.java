package com.eventusgest.listeners;

import com.eventusgest.modelo.Movement;

import java.util.ArrayList;

public interface MovementListener {
    void onRefreshMovementList(ArrayList<Movement> movementList);
}
