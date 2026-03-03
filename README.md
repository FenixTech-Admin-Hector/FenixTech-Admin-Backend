# 🐦‍🔥 FenixTech - Backend API

Este repositorio contiene el código fuente del servidor (Backend) para el proyecto **FenixTech**. Está construido sobre **Java 21** y **Spring Boot**, utilizando una arquitectura de microservicios contenerizada con **Docker** para garantizar un entorno de desarrollo robusto y replicable.

---

## 🚀 Guía de Inicio Rápido

Sigue estos pasos para levantar la infraestructura de base de datos y servidor localmente.

### 1. Prerrequisitos
*   **Docker Desktop**: Debe estar instalado y ejecutándose.
*   **Java 21 JDK**: Necesario para compilar el proyecto localmente (aunque Docker se encarga de la ejecución).
*   **Maven**: (Opcional) El proyecto incluye un wrapper (`mvnw`), pero tenerlo instalado es útil.

### 2. Configuración de Entorno (.env)
El proyecto utiliza variables de entorno para no quemar credenciales en el código. Crea un archivo llamado `.env` en la raíz del proyecto (`/FenixTech-BackEnd/.env`) con el siguiente contenido:

```properties
# Configuración de Base de Datos
DB_NAME=fenixdb
DB_USER=fenixuser
DB_PASSWORD=fenixpass
DB_ROOT_PASSWORD=rootpass

# Puertos expuestos en tu máquina (Host)
DB_PORT=3306
APP_PORT=8080
```

### 3. Ejecución Automática (Recomendado)
Hemos preparado un script en Windows para facilitar el despliegue. Este script detiene contenedores antiguos, compila el código Java, construye la imagen Docker y levanta todo el sistema.

Simplemente ejecuta en la terminal:

```cmd
.\start.bat
```

### 4. Ejecución Manual
Si prefieres hacerlo paso a paso:

1.  **Compilar la aplicación:**
    ```bash
    ./mvnw clean package -DskipTests
    ```
2.  **Levantar Docker:**
    ```bash
    docker-compose up -d --build
    ```

---

## 🐳 Arquitectura Docker

El proyecto utiliza **Docker Compose** para orquestar dos servicios principales que se comunican a través de una red interna llamada `fenix_network`.

### Estructura de Servicios

1.  **Base de Datos (`mariadb`)**:
    *   Imagen: `mariadb:11.4`
    *   Puerto Interno: `3306`
    *   Persistencia: Los datos se guardan en el volumen `./data_mariadb` para que no se pierdan al reiniciar.

2.  **Aplicación Backend (`app-spring`)**:
    *   Imagen: Construida desde el `Dockerfile` (Java 21 + Alpine Linux).
    *   Puerto Interno: `8080`
    *   Conexión: Se conecta a la BD usando el host `mariadb` (nombre del servicio en la red Docker).

### Diagrama de Red
```plaintext
[ TU ORDENADOR (Host) ]
      │
      ├── Puerto 8080 ────────┐
      │                       ▼
      │                 [ Contenedor: app-spring ]
      │                 (Spring Boot API)
      │                       │
      │                 (Red Interna: fenix_network)
      │                       │
      ├── Puerto 3306 ────────┼──────┐
                              ▼      │
                        [ Contenedor: mariadb ]
                        (Base de Datos)
```

---

## 🛠️ Tecnologías Usadas

*   **Lenguaje:** Java 21
*   **Framework:** Spring Boot 3.x
*   **Base de Datos:** MariaDB
*   **ORM:** Hibernate / JPA
*   **Gestor de Dependencias:** Maven
*   **Contenedores:** Docker & Docker Compose

## 🗄️ Link Esquema de Base de Datos