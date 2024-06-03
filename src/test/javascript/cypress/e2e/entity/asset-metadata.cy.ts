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

describe('AssetMetadata e2e test', () => {
  const assetMetadataPageUrl = '/asset-metadata';
  const assetMetadataPageUrlPattern = new RegExp('/asset-metadata(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  // const assetMetadataSample = {};

  let assetMetadata;
  // let asset;

  beforeEach(() => {
    cy.login(username, password);
  });

  /* Disabled due to incompatibility
  beforeEach(() => {
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/assets',
      body: {"uid":"fe3a1a06-d9c6-4eee-b39d-869a142c8630","name":"provided gray","storageTypeUsed":"LOCAL_FILESYSTEM","fullFilenamePath":"certification hilarious gracefully","status":"DELETED","preferredPurpose":"OTHER","assetContentAsBlob":"Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci5wbmc=","assetContentAsBlobContentType":"unknown"},
    }).then(({ body }) => {
      asset = body;
    });
  });
   */

  beforeEach(() => {
    cy.intercept('GET', '/api/asset-metadata+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/asset-metadata').as('postEntityRequest');
    cy.intercept('DELETE', '/api/asset-metadata/*').as('deleteEntityRequest');
  });

  /* Disabled due to incompatibility
  beforeEach(() => {
    // Simulate relationships api for better performance and reproducibility.
    cy.intercept('GET', '/api/assets', {
      statusCode: 200,
      body: [asset],
    });

  });
   */

  afterEach(() => {
    if (assetMetadata) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/asset-metadata/${assetMetadata.id}`,
      }).then(() => {
        assetMetadata = undefined;
      });
    }
  });

  /* Disabled due to incompatibility
  afterEach(() => {
    if (asset) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/assets/${asset.id}`,
      }).then(() => {
        asset = undefined;
      });
    }
  });
   */

  it('AssetMetadata menu should load AssetMetadata page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('asset-metadata');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('AssetMetadata').should('exist');
    cy.url().should('match', assetMetadataPageUrlPattern);
  });

  describe('AssetMetadata page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(assetMetadataPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create AssetMetadata page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/asset-metadata/new$'));
        cy.getEntityCreateUpdateHeading('AssetMetadata');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', assetMetadataPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      /* Disabled due to incompatibility
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/asset-metadata',
          body: {
            ...assetMetadataSample,
            asset: asset,
          },
        }).then(({ body }) => {
          assetMetadata = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/asset-metadata+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/asset-metadata?page=0&size=20>; rel="last",<http://localhost/api/asset-metadata?page=0&size=20>; rel="first"',
              },
              body: [assetMetadata],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(assetMetadataPageUrl);

        cy.wait('@entitiesRequestInternal');
      });
       */

      beforeEach(function () {
        cy.visit(assetMetadataPageUrl);

        cy.wait('@entitiesRequest').then(({ response }) => {
          if (response.body.length === 0) {
            this.skip();
          }
        });
      });

      it('detail button click should load details AssetMetadata page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('assetMetadata');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', assetMetadataPageUrlPattern);
      });

      it('edit button click should load edit AssetMetadata page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('AssetMetadata');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', assetMetadataPageUrlPattern);
      });

      it('edit button click should load edit AssetMetadata page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('AssetMetadata');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', assetMetadataPageUrlPattern);
      });

      it.skip('last delete button click should delete instance of AssetMetadata', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('assetMetadata').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', assetMetadataPageUrlPattern);

        assetMetadata = undefined;
      });
    });
  });

  describe('new AssetMetadata page', () => {
    beforeEach(() => {
      cy.visit(`${assetMetadataPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('AssetMetadata');
    });

    it.skip('should create an instance of AssetMetadata', () => {
      cy.get(`[data-cy="metadataDetails"]`).type('../fake-data/blob/hipster.txt');
      cy.get(`[data-cy="metadataDetails"]`).invoke('val').should('match', new RegExp('../fake-data/blob/hipster.txt'));

      cy.get(`[data-cy="asset"]`).select(1);

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        assetMetadata = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', assetMetadataPageUrlPattern);
    });
  });
});
