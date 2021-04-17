package com.etz.authorisationserver.entity;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Entity
@Table(name = "user")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class User extends BaseEntity implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank(message = "User number cannot be empty")
	@Column(name = "username")
	private String username;

	@NotBlank(message = "User name cannot be empty")
	@Column(name = "password", columnDefinition = "TEXT",nullable = false)
	private String password;

	@NotBlank(message = "First name cannot be empty")
	@Column(name = "first_name")
	private String firstName;

	@NotBlank(message = "Last name cannot be empty")
	@Column(name = "last_name")
	private String lastName;

	@NotBlank(message = "Phone cannot be empty")
	@Column(name = "phone")
	private String phone;

	@Email(message="please enter valid email")
	@NotBlank(message = "email cannot be empty")
	@Column(name = "email")
	private String email;

	@Column( name = "has_Role", columnDefinition = "TINYINT", length = 1)
	private Boolean hasRole = Boolean.TRUE;

	@Column(name = "has_Permission", columnDefinition = "TINYINT", length = 1)
	private Boolean hasPermission = Boolean.FALSE;

	@Column(nullable = false, name = "status", columnDefinition = "TINYINT", length = 1)
	private Boolean status;

	@ToString.Exclude
	@ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
	@JoinTable(name = "user_role",
			joinColumns = @JoinColumn(name = "user_id"),
			inverseJoinColumns = @JoinColumn(name = "role_id"))
	private List<Role> roles = new ArrayList<>();


	@ToString.Exclude
	@ManyToMany
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
		User that = (User) o;

		return id != null && id.equals(that.id);
	}

	@Override
	public int hashCode() {
		return getClass().hashCode();
	}
}
