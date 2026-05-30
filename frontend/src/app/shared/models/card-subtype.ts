import { UUID } from 'node:crypto';

export interface CardSubtype {
  id: UUID;
  name: string;
  slug: string;
}

export function getSubtypeMatchState(
  attempt: CardSubtype[],
  target: CardSubtype[],
): 'success' | 'warning' | 'error' {
  const targetIds = new Set(target.map((c) => c.id));

  const matches = attempt.filter((c) => targetIds.has(c.id)).length;

  if (matches === target.length && attempt.length === target.length) {
    return 'success';
  }

  if (matches > 0) {
    return 'warning';
  }

  return 'error';
}
