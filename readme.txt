RecyclerPrueba App - Flujo Completo de la Aplicacion

Descripcion General
RecyclerPrueba es una aplicacion Android educativa desarrollada en Java que permite gestionar contactos (posiblemente estudiantes o profesores) y asociar preguntas de evaluacion a cada uno de ellos. La aplicacion utiliza Firebase Firestore para el almacenamiento de datos en la nube, permitiendo sincronizacion en tiempo real. La interfaz principal se basa en un ViewPager2 con navegacion inferior para alternar entre tres secciones principales: Inicio (Home), Puntajes (Score) y Profesor (Teacher).

Arquitectura de la Aplicacion
- Lenguaje: Java
- Framework: Android SDK con ViewPager2 y RecyclerView
- Base de Datos: Firebase Firestore con persistencia offline habilitada
- Navegacion: BottomNavigationView con ViewPager2
- Patron de Diseno: MVP (Model-View-Presenter) implicito con Fragments y Activities

Modelos de Datos
Contacts
- Campos: name (String), phoneNo (String)
- Uso: Representa un contacto (estudiante/profesor) con nombre y numero telefonico

Pregunta
- Campos: enunciado (String), correcta (String), incorrecta (String)
- Uso: Representa una pregunta de evaluacion con su enunciado y dos opciones de respuesta

Estructura de Firebase Firestore
contacts2/
  {contactoId}/
    name: String
    phoneNo: String
    fechaCreacion: Timestamp
    preguntas/
      {preguntaId}/
        enunciado: String
        correcta: String
        incorrecta: String

Flujo Principal de la Aplicacion

1. Inicio de la Aplicacion
- La aplicacion se lanza desde MainActivity, que es la actividad principal declarada en el AndroidManifest.xml.
- En onCreate(), se configura el ViewPager2 con tres fragments: HomeFragment, ScoreFragment, y TeacherFragment.
- Se establece la navegacion inferior (BottomNavigationView) que permite alternar entre los fragments.
- Por defecto, se muestra el HomeFragment (posicion 0).

2. Fragmento de Inicio (HomeFragment)
- Proposito: Mostrar la lista de contactos almacenados en Firebase.
- Inicializacion:
  - Se obtiene una instancia de FirebaseFirestore.
  - Se inicializan listas para contactos y sus IDs.
  - Se configura un RecyclerView con LinearLayoutManager.
- Interfaz:
  - Muestra una lista de contactos usando ContactsAdapter.
  - Incluye un FloatingActionButton (FAB) para agregar nuevos contactos.
- Funcionalidad:
  - Al cargar la vista (onViewCreated), se configura el FAB para navegar a CrearContactoActivity.
  - Se configura el ContactsAdapter con un listener para clics en contactos.
  - Se llama a obtenerContactos() que establece un SnapshotListener en la coleccion "contacts2" de Firestore.
  - Los cambios en Firestore se reflejan automaticamente en la lista gracias al listener en tiempo real.
- Interaccion del Usuario:
  - Presionar FAB: Navega a CrearContactoActivity para crear un nuevo contacto.
  - Hacer clic en un contacto: Abre PreguntasActivity pasando el ID del contacto seleccionado como extra en el Intent.

3. Creacion de Contacto (CrearContactoActivity)
- Proposito: Formulario para crear y guardar un nuevo contacto en Firebase.
- Inicializacion:
  - Se obtiene instancia de FirebaseFirestore.
  - Se referencian los campos EditText para nombre y telefono, y el boton guardar.
- Interfaz:
  - Campos de entrada: Nombre y Telefono.
  - Boton "Guardar".
- Funcionalidad:
  - Al presionar el boton guardar, se valida que los campos no esten vacios.
  - Se crea un HashMap con los datos del contacto, incluyendo fechaCreacion con FieldValue.serverTimestamp().
  - Se agrega el documento a la coleccion "contacts2" de Firestore.
  - Exito: Muestra Toast de confirmacion, obtiene el ID del nuevo documento, y navega a PreguntasActivity pasando el ID como extra. Finaliza la actividad actual.
  - Error: Muestra Toast de error y registra el error en Log.
