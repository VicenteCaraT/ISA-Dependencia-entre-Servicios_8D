package ar.edu.um.isa.oncall.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link ar.edu.um.isa.oncall.domain.ServicioCritico} entity.
 */
@Schema(
    description = "Perfil adicional para servicios cuya indisponibilidad requiere atención\nprioritaria. Se separa de Criticidad porque esta última clasifica el\nservicio y este perfil define cómo se trata al propagar y escalar."
)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ServicioCriticoDTO implements Serializable {

    private Long id;

    @NotNull
    @Min(value = 1)
    @Max(value = 10)
    private Integer prioridadEscalamiento;

    @NotNull
    private Boolean requiereEscalamientoInmediato;

    @Size(max = 500)
    private String justificacion;

    @NotNull
    private ServicioDTO servicio;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getPrioridadEscalamiento() {
        return prioridadEscalamiento;
    }

    public void setPrioridadEscalamiento(Integer prioridadEscalamiento) {
        this.prioridadEscalamiento = prioridadEscalamiento;
    }

    public Boolean getRequiereEscalamientoInmediato() {
        return requiereEscalamientoInmediato;
    }

    public void setRequiereEscalamientoInmediato(Boolean requiereEscalamientoInmediato) {
        this.requiereEscalamientoInmediato = requiereEscalamientoInmediato;
    }

    public String getJustificacion() {
        return justificacion;
    }

    public void setJustificacion(String justificacion) {
        this.justificacion = justificacion;
    }

    public ServicioDTO getServicio() {
        return servicio;
    }

    public void setServicio(ServicioDTO servicio) {
        this.servicio = servicio;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ServicioCriticoDTO)) {
            return false;
        }

        ServicioCriticoDTO servicioCriticoDTO = (ServicioCriticoDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, servicioCriticoDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ServicioCriticoDTO{" +
            "id=" + getId() +
            ", prioridadEscalamiento=" + getPrioridadEscalamiento() +
            ", requiereEscalamientoInmediato='" + getRequiereEscalamientoInmediato() + "'" +
            ", justificacion='" + getJustificacion() + "'" +
            ", servicio=" + getServicio() +
            "}";
    }
}
