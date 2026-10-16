package ar.edu.um.isa.oncall.web.rest;

import ar.edu.um.isa.oncall.repository.ServicioCriticoRepository;
import ar.edu.um.isa.oncall.service.ServicioCriticoService;
import ar.edu.um.isa.oncall.service.dto.ServicioCriticoDTO;
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
 * REST controller for managing {@link ar.edu.um.isa.oncall.domain.ServicioCritico}.
 */
@RestController
@RequestMapping("/api/servicio-criticos")
public class ServicioCriticoResource {

    private static final Logger LOG = LoggerFactory.getLogger(ServicioCriticoResource.class);

    private static final String ENTITY_NAME = "servicioCritico";

    @Value("${jhipster.clientApp.name:oncall}")
    private String applicationName;

    private final ServicioCriticoService servicioCriticoService;

    private final ServicioCriticoRepository servicioCriticoRepository;

    public ServicioCriticoResource(ServicioCriticoService servicioCriticoService, ServicioCriticoRepository servicioCriticoRepository) {
        this.servicioCriticoService = servicioCriticoService;
        this.servicioCriticoRepository = servicioCriticoRepository;
    }

    /**
     * {@code POST  /servicio-criticos} : Create a new servicioCritico.
     *
     * @param servicioCriticoDTO the servicioCriticoDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new servicioCriticoDTO, or with status {@code 400 (Bad Request)} if the servicioCritico has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ServicioCriticoDTO> createServicioCritico(@Valid @RequestBody ServicioCriticoDTO servicioCriticoDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save ServicioCritico : {}", servicioCriticoDTO);
        if (servicioCriticoDTO.getId() != null) {
            throw new BadRequestAlertException("A new servicioCritico cannot already have an ID", ENTITY_NAME, "idexists");
        }
        servicioCriticoDTO = servicioCriticoService.save(servicioCriticoDTO);
        return ResponseEntity.created(new URI("/api/servicio-criticos/" + servicioCriticoDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, servicioCriticoDTO.getId().toString()))
            .body(servicioCriticoDTO);
    }

    /**
     * {@code PUT  /servicio-criticos/:id} : Updates an existing servicioCritico.
     *
     * @param id the id of the servicioCriticoDTO to save.
     * @param servicioCriticoDTO the servicioCriticoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated servicioCriticoDTO,
     * or with status {@code 400 (Bad Request)} if the servicioCriticoDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the servicioCriticoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ServicioCriticoDTO> updateServicioCritico(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ServicioCriticoDTO servicioCriticoDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update ServicioCritico : {}, {}", id, servicioCriticoDTO);
        if (servicioCriticoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, servicioCriticoDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!servicioCriticoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        servicioCriticoDTO = servicioCriticoService.update(servicioCriticoDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, servicioCriticoDTO.getId().toString()))
            .body(servicioCriticoDTO);
    }

    /**
     * {@code PATCH  /servicio-criticos/:id} : Partial updates given fields of an existing servicioCritico, field will ignore if it is null
     *
     * @param id the id of the servicioCriticoDTO to save.
     * @param servicioCriticoDTO the servicioCriticoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated servicioCriticoDTO,
     * or with status {@code 400 (Bad Request)} if the servicioCriticoDTO is not valid,
     * or with status {@code 404 (Not Found)} if the servicioCriticoDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the servicioCriticoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ServicioCriticoDTO> partialUpdateServicioCritico(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ServicioCriticoDTO servicioCriticoDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update ServicioCritico partially : {}, {}", id, servicioCriticoDTO);
        if (servicioCriticoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, servicioCriticoDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!servicioCriticoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ServicioCriticoDTO> result = servicioCriticoService.partialUpdate(servicioCriticoDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, servicioCriticoDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /servicio-criticos} : get all the Servicio Criticos.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Servicio Criticos in body.
     */
    @GetMapping("")
    public List<ServicioCriticoDTO> getAllServicioCriticos(
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        LOG.debug("REST request to get all ServicioCriticos");
        return servicioCriticoService.findAll();
    }

    /**
     * {@code GET  /servicio-criticos/:id} : get the "id" servicioCritico.
     *
     * @param id the id of the servicioCriticoDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the servicioCriticoDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ServicioCriticoDTO> getServicioCritico(@PathVariable("id") Long id) {
        LOG.debug("REST request to get ServicioCritico : {}", id);
        Optional<ServicioCriticoDTO> servicioCriticoDTO = servicioCriticoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(servicioCriticoDTO);
    }

    /**
     * {@code DELETE  /servicio-criticos/:id} : delete the "id" servicioCritico.
     *
     * @param id the id of the servicioCriticoDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteServicioCritico(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete ServicioCritico : {}", id);
        servicioCriticoService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
