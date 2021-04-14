package com.etz.authorisationserver.dto.request;

import com.etz.authorisationserver.entity.User;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties({"password"})
@EqualsAndHashCode(callSuper = true)
@Data
public class UpdateUserRequest extends User {
    private Long roleId;
    private List<Long> permissions;
}
