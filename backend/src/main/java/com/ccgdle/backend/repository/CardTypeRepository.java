package com.ccgdle.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.ccgdle.backend.model.CardType;

public interface CardTypeRepository extends JpaRepository<CardType, Integer> {

}
