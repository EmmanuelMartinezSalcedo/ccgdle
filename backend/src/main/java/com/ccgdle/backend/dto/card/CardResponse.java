package com.ccgdle.backend.dto.card;

import java.util.List;
import java.util.UUID;

public record CardResponse(int id, String name, String slug, String image, int manaCost,
    CardRarity rarity, CardSet set, CardType type, List<CardClass> classes,
    List<CardKeyword> keywords, List<CardSubtype> subtypes) {

  public record CardRarity(int id, String name, String slug, int dustValue) {
  }

  public record CardSet(int id, String name, String slug) {
  }

  public record CardType(int id, String name, String slug) {
  }

  public record CardClass(int id, String name, String slug) {
  }

  public record CardKeyword(int id, String name, String slug) {
  }

  public record CardSubtype(UUID id, String name, String slug) {
  }
}
