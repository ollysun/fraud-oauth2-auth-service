package com.etz.authorisationserver.dto.request;

import java.io.Serializable;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ResetTokenRequestModel implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@NotBlank(message="Please provide the username")
	private String username;
	@Email(message="Please enter valid email")
	@NotBlank(message="Please provide the email")
	private String email;
}
