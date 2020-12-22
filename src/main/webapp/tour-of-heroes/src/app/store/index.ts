import { ActionReducerMap } from '@ngrx/store';

import { heroesReducer } from './heroes.reducer';
import { HeroesState } from './heroes.state';

export interface AppState {
  hero: HeroesState;
}

export const appReducers: ActionReducerMap<AppState> = {
  hero: heroesReducer
};
