package com.example.demo.service;

import com.example.demo.config.GoogleProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Map;
import java.util.Objects;

@Service
@Qualifier(value = "userService")
public class GoogleServiceImpl implements GoogleService {

    private final GoogleProperties googleProperties;

    @Autowired
    public GoogleServiceImpl(GoogleProperties googleProperties) {
        this.googleProperties = googleProperties;
    }

    @Override
    public String getOauthUrl(){
        return googleProperties.getFullOauthUrl();

    }

    @Override
    public String getUserName(String accessToken) {
        String nameRequestResource = UriComponentsBuilder.newInstance()
                .scheme("https")
                .host(googleProperties.getGoogleApiEndpointUrl())
                .queryParam("access_token", accessToken)
                .build().toString();
        RestTemplate getUserNameRequest = new RestTemplate();
        var nameResponse= getUserNameRequest.getForEntity(nameRequestResource, Map.class);
        if(nameResponse.getStatusCode()== HttpStatus.OK){
            return (String) Objects.requireNonNull(nameResponse.getBody()).get("emailAddress");
        }
        return "";
    }

    @Override
    public Map getTokenResponse(String authCode) {
        Map<Object, Object> values = googleProperties.getTokenRequestBody(authCode);
        RestTemplate tokenRequest = new RestTemplate();
        var requestBody = new HttpEntity<Map>(values);
        return tokenRequest.postForObject(URI.create("https://".concat(googleProperties.getTokenUrl())), requestBody, Map.class);
    }
}
