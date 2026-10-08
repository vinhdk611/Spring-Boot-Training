package com.testApi.demoApi.service;

import com.testApi.demoApi.dto.permissionDto.PermissionRequest;
import com.testApi.demoApi.dto.permissionDto.PermissionResponse;

import java.util.List;

public interface PermissionService {
    PermissionResponse createPermission(PermissionRequest permissionRequest);
    List<PermissionResponse> getPermissions();
    void deletePermissionById(String id);
}
