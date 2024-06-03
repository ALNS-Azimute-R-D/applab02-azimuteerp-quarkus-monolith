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

describe('CustomerType e2e test', () => {
  const customerTypePageUrl = '/customer-type';
  const customerTypePageUrlPattern = new RegExp('/customer-type(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const customerTypeSample = { name: 'zowie however whoeve' };

  let customerType;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/customer-types+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/customer-types').as('postEntityRequest');
    cy.intercept('DELETE', '/api/customer-types/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (customerType) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/customer-types/${customerType.id}`,
      }).then(() => {
        customerType = undefined;
      });
    }
  });

  it('CustomerTypes menu should load CustomerTypes page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('customer-type');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('CustomerType').should('exist');
    cy.url().should('match', customerTypePageUrlPattern);
  });

  describe('CustomerType page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(customerTypePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create CustomerType page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/customer-type/new$'));
        cy.getEntityCreateUpdateHeading('CustomerType');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', customerTypePageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/customer-types',
          body: customerTypeSample,
        }).then(({ body }) => {
          customerType = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/customer-types+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/customer-types?page=0&size=20>; rel="last",<http://localhost/api/customer-types?page=0&size=20>; rel="first"',
              },
              body: [customerType],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(customerTypePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details CustomerType page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('customerType');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', customerTypePageUrlPattern);
      });

      it('edit button click should load edit CustomerType page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('CustomerType');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', customerTypePageUrlPattern);
      });

      it('edit button click should load edit CustomerType page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('CustomerType');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', customerTypePageUrlPattern);
      });

      it('last delete button click should delete instance of CustomerType', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('customerType').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', customerTypePageUrlPattern);

        customerType = undefined;
      });
    });
  });

  describe('new CustomerType page', () => {
    beforeEach(() => {
      cy.visit(`${customerTypePageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('CustomerType');
    });

    it('should create an instance of CustomerType', () => {
      cy.get(`[data-cy="name"]`).type('allocation sleepily ');
      cy.get(`[data-cy="name"]`).should('have.value', 'allocation sleepily ');

      cy.get(`[data-cy="description"]`).type('weapon');
      cy.get(`[data-cy="description"]`).should('have.value', 'weapon');

      cy.get(`[data-cy="businessHandlerClazz"]`).type('mandate');
      cy.get(`[data-cy="businessHandlerClazz"]`).should('have.value', 'mandate');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        customerType = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', customerTypePageUrlPattern);
    });
  });
});
