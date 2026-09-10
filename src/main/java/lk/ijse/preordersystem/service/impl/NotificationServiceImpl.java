package lk.ijse.preordersystem.service.impl;

import lk.ijse.preordersystem.dto.NotificationDTO;
import lk.ijse.preordersystem.entity.Notification;
import lk.ijse.preordersystem.entity.User;
import lk.ijse.preordersystem.repository.NotificationRepository;
import lk.ijse.preordersystem.repository.UserRepository;
import lk.ijse.preordersystem.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;

    @Override
    public void createNotification(long userId, String message) {

        log.info("Execute method createNotification");

        try {

            Optional<User> optionalUser = userRepository.findById(userId);
            if (optionalUser.isEmpty()) {
                return;
            }

            Notification notification = new Notification();
            notification.setUser(optionalUser.get());
            notification.setMessage(message);
            notification.setRead(false);
            notification.setCreatedAt(LocalDateTime.now());

            notificationRepository.save(notification);
            log.info("Notification created successfully");

        }catch (Exception e){
            log.error("Error in method createNotification" + e.getMessage());
        }
    }

    @Override
    public List<NotificationDTO> getNotificationsForUser(long userId) {

        log.info("Execute method getNotificationsForUser");

        try {

            List<NotificationDTO> responseList = new ArrayList<>();
            List<Notification> notificationList = notificationRepository.findByUser_UserIdOrderByCreatedAtDesc(userId);

            for (Notification notification : notificationList) {

                NotificationDTO notificationDTO = new NotificationDTO();
                notificationDTO.setNotificationId(notification.getNotificationId());
                notificationDTO.setMessage(notification.getMessage());
                notificationDTO.setRead(notification.isRead());
                notificationDTO.setCreatedAt(notification.getCreatedAt());

                responseList.add(notificationDTO);
            }

            log.info("Notifications retrieved successfully");
            return responseList;

        }catch (Exception e){
            log.error("Error in method getNotificationsForUser" + e.getMessage());
            throw e;
        }
    }

    @Override
    public void markAsRead(Long notificationId) {

        log.info("Execute method markAsRead");

        try {

            Optional<Notification> optionalNotification = notificationRepository.findById(notificationId);
            if (optionalNotification.isEmpty()) {
                throw new RuntimeException("Notification not found");
            }

            Notification notification = optionalNotification.get();
            notification.setRead(true);
            notificationRepository.save(notification);

            log.info("Notification marked as read");

        }catch (Exception e){
            log.error("Error in method markAsRead" + e.getMessage());
            throw e;
        }
    }
}
