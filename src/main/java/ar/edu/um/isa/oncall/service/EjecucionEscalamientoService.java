package ar.edu.um.isa.oncall.service;

import ar.edu.um.isa.oncall.domain.DependenciaDeServicio;
import ar.edu.um.isa.oncall.domain.ImpactoPropagado;
import ar.edu.um.isa.oncall.domain.Incidente;
import ar.edu.um.isa.oncall.domain.Notificacion;
import ar.edu.um.isa.oncall.domain.PasoEscalamiento;
import ar.edu.um.isa.oncall.domain.Servicio;
import ar.edu.um.isa.oncall.domain.enumeration.EstadoImpacto;
import ar.edu.um.isa.oncall.domain.enumeration.EstadoIncidente;
import ar.edu.um.isa.oncall.domain.enumeration.EstadoNotificacion;
import ar.edu.um.isa.oncall.repository.DependenciaDeServicioRepository;
import ar.edu.um.isa.oncall.repository.ImpactoPropagadoRepository;
import ar.edu.um.isa.oncall.repository.IncidenteRepository;
import ar.edu.um.isa.oncall.repository.NotificacionRepository;
import ar.edu.um.isa.oncall.repository.PasoEscalamientoRepository;
import jakarta.persistence.EntityNotFoundException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayDeque;
import java.util.HashSet;
import java.util.Set;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementa la regla D para los incidentes abiertos.
 *
 * Primero recorre el grafo en la dirección proveedor → dependiente y registra una
 * sola vez cada impacto. Después ejecuta los pasos vencidos que tienen destinatario
 * directo. Los pasos basados en rotación quedan fuera hasta contar con la regla C.
 */
@Service
@Transactional
public class EjecucionEscalamientoService {

    private final IncidenteRepository incidenteRepository;
    private final DependenciaDeServicioRepository dependenciaRepository;
    private final ImpactoPropagadoRepository impactoRepository;
    private final PasoEscalamientoRepository pasoRepository;
    private final NotificacionRepository notificacionRepository;

    public EjecucionEscalamientoService(
        IncidenteRepository incidenteRepository,
        DependenciaDeServicioRepository dependenciaRepository,
        ImpactoPropagadoRepository impactoRepository,
        PasoEscalamientoRepository pasoRepository,
        NotificacionRepository notificacionRepository
    ) {
        this.incidenteRepository = incidenteRepository;
        this.dependenciaRepository = dependenciaRepository;
        this.impactoRepository = impactoRepository;
        this.pasoRepository = pasoRepository;
        this.notificacionRepository = notificacionRepository;
    }

    public ResultadoEscalamiento ejecutar(Long incidenteId) {
        Incidente incidente = incidenteRepository
            .findOneWithServicios(incidenteId)
            .orElseThrow(() -> new EntityNotFoundException("Incidente no encontrado"));
        if (incidente.getEstado() != EstadoIncidente.ABIERTO) {
            return new ResultadoEscalamiento(incidenteId, 0, 0, 0, "El incidente no está abierto; no corresponde escalarlo.");
        }

        Set<Long> serviciosAfectados = propagarImpactos(incidente);
        int notificacionesCreadas = crearNotificacionesVencidas(incidente, serviciosAfectados);
        return new ResultadoEscalamiento(
            incidenteId,
            serviciosAfectados.size(),
            notificacionesCreadas,
            0,
            "Impactos y escalamiento procesados de forma idempotente."
        );
    }

    private Set<Long> propagarImpactos(Incidente incidente) {
        Set<Long> visitados = new HashSet<>();
        ArrayDeque<Long> pendientes = new ArrayDeque<>();
        for (Servicio servicio : incidente.getServicios()) {
            visitados.add(servicio.getId());
            pendientes.add(servicio.getId());
        }

        while (!pendientes.isEmpty()) {
            Long proveedorId = pendientes.removeFirst();
            for (DependenciaDeServicio dependencia : dependenciaRepository.findDependenciasObligatoriasActivasDesde(Set.of(proveedorId))) {
                Servicio dependiente = dependencia.getServicioDependiente();
                if (!visitados.add(dependiente.getId())) {
                    continue; // Corta ciclos y caminos alternativos del grafo.
                }
                if (!impactoRepository.existsByIncidenteIdAndServicioAfectadoId(incidente.getId(), dependiente.getId())) {
                    impactoRepository.save(
                        new ImpactoPropagado()
                            .incidente(incidente)
                            .servicioAfectado(dependiente)
                            .dependencia(dependencia)
                            .detectadoEn(Instant.now())
                            .estado(EstadoImpacto.DETECTADO)
                            .detalle("Impacto propagado desde " + dependencia.getServicioProveedor().getNombre())
                    );
                }
                pendientes.addLast(dependiente.getId());
            }
        }
        return visitados;
    }

    private int crearNotificacionesVencidas(Incidente incidente, Set<Long> serviciosAfectados) {
        int creadas = 0;
        Instant referencia = incidente.getDetectadoEn();
        for (Long servicioId : serviciosAfectados) {
            int esperaAcumulada = 0;
            for (PasoEscalamiento paso : pasoRepository.findCadenaPorServicio(servicioId)) {
                esperaAcumulada += paso.getEsperaMinutos();
                if (paso.getDestinatarioDirecto() == null || Instant.now().isBefore(referencia.plus(esperaAcumulada, ChronoUnit.MINUTES))) {
                    continue;
                }
                if (
                    notificacionRepository.existsByIncidenteIdAndDestinatarioIdAndCanal(
                        incidente.getId(),
                        paso.getDestinatarioDirecto().getId(),
                        paso.getCanal()
                    )
                ) {
                    continue;
                }
                String destino =
                    paso.getDestinatarioDirecto().getEmail() != null
                        ? paso.getDestinatarioDirecto().getEmail()
                        : paso.getDestinatarioDirecto().getLogin();
                notificacionRepository.save(
                    new Notificacion()
                        .incidente(incidente)
                        .destinatario(paso.getDestinatarioDirecto())
                        .canal(paso.getCanal())
                        .destino(destino)
                        .estado(EstadoNotificacion.PENDIENTE)
                        .intentos(0)
                );
                creadas++;
            }
        }
        return creadas;
    }

    public record ResultadoEscalamiento(
        Long incidenteId,
        int serviciosProcesados,
        int notificacionesCreadas,
        int pasosPendientesDeRotacion,
        String mensaje
    ) {}
}
