package com.etz.authorisationserver.repository;

import com.etz.authorisationserver.entity.UserEntity;
import com.google.common.base.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    UserEntity findByEmail(String email);
    UserEntity findByUsername(String username);
    List<UserEntity> findByStatus(Boolean statusVal);

    Optional<UserEntity> findByEmailOrUsername(String email, String username);
}
