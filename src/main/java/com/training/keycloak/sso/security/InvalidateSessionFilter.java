package com.training.keycloak.sso.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class InvalidateSessionFilter extends OncePerRequestFilter {

    @Autowired
    OAuth2AuthorizedClientService clientService;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    KeycloakLogoutHandler keycloakLogoutHandler;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        System.out.println(request.getRequestURI());
        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        try {
            // Check the user's session on the Keycloak server
            //auth/realms/develop/protocol/openid-connect/userinfo
//            System.out.println(authentication.getName()+"   aro");
            OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;

            OAuth2AuthorizedClient client =
                    clientService.loadAuthorizedClient(
                            oauthToken.getAuthorizedClientRegistrationId(),
                            oauthToken.getName());

            String accessToken = client.getAccessToken().getTokenValue();
            OidcUser oidcUser=(OidcUser) authentication.getPrincipal();

//            System.out.println(accessToken);
            String sessionValidationUrl = oidcUser.getIssuer()+"/protocol/openid-connect/userinfo";

            HttpHeaders headers = new HttpHeaders();
//            System.out.println("oidcUser.getUserInfo().toString() = "+oidcUser.getUserInfo().toString().toString());
            headers.set("Authorization", "Bearer "+accessToken);
            HttpEntity<?> entity = new HttpEntity<>(headers);

//            restTemplate.getForEntity(sessionValidationUrl, HttpMethod.POST,entity, Void.class);
            ResponseEntity<Void> userInfo = restTemplate.exchange(sessionValidationUrl, HttpMethod.GET, entity, Void.class);
//            System.out.println(userInfo.getStatusCode());
            // If the session is valid, continue with the login
            filterChain.doFilter(request, response);


        } catch (HttpClientErrorException e) {
//            e.printStackTrace();
            if (e.getStatusCode() == HttpStatus.UNAUTHORIZED) {
                // If the session is no longer valid, you can log the user out or take other appropriate actions.
                request.getSession().invalidate();
                // Redirect to a logout success page or other appropriate action
                response.sendRedirect("/");
            } else {
                // Handle other errors as needed
//                filterChain.doFilter(request, response);
            }
        }
    }
}
