package com.etz.authorisationserver.services;

import com.etz.authorisationserver.dto.request.CreatePermissionRequest;
import com.etz.authorisationserver.entity.Permission;
import com.etz.authorisationserver.exception.ResourceNotFoundException;
import com.etz.authorisationserver.repository.PermissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class PermissionService {

    @Autowired
    private PermissionRepository iPermissionRepository;

    public List<Permission> getAllPermissions(Long permissionId, Boolean activatedStatus) {
        List<Permission> permissionList = new ArrayList<>();
        if (permissionId != null){
            Permission permission = iPermissionRepository.findById(permissionId)
                    .orElseThrow(() -> new ResourceNotFoundException("Permission Not found " + permissionId));
            permissionList.add(permission);
        } else if (Boolean.TRUE.equals(activatedStatus)) {
            permissionList.addAll(iPermissionRepository.findByStatus(true));
        }else{
            permissionList = iPermissionRepository.findAll();
        }
        return permissionList;
    }

    public Permission createPermission(CreatePermissionRequest createPermissionRequest) {
        return iPermissionRepository.save(createPermissionRequest);
    }
}
