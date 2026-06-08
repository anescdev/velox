# Velox 🕐

Software de escritorio para la **contabilización de horas de trabajo** de empleadas. Ligero, específico y sin distracciones.

(Imagen en futuro)

## ¿Por qué existe este proyecto?

Mis padres gestionaban las horas de sus trabajadoras con **Factucont**, un software de facturación que no está pensado para eso. La alternativa era Excel, pero acababa siendo una hoja gigantesca llena de copias y pegados que crecía sin control.

Evalué soluciones más completas como **Odoo** — de hecho sé crear módulos para él — pero tiene demasiados campos y conceptos que no necesitan. Buscan algo de escritorio, tradicional, que haga exactamente lo que necesitan y nada más.

Así nació Velox: rápido de usar, sin curva de aprendizaje, sin funcionalidades innecesarias. El nombre no es casual — el objetivo es que mi padre haga en segundos lo que antes le llevaba minutos.

---

## Funcionalidades (v1.0 MVP)

- **Gestión de trabajadoras** — crear, modificar, listar y eliminar
- **Sumatorios de horas** — organizados mes a mes por trabajadora
- **Control de pendientes** — si un sumatorio del mes no ha sido creado, queda marcado como pendiente
- **Exportación** — impresión en papel o exportación a PDF para enviar a cada trabajadora

---

## Tecnologías

| Tecnología | Uso |
|---|---|
| Java 23 o superior | Lenguaje principal |
| JavaFX 25 | Interfaz gráfica de escritorio |
| SQLite + JDBC | Base de datos local embebida |
| Ikonli + [Typicon](https://kordamp.org/ikonli/cheat-sheet-typicons.html) | Iconografía |
| Sin definir | Generación de PDFs |
| Maven | Gestión de dependencias y build |

---

## Arquitectura

La aplicación sigue el patrón **MVVM** (Model-View-ViewModel):

- **Vista** (FXML) — define la estructura visual, sin lógica
- **ViewModel** — coordina el estado de la UI y delega en los servicios
- **Servicio** — contiene la lógica de negocio
- **Repositorio** — acceso a la base de datos SQLite

La navegación principal se gestiona mediante un `TabPane` donde cada sección (trabajadoras, sumatorios) se abre como una pestaña. Los formularios de creación y edición se presentan como `Dialog` modales. La base de datos es un único archivo `.db` local, lo que simplifica las copias de seguridad.

---

## Requisitos

- Java 23 o superior
- Sistema operativo: Windows, Linux o macOS

---

## Instalación

### Para desarrolladores

```bash
git clone https://github.com/anescdev/velox.git
cd velox
mvn javafx:run
```

### Para usuarios finales

Descarga el instalador correspondiente a tu sistema operativo desde la sección [Releases](https://github.com/anescdev/velox/releases) y ejecútalo. No necesitas instalar Java ni ninguna dependencia adicional.

> Los instaladores nativos se generarán a partir de la versión 1.0.

---

## Roadmap

- [ ] Copias de seguridad automáticas configurables
- [ ] Exportación a PDF
- [ ] Más idiomas

---

## Autor

Desarrollado por [AnesCDev](https://github.com/anescdev)

---

## Licencia


Este proyecto está licenciado bajo la **GNU General Public License v3.0** — consulta el archivo [LICENSE](LICENSE) para más detalles.
 
En resumen: puedes usar, modificar y distribuir este software libremente, pero cualquier trabajo derivado debe mantener la misma licencia y ser también open source.
