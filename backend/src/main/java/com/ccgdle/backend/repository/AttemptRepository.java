package com.ccgdle.backend.repository;

import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import com.ccgdle.backend.model.Attempt;
import com.ccgdle.backend.model.UserGame;

public interface AttemptRepository extends JpaRepository<Attempt, UUID> {
  List<Attempt> findByUserGameOrderByAttemptNumberDesc(UserGame userGame);
  int countByUserGame(UserGame userGame);
}