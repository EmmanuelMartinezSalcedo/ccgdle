package com.ccgdle.backend.dto.game;

import java.util.UUID;

public record UserGameResponse(UUID id, UUID userId, UUID gameId) {
}

