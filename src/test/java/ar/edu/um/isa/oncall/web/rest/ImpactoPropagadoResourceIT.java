package ar.edu.um.isa.oncall.web.rest;

import static ar.edu.um.isa.oncall.domain.ImpactoPropagadoAsserts.*;
import static ar.edu.um.isa.oncall.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import ar.edu.um.isa.oncall.IntegrationTest;
import ar.edu.um.isa.oncall.domain.ImpactoPropagado;
import ar.edu.um.isa.oncall.domain.Incidente;
import ar.edu.um.isa.oncall.domain.Servicio;
import ar.edu.um.isa.oncall.domain.enumeration.EstadoImpacto;
import ar.edu.um.isa.oncall.repository.ImpactoPropagadoRepository;
import ar.edu.um.isa.oncall.service.ImpactoPropagadoService;
import ar.edu.um.isa.oncall.service.dto.ImpactoPropagadoDTO;
import ar.edu.um.isa.oncall.service.mapper.ImpactoPropagadoMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.time.Instant;
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
 * Integration tests for the {@link ImpactoPropagadoResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class ImpactoPropagadoResourceIT {

    private static final Instant DEFAULT_DETECTADO_EN = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DETECTADO_EN = Instant.ofEpochMilli(1701729143509L);

    private static final EstadoImpacto DEFAULT_ESTADO = EstadoImpacto.DETECTADO;
    private static final EstadoImpacto UPDATED_ESTADO = EstadoImpacto.NOTIFICADO;

    private static final String DEFAULT_DETALLE = "AAAAAAAAAA";
    private static final String UPDATED_DETALLE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/impacto-propagados";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ImpactoPropagadoRepository impactoPropagadoRepository;

    @Mock
    private ImpactoPropagadoRepository impactoPropagadoRepositoryMock;

    @Autowired
    private ImpactoPropagadoMapper impactoPropagadoMapper;

    @Mock
    private ImpactoPropagadoService impactoPropagadoServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restImpactoPropagadoMockMvc;

    private ImpactoPropagado impactoPropagado;

    private ImpactoPropagado insertedImpactoPropagado;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ImpactoPropagado createEntity(EntityManager em) {
        ImpactoPropagado impactoPropagado = new ImpactoPropagado()
            .detectadoEn(DEFAULT_DETECTADO_EN)
            .estado(DEFAULT_ESTADO)
            .detalle(DEFAULT_DETALLE);
        // Add required entity
        Incidente incidente;
        if (TestUtil.findAll(em, Incidente.class).isEmpty()) {
            incidente = IncidenteResourceIT.createEntity();
            em.persist(incidente);
            em.flush();
        } else {
            incidente = TestUtil.findAll(em, Incidente.class).get(0);
        }
        impactoPropagado.setIncidente(incidente);
        // Add required entity
        Servicio servicio;
        if (TestUtil.findAll(em, Servicio.class).isEmpty()) {
            servicio = ServicioResourceIT.createEntity(em);
            em.persist(servicio);
            em.flush();
        } else {
            servicio = TestUtil.findAll(em, Servicio.class).get(0);
        }
        impactoPropagado.setServicioAfectado(servicio);
        return impactoPropagado;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ImpactoPropagado createUpdatedEntity(EntityManager em) {
        ImpactoPropagado updatedImpactoPropagado = new ImpactoPropagado()
            .detectadoEn(UPDATED_DETECTADO_EN)
            .estado(UPDATED_ESTADO)
            .detalle(UPDATED_DETALLE);
        // Add required entity
        Incidente incidente;
        if (TestUtil.findAll(em, Incidente.class).isEmpty()) {
            incidente = IncidenteResourceIT.createUpdatedEntity();
            em.persist(incidente);
            em.flush();
        } else {
            incidente = TestUtil.findAll(em, Incidente.class).get(0);
        }
        updatedImpactoPropagado.setIncidente(incidente);
        // Add required entity
        Servicio servicio;
        if (TestUtil.findAll(em, Servicio.class).isEmpty()) {
            servicio = ServicioResourceIT.createUpdatedEntity(em);
            em.persist(servicio);
            em.flush();
        } else {
            servicio = TestUtil.findAll(em, Servicio.class).get(0);
        }
        updatedImpactoPropagado.setServicioAfectado(servicio);
        return updatedImpactoPropagado;
    }

    @BeforeEach
    void initTest() {
        impactoPropagado = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedImpactoPropagado != null) {
            impactoPropagadoRepository.delete(insertedImpactoPropagado);
            insertedImpactoPropagado = null;
        }
    }

    @Test
    @Transactional
    void createImpactoPropagado() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the ImpactoPropagado
        ImpactoPropagadoDTO impactoPropagadoDTO = impactoPropagadoMapper.toDto(impactoPropagado);
        var returnedImpactoPropagadoDTO = om.readValue(
            restImpactoPropagadoMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(impactoPropagadoDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ImpactoPropagadoDTO.class
        );

        // Validate the ImpactoPropagado in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedImpactoPropagado = impactoPropagadoMapper.toEntity(returnedImpactoPropagadoDTO);
        assertImpactoPropagadoUpdatableFieldsEquals(returnedImpactoPropagado, getPersistedImpactoPropagado(returnedImpactoPropagado));

        insertedImpactoPropagado = returnedImpactoPropagado;
    }

    @Test
    @Transactional
    void createImpactoPropagadoWithExistingId() throws Exception {
        // Create the ImpactoPropagado with an existing ID
        impactoPropagado.setId(1L);
        ImpactoPropagadoDTO impactoPropagadoDTO = impactoPropagadoMapper.toDto(impactoPropagado);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restImpactoPropagadoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(impactoPropagadoDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ImpactoPropagado in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkDetectadoEnIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        impactoPropagado.setDetectadoEn(null);

        // Create the ImpactoPropagado, which fails.
        ImpactoPropagadoDTO impactoPropagadoDTO = impactoPropagadoMapper.toDto(impactoPropagado);

        restImpactoPropagadoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(impactoPropagadoDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEstadoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        impactoPropagado.setEstado(null);

        // Create the ImpactoPropagado, which fails.
        ImpactoPropagadoDTO impactoPropagadoDTO = impactoPropagadoMapper.toDto(impactoPropagado);

        restImpactoPropagadoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(impactoPropagadoDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllImpactoPropagados() throws Exception {
        // Initialize the database
        insertedImpactoPropagado = impactoPropagadoRepository.saveAndFlush(impactoPropagado);

        // Get all the impactoPropagadoList
        restImpactoPropagadoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(impactoPropagado.getId().intValue())))
            .andExpect(jsonPath("$.[*].detectadoEn").value(hasItem(DEFAULT_DETECTADO_EN.toString())))
            .andExpect(jsonPath("$.[*].estado").value(hasItem(DEFAULT_ESTADO.toString())))
            .andExpect(jsonPath("$.[*].detalle").value(hasItem(DEFAULT_DETALLE)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllImpactoPropagadosWithEagerRelationshipsIsEnabled() throws Exception {
        when(impactoPropagadoServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restImpactoPropagadoMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(impactoPropagadoServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllImpactoPropagadosWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(impactoPropagadoServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restImpactoPropagadoMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(impactoPropagadoRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getImpactoPropagado() throws Exception {
        // Initialize the database
        insertedImpactoPropagado = impactoPropagadoRepository.saveAndFlush(impactoPropagado);

        // Get the impactoPropagado
        restImpactoPropagadoMockMvc
            .perform(get(ENTITY_API_URL_ID, impactoPropagado.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(impactoPropagado.getId().intValue()))
            .andExpect(jsonPath("$.detectadoEn").value(DEFAULT_DETECTADO_EN.toString()))
            .andExpect(jsonPath("$.estado").value(DEFAULT_ESTADO.toString()))
            .andExpect(jsonPath("$.detalle").value(DEFAULT_DETALLE));
    }

    @Test
    @Transactional
    void getNonExistingImpactoPropagado() throws Exception {
        // Get the impactoPropagado
        restImpactoPropagadoMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingImpactoPropagado() throws Exception {
        // Initialize the database
        insertedImpactoPropagado = impactoPropagadoRepository.saveAndFlush(impactoPropagado);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the impactoPropagado
        ImpactoPropagado updatedImpactoPropagado = impactoPropagadoRepository.findById(impactoPropagado.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedImpactoPropagado are not directly saved in db
        em.detach(updatedImpactoPropagado);
        updatedImpactoPropagado.detectadoEn(UPDATED_DETECTADO_EN).estado(UPDATED_ESTADO).detalle(UPDATED_DETALLE);
        ImpactoPropagadoDTO impactoPropagadoDTO = impactoPropagadoMapper.toDto(updatedImpactoPropagado);

        restImpactoPropagadoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, impactoPropagadoDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(impactoPropagadoDTO))
            )
            .andExpect(status().isOk());

        // Validate the ImpactoPropagado in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedImpactoPropagadoToMatchAllProperties(updatedImpactoPropagado);
    }

    @Test
    @Transactional
    void putNonExistingImpactoPropagado() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        impactoPropagado.setId(longCount.incrementAndGet());

        // Create the ImpactoPropagado
        ImpactoPropagadoDTO impactoPropagadoDTO = impactoPropagadoMapper.toDto(impactoPropagado);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restImpactoPropagadoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, impactoPropagadoDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(impactoPropagadoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ImpactoPropagado in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchImpactoPropagado() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        impactoPropagado.setId(longCount.incrementAndGet());

        // Create the ImpactoPropagado
        ImpactoPropagadoDTO impactoPropagadoDTO = impactoPropagadoMapper.toDto(impactoPropagado);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restImpactoPropagadoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(impactoPropagadoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ImpactoPropagado in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamImpactoPropagado() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        impactoPropagado.setId(longCount.incrementAndGet());

        // Create the ImpactoPropagado
        ImpactoPropagadoDTO impactoPropagadoDTO = impactoPropagadoMapper.toDto(impactoPropagado);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restImpactoPropagadoMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(impactoPropagadoDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ImpactoPropagado in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateImpactoPropagadoWithPatch() throws Exception {
        // Initialize the database
        insertedImpactoPropagado = impactoPropagadoRepository.saveAndFlush(impactoPropagado);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the impactoPropagado using partial update
        ImpactoPropagado partialUpdatedImpactoPropagado = new ImpactoPropagado();
        partialUpdatedImpactoPropagado.setId(impactoPropagado.getId());

        partialUpdatedImpactoPropagado.estado(UPDATED_ESTADO).detalle(UPDATED_DETALLE);

        restImpactoPropagadoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedImpactoPropagado.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedImpactoPropagado))
            )
            .andExpect(status().isOk());

        // Validate the ImpactoPropagado in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertImpactoPropagadoUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedImpactoPropagado, impactoPropagado),
            getPersistedImpactoPropagado(impactoPropagado)
        );
    }

    @Test
    @Transactional
    void fullUpdateImpactoPropagadoWithPatch() throws Exception {
        // Initialize the database
        insertedImpactoPropagado = impactoPropagadoRepository.saveAndFlush(impactoPropagado);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the impactoPropagado using partial update
        ImpactoPropagado partialUpdatedImpactoPropagado = new ImpactoPropagado();
        partialUpdatedImpactoPropagado.setId(impactoPropagado.getId());

        partialUpdatedImpactoPropagado.detectadoEn(UPDATED_DETECTADO_EN).estado(UPDATED_ESTADO).detalle(UPDATED_DETALLE);

        restImpactoPropagadoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedImpactoPropagado.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedImpactoPropagado))
            )
            .andExpect(status().isOk());

        // Validate the ImpactoPropagado in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertImpactoPropagadoUpdatableFieldsEquals(
            partialUpdatedImpactoPropagado,
            getPersistedImpactoPropagado(partialUpdatedImpactoPropagado)
        );
    }

    @Test
    @Transactional
    void patchNonExistingImpactoPropagado() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        impactoPropagado.setId(longCount.incrementAndGet());

        // Create the ImpactoPropagado
        ImpactoPropagadoDTO impactoPropagadoDTO = impactoPropagadoMapper.toDto(impactoPropagado);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restImpactoPropagadoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, impactoPropagadoDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(impactoPropagadoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ImpactoPropagado in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchImpactoPropagado() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        impactoPropagado.setId(longCount.incrementAndGet());

        // Create the ImpactoPropagado
        ImpactoPropagadoDTO impactoPropagadoDTO = impactoPropagadoMapper.toDto(impactoPropagado);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restImpactoPropagadoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(impactoPropagadoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ImpactoPropagado in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamImpactoPropagado() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        impactoPropagado.setId(longCount.incrementAndGet());

        // Create the ImpactoPropagado
        ImpactoPropagadoDTO impactoPropagadoDTO = impactoPropagadoMapper.toDto(impactoPropagado);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restImpactoPropagadoMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(impactoPropagadoDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ImpactoPropagado in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteImpactoPropagado() throws Exception {
        // Initialize the database
        insertedImpactoPropagado = impactoPropagadoRepository.saveAndFlush(impactoPropagado);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the impactoPropagado
        restImpactoPropagadoMockMvc
            .perform(delete(ENTITY_API_URL_ID, impactoPropagado.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return impactoPropagadoRepository.count();
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

    protected ImpactoPropagado getPersistedImpactoPropagado(ImpactoPropagado impactoPropagado) {
        return impactoPropagadoRepository.findById(impactoPropagado.getId()).orElseThrow();
    }

    protected void assertPersistedImpactoPropagadoToMatchAllProperties(ImpactoPropagado expectedImpactoPropagado) {
        assertImpactoPropagadoAllPropertiesEquals(expectedImpactoPropagado, getPersistedImpactoPropagado(expectedImpactoPropagado));
    }

    protected void assertPersistedImpactoPropagadoToMatchUpdatableProperties(ImpactoPropagado expectedImpactoPropagado) {
        assertImpactoPropagadoAllUpdatablePropertiesEquals(
            expectedImpactoPropagado,
            getPersistedImpactoPropagado(expectedImpactoPropagado)
        );
    }
}
