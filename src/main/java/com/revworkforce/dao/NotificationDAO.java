package com.revworkforce.dao;

import java.util.List;

import com.revworkforce.entity.Notification;

public interface NotificationDAO {
    boolean createNotification(Notification notification);

    List<Notification> getNotificationsByEmployee(String employeeId);

    boolean markAsRead(int notificationId);

    int getUnreadCount(String employeeId);

    boolean existsToday(String employeeId, String messageSubstring);
}
