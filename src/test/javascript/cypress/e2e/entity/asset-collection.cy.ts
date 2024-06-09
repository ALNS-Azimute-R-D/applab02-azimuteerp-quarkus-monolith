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

describe('AssetCollection e2e test', () => {
  const assetCollectionPageUrl = '/asset-collection';
  const assetCollectionPageUrlPattern = new RegExp('/asset-collection(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const assetCollectionSample = { name: 'twist er knotty', activationStatus: 'ACTIVE' };

  let assetCollection;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/asset-collections+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/asset-collections').as('postEntityRequest');
    cy.intercept('DELETE', '/api/asset-collections/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (assetCollection) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/asset-collections/${assetCollection.id}`,
      }).then(() => {
        assetCollection = undefined;
      });
    }
  });

  it('AssetCollections menu should load AssetCollections page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('asset-collection');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('AssetCollection').should('exist');
    cy.url().should('match', assetCollectionPageUrlPattern);
  });

  describe('AssetCollection page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(assetCollectionPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create AssetCollection page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/asset-collection/new$'));
        cy.getEntityCreateUpdateHeading('AssetCollection');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', assetCollectionPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/asset-collections',
          body: assetCollectionSample,
        }).then(({ body }) => {
          assetCollection = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/asset-collections+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/asset-collections?page=0&size=20>; rel="last",<http://localhost/api/asset-collections?page=0&size=20>; rel="first"',
              },
              body: [assetCollection],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(assetCollectionPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details AssetCollection page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('assetCollection');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', assetCollectionPageUrlPattern);
      });

      it('edit button click should load edit AssetCollection page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('AssetCollection');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', assetCollectionPageUrlPattern);
      });

      it('edit button click should load edit AssetCollection page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('AssetCollection');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', assetCollectionPageUrlPattern);
      });

      it('last delete button click should delete instance of AssetCollection', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('assetCollection').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', assetCollectionPageUrlPattern);

        assetCollection = undefined;
      });
    });
  });

  describe('new AssetCollection page', () => {
    beforeEach(() => {
      cy.visit(`${assetCollectionPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('AssetCollection');
    });

    it('should create an instance of AssetCollection', () => {
      cy.get(`[data-cy="name"]`).type('healthily butcher instead');
      cy.get(`[data-cy="name"]`).should('have.value', 'healthily butcher instead');

      cy.get(`[data-cy="fullFilenamePath"]`).type('unconscious difference');
      cy.get(`[data-cy="fullFilenamePath"]`).should('have.value', 'unconscious difference');

      cy.get(`[data-cy="activationStatus"]`).select('INVALID');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        assetCollection = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', assetCollectionPageUrlPattern);
    });
  });
});
