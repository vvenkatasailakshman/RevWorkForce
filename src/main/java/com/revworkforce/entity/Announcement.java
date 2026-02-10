package com.revworkforce.entity;

import java.sql.Timestamp;

public class Announcement {
    private int announcementId;
    private String title;
    private String content;
    private String postedBy;
    private Timestamp postedOn;

    public Announcement() {
    }

    public Announcement(int announcementId, String title, String content, String postedBy, Timestamp postedOn) {
        this.announcementId = announcementId;
        this.title = title;
        this.content = content;
        this.postedBy = postedBy;
        this.postedOn = postedOn;
    }

    public int getAnnouncementId() {
        return announcementId;
    }

    public void setAnnouncementId(int announcementId) {
        this.announcementId = announcementId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPostedBy() {
        return postedBy;
    }

    public void setPostedBy(String postedBy) {
        this.postedBy = postedBy;
    }

    public Timestamp getPostedOn() {
        return postedOn;
    }

    public void setPostedOn(Timestamp postedOn) {
        this.postedOn = postedOn;
    }
}
