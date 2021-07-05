package com.etz.authorisationserver.services;

import com.etz.authorisationserver.dto.request.CreatePermissionRequest;
import com.etz.authorisationserver.dto.response.PermissionEntityResponse;
import com.etz.authorisationserver.dto.response.RoleResponse;
import com.etz.authorisationserver.entity.Permission;
import com.etz.authorisationserver.entity.Role;
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

    public List<PermissionEntityResponse> getAllPermissions(Long permissionId, Boolean activatedStatus) {
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
        return getPermissionResponse(permissionList);
    }

    private List<PermissionEntityResponse> getPermissionResponse(List<Permission> permissionList){
        List<PermissionEntityResponse> permissionResponseList = new ArrayList<>();
        permissionList.forEach(permissionListObject -> {
            PermissionEntityResponse permissionResponse = PermissionEntityResponse.builder()
                    .permissionId(permissionListObject.getId())
                    .name(permissionListObject.getName())
                    .status(permissionListObject.getStatus())
                    .createdBy(permissionListObject.getCreatedBy())
                    .createdAt(permissionListObject.getCreatedAt())
                    .build();
            permissionResponseList.add(permissionResponse);
        });
        return permissionResponseList;
    }

    public PermissionEntityResponse createPermission(CreatePermissionRequest createPermissionRequest) {
        Permission permission = new Permission();
        permission.setName(createPermissionRequest.getName());
        permission.setStatus(Boolean.TRUE);
        permission.setCreatedBy(createPermissionRequest.getCreatedBy());

        Permission createPermission = iPermissionRepository.save(permission);

        return PermissionEntityResponse.builder()
                    .permissionId(createPermission.getId())
                    .name(createPermission.getName())
                    .status(createPermission.getStatus())
                    .createdBy(createPermission.getCreatedBy())
                    .createdAt(createPermission.getCreatedAt())
                    .build();
    }
}
