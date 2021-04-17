package com.etz.authorisationserver.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.*;

@Entity
@Table(name = "permission")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class Permission extends BaseEntity{
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "bigint")
    private long id;

    @Column(name = "name")
    private String name;

    @Column(nullable = false, name = "status", columnDefinition = "TINYINT", length = 1)
    private Boolean status;

    @ToString.Exclude
    @ManyToMany(mappedBy = "rolePermissions", fetch = FetchType.LAZY)
    private List<Role> roles = new ArrayList<>();

    @ToString.Exclude
    @ManyToMany(mappedBy = "permissions",fetch = FetchType.LAZY)
    private List<User> users = new ArrayList<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Permission)) return false;
        if (!super.equals(o)) return false;
        Permission that = (Permission) o;
        return id == that.id && name.equals(that.name) && status.equals(that.status) && roles.equals(that.roles) && users.equals(that.users);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), id, name, status, roles, users);
    }
}
