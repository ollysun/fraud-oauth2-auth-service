package com.etz.authorisationserver.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "role")
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
    @Enumerated(EnumType.ORDINAL)
    private Boolean status;

    @ToString.Exclude
    @OneToMany(mappedBy = "permission")
    Set<RolePermission> rolePermission = new HashSet<>();

    @ToString.Exclude
    @OneToMany(mappedBy = "permission")
    Set<UserPermission> userPermission = new HashSet<>();
}
