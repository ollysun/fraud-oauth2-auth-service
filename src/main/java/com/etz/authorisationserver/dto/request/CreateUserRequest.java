package com.etz.authorisationserver.dto.request;
import lombok.Data;
import java.io.Serializable;
import java.util.List;

@Data
public class CreateUserRequest implements Serializable {
    private String username;
    private String password;
    private String firstname;
    private String lastname;
    private String phone;
    private String email;
    private Boolean hasRole;
    private Boolean status;
    private Boolean hasPermission;
    private List<Long> roleId;
    private List<Long> permissionIds;
    private String createdBy;
}
