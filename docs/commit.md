# 📝 Estándar de Mensajes de Commit - FenixTech (Backend)

Para mantener un historial de proyecto limpio y facilitar la integración con nuestro **GitHub Project**, seguimos el estándar de **Conventional Commits** adaptado a nuestro entorno de Spring Boot.

---

## 🏗️ Estructura del Mensaje

Cada commit debe seguir este formato:

`tipo(contexto opcional): descripción`

### 1. Tipos de Commit
Utilizamos estos prefijos según el cambio realizado:

* **`feat`**: Una nueva funcionalidad para el sistema (ej: crear el endpoint de login).
* **`fix`**: Corrección de un error o bug en la lógica.
* **`test`**: Añadir o modificar pruebas unitarias o de integración (JUnit, Mockito).
* **`perf`**: Cambios de código que mejoran el rendimiento (ej: optimización de consultas SQL).
* **`style`**: Cambios de formato que no afectan la lógica (espacios, Checkstyle, formato de código Java).
* **`refactor`**: Cambio en el código que ni arregla un bug ni añade una función (ej: extraer un método repetido).
* **`docs`**: Solo cambios en la documentación (como Swagger o este README).
* **`chore`**: Tareas de mantenimiento, instalación de dependencias o configuración general.

### 2. El Contexto (Scope)
Es opcional pero muy recomendado para saber qué capa o módulo de Spring Boot has tocado. En nuestro backend utilizamos los siguientes contextos:

* **`(api)`**: Controladores (Controllers) y definición de endpoints REST.
* **`(auth)`**: Lógica específica de autenticación (login, registro, tokens).
* **`(security)`**: Configuraciones de Spring Security, filtros y permisos de acceso.
* **`(db)`**: Migraciones (Flyway/Liquibase), scripts SQL o configuración de la base de datos.
* **`(service)`**: Capa de lógica de negocio (`@Service`).
* **`(entity)`**: Modelos de dominio y entidades JPA (`@Entity`).
* **`(dto)`**: Objetos de transferencia de datos (Data Transfer Objects).
* **`(config)`**: Clases de configuración (`@Configuration`), `application.yml` o `application.properties`.
* **`(pom)`**: Cambios en el archivo `pom.xml` (gestión de dependencias y plugins de Maven).

---

## 🔗 Vínculo con GitHub Projects

Es fundamental vincular cada commit con su **User Story** o **Task**. Para ello, añade el número de la Issue precedido por `#` al final del mensaje.

* **`closes #ID`**: Mueve automáticamente la tarea a la columna **Done**.
* **`ref #ID`**: Solo vincula el commit a la tarea sin cerrarla.

---

## ✅ Ejemplos Prácticos

### Si añades una nueva dependencia:
\`\`\`bash
git commit -m "chore(pom): añadir dependencia de spring-boot-starter-security closes #15"
\`\`\`

### Si creas un nuevo endpoint:
\`\`\`bash
git commit -m "feat(api): crear endpoint GET para listar usuarios closes #22"
\`\`\`

### Si modificas un modelo de la base de datos:
\`\`\`bash
git commit -m "feat(entity): añadir campo de fecha de creacion al modelo User ref #18"
\`\`\`

### Si arreglas un fallo en un test:
\`\`\`bash
git commit -m "test(service): corregir assert en UserServiceImplTest ref #30"
\`\`\`