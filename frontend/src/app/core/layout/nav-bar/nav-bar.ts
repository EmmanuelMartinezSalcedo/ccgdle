import { Component, inject, OnInit, signal } from '@angular/core';
import { GameModes } from '../../../shared/services/game-modes';
import { GameMode } from '../../../shared/models/game-mode';
import { ActivatedRoute, RouterLink } from '@angular/router';

@Component({
  selector: 'ccgdle-nav-bar',
  imports: [RouterLink],
  templateUrl: './nav-bar.html',
  styleUrl: './nav-bar.css',
})
export class NavBar implements OnInit {
  readonly #gameModesService = inject(GameModes);
  readonly route = inject(ActivatedRoute);

  date = this.route.snapshot.params['date'];
  gameModes = signal<GameMode[]>([]);

  ngOnInit(): void {
    this.#gameModesService.getGameModes().subscribe((gameModes) => {
      this.gameModes.set(gameModes);
    });
  }
}
