package com.etz.authorisationserver.repository;

import com.etz.authorisationserver.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IUserRepository extends JpaRepository<User, Long> {

    User findByEmail(String email);

    User findByUserId(Long userId);

    User findByUsername(String username);

    @Override
    void delete(User user);
}
