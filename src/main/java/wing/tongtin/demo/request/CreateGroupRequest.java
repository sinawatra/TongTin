package wing.tongtin.demo.request;


import lombok.Data;

import java.math.BigDecimal;

@Data
public class CreateGroupRequest {
    private String groupName;
    private BigDecimal contributionAmount;
    private Integer totalMembers;

}
