package com.etz.authorisationserver.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Where;
import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "user_permission")
@Getter
@Setter
@ToString
@Where(clause = "deleted=false")
@RequiredArgsConstructor
public class UserPermission extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", columnDefinition = "bigint")
    private Long userId;

    @Column(name = "permission_id", columnDefinition = "bigint")
    private Long permissionId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserPermission)) return false;
        if (!super.equals(o)) return false;
        UserPermission that = (UserPermission) o;
        return userId.equals(that.userId) && permissionId.equals(that.permissionId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(),  userId, permissionId);
    }
}
