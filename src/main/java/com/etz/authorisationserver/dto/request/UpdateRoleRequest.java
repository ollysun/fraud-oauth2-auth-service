package com.etz.authorisationserver.dto.request;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class UpdateRoleRequest implements Serializable {
    private Long roleId;
    private String roleName;
    private String description;
    private List<Long> permissions;
    private Boolean status;
    private String updatedBy;
}
