---**Proyecto desarrollado por el equipo: 404: Brain Not Found**---

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

