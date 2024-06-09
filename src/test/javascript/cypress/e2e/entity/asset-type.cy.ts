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

describe('AssetType e2e test', () => {
  const assetTypePageUrl = '/asset-type';
  const assetTypePageUrlPattern = new RegExp('/asset-type(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const assetTypeSample = { name: 'kettle' };

  let assetType;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/asset-types+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/asset-types').as('postEntityRequest');
    cy.intercept('DELETE', '/api/asset-types/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (assetType) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/asset-types/${assetType.id}`,
      }).then(() => {
        assetType = undefined;
      });
    }
  });

  it('AssetTypes menu should load AssetTypes page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('asset-type');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('AssetType').should('exist');
    cy.url().should('match', assetTypePageUrlPattern);
  });

  describe('AssetType page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(assetTypePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create AssetType page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/asset-type/new$'));
        cy.getEntityCreateUpdateHeading('AssetType');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', assetTypePageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/asset-types',
          body: assetTypeSample,
        }).then(({ body }) => {
          assetType = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/asset-types+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/asset-types?page=0&size=20>; rel="last",<http://localhost/api/asset-types?page=0&size=20>; rel="first"',
              },
              body: [assetType],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(assetTypePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details AssetType page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('assetType');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', assetTypePageUrlPattern);
      });

      it('edit button click should load edit AssetType page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('AssetType');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', assetTypePageUrlPattern);
      });

      it('edit button click should load edit AssetType page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('AssetType');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', assetTypePageUrlPattern);
      });

      it('last delete button click should delete instance of AssetType', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('assetType').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', assetTypePageUrlPattern);

        assetType = undefined;
      });
    });
  });

  describe('new AssetType page', () => {
    beforeEach(() => {
      cy.visit(`${assetTypePageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('AssetType');
    });

    it('should create an instance of AssetType', () => {
      cy.get(`[data-cy="acronym"]`).type('even');
      cy.get(`[data-cy="acronym"]`).should('have.value', 'even');

      cy.get(`[data-cy="name"]`).type('athwart during');
      cy.get(`[data-cy="name"]`).should('have.value', 'athwart during');

      cy.get(`[data-cy="description"]`).type('beneficial');
      cy.get(`[data-cy="description"]`).should('have.value', 'beneficial');

      cy.get(`[data-cy="handlerClazzName"]`).type('excitedly yearningly ack');
      cy.get(`[data-cy="handlerClazzName"]`).should('have.value', 'excitedly yearningly ack');

      cy.get(`[data-cy="customAttributesDetailsJSON"]`).type('visa spiteful');
      cy.get(`[data-cy="customAttributesDetailsJSON"]`).should('have.value', 'visa spiteful');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        assetType = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', assetTypePageUrlPattern);
    });
  });
});
