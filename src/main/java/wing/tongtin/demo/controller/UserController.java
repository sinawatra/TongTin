package wing.tongtin.demo.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import wing.tongtin.demo.entity.UserEntity;
import wing.tongtin.demo.response.ApiResponse;
import wing.tongtin.demo.service.ContributionService;
import wing.tongtin.demo.service.UserService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final ContributionService contributionService;

    @GetMapping("/{id}")
    public ApiResponse<?> getUserInfo(@PathVariable String id) {
        UserEntity user = userService.getUserInfoById(id);
        return ApiResponse.builder()
                .success(true)
                .message("User found")
                .data(user)
                .build();
    }

    @GetMapping("/{userId}/payouts")
    public ApiResponse<?> getUserPayouts(@PathVariable String userId) {
        return ApiResponse.builder()
                .success(true)
                .message("Success")
                .data(contributionService.getUserPayouts(userId))
                .build();
    }
}