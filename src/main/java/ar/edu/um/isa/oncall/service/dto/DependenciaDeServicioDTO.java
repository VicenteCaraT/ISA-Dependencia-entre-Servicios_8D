package ar.edu.um.isa.oncall.service.dto;

import ar.edu.um.isa.oncall.domain.enumeration.TipoDependencia;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link ar.edu.um.isa.oncall.domain.DependenciaDeServicio} entity.
 */
@Schema(
    description = "Declara que un servicio consumidor necesita a otro servicio proveedor.\nLa dirección es importante: si falla el proveedor, el impacto puede\npropagarse hacia el consumidor; el sentido inverso no se asume."
)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DependenciaDeServicioDTO implements Serializable {

    private Long id;

    @NotNull
    private TipoDependencia tipo;

    @NotNull
    private Boolean activa;

    @Size(max = 500)
    private String descripcion;

    @NotNull
    private ServicioDTO servicioDependiente;

    @NotNull
    private ServicioDTO servicioProveedor;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TipoDependencia getTipo() {
        return tipo;
    }

    public void setTipo(TipoDependencia tipo) {
        this.tipo = tipo;
    }

    public Boolean getActiva() {
        return activa;
    }

    public void setActiva(Boolean activa) {
        this.activa = activa;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public ServicioDTO getServicioDependiente() {
        return servicioDependiente;
    }

    public void setServicioDependiente(ServicioDTO servicioDependiente) {
        this.servicioDependiente = servicioDependiente;
    }

    public ServicioDTO getServicioProveedor() {
        return servicioProveedor;
    }

    public void setServicioProveedor(ServicioDTO servicioProveedor) {
        this.servicioProveedor = servicioProveedor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DependenciaDeServicioDTO)) {
            return false;
        }

        DependenciaDeServicioDTO dependenciaDeServicioDTO = (DependenciaDeServicioDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, dependenciaDeServicioDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DependenciaDeServicioDTO{" +
            "id=" + getId() +
            ", tipo='" + getTipo() + "'" +
            ", activa='" + getActiva() + "'" +
            ", descripcion='" + getDescripcion() + "'" +
            ", servicioDependiente=" + getServicioDependiente() +
            ", servicioProveedor=" + getServicioProveedor() +
            "}";
    }
}
