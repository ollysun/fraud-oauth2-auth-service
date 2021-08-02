package com.etz.authorisationserver.config;

import com.etz.authorisationserver.entity.Role;
import com.etz.authorisationserver.entity.UserEntity;
import com.etz.authorisationserver.security.SecurityUser;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class CustomTokenConverter extends JwtAccessTokenConverter {

    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {

        final Map<String, Object> additionalInfo = new HashMap<>();

        SecurityUser securityUser = (SecurityUser) authentication.getPrincipal();
        UserEntity user = securityUser.getUser();
        additionalInfo.put("role", user.getRoles().stream().map(Role::getName).collect(Collectors.toList()));
        additionalInfo.put("userId", securityUser.getUser().getId());

        ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(additionalInfo);
        accessToken = super.enhance(accessToken, authentication);
        ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(new HashMap<>());
        return accessToken;
    }


}
