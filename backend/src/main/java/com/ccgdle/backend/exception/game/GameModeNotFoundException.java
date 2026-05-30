package com.ccgdle.backend.exception.game;

public class GameModeNotFoundException extends RuntimeException {
  public GameModeNotFoundException() {
    super("GAME_MODE_NOT_FOUND");
  }

  public GameModeNotFoundException(String msg) {
    super(msg);
  }
}
