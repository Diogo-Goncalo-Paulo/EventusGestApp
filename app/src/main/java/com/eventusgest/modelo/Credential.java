package com.eventusgest.modelo;

public class Credential {
    private int id, idEntity, idCurrentArea, idEvent, flagged, blocked;
    private String ucid, carrierName, carrierType, carrierPhoto, carrierInfo, entityName, qrCode, entityTypeName, currentAreaName;

    private static int autoIncrementedId = 1;

    public Credential(int id, int idEntity, int idCurrentArea, int idEvent, int flagged, int blocked, String ucid, String carrierName, String carrierType, String carrierPhoto, String entityName, String carrierInfo, String qrCode, String entityTypeName, String currentAreaName) {
        this.id = id;
        this.idEntity = idEntity;
        this.idCurrentArea = idCurrentArea;
        this.idEvent = idEvent;
        this.flagged = flagged;
        this.blocked = blocked;
        this.ucid = ucid;
        this.carrierName = carrierName;
        this.carrierPhoto = carrierPhoto;
        this.entityName = entityName;
        this.carrierType = carrierType;
        this.carrierInfo = carrierInfo;
        this.qrCode = qrCode;
        this.entityTypeName = entityTypeName;
        this.currentAreaName = currentAreaName;
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

    public String getCarrierInfo() {
        return carrierInfo;
    }

    public void setCarrierInfo(String carrierInfo) {
        this.carrierInfo = carrierInfo;
    }

    public String getQrCode() {
        return qrCode;
    }

    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }

    public String getEntityTypeName() {
        return entityTypeName;
    }

    public void setEntityTypeName(String entityTypeName) {
        this.entityTypeName = entityTypeName;
    }

    public String getCurrentAreaName() {
        return currentAreaName;
    }

    public void setCurrentAreaName(String currentAreaName) {
        this.currentAreaName = currentAreaName;
    }
}
