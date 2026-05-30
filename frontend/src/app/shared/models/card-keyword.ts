export interface CardKeyword {
  id: number;
  name: string;
  slug: string;
}
export function getKeywordMatchState(
  attempt: CardKeyword[],
  target: CardKeyword[],
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
