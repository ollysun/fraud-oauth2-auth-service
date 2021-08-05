package com.etz.authorisationserver.repository;

import com.etz.authorisationserver.entity.UserEntity;
//import com.google.common.base.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    UserEntity findByEmail(String email);

    UserEntity findByUsername(String username);
    List<UserEntity> findByStatus(Boolean statusVal);

    Optional<UserEntity> findByEmailAndUsername(String email, String username);

    UserEntity findByUsernameAndDeletedFalseAndStatusTrue(String username);
    
    Optional<UserEntity> findByPassword(String password);
    
    // Optional<UserEntity> findByUserId(String userId);
    
    @Transactional
    @Modifying
    @Query("Update UserEntity r Set deleted = true, status = 0 Where r.id = ?1")
    void deleteByUserId(Long userId);

}
