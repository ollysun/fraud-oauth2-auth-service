package com.etz.authorisationserver.entity;

import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalTime;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Table(name="oauth_client_details")
public class OauthClientDetails {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", columnDefinition = "bigint unsigned")
	private Integer id;
	
	@Column(name="client_id")
	private String clientId;
	
	@Column(name="client_name")
	private String clientName;
	
	@Column(name="resource_ids")
	private String resourceIds;
	
	@Column(name="client_secret")
	private String clientSecret;
	
	@Column(name="scope")
	private String scope;
	
	@Column(name="authorized_grant_types")
	private String authorizedGrantTypes;
	
	@Column(name="web_server_redirect_uri")
	private String webServerRedirectUri;
	
	@Column(name="authorities")
	private String authorities;
	
	@Column(name="access_token_validity", length=11)
	private Integer accessTokenValidity;
	
	@Column(name="refresh_token_validity", length=11)
	private Integer refreshTokenValidity;
	
	@Column(name="additional_information", length=4096)
	private String additionalInformation;
	
	@Column(name="autoapprove", columnDefinition = "TINYINT(4)")
	private Integer autoapprove;

	@Column(name = "created_at")
	@CreationTimestamp
	private LocalTime createdAt;


	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
		OauthClientDetails that = (OauthClientDetails) o;

		return id != null && id.equals(that.id);
	}

	@Override
	public int hashCode() {
		return 608143201;
	}
}

