package com.etz.authorisationserver.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "permission")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class Permission extends BaseEntity {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "bigint")
    private long id;

    @Column(name = "name")
    private String name;

    @Column(nullable = false, name = "status", columnDefinition = "TINYINT", length = 1)
    private Boolean status;


    @ToString.Exclude
    @OneToMany(mappedBy = "permission")
    private Set<UserPermission> userPermission = new HashSet<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Permission)) return false;
        if (!super.equals(o)) return false;
        Permission that = (Permission) o;
        return id == that.id && name.equals(that.name) && status.equals(that.status) && userPermission.equals(that.userPermission);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), id, name, status, userPermission);
    }
}
