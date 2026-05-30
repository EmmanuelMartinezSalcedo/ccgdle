package com.ccgdle.backend.controller;

import java.time.LocalDate;
import java.util.List;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ccgdle.backend.dto.card.CardResponse;
import com.ccgdle.backend.dto.card.Attempt;
import com.ccgdle.backend.dto.card.SyncResponse;
import com.ccgdle.backend.service.CardService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/cards")
@RequiredArgsConstructor
public class CardController {

  private final CardService cardService;

  @PostMapping("/sync")
  public ResponseEntity<SyncResponse> sync() {
    return ResponseEntity.ok(cardService.syncCards());
  }

  @GetMapping("/search/{name}")
  public ResponseEntity<List<CardResponse>> search(@PathVariable String name) {
    return ResponseEntity.ok(cardService.search(name));
  }

  @GetMapping("/game-card/{slug}/{date}")
  public ResponseEntity<CardResponse> getGameCard(@PathVariable String slug,
      @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
    return ResponseEntity.ok(cardService.getGameCard(date, slug));
  }

  @GetMapping("/game-attempts/{slug}/{date}/{userId}")
  public ResponseEntity<List<Attempt>> getGameAttempts(@PathVariable String slug,
      @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
      @PathVariable java.util.UUID userId) {
    return ResponseEntity.ok(cardService.getGameAttempts(date, slug, userId));
  }
}
