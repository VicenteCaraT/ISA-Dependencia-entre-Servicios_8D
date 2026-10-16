package ar.edu.um.isa.oncall.service.mapper;

import ar.edu.um.isa.oncall.domain.Servicio;
import ar.edu.um.isa.oncall.domain.ServicioCritico;
import ar.edu.um.isa.oncall.service.dto.ServicioCriticoDTO;
import ar.edu.um.isa.oncall.service.dto.ServicioDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ServicioCritico} and its DTO {@link ServicioCriticoDTO}.
 */
@Mapper(componentModel = "spring")
public interface ServicioCriticoMapper extends EntityMapper<ServicioCriticoDTO, ServicioCritico> {
    @Mapping(target = "servicio", source = "servicio", qualifiedByName = "servicioNombre")
    ServicioCriticoDTO toDto(ServicioCritico s);

    @Named("servicioNombre")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "nombre", source = "nombre")
    ServicioDTO toDtoServicioNombre(Servicio servicio);
}
