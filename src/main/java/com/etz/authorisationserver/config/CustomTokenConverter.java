package com.etz.authorisationserver.config;

import com.etz.authorisationserver.entity.Role;
import com.etz.authorisationserver.entity.User;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class CustomTokenConverter extends JwtAccessTokenConverter {

    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {

        final Map<String, Object> additionalInfo = new HashMap<>();
        additionalInfo.put("customized", "true");
        User user = (User) authentication.getPrincipal();
        additionalInfo.put("role", user.getRoles().stream().map(Role::getName).collect(Collectors.toList()));
        ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(additionalInfo);

        return super.enhance(accessToken, authentication);
    }
}
