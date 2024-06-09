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

describe('RawAssetProcTmp e2e test', () => {
  const rawAssetProcTmpPageUrl = '/raw-asset-proc-tmp';
  const rawAssetProcTmpPageUrlPattern = new RegExp('/raw-asset-proc-tmp(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const rawAssetProcTmpSample = { name: 'ick reset' };

  let rawAssetProcTmp;
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
        acronym: 'tambour brr',
        name: 'forenenst ick',
        description: 'officially weak',
        handlerClazzName: 'gee',
        customAttributesDetailsJSON: 'bath contemplate',
      },
    }).then(({ body }) => {
      assetType = body;
    });
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/raw-asset-proc-tmps+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/raw-asset-proc-tmps').as('postEntityRequest');
    cy.intercept('DELETE', '/api/raw-asset-proc-tmps/*').as('deleteEntityRequest');
  });

  beforeEach(() => {
    // Simulate relationships api for better performance and reproducibility.
    cy.intercept('GET', '/api/asset-types', {
      statusCode: 200,
      body: [assetType],
    });

    cy.intercept('GET', '/api/assets', {
      statusCode: 200,
      body: [],
    });
  });

  afterEach(() => {
    if (rawAssetProcTmp) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/raw-asset-proc-tmps/${rawAssetProcTmp.id}`,
      }).then(() => {
        rawAssetProcTmp = undefined;
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

  it('RawAssetProcTmps menu should load RawAssetProcTmps page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('raw-asset-proc-tmp');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('RawAssetProcTmp').should('exist');
    cy.url().should('match', rawAssetProcTmpPageUrlPattern);
  });

  describe('RawAssetProcTmp page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(rawAssetProcTmpPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create RawAssetProcTmp page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/raw-asset-proc-tmp/new$'));
        cy.getEntityCreateUpdateHeading('RawAssetProcTmp');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', rawAssetProcTmpPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/raw-asset-proc-tmps',
          body: {
            ...rawAssetProcTmpSample,
            assetType: assetType,
          },
        }).then(({ body }) => {
          rawAssetProcTmp = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/raw-asset-proc-tmps+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/raw-asset-proc-tmps?page=0&size=20>; rel="last",<http://localhost/api/raw-asset-proc-tmps?page=0&size=20>; rel="first"',
              },
              body: [rawAssetProcTmp],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(rawAssetProcTmpPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details RawAssetProcTmp page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('rawAssetProcTmp');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', rawAssetProcTmpPageUrlPattern);
      });

      it('edit button click should load edit RawAssetProcTmp page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('RawAssetProcTmp');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', rawAssetProcTmpPageUrlPattern);
      });

      it('edit button click should load edit RawAssetProcTmp page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('RawAssetProcTmp');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', rawAssetProcTmpPageUrlPattern);
      });

      it('last delete button click should delete instance of RawAssetProcTmp', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('rawAssetProcTmp').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', rawAssetProcTmpPageUrlPattern);

        rawAssetProcTmp = undefined;
      });
    });
  });

  describe('new RawAssetProcTmp page', () => {
    beforeEach(() => {
      cy.visit(`${rawAssetProcTmpPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('RawAssetProcTmp');
    });

    it('should create an instance of RawAssetProcTmp', () => {
      cy.get(`[data-cy="name"]`).type('fluster');
      cy.get(`[data-cy="name"]`).should('have.value', 'fluster');

      cy.get(`[data-cy="statusRawProcessing"]`).select('PROCESSING');

      cy.get(`[data-cy="fullFilenamePath"]`).type('yet');
      cy.get(`[data-cy="fullFilenamePath"]`).should('have.value', 'yet');

      cy.setFieldImageAsBytesOfEntity('assetRawContentAsBlob', 'integration-test.png', 'image/png');

      cy.get(`[data-cy="customAttributesDetailsJSON"]`).type('anglicize except indeed');
      cy.get(`[data-cy="customAttributesDetailsJSON"]`).should('have.value', 'anglicize except indeed');

      cy.get(`[data-cy="assetType"]`).select(1);

      // since cypress clicks submit too fast before the blob fields are validated
      cy.wait(200); // eslint-disable-line cypress/no-unnecessary-waiting
      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        rawAssetProcTmp = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', rawAssetProcTmpPageUrlPattern);
    });
  });
});
