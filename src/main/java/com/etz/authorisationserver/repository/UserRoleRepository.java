package com.etz.authorisationserver.repository;

import com.etz.authorisationserver.entity.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, Long> {

    UserRole findByUserId(Long userId);
    UserRole findByRoleId(Long roleId);

    Optional<UserRole> findByUserIdAndRoleId(Long userId, Long roleId);

    boolean existsByRoleId(Long ruleId);


    @Transactional
    @Modifying
    @Query("Update UserRole u set deleted = true Where u.roleId = ?1")
    void deleteByRoleId(Long roleId);

    @Transactional
    @Modifying
    @Query("Update UserRole u set deleted = true Where u.userId = ?1")
    void deleteByUserId(Long userId);

//    @Modifying
//    @Query("DELETE FROM Product p WHERE p.id IN (:ids)")
//    public void deleteProductsByIds(@Param("ids") List<Integer> ids);
}
