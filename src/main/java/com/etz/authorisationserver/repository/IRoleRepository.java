package com.etz.authorisationserver.repository;

import com.etz.authorisationserver.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IRoleRepository extends JpaRepository<Role, Long> {

}
