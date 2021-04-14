package com.etz.authorisationserver.repository;

import com.etz.authorisationserver.entity.RolePermission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IRolePermissionRepository extends JpaRepository<RolePermission, Long> {
}
