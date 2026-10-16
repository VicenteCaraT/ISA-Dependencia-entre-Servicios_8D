package ar.edu.um.isa.oncall.service.mapper;

import static ar.edu.um.isa.oncall.domain.DependenciaDeServicioAsserts.*;
import static ar.edu.um.isa.oncall.domain.DependenciaDeServicioTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DependenciaDeServicioMapperTest {

    private DependenciaDeServicioMapper dependenciaDeServicioMapper;

    @BeforeEach
    void setUp() {
        dependenciaDeServicioMapper = new DependenciaDeServicioMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getDependenciaDeServicioSample1();
        var actual = dependenciaDeServicioMapper.toEntity(dependenciaDeServicioMapper.toDto(expected));
        assertDependenciaDeServicioAllPropertiesEquals(expected, actual);
    }
}
