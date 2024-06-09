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

describe('OrganizationDomain e2e test', () => {
  const organizationDomainPageUrl = '/organization-domain';
  const organizationDomainPageUrlPattern = new RegExp('/organization-domain(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  // const organizationDomainSample = {"domainAcronym":"actually hmph massage","name":"geez ack","isVerified":true,"activationStatus":"PENDENT"};

  let organizationDomain;
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
      body: {"acronym":"automate sharp badly","businessCode":"denude row","hierarchicalLevel":"save","name":"dart water gratefully","description":"regal sharply","businessHandlerClazz":"fooey wick kindly","mainContactPersonDetailsJSON":"although","technicalEnvironmentsDetailsJSON":"sag accent","customAttributesDetailsJSON":"considering trading","organizationStatus":"IN_FAILURE","activationStatus":"ON_HOLD","logoImg":"Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci5wbmc=","logoImgContentType":"unknown"},
    }).then(({ body }) => {
      organization = body;
    });
  });
   */

  beforeEach(() => {
    cy.intercept('GET', '/api/organization-domains+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/organization-domains').as('postEntityRequest');
    cy.intercept('DELETE', '/api/organization-domains/*').as('deleteEntityRequest');
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
    if (organizationDomain) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/organization-domains/${organizationDomain.id}`,
      }).then(() => {
        organizationDomain = undefined;
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

  it('OrganizationDomains menu should load OrganizationDomains page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('organization-domain');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('OrganizationDomain').should('exist');
    cy.url().should('match', organizationDomainPageUrlPattern);
  });

  describe('OrganizationDomain page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(organizationDomainPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create OrganizationDomain page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/organization-domain/new$'));
        cy.getEntityCreateUpdateHeading('OrganizationDomain');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', organizationDomainPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      /* Disabled due to incompatibility
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/organization-domains',
          body: {
            ...organizationDomainSample,
            organization: organization,
          },
        }).then(({ body }) => {
          organizationDomain = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/organization-domains+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/organization-domains?page=0&size=20>; rel="last",<http://localhost/api/organization-domains?page=0&size=20>; rel="first"',
              },
              body: [organizationDomain],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(organizationDomainPageUrl);

        cy.wait('@entitiesRequestInternal');
      });
       */

      beforeEach(function () {
        cy.visit(organizationDomainPageUrl);

        cy.wait('@entitiesRequest').then(({ response }) => {
          if (response.body.length === 0) {
            this.skip();
          }
        });
      });

      it('detail button click should load details OrganizationDomain page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('organizationDomain');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', organizationDomainPageUrlPattern);
      });

      it('edit button click should load edit OrganizationDomain page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('OrganizationDomain');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', organizationDomainPageUrlPattern);
      });

      it('edit button click should load edit OrganizationDomain page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('OrganizationDomain');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', organizationDomainPageUrlPattern);
      });

      it.skip('last delete button click should delete instance of OrganizationDomain', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('organizationDomain').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', organizationDomainPageUrlPattern);

        organizationDomain = undefined;
      });
    });
  });

  describe('new OrganizationDomain page', () => {
    beforeEach(() => {
      cy.visit(`${organizationDomainPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('OrganizationDomain');
    });

    it.skip('should create an instance of OrganizationDomain', () => {
      cy.get(`[data-cy="domainAcronym"]`).type('different');
      cy.get(`[data-cy="domainAcronym"]`).should('have.value', 'different');

      cy.get(`[data-cy="name"]`).type('obvious after puffin');
      cy.get(`[data-cy="name"]`).should('have.value', 'obvious after puffin');

      cy.get(`[data-cy="isVerified"]`).should('not.be.checked');
      cy.get(`[data-cy="isVerified"]`).click();
      cy.get(`[data-cy="isVerified"]`).should('be.checked');

      cy.get(`[data-cy="businessHandlerClazz"]`).type('uh-huh');
      cy.get(`[data-cy="businessHandlerClazz"]`).should('have.value', 'uh-huh');

      cy.get(`[data-cy="activationStatus"]`).select('INVALID');

      cy.get(`[data-cy="organization"]`).select(1);

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        organizationDomain = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', organizationDomainPageUrlPattern);
    });
  });
});
