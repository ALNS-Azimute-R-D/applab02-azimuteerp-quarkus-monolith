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

describe('InventoryTransaction e2e test', () => {
  const inventoryTransactionPageUrl = '/inventory-transaction';
  const inventoryTransactionPageUrlPattern = new RegExp('/inventory-transaction(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  // const inventoryTransactionSample = {"invoiceId":10021,"quantity":30416,"activationStatus":"BLOCKED"};

  let inventoryTransaction;
  // let supplier;
  // let product;
  // let warehouse;

  beforeEach(() => {
    cy.login(username, password);
  });

  /* Disabled due to incompatibility
  beforeEach(() => {
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/suppliers',
      body: {"acronym":"brr","companyName":"a though","streetAddress":"origin segment","houseNumber":"wetly whoever incomp","locationName":"lean excepting","city":"San Marcos","stateProvince":"including after enchanted","zipPostalCode":"rim because","countryRegion":"briskly sluice","pointLocation":"Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci5wbmc=","pointLocationContentType":"unknown","mainEmail":"H@\"\\G.^7","phoneNumber1":"save hollow psy","phoneNumber2":"whenever anneal","customAttributesDetailsJSON":"under classmate till","activationStatus":"INVALID"},
    }).then(({ body }) => {
      supplier = body;
    });
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/products',
      body: {"productSKU":"mmm with however","productName":"in colorfully annual","description":"psst yet hurdle","standardCost":31316.13,"listPrice":19833.79,"reorderLevel":25230,"targetLevel":20657,"quantityPerUnit":"a cluttered sympathetically","discontinued":false,"minimumReorderQuantity":22182,"suggestedCategory":"times um","attachments":"Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci5wbmc=","attachmentsContentType":"unknown","activationStatus":"ON_HOLD"},
    }).then(({ body }) => {
      product = body;
    });
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/warehouses',
      body: {"acronym":"klap militarize","name":"plume wisely lie","description":"fooey","streetAddress":"revoke bully impressionable","houseNumber":"why","locationName":"brazen difficult strap","postalCode":"gah joint","pointLocation":"Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci5wbmc=","pointLocationContentType":"unknown","mainEmail":"~@N$H.g","landPhoneNumber":"who","mobilePhoneNumber":"creep prune","faxNumber":"times foolish s","customAttributesDetailsJSON":"minimize devoice fortunately","activationStatus":"BLOCKED"},
    }).then(({ body }) => {
      warehouse = body;
    });
  });
   */

  beforeEach(() => {
    cy.intercept('GET', '/api/inventory-transactions+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/inventory-transactions').as('postEntityRequest');
    cy.intercept('DELETE', '/api/inventory-transactions/*').as('deleteEntityRequest');
  });

  /* Disabled due to incompatibility
  beforeEach(() => {
    // Simulate relationships api for better performance and reproducibility.
    cy.intercept('GET', '/api/suppliers', {
      statusCode: 200,
      body: [supplier],
    });

    cy.intercept('GET', '/api/products', {
      statusCode: 200,
      body: [product],
    });

    cy.intercept('GET', '/api/warehouses', {
      statusCode: 200,
      body: [warehouse],
    });

  });
   */

  afterEach(() => {
    if (inventoryTransaction) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/inventory-transactions/${inventoryTransaction.id}`,
      }).then(() => {
        inventoryTransaction = undefined;
      });
    }
  });

  /* Disabled due to incompatibility
  afterEach(() => {
    if (supplier) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/suppliers/${supplier.id}`,
      }).then(() => {
        supplier = undefined;
      });
    }
    if (product) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/products/${product.id}`,
      }).then(() => {
        product = undefined;
      });
    }
    if (warehouse) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/warehouses/${warehouse.id}`,
      }).then(() => {
        warehouse = undefined;
      });
    }
  });
   */

  it('InventoryTransactions menu should load InventoryTransactions page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('inventory-transaction');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('InventoryTransaction').should('exist');
    cy.url().should('match', inventoryTransactionPageUrlPattern);
  });

  describe('InventoryTransaction page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(inventoryTransactionPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create InventoryTransaction page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/inventory-transaction/new$'));
        cy.getEntityCreateUpdateHeading('InventoryTransaction');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', inventoryTransactionPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      /* Disabled due to incompatibility
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/inventory-transactions',
          body: {
            ...inventoryTransactionSample,
            supplier: supplier,
            product: product,
            warehouse: warehouse,
          },
        }).then(({ body }) => {
          inventoryTransaction = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/inventory-transactions+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/inventory-transactions?page=0&size=20>; rel="last",<http://localhost/api/inventory-transactions?page=0&size=20>; rel="first"',
              },
              body: [inventoryTransaction],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(inventoryTransactionPageUrl);

        cy.wait('@entitiesRequestInternal');
      });
       */

      beforeEach(function () {
        cy.visit(inventoryTransactionPageUrl);

        cy.wait('@entitiesRequest').then(({ response }) => {
          if (response.body.length === 0) {
            this.skip();
          }
        });
      });

      it('detail button click should load details InventoryTransaction page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('inventoryTransaction');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', inventoryTransactionPageUrlPattern);
      });

      it('edit button click should load edit InventoryTransaction page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('InventoryTransaction');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', inventoryTransactionPageUrlPattern);
      });

      it('edit button click should load edit InventoryTransaction page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('InventoryTransaction');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', inventoryTransactionPageUrlPattern);
      });

      it.skip('last delete button click should delete instance of InventoryTransaction', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('inventoryTransaction').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', inventoryTransactionPageUrlPattern);

        inventoryTransaction = undefined;
      });
    });
  });

  describe('new InventoryTransaction page', () => {
    beforeEach(() => {
      cy.visit(`${inventoryTransactionPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('InventoryTransaction');
    });

    it.skip('should create an instance of InventoryTransaction', () => {
      cy.get(`[data-cy="invoiceId"]`).type('7631');
      cy.get(`[data-cy="invoiceId"]`).should('have.value', '7631');

      cy.get(`[data-cy="transactionCreatedDate"]`).type('2024-06-07T12:03');
      cy.get(`[data-cy="transactionCreatedDate"]`).blur();
      cy.get(`[data-cy="transactionCreatedDate"]`).should('have.value', '2024-06-07T12:03');

      cy.get(`[data-cy="transactionModifiedDate"]`).type('2024-06-08T06:32');
      cy.get(`[data-cy="transactionModifiedDate"]`).blur();
      cy.get(`[data-cy="transactionModifiedDate"]`).should('have.value', '2024-06-08T06:32');

      cy.get(`[data-cy="quantity"]`).type('11670');
      cy.get(`[data-cy="quantity"]`).should('have.value', '11670');

      cy.get(`[data-cy="transactionComments"]`).type('unexpectedly');
      cy.get(`[data-cy="transactionComments"]`).should('have.value', 'unexpectedly');

      cy.get(`[data-cy="activationStatus"]`).select('PENDENT');

      cy.get(`[data-cy="supplier"]`).select(1);
      cy.get(`[data-cy="product"]`).select(1);
      cy.get(`[data-cy="warehouse"]`).select(1);

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        inventoryTransaction = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', inventoryTransactionPageUrlPattern);
    });
  });
});
