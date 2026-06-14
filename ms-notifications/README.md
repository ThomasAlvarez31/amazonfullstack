# MS Notifications

Microservicio de notificaciones. Consume eventos de órdenes desde RabbitMQ y persiste notificaciones.

## Tecnologías

- Spring Boot 3.2, RabbitMQ (AMQP), Kafka, MySQL
- Swagger UI: `http://localhost:8089/swagger-ui/index.html`
- JaCoCo: cobertura mínima 85%

## Endpoints principales

| Método | Ruta | Descripción |
|--------|------|-------------|
| `GET` | `/api/notifications` | Listar notificaciones |
| `GET` | `/api/notifications/user/{userId}` | Notificaciones por usuario |
| `DELETE` | `/api/notifications/{id}` | Eliminar notificación |

## Variables de entorno

| Variable | Default | Descripción |
|----------|---------|-------------|
| `DB_HOST` | `localhost` | Host de MySQL |
| `DB_USER` | `root` | Usuario MySQL |
| `DB_PASSWORD` | `rootpass` | Contraseña MySQL |
| `RABBITMQ_HOST` | `localhost` | Host RabbitMQ *(si aplica)* |
| `REDIS_HOST` | `localhost` | Host Redis *(si aplica)* |

## Ejecución local

```bash
mvn spring-boot:run
```

## Tests

```bash
mvn verify   # ejecuta tests + reporte JaCoCo
```

El reporte HTML queda en `target/site/jacoco/index.html`.
