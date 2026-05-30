package com.ccgdle.backend.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import com.ccgdle.backend.model.GameMode;

public interface GameModeRepository extends JpaRepository<GameMode, UUID> {
  Optional<GameMode> findByName(String name);

  Optional<GameMode> findBySlug(String slug);
}
