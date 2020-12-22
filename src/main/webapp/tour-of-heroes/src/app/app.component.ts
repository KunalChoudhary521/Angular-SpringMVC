import { Component, OnInit } from '@angular/core';
import { Store } from '@ngrx/store';
import { AppState } from './store';
import * as HeroesActions from './store/heroes.actions';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit {
  title = 'Tour of Heroes';

  constructor(private store: Store<AppState>) { }

  ngOnInit() {
    // Dispatch here so that Dashboard or Heroes component get the heroes when application loads
    this.store.dispatch(HeroesActions.getHeroes());
  }
}
