package com.etz.authorisationserver.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.etz.authorisationserver.dto.request.ResetTokenRequestModel;
import com.etz.authorisationserver.dto.response.RequestResetResponse;
import com.etz.authorisationserver.services.PasswordResetService;
import com.etz.authorisationserver.services.UserEntityService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/password/forget")
@Validated
public class PasswordResetController {
	
	@Autowired
    private PasswordResetService userService;
	
	 @PostMapping
	    public ResponseEntity<RequestResetResponse> requestPasswordReset(@RequestBody ResetTokenRequestModel resetTokenRequest) {
			
	    RequestResetResponse requestResetResponse = new RequestResetResponse(true, "token generated", null);
	    	
	    	userService.resetUserPassword(resetTokenRequest.getEmail(),resetTokenRequest.getUserName());//call the service method with the supplied email
	    	return new ResponseEntity<RequestResetResponse>(requestResetResponse, HttpStatus.OK);
		}
	    
}
