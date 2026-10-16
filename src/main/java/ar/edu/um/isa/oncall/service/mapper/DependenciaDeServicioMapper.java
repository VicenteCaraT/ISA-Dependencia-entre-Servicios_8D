package ar.edu.um.isa.oncall.service.mapper;

import ar.edu.um.isa.oncall.domain.DependenciaDeServicio;
import ar.edu.um.isa.oncall.domain.Servicio;
import ar.edu.um.isa.oncall.service.dto.DependenciaDeServicioDTO;
import ar.edu.um.isa.oncall.service.dto.ServicioDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link DependenciaDeServicio} and its DTO {@link DependenciaDeServicioDTO}.
 */
@Mapper(componentModel = "spring")
public interface DependenciaDeServicioMapper extends EntityMapper<DependenciaDeServicioDTO, DependenciaDeServicio> {
    @Mapping(target = "servicioDependiente", source = "servicioDependiente", qualifiedByName = "servicioNombre")
    @Mapping(target = "servicioProveedor", source = "servicioProveedor", qualifiedByName = "servicioNombre")
    DependenciaDeServicioDTO toDto(DependenciaDeServicio s);

    @Named("servicioNombre")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "nombre", source = "nombre")
    ServicioDTO toDtoServicioNombre(Servicio servicio);
}
