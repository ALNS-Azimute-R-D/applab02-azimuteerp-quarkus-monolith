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

describe('Supplier e2e test', () => {
  const supplierPageUrl = '/supplier';
  const supplierPageUrlPattern = new RegExp('/supplier(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const supplierSample = { acronym: 'opposite even', companyName: 'depersonalise after', streetAddress: 'cluttered unfurl whenever' };

  let supplier;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/suppliers+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/suppliers').as('postEntityRequest');
    cy.intercept('DELETE', '/api/suppliers/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (supplier) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/suppliers/${supplier.id}`,
      }).then(() => {
        supplier = undefined;
      });
    }
  });

  it('Suppliers menu should load Suppliers page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('supplier');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Supplier').should('exist');
    cy.url().should('match', supplierPageUrlPattern);
  });

  describe('Supplier page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(supplierPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Supplier page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/supplier/new$'));
        cy.getEntityCreateUpdateHeading('Supplier');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', supplierPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/suppliers',
          body: supplierSample,
        }).then(({ body }) => {
          supplier = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/suppliers+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/suppliers?page=0&size=20>; rel="last",<http://localhost/api/suppliers?page=0&size=20>; rel="first"',
              },
              body: [supplier],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(supplierPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Supplier page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('supplier');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', supplierPageUrlPattern);
      });

      it('edit button click should load edit Supplier page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Supplier');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', supplierPageUrlPattern);
      });

      it('edit button click should load edit Supplier page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Supplier');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', supplierPageUrlPattern);
      });

      it('last delete button click should delete instance of Supplier', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('supplier').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', supplierPageUrlPattern);

        supplier = undefined;
      });
    });
  });

  describe('new Supplier page', () => {
    beforeEach(() => {
      cy.visit(`${supplierPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Supplier');
    });

    it('should create an instance of Supplier', () => {
      cy.get(`[data-cy="acronym"]`).type('given subsume massage');
      cy.get(`[data-cy="acronym"]`).should('have.value', 'given subsume massage');

      cy.get(`[data-cy="companyName"]`).type('bah');
      cy.get(`[data-cy="companyName"]`).should('have.value', 'bah');

      cy.get(`[data-cy="representativeLastName"]`).type('termite roller into');
      cy.get(`[data-cy="representativeLastName"]`).should('have.value', 'termite roller into');

      cy.get(`[data-cy="representativeFirstName"]`).type('striking flop');
      cy.get(`[data-cy="representativeFirstName"]`).should('have.value', 'striking flop');

      cy.get(`[data-cy="jobTitle"]`).type('Central Security Designer');
      cy.get(`[data-cy="jobTitle"]`).should('have.value', 'Central Security Designer');

      cy.get(`[data-cy="streetAddress"]`).type('by');
      cy.get(`[data-cy="streetAddress"]`).should('have.value', 'by');

      cy.get(`[data-cy="houseNumber"]`).type('hearty');
      cy.get(`[data-cy="houseNumber"]`).should('have.value', 'hearty');

      cy.get(`[data-cy="locationName"]`).type('opposite');
      cy.get(`[data-cy="locationName"]`).should('have.value', 'opposite');

      cy.get(`[data-cy="city"]`).type('South Joan');
      cy.get(`[data-cy="city"]`).should('have.value', 'South Joan');

      cy.get(`[data-cy="stateProvince"]`).type('practice');
      cy.get(`[data-cy="stateProvince"]`).should('have.value', 'practice');

      cy.get(`[data-cy="zipPostalCode"]`).type('drowse');
      cy.get(`[data-cy="zipPostalCode"]`).should('have.value', 'drowse');

      cy.get(`[data-cy="countryRegion"]`).type('brownie');
      cy.get(`[data-cy="countryRegion"]`).should('have.value', 'brownie');

      cy.get(`[data-cy="webPage"]`).type('../fake-data/blob/hipster.txt');
      cy.get(`[data-cy="webPage"]`).invoke('val').should('match', new RegExp('../fake-data/blob/hipster.txt'));

      cy.setFieldImageAsBytesOfEntity('pointLocation', 'integration-test.png', 'image/png');

      cy.get(`[data-cy="mainEmail"]`).type("GcwQ'@;T${.gU_I^");
      cy.get(`[data-cy="mainEmail"]`).should('have.value', "GcwQ'@;T${.gU_I^");

      cy.get(`[data-cy="landPhoneNumber"]`).type('sway');
      cy.get(`[data-cy="landPhoneNumber"]`).should('have.value', 'sway');

      cy.get(`[data-cy="mobilePhoneNumber"]`).type('meh foolishly');
      cy.get(`[data-cy="mobilePhoneNumber"]`).should('have.value', 'meh foolishly');

      cy.get(`[data-cy="faxNumber"]`).type('heavily fast wo');
      cy.get(`[data-cy="faxNumber"]`).should('have.value', 'heavily fast wo');

      cy.get(`[data-cy="extraDetails"]`).type('../fake-data/blob/hipster.txt');
      cy.get(`[data-cy="extraDetails"]`).invoke('val').should('match', new RegExp('../fake-data/blob/hipster.txt'));

      // since cypress clicks submit too fast before the blob fields are validated
      cy.wait(200); // eslint-disable-line cypress/no-unnecessary-waiting
      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        supplier = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', supplierPageUrlPattern);
    });
  });
});
