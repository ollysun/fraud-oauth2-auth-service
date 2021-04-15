package com.etz.authorisationserver.repository;

import com.etz.authorisationserver.entity.Role;
import com.etz.authorisationserver.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findByEmail(String email);

    User findByUserId(Long userId);

    User findByUsername(String username);
    List<User> findByStatus(Boolean statusVal);
    @Override
    void delete(User user);
}
