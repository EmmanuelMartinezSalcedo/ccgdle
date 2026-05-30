package com.ccgdle.backend.exception.attempt;

public class MaximumAttemptsExceededException extends RuntimeException {
  public MaximumAttemptsExceededException() {
    super("MAXIMUM_ATTEMPTS_EXCEEDED");
  }

  public MaximumAttemptsExceededException(String msg) {
    super(msg);
  }
}
