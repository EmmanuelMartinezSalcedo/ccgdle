package com.ccgdle.backend.dto.card;

import java.util.List;

public record CardRarityResponse(int id, String name, String slug, List<Integer> craftingCost,
    List<Integer> dustValue) {
}
