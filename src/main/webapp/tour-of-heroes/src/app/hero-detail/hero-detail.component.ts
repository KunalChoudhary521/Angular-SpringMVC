import { Component, Input, EventEmitter, Output } from '@angular/core';

import { Hero }         from '../hero';

@Component({
  selector: 'app-hero-detail-ui',
  templateUrl: './hero-detail.component.html',
  styleUrls: [ './hero-detail.component.css' ]
})
export class HeroDetailComponent {
  @Input() hero: Hero;
  @Output() cancel = new EventEmitter<void>();
  @Output() heroChange = new EventEmitter<Hero>();
}
