import { Injectable } from '@angular/core';
import { Actions, createEffect, ofType } from '@ngrx/effects';
import { of } from 'rxjs';
import { catchError, map, switchMap, tap } from 'rxjs/operators';
import { HeroService } from '../hero.service';
import * as HeroesActions from './heroes.actions';
import { Location } from '@angular/common';
import { Hero } from '../hero';

@Injectable()
export class HeroesEffects {

  loadHeroes$ = createEffect(() => this.actions$.pipe(
    ofType(HeroesActions.getHeroes.type),
    switchMap(() => this.heroService.getHeroes().pipe(
      map(heroes => ({ type: HeroesActions.getHeroesSuccess.type, heroes })),
      catchError((err) => of({ type: HeroesActions.getHeroesFail.type, payload: err}))
    ))
    )
  );

  addHero$ = createEffect(() => this.actions$.pipe(
    ofType(HeroesActions.addHero.type),
    switchMap(({ hero }) => this.heroService.addHero(hero).pipe(
      map((heroToAdd) => ({ type: HeroesActions.addHeroSuccess.type, hero: heroToAdd })),
      catchError((err) => of({ type: HeroesActions.addHeroFail.type, payload: err }))
    ))
    )
  );

  deleteHero$ = createEffect(() => this.actions$.pipe(
    ofType(HeroesActions.deleteHero.type),
    switchMap(({ hero }: {hero: Hero}) => this.heroService.deleteHero(hero).pipe(
      map(() => ({ type: HeroesActions.deleteHeroSuccess.type, heroId: hero.id })),
      catchError((err) => of({ type: HeroesActions.deleteHeroFail.type, payload: err }))
    ))
    )
  );

  updateHero$ = createEffect(() => this.actions$.pipe(
    ofType(HeroesActions.updateHero.type),
    switchMap(({ hero }: {hero: Hero}) => this.heroService.updateHero(hero).pipe(
      map(() => ({ type: HeroesActions.updateHeroSuccess.type, hero: {id: hero.id, changes: hero} })),
      catchError((err) => of({ type: HeroesActions.updateHeroFail.type, payload: err }))
    ))
    )
  );

  goBack$ =  createEffect(() => this.actions$.pipe(
    ofType(HeroesActions.updateHeroSuccess.type),
    tap(() => this.location.back())
  ), { dispatch: false });

  constructor(private actions$: Actions,
              private heroService: HeroService,
              private location: Location) {}
}
