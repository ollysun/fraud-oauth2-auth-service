package com.etz.authorisationserver.entity;

import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        UserRole userRole = (UserRole) o;

        return id != null && id.equals(userRole.id);
    }

    @Override
    public int hashCode() {
        return 1195449715;
    }
}
