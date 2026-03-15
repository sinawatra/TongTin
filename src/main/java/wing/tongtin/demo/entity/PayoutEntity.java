package wing.tongtin.demo.entity;

import wing.tongtin.demo.enumeration.PaymentStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "payouts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PayoutEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne
    @JoinColumn(name = "recipient_id", referencedColumnName = "id")
    private UserEntity recipient;

    @ManyToOne
    @JoinColumn(name = "group_id", referencedColumnName = "id")
    private TontineGroupEntity group;

    private Integer cycleNumber;

    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    private PaymentStatus status;

    private LocalDateTime paidAt;
}