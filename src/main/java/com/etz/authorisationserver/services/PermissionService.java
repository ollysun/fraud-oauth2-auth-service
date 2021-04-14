package com.etz.authorisationserver.services;

import com.etz.authorisationserver.dto.request.CreatePermissionRequest;
import com.etz.authorisationserver.dto.request.CreateUserRequest;
import com.etz.authorisationserver.entity.Permission;
import com.etz.authorisationserver.entity.User;
import com.etz.authorisationserver.repository.IPermissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PermissionService {

    @Autowired
    private IPermissionRepository iPermissionRepository;

    public List<Permission> getAllPermissions() {
        List<Permission> permissionList;
        permissionList = iPermissionRepository.findAll();
        return permissionList;
    }

    public Permission createPermission(CreatePermissionRequest createPermissionRequest) {
        return iPermissionRepository.save(createPermissionRequest);
    }
}
