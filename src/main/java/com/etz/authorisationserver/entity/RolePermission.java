package com.etz.authorisationserver.entity;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "role_permission")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class RolePermission extends BaseEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "role_id", columnDefinition = "bigint")
    private Long roleId;

    @Column(name = "permission_id", columnDefinition = "bigint")
    private Long permissionId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RolePermission)) return false;
        if (!super.equals(o)) return false;
        RolePermission that = (RolePermission) o;
        return id.equals(that.id) && roleId.equals(that.roleId) && permissionId.equals(that.permissionId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), id, roleId, permissionId);
    }
}
