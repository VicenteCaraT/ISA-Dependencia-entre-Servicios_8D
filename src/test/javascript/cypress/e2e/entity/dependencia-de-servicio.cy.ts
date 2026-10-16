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

describe('DependenciaDeServicio e2e test', () => {
  const dependenciaDeServicioPageUrl = '/dependencia-de-servicio';
  let username: string;
  let password: string;
  // const dependenciaDeServicioSample = {"tipo":"OPCIONAL","activa":false};

  let dependenciaDeServicio;
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
      body: {"nombre":"sorrowful tomography custody","descripcion":"avalanche scout indeed","criticidad":"TIER3","entorno":"PRODUCCION","repositorioUrl":"unnaturally indeed only","activo":true},
    }).then(({ body }) => {
      servicio = body;
    });
  });
   */

  beforeEach(() => {
    cy.intercept('GET', '/api/dependencia-de-servicios+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/dependencia-de-servicios').as('postEntityRequest');
    cy.intercept('DELETE', '/api/dependencia-de-servicios/*').as('deleteEntityRequest');
  });

  /* Disabled due to incompatibility
  beforeEach(() => {
    // Simulate relationships api for better performance and reproducibility.
    cy.intercept('GET', '/api/servicios', {
      statusCode: 200,
      body: [servicio],
    });

    cy.intercept('GET', '/api/impacto-propagados', {
      statusCode: 200,
      body: [],
    });

  });
   */

  afterEach(() => {
    if (dependenciaDeServicio) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/dependencia-de-servicios/${dependenciaDeServicio.id}`,
      }).then(() => {
        dependenciaDeServicio = undefined;
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

  it('DependenciaDeServicios menu should load DependenciaDeServicios page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('dependencia-de-servicio');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('DependenciaDeServicio').should('exist');
    cy.location('pathname').should('eq', dependenciaDeServicioPageUrl);
  });

  describe('DependenciaDeServicio page', () => {
    it('should have translated page title', () => {
      cy.visit(dependenciaDeServicioPageUrl);
      cy.getEntityHeading('DependenciaDeServicio').should('not.contain', 'oncallApp.dependenciaDeServicio.home.title');
    });

    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(dependenciaDeServicioPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create DependenciaDeServicio page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.location('pathname').should('eq', `${dependenciaDeServicioPageUrl}/new`);
        cy.getEntityCreateUpdateHeading('DependenciaDeServicio');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', dependenciaDeServicioPageUrl);
      });
    });

    describe('with existing value', () => {
      /* Disabled due to incompatibility
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/dependencia-de-servicios',
          body: {
            ...dependenciaDeServicioSample,
            servicioDependiente: servicio,
            servicioProveedor: servicio,
          },
        }).then(({ body }) => {
          dependenciaDeServicio = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/dependencia-de-servicios+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [dependenciaDeServicio],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(dependenciaDeServicioPageUrl);

        cy.wait('@entitiesRequestInternal');
      });
       */

      beforeEach(function () {
        cy.visit(dependenciaDeServicioPageUrl);

        cy.wait('@entitiesRequest').then(({ response }) => {
          if (response?.body.length === 0) {
            this.skip();
          }
        });
      });

      it('detail button click should load details DependenciaDeServicio page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('dependenciaDeServicio');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', dependenciaDeServicioPageUrl);
      });

      it('edit button click should load edit DependenciaDeServicio page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('DependenciaDeServicio');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', dependenciaDeServicioPageUrl);
      });

      it('edit button click should load edit DependenciaDeServicio page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('DependenciaDeServicio');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', dependenciaDeServicioPageUrl);
      });

      // Reason: cannot create a required entity with relationship with required relationships.
      it.skip('last delete button click should delete instance of DependenciaDeServicio', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('dependenciaDeServicio').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', dependenciaDeServicioPageUrl);

        dependenciaDeServicio = undefined;
      });
    });
  });

  describe('new DependenciaDeServicio page', () => {
    beforeEach(() => {
      cy.visit(dependenciaDeServicioPageUrl);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('DependenciaDeServicio');
    });

    // Reason: cannot create a required entity with relationship with required relationships.
    it.skip('should create an instance of DependenciaDeServicio', () => {
      cy.get(`[data-cy="tipo"]`).select('OPCIONAL');

      cy.get(`[data-cy="activa"]`).should('not.be.checked');
      cy.get(`[data-cy="activa"]`).click();
      cy.get(`[data-cy="activa"]`).should('be.checked');

      cy.get(`[data-cy="descripcion"]`).type('make playfully');
      cy.get(`[data-cy="descripcion"]`).should('have.value', 'make playfully');

      cy.get(`[data-cy="servicioDependiente"]`).select(1);
      cy.get(`[data-cy="servicioProveedor"]`).select(1);

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        dependenciaDeServicio = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.location('pathname').should('eq', dependenciaDeServicioPageUrl);
    });
  });
});
