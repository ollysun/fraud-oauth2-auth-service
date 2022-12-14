package com.etz.authorisationserver.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "role")
@Getter
@Setter
@ToString
@Where(clause = "deleted=false")
@RequiredArgsConstructor
public class Role extends BaseEntity implements Serializable {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "bigint")
    private Long id;

    @Column(name = "name", unique = true, length = 200)
    private String name;

    @Column(name = "description")
    private String description;

    @Column(nullable = false, name = "status", columnDefinition = "TINYINT", length = 1)
    private Boolean status;
	
	@Column(name = "authorised", columnDefinition = "TINYINT", length = 1)
	private Boolean authorised;
	
	@Column(name = "authoriser", length=100)
	private String authoriser;

    @ToString.Exclude
    @ManyToMany(mappedBy = "roles", cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    private List<UserEntity> userEntity = new ArrayList<>();
    

    @ToString.Exclude
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "role_permission",
            joinColumns = @JoinColumn(name = "role_id",
                    nullable = false, updatable = false),
            inverseJoinColumns = @JoinColumn(name = "permission_id",
                    nullable = false, updatable = false)
    )
    private List<PermissionEntity> rolePermissionEntities = new ArrayList<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Role role = (Role) o;

        return Objects.equals(id, role.id);
    }

    @Override
    public int hashCode() {
        return 1179619963;
    }
}
