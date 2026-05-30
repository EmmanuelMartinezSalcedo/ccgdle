import { Component, input } from '@angular/core';

@Component({
  selector: 'ccgdle-game-challenge-count-badge',
  imports: [],
  templateUrl: './game-challenge-count-badge.html',
  styleUrl: './game-challenge-count-badge.css',
})
export class GameChallengeCountBadge {
  count = input.required<number>();
}
