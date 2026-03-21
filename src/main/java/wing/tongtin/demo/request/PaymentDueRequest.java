package wing.tongtin.demo.request;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentDueRequest {
    private String groupId;
    private LocalDateTime dueDate;
}