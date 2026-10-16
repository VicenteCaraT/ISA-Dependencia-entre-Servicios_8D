package ar.edu.um.isa.oncall.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class ServicioCriticoTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + 2 * Short.MAX_VALUE);

    public static ServicioCritico getServicioCriticoSample1() {
        return new ServicioCritico().id(1L).prioridadEscalamiento(1).justificacion("justificacion1");
    }

    public static ServicioCritico getServicioCriticoSample2() {
        return new ServicioCritico().id(2L).prioridadEscalamiento(2).justificacion("justificacion2");
    }

    public static ServicioCritico getServicioCriticoRandomSampleGenerator() {
        return new ServicioCritico()
            .id(longCount.incrementAndGet())
            .prioridadEscalamiento(intCount.incrementAndGet())
            .justificacion(UUID.randomUUID().toString());
    }
}
