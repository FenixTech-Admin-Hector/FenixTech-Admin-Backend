# 🌿 Estándar de Gestión de Ramas (Git Flow) - FenixTech

Para garantizar la estabilidad del proyecto y facilitar la colaboración, seguimos un flujo de trabajo basado en **ramas de funcionalidad** (feature branches).

---

## 1. La Rama Principal (`main`)
* **Código Estable**: Contiene únicamente código que funciona perfectamente y ha sido revisado.
* **Protección**: **PROHIBIDO** realizar commits directos a `main`. Todo cambio debe entrar vía Pull Request.

---

## 2. Tipos de Ramas y Nomenclatura
Cada vez que empieces una tarea del Project, crea una rama con el prefijo correspondiente:

| Prefijo | Uso | Ejemplo |
| :--- | :--- | :--- |
| `feat/` | Nueva funcionalidad o componente | `feat/hero-video` |
| `fix/` | Corrección de errores | `fix/error-scss-cards` |
| `style/` | Cambios de diseño/estilo (sin lógica) | `style/colores-header` |
| `docs/` | Cambios en la documentación | `docs/guia-ramas` |
| `chore/` | Mantenimiento o configuración | `chore/update-gitignore` |

---

## 3. El Ciclo de Desarrollo Estándar

Sigue estos pasos para cada tarea asignada:

### Paso 1: Sincronización
Antes de crear una rama, asegúrate de tener lo último de tus compañeros:
```bash
git checkout main
git pull origin main
```

### Paso 2: Creación de la Rama
Crea una rama descriptiva para tu tarea asociada con el #ID de la issue:
```bash
git checkout -b feat/12-nombre-de-tu-tarea
```

### Paso 3: Trabajo y Commit
Desarrolla tu código en los archivos SCSS/HTML correspondientes. Al terminar, haz el commit siguiendo el estándar:
```bash
git add .
git commit -m "feat(contexto): descripción breve closes #ID"
```
(Recuerda usar closes #ID para vincularlo a la User Story del Project).

### Paso 4: Subida y Pull Request
Sube tu rama al servidor y abre una Pull Request en GitHub:
```bash
git push origin feat/12-nombre-de-tu-tarea
```

## ⚠️ Reglas de Oro
1. **Unir con Main:** Antes de dar por finalizada una tarea, asegúrate de que tu rama está al día con main para evitar conflictos.

2. **Aviso de Push:** Como indica el index.html, AVISAD POR EL GRUPO cuando hagáis un push importante a main para que el resto pueda actualizar su local.

3. **Revisión:** Al menos un compañero debería revisar la Pull Request antes de hacer el merge definitivo.
