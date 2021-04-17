package com.etz.authorisationserver.dto.request;

import com.etz.authorisationserver.entity.Permission;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@JsonIgnoreProperties({ "updatedBy", "updatedAt" })
public class CreatePermissionRequest extends Permission {
}
