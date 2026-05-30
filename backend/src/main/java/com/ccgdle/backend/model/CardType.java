package com.ccgdle.backend.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "card_types")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CardType {
  @Id
  private int id;

  @Column(nullable = false)
  private String name;

  @Column(nullable = false)
  private String slug;

  @OneToMany(mappedBy = "cardType", cascade = CascadeType.ALL, orphanRemoval = true)
  @Builder.Default
  private List<Card> cards = new ArrayList<>();
}
