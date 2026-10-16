package ar.edu.um.isa.oncall.domain.enumeration;

/**
 * Estado de la propagación para conservar una traza idempotente.
 */
public enum EstadoImpacto {
    DETECTADO,
    NOTIFICADO,
    MITIGADO,
    DESCARTADO,
}
