package com.example.meetingapp;

public class Contents {
    private String meetingId, id, content;

    public Contents() {
    }

    public Contents(String meetingId, String id, String content) {
        this.meetingId = meetingId;
        this.id = id;
        this.content = content;
    }

    public String getMeetingId() {
        return meetingId;
    }

    public void setMeetingId(String meetingId) {
        this.meetingId = meetingId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
