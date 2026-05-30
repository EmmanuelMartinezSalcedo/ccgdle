package com.ccgdle.backend.service;

import java.util.List;
import java.util.Objects;
import org.springframework.stereotype.Service;
import com.ccgdle.backend.constants.MetadataEndpoint;
import com.ccgdle.backend.dto.card.CardRarityResponse;
import com.ccgdle.backend.model.CardRarity;
import com.ccgdle.backend.repository.CardRarityRepository;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CardRarityService {
  private final HearthstoneApiService hearthstoneApiService;
  private final CardRarityRepository cardRarityRepository;

  @Transactional
  public void syncRarities() {

    List<CardRarityResponse> rarities =
        hearthstoneApiService.getMetadata(MetadataEndpoint.RARITIES, CardRarityResponse.class);

    List<CardRarity> entities = rarities.stream().map(r -> CardRarity.builder().id(r.id())
        .name(r.name()).slug(r.slug()).dustValue(extractDust(r)).build()).toList();

    cardRarityRepository.saveAll(entities);
  }

  private Integer extractDust(CardRarityResponse r) {

    if (r.dustValue() == null) {
      return 0;
    }

    return r.dustValue().stream().filter(Objects::nonNull).findFirst().orElse(0);
  }
}
