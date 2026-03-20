package lk.ijse.eca.notificationservice.dto;

import lk.ijse.eca.notificationservice.document.Notification;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificationDTO {
    private String id;
    private String recipientEmail;
    private String recipientName;
    private String subject;
    private String message;
    private Notification.NotificationType type;
    private Notification.NotificationStatus status;
    private String referenceId;
    private String referenceType;
    private LocalDateTime createdAt;
    private LocalDateTime sentAt;
}
