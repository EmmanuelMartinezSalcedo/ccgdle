package com.ccgdle.backend.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Random;
import org.springframework.stereotype.Service;
import com.ccgdle.backend.constants.CardRarityConstants;
import com.ccgdle.backend.constants.CardSetConstants;
import com.ccgdle.backend.constants.GameModesConstants;
import com.ccgdle.backend.model.Card;
import com.ccgdle.backend.model.Game;
import com.ccgdle.backend.model.GameMode;
import com.ccgdle.backend.repository.CardRepository;
import com.ccgdle.backend.repository.GameModeRepository;
import com.ccgdle.backend.repository.GameRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class GameService {
  private final GameRepository gameRepository;
  private final GameModeRepository gameModeRepository;
  private final CardRepository cardRepository;
  private final Random random = new Random();

  public void generateTodayGames() {
    log.info("Generating games for today");
    LocalDate tomorrow = LocalDate.now().plusDays(1);

    for (GameModesConstants mode : GameModesConstants.values()) {
      GameMode gameMode = gameModeRepository.findByName(mode.getName())
          .orElseThrow(() -> new RuntimeException("Game mode not found: " + mode.getName()));

      List<Card> cards = getCardsForMode(mode, gameMode);

      boolean gameExists = gameRepository.findByDateAndGameMode(tomorrow, gameMode).isPresent();

      if (!gameExists && !cards.isEmpty()) {
        Card selectedCard = cards.get(random.nextInt(cards.size()));

        Game game = Game.builder().date(tomorrow).card(selectedCard).gameMode(gameMode).build();

        gameRepository.save(game);
      }
    }
  }

  private List<Card> getCardsForMode(GameModesConstants mode, GameMode gameMode) {
    List<Integer> standardSets = CardSetConstants.STANDARD_SETS;
    int legendaryId = CardRarityConstants.LEGENDARY_ID.get(0);

    List<Integer> usedCardIds = gameRepository.findCardIdsByGameMode(gameMode);
    List<Card> availableCards;

    availableCards = switch (mode) {
      case STANDARD -> cardRepository.findByCardSetIdIn(standardSets);
      case LEGENDARIES_STANDARD -> cardRepository.findByCardSetIdInAndCardRarityId(standardSets,
          legendaryId);
      case WILD -> cardRepository.findAll();
      case LEGENDARIES_WILD -> cardRepository.findByCardRarityId(legendaryId);
    };

    return availableCards.stream().filter(card -> !usedCardIds.contains(card.getId())).toList();
  }
}
