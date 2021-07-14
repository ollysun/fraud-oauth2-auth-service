package com.etz.authorisationserver.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.ResultCheckStyle;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.io.Serializable;
import java.util.*;

@Entity
@Table(name = "permission")
@Getter
@Setter
@ToString
@SQLDelete(sql = "UPDATE permission SET deleted = true, status=0 WHERE id = ?", check = ResultCheckStyle.COUNT)
@Where(clause = "deleted=false")
@RequiredArgsConstructor
public class PermissionEntity extends BaseEntity implements Serializable {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "bigint")
    private long id;

    @Column(name = "name", unique = true, length = 200)
    private String name;

    @Column(nullable = false, name = "status", columnDefinition = "TINYINT", length = 1)
    private Boolean status;

    @ToString.Exclude
    @ManyToMany(mappedBy = "rolePermissionEntities", fetch = FetchType.LAZY)
    private List<Role> roles = new ArrayList<>();

    @ToString.Exclude
    @ManyToMany(mappedBy = "permissionEntities",fetch = FetchType.LAZY)
    private List<UserEntity> userEntities = new ArrayList<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PermissionEntity)) return false;
        if (!super.equals(o)) return false;
        PermissionEntity that = (PermissionEntity) o;
        return id == that.id && name.equals(that.name) && status.equals(that.status) && roles.equals(that.roles) && userEntities.equals(that.userEntities);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), id, name, status, roles, userEntities);
    }
}
