import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { GameMode } from '../models/game-mode';
import { baseUrl } from '../constants/base-url';

@Injectable({
  providedIn: 'root',
})
export class GameModes {
  readonly #http = inject(HttpClient);

  getGameModes(): Observable<GameMode[]> {
    const result$ = this.#http.get<GameMode[]>(`${baseUrl}/game-modes`);

    return result$;
  }
}
