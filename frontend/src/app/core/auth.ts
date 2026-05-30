import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { baseUrl } from '../shared/constants/base-url';
import { UUID } from 'node:crypto';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class Auth {
  readonly #http = inject(HttpClient);

  createUser(): Observable<{
    userId: UUID;
  }> {
    const result$ = this.#http.post<{
      userId: UUID;
    }>(`${baseUrl}/users/create`, {});
    return result$;
  }
}
