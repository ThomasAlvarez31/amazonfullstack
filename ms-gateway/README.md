# MS Gateway

BFF (Backend For Frontend) / API Gateway que enruta peticiones a los microservicios.

## Tecnologías

- Spring Cloud Gateway, Spring Boot 3.2
- Swagger UI: `http://localhost:8080/swagger-ui/index.html`
- JaCoCo: cobertura mínima 85%

## Endpoints principales

| Método | Ruta | Descripción |
|--------|------|-------------|
| `*` | `/api/auth/**` | → ms-auth:8081 |
| `*` | `/api/products/**` | → ms-products:8085 |
| `*` | `/api/orders/**` | → ms-orders:8084 |

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
