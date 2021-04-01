package com.etz.authorisationserver.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Data
@Table(name = "role_permission")
public class RolePermission extends BaseEntity implements Serializable
{
    private static final long serialVersionUID = -6952935229225959482L;

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "role_id", columnDefinition = "bigint")
    private Long roleId;

    @Column(name = "permission_id", columnDefinition = "bigint")
    private Long permissionId;
}
