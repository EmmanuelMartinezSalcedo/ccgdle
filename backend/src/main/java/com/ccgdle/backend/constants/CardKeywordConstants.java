package com.ccgdle.backend.constants;

import java.util.List;

public class CardKeywordConstants {
  public static final List<Integer> EXCLUDED_IDS = List.of(357);
  // [Fabled+]

  public static final List<String> MISSING_KEYWORDS = List.of("371:Shatter:shatter",
      "72:Cast When Drawn:cast-when-drawn", "301:Gigantify:gigantify", "26:Jade Golem:jade-golem");
  // id:name:slug

  private CardKeywordConstants() {
    throw new UnsupportedOperationException("Utility class");
  }
}
