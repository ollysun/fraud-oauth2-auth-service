package com.etz.authorisationserver.entity;

import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Table(name="oauth_code")
public class OauthCode {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID", columnDefinition = "bigint unsigned")
	private Integer id;
	
	@Column(name="code")
	private String code;
	
	@Lob
	@Column(name="authentication", columnDefinition = "mediumblob")
	private byte[] authentication;


	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
		OauthCode oauthCode = (OauthCode) o;

		return id != null && id.equals(oauthCode.id);
	}

	@Override
	public int hashCode() {
		return 2045389063;
	}
}
