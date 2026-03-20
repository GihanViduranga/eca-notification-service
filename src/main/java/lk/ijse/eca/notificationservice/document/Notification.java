package lk.ijse.eca.notificationservice.document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;

@Document(collection = "notifications")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Notification {

    @Id
    private String id;

    private String recipientEmail;
    private String recipientName;
    private String subject;
    private String message;
    private NotificationType type;
    private NotificationStatus status;
    private String referenceId;   // orderId, productId, etc.
    private String referenceType; // ORDER, PRODUCT, SYSTEM

    private LocalDateTime createdAt;
    private LocalDateTime sentAt;

    public enum NotificationType {
        ORDER_CREATED, ORDER_CONFIRMED, ORDER_SHIPPED,
        ORDER_DELIVERED, ORDER_CANCELLED,
        PRODUCT_BACK_IN_STOCK, SYSTEM_ALERT
    }

    public enum NotificationStatus {
        PENDING, SENT, FAILED, READ
    }
}
