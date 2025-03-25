package com.nimbleways.springboilerplate.common.api.security;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.web.authentication.www.BasicAuthenticationConverter;

import java.io.IOException;

public class SimpleBasicAuthenticationFilter implements Filter {
    public static final BasicAuthenticationConverter BASIC_AUTHENTICATION_CONVERTER = new BasicAuthenticationConverter();
    private final String realm;
    private final String email;
    private final String password;

    public SimpleBasicAuthenticationFilter(String realm, String email, String password) {
        this.realm = realm;
        this.email = email;
        this.password = password;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        if (!isAuthorized(httpRequest)) {
            String headerValue = "%s realm=\"%s\"".formatted(BasicAuthenticationConverter.AUTHENTICATION_SCHEME_BASIC,
                    realm);
            httpResponse.setHeader("WWW-Authenticate", headerValue);
            throw new AuthorizationDeniedException("Access Denied");
        }
        chain.doFilter(request, response);
    }

    private boolean isAuthorized(HttpServletRequest httpRequest) {
        UsernamePasswordAuthenticationToken authenticationToken = BASIC_AUTHENTICATION_CONVERTER.convert(httpRequest);
        return authenticationToken != null
                && email.equals(authenticationToken.getPrincipal())
                && password.equals(authenticationToken.getCredentials());
    }
}
