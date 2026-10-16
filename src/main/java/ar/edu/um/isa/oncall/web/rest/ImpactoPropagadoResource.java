package ar.edu.um.isa.oncall.web.rest;

import ar.edu.um.isa.oncall.repository.ImpactoPropagadoRepository;
import ar.edu.um.isa.oncall.service.ImpactoPropagadoService;
import ar.edu.um.isa.oncall.service.dto.ImpactoPropagadoDTO;
import ar.edu.um.isa.oncall.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link ar.edu.um.isa.oncall.domain.ImpactoPropagado}.
 */
@RestController
@RequestMapping("/api/impacto-propagados")
public class ImpactoPropagadoResource {

    private static final Logger LOG = LoggerFactory.getLogger(ImpactoPropagadoResource.class);

    private static final String ENTITY_NAME = "impactoPropagado";

    @Value("${jhipster.clientApp.name:oncall}")
    private String applicationName;

    private final ImpactoPropagadoService impactoPropagadoService;

    private final ImpactoPropagadoRepository impactoPropagadoRepository;

    public ImpactoPropagadoResource(
        ImpactoPropagadoService impactoPropagadoService,
        ImpactoPropagadoRepository impactoPropagadoRepository
    ) {
        this.impactoPropagadoService = impactoPropagadoService;
        this.impactoPropagadoRepository = impactoPropagadoRepository;
    }

    /**
     * {@code POST  /impacto-propagados} : Create a new impactoPropagado.
     *
     * @param impactoPropagadoDTO the impactoPropagadoDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new impactoPropagadoDTO, or with status {@code 400 (Bad Request)} if the impactoPropagado has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ImpactoPropagadoDTO> createImpactoPropagado(@Valid @RequestBody ImpactoPropagadoDTO impactoPropagadoDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save ImpactoPropagado : {}", impactoPropagadoDTO);
        if (impactoPropagadoDTO.getId() != null) {
            throw new BadRequestAlertException("A new impactoPropagado cannot already have an ID", ENTITY_NAME, "idexists");
        }
        impactoPropagadoDTO = impactoPropagadoService.save(impactoPropagadoDTO);
        return ResponseEntity.created(new URI("/api/impacto-propagados/" + impactoPropagadoDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, impactoPropagadoDTO.getId().toString()))
            .body(impactoPropagadoDTO);
    }

    /**
     * {@code PUT  /impacto-propagados/:id} : Updates an existing impactoPropagado.
     *
     * @param id the id of the impactoPropagadoDTO to save.
     * @param impactoPropagadoDTO the impactoPropagadoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated impactoPropagadoDTO,
     * or with status {@code 400 (Bad Request)} if the impactoPropagadoDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the impactoPropagadoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ImpactoPropagadoDTO> updateImpactoPropagado(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ImpactoPropagadoDTO impactoPropagadoDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update ImpactoPropagado : {}, {}", id, impactoPropagadoDTO);
        if (impactoPropagadoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, impactoPropagadoDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!impactoPropagadoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        impactoPropagadoDTO = impactoPropagadoService.update(impactoPropagadoDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, impactoPropagadoDTO.getId().toString()))
            .body(impactoPropagadoDTO);
    }

    /**
     * {@code PATCH  /impacto-propagados/:id} : Partial updates given fields of an existing impactoPropagado, field will ignore if it is null
     *
     * @param id the id of the impactoPropagadoDTO to save.
     * @param impactoPropagadoDTO the impactoPropagadoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated impactoPropagadoDTO,
     * or with status {@code 400 (Bad Request)} if the impactoPropagadoDTO is not valid,
     * or with status {@code 404 (Not Found)} if the impactoPropagadoDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the impactoPropagadoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ImpactoPropagadoDTO> partialUpdateImpactoPropagado(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ImpactoPropagadoDTO impactoPropagadoDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update ImpactoPropagado partially : {}, {}", id, impactoPropagadoDTO);
        if (impactoPropagadoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, impactoPropagadoDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!impactoPropagadoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ImpactoPropagadoDTO> result = impactoPropagadoService.partialUpdate(impactoPropagadoDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, impactoPropagadoDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /impacto-propagados} : get all the Impacto Propagados.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Impacto Propagados in body.
     */
    @GetMapping("")
    public List<ImpactoPropagadoDTO> getAllImpactoPropagados(
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        LOG.debug("REST request to get all ImpactoPropagados");
        return impactoPropagadoService.findAll();
    }

    /**
     * {@code GET  /impacto-propagados/:id} : get the "id" impactoPropagado.
     *
     * @param id the id of the impactoPropagadoDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the impactoPropagadoDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ImpactoPropagadoDTO> getImpactoPropagado(@PathVariable("id") Long id) {
        LOG.debug("REST request to get ImpactoPropagado : {}", id);
        Optional<ImpactoPropagadoDTO> impactoPropagadoDTO = impactoPropagadoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(impactoPropagadoDTO);
    }

    /**
     * {@code DELETE  /impacto-propagados/:id} : delete the "id" impactoPropagado.
     *
     * @param id the id of the impactoPropagadoDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteImpactoPropagado(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete ImpactoPropagado : {}", id);
        impactoPropagadoService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
