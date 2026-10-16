package ar.edu.um.isa.oncall.repository;

import ar.edu.um.isa.oncall.domain.ImpactoPropagado;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ImpactoPropagado entity.
 */
@Repository
public interface ImpactoPropagadoRepository extends JpaRepository<ImpactoPropagado, Long> {
    /** Evita registrar dos veces el impacto del mismo incidente sobre un servicio. */
    boolean existsByIncidenteIdAndServicioAfectadoId(Long incidenteId, Long servicioId);

    default Optional<ImpactoPropagado> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<ImpactoPropagado> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<ImpactoPropagado> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select impactoPropagado from ImpactoPropagado impactoPropagado left join fetch impactoPropagado.incidente left join fetch impactoPropagado.servicioAfectado left join fetch impactoPropagado.dependencia",
        countQuery = "select count(impactoPropagado) from ImpactoPropagado impactoPropagado"
    )
    Page<ImpactoPropagado> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select impactoPropagado from ImpactoPropagado impactoPropagado left join fetch impactoPropagado.incidente left join fetch impactoPropagado.servicioAfectado left join fetch impactoPropagado.dependencia"
    )
    List<ImpactoPropagado> findAllWithToOneRelationships();

    @Query(
        "select impactoPropagado from ImpactoPropagado impactoPropagado left join fetch impactoPropagado.incidente left join fetch impactoPropagado.servicioAfectado left join fetch impactoPropagado.dependencia where impactoPropagado.id =:id"
    )
    Optional<ImpactoPropagado> findOneWithToOneRelationships(@Param("id") Long id);
}
