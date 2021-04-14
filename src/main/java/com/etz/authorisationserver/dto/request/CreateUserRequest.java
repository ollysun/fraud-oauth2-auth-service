package com.etz.authorisationserver.dto.request;

import com.etz.authorisationserver.entity.Permission;
import com.etz.authorisationserver.entity.User;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class CreateUserRequest extends User {
    private Long roleId;
    private List<Long> permissions;
}
