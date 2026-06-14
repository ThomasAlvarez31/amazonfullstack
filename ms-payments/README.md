# MS Payments

Microservicio de gestión de pagos asociados a órdenes.

## Tecnologías

- Spring Boot 3.2, JPA, MySQL
- Swagger UI: `http://localhost:8087/swagger-ui/index.html`
- JaCoCo: cobertura mínima 85%

## Endpoints principales

| Método | Ruta | Descripción |
|--------|------|-------------|
| `GET` | `/api/payments` | Listar pagos |
| `GET` | `/api/payments/{id}` | Buscar por ID |
| `POST` | `/api/payments` | Registrar pago |
| `PUT` | `/api/payments/{id}` | Actualizar pago |
| `DELETE` | `/api/payments/{id}` | Eliminar pago |

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
