package com.etz.authorisationserver.dto.request;

import lombok.Data;

import java.util.List;

@Data
public class UpdateRoleRequest {
    private String roleName;
    private String description;
    private List<Long> permissions;
    private Boolean status;
    private String updatedBy;
}
