---**Proyecto desarrollado por el equipo: 404: Brain Not Found**---

## En la carpeta Catalogo carreras UASD1 encontrar la :

## Aplicación de escritorio en Java (Swing) conectada a una base de datos SQLite que funciona como un Catálogo Académico de las carreras de la UASD.

# --**Lenguaje, Arquitectura y Tecnologías utilizadas**-- 

• Interfaz Gráfica (GUI): Construida con javax.swing (JFrame, JDialog, JTable, JComboBox, CardLayout, etc.) 
con una paleta de colores institucionales (azul marino y tonos neutros).

• Base de Datos: Que utiliza SQLite (jdbc:sqlite:uasd_catalogo.db) mediante JDBC para almacenar y consultar la información de forma local.

 # --**Estructura de la Base de Datos**--

Al iniciarse, el método inicializarBaseDatosSQLite() crea automáticamente cuatro tablas si no existen:
1. usuarios: Almacena credenciales de acceso (usuario, clave).

2. facultades: Guarda el id, nombre y la ruta de la imagen del logo (del 1 al 9).

3. escuelas: Relacionada con facultades mediante facultad_id.

4. carreras: Contiene codigo, nombre, creditos y su relación con escuelas (escuela_id).

## --**Backend (Lógica del Servidor, Datos y Escritorio)**--
Es la capa encargada de procesar los datos, validar reglas y comunicarse con el almacenamiento. Está desarrollada en Java y SQL.

• Motor de Base de Datos: SQLite (archivo local uasd_catalogo.db) conectado mediante el controlador JDBC (org.sqlite.JDBC).

• Gestión de Conexiones y Consultas: Uso de DriverManager, Connection, Statement, PreparedStatement (para evitar inyección SQL) y ResultSet.

• Inicialización y Esquema (inicializarBaseDatosSQLite): Creación automática de las tablas (usuarios, facultades, escuelas, carreras), migración de columnas (ALTER TABLE) e inserción de usuarios por defecto.

• Lógica de Autenticación (validarUsuario): Consulta a la base de datos para verificar si las credenciales ingresadas existen.

• Construcción Dinámica de Consultas (cargarTabla): Ensamblaje de sentencias SELECT con JOIN entre tres tablas, aplicando filtros dinámicos (WHERE, LIKE) y ordenamiento (ORDER BY) según lo que el usuario elija en el frontend.

## --**Frontend (Interfaz Gráfica e Interactividad con el Usuario)**--
Es toda la capa visual con la que interactúa el usuario o usuaria. Y está desarrollada en Java Swing y AWT.

• Ventanas y Contenedores: JFrame (ventana principal), JDialog (ventana de inicio de sesión), JPanel y CardLayout (para alternar entre el banner general y el de cada facultad).

• Componentes de Entrada y Control: JTextField (barra de búsqueda), JPasswordField (clave), JComboBox (listas desplegables de facultades, escuelas y ordenamiento), JCheckBox (orden ascendente) y JButton.

• Visualización de Datos: JTable y DefaultTableModel para mostrar el listado de carreras en filas y columnas, junto con JScrollPane y JLabel para textos e imágenes.

• Diseño y Estilos: Paleta de colores personalizada (Color), bordes (LineBorder, EmptyBorder, TitledBorder), fuentes (Font) y un DefaultTableCellRenderer para alternar el color de las filas.

• Eventos: ActionListener para capturar clics en botones, menús y cambios en los selectores.

## --Estructura del Proyecto y Función de Cada Archivo--


1. CatalogoCarrerasUASD1.java (Código Fuente)

Es el núcleo del sistema. Contiene tanto la interfaz gráfica (Swing) como la lógica y acceso a datos (JDBC). Se encarga de arrancar la aplicación, dibujar las ventanas, validar el inicio de sesión y ejecutar las consultas SQL.

2. uasd_catalogo.db (Base de Datos)

Es el archivo de base de datos SQLite donde se guarda toda la información de forma persistente.

