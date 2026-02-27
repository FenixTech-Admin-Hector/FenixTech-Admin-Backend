# 📝 Estándar de Mensajes de Commit - FenixTech

Para mantener un historial de proyecto limpio y facilitar la integración con nuestro **GitHub Project**, seguimos el estándar de **Conventional Commits**.

---

## 🏗️ Estructura del Mensaje

Cada commit debe seguir este formato:

`tipo(contexto opcional): descripción`

### 1. Tipos de Commit
Utilizamos estos prefijos según el cambio realizado:

* **`feat`**: Una nueva funcionalidad para el usuario (ej: añadir el mega-menu).
* **`fix`**: Corrección de un error o bug.
* **`style`**: Cambios que no afectan la lógica (CSS, SCSS, espacios, formato).
* **`refactor`**: Cambio en el código que ni arregla un bug ni añade una función.
* **`docs`**: Solo cambios en la documentación (como este README).
* **`chore`**: Tareas de mantenimiento, instalación de paquetes o configuración (ej: `.gitignore`, `package.json`).

### 2. El Contexto (Scope)
Es opcional pero recomendado para saber qué parte has tocado. Ejemplos en nuestro caso: `(header)`, `(card)`, `(scss)`, `(html)`.

---

## 🔗 Vínculo con GitHub Projects

Es fundamental vincular cada commit con su **User Story** o **Task**. Para ello, añade el número de la Issue precedido por `#` al final del mensaje.

* **`closes #ID`**: Mueve automáticamente la tarea a la columna **Done**.
* **`ref #ID`**: Solo vincula el commit a la tarea sin cerrarla.



---

## ✅ Ejemplos Prácticos

### Si creas la estructura inicial:
```bash
git commit -m "chore: inicializar carpetas scss y configurar gitignore"