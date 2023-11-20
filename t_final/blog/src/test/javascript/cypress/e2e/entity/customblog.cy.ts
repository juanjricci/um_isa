
describe('Blog e2e custom test', () => {
    const blogPageUrl = 'localhost:9000/blog';
    const username = Cypress.env('E2E_USERNAME') ?? 'user';
    const password = Cypress.env('E2E_PASSWORD') ?? 'user';
    const blogSample = { name: 'Zample Blog', handle: 'zample-blog' };
  
    let blog;
  
    beforeEach(() => {
      cy.login(username, password);
    });
  
    beforeEach(() => {
      cy.intercept('GET', '/api/blogs+(?*|)').as('entitiesRequest');
      cy.intercept('POST', '/api/blogs').as('postEntityRequest');
      cy.intercept('DELETE', '/api/blogs/*').as('deleteEntityRequest');
    });
  
    afterEach(() => {
      if (blog) {
        cy.authenticatedRequest({
          method: 'DELETE',
          url: `/api/blogs/${blog.id}`,
        }).then(() => {
          blog = undefined;
        });
      }
    });

    describe('Create sample blog and sort by id, name or handle', () => {
      beforeEach(() => {          
          cy.authenticatedRequest({
            method: 'POST',
            Authorization: 'Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbiIsImV4cCI6MTcwMDA0ODEzNSwiYXV0aCI6IlJPTEVfQURNSU4gUk9MRV9VU0VSIiwiaWF0IjoxNjk5OTYxNzM1fQ.ynhO2ybbwwTvWWvGnR3IzG5GzjAnKpMUSFHw7xZdTAIDEV_NW9I1e5eKwCxsEjeljsZmA9nQO-ghJ0ZQvC3Rgw',
            url: '/api/blogs',
            body: blogSample,
            headers: {
              'Content-Type': 'application/json',
            },
          })
          cy.visit(`${blogPageUrl}`);
          cy.wait('@entitiesRequest').then(({ response }) => {
            const newlyCreatedBlog = response.body.find(blog => blog.name === blogSample.name);
            expect(newlyCreatedBlog).to.exist;
            blog = newlyCreatedBlog
          });
        });

        it('should click on sort by id and should sort by id', () => {
            // Click on the "Sort by ID" table header
            cy.get('th[jhisortby="id"]').click();

            // Wait for the entities request to complete after sorting
            cy.wait('@entitiesRequest');

            // Get the first row in the table
            cy.get('tbody > tr').first().should('contain.text', 'zample-blog')

        });

        it('should click on sort by name and should sort by name', () => {
          // Click on the "Sort by Name" table header
          cy.get('th[jhisortby="name"]').click();
          cy.wait('@entitiesRequest');

          cy.get('th[jhisortby="name"]').click();
          cy.wait('@entitiesRequest');

          // Get the first row in the table
          cy.get('tbody > tr').first().should('contain.text', 'zample-blog');

        });

        it('should click on sort by handle and should sort by handle', () => {
          // Click on the "Sort by Name" table header
          cy.get('th[jhisortby="handle"]').click();
          cy.wait('@entitiesRequest');

          cy.get('th[jhisortby="handle"]').click();
          cy.wait('@entitiesRequest');

          // Get the first row in the table
          cy.get('tbody > tr').first().should('contain.text', 'zample-blog');
          
        });
        
    });
  });
  