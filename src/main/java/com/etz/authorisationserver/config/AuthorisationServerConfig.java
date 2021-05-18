package com.etz.authorisationserver.config;

import com.etz.authorisationserver.services.CustomUserDetailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.token.*;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

import javax.sql.DataSource;
import java.util.Arrays;
import java.util.Collections;

@Slf4j
@Configuration
@EnableAuthorizationServer
public class AuthorisationServerConfig
        extends AuthorizationServerConfigurerAdapter {

    private final DataSource dataSource;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final CustomUserDetailService userDetailsService;
    @Autowired
    private final JdbcTokenStores jdbcTokenStores;

    public AuthorisationServerConfig(DataSource dataSource, PasswordEncoder passwordEncoder,
                                     AuthenticationManager authenticationManager,
                                     CustomUserDetailService userDetailsService, JdbcTokenStores jdbcTokenStores) {
        this.dataSource = dataSource;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.jdbcTokenStores = jdbcTokenStores;
    }

    @Bean
    @Primary
    public DefaultTokenServices tokenServices(final TokenStore tokenStore,
                                              final ClientDetailsService clientDetailsService) {
        DefaultTokenServices tokenServices = new DefaultTokenServices();
        tokenServices.setSupportRefreshToken(true);
        tokenServices.setTokenStore(jdbcTokenStores);
        tokenServices.setClientDetailsService(clientDetailsService);
        tokenServices.setAuthenticationManager(this.authenticationManager);
        return tokenServices;
    }

    @Bean
    public JwtAccessTokenConverter jwtAccessTokenConverter() {
        CustomTokenConverter tokenConverter = new CustomTokenConverter();
        tokenConverter.setSigningKey("AuthETransactNgView2021");
        return tokenConverter;
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.jdbc(this.dataSource);
        log.info(String.valueOf(clients));
    }

    @Override
    public void configure(final AuthorizationServerEndpointsConfigurer endpoints) {
        TokenEnhancerChain enhancerChain = new TokenEnhancerChain();
        enhancerChain.setTokenEnhancers(Collections.singletonList(jwtAccessTokenConverter()));
        endpoints.authenticationManager(authenticationManager)
                .accessTokenConverter(jwtAccessTokenConverter())
                .userDetailsService(userDetailsService)
                .tokenStore(jdbcTokenStores)
                .tokenEnhancer(enhancerChain);

        log.info(endpoints.userDetailsService(userDetailsService).toString());

    }


    @Override
    public void configure(AuthorizationServerSecurityConfigurer oauthServer) {
        oauthServer.passwordEncoder(passwordEncoder)
                .tokenKeyAccess("permitAll()")
                .checkTokenAccess("isAuthenticated()")
                .allowFormAuthenticationForClients();
    }



}
