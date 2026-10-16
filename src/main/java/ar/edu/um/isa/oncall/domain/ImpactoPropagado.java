package ar.edu.um.isa.oncall.domain;

import ar.edu.um.isa.oncall.domain.enumeration.EstadoImpacto;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * Registra que un incidente alcanzó un servicio a través del grafo.
 * No se infiere que el servicio sea la causa: solo que fue afectado.
 */
@Entity
@Table(name = "impacto_propagado")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ImpactoPropagado implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "detectado_en", nullable = false)
    private Instant detectadoEn;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false)
    private EstadoImpacto estado;

    @Size(max = 1000)
    @Column(name = "detalle", length = 1000)
    private String detalle;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(
        value = { "comandante", "servicios", "postmortem", "alertas", "eventos", "impactoPropagados", "notificacions" },
        allowSetters = true
    )
    private Incidente incidente;

    @ManyToOne(optional = false)
    @NotNull
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
    private Servicio servicioAfectado;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "servicioDependiente", "servicioProveedor", "impactoPropagados" }, allowSetters = true)
    private DependenciaDeServicio dependencia;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ImpactoPropagado id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getDetectadoEn() {
        return this.detectadoEn;
    }

    public ImpactoPropagado detectadoEn(Instant detectadoEn) {
        this.setDetectadoEn(detectadoEn);
        return this;
    }

    public void setDetectadoEn(Instant detectadoEn) {
        this.detectadoEn = detectadoEn;
    }

    public EstadoImpacto getEstado() {
        return this.estado;
    }

    public ImpactoPropagado estado(EstadoImpacto estado) {
        this.setEstado(estado);
        return this;
    }

    public void setEstado(EstadoImpacto estado) {
        this.estado = estado;
    }

    public String getDetalle() {
        return this.detalle;
    }

    public ImpactoPropagado detalle(String detalle) {
        this.setDetalle(detalle);
        return this;
    }

    public void setDetalle(String detalle) {
        this.detalle = detalle;
    }

    public Incidente getIncidente() {
        return this.incidente;
    }

    public void setIncidente(Incidente incidente) {
        this.incidente = incidente;
    }

    public ImpactoPropagado incidente(Incidente incidente) {
        this.setIncidente(incidente);
        return this;
    }

    public Servicio getServicioAfectado() {
        return this.servicioAfectado;
    }

    public void setServicioAfectado(Servicio servicio) {
        this.servicioAfectado = servicio;
    }

    public ImpactoPropagado servicioAfectado(Servicio servicio) {
        this.setServicioAfectado(servicio);
        return this;
    }

    public DependenciaDeServicio getDependencia() {
        return this.dependencia;
    }

    public void setDependencia(DependenciaDeServicio dependenciaDeServicio) {
        this.dependencia = dependenciaDeServicio;
    }

    public ImpactoPropagado dependencia(DependenciaDeServicio dependenciaDeServicio) {
        this.setDependencia(dependenciaDeServicio);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ImpactoPropagado)) {
            return false;
        }
        return getId() != null && getId().equals(((ImpactoPropagado) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ImpactoPropagado{" +
            "id=" + getId() +
            ", detectadoEn='" + getDetectadoEn() + "'" +
            ", estado='" + getEstado() + "'" +
            ", detalle='" + getDetalle() + "'" +
            "}";
    }
}
