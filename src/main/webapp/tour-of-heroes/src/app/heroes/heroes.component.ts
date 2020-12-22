import { Component, EventEmitter, Input, Output } from '@angular/core';

import { Hero } from '../hero';

@Component({
  selector: 'app-heroes-ui',
  templateUrl: './heroes.component.html',
  styleUrls: ['./heroes.component.css']
})
export class HeroesComponent {
  @Input() heroes: Hero[];
  @Output() selectHero = new EventEmitter<number>();
  @Output() addHero = new EventEmitter<string>();
  @Output() deleteHero = new EventEmitter<Hero>();

}
