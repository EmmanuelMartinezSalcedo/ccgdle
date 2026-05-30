package com.ccgdle.backend.controller;

import java.time.LocalDate;
import java.util.List;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ccgdle.backend.dto.game.CreateUserGameRequest;
import com.ccgdle.backend.dto.game.GameModeResponse;
import com.ccgdle.backend.dto.game.UserGameResponse;
import com.ccgdle.backend.service.GameModeService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/game-modes")
@RequiredArgsConstructor
public class GameModeController {

  private final GameModeService gameModeService;

  @GetMapping
  public ResponseEntity<List<GameModeResponse>> getAll() {
    return ResponseEntity.ok(gameModeService.findAll());
  }

  @PostMapping("/user-game")
  public ResponseEntity<UserGameResponse> createUserGame(
      @RequestBody CreateUserGameRequest request) {
    return ResponseEntity.ok(gameModeService.createUserGame(request));
  }

  @GetMapping("/user-game/{slug}/{date}/{userId}")
  public ResponseEntity<UserGameResponse> getUserGame(@PathVariable String slug,
      @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
      @PathVariable java.util.UUID userId) {
    return ResponseEntity.ok(gameModeService.getUserGame(slug, date, userId));
  }

}
