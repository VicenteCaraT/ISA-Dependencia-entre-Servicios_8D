package ar.edu.um.isa.oncall.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import ar.edu.um.isa.oncall.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DependenciaDeServicioDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(DependenciaDeServicioDTO.class);
        DependenciaDeServicioDTO dependenciaDeServicioDTO1 = new DependenciaDeServicioDTO();
        dependenciaDeServicioDTO1.setId(1L);
        DependenciaDeServicioDTO dependenciaDeServicioDTO2 = new DependenciaDeServicioDTO();
        assertThat(dependenciaDeServicioDTO1).isNotEqualTo(dependenciaDeServicioDTO2);
        dependenciaDeServicioDTO2.setId(dependenciaDeServicioDTO1.getId());
        assertThat(dependenciaDeServicioDTO1).isEqualTo(dependenciaDeServicioDTO2);
        dependenciaDeServicioDTO2.setId(2L);
        assertThat(dependenciaDeServicioDTO1).isNotEqualTo(dependenciaDeServicioDTO2);
        dependenciaDeServicioDTO1.setId(null);
        assertThat(dependenciaDeServicioDTO1).isNotEqualTo(dependenciaDeServicioDTO2);
    }
}
