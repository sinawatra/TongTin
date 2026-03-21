package wing.tongtin.demo.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import wing.tongtin.demo.entity.NotificationEntity;
import wing.tongtin.demo.entity.TontineGroupEntity;
import wing.tongtin.demo.entity.UserEntity;
import wing.tongtin.demo.enumeration.NotificationType;
import wing.tongtin.demo.repository.NotificationRepository;
import wing.tongtin.demo.repository.TontineGroupRepository;
import wing.tongtin.demo.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;
    private final TontineGroupRepository groupRepository;

    public List<NotificationEntity> getUserNotifications() {
        String userId = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        return notificationRepository.findByUserIdOrderByCreatedAtDesc(userId);
    }

    public List<NotificationEntity> getUnreadNotifications() {
        String userId = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        return notificationRepository.findByUserIdAndIsReadFalseOrderByCreatedAtDesc(userId);
    }

    public int getUnreadCount() {
        String userId = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        return notificationRepository.countByUserIdAndIsReadFalse(userId);
    }

    public NotificationEntity markAsRead(String notificationId) {
        NotificationEntity notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new RuntimeException("Notification not found"));
        notification.setIsRead(true);
        return notificationRepository.save(notification);
    }

    public void markAllAsRead() {
        String userId = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        List<NotificationEntity> unreadNotifications = notificationRepository.findByUserIdAndIsReadFalseOrderByCreatedAtDesc(userId);
        unreadNotifications.forEach(notification -> notification.setIsRead(true));
        notificationRepository.saveAll(unreadNotifications);
    }

    public NotificationEntity createJoinGroupNotification(UserEntity user, TontineGroupEntity group) {
        NotificationEntity notification = NotificationEntity.builder()
                .user(user)
                .group(group)
                .title("Joined Group Successfully")
                .message("You have successfully joined the group: " + group.getGroupName())
                .type(NotificationType.JOIN_GROUP_SUCCESS)
                .isRead(false)
                .build();
        return notificationRepository.save(notification);
    }

    public NotificationEntity createContributionNotification(UserEntity user, TontineGroupEntity group, String amount) {
        NotificationEntity notification = NotificationEntity.builder()
                .user(user)
                .group(group)
                .title("Contribution Successful")
                .message("Your contribution of " + amount + " to " + group.getGroupName() + " was successful.")
                .type(NotificationType.CONTRIBUTION_SUCCESS)
                .isRead(false)
                .build();
        return notificationRepository.save(notification);
    }

    public NotificationEntity createPaymentDueNotification(UserEntity user, TontineGroupEntity group, LocalDateTime dueDate) {
        NotificationEntity notification = NotificationEntity.builder()
                .user(user)
                .group(group)
                .title("Payment Due Reminder")
                .message("Your payment for " + group.getGroupName() + " is due soon. Amount: " + group.getContributionAmount())
                .type(NotificationType.PAYMENT_DUE_REMINDER)
                .isRead(false)
                .dueDate(dueDate)
                .build();
        return notificationRepository.save(notification);
    }

    public NotificationEntity sendJoinGroupNotification(String groupId) {
        String userId = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        TontineGroupEntity group = groupRepository.findById(groupId)
                .orElseThrow(() -> new RuntimeException("Group not found"));
        return createJoinGroupNotification(user, group);
    }

    public NotificationEntity sendContributionNotification(String groupId, String amount) {
        String userId = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        TontineGroupEntity group = groupRepository.findById(groupId)
                .orElseThrow(() -> new RuntimeException("Group not found"));
        return createContributionNotification(user, group, amount);
    }

    public NotificationEntity sendPaymentDueNotification(String groupId, LocalDateTime dueDate) {
        String userId = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        TontineGroupEntity group = groupRepository.findById(groupId)
                .orElseThrow(() -> new RuntimeException("Group not found"));
        return createPaymentDueNotification(user, group, dueDate);
    }
}