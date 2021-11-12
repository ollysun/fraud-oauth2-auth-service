package com.etz.authorisationserver.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.etz.authorisationserver.dto.request.CreatePermissionRequest;
import com.etz.authorisationserver.dto.response.PermissionEntityResponse;
import com.etz.authorisationserver.entity.PermissionEntity;
import com.etz.authorisationserver.entity.RolePermission;
import com.etz.authorisationserver.entity.UserPermission;
import com.etz.authorisationserver.exception.AuthServiceException;
import com.etz.authorisationserver.exception.ResourceNotFoundException;
import com.etz.authorisationserver.repository.PermissionRepository;
import com.etz.authorisationserver.repository.RolePermissionRepository;
import com.etz.authorisationserver.repository.UserPermissionRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class PermissionService {

    @Autowired
    private PermissionRepository iPermissionRepository;

    @Autowired
    private UserPermissionRepository userPermissionRepository;

    @Autowired
    private RolePermissionRepository rolePermissionRepository;

    //@PreAuthorize("hasAuthority('PERMISSION.READ')")
    @Transactional(readOnly = true)
    public List<PermissionEntityResponse> getAllPermissions(Long permissionId, Boolean activatedStatus) {
        List<PermissionEntity> permissionEntityList = new ArrayList<>();
        if (permissionId != null){
            PermissionEntity permissionEntity = iPermissionRepository.findById(permissionId)
                    .orElseThrow(() -> new ResourceNotFoundException("PermissionEntity Not found " + permissionId));
            permissionEntityList.add(permissionEntity);
        } else if (Boolean.TRUE.equals(activatedStatus)) {
            permissionEntityList.addAll(iPermissionRepository.findByStatus(true));
        }else{
            permissionEntityList = iPermissionRepository.findAll();
        }
        return getPermissionResponse(permissionEntityList);
    }

    private List<PermissionEntityResponse> getPermissionResponse(List<PermissionEntity> permissionEntityList){
        List<PermissionEntityResponse> permissionResponseList = new ArrayList<>();
        permissionEntityList.forEach(permissionListObject -> {
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

    //@PreAuthorize("hasAuthority('PERMISSION.CREATE')")
    @Transactional(rollbackFor = Throwable.class)
    public PermissionEntityResponse addPermission(CreatePermissionRequest createPermissionRequest) {
        PermissionEntity permissionEntity = new PermissionEntity();
        permissionEntity.setName(createPermissionRequest.getName());
        permissionEntity.setStatus(Boolean.TRUE);
        permissionEntity.setCreatedBy(createPermissionRequest.getCreatedBy());


        PermissionEntity createPermissionEntity = iPermissionRepository.save(permissionEntity);

        return PermissionEntityResponse.builder()
                    .permissionId(createPermissionEntity.getId())
                    .name(createPermissionEntity.getName())
                    .status(createPermissionEntity.getStatus())
                    .createdBy(createPermissionEntity.getCreatedBy())
                    .createdAt(createPermissionEntity.getCreatedAt())
                    .build();
    }

   // @PreAuthorize("hasAuthority('PERMISSION.DELETE')")
    @Transactional(rollbackFor = Throwable.class)
    public Boolean deletePermissionInTransaction(Long permissionId) {
        PermissionEntity permissionEntity = iPermissionRepository.findById(permissionId)
                                                                 .orElseThrow(() -> new ResourceNotFoundException("Permission details not found " + permissionId));
        List<UserPermission> userPermissionList = userPermissionRepository.findByPermissionId(permissionId);

        List<RolePermission> rolePermissionList = rolePermissionRepository.findByPermissionId(permissionId);

        try{
            iPermissionRepository.deleteByPermissionId(permissionEntity.getId());
            userPermissionList.forEach(userPermission -> userPermissionRepository.deleteByPermissionId(userPermission.getPermissionId()));
            rolePermissionList.forEach(rolePermission -> rolePermissionRepository.deleteByPermissionId(rolePermission.getPermissionId()));
        } catch (Exception ex) {
            log.error("Error occurred while deactivating Permission entity from database", ex);
            throw new AuthServiceException("Error deleting User entity and relation from the database " + ex.getMessage());
        }

        return Boolean.TRUE;
    }
}
