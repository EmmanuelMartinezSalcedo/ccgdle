package com.ccgdle.backend.dto.card;

public record Attempt(CardResponse card, boolean result, int attemptNumber) {
}
