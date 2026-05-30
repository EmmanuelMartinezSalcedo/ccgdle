package com.ccgdle.backend.dto.card;

import java.time.LocalDate;

public record GameCardRequest(LocalDate date, String slug) {
}
