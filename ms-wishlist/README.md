# MS Wishlist

Microservicio de lista de deseos por usuario.

## Tecnologías

- Spring Boot 3.2, JPA, MySQL
- Swagger UI: `http://localhost:8093/swagger-ui/index.html`
- JaCoCo: cobertura mínima 85%

## Endpoints principales

| Método | Ruta | Descripción |
|--------|------|-------------|
| `GET` | `/api/wishlist/{userId}` | Lista de deseos del usuario |
| `POST` | `/api/wishlist` | Agregar a lista |
| `DELETE` | `/api/wishlist/{id}` | Eliminar ítem |
| `DELETE` | `/api/wishlist/user/{userId}/product/{productId}` | Eliminar por usuario+producto |

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
