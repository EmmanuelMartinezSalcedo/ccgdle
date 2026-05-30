import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';

export const redirectToTodayGuard: CanActivateFn = () => {
  const router = inject(Router);

  const today = new Date();

  const year = today.getFullYear();
  const month = String(today.getMonth() + 1).padStart(2, '0');
  const day = String(today.getDate()).padStart(2, '0');

  const date = `${year}-${month}-${day}`;

  return router.parseUrl(`/game/standard/${date}`);
};
