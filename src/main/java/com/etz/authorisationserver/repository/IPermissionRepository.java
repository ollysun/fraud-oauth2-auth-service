package com.etz.authorisationserver.repository;

import com.etz.authorisationserver.entity.Permission;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IPermissionRepository extends JpaRepository<Permission, Long> {
}
