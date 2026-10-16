package ar.edu.um.isa.oncall.web.rest;

import static ar.edu.um.isa.oncall.domain.ServicioCriticoAsserts.*;
import static ar.edu.um.isa.oncall.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import ar.edu.um.isa.oncall.IntegrationTest;
import ar.edu.um.isa.oncall.domain.Servicio;
import ar.edu.um.isa.oncall.domain.ServicioCritico;
import ar.edu.um.isa.oncall.repository.ServicioCriticoRepository;
import ar.edu.um.isa.oncall.service.ServicioCriticoService;
import ar.edu.um.isa.oncall.service.dto.ServicioCriticoDTO;
import ar.edu.um.isa.oncall.service.mapper.ServicioCriticoMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link ServicioCriticoResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class ServicioCriticoResourceIT {

    private static final Integer DEFAULT_PRIORIDAD_ESCALAMIENTO = 1;
    private static final Integer UPDATED_PRIORIDAD_ESCALAMIENTO = 2;

    private static final Boolean DEFAULT_REQUIERE_ESCALAMIENTO_INMEDIATO = false;
    private static final Boolean UPDATED_REQUIERE_ESCALAMIENTO_INMEDIATO = true;

    private static final String DEFAULT_JUSTIFICACION = "AAAAAAAAAA";
    private static final String UPDATED_JUSTIFICACION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/servicio-criticos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ServicioCriticoRepository servicioCriticoRepository;

    @Mock
    private ServicioCriticoRepository servicioCriticoRepositoryMock;

    @Autowired
    private ServicioCriticoMapper servicioCriticoMapper;

    @Mock
    private ServicioCriticoService servicioCriticoServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restServicioCriticoMockMvc;

    private ServicioCritico servicioCritico;

    private ServicioCritico insertedServicioCritico;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ServicioCritico createEntity(EntityManager em) {
        ServicioCritico servicioCritico = new ServicioCritico()
            .prioridadEscalamiento(DEFAULT_PRIORIDAD_ESCALAMIENTO)
            .requiereEscalamientoInmediato(DEFAULT_REQUIERE_ESCALAMIENTO_INMEDIATO)
            .justificacion(DEFAULT_JUSTIFICACION);
        // Add required entity
        Servicio servicio;
        if (TestUtil.findAll(em, Servicio.class).isEmpty()) {
            servicio = ServicioResourceIT.createEntity(em);
            em.persist(servicio);
            em.flush();
        } else {
            servicio = TestUtil.findAll(em, Servicio.class).get(0);
        }
        servicioCritico.setServicio(servicio);
        return servicioCritico;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ServicioCritico createUpdatedEntity(EntityManager em) {
        ServicioCritico updatedServicioCritico = new ServicioCritico()
            .prioridadEscalamiento(UPDATED_PRIORIDAD_ESCALAMIENTO)
            .requiereEscalamientoInmediato(UPDATED_REQUIERE_ESCALAMIENTO_INMEDIATO)
            .justificacion(UPDATED_JUSTIFICACION);
        // Add required entity
        Servicio servicio;
        if (TestUtil.findAll(em, Servicio.class).isEmpty()) {
            servicio = ServicioResourceIT.createUpdatedEntity(em);
            em.persist(servicio);
            em.flush();
        } else {
            servicio = TestUtil.findAll(em, Servicio.class).get(0);
        }
        updatedServicioCritico.setServicio(servicio);
        return updatedServicioCritico;
    }

    @BeforeEach
    void initTest() {
        servicioCritico = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedServicioCritico != null) {
            servicioCriticoRepository.delete(insertedServicioCritico);
            insertedServicioCritico = null;
        }
    }

    @Test
    @Transactional
    void createServicioCritico() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the ServicioCritico
        ServicioCriticoDTO servicioCriticoDTO = servicioCriticoMapper.toDto(servicioCritico);
        var returnedServicioCriticoDTO = om.readValue(
            restServicioCriticoMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(servicioCriticoDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ServicioCriticoDTO.class
        );

        // Validate the ServicioCritico in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedServicioCritico = servicioCriticoMapper.toEntity(returnedServicioCriticoDTO);
        assertServicioCriticoUpdatableFieldsEquals(returnedServicioCritico, getPersistedServicioCritico(returnedServicioCritico));

        insertedServicioCritico = returnedServicioCritico;
    }

    @Test
    @Transactional
    void createServicioCriticoWithExistingId() throws Exception {
        // Create the ServicioCritico with an existing ID
        servicioCritico.setId(1L);
        ServicioCriticoDTO servicioCriticoDTO = servicioCriticoMapper.toDto(servicioCritico);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restServicioCriticoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(servicioCriticoDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ServicioCritico in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkPrioridadEscalamientoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        servicioCritico.setPrioridadEscalamiento(null);

        // Create the ServicioCritico, which fails.
        ServicioCriticoDTO servicioCriticoDTO = servicioCriticoMapper.toDto(servicioCritico);

        restServicioCriticoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(servicioCriticoDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkRequiereEscalamientoInmediatoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        servicioCritico.setRequiereEscalamientoInmediato(null);

        // Create the ServicioCritico, which fails.
        ServicioCriticoDTO servicioCriticoDTO = servicioCriticoMapper.toDto(servicioCritico);

        restServicioCriticoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(servicioCriticoDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllServicioCriticos() throws Exception {
        // Initialize the database
        insertedServicioCritico = servicioCriticoRepository.saveAndFlush(servicioCritico);

        // Get all the servicioCriticoList
        restServicioCriticoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(servicioCritico.getId().intValue())))
            .andExpect(jsonPath("$.[*].prioridadEscalamiento").value(hasItem(DEFAULT_PRIORIDAD_ESCALAMIENTO)))
            .andExpect(jsonPath("$.[*].requiereEscalamientoInmediato").value(hasItem(DEFAULT_REQUIERE_ESCALAMIENTO_INMEDIATO)))
            .andExpect(jsonPath("$.[*].justificacion").value(hasItem(DEFAULT_JUSTIFICACION)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllServicioCriticosWithEagerRelationshipsIsEnabled() throws Exception {
        when(servicioCriticoServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restServicioCriticoMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(servicioCriticoServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllServicioCriticosWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(servicioCriticoServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restServicioCriticoMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(servicioCriticoRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getServicioCritico() throws Exception {
        // Initialize the database
        insertedServicioCritico = servicioCriticoRepository.saveAndFlush(servicioCritico);

        // Get the servicioCritico
        restServicioCriticoMockMvc
            .perform(get(ENTITY_API_URL_ID, servicioCritico.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(servicioCritico.getId().intValue()))
            .andExpect(jsonPath("$.prioridadEscalamiento").value(DEFAULT_PRIORIDAD_ESCALAMIENTO))
            .andExpect(jsonPath("$.requiereEscalamientoInmediato").value(DEFAULT_REQUIERE_ESCALAMIENTO_INMEDIATO))
            .andExpect(jsonPath("$.justificacion").value(DEFAULT_JUSTIFICACION));
    }

    @Test
    @Transactional
    void getNonExistingServicioCritico() throws Exception {
        // Get the servicioCritico
        restServicioCriticoMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingServicioCritico() throws Exception {
        // Initialize the database
        insertedServicioCritico = servicioCriticoRepository.saveAndFlush(servicioCritico);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the servicioCritico
        ServicioCritico updatedServicioCritico = servicioCriticoRepository.findById(servicioCritico.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedServicioCritico are not directly saved in db
        em.detach(updatedServicioCritico);
        updatedServicioCritico
            .prioridadEscalamiento(UPDATED_PRIORIDAD_ESCALAMIENTO)
            .requiereEscalamientoInmediato(UPDATED_REQUIERE_ESCALAMIENTO_INMEDIATO)
            .justificacion(UPDATED_JUSTIFICACION);
        ServicioCriticoDTO servicioCriticoDTO = servicioCriticoMapper.toDto(updatedServicioCritico);

        restServicioCriticoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, servicioCriticoDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(servicioCriticoDTO))
            )
            .andExpect(status().isOk());

        // Validate the ServicioCritico in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedServicioCriticoToMatchAllProperties(updatedServicioCritico);
    }

    @Test
    @Transactional
    void putNonExistingServicioCritico() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        servicioCritico.setId(longCount.incrementAndGet());

        // Create the ServicioCritico
        ServicioCriticoDTO servicioCriticoDTO = servicioCriticoMapper.toDto(servicioCritico);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restServicioCriticoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, servicioCriticoDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(servicioCriticoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ServicioCritico in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchServicioCritico() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        servicioCritico.setId(longCount.incrementAndGet());

        // Create the ServicioCritico
        ServicioCriticoDTO servicioCriticoDTO = servicioCriticoMapper.toDto(servicioCritico);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restServicioCriticoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(servicioCriticoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ServicioCritico in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamServicioCritico() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        servicioCritico.setId(longCount.incrementAndGet());

        // Create the ServicioCritico
        ServicioCriticoDTO servicioCriticoDTO = servicioCriticoMapper.toDto(servicioCritico);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restServicioCriticoMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(servicioCriticoDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ServicioCritico in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateServicioCriticoWithPatch() throws Exception {
        // Initialize the database
        insertedServicioCritico = servicioCriticoRepository.saveAndFlush(servicioCritico);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the servicioCritico using partial update
        ServicioCritico partialUpdatedServicioCritico = new ServicioCritico();
        partialUpdatedServicioCritico.setId(servicioCritico.getId());

        partialUpdatedServicioCritico
            .prioridadEscalamiento(UPDATED_PRIORIDAD_ESCALAMIENTO)
            .requiereEscalamientoInmediato(UPDATED_REQUIERE_ESCALAMIENTO_INMEDIATO)
            .justificacion(UPDATED_JUSTIFICACION);

        restServicioCriticoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedServicioCritico.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedServicioCritico))
            )
            .andExpect(status().isOk());

        // Validate the ServicioCritico in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertServicioCriticoUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedServicioCritico, servicioCritico),
            getPersistedServicioCritico(servicioCritico)
        );
    }

    @Test
    @Transactional
    void fullUpdateServicioCriticoWithPatch() throws Exception {
        // Initialize the database
        insertedServicioCritico = servicioCriticoRepository.saveAndFlush(servicioCritico);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the servicioCritico using partial update
        ServicioCritico partialUpdatedServicioCritico = new ServicioCritico();
        partialUpdatedServicioCritico.setId(servicioCritico.getId());

        partialUpdatedServicioCritico
            .prioridadEscalamiento(UPDATED_PRIORIDAD_ESCALAMIENTO)
            .requiereEscalamientoInmediato(UPDATED_REQUIERE_ESCALAMIENTO_INMEDIATO)
            .justificacion(UPDATED_JUSTIFICACION);

        restServicioCriticoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedServicioCritico.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedServicioCritico))
            )
            .andExpect(status().isOk());

        // Validate the ServicioCritico in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertServicioCriticoUpdatableFieldsEquals(
            partialUpdatedServicioCritico,
            getPersistedServicioCritico(partialUpdatedServicioCritico)
        );
    }

    @Test
    @Transactional
    void patchNonExistingServicioCritico() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        servicioCritico.setId(longCount.incrementAndGet());

        // Create the ServicioCritico
        ServicioCriticoDTO servicioCriticoDTO = servicioCriticoMapper.toDto(servicioCritico);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restServicioCriticoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, servicioCriticoDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(servicioCriticoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ServicioCritico in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchServicioCritico() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        servicioCritico.setId(longCount.incrementAndGet());

        // Create the ServicioCritico
        ServicioCriticoDTO servicioCriticoDTO = servicioCriticoMapper.toDto(servicioCritico);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restServicioCriticoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(servicioCriticoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ServicioCritico in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamServicioCritico() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        servicioCritico.setId(longCount.incrementAndGet());

        // Create the ServicioCritico
        ServicioCriticoDTO servicioCriticoDTO = servicioCriticoMapper.toDto(servicioCritico);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restServicioCriticoMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(servicioCriticoDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ServicioCritico in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteServicioCritico() throws Exception {
        // Initialize the database
        insertedServicioCritico = servicioCriticoRepository.saveAndFlush(servicioCritico);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the servicioCritico
        restServicioCriticoMockMvc
            .perform(delete(ENTITY_API_URL_ID, servicioCritico.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return servicioCriticoRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected ServicioCritico getPersistedServicioCritico(ServicioCritico servicioCritico) {
        return servicioCriticoRepository.findById(servicioCritico.getId()).orElseThrow();
    }

    protected void assertPersistedServicioCriticoToMatchAllProperties(ServicioCritico expectedServicioCritico) {
        assertServicioCriticoAllPropertiesEquals(expectedServicioCritico, getPersistedServicioCritico(expectedServicioCritico));
    }

    protected void assertPersistedServicioCriticoToMatchUpdatableProperties(ServicioCritico expectedServicioCritico) {
        assertServicioCriticoAllUpdatablePropertiesEquals(expectedServicioCritico, getPersistedServicioCritico(expectedServicioCritico));
    }
}
