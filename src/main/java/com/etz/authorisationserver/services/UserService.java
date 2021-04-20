package com.etz.authorisationserver.services;

import com.etz.authorisationserver.dto.request.CreateUserRequest;
import com.etz.authorisationserver.dto.request.UpdateUserRequest;
import com.etz.authorisationserver.dto.response.UpdatedUserResponse;
import com.etz.authorisationserver.dto.response.UserResponse;
import com.etz.authorisationserver.entity.*;
import com.etz.authorisationserver.exception.ResourceNotFoundException;
import com.etz.authorisationserver.repository.*;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

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

    @Autowired
    private PasswordEncoder passwordEncoder;


    public UserResponse createUser(CreateUserRequest createUserRequest){
        UserRole userRole = new UserRole();
        UserPermission userPermission = new UserPermission();
        createUserRequest.setStatus(Boolean.TRUE);
        createUserRequest.setPassword(passwordEncoder.encode(createUserRequest.getPassword()));

        User userRequest = new User();
        userRequest.setFirstName(createUserRequest.getFirstname());
        userRequest.setPassword(createUserRequest.getPassword());
        userRequest.setUsername(createUserRequest.getUsername());
        userRequest.setLastName(createUserRequest.getLastname());
        userRequest.setStatus(createUserRequest.getStatus());
        userRequest.setPhone(createUserRequest.getPhone());
        userRequest.setEmail(createUserRequest.getEmail());
        userRequest.setCreatedBy(createUserRequest.getCreatedBy());
        User user = userRepository.save(userRequest);
        log.info(user.toString());
        if (Boolean.TRUE.equals(createUserRequest.getHasRole())
                && !(createUserRequest.getRoleId().isEmpty())){
            createUserRequest.getRoleId().forEach(roleId -> {
                userRole.setRoleId(roleId);
                userRole.setUserId(user.getId());
                userRole.setCreatedBy(createUserRequest.getCreatedBy());
                userRoleRepository.save(userRole);
            });
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


    private UserResponse outputUserResponse(User user, CreateUserRequest createUserRequest){
        UserResponse userResponse = new UserResponse();
        userResponse.setUserId(user.getId());
        userResponse.setUserName(user.getUsername());
        userResponse.setStatus(user.getStatus());
        userResponse.setFirstName(user.getFirstName());
        userResponse.setLastName(user.getLastName());
        userResponse.setPhone(user.getPhone());
        userResponse.setEmail(user.getEmail());
        userResponse.setHasRole(user.getHasRole());
        userResponse.setRoleId(createUserRequest.getRoleId());
        userResponse.setHasPermission(user.getHasPermission());
        userResponse.setPermissionNames(getPermissionName(createUserRequest.getPermissionIds()));
        userResponse.setCreatedBy(user.getCreatedBy());
        userResponse.setCreatedAt(user.getCreatedAt());
        userResponse.setOptLock(user.getVersion());
        return userResponse;
    }

    // get permissions Names
    public List<String> getPermissionName(List<Long> permissionList){
        List<String> permissionNames = new ArrayList<>();
        permissionList.forEach(permissionId -> permissionNames.add(permissionRepository.getOne(permissionId).getName()));
        return permissionNames;
    }


    public Boolean updateUser(UpdateUserRequest updateUserRequest, Long userId){
        UserRole newUserRole = new UserRole();
        UserPermission userPermission = new UserPermission();
        List<UserRole> newUserRoleList = new ArrayList<>();
        List<UserPermission> newUserPermissionList = new ArrayList<>();
        User user = new User();
        if(userId != null) {
            user = userRepository.findById(userId)
                    .orElseThrow(() -> new ResourceNotFoundException("Role Not found " + userId));
            user.setUsername(updateUserRequest.getUsername());
            user.setPassword(passwordEncoder.encode(updateUserRequest.getPassword()));
            user.setFirstName(updateUserRequest.getFirstName());
            user.setLastName(updateUserRequest.getLastName());
            user.setPhone(updateUserRequest.getPhone());
            user.setEmail(updateUserRequest.getEmail());
            user.setHasRole(updateUserRequest.getHasRole());
            user.setHasPermission(updateUserRequest.getHasPermission());
            user.setStatus(updateUserRequest.getStatus());
            user.setUpdatedBy(updateUserRequest.getUpdatedBy());
        }
        User updatedUser = userRepository.save(user);

        if (Boolean.TRUE.equals(updateUserRequest.getHasRole())
                && !(updateUserRequest.getRoleId().isEmpty())){

            List<UserRole> previousUserRoleList = userRoleRepository.findUserById(updatedUser.getId());
            updateUserRequest.getRoleId().forEach(roleId -> {
                newUserRole.setRoleId(roleId);
                newUserRole.setUserId(updatedUser.getId());
                newUserRole.setCreatedBy(updateUserRequest.getUpdatedBy());
                newUserRoleList.add(newUserRole);
            });
            //remove duplicates
            newUserRoleList.removeAll(previousUserRoleList);
            previousUserRoleList.addAll(newUserRoleList);
            userRoleRepository.saveAll(previousUserRoleList);
        }

        if (Boolean.TRUE.equals(updateUserRequest.getHasPermission())
                && !(updateUserRequest.getPermissionIds().isEmpty())) {
            List<UserPermission> previousUserPermissionList = userPermissionRepository.findByUserId(updatedUser.getId());

            updateUserRequest.getPermissionIds().forEach(permissionId -> {
                userPermission.setUserId(updatedUser.getId());
                userPermission.setPermissionId(permissionId);
                userPermission.setUpdatedBy(updateUserRequest.getCreatedBy());
                newUserPermissionList.add(userPermission);
            });

            //to remove duplicates
            Set<UserPermission> setOfUserPermission = new LinkedHashSet<>(previousUserPermissionList);
            setOfUserPermission.addAll(newUserPermissionList);
            List<UserPermission> combinedList = new ArrayList<>(setOfUserPermission);
            userPermissionRepository.saveAll(combinedList);
        }
        return true;
        //return outputUpdatedUserResponse(updatedUser, updateUserRequest);
    }



    private UpdatedUserResponse outputUpdatedUserResponse(User user, UpdateUserRequest updateUserRequest){
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

    public List<UserResponse> getAllUsers(Long userId, Boolean activatedStatus){
        List<User> userList = new ArrayList<>();
        List<UserResponse> userResponseList;
        if (userId != null){
            User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User Not found " + userId));
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
        List<UserRole> userRoleList = userRoleRepository.findUserById(userId);
        List<Long> roleId = new ArrayList<>();
        userRoleList.forEach(userRoleId -> roleId.add(userRoleId.getRoleId()));
        return roleId;
    }

    private List<UserResponse> assignUserResponseList(List<User> userList) {
        List<UserResponse> userResponseList = new ArrayList<>();
        for (User userListObject : userList) {
            UserResponse userResponse = new UserResponse();
            userResponse.setUserId(userListObject.getId());
            userResponse.setEmail(userListObject.getEmail());
            userResponse.setFirstName(userListObject.getFirstName());
            userResponse.setLastName(userListObject.getLastName());
            userResponse.setHasPermission(userListObject.getHasPermission());
            userResponse.setHasRole(userListObject.getHasRole());
            userResponse.setPhone(userListObject.getPhone());
            userResponse.setRoleId(getUserRoleId(userListObject.getId()));
            userResponse.setPermissionNames(getUserPermissions(userListObject.getId()));
            userResponse.setStatus(userListObject.getStatus());
            userResponse.setUserName(userListObject.getUsername());
            userResponse.setCreatedBy(userListObject.getCreatedBy());
            userResponse.setCreatedAt(userListObject.getCreatedAt());
            userResponse.setOptLock(userListObject.getVersion());
            userResponseList.add(userResponse);
        }
        return userResponseList;
    }



}
