package ar.edu.um.isa.oncall.domain;

import static ar.edu.um.isa.oncall.domain.ServicioCriticoTestSamples.*;
import static ar.edu.um.isa.oncall.domain.ServicioTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import ar.edu.um.isa.oncall.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ServicioCriticoTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ServicioCritico.class);
        ServicioCritico servicioCritico1 = getServicioCriticoSample1();
        ServicioCritico servicioCritico2 = new ServicioCritico();
        assertThat(servicioCritico1).isNotEqualTo(servicioCritico2);

        servicioCritico2.setId(servicioCritico1.getId());
        assertThat(servicioCritico1).isEqualTo(servicioCritico2);

        servicioCritico2 = getServicioCriticoSample2();
        assertThat(servicioCritico1).isNotEqualTo(servicioCritico2);
    }

    @Test
    void servicioTest() {
        ServicioCritico servicioCritico = getServicioCriticoRandomSampleGenerator();
        Servicio servicioBack = getServicioRandomSampleGenerator();

        servicioCritico.setServicio(servicioBack);
        assertThat(servicioCritico.getServicio()).isEqualTo(servicioBack);

        servicioCritico.servicio(null);
        assertThat(servicioCritico.getServicio()).isNull();
    }
}
