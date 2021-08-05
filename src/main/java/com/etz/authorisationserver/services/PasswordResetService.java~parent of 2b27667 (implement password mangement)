package com.etz.authorisationserver.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.etz.authorisationserver.repository.UserRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class PasswordResetService {

	@Autowired
	private UserRepository userRepository;
	
    public void resetUserPassword(String email, String userName) {
		
    	//check if user exists in the db
    	
    	// fetch user by emailOrusername if not throw exception "user not found"
    	// generate token(hash key), save to reset password entity,
    	
    	if (userRepository.findByEmailOrUsername(email, userName)!=null) {
			//generate token and send to the supplied email?
		}
    	
	}
}
