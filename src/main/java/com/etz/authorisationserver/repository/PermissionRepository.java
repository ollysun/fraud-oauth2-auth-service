package com.etz.authorisationserver.repository;

import com.etz.authorisationserver.entity.PermissionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface PermissionRepository extends JpaRepository<PermissionEntity, Long> {

    PermissionEntity findByName(String name);

    List<PermissionEntity> findByStatus(Boolean status);

    long countById(Long id);


}
