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

describe('Locality e2e test', () => {
  const localityPageUrl = '/locality';
  const localityPageUrlPattern = new RegExp('/locality(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const localitySample = { acronym: 'consensu', name: 'merry loyally astride' };

  let locality;
  let country;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/countries',
      body: {
        acronym: 'val',
        name: 'ick than',
        continent: 'EUROPE',
        geoPolygonArea: 'Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci5wbmc=',
        geoPolygonAreaContentType: 'unknown',
      },
    }).then(({ body }) => {
      country = body;
    });
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/localities+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/localities').as('postEntityRequest');
    cy.intercept('DELETE', '/api/localities/*').as('deleteEntityRequest');
  });

  beforeEach(() => {
    // Simulate relationships api for better performance and reproducibility.
    cy.intercept('GET', '/api/countries', {
      statusCode: 200,
      body: [country],
    });
  });

  afterEach(() => {
    if (locality) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/localities/${locality.id}`,
      }).then(() => {
        locality = undefined;
      });
    }
  });

  afterEach(() => {
    if (country) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/countries/${country.id}`,
      }).then(() => {
        country = undefined;
      });
    }
  });

  it('Localities menu should load Localities page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('locality');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Locality').should('exist');
    cy.url().should('match', localityPageUrlPattern);
  });

  describe('Locality page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(localityPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Locality page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/locality/new$'));
        cy.getEntityCreateUpdateHeading('Locality');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', localityPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/localities',
          body: {
            ...localitySample,
            country: country,
          },
        }).then(({ body }) => {
          locality = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/localities+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/localities?page=0&size=20>; rel="last",<http://localhost/api/localities?page=0&size=20>; rel="first"',
              },
              body: [locality],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(localityPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Locality page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('locality');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', localityPageUrlPattern);
      });

      it('edit button click should load edit Locality page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Locality');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', localityPageUrlPattern);
      });

      it('edit button click should load edit Locality page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Locality');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', localityPageUrlPattern);
      });

      it('last delete button click should delete instance of Locality', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('locality').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', localityPageUrlPattern);

        locality = undefined;
      });
    });
  });

  describe('new Locality page', () => {
    beforeEach(() => {
      cy.visit(`${localityPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Locality');
    });

    it('should create an instance of Locality', () => {
      cy.get(`[data-cy="acronym"]`).type('excludin');
      cy.get(`[data-cy="acronym"]`).should('have.value', 'excludin');

      cy.get(`[data-cy="name"]`).type('furiously sudden');
      cy.get(`[data-cy="name"]`).should('have.value', 'furiously sudden');

      cy.get(`[data-cy="description"]`).type('../fake-data/blob/hipster.txt');
      cy.get(`[data-cy="description"]`).invoke('val').should('match', new RegExp('../fake-data/blob/hipster.txt'));

      cy.setFieldImageAsBytesOfEntity('geoPolygonArea', 'integration-test.png', 'image/png');

      cy.get(`[data-cy="country"]`).select(1);

      // since cypress clicks submit too fast before the blob fields are validated
      cy.wait(200); // eslint-disable-line cypress/no-unnecessary-waiting
      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        locality = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', localityPageUrlPattern);
    });
  });
});
