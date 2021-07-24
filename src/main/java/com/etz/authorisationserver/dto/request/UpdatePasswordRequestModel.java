package com.etz.authorisationserver.dto.request;

import javax.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class UpdatePasswordRequestModel {
	
	@NotBlank(message="Please provide the userId")
	private String userId;
	@NotBlank(message="Please provide the token")
	private String token;
	@NotBlank(message="Please provide the password")
	private String password;
}
