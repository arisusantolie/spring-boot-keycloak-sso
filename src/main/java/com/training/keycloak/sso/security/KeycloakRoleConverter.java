package com.training.keycloak.sso.security;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class KeycloakRoleConverter implements Converter<Jwt, Collection<GrantedAuthority>> {
    @Override
    public Collection<GrantedAuthority> convert(Jwt jwt) {



        Map<String,Object> resourceAccess=(Map<String, Object>) jwt.getClaims().get("resource_access");
        Map<String,Object> resourceAccessGitClient=(Map<String, Object>) resourceAccess.get("finance");

        if(resourceAccessGitClient==null||resourceAccessGitClient.isEmpty()){
            return new ArrayList<>();
        }

		Collection<GrantedAuthority> returnValue=((List<String>) resourceAccessGitClient.get("roles")).stream().map(roleName-> "ROLE_"+roleName)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());


        return returnValue;
    }
}
