import { TestBed } from '@angular/core/testing';

import { GameModes } from './game-modes';

describe('GameModes', () => {
  let service: GameModes;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(GameModes);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
