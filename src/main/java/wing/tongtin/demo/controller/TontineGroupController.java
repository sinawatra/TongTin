package wing.tongtin.demo.controller;

import wing.tongtin.demo.entity.TontineGroupEntity;
import wing.tongtin.demo.request.ContributeRequest;
import wing.tongtin.demo.request.CreateGroupRequest;
import wing.tongtin.demo.response.ApiResponse;
import wing.tongtin.demo.service.ContributionService;
import wing.tongtin.demo.service.TontineService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/groups")
@RequiredArgsConstructor
public class TontineGroupController {

    private final TontineService tontineService;
    private final ContributionService contributionService;

    @PostMapping
    public ApiResponse<?> createGroup(@RequestBody CreateGroupRequest request) {

        return ApiResponse.builder()
                .success(true)
                .message("Group created")
                .data(tontineService.createGroup(request))
                .build();
    }


    @GetMapping
    public  ApiResponse<?> getGroupListing() {

        return ApiResponse.builder()
                .success(true)
                .message("Sucess")
                .data(tontineService.getAllGroupListing())
                .build();

    }


    @GetMapping("/{id}")
    public ApiResponse<?> getGroupInfoById(@PathVariable String id) {
        TontineGroupEntity tontineGroup = tontineService.getGroupInFoById(id);
        return ApiResponse.builder()
                .success(true)
                .message("Success").data(tontineGroup)
                .build();
    }

    @PostMapping("/{id}/join")
    public ApiResponse<?> joinGroupByID(@PathVariable String id) {
        return ApiResponse.builder()
                .success(true)
                .message("Joined group successfully")
                .data(tontineService.joinGroupById(id))
                .build();
    }

    @GetMapping("/{groupId}/members")
    public ApiResponse<?> getGroupMembers(@PathVariable String groupId) {
        return ApiResponse.builder()
                .success(true)
                .message("Success")
                .data(tontineService.getGroupMembers(groupId))
                .build();
    }

    @PostMapping("/{groupId}/contribute")
    public ApiResponse<?> contribute(@PathVariable String groupId, @RequestBody ContributeRequest request) {
        return ApiResponse.builder()
                .success(true)
                .message("Contribution recorded successfully")
                .data(contributionService.contribute(groupId, request))
                .build();
    }

    @GetMapping("/{groupId}/contributions")
    public ApiResponse<?> getGroupContributions(@PathVariable String groupId) {
        return ApiResponse.builder()
                .success(true)
                .message("Success")
                .data(contributionService.getGroupContributions(groupId))
                .build();
    }

    @GetMapping("/{groupId}/contributions/{userId}")
    public ApiResponse<?> getUserContributions(@PathVariable String groupId, @PathVariable String userId) {
        return ApiResponse.builder()
                .success(true)
                .message("Success")
                .data(contributionService.getUserContributions(groupId, userId))
                .build();
    }

    @PostMapping("/{groupId}/cycle/payout")
    public ApiResponse<?> triggerPayout(@PathVariable String groupId) {
        return ApiResponse.builder()
                .success(true)
                .message("Payout processed successfully")
                .data(contributionService.triggerPayout(groupId))
                .build();
    }

    @GetMapping("/{groupId}/payouts")
    public ApiResponse<?> getGroupPayouts(@PathVariable String groupId) {
        return ApiResponse.builder()
                .success(true)
                .message("Success")
                .data(contributionService.getGroupPayouts(groupId))
                .build();
    }

}