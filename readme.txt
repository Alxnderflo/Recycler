# RecyclerPrueba App - Flujo Completo de la Aplicación

## Descripción General
RecyclerPrueba es una aplicación Android educativa desarrollada en Java que permite gestionar contactos (posiblemente estudiantes o profesores) y asociar preguntas de evaluación a cada uno de ellos. La aplicación utiliza Firebase Firestore para el almacenamiento de datos en la nube, permitiendo sincronización en tiempo real. La interfaz principal se basa en un ViewPager2 con navegación inferior para alternar entre tres secciones principales: Inicio (Home), Puntajes (Score) y Profesor (Teacher).

## Arquitectura de la Aplicación
- Lenguaje: Java
- Framework: Android SDK con ViewPager2 y RecyclerView
- Base de Datos: Firebase Firestore
- Navegación: BottomNavigationView con ViewPager2
- Patrón de Diseño: MVP (Model-View-Presenter) implícito con Fragments y Activities

## Modelos de Datos
 Contacts
- Campos: name (String), phoneNo (String)
- Uso: Representa un contacto (estudiante/profesor) con nombre y número telefónico

 Pregunta
- Campos: enunciado (String), correcta (String), incorrecta (String)
- Uso: Representa una pregunta de evaluación con su enunciado y dos opciones de respuesta

## Estructura de Firebase Firestore
```
contacts2/
├── {contactoId}/
│   ├── name: String
│   ├── phoneNo: String
│   ├── fechaCreacion: Timestamp
│   └── preguntas/
│       ├── {preguntaId}/
│       │   ├── enunciado: String
│       │   ├── correcta: String
│       │   └── incorrecta: String
```

## Flujo Principal de la Aplicación

 1. Inicio de la Aplicación
- La aplicación se lanza desde `MainActivity`, que es la actividad principal declarada en el `AndroidManifest.xml`.
- En `onCreate()`, se configura el ViewPager2 con tres fragments: `HomeFragment`, `ScoreFragment`, y `TeacherFragment`.
- Se establece la navegación inferior (`BottomNavigationView`) que permite alternar entre los fragments.
- Por defecto, se muestra el `HomeFragment` (posición 0).

 2. Fragmento de Inicio (HomeFragment)
- Propósito: Mostrar la lista de contactos almacenados en Firebase.
- Inicialización:
  - Se obtiene una instancia de `FirebaseFirestore`.
  - Se inicializan listas para contactos y sus IDs.
  - Se configura un `RecyclerView` con `LinearLayoutManager`.
- Interfaz:
  - Muestra una lista de contactos usando `ContactsAdapter`.
  - Incluye un `FloatingActionButton` (FAB) para agregar nuevos contactos.
- Funcionalidad:
  - Al cargar la vista (`onViewCreated`), se configura el FAB para navegar a `CrearContactoActivity`.
  - Se configura el `ContactsAdapter` con un listener para clics en contactos.
  - Se llama a `obtenerContactos()` que establece un `SnapshotListener` en la colección "contacts2" de Firestore.
  - Los cambios en Firestore se reflejan automáticamente en la lista gracias al listener en tiempo real.
- Interacción del Usuario:
  - Presionar FAB: Navega a `CrearContactoActivity` para crear un nuevo contacto.
  - Hacer clic en un contacto: Abre `PreguntasActivity` pasando el ID del contacto seleccionado como extra en el Intent.

 3. Creación de Contacto (CrearContactoActivity)
- Propósito: Formulario para crear y guardar un nuevo contacto en Firebase.
- Inicialización:
  - Se obtiene instancia de `FirebaseFirestore`.
  - Se referencian los campos `EditText` para nombre y teléfono, y el botón guardar.
- Interfaz:
  - Campos de entrada: Nombre y Teléfono.
  - Botón "Guardar".
