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

describe('StockLevel e2e test', () => {
  const stockLevelPageUrl = '/stock-level';
  const stockLevelPageUrlPattern = new RegExp('/stock-level(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  // const stockLevelSample = {"lastModifiedDate":"2024-06-08T03:37:46.402Z","remainingQuantity":31293};

  let stockLevel;
  // let warehouse;
  // let product;

  beforeEach(() => {
    cy.login(username, password);
  });

  /* Disabled due to incompatibility
  beforeEach(() => {
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/warehouses',
      body: {"acronym":"after troubled opposite","name":"above duh","description":"youngster scarily hmph","streetAddress":"incidentally reprocess","houseNumber":"along yuck more","locationName":"but gah geez","postalCode":"cohere ex","pointLocation":"Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci5wbmc=","pointLocationContentType":"unknown","mainEmail":"w:i9V@M4.D=Vr","landPhoneNumber":"whip plus gadzo","mobilePhoneNumber":"besides","faxNumber":"furthermore","customAttributesDetailsJSON":"offer","activationStatus":"PENDENT"},
    }).then(({ body }) => {
      warehouse = body;
    });
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/products',
      body: {"productSKU":"tracking","productName":"hollow drool hotdog","description":"document","standardCost":7211.79,"listPrice":4492.61,"reorderLevel":19262,"targetLevel":14584,"quantityPerUnit":"er","discontinued":false,"minimumReorderQuantity":29190,"suggestedCategory":"ouch hourly precious","attachments":"Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci5wbmc=","attachmentsContentType":"unknown","activationStatus":"INVALID"},
    }).then(({ body }) => {
      product = body;
    });
  });
   */

  beforeEach(() => {
    cy.intercept('GET', '/api/stock-levels+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/stock-levels').as('postEntityRequest');
    cy.intercept('DELETE', '/api/stock-levels/*').as('deleteEntityRequest');
  });

  /* Disabled due to incompatibility
  beforeEach(() => {
    // Simulate relationships api for better performance and reproducibility.
    cy.intercept('GET', '/api/warehouses', {
      statusCode: 200,
      body: [warehouse],
    });

    cy.intercept('GET', '/api/products', {
      statusCode: 200,
      body: [product],
    });

  });
   */

  afterEach(() => {
    if (stockLevel) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/stock-levels/${stockLevel.id}`,
      }).then(() => {
        stockLevel = undefined;
      });
    }
  });

  /* Disabled due to incompatibility
  afterEach(() => {
    if (warehouse) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/warehouses/${warehouse.id}`,
      }).then(() => {
        warehouse = undefined;
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
  });
   */

  it('StockLevels menu should load StockLevels page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('stock-level');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('StockLevel').should('exist');
    cy.url().should('match', stockLevelPageUrlPattern);
  });

  describe('StockLevel page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(stockLevelPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create StockLevel page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/stock-level/new$'));
        cy.getEntityCreateUpdateHeading('StockLevel');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', stockLevelPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      /* Disabled due to incompatibility
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/stock-levels',
          body: {
            ...stockLevelSample,
            warehouse: warehouse,
            product: product,
          },
        }).then(({ body }) => {
          stockLevel = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/stock-levels+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/stock-levels?page=0&size=20>; rel="last",<http://localhost/api/stock-levels?page=0&size=20>; rel="first"',
              },
              body: [stockLevel],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(stockLevelPageUrl);

        cy.wait('@entitiesRequestInternal');
      });
       */

      beforeEach(function () {
        cy.visit(stockLevelPageUrl);

        cy.wait('@entitiesRequest').then(({ response }) => {
          if (response.body.length === 0) {
            this.skip();
          }
        });
      });

      it('detail button click should load details StockLevel page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('stockLevel');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', stockLevelPageUrlPattern);
      });

      it('edit button click should load edit StockLevel page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('StockLevel');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', stockLevelPageUrlPattern);
      });

      it('edit button click should load edit StockLevel page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('StockLevel');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', stockLevelPageUrlPattern);
      });

      it.skip('last delete button click should delete instance of StockLevel', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('stockLevel').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', stockLevelPageUrlPattern);

        stockLevel = undefined;
      });
    });
  });

  describe('new StockLevel page', () => {
    beforeEach(() => {
      cy.visit(`${stockLevelPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('StockLevel');
    });

    it.skip('should create an instance of StockLevel', () => {
      cy.get(`[data-cy="lastModifiedDate"]`).type('2024-06-08T00:41');
      cy.get(`[data-cy="lastModifiedDate"]`).blur();
      cy.get(`[data-cy="lastModifiedDate"]`).should('have.value', '2024-06-08T00:41');

      cy.get(`[data-cy="remainingQuantity"]`).type('17017');
      cy.get(`[data-cy="remainingQuantity"]`).should('have.value', '17017');

      cy.get(`[data-cy="commonAttributesDetailsJSON"]`).type('where before');
      cy.get(`[data-cy="commonAttributesDetailsJSON"]`).should('have.value', 'where before');

      cy.get(`[data-cy="warehouse"]`).select(1);
      cy.get(`[data-cy="product"]`).select(1);

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        stockLevel = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', stockLevelPageUrlPattern);
    });
  });
});
