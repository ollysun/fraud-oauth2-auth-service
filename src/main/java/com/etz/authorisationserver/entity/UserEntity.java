package com.etz.authorisationserver.entity;

import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


@Entity
@Table(name = "user")
@Getter
@Setter
@ToString
@Where(clause = "deleted=false")
@RequiredArgsConstructor
public class UserEntity extends BaseEntity implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "username", unique = true, length = 250)
	private String username;

	@Column(name = "password", columnDefinition = "TEXT",nullable = false, unique = true)
	private String password;

	@Column(name = "first_name")
	private String firstName;

	@Column(name = "last_name")
	private String lastName;

	@Column(name = "phone", unique = true, length = 200)
	private String phone;


	@Column(name = "email", unique = true, length = 250)
	private String email;

	@Column( name = "has_Role", columnDefinition = "TINYINT default true", length = 1, nullable = false)
	private Boolean hasRole = Boolean.TRUE;

	@Column(name = "has_Permission", columnDefinition = "TINYINT default true", length = 1, nullable = false)
	private Boolean hasPermission = Boolean.TRUE;

	@Column(nullable = false, name = "status", columnDefinition = "TINYINT", length = 1)
	private Boolean status;
	
	@OneToMany(mappedBy = "userId",fetch = FetchType.LAZY,
			cascade = CascadeType.ALL, orphanRemoval = true)
	@ToString.Exclude
	private List<ResetPasswordTokens> resetPasswordTokens =new ArrayList<ResetPasswordTokens>();
	
	@ToString.Exclude
	@ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinTable(name = "user_role",
			joinColumns = {@JoinColumn(name = "user_id")},
			inverseJoinColumns = {@JoinColumn(name = "role_id")})
	private List<Role> roles = new ArrayList<>();

	@ToString.Exclude
	@ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinTable(
			name = "user_permission",
			joinColumns = @JoinColumn(name = "user_id"),
			inverseJoinColumns = @JoinColumn(name = "permission_id")
	)
	private List<PermissionEntity> permissionEntities = new ArrayList<>();

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
		UserEntity that = (UserEntity) o;

		return Objects.equals(id, that.id);
	}

	@Override
	public int hashCode() {
		return 1838525018;
	}
}
