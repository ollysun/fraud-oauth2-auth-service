package com.etz.authorisationserver.services;

import com.etz.authorisationserver.dto.request.CreateUserRequest;
import com.etz.authorisationserver.dto.request.UpdateUserRequest;
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
    IUserRepository iUserRepository;


    @Autowired
    private IUserPermissionRepository iUserPermissionRepository;

    @Autowired
    private IUserRoleRepository iUserRoleRepository;

    private UserRole userRole;
    private UserPermission userPermission;


    public User createUser(CreateUserRequest createUserRequest){
        createUserRequest.setStatus(Boolean.TRUE);
        User user = iUserRepository.save(createUserRequest);
        if (Boolean.TRUE.equals(createUserRequest.getHasRole())){
            userRole.setRoleId(createUserRequest.getRoleId());
            userRole.setUserId(user.getId());
            userRole.setCreatedBy(createUserRequest.getCreatedBy());
            iUserRoleRepository.save(userRole);
        }

        if (Boolean.TRUE.equals(createUserRequest.getHasPermission())){
            for(Long permissionId : createUserRequest.getPermissions()){
                userPermission.setUserId(user.getId());
                userPermission.setPermissionId(permissionId);
                userPermission.setCreatedBy(createUserRequest.getCreatedBy());
                iUserPermissionRepository.save(userPermission);
            }
        }
        return user;
    }

    public User updateUser(UpdateUserRequest updateUserRequest, Long userId){
        User user = iUserRepository.findByUserId(userId);
        if (user == null){
            throw new RuntimeException("User not found");
        }
        User updatedUser = iUserRepository.save(updateUserRequest);

//        if (updateUserRequest.getPermissions().size() > 0) {
//            // get previous user permissions
//            List<UserPermission> previousUserPermEntity = iUserPermissionRepository
//                    .findByUserId(updatedUser.getId());
//            List<Long> previousUserPermissions = new ArrayList<>();
//            previousUserPermEntity.forEach(userPermEntity -> {
//                previousUserPermissions.add(userPermEntity.getPermissionId());
//            });
//        }

        if (Boolean.TRUE.equals(updateUserRequest.getHasRole())){
            userRole.setRoleId(updateUserRequest.getRoleId());
            userRole.setUserId(updatedUser.getId());
            userRole.setUpdatedBy(updateUserRequest.getUpdatedBy());
            iUserRoleRepository.save(userRole);
        }

        if (Boolean.TRUE.equals(updateUserRequest.getHasPermission())
                && updateUserRequest.getPermissions().size() > 0){
            updateUserRequest.getPermissions().forEach(permissionId -> {
                userPermission.setUserId(updatedUser.getId());
                userPermission.setPermissionId(permissionId);
                userPermission.setUpdatedBy(updateUserRequest.getCreatedBy());
                iUserPermissionRepository.save(userPermission);
            });

        }
        return updatedUser;
    }

//    private void deleteRemovedUserPermission(List<UserPermission> previousUserPermEntity, List<Long> permissions) {
//        // get previous user permissions IDs
//        List<Long> previousUserPermissions = new ArrayList<>();
//        previousUserPermEntity.forEach(userPermEntity -> {
//            previousUserPermissions.add(userPermEntity.getPermissionId());
//        });
//
//        // filter out permissions that should still be retained
//        List<Long> previousUserPermToDelete = previousUserPermissions.stream().collect(Collectors.toList());
//        previousUserPermToDelete.removeAll(permissions);
//
//        // delete permissions no longer needed
//        previousUserPermEntity.forEach(userPermEntity -> {
//            previousUserPermToDelete.forEach(permissionId ->{
//                if(userPermEntity.getPermissionId() == permissionId) {
//                    userPermissionRepository.deleteById(userPermEntity.getId());
//                }
//            });
//        });
//    }

    public List<User> getAllUsers(Long userId){
        List<User> userList = new ArrayList<>();
        if (userId == null){
            userList = iUserRepository.findAll();
        }else{
            userList.add(iUserRepository.findByUserId(userId));
        }
        return userList;
    }


    public boolean deleteUser(Long userId) {
        iUserRepository.deleteById(userId);
        deleteUserRoleAndPermission(userId);
        return true;
    }

    private void deleteUserRoleAndPermission(Long userId){
        List<UserRole> userRoleList = iUserRoleRepository.findUserById(userId);
        userRoleList.forEach(userRoleEntity -> {
            iUserRoleRepository.deleteById(userId);
        });

        List<UserPermission> userPermissionList = iUserPermissionRepository.findByUserId(userId);
        userPermissionList.forEach(userRoleEntity -> {
            iUserPermissionRepository.deleteById(userId);
        });
    }
}
