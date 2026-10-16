package ar.edu.um.isa.oncall.service.mapper;

import static ar.edu.um.isa.oncall.domain.ServicioCriticoAsserts.*;
import static ar.edu.um.isa.oncall.domain.ServicioCriticoTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ServicioCriticoMapperTest {

    private ServicioCriticoMapper servicioCriticoMapper;

    @BeforeEach
    void setUp() {
        servicioCriticoMapper = new ServicioCriticoMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getServicioCriticoSample1();
        var actual = servicioCriticoMapper.toEntity(servicioCriticoMapper.toDto(expected));
        assertServicioCriticoAllPropertiesEquals(expected, actual);
    }
}
