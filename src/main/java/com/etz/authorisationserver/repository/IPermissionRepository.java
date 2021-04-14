package com.etz.authorisationserver.repository;

import com.etz.authorisationserver.entity.Permission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IPermissionRepository extends JpaRepository<Permission, Long> {

    Permission findByName(String name);


    @Override
    void delete(Permission permission);
}
