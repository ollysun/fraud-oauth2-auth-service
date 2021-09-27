package com.etz.authorisationserver.dto.request;

import java.io.Serializable;
import java.util.Objects;

import org.hibernate.Hibernate;

import com.etz.authorisationserver.entity.BaseEntity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
//@JsonIgnoreProperties(value = {"roleId", "userId", "system", "status", "version", "delete"})
public class UserNotification extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private Long id;

	private String entityName;
	
	private String entityID;
	
	private int notifyType;

	private Long roleId;
	
	private Long userId;
	
	private Boolean system = false;
	
	private String message;
	
	private Boolean status;
	
	private int version;


	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
		UserNotification that = (UserNotification) o;

		return Objects.equals(id, that.id);
	}

	@Override
	public int hashCode() {
		return 1854275613;
	}
}
