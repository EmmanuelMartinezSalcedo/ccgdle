package com.ccgdle.backend.constants;

public enum GameModesConstants {
  STANDARD("Standard"), LEGENDARIES_STANDARD("Legendaries Standard"), WILD(
      "Wild"), LEGENDARIES_WILD("Legendaries Wild");

  private final String name;

  GameModesConstants(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }
}