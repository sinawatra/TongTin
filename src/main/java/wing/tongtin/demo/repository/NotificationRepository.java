package wing.tongtin.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import wing.tongtin.demo.entity.NotificationEntity;
import wing.tongtin.demo.enumeration.NotificationType;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<NotificationEntity, String> {

    List<NotificationEntity> findByUserIdOrderByCreatedAtDesc(String userId);

    List<NotificationEntity> findByUserIdAndIsReadFalseOrderByCreatedAtDesc(String userId);

    List<NotificationEntity> findByTypeAndDueDateBetween(NotificationType type, LocalDateTime start, LocalDateTime end);

    int countByUserIdAndIsReadFalse(String userId);
}