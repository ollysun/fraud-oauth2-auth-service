package com.etz.authorisationserver.repository;

import com.etz.authorisationserver.entity.Role;
import com.etz.authorisationserver.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    List<Role> findByStatus(Boolean statusVal);


    void delete(Role role);
}
