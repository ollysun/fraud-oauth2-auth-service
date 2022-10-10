package com.etz.authorisationserver.config;

import java.io.IOException;
import java.util.Objects;
import java.util.Optional;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.filter.OncePerRequestFilter;

import com.etz.authorisationserver.constant.AppConstant;
import com.etz.authorisationserver.util.RequestUtil;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException, ServletException, IOException {

        String authToken = getAuthToken(req.getHeader(AppConstant.AUTHORIZATION)).orElse(null);
        RequestUtil.setToken(authToken);
        
        chain.doFilter(req, res);
    }

    private Optional<String> getAuthToken(String header){
        if (!Objects.isNull(header) && header.startsWith(AppConstant.TOKEN_PREFIX)) {
            return Optional.of(header.replace(AppConstant.TOKEN_PREFIX, ""));
        }
        return Optional.ofNullable(header);
    }
}