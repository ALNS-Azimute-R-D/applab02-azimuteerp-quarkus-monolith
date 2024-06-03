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

describe('TypeOfPerson e2e test', () => {
  const typeOfPersonPageUrl = '/type-of-person';
  const typeOfPersonPageUrlPattern = new RegExp('/type-of-person(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const typeOfPersonSample = { code: 'after', description: 'instead sick oblige' };

  let typeOfPerson;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/type-of-people+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/type-of-people').as('postEntityRequest');
    cy.intercept('DELETE', '/api/type-of-people/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (typeOfPerson) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/type-of-people/${typeOfPerson.id}`,
      }).then(() => {
        typeOfPerson = undefined;
      });
    }
  });

  it('TypeOfPeople menu should load TypeOfPeople page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('type-of-person');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('TypeOfPerson').should('exist');
    cy.url().should('match', typeOfPersonPageUrlPattern);
  });

  describe('TypeOfPerson page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(typeOfPersonPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create TypeOfPerson page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/type-of-person/new$'));
        cy.getEntityCreateUpdateHeading('TypeOfPerson');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', typeOfPersonPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/type-of-people',
          body: typeOfPersonSample,
        }).then(({ body }) => {
          typeOfPerson = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/type-of-people+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/type-of-people?page=0&size=20>; rel="last",<http://localhost/api/type-of-people?page=0&size=20>; rel="first"',
              },
              body: [typeOfPerson],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(typeOfPersonPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details TypeOfPerson page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('typeOfPerson');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', typeOfPersonPageUrlPattern);
      });

      it('edit button click should load edit TypeOfPerson page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('TypeOfPerson');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', typeOfPersonPageUrlPattern);
      });

      it('edit button click should load edit TypeOfPerson page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('TypeOfPerson');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', typeOfPersonPageUrlPattern);
      });

      it('last delete button click should delete instance of TypeOfPerson', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('typeOfPerson').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', typeOfPersonPageUrlPattern);

        typeOfPerson = undefined;
      });
    });
  });

  describe('new TypeOfPerson page', () => {
    beforeEach(() => {
      cy.visit(`${typeOfPersonPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('TypeOfPerson');
    });

    it('should create an instance of TypeOfPerson', () => {
      cy.get(`[data-cy="code"]`).type('breez');
      cy.get(`[data-cy="code"]`).should('have.value', 'breez');

      cy.get(`[data-cy="description"]`).type('qua towards how');
      cy.get(`[data-cy="description"]`).should('have.value', 'qua towards how');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        typeOfPerson = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', typeOfPersonPageUrlPattern);
    });
  });
});
