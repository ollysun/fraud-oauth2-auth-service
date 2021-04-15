package com.etz.authorisationserver.dto.response;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class RoleResponse implements Serializable {
    private Long roleId;
    private String roleName;
    private String description;
    private List<String> permissions;
    private Boolean status;
    private String createdBy;
    private LocalDateTime createdAt;

}
