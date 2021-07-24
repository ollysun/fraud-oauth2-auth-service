package com.etz.authorisationserver.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.etz.authorisationserver.entity.ResetPasswordTokens;
import com.etz.authorisationserver.entity.UserEntity;

public interface ResetPasswordRepository extends JpaRepository<ResetPasswordTokens, Long> {
 
	ResetPasswordTokens findByToken(String token);
}
