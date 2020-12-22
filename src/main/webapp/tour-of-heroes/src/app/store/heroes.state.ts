import { Hero } from '../hero';
import { createEntityAdapter, EntityAdapter, EntityState } from '@ngrx/entity';

export interface HeroesState extends EntityState<Hero> {
  selectedHeroId: number | null;
  isLoading: boolean;
  errors: any;
}

const selectHeroId = (hero: Hero) => hero.id;
const sortById = (a: Hero, b: Hero): number => a.id - b.id;

export const heroAdapter: EntityAdapter<Hero> = createEntityAdapter<Hero>({
  selectId: selectHeroId,
  sortComparer: sortById
});
