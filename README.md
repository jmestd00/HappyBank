# HappyBank
## ¿En qué consiste?
Este proyecto consiste en un sistema de gestión de cuentas bancarias, 
el cual permite realizar operaciones como:
* Distinción entre administradores y clientes
1. Para los administradores:
   * Crear cuentas
   * Eliminar cuentas
   * Modificar los datos de un cliente
   * Consultar el listado de usuarios del banco
   * Consultar el historial de transacciones de una cuenta
   * Activar o desactivar la copia de seguridad

2. Para los clientes:
   * Realizar transferencias entre cuentas
   * Consultar el saldo de su propia cuenta
   * Revisar el capital y los datos de su propia cuenta
   * Consultar el historial de transacciones de una cuenta

3. Para el sistema:
   * Realizar copias de seguridad de los datos
   * Guardar el historial de acciones a través de un log

## Tecnologías
* Java 21
* JavaFX 21.0.5
* JUnit 4.13.1
* MySQL 15.1
* Maven 4.0.0
* SceneBuilder 11.0.0
* Git 2.39.5
* Log4j2 2.23.1

## ¿Cómo se ejecuta?
Se descarga el archivo correspondiente al sistema operativo desde la release y se ejecuta en la terminal el comando
```java -jar HappyBank.jar```

Alternativamente, a través del código fuente del programa se podrá generar el archivo jar mediante el comando
```mvn clean package```, para ser ejecutado de la misma forma que el anterior.

## Desarrolladores
* Jose Ángel Mestas Díaz
* Raúl Justel Aller
* Iria Rodríguez Fernández
* David Fernández Janeiro

## Estado del proyecto
El proyecto se encuentra terminado, por lo que se agradece el reporte de cualquier problema o bug a través de las issues de GitHub.
