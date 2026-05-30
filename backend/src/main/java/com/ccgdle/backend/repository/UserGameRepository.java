package com.ccgdle.backend.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import com.ccgdle.backend.model.Game;
import com.ccgdle.backend.model.User;
import com.ccgdle.backend.model.UserGame;

public interface UserGameRepository extends JpaRepository<UserGame, UUID> {
  Optional<UserGame> findByUserAndGame(User user, Game game);

  List<UserGame> findByUserAndGameIn(User user, List<Game> games);
}