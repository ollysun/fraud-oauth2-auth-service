package com.etz.authorisationserver.controller;

import javax.validation.Valid;

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
import com.etz.authorisationserver.dto.request.UpdatePasswordRequestModel;
import com.etz.authorisationserver.dto.response.BooleanResponse;
import com.etz.authorisationserver.services.PasswordResetService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/password")
public class PasswordResetController {
	
	private final PasswordResetService passwordResetService;

	@PostMapping("/forget")
	public ResponseEntity<BooleanResponse> requestPasswordReset(@RequestBody @Valid ResetTokenRequestModel resetTokenRequest) {
		BooleanResponse response = new BooleanResponse(passwordResetService.resetUserPassword(resetTokenRequest.getEmail(), resetTokenRequest.getUserName()));
		return new ResponseEntity<BooleanResponse>(response , HttpStatus.OK);
	}
	    
	@GetMapping("/token")//recieve the token as a path variable? no!
	public ResponseEntity<BooleanResponse> checkTokenvalidity(@RequestParam String user, @RequestParam String token){
		BooleanResponse response = new BooleanResponse(passwordResetService.checkTokenValidity(user, token));
		response.setMessage("If returned data is true, token is available else its expired/not in the db");
		return new ResponseEntity<BooleanResponse>(response , HttpStatus.OK);//return true?
	}
	
	@PutMapping("/update")//test these two endpoints from postman
    public ResponseEntity<BooleanResponse> updatePassword(@RequestBody @Valid UpdatePasswordRequestModel updatePasswordRequestModel){
    	
    	BooleanResponse response = new BooleanResponse(passwordResetService.updatePassword(updatePasswordRequestModel.getUserId(), updatePasswordRequestModel.getPassword()));
    	response.setMessage("If returned data is true, password updated successfully else unable to update password");
    	return new ResponseEntity<BooleanResponse>(response , HttpStatus.OK);
    }
	
	@PutMapping("/change")
    public ResponseEntity<BooleanResponse> changePassword(@RequestBody @Valid ChangePasswordRequestModel changePasswordRequestModel){
    	
    	BooleanResponse response = new BooleanResponse(passwordResetService.changePassword(changePasswordRequestModel.getOldPassword(), changePasswordRequestModel.getNewPassword()));
    	response.setMessage("If returned data is true, password changed successfully else unable to change password");
    	return new ResponseEntity<BooleanResponse>(response , HttpStatus.OK);
    }
}
