package com.testApi.demoApi.controller;


import com.testApi.demoApi.dto.ApiResponse;
import com.testApi.demoApi.dto.permissionDto.PermissionRequest;
import com.testApi.demoApi.dto.permissionDto.PermissionResponse;
import com.testApi.demoApi.service.PermissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/permission")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class PermissionController {

    private final PermissionService permissionService;

    @PostMapping
    ApiResponse<PermissionResponse> create(@RequestBody PermissionRequest permissionRequest) {
        return ApiResponse.<PermissionResponse>builder()
                .code(200)
                .result(permissionService.createPermission(permissionRequest))
                .build();
    }

    @GetMapping
    ApiResponse<List<PermissionResponse>> getList() {
        return ApiResponse.<List<PermissionResponse>>builder()
                .code(200)
                .result(permissionService.getPermissions())
                .build();
    }

    @DeleteMapping("/{id}")
    ApiResponse<Void> deleteById(@PathVariable String id) {
        permissionService.deletePermissionById(id);
        return ApiResponse.<Void>builder()
                .code(200)
                .message("Delete Successfully")
                .build();
    }
}
