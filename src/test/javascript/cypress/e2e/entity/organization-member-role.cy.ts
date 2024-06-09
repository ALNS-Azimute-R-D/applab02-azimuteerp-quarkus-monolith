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

describe('OrganizationMemberRole e2e test', () => {
  const organizationMemberRolePageUrl = '/organization-member-role';
  const organizationMemberRolePageUrlPattern = new RegExp('/organization-member-role(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  // const organizationMemberRoleSample = {"joinedAt":"2024-06-08"};

  let organizationMemberRole;
  // let organizationMembership;
  // let organizationRole;

  beforeEach(() => {
    cy.login(username, password);
  });

  /* Disabled due to incompatibility
  beforeEach(() => {
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/organization-memberships',
      body: {"joinedAt":"2024-06-07","activationStatus":"INACTIVE"},
    }).then(({ body }) => {
      organizationMembership = body;
    });
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/organization-roles',
      body: {"roleName":"among","activationStatus":"ACTIVE"},
    }).then(({ body }) => {
      organizationRole = body;
    });
  });
   */

  beforeEach(() => {
    cy.intercept('GET', '/api/organization-member-roles+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/organization-member-roles').as('postEntityRequest');
    cy.intercept('DELETE', '/api/organization-member-roles/*').as('deleteEntityRequest');
  });

  /* Disabled due to incompatibility
  beforeEach(() => {
    // Simulate relationships api for better performance and reproducibility.
    cy.intercept('GET', '/api/organization-memberships', {
      statusCode: 200,
      body: [organizationMembership],
    });

    cy.intercept('GET', '/api/organization-roles', {
      statusCode: 200,
      body: [organizationRole],
    });

  });
   */

  afterEach(() => {
    if (organizationMemberRole) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/organization-member-roles/${organizationMemberRole.id}`,
      }).then(() => {
        organizationMemberRole = undefined;
      });
    }
  });

  /* Disabled due to incompatibility
  afterEach(() => {
    if (organizationMembership) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/organization-memberships/${organizationMembership.id}`,
      }).then(() => {
        organizationMembership = undefined;
      });
    }
    if (organizationRole) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/organization-roles/${organizationRole.id}`,
      }).then(() => {
        organizationRole = undefined;
      });
    }
  });
   */

  it('OrganizationMemberRoles menu should load OrganizationMemberRoles page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('organization-member-role');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('OrganizationMemberRole').should('exist');
    cy.url().should('match', organizationMemberRolePageUrlPattern);
  });

  describe('OrganizationMemberRole page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(organizationMemberRolePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create OrganizationMemberRole page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/organization-member-role/new$'));
        cy.getEntityCreateUpdateHeading('OrganizationMemberRole');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', organizationMemberRolePageUrlPattern);
      });
    });

    describe('with existing value', () => {
      /* Disabled due to incompatibility
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/organization-member-roles',
          body: {
            ...organizationMemberRoleSample,
            organizationMembership: organizationMembership,
            organizationRole: organizationRole,
          },
        }).then(({ body }) => {
          organizationMemberRole = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/organization-member-roles+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/organization-member-roles?page=0&size=20>; rel="last",<http://localhost/api/organization-member-roles?page=0&size=20>; rel="first"',
              },
              body: [organizationMemberRole],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(organizationMemberRolePageUrl);

        cy.wait('@entitiesRequestInternal');
      });
       */

      beforeEach(function () {
        cy.visit(organizationMemberRolePageUrl);

        cy.wait('@entitiesRequest').then(({ response }) => {
          if (response.body.length === 0) {
            this.skip();
          }
        });
      });

      it('detail button click should load details OrganizationMemberRole page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('organizationMemberRole');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', organizationMemberRolePageUrlPattern);
      });

      it('edit button click should load edit OrganizationMemberRole page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('OrganizationMemberRole');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', organizationMemberRolePageUrlPattern);
      });

      it('edit button click should load edit OrganizationMemberRole page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('OrganizationMemberRole');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', organizationMemberRolePageUrlPattern);
      });

      it.skip('last delete button click should delete instance of OrganizationMemberRole', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('organizationMemberRole').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', organizationMemberRolePageUrlPattern);

        organizationMemberRole = undefined;
      });
    });
  });

  describe('new OrganizationMemberRole page', () => {
    beforeEach(() => {
      cy.visit(`${organizationMemberRolePageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('OrganizationMemberRole');
    });

    it.skip('should create an instance of OrganizationMemberRole', () => {
      cy.get(`[data-cy="joinedAt"]`).type('2024-06-07');
      cy.get(`[data-cy="joinedAt"]`).blur();
      cy.get(`[data-cy="joinedAt"]`).should('have.value', '2024-06-07');

      cy.get(`[data-cy="organizationMembership"]`).select(1);
      cy.get(`[data-cy="organizationRole"]`).select(1);

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        organizationMemberRole = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', organizationMemberRolePageUrlPattern);
    });
  });
});
