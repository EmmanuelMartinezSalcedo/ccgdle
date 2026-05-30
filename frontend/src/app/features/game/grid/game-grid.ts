import { Component, input, signal } from '@angular/core';
import { GAME_CONFIG } from '../../../shared/constants/game-config';
import { Card } from '../../../shared/models/card';
import { getRarityArrow } from '../../../shared/models/card-rarity';
import { getSetArrow } from '../../../shared/models/card-set';
import { CardClass, getClassMatchState } from '../../../shared/models/card-class';
import { CardSubtype, getSubtypeMatchState } from '../../../shared/models/card-subtype';
import { CardKeyword, getKeywordMatchState } from '../../../shared/models/card-keyword';
import { Attempt } from '../../../shared/models/attempt';

@Component({
  selector: 'ccgdle-game-grid',
  imports: [],
  templateUrl: './game-grid.html',
  styleUrl: './game-grid.css',
})
export class GameGrid {
  maxAttempts: number = GAME_CONFIG.maxAttempts;
  gameCard = input.required<Card>();
  attempts = input.required<Attempt[]>();

  getRarityArrow = getRarityArrow;
  getSetArrow = getSetArrow;

  modalImageUrl = signal('');

  getClassState(attempt: CardClass[], target: CardClass[]) {
    return getClassMatchState(attempt, target);
  }
  getSubtypeState(attempt: CardSubtype[], target: CardSubtype[]) {
    return getSubtypeMatchState(attempt, target);
  }

  getKeywordState(attempt: CardKeyword[], target: CardKeyword[]) {
    return getKeywordMatchState(attempt, target);
  }

  openModal(imageUrl: string) {
    this.modalImageUrl.set(imageUrl);
    const modal = document.getElementById('imageModal') as HTMLDialogElement;
    if (modal) {
      modal.showModal();
    }
  }
}
