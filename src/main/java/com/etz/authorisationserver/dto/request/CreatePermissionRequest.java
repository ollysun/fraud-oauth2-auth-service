package com.etz.authorisationserver.dto.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Data
public class CreatePermissionRequest implements Serializable {
    @NotBlank(message="Please provide the permission name")
    private String name;

    @NotBlank(message="Please provide the name of the creator")
    private String createdBy;
}