• Si el archivo no existe, el programa lo crea automáticamente en la raíz del proyecto al ejecutarse por primera vez.
• Almacena las 4 tablas del sistema: usuarios, facultades, escuelas y carreras.

3. sqlite-jdbc-x.x.x.jar (Librería / Dependencia)

Es el conector oficial (Driver JDBC) que permite que Java entienda y se comunique con el archivo uasd_catalogo.db. Sin este archivo agregado al Classpath del proyecto, el programa lanzará un error (ClassNotFoundException) al intentar abrir la base de datos.

4. escudo_uasd.png (Recurso Gráfico)

Imagen del escudo de la universidad. El programa la carga en tres lugares:
• En la ventana de inicio de sesión (Login).
• En la esquina superior izquierda del encabezado principal.
• A ambos lados del banner cuando se selecciona la vista "TODAS LAS CARRERAS".

5. facultad_1.png hasta facultad_9.png (Recursos Gráficos)

Son los logotipos individuales de cada una de las 9 facultades de la UASD. Se muestran dinámicamente en la tarjeta de identidad superior cuando el usuario filtra las carreras por una facultad en específico.


# --**Flujo y Funcionalidades Principales**--

• Inicio de Sesión (mostrarLogin): Antes de abrir el catálogo, muestra una ventana modal (JDialog) que valida el usuario y la contraseña contra la base de datos.    Si se cancela, el programa se cierra.

• Encabezado y Barra de Menú:

• Muestra el escudo de la UASD, el título y el usuario activo.
	
• Incluye un menú superior con accesos rápidos para navegar por el catálogo, aplicar/limpiar filtros, cambiar la altura de las filas (Vista compacta) y cerrar       sesión.

• Panel de Filtros y Búsqueda:
  
  • Búsqueda por texto: Filtra coincidencias en tiempo real (al presionar Enter o Aplicar filtros) por código, carrera, escuela o facultad.
	
  • Filtros en cascada: Al seleccionar una Facultad, el selector de Escuelas se actualiza automáticamente para mostrar solo las escuelas pertenecientes a dicha        facultad.
	
• Ordenamiento: Permite ordenar los resultados por Facultad, Escuela, Carrera, Código o Créditos de forma ascendente o descendente.

• Tarjeta de Identidad Dinámica (CardLayout): Cambia visualmente según el filtro activo; muestra el banner general de "TODAS LAS CARRERAS" o el logotipo y nombre    específico de la facultad seleccionada.

• Tabla de Resultados (JTable): Despliega las carreras en 5 columnas no editables (Código, Carrera, Créditos, Escuela, Facultad) con filas alternadas en color       para facilitar la lectura.

• Barra de Estado: Indica cuántas carreras se encontraron bajo el filtro actual e incluye un botón para cerrar sesión y volver al login.


## --Capturas de Pantalla--

<img width="416" height="273" alt="Muetras inicio de sesion" src="https://github.com/user-attachments/assets/94cd29d3-cd62-402c-b07f-a3da937a1985" />


<img width="1920" height="1080" alt="Muestra tras el inicio de sesion" src="https://github.com/user-attachments/assets/cedc9446-331b-49cb-84cd-df6e03a90709" />


<img width="1920" height="1080" alt="Muestra de busqueda y flitros aplicados" src="https://github.com/user-attachments/assets/d6aab83e-9d3c-4627-99f1-9d67168470ec" />






▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼

## En la carpeta Web catalogo encontrar la :
## Aplicación Web (híbrida con pasarela de escritorio) diseñada para consultar de manera dinámica las 9 facultades, 50 escuelas, 61 carreras y sus respectivos pensum académicos.

# --**Lenguajes y Tecnologías Utilizadas**--

->**Lenguaje principal del Backend** : Java (JDK / Java EE - Servlets) encargado de la lógica de negocio, conexión
a base de datos, endpoints de API y la pasarela de auto despliegue.

->**SQL (SQLite)**: Motor de base de datos embebido (uasd_catalogo_final.db)
que almacena de forma relacional las facultades, escuelas, carreras y asignaturas.

