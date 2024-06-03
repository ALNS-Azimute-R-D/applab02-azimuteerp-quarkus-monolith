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

describe('TypeOfOrganization e2e test', () => {
  const typeOfOrganizationPageUrl = '/type-of-organization';
  const typeOfOrganizationPageUrlPattern = new RegExp('/type-of-organization(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const typeOfOrganizationSample = {
    acronym: 'but jackknife',
    name: 'conceal midst',
    description: 'Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci50eHQ=',
  };

  let typeOfOrganization;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/type-of-organizations+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/type-of-organizations').as('postEntityRequest');
    cy.intercept('DELETE', '/api/type-of-organizations/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (typeOfOrganization) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/type-of-organizations/${typeOfOrganization.id}`,
      }).then(() => {
        typeOfOrganization = undefined;
      });
    }
  });

  it('TypeOfOrganizations menu should load TypeOfOrganizations page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('type-of-organization');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('TypeOfOrganization').should('exist');
    cy.url().should('match', typeOfOrganizationPageUrlPattern);
  });

  describe('TypeOfOrganization page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(typeOfOrganizationPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create TypeOfOrganization page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/type-of-organization/new$'));
        cy.getEntityCreateUpdateHeading('TypeOfOrganization');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', typeOfOrganizationPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/type-of-organizations',
          body: typeOfOrganizationSample,
        }).then(({ body }) => {
          typeOfOrganization = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/type-of-organizations+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/type-of-organizations?page=0&size=20>; rel="last",<http://localhost/api/type-of-organizations?page=0&size=20>; rel="first"',
              },
              body: [typeOfOrganization],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(typeOfOrganizationPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details TypeOfOrganization page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('typeOfOrganization');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', typeOfOrganizationPageUrlPattern);
      });

      it('edit button click should load edit TypeOfOrganization page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('TypeOfOrganization');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', typeOfOrganizationPageUrlPattern);
      });

      it('edit button click should load edit TypeOfOrganization page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('TypeOfOrganization');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', typeOfOrganizationPageUrlPattern);
      });

      it('last delete button click should delete instance of TypeOfOrganization', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('typeOfOrganization').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', typeOfOrganizationPageUrlPattern);

        typeOfOrganization = undefined;
      });
    });
  });

  describe('new TypeOfOrganization page', () => {
    beforeEach(() => {
      cy.visit(`${typeOfOrganizationPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('TypeOfOrganization');
    });

    it('should create an instance of TypeOfOrganization', () => {
      cy.get(`[data-cy="acronym"]`).type('hopelessly apse');
      cy.get(`[data-cy="acronym"]`).should('have.value', 'hopelessly apse');

      cy.get(`[data-cy="name"]`).type('onto beneath');
      cy.get(`[data-cy="name"]`).should('have.value', 'onto beneath');

      cy.get(`[data-cy="description"]`).type('../fake-data/blob/hipster.txt');
      cy.get(`[data-cy="description"]`).invoke('val').should('match', new RegExp('../fake-data/blob/hipster.txt'));

      cy.get(`[data-cy="businessHandlerClazz"]`).type('pro');
      cy.get(`[data-cy="businessHandlerClazz"]`).should('have.value', 'pro');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        typeOfOrganization = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', typeOfOrganizationPageUrlPattern);
    });
  });
});
