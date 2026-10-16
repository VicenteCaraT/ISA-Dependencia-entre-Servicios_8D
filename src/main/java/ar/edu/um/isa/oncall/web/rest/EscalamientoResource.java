package ar.edu.um.isa.oncall.web.rest;

import ar.edu.um.isa.oncall.service.EjecucionEscalamientoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Punto de entrada explícito para ejecutar la regla D en forma manual o desde
 * un scheduler. Repetir la llamada no duplica impactos ni notificaciones.
 */
@RestController
@RequestMapping("/api")
public class EscalamientoResource {

    private final EjecucionEscalamientoService escalamientoService;

    public EscalamientoResource(EjecucionEscalamientoService escalamientoService) {
        this.escalamientoService = escalamientoService;
    }

    @PostMapping("/incidentes/{incidenteId}/escalamientos/ejecutar")
    public ResponseEntity<EjecucionEscalamientoService.ResultadoEscalamiento> ejecutar(@PathVariable Long incidenteId) {
        return ResponseEntity.ok(escalamientoService.ejecutar(incidenteId));
    }
}
