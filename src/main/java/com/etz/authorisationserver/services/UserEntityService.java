package com.etz.authorisationserver.services;

import com.etz.authorisationserver.dto.request.CreateUserRequest;
import com.etz.authorisationserver.dto.request.UpdateUserRequest;
import com.etz.authorisationserver.dto.response.UserResponse;
import com.etz.authorisationserver.entity.*;
import com.etz.authorisationserver.exception.AuthServiceException;
import com.etz.authorisationserver.exception.ResourceNotFoundException;
import com.etz.authorisationserver.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.etz.authorisationserver.constant.AppConstant;
import com.etz.authorisationserver.dto.request.ApprovalRequest;
import com.etz.authorisationserver.dto.request.CreateUserRequest;
import com.etz.authorisationserver.dto.request.UpdateUserRequest;
import com.etz.authorisationserver.dto.response.UserResponse;
import com.etz.authorisationserver.entity.PermissionEntity;
import com.etz.authorisationserver.entity.Role;
import com.etz.authorisationserver.entity.UserEntity;
import com.etz.authorisationserver.entity.UserPermission;
import com.etz.authorisationserver.entity.UserRole;
import com.etz.authorisationserver.exception.AuthServiceException;
import com.etz.authorisationserver.exception.ResourceNotFoundException;
import com.etz.authorisationserver.repository.PermissionRepository;
import com.etz.authorisationserver.repository.RoleRepository;
import com.etz.authorisationserver.repository.UserPermissionRepository;
import com.etz.authorisationserver.repository.UserRepository;
import com.etz.authorisationserver.repository.UserRoleRepository;
import com.etz.authorisationserver.util.AppUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserEntityService {

    private final UserRepository userRepository;

    private final UserPermissionRepository userPermissionRepository;

    private final UserRoleRepository userRoleRepository;

    private final PermissionRepository permissionRepository;

    private final RoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder;

    private final AppUtil appUtil;

    @PreAuthorize("hasAnyAuthority('USER.CREATE','USER.APPROVE')")
    @Transactional(rollbackFor = Throwable.class)
    public UserResponse createUser(CreateUserRequest createUserRequest){
        List<UserPermission> userPermissionList = new ArrayList<>();

        if(userRepository.existsByUsernameOrEmailOrPhoneOrPassword(createUserRequest.getUsername(),
                createUserRequest.getEmail(), createUserRequest.getPhone(), createUserRequest.getPassword())){
            throw new AuthServiceException("Similar record exist for username " +  createUserRequest.getUsername());
        }

        createUserRequest.setStatus(Boolean.TRUE);
        createUserRequest.setPassword(passwordEncoder.encode(createUserRequest.getPassword()));

        UserEntity userRequest = new UserEntity();
        userRequest.setFirstName(createUserRequest.getFirstname());
        userRequest.setPassword(createUserRequest.getPassword());
        userRequest.setUsername(createUserRequest.getUsername());
        userRequest.setLastName(createUserRequest.getLastname());
        userRequest.setStatus(createUserRequest.getStatus());
        userRequest.setPhone(createUserRequest.getPhone());
        userRequest.setEmail(createUserRequest.getEmail());
        userRequest.setCreatedBy(createUserRequest.getCreatedBy());

        UserEntity user = userRepository.save(userRequest);
        if (Boolean.compare(createUserRequest.getHasRole(), Boolean.TRUE) == 0
                && Objects.nonNull(createUserRequest.getRoleId())){
            Role roleEntity = roleRepository.findById(createUserRequest.getRoleId())
                    .orElseThrow(() -> new ResourceNotFoundException("Role not found for this Id " + createUserRequest.getRoleId()));
            if(roleEntity != null) {
                UserRole userRole = new UserRole();
                userRole.setRoleId(createUserRequest.getRoleId());
                userRole.setUserId(user.getId());
                userRole.setCreatedBy(createUserRequest.getCreatedBy());
                userRoleRepository.save(userRole);
            }
        }

        if (Boolean.compare(createUserRequest.getHasPermission(), Boolean.TRUE) == 0
                && !(createUserRequest.getPermissionIds().isEmpty())){
            createUserRequest.getPermissionIds().forEach(permissionId -> {
                PermissionEntity permissionEntity = permissionRepository.findById(permissionId)
                        .orElseThrow(() -> new ResourceNotFoundException("Permission not found for this Id " + permissionId));
                if(permissionEntity != null) {
                    UserPermission userPermission = new UserPermission();
                    userPermission.setUserId(user.getId());
                    userPermission.setPermissionId(permissionId);
                    userPermission.setCreatedBy(createUserRequest.getCreatedBy());
                    userPermissionList.add(userPermission);
                }
            });
            userPermissionRepository.saveAll(userPermissionList);
            log.info("userpermission " + userPermissionList);
        }
        
       UserResponse userResponse = outputUserResponse(user, createUserRequest);

        // todo: sent mail notification to user under the role approval
        // create user notification
       appUtil.createUserNotification(AppConstant.USER, String.valueOf(user.getId()), createUserRequest.getCreatedBy());
       
       return userResponse;
    }


    private UserResponse outputUserResponse(UserEntity user, CreateUserRequest createUserRequest){
        return UserResponse.builder()
                           .userId(user.getId())
                           .userName(user.getUsername())
                           .status(user.getStatus())
                           .firstName(user.getFirstName())
                           .lastName(user.getLastName())
                           .phone(user.getPhone())
                           .email(user.getEmail())
                           .hasRole(user.getHasRole())
                           .roleId(createUserRequest.getRoleId())
                           .hasPermission(user.getHasPermission())
                           .permissionNames(getPermissionName(createUserRequest.getPermissionIds()))
                           .createdBy(user.getCreatedBy())
                           .createdAt(user.getCreatedAt())
                           .build();
    }

    // get permissions Names
    public List<String> getPermissionName(List<Long> permissionList){
        List<String> permissionNames = new ArrayList<>();
        permissionList.forEach(permissionId -> permissionNames.add(permissionRepository.getOne(permissionId).getName()));
        return permissionNames;
    }

    @PreAuthorize("hasAnyAuthority('USER.UPDATE','USER.APPROVE')")
    @Transactional(rollbackFor = Throwable.class)
    public Boolean updateUser(UpdateUserRequest updateUserRequest){

        UserEntity user = new UserEntity();
        if(updateUserRequest.getUserId() != null) {
            user = userRepository.findById(updateUserRequest.getUserId())
                    .orElseThrow(() -> new ResourceNotFoundException("UserEntity Not found " + updateUserRequest.getUserId()));
            user.setUsername(updateUserRequest.getUsername());
            user.setFirstName(updateUserRequest.getFirstname());
            user.setLastName(updateUserRequest.getLastname());
            user.setPhone(updateUserRequest.getPhone());
            user.setEmail(updateUserRequest.getEmail());
            user.setHasRole(updateUserRequest.getHasRole());
            user.setHasPermission(updateUserRequest.getHasPermission());
            user.setStatus(updateUserRequest.getStatus());
            user.setUpdatedBy(updateUserRequest.getUpdatedBy());
        }
        UserEntity updatedUser = userRepository.save(user);
        if (Boolean.TRUE.equals(updateUserRequest.getHasRole())

                && Objects.nonNull(updateUserRequest.getRoleId())){

            Role roleEntity = roleRepository.findById(updateUserRequest.getRoleId())
                    .orElseThrow(() -> new ResourceNotFoundException("Role not found for this Id " + updateUserRequest.getRoleId()));

            if(Objects.nonNull(roleEntity)) {
                //delete previous role to enforce one user and one role
                UserRole deletedPreviousUserRole = userRoleRepository.findByUserId(updateUserRequest.getUserId());
                if(deletedPreviousUserRole != null) {
                    userRoleRepository.delete(deletedPreviousUserRole);
                }
                UserRole previousUserRoleList = new UserRole();
                previousUserRoleList.setRoleId(updateUserRequest.getRoleId());
                previousUserRoleList.setUserId(updateUserRequest.getUserId());
                previousUserRoleList.setUpdatedBy(updateUserRequest.getUpdatedBy());
                userRoleRepository.save(previousUserRoleList);
            }
        }

        if (Boolean.compare(updateUserRequest.getHasPermission(), Boolean.TRUE) == 0
                && !(updateUserRequest.getPermissionIds().isEmpty())) {
            List<UserPermission> previousUserPermissionList = userPermissionRepository.findByUserId(updatedUser.getId());

            deleteExistingUserPermission(previousUserPermissionList, updateUserRequest.getPermissionIds());

            for (Long permissionId : updateUserRequest.getPermissionIds()) {
                PermissionEntity permissionEntity = permissionRepository.findById(permissionId)
                        .orElseThrow(() -> new ResourceNotFoundException("Permission not found for this Id " + permissionId));
                if(permissionEntity != null) {
                    UserPermission userPermission = new UserPermission();
                    userPermission.setUserId(updatedUser.getId());
                    userPermission.setPermissionId(permissionId);
                    userPermission.setUpdatedBy(updateUserRequest.getUpdatedBy());
                    userPermissionRepository.save(userPermission);
                }
            }

        }
        //todo send mail notification to user with approval role on userupdated
        // create user notification
        appUtil.createUserNotification(AppConstant.USER, String.valueOf(user.getId()), updateUserRequest.getUpdatedBy());
        return true;
    }

    private void deleteExistingUserPermission(List<UserPermission> previousUserPerm, List<Long> permissionIds) {
        // get previous user permissions IDs
        List<Long> previousUserPermissionIds = new ArrayList<>();
        previousUserPerm.forEach(userPermEntity -> previousUserPermissionIds.add(userPermEntity.getPermissionId()));
        // check for common elements
        if(Boolean.FALSE.equals(Collections.disjoint(previousUserPermissionIds,permissionIds))){
            //bring out the common elements for delete
            List<Long> commonElements = previousUserPermissionIds.stream()
                                                                 .filter(permissionIds::contains)
                                                                 .collect(Collectors.toList());
            commonElements.forEach(userPermissionRepository::deleteByPermissionIdPermanent);
        }
    }

    @PreAuthorize("hasAuthority('USER.READ')")
    @Transactional(readOnly = true)
    public List<UserResponse> getAllUsers(Long userId, Boolean activatedStatus){
        List<UserEntity> userList = new ArrayList<>();
        List<UserResponse> userResponseList;
        if (userId != null){
            UserEntity user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("UserEntity Not found " + userId));
            userList.add(user);
        } else if (Boolean.TRUE.equals(activatedStatus)) {
            userList.addAll(userRepository.findByStatus(true));
        }else{
            userList = userRepository.findAll();
        }
        userResponseList = assignUserResponseList(userList);
        return userResponseList;
    }

    private List<String> getUserPermissions(Long userId) {
        List<UserPermission> userPermissionList = userPermissionRepository.findByUserId(userId);
        if(userPermissionList.isEmpty()){
            return Collections.emptyList();
        }
        List<String> permissionsList = new ArrayList<>();
        userPermissionList.forEach(userPermissionListId -> permissionsList.add(permissionRepository.getOne(userPermissionListId.getPermissionId()).getName()));
        return permissionsList;
    }


    private List<UserResponse> assignUserResponseList(List<UserEntity> userList) {
        List<UserResponse> userResponseList = new ArrayList<>();
        for (UserEntity userListObject : userList) {
            UserResponse userResponse = UserResponse.builder()
                    .userId(userListObject.getId())
                    .email(userListObject.getEmail())
                    .firstName(userListObject.getFirstName())
                    .lastName(userListObject.getLastName())
                    .hasPermission(userListObject.getHasPermission())
                    .hasRole(userListObject.getHasRole())
                    .phone(userListObject.getPhone())
                    .roleId(getUserRoleId(userListObject.getId()))
                    .permissionNames(getUserPermissions(userListObject.getId()))
                    .status(userListObject.getStatus())
                    .userName(userListObject.getUsername())
                    .createdBy(userListObject.getCreatedBy())
                    .createdAt(userListObject.getCreatedAt())
                    .build();
            userResponseList.add(userResponse);
        }
        return userResponseList;
    }

    private Long getUserRoleId(Long userId){
        UserRole userRole = userRoleRepository.findByUserId(userId);
        if (userRole == null){
            return null;
        }
        return userRole.getRoleId();
    }

    @PreAuthorize("hasAnyAuthority('USER.DELETE','USER.APPROVE')")
    @Transactional(rollbackFor = Throwable.class)
    public Boolean deleteUserInTransaction(Long userId) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("UserEntity Not found for this id = " + userId));
        UserRole userRole = userRoleRepository.findByUserId(user.getId());
        List<UserPermission> userPermissionList = userPermissionRepository.findByUserId(user.getId());
        try {
            userRepository.deleteByUserId(user.getId());
            if (userRole != null) {
                userRoleRepository.deleteByUserId(userRole.getUserId());
            }
            if (!userPermissionList.isEmpty()) {
                userPermissionRepository.deleteByUserId(userPermissionList.get(0).getUserId());
            }
        } catch (Exception ex) {
            log.error("Error occurred while deleting User entity from database", ex);
            throw new AuthServiceException("Error deleting User entity and relation from the database " + ex.getMessage());
        }
        return Boolean.TRUE;
    }

    @PreAuthorize("hasAuthority('USER.APPROVE')")
	public UserResponse updateUserAuthoriser(ApprovalRequest request) {
		Optional<UserEntity> userOptional = userRepository.findById(Long.valueOf(request.getEntityId()));
		if(!userOptional.isPresent()) {
			throw new ResourceNotFoundException("UserEntity not found for ID " + Long.valueOf(request.getEntityId()));
		}
		UserEntity userEntity = userOptional.get();
		userEntity.setAuthorised(request.getAction().equalsIgnoreCase(AppConstant.APPROVE_ACTION) ? Boolean.TRUE : Boolean.FALSE);
		userEntity.setAuthoriser(request.getCreatedBy());
		userEntity.setUpdatedBy(request.getCreatedBy());
		UserEntity updatedUser = userRepository.save(userEntity);
		
		return UserResponse.builder()
                .userId(updatedUser.getId())
                .userName(updatedUser.getUsername())
                .status(updatedUser.getStatus())
                .firstName(updatedUser.getFirstName())
                .lastName(updatedUser.getLastName())
                .phone(updatedUser.getPhone())
                .email(updatedUser.getEmail())
                .hasRole(updatedUser.getHasRole())
                .hasPermission(updatedUser.getHasPermission())
                .createdBy(updatedUser.getCreatedBy())
                .createdAt(updatedUser.getCreatedAt())
                .build();
	}
    

}
