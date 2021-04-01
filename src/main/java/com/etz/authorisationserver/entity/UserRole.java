package com.etz.authorisationserver.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Data
@Table(name = "user_role")
public class UserRole extends BaseEntity implements Serializable
{
    private static final long serialVersionUID = -6952935229225959482L;

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", columnDefinition = "bigint")
    private Long userId;

    @Column(name = "role_id", columnDefinition = "bigint")
    private Long roleId;
}
