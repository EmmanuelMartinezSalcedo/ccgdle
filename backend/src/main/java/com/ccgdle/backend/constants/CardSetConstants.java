package com.ccgdle.backend.constants;

import java.util.List;

public class CardSetConstants {
  public static final List<Integer> IF_DUPLICATED_IGNORE_IDS = List.of(1898, 1637, 1635);
  // ["Caverns of Time", "Core", "Legacy"]

  public static final Integer LEGACY_SET_ID = 1635;
  public static final List<Integer> LEGACY_SET_ALIASES_IDS = List.of(3, 4);

  public static final Integer ANY_COPY_SET_ID = 1898;
  public static final Integer CORE_SET_ID = 1637;
  public static final Integer LEGACY_COPY_SET_ID = 1635;

  public static final List<Integer> SET_PRIORITY = List.of(9999, 1898, 1637, 1635);

  public static final List<Integer> STANDARD_SETS = List.of(1980, 1957, 1952, 1946, 1637);
  // ["CATACLYSM", "Across the Timeways", "The Lost City of Un'Goro", "Into the Emerald Dream",
  // "Core"]

  private CardSetConstants() {
    throw new UnsupportedOperationException("Utility class");
  }
}
