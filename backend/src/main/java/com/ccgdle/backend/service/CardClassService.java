package com.ccgdle.backend.service;

import java.util.List;

import org.springframework.stereotype.Service;
import com.ccgdle.backend.constants.MetadataEndpoint;
import com.ccgdle.backend.dto.card.CardClassResponse;
import com.ccgdle.backend.model.CardClass;
import com.ccgdle.backend.repository.CardClassRepository;

import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CardClassService {

  private final HearthstoneApiService hearthstoneApiService;
  private final CardClassRepository cardClassRepository;

  @Transactional
  public void syncClasses() {

    List<CardClassResponse> classes =
        hearthstoneApiService.getMetadata(MetadataEndpoint.CLASSES, CardClassResponse.class);

    List<CardClass> entities = classes.stream()
        .map(r -> CardClass.builder().id(r.id()).name(r.name()).slug(r.slug()).build()).toList();

    cardClassRepository.saveAll(entities);
  }
}
