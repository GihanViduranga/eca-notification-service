package lk.ijse.eca.notificationservice.service;

import lk.ijse.eca.notificationservice.dto.NotificationDTO;
import java.util.List;

public interface NotificationService {
    NotificationDTO createNotification(NotificationDTO dto);
    NotificationDTO getNotificationById(String id);
    List<NotificationDTO> getAllNotifications();
    List<NotificationDTO> getNotificationsByEmail(String email);
    List<NotificationDTO> getNotificationsByStatus(String status);
    NotificationDTO markAsRead(String id);
    void deleteNotification(String id);
    NotificationDTO sendOrderNotification(String customerEmail, String customerName, String orderId, String type);
}
