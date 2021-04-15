package com.etz.authorisationserver.dto.response;

import com.etz.authorisationserver.entity.User;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class UserEntityResponse {
    private Long userId;
    private String userName;
    private String firstName;
    private String lastName;
    private String phone;
    private String email;
    private Boolean hasRole;
    private List<Long> roleId;
    private Boolean hasPermission;
    private List<String> permissions;
    private Boolean status;
    private String createdBy;
    private LocalDateTime createdAt;
}
