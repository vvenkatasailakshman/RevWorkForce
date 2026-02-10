package com.revworkforce.dao;

import java.util.List;

import com.revworkforce.entity.Announcement;

public interface AnnouncementDAO {
    boolean createAnnouncement(Announcement announcement);

    List<Announcement> getAllAnnouncements();
}
