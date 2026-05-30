import { UUID } from 'node:crypto';

export interface UserGame {
  id: UUID;
  userId: UUID;
  gameId: UUID;
}
