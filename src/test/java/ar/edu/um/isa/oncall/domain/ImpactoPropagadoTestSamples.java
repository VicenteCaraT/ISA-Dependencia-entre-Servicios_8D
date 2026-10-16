package ar.edu.um.isa.oncall.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ImpactoPropagadoTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    public static ImpactoPropagado getImpactoPropagadoSample1() {
        return new ImpactoPropagado().id(1L).detalle("detalle1");
    }

    public static ImpactoPropagado getImpactoPropagadoSample2() {
        return new ImpactoPropagado().id(2L).detalle("detalle2");
    }

    public static ImpactoPropagado getImpactoPropagadoRandomSampleGenerator() {
        return new ImpactoPropagado().id(longCount.incrementAndGet()).detalle(UUID.randomUUID().toString());
    }
}
