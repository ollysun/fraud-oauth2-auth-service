package com.etz.authorisationserver.entity;


import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Table(name="oauth_refresh_token")
public class OauthRefreshToken {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID", columnDefinition = "bigint unsigned")
	private Integer id;
	
	@Column(name="token_id")
	private String tokenId;
	
	@Lob
	@Column(name="token", columnDefinition = "mediumblob")
	private byte[] token;
	
	@Lob
	@Column(name="authentication", columnDefinition = "mediumblob")
	private byte[] authentication;

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
		OauthRefreshToken that = (OauthRefreshToken) o;

		return id != null && id.equals(that.id);
	}

	@Override
	public int hashCode() {
		return 586801522;
	}
}

