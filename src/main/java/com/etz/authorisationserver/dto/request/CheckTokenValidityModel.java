package com.etz.authorisationserver.dto.request;

import java.io.Serializable;

import javax.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class CheckTokenValidityModel implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@NotBlank(message="Please provide the user Id")//is enforced by the @Valid in the corresponding controller 
	private String userId;
	@NotBlank(message="Please provide the token")
	private String token;
	
}
