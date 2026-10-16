package ar.edu.um.isa.oncall.service;

import ar.edu.um.isa.oncall.domain.ServicioCritico;
import ar.edu.um.isa.oncall.repository.ServicioCriticoRepository;
import ar.edu.um.isa.oncall.service.dto.ServicioCriticoDTO;
import ar.edu.um.isa.oncall.service.mapper.ServicioCriticoMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ar.edu.um.isa.oncall.domain.ServicioCritico}.
 */
@Service
@Transactional
public class ServicioCriticoService {

    private static final Logger LOG = LoggerFactory.getLogger(ServicioCriticoService.class);

    private final ServicioCriticoRepository servicioCriticoRepository;

    private final ServicioCriticoMapper servicioCriticoMapper;

    public ServicioCriticoService(ServicioCriticoRepository servicioCriticoRepository, ServicioCriticoMapper servicioCriticoMapper) {
        this.servicioCriticoRepository = servicioCriticoRepository;
        this.servicioCriticoMapper = servicioCriticoMapper;
    }

    /**
     * Save a servicioCritico.
     *
     * @param servicioCriticoDTO the entity to save.
     * @return the persisted entity.
     */
    public ServicioCriticoDTO save(ServicioCriticoDTO servicioCriticoDTO) {
        LOG.debug("Request to save ServicioCritico : {}", servicioCriticoDTO);
        ServicioCritico servicioCritico = servicioCriticoMapper.toEntity(servicioCriticoDTO);
        servicioCritico = servicioCriticoRepository.save(servicioCritico);
        return servicioCriticoMapper.toDto(servicioCritico);
    }

    /**
     * Update a servicioCritico.
     *
     * @param servicioCriticoDTO the entity to save.
     * @return the persisted entity.
     */
    public ServicioCriticoDTO update(ServicioCriticoDTO servicioCriticoDTO) {
        LOG.debug("Request to update ServicioCritico : {}", servicioCriticoDTO);
        ServicioCritico servicioCritico = servicioCriticoMapper.toEntity(servicioCriticoDTO);
        servicioCritico = servicioCriticoRepository.save(servicioCritico);
        return servicioCriticoMapper.toDto(servicioCritico);
    }

    /**
     * Partially update a servicioCritico.
     *
     * @param servicioCriticoDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ServicioCriticoDTO> partialUpdate(ServicioCriticoDTO servicioCriticoDTO) {
        LOG.debug("Request to partially update ServicioCritico : {}", servicioCriticoDTO);

        return servicioCriticoRepository
            .findById(servicioCriticoDTO.getId())
            .map(existingServicioCritico -> {
                servicioCriticoMapper.partialUpdate(existingServicioCritico, servicioCriticoDTO);

                return existingServicioCritico;
            })
            .map(servicioCriticoRepository::save)
            .map(servicioCriticoMapper::toDto);
    }

    /**
     * Get all the servicioCriticos.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<ServicioCriticoDTO> findAll() {
        LOG.debug("Request to get all ServicioCriticos");
        return servicioCriticoRepository
            .findAll()
            .stream()
            .map(servicioCriticoMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get all the servicioCriticos with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<ServicioCriticoDTO> findAllWithEagerRelationships(Pageable pageable) {
        return servicioCriticoRepository.findAllWithEagerRelationships(pageable).map(servicioCriticoMapper::toDto);
    }

    /**
     * Get one servicioCritico by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ServicioCriticoDTO> findOne(Long id) {
        LOG.debug("Request to get ServicioCritico : {}", id);
        return servicioCriticoRepository.findOneWithEagerRelationships(id).map(servicioCriticoMapper::toDto);
    }

    /**
     * Delete the servicioCritico by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete ServicioCritico : {}", id);
        servicioCriticoRepository.deleteById(id);
    }
}
