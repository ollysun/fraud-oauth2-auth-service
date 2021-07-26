package com.etz.authorisationserver.dto.request;

import java.io.Serializable;

import javax.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class ResetTokenRequestModel implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@NotBlank(message="Please provide the username")
	private String userName;
	@NotBlank(message="Please provide the email")
	private String email;
}
