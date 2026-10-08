package com.testApi.demoApi.service;


import com.testApi.demoApi.dto.roleDto.RoleRequest;
import com.testApi.demoApi.dto.roleDto.RoleResponse;

import java.util.List;

public interface RoleService {
    RoleResponse createRole(RoleRequest roleRequest);
    List<RoleResponse> getRoles();
    void deleteRoleById(String id);
}
