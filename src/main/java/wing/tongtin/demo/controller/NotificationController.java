package wing.tongtin.demo.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import wing.tongtin.demo.request.PaymentDueRequest;
import wing.tongtin.demo.response.ApiResponse;
import wing.tongtin.demo.service.NotificationService;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping
    public ApiResponse<?> getUserNotifications() {
        return ApiResponse.builder()
                .success(true)
                .message("Success")
                .data(notificationService.getUserNotifications())
                .build();
    }

    @GetMapping("/unread")
    public ApiResponse<?> getUnreadNotifications() {
        return ApiResponse.builder()
                .success(true)
                .message("Success")
                .data(notificationService.getUnreadNotifications())
                .build();
    }

    @GetMapping("/unread/count")
    public ApiResponse<?> getUnreadCount() {
        return ApiResponse.builder()
                .success(true)
                .message("Success")
                .data(notificationService.getUnreadCount())
                .build();
    }

    @PutMapping("/{id}/read")
    public ApiResponse<?> markAsRead(@PathVariable String id) {
        return ApiResponse.builder()
                .success(true)
                .message("Notification marked as read")
                .data(notificationService.markAsRead(id))
                .build();
    }

    @PutMapping("/read-all")
    public ApiResponse<?> markAllAsRead() {
        notificationService.markAllAsRead();
        return ApiResponse.builder()
                .success(true)
                .message("All notifications marked as read")
                .data(null)
                .build();
    }

    @PostMapping("/join-group/{groupId}")
    public ApiResponse<?> sendJoinGroupNotification(@PathVariable String groupId) {
        return ApiResponse.builder()
                .success(true)
                .message("Join group notification sent")
                .data(notificationService.sendJoinGroupNotification(groupId))
                .build();
    }

    @PostMapping("/contribution/{groupId}")
    public ApiResponse<?> sendContributionNotification(
            @PathVariable String groupId,
            @RequestParam String amount) {
        return ApiResponse.builder()
                .success(true)
                .message("Contribution notification sent")
                .data(notificationService.sendContributionNotification(groupId, amount))
                .build();
    }

    @PostMapping("/payment-due")
    public ApiResponse<?> sendPaymentDueNotification(@RequestBody PaymentDueRequest request) {
        return ApiResponse.builder()
                .success(true)
                .message("Payment due reminder sent")
                .data(notificationService.sendPaymentDueNotification(request.getGroupId(), request.getDueDate()))
                .build();
    }
}