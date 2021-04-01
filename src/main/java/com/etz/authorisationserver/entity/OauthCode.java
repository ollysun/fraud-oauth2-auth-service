package com.etz.authorisationserver.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

@Data
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

	
	
}
