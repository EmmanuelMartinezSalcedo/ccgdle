export interface CardSet {
  id: number;
  name: string;
  slug: string;
}

export function getSetArrow(
  attempt: CardSet,
  target: CardSet,
): 'arrow_upward' | 'arrow_downward' | null {
  if (attempt.id === target.id) return null;

  return attempt.id > target.id ? 'arrow_downward' : 'arrow_upward';
}
