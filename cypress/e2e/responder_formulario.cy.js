describe("Rellenar un formulario automáticamente", () => {
  it("Debe rellenar el formulario con los datos especificados", () => {

    // setea las cookies (para iniciar sesión)
    cy.setCookie('MoodleSession', '6nnaa3iiepj7ddv5ivcgbb5hi1');

    // visita el sitio del formulario
    cy.visit('https://virtual.um.edu.ar/mod/questionnaire/complete.php?id=210589')
    
    // // Localiza el campo de nombre
    // cy.get("#username").type("j.ricci");

    // // Localiza el campo de correo electrónico
    // cy.get("#password").type("********");

    // Hace clic en el botón de envío
    // cy.get("#loginbtn").click();

    // hace click en el radioButton
    cy.get("#auto-rb0001").click();

    // selecciona un elemento de menu desplegable
    cy.get("#dropSelecciòn")
      .select("Tercero")
      .invoke("val")
      .should("eq", "4053");

    // hace click en el botón Enviar Encuesta
    cy.contains('Enviar encuesta').click();
    
  });
});