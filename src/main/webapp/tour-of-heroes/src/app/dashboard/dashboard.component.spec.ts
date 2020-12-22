import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DashboardContainerComponent } from './dashboard.container';
import { HeroSearchComponent } from '../hero-search/hero-search.component';

import { RouterTestingModule } from '@angular/router/testing';
import { HEROES } from '../mock-heroes';
import { DashboardComponent } from './dashboard.component';
import { MockStore, provideMockStore } from '@ngrx/store/testing';
import { AppState } from '../store';
import { initialAppState } from '../store/heroes.reducer';

describe('DashboardComponent', () => {
  let component: DashboardContainerComponent;
  let fixture: ComponentFixture<DashboardContainerComponent>;
  let store: MockStore<AppState>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [
        DashboardContainerComponent,
        DashboardComponent,
        HeroSearchComponent
      ],
      imports: [
        RouterTestingModule.withRoutes([])
      ],
      providers: [
        provideMockStore<AppState>({initialState: {
          hero: {
            ...initialAppState,
            entities: HEROES.reduce((map, obj) => { map[obj.id] = obj; return map; } , {}),
            ids: HEROES.map(h => h.id)
          },
        }
        }),
        MockStore
      ]
    })
    .compileComponents();

  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DashboardContainerComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
    store = TestBed.get(MockStore);
  });

  it('should be created', () => {
    expect(component).toBeTruthy();
  });

  it('should display "Top Heroes" as headline', () => {
    expect(fixture.nativeElement.querySelector('h3').textContent).toEqual('Top Heroes');
  });

  it('should display 4 hero links', async(() => {
    expect(store).toBeTruthy();
    expect(fixture.nativeElement.querySelectorAll('a').length).toEqual(4);
  }));

});
