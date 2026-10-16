package ar.edu.um.isa.oncall.web.rest;

import static ar.edu.um.isa.oncall.domain.DependenciaDeServicioAsserts.*;
import static ar.edu.um.isa.oncall.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import ar.edu.um.isa.oncall.IntegrationTest;
import ar.edu.um.isa.oncall.domain.DependenciaDeServicio;
import ar.edu.um.isa.oncall.domain.Servicio;
import ar.edu.um.isa.oncall.domain.enumeration.TipoDependencia;
import ar.edu.um.isa.oncall.repository.DependenciaDeServicioRepository;
import ar.edu.um.isa.oncall.service.DependenciaDeServicioService;
import ar.edu.um.isa.oncall.service.dto.DependenciaDeServicioDTO;
import ar.edu.um.isa.oncall.service.mapper.DependenciaDeServicioMapper;
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
 * Integration tests for the {@link DependenciaDeServicioResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class DependenciaDeServicioResourceIT {

    private static final TipoDependencia DEFAULT_TIPO = TipoDependencia.OBLIGATORIA;
    private static final TipoDependencia UPDATED_TIPO = TipoDependencia.OPCIONAL;

    private static final Boolean DEFAULT_ACTIVA = false;
    private static final Boolean UPDATED_ACTIVA = true;

    private static final String DEFAULT_DESCRIPCION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPCION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/dependencia-de-servicios";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    @Autowired
    private ObjectMapper om;

    @Autowired
    private DependenciaDeServicioRepository dependenciaDeServicioRepository;

    @Mock
    private DependenciaDeServicioRepository dependenciaDeServicioRepositoryMock;

    @Autowired
    private DependenciaDeServicioMapper dependenciaDeServicioMapper;

    @Mock
    private DependenciaDeServicioService dependenciaDeServicioServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDependenciaDeServicioMockMvc;

    private DependenciaDeServicio dependenciaDeServicio;

    private DependenciaDeServicio insertedDependenciaDeServicio;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DependenciaDeServicio createEntity(EntityManager em) {
        DependenciaDeServicio dependenciaDeServicio = new DependenciaDeServicio()
            .tipo(DEFAULT_TIPO)
            .activa(DEFAULT_ACTIVA)
            .descripcion(DEFAULT_DESCRIPCION);
        // Add required entity
        Servicio servicio;
        if (TestUtil.findAll(em, Servicio.class).isEmpty()) {
            servicio = ServicioResourceIT.createEntity(em);
            em.persist(servicio);
            em.flush();
        } else {
            servicio = TestUtil.findAll(em, Servicio.class).get(0);
        }
        dependenciaDeServicio.setServicioDependiente(servicio);
        // Add required entity
        dependenciaDeServicio.setServicioProveedor(servicio);
        return dependenciaDeServicio;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DependenciaDeServicio createUpdatedEntity(EntityManager em) {
        DependenciaDeServicio updatedDependenciaDeServicio = new DependenciaDeServicio()
            .tipo(UPDATED_TIPO)
            .activa(UPDATED_ACTIVA)
            .descripcion(UPDATED_DESCRIPCION);
        // Add required entity
        Servicio servicio;
        if (TestUtil.findAll(em, Servicio.class).isEmpty()) {
            servicio = ServicioResourceIT.createUpdatedEntity(em);
            em.persist(servicio);
            em.flush();
        } else {
            servicio = TestUtil.findAll(em, Servicio.class).get(0);
        }
        updatedDependenciaDeServicio.setServicioDependiente(servicio);
        // Add required entity
        updatedDependenciaDeServicio.setServicioProveedor(servicio);
        return updatedDependenciaDeServicio;
    }

    @BeforeEach
    void initTest() {
        dependenciaDeServicio = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedDependenciaDeServicio != null) {
            dependenciaDeServicioRepository.delete(insertedDependenciaDeServicio);
            insertedDependenciaDeServicio = null;
        }
    }

    @Test
    @Transactional
    void createDependenciaDeServicio() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the DependenciaDeServicio
        DependenciaDeServicioDTO dependenciaDeServicioDTO = dependenciaDeServicioMapper.toDto(dependenciaDeServicio);
        var returnedDependenciaDeServicioDTO = om.readValue(
            restDependenciaDeServicioMockMvc
                .perform(
                    post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(dependenciaDeServicioDTO))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            DependenciaDeServicioDTO.class
        );

        // Validate the DependenciaDeServicio in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedDependenciaDeServicio = dependenciaDeServicioMapper.toEntity(returnedDependenciaDeServicioDTO);
        assertDependenciaDeServicioUpdatableFieldsEquals(
            returnedDependenciaDeServicio,
            getPersistedDependenciaDeServicio(returnedDependenciaDeServicio)
        );

        insertedDependenciaDeServicio = returnedDependenciaDeServicio;
    }

    @Test
    @Transactional
    void createDependenciaDeServicioWithExistingId() throws Exception {
        // Create the DependenciaDeServicio with an existing ID
        dependenciaDeServicio.setId(1L);
        DependenciaDeServicioDTO dependenciaDeServicioDTO = dependenciaDeServicioMapper.toDto(dependenciaDeServicio);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDependenciaDeServicioMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(dependenciaDeServicioDTO)))
            .andExpect(status().isBadRequest());

        // Validate the DependenciaDeServicio in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTipoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        dependenciaDeServicio.setTipo(null);

        // Create the DependenciaDeServicio, which fails.
        DependenciaDeServicioDTO dependenciaDeServicioDTO = dependenciaDeServicioMapper.toDto(dependenciaDeServicio);

        restDependenciaDeServicioMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(dependenciaDeServicioDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkActivaIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        dependenciaDeServicio.setActiva(null);

        // Create the DependenciaDeServicio, which fails.
        DependenciaDeServicioDTO dependenciaDeServicioDTO = dependenciaDeServicioMapper.toDto(dependenciaDeServicio);

        restDependenciaDeServicioMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(dependenciaDeServicioDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllDependenciaDeServicios() throws Exception {
        // Initialize the database
        insertedDependenciaDeServicio = dependenciaDeServicioRepository.saveAndFlush(dependenciaDeServicio);

        // Get all the dependenciaDeServicioList
        restDependenciaDeServicioMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(dependenciaDeServicio.getId().intValue())))
            .andExpect(jsonPath("$.[*].tipo").value(hasItem(DEFAULT_TIPO.toString())))
            .andExpect(jsonPath("$.[*].activa").value(hasItem(DEFAULT_ACTIVA)))
            .andExpect(jsonPath("$.[*].descripcion").value(hasItem(DEFAULT_DESCRIPCION)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllDependenciaDeServiciosWithEagerRelationshipsIsEnabled() throws Exception {
        when(dependenciaDeServicioServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restDependenciaDeServicioMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(dependenciaDeServicioServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllDependenciaDeServiciosWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(dependenciaDeServicioServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restDependenciaDeServicioMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(dependenciaDeServicioRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getDependenciaDeServicio() throws Exception {
        // Initialize the database
        insertedDependenciaDeServicio = dependenciaDeServicioRepository.saveAndFlush(dependenciaDeServicio);

        // Get the dependenciaDeServicio
        restDependenciaDeServicioMockMvc
            .perform(get(ENTITY_API_URL_ID, dependenciaDeServicio.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(dependenciaDeServicio.getId().intValue()))
            .andExpect(jsonPath("$.tipo").value(DEFAULT_TIPO.toString()))
            .andExpect(jsonPath("$.activa").value(DEFAULT_ACTIVA))
            .andExpect(jsonPath("$.descripcion").value(DEFAULT_DESCRIPCION));
    }

    @Test
    @Transactional
    void getNonExistingDependenciaDeServicio() throws Exception {
        // Get the dependenciaDeServicio
        restDependenciaDeServicioMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingDependenciaDeServicio() throws Exception {
        // Initialize the database
        insertedDependenciaDeServicio = dependenciaDeServicioRepository.saveAndFlush(dependenciaDeServicio);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the dependenciaDeServicio
        DependenciaDeServicio updatedDependenciaDeServicio = dependenciaDeServicioRepository
            .findById(dependenciaDeServicio.getId())
            .orElseThrow();
        // Disconnect from session so that the updates on updatedDependenciaDeServicio are not directly saved in db
        em.detach(updatedDependenciaDeServicio);
        updatedDependenciaDeServicio.tipo(UPDATED_TIPO).activa(UPDATED_ACTIVA).descripcion(UPDATED_DESCRIPCION);
        DependenciaDeServicioDTO dependenciaDeServicioDTO = dependenciaDeServicioMapper.toDto(updatedDependenciaDeServicio);

        restDependenciaDeServicioMockMvc
            .perform(
                put(ENTITY_API_URL_ID, dependenciaDeServicioDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(dependenciaDeServicioDTO))
            )
            .andExpect(status().isOk());

        // Validate the DependenciaDeServicio in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedDependenciaDeServicioToMatchAllProperties(updatedDependenciaDeServicio);
    }

    @Test
    @Transactional
    void putNonExistingDependenciaDeServicio() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        dependenciaDeServicio.setId(longCount.incrementAndGet());

        // Create the DependenciaDeServicio
        DependenciaDeServicioDTO dependenciaDeServicioDTO = dependenciaDeServicioMapper.toDto(dependenciaDeServicio);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDependenciaDeServicioMockMvc
            .perform(
                put(ENTITY_API_URL_ID, dependenciaDeServicioDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(dependenciaDeServicioDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DependenciaDeServicio in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDependenciaDeServicio() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        dependenciaDeServicio.setId(longCount.incrementAndGet());

        // Create the DependenciaDeServicio
        DependenciaDeServicioDTO dependenciaDeServicioDTO = dependenciaDeServicioMapper.toDto(dependenciaDeServicio);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDependenciaDeServicioMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(dependenciaDeServicioDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DependenciaDeServicio in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDependenciaDeServicio() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        dependenciaDeServicio.setId(longCount.incrementAndGet());

        // Create the DependenciaDeServicio
        DependenciaDeServicioDTO dependenciaDeServicioDTO = dependenciaDeServicioMapper.toDto(dependenciaDeServicio);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDependenciaDeServicioMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(dependenciaDeServicioDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the DependenciaDeServicio in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDependenciaDeServicioWithPatch() throws Exception {
        // Initialize the database
        insertedDependenciaDeServicio = dependenciaDeServicioRepository.saveAndFlush(dependenciaDeServicio);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the dependenciaDeServicio using partial update
        DependenciaDeServicio partialUpdatedDependenciaDeServicio = new DependenciaDeServicio();
        partialUpdatedDependenciaDeServicio.setId(dependenciaDeServicio.getId());

        partialUpdatedDependenciaDeServicio.tipo(UPDATED_TIPO);

        restDependenciaDeServicioMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDependenciaDeServicio.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedDependenciaDeServicio))
            )
            .andExpect(status().isOk());

        // Validate the DependenciaDeServicio in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertDependenciaDeServicioUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedDependenciaDeServicio, dependenciaDeServicio),
            getPersistedDependenciaDeServicio(dependenciaDeServicio)
        );
    }

    @Test
    @Transactional
    void fullUpdateDependenciaDeServicioWithPatch() throws Exception {
        // Initialize the database
        insertedDependenciaDeServicio = dependenciaDeServicioRepository.saveAndFlush(dependenciaDeServicio);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the dependenciaDeServicio using partial update
        DependenciaDeServicio partialUpdatedDependenciaDeServicio = new DependenciaDeServicio();
        partialUpdatedDependenciaDeServicio.setId(dependenciaDeServicio.getId());

        partialUpdatedDependenciaDeServicio.tipo(UPDATED_TIPO).activa(UPDATED_ACTIVA).descripcion(UPDATED_DESCRIPCION);

        restDependenciaDeServicioMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDependenciaDeServicio.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedDependenciaDeServicio))
            )
            .andExpect(status().isOk());

        // Validate the DependenciaDeServicio in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertDependenciaDeServicioUpdatableFieldsEquals(
            partialUpdatedDependenciaDeServicio,
            getPersistedDependenciaDeServicio(partialUpdatedDependenciaDeServicio)
        );
    }

    @Test
    @Transactional
    void patchNonExistingDependenciaDeServicio() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        dependenciaDeServicio.setId(longCount.incrementAndGet());

        // Create the DependenciaDeServicio
        DependenciaDeServicioDTO dependenciaDeServicioDTO = dependenciaDeServicioMapper.toDto(dependenciaDeServicio);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDependenciaDeServicioMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, dependenciaDeServicioDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(dependenciaDeServicioDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DependenciaDeServicio in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDependenciaDeServicio() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        dependenciaDeServicio.setId(longCount.incrementAndGet());

        // Create the DependenciaDeServicio
        DependenciaDeServicioDTO dependenciaDeServicioDTO = dependenciaDeServicioMapper.toDto(dependenciaDeServicio);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDependenciaDeServicioMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(dependenciaDeServicioDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DependenciaDeServicio in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDependenciaDeServicio() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        dependenciaDeServicio.setId(longCount.incrementAndGet());

        // Create the DependenciaDeServicio
        DependenciaDeServicioDTO dependenciaDeServicioDTO = dependenciaDeServicioMapper.toDto(dependenciaDeServicio);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDependenciaDeServicioMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(dependenciaDeServicioDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the DependenciaDeServicio in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDependenciaDeServicio() throws Exception {
        // Initialize the database
        insertedDependenciaDeServicio = dependenciaDeServicioRepository.saveAndFlush(dependenciaDeServicio);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the dependenciaDeServicio
        restDependenciaDeServicioMockMvc
            .perform(delete(ENTITY_API_URL_ID, dependenciaDeServicio.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return dependenciaDeServicioRepository.count();
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

    protected DependenciaDeServicio getPersistedDependenciaDeServicio(DependenciaDeServicio dependenciaDeServicio) {
        return dependenciaDeServicioRepository.findById(dependenciaDeServicio.getId()).orElseThrow();
    }

    protected void assertPersistedDependenciaDeServicioToMatchAllProperties(DependenciaDeServicio expectedDependenciaDeServicio) {
        assertDependenciaDeServicioAllPropertiesEquals(
            expectedDependenciaDeServicio,
            getPersistedDependenciaDeServicio(expectedDependenciaDeServicio)
        );
    }

    protected void assertPersistedDependenciaDeServicioToMatchUpdatableProperties(DependenciaDeServicio expectedDependenciaDeServicio) {
        assertDependenciaDeServicioAllUpdatablePropertiesEquals(
            expectedDependenciaDeServicio,
            getPersistedDependenciaDeServicio(expectedDependenciaDeServicio)
        );
    }
}
