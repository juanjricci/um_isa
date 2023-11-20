import {
    entityDetailsBackButtonSelector,
  } from '../../support/entity';


describe('Post e2e custom test', () => {
    const postPageUrl = 'localhost:9000/post';
    const postPageUrlPattern = new RegExp('localhost:9000/post(\\?.*)?$');
    const blogPageUrlPattern = new RegExp('localhost:9000/blog/[0-9]+/view');
    const username = Cypress.env('E2E_USERNAME') ?? 'user';
    const password = Cypress.env('E2E_PASSWORD') ?? 'user';
    beforeEach(() => {
      cy.login(username, password);
    });
  
    beforeEach(() => {
      cy.intercept('GET', '/api/posts+(?*|)').as('entitiesRequest');
      cy.intercept('POST', '/api/posts').as('postEntityRequest');
      cy.intercept('DELETE', '/api/posts/*').as('deleteEntityRequest');
    });

    describe('Click on Blog link, redirect to blog details and return', () => {        

        beforeEach(() => {
            cy.visit(`${postPageUrl}`);
            cy.wait('@entitiesRequest');
        })

        it('Should click on the Blog link and redirect to blog details', () => {

            cy.contains('tr', 'Admin Post').contains('a', 'Admin Blog').click();

            cy.getEntityDetailsHeading('blog');

            cy.url().should('match', blogPageUrlPattern);      
            
        });

        it('should click back button and go back', () => {
            cy.contains('tr', 'Admin Post').contains('a', 'Admin Blog').click();
            cy.get(entityDetailsBackButtonSelector).click();
            cy.wait('@entitiesRequest').then(({ response }) => {
              expect(response.statusCode).to.equal(200);
            });
            cy.url().should('match', postPageUrlPattern);
        });

    });
    
});
  