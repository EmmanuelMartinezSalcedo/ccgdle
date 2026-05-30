import { Routes } from '@angular/router';

export const GAME_ROUTES: Routes = [
  {
    path: ':gameMode/:date',
    loadComponent: () => import('./game-page/game-page').then((m) => m.GamePage),
  },
];
