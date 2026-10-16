package ar.edu.um.isa.oncall.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class DependenciaDeServicioTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    public static DependenciaDeServicio getDependenciaDeServicioSample1() {
        return new DependenciaDeServicio().id(1L).descripcion("descripcion1");
    }

    public static DependenciaDeServicio getDependenciaDeServicioSample2() {
        return new DependenciaDeServicio().id(2L).descripcion("descripcion2");
    }

    public static DependenciaDeServicio getDependenciaDeServicioRandomSampleGenerator() {
        return new DependenciaDeServicio().id(longCount.incrementAndGet()).descripcion(UUID.randomUUID().toString());
    }
}
