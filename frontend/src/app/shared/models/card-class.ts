export interface CardClass {
  id: number;
  name: string;
  slug: string;
}

export function getClassMatchState(
  attempt: CardClass[],
  target: CardClass[],
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