- Funcionalidad:
  - Al presionar el botón guardar, se valida que los campos no estén vacíos.
  - Se crea un `HashMap` con los datos del contacto, incluyendo `fechaCreacion` con `FieldValue.serverTimestamp()`.
  - Se agrega el documento a la colección "contacts2" de Firestore.
  - Éxito: Muestra Toast de confirmación, obtiene el ID del nuevo documento, y navega a `PreguntasActivity` pasando el ID como extra. Finaliza la actividad actual.
  - Error: Muestra Toast de error y registra el error en Log.
- Flujo: Crear Contacto → Guardar en Firebase → Navegar a Preguntas del Contacto.

 4. Actividad de Preguntas (PreguntasActivity)
- Propósito: Mostrar y gestionar las preguntas asociadas a un contacto específico.
- Inicialización:
  - Recibe el `contactoId` como extra del Intent.
  - Si no hay ID válido, muestra error y finaliza.
  - Inicializa `FirebaseFirestore` y lista de preguntas.
- Interfaz:
  - `RecyclerView` para mostrar preguntas usando `PreguntasAdapter`.
  - Layout vacío (`layoutVacio`) cuando no hay preguntas.
  - Dos botones: "btnAdd" y "btnCrearPregunta" (ambos hacen lo mismo: abrir creación de pregunta).
- Funcionalidad:
  - En `onCreate()`, llama a `cargarPreguntas()` que consulta la subcolección "preguntas" del documento del contacto.
  - Actualiza la vista: muestra lista si hay preguntas, layout vacío si no.
  - En `onResume()`, recarga las preguntas para reflejar cambios (ej. después de crear una nueva).
  - Los botones de agregar navegan a `CrearPreguntasActivity` pasando el `contactoId`.
- Interacción del Usuario:
  - Ver lista de preguntas del contacto.
  - Agregar nueva pregunta → Navega a `CrearPreguntasActivity`.

 5. Creación de Pregunta (CrearPreguntasActivity)
- Propósito: Formulario para crear y guardar una nueva pregunta asociada a un contacto.
- Inicialización:
  - Recibe `contactoId` como extra.
  - Si no hay ID, finaliza.
  - Inicializa `FirebaseFirestore`.
- Interfaz:
  - Campos de entrada: Enunciado, Respuesta Correcta, Respuesta Incorrecta.
  - Botón "Guardar Pregunta".
- Funcionalidad:
  - Al presionar guardar, valida campos no vacíos.
  - Crea objeto `Pregunta` con los datos.
  - Agrega el documento a la subcolección "preguntas" del contacto en Firestore.
  - Éxito: Muestra Toast de confirmación y finaliza la actividad (regresa a `PreguntasActivity`).
  - Error: Muestra Toast de error.
- Flujo: Crear Pregunta → Guardar en Firebase → Regresar a Lista de Preguntas.

 6. Fragmentos Secundarios
- ScoreFragment: Actualmente vacío, solo infla `fragment_score.xml`. Posiblemente para mostrar puntajes o estadísticas futuras.
- TeacherFragment: Actualmente vacío, solo infla `fragment_teacher.xml`. Posiblemente para funcionalidades de profesor/administrador.

## Navegación y Ciclo de Vida
- Navegación Inferior: Permite alternar entre Home (0), Score (1), Teacher (2) sin perder el estado de los fragments.
- Stack de Activities: 
  - MainActivity (siempre en fondo)
  - CrearContactoActivity → PreguntasActivity
  - Desde PreguntasActivity → CrearPreguntasActivity
- Sincronización: Todos los cambios en Firebase se reflejan automáticamente gracias a los SnapshotListeners.

## Adaptadores y Vistas
- ContactsAdapter: Gestiona la lista de contactos en `HomeFragment`. Maneja clics para navegar a preguntas.
- PreguntasAdapter: Gestiona la lista de preguntas en `PreguntasActivity`. Solo muestra información.
- ViewPagerAdapter: Gestiona los fragments en el ViewPager2 de `MainActivity`.

## Permisos y Configuración
- Internet: Requiere permiso para acceder a Firebase.
- Firebase: Configurado con `google-services.json`. Usa Firestore para almacenamiento.
