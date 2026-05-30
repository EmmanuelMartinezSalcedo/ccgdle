import { ComponentFixture, TestBed } from '@angular/core/testing';

import { GameChallengeCountBadge } from './game-challenge-count-badge';

describe('GameChallengeCountBadge', () => {
  let component: GameChallengeCountBadge;
  let fixture: ComponentFixture<GameChallengeCountBadge>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [GameChallengeCountBadge],
    }).compileComponents();

    fixture = TestBed.createComponent(GameChallengeCountBadge);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
