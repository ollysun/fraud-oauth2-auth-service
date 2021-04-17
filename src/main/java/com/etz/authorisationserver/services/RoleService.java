package com.etz.authorisationserver.services;

import com.etz.authorisationserver.dto.request.CreateRoleRequest;
import com.etz.authorisationserver.dto.request.UpdateRoleRequest;
import com.etz.authorisationserver.dto.response.RoleResponse;
import com.etz.authorisationserver.entity.Role;
import com.etz.authorisationserver.entity.RolePermission;
import com.etz.authorisationserver.exception.ResourceNotFoundException;
import com.etz.authorisationserver.repository.PermissionRepository;
import com.etz.authorisationserver.repository.RolePermissionRepository;
import com.etz.authorisationserver.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private RolePermissionRepository rolePermissionRepository;

    @Autowired
    private PermissionRepository permissionRepository;


    @Transactional
    public RoleResponse createRole(CreateRoleRequest createRoleRequest) {
        RolePermission rolePermission = new RolePermission();
        Role role = new Role();
        role.setName(createRoleRequest.getRoleName());
        role.setDescription(createRoleRequest.getDescription());
        role.setStatus(Boolean.TRUE);
        role.setCreatedBy(createRoleRequest.getCreatedBy());
        Role createdRole = roleRepository.save(role);
        createRoleRequest.getPermissionList().forEach(permissionId -> {
            rolePermission.setRoleId(createdRole.getId());
            rolePermission.setPermissionId(permissionId);
            rolePermission.setCreatedBy(createRoleRequest.getCreatedBy());
            rolePermissionRepository.save(rolePermission);
        });

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

    public Boolean updateRole(UpdateRoleRequest updateRoleRequest, Long roleId) {
        RolePermission rolePermission = new RolePermission();
        List<RolePermission> newRolePermissionList = new ArrayList<>();
        Role roleOptional = new Role();
        if(roleId != null) {
            roleOptional = roleRepository.findById(roleId)
                    .orElseThrow(() -> new ResourceNotFoundException("Role Not found " + roleId));

            roleOptional.setName(updateRoleRequest.getRoleName());
            roleOptional.setDescription(updateRoleRequest.getDescription());
            roleOptional.setStatus(updateRoleRequest.getStatus());
            roleOptional.setUpdatedBy(updateRoleRequest.getUpdatedBy());
        }
        Role updatedRole =  roleRepository.save(roleOptional);

        List<RolePermission> previousRolePermissionList = rolePermissionRepository.findByRoleId(roleId);
        updateRoleRequest.getPermissions().forEach(rolePermissionObject ->{
            rolePermission.setRoleId(updatedRole.getId());
            rolePermission.setPermissionId(rolePermissionObject);
            rolePermission.setUpdatedBy(updateRoleRequest.getUpdatedBy());
            newRolePermissionList.add(rolePermission);
        });

        //to remove duplicates
        Set<RolePermission> setOfRolePermission = new LinkedHashSet<>(previousRolePermissionList);
        setOfRolePermission.addAll(newRolePermissionList);
        List<RolePermission> combinedList = new ArrayList<>(setOfRolePermission);
        rolePermissionRepository.saveAll(combinedList);
        return true;
    }

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
}
