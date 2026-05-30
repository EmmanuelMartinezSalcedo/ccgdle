import { inject } from '@angular/core';
import { CanActivateFn } from '@angular/router';
import { map, of, tap } from 'rxjs';
import { Auth } from './auth';
import { Cookies } from './cookies';

export const userInitGuard: CanActivateFn = () => {
  const auth = inject(Auth);
  const cookies = inject(Cookies);

  const user = cookies.get('user');

  if (user) return of(true);

  return auth.createUser().pipe(
    tap((response) => cookies.set('user', response.userId)),
    map(() => true),
  );
};
