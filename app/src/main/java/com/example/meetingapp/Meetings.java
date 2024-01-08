package com.example.meetingapp;

public class Meetings {
    String id;
     String topic;
     String pass;
     String date;
     String time;
     String name;

    public Meetings() {
    }

    public Meetings(String id, String topic, String pass, String date, String time, String name) {
        this.id = id;
        this.topic = topic;
        this.pass = pass;
        this.date = date;
        this.time = time;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
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

    public String getname() {
        return name;
    }

    public void setname(String name) {
        this.name = name;
    }
}
