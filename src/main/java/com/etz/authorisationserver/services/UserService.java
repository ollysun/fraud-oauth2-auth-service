package com.etz.authorisationserver.services;

import com.etz.authorisationserver.dto.request.CreateUserRequest;
import com.etz.authorisationserver.dto.request.UpdateUserRequest;
import com.etz.authorisationserver.dto.response.UserEntityResponse;
import com.etz.authorisationserver.entity.User;
import com.etz.authorisationserver.entity.UserPermission;
import com.etz.authorisationserver.entity.UserRole;
import com.etz.authorisationserver.repository.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@Transactional
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserPermissionRepository userPermissionRepository;

    @Autowired
    private UserRoleRepository userRoleRepository;

    @Autowired
    private PermissionRepository permissionRepository;

    private UserRole userRole;
    private UserPermission userPermission;


    public User createUser(CreateUserRequest createUserRequest){
        createUserRequest.setStatus(Boolean.TRUE);
        User user = userRepository.save(createUserRequest);
        if (Boolean.TRUE.equals(createUserRequest.getHasRole())){
            createUserRequest.getRoleId().forEach(roleId -> {
                userRole.setRoleId(roleId);
                userRole.setUserId(user.getId());
                userRole.setCreatedBy(createUserRequest.getCreatedBy());
                userRoleRepository.save(userRole);
            });
        }

        if (Boolean.TRUE.equals(createUserRequest.getHasPermission())){
            for(Long permissionId : createUserRequest.getPermissions()){
                userPermission.setUserId(user.getId());
                userPermission.setPermissionId(permissionId);
                userPermission.setCreatedBy(createUserRequest.getCreatedBy());
                userPermissionRepository.save(userPermission);
            }
        }
        return user;
    }

    public User updateUser(UpdateUserRequest updateUserRequest, Long userId){
        User user = userRepository.findByUserId(userId);
        if (user == null){
            throw new RuntimeException("User not found");
        }
        User updatedUser = userRepository.save(updateUserRequest);

        if (Boolean.TRUE.equals(updateUserRequest.getHasRole())){
            updateUserRequest.getRoleId().forEach(roleId -> {
                userRole.setRoleId(roleId);
                userRole.setUserId(userId);
                userRole.setCreatedBy(updateUserRequest.getUpdatedBy());
                userRoleRepository.save(userRole);
            });
        }

        if (Boolean.TRUE.equals(updateUserRequest.getHasPermission())
                && !(updateUserRequest.getPermissions().isEmpty())){
            updateUserRequest.getPermissions().forEach(permissionId -> {
                userPermission.setUserId(updatedUser.getId());
                userPermission.setPermissionId(permissionId);
                userPermission.setUpdatedBy(updateUserRequest.getCreatedBy());
                userPermissionRepository.save(userPermission);
            });

        }
        return updatedUser;
    }

    public List<UserEntityResponse> getAllUsers(Long userId, Boolean activatedStatus){
        List<User> userList = new ArrayList<>();
        List<UserEntityResponse> userResponseList;
        if (userId == null){
            userList.add(userRepository.findByUserId(userId));
        } else if (Boolean.TRUE.equals(activatedStatus)) {
            userList.addAll(userRepository.findByStatus(true));
        }else{
            userList = userRepository.findAll();
        }
        userResponseList= assignUserResponseList(userList);
        return userResponseList;
    }

    private List<String> getUserPermissions(Long userId) {
        List<UserPermission> userPermissionList = userPermissionRepository.findByUserId(userId);
        List<String> permissionsList = new ArrayList<>();
        userPermissionList.forEach(userPermissionListId ->{
            permissionsList.add(permissionRepository.getOne(userPermissionListId.getPermissionId()).getName());
        });
        return permissionsList;
    }

    private List<Long> getUserRoleId(Long userId) {
        List<UserRole> userRoleList = userRoleRepository.findUserById(userId);
        List<Long> roleId = new ArrayList<>();
        userRoleList.forEach(userRoleId -> {
            roleId.add(userRoleId.getRoleId());
        });
        return roleId;
    }

    private List<UserEntityResponse> assignUserResponseList(List<User> userList) {
        List<UserEntityResponse> userResponseList = new ArrayList<>();
        UserEntityResponse userResponse = new UserEntityResponse();
        userList.forEach(userListObject ->{
            userResponse.setUserId(userListObject.getId());
            userResponse.setEmail(userListObject.getEmail());
            userResponse.setFirstName(userListObject.getFirstName());
            userResponse.setLastName(userListObject.getLastName());
            userResponse.setHasPermission(userListObject.getHasPermission());
            userResponse.setHasRole(userListObject.getHasRole());
            userResponse.setPhone(userListObject.getPhone());
            userResponse.setRoleId(getUserRoleId(userListObject.getId()));
            userResponse.setPermissions(getUserPermissions(userListObject.getId()));
            userResponse.setStatus(userListObject.getStatus());
            userResponse.setUserName(userListObject.getUsername());
            userResponse.setCreatedBy(userListObject.getCreatedBy());
            userResponse.setCreatedAt(userListObject.getCreatedAt());
            userResponseList.add(userResponse);
        });
        return userResponseList;
    }



}
