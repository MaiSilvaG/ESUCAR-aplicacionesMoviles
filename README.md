# ESUCAR – Aplicación móvil de gestión académica

Aplicación Android desarrollada con **Kotlin** y **Jetpack Compose** para la *Escuela de Suboficiales de Carabineros de Chile (ESUCAR)*. Permite a estudiantes y docentes gestionar asignaturas, material de estudio y consultas, con una base de datos local (**Room / SQLite**) y una vista distinta para cada rol.

## Integrante

- Maria Silva

## Tecnologías

- Kotlin y Jetpack Compose (Material 3)
- Navigation Compose (barra de navegación inferior y rutas secundarias)
- Room (SQLite) con KSP para la base de datos local

## Funcionalidades implementadas

### Generales

- Inicio de sesión validado con la base de datos, con mensajes de error.
- Registro de nuevos usuarios con validaciones: campos obligatorios, correo con formato válido, que las contraseñas coincidan y que los correo no se repitan.
- Dos roles, **Estudiante** y **Docente**, con pantallas distintas en inicio, asignaturas, detalle de asignatura, consultas y perfil.
- Base de datos Room con las tablas `usuarios`, `asignaturas`, `unidades`, `materiales` y `consultas`, relacionadas con claves foráneas.
- Carga de datos iniciales (asignaturas, unidades, materiales, consultas y usuarios) la primera vez que se crea la base.
- Tema personalizado en verde y naranjo, con logo y nombre de la institución en el inicio de sesión.

### Estudiante

- **Inicio:** saludo con su nombre, progreso general, tarjetas de sus cursos con barra de progreso y acceso directo para enviar una consulta.
- **Asignaturas:** lista de asignaturas con el porcentaje de materiales estudiados.
- **Detalle de asignatura:** unidades desplegables con sus materiales. Puede marcar cada material como estudiado y el avance se guarda en la base de datos.
- **Consultas:** crea consultas eligiendo asignatura y unidad, ve la lista, abre el detalle y lee la respuesta del docente.
- **Perfil:** datos del usuario, mi actividad (asignaturas inscritas y progreso general), mis asignaturas, configuración y cierre de sesión.

### Docente

- **Inicio:** saludo con su nombre, métricas (cantidad de asignaturas y consultas pendientes), tarjetas de los cursos sin barra de progreso y acceso a las consultas recibidas.
- **Asignaturas y detalle:** ve las asignaturas, sus unidades y sus materiales, sin controles de estudio.
- **Publicar material:** formulario con asignatura autocompletada, unidad en lista desplegable, título del material y tipo. El material queda disponible de inmediato para los estudiantes.
- **Consultas:** ve las consultas con el nombre del estudiante, abre el detalle y responde las que no tienen respuesta.
- **Perfil:** datos del usuario, mi actividad (asignaturas inscritas y consultas por responder), mis asignaturas, configuración y cierre de sesión.

## Estructura del proyecto

```
app/src/main/java/com/example/esucar/
├── data/
│   ├── AppDatabase.kt        # base de datos Room y carga inicial
│   ├── DatosIniciales.kt     # datos de ejemplo
│   ├── Sesion.kt             # usuario que inició sesión
│   ├── dao/                  # consultas a la base de datos
│   ├── entity/               # tablas
│   └── relation/             # relaciones entre tablas
├── model/
│   └── SubDestination.kt     # rutas de las pantallas secundarias
└── ui/
    ├── theme/                # colores, tema y tipografía
    └── view/
        ├── asignaturas/      # lista, detalle y publicar material
        ├── consultas/        # lista, detalle y formulario
        ├── home/             # inicio de estudiante y docente
        ├── login/            # inicio de sesión y registro
        ├── navigation/       # rutas y barra de navegación
        └── perfil/           # perfil de estudiante y docente
```

### Usuarios de prueba

| Rol        | Correo         | Contraseña |
|------------|----------------|------------|
| Estudiante | maria@esucar.com     | 1234       |
| Docente    | profe@esucar.com     | 1234       |

Se definen en `data/DatosIniciales.kt`.
