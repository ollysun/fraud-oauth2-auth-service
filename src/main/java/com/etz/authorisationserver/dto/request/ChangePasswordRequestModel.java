package com.etz.authorisationserver.dto.request;

import javax.validation.constraints.NotBlank;

import com.etz.authorisationserver.validation.ValidPassword;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ChangePasswordRequestModel {
	@NotBlank(message="Please provide your old password")
	private String oldPassword;

	@ValidPassword
	@NotBlank(message="Please provide the new password")
    private String newPassword;
}
