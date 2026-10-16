package ar.edu.um.isa.oncall.web.rest;

import ar.edu.um.isa.oncall.repository.DependenciaDeServicioRepository;
import ar.edu.um.isa.oncall.service.DependenciaDeServicioService;
import ar.edu.um.isa.oncall.service.dto.DependenciaDeServicioDTO;
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
 * REST controller for managing {@link ar.edu.um.isa.oncall.domain.DependenciaDeServicio}.
 */
@RestController
@RequestMapping("/api/dependencia-de-servicios")
public class DependenciaDeServicioResource {

    private static final Logger LOG = LoggerFactory.getLogger(DependenciaDeServicioResource.class);

    private static final String ENTITY_NAME = "dependenciaDeServicio";

    @Value("${jhipster.clientApp.name:oncall}")
    private String applicationName;

    private final DependenciaDeServicioService dependenciaDeServicioService;

    private final DependenciaDeServicioRepository dependenciaDeServicioRepository;

    public DependenciaDeServicioResource(
        DependenciaDeServicioService dependenciaDeServicioService,
        DependenciaDeServicioRepository dependenciaDeServicioRepository
    ) {
        this.dependenciaDeServicioService = dependenciaDeServicioService;
        this.dependenciaDeServicioRepository = dependenciaDeServicioRepository;
    }

    /**
     * {@code POST  /dependencia-de-servicios} : Create a new dependenciaDeServicio.
     *
     * @param dependenciaDeServicioDTO the dependenciaDeServicioDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new dependenciaDeServicioDTO, or with status {@code 400 (Bad Request)} if the dependenciaDeServicio has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<DependenciaDeServicioDTO> createDependenciaDeServicio(
        @Valid @RequestBody DependenciaDeServicioDTO dependenciaDeServicioDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to save DependenciaDeServicio : {}", dependenciaDeServicioDTO);
        if (dependenciaDeServicioDTO.getId() != null) {
            throw new BadRequestAlertException("A new dependenciaDeServicio cannot already have an ID", ENTITY_NAME, "idexists");
        }
        dependenciaDeServicioDTO = dependenciaDeServicioService.save(dependenciaDeServicioDTO);
        return ResponseEntity.created(new URI("/api/dependencia-de-servicios/" + dependenciaDeServicioDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, dependenciaDeServicioDTO.getId().toString()))
            .body(dependenciaDeServicioDTO);
    }

    /**
     * {@code PUT  /dependencia-de-servicios/:id} : Updates an existing dependenciaDeServicio.
     *
     * @param id the id of the dependenciaDeServicioDTO to save.
     * @param dependenciaDeServicioDTO the dependenciaDeServicioDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated dependenciaDeServicioDTO,
     * or with status {@code 400 (Bad Request)} if the dependenciaDeServicioDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the dependenciaDeServicioDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<DependenciaDeServicioDTO> updateDependenciaDeServicio(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody DependenciaDeServicioDTO dependenciaDeServicioDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update DependenciaDeServicio : {}, {}", id, dependenciaDeServicioDTO);
        if (dependenciaDeServicioDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, dependenciaDeServicioDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!dependenciaDeServicioRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        dependenciaDeServicioDTO = dependenciaDeServicioService.update(dependenciaDeServicioDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, dependenciaDeServicioDTO.getId().toString()))
            .body(dependenciaDeServicioDTO);
    }

    /**
     * {@code PATCH  /dependencia-de-servicios/:id} : Partial updates given fields of an existing dependenciaDeServicio, field will ignore if it is null
     *
     * @param id the id of the dependenciaDeServicioDTO to save.
     * @param dependenciaDeServicioDTO the dependenciaDeServicioDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated dependenciaDeServicioDTO,
     * or with status {@code 400 (Bad Request)} if the dependenciaDeServicioDTO is not valid,
     * or with status {@code 404 (Not Found)} if the dependenciaDeServicioDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the dependenciaDeServicioDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<DependenciaDeServicioDTO> partialUpdateDependenciaDeServicio(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody DependenciaDeServicioDTO dependenciaDeServicioDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update DependenciaDeServicio partially : {}, {}", id, dependenciaDeServicioDTO);
        if (dependenciaDeServicioDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, dependenciaDeServicioDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!dependenciaDeServicioRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<DependenciaDeServicioDTO> result = dependenciaDeServicioService.partialUpdate(dependenciaDeServicioDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, dependenciaDeServicioDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /dependencia-de-servicios} : get all the Dependencia De Servicios.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Dependencia De Servicios in body.
     */
    @GetMapping("")
    public List<DependenciaDeServicioDTO> getAllDependenciaDeServicios(
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        LOG.debug("REST request to get all DependenciaDeServicios");
        return dependenciaDeServicioService.findAll();
    }

    /**
     * {@code GET  /dependencia-de-servicios/:id} : get the "id" dependenciaDeServicio.
     *
     * @param id the id of the dependenciaDeServicioDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the dependenciaDeServicioDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<DependenciaDeServicioDTO> getDependenciaDeServicio(@PathVariable("id") Long id) {
        LOG.debug("REST request to get DependenciaDeServicio : {}", id);
        Optional<DependenciaDeServicioDTO> dependenciaDeServicioDTO = dependenciaDeServicioService.findOne(id);
        return ResponseUtil.wrapOrNotFound(dependenciaDeServicioDTO);
    }

    /**
     * {@code DELETE  /dependencia-de-servicios/:id} : delete the "id" dependenciaDeServicio.
     *
     * @param id the id of the dependenciaDeServicioDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDependenciaDeServicio(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete DependenciaDeServicio : {}", id);
        dependenciaDeServicioService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
