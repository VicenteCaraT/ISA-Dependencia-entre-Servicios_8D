package ar.edu.um.isa.oncall.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serial;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * Perfil adicional para servicios cuya indisponibilidad requiere atención
 * prioritaria. Se separa de Criticidad porque esta última clasifica el
 * servicio y este perfil define cómo se trata al propagar y escalar.
 */
@Entity
@Table(name = "servicio_critico")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ServicioCritico implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Min(value = 1)
    @Max(value = 10)
    @Column(name = "prioridad_escalamiento", nullable = false)
    private Integer prioridadEscalamiento;

    @NotNull
    @Column(name = "requiere_escalamiento_inmediato", nullable = false)
    private Boolean requiereEscalamientoInmediato;

    @Size(max = 500)
    @Column(name = "justificacion", length = 500)
    private String justificacion;

    @JsonIgnoreProperties(
        value = {
            "equipo",
            "perfilCritico",
            "objetivos",
            "alertas",
            "dependenciaComoConsumidors",
            "dependenciaComoProveedors",
            "impactoPropagados",
            "politicas",
            "incidentes",
        },
        allowSetters = true
    )
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @NotNull
    @JoinColumn(unique = true)
    private Servicio servicio;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ServicioCritico id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getPrioridadEscalamiento() {
        return this.prioridadEscalamiento;
    }

    public ServicioCritico prioridadEscalamiento(Integer prioridadEscalamiento) {
        this.setPrioridadEscalamiento(prioridadEscalamiento);
        return this;
    }

    public void setPrioridadEscalamiento(Integer prioridadEscalamiento) {
        this.prioridadEscalamiento = prioridadEscalamiento;
    }

    public Boolean getRequiereEscalamientoInmediato() {
        return this.requiereEscalamientoInmediato;
    }

    public ServicioCritico requiereEscalamientoInmediato(Boolean requiereEscalamientoInmediato) {
        this.setRequiereEscalamientoInmediato(requiereEscalamientoInmediato);
        return this;
    }

    public void setRequiereEscalamientoInmediato(Boolean requiereEscalamientoInmediato) {
        this.requiereEscalamientoInmediato = requiereEscalamientoInmediato;
    }

    public String getJustificacion() {
        return this.justificacion;
    }

    public ServicioCritico justificacion(String justificacion) {
        this.setJustificacion(justificacion);
        return this;
    }

    public void setJustificacion(String justificacion) {
        this.justificacion = justificacion;
    }

    public Servicio getServicio() {
        return this.servicio;
    }

    public void setServicio(Servicio servicio) {
        this.servicio = servicio;
    }

    public ServicioCritico servicio(Servicio servicio) {
        this.setServicio(servicio);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ServicioCritico)) {
            return false;
        }
        return getId() != null && getId().equals(((ServicioCritico) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ServicioCritico{" +
            "id=" + getId() +
            ", prioridadEscalamiento=" + getPrioridadEscalamiento() +
            ", requiereEscalamientoInmediato='" + getRequiereEscalamientoInmediato() + "'" +
            ", justificacion='" + getJustificacion() + "'" +
            "}";
    }
}
