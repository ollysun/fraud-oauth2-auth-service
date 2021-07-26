package com.etz.authorisationserver.entity;

import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "reset_password_tokens")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ResetPasswordTokens extends BaseEntity implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ToString.Exclude
	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "user_id",nullable = false,columnDefinition = "int(50)")
	private UserEntity userId;
	
	@Column(name = "token", length = 150,nullable = false)
	private String token;
	
	@Column(name = "consumed",nullable = false)
	private boolean consumed;
	
	@Column(name = "expired",nullable = false)
	private boolean expired;
	
	@Column(name = "expirationDate",nullable = false)
	private LocalDateTime expirationDate;

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
		ResetPasswordTokens that = (ResetPasswordTokens) o;

		return Objects.equals(id, that.id);
	}

	@Override
	public int hashCode() {
		return 283571791;
	}
}
