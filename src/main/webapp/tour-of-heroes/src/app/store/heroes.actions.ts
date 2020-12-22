import { createAction, props } from '@ngrx/store';
import { Hero } from '../hero';
import { Update } from '@ngrx/entity';

export const getHeroes = createAction('[Heroes] Get Heroes');
export const getHeroesSuccess = createAction('[Heroes] Get Heroes Success', props<{ heroes: Hero[] }>());
export const getHeroesFail = createAction('[Heroes] Get Heroes Fail', props<{ errors: any }>());

export const addHero = createAction('[Heroes] Add Hero', (hero: Hero) => ({ hero }));
export const addHeroSuccess = createAction('[Heroes] Add Hero Success', props<{ hero: Hero }>());
export const addHeroFail = createAction('[Heroes] Add Hero Fail', props<{ errors: any }>());

export const deleteHero = createAction('[Heroes] Delete Hero', (hero: Hero) => ({ hero }));
export const deleteHeroSuccess = createAction('[Heroes] Delete Hero Success', props<{ heroId: number }>());
export const deleteHeroFail = createAction('[Heroes] Delete Hero Fail', props<{ errors: any }>());

export const updateHero = createAction('[Heroes] Update Hero', (hero: Hero) => ({ hero }));
export const updateHeroSuccess = createAction('[Heroes] Update Hero Success', props<{ hero: Update<Hero> }>());
export const updateHeroFail = createAction('[Heroes] Update Hero Fail', props<{ errors: any }>());
