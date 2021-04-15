package com.etz.authorisationserver.services;

import com.etz.authorisationserver.dto.request.CreateRoleRequest;
import com.etz.authorisationserver.dto.request.UpdateRoleRequest;
import com.etz.authorisationserver.dto.request.UpdateUserRequest;
import com.etz.authorisationserver.dto.response.RoleResponse;
import com.etz.authorisationserver.entity.Role;
import com.etz.authorisationserver.entity.RolePermissionEntity;
import com.etz.authorisationserver.entity.User;
import com.etz.authorisationserver.entity.UserPermission;
import com.etz.authorisationserver.repository.PermissionRepository;
import com.etz.authorisationserver.repository.RolePermissionRepository;
import com.etz.authorisationserver.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private RolePermissionRepository rolePermissionRepository;

    @Autowired
    private PermissionRepository permissionRepository;

    private Role role;
    private RolePermissionEntity rolePermissionEntity;

    @Transactional
    public Role createRole(CreateRoleRequest createRoleRequest) {
        role.setName(createRoleRequest.getRoleName());
        role.setDescription(createRoleRequest.getDescription());
        role.setStatus(Boolean.TRUE);
        role.setCreatedBy(createRoleRequest.getCreatedBy());
        Role createdRole = roleRepository.save(role);
        createRoleRequest.getPermissionList().forEach(permissionId -> {
            rolePermissionEntity.setRoleId(createdRole.getId());
            rolePermissionEntity.setPermissionId(permissionId);
            rolePermissionEntity.setCreatedBy(createRoleRequest.getCreatedBy());
            rolePermissionRepository.save(rolePermissionEntity);
        });
        return createdRole;
    }

    public Boolean updateRole(UpdateRoleRequest updateRoleRequest, Long roleId) {
        Optional<Role> roleOptional = roleRepository.findById(roleId);
        if (roleOptional.isPresent()){
            roleOptional.get().setName(updateRoleRequest.getRoleName());
            roleOptional.get().setDescription(updateRoleRequest.getDescription());
            roleOptional.get().setStatus(updateRoleRequest.getStatus());
            roleOptional.get().setUpdatedBy(updateRoleRequest.getUpdatedBy());
            roleRepository.save(roleOptional.get());
        }else{
            throw new RuntimeException("Not found");
        }
        List<RolePermissionEntity> rolePermissionList = rolePermissionRepository.findByRoleId(roleId);
        updateRoleRequest.getPermissions().forEach(rolePermissionObject ->{
            rolePermissionEntity.setRoleId(roleId);
            rolePermissionEntity.setPermissionId(rolePermissionObject);
            rolePermissionList.add(rolePermissionEntity);
        });
        rolePermissionRepository.saveAll(rolePermissionList);
        return true;
    }

    @Transactional(readOnly = true)
    public List<RoleResponse> getRoles(Long roleId, Boolean activatedStatus) {
        List<Role> roleList = new ArrayList<>();
        List<RoleResponse> roleResponseList;
        if (roleId != null) {
            roleList.add(roleRepository.findById(roleId).orElseThrow(() -> new RuntimeException("not found")));
            roleResponseList = getRoleResponse(roleList);
        } else if (Boolean.TRUE.equals(activatedStatus)) {
            roleList.addAll(roleRepository.findByStatus(true));
            roleResponseList = getRoleResponse(roleList);
        } else {
            roleList = roleRepository.findAll();
            roleResponseList = getRoleResponse(roleList);
        }
        return roleResponseList;
    }

    private List<RoleResponse> getRoleResponse(List<Role> roleList){
        List<RoleResponse> roleResponseList = new ArrayList<>();
        RoleResponse roleResponse = new RoleResponse();
        roleList.forEach(roleListObject -> {
            roleResponse.setRoleId(roleListObject.getId());
            roleResponse.setRoleName(roleListObject.getName());
            roleResponse.setDescription(roleListObject.getDescription());
            roleResponse.setStatus(roleListObject.getStatus());
            roleResponse.setPermissions(getPermissions(roleListObject.getId()));
            roleResponse.setCreatedBy(roleListObject.getCreatedBy());
            roleResponse.setCreatedAt(roleListObject.getCreatedAt());
            roleResponseList.add(roleResponse);
        });
        return roleResponseList;
    }

    private List<String> getPermissions(Long roleId) {
        List<RolePermissionEntity> rolePermissionList = rolePermissionRepository.findByRoleId(roleId);
        List<String> permissionsList = new ArrayList<>();
        rolePermissionList.forEach(rolePermissionObject -> {
            permissionsList.add(permissionRepository.getOne(rolePermissionObject.getRoleId()).getName());
        });
        return permissionsList;
    }
}
