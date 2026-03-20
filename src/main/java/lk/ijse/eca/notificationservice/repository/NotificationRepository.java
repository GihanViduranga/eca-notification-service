package lk.ijse.eca.notificationservice.repository;

import lk.ijse.eca.notificationservice.document.Notification;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface NotificationRepository extends MongoRepository<Notification, String> {
    List<Notification> findByRecipientEmail(String email);
    List<Notification> findByStatus(Notification.NotificationStatus status);
    List<Notification> findByType(Notification.NotificationType type);
    List<Notification> findByReferenceIdAndReferenceType(String referenceId, String referenceType);
}
