package wing.tongtin.demo.request;


import lombok.Data;

@Data
public class RegisterRequest {

    private String fullName;
    private String phone;
    private String password;
}