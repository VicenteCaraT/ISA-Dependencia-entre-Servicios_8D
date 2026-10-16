package ar.edu.um.isa.oncall.service.mapper;

import ar.edu.um.isa.oncall.domain.DependenciaDeServicio;
import ar.edu.um.isa.oncall.domain.ImpactoPropagado;
import ar.edu.um.isa.oncall.domain.Incidente;
import ar.edu.um.isa.oncall.domain.Servicio;
import ar.edu.um.isa.oncall.service.dto.DependenciaDeServicioDTO;
import ar.edu.um.isa.oncall.service.dto.ImpactoPropagadoDTO;
import ar.edu.um.isa.oncall.service.dto.IncidenteDTO;
import ar.edu.um.isa.oncall.service.dto.ServicioDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ImpactoPropagado} and its DTO {@link ImpactoPropagadoDTO}.
 */
@Mapper(componentModel = "spring")
public interface ImpactoPropagadoMapper extends EntityMapper<ImpactoPropagadoDTO, ImpactoPropagado> {
    @Mapping(target = "incidente", source = "incidente", qualifiedByName = "incidenteTitulo")
    @Mapping(target = "servicioAfectado", source = "servicioAfectado", qualifiedByName = "servicioNombre")
    @Mapping(target = "dependencia", source = "dependencia", qualifiedByName = "dependenciaDeServicioDescripcion")
    ImpactoPropagadoDTO toDto(ImpactoPropagado s);

    @Named("incidenteTitulo")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "titulo", source = "titulo")
    IncidenteDTO toDtoIncidenteTitulo(Incidente incidente);

    @Named("servicioNombre")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "nombre", source = "nombre")
    ServicioDTO toDtoServicioNombre(Servicio servicio);

    @Named("dependenciaDeServicioDescripcion")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "descripcion", source = "descripcion")
    DependenciaDeServicioDTO toDtoDependenciaDeServicioDescripcion(DependenciaDeServicio dependenciaDeServicio);
}
