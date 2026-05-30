import { UrlParams } from './../../shared/models/url-params';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { catchError, Observable, throwError } from 'rxjs';
import { Card } from '../../shared/models/card';
import { baseUrl } from '../../shared/constants/base-url';
import { UUID } from 'node:crypto';
import { Attempt } from '../../shared/models/attempt';
import { Cookies } from '../../core/cookies';
import { UserGame } from '../../shared/models/user-game';

@Injectable({
  providedIn: 'root',
})
export class Game {
  readonly #http = inject(HttpClient);
  readonly #cookieService = inject(Cookies);

  getCurrentGameCard(urlParams: UrlParams): Observable<Card> {
    const result$ = this.#http.get<Card>(
      `${baseUrl}/cards/game-card/${urlParams.slug}/${urlParams.date}`,
    );

    return result$;
  }

  getCurrentGameAttempts(urlParams: UrlParams): Observable<Attempt[]> {
    const result$ = this.#http.get<Attempt[]>(
      `${baseUrl}/cards/game-attempts/${urlParams.slug}/${urlParams.date}/${this.#cookieService.get('user') as UUID}`,
    );

    return result$;
  }

  createUserGame(urlParams: UrlParams): Observable<UserGame> {
    const body = {
      userId: this.#cookieService.get('user') as UUID,
      date: urlParams.date,
      slug: urlParams.slug,
    };

    const result$ = this.#http.post<UserGame>(`${baseUrl}/game-modes/user-game`, body);

    return result$;
  }

  getUserGame(urlParams: UrlParams): Observable<UserGame> {
    const result$ = this.#http.get<UserGame>(
      `${baseUrl}/game-modes/user-game/${urlParams.slug}/${urlParams.date}/${this.#cookieService.get('user') as UUID}`,
    );
    return result$;
  }

  getOrCreateUserGame(urlParams: UrlParams): Observable<UserGame> {
    return this.getUserGame(urlParams).pipe(
      catchError((error: HttpErrorResponse) => {
        if (error.status === 404) {
          return this.createUserGame(urlParams);
        }

        return throwError(() => error);
      }),
    );
  }

  searchCards(name: string): Observable<Card[]> {
    const result$ = this.#http.get<Card[]>(`${baseUrl}/cards/search/${name}`);
    return result$;
  }

  makeAttempt(userGameId: UUID, cardId: number): Observable<Attempt> {
    const body = {
      userId: this.#cookieService.get('user') as UUID,
      userGameId,
      cardId,
    };

    const result$ = this.#http.post<Attempt>(`${baseUrl}/users/attempt`, body);
    return result$;
  }
}
