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

describe('Warehouse e2e test', () => {
  const warehousePageUrl = '/warehouse';
  const warehousePageUrlPattern = new RegExp('/warehouse(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const warehouseSample = { acronym: 'curd', name: 'summons sandy aboard', streetAddress: 'spectate', postalCode: 'and baggy' };

  let warehouse;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/warehouses+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/warehouses').as('postEntityRequest');
    cy.intercept('DELETE', '/api/warehouses/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (warehouse) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/warehouses/${warehouse.id}`,
      }).then(() => {
        warehouse = undefined;
      });
    }
  });

  it('Warehouses menu should load Warehouses page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('warehouse');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Warehouse').should('exist');
    cy.url().should('match', warehousePageUrlPattern);
  });

  describe('Warehouse page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(warehousePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Warehouse page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/warehouse/new$'));
        cy.getEntityCreateUpdateHeading('Warehouse');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', warehousePageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/warehouses',
          body: warehouseSample,
        }).then(({ body }) => {
          warehouse = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/warehouses+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/warehouses?page=0&size=20>; rel="last",<http://localhost/api/warehouses?page=0&size=20>; rel="first"',
              },
              body: [warehouse],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(warehousePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Warehouse page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('warehouse');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', warehousePageUrlPattern);
      });

      it('edit button click should load edit Warehouse page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Warehouse');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', warehousePageUrlPattern);
      });

      it('edit button click should load edit Warehouse page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Warehouse');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', warehousePageUrlPattern);
      });

      it('last delete button click should delete instance of Warehouse', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('warehouse').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', warehousePageUrlPattern);

        warehouse = undefined;
      });
    });
  });

  describe('new Warehouse page', () => {
    beforeEach(() => {
      cy.visit(`${warehousePageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Warehouse');
    });

    it('should create an instance of Warehouse', () => {
      cy.get(`[data-cy="acronym"]`).type('recite medicate');
      cy.get(`[data-cy="acronym"]`).should('have.value', 'recite medicate');

      cy.get(`[data-cy="name"]`).type('owlishly');
      cy.get(`[data-cy="name"]`).should('have.value', 'owlishly');

      cy.get(`[data-cy="description"]`).type('../fake-data/blob/hipster.txt');
      cy.get(`[data-cy="description"]`).invoke('val').should('match', new RegExp('../fake-data/blob/hipster.txt'));

      cy.get(`[data-cy="streetAddress"]`).type('unhinge dust');
      cy.get(`[data-cy="streetAddress"]`).should('have.value', 'unhinge dust');

      cy.get(`[data-cy="houseNumber"]`).type('among fooey');
      cy.get(`[data-cy="houseNumber"]`).should('have.value', 'among fooey');

      cy.get(`[data-cy="locationName"]`).type('consequently muster');
      cy.get(`[data-cy="locationName"]`).should('have.value', 'consequently muster');

      cy.get(`[data-cy="postalCode"]`).type('irritably');
      cy.get(`[data-cy="postalCode"]`).should('have.value', 'irritably');

      cy.setFieldImageAsBytesOfEntity('pointLocation', 'integration-test.png', 'image/png');

      cy.get(`[data-cy="mainEmail"]`).type('FVq1@}J>D.l');
      cy.get(`[data-cy="mainEmail"]`).should('have.value', 'FVq1@}J>D.l');

      cy.get(`[data-cy="landPhoneNumber"]`).type('over sight');
      cy.get(`[data-cy="landPhoneNumber"]`).should('have.value', 'over sight');

      cy.get(`[data-cy="mobilePhoneNumber"]`).type('asphalt');
      cy.get(`[data-cy="mobilePhoneNumber"]`).should('have.value', 'asphalt');

      cy.get(`[data-cy="faxNumber"]`).type('turning plastic');
      cy.get(`[data-cy="faxNumber"]`).should('have.value', 'turning plastic');

      cy.get(`[data-cy="extraDetails"]`).type('../fake-data/blob/hipster.txt');
      cy.get(`[data-cy="extraDetails"]`).invoke('val').should('match', new RegExp('../fake-data/blob/hipster.txt'));

      // since cypress clicks submit too fast before the blob fields are validated
      cy.wait(200); // eslint-disable-line cypress/no-unnecessary-waiting
      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        warehouse = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', warehousePageUrlPattern);
    });
  });
});
