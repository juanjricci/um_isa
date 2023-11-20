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

describe('Like e2e test', () => {
  const likePageUrl = '/like';
  const likePageUrlPattern = new RegExp('/like(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const likeSample = { isliked: true };

  let like;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/likes+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/likes').as('postEntityRequest');
    cy.intercept('DELETE', '/api/likes/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (like) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/likes/${like.id}`,
      }).then(() => {
        like = undefined;
      });
    }
  });

  it('Likes menu should load Likes page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('like');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Like').should('exist');
    cy.url().should('match', likePageUrlPattern);
  });

  describe('Like page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(likePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Like page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/like/new$'));
        cy.getEntityCreateUpdateHeading('Like');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', likePageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/likes',
          body: likeSample,
        }).then(({ body }) => {
          like = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/likes+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [like],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(likePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Like page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('like');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', likePageUrlPattern);
      });

      it('edit button click should load edit Like page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Like');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', likePageUrlPattern);
      });

      it('edit button click should load edit Like page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Like');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', likePageUrlPattern);
      });

      it('last delete button click should delete instance of Like', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('like').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', likePageUrlPattern);

        like = undefined;
      });
    });
  });

  describe('new Like page', () => {
    beforeEach(() => {
      cy.visit(`${likePageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Like');
    });

    it('should create an instance of Like', () => {
      cy.get(`[data-cy="isliked"]`).should('not.be.checked');
      cy.get(`[data-cy="isliked"]`).click();
      cy.get(`[data-cy="isliked"]`).should('be.checked');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        like = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', likePageUrlPattern);
    });
  });
});
