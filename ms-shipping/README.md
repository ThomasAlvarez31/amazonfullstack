# MS Shipping

Microservicio de gestión de envíos asociados a órdenes.

## Tecnologías

- Spring Boot 3.2, JPA, MySQL
- Swagger UI: `http://localhost:8088/swagger-ui/index.html`
- JaCoCo: cobertura mínima 85%

## Endpoints principales

| Método | Ruta | Descripción |
|--------|------|-------------|
| `GET` | `/api/shipping` | Listar envíos |
| `GET` | `/api/shipping/{id}` | Buscar por ID |
| `GET` | `/api/shipping/order/{orderId}` | Envíos por orden |
| `POST` | `/api/shipping` | Crear envío |
| `PUT` | `/api/shipping/{id}` | Actualizar envío |

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
