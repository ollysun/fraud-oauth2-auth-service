package com.etz.authorisationserver.dto.request;

import javax.validation.constraints.NotBlank;

import lombok.Data;
@Data
public class ChangePasswordRequestModel {
	@NotBlank(message="Please provide your old password")
	private String oldPassword;
	@NotBlank(message="Please provide the new password")
    private String newPassword;
}
