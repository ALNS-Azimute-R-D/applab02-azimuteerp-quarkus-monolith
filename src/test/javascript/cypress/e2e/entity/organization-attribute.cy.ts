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

describe('OrganizationAttribute e2e test', () => {
  const organizationAttributePageUrl = '/organization-attribute';
  const organizationAttributePageUrlPattern = new RegExp('/organization-attribute(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  // const organizationAttributeSample = {};

  let organizationAttribute;
  // let organization;

  beforeEach(() => {
    cy.login(username, password);
  });

  /* Disabled due to incompatibility
  beforeEach(() => {
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/organizations',
      body: {"acronym":"discrepancy giddy in","businessCode":"stupefy restful","hierarchicalLevel":"on","name":"row","description":"save","businessHandlerClazz":"dart water gratefully","mainContactPersonDetails":"Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci50eHQ=","technicalEnvironmentsDetails":"Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci50eHQ=","commonCustomAttributesDetails":"Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci50eHQ=","organizationStatus":"READY_TO_START","activationStatus":"INVALID","logoImg":"Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci5wbmc=","logoImgContentType":"unknown"},
    }).then(({ body }) => {
      organization = body;
    });
  });
   */

  beforeEach(() => {
    cy.intercept('GET', '/api/organization-attributes+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/organization-attributes').as('postEntityRequest');
    cy.intercept('DELETE', '/api/organization-attributes/*').as('deleteEntityRequest');
  });

  /* Disabled due to incompatibility
  beforeEach(() => {
    // Simulate relationships api for better performance and reproducibility.
    cy.intercept('GET', '/api/organizations', {
      statusCode: 200,
      body: [organization],
    });

  });
   */

  afterEach(() => {
    if (organizationAttribute) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/organization-attributes/${organizationAttribute.id}`,
      }).then(() => {
        organizationAttribute = undefined;
      });
    }
  });

  /* Disabled due to incompatibility
  afterEach(() => {
    if (organization) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/organizations/${organization.id}`,
      }).then(() => {
        organization = undefined;
      });
    }
  });
   */

  it('OrganizationAttributes menu should load OrganizationAttributes page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('organization-attribute');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('OrganizationAttribute').should('exist');
    cy.url().should('match', organizationAttributePageUrlPattern);
  });

  describe('OrganizationAttribute page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(organizationAttributePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create OrganizationAttribute page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/organization-attribute/new$'));
        cy.getEntityCreateUpdateHeading('OrganizationAttribute');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', organizationAttributePageUrlPattern);
      });
    });

    describe('with existing value', () => {
      /* Disabled due to incompatibility
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/organization-attributes',
          body: {
            ...organizationAttributeSample,
            organization: organization,
          },
        }).then(({ body }) => {
          organizationAttribute = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/organization-attributes+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/organization-attributes?page=0&size=20>; rel="last",<http://localhost/api/organization-attributes?page=0&size=20>; rel="first"',
              },
              body: [organizationAttribute],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(organizationAttributePageUrl);

        cy.wait('@entitiesRequestInternal');
      });
       */

      beforeEach(function () {
        cy.visit(organizationAttributePageUrl);

        cy.wait('@entitiesRequest').then(({ response }) => {
          if (response.body.length === 0) {
            this.skip();
          }
        });
      });

      it('detail button click should load details OrganizationAttribute page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('organizationAttribute');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', organizationAttributePageUrlPattern);
      });

      it('edit button click should load edit OrganizationAttribute page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('OrganizationAttribute');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', organizationAttributePageUrlPattern);
      });

      it('edit button click should load edit OrganizationAttribute page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('OrganizationAttribute');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', organizationAttributePageUrlPattern);
      });

      it.skip('last delete button click should delete instance of OrganizationAttribute', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('organizationAttribute').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', organizationAttributePageUrlPattern);

        organizationAttribute = undefined;
      });
    });
  });

  describe('new OrganizationAttribute page', () => {
    beforeEach(() => {
      cy.visit(`${organizationAttributePageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('OrganizationAttribute');
    });

    it.skip('should create an instance of OrganizationAttribute', () => {
      cy.get(`[data-cy="attributeName"]`).type('around');
      cy.get(`[data-cy="attributeName"]`).should('have.value', 'around');

      cy.get(`[data-cy="attributeValue"]`).type('brr mmm');
      cy.get(`[data-cy="attributeValue"]`).should('have.value', 'brr mmm');

      cy.get(`[data-cy="organization"]`).select(1);

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        organizationAttribute = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', organizationAttributePageUrlPattern);
    });
  });
});
