package com.etz.authorisationserver.dto.request;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

@Data
public class CreateUserRequest implements Serializable {
    @NotNull(message = "User name cannot be empty")
    private String username;

    @NotNull(message = "Password cannot be empty")
    private String password;

    @NotNull(message = "First name cannot be empty")
    private String firstname;

    @NotNull(message = "Last name cannot be empty")
    private String lastname;

    @NotNull(message = "Phone cannot be empty")
    private String phone;

    @Email(message="please enter valid email")
    @NotNull(message = "email cannot be empty")
    private String email;

    @NotNull(message = "Please let know if the user has role")
    private Boolean hasRole;

    @NotNull(message = "Please let know if the User has permission")
    private Boolean hasPermission;

    private Boolean status;
    
    @Valid
    @NotNull(message ="Please enter the roleId")
    private List<Long> roleId;
    private List<Long> permissionIds;

    @NotNull(message = "createdBy cannot be empty")
    private String createdBy;
}
