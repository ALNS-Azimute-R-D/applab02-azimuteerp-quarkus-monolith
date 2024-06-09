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

describe('Order e2e test', () => {
  const orderPageUrl = '/order';
  const orderPageUrlPattern = new RegExp('/order(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  // const orderSample = {"businessCode":"brr","placedDate":"2024-06-08T03:58:58.110Z","status":"COMPLETED","activationStatus":"PENDENT"};

  let order;
  // let customer;

  beforeEach(() => {
    cy.login(username, password);
  });

  /* Disabled due to incompatibility
  beforeEach(() => {
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/customers',
      body: {"customerBusinessCode":"unimpressively ","fullname":"old doss","customAttributesDetailsJSON":"er although because","customerStatus":"WORKING","activationStatus":"ACTIVE"},
    }).then(({ body }) => {
      customer = body;
    });
  });
   */

  beforeEach(() => {
    cy.intercept('GET', '/api/orders+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/orders').as('postEntityRequest');
    cy.intercept('DELETE', '/api/orders/*').as('deleteEntityRequest');
  });

  /* Disabled due to incompatibility
  beforeEach(() => {
    // Simulate relationships api for better performance and reproducibility.
    cy.intercept('GET', '/api/order-items', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/invoices', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/customers', {
      statusCode: 200,
      body: [customer],
    });

  });
   */

  afterEach(() => {
    if (order) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/orders/${order.id}`,
      }).then(() => {
        order = undefined;
      });
    }
  });

  /* Disabled due to incompatibility
  afterEach(() => {
    if (customer) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/customers/${customer.id}`,
      }).then(() => {
        customer = undefined;
      });
    }
  });
   */

  it('Orders menu should load Orders page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('order');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Order').should('exist');
    cy.url().should('match', orderPageUrlPattern);
  });

  describe('Order page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(orderPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Order page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/order/new$'));
        cy.getEntityCreateUpdateHeading('Order');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', orderPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      /* Disabled due to incompatibility
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/orders',
          body: {
            ...orderSample,
            customer: customer,
          },
        }).then(({ body }) => {
          order = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/orders+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/orders?page=0&size=20>; rel="last",<http://localhost/api/orders?page=0&size=20>; rel="first"',
              },
              body: [order],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(orderPageUrl);

        cy.wait('@entitiesRequestInternal');
      });
       */

      beforeEach(function () {
        cy.visit(orderPageUrl);

        cy.wait('@entitiesRequest').then(({ response }) => {
          if (response.body.length === 0) {
            this.skip();
          }
        });
      });

      it('detail button click should load details Order page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('order');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', orderPageUrlPattern);
      });

      it('edit button click should load edit Order page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Order');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', orderPageUrlPattern);
      });

      it('edit button click should load edit Order page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Order');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', orderPageUrlPattern);
      });

      it.skip('last delete button click should delete instance of Order', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('order').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', orderPageUrlPattern);

        order = undefined;
      });
    });
  });

  describe('new Order page', () => {
    beforeEach(() => {
      cy.visit(`${orderPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Order');
    });

    it.skip('should create an instance of Order', () => {
      cy.get(`[data-cy="businessCode"]`).type('ouch');
      cy.get(`[data-cy="businessCode"]`).should('have.value', 'ouch');

      cy.get(`[data-cy="placedDate"]`).type('2024-06-07T10:57');
      cy.get(`[data-cy="placedDate"]`).blur();
      cy.get(`[data-cy="placedDate"]`).should('have.value', '2024-06-07T10:57');

      cy.get(`[data-cy="totalTaxValue"]`).type('24298.19');
      cy.get(`[data-cy="totalTaxValue"]`).should('have.value', '24298.19');

      cy.get(`[data-cy="totalDueValue"]`).type('4836.63');
      cy.get(`[data-cy="totalDueValue"]`).should('have.value', '4836.63');

      cy.get(`[data-cy="status"]`).select('CANCELLED');

      cy.get(`[data-cy="estimatedDeliveryDate"]`).type('2024-06-08T03:19');
      cy.get(`[data-cy="estimatedDeliveryDate"]`).blur();
      cy.get(`[data-cy="estimatedDeliveryDate"]`).should('have.value', '2024-06-08T03:19');

      cy.get(`[data-cy="activationStatus"]`).select('PENDENT');

      cy.get(`[data-cy="customer"]`).select(1);

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        order = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', orderPageUrlPattern);
    });
  });
});
