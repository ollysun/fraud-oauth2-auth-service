package com.etz.authorisationserver.dto.request;

import lombok.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class CreateRoleRequest implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@NotBlank(message = "role name cannot be empty")
    private String roleName;
    private String description;
    @Valid
    @NotNull(message="Please enter list of permission Ids")
    private List<Long> permissionList;
    @NotBlank(message = "createdBy cannot be empty")
    private String createdBy;
}
