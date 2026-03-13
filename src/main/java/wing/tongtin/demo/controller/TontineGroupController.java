package wing.tongtin.demo.controller;

import  wing.tongtin.demo.request.CreateGroupRequest;
import  wing.tongtin.demo.response.ApiResponse;
import  wing.tongtin.demo.service.TontineService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping("/api/groups")
@RequiredArgsConstructor
public class TontineGroupController {

    private final TontineService tontineService;

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

}