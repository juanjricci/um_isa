describe('check if header is well displayed', () => {
    const commentPageUrl = 'localhost:9000/comment';
    const username = Cypress.env('E2E_USERNAME') ?? 'user';
    const password = Cypress.env('E2E_PASSWORD') ?? 'user';
    const expectedHeaders = ['Id', 'Content', 'User', 'Post'];
    beforeEach(() => {
        cy.login(username, password);
    });

    beforeEach(() => {
        cy.intercept('GET', '/api/comments+(?*|)').as('entitiesRequest');
        cy.intercept('POST', '/api/comments').as('postEntityRequest');
        cy.intercept('DELETE', '/api/comments/*').as('deleteEntityRequest');
    });
        
    it('table header should show Id, Content, User, Post', () => {
        cy.visit(`${commentPageUrl}`);
        cy.get('thead > tr').find('th').find('span').each(($el, index) => {
            expect($el.text()).to.eq(expectedHeaders[index])
        });
    });
});