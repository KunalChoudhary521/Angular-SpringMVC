import { NgModule }       from '@angular/core';
import { BrowserModule }  from '@angular/platform-browser';
import { FormsModule }    from '@angular/forms';
import { HttpClientModule }    from '@angular/common/http';

import { HttpClientInMemoryWebApiModule } from 'angular-in-memory-web-api';
import { InMemoryDataService }  from './in-memory-data.service';

import { AppRoutingModule }     from './app-routing.module';

import { AppComponent }         from './app.component';
import { DashboardComponent }   from './dashboard/dashboard.component';
import { HeroDetailComponent }  from './hero-detail/hero-detail.component';
import { HeroesComponent }      from './heroes/heroes.component';
import { HeroSearchComponent }  from './hero-search/hero-search.component';
import { MessagesComponent }    from './messages/messages.component';
import { DashboardContainerComponent } from './dashboard/dashboard.container';
import { HeroDetailContainerComponent } from './hero-detail/hero-detail.container';
import { HeroesContainerComponent } from './heroes/heroes.container';
import { MessagesContainerComponent } from './messages/messages.container';
import { StoreModule } from '@ngrx/store';
import { EffectsModule } from '@ngrx/effects';
import { StoreDevtoolsModule } from '@ngrx/store-devtools';
import { environment } from '../environments/environment';
import { HeroesEffects } from './store/heroes.effects';
import { appReducers } from './store';

@NgModule({
  imports: [
    BrowserModule,
    FormsModule,
    AppRoutingModule,
    HttpClientModule,

    // The HttpClientInMemoryWebApiModule module intercepts HTTP requests
    // and returns simulated server responses.
    // Remove it when a real server is ready to receive requests.
    HttpClientInMemoryWebApiModule.forRoot(
      InMemoryDataService, { dataEncapsulation: false }
    ),
    StoreModule.forRoot(appReducers),
    EffectsModule.forRoot([HeroesEffects]),
    StoreDevtoolsModule.instrument({ logOnly: environment.production })
  ],
  declarations: [
    AppComponent,
    DashboardContainerComponent,
    DashboardComponent,
    HeroesContainerComponent,
    HeroesComponent,
    HeroDetailContainerComponent,
    HeroDetailComponent,
    MessagesContainerComponent,
    MessagesComponent,
    HeroSearchComponent
  ],
  bootstrap: [ AppComponent ]
})

export class AppModule { }
