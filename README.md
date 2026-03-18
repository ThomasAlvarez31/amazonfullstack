Recreacion de Amazon para asignatura fullstack 3 realizado con las tecnologias de Java con Spring Boot

## Puertos de Microservicios

| Servicio      | Puerto |
|---------------|--------|
| MS-Auth       | 8081   |
| MS-Users      |  n/a   |
| MS-Products   |  n/a   |
| MS-Orders     |  n/a   |
| MS-Inventory  |  n/a   |
| MS-Payments   |  n/a   |


# Ejecucion
Ejecucion MS-Auth
```Bash
mvn package clean
java -jar target/ms-auth-0.0.1-SNAPSHOT.jar
```