package com.etz.authorisationserver.repository;

import com.etz.authorisationserver.entity.RolePermission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface RolePermissionRepository extends JpaRepository<RolePermission, Long> {
    List<RolePermission> findByRoleId(Long roleId);

    @Transactional
    @Modifying
    @Query("Update RolePermission u set deleted = true Where u.roleId = ?1")
    void deleteByRoleId(Long roleId);
}
