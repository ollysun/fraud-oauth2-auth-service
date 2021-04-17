package com.etz.authorisationserver.config;

import com.etz.authorisationserver.entity.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.provider.token.DefaultUserAuthenticationConverter;

import java.util.Map;

public class CustomUserAuthenticationConverter extends DefaultUserAuthenticationConverter {

    @Override
    public Map<String, ?> convertUserAuthentication(Authentication authentication) {

        Map<String, Object> response = (Map<String, Object>) super.convertUserAuthentication(authentication);

        // Add additional info
        User user = (User) authentication.getPrincipal();
        response.put("role", user.getRoles().get(0).getName());
        return response;
    }
}
