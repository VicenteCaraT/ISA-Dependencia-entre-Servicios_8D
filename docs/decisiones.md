# Decisiones de diseño — Extensión 8: Dependencias entre servicios

## Objetivo

La extensión representa el grafo de dependencias de la plataforma. Permite saber qué servicios consumidores pueden verse afectados cuando falla un servicio proveedor, conservar la propagación asociada a cada incidente y distinguir los servicios que requieren atención prioritaria.

## Modelo agregado

- **DependenciaDeServicio** representa un arco dirigido: un `servicioDependiente` consume un `servicioProveedor`. Incluye el tipo de dependencia, si está activa y una descripción.
- **ImpactoPropagado** registra que un incidente alcanzó un servicio concreto a través de una dependencia. Conserva la fecha, el estado y el detalle de la detección.
- **ServicioCritico** es un perfil opcional, uno a uno con `Servicio`, para definir prioridad de escalamiento y si la atención debe ser inmediata.

```text
Servicio proveedor  ──>  Servicio dependiente
        │                         │
        └── DependenciaDeServicio ┘

Incidente ──> ImpactoPropagado ──> Servicio afectado
                         │
                         └── DependenciaDeServicio
```

## Decisiones y casos borde

### El grafo tiene dirección

Una dependencia se lee como *el dependiente necesita al proveedor*. Si se cae el proveedor, el impacto puede viajar hacia el dependiente; no se propaga en el sentido contrario. Esta dirección evita declarar afectados servicios que no consumen al servicio que falló.

### Dependencia obligatoria u opcional

`OBLIGATORIA` indica que la falla del proveedor compromete al dependiente. `OPCIONAL` indica degradación posible, pero no crea impacto automáticamente. Así el modelo evita transformar integraciones no críticas —por ejemplo, analítica— en una caída total.

### Idempotencia y ciclos

Un mismo incidente no debe generar dos impactos para el mismo servicio, aunque existan varios caminos en el grafo o el proceso de escalamiento se ejecute dos veces. La regla D debe consultar los impactos existentes antes de crear uno nuevo. Además debe conservar un conjunto de servicios visitados para cortar ciclos como `A → B → A`.

### Servicio raíz y trazabilidad

El servicio inicial del incidente puede registrarse como impacto sin una dependencia asociada. Los servicios derivados sí conservan el arco que los alcanzó. De esta forma se mantiene la trazabilidad sin inventar una dependencia para la causa inicial.

### Criticidad y prioridad de escalamiento

`Servicio.criticidad` sigue describiendo la importancia general del catálogo. `ServicioCritico` no la duplica: agrega una prioridad operativa y la decisión de escalar de inmediato. El perfil es opcional porque no todos los servicios necesitan un tratamiento especial.

## Regla D: cadena de escalamiento

La regla asignada es la ejecución de escalamiento. Se implementó en `EjecucionEscalamientoService` y se expone mediante `POST /api/incidentes/{id}/escalamientos/ejecutar`. Cuando un incidente no está reconocido, resuelto, mitigado o cerrado, el proceso recorre sus impactos y selecciona las políticas de los servicios afectados. Para cada política respeta el orden de `PasoEscalamiento` y evita repetir notificaciones ya emitidas.

En esta etapa se ejecutan los pasos con `destinatarioDirecto`. Un paso que usa `Rotacion` necesita primero resolver quién está de guardia (regla C), por eso se mantiene explícitamente fuera de este flujo en lugar de elegir una persona arbitraria.

Ante servicios impactados con rotaciones distintas, se escalan todas las cadenas aplicables: cada servicio afectado representa una responsabilidad operativa diferente. El perfil `ServicioCritico` conserva la prioridad necesaria para ordenar ese procesamiento en una evolución posterior; no elimina los demás destinos.

## Verificación

1. Crear servicios y una dependencia dirigida entre proveedor y dependiente.
2. Crear un incidente asociado al servicio proveedor.
3. Registrar o ejecutar la propagación y verificar que aparece un `ImpactoPropagado` sobre el dependiente.
4. Ejecutar el proceso por segunda vez y comprobar que no duplica impactos ni notificaciones.
5. Probar un ciclo de dependencias y verificar que el proceso finaliza.

Las migraciones generadas para las tres entidades están incluidas en `config/liquibase/master.xml`, por lo que Liquibase crea sus tablas al iniciar la aplicación.
