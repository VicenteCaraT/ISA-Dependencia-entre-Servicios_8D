package ar.edu.um.isa.oncall.domain;

import static ar.edu.um.isa.oncall.domain.DependenciaDeServicioTestSamples.*;
import static ar.edu.um.isa.oncall.domain.ImpactoPropagadoTestSamples.*;
import static ar.edu.um.isa.oncall.domain.IncidenteTestSamples.*;
import static ar.edu.um.isa.oncall.domain.ServicioTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import ar.edu.um.isa.oncall.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ImpactoPropagadoTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ImpactoPropagado.class);
        ImpactoPropagado impactoPropagado1 = getImpactoPropagadoSample1();
        ImpactoPropagado impactoPropagado2 = new ImpactoPropagado();
        assertThat(impactoPropagado1).isNotEqualTo(impactoPropagado2);

        impactoPropagado2.setId(impactoPropagado1.getId());
        assertThat(impactoPropagado1).isEqualTo(impactoPropagado2);

        impactoPropagado2 = getImpactoPropagadoSample2();
        assertThat(impactoPropagado1).isNotEqualTo(impactoPropagado2);
    }

    @Test
    void incidenteTest() {
        ImpactoPropagado impactoPropagado = getImpactoPropagadoRandomSampleGenerator();
        Incidente incidenteBack = getIncidenteRandomSampleGenerator();

        impactoPropagado.setIncidente(incidenteBack);
        assertThat(impactoPropagado.getIncidente()).isEqualTo(incidenteBack);

        impactoPropagado.incidente(null);
        assertThat(impactoPropagado.getIncidente()).isNull();
    }

    @Test
    void servicioAfectadoTest() {
        ImpactoPropagado impactoPropagado = getImpactoPropagadoRandomSampleGenerator();
        Servicio servicioBack = getServicioRandomSampleGenerator();

        impactoPropagado.setServicioAfectado(servicioBack);
        assertThat(impactoPropagado.getServicioAfectado()).isEqualTo(servicioBack);

        impactoPropagado.servicioAfectado(null);
        assertThat(impactoPropagado.getServicioAfectado()).isNull();
    }

    @Test
    void dependenciaTest() {
        ImpactoPropagado impactoPropagado = getImpactoPropagadoRandomSampleGenerator();
        DependenciaDeServicio dependenciaDeServicioBack = getDependenciaDeServicioRandomSampleGenerator();

        impactoPropagado.setDependencia(dependenciaDeServicioBack);
        assertThat(impactoPropagado.getDependencia()).isEqualTo(dependenciaDeServicioBack);

        impactoPropagado.dependencia(null);
        assertThat(impactoPropagado.getDependencia()).isNull();
    }
}
