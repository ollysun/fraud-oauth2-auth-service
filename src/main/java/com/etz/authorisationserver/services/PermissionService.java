package com.etz.authorisationserver.services;

import com.etz.authorisationserver.dto.request.CreatePermissionRequest;
import com.etz.authorisationserver.dto.response.PermissionEntityResponse;
import com.etz.authorisationserver.entity.PermissionEntity;
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

    public PermissionEntityResponse createPermission(CreatePermissionRequest createPermissionRequest) {
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
}
