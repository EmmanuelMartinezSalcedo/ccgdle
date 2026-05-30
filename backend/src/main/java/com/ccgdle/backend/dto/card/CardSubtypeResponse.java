package com.ccgdle.backend.dto.card;

import java.util.List;

public record CardSubtypeResponse(int id, String name, String slug, List<Integer> gameModes) {

}
