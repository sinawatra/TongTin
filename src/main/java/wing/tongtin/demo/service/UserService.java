package wing.tongtin.demo.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import wing.tongtin.demo.entity.UserEntity;
import wing.tongtin.demo.repository.UserRepository;
import wing.tongtin.demo.request.RegisterRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;


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
                .imageUrl(request.getImageUrl())
                .kycVerified(false)
                .build();

        return userRepository.save(user);
    }

    public UserEntity getUserInfoById(String userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public UserEntity addMoney(String userId, BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("Amount must be greater than 0");
        }
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (user.getBalance() == null) {
            user.setBalance(BigDecimal.ZERO);
        }

        user.setBalance(user.getBalance().add(amount));
        return userRepository.save(user);
    }



}