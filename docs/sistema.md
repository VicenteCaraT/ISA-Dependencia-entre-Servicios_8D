# OnCall: sistema de guardias e incidentes

OnCall es una aplicación para organizar la respuesta de un equipo técnico ante alertas e incidentes en producción. Centraliza qué servicios se operan, quién está de guardia, cómo se escala una situación y qué se aprendió después de resolverla.

## Problema que resuelve

Los sistemas de monitoreo generan alertas constantemente. Algunas son ruido y otras representan una interrupción real. El sistema permite registrar esas señales, convertirlas en incidentes gestionables y dejar trazabilidad de toda la respuesta.

## Flujo principal

1. Se registra una **alerta** asociada a un servicio.
2. Un operador crea o vincula un **incidente** para gestionar el problema.
3. El incidente registra sus estados y eventos: detección, reconocimiento, mitigación y resolución.
4. Las **rotaciones** y los **turnos de guardia** permiten identificar a la persona responsable en cada momento.
5. Una **política de escalamiento** define a quién y por qué canal se debe notificar si el incidente no es reconocido a tiempo.
6. Al finalizar, se puede documentar un **postmortem** y crear **acciones correctivas** para evitar que el problema vuelva a ocurrir.

## Módulos del sistema

| Módulo | Qué administra |
| --- | --- |
| Catálogo | Equipos, servicios, criticidad, entorno y objetivos de servicio. |
| Detección | Alertas, incidentes y eventos que forman la línea de tiempo. |
| Guardias | Rotaciones, turnos, reemplazos y responsables actuales. |
| Escalamiento | Políticas, pasos, canales y destinatarios de las notificaciones. |
| Aprendizaje | Postmortems y acciones correctivas asociadas a incidentes. |
| Administración | Usuarios, roles y permisos de la aplicación. |

## Qué puede hacer hoy

La aplicación generada ofrece ABM (alta, baja, modificación y listado), autenticación, validaciones, filtros y paginación para las entidades del modelo. El usuario `admin` puede recorrer y administrar todos los módulos.

## Reglas de negocio pendientes

El generador crea la estructura, pero estas reglas deben implementarse manualmente:

- Deduplicar alertas mediante su `fingerprint` para no abrir incidentes duplicados.
- Resolver quién está de guardia en una rotación, considerando reemplazos.
- Ejecutar escalamiento y enviar notificaciones cuando no se reconoce un incidente.
- Calcular métricas como MTTA, MTTR y cumplimiento de objetivos de servicio.

## Tecnología

El backend usa Java, Spring Boot y PostgreSQL. El frontend usa Angular. JHipster genera la estructura inicial de la aplicación a partir del modelo definido en `oncall.jh`.
