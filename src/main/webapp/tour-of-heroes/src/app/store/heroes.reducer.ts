import { heroAdapter, HeroesState } from './heroes.state';
import { Action, createReducer, on } from '@ngrx/store';
import * as HeroesActions from './heroes.actions';

export const initialAppState: HeroesState = heroAdapter.getInitialState({
  selectedHeroId: null,
  isLoading: false,
  errors: null
});

// tslint:disable-next-line:variable-name
const _heroesReducer = createReducer(initialAppState,
  on(HeroesActions.getHeroes, state => ({...state, isLoading: true})),
  on(HeroesActions.getHeroesSuccess, (state, { heroes }) => heroAdapter.addMany(heroes, state)),
  on(HeroesActions.getHeroesFail, (state, { errors }) => ({...state, heroes: null, isLoading: false, errors })),

  on(HeroesActions.addHero, state => ({...state })),
  on(HeroesActions.addHeroSuccess, (state, { hero }) => heroAdapter.addOne(hero, state)),
  on(HeroesActions.addHeroFail, (state, { errors }) => ({...state, errors })),

  on(HeroesActions.deleteHero, state => ({...state })),
  on(HeroesActions.deleteHeroSuccess, (state, { heroId }) => heroAdapter.removeOne(heroId, state)),
  on(HeroesActions.deleteHeroFail, (state, { errors }) => ({...state, errors })),

  on(HeroesActions.updateHero, state => ({...state })),
  on(HeroesActions.updateHeroSuccess, (state, { hero }) => heroAdapter.updateOne(hero, state)),
  on(HeroesActions.updateHeroFail, (state, { errors }) => ({...state, errors }))
);


/* The exported reducer function is necessary as function calls are not supported by the AOT compiler. */
export function heroesReducer(state: HeroesState | undefined, action: Action) {
  return _heroesReducer(state, action);
}
