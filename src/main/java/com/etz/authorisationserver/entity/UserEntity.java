package com.etz.authorisationserver.entity;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "user")
@Data
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

	@ToString.Exclude
	@ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinTable(name = "user_role",
			joinColumns = @JoinColumn(name = "user_id"),
			inverseJoinColumns = @JoinColumn(name = "role_id"))
	private List<Role> roles = new ArrayList<>();


	@ToString.Exclude
	@ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinTable(
			name = "user_permission",
			joinColumns = @JoinColumn(name = "user_id"),
			inverseJoinColumns = @JoinColumn(name = "permission_id")
	)
	private List<Permission> permissions = new ArrayList<>();

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		UserEntity that = (UserEntity) o;

		return id != null && id.equals(that.id);
	}

	@Override
	public int hashCode() {
		return getClass().hashCode();
	}
}
