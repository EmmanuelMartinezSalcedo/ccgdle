import { Component, DestroyRef, inject, OnInit, PLATFORM_ID, signal } from '@angular/core';
import { GameChallengeCountBadge } from '../challenge-count-badge/game-challenge-count-badge';
import { GameGrid } from '../grid/game-grid';
import { Card } from '../../../shared/models/card';
import { Game } from '../game';
import { Attempt } from '../../../shared/models/attempt';
import { UrlParams } from '../../../shared/models/url-params';
import { ActivatedRoute } from '@angular/router';
import { isPlatformBrowser } from '@angular/common';
import { UserGame } from '../../../shared/models/user-game';
import { form, FormField } from '@angular/forms/signals';
import { FormsModule } from '@angular/forms';

import { toObservable, takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { debounceTime, distinctUntilChanged, map, of, switchMap, tap } from 'rxjs';
import { GameStatistics } from '../statistics/game-statistics';

@Component({
  selector: 'ccgdle-game-page',
  imports: [GameChallengeCountBadge, GameGrid, FormsModule, FormField, GameStatistics],
  templateUrl: './game-page.html',
  styleUrl: './game-page.css',
})
export class GamePage implements OnInit {
  readonly #route = inject(ActivatedRoute);
  readonly #platformId = inject(PLATFORM_ID);
  readonly #gameService = inject(Game);

  searchModel = signal<{
    name: string;
  }>({ name: '' });
  searchForm = form(this.searchModel);

  gameCard = signal<Card | null>(null);
  attempts = signal<Attempt[]>([]);
  currentGame = signal<UserGame | null>(null);
  searchResults = signal<Card[]>([]);
  isSearching = signal(false);

  readonly #destroy$ = inject(DestroyRef);
  constructor() {
    toObservable(this.searchModel)
      .pipe(
        map((m) => m.name.trim()),
        tap(() => this.isSearching.set(true)),
        debounceTime(500),
        distinctUntilChanged(),
        switchMap((name) => (name ? this.#gameService.searchCards(name) : of([]))),
        tap(() => this.isSearching.set(false)),
        takeUntilDestroyed(this.#destroy$),
      )
      .subscribe((cards) => this.searchResults.set(cards));
  }

  ngOnInit(): void {
    if (!isPlatformBrowser(this.#platformId)) return;

    this.#route.params.subscribe((params) => {
      const urlParams: UrlParams = {
        slug: params['gameMode'],
        date: params['date'],
      };

      this.#gameService.getCurrentGameCard(urlParams).subscribe((card) => {
        this.gameCard.set(card);
      });

      this.#gameService.getCurrentGameAttempts(urlParams).subscribe((attempts) => {
        this.attempts.set(attempts);
      });

      this.#gameService.getOrCreateUserGame(urlParams).subscribe((game) => {
        this.currentGame.set(game);
      });
    });
  }

  selectCard(cardId: number): void {
    const userGameId = this.currentGame()?.id;
    if (!userGameId) return;

    this.#gameService.makeAttempt(userGameId, cardId).subscribe((attempt) => {
      console.log(attempt);
      this.attempts.update((prev) => [...prev, attempt]);
      this.searchResults.set([]);
      this.searchModel.set({ name: '' });
    });
  }
}
