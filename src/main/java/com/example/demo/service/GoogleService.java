package com.example.demo.service;

import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public interface GoogleService {

    String getOauthUrl();

    String getUserName(String accessToken);

    Map<Object, Object> getTokenResponse(String authCode);

}
