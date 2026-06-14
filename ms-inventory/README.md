# MS Inventory

Microservicio de gestión de inventario de productos.

## Tecnologías

- Spring Boot 3.2, JPA, MySQL
- Swagger UI: `http://localhost:8090/swagger-ui/index.html`
- JaCoCo: cobertura mínima 85%

## Endpoints principales

| Método | Ruta | Descripción |
|--------|------|-------------|
| `GET` | `/api/inventory` | Listar inventario |
| `GET` | `/api/inventory/{id}` | Buscar por ID |
| `POST` | `/api/inventory` | Registrar stock |
| `PUT` | `/api/inventory/{id}` | Actualizar stock |
| `DELETE` | `/api/inventory/{id}` | Eliminar registro |

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
