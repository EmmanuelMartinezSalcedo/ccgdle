import { Component, input } from '@angular/core';
import { GAME_CONFIG } from '../../../shared/constants/game-config';

@Component({
  selector: 'ccgdle-game-statistics',
  imports: [],
  templateUrl: './game-statistics.html',
  styleUrl: './game-statistics.css',
})
export class GameStatistics {
  maxAttempts: number = GAME_CONFIG.maxAttempts;
  currentAttempts = input.required<number>();
}
