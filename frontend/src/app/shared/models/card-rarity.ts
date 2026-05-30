export interface CardRarity {
  id: number;
  name: string;
  slug: string;
  dustValue: number | null;
}

export function getRarityArrow(
  attempt: CardRarity,
  target: CardRarity,
): 'arrow_upward' | 'arrow_downward' | null {
  const attemptValue = attempt.dustValue ?? 0;
  const targetValue = target.dustValue ?? 0;

  if (attemptValue === targetValue) return null;

  return attemptValue > targetValue ? 'arrow_downward' : 'arrow_upward';
}
