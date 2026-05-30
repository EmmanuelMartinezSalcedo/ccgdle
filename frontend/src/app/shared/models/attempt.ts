import { UUID } from 'node:crypto';
import { Card } from './card';

export interface Attempt {
  id: UUID;
  card: Card;
  result: boolean;
  attemptNumber: number;
}
