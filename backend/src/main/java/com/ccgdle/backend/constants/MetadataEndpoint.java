package com.ccgdle.backend.constants;

public enum MetadataEndpoint {
  CLASSES("classes"), TYPES("types"), MINIONTYPES("minionTypes"), SPELLSCHOOLS(
      "spellSchools"), SETS("sets"), RARITIES("rarities"), KEYWORDS("keywords"), CARDS("cards");

  private final String value;

  MetadataEndpoint(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }
}
