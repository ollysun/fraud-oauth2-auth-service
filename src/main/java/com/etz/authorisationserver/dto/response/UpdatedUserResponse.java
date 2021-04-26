package com.etz.authorisationserver.dto.response;

import com.etz.authorisationserver.entity.UserEntity;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@JsonIgnoreProperties({ "id", "createdBy", "createdAt" })
@Data
public class UpdatedUserResponse extends UserEntity {
    private Long userId;
    private List<String> permissionIds;
    private List<Long> roleId;
}


