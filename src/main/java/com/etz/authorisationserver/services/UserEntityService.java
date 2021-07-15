package com.etz.authorisationserver.services;

import com.etz.authorisationserver.dto.request.CreateUserRequest;
import com.etz.authorisationserver.dto.request.UpdateUserRequest;
import com.etz.authorisationserver.dto.response.UpdatedUserResponse;
import com.etz.authorisationserver.dto.response.UserResponse;
import com.etz.authorisationserver.entity.UserEntity;
import com.etz.authorisationserver.entity.UserPermission;
import com.etz.authorisationserver.entity.UserRole;
import com.etz.authorisationserver.exception.ResourceNotFoundException;
import com.etz.authorisationserver.repository.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.*;

@Slf4j
@Service
public class UserEntityService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserPermissionRepository userPermissionRepository;

    @Autowired
    private UserRoleRepository userRoleRepository;

    @Autowired
    private PermissionRepository permissionRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional(rollbackFor = Throwable.class)
    public UserResponse createUser(CreateUserRequest createUserRequest){
        UserRole userRole = new UserRole();
        UserPermission userPermission = new UserPermission();
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
        log.info(user.toString());
        if (Boolean.TRUE.equals(createUserRequest.getHasRole())
                && Objects.nonNull(createUserRequest.getRoleId())){
                userRole.setRoleId(createUserRequest.getRoleId());
                userRole.setUserId(user.getId());
                userRole.setCreatedBy(createUserRequest.getCreatedBy());
                userRoleRepository.save(userRole);
        }

        if (Boolean.TRUE.equals(createUserRequest.getHasPermission())
                && !(createUserRequest.getPermissionIds().isEmpty())){
            createUserRequest.getPermissionIds().forEach(permissionId -> {
                userPermission.setUserId(user.getId());
                userPermission.setPermissionId(permissionId);
                userPermission.setCreatedBy(createUserRequest.getCreatedBy());
                userPermissionRepository.save(userPermission);
            });
        }
        return outputUserResponse(user, createUserRequest);
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
                           .optLock(user.getVersion())
                           .build();
    }

    // get permissions Names
    public List<String> getPermissionName(List<Long> permissionList){
        List<String> permissionNames = new ArrayList<>();
        permissionList.forEach(permissionId -> permissionNames.add(permissionRepository.getOne(permissionId).getName()));
        return permissionNames;
    }

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
            UserRole previousUserRoleList = userRoleRepository.findByUserIdAndRoleId(updateUserRequest.getUserId(), updateUserRequest.getRoleId())
                                            .orElseThrow(() -> new ResourceNotFoundException("user Id " + updateUserRequest.getUserId() + " and role Id " + updateUserRequest.getRoleId() + "not found"));

            previousUserRoleList.setRoleId(updateUserRequest.getRoleId());
            previousUserRoleList.setUserId(updateUserRequest.getUserId());
            previousUserRoleList.setUpdatedBy(updateUserRequest.getUpdatedBy());
            userRoleRepository.save(previousUserRoleList);
        }

        if (Boolean.TRUE.equals(updateUserRequest.getHasPermission())
                && !(updateUserRequest.getPermissionIds().isEmpty())) {
            List<UserPermission> previousUserPermissionList = userPermissionRepository.findByUserId(updatedUser.getId());

            deleteUserPermission(previousUserPermissionList, updateUserRequest.getPermissionIds());

            for (Long permissionId : updateUserRequest.getPermissionIds()) {
                UserPermission userPermission = new UserPermission();
                userPermission.setUserId(updatedUser.getId());
                userPermission.setPermissionId(permissionId);
                userPermission.setUpdatedBy(updateUserRequest.getUpdatedBy());
                userPermissionRepository.save(userPermission);
            }

        }
        return true;
    }
    private void deleteUserRole(List<UserRole> previousUserRole, List<Long> roles) {
        // get previous user permissions IDs
        List<Long> previousUserRoleId = new ArrayList<>();
        previousUserRole.forEach(userRoleId -> previousUserRoleId.add(userRoleId.getRoleId()));
        // Take out duplicate from the role
        List<Long> duplicateRoles = removeDuplicateInList(previousUserRoleId,roles);

        if (!(duplicateRoles.isEmpty())) {
            // delete roles no longer needed
            previousUserRole.forEach(userRole -> duplicateRoles.forEach(roleId -> {
                if (userRole.getRoleId().equals(roleId)) {
                    userRoleRepository.deleteById(userRole.getId());
                }
            }));
        }
    }


    private List<Long> removeDuplicateInList(List<Long> listOne, List<Long> listTwo){
        List<Long> duplicateList = new ArrayList<>();
        for (Long numberVal : listOne) {
            if (listTwo.contains(numberVal)) {
                duplicateList.add(numberVal);
            }
        }
        return duplicateList;
    }

    private void deleteUserPermission(List<UserPermission> previousUserPerm, List<Long> permissions) {
        // get previous user permissions IDs
        List<Long> previousUserPermissionIds = new ArrayList<>();
        previousUserPerm.forEach(userPermEntity -> previousUserPermissionIds.add(userPermEntity.getPermissionId()));

        List<Long> duplicatePermissions = removeDuplicateInList(previousUserPermissionIds,permissions);

        if (!(duplicatePermissions.isEmpty())) {
            // delete permissions no longer needed
            previousUserPerm.forEach(userPerm -> duplicatePermissions.forEach(permissionId -> {
                if (userPerm.getPermissionId().equals(permissionId)) {
                    userPermissionRepository.deleteById(userPerm.getId());
                }
            }));
        }
    }

    private UpdatedUserResponse outputUpdatedUserResponse(UserEntity user, UpdateUserRequest updateUserRequest){
        UpdatedUserResponse userResponse = new UpdatedUserResponse();
        userResponse.setUserId(user.getId());
        userResponse.setUsername(user.getUsername());
        userResponse.setStatus(user.getStatus());
        userResponse.setFirstName(user.getFirstName());
        userResponse.setLastName(user.getLastName());
        userResponse.setPhone(user.getPhone());
        userResponse.setEmail(user.getEmail());
        userResponse.setHasRole(user.getHasRole());
        userResponse.setRoleId(updateUserRequest.getRoleId());
        userResponse.setHasPermission(user.getHasPermission());
        userResponse.setPermissionIds(getPermissionName(updateUserRequest.getPermissionIds()));
        userResponse.setUpdatedBy(user.getUpdatedBy());
        userResponse.setUpdatedAt(user.getUpdatedAt());

        return userResponse;
    }

    @Transactional(rollbackFor = Throwable.class)
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
        List<String> permissionsList = new ArrayList<>();
        userPermissionList.forEach(userPermissionListId -> permissionsList.add(permissionRepository.getOne(userPermissionListId.getPermissionId()).getName()));
        return permissionsList;
    }

    private List<Long> getUserRoleId(Long userId) {
        List<UserRole> userRoleList = userRoleRepository.findByUserId(userId);
        List<Long> roleId = new ArrayList<>();
        userRoleList.forEach(userRoleId -> roleId.add(userRoleId.getRoleId()));
        return roleId;
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
                    .roleId(userListObject.getId())
                    .permissionNames(getUserPermissions(userListObject.getId()))
                    .status(userListObject.getStatus())
                    .userName(userListObject.getUsername())
                    .createdBy(userListObject.getCreatedBy())
                    .createdAt(userListObject.getCreatedAt())
                    .optLock(userListObject.getVersion())
                    .build();
            userResponseList.add(userResponse);
        }
        return userResponseList;
    }


    

}
