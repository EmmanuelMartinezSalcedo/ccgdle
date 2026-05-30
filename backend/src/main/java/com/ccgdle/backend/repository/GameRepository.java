package com.ccgdle.backend.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.ccgdle.backend.model.Game;
import com.ccgdle.backend.model.GameMode;

public interface GameRepository extends JpaRepository<Game, UUID> {
  Optional<Game> findByDateAndGameMode(LocalDate date, GameMode gameMode);

  @Query("SELECT c.card.id FROM Game c WHERE c.gameMode = :gameMode")
  List<Integer> findCardIdsByGameMode(@Param("gameMode") GameMode gameMode);
}
