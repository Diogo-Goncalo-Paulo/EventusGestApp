package com.eventusgest.modelo;

import android.content.SharedPreferences;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

public class Movement {

    private int id, idCredential, idAccessPoint, idAreaFrom, idAreaTo, idUser, idEvent,lastMovement;
    private String time, nameAreaFrom, nameAreaTo, nameAccessPoint, nameUser, nameCredential;

    public Movement(int id, int idCredential, int idAccessPoint, int idAreaFrom, int idAreaTo, int idUser, int idEvent, String time, String nameAreaFrom, String nameAreaTo, String nameAccessPoint, String nameUser, String nameCredential, int lastMovement) {
        this.id = id;
        this.idCredential = idCredential;
        this.idAccessPoint = idAccessPoint;
        this.idAreaFrom = idAreaFrom;
        this.idAreaTo = idAreaTo;
        this.idUser = idUser;
        this.idEvent = idEvent;
        this.time = time;
        this.nameAreaFrom = nameAreaFrom;
        this.nameAreaTo = nameAreaTo;
        this.nameAccessPoint = nameAccessPoint;
        this.nameUser = nameUser;
        this.nameCredential = nameCredential;
        this.lastMovement = lastMovement;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdCredential() {
        return idCredential;
    }

    public void setIdCredential(int idCredential) {
        this.idCredential = idCredential;
    }

    public int getIdAccessPoint() {
        return idAccessPoint;
    }

    public void setIdAccessPoint(int idAccessPoint) {
        this.idAccessPoint = idAccessPoint;
    }

    public int getIdAreaFrom() {
        return idAreaFrom;
    }

    public void setIdAreaFrom(int idAreaFrom) {
        this.idAreaFrom = idAreaFrom;
    }

    public int getIdAreaTo() {
        return idAreaTo;
    }

    public void setIdAreaTo(int idAreaTo) {
        this.idAreaTo = idAreaTo;
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public int getIdEvent() {
        return idEvent;
    }

    public void setIdEvent(int idEvent) {
        this.idEvent = idEvent;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getNameAreaFrom() {
        return nameAreaFrom;
    }

    public void setNameAreaFrom(String nameAreaFrom) {
        this.nameAreaFrom = nameAreaFrom;
    }

    public String getNameAreaTo() {
        return nameAreaTo;
    }

    public void setNameAreaTo(String nameAreaTo) {
        this.nameAreaTo = nameAreaTo;
    }

    public String getNameAccessPoint() {
        return nameAccessPoint;
    }

    public void setNameAccessPoint(String nameAccessPoint) {
        this.nameAccessPoint = nameAccessPoint;
    }

    public String getNameUser() {
        return nameUser;
    }

    public void setNameUser(String nameUser) {
        this.nameUser = nameUser;
    }

    public String getNameCredential() {
        return nameCredential;
    }

    public void setNameCredential(String nameCredential) {
        this.nameCredential = nameCredential;
    }

    public int getLastMovement() {
        return lastMovement;
    }

    public void setLastMovement(int lastMovement) {
        this.lastMovement = lastMovement;
    }

    public JSONObject toJSON(int userId){

        JSONObject jsonObject= new JSONObject();
        try {
            jsonObject.put("id", getId());
            jsonObject.put("time", getTime());
            jsonObject.put("idCredential", getIdCredential());
            jsonObject.put("idAccessPoint", getIdAccessPoint());
            jsonObject.put("idAreaFrom", getIdAreaFrom());
            jsonObject.put("idAreaTo", getIdAreaTo());
            jsonObject.put("idUser", userId);

            return jsonObject;
        } catch (JSONException e) {
            e.printStackTrace();
            jsonObject = null;
            return jsonObject;
        }

    }
}


