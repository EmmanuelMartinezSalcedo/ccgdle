package com.ccgdle.backend.service;

import java.util.List;

import org.springframework.stereotype.Service;
import com.ccgdle.backend.constants.MetadataEndpoint;
import com.ccgdle.backend.dto.card.CardTypeResponse;
import com.ccgdle.backend.model.CardType;
import com.ccgdle.backend.repository.CardTypeRepository;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CardTypeService {
  private final HearthstoneApiService hearthstoneApiService;
  private final CardTypeRepository cardTypeRepository;

  @Transactional
  public void syncTypes() {

    List<CardTypeResponse> types =
        hearthstoneApiService.getMetadata(MetadataEndpoint.TYPES, CardTypeResponse.class);

    List<CardType> entities =
        types.stream().filter(r -> r.gameModes() != null && r.gameModes().contains(1))
            .map(r -> CardType.builder().id(r.id()).name(r.name()).slug(r.slug()).build()).toList();

    cardTypeRepository.saveAll(entities);
  }
}
