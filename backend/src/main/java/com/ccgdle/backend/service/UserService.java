package com.ccgdle.backend.service;

import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ccgdle.backend.constants.AttemptConstants;
import com.ccgdle.backend.dto.card.CardResponse;
import com.ccgdle.backend.dto.user.AttemptRequest;
import com.ccgdle.backend.dto.user.AttemptResponse;
import com.ccgdle.backend.exception.attempt.MaximumAttemptsExceededException;
import com.ccgdle.backend.model.Attempt;
import com.ccgdle.backend.model.Card;
import com.ccgdle.backend.model.User;
import com.ccgdle.backend.model.UserGame;
import com.ccgdle.backend.repository.AttemptRepository;
import com.ccgdle.backend.repository.CardClassRepository;
import com.ccgdle.backend.repository.CardKeywordRepository;
import com.ccgdle.backend.repository.CardRepository;
import com.ccgdle.backend.repository.CardRarityRepository;
import com.ccgdle.backend.repository.CardSetRepository;
import com.ccgdle.backend.repository.CardSubtypeRepository;
import com.ccgdle.backend.repository.CardTypeRepository;
import com.ccgdle.backend.repository.UserRepository;
import com.ccgdle.backend.repository.UserGameRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
  private final UserRepository userRepository;
  private final UserGameRepository userGameRepository;
  private final AttemptRepository attemptRepository;
  private final CardRepository cardRepository;
  private final CardRarityRepository cardRarityRepository;
  private final CardSetRepository cardSetRepository;
  private final CardTypeRepository cardTypeRepository;
  private final CardClassRepository cardClassRepository;
  private final CardKeywordRepository cardKeywordRepository;
  private final CardSubtypeRepository cardSubtypeRepository;

  @Transactional
  public User createUser() {
    return userRepository.save(User.builder().build());
  }

  @Transactional
  public AttemptResponse attempt(AttemptRequest request) {
    UserGame userGame = userGameRepository.findById(request.userGameId()).orElseThrow();
    int attemptCount = attemptRepository.countByUserGame(userGame);

    if (attemptCount == AttemptConstants.MAX_ATTEMPTS) {
      throw new MaximumAttemptsExceededException();
    }

    Card targetCard = userGame.getGame().getCard();
    Card guessedCard = cardRepository.findById(request.cardId()).orElseThrow();

    boolean correct = targetCard.getId() == guessedCard.getId();
    int attemptNumber = attemptCount + 1;

    Attempt attempt = attemptRepository.save(Attempt.builder().userGame(userGame).card(guessedCard)
        .result(correct).attemptNumber(attemptNumber).build());

    CardResponse cardResponse = buildCardResponse(guessedCard);

    return new AttemptResponse(attempt.getId(), cardResponse, correct, attemptNumber);
  }

  private CardResponse buildCardResponse(Card card) {
    CardResponse.CardRarity rarity = cardRarityRepository.findById(card.getCardRarity().getId())
        .map(
            r -> new CardResponse.CardRarity(r.getId(), r.getName(), r.getSlug(), r.getDustValue()))
        .orElse(null);

    CardResponse.CardSet set = cardSetRepository.findById(card.getCardSet().getId())
        .map(s -> new CardResponse.CardSet(s.getId(), s.getName(), s.getSlug())).orElse(null);

    CardResponse.CardType type = cardTypeRepository.findById(card.getCardType().getId())
        .map(t -> new CardResponse.CardType(t.getId(), t.getName(), t.getSlug())).orElse(null);

    var classes = card.getCardClasses().stream()
        .map(c -> cardClassRepository.findById(c.getId())
            .map(cc -> new CardResponse.CardClass(cc.getId(), cc.getName(), cc.getSlug())))
        .filter(Optional::isPresent).map(Optional::get).toList();

    var keywords = card.getCardKeywords().stream()
        .map(k -> cardKeywordRepository.findById(k.getId())
            .map(kk -> new CardResponse.CardKeyword(kk.getId(), kk.getName(), kk.getSlug())))
        .filter(Optional::isPresent).map(Optional::get).toList();

    var subtypes = card.getCardSubtypes().stream()
        .map(s -> cardSubtypeRepository.findById(s.getId())
            .map(ss -> new CardResponse.CardSubtype(ss.getId(), ss.getName(), ss.getSlug())))
        .filter(Optional::isPresent).map(Optional::get).toList();

    return new CardResponse(card.getId(), card.getName(), card.getSlug(), card.getImage(),
        card.getManaCost(), rarity, set, type, classes, keywords, subtypes);
  }
}
