import { inject, Injectable, PLATFORM_ID, REQUEST, RESPONSE_INIT } from '@angular/core';
import { isPlatformBrowser } from '@angular/common';

@Injectable({
  providedIn: 'root',
})
export class Cookies {
  readonly #platformId = inject(PLATFORM_ID);
  readonly #request = inject(REQUEST, { optional: true });
  readonly #responseInit = inject(RESPONSE_INIT, { optional: true });

  private appendSetCookie(parts: string): void {
    const headers = (this.#responseInit as any)?.headers;
    if (headers) {
      if (headers instanceof Headers) {
        headers.append('Set-Cookie', parts);
      } else {
        const existing = (headers as Record<string, string>)['Set-Cookie'];
        (headers as Record<string, string>)['Set-Cookie'] = existing
          ? `${existing}, ${parts}`
          : parts;
      }
    }
  }

  get(name: string): string | null {
    const raw = isPlatformBrowser(this.#platformId)
      ? document.cookie
      : (this.#request?.headers.get('cookie') ?? '');

    const match = new RegExp(`(?:^|; )${name}=([^;]*)`).exec(raw);

    return match ? decodeURIComponent(match[1]) : null;
  }

  set(
    name: string,
    value: string,
    options: {
      maxAge?: number;
      path?: string;
      secure?: boolean;
      sameSite?: 'Lax' | 'Strict' | 'None';
      httpOnly?: boolean;
    } = {},
  ): void {
    const parts = [
      `${name}=${encodeURIComponent(value)}`,
      `Path=${options.path ?? '/'}`,
      options.maxAge ? `Max-Age=${options.maxAge}` : '',
      options.secure ? 'Secure' : '',
      options.httpOnly ? 'HttpOnly' : '',
      `SameSite=${options.sameSite ?? 'Lax'}`,
    ]
      .filter(Boolean)
      .join('; ');

    if (isPlatformBrowser(this.#platformId)) {
      document.cookie = parts;
    } else {
      this.appendSetCookie(parts);
    }
  }

  delete(name: string, path = '/'): void {
    this.set(name, '', { maxAge: 0, path });
  }
}
