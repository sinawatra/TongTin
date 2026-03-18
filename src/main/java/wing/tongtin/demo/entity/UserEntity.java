package wing.tongtin.demo.entity;


import jakarta.persistence.*;
import lombok.*;
import wing.tongtin.demo.enumeration.Role;

import java.math.BigDecimal;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String fullName;

    @Column(unique = true)
    private String phone;

    private String email;

    private String password;

    private Boolean kycVerified;

    private BigDecimal balance;

    private String imageUrl;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private Role role = Role.USER;
}


