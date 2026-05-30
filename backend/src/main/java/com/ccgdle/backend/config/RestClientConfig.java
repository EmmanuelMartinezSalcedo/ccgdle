package com.ccgdle.backend.config;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.Instant;
import java.util.Base64;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestClientConfig {

  private static final Logger logger = LoggerFactory.getLogger(RestClientConfig.class);

  @Value("${spring.security.oauth2.client.registration.my-client.client-id}")
  private String clientId;

  @Value("${spring.security.oauth2.client.registration.my-client.client-secret}")
  private String clientSecret;

  @Value("${spring.security.oauth2.client.provider.my-provider.token-uri}")
  private String tokenUri;

  @Bean
  public RestTemplate restTemplate() {
    RestTemplate restTemplate =
        new RestTemplate(new HttpComponentsClientHttpRequestFactory());

    ClientHttpRequestInterceptor interceptor =
        new OAuth2Interceptor(clientId, clientSecret, tokenUri);

    restTemplate.setInterceptors(List.of(interceptor));

    return restTemplate;
  }

  private static class OAuth2Interceptor implements ClientHttpRequestInterceptor {
    private static final Logger logger = LoggerFactory.getLogger(OAuth2Interceptor.class);

    private final String clientId;
    private final String clientSecret;
    private final String tokenUri;
    private volatile Instant tokenExpiry;
    private volatile String cachedAccessToken;

    OAuth2Interceptor(String clientId, String clientSecret, String tokenUri) {
      this.clientId = clientId;
      this.clientSecret = clientSecret;
      this.tokenUri = tokenUri;
    }

    @Override
    public org.springframework.http.client.ClientHttpResponse intercept(HttpRequest request, byte[] body,
        ClientHttpRequestExecution execution) throws java.io.IOException {

      if (needsNewToken()) {
        fetchNewToken();
      }

      if (cachedAccessToken != null) {
        request.getHeaders().set(HttpHeaders.AUTHORIZATION, "Bearer " + cachedAccessToken);
      }

      return execution.execute(request, body);
    }

    private synchronized boolean needsNewToken() {
      return cachedAccessToken == null || tokenExpiry == null
          || Instant.now().plusSeconds(60).isAfter(tokenExpiry);
    }

    private void fetchNewToken() {
      try {
        String auth = clientId + ":" + clientSecret;
        String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes());

        RestTemplate rt = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.set("Authorization", "Basic " + encodedAuth);

        String body = "grant_type=client_credentials";

        var response = rt.postForEntity(tokenUri,
            new org.springframework.http.HttpEntity<>(body, headers),
            TokenResponse.class);

        if (response.getBody() != null) {
          TokenResponse tokenResponse = response.getBody();
          
          if (tokenResponse.accessTokenValue() != null && !tokenResponse.accessTokenValue().isEmpty()) {
            cachedAccessToken = tokenResponse.accessTokenValue();
            int expiresIn = tokenResponse.expiresInValue() != null ? tokenResponse.expiresInValue() : 3600;
            tokenExpiry = Instant.now().plusSeconds(expiresIn - 60);
          }
        }
      } catch (Exception e) {
        logger.error("Failed to fetch OAuth2 token", e);
      }
    }
  }

  private record TokenResponse(
      @JsonProperty("access_token") String accessToken,
      @JsonProperty("expires_in") Integer expiresIn,
      @JsonProperty("token_type") String tokenType) {
    String accessTokenValue() { return accessToken; }
    Integer expiresInValue() { return expiresIn; }
  }
}