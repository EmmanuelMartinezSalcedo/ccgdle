package com.ccgdle.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.ccgdle.backend.model.CardRarity;

public interface CardRarityRepository extends JpaRepository<CardRarity, Integer> {

}
