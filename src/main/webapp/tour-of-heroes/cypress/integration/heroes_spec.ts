import { HEROES } from '../../src/app/mock-heroes';
import { Hero } from "../../src/app/hero";

function visitUrl(url) {
  cy.intercept('GET', '/api/heroes', { body: HEROES }).as('getHeroes');
  cy.visit(url);
  cy.wait('@getHeroes');
}

describe('Tour of Heroes - E2E tests', () => {

  describe('Initial Page', () => {

    /* method executes before ALL tests in a describe block*/
    before(() => {
      visitUrl('http://localhost:4200');
    });

    it('displays title', () => {
      cy.get(`app-root h1`).contains('Tour of Heroes');
    });

    it(`displays heroes-app tabs`, () => {
      cy.get('app-root .nav-item').children().contains('Dashboard');
      cy.get('app-root .nav-item').children().contains('Heroes');
    });

    it('has dashboard as the active view', () => {
      cy.get('app-dashboard').should('exist');

      cy.get('app-heroes').should('not.exist');
    });

    it('displays fetched heroes message', () => {
      cy.get('app-messages div').contains('HeroService: fetched heroes');
    });

  });

  describe('Dashboard tests', () => {

    before(() => {
      visitUrl('http://localhost:4200');
    });

    it('has 4 top heroes', () => {
      cy.get(`app-dashboard h3`).contains('Top Heroes');
      cy.get(`app-dashboard-ui div a`).children().should('have.length', 4);
    });

    it('shows hero-detail component by clicking on a dashboard hero', () => {
      cy.get(`app-dashboard-ui div a`).first().click();
      cy.get('app-hero-detail-ui').should('exist');

      cy.url().should('include', '/detail/');
    });

    it('can switch to Heroes view', () => {
      cy.get('app-root .nav-item a').eq(1).click();
      cy.get('app-heroes').should('exist');
      cy.get('app-heroes h2').contains('My Heroes');

      cy.get('app-dashboard').should('not.exist');
    });

  });

  describe('Hero search tests', () => {

    before(() => {
      visitUrl('http://localhost:4200');
    });

    it('can search for a hero', () => {
      cy.get('app-hero-search input').type('Mag');
      cy.get('app-hero-search ul.search-result li').should((list) => {
        expect(list.length).to.be.greaterThan(0);
      });

      cy.get('app-hero-search input').clear();
      cy.get('app-hero-search ul.search-result li').should('not.exist');
    });

  });

  describe('Perform CRUD on Heroes', () => {

    beforeEach(() => {
      visitUrl('http://localhost:4200/heroes');
    });

    it('shows hero-detail component by clicking on a hero', () => {
      cy.get(`app-heroes-ui ul li`).eq(3).click();
      cy.get('app-hero-detail-ui').should('exist');

      cy.url().should('include', '/detail/');

      cy.get('app-hero-detail-ui button').contains('Back').click();
      cy.url().should('include', '/heroes');
    });

    it('adds hero', () => {
      const newHero: Hero = { id: 42, name: 'Brand New Hero' };
      cy.intercept('POST', '/api/heroes', { body: newHero }).as('addHero');

      cy.get(`app-heroes-ui ul li`).should('have.length', 10);
      cy.get('app-heroes-ui input').type(newHero.name);
      cy.get('app-heroes-ui button').contains('Add').click();
      cy.wait('@addHero');

      cy.get(`app-heroes-ui ul li`).should('have.length', 11);
      cy.get(`app-heroes-ui ul li`).contains(newHero.name);
      cy.get('app-messages div').contains('HeroService: added');
    });

    it('updates hero', () => {
      const updatedHero: Hero = HEROES[HEROES.length - 1];
      updatedHero.name = 'Updated Hero';
      cy.intercept('PUT', `/api/heroes/${updatedHero.id}`, { body: updatedHero }).as('updateHero');

      cy.get(`app-heroes-ui ul li`).last().click();
      cy.url().should('include', '/detail');
      cy.get('app-hero-detail-ui #heroName').clear().type('Updated Hero');
      cy.get('app-hero-detail-ui button').contains('Save').click();
      cy.url().should('include', '/heroes');
      cy.wait('@updateHero');

      cy.get(`app-heroes-ui ul li`).contains('Updated Hero');
      cy.get('app-messages div').contains('HeroService: updated hero');
    });

    it('deletes 2 heroes', () => {
      let lastHero: Hero = HEROES[HEROES.length - 1];
      let secondLastHero: Hero = HEROES[HEROES.length - 2];

      cy.intercept('DELETE', `/api/heroes/${lastHero.id}`, { statusCode: 200 }).as('lastHero');
      cy.get(`app-heroes-ui ul li button`).last().contains('x').click();
      cy.wait('@lastHero');

      cy.get('app-messages div').contains('HeroService: deleted hero');
      cy.get(`app-heroes-ui ul li`).should('have.length', 9);

      cy.intercept('DELETE', `/api/heroes/${secondLastHero.id}`, { statusCode: 200 }).as('secondLastHero');
      cy.get(`app-heroes-ui ul li button`).last().contains('x').click();
      cy.wait('@secondLastHero');

      cy.get('app-messages div').contains('HeroService: deleted hero');
      cy.get(`app-heroes-ui ul li`).should('have.length', 8);
    });

    it('does not update hero if "Back" button is clicked', () => {
      cy.get(`app-heroes-ui ul li`).first().should(hero => {
        expect(hero).to.contain('Dr Nice');
      });

      cy.get(`app-heroes-ui ul li`).first().click();
      cy.get('app-hero-detail-ui #heroName').clear().type('Not to be updated');
      cy.get('app-hero-detail-ui button').contains('Back').click();

      cy.get('app-heroes-ui ul li').should((list) => {
        expect(list).to.not.contain('Not to be updated');
        expect(list).to.contain('Dr Nice');
      });
    });

    it('clears Messages by clicking the "clear" button', () => {
      cy.get(`app-messages-ui`).should('exist');
      cy.get(`app-messages-ui .messages-title`).contains('Messages');
      cy.get(`app-messages-ui .hero-message`).should('have.length', 1);
      cy.get(`app-messages-ui button`).contains('Clear').click();

      cy.get(`app-messages-ui div`).should('not.exist');
    });

  });

});

