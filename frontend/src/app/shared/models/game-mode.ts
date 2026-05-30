import { UUID } from 'node:crypto';

export interface GameMode {
  id: UUID;
  name: string;
  slug: string;
}
