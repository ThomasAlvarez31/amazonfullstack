Recreacion de Amazon para asignatura fullstack 3 realizado con las tecnologias de Java con Spring Boot

## Puertos de Microservicios

| Servicio      | Puerto |
|---------------|--------|
| MS-Auth       | 9000   |
| MS-Users      | 8082   |
| MS-Inventory  | 8083   |
| MS-Orders     | 8084   |
| MS-Products   | 8085   |
| MS-Payments   | 8086   |
| MS-Cart       | 8087   |


# Ejecucion
Ejecucion MS-Auth
```Bash
mvn package clean
java -jar target/ms-auth-0.0.1-SNAPSHOT.jar
```
