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

describe('OrganizationRole e2e test', () => {
  const organizationRolePageUrl = '/organization-role';
  const organizationRolePageUrlPattern = new RegExp('/organization-role(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  // const organizationRoleSample = {"roleName":"and mellow whoa","activationStatus":"PENDENT"};

  let organizationRole;
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
      body: {"acronym":"though junk","businessCode":"archives","hierarchicalLevel":"ballpark","name":"questionably hostel","description":"incidentally","businessHandlerClazz":"doubtfully like ew","mainContactPersonDetailsJSON":"doting vacation","technicalEnvironmentsDetailsJSON":"vol zowie past","customAttributesDetailsJSON":"opposite","organizationStatus":"UNDER_EVALUATION","activationStatus":"INACTIVE","logoImg":"Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci5wbmc=","logoImgContentType":"unknown"},
    }).then(({ body }) => {
      organization = body;
    });
  });
   */

  beforeEach(() => {
    cy.intercept('GET', '/api/organization-roles+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/organization-roles').as('postEntityRequest');
    cy.intercept('DELETE', '/api/organization-roles/*').as('deleteEntityRequest');
  });

  /* Disabled due to incompatibility
  beforeEach(() => {
    // Simulate relationships api for better performance and reproducibility.
    cy.intercept('GET', '/api/organizations', {
      statusCode: 200,
      body: [organization],
    });

    cy.intercept('GET', '/api/organization-member-roles', {
      statusCode: 200,
      body: [],
    });

  });
   */

  afterEach(() => {
    if (organizationRole) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/organization-roles/${organizationRole.id}`,
      }).then(() => {
        organizationRole = undefined;
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

  it('OrganizationRoles menu should load OrganizationRoles page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('organization-role');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('OrganizationRole').should('exist');
    cy.url().should('match', organizationRolePageUrlPattern);
  });

  describe('OrganizationRole page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(organizationRolePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create OrganizationRole page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/organization-role/new$'));
        cy.getEntityCreateUpdateHeading('OrganizationRole');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', organizationRolePageUrlPattern);
      });
    });

    describe('with existing value', () => {
      /* Disabled due to incompatibility
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/organization-roles',
          body: {
            ...organizationRoleSample,
            organization: organization,
          },
        }).then(({ body }) => {
          organizationRole = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/organization-roles+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/organization-roles?page=0&size=20>; rel="last",<http://localhost/api/organization-roles?page=0&size=20>; rel="first"',
              },
              body: [organizationRole],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(organizationRolePageUrl);

        cy.wait('@entitiesRequestInternal');
      });
       */

      beforeEach(function () {
        cy.visit(organizationRolePageUrl);

        cy.wait('@entitiesRequest').then(({ response }) => {
          if (response.body.length === 0) {
            this.skip();
          }
        });
      });

      it('detail button click should load details OrganizationRole page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('organizationRole');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', organizationRolePageUrlPattern);
      });

      it('edit button click should load edit OrganizationRole page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('OrganizationRole');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', organizationRolePageUrlPattern);
      });

      it('edit button click should load edit OrganizationRole page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('OrganizationRole');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', organizationRolePageUrlPattern);
      });

      it.skip('last delete button click should delete instance of OrganizationRole', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('organizationRole').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', organizationRolePageUrlPattern);

        organizationRole = undefined;
      });
    });
  });

  describe('new OrganizationRole page', () => {
    beforeEach(() => {
      cy.visit(`${organizationRolePageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('OrganizationRole');
    });

    it.skip('should create an instance of OrganizationRole', () => {
      cy.get(`[data-cy="roleName"]`).type('till');
      cy.get(`[data-cy="roleName"]`).should('have.value', 'till');

      cy.get(`[data-cy="activationStatus"]`).select('INACTIVE');

      cy.get(`[data-cy="organization"]`).select(1);

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        organizationRole = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', organizationRolePageUrlPattern);
    });
  });
});
