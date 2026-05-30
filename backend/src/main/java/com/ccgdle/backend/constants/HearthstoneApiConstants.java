package com.ccgdle.backend.constants;

public final class HearthstoneApiConstants {

  public static final String BASE_URL = "https://us.api.blizzard.com";
  public static final String CARDS_PATH = "/hearthstone/cards";
  public static final String METADATA_PATH = "/hearthstone/metadata";
  public static final String LOCALE = "en_US";

  public static final int PAGE_SIZE = 100;
  public static final int COLLECTIBLE = 1;
  public static final String REGION = "us";

  private HearthstoneApiConstants() {
    throw new UnsupportedOperationException("Utility class");
  }
}
