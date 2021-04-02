package com.etz.authorisationserver.sevices;

import com.etz.authorisationserver.entity.Permission;
import com.etz.authorisationserver.entity.Role;
import com.etz.authorisationserver.entity.User;
import com.etz.authorisationserver.repository.IUserRepository;
import com.etz.authorisationserver.security.SecurityUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
@Transactional
public class CustomUserDetailService implements UserDetailsService {

    @Autowired
    private IUserRepository iUserRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = iUserRepository.findByUsername(username);
        if (user == null)
            throw new UsernameNotFoundException("Could not find User");
        return new SecurityUser(user, getAuthorities(user.getRoles()));
    }

    private Collection<? extends GrantedAuthority> getAuthorities(final Collection<Role> roles) {
        return getGrantedAuthorities(getPermissions(roles));
    }

    private List<String> getPermissions(final Collection<Role> roles) {
        final List<String> permissions = new ArrayList<>();
        final List<Permission> collection = new ArrayList<>();
        for (final Role role : roles) {
            collection.addAll(role.getPermissions());
        }
        for (final Permission item : collection) {
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
