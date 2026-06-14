# MS Orders

Microservicio de órdenes de compra. Publica evento `order.created` a RabbitMQ al crear una orden.

## Tecnologías

- Spring Boot 3.2, RabbitMQ (AMQP), JPA, MySQL
- Swagger UI: `http://localhost:8084/swagger-ui/index.html`
- JaCoCo: cobertura mínima 85%

## Endpoints principales

| Método | Ruta | Descripción |
|--------|------|-------------|
| `GET` | `/api/orders` | Listar órdenes |
| `GET` | `/api/orders/{id}` | Buscar por ID |
| `POST` | `/api/orders` | Crear orden (publica evento RabbitMQ) |
| `PUT` | `/api/orders/{id}` | Actualizar orden |
| `DELETE` | `/api/orders/{id}` | Eliminar orden |

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
