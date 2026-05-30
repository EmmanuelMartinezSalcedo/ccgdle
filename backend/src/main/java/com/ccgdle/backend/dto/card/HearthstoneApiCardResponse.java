package com.ccgdle.backend.dto.card;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record HearthstoneApiCardResponse(int id, String name, String slug, Integer classId,
    List<Integer> multiClassIds, Integer spellSchoolId, Integer cardTypeId, Integer cardSetId,
    Integer rarityId, Integer manaCost, String image, List<Integer> keywordIds,
    List<Integer> copyOfCardId, Integer minionTypeId, List<Integer> multiTypeIds,
    Integer parentId) {
}
