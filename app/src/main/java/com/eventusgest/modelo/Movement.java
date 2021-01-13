package com.eventusgest.modelo;

import java.util.Date;

public class Movement {

    private int id, idCredential, idAccessPoint, idAreaFrom, idAreaTo, idUser;
    private Date time;

    public Movement(int id, int idCredential, int idAccessPoint, int idAreaFrom, int idAreaTo, int idUser, Date time) {
        this.id = id;
        this.idCredential = idCredential;
        this.idAccessPoint = idAccessPoint;
        this.idAreaFrom = idAreaFrom;
        this.idAreaTo = idAreaTo;
        this.idUser = idUser;
        this.time = time;
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

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }
}
