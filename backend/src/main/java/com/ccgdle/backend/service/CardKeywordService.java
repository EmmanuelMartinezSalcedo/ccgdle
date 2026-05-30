package com.ccgdle.backend.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.ccgdle.backend.constants.CardKeywordConstants;
import com.ccgdle.backend.constants.MetadataEndpoint;
import com.ccgdle.backend.dto.card.CardKeywordResponse;
import com.ccgdle.backend.model.CardKeyword;
import com.ccgdle.backend.repository.CardKeywordRepository;

import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CardKeywordService {

  private final HearthstoneApiService hearthstoneApiService;
  private final CardKeywordRepository cardKeywordRepository;

  @Transactional
  public void syncKeywords() {

    List<CardKeywordResponse> keywords =
        hearthstoneApiService.getMetadata(MetadataEndpoint.KEYWORDS, CardKeywordResponse.class);

    List<CardKeyword> entities = new ArrayList<>(keywords.stream()
        .filter(r -> r.gameModes() != null && r.gameModes().contains(1))
        .filter(r -> !CardKeywordConstants.EXCLUDED_IDS.contains(r.id()))
        .map(r -> CardKeyword.builder().id(r.id()).name(r.name()).slug(r.slug()).build()).toList());

    List<CardKeyword> missing = CardKeywordConstants.MISSING_KEYWORDS.stream().map(s -> {
      String[] parts = s.split(":");

      return CardKeyword.builder().id(Integer.parseInt(parts[0])).name(parts[1]).slug(parts[2])
          .build();
    }).toList();

    entities.addAll(missing);

    cardKeywordRepository.saveAll(entities);
  }
}
