package com.testApi.demoApi.controller;

import com.testApi.demoApi.dto.ApiResponse;
import com.testApi.demoApi.dto.roleDto.RoleRequest;
import com.testApi.demoApi.dto.roleDto.RoleResponse;
import com.testApi.demoApi.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/role")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class RoleController {
    private final RoleService roleService;

    @PostMapping
    ApiResponse<RoleResponse> create(@RequestBody RoleRequest roleRequest) {
        return ApiResponse.<RoleResponse>builder()
                .code(200)
                .result(roleService.createRole(roleRequest))
                .build();
    }

    @GetMapping
    ApiResponse<List<RoleResponse>> getList() {
        return ApiResponse.<List<RoleResponse>>builder()
                .code(200)
                .result(roleService.getRoles())
                .build();
    }

    @DeleteMapping("/{id}")
    ApiResponse<Void> deleteById(@PathVariable String id) {
        roleService.deleteRoleById(id);
        return ApiResponse.<Void>builder()
                .code(200)
                .message("Delete Successfully")
                .build();
    }
}
