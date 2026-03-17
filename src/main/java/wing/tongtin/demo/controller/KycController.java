package wing.tongtin.demo.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import wing.tongtin.demo.request.KycRequest;
import wing.tongtin.demo.response.ApiResponse;
import wing.tongtin.demo.service.KycService;

@RestController
@RequestMapping("/api/kyc")
@RequiredArgsConstructor
public class KycController {

    private final KycService kycService;

    @PostMapping("/submit")
    public ApiResponse<?> submitKycDocument(
            @AuthenticationPrincipal String userId,
            @RequestBody KycRequest request) {
        return ApiResponse.builder()
                .success(true)
                .message("KYC document submitted successfully")
                .data(kycService.submitKycDocument(userId, request))
                .build();
    }

    @GetMapping("/my-documents")
    public ApiResponse<?> getMyKycDocuments(@AuthenticationPrincipal String userId) {
        return ApiResponse.builder()
                .success(true)
                .message("Success")
                .data(kycService.getUserKycDocuments(userId))
                .build();
    }

    @GetMapping("/{kycId}")
    public ApiResponse<?> getKycDocument(@PathVariable String kycId) {
        return ApiResponse.builder()
                .success(true)
                .message("Success")
                .data(kycService.getKycDocument(kycId))
                .build();
    }
}
