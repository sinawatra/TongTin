package wing.tongtin.demo.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import wing.tongtin.demo.response.ApiResponse;
import wing.tongtin.demo.service.OverviewService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class OverviewController {

    private final OverviewService overviewService;

    @GetMapping("/overview")
    public ApiResponse<?> getOverview() {
        return ApiResponse.builder()
                .success(true)
                .message("Success")
                .data(overviewService.getOverview())
                .build();
    }
}