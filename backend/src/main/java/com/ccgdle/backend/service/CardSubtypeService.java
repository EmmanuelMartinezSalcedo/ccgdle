package com.ccgdle.backend.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import com.ccgdle.backend.constants.MetadataEndpoint;
import com.ccgdle.backend.dto.card.CardSubtypeResponse;
import com.ccgdle.backend.model.CardSubtype;
import com.ccgdle.backend.repository.CardSubtypeRepository;
import com.ccgdle.backend.constants.CardSubtypeConstants;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CardSubtypeService {
  private final HearthstoneApiService hearthstoneApiService;
  private final CardSubtypeRepository cardSubtypeRepository;

  @Transactional
  public void syncSubtypes() {

    List<CardSubtypeResponse> minions =
        hearthstoneApiService.getMetadata(MetadataEndpoint.MINIONTYPES, CardSubtypeResponse.class);

    List<CardSubtype> minionEntities =
        minions.stream().filter(r -> r.gameModes() != null && r.gameModes().contains(1))
            .filter(r -> cardSubtypeRepository
                .findByApiIdAndSubtypeType(r.id(), CardSubtypeConstants.CardSubtypeTypes.MINION)
                .isEmpty())
            .map(r -> CardSubtype.builder().apiId(r.id()).name(r.name()).slug(r.slug())
                .subtypeType(CardSubtypeConstants.CardSubtypeTypes.MINION).build())
            .toList();

    List<CardSubtypeResponse> spells =
        hearthstoneApiService.getMetadata(MetadataEndpoint.SPELLSCHOOLS, CardSubtypeResponse.class);

    List<CardSubtype> spellEntities =
        spells.stream().filter(r -> r.gameModes() != null && r.gameModes().contains(1))
            .filter(r -> cardSubtypeRepository
                .findByApiIdAndSubtypeType(r.id(), CardSubtypeConstants.CardSubtypeTypes.SPELL)
                .isEmpty())
            .map(r -> CardSubtype.builder().apiId(r.id()).name(r.name()).slug(r.slug())
                .subtypeType(CardSubtypeConstants.CardSubtypeTypes.SPELL).build())
            .toList();

    List<CardSubtype> entities = new ArrayList<>(minionEntities);
    entities.addAll(spellEntities);

    if (!entities.isEmpty()) {
      cardSubtypeRepository.saveAll(entities);
    }
  }
}
