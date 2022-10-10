package com.etz.authorisationserver.security;

import com.etz.authorisationserver.entity.UserEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public class SecurityUser implements UserDetails {

    /**
	 * 
	 */
	private static final long serialVersionUID = 6501213562982986167L;

	private final UserEntity user;

    private final Collection<? extends GrantedAuthority> roles;

    public SecurityUser(UserEntity user, Collection<? extends GrantedAuthority> roles) {
        this.user = user;
        this.roles = roles;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return user.getStatus();
    }

    public UserEntity getUser() {
        return user;
    }
}
