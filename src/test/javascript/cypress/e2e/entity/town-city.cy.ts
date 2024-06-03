import {
  entityTableSelector,
  entityDetailsButtonSelector,
  entityDetailsBackButtonSelector,
  entityCreateButtonSelector,
  entityCreateSaveButtonSelector,
  entityCreateCancelButtonSelector,
  entityEditButtonSelector,
  entityDeleteButtonSelector,
  entityConfirmDeleteButtonSelector,
} from '../../support/entity';

describe('TownCity e2e test', () => {
  const townCityPageUrl = '/town-city';
  const townCityPageUrlPattern = new RegExp('/town-city(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  // const townCitySample = {"acronym":"flub sig","name":"misrepresent vicious"};

  let townCity;
  // let province;

  beforeEach(() => {
    cy.login(username, password);
  });

  /* Disabled due to incompatibility
  beforeEach(() => {
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/provinces',
      body: {"acronym":"exo","name":"save zowie","geoPolygonArea":"Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci5wbmc=","geoPolygonAreaContentType":"unknown"},
    }).then(({ body }) => {
      province = body;
    });
  });
   */

  beforeEach(() => {
    cy.intercept('GET', '/api/town-cities+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/town-cities').as('postEntityRequest');
    cy.intercept('DELETE', '/api/town-cities/*').as('deleteEntityRequest');
  });

  /* Disabled due to incompatibility
  beforeEach(() => {
    // Simulate relationships api for better performance and reproducibility.
    cy.intercept('GET', '/api/provinces', {
      statusCode: 200,
      body: [province],
    });

    cy.intercept('GET', '/api/districts', {
      statusCode: 200,
      body: [],
    });

  });
   */

  afterEach(() => {
    if (townCity) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/town-cities/${townCity.id}`,
      }).then(() => {
        townCity = undefined;
      });
    }
  });

  /* Disabled due to incompatibility
  afterEach(() => {
    if (province) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/provinces/${province.id}`,
      }).then(() => {
        province = undefined;
      });
    }
  });
   */

  it('TownCities menu should load TownCities page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('town-city');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('TownCity').should('exist');
    cy.url().should('match', townCityPageUrlPattern);
  });

  describe('TownCity page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(townCityPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create TownCity page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/town-city/new$'));
        cy.getEntityCreateUpdateHeading('TownCity');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', townCityPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      /* Disabled due to incompatibility
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/town-cities',
          body: {
            ...townCitySample,
            province: province,
          },
        }).then(({ body }) => {
          townCity = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/town-cities+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/town-cities?page=0&size=20>; rel="last",<http://localhost/api/town-cities?page=0&size=20>; rel="first"',
              },
              body: [townCity],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(townCityPageUrl);

        cy.wait('@entitiesRequestInternal');
      });
       */

      beforeEach(function () {
        cy.visit(townCityPageUrl);

        cy.wait('@entitiesRequest').then(({ response }) => {
          if (response.body.length === 0) {
            this.skip();
          }
        });
      });

      it('detail button click should load details TownCity page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('townCity');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', townCityPageUrlPattern);
      });

      it('edit button click should load edit TownCity page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('TownCity');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', townCityPageUrlPattern);
      });

      it('edit button click should load edit TownCity page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('TownCity');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', townCityPageUrlPattern);
      });

      it.skip('last delete button click should delete instance of TownCity', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('townCity').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', townCityPageUrlPattern);

        townCity = undefined;
      });
    });
  });

  describe('new TownCity page', () => {
    beforeEach(() => {
      cy.visit(`${townCityPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('TownCity');
    });

    it.skip('should create an instance of TownCity', () => {
      cy.get(`[data-cy="acronym"]`).type('when');
      cy.get(`[data-cy="acronym"]`).should('have.value', 'when');

      cy.get(`[data-cy="name"]`).type('seldom');
      cy.get(`[data-cy="name"]`).should('have.value', 'seldom');

      cy.setFieldImageAsBytesOfEntity('geoPolygonArea', 'integration-test.png', 'image/png');

      cy.get(`[data-cy="province"]`).select(1);

      // since cypress clicks submit too fast before the blob fields are validated
      cy.wait(200); // eslint-disable-line cypress/no-unnecessary-waiting
      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        townCity = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', townCityPageUrlPattern);
    });
  });
});
