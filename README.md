# E-Commerce Fullstack

Aplicación de e-commerce desarrollada con arquitectura de microservicios usando Java Spring Boot, frontend en TypeScript y contenerización con Docker.

## Microservicios y Puertos

| Servicio           | Puerto | Descripción                        |
|--------------------|--------|------------------------------------|
| ms-gateway         | 8080   | API Gateway (punto de entrada)     |
| ms-auth            | 9000   | Autenticación y JWT                |
| ms-users           | 8082   | Gestión de usuarios                |
| ms-inventory       | 8083   | Control de inventario              |
| ms-orders          | 8084   | Gestión de pedidos                 |
| ms-products        | 8085   | Catálogo de productos              |
| ms-payments        | 8086   | Procesamiento de pagos             |
| ms-cart            | 8087   | Carrito de compras                 |
| ms-shipping        | 8088   | Envíos y despacho                  |
| ms-notifications   | 8089   | Notificaciones vía Kafka           |
| ms-reviews         | 8090   | Reseñas de productos               |
| ms-search          | 8091   | Búsqueda de productos              |
| ms-wishlist        | 8092   | Lista de deseos                    |

## Tecnologías

- **Backend:** Java 17, Spring Boot 3.2, Spring Cloud Gateway, Spring Data JPA, H2
- **Mensajería:** Apache Kafka + Zookeeper
- **Frontend:** TypeScript, HTML, CSS (ES Modules nativos)
- **Contenedores:** Docker, Docker Compose

## Ejecución con Docker (recomendado)

Levanta todos los servicios con un solo comando:

```bash
docker-compose up --build
```

El frontend se accede directamente abriendo los archivos HTML en el navegador. Todos los MS se comunican a través del gateway en `http://localhost:8080`.

## Ejecución individual (desarrollo)

Requiere Java 17 y Maven instalados.

```bash
cd ms-auth
mvn clean package -DskipTests
java -jar target/ms-auth-0.0.1-SNAPSHOT.jar
```

Repetir para cada microservicio. El gateway debe iniciarse último.

## Estructura del proyecto

```
amazonfullstack/
├── ms-gateway/        # API Gateway - puerto 8080
├── ms-auth/           # Autenticación  - puerto 9000
├── ms-users/          # Usuarios        - puerto 8082
├── ms-inventory/      # Inventario      - puerto 8083
├── ms-orders/         # Pedidos         - puerto 8084
├── ms-products/       # Productos       - puerto 8085
├── ms-payments/       # Pagos           - puerto 8086
├── ms-cart/           # Carrito         - puerto 8087
├── ms-shipping/       # Envíos          - puerto 8088
├── ms-notifications/  # Notificaciones  - puerto 8089
├── ms-reviews/        # Reseñas         - puerto 8090
├── ms-search/         # Búsqueda        - puerto 8091
├── ms-wishlist/       # Lista de deseos - puerto 8092
├── front-end/         # Frontend TypeScript/HTML/CSS
└── docker-compose.yml
```
