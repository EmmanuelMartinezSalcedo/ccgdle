package com.ccgdle.backend.model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.ccgdle.backend.constants.CardSubtypeConstants;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "card_subtypes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CardSubtype {
  @Id
  @GeneratedValue
  private UUID id;

  @Column(name = "api_id")
  private int apiId;

  @Column(nullable = false)
  private String name;

  @Column(nullable = false)
  private String slug;

  @Column(name = "subtype_type", nullable = false)
  @Enumerated(EnumType.STRING)
  private CardSubtypeConstants.CardSubtypeTypes subtypeType;

  @ManyToMany(mappedBy = "cardSubtypes")
  @Builder.Default
  private List<Card> cards = new ArrayList<>();
}
