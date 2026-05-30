package com.ccgdle.backend.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import com.ccgdle.backend.constants.CardSetConstants;
import com.ccgdle.backend.constants.CardSubtypeConstants.CardSubtypeTypes;
import com.ccgdle.backend.dto.card.CardResponse;
import com.ccgdle.backend.dto.card.HearthstoneApiCardResponse;
import com.ccgdle.backend.dto.card.PagintedCardResponse;
import com.ccgdle.backend.dto.card.SyncResponse;
import com.ccgdle.backend.exception.game.GameNotFoundException;
import com.ccgdle.backend.model.Card;
import com.ccgdle.backend.model.CardClass;
import com.ccgdle.backend.model.CardKeyword;
import com.ccgdle.backend.model.CardRarity;
import com.ccgdle.backend.model.CardSet;
import com.ccgdle.backend.model.CardSubtype;
import com.ccgdle.backend.model.CardType;
import com.ccgdle.backend.model.Game;
import com.ccgdle.backend.model.GameMode;
import com.ccgdle.backend.model.User;
import com.ccgdle.backend.model.UserGame;
import com.ccgdle.backend.model.Attempt;
import com.ccgdle.backend.repository.CardClassRepository;
import com.ccgdle.backend.repository.CardKeywordRepository;
import com.ccgdle.backend.repository.CardRarityRepository;
import com.ccgdle.backend.repository.CardRepository;
import com.ccgdle.backend.repository.CardSetRepository;
import com.ccgdle.backend.repository.CardSubtypeRepository;
import com.ccgdle.backend.repository.CardTypeRepository;
import com.ccgdle.backend.repository.GameModeRepository;
import com.ccgdle.backend.repository.GameRepository;
import com.ccgdle.backend.repository.UserGameRepository;
import com.ccgdle.backend.repository.UserRepository;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CardService {
  private static final Logger logger = LoggerFactory.getLogger(CardService.class);

  private final CardClassService cardClassService;
  private final CardTypeService cardTypeService;
  private final CardSubtypeService cardSubtypeService;
  private final CardSetService cardSetService;
  private final CardRarityService cardRarityService;
  private final CardKeywordService cardKeywordService;

  private final HearthstoneApiService hearthstoneApiService;
  private final CardPersistenceService cardPersistenceService;
  private final CardRepository cardRepository;
  private final CardClassRepository cardClassRepository;
  private final CardKeywordRepository cardKeywordRepository;
  private final CardRarityRepository cardRarityRepository;
  private final CardSetRepository cardSetRepository;
  private final CardTypeRepository cardTypeRepository;
  private final CardSubtypeRepository cardSubtypeRepository;
  private final GameRepository gameRepository;
  private final GameModeRepository gameModeRepository;
  private final UserGameRepository userGameRepository;
  private final UserRepository userRepository;

  private record CardRelations(CardRarity rarity, CardSet set, CardType type,
      List<String> missing) {
  }

  @Transactional
  public SyncResponse syncCards() {
    syncMetadata();
    List<Card> cardsToSave = fetchAndProcessAllPages();
    return cardPersistenceService.persistCards(cardsToSave);
  }

  public List<CardResponse> search(String name) {
    List<Card> cards = cardRepository.search(name);
    return cards.stream().map(this::mapToCardResponse).toList();
  }

  public CardResponse getGameCard(LocalDate date, String slug) {
    GameMode gameMode = gameModeRepository.findBySlug(slug).orElseThrow(GameNotFoundException::new);

    Game game = gameRepository.findByDateAndGameMode(date, gameMode)
        .orElseThrow(GameNotFoundException::new);

    return mapToCardResponse(game.getCard());
  }

  public List<com.ccgdle.backend.dto.card.Attempt> getGameAttempts(LocalDate date, String slug,
      UUID userId) {
    User user = userRepository.findById(userId).orElse(null);
    if (user == null) {
      return List.of();
    }

    GameMode gameMode = gameModeRepository.findBySlug(slug).orElse(null);
    if (gameMode == null) {
      return List.of();
    }

    Game game = gameRepository.findByDateAndGameMode(date, gameMode).orElse(null);
    if (game == null) {
      return List.of();
    }

    UserGame userGame = userGameRepository.findByUserAndGame(user, game).orElse(null);
    if (userGame == null) {
      return List.of();
    }

    List<Attempt> attempts = userGame.getAttempts();

    return attempts.stream()
        .map(a -> new com.ccgdle.backend.dto.card.Attempt(mapToCardResponse(a.getCard()),
            a.isResult(), a.getAttemptNumber()))
        .toList();
  }

  private CardResponse mapToCardResponse(Card card) {
    return new CardResponse(card.getId(), card.getName(), card.getSlug(), card.getImage(),
        card.getManaCost(),
        card.getCardRarity() != null
            ? new CardResponse.CardRarity(card.getCardRarity().getId(),
                card.getCardRarity().getName(), card.getCardRarity().getSlug(),
                card.getCardRarity().getDustValue())
            : null,
        card.getCardSet() != null
            ? new CardResponse.CardSet(card.getCardSet().getId(), card.getCardSet().getName(),
                card.getCardSet().getSlug())
            : null,
        card.getCardType() != null
            ? new CardResponse.CardType(card.getCardType().getId(), card.getCardType().getName(),
                card.getCardType().getSlug())
            : null,
        card.getCardClasses() != null
            ? card.getCardClasses().stream()
                .map(c -> new CardResponse.CardClass(c.getId(), c.getName(), c.getSlug())).toList()
            : List.of(),
        card.getCardKeywords() != null ? card.getCardKeywords().stream()
            .map(k -> new CardResponse.CardKeyword(k.getId(), k.getName(), k.getSlug())).toList()
            : List.of(),
        card.getCardSubtypes() != null ? card.getCardSubtypes().stream()
            .map(s -> new CardResponse.CardSubtype(s.getId(), s.getName(), s.getSlug())).toList()
            : List.of());
  }

  private void syncMetadata() {
    cardClassService.syncClasses();
    cardTypeService.syncTypes();
    cardSubtypeService.syncSubtypes();
    cardSetService.syncSets();
    cardRarityService.syncRarities();
    cardKeywordService.syncKeywords();
  }

  private List<Card> fetchAndProcessAllPages() {
    List<Card> cardsToSave = new ArrayList<>();
    Map<Integer, HearthstoneApiCardResponse> processedCards = new HashMap<>();

    PagintedCardResponse firstPage = hearthstoneApiService.getCards(1);
    int totalPages = firstPage.pageCount();

    for (int pageNum = 1; pageNum <= totalPages; pageNum++) {
      List<Card> pageCards = fetchAndProcessPage(pageNum, firstPage, processedCards);
      cardsToSave.addAll(pageCards);
    }

    return cardsToSave;
  }

  private List<Card> fetchAndProcessPage(int pageNum, PagintedCardResponse firstPage,
      Map<Integer, HearthstoneApiCardResponse> processedCards) {
    List<Card> pageCards = new ArrayList<>();
    List<HearthstoneApiCardResponse> filteredBatch = new ArrayList<>();

    PagintedCardResponse page =
        (pageNum == 1) ? firstPage : hearthstoneApiService.getCards(pageNum);
    if (page == null || page.cards() == null || page.cards().isEmpty()) {
      return pageCards;
    }

    for (HearthstoneApiCardResponse card : page.cards()) {
      if (card.parentId() == null) {
        filteredBatch.add(card);
      }
    }

    for (HearthstoneApiCardResponse CardResponse : filteredBatch) {
      checkForCopies(CardResponse, processedCards);
      Card card = mapToCard(CardResponse);
      if (card != null) {
        pageCards.add(card);
        processedCards.put(CardResponse.id(), CardResponse);
      }
    }

    return pageCards;
  }

  private void checkForCopies(HearthstoneApiCardResponse cardResponse,
      Map<Integer, HearthstoneApiCardResponse> processedCards) {
    if (!hasCopies(cardResponse))
      return;

    List<HearthstoneApiCardResponse> allCopies = collectCopies(cardResponse, processedCards);

    if (!hasMultipleCopies(allCopies))
      return;
    if (!allCopiesProcessed(allCopies, processedCards))
      return;

    logIfNotPriority(cardResponse, allCopies);
  }

  private boolean hasCopies(HearthstoneApiCardResponse cardResponse) {
    return cardResponse.copyOfCardId() != null && !cardResponse.copyOfCardId().isEmpty();
  }

  private List<HearthstoneApiCardResponse> collectCopies(HearthstoneApiCardResponse cardResponse,
      Map<Integer, HearthstoneApiCardResponse> processedCards) {
    List<HearthstoneApiCardResponse> allCopies = new ArrayList<>();
    allCopies.add(cardResponse);

    for (Integer copyId : cardResponse.copyOfCardId()) {
      HearthstoneApiCardResponse copyCard = processedCards.get(copyId);
      if (copyCard != null) {
        allCopies.add(copyCard);
      }
    }

    return allCopies;
  }

  private boolean hasMultipleCopies(List<HearthstoneApiCardResponse> allCopies) {
    return allCopies.size() > 1;
  }

  private boolean allCopiesProcessed(List<HearthstoneApiCardResponse> allCopies,
      Map<Integer, HearthstoneApiCardResponse> processedCards) {
    for (HearthstoneApiCardResponse copy : allCopies) {
      if (!processedCards.containsKey(copy.id())) {
        return false;
      }
    }
    return true;
  }

  private void logIfNotPriority(HearthstoneApiCardResponse cardResponse,
      List<HearthstoneApiCardResponse> allCopies) {
    Integer prioritySetId = resolveDuplicatePriority(allCopies);

    if (!cardResponse.cardSetId().equals(prioritySetId)) {
      logger.warn("Card skipped duplicate - id={}, name={}, set={}, prioritySet={}",
          cardResponse.id(), cardResponse.name(), cardResponse.cardSetId(), prioritySetId);
    }
  }

  private Integer resolveDuplicatePriority(List<HearthstoneApiCardResponse> copies) {
    int bestPriority = Integer.MAX_VALUE;
    Integer bestSetId = null;

    for (HearthstoneApiCardResponse copy : copies) {
      int setId = copy.cardSetId();
      if (CardSetConstants.LEGACY_SET_ALIASES_IDS.contains(setId)) {
        setId = CardSetConstants.LEGACY_SET_ID;
      }
      int priorityIndex = CardSetConstants.SET_PRIORITY.indexOf(setId);
      int priority;
      if (priorityIndex == -1) {
        priority = 0;
      } else {
        priority = priorityIndex + 1;
      }
      if (priority < bestPriority) {
        bestPriority = priority;
        bestSetId = copy.cardSetId();
      }
    }
    return bestSetId;
  }

  private Card mapToCard(HearthstoneApiCardResponse cardResponse) {
    List<CardClass> cardClasses = mapCardClasses(cardResponse);
    List<CardKeyword> cardKeywords = mapCardKeywords(cardResponse);
    CardRelations relations = mapCardRelations(cardResponse);

    if (!relations.missing().isEmpty()) {
      logger.warn("Card skipped - id={}, name={}, missing={}", cardResponse.id(),
          cardResponse.name(), relations.missing());
      return null;
    }

    List<CardSubtype> cardSubtypes = mapCardSubtypes(cardResponse);

    return Card.builder().id(cardResponse.id()).name(cardResponse.name()).slug(cardResponse.slug())
        .image(cardResponse.image()).manaCost(cardResponse.manaCost()).cardClasses(cardClasses)
        .cardKeywords(cardKeywords).cardRarity(relations.rarity()).cardSet(relations.set())
        .cardType(relations.type()).cardSubtypes(cardSubtypes).build();
  }

  private List<CardClass> mapCardClasses(HearthstoneApiCardResponse cardResponse) {
    Set<Integer> classIds = new HashSet<>();
    if (cardResponse.classId() != null) {
      classIds.add(cardResponse.classId());
    }
    if (cardResponse.multiClassIds() != null) {
      classIds.addAll(cardResponse.multiClassIds());
    }

    List<CardClass> cardClasses = new ArrayList<>();
    for (Integer classId : classIds) {
      cardClassRepository.findById(classId).ifPresent(cardClasses::add);
    }
    return cardClasses;
  }

  private List<CardKeyword> mapCardKeywords(HearthstoneApiCardResponse cardResponse) {
    List<CardKeyword> cardKeywords = new ArrayList<>();
    if (cardResponse.keywordIds() != null) {
      for (Integer keywordId : cardResponse.keywordIds()) {
        cardKeywordRepository.findById(keywordId).ifPresent(cardKeywords::add);
      }
    }
    return cardKeywords;
  }

  private CardRelations mapCardRelations(HearthstoneApiCardResponse cardResponse) {
    CardRarity cardRarity = cardRarityRepository.findById(cardResponse.rarityId()).orElse(null);

    Integer setId = cardResponse.cardSetId();
    if (CardSetConstants.LEGACY_SET_ALIASES_IDS.contains(setId)) {
      setId = CardSetConstants.LEGACY_SET_ID;
    }
    CardSet cardSet = cardSetRepository.findById(setId).orElse(null);
    CardType cardType = cardTypeRepository.findById(cardResponse.cardTypeId()).orElse(null);

    List<String> missingFields = new ArrayList<>();
    if (cardRarity == null)
      missingFields.add("rarity=" + cardResponse.rarityId());
    if (cardSet == null)
      missingFields.add("set=" + cardResponse.cardSetId());
    if (cardType == null)
      missingFields.add("type=" + cardResponse.cardTypeId());

    return new CardRelations(cardRarity, cardSet, cardType, missingFields);
  }

  private List<CardSubtype> mapCardSubtypes(HearthstoneApiCardResponse cardResponse) {
    List<CardSubtype> cardSubtypes = new ArrayList<>();
    if (cardResponse.spellSchoolId() != null) {
      cardSubtypes.addAll(cardSubtypeRepository
          .findByApiIdAndSubtypeType(cardResponse.spellSchoolId(), CardSubtypeTypes.SPELL));
    }
    if (cardResponse.minionTypeId() != null) {
      cardSubtypes.addAll(cardSubtypeRepository
          .findByApiIdAndSubtypeType(cardResponse.minionTypeId(), CardSubtypeTypes.MINION));
    }
    if (cardResponse.multiTypeIds() != null) {
      for (Integer typeId : cardResponse.multiTypeIds()) {
        cardSubtypes.addAll(
            cardSubtypeRepository.findByApiIdAndSubtypeType(typeId, CardSubtypeTypes.MINION));
      }
    }
    return cardSubtypes;
  }
}
