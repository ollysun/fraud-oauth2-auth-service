package com.etz.authorisationserver.repository;

import com.etz.authorisationserver.entity.Permission;
import com.etz.authorisationserver.entity.UserPermission;
import com.etz.authorisationserver.entity.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IUserRoleRepository extends JpaRepository<UserRole, Long> {

    List<UserRole> findUserById(Long userId);

}
