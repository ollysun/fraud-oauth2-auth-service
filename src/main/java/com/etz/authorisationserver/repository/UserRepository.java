package com.etz.authorisationserver.repository;

import com.etz.authorisationserver.entity.UserEntity;
<<<<<<< HEAD
=======
import com.google.common.base.Optional;

>>>>>>> parent of 2b27667 (implement password mangement)
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
<<<<<<< HEAD
    List<UserEntity> findByStatus(Boolean statusVal);

=======
    
>>>>>>> parent of 2b27667 (implement password mangement)
    @Transactional
    @Modifying
    @Query("Update UserEntity r Set deleted = true, status = 0 Where r.id = ?1")
    void deleteByUserId(Long userId);
}
