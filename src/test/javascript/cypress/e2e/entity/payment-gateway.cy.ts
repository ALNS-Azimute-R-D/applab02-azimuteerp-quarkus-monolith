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

describe('PaymentGateway e2e test', () => {
  const paymentGatewayPageUrl = '/payment-gateway';
  const paymentGatewayPageUrlPattern = new RegExp('/payment-gateway(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const paymentGatewaySample = {
    aliasCode: 'towards pure adventu',
    description: 'juice self-reliant gazelle',
    activationStatus: 'INACTIVE',
  };

  let paymentGateway;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/payment-gateways+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/payment-gateways').as('postEntityRequest');
    cy.intercept('DELETE', '/api/payment-gateways/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (paymentGateway) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/payment-gateways/${paymentGateway.id}`,
      }).then(() => {
        paymentGateway = undefined;
      });
    }
  });

  it('PaymentGateways menu should load PaymentGateways page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('payment-gateway');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('PaymentGateway').should('exist');
    cy.url().should('match', paymentGatewayPageUrlPattern);
  });

  describe('PaymentGateway page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(paymentGatewayPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create PaymentGateway page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/payment-gateway/new$'));
        cy.getEntityCreateUpdateHeading('PaymentGateway');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', paymentGatewayPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/payment-gateways',
          body: paymentGatewaySample,
        }).then(({ body }) => {
          paymentGateway = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/payment-gateways+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/payment-gateways?page=0&size=20>; rel="last",<http://localhost/api/payment-gateways?page=0&size=20>; rel="first"',
              },
              body: [paymentGateway],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(paymentGatewayPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details PaymentGateway page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('paymentGateway');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', paymentGatewayPageUrlPattern);
      });

      it('edit button click should load edit PaymentGateway page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('PaymentGateway');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', paymentGatewayPageUrlPattern);
      });

      it('edit button click should load edit PaymentGateway page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('PaymentGateway');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', paymentGatewayPageUrlPattern);
      });

      it('last delete button click should delete instance of PaymentGateway', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('paymentGateway').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', paymentGatewayPageUrlPattern);

        paymentGateway = undefined;
      });
    });
  });

  describe('new PaymentGateway page', () => {
    beforeEach(() => {
      cy.visit(`${paymentGatewayPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('PaymentGateway');
    });

    it('should create an instance of PaymentGateway', () => {
      cy.get(`[data-cy="aliasCode"]`).type('spry');
      cy.get(`[data-cy="aliasCode"]`).should('have.value', 'spry');

      cy.get(`[data-cy="description"]`).type('duh');
      cy.get(`[data-cy="description"]`).should('have.value', 'duh');

      cy.get(`[data-cy="businessHandlerClazz"]`).type('yet military');
      cy.get(`[data-cy="businessHandlerClazz"]`).should('have.value', 'yet military');

      cy.get(`[data-cy="activationStatus"]`).select('ON_HOLD');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        paymentGateway = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', paymentGatewayPageUrlPattern);
    });
  });
});
