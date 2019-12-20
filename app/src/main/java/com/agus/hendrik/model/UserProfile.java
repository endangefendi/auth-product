package com.agus.hendrik.model;

public class UserProfile {
    private String deviceId, email, uid;
    private int level;

    public UserProfile() {
    }

    public UserProfile(String deviceId, int level, String email, String uid) {

        this.level = level;
        this.deviceId = deviceId;
        this.email = email;
        this.uid = uid;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }
}