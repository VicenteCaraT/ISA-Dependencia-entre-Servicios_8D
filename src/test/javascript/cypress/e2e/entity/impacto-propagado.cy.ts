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

describe('ImpactoPropagado e2e test', () => {
  const impactoPropagadoPageUrl = '/impacto-propagado';
  let username: string;
  let password: string;
  // const impactoPropagadoSample = {"detectadoEn":"2026-10-15T12:41:36.348Z","estado":"MITIGADO"};

  let impactoPropagado;
  // let incidente;
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
      url: '/api/incidentes',
      body: {"titulo":"agile lively until","descripcion":"anti annually truthfully","severidad":"SEV3","estado":"RECONOCIDO","detectadoEn":"2023-12-04T02:12:53.431Z","reconocidoEn":"2023-12-04T17:30:59.874Z","mitigadoEn":"2023-12-04T07:30:03.725Z","resueltoEn":"2023-12-04T08:25:09.127Z","usuariosAfectados":22956,"cumplioObjetivo":false},
    }).then(({ body }) => {
      incidente = body;
    });
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/servicios',
      body: {"nombre":"apud","descripcion":"vein","criticidad":"TIER1","entorno":"DESARROLLO","repositorioUrl":"intellect guard","activo":true},
    }).then(({ body }) => {
      servicio = body;
    });
  });
   */

  beforeEach(() => {
    cy.intercept('GET', '/api/impacto-propagados+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/impacto-propagados').as('postEntityRequest');
    cy.intercept('DELETE', '/api/impacto-propagados/*').as('deleteEntityRequest');
  });

  /* Disabled due to incompatibility
  beforeEach(() => {
    // Simulate relationships api for better performance and reproducibility.
    cy.intercept('GET', '/api/incidentes', {
      statusCode: 200,
      body: [incidente],
    });

    cy.intercept('GET', '/api/servicios', {
      statusCode: 200,
      body: [servicio],
    });

    cy.intercept('GET', '/api/dependencia-de-servicios', {
      statusCode: 200,
      body: [],
    });

  });
   */

  afterEach(() => {
    if (impactoPropagado) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/impacto-propagados/${impactoPropagado.id}`,
      }).then(() => {
        impactoPropagado = undefined;
      });
    }
  });

  /* Disabled due to incompatibility
  afterEach(() => {
    if (incidente) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/incidentes/${incidente.id}`,
      }).then(() => {
        incidente = undefined;
      });
    }
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

  it('ImpactoPropagados menu should load ImpactoPropagados page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('impacto-propagado');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('ImpactoPropagado').should('exist');
    cy.location('pathname').should('eq', impactoPropagadoPageUrl);
  });

  describe('ImpactoPropagado page', () => {
    it('should have translated page title', () => {
      cy.visit(impactoPropagadoPageUrl);
      cy.getEntityHeading('ImpactoPropagado').should('not.contain', 'oncallApp.impactoPropagado.home.title');
    });

    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(impactoPropagadoPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create ImpactoPropagado page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.location('pathname').should('eq', `${impactoPropagadoPageUrl}/new`);
        cy.getEntityCreateUpdateHeading('ImpactoPropagado');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', impactoPropagadoPageUrl);
      });
    });

    describe('with existing value', () => {
      /* Disabled due to incompatibility
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/impacto-propagados',
          body: {
            ...impactoPropagadoSample,
            incidente: incidente,
            servicioAfectado: servicio,
          },
        }).then(({ body }) => {
          impactoPropagado = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/impacto-propagados+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [impactoPropagado],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(impactoPropagadoPageUrl);

        cy.wait('@entitiesRequestInternal');
      });
       */

      beforeEach(function () {
        cy.visit(impactoPropagadoPageUrl);

        cy.wait('@entitiesRequest').then(({ response }) => {
          if (response?.body.length === 0) {
            this.skip();
          }
        });
      });

      it('detail button click should load details ImpactoPropagado page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('impactoPropagado');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', impactoPropagadoPageUrl);
      });

      it('edit button click should load edit ImpactoPropagado page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('ImpactoPropagado');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', impactoPropagadoPageUrl);
      });

      it('edit button click should load edit ImpactoPropagado page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('ImpactoPropagado');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', impactoPropagadoPageUrl);
      });

      // Reason: cannot create a required entity with relationship with required relationships.
      it.skip('last delete button click should delete instance of ImpactoPropagado', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('impactoPropagado').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', impactoPropagadoPageUrl);

        impactoPropagado = undefined;
      });
    });
  });

  describe('new ImpactoPropagado page', () => {
    beforeEach(() => {
      cy.visit(impactoPropagadoPageUrl);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('ImpactoPropagado');
    });

    // Reason: cannot create a required entity with relationship with required relationships.
    it.skip('should create an instance of ImpactoPropagado', () => {
      cy.get(`[data-cy="detectadoEn"]`).type('2026-10-15T13:28');
      cy.get(`[data-cy="detectadoEn"]`).blur();
      cy.get(`[data-cy="detectadoEn"]`).should('have.value', '2026-10-15T13:28');

      cy.get(`[data-cy="estado"]`).select('MITIGADO');

      cy.get(`[data-cy="detalle"]`).type('scary');
      cy.get(`[data-cy="detalle"]`).should('have.value', 'scary');

      cy.get(`[data-cy="incidente"]`).select(1);
      cy.get(`[data-cy="servicioAfectado"]`).select(1);

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        impactoPropagado = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.location('pathname').should('eq', impactoPropagadoPageUrl);
    });
  });
});
