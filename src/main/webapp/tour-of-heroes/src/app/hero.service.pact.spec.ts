import { TestBed } from '@angular/core/testing';
import { Matchers, InteractionObject, Pact } from '@pact-foundation/pact';
import { HeroService } from './hero.service';
import * as path from 'path';
import { HTTPMethod } from '@pact-foundation/pact/common/request';
import { Hero } from './hero';
import { HTTP_INTERCEPTORS, HttpClientModule } from '@angular/common/http';
import { BaseUrlInterceptor } from './base-url.interceptor';
const { like } = Matchers;

describe('HeroServicePact', () => {

  const provider = new Pact({
    cors: true,
    spec: 3,
    consumer: 'angular-ui',
    provider: 'spring-boot',
    port: 8085,
    dir: path.resolve(process.cwd(), 'src/pacts'),
    log: path.resolve(process.cwd(), 'src/logs', 'pact-server.log'),
    logLevel: 'info'
  });

  beforeAll(async () => {
    await provider.setup();
  });

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientModule],
      providers: [HeroService,
        { provide: HTTP_INTERCEPTORS, useClass: BaseUrlInterceptor, multi: true }
      ]
    });
  });

  afterEach(async () => {
    await provider.verify();
  });

  afterAll(async () => {
    await provider.finalize();
  });

  it('get all heroes', async () => {
    const expectedHeroes: Hero[] = [
      {id: 1, name: 'CDC Hero 1'},
      {id: 2, name: 'CDC Hero 2'}
    ];

    const interaction: InteractionObject = {
      state: 'A list of heroes exists',
      uponReceiving: 'a request to GET all heroes',
      withRequest: {
        method: HTTPMethod.GET,
        path: '/api/heroes'
      },
      willRespondWith: {
        status: 200,
        body: like(expectedHeroes)
      }
    };
    await provider.addInteraction(interaction);

    const heroService = TestBed.inject(HeroService);

    /*heroService.getHeroes().subscribe(response => {
      expect(response).toEqual(expectedHeroes);
    });*/
    const response = await heroService.getHeroes().toPromise();
    expect(response).toBeTruthy();

  });
});
