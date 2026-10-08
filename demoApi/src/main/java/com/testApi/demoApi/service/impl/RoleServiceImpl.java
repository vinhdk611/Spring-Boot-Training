package com.testApi.demoApi.service.impl;

import com.testApi.demoApi.dto.permissionDto.PermissionResponse;
import com.testApi.demoApi.dto.roleDto.RoleRequest;
import com.testApi.demoApi.dto.roleDto.RoleResponse;
import com.testApi.demoApi.entity.Permission;
import com.testApi.demoApi.entity.Role;
import com.testApi.demoApi.exception.AppException;
import com.testApi.demoApi.exception.ErrorCode;
import com.testApi.demoApi.mapper.PermissionMapper;
import com.testApi.demoApi.mapper.RoleMapper;
import com.testApi.demoApi.repository.PermissionRepository;
import com.testApi.demoApi.repository.RoleRepository;
import com.testApi.demoApi.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;
    private final RoleMapper roleMapper;
    private final PermissionRepository permissionRepository;
    private final PermissionMapper permissionMapper;

    @Override
    public RoleResponse createRole(RoleRequest roleRequest) {
        Role role = roleMapper.toRole(roleRequest);
        Set<Permission> permissions = roleRequest.getPermissions().stream()
                .map((id) -> permissionRepository.findById(id)
                        .orElseThrow(() -> new AppException(ErrorCode.PERMISSION_NOT_FOUND))).collect(Collectors.toSet());
        role.setPermissions(permissions);
        role =  roleRepository.save(role);

        RoleResponse roleResponse = roleMapper.toRoleResponse(role);
        Set<PermissionResponse> permissionResponses = role.getPermissions()
                .stream().map(permissionMapper::toPermissionResponse).collect(Collectors.toSet());

        roleResponse.setPermissions(permissionResponses);

        return roleResponse;
    }

    @Override
    public List<RoleResponse> getRoles() {
        List<Role> roles = roleRepository.findAll();
        return roles.stream().map(roleMapper::toRoleResponse).collect(Collectors.toList());
    }

    @Override
    public void deleteRoleById(String id) {
        roleRepository.deleteById(id);
    }
}
