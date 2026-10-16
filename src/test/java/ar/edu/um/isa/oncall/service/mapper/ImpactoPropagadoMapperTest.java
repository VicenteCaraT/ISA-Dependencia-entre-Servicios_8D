package ar.edu.um.isa.oncall.service.mapper;

import static ar.edu.um.isa.oncall.domain.ImpactoPropagadoAsserts.*;
import static ar.edu.um.isa.oncall.domain.ImpactoPropagadoTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ImpactoPropagadoMapperTest {

    private ImpactoPropagadoMapper impactoPropagadoMapper;

    @BeforeEach
    void setUp() {
        impactoPropagadoMapper = new ImpactoPropagadoMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getImpactoPropagadoSample1();
        var actual = impactoPropagadoMapper.toEntity(impactoPropagadoMapper.toDto(expected));
        assertImpactoPropagadoAllPropertiesEquals(expected, actual);
    }
}
