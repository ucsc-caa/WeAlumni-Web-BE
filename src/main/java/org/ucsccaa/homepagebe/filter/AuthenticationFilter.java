package org.ucsccaa.homepagebe.filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.ucsccaa.homepagebe.services.AuthenticationService;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter(filterName = "AuthenticationFilter", urlPatterns = {"/articles/*", "/users/*"})

public class AuthenticationFilter implements Filter {
    @Autowired
    private AuthenticationService authenticationService;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        String method = httpServletRequest.getMethod();
        String uri = httpServletRequest.getRequestURI();
        if (("POST".equals(method) && uri.contains("users")) || ("GET".equals(method) && uri.contains("articles"))) {
            chain.doFilter(httpServletRequest, httpServletResponse);
        } else {
            try {
                String token = httpServletRequest.getHeader("authorization").substring(6);
                if (authenticationService.validateToken(token)) {
                    chain.doFilter(httpServletRequest, httpServletResponse);
                } else {
                    httpServletResponse.sendError(401, "INVALID TOKEN");
                }
            } catch(Exception e) {
                httpServletResponse.sendError(401, "TOKEN NOT PROVIDED");
            }
        }
    }
}
