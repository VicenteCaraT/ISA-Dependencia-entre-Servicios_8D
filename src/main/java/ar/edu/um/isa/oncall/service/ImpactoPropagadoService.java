package ar.edu.um.isa.oncall.service;

import ar.edu.um.isa.oncall.domain.ImpactoPropagado;
import ar.edu.um.isa.oncall.repository.ImpactoPropagadoRepository;
import ar.edu.um.isa.oncall.service.dto.ImpactoPropagadoDTO;
import ar.edu.um.isa.oncall.service.mapper.ImpactoPropagadoMapper;
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
 * Service Implementation for managing {@link ar.edu.um.isa.oncall.domain.ImpactoPropagado}.
 */
@Service
@Transactional
public class ImpactoPropagadoService {

    private static final Logger LOG = LoggerFactory.getLogger(ImpactoPropagadoService.class);

    private final ImpactoPropagadoRepository impactoPropagadoRepository;

    private final ImpactoPropagadoMapper impactoPropagadoMapper;

    public ImpactoPropagadoService(ImpactoPropagadoRepository impactoPropagadoRepository, ImpactoPropagadoMapper impactoPropagadoMapper) {
        this.impactoPropagadoRepository = impactoPropagadoRepository;
        this.impactoPropagadoMapper = impactoPropagadoMapper;
    }

    /**
     * Save a impactoPropagado.
     *
     * @param impactoPropagadoDTO the entity to save.
     * @return the persisted entity.
     */
    public ImpactoPropagadoDTO save(ImpactoPropagadoDTO impactoPropagadoDTO) {
        LOG.debug("Request to save ImpactoPropagado : {}", impactoPropagadoDTO);
        ImpactoPropagado impactoPropagado = impactoPropagadoMapper.toEntity(impactoPropagadoDTO);
        impactoPropagado = impactoPropagadoRepository.save(impactoPropagado);
        return impactoPropagadoMapper.toDto(impactoPropagado);
    }

    /**
     * Update a impactoPropagado.
     *
     * @param impactoPropagadoDTO the entity to save.
     * @return the persisted entity.
     */
    public ImpactoPropagadoDTO update(ImpactoPropagadoDTO impactoPropagadoDTO) {
        LOG.debug("Request to update ImpactoPropagado : {}", impactoPropagadoDTO);
        ImpactoPropagado impactoPropagado = impactoPropagadoMapper.toEntity(impactoPropagadoDTO);
        impactoPropagado = impactoPropagadoRepository.save(impactoPropagado);
        return impactoPropagadoMapper.toDto(impactoPropagado);
    }

    /**
     * Partially update a impactoPropagado.
     *
     * @param impactoPropagadoDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ImpactoPropagadoDTO> partialUpdate(ImpactoPropagadoDTO impactoPropagadoDTO) {
        LOG.debug("Request to partially update ImpactoPropagado : {}", impactoPropagadoDTO);

        return impactoPropagadoRepository
            .findById(impactoPropagadoDTO.getId())
            .map(existingImpactoPropagado -> {
                impactoPropagadoMapper.partialUpdate(existingImpactoPropagado, impactoPropagadoDTO);

                return existingImpactoPropagado;
            })
            .map(impactoPropagadoRepository::save)
            .map(impactoPropagadoMapper::toDto);
    }

    /**
     * Get all the impactoPropagados.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<ImpactoPropagadoDTO> findAll() {
        LOG.debug("Request to get all ImpactoPropagados");
        return impactoPropagadoRepository
            .findAll()
            .stream()
            .map(impactoPropagadoMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get all the impactoPropagados with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<ImpactoPropagadoDTO> findAllWithEagerRelationships(Pageable pageable) {
        return impactoPropagadoRepository.findAllWithEagerRelationships(pageable).map(impactoPropagadoMapper::toDto);
    }

    /**
     * Get one impactoPropagado by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ImpactoPropagadoDTO> findOne(Long id) {
        LOG.debug("Request to get ImpactoPropagado : {}", id);
        return impactoPropagadoRepository.findOneWithEagerRelationships(id).map(impactoPropagadoMapper::toDto);
    }

    /**
     * Delete the impactoPropagado by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete ImpactoPropagado : {}", id);
        impactoPropagadoRepository.deleteById(id);
    }
}
