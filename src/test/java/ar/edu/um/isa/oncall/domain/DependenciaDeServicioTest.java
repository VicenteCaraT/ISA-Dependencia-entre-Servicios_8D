package ar.edu.um.isa.oncall.domain;

import static ar.edu.um.isa.oncall.domain.DependenciaDeServicioTestSamples.*;
import static ar.edu.um.isa.oncall.domain.ImpactoPropagadoTestSamples.*;
import static ar.edu.um.isa.oncall.domain.ServicioTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import ar.edu.um.isa.oncall.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class DependenciaDeServicioTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DependenciaDeServicio.class);
        DependenciaDeServicio dependenciaDeServicio1 = getDependenciaDeServicioSample1();
        DependenciaDeServicio dependenciaDeServicio2 = new DependenciaDeServicio();
        assertThat(dependenciaDeServicio1).isNotEqualTo(dependenciaDeServicio2);

        dependenciaDeServicio2.setId(dependenciaDeServicio1.getId());
        assertThat(dependenciaDeServicio1).isEqualTo(dependenciaDeServicio2);

        dependenciaDeServicio2 = getDependenciaDeServicioSample2();
        assertThat(dependenciaDeServicio1).isNotEqualTo(dependenciaDeServicio2);
    }

    @Test
    void servicioDependienteTest() {
        DependenciaDeServicio dependenciaDeServicio = getDependenciaDeServicioRandomSampleGenerator();
        Servicio servicioBack = getServicioRandomSampleGenerator();

        dependenciaDeServicio.setServicioDependiente(servicioBack);
        assertThat(dependenciaDeServicio.getServicioDependiente()).isEqualTo(servicioBack);

        dependenciaDeServicio.servicioDependiente(null);
        assertThat(dependenciaDeServicio.getServicioDependiente()).isNull();
    }

    @Test
    void servicioProveedorTest() {
        DependenciaDeServicio dependenciaDeServicio = getDependenciaDeServicioRandomSampleGenerator();
        Servicio servicioBack = getServicioRandomSampleGenerator();

        dependenciaDeServicio.setServicioProveedor(servicioBack);
        assertThat(dependenciaDeServicio.getServicioProveedor()).isEqualTo(servicioBack);

        dependenciaDeServicio.servicioProveedor(null);
        assertThat(dependenciaDeServicio.getServicioProveedor()).isNull();
    }

    @Test
    void impactoPropagadoTest() {
        DependenciaDeServicio dependenciaDeServicio = getDependenciaDeServicioRandomSampleGenerator();
        ImpactoPropagado impactoPropagadoBack = getImpactoPropagadoRandomSampleGenerator();

        dependenciaDeServicio.addImpactoPropagado(impactoPropagadoBack);
        assertThat(dependenciaDeServicio.getImpactoPropagados()).containsOnly(impactoPropagadoBack);
        assertThat(impactoPropagadoBack.getDependencia()).isEqualTo(dependenciaDeServicio);

        dependenciaDeServicio.removeImpactoPropagado(impactoPropagadoBack);
        assertThat(dependenciaDeServicio.getImpactoPropagados()).doesNotContain(impactoPropagadoBack);
        assertThat(impactoPropagadoBack.getDependencia()).isNull();

        dependenciaDeServicio.impactoPropagados(new HashSet<>(Set.of(impactoPropagadoBack)));
        assertThat(dependenciaDeServicio.getImpactoPropagados()).containsOnly(impactoPropagadoBack);
        assertThat(impactoPropagadoBack.getDependencia()).isEqualTo(dependenciaDeServicio);

        dependenciaDeServicio.setImpactoPropagados(new HashSet<>());
        assertThat(dependenciaDeServicio.getImpactoPropagados()).doesNotContain(impactoPropagadoBack);
        assertThat(impactoPropagadoBack.getDependencia()).isNull();
    }
}
