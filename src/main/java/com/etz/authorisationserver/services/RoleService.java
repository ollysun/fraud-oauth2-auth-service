package com.etz.authorisationserver.services;

import com.etz.authorisationserver.dto.request.CreateRoleRequest;
import com.etz.authorisationserver.dto.request.UpdateRoleRequest;
import com.etz.authorisationserver.dto.response.RoleResponse;
import com.etz.authorisationserver.entity.*;
import com.etz.authorisationserver.exception.AuthServiceException;
import com.etz.authorisationserver.exception.ResourceNotFoundException;
import com.etz.authorisationserver.repository.PermissionRepository;
import com.etz.authorisationserver.repository.RolePermissionRepository;
import com.etz.authorisationserver.repository.RoleRepository;
import com.etz.authorisationserver.repository.UserRoleRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private RolePermissionRepository rolePermissionRepository;

    @Autowired
    private PermissionRepository permissionRepository;

    @Autowired
    private UserRoleRepository userRoleRepository;

    //@PreAuthorize("hasAnyAuthority('ROLE.CREATE','ROLE.APPROVE')")
    @Transactional(rollbackFor = Throwable.class)
    public RoleResponse addRole(CreateRoleRequest createRoleRequest) {
        Role role = new Role();
        role.setName(createRoleRequest.getRoleName());
        role.setDescription(createRoleRequest.getDescription());
        role.setStatus(Boolean.TRUE);
        role.setCreatedBy(createRoleRequest.getCreatedBy());
        Role createdRole = roleRepository.save(role);
        if(!(createRoleRequest.getPermissionList().isEmpty())) {
            for (Long permissionId : createRoleRequest.getPermissionList()) {
                PermissionEntity permissionEntity = permissionRepository.findById(permissionId)
                        .orElseThrow(() -> new ResourceNotFoundException("Permission not found for this Id " + permissionId));
                if(permissionEntity != null) {
                    RolePermission rolePermission = new RolePermission();
                    rolePermission.setRoleId(createdRole.getId());
                    rolePermission.setPermissionId(permissionId);
                    rolePermission.setCreatedBy(createRoleRequest.getCreatedBy());
                    rolePermissionRepository.save(rolePermission);
                }
            }
        }

        return RoleResponse.builder()
                                    .roleId(role.getId())
                                    .roleName(role.getName())
                                    .description(role.getDescription())
                                    .status(role.getStatus())
                                    .permissions(getPermissionNamesFromPermissionTable(createRoleRequest.getPermissionList()))
                                    .createdBy(createRoleRequest.getCreatedBy())
                                    .createdAt(role.getCreatedAt())
                                    .build();
    }

    List<String> getPermissionNamesFromPermissionTable(List<Long> permissionIds){
        List<String> permissionNameList = new ArrayList<>();
        permissionIds.forEach(permissionId -> permissionNameList.add(permissionRepository.getOne(permissionId).getName()));
        return permissionNameList;
    }


    //@PreAuthorize("hasAnyAuthority('ROLE.UPDATE','ROLE.APPROVE')")
    @Transactional(rollbackFor = Throwable.class)
    public Boolean updateRole(UpdateRoleRequest updateRoleRequest) {
        Role roleOptional = new Role();
        if(updateRoleRequest.getRoleId() != null) {
            roleOptional = roleRepository.findById(updateRoleRequest.getRoleId())
                    .orElseThrow(() -> new ResourceNotFoundException("Role Not found " + updateRoleRequest.getRoleId()));

            roleOptional.setName(updateRoleRequest.getRoleName());
            roleOptional.setDescription(updateRoleRequest.getDescription());
            roleOptional.setStatus(updateRoleRequest.getStatus());
            roleOptional.setUpdatedBy(updateRoleRequest.getUpdatedBy());
        }
        Role updatedRole =  roleRepository.save(roleOptional);
        List<RolePermission> previousRolePermissionList = rolePermissionRepository.findByRoleId(updatedRole.getId());

        deleteExistingPermission(previousRolePermissionList,updateRoleRequest.getPermissions());

        updateRoleRequest.getPermissions().forEach(rolePermissionObject ->{
            PermissionEntity permissionEntity = permissionRepository.findById(rolePermissionObject)
                    .orElseThrow(() -> new ResourceNotFoundException("Permission not found for this Id " + rolePermissionObject));
            if(permissionEntity != null) {
                RolePermission rolePermission = new RolePermission();
                rolePermission.setRoleId(updatedRole.getId());
                rolePermission.setPermissionId(rolePermissionObject);
                rolePermission.setUpdatedBy(updateRoleRequest.getUpdatedBy());
                rolePermissionRepository.save(rolePermission);
            }
        });

        return true;
    }


    private void deleteExistingPermission(List<RolePermission> previousRolePermissionList, List<Long> permissionIds) {
        // get previous user permissions IDs
        List<Long> previousRolePermissionId = new ArrayList<>();
        previousRolePermissionList.forEach(rolePermEntity -> previousRolePermissionId.add(rolePermEntity.getPermissionId()));

        // check for common elements if exists
        if(Boolean.FALSE.equals(Collections.disjoint(previousRolePermissionId,permissionIds))){
            //bring out the common elements for delete
            List<Long> commonElements = previousRolePermissionId.stream()
                    .filter(permissionIds::contains)
                    .collect(Collectors.toList());
            commonElements.forEach(commonElement -> rolePermissionRepository.deleteByPermissionIdPermanent(commonElement));
        }
    }

   // @PreAuthorize("hasAuthority('ROLE.READ')")
    @Transactional(readOnly = true)
    public List<RoleResponse> getRoles(Long roleId, Boolean activatedStatus) {
        List<Role> roleList = new ArrayList<>();
        if (roleId != null) {
            roleList.add(roleRepository.findById(roleId).orElseThrow(() -> new ResourceNotFoundException("Role not found for id " + roleId)));
        } else if (Boolean.TRUE.equals(activatedStatus)) {
            roleList.addAll(roleRepository.findByStatus(true));
        } else {
            roleList = roleRepository.findAll();
        }
        return getRoleResponse(roleList);
    }

    private List<RoleResponse> getRoleResponse(List<Role> roleList){
        List<RoleResponse> roleResponseList = new ArrayList<>();
        roleList.forEach(roleListObject -> {
            RoleResponse roleResponse = RoleResponse.builder()
                    .roleId(roleListObject.getId())
                    .roleName(roleListObject.getName())
                    .description(roleListObject.getDescription())
                    .status(roleListObject.getStatus())
                    .permissions(getPermissions(roleListObject.getId()))
                    .createdBy(roleListObject.getCreatedBy())
                    .createdAt(roleListObject.getCreatedAt())
                    .build();
            roleResponseList.add(roleResponse);
        });
        return roleResponseList;
    }

    private List<String> getPermissions(Long roleId) {
        List<RolePermission> rolePermissionList = rolePermissionRepository.findByRoleId(roleId);
        List<String> permissionsList = new ArrayList<>();
        rolePermissionList.forEach(rolePermissionObject -> permissionsList.add(permissionRepository.getOne(rolePermissionObject.getRoleId()).getName()));
        return permissionsList;
    }

    //@PreAuthorize("hasAnyAuthority('ROLE.DELETE','ROLE.APPROVE')")
    @Transactional(rollbackFor = Throwable.class)
    public Boolean deleteRoleInTransaction(Long roleId) {
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new ResourceNotFoundException("Role Not found for this id = " + roleId));
        List<RolePermission> rolePermissionList = rolePermissionRepository.findByRoleId(roleId);
        UserRole userRole = userRoleRepository.findByRoleId(role.getId());
        try {
            roleRepository.deleteByRoleId(role.getId());
            if (userRole != null) {
                userRoleRepository.deleteByRoleId(userRole.getRoleId());
            }
            if(!rolePermissionList.isEmpty()) {
                for (RolePermission rolePermission : rolePermissionList) {
                    rolePermissionRepository.deleteByRoleId(rolePermission.getRoleId());
                }
            }
        } catch (Exception ex) {
            log.error("Error occurred while deleting User entity from database", ex);
            throw new AuthServiceException("Error deleting User entity and relation from the database " + ex.getMessage());
        }
        return Boolean.TRUE;
    }
}
