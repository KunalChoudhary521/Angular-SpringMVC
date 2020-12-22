import { Component } from '@angular/core';
import { Observable } from 'rxjs';
import { Hero } from '../hero';
import { Router } from '@angular/router';
import { select, Store } from '@ngrx/store';
import { AppState } from '../store';
import { heroesSelector } from '../store/heroes.selector';
import { addHero, deleteHero } from '../store/heroes.actions';

@Component({
  selector: 'app-heroes',
  template: `<app-heroes-ui [heroes]="heroes$ | async"
                            (selectHero)="navigateToHero($event)"
                            (addHero)="add($event)"
                            (deleteHero)="delete($event)"></app-heroes-ui>`
})
export class HeroesContainerComponent {

  heroes$: Observable<Hero[]> = this.store.pipe(select(heroesSelector));

  constructor(private router: Router, private store: Store<AppState>) { }

  navigateToHero(heroId: number) {
    this.router.navigate([`/detail/${heroId}`]);
  }

  add(name: string): void {
    name = name.trim();
    if (name) {
      this.store.dispatch(addHero({ name } as Hero));
    }
  }

  delete(heroToDelete: Hero): void {
    this.store.dispatch(deleteHero(heroToDelete));
  }



}
