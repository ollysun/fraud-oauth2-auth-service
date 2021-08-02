package com.etz.authorisationserver.services;

import com.etz.authorisationserver.entity.PermissionEntity;
import com.etz.authorisationserver.entity.Role;
import com.etz.authorisationserver.entity.UserEntity;
import com.etz.authorisationserver.repository.UserRepository;
import com.etz.authorisationserver.security.SecurityUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Slf4j
@Service
@Transactional
public class CustomUserDetailService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        UserEntity user = userRepository.findByUsernameAndDeletedFalseAndStatusTrue(username);
        if (user == null)
            throw new UsernameNotFoundException("Could not find UserEntity");
        return new SecurityUser(user, getAuthorities(user.getRoles(), user));
    }

    private Collection<? extends GrantedAuthority> getAuthorities(final Collection<Role> roles, UserEntity user) {
        return getGrantedAuthorities(getPermissions(roles, user));
    }

    private List<String> getPermissions(final Collection<Role> roles, UserEntity user) {
        final List<String> permissions = new ArrayList<>();
        final List<PermissionEntity> collection = new ArrayList<>();
        for (final Role role : roles) {
            collection.addAll(role.getRolePermissionEntities());
        }
        if (!user.getPermissionEntities().isEmpty()) {
            collection.addAll(user.getPermissionEntities());
        }
        for (final PermissionEntity item : collection) {
            permissions.add(item.getName());
        }

        return permissions;
    }

    private List<GrantedAuthority> getGrantedAuthorities(final List<String> permissions) {
        final List<GrantedAuthority> authorities = new ArrayList<>();
        for (final String permission : permissions) {
            authorities.add(new SimpleGrantedAuthority(permission));
        }
        return authorities;
    }
}
