package com.ccgdle.backend.exception.game;

public class UserGameAlreadyExistsException extends RuntimeException {
  public UserGameAlreadyExistsException() {
    super("USER_GAME_ALREADY_EXISTS");
  }

  public UserGameAlreadyExistsException(String msg) {
    super(msg);
  }
}
