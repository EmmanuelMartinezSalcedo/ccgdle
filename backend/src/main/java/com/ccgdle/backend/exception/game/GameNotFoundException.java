package com.ccgdle.backend.exception.game;

public class GameNotFoundException extends RuntimeException {
  public GameNotFoundException() {
    super("GAME_NOT_FOUND_FOR_DATE");
  }

  public GameNotFoundException(String msg) {
    super(msg);
  }
}
