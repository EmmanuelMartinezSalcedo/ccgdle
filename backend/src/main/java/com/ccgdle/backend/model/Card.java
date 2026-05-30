package com.ccgdle.backend.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "cards")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Card {
  @Id
  private int id;

  @Column(nullable = false)
  private String name;

  @Column(nullable = false)
  private String slug;

  @Column(nullable = false)
  private String image;

  @Column(name = "mana_cost", nullable = false)
  private int manaCost;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "rarity_id", nullable = false)
  private CardRarity cardRarity;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "set_id", nullable = false)
  private CardSet cardSet;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "type_id", nullable = false)
  private CardType cardType;

  @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
  @JoinTable(name = "class_junction", joinColumns = @JoinColumn(name = "card_id"),
      inverseJoinColumns = @JoinColumn(name = "card_class_id"))
  @Builder.Default
  private List<CardClass> cardClasses = new ArrayList<>();

  @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
  @JoinTable(name = "keyword_junction", joinColumns = @JoinColumn(name = "card_id"),
      inverseJoinColumns = @JoinColumn(name = "card_keyword_id"))
  @Builder.Default
  private List<CardKeyword> cardKeywords = new ArrayList<>();

  @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
  @JoinTable(name = "subtype_junction", joinColumns = @JoinColumn(name = "card_id"),
      inverseJoinColumns = @JoinColumn(name = "card_subtype_id", referencedColumnName = "id"))
  @Builder.Default
  private List<CardSubtype> cardSubtypes = new ArrayList<>();

  @OneToOne(mappedBy = "card")
  private Game game;

  @OneToMany(mappedBy = "card", cascade = CascadeType.ALL, orphanRemoval = true)
  @Builder.Default
  private List<Attempt> attempts = new ArrayList<>();
}
