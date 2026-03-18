package wing.tongtin.demo.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import wing.tongtin.demo.repository.ContributionRepository;
import wing.tongtin.demo.repository.TontineGroupRepository;
import wing.tongtin.demo.repository.UserRepository;
import wing.tongtin.demo.request.DepositRequest;
import wing.tongtin.demo.request.KycReviewRequest;
import wing.tongtin.demo.response.ApiResponse;
import wing.tongtin.demo.service.KycService;
import wing.tongtin.demo.service.UserService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final TontineGroupRepository groupRepository;
    private final ContributionRepository contributionRepository;
    private final UserRepository userRepository;
    private final KycService kycService;
    private final UserService userService;

    @GetMapping("/groups")
    public ApiResponse<?> getAllGroups() {
        return ApiResponse.builder()
                .success(true)
                .message("Success")
                .data(groupRepository.findAll())
                .build();
    }

    @GetMapping("/contributions")
    public ApiResponse<?> getAllContributions() {
        return ApiResponse.builder()
                .success(true)
                .message("Success")
                .data(contributionRepository.findAll())
                .build();
    }

    @GetMapping("/users")
    public ApiResponse<?> getAllUsers() {
        return ApiResponse.builder()
                .success(true)
                .message("Success")
                .data(userRepository.findAll())
                .build();
    }

    @GetMapping("/kyc")
    public ApiResponse<?> getAllKycDocuments() {
        return ApiResponse.builder()
                .success(true)
                .message("Success")
                .data(kycService.getAllKycDocuments())
                .build();
    }

    @GetMapping("/kyc/pending")
    public ApiResponse<?> getPendingKycDocuments() {
        return ApiResponse.builder()
                .success(true)
                .message("Success")
                .data(kycService.getPendingKycDocuments())
                .build();
    }

    @GetMapping("/kyc/user/{userId}")
    public ApiResponse<?> getUserKycDocuments(@PathVariable String userId) {
        return ApiResponse.builder()
                .success(true)
                .message("Success")
                .data(kycService.getUserKycDocuments(userId))
                .build();
    }

    @PutMapping("/kyc/{kycId}/review")
    public ApiResponse<?> reviewKycDocument(
            @PathVariable String kycId,
            @AuthenticationPrincipal String adminId,
            @RequestBody KycReviewRequest request) {
        return ApiResponse.builder()
                .success(true)
                .message("KYC document reviewed successfully")
                .data(kycService.reviewKycDocument(kycId, adminId, request))
                .build();
    }

    @PutMapping("/kyc/user/{userId}/approve-all")
    public ApiResponse<?> approveAllUserKyc(
            @PathVariable String userId,
            @AuthenticationPrincipal String adminId) {
        return ApiResponse.builder()
                .success(true)
                .message("All KYC documents approved successfully")
                .data(kycService.approveAllUserKyc(userId, adminId))
                .build();
    }

    @PutMapping("/deposit/{userId}")
    public ApiResponse<?> depositMoney(
            @PathVariable String userId,
            @RequestBody DepositRequest request,
            @AuthenticationPrincipal String adminId
    ) {
        return ApiResponse.builder()
                .success(true)
                .message("Successfully deposit into account")
                .data(userService.addMoney(userId, request.getAmount()))
                .build();
    }
}
