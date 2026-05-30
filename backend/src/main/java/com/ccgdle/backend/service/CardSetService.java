package com.ccgdle.backend.service;

import java.util.List;
import org.springframework.stereotype.Service;
import com.ccgdle.backend.constants.MetadataEndpoint;
import com.ccgdle.backend.dto.card.CardSetResponse;
import com.ccgdle.backend.model.CardSet;
import com.ccgdle.backend.repository.CardSetRepository;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CardSetService {
  private final HearthstoneApiService hearthstoneApiService;
  private final CardSetRepository cardSetRepository;

  @Transactional
  public void syncSets() {

    List<CardSetResponse> sets =
        hearthstoneApiService.getMetadata(MetadataEndpoint.SETS, CardSetResponse.class);

    List<CardSet> entities = sets.stream()
        .map(r -> CardSet.builder().id(r.id()).name(r.name()).slug(r.slug()).build()).toList();

    cardSetRepository.saveAll(entities);
  }
}
