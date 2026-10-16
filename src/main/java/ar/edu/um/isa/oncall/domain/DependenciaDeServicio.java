package ar.edu.um.isa.oncall.domain;

import ar.edu.um.isa.oncall.domain.enumeration.TipoDependencia;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serial;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * Declara que un servicio consumidor necesita a otro servicio proveedor.
 * La dirección es importante: si falla el proveedor, el impacto puede
 * propagarse hacia el consumidor; el sentido inverso no se asume.
 */
@Entity
@Table(name = "dependencia_de_servicio")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DependenciaDeServicio implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo", nullable = false)
    private TipoDependencia tipo;

    @NotNull
    @Column(name = "activa", nullable = false)
    private Boolean activa;

    @Size(max = 500)
    @Column(name = "descripcion", length = 500)
    private String descripcion;

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
    private Servicio servicioDependiente;

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
    private Servicio servicioProveedor;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "dependencia")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "incidente", "servicioAfectado", "dependencia" }, allowSetters = true)
    private Set<ImpactoPropagado> impactoPropagados = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public DependenciaDeServicio id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TipoDependencia getTipo() {
        return this.tipo;
    }

    public DependenciaDeServicio tipo(TipoDependencia tipo) {
        this.setTipo(tipo);
        return this;
    }

    public void setTipo(TipoDependencia tipo) {
        this.tipo = tipo;
    }

    public Boolean getActiva() {
        return this.activa;
    }

    public DependenciaDeServicio activa(Boolean activa) {
        this.setActiva(activa);
        return this;
    }

    public void setActiva(Boolean activa) {
        this.activa = activa;
    }

    public String getDescripcion() {
        return this.descripcion;
    }

    public DependenciaDeServicio descripcion(String descripcion) {
        this.setDescripcion(descripcion);
        return this;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Servicio getServicioDependiente() {
        return this.servicioDependiente;
    }

    public void setServicioDependiente(Servicio servicio) {
        this.servicioDependiente = servicio;
    }

    public DependenciaDeServicio servicioDependiente(Servicio servicio) {
        this.setServicioDependiente(servicio);
        return this;
    }

    public Servicio getServicioProveedor() {
        return this.servicioProveedor;
    }

    public void setServicioProveedor(Servicio servicio) {
        this.servicioProveedor = servicio;
    }

    public DependenciaDeServicio servicioProveedor(Servicio servicio) {
        this.setServicioProveedor(servicio);
        return this;
    }

    public Set<ImpactoPropagado> getImpactoPropagados() {
        return this.impactoPropagados;
    }

    public void setImpactoPropagados(Set<ImpactoPropagado> impactoPropagados) {
        if (this.impactoPropagados != null) {
            this.impactoPropagados.forEach(i -> i.setDependencia(null));
        }
        if (impactoPropagados != null) {
            impactoPropagados.forEach(i -> i.setDependencia(this));
        }
        this.impactoPropagados = impactoPropagados;
    }

    public DependenciaDeServicio impactoPropagados(Set<ImpactoPropagado> impactoPropagados) {
        this.setImpactoPropagados(impactoPropagados);
        return this;
    }

    public DependenciaDeServicio addImpactoPropagado(ImpactoPropagado impactoPropagado) {
        this.impactoPropagados.add(impactoPropagado);
        impactoPropagado.setDependencia(this);
        return this;
    }

    public DependenciaDeServicio removeImpactoPropagado(ImpactoPropagado impactoPropagado) {
        this.impactoPropagados.remove(impactoPropagado);
        impactoPropagado.setDependencia(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DependenciaDeServicio)) {
            return false;
        }
        return getId() != null && getId().equals(((DependenciaDeServicio) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DependenciaDeServicio{" +
            "id=" + getId() +
            ", tipo='" + getTipo() + "'" +
            ", activa='" + getActiva() + "'" +
            ", descripcion='" + getDescripcion() + "'" +
            "}";
    }
}
