package com.eventusgest.modelo;

import androidx.annotation.NonNull;

import org.json.JSONArray;

import java.util.ArrayList;

public class AccessPoint {
    int id;
    String name;
    int[] areas;

    public AccessPoint(int id, String name, int[] areas) {
        this.id = id;
        this.name = name;
        this.areas = areas;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int[] getAreas() {
        return areas;
    }

    public void setAreas(int[] areas) {
        this.areas = areas;
    }
}
