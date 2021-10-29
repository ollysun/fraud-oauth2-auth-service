package com.etz.authorisationserver.dto.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ApprovalRequest {
	@NotBlank
	private String entity;
	
	@NotBlank
	private String entityId;
	
	@NotNull
	private Long userRole;
	private Long userId;
	
	@NotBlank
	private String action;
	private String createdBy;
}
