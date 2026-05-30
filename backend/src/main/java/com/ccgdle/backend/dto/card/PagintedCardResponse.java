package com.ccgdle.backend.dto.card;

import java.util.List;

public record PagintedCardResponse(List<HearthstoneApiCardResponse> cards, int cardCount,
    int pageCount, int page) {
}
