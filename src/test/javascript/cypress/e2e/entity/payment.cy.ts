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

describe('Payment e2e test', () => {
  const paymentPageUrl = '/payment';
  const paymentPageUrlPattern = new RegExp('/payment(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const paymentSample = {
    installmentNumber: 2765,
    paymentDueDate: '2024-06-07T14:46:42.482Z',
    paymentPaidDate: '2024-06-07T13:29:01.558Z',
    paymentAmount: 4849.56,
    typeOfPayment: 'CREDIT',
    statusPayment: 'PAID',
    activationStatus: 'INVALID',
  };

  let payment;
  let paymentGateway;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/payment-gateways',
      body: { aliasCode: 'only', description: 'ha', businessHandlerClazz: 'inasmuch', activationStatus: 'PENDENT' },
    }).then(({ body }) => {
      paymentGateway = body;
    });
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/payments+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/payments').as('postEntityRequest');
    cy.intercept('DELETE', '/api/payments/*').as('deleteEntityRequest');
  });

  beforeEach(() => {
    // Simulate relationships api for better performance and reproducibility.
    cy.intercept('GET', '/api/payment-gateways', {
      statusCode: 200,
      body: [paymentGateway],
    });
  });

  afterEach(() => {
    if (payment) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/payments/${payment.id}`,
      }).then(() => {
        payment = undefined;
      });
    }
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

  it('Payments menu should load Payments page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('payment');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Payment').should('exist');
    cy.url().should('match', paymentPageUrlPattern);
  });

  describe('Payment page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(paymentPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Payment page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/payment/new$'));
        cy.getEntityCreateUpdateHeading('Payment');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', paymentPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/payments',
          body: {
            ...paymentSample,
            paymentGateway: paymentGateway,
          },
        }).then(({ body }) => {
          payment = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/payments+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/payments?page=0&size=20>; rel="last",<http://localhost/api/payments?page=0&size=20>; rel="first"',
              },
              body: [payment],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(paymentPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Payment page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('payment');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', paymentPageUrlPattern);
      });

      it('edit button click should load edit Payment page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Payment');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', paymentPageUrlPattern);
      });

      it('edit button click should load edit Payment page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Payment');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', paymentPageUrlPattern);
      });

      it('last delete button click should delete instance of Payment', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('payment').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', paymentPageUrlPattern);

        payment = undefined;
      });
    });
  });

  describe('new Payment page', () => {
    beforeEach(() => {
      cy.visit(`${paymentPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Payment');
    });

    it('should create an instance of Payment', () => {
      cy.get(`[data-cy="installmentNumber"]`).type('12536');
      cy.get(`[data-cy="installmentNumber"]`).should('have.value', '12536');

      cy.get(`[data-cy="paymentDueDate"]`).type('2024-06-07T16:02');
      cy.get(`[data-cy="paymentDueDate"]`).blur();
      cy.get(`[data-cy="paymentDueDate"]`).should('have.value', '2024-06-07T16:02');

      cy.get(`[data-cy="paymentPaidDate"]`).type('2024-06-07T22:11');
      cy.get(`[data-cy="paymentPaidDate"]`).blur();
      cy.get(`[data-cy="paymentPaidDate"]`).should('have.value', '2024-06-07T22:11');

      cy.get(`[data-cy="paymentAmount"]`).type('13506.51');
      cy.get(`[data-cy="paymentAmount"]`).should('have.value', '13506.51');

      cy.get(`[data-cy="typeOfPayment"]`).select('DEBIT');

      cy.get(`[data-cy="statusPayment"]`).select('CANCELLED');

      cy.get(`[data-cy="customAttributesDetailsJSON"]`).type('um');
      cy.get(`[data-cy="customAttributesDetailsJSON"]`).should('have.value', 'um');

      cy.get(`[data-cy="activationStatus"]`).select('PENDENT');

      cy.get(`[data-cy="paymentGateway"]`).select(1);

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        payment = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', paymentPageUrlPattern);
    });
  });
});
