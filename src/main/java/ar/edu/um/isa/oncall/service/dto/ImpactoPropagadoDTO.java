package ar.edu.um.isa.oncall.service.dto;

import ar.edu.um.isa.oncall.domain.enumeration.EstadoImpacto;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link ar.edu.um.isa.oncall.domain.ImpactoPropagado} entity.
 */
@Schema(
    description = "Registra que un incidente alcanzó un servicio a través del grafo.\nNo se infiere que el servicio sea la causa: solo que fue afectado."
)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ImpactoPropagadoDTO implements Serializable {

    private Long id;

    @NotNull
    private Instant detectadoEn;

    @NotNull
    private EstadoImpacto estado;

    @Size(max = 1000)
    private String detalle;

    @NotNull
    private IncidenteDTO incidente;

    @NotNull
    private ServicioDTO servicioAfectado;

    private DependenciaDeServicioDTO dependencia;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getDetectadoEn() {
        return detectadoEn;
    }

    public void setDetectadoEn(Instant detectadoEn) {
        this.detectadoEn = detectadoEn;
    }

    public EstadoImpacto getEstado() {
        return estado;
    }

    public void setEstado(EstadoImpacto estado) {
        this.estado = estado;
    }

    public String getDetalle() {
        return detalle;
    }

    public void setDetalle(String detalle) {
        this.detalle = detalle;
    }

    public IncidenteDTO getIncidente() {
        return incidente;
    }

    public void setIncidente(IncidenteDTO incidente) {
        this.incidente = incidente;
    }

    public ServicioDTO getServicioAfectado() {
        return servicioAfectado;
    }

    public void setServicioAfectado(ServicioDTO servicioAfectado) {
        this.servicioAfectado = servicioAfectado;
    }

    public DependenciaDeServicioDTO getDependencia() {
        return dependencia;
    }

    public void setDependencia(DependenciaDeServicioDTO dependencia) {
        this.dependencia = dependencia;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ImpactoPropagadoDTO)) {
            return false;
        }

        ImpactoPropagadoDTO impactoPropagadoDTO = (ImpactoPropagadoDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, impactoPropagadoDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ImpactoPropagadoDTO{" +
            "id=" + getId() +
            ", detectadoEn='" + getDetectadoEn() + "'" +
            ", estado='" + getEstado() + "'" +
            ", detalle='" + getDetalle() + "'" +
            ", incidente=" + getIncidente() +
            ", servicioAfectado=" + getServicioAfectado() +
            ", dependencia=" + getDependencia() +
            "}";
    }
}
