package com.nr.authcodeflow.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

/*
JwtAuthenticationConverter in package org.springframework.security.oauth2.server.resource.authentication;
 */
@Component
public class JwtAuthConverter implements Converter<Jwt, AbstractAuthenticationToken> {
    private final JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();

    @Value("${app.clientId}")
    private String clientName;

    @Override
    public AbstractAuthenticationToken convert(Jwt jwt) {

        var authorities = Stream.concat(
                jwtGrantedAuthoritiesConverter.convert(jwt).stream(),
                extractRoles(jwt).stream()
        );
        return new JwtAuthenticationToken(jwt, authorities.toList(), getPrincipleClaimName(jwt));
    }

    private String getPrincipleClaimName(Jwt jwt) {
        return jwt.getClaim("preferred_username") != null ? jwt.getClaim("preferred_username") : jwt.getSubject();
    }

    private Collection<? extends GrantedAuthority> extractRoles(Jwt jwt) {

        if (!jwt.hasClaim("resource_access")) {
            return Set.of();
        }
        Map<String, Object> resourceAccess = jwt.getClaim("resource_access");
        if (!resourceAccess.containsKey(clientName)) {
            return Set.of();
        }
        Map<String, Object> clientInfo = (Map<String, Object>) resourceAccess.get(clientName);
        if (!clientInfo.containsKey("roles")) {
            return Set.of();
        }
        var roles = (Collection<String>) clientInfo.get("roles");
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                .toList();
    }
}
