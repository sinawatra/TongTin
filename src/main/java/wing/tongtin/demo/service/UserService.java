package wing.tongtin.demo.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import wing.tongtin.demo.entity.UserEntity;
import wing.tongtin.demo.repository.UserRepository;
import wing.tongtin.demo.request.RegisterRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserEntity register(RegisterRequest request) {
        if (userRepository.findByPhone(request.getPhone()).isPresent()) {
            throw new RuntimeException("User with this phone number already exists");
        }

        String hashedPassword = passwordEncoder.encode(request.getPassword());
        UserEntity user = UserEntity.builder()
                .fullName(request.getFullName())
                .phone(request.getPhone())
                .password(hashedPassword)
                .kycVerified(false)
                .build();

        return userRepository.save(user);
    }

}