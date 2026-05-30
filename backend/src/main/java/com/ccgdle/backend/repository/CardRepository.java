package com.ccgdle.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.ccgdle.backend.model.Card;

public interface CardRepository extends JpaRepository<Card, Integer> {
  List<Card> findByCardSetIdIn(List<Integer> setIds);

  List<Card> findByCardSetIdInAndCardRarityId(List<Integer> setIds, int rarityId);

  List<Card> findByCardRarityId(int rarityId);

  @Query(value = """
      SELECT c.*,
          CASE
              WHEN unaccent(LOWER(c.name)) = unaccent(LOWER(:name))
                  THEN 0
              WHEN unaccent(LOWER(c.name)) LIKE unaccent(LOWER(CONCAT(:name, '%')))
                  THEN 1
              WHEN unaccent(LOWER(c.name)) LIKE unaccent(LOWER(CONCAT('%', :name, '%')))
                  THEN 2
              ELSE 3
          END AS relevancia
      FROM cards c
      LEFT JOIN LATERAL (
          SELECT MIN(levenshtein(word, unaccent(LOWER(:name)))) AS distancia
          FROM unnest(string_to_array(unaccent(LOWER(c.name)), ' ')) AS word
      ) lev ON true
      WHERE
          unaccent(LOWER(c.name)) LIKE unaccent(LOWER(CONCAT('%', :name, '%')))
          OR lev.distancia <= 3
      ORDER BY relevancia ASC, lev.distancia ASC
      LIMIT 10
      """, nativeQuery = true)
  List<Card> search(@Param("name") String name);
}
