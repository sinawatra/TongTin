package wing.tongtin.demo.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegisterResponse {
    private String id;
    private String fullName;
    private String phone;
    private String email;
    private String imageUrl;
    private Boolean kycVerified;
    private String token;
    private String tokenType;
}