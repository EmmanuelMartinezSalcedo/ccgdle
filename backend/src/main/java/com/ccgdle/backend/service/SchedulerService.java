package com.ccgdle.backend.service;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SchedulerService {
  private final GameService gameService;

  @Scheduled(cron = "0 35 20 * * *", zone = "America/New_York")
  public void generateTodayGames() {
    gameService.generateTodayGames();
  }
}
