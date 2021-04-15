package com.etz.authorisationserver.dto.request;

import lombok.*;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class CreateRoleRequest implements Serializable {
    private String roleName;
    private String description;
    private List<Long> permissionList;
    private String createdBy;
}
