describe('example to-do app', () => {
    beforeEach(() => {
        cy.visit('https://biblioteca.um.edu.ar/')
      })
    it('writes into form', () => {
        cy.get('#translControl1').type('Pressman')
        cy.get('#searchform').submit()
        cy.get('tbody tr').should('have.length', 2)
        cy.get('tbody tr').first().should('contain.text', 'INGENIERIA DEL SOFTWARE: UN ENFOQUE ')
        cy.get('#title_summary_60480 a').should('contain.text', 'INGENIERIA DEL SOFTWARE: UN ENFOQUE ')
        cy.get('#title_summary_60481 a').should('have.text', 'INGENIERIA DEL SOFTWARE ')
      })
    
})