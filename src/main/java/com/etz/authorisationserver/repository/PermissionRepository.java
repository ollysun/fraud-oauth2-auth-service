package com.etz.authorisationserver.repository;

import com.etz.authorisationserver.entity.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface PermissionRepository extends JpaRepository<Permission, Long> {

    Permission findByName(String name);


    @Override
    void delete(Permission permission);
}
