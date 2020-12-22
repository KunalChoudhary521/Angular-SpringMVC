import { Component } from '@angular/core';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { Hero } from '../hero';
import { Router } from '@angular/router';
import { select, Store } from '@ngrx/store';
import { heroesSelector } from '../store/heroes.selector';
import { AppState } from '../store';

@Component({
  selector: 'app-dashboard',
  template: `<app-dashboard-ui [heroes]="topHeroes$ | async"
                               (selectHero)="navigateToHero($event)"></app-dashboard-ui>`
})
export class DashboardContainerComponent {
  topHeroes$: Observable<Hero[]> = this.store.pipe(select(heroesSelector), map(heroes => heroes.slice(1, 5)));

  constructor(private router: Router, private store: Store<AppState>) { }

  navigateToHero(heroId: number) {
    this.router.navigate([`/detail/${heroId}`]);
  }
}
