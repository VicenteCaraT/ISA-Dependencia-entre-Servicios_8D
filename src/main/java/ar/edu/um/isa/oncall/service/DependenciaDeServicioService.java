package ar.edu.um.isa.oncall.service;

import ar.edu.um.isa.oncall.domain.DependenciaDeServicio;
import ar.edu.um.isa.oncall.repository.DependenciaDeServicioRepository;
import ar.edu.um.isa.oncall.service.dto.DependenciaDeServicioDTO;
import ar.edu.um.isa.oncall.service.mapper.DependenciaDeServicioMapper;
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
 * Service Implementation for managing {@link ar.edu.um.isa.oncall.domain.DependenciaDeServicio}.
 */
@Service
@Transactional
public class DependenciaDeServicioService {

    private static final Logger LOG = LoggerFactory.getLogger(DependenciaDeServicioService.class);

    private final DependenciaDeServicioRepository dependenciaDeServicioRepository;

    private final DependenciaDeServicioMapper dependenciaDeServicioMapper;

    public DependenciaDeServicioService(
        DependenciaDeServicioRepository dependenciaDeServicioRepository,
        DependenciaDeServicioMapper dependenciaDeServicioMapper
    ) {
        this.dependenciaDeServicioRepository = dependenciaDeServicioRepository;
        this.dependenciaDeServicioMapper = dependenciaDeServicioMapper;
    }

    /**
     * Save a dependenciaDeServicio.
     *
     * @param dependenciaDeServicioDTO the entity to save.
     * @return the persisted entity.
     */
    public DependenciaDeServicioDTO save(DependenciaDeServicioDTO dependenciaDeServicioDTO) {
        LOG.debug("Request to save DependenciaDeServicio : {}", dependenciaDeServicioDTO);
        DependenciaDeServicio dependenciaDeServicio = dependenciaDeServicioMapper.toEntity(dependenciaDeServicioDTO);
        dependenciaDeServicio = dependenciaDeServicioRepository.save(dependenciaDeServicio);
        return dependenciaDeServicioMapper.toDto(dependenciaDeServicio);
    }

    /**
     * Update a dependenciaDeServicio.
     *
     * @param dependenciaDeServicioDTO the entity to save.
     * @return the persisted entity.
     */
    public DependenciaDeServicioDTO update(DependenciaDeServicioDTO dependenciaDeServicioDTO) {
        LOG.debug("Request to update DependenciaDeServicio : {}", dependenciaDeServicioDTO);
        DependenciaDeServicio dependenciaDeServicio = dependenciaDeServicioMapper.toEntity(dependenciaDeServicioDTO);
        dependenciaDeServicio = dependenciaDeServicioRepository.save(dependenciaDeServicio);
        return dependenciaDeServicioMapper.toDto(dependenciaDeServicio);
    }

    /**
     * Partially update a dependenciaDeServicio.
     *
     * @param dependenciaDeServicioDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<DependenciaDeServicioDTO> partialUpdate(DependenciaDeServicioDTO dependenciaDeServicioDTO) {
        LOG.debug("Request to partially update DependenciaDeServicio : {}", dependenciaDeServicioDTO);

        return dependenciaDeServicioRepository
            .findById(dependenciaDeServicioDTO.getId())
            .map(existingDependenciaDeServicio -> {
                dependenciaDeServicioMapper.partialUpdate(existingDependenciaDeServicio, dependenciaDeServicioDTO);

                return existingDependenciaDeServicio;
            })
            .map(dependenciaDeServicioRepository::save)
            .map(dependenciaDeServicioMapper::toDto);
    }

    /**
     * Get all the dependenciaDeServicios.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<DependenciaDeServicioDTO> findAll() {
        LOG.debug("Request to get all DependenciaDeServicios");
        return dependenciaDeServicioRepository
            .findAll()
            .stream()
            .map(dependenciaDeServicioMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get all the dependenciaDeServicios with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<DependenciaDeServicioDTO> findAllWithEagerRelationships(Pageable pageable) {
        return dependenciaDeServicioRepository.findAllWithEagerRelationships(pageable).map(dependenciaDeServicioMapper::toDto);
    }

    /**
     * Get one dependenciaDeServicio by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<DependenciaDeServicioDTO> findOne(Long id) {
        LOG.debug("Request to get DependenciaDeServicio : {}", id);
        return dependenciaDeServicioRepository.findOneWithEagerRelationships(id).map(dependenciaDeServicioMapper::toDto);
    }

    /**
     * Delete the dependenciaDeServicio by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete DependenciaDeServicio : {}", id);
        dependenciaDeServicioRepository.deleteById(id);
    }
}
