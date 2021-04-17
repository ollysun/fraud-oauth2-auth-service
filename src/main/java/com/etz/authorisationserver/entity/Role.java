package com.etz.authorisationserver.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;
import java.util.*;

@Entity
@Table(name = "role")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class Role extends BaseEntity implements Serializable {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "bigint")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(nullable = false, name = "status", columnDefinition = "TINYINT", length = 1)
    private Boolean status;

    @ToString.Exclude
    @ManyToMany(mappedBy = "roles", cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    private List<User> users = new ArrayList<>();

    @ToString.Exclude
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "role_permission",
            joinColumns = @JoinColumn(name = "role_id",
                    nullable = false, updatable = false),
            inverseJoinColumns = @JoinColumn(name = "permission_id",
                    nullable = false, updatable = false)
    )
    private List<Permission> rolePermissions = new ArrayList<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Role)) return false;
        if (!super.equals(o)) return false;
        Role role = (Role) o;
        return id.equals(role.id) && Objects.equals(name, role.name) && description.equals(role.description) && status.equals(role.status) && users.equals(role.users) && rolePermissions.equals(role.rolePermissions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), id, name, description, status, users, rolePermissions);
    }
}
