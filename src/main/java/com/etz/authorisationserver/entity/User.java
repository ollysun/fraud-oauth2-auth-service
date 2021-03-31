package com.etz.authorisationserver.entity;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;


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

	@NotBlank(message = "Last name cannot be empty")
	@Column(name = "phone")
	private String phone;

	@Email(message="please enter valid email")
	@NotBlank(message = "email cannot be empty")
	@Column(name = "email")
	private String email;

	private Boolean status;

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "users_role",
			joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
			inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
	private Collection<Role> roles;

	@ToString.Exclude
	@OneToMany(mappedBy = "user")
	private Collection<UserPermission> userPermissions = new HashSet<>();

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
