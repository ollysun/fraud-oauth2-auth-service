package com.etz.authorisationserver.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Collections;

@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

    private static final String RESOURCE_ID = "test-manager";

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) {
        resources.resourceId(RESOURCE_ID).tokenStore(tokenStore()).stateless(false);
    }

    @Bean
    public TokenStore tokenStore(){
        return new JwtTokenStore(converter());
    }

//    @Override
//    public void configure(HttpSecurity http) throws Exception {
//        http.cors().and().csrf().disable();
//        http.authorizeRequests()
//                .antMatchers("/health","/info", "/trace", "/monitoring",
//                             "/webjars/**","/swagger.html", "/api/**")
//                .permitAll();
//        http.authorizeRequests().antMatchers("/api/v1/**")
//                .authenticated();
//    }

    @Bean
    public DefaultTokenServices tokenServices(final TokenStore tokenStore) {
        DefaultTokenServices tokenServices = new DefaultTokenServices();
        tokenServices.setTokenStore(tokenStore);
        return tokenServices;
    }


    @Bean
    public JwtAccessTokenConverter converter(){
        JwtAccessTokenConverter conv = new  JwtAccessTokenConverter();
        conv.setSigningKey("AuthETransactNgView2021");
        return conv;
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(Collections.singletonList("*"));
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        config.setAllowCredentials(true);
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}
