package com.portfolio.gateway.config;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

class KeycloakAuthoritiesConverter implements Converter<Jwt, Collection<GrantedAuthority>> {

    private static final String REALM_ACCESS = "realm_access";
    private static final String RESOURCE_ACCESS = "resource_access";
    private static final String ROLES = "roles";

    @Override
    public Collection<GrantedAuthority> convert(Jwt jwt) {
        if (jwt == null) {
            return Collections.emptyList();
        }
        Set<String> roles = new HashSet<>();

        Map<String, Object> realm = jwt.getClaim(REALM_ACCESS);
        if (realm != null) {
            roles.addAll(extractRoles(realm));
        }
        Map<String, Object> resource = jwt.getClaim(RESOURCE_ACCESS);
        if (resource != null) {
            resource.values().stream()
                .filter(Map.class::isInstance)
                .map(Map.class::cast)
                .forEach(entry -> roles.addAll(extractRoles(entry)));
        }

        return roles.stream()
            .map(role -> role.startsWith("ROLE_") ? role : "ROLE_" + role)
            .map(SimpleGrantedAuthority::new)
            .collect(Collectors.toUnmodifiableSet());
    }

    @SuppressWarnings("unchecked")
    private Collection<String> extractRoles(Map<String, Object> source) {
        Object roles = source.get(ROLES);
        if (roles instanceof Collection<?> collection) {
            return collection.stream()
                .filter(String.class::isInstance)
                .map(String.class::cast)
                .collect(Collectors.toSet());
        }
        return Collections.emptySet();
    }
}
