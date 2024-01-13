package com.example.meetingapp;

public class Files {
    private String fileID, fileUrl,date,time;

    public Files() {
    }

    public Files(String fileID, String fileUrl,String date,String time) {
        this.fileID = fileID;
        this.fileUrl = fileUrl;
        this.date = date;
        this.time = time;
    }

    public String getfileID() {
        return fileID;
    }

    public void setfileID(String fileID) {
        this.fileID = fileID;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
