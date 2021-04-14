package com.etz.authorisationserver.repository;

import com.etz.authorisationserver.entity.Permission;
import com.etz.authorisationserver.entity.UserPermission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IUserPermissionRepository extends JpaRepository<UserPermission, Long> {

    List<UserPermission> findByUserId(Long userId);
}
