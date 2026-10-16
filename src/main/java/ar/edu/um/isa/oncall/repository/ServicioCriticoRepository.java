package ar.edu.um.isa.oncall.repository;

import ar.edu.um.isa.oncall.domain.ServicioCritico;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ServicioCritico entity.
 */
@Repository
public interface ServicioCriticoRepository extends JpaRepository<ServicioCritico, Long> {
    default Optional<ServicioCritico> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<ServicioCritico> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<ServicioCritico> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select servicioCritico from ServicioCritico servicioCritico left join fetch servicioCritico.servicio",
        countQuery = "select count(servicioCritico) from ServicioCritico servicioCritico"
    )
    Page<ServicioCritico> findAllWithToOneRelationships(Pageable pageable);

    @Query("select servicioCritico from ServicioCritico servicioCritico left join fetch servicioCritico.servicio")
    List<ServicioCritico> findAllWithToOneRelationships();

    @Query(
        "select servicioCritico from ServicioCritico servicioCritico left join fetch servicioCritico.servicio where servicioCritico.id =:id"
    )
    Optional<ServicioCritico> findOneWithToOneRelationships(@Param("id") Long id);
}
