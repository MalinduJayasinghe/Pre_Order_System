package lk.ijse.preordersystem.controllers;

import lk.ijse.preordersystem.dto.CommonResponse;
import lk.ijse.preordersystem.dto.NotificationDTO;
import lk.ijse.preordersystem.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "v1/notification")
@RequiredArgsConstructor
@Slf4j
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping(value = "/user/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse getNotificationsForUser(@PathVariable long userId) {

        log.info("getNotificationsForUser API was called");
        List<NotificationDTO> notifications = notificationService.getNotificationsForUser(userId);

        log.info("getNotificationsForUser API successful");
        return new CommonResponse(0, notifications, "Notifications called");
    }

    @PatchMapping(value = "/{notificationId}/read", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse markAsRead(@PathVariable Long notificationId) {

        log.info("markAsRead API was called");
        notificationService.markAsRead(notificationId);

        log.info("markAsRead API successful");
        return new CommonResponse(0, "Notification Read", "Notification marked as read");
    }
}
