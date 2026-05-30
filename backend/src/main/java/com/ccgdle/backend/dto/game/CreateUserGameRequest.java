package com.ccgdle.backend.dto.game;

import java.time.LocalDate;
import java.util.UUID;
import com.fasterxml.jackson.annotation.JsonProperty;

public record CreateUserGameRequest(@JsonProperty("userId") UUID userId,
    @JsonProperty("date") LocalDate date, @JsonProperty("slug") String slug) {
}
