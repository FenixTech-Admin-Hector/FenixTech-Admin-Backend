# 🌿 Estándar de Gestión de Ramas (Git Flow) - FenixTech (Backend)

Para garantizar la estabilidad de nuestra API y facilitar la colaboración, seguimos un flujo de trabajo basado en **ramas de funcionalidad** (feature branches).

---

## 1. La Rama Principal (`main`)
* **Código Estable**: Contiene únicamente código que compila, pasa los tests y ha sido revisado.
* **Protección**: **PROHIBIDO** realizar commits directos a `main`. Todo cambio debe entrar vía Pull Request.

---

## 2. Tipos de Ramas y Nomenclatura
Cada vez que empieces una tarea del Project, crea una rama con el prefijo correspondiente:

| Prefijo | Uso | Ejemplo |
| :--- | :--- | :--- |
| `feat/` | Nueva funcionalidad, capa o endpoint | `feat/15-jwt-auth` |
| `fix/` | Corrección de errores | `fix/22-nullpointer-user-service` |
| `test/` | Añadir o arreglar tests exclusivamente | `test/30-cobertura-auth` |
| `docs/` | Cambios en la documentación o Swagger | `docs/actualizar-readme-db` |
| `chore/` | Mantenimiento, dependencias, Docker | `chore/update-flyway` |

---

## 3. El Ciclo de Desarrollo Estándar

Sigue estos pasos para cada tarea asignada:

### Paso 1: Sincronización
Antes de crear una rama, asegúrate de tener lo último de tus compañeros:
\`\`\`bash
git checkout main
git pull origin main
\`\`\`

### Paso 2: Creación de la Rama
Crea una rama descriptiva para tu tarea asociada con el #ID de la issue:
\`\`\`bash
git checkout -b feat/12-nombre-de-tu-tarea
\`\`\`

### Paso 3: Desarrollo y Validación Local (¡CRÍTICO!)
Desarrolla tu código. Antes de hacer commit, **asegúrate de que el proyecto compila y los tests pasan**:
\`\`\`bash
mvn clean test
# (o ./gradlew test si usáis Gradle)
\`\`\`

### Paso 4: Commit
Si todo está en verde, haz el commit siguiendo el estándar:
\`\`\`bash
git add .
git commit -m "feat(contexto): descripción breve closes #ID"
\`\`\`

### Paso 5: Subida y Pull Request
Sube tu rama al servidor y abre una Pull Request en GitHub:
\`\`\`bash
git push origin feat/12-nombre-de-tu-tarea
\`\`\`

---

## ⚠️ Reglas de Oro

1. **Unir con Main:** Antes de dar por finalizada una tarea, asegúrate de que tu rama está al día con `main` para evitar conflictos de merge.
2. **Aviso de Push Crítico:** AVISAD POR EL GRUPO cuando hagáis merges a `main` que incluyan:
   * Cambios en **Entidades** o **Migraciones de Base de Datos** (Flyway/Liquibase).
   * Cambios en los archivos **`application.properties` o `application.yml`** (nuevas variables de entorno requeridas).
3. **Revisión:** Al menos un compañero debería revisar la Pull Request antes de hacer el merge definitivo.