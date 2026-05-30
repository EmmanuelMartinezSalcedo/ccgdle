import { Routes } from '@angular/router';
import { redirectToTodayGuard } from './core/redirect-to-today-guard';
import { userInitGuard } from './core/user-init-guard';

export const routes: Routes = [
  {
    path: '',
    canActivate: [userInitGuard],
    children: [
      {
        path: 'game',
        loadChildren: () => import('./features/game/game.routes').then((m) => m.GAME_ROUTES),
      },
      {
        path: '',
        canActivate: [redirectToTodayGuard],
        loadComponent: () =>
          import('./features/empty/empty-page/empty-page').then((m) => m.EmptyPage),
        pathMatch: 'full',
      },
      {
        path: '**',
        loadComponent: () =>
          import('./features/empty/empty-page/empty-page').then((m) => m.EmptyPage),
        canActivate: [redirectToTodayGuard],
        pathMatch: 'full',
      },
    ],
  },
];
