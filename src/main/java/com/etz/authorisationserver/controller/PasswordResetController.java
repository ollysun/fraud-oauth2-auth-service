package com.etz.authorisationserver.controller;

import javax.validation.Valid;

import com.etz.authorisationserver.dto.request.PasswordDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.etz.authorisationserver.dto.request.ChangePasswordRequestModel;
import com.etz.authorisationserver.dto.request.ResetTokenRequestModel;
import com.etz.authorisationserver.dto.response.BooleanResponse;
import com.etz.authorisationserver.services.PasswordResetService;

import lombok.RequiredArgsConstructor;

import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/password")
public class PasswordResetController {
	
	private final PasswordResetService passwordResetService;

	@PostMapping("/forget")
	public ResponseEntity<BooleanResponse> requestPasswordReset(@RequestBody @Valid ResetTokenRequestModel resetTokenRequest) {
		BooleanResponse response = new BooleanResponse(passwordResetService.resetUserPassword(resetTokenRequest));
		return new ResponseEntity<>(response , HttpStatus.OK);
	}
	    
	@GetMapping("/reset")
	public ResponseEntity<Void> checkTokenvalidity(@RequestParam String userDetails){
		String redirecturl = passwordResetService.showChangePasswordPage(userDetails);
		return ResponseEntity.status(HttpStatus.FOUND).location(URI.create(redirecturl)).build();
	}
	
	@PutMapping("/update")
    public ResponseEntity<BooleanResponse> updatePassword(@RequestParam String users, @RequestBody @Valid PasswordDto passwordDto){
    	BooleanResponse response = new BooleanResponse(passwordResetService.updatePassword(users,passwordDto));
    	response.setMessage("Updated Successfully");
    	return new ResponseEntity<>(response , HttpStatus.OK);
    }
	
	@PutMapping("/change")
    public ResponseEntity<BooleanResponse> changePassword(@RequestBody @Valid ChangePasswordRequestModel changePasswordRequestModel){
    	BooleanResponse response = new BooleanResponse(passwordResetService.changePassword(changePasswordRequestModel));
    	response.setMessage("Password changed Successfully");
    	return new ResponseEntity<>(response , HttpStatus.OK);
    }
}
