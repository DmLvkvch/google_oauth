package com.example.demo.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
@PropertySource("classpath:application.properties")
public class GoogleProperties {
    @Value("${server.port}")
    private Integer port;

    @Value("${google.client.id}")
    private String clientId;

    @Value("${google.client.secret}")
    private String clientSecret;

    @Value("${google.oauth.url}")
    private String oauthUrl;

    @Value("${google.redirect.endpoint}")
    private String redirectEndPoint;

    @Value("${google.scope.url}")
    private String scopeUrl;

    @Value("${google.scopes}")
    private List<String> scopes;

    @Value("${google.token.url}")
    private String tokenUrl;

    @Value("${google.mail.endpoint}")
    private String googleApiEndpointUrl;

    public String getGoogleApiEndpointUrl() {
        return googleApiEndpointUrl;
    }

    public String getTokenUrl() {
        return tokenUrl;
    }

    public String getClientId() {
        return clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }


    public String getOauthUrl() {
        return oauthUrl;
    }

    public String getRedirectEndPoint() {
        return redirectEndPoint;
    }

    public String getScopeUrl() {
        return scopeUrl;
    }

    public List<String> getScopes() {
        return scopes;
    }

    private String getScopesQuery(){
        String scopeUrl = getScopeUrl();
        StringBuilder scopes = new StringBuilder();
        for (String scope: getScopes()) {
            scopes.append("https://")
                    .append(scopeUrl)
                    .append(scope)
                    .append(" ");
        }
        return scopes.toString();
    }

    public String getRedirectUrl(){
        return UriComponentsBuilder.newInstance()
                .scheme("http")
                .host("localhost")
                .port(getPort())
                .path(getRedirectEndPoint())
                .build().toString();
    }

    public Integer getPort() {
        return port;
    }

    public String getFullOauthUrl(){
        UriComponents uriComponents = UriComponentsBuilder.newInstance()
                .scheme("https")
                .host(getOauthUrl())
                .queryParam("client_id", getClientId())
                .queryParam("redirect_uri", getRedirectUrl())
                .queryParam("response_type", "code")
                .queryParam("scope", getScopesQuery())
                .queryParam("access_type", "offline")
                .queryParam("state", "123").build();
        return uriComponents.toUri().toASCIIString();

    }

    public Map<Object, Object> getTokenRequestBody(String authCode){
        return new HashMap<>() {{
            put("client_id", getClientId());
            put("client_secret", getClientSecret());
            put("code", authCode);
            put("grant_type", "authorization_code");
            put("redirect_uri", getRedirectUrl());
        }};
    }
}
