package com.eventusgest.modelo;

public class Credential {
    private int id, idEntity, idCurrentArea, idEvent, flagged, blocked;
    private String ucid, carrierName, carrierType, carrierPhoto, entityName;

    private static int autoIncrementedId = 1;

    public Credential(int id, int idEntity, int idCurrentArea, int idEvent, int flagged, int blocked, String ucid, String carrierName, String carrierType, String carrierPhoto, String entityName) {
        this.id = id;
        this.idEntity = idEntity;
        this.idCurrentArea = idCurrentArea;
        this.idEvent = idEvent;
        this.flagged = flagged;
        this.blocked = blocked;
        this.ucid = ucid;
        this.carrierName = carrierName;
        this.carrierType = carrierType;
        this.carrierPhoto = carrierPhoto;
        this.entityName = entityName;
    }

    public int getId() {
        return id;
    }

    public int getIdEntity() {
        return idEntity;
    }

    public int getIdCurrentArea() {
        return idCurrentArea;
    }

    public int getIdEvent() {
        return idEvent;
    }

    public int getFlagged() {
        return flagged;
    }

    public int getBlocked() {
        return blocked;
    }

    public String getUcid() {
        return ucid;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setIdEntity(int idEntity) {
        this.idEntity = idEntity;
    }

    public void setIdCurrentArea(int idCurrentArea) {
        this.idCurrentArea = idCurrentArea;
    }

    public void setIdEvent(int idEvent) {
        this.idEvent = idEvent;
    }

    public void setFlagged(int flagged) {
        this.flagged = flagged;
    }

    public void setBlocked(int blocked) {
        this.blocked = blocked;
    }

    public void setUcid(String ucid) {
        this.ucid = ucid;
    }

    public void setID(int id) { this.id = id; }

    public String getCarrierName() {
        return carrierName;
    }

    public void setCarrierName(String carrierName) {
        this.carrierName = carrierName;
    }

    public String getCarrierType() {
        return carrierType;
    }

    public void setCarrierType(String carrierType) {
        this.carrierType = carrierType;
    }

    public String getCarrierPhoto() {
        return carrierPhoto;
    }

    public void setCarrierPhoto(String carrierPhoto) {
        this.carrierPhoto = carrierPhoto;
    }

    public String getEntityName() {
        return entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }
}
