import { Component } from '@angular/core';
import { Observable } from 'rxjs';
import { filter, switchMap } from 'rxjs/operators';
import { Hero } from '../hero';
import { ActivatedRoute } from '@angular/router';
import { Location } from '@angular/common';
import { Store } from '@ngrx/store';
import { AppState } from '../store';
import { heroByIdSelector } from '../store/heroes.selector';
import { updateHero } from '../store/heroes.actions';


@Component({
  selector: 'app-hero-detail',
  template: `<app-hero-detail-ui [hero]="hero$ | async"
                                 (heroChange)="save($event)"
                                 (cancel)="goBack()"></app-hero-detail-ui>`
})
export class HeroDetailContainerComponent {
  /*
  * Note: app-root dispatches getHeroes action which loads all heroes in the store.
  * Even if the user initially goes to /detail/:id page, it will still be able to retrieve hero by id.
  * If getHeroes action was not dispatched in app-root, it would be necessary to dispatch getHeroes here
  * to get all Heroes load heroes if they were not already in the store.
  * */
  hero$: Observable<Hero> = this.route.paramMap.pipe(
                                                filter(params => params.has('id')),
                                                switchMap(params => this.store.select(heroByIdSelector, +params.get('id'))));

  constructor(private route: ActivatedRoute,
              private location: Location,
              private store: Store<AppState>) { }

  goBack(): void {
    this.location.back();
  }

  save(hero): void {
    this.store.dispatch(updateHero(hero));
  }
}
