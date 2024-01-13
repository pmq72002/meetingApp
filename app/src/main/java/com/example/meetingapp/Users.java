package com.example.meetingapp;


public class Users {
    String userId,name,profile,mnv,position;

    public Users(String userId, String name, String profile,String mnv,String position) {
        this.userId = userId;
        this.name = name;
        this.profile = profile;
        this.mnv = mnv;
        this.position = position;
    }

    public Users() {
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public String getMnv() {
        return mnv;
    }

    public void setMnv(String mnv) {
        this.mnv = mnv;
    }

    public String getposition() {
        return position;
    }

    public void setposition(String position) {
        this.position = position;
    }
}

