package com.testApi.demoApi.service.impl;

import com.testApi.demoApi.dto.permissionDto.PermissionRequest;
import com.testApi.demoApi.dto.permissionDto.PermissionResponse;
import com.testApi.demoApi.entity.Permission;
import com.testApi.demoApi.mapper.PermissionMapper;
import com.testApi.demoApi.repository.PermissionRepository;
import com.testApi.demoApi.service.PermissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PermissionServiceImpl implements PermissionService {

    private final PermissionRepository permissionRepository;
    private final PermissionMapper permissionMapper;

    @Override
    public PermissionResponse createPermission(PermissionRequest permissionRequest) {
        Permission permission = permissionMapper.toPermission(permissionRequest);
        permission = permissionRepository.save(permission);
        return permissionMapper.toPermissionResponse(permission);
    }

    @Override
    public List<PermissionResponse> getPermissions() {
        List<Permission> permissions = permissionRepository.findAll();
        return permissions.stream().map(permissionMapper::toPermissionResponse).toList();
    }

    @Override
    public void deletePermissionById(String id) {
        permissionRepository.deleteById(id);
    }
}
