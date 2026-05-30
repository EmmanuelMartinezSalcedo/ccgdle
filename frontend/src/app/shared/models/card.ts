import { CardClass } from './card-class';
import { CardKeyword } from './card-keyword';
import { CardRarity } from './card-rarity';
import { CardSet } from './card-set';
import { CardSubtype } from './card-subtype';
import { CardType } from './card-type';

export interface Card {
  id: number;
  name: string;
  slug: string;
  image: string;
  manaCost: number;
  rarity: CardRarity;
  set: CardSet;
  type: CardType;
  classes: CardClass[];
  keywords: CardKeyword[];
  subtypes: CardSubtype[];
}
