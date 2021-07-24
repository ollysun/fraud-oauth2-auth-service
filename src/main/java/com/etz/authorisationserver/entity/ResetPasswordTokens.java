package com.etz.authorisationserver.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.etz.authorisationserver.util.Uuid5;

import lombok.Data;

@Entity
@Table(name = "reset_password_tokens")
@Data
public class ResetPasswordTokens extends BaseEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7809557303795928489L;


	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	
	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "user_id",nullable = false,columnDefinition = "int(50)")
	//@Column(name = "userID", columnDefinition = "int(50)",nullable = false)
	private UserEntity userId;
	
	@Column(name = "token", length = 150,nullable = false)//Hashed UUID 4 ID?
	private String token;
	
	@Column(name = "consumed",nullable = false)
	private boolean consumed;
	
	@Column(name = "expired",nullable = false)
	private boolean expired;
	
	@Column(name = "expirationDate",nullable = false)//Token expiration date. 30 minute from creation time.
	private Calendar expirationDate;
	
	
}
