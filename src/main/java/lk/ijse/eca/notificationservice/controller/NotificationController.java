package lk.ijse.eca.notificationservice.controller;

import lk.ijse.eca.notificationservice.dto.NotificationDTO;
import lk.ijse.eca.notificationservice.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/notifications")
@RequiredArgsConstructor
@Slf4j
public class NotificationController {

    private final NotificationService notificationService;

    @PostMapping
    public ResponseEntity<NotificationDTO> createNotification(@RequestBody NotificationDTO dto) {
        log.info("Creating notification for: {}", dto.getRecipientEmail());
        return ResponseEntity.status(HttpStatus.CREATED).body(notificationService.createNotification(dto));
    }

    @PostMapping("/order")
    public ResponseEntity<NotificationDTO> sendOrderNotification(
            @RequestParam String email,
            @RequestParam String name,
            @RequestParam String orderId,
            @RequestParam String type) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(notificationService.sendOrderNotification(email, name, orderId, type));
    }

    @GetMapping("/{id}")
    public ResponseEntity<NotificationDTO> getById(@PathVariable String id) {
        return ResponseEntity.ok(notificationService.getNotificationById(id));
    }

    @GetMapping
    public ResponseEntity<List<NotificationDTO>> getAll() {
        return ResponseEntity.ok(notificationService.getAllNotifications());
    }

    @GetMapping("/recipient")
    public ResponseEntity<List<NotificationDTO>> getByEmail(@RequestParam String email) {
        return ResponseEntity.ok(notificationService.getNotificationsByEmail(email));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<NotificationDTO>> getByStatus(@PathVariable String status) {
        return ResponseEntity.ok(notificationService.getNotificationsByStatus(status));
    }

    @PatchMapping("/{id}/read")
    public ResponseEntity<NotificationDTO> markAsRead(@PathVariable String id) {
        return ResponseEntity.ok(notificationService.markAsRead(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> delete(@PathVariable String id) {
        notificationService.deleteNotification(id);
        Map<String, String> resp = new HashMap<>();
        resp.put("message", "Notification deleted successfully");
        return ResponseEntity.ok(resp);
    }

    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> health() {
        Map<String, String> resp = new HashMap<>();
        resp.put("status", "UP");
        resp.put("service", "notification-service");
        resp.put("database", "MongoDB");
        return ResponseEntity.ok(resp);
    }
}
