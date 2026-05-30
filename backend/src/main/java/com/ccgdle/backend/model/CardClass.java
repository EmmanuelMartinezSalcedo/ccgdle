package com.ccgdle.backend.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "card_classes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CardClass {
  @Id
  private int id;

  @Column(nullable = false)
  private String name;

  @Column(nullable = false)
  private String slug;

  @ManyToMany(mappedBy = "cardClasses")
  @Builder.Default
  private List<Card> cards = new ArrayList<>();
}
