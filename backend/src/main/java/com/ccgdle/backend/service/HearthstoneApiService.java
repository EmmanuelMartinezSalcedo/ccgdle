package com.ccgdle.backend.service;

import java.util.List;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.ResolvableType;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.ccgdle.backend.constants.HearthstoneApiConstants;
import com.ccgdle.backend.constants.MetadataEndpoint;
import com.ccgdle.backend.dto.card.PagintedCardResponse;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class HearthstoneApiService {

  private final RestTemplate restTemplate;

  public <T> List<T> getMetadata(MetadataEndpoint endpoint, Class<T> responseType) {
    String url = UriComponentsBuilder
        .fromUriString(HearthstoneApiConstants.METADATA_PATH + "/" + endpoint.getValue())
        .queryParam("locale", HearthstoneApiConstants.LOCALE).build().toUriString();

    ResponseEntity<List<T>> response = restTemplate.exchange(HearthstoneApiConstants.BASE_URL + url,
        HttpMethod.GET, null, ParameterizedTypeReference
            .forType(ResolvableType.forClassWithGenerics(List.class, responseType).getType()));

    return response.getBody() != null ? response.getBody() : List.of();
  }

  public PagintedCardResponse getCards(int page) {
    String url = UriComponentsBuilder.fromUriString(HearthstoneApiConstants.CARDS_PATH)
        .queryParam("locale", HearthstoneApiConstants.LOCALE).queryParam("page", page)
        .queryParam("pageSize", HearthstoneApiConstants.PAGE_SIZE)
        .queryParam("collectible", HearthstoneApiConstants.COLLECTIBLE)
        .queryParam("region", HearthstoneApiConstants.REGION).build().toUriString();

    return restTemplate.getForObject(HearthstoneApiConstants.BASE_URL + url,
        PagintedCardResponse.class);
  }
}
