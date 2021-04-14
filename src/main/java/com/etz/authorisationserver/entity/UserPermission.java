package com.etz.authorisationserver.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Table(name = "user_permission")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class UserPermission extends BaseEntity {

    @Id
    @Column(name = "id", columnDefinition = "bigint")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "user_id", columnDefinition = "bigint", referencedColumnName = "id", foreignKey = @ForeignKey(name = "FK_USER_PERMISSION_ROLE_ID"))
    private Long userId;

    @JoinColumn(name = "permission_id", columnDefinition = "bigint", referencedColumnName = "id", foreignKey = @ForeignKey(name = "FK_USER_PERMISSION_PERMISSION_ID"))
    private Long permissionId;


}
