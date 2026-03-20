package lk.ijse.eca.notificationservice.service;

import lk.ijse.eca.notificationservice.document.Notification;
import lk.ijse.eca.notificationservice.dto.NotificationDTO;
import lk.ijse.eca.notificationservice.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final ModelMapper modelMapper;

    @Override
    public NotificationDTO createNotification(NotificationDTO dto) {
        Notification notification = modelMapper.map(dto, Notification.class);
        notification.setId(null);
        notification.setStatus(Notification.NotificationStatus.PENDING);
        notification.setCreatedAt(LocalDateTime.now());

        // Simulate sending
        notification.setStatus(Notification.NotificationStatus.SENT);
        notification.setSentAt(LocalDateTime.now());

        Notification saved = notificationRepository.save(notification);
        log.info("Notification created and sent to: {}", saved.getRecipientEmail());
        return modelMapper.map(saved, NotificationDTO.class);
    }

    @Override
    public NotificationDTO getNotificationById(String id) {
        Notification n = notificationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notification not found: " + id));
        return modelMapper.map(n, NotificationDTO.class);
    }

    @Override
    public List<NotificationDTO> getAllNotifications() {
        return notificationRepository.findAll().stream()
                .map(n -> modelMapper.map(n, NotificationDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<NotificationDTO> getNotificationsByEmail(String email) {
        return notificationRepository.findByRecipientEmail(email).stream()
                .map(n -> modelMapper.map(n, NotificationDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<NotificationDTO> getNotificationsByStatus(String status) {
        Notification.NotificationStatus s = Notification.NotificationStatus.valueOf(status.toUpperCase());
        return notificationRepository.findByStatus(s).stream()
                .map(n -> modelMapper.map(n, NotificationDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public NotificationDTO markAsRead(String id) {
        Notification n = notificationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notification not found: " + id));
        n.setStatus(Notification.NotificationStatus.READ);
        return modelMapper.map(notificationRepository.save(n), NotificationDTO.class);
    }

    @Override
    public void deleteNotification(String id) {
        if (!notificationRepository.existsById(id)) {
            throw new RuntimeException("Notification not found: " + id);
        }
        notificationRepository.deleteById(id);
    }

    @Override
    public NotificationDTO sendOrderNotification(String customerEmail, String customerName,
                                                  String orderId, String type) {
        NotificationDTO dto = new NotificationDTO();
        dto.setRecipientEmail(customerEmail);
        dto.setRecipientName(customerName);
        dto.setReferenceId(orderId);
        dto.setReferenceType("ORDER");
        dto.setType(Notification.NotificationType.valueOf(type));

        switch (type) {
            case "ORDER_CREATED" -> {
                dto.setSubject("Order Confirmed - #" + orderId);
                dto.setMessage("Dear " + customerName + ", your order #" + orderId +
                        " has been received and is being processed.");
            }
            case "ORDER_SHIPPED" -> {
                dto.setSubject("Your Order #" + orderId + " Has Been Shipped");
                dto.setMessage("Dear " + customerName + ", your order #" + orderId +
                        " is on its way!");
            }
            case "ORDER_DELIVERED" -> {
                dto.setSubject("Order #" + orderId + " Delivered");
                dto.setMessage("Dear " + customerName + ", your order #" + orderId +
                        " has been delivered. Thank you for shopping with us!");
            }
            default -> {
                dto.setSubject("Order Update - #" + orderId);
                dto.setMessage("Dear " + customerName + ", there's an update on your order #" + orderId);
            }
        }
        return createNotification(dto);
    }
}
