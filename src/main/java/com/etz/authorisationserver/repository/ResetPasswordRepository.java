package com.etz.authorisationserver.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.etz.authorisationserver.entity.ResetPasswordTokens;
import com.etz.authorisationserver.entity.UserEntity;

import java.util.List;
import java.util.Optional;

public interface ResetPasswordRepository extends JpaRepository<ResetPasswordTokens, Long> {
 
	ResetPasswordTokens findByToken(String token);

	Optional<ResetPasswordTokens> findByUserIdAndTokenAndExpiredFalse(Long userId, String token);
}
