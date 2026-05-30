package com.ccgdle.backend.dto.user;

import com.ccgdle.backend.dto.card.CardResponse;
import java.util.UUID;

public record AttemptResponse(UUID attemptId, CardResponse card, boolean correct, int attemptNumber) {}