package com.ccgdle.backend.dto.game;

import java.util.UUID;

public record GameModeResponse(UUID id, String name, String slug) {
}
