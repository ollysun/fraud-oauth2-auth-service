package com.etz.authorisationserver.config;

import com.etz.authorisationserver.services.CustomUserDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;


import java.util.Collections;



@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final CustomUserDetailService customUserDetailService;

	private static final String[] SWAGGER_WHITELIST = {
            // -- swagger ui
            "/swagger", "/v2/api-docs", "/swagger-resources", "/swagger-resources/**", "/configuration/ui", "/actuator/health",
            "/configuration/security", "/swagger-ui.html", "/webjars/**" };

//    @Bean
//    @Override
//    public UserDetailsService userDetailsService(){
//        return new CustomUserDetailService();
//    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {//details of a user are fetched from the db for authentication
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(customUserDetailService);
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }

    @Override
    protected void configure(final AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(customUserDetailService)
                .passwordEncoder(passwordEncoder());
    }


    @Override
    public void configure(HttpSecurity http) throws Exception {
        // Disabling CSRF protection due to stateless authentication
        http .csrf().disable();
                // be stateless we do not allow cookie use oauth2  jwt
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.authorizeRequests()
                .antMatchers("/health","/info", "/trace", "/monitoring",
                        "/webjars/**","/swagger.html", "/api/v1/login","/api/oauth/client","/api/oauth/token")
                .permitAll()
                .antMatchers(SWAGGER_WHITELIST)
                .permitAll();
        http.authorizeRequests().antMatchers("/api/v1/**")
                .authenticated();

    }



    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManager();
    }


    @Bean
    public FilterRegistrationBean<CorsFilter> corFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

        CorsConfiguration configAutenticacao = new CorsConfiguration();
        configAutenticacao.setAllowCredentials(true);
        configAutenticacao.setAllowedOriginPatterns(Collections.singletonList("*"));
        configAutenticacao.addAllowedHeader("*");
        configAutenticacao.addAllowedMethod("*");
        source.registerCorsConfiguration("/**", configAutenticacao); // Global for all paths

        FilterRegistrationBean<CorsFilter> bean = new FilterRegistrationBean<>(new CorsFilter(source));
        bean.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return bean;
    }

}
