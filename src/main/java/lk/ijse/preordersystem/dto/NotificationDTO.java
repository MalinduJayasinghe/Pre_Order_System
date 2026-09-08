package lk.ijse.preordersystem.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificationDTO {

    private long notificationId;
    private String message;
    private boolean isRead;
    private LocalDateTime createdAt;
}
