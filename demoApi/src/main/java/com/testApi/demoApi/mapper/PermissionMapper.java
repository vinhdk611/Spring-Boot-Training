package com.testApi.demoApi.mapper;

import com.testApi.demoApi.dto.permissionDto.PermissionRequest;
import com.testApi.demoApi.dto.permissionDto.PermissionResponse;
import com.testApi.demoApi.entity.Permission;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PermissionMapper {

    PermissionResponse toPermissionResponse(Permission permission);

    Permission toPermission(PermissionRequest permissionRequest);

}
