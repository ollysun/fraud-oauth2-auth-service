package com.etz.authorisationserver.repository;

import com.etz.authorisationserver.entity.Role;
import com.etz.authorisationserver.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    List<Role> findByStatus(Boolean statusVal);


    @Transactional
    @Modifying
    @Query("Update Role r set deleted = true, status=0 Where r.id = ?1")
    void deleteByRoleId(Long roleId);
}
