package com.ccgdle.backend.service;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.ccgdle.backend.dto.card.SyncResponse;
import com.ccgdle.backend.model.Card;
import com.ccgdle.backend.repository.CardRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CardPersistenceService {

  private final CardRepository cardRepository;

  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public SyncResponse persistCards(List<Card> cardsToSave) {
    long cardsBefore = cardRepository.count();
    cardRepository.saveAll(cardsToSave);
    long cardsAfter = cardRepository.count();
    return new SyncResponse((int) cardsBefore, (int) cardsAfter);
  }
}