package com.etz.authorisationserver.entity;

import lombok.*;
import org.hibernate.annotations.ResultCheckStyle;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Where(clause = "deleted=false")
@Table(name = "user_role")
public class UserRole extends BaseEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", columnDefinition = "bigint")
    private Long userId;

    @Column(name = "role_id", columnDefinition = "bigint")
    private Long roleId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserRole)) return false;
        if (!super.equals(o)) return false;
        UserRole userRole = (UserRole) o;
        return userId.equals(userRole.userId) && roleId.equals(userRole.roleId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), userId, roleId);
    }
}
