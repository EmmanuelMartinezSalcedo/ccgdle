package com.ccgdle.backend.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import com.ccgdle.backend.constants.CardSubtypeConstants.CardSubtypeTypes;
import com.ccgdle.backend.model.CardSubtype;

public interface CardSubtypeRepository extends JpaRepository<CardSubtype, UUID> {
  Optional<CardSubtype> findByApiId(int apiId);
  List<CardSubtype> findByApiIdAndSubtypeType(int apiId, CardSubtypeTypes subtypeType);
}
