package com.etz.authorisationserver.repository;

import com.etz.authorisationserver.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    UserEntity findByEmail(String email);


    UserEntity findByUsernameAndDeletedFalseAndStatusTrue(String username);
    List<UserEntity> findByStatus(Boolean statusVal);

    @Transactional
    @Modifying
    @Query("Update UserEntity r Set deleted = true, status = 0 Where r.id = ?1")
    void deleteByUserId(Long userId);
}