- Flujo: Crear Contacto -> Guardar en Firebase -> Navegar a Preguntas del Contacto.

4. Actividad de Preguntas (PreguntasActivity)
- Proposito: Mostrar y gestionar las preguntas asociadas a un contacto especifico.
- Inicializacion:
  - Recibe el contactoId como extra del Intent.
  - Si no hay ID valido, muestra error y finaliza.
  - Inicializa FirebaseFirestore y lista de preguntas.
- Interfaz:
  - RecyclerView para mostrar preguntas usando PreguntasAdapter.
  - Layout vacio (layoutVacio) cuando no hay preguntas.
  - Dos botones: "btnAdd" y "btnCrearPregunta" (ambos hacen lo mismo: abrir creacion de pregunta).
- Funcionalidad:
  - En onCreate(), llama a cargarPreguntas() que consulta la subcoleccion "preguntas" del documento del contacto.
  - Actualiza la vista: muestra lista si hay preguntas, layout vacio si no.
  - En onResume(), recarga las preguntas para reflejar cambios (ej. despues de crear una nueva).
  - Los botones de agregar navegan a CrearPreguntasActivity pasando el contactoId.
- Interaccion del Usuario:
  - Ver lista de preguntas del contacto.
  - Agregar nueva pregunta -> Navega a CrearPreguntasActivity.

5. Creacion de Pregunta (CrearPreguntasActivity)
- Proposito: Formulario para crear y guardar una nueva pregunta asociada a un contacto.
- Inicializacion:
  - Recibe contactoId como extra.
  - Si no hay ID, finaliza.
  - Inicializa FirebaseFirestore.
- Interfaz:
  - Campos de entrada: Enunciado, Respuesta Correcta, Respuesta Incorrecta.
  - Boton "Guardar Pregunta".
- Funcionalidad:
  - Al presionar guardar, valida campos no vacios.
  - Crea objeto Pregunta con los datos.
  - Agrega el documento a la subcoleccion "preguntas" del contacto en Firestore.
  - Exito: Muestra Toast de confirmacion y finaliza la actividad (regresa a PreguntasActivity).
  - Error: Muestra Toast de error.
- Flujo: Crear Pregunta -> Guardar en Firebase -> Regresar a Lista de Preguntas.

6. Fragmentos Secundarios
- ScoreFragment: Actualmente vacio, solo infla fragment_score.xml. Posiblemente para mostrar puntajes o estadisticas futuras.
- TeacherFragment: Actualmente vacio, solo infla fragment_teacher.xml. Posiblemente para funcionalidades de profesor/administrador.

7. Clase MyApplication
- Proposito: Configurar la aplicacion globalmente, especificamente habilitar la persistencia offline en Firebase Firestore.
- Funcionalidad:
  - Extiende Application para inicializar componentes al inicio de la app.
  - En onCreate(), obtiene la instancia de FirebaseFirestore y configura FirebaseFirestoreSettings con setPersistenceEnabled(true).
  - Esto permite que los datos se almacenen localmente en el dispositivo, permitiendo acceso offline y sincronizacion automatica cuando hay conexion a internet.

Navegacion y Ciclo de Vida
- Navegacion Inferior: Permite alternar entre Home (0), Score (1), Teacher (2) sin perder el estado de los fragments.
- Stack de Activities:
  - MainActivity (siempre en fondo)
  - CrearContactoActivity -> PreguntasActivity
  - Desde PreguntasActivity -> CrearPreguntasActivity
- Sincronizacion: Todos los cambios en Firebase se reflejan automaticamente gracias a los SnapshotListeners. Con persistencia offline, los datos locales se sincronizan cuando hay conexion.

Adaptadores y Vistas
- ContactsAdapter: Gestiona la lista de contactos en HomeFragment. Maneja clics para navegar a preguntas.
- PreguntasAdapter: Gestiona la lista de preguntas en PreguntasActivity. Solo muestra informacion.
- ViewPagerAdapter: Gestiona los fragments en el ViewPager2 de MainActivity.

Permisos y Configuracion
- Internet: Requiere permiso para acceder a Firebase.
- Firebase: Configurado con google-services.json. Usa Firestore para almacenamiento con persistencia offline habilitada.
