package wing.tongtin.demo.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class OverviewResponse {
    private Long totalUser;
    private Long totalGroup;
    private Long totalMoney;
    private Long totalPendingKyc;
}


