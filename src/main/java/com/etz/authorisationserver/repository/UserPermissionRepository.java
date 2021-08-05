package com.etz.authorisationserver.repository;

import com.etz.authorisationserver.entity.UserPermission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface UserPermissionRepository extends JpaRepository<UserPermission, Long> {

    List<UserPermission> findByUserId(Long userId);

    List<UserPermission> findByPermissionId(Long permissionId);

    @Transactional
    @Modifying
    @Query("Update UserPermission u set deleted = true Where u.userId = ?1")
    void deleteByUserId(Long userId);

    @Transactional
    @Modifying
    @Query("Update UserPermission u set deleted = true Where u.permissionId = ?1")
    void deleteByPermissionId(Long permissionId);

    @Transactional
    @Modifying
    @Query("Delete From UserPermission u Where u.permissionId = ?1")
    void deleteByPermissionIdPermanent(Long permissionId);
}

