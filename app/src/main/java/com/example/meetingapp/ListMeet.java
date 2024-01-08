package com.example.meetingapp;

public class ListMeet {
    private String topic, describe;

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public ListMeet(String topic, String describe) {
        this.topic = topic;
        this.describe = describe;
    }
}
