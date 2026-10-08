package com.testApi.demoApi.mapper;

import com.testApi.demoApi.dto.roleDto.RoleRequest;
import com.testApi.demoApi.dto.roleDto.RoleResponse;
import com.testApi.demoApi.entity.Role;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RoleMapper {

    @Mapping(target = "permissions", ignore = true)
    RoleResponse toRoleResponse(Role role);

    @Mapping(target = "permissions", ignore = true)
    Role toRole(RoleRequest roleRequest);

}
