import { createFeatureSelector, createSelector } from '@ngrx/store';
import { heroAdapter, HeroesState } from './heroes.state';

const selectHeroesState = createFeatureSelector<HeroesState>('hero');
const getSelectedHeroId = (state: HeroesState) => state.selectedHeroId;

const {
  selectAll,
  selectEntities,
  selectIds,
  /*selectTotal*/
} = heroAdapter.getSelectors();

const selectCurrentHeroId = createSelector(selectHeroesState, getSelectedHeroId);
export const heroesSelector = createSelector(selectHeroesState, selectAll);

const selectHeroEntities = createSelector(selectHeroesState, selectEntities);
export const heroByIdSelector = createSelector(selectHeroEntities, (entities, heroId) => entities[heroId]);
export const matchingHeroNamesSelector = createSelector(heroesSelector,
                                                        (heroes, term) => term ? heroes.filter(h => h.name.includes(term)) : []);
