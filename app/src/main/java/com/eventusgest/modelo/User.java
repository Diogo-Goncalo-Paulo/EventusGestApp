package com.eventusgest.modelo;

public class User {
    int id, currentEvent, accessPoint;
    String username, displayName, eventName, accessPointName, role;


    public User(int id, int currentEvent, String eventName, int accessPoint, String accessPointName, String username, String displayName, String role) {
        this.id = id;
        this.currentEvent = currentEvent;
        this.accessPoint = accessPoint;
        this.username = username;
        this.displayName = displayName;
        this.eventName = eventName;
        this.accessPointName = accessPointName;
        this.role = role;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getAccessPointName() {
        return accessPointName;
    }

    public void setAccessPointName(String accessPointName) {
        this.accessPointName = accessPointName;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCurrentEvent() {
        return currentEvent;
    }

    public void setCurrentEvent(int currentEvent) {
        this.currentEvent = currentEvent;
    }

    public int getAccessPoint() {
        return accessPoint;
    }

    public void setAccessPoint(int accessPoint) {
        this.accessPoint = accessPoint;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }
}
