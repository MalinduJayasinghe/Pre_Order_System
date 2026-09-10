package lk.ijse.preordersystem.service;

import lk.ijse.preordersystem.dto.NotificationDTO;

import java.util.List;

public interface NotificationService {

    void createNotification(long userId, String message);
    List<NotificationDTO> getNotificationsForUser(long userId);
    void markAsRead(Long notificationId);
}
