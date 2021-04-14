package com.etz.authorisationserver.dto.request;

import lombok.Data;

import java.util.List;

@Data
public class CreateRoleRequest {
	private String roleName;
	private String description;
	private List<Long> permissionList;
	private String createdBy;
}
