package com.etz.authorisationserver.dto.response;


import com.etz.authorisationserver.entity.Role;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@JsonIgnoreProperties({ "id", "createdBy", "createdAt" })
@Data
public class UpdatedRoleResponse extends Role {
    private Long roleId;
    private List<String> permissionNames;
}
