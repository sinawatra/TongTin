package wing.tongtin.demo.response;

import lombok.*;
import wing.tongtin.demo.entity.KycEntity;
import wing.tongtin.demo.enumeration.KycDocumentType;
import wing.tongtin.demo.enumeration.KycStatus;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class KycResponse {
    private String id;
    private String file;
    private KycDocumentType type;
    private KycStatus status;
    private String userId;
    private String userFullName;
    private String userPhone;
    private String reviewedBy;
    private String rejectionReason;
    private LocalDateTime createdAt;
    private LocalDateTime reviewedAt;

    public static KycResponse fromEntity(KycEntity entity) {
        return KycResponse.builder()
                .id(entity.getId())
                .file(entity.getFile())
                .type(entity.getType())
                .status(entity.getStatus())
                .userId(entity.getUser().getId())
                .userFullName(entity.getUser().getFullName())
                .userPhone(entity.getUser().getPhone())
                .reviewedBy(entity.getReviewedBy())
                .rejectionReason(entity.getRejectionReason())
                .createdAt(entity.getCreatedAt())
                .reviewedAt(entity.getReviewedAt())
                .build();
    }
}
