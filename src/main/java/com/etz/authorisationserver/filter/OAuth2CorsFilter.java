package com.etz.authorisationserver.filter;

import org.springframework.stereotype.Component;

import javax.servlet.*;
import java.io.IOException;
@Component
public class OAuth2CorsFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
                         FilterChain filterChain) throws IOException, ServletException {
        HttpServletResponse response = (HttpServletResponse) res;
        HttpServletRequest request = (HttpServletRequest) req;
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "POST, PUT, GET, OPTIONS, DELETE");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Headers", "x-requested-with, authorization, Content-Type");
        response.setHeader("Content-Type", "application/x-www-form-urlencoded");

        if ("OPTIONS".equalsIgnoreCase(request.getMethod()))
        {
            response.setStatus(HttpServletResponse.SC_OK);
        }
        else
        {
            chain.doFilter(req, res);
        }
    }

    @Override
    public void destroy() {

    }
}
