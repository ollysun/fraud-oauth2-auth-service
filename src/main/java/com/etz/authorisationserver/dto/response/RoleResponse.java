package com.etz.authorisationserver.dto.response;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class RoleResponse<T> implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private Long roleId;
    private String roleName;
    private String description;
    private List<T> permissions;
    private Boolean status;
    private String createdBy;
    private LocalDateTime createdAt;

}
