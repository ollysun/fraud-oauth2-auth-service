package com.etz.authorisationserver.dto.request;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

@Data
public class UpdateRoleRequest implements Serializable {
    @NotNull(message="Please provide the user id")
    private Long roleId;

    @NotNull(message = "User name cannot be empty")
    private String roleName;
    private String description;
    private List<Long> permissions;
    private Boolean status;
    private String updatedBy;
}
