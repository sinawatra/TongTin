package wing.tongtin.demo.entity;

import jakarta.persistence.*;
import lombok.*;
import wing.tongtin.demo.enumeration.NotificationType;

import java.time.LocalDateTime;

@Entity
@Table(name = "notifications")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name = "group_id", referencedColumnName = "id")
    private TontineGroupEntity group;

    private String title;

    private String message;

    @Enumerated(EnumType.STRING)
    private NotificationType type;

    @Builder.Default
    private Boolean isRead = false;

    private LocalDateTime dueDate;
}