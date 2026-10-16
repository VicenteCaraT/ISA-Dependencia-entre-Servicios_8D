package ar.edu.um.isa.oncall.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import ar.edu.um.isa.oncall.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ImpactoPropagadoDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ImpactoPropagadoDTO.class);
        ImpactoPropagadoDTO impactoPropagadoDTO1 = new ImpactoPropagadoDTO();
        impactoPropagadoDTO1.setId(1L);
        ImpactoPropagadoDTO impactoPropagadoDTO2 = new ImpactoPropagadoDTO();
        assertThat(impactoPropagadoDTO1).isNotEqualTo(impactoPropagadoDTO2);
        impactoPropagadoDTO2.setId(impactoPropagadoDTO1.getId());
        assertThat(impactoPropagadoDTO1).isEqualTo(impactoPropagadoDTO2);
        impactoPropagadoDTO2.setId(2L);
        assertThat(impactoPropagadoDTO1).isNotEqualTo(impactoPropagadoDTO2);
        impactoPropagadoDTO1.setId(null);
        assertThat(impactoPropagadoDTO1).isNotEqualTo(impactoPropagadoDTO2);
    }
}