->**JSP (JavaServer Pages)**: Motor de plantillas utilizado para renderizar la vista principal desde el servidor.

->**HTML5 y CSS3**: Estructuración semántica y diseño visual responsivo con temática institucional
(Azul Marino #002D62 y Dorado #D4AF37), animaciones y efectos de transparencia.

->**JavaScript (ES6 / Fetch API)**: Consumo asíncrono (AJAX) de la API interna para
cargar escuelas, carreras y ventanas modales de pensum sin recargar la página.

->**Java Swing & AWT**: Utilizados para crear la ventana de inicio rápido en escritorio y abrir automáticamente el navegador web.

->**Apache Tomcat 9.0**: Servidor de aplicaciones web encargado de ejecutar los Servlets y servir el archivo .war .


## --**Backend (Lógica del Servidor, Datos y Escritorio)**--

Conocido como el cerebro de la aplicación. Se encarga de almacenar la información académica, procesar las peticiones y lanzar el sistema.

->Que lo compone:

->**ConexionDB.java (Base de Datos)**: Crea el archivo uasd_catalogo_final.db en SQLite y ejecuta las sentencias INSERT
para poblar las 9 facultades, 50 escuelas, 61 carreras y todos los pensum automáticamente.


->**Item.java (Modelo)**: Define la estructura básica (id y nombre) para transportar los registros de la base de datos hacia los controladores.

->**MainServlet.java (Controlador Inicial - index)**: Consulta las facultades en SQLite
cuando el usuario entra a la página y envía esa lista inicial al Frontend.

->**ApiServlet.java (API REST - Api)**: Recibe peticiones en segundo plano y responde con datos puros en formato
JSON (escuelas según facultad, carreras según escuela y asignaturas según carrera).

->**PrincipalEscritorio.java (Pasarela / Launcher)**: Automatiza el despliegue copiando el archivo .war en Tomcat, levanta la base de datos y abre el navegador web del usuario.


## --**Frontend (Interfaz Gráfica e Interactividad con el Usuario)**--

Este es la cara visible de sistema que interactúa con el usuario o usuaria desde su navegador de forma dinamica.

->Que lo compone:

->Lenguajes y Tecnologías: HTML5, CSS3, JavaScript (ES6 / Fetch API) y JSP (JavaServer Pages) aun que ya lo mencionamos arriba.

->index.jsp (Vista Principal): Este es el único archivo de interfaz, pero integra cuatro capas visuales y funcionales:

1. Estructura (HTML5 + JSP): Renderiza la barra superior institucional, el contenedor de 3 columnas (Facultades, Escuelas y Carreras) y la ventana emergente (Modal) para los pensums.
   Utiliza código JSP para pintar los botones de las 9 facultades apenas carga la página.

3. Diseño Visual (CSS3): Aplica los colores oficiales de la UASD:
   (Azul Marino #002D62 y Dorado #D4AF37), efectos de desenfoque (glassmorphism), animaciones de entrada y la pantalla de bienvenida (Splash Screen).

5. Lógica Dinámica (JavaScript / AJAX): Mediante fetch(), se comunica con ApiServlet sin recargar la página.
   Se encarga de limpiar y rellenar los paneles dinámicamente al hacer clic, además de agrupar y dibujar los bloques de semestres y créditos dentro del modal.



## --Estructura del Proyecto y Función de Cada Archivo--


1. **Paquete com.uasd.config (Configuración e Inicio)**

• ConexionDB.java:
	
  • Establece la conexión con la base de datos local SQLite (uasd_catalogo_final.db) usando el driver org.sqlite.JDBC.
	
  • Verifica si las tablas (facultad, escuela, carrera, pensum) existen; si es la primera ejecución, las crea e inserta automáticamente todo el catálogo académico de la universidad.
• PrincipalEscritorio.java:
	
  • Funciona como una pasarela de escritorio (Launcher).
	
  • Inicializa la base de datos en un hilo secundario.
	
  • Realiza un autodespliegue copiando el archivo WebCatalogo.war hacia la carpeta webapps de Apache Tomcat 9.0.
	
  • Espera 4 segundos a que el servidor levante el proyecto, abre automáticamente la URL 
  http://localhost:8080/WebCatalogo/index en el navegador predeterminado y cierra la ventana puente.

2. **Paquete com.uasd.model (Modelo de Datos)**

• Item.java:
	
  • Clase POJO (Plain Old Java Object) que representa una entidad genérica con id y nombre. 
  Se utiliza para transportar de forma limpia y estandarizada los datos de facultades, escuelas y carreras entre la base de datos, los controladores y la vista.

3. **Paquete com.uasd.controller (Controladores y API)**

• MainServlet.java (Ruta: /index):
	
  • Es el controlador principal de entrada a la aplicación web.
	
  • Consulta las 9 facultades en la base de datos, las guarda como atributo en la petición (request) y redirige la ejecución hacia index.jsp.

• ApiServlet.java (Ruta: /api):
	
  • Actúa como una API REST interna que responde exclusivamente en formato JSON (application/json;charset=UTF-8).
	
  • Recibe dos parámetros por URL (tipo e id) y devuelve dinámicamente:
		• ?tipo=escuelas&id={facultad_id}: Lista de escuelas pertenecientes a una facultad.
		• ?tipo=carreras&id={escuela_id}: Lista de carreras pertenecientes a una escuela.
		• ?tipo=pensum&id={carrera_id}: Listado de asignaturas (código, nombre, créditos y semestre) ordenadas por semestre.
	
  • Construye las cadenas JSON manualmente para evitar fallos de serialización.

4. **Capa de Vista (Web Pages)**

• index.jsp:
	
  • Interfaz de Usuario: Presenta una pantalla de bienvenida (Splash Screen de 5 segundos), un fondo animado y un diseño de 3 paneles interactivos
  (Facultades \(\rightarrow \) Escuelas \(\rightarrow \) Carreras).
	
  • Renderizado Híbrido: Carga las facultades iniciales mediante código Java incrustado (Scriptlets) enviado desde MainServlet.
	
  • Interactividad AJAX: Contiene las funciones JavaScript (cargarEscuelas, cargarCarreras y abrirPensum) que consultan a ApiServlet mediante fetch().
	
  • Modal de Pensum: Despliega una ventana emergente que agrupa y muestra las materias por semestre junto con su clave y cantidad de créditos.

## --**Flujo de Funcionamiento**--

1. El usuario ejecuta PrincipalEscritorio.java (o despliega WebCatalogo.war directamente en Tomcat).

3. ConexionDB genera el archivo uasd_catalogo_final.db y lo compelta con los datos académicos si aún no existe.

4. El navegador abre /index, ejecutando MainServlet, el cual carga las facultades en index.jsp.

5. Al hacer clic en una Facultad, JavaScript consulta /api?tipo=escuelas y llena el segundo panel.

6. Al hacer clic en una Escuela, JavaScript consulta /api?tipo=carreras y llena el tercer panel.

7. Al hacer clic en una Carrera, JavaScript consulta /api?tipo=pensum

 
## --Capturas de Pantalla--

<img width="1919" height="1034" alt="Pantalla de carga antes de iniciar" src="https://github.com/user-attachments/assets/f06318d5-7d41-450c-99e6-183a90cba121" />


<img width="1920" height="1035" alt="Pantalla tras el inicio pagina principal" src="https://github.com/user-attachments/assets/c8bcafa0-8f5f-4148-8142-8ab98a57d23d" />


<img width="1920" height="1036" alt="Pantalla con facultad selcionada y escuela selecionada" src="https://github.com/user-attachments/assets/104155d8-3fc9-45bb-ade5-e9c9d3e54f37" />


<img width="1920" height="1037" alt="Pantalla mostarando pensum" src="https://github.com/user-attachments/assets/28c20f93-5124-4ceb-92f2-a4ecd0082d49" />



**Proyecto desarrollado por el equipo: 404: Brain Not Found**

