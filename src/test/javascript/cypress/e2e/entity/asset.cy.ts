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

describe('Asset e2e test', () => {
  const assetPageUrl = '/asset';
  const assetPageUrlPattern = new RegExp('/asset(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const assetSample = { name: 'dimly trellis', activationStatus: 'PENDENT' };

  let asset;
  let assetType;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/asset-types',
      body: {
        acronym: 'skill',
        name: 'before amid subcontract',
        description: 'ha',
        handlerClazzName: 'consequently',
        customAttributesDetailsJSON: 'factor aha',
      },
    }).then(({ body }) => {
      assetType = body;
    });
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/assets+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/assets').as('postEntityRequest');
    cy.intercept('DELETE', '/api/assets/*').as('deleteEntityRequest');
  });

  beforeEach(() => {
    // Simulate relationships api for better performance and reproducibility.
    cy.intercept('GET', '/api/asset-types', {
      statusCode: 200,
      body: [assetType],
    });

    cy.intercept('GET', '/api/raw-asset-proc-tmps', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/asset-metadata', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/asset-collections', {
      statusCode: 200,
      body: [],
    });
  });

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

  it('Assets menu should load Assets page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('asset');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Asset').should('exist');
    cy.url().should('match', assetPageUrlPattern);
  });

  describe('Asset page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(assetPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Asset page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/asset/new$'));
        cy.getEntityCreateUpdateHeading('Asset');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', assetPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/assets',
          body: {
            ...assetSample,
            assetType: assetType,
          },
        }).then(({ body }) => {
          asset = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/assets+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/assets?page=0&size=20>; rel="last",<http://localhost/api/assets?page=0&size=20>; rel="first"',
              },
              body: [asset],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(assetPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Asset page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('asset');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', assetPageUrlPattern);
      });

      it('edit button click should load edit Asset page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Asset');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', assetPageUrlPattern);
      });

      it('edit button click should load edit Asset page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Asset');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', assetPageUrlPattern);
      });

      it('last delete button click should delete instance of Asset', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('asset').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', assetPageUrlPattern);

        asset = undefined;
      });
    });
  });

  describe('new Asset page', () => {
    beforeEach(() => {
      cy.visit(`${assetPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Asset');
    });

    it('should create an instance of Asset', () => {
      cy.get(`[data-cy="name"]`).type('hostess more inside');
      cy.get(`[data-cy="name"]`).should('have.value', 'hostess more inside');

      cy.get(`[data-cy="storageTypeUsed"]`).select('OTHER_CLOUD_STORAGE');

      cy.get(`[data-cy="fullFilenamePath"]`).type('dearest wrong');
      cy.get(`[data-cy="fullFilenamePath"]`).should('have.value', 'dearest wrong');

      cy.get(`[data-cy="status"]`).select('DELETED');

      cy.get(`[data-cy="preferredPurpose"]`).select('PHOTO_ALBUM');

      cy.setFieldImageAsBytesOfEntity('assetContentAsBlob', 'integration-test.png', 'image/png');

      cy.get(`[data-cy="activationStatus"]`).select('PENDENT');

      cy.get(`[data-cy="assetType"]`).select(1);

      // since cypress clicks submit too fast before the blob fields are validated
      cy.wait(200); // eslint-disable-line cypress/no-unnecessary-waiting
      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        asset = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', assetPageUrlPattern);
    });
  });
});
