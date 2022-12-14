package com.etz.authorisationserver.services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.etz.authorisationserver.constant.AppConstant;
import com.etz.authorisationserver.dto.request.ApprovalRequest;
import com.etz.authorisationserver.dto.request.CreateRoleRequest;
import com.etz.authorisationserver.dto.request.UpdateRoleRequest;
import com.etz.authorisationserver.dto.response.RoleResponse;
import com.etz.authorisationserver.entity.PermissionEntity;
import com.etz.authorisationserver.entity.Role;
import com.etz.authorisationserver.entity.RolePermission;
import com.etz.authorisationserver.entity.UserRole;
import com.etz.authorisationserver.exception.AuthServiceException;
import com.etz.authorisationserver.exception.ResourceNotFoundException;
import com.etz.authorisationserver.repository.PermissionRepository;
import com.etz.authorisationserver.repository.RolePermissionRepository;
import com.etz.authorisationserver.repository.RoleRepository;
import com.etz.authorisationserver.repository.UserRoleRepository;
import com.etz.authorisationserver.util.AppUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class RoleService {

    private final RoleRepository roleRepository;

    private final RolePermissionRepository rolePermissionRepository;

    private final PermissionRepository permissionRepository;

    private final UserRoleRepository userRoleRepository;

    private final AppUtil appUtil;

    @PreAuthorize("hasAnyAuthority('ROLE.CREATE','ROLE.APPROVE')")
    @Transactional(rollbackFor = Throwable.class)
    public RoleResponse<Long> addRole(CreateRoleRequest createRoleRequest) {
        Role role = new Role();
        role.setName(createRoleRequest.getRoleName());
        role.setDescription(createRoleRequest.getDescription());
        role.setStatus(Boolean.TRUE);
        role.setCreatedBy(createRoleRequest.getCreatedBy());
    //    Role createdRole = new Role();
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

        
        RoleResponse<Long> roleResponse = RoleResponse.<Long>builder()
	                                    .roleId(createdRole.getId())
	                                    .roleName(role.getName())
	                                    .description(role.getDescription())
	                                    .status(role.getStatus())
	                                    //.permissions(getPermissionNamesFromPermissionTable(createRoleRequest.getPermissionList()))
	                                    .permissions(createRoleRequest.getPermissionList())
	                                    .createdBy(createRoleRequest.getCreatedBy())
	                                    .createdAt(role.getCreatedAt())
	                                    .build();
        
        // create user notification
        appUtil.createUserNotification(AppConstant.ROLE, String.valueOf(createdRole.getId()), createRoleRequest.getCreatedBy());
        
        return roleResponse;
    }

    List<String> getPermissionNamesFromPermissionTable(List<Long> permissionIds){
        List<String> permissionNameList = new ArrayList<>();
        permissionIds.forEach(permissionId -> permissionNameList.add(permissionRepository.getOne(permissionId).getName()));
        return permissionNameList;
    }


    @PreAuthorize("hasAnyAuthority('ROLE.UPDATE','ROLE.APPROVE')")
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
       // Role updatedRole = new Role();
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
        
        // create user notification
        appUtil.createUserNotification(AppConstant.ROLE, String.valueOf(updatedRole.getId()), updateRoleRequest.getUpdatedBy());
        
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
            commonElements.forEach(rolePermissionRepository::deleteByPermissionIdPermanent);
        }
    }

   // @PreAuthorize("hasAuthority('ROLE.READ')")
    @Transactional(readOnly = true)
    public List<RoleResponse<String>> getRoles(Long roleId, Boolean activatedStatus) {
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

    private List<RoleResponse<String>> getRoleResponse(List<Role> roleList){
        List<RoleResponse<String>> roleResponseList = new ArrayList<>();
        roleList.forEach(roleListObject -> {
            RoleResponse<String> roleResponse = RoleResponse.<String>builder()
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
        rolePermissionList.forEach(rolePermissionObject -> permissionsList.add(permissionRepository.getOne(rolePermissionObject.getPermissionId()).getName()));
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
    
    //@PreAuthorize("hasAuthority('ROLE.APPROVE')")
	public RoleResponse<String> updateRoleAuthoriser(ApprovalRequest request) {
		Optional<Role> roleOptional = roleRepository.findById(Long.valueOf(request.getEntityId()));
		if(!roleOptional.isPresent()) {
            log.error("Role not found for ID " + Long.valueOf(request.getEntityId()));
			throw new ResourceNotFoundException("Role not found for ID " + Long.valueOf(request.getEntityId()));
		}
		Role role = roleOptional.get();
		role.setAuthorised(request.getAction().equalsIgnoreCase(AppConstant.APPROVE_ACTION) ? Boolean.TRUE : Boolean.FALSE);
		role.setAuthoriser(request.getCreatedBy());
		role.setUpdatedBy(request.getCreatedBy());
		Role updatedRole = roleRepository.save(role);
		
		return RoleResponse.<String>builder()
                .roleId(updatedRole.getId())
                .roleName(updatedRole.getName())
                .description(updatedRole.getDescription())
                .status(updatedRole.getStatus())
                .createdBy(updatedRole.getCreatedBy())
                .createdAt(updatedRole.getCreatedAt())
                .build();
	}
	
	//@PreAuthorize("hasAuthority('ROLE.CREATE')")
	public List<Long> getRoleIdsByPermissionNane(String permissionName){
		// get permission ID for entity with approve permission
		PermissionEntity permissionEntity = permissionRepository.findByName(permissionName.toUpperCase());
    	if(Objects.isNull(permissionEntity))  {
            log.error("Permission not found for permission name " + permissionName.toUpperCase());
    		throw new ResourceNotFoundException("Permission not found for permission name " + permissionName.toUpperCase());
    	}
    	
    	// get all role(s) that have been assigned the approval permission ID gotten from above
    	List<Long> roleIdsHavingEntityApprovalPermissionId = rolePermissionRepository.findByPermissionId(permissionEntity.getId()).stream().map(RolePermission::getRoleId).collect(Collectors.toList());
    	if(roleIdsHavingEntityApprovalPermissionId.isEmpty()) {
            log.error("No Role has been assigned " + permissionName.toUpperCase() +" permission");
    		throw new ResourceNotFoundException("No Role has been assigned " + permissionName.toUpperCase() +" permission");
    	}
    	return roleIdsHavingEntityApprovalPermissionId;
	}
	
}
