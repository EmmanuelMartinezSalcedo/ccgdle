package com.ccgdle.backend.exception.game;

public class UserGameNotFoundException extends RuntimeException {
  public UserGameNotFoundException() {
    super("USER_GAME_NOT_FOUND");
  }

  public UserGameNotFoundException(String msg) {
    super(msg);
  }
}
