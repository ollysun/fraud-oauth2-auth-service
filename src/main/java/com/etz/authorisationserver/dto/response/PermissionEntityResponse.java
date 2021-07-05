package com.etz.authorisationserver.dto.response;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Builder
@Data
public class PermissionEntityResponse implements Serializable {

    private Long permissionId;
    private String name;
    private Boolean status;
    private String createdBy;
    private LocalDateTime createdAt;
}
