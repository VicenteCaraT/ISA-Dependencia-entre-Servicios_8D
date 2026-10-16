import {
  entityConfirmDeleteButtonSelector,
  entityCreateButtonSelector,
  entityCreateCancelButtonSelector,
  entityCreateSaveButtonSelector,
  entityDeleteButtonSelector,
  entityDetailsBackButtonSelector,
  entityDetailsButtonSelector,
  entityEditButtonSelector,
  entityTableSelector,
} from '../../support/entity';

describe('ServicioCritico e2e test', () => {
  const servicioCriticoPageUrl = '/servicio-critico';
  let username: string;
  let password: string;
  // const servicioCriticoSample = {"prioridadEscalamiento":1,"requiereEscalamientoInmediato":true};

  let servicioCritico;
  // let servicio;

  before(() => {
    cy.credentials().then(credentials => {
      ({ username, password } = credentials);
    });
  });

  beforeEach(() => {
    cy.login(username, password);
  });

  /* Disabled due to incompatibility
  beforeEach(() => {
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/servicios',
      body: {"nombre":"guidance","descripcion":"inside meh","criticidad":"TIER1","entorno":"DESARROLLO","repositorioUrl":"hm","activo":true},
    }).then(({ body }) => {
      servicio = body;
    });
  });
   */

  beforeEach(() => {
    cy.intercept('GET', '/api/servicio-criticos+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/servicio-criticos').as('postEntityRequest');
    cy.intercept('DELETE', '/api/servicio-criticos/*').as('deleteEntityRequest');
  });

  /* Disabled due to incompatibility
  beforeEach(() => {
    // Simulate relationships api for better performance and reproducibility.
    cy.intercept('GET', '/api/servicios', {
      statusCode: 200,
      body: [servicio],
    });

  });
   */

  afterEach(() => {
    if (servicioCritico) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/servicio-criticos/${servicioCritico.id}`,
      }).then(() => {
        servicioCritico = undefined;
      });
    }
  });

  /* Disabled due to incompatibility
  afterEach(() => {
    if (servicio) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/servicios/${servicio.id}`,
      }).then(() => {
        servicio = undefined;
      });
    }
  });
   */

  it('ServicioCriticos menu should load ServicioCriticos page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('servicio-critico');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('ServicioCritico').should('exist');
    cy.location('pathname').should('eq', servicioCriticoPageUrl);
  });

  describe('ServicioCritico page', () => {
    it('should have translated page title', () => {
      cy.visit(servicioCriticoPageUrl);
      cy.getEntityHeading('ServicioCritico').should('not.contain', 'oncallApp.servicioCritico.home.title');
    });

    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(servicioCriticoPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create ServicioCritico page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.location('pathname').should('eq', `${servicioCriticoPageUrl}/new`);
        cy.getEntityCreateUpdateHeading('ServicioCritico');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', servicioCriticoPageUrl);
      });
    });

    describe('with existing value', () => {
      /* Disabled due to incompatibility
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/servicio-criticos',
          body: {
            ...servicioCriticoSample,
            servicio: servicio,
          },
        }).then(({ body }) => {
          servicioCritico = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/servicio-criticos+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [servicioCritico],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(servicioCriticoPageUrl);

        cy.wait('@entitiesRequestInternal');
      });
       */

      beforeEach(function () {
        cy.visit(servicioCriticoPageUrl);

        cy.wait('@entitiesRequest').then(({ response }) => {
          if (response?.body.length === 0) {
            this.skip();
          }
        });
      });

      it('detail button click should load details ServicioCritico page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('servicioCritico');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', servicioCriticoPageUrl);
      });

      it('edit button click should load edit ServicioCritico page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('ServicioCritico');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', servicioCriticoPageUrl);
      });

      it('edit button click should load edit ServicioCritico page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('ServicioCritico');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', servicioCriticoPageUrl);
      });

      // Reason: cannot create a required entity with relationship with required relationships.
      it.skip('last delete button click should delete instance of ServicioCritico', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('servicioCritico').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', servicioCriticoPageUrl);

        servicioCritico = undefined;
      });
    });
  });

  describe('new ServicioCritico page', () => {
    beforeEach(() => {
      cy.visit(servicioCriticoPageUrl);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('ServicioCritico');
    });

    // Reason: cannot create a required entity with relationship with required relationships.
    it.skip('should create an instance of ServicioCritico', () => {
      cy.get(`[data-cy="prioridadEscalamiento"]`).type('9');
      cy.get(`[data-cy="prioridadEscalamiento"]`).should('have.value', '9');

      cy.get(`[data-cy="requiereEscalamientoInmediato"]`).should('not.be.checked');
      cy.get(`[data-cy="requiereEscalamientoInmediato"]`).click();
      cy.get(`[data-cy="requiereEscalamientoInmediato"]`).should('be.checked');

      cy.get(`[data-cy="justificacion"]`).type('gadzooks');
      cy.get(`[data-cy="justificacion"]`).should('have.value', 'gadzooks');

      cy.get(`[data-cy="servicio"]`).select(1);

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        servicioCritico = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.location('pathname').should('eq', servicioCriticoPageUrl);
    });
  });
});
