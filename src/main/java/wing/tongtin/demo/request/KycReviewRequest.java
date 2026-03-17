package wing.tongtin.demo.request;

import lombok.Data;
import wing.tongtin.demo.enumeration.KycStatus;

@Data
public class KycReviewRequest {
    private KycStatus status;
    private String rejectionReason;
}