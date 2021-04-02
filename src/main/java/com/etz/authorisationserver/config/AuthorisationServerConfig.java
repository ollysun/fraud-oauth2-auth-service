package com.etz.authorisationserver.config;

import com.etz.authorisationserver.sevices.CustomUserDetailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

import javax.sql.DataSource;
import java.util.Objects;

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
    private JdbcTokenStores jdbcTokenStores;

    public AuthorisationServerConfig(DataSource dataSource, PasswordEncoder passwordEncoder,
                                     AuthenticationManager authenticationManager,
                                     CustomUserDetailService userDetailsService, JdbcTokenStores jdbcTokenStores) {
        this.dataSource = dataSource;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.jdbcTokenStores = jdbcTokenStores;
    }

//    @Bean
//    public JdbcTokenStore tokenStore() {
//        return new JdbcTokenStore(Objects.requireNonNull(jdbcTemplate.getDataSource()));
//    }



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
        JwtAccessTokenConverter jwtAccessTokenConverter = new JwtAccessTokenConverter();
//        KeyStoreKeyFactory keyStoreKeyFactory =
//                new KeyStoreKeyFactory(
//                        new ClassPathResource("auth.jks"),"auth123".toCharArray());
//          jwtAccessTokenConverter.setKeyPair(keyStoreKeyFactory.getKeyPair("auth"));
//        return jwtAccessTokenConverter;

//        ClassPathResource ksFile =
//                new ClassPathResource("auth.jks");
//        KeyStoreKeyFactory ksFactory =
//                new KeyStoreKeyFactory(ksFile, "auth123".toCharArray());
//        KeyPair keyPair = ksFactory.getKeyPair("auth");
//        jwtAccessTokenConverter.setKeyPair(keyPair);
//        return jwtAccessTokenConverter;

        jwtAccessTokenConverter.setSigningKey("secret");
        return jwtAccessTokenConverter;
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.jdbc(this.dataSource);
    }

    @Override
    public void configure(final AuthorizationServerEndpointsConfigurer endpoints) {
        endpoints.authenticationManager(authenticationManager)
                .accessTokenConverter(jwtAccessTokenConverter())
                .userDetailsService(userDetailsService)
                .tokenStore(jdbcTokenStores);
    }


    @Override
    public void configure(AuthorizationServerSecurityConfigurer oauthServer) {
        oauthServer.passwordEncoder(passwordEncoder)
                .tokenKeyAccess("permitAll()")
                .checkTokenAccess("isAuthenticated()");
    }

//    private KeyPair keyPair(SecurityProperties.JwtProperties jwtProperties, KeyStoreKeyFactory keyStoreKeyFactory) {
//        return keyStoreKeyFactory.getKeyPair(jwtProperties.getKeyPairAlias(), jwtProperties.getKeyPairPassword().toCharArray());
//    }
//
//    private KeyStoreKeyFactory keyStoreKeyFactory(SecurityProperties.JwtProperties jwtProperties) {
//        return new KeyStoreKeyFactory(jwtProperties.getKeyStore(), jwtProperties.getKeyStorePassword().toCharArray());
//    }
}
