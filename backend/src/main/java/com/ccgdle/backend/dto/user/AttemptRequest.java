package com.ccgdle.backend.dto.user;

import java.util.UUID;

public record AttemptRequest(UUID userId, UUID userGameId, int cardId) {
}
