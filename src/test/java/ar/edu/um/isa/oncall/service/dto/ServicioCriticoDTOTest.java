package ar.edu.um.isa.oncall.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import ar.edu.um.isa.oncall.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ServicioCriticoDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ServicioCriticoDTO.class);
        ServicioCriticoDTO servicioCriticoDTO1 = new ServicioCriticoDTO();
        servicioCriticoDTO1.setId(1L);
        ServicioCriticoDTO servicioCriticoDTO2 = new ServicioCriticoDTO();
        assertThat(servicioCriticoDTO1).isNotEqualTo(servicioCriticoDTO2);
        servicioCriticoDTO2.setId(servicioCriticoDTO1.getId());
        assertThat(servicioCriticoDTO1).isEqualTo(servicioCriticoDTO2);
        servicioCriticoDTO2.setId(2L);
        assertThat(servicioCriticoDTO1).isNotEqualTo(servicioCriticoDTO2);
        servicioCriticoDTO1.setId(null);
        assertThat(servicioCriticoDTO1).isNotEqualTo(servicioCriticoDTO2);
    }
}
