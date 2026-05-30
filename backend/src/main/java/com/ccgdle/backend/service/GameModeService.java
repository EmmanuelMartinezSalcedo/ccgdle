package com.ccgdle.backend.service;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import com.ccgdle.backend.dto.game.CreateUserGameRequest;
import com.ccgdle.backend.dto.game.GameModeResponse;
import com.ccgdle.backend.dto.game.UserGameResponse;
import com.ccgdle.backend.exception.game.GameModeNotFoundException;
import com.ccgdle.backend.exception.game.GameNotFoundException;
import com.ccgdle.backend.exception.game.UserGameAlreadyExistsException;
import com.ccgdle.backend.exception.game.UserGameNotFoundException;
import com.ccgdle.backend.model.Game;
import com.ccgdle.backend.model.GameMode;
import com.ccgdle.backend.model.User;
import com.ccgdle.backend.model.UserGame;
import com.ccgdle.backend.repository.GameModeRepository;
import com.ccgdle.backend.repository.GameRepository;
import com.ccgdle.backend.repository.UserGameRepository;
import com.ccgdle.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class GameModeService {

  private final GameModeRepository gameModeRepository;
  private final UserGameRepository userGameRepository;
  private final UserRepository userRepository;
  private final GameRepository gameRepository;

  public List<GameModeResponse> findAll() {
    return gameModeRepository.findAll().stream()
        .map(gm -> new GameModeResponse(gm.getId(), gm.getName(), gm.getSlug())).toList();
  }

  @Transactional
  public UserGameResponse createUserGame(@RequestBody CreateUserGameRequest request) {
    log.info("Create user game request: {}", request);
    GameMode gameMode =
        gameModeRepository.findBySlug(request.slug()).orElseThrow(GameModeNotFoundException::new);

    Game game = gameRepository.findByDateAndGameMode(request.date(), gameMode)
        .orElseThrow(GameNotFoundException::new);

    User user = userRepository.getReferenceById(request.userId());

    if (userGameRepository.findByUserAndGame(user, game).isPresent()) {
      throw new UserGameAlreadyExistsException();
    }

    UserGame userGame = userGameRepository.save(UserGame.builder().user(user).game(game).build());

    return new UserGameResponse(userGame.getId(), user.getId(), game.getId());
  }

  @Transactional(readOnly = true)
  public UserGameResponse getUserGame(String slug, java.time.LocalDate date,
      java.util.UUID userId) {
    GameMode gameMode =
        gameModeRepository.findBySlug(slug).orElseThrow(GameModeNotFoundException::new);

    Game game = gameRepository.findByDateAndGameMode(date, gameMode)
        .orElseThrow(GameNotFoundException::new);

    User user = userRepository.getReferenceById(userId);

    UserGame userGame = userGameRepository.findByUserAndGame(user, game)
        .orElseThrow(UserGameNotFoundException::new);

    return new UserGameResponse(userGame.getId(), user.getId(), game.getId());
  }
}
