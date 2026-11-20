package com.uasd.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ConexionDB {
    private static final String RUTA_PROYECTO = System.getProperty("user.dir");
    // Cambiamos el nombre para forzar la creacion de un archivo nuevo y limpio
    private static final String URL = "jdbc:sqlite:" + RUTA_PROYECTO + "/uasd_catalogo_final.db";
   
    private static java.sql.Connection connection = null;

       private static boolean inicializada = false;

    public static Connection getConnection() {
        try {
            Class.forName("org.sqlite.JDBC");
            
            java.util.Properties props = new java.util.Properties();
            props.setProperty("PRAGMA encoding", "\"UTF-8\"");
            
            if (!inicializada) {
                inicializarTablas();
                inicializada = true;
            }
            
            return DriverManager.getConnection(URL, props);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    private static void inicializarTablas() {
        String tFacultad = "CREATE TABLE IF NOT EXISTS facultad (id INTEGER PRIMARY KEY AUTOINCREMENT, nombre TEXT);";
        String tEscuela = "CREATE TABLE IF NOT EXISTS escuela (id INTEGER PRIMARY KEY AUTOINCREMENT, nombre TEXT, facultad_id INTEGER, FOREIGN KEY(facultad_id) REFERENCES facultad(id));";
        String tCarrera = "CREATE TABLE IF NOT EXISTS carrera (id INTEGER PRIMARY KEY AUTOINCREMENT, nombre TEXT, escuela_id INTEGER, FOREIGN KEY(escuela_id) REFERENCES escuela(id));";
        String tPensum = "CREATE TABLE IF NOT EXISTS pensum (id INTEGER PRIMARY KEY AUTOINCREMENT, codigo TEXT, nombre TEXT, creditos INTEGER, semestre INTEGER, carrera_id INTEGER, FOREIGN KEY(carrera_id) REFERENCES carrera(id));";
        
        try (Connection conn = DriverManager.getConnection(URL);
             Statement stmt = conn.createStatement()) {
            
            stmt.execute(tFacultad);
            stmt.execute(tEscuela);
            stmt.execute(tCarrera);
            stmt.execute(tPensum);
            
            ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM facultad");
            if (rs.next() && rs.getInt(1) == 0) {
                
                // 1. INSERCION DE LAS 9 FACULTADES (SIN ACENTOS)
                stmt.execute("INSERT INTO facultad (id, nombre) VALUES " +
                        "(1, 'Facultad de Artes'), " +
                        "(2, 'Facultad de Ciencias'), " +
                        "(3, 'Facultad de Ciencias Economicas y Sociales'), " +
                        "(4, 'Facultad de Ciencias Juridicas y Politicas'), " +
                        "(5, 'Facultad de Ciencias de la Educacion'), " +
                        "(6, 'Facultad de Ciencias de la Salud'), " +
                        "(7, 'Facultad de Ciencias Agronomicas y Veterinarias'), " +
                        "(8, 'Facultad de Humanidades'), " +
                        "(9, 'Facultad de Ingenieria y Arquitectura')");
                
                insertarEscuelas(stmt);
                insertarCarrerasParte1(stmt);
                insertarCarrerasParte2(stmt);
                insertarPensum(stmt);
                System.out.println(">>> BASE DE DATOS CREADA EXITOSAMENTE EN FORMATO ASCII LIMPIO.");

            
            
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void insertarEscuelas(Statement stmt) throws SQLException {
        // Artes (1) y Ciencias (2)
        stmt.execute("INSERT INTO escuela (id, nombre, facultad_id) VALUES " +
                "(1, 'Escuela de Artes Plasticas', 1), (2, 'Escuela de Cine, TV y Fotografia', 1), " +
                "(3, 'Escuela de Critica e Historia del Arte', 1), (4, 'Escuela de Diseno Industrial y Modas', 1), " +
                "(5, 'Escuela de Musica', 1), (6, 'Escuela de Publicidad', 1), (7, 'Escuela de Teatro', 1), " +
                "(8, 'Escuela de Biologia', 2), (9, 'Escuela de Ciencias Geograficas', 2), " +
                "(10, 'Escuela de Fisica', 2), (11, 'Escuela de Informatica', 2), " +
                "(12, 'Escuela de Matematica', 2), (13, 'Escuela de Microbiologia y Parasitologia', 2), " +
                "(14, 'Escuela de Quimica', 2)");

        // Economicas (3) y Juridicas (4)
        stmt.execute("INSERT INTO escuela (id, nombre, facultad_id) VALUES " +
                "(15, 'Escuela de Administracion', 3), (16, 'Escuela de Contabilidad', 3), " +
                "(17, 'Escuela de Economia', 3), (18, 'Escuela de Estadistica', 3), " +
                "(19, 'Escuela de Mercadotecnia', 3), (20, 'Escuela de Sociologia', 3), " +
                "(21, 'Escuela de Turismo y Hoteleria', 3), " +
                "(22, 'Escuela de Ciencias Politicas', 4), (23, 'Escuela de Derecho', 4)");

        // Educacion (5) y Salud (6)
        stmt.execute("INSERT INTO escuela (id, nombre, facultad_id) VALUES " +
                "(24, 'Escuela de Bibliotecologia, Tecnologia e Innovacion', 5), (25, 'Escuela de Educacion Fisica y Ciencias del Deporte', 5), " +
                "(26, 'Escuela de Educacion Infantil y Basica', 5), (27, 'Escuela de Educacion Media', 5), " +
                "(28, 'Escuela de Orientacion Educativa y Psicopedagogia', 5), (29, 'Escuela de Teoria y Gestion Educativa', 5), " +
                "(30, 'Escuela de Bioanalisis', 6), (31, 'Escuela de Enfermeria', 6), " +
                "(32, 'Escuela de Farmacia', 6), (33, 'Escuela de Medicina', 6), " +
                "(34, 'Escuela de Odontologia', 6), (35, 'Escuela de Salud Publica', 6)");

        // Agronomicas (7), Humanidades (8) e Ingenieria (9)
        stmt.execute("INSERT INTO escuela (id, nombre, facultad_id) VALUES " +
                "(36, 'Escuela de Agronomia', 7), (37, 'Escuela de Veterinaria', 7), (38, 'Escuela de Zootecnia', 7), " +
                "(39, 'Escuela de Comunicacion Social', 8), (40, 'Escuela de Filosofia', 8), " +
                "(41, 'Escuela de Historia y Antropologia', 8), (42, 'Escuela de Idiomas', 8), " +
                "(43, 'Escuela de Letras', 8), (44, 'Escuela de Psicologia', 8), " +
                "(45, 'Escuela de Agrimensura', 9), (46, 'Escuela de Arquitectura', 9), " +
                "(47, 'Escuela de Electromecanica', 9), (48, 'Escuela de Ingenieria Civil', 9), " +
                "(49, 'Escuela de Ingenieria Industrial', 9), (50, 'Escuela de Ingenieria Quimica', 9)");
    }

    private static void insertarCarrerasParte1(Statement stmt) throws SQLException {
        // Artes (Escuelas 1 a 7)
        stmt.execute("INSERT INTO carrera (nombre, escuela_id) VALUES " +
                "('Licenciatura en Artes Plasticas', 1), ('Licenciatura en Artes Plasticas, mencion Escultura', 1), " +
                "('Licenciatura en Artes Plasticas, mencion Pintura', 1), ('Licenciatura en Cinematografia, mencion Cine', 2), " +
                "('Licenciatura en Cinematografia, mencion Television', 2), ('Tecnico en Fotografia', 2), " +
                "('Licenciatura en Historia y Critica del Arte', 3), ('Licenciatura en Artes Industriales, mencion Diseno Industrial', 4), " +
                "('Licenciatura en Artes Industriales, mencion Diseno de Modas', 4), ('Licenciatura en Musica', 5), " +
                "('Licenciatura en Publicidad, mencion Diseno', 6), ('Licenciatura en Teatro', 7)");

        // Ciencias (Escuelas 8 a 14)
        stmt.execute("INSERT INTO carrera (nombre, escuela_id) VALUES " +
                "('Licenciatura en Biologia', 8), ('Licenciatura en Geografia', 9), " +
                "('Licenciatura en Fisica', 10), ('Licenciatura en Informatica', 11), " +
                "('Licenciatura en Matemáticas', 12), ('Licenciatura en Microbiologia', 13), " +
                "('Licenciatura en Quimica', 14), ('Tecnologo Superior en Alimentos', 14)");

        // Economicas (15-21) y Politicas/Derecho (22-23)
        stmt.execute("INSERT INTO carrera (nombre, escuela_id) VALUES " +
                "('Licenciatura en Administracion de Empresas', 15), ('Licenciatura en Contabilidad', 16), " +
                "('Licenciatura en Economia', 17), ('Licenciatura en Estadistica, mencion Informatica', 18), " +
                "('Licenciatura en Mercadotecnia', 19), ('Licenciatura en Sociologia', 20), " +
                "('Licenciatura en Turismo y Hoteleria', 21), ('Licenciatura en Ciencias Politicas', 22), " +
                "('Licenciatura en Derecho', 23)");
    }

    
    private static void insertarCarrerasParte2(Statement stmt) throws SQLException {
        // Educacion (Escuelas 24 a 29)
        stmt.execute("INSERT INTO carrera (nombre, escuela_id) VALUES " +
                "('Licenciatura en Ciencias de la Informacion', 24), ('Licenciatura en Educacion Fisica', 25), " +
                "('Licenciatura en Educacion Basica', 26), ('Licenciatura en Educacion Infantil', 26), " +
                "('Licenciatura en Educacion, mencion Filosofia y Letras', 27), ('Licenciatura en Educacion Especial', 28)");

        // Salud (Escuelas 30 a 35)
        stmt.execute("INSERT INTO carrera (nombre, escuela_id) VALUES " +
                "('Licenciatura en Bioanalisis', 30), ('Licenciatura en Imagenologia', 30), " +
                "('Licenciatura en Enfermeria', 31), ('Licenciatura en Farmacia', 32), " +
                "('Doctor en Medicina', 33), ('Doctor en Odontologia', 34), " +
                "('Tecnico Superior en Atencion Prehospitalaria', 35)");

        // Agronomicas (36-38) y Humanidades (39-44)
        stmt.execute("INSERT INTO carrera (nombre, escuela_id) VALUES " +
                "('Ingenieria Agronomica', 36), ('Licenciatura en Medicina Veterinaria', 37), " +
                "('Ingenieria en Zootecnia', 38), ('Licenciatura en Comunicacion Social, mencion Periodismo', 39), " +
                "('Licenciatura en Filosofia', 40), ('Licenciatura en Lenguas Modernas, mencion Ingles', 42), " +
                "('Licenciatura en Letras', 43), ('Licenciatura en Psicologia Clinica', 44), " +
                "('Licenciatura en Psicologia Industrial', 44)");

        // Ingenieria y Arquitectura (Escuelas 45 a 50)
        stmt.execute("INSERT INTO carrera (nombre, escuela_id) VALUES " +
                "('Licenciatura en Agrimensura', 45), ('Arquitectura', 46), " +
                "('Ingenieria Electromecanica, mencion Electrica', 47), ('Ingenieria Electromecanica, mencion Electronica', 47), " +
                "('Ingenieria Civil', 48), ('Ingenieria Industrial', 49), " +
                "('Ingenieria Quimica', 50)");
        
        // Correcion de carreras que no se mostraban 
                // Carrera de la Escuela 29 (id = 59)
        stmt.execute("INSERT INTO carrera (nombre, escuela_id) VALUES " +
                "('Licenciatura en Educacion, mencion Gestion Educativa', 29)");
        
                // Carreras de la Escuela 41 (id = 60 y 61)
        stmt.execute("INSERT INTO carrera (nombre, escuela_id) VALUES " +
                "('Licenciatura en Historia', 41), " +
                "('Licenciatura en Antropologia', 41)");


    }
    
        private static void insertarPensum(Statement stmt) throws SQLException {
        // Inico de los pensum.
        // Iniciand o por la facultadad de Artes
       
        // Pensum de Licenciatura en Artes Plasticas (carrera_id = 1)
        stmt.execute("INSERT INTO pensum (codigo, nombre, creditos, semestre, carrera_id) VALUES " +
                "('ART-1100', 'Taller De Educacion Estetica', 4, 1, 1), " +
                "('ART-1130', 'Historia Del Arte I', 3, 1, 1), " +
                "('ART-1270', 'Dibujo I', 4, 1, 1), " +
                "('DOP-1000', 'Orientacion Institucional', 1, 1, 1), " +
                "('HIS-0110', 'Fund de Hist Social Dominicana', 3, 1, 1), " +
                "('LET-0110', 'Lengua Espanola Basica I', 3, 1, 1), " +
                "('SOC-0100', 'Introd A Las Ciencias Sociales', 3, 1, 1), " +
                // Semestre 2
                "('ART-1170', 'His Del Arte Dominicano I', 3, 2, 1), " +
                "('ART-1290', 'Dibujo II', 4, 2, 1), " +
                "('ART-3210', 'Pintura I', 3, 2, 1), " +
                "('ART-3270', 'Escultura I', 3, 2, 1), " +
                "('FIL-0110', 'Int a la Filosofia', 3, 2, 1), " +
                "('LET-0120', 'Lengua Espanola Basica II', 3, 2, 1), " +
                // Semestre 3
                "('ART-2110', 'Estetica I', 3, 3, 1), " +
                "('ART-2190', 'Teoria De Las Artes Visuales', 3, 3, 1), " +
                "('ART-3010', 'Taller De Comp Artistica', 4, 3, 1), " +
                "('ART-3220', 'Pintura II', 3, 3, 1), " +
                "('ART-3250', 'Dibujo III', 3, 3, 1), " +
                "('ART-4280', 'Escultura II', 4, 3, 1), " +
                // Semestre 4
                "('ART-3100', 'Arte Contemporaneo', 3, 4, 1), " +
                "('ART-3260', 'Int A La Pintura Al Oleo', 3, 4, 1), " +
                "('ART-4210', 'Tecnicas Pictoricas', 3, 4, 1), " +
                "('ART-4250', 'Pintura III', 4, 4, 1), " +
                "('ART-4260', 'Dibujo IV', 3, 4, 1), " +
                "('FIL-1240', 'Metodologia De La Inv Cientif', 3, 4, 1), " +
                // Semestre 5
                "('ART-2140', 'Teoria Y Critica Del Arte', 3, 5, 1), " +
                "('ART-3130', 'Historia Del Arte Latinoamer', 3, 5, 1), " +
                "('ART-4100', 'Semiologia Del Arte', 3, 5, 1), " +
                "('ART-4220', 'Pintura De Paisaje', 4, 5, 1), " +
                "('ART-4230', 'Grabado I', 3, 5, 1), " +
                "('ART-4270', 'Dibujo Del Natural I', 4, 5, 1), " +
                "('ART-5210', 'Taller Creativo I', 4, 5, 1), " +
                // Semestre 6
                "('ART-3170', 'Museos', 3, 6, 1), " +
                "('ART-4240', 'Grabado II', 3, 6, 1), " +
                "('ART-4290', 'Dibujo Del Natural II', 4, 6, 1), " +
                "('ART-5220', 'Taller Creativo II', 4, 6, 1), " +
                "('ART-5230', 'Pintura Mural I', 3, 6, 1), " +
                "('ART-5260', 'Pintura IV', 4, 6, 1), " +
                // Semestre 7
                "('ART-3110', 'Elaboracion De Proy De Artes', 3, 7, 1), " +
                "('ART-4110', 'Met Y Tec De La Ed Artistica', 3, 7, 1), " +
                "('ART-5240', 'Pintura Mural II', 4, 7, 1), " +
                // Tesis de Grado (Semestre 8)
                "('ART-7420', 'Tesis De Grado O Curso Equival', 8, 8, 1)");
        
        // Pensum de Licenciatura en Artes Plasticas, mencion Escultura (carrera_id = 2)
        stmt.execute("INSERT INTO pensum (codigo, nombre, creditos, semestre, carrera_id) VALUES " +
                "('CRI-1100', 'Taller de Educacion Estetica', 4, 1, 2), " +
                "('CRI-1130', 'Historia del Arte I', 3, 1, 2), " +
                "('DOP-1000', 'Orientacion Institucional', 1, 1, 2), " +
                "('HIS-0110', 'Fund de Hist Social Dominicana', 3, 1, 2), " +
                "('LET-0110', 'Lengua Espanola Basica I', 3, 1, 2), " +
                "('PLA-1270', 'Dibujo I', 4, 1, 2), " +
                "('PLA-2200', 'Modelado I', 3, 1, 2), " +
                "('PUB-1300', 'Diseno Basico Bidimensional', 3, 1, 2), " +
                // Semestre 2
                "('CRI-1140', 'Historia del Arte II', 3, 2, 2), " +
                "('FIL-0110', 'Int a la Filosofia', 3, 2, 2), " +
                "('LET-0120', 'Lengua Espanola Basica II', 3, 2, 2), " +
                "('PLA-1290', 'Dibujo II', 4, 2, 2), " +
                "('PLA-2280', 'Teoria Y Practica Del Color', 3, 2, 2), " +
                "('PLA-3200', 'Modelado II', 3, 2, 2), " +
                "('PUB-2300', 'Diseno Basico Tridimensional', 3, 2, 2), " +
                "('SOC-0110', 'Int A Las Ciencias Sociales', 2, 2, 2), " +
                // Semestre 3
                "('ARQ-3290', 'Perspectiva Artistica', 3, 3, 2), " +
                "('CRI-1170', 'Historia del Arte Dominicano I', 3, 3, 2), " +
                "('CRI-2110', 'Estetica I', 3, 3, 2), " +
                "('PLA-3210', 'Pintura I', 3, 3, 2), " +
                "('PLA-3250', 'Dibujo III', 3, 3, 2), " +
                "('PLA-3260', 'Int A La Pintura Al Oleo', 3, 3, 2), " +
                "('PLA-3270', 'Escultura I', 3, 3, 2), " +
                "('PLA-3280', 'Anatomia Artistica', 3, 3, 2), " +
                // Semestre 4
                "('CRI-2190', 'Teoria De Las Artes Visuales', 3, 4, 2), " +
                "('FIL-1240', 'Metodologia De La Inv Cientif', 3, 4, 2), " +
                "('PLA-3010', 'Taller De Comp Artistica', 4, 4, 2), " +
                "('PLA-3220', 'Pintura II', 3, 4, 2), " +
                "('PLA-4220', 'Pintura De Paisaje', 4, 4, 2), " +
                "('PLA-4260', 'Dibujo IV', 3, 4, 2), " +
                "('PLA-4280', 'Escultura II', 4, 4, 2), " +
                // Semestre 5
                "('CRI-3100', 'Arte Contemporaneo', 3, 5, 2), " +
                "('DIS-1700', 'Tecnologia De Los Materiales', 3, 5, 2), " +
                "('PLA-2700', 'Tecnicas De Moldes Y Vaciados', 4, 5, 2), " +
                "('PLA-4270', 'Dibujo Del Natural I', 4, 5, 2), " +
                "('PLA-5270', 'Ceramica Artistica I', 3, 5, 2), " +
                "('PLA-5280', 'Escultura III', 3, 5, 2), " +
                "('PLA-5290', 'Taller De Escultura I', 4, 5, 2), " +
                // Semestre 6
                "('CRI-3130', 'Historia Del Arte Latinoamer', 3, 6, 2), " +
                "('CRI-4100', 'Semiologia Del Arte', 3, 6, 2), " +
                "('DIS-2710', 'Tecnica De Fundicion Y Vaciado', 4, 6, 2), " +
                "('PLA-4290', 'Dibujo Del Natural II', 4, 6, 2), " +
                "('PLA-6220', 'Ceramica Artistica II', 3, 6, 2), " +
                "('PLA-6230', 'Escultura IV', 4, 6, 2), " +
                "('PLA-6240', 'Taller De Escultura II', 4, 6, 2), " +
                // Semestre 7
                "('CRI-2100', 'Arte Y Sociedad', 3, 7, 2), " +
                "('CRI-2140', 'Teoria y Critica del Arte', 3, 7, 2), " +
                "('CRI-3110', 'Elaboracion De Proy De Artes', 3, 7, 2), " +
                "('CRI-3170', 'Museos', 3, 7, 2), " +
                "('CRI-4110', 'Met Y Tec De La Ed Artistica', 3, 7, 2), " +
                "('DIS-3730', 'Taller De Fundicion Y Vaciado', 5, 7, 2), " +
                "('PLA-6250', 'Escultura Monumental', 4, 7, 2), " +
                // Tesis de Grado (Semestre 8)
                "('PLA-7230', 'Tesis de Grado o Curso Equival', 8, 8, 2)");
        
        // Pensum de Licenciatura en Artes Plasticas, mencion Pintura (carrera_id = 3)
        stmt.execute("INSERT INTO pensum (codigo, nombre, creditos, semestre, carrera_id) VALUES " +
                "('CRI-1100', 'Taller de Educacion Estetica', 4, 1, 3), " +
                "('CRI-1130', 'Historia del Arte I', 3, 1, 3), " +
                "('DOP-1000', 'Orientacion Institucional', 1, 1, 3), " +
                "('HIS-0110', 'Fund de Hist Social Dominicana', 3, 1, 3), " +
                "('LET-0110', 'Lengua Espanola Basica I', 3, 1, 3), " +
                "('PLA-1270', 'Dibujo I', 4, 1, 3), " +
                "('PLA-2200', 'Modelado I', 3, 1, 3), " +
                "('PUB-1300', 'Diseno Basico Bidimensional', 3, 1, 3), " +
                // Semestre 2
                "('CRI-1140', 'Historia del Arte II', 3, 2, 3), " +
                "('FIL-0110', 'Int a la Filosofia', 3, 2, 3), " +
                "('LET-0120', 'Lengua Espanola Basica II', 3, 2, 3), " +
                "('PLA-1290', 'Dibujo II', 4, 2, 3), " +
                "('PLA-2280', 'Teoria Y Practica Del Color', 3, 2, 3), " +
                "('PLA-3200', 'Modelado II', 3, 2, 3), " +
                "('PUB-2300', 'Diseno Basico Tridimensional', 3, 2, 3), " +
                "('SOC-0110', 'Int A Las Ciencias Sociales', 2, 2, 3), " +
                // Semestre 3
                "('ARQ-3290', 'Perspectiva Artistica', 3, 3, 3), " +
                "('CRI-1170', 'Historia del Arte Dominicano I', 3, 3, 3), " +
                "('CRI-2110', 'Estetica I', 3, 3, 3), " +
                "('PLA-3210', 'Pintura I', 3, 3, 3), " +
                "('PLA-3250', 'Dibujo III', 3, 3, 3), " +
                "('PLA-3260', 'Int A La Pintura Al Oleo', 3, 3, 3), " +
                "('PLA-3270', 'Escultura I', 3, 3, 3), " +
                "('PLA-3280', 'Anatomia Artistica', 3, 3, 3), " +
                // Semestre 4
                "('CRI-2190', 'Teoria De Las Artes Visuales', 3, 4, 3), " +
                "('FIL-1240', 'Metodologia De La Inv Cientif', 3, 4, 3), " +
                "('PLA-3010', 'Taller De Comp Artistica', 4, 4, 3), " +
                "('PLA-3220', 'Pintura II', 3, 4, 3), " +
                "('PLA-4220', 'Pintura De Paisaje', 4, 4, 3), " +
                "('PLA-4260', 'Dibujo IV', 3, 4, 3), " +
                "('PLA-4280', 'Escultura II', 4, 4, 3), " +
                // Semestre 5
                "('CRI-3100', 'Arte Contemporaneo', 3, 5, 3), " +
                "('PLA-4200', 'Artes Aplicadas', 3, 5, 3), " +
                "('PLA-4210', 'Tecnicas Pictoricas', 3, 5, 3), " +
                "('PLA-4230', 'Grabado I', 3, 5, 3), " +
                "('PLA-4250', 'Pintura III', 4, 5, 3), " +
                "('PLA-4270', 'Dibujo Del Natural I', 4, 5, 3), " +
                "('PLA-5210', 'Taller Creativo I', 4, 5, 3), " +
                // Semestre 6
                "('CRI-4100', 'Semiologia Del Arte', 3, 6, 3), " +
                "('PLA-4240', 'Grabado II', 3, 6, 3), " +
                "('PLA-4290', 'Dibujo Del Natural II', 4, 6, 3), " +
                "('PLA-5200', 'Tec Restauracion Y Conserv Obr', 3, 6, 3), " +
                "('PLA-5220', 'Taller Creativo II', 4, 6, 3), " +
                "('PLA-5230', 'Pintura Mural I', 3, 6, 3), " +
                "('PLA-5260', 'Pintura IV', 4, 6, 3), " +
                // Semestre 7
                "('CRI-2140', 'Teoria y Critica del Arte', 3, 7, 3), " +
                "('CRI-3110', 'Elaboracion De Proy De Artes', 3, 7, 3), " +
                "('CRI-3130', 'Historia Del Arte Latinoamer', 3, 7, 3), " +
                "('CRI-3170', 'Museos', 3, 7, 3), " +
                "('CRI-4110', 'Met Y Tec De La Ed Artistica', 3, 7, 3), " +
                "('PLA-5240', 'Pintura Mural II', 4, 7, 3), " +
                "('PLA-5250', 'Taller Restauracion De Obras', 4, 7, 3), " +
                // Tesis de Grado (Semestre 8)
                "('PLA-8200', 'Tesis De Grado O Curso Equival', 8, 8, 3)");
        
        // Pensum de Cine y Televisión (carrera_id = 4)
        stmt.execute("INSERT INTO pensum (codigo, nombre, creditos, semestre, carrera_id) VALUES " +
                "('CIN-3610', 'Fotografia', 3, 1, 4), " +
                "('CRI-1100', 'Taller de Educacion Estetica', 4, 1, 4), " +
                "('CRI-1130', 'Historia del Arte I', 3, 1, 4), " +
                "('DOP-1000', 'Orientacion Institucional', 1, 1, 4), " +
                "('FIL-0110', 'Int a la Filosofia', 3, 1, 4), " +
                "('HIS-0110', 'Fund de Hist Social Dominicana', 3, 1, 4), " +
                "('LET-0110', 'Lengua Espanola Basica I', 3, 1, 4), " +
                "('PLA-1600', 'Expresion Grafica', 2, 1, 4), " +
                "('SOC-0110', 'Int A Las Ciencias Sociales', 2, 1, 4), " +
                // Semestre 2
                "('CIN-1160', 'Historia Del Cine', 3, 2, 4), " +
                "('CIN-1200', 'Teoria Del Color', 2, 2, 4), " +
                "('CIN-1620', 'Lenguaje Cinematografico', 3, 2, 4), " +
                "('CIN-3620', 'Fotografia II', 3, 2, 4), " +
                "('CRI-1140', 'Historia del Arte II', 3, 2, 4), " +
                "('FIS-0140', 'Fisica Basica', 4, 2, 4), " +
                "('LET-0120', 'Lengua Espanola Basica II', 3, 2, 4), " +
                "('PSI-1050', 'Int A La Psicologia', 3, 2, 4), " +
                // Semestre 3
                "('CIN-2330', 'Iluminacion', 3, 3, 4), " +
                "('CIN-3600', 'Teoria Del Cine', 3, 3, 4), " +
                "('CIN-3680', 'Camara Cinematografica', 3, 3, 4), " +
                "('CIN-4630', 'Tecnica De Video Y Edicion I', 3, 3, 4), " +
                "('COM-1160', 'Int A Las Ciencias De La Com', 3, 3, 4), " +
                "('CRI-2110', 'Estetica I', 3, 3, 4), " +
                "('FIL-1240', 'Metodologia De La Inv Cientif', 3, 3, 4), " +
                "('LET-1310', 'Int Estudio De La Literatura', 3, 3, 4), " +
                // Semestre 4
                "('CIN-2400', 'Escenografia Para Cine Y Tv', 3, 4, 4), " +
                "('CIN-2670', 'Tecnologia Audiovisual', 3, 4, 4), " +
                "('CIN-3310', 'Rodaje', 4, 4, 4), " +
                "('CIN-3690', 'Camara De Television', 3, 4, 4), " +
                "('CIN-4390', 'Documental De Cine Y Tv', 3, 4, 4), " +
                "('CIN-4600', 'Tecnicas De Sonido', 2, 4, 4), " +
                "('CIN-4610', 'Guion I', 3, 4, 4), " +
                "('CIN-4640', 'Tecnica De Video Y Edicion II', 3, 4, 4), " +
                // Semestre 5
                "('CIN-1690', 'Realizacion Cinematografica I', 5, 5, 4), " +
                "('CIN-2630', 'Guion Cinematografico', 4, 5, 4), " +
                "('CIN-3660', 'Semiologia Del Cine', 2, 5, 4), " +
                "('CIN-4650', 'Sonido Y Musicalizacion', 3, 5, 4), " +
                "('CIN-4660', 'Luminotecnia', 3, 5, 4), " +
                "('TEA-3410', 'Actuacion', 4, 5, 4), " +
                // Semestre 6
                "('CIN-3650', 'Teoria Y Critica Del Cine', 3, 6, 4), " +
                "('CIN-5620', 'Taller De Realizacion Cinemat', 5, 6, 4), " +
                "('CIN-5640', 'Produccion Cinematografica', 3, 6, 4), " +
                "('CRI-1170', 'Historia del Arte Dominicano I', 3, 6, 4), " +
                "('CRI-3110', 'Elaboracion De Proy De Artes', 3, 6, 4), " +
                "('CRI-3130', 'Historia Del Arte Latinoamer', 3, 6, 4), " +
                "('TEA-3440', 'Maquillaje Y Caracterizacion', 3, 6, 4), " +
                // Semestre 7
                "('CIN-5650', 'Efectos Especiales', 2, 7, 4), " +
                "('CIN-5660', 'Direccion Cinematografica', 3, 7, 4), " +
                "('CIN-5680', 'Video Reportaje', 3, 7, 4), " +
                "('CRI-4110', 'Met Y Tec De La Ed Artistica', 3, 7, 4), " +
                // Tesis de Grado (Semestre 8)
                "('CIN-7610', 'Tesis De Grado O Curso Equival', 8, 8, 4)");

        //  Licenciatura en Cinematografia, mencion Television (carrera_id = 5)
        stmt.execute("INSERT INTO pensum (codigo, nombre, creditos, semestre, carrera_id) VALUES " +
                // Semestre 1
                "('CIN-3610', 'Fotografia', 3, 1, 5), " +
                "('CRI-1100', 'Taller de Educacion Estetica', 4, 1, 5), " +
                "('CRI-1130', 'Historia del Arte I', 3, 1, 5), " +
                "('DOP-1000', 'Orientacion Institucional', 1, 1, 5), " +
                "('FIL-0110', 'Int a la Filosofia', 3, 1, 5), " +
                "('HIS-0110', 'Fund de Hist Social Dominicana', 3, 1, 5), " +
                "('LET-0110', 'Lengua Espanola Basica I', 3, 1, 5), " +
                "('PLA-1600', 'Expresion Grafica', 2, 1, 5), " +
                "('SOC-0110', 'Int A Las Ciencias Sociales', 2, 1, 5), " +
                // Semestre 2
                "('CIN-1160', 'Historia Del Cine', 3, 2, 5), " +
                "('CIN-1200', 'Teoria Del Color', 2, 2, 5), " +
                "('CIN-1620', 'Lenguaje Cinematografico', 3, 2, 5), " +
                "('CIN-3620', 'Fotografia II', 3, 2, 5), " +
                "('CRI-1140', 'Historia del Arte II', 3, 2, 5), " +
                "('FIS-0140', 'Fisica Basica', 4, 2, 5), " +
                "('LET-0120', 'Lengua Espanola Basica II', 3, 2, 5), " +
                "('PSI-1050', 'Int A La Psicologia', 3, 2, 5), " +
                // Semestre 3
                "('CIN-2330', 'Iluminacion', 3, 3, 5), " +
                "('CIN-3600', 'Teoria Del Cine', 3, 3, 5), " +
                "('CIN-3680', 'Camara Cinematografica', 3, 3, 5), " +
                "('CIN-4630', 'Tecnica De Video Y Edicion I', 3, 3, 5), " +
                "('COM-1160', 'Int A Las Ciencias De La Com', 3, 3, 5), " +
                "('CRI-2110', 'Estetica I', 3, 3, 5), " +
                "('FIL-1240', 'Metodologia De La Inv Cientif', 3, 3, 5), " +
                "('LET-1310', 'Int Estudio De La Literatura', 3, 3, 5), " +
                // Semestre 4
                "('CIN-2400', 'Escenografia Para Cine Y Tv', 3, 4, 5), " +
                "('CIN-2670', 'Tecnologia Audiovisual', 3, 4, 5), " +
                "('CIN-3310', 'Rodaje', 4, 4, 5), " +
                "('CIN-3690', 'Camara De Television', 3, 4, 5), " +
                "('CIN-4390', 'Documental De Cine Y Tv', 3, 4, 5), " +
                "('CIN-4600', 'Tecnicas De Sonido', 2, 4, 5), " +
                "('CIN-4610', 'Guion I', 3, 4, 5), " +
                "('CIN-4640', 'Tecnica De Video Y Edicion II', 3, 4, 5), " +
                // Semestre 5
                "('CIN-1650', 'Tecnica De Television', 3, 5, 5), " +
                "('CIN-1660', 'Produccion De Television', 3, 5, 5), " +
                "('CIN-4620', 'Guion De Television', 3, 5, 5), " +
                "('CIN-4650', 'Sonido Y Musicalizacion', 3, 5, 5), " +
                "('CIN-4660', 'Luminotecnia', 3, 5, 5), " +
                "('CIN-5600', 'Television Y Sociedad', 3, 5, 5), " +
                "('PSI-2320', 'Psicologia De La Comunicacion', 3, 5, 5), " +
                "('TEA-3420', 'Diccion Y Vocalizacion', 3, 5, 5), " +
                // Semestre 6
                "('CIN-3020', 'Historia De La Television', 2, 6, 5), " +
                "('CIN-3670', 'Realizacion De Comerciales Tv', 4, 6, 5), " +
                "('CIN-4680', 'Taller De Produccion De Tv', 4, 6, 5), " +
                "('CIN-4690', 'Sistema De Television', 2, 6, 5), " +
                "('COM-3360', 'Publicidad', 3, 6, 5), " +
                "('CRI-1170', 'Historia del Arte Dominicano I', 3, 6, 5), " +
                "('CRI-3110', 'Elaboracion De Proy De Artes', 3, 6, 5), " +
                "('TEA-3440', 'Maquillaje Y Caracterizacion', 3, 6, 5), " +
                // Semestre 7
                "('CIN-5300', 'Produccion De Programa De Tv', 4, 7, 5), " +
                "('CIN-5680', 'Video Reportaje', 3, 7, 5), " +
                "('CIN-5690', 'Direccion Tecnica De Tv', 3, 7, 5), " +
                "('CRI-2010', 'Semiologia de la Imagen', 2, 7, 5), " +
                "('CRI-2140', 'Teoria y Critica del Arte', 3, 7, 5), " +
                // Tesis de Grado (Semestre 8)
                "('CIN-7620', 'Tesis De Grado O Curso Equival', 8, 8, 5)");
        
         //  Licenciatura en Cinematografia, mencion Television (carrera_id = 5)
        stmt.execute("INSERT INTO pensum (codigo, nombre, creditos, semestre, carrera_id) VALUES " +
                // Semestre 1
                "('CIN-3610', 'Fotografia', 3, 1, 6), " +
                "('CRI-1100', 'Taller de Educacion Estetica', 4, 1, 6), " +
                "('CRI-1130', 'Historia del Arte I', 3, 1, 6), " +
                "('DOP-1000', 'Orientacion Institucional', 1, 1, 6), " +
                "('FIL-0110', 'Int a la Filosofia', 3, 1, 6), " +
                "('HIS-0110', 'Fund de Hist Social Dominicana', 3, 1, 6), " +
                "('LET-0110', 'Lengua Espanola Basica I', 3, 1, 6), " +
                "('PLA-1600', 'Expresion Grafica', 2, 1, 6), " +
                "('SOC-0110', 'Int A Las Ciencias Sociales', 2, 1, 6), " +
                // Semestre 2
                "('CIN-1160', 'Historia Del Cine', 3, 2, 6), " +
                "('CIN-1200', 'Teoria Del Color', 2, 2, 6), " +
                "('CIN-1620', 'Lenguaje Cinematografico', 3, 2, 6), " +
                "('CIN-3620', 'Fotografia II', 3, 2, 6), " +
                "('CRI-1140', 'Historia del Arte II', 3, 2, 6), " +
                "('FIS-0140', 'Fisica Basica', 4, 2, 6), " +
                "('LET-0120', 'Lengua Espanola Basica II', 3, 2, 6), " +
                "('PSI-1050', 'Int A La Psicologia', 3, 2, 6), " +
                // Semestre 3
                "('CIN-2330', 'Iluminacion', 3, 3, 6), " +
                "('CIN-2670', 'Tecnologia Audiovisual', 3, 3, 6), " +
                "('CIN-4190', 'Historia De La Fotografia', 2, 3, 6), " +
                "('CIN-5310', 'Fotografia III', 2, 3, 6), " +
                "('CIN-5320', 'Composicion Fotografica', 3, 3, 6), " +
                "('CIN-5330', 'Revelado Fotografico I', 3, 3, 6), " +
                "('CRI-2110', 'Estetica I', 3, 3, 6), " +
                "('TEA-3440', 'Maquillaje Y Caracterizacion', 3, 3, 6), " +
                // Semestre 4
                "('CIN-3630', 'Fotografia Publicitaria', 3, 4, 6), " +
                "('CIN-3690', 'Camara De Television', 3, 4, 6), " +
                "('CIN-5340', 'Fotografia IV', 3, 4, 6), " +
                "('CIN-5350', 'Revelado Fotografico II', 3, 4, 6), " +
                "('COM-1140', 'Fotoperiodismo I', 3, 4, 6), " +
                "('FIL-1240', 'Metodologia De La Inv Cientif', 3, 4, 6), " +
                // Semestre 5
                "('CIN-4660', 'Luminotecnia', 3, 5, 6), " +
                "('CIN-5360', 'Tecnologia Fotografica', 3, 5, 6), " +
                "('CIN-5370', 'Ilustracion Fotografica', 3, 5, 6), " +
                "('CIN-5380', 'Fotografia V', 4, 5, 6), " +
                "('COM-1150', 'Fotoperiodismo II', 2, 5, 6)");
        
         //  Licenciatura en Historia y Critica del Arte (carrera_id = 7)
        stmt.execute("INSERT INTO pensum (codigo, nombre, creditos, semestre, carrera_id) VALUES " +
                // Semestre 1
                "('CRI-1100', 'Taller de Educacion Estetica', 4, 1, 7), " +
                "('CRI-1130', 'Historia del Arte I', 3, 1, 7), " +
                "('DOP-1000', 'Orientacion Institucional', 1, 1, 7), " +
                "('FIL-0110', 'Int a la Filosofia', 3, 1, 7), " +
                "('HIS-0110', 'Fund de Hist Social Dominicana', 3, 1, 7), " +
                "('HIS-1510', 'Int A La Antropologia', 3, 1, 7), " +
                "('LET-0110', 'Lengua Espanola Basica I', 3, 1, 7), " +
                "('SOC-0110', 'Int A Las Ciencias Sociales', 2, 1, 7), " +
                // Semestre 2
                "('CRI-1020', 'Arte y Lenguaje', 2, 2, 7), " +
                "('CRI-1140', 'Historia del Arte II', 3, 2, 7), " +
                "('CRI-2190', 'Teoria De Las Artes Visuales', 3, 2, 7), " +
                "('CRI-4150', 'Arte Precolombino Y Colonial', 3, 2, 7), " +
                "('HIS-1360', 'Fundamentos De His Universal', 4, 2, 7), " +
                "('HIS-1570', 'Int Metodologia Arqueologica', 3, 2, 7), " +
                "('LET-0120', 'Lengua Espanola Basica II', 3, 2, 7), " +
                "('PSI-1050', 'Int A La Psicologia', 3, 2, 7), " +
                // Semestre 3
                "('CRI-1040', 'Psicologia del Arte', 3, 3, 7), " +
                "('CRI-1190', 'Historia del Arte III', 3, 3, 7), " +
                "('CRI-2110', 'Estetica I', 3, 3, 7), " +
                "('CRI-3140', 'Sociologia Del Arte', 3, 3, 7), " +
                "('FIL-1240', 'Metodologia De La Inv Cientif', 3, 3, 7), " +
                "('HIS-1200', 'Historia General De America', 3, 3, 7), " +
                "('HIS-5500', 'Antropologia Del Arte', 3, 3, 7), " +
                "('LET-2400', 'Int A La Lit Universal', 3, 3, 7), " +
                // Semestre 4
                "('CRI-2120', 'Estetica II', 3, 4, 7), " +
                "('CRI-2140', 'Teoria y Critica del Arte', 3, 4, 7), " +
                "('CRI-3130', 'Historia Del Arte Latinoamer', 3, 4, 7), " +
                "('CRI-3150', 'Met De La Investig Artistica', 3, 4, 7), " +
                "('CRI-3170', 'Museos', 3, 4, 7), " +
                "('CRI-4100', 'Semiologia Del Arte', 3, 4, 7), " +
                "('CRI-4130', 'Historia Del Arte IV', 3, 4, 7), " +
                "('PUB-2390', 'Fund Teoricos Del Diseno', 3, 4, 7), " +
                // Semestre 5
                "('CRI-1170', 'Historia del Arte Dominicano I', 3, 5, 7), " +
                "('CRI-2010', 'Semiologia de la Imagen', 2, 5, 7), " +
                "('CRI-3180', 'Teoria Y Critica Del Arte I', 3, 5, 7), " +
                "('CRI-3190', 'Teoria Y Critica Artes Liter', 3, 5, 7), " +
                "('CRI-4120', 'Museografia I', 3, 5, 7), " +
                "('DIS-2790', 'Arte Y Tecnologia', 3, 5, 7), " +
                "('MUS-3550', 'Historia De La Musica', 3, 5, 7), " +
                "('TEA-3430', 'Historia Del Teatro I', 3, 5, 7), " +
                // Semestre 6
                "('ARQ-1800', 'Historia De La Arquitectura', 3, 6, 7), " +
                "('CIN-1160', 'Historia Del Cine', 3, 6, 7), " +
                "('CIN-2670', 'Tecnologia Audiovisual', 3, 6, 7), " +
                "('CRI-3110', 'Elaboracion De Proy De Artes', 3, 6, 7), " +
                "('CRI-4170', 'Teoria Y Critica Del Arte II', 3, 6, 7), " +
                "('CRI-5020', 'Museografia II', 3, 6, 7), " +
                "('CRI-5120', 'His Del Arte Dominicano II', 3, 6, 7), " +
                "('DIS-2770', 'Teoria De Los Objetos', 3, 6, 7), " +
                "('MUS-1570', 'Lenguaje Musical', 3, 6, 7), " +
                // Semestre 7
                "('CIN-3650', 'Teoria Y Critica Del Cine', 3, 7, 7), " +
                "('CRI-2020', 'Seminario de Textos Artisticos', 3, 7, 7), " +
                "('CRI-3100', 'Arte Contemporaneo', 3, 7, 7), " +
                "('CRI-3330', 'Teoria Y Critica De La Arquit', 3, 7, 7), " +
                "('CRI-4110', 'Met Y Tec De La Ed Artistica', 3, 7, 7), " +
                "('MUS-4500', 'Teoria Y Critica De La Musica', 3, 7, 7), " +
                "('TEA-4400', 'Teoria Y Critica Del Teatro', 3, 7, 7), " +
                // Tesis de Grado (Semestre 8)
                "('CRI-7130', 'Tesis De Grado O Curso Equival', 8, 8, 7)");
        
         //  Licenciatura en Artes Industriales, mencion Diseno Industrial (carrera_id = 8)
        stmt.execute("INSERT INTO pensum (codigo, nombre, creditos, semestre, carrera_id) VALUES " +
                // Semestre 1
                "('CRI-1100', 'Taller de Educacion Estetica', 4, 1, 8), " +
                "('CRI-1130', 'Historia del Arte I', 3, 1, 8), " +
                "('DIS-1710', 'Int Al Diseno Artesanal E Ind', 3, 1, 8), " +
                "('DOP-1000', 'Orientacion Institucional', 1, 1, 8), " +
                "('HIS-0110', 'Fund de Hist Social Dominicana', 3, 1, 8), " +
                "('LET-0110', 'Lengua Espanola Basica I', 3, 1, 8), " +
                "('PLA-1270', 'Dibujo I', 4, 1, 8), " +
                "('PUB-1300', 'Diseno Basico Bidimensional', 3, 1, 8), " +
                // Semestre 2
                "('ARQ-1050', 'Dibujo Tecnico I', 4, 2, 8), " +
                "('CRI-1140', 'Historia del Arte II', 3, 2, 8), " +
                "('DIS-1770', 'Fact Humanos Y Func Del Diseno', 2, 2, 8), " +
                "('DIS-1780', 'Fund Diseno Artesanal E Indust', 2, 2, 8), " +
                "('LET-0120', 'Lengua Espanola Basica II', 3, 2, 8), " +
                "('PLA-1290', 'Dibujo II', 4, 2, 8), " +
                "('PLA-2200', 'Modelado I', 3, 2, 8), " +
                "('PUB-2300', 'Diseno Basico Tridimensional', 3, 2, 8), " +
                // Semestre 3
                "('ARQ-2010', 'Dibujo Tecnico Industrial', 4, 3, 8), " +
                "('DIS-1730', 'Diseno Industrial I', 3, 3, 8), " +
                "('DIS-1750', 'Taller De Ceramica Indust I', 4, 3, 8), " +
                "('DIS-3710', 'Taller Artesanal I', 3, 3, 8), " +
                "('FIL-0110', 'Int a la Filosofia', 3, 3, 8), " +
                "('PLA-2270', 'Dib Avanzado Y Composicion I', 3, 3, 8), " +
                "('PLA-2280', 'Teoria Y Practica Del Color', 3, 3, 8), " +
                // Semestre 4
                "('CRI-1170', 'Historia del Arte Dominicano I', 3, 4, 8), " +
                "('CRI-2110', 'Estetica I', 3, 4, 8), " +
                "('DIS-1740', 'Diseno Industrial II', 3, 4, 8), " +
                "('DIS-1760', 'Taller De Ceramica Indust II', 4, 4, 8), " +
                "('DIS-2770', 'Teoria De Los Objetos', 3, 4, 8), " +
                "('DIS-3290', 'Tecnica De Pintura Y Esmalte', 3, 4, 8), " +
                "('DIS-3720', 'Taller Artesanal II', 3, 4, 8), " +
                "('SOC-0110', 'Int A Las Ciencias Sociales', 2, 4, 8), " +
                // Semestre 5
                "('DIS-2740', 'Cultura Y Ambiente Del Diseno', 3, 5, 8), " +
                "('DIS-2750', 'Diseno Industrial III', 4, 5, 8), " +
                "('DIS-2760', 'Taller Industrial I', 4, 5, 8), " +
                "('DIS-2780', 'Cs Y Tecnol De Los Materiales', 3, 5, 8), " +
                "('DIS-3700', 'Decorado De Superficie', 3, 5, 8), " +
                "('FIL-1240', 'Metodologia De La Inv Cientif', 3, 5, 8), " +
                "('PSI-1050', 'Int A La Psicologia', 3, 5, 8), " +
                "('PUB-1790', 'Construccion De Modelo', 3, 5, 8), " +
                // Semestre 6
                "('CRI-3110', 'Elaboracion De Proy De Artes', 3, 6, 8), " +
                "('DIS-2710', 'Tecnica De Fundicion Y Vaciado', 4, 6, 8), " +
                "('DIS-2790', 'Arte Y Tecnologia', 3, 6, 8), " +
                "('DIS-3760', 'Diseno Industrial IV', 4, 6, 8), " +
                "('DIS-3770', 'Taller Industrial II', 4, 6, 8), " +
                "('PUB-4300', 'Int Al Sistema Graf Por Comput', 3, 6, 8), " +
                // Semestre 7
                "('ADM-4610', 'Investigacion De Mercado I', 3, 7, 8), " +
                "('CRI-4110', 'Met Y Tec De La Ed Artistica', 3, 7, 8), " +
                "('DIS-2730', 'Procesos De Produccion Indust', 3, 7, 8), " +
                "('DIS-3730', 'Taller De Fundicion Y Vaciado', 5, 7, 8), " +
                "('DIS-3740', 'Tecnologia Industrial', 3, 7, 8), " +
                "('DIS-4710', 'Diseno Industrial Por Computad', 4, 7, 8), " +
                "('PSI-1320', 'Int A La Psi Industrial', 3, 7, 8), " +
                // Tesis de Grado (Semestre 8)
                "('DIS-7740', 'Tesis De Grado O Curso Equival', 8, 8, 8)");
        
        //  Licenciatura en Artes Industriales, mencion Diseno de Modas (carrera_id = 9)
        stmt.execute("INSERT INTO pensum (codigo, nombre, creditos, semestre, carrera_id) VALUES " +
                // Semestre 1
                "('CRI-1100', 'Taller de Educacion Estetica', 4, 1, 9), " +
                "('CRI-1130', 'Historia del Arte I', 3, 1, 9), " +
                "('DIS-1800', 'Introduccion Al Diseno De Moda', 3, 1, 9), " +
                "('DOP-1000', 'Orientacion Institucional', 1, 1, 9), " +
                "('HIS-0110', 'Fund de Hist Social Dominicana', 3, 1, 9), " +
                "('LET-0110', 'Lengua Espanola Basica I', 3, 1, 9), " +
                "('PLA-1270', 'Dibujo I', 4, 1, 9), " +
                "('PUB-1300', 'Diseno Basico Bidimensional', 3, 1, 9), " +
                // Semestre 2
                "('ARQ-1050', 'Dibujo Tecnico I', 4, 2, 9), " +
                "('CRI-1140', 'Historia del Arte II', 3, 2, 9), " +
                "('DIS-1770', 'Fact Humanos Y Fund Del Diseno', 2, 2, 9), " +
                "('LET-0120', 'Lengua Espanola Basica II', 3, 2, 9), " +
                "('PLA-1290', 'Dibujo II', 4, 2, 9), " +
                "('PLA-3280', 'Anatomia Artistica', 3, 2, 9), " +
                "('PUB-2300', 'Diseno Basico Tridimensional', 3, 2, 9), " +
                "('SOC-0110', 'Int A Las Ciencias Sociales', 2, 2, 9), " +
                // Semestre 3
                "('CIN-3610', 'Fotografia', 3, 3, 9), " +
                "('DIS-1810', 'Dibujo De Moda', 3, 3, 9), " +
                "('DIS-1820', 'Modelado Y Const De Moda I', 3, 3, 9), " +
                "('DIS-1830', 'Diseno De Moda I', 3, 3, 9), " +
                "('DIS-1850', 'Historia De La Moda I', 2, 3, 9), " +
                "('FIL-0110', 'Int a la Filosofia', 3, 3, 9), " +
                "('PLA-2280', 'Teoria Y Practica Del Color', 3, 3, 9), " +
                "('QUI-0140', 'Quimica Basica', 3, 3, 9), " +
                // Semestre 4
                "('CIN-1870', 'Fotografia De Moda', 3, 4, 9), " +
                "('CRI-2110', 'Estetica I', 3, 4, 9), " +
                "('DIS-1840', 'Diseno De Moda II', 3, 4, 9), " +
                "('DIS-1860', 'Historia De La Moda II', 2, 4, 9), " +
                "('DIS-1880', 'Introduccion Al Textil', 3, 4, 9), " +
                "('DIS-2800', 'Tec De Ilustracion De Moda', 3, 4, 9), " +
                "('DIS-2810', 'Taller De Confeccion I', 4, 4, 9), " +
                "('DIS-2880', 'Modelado Y Const De Moda II', 3, 4, 9), " +
                // Semestre 5
                "('CRI-1170', 'Historia del Arte Dominicano I', 3, 5, 9), " +
                "('DIS-1890', 'Tecnologia Textil', 3, 5, 9), " +
                "('DIS-2740', 'Cultura Y Ambiente Del Diseno', 3, 5, 9), " +
                "('DIS-2820', 'Taller De Confeccion II', 4, 5, 9), " +
                "('DIS-2830', 'Ilustracion De Moda I', 3, 5, 9), " +
                "('DIS-2850', 'Diseno De Moda III', 3, 5, 9), " +
                "('DIS-2870', 'Diseno Textil Y Estampado', 3, 5, 9), " +
                "('FIL-1240', 'Metodologia De La Inv Cientif', 3, 5, 9), " +
                // Semestre 6
                "('ADM-3630', 'Fundamentos De Mercadotecnia', 4, 6, 9), " +
                "('CRI-4110', 'Met Y Tec De La Ed Artistica', 3, 6, 9), " +
                "('DIS-2730', 'Procesos De Produccion Indust', 3, 6, 9), " +
                "('DIS-2840', 'Ilustracion De Moda II', 3, 6, 9), " +
                "('DIS-2860', 'Diseno De Moda IV', 4, 6, 9), " +
                "('DIS-3830', 'Taller De Confeccion III', 4, 6, 9), " +
                "('PUB-4300', 'Int Al Sistema Graf Por Comput', 3, 6, 9), " +
                // Semestre 7
                "('ADM-4610', 'Investigacion De Mercado I', 3, 7, 9), " +
                "('CRI-3110', 'Elaboracion De Proy De Artes', 3, 7, 9), " +
                "('DIS-2890', 'Diseno Textil', 5, 7, 9), " +
                "('DIS-3820', 'Tecnicas De Decoracion De Moda', 3, 7, 9), " +
                "('DIS-3840', 'Diseno De Moda V', 3, 7, 9), " +
                "('DIS-3850', 'Estilo Y Tendencia De La Moda', 3, 7, 9), " +
                "('PSI-1320', 'Int A La Psi Industrial', 3, 7, 9), " +
                // Tesis de Grado (Semestre 8)
                "('DIS-7750', 'Tesis De Grado O Curso Equival', 8, 8, 9)");
        
        //  Licenciatura en Publicidad, mencion Diseno (carrera_id = 11)
         stmt.execute("INSERT INTO pensum (codigo, nombre, creditos, semestre, carrera_id) VALUES " +
                // Semestre 1
                "('ART-1100', 'Taller De Educacion Estetica', 4, 1, 10), " +
                "('ART-1130', 'Historia Del Arte I', 3, 1, 10), " +
                "('ART-1500', 'Lenguaje Musical I', 3, 1, 10), " +
                "('ART-1910', 'Instrumento I', 3, 1, 10), " +
                "('ART-1930', 'Entrenamiento Ritmico Audit I', 2, 1, 10), " +
                "('ART-2480', 'Expresion Corporal', 3, 1, 10), " +
                "('DOP-1000', 'Orientacion Institucional', 1, 1, 10), " +
                "('HIS-0110', 'Fund de Hist Social Dominicana', 3, 1, 10), " +
                "('LET-0110', 'Lengua Espanola Basica I', 3, 1, 10), " +
                // Semestre 2
                "('ART-1140', 'Historia Del Arte II', 3, 2, 10), " +
                "('ART-1920', 'Instrumento II', 3, 2, 10), " +
                "('ART-1940', 'Entrenamiento Ritmico Audit II', 3, 2, 10), " +
                "('ART-2500', 'Lenguaje Musical II', 3, 2, 10), " +
                "('FIL-0110', 'Int a la Filosofia', 3, 2, 10), " +
                "('LET-0120', 'Lengua Espanola Basica II', 3, 2, 10), " +
                "('PED-1110', 'Int A La Educacion', 3, 2, 10), " +
                "('SOC-0100', 'Introd A Las Ciencias Sociales', 3, 2, 10), " +
                // Semestre 3
                "('ART-1170', 'His Del Arte Dominicano I', 3, 3, 10), " +
                "('ART-2930', 'Instrumento III (Guitarra I)', 3, 3, 10), " +
                "('ART-3500', 'Int A Las Formas Musicales', 3, 3, 10), " +
                "('ART-3570', 'Fund De La Tecnica Vocal', 2, 3, 10), " +
                "('ART-4540', 'Teoria De La Educ Musical I', 3, 3, 10), " +
                "('FIL-1240', 'Metodologia De La Inv Cientif', 3, 3, 10), " +
                "('PED-1220', 'Didactica General', 5, 3, 10), " +
                "('PSI-1050', 'Int A La Psicologia', 3, 3, 10), " +
                // Semestre 4
                "('ART-1970', 'Armonia Musical I', 3, 4, 10), " +
                "('ART-2900', 'Conjunto Orquestal I', 3, 4, 10), " +
                "('ART-2940', 'Instrumento IV (Guitarra II)', 3, 4, 10), " +
                "('ART-4520', 'Tecnica Vocal I', 2, 4, 10), " +
                "('ART-4530', 'Estetica Musical', 3, 4, 10), " +
                "('ART-4950', 'Teoria De La Educ Musical II', 3, 4, 10), " +
                "('ART-5100', 'Fundamentos Del Folklore', 2, 4, 10), " +
                "('ART-5570', 'Pract De La Educ Musical I', 3, 4, 10), " +
                // Semestre 5
                "('ART-1980', 'Armonia Musical II', 3, 5, 10), " +
                "('ART-2960', 'Conjunto Orquestal II', 4, 5, 10), " +
                "('ART-3590', 'Practica Coral I', 2, 5, 10), " +
                "('ART-3910', 'Instrumento V (Guitarra III)', 4, 5, 10), " +
                "('ART-5590', 'Pract De La Educ Musical II', 4, 5, 10), " +
                "('ART-6500', 'Musico-Terapia', 2, 5, 10), " +
                // Semestre 6
                "('ART-1990', 'Armonia Musical III', 3, 6, 10), " +
                "('ART-3110', 'Elaboracion De Proy De Artes', 3, 6, 10), " +
                "('ART-3920', 'Instrumento VI (Guitarra IV)', 4, 6, 10), " +
                "('ART-3940', 'Acustica Y Organologia', 3, 6, 10), " +
                "('ART-3950', 'Conjunto Orquestal III', 4, 6, 10), " +
                "('ART-4500', 'Teoria Y Critica De La Musica', 3, 6, 10), " +
                "('ART-4510', 'Practica Coral II', 2, 6, 10), " +
                "('ART-5540', 'Composicion Musical I', 3, 6, 10), " +
                // Tesis de Grado (Semestre 7)
                "('ART-7430', 'Tesis De Grado O Curso Equival', 8, 7, 10)");
         
          //  Licenciatura en Publicidad, mencion Diseno (carrera_id = 11)
        stmt.execute("INSERT INTO pensum (codigo, nombre, creditos, semestre, carrera_id) VALUES " +
                // Semestre 1
                "('CRI-1100', 'Taller de Educacion Estetica', 4, 1, 11), " +
                "('CRI-1130', 'Historia del Arte I', 3, 1, 11), " +
                "('DOP-1000', 'Orientacion Institucional', 1, 1, 11), " +
                "('FIL-0110', 'Int a la Filosofia', 3, 1, 11), " +
                "('HIS-0110', 'Fund de Hist Social Dominicana', 3, 1, 11), " +
                "('LET-0110', 'Lengua Espanola Basica I', 3, 1, 11), " +
                "('PLA-1270', 'Dibujo I', 4, 1, 11), " +
                "('PUB-1300', 'Diseno Basico Bidimensional', 3, 1, 11), " +
                // Semestre 2
                "('ARQ-1050', 'Dibujo Tecnico I', 4, 2, 11), " +
                "('CRI-1140', 'Historia del Arte II', 3, 2, 11), " +
                "('LET-0120', 'Lengua Espanola Basica II', 3, 2, 11), " +
                "('PLA-1290', 'Dibujo II', 4, 2, 11), " +
                "('PLA-2280', 'Teoria Y Practica Del Color', 3, 2, 11), " +
                "('PLA-3280', 'Anatomia Artistica', 3, 2, 11), " +
                "('PUB-1350', 'Diseno Grafico Publicitario I', 3, 2, 11), " +
                "('PUB-2300', 'Diseno Basico Tridimensional', 3, 2, 11), " +
                // Semestre 3
                "('ARQ-3290', 'Perspectiva Artistica', 3, 3, 11), " +
                "('COM-1280', 'Artes Graficas I', 2, 3, 11), " +
                "('CRI-1170', 'Historia del Arte Dominicano I', 3, 3, 11), " +
                "('CRI-2110', 'Estetica I', 3, 3, 11), " +
                "('PLA-2200', 'Modelado I', 3, 3, 11), " +
                "('PLA-2270', 'Dib Avanzado Y Composicion I', 3, 3, 11), " +
                "('PLA-3210', 'Pintura I', 3, 3, 11), " +
                "('PUB-1360', 'Diseno Graf Publicitario II', 3, 3, 11), " +
                "('SOC-0110', 'Int A Las Ciencias Sociales', 2, 3, 11), " +
                // Semestre 4
                "('CIN-3610', 'Fotografia', 3, 4, 11), " +
                "('COM-1290', 'Artes Graficas II', 2, 4, 11), " +
                "('FIL-1240', 'Metodologia De La Inv Cientif', 3, 4, 11), " +
                "('PLA-2290', 'Dib Avanzado Y Composicion II', 3, 4, 11), " +
                "('PLA-3220', 'Pintura II', 3, 4, 11), " +
                "('PLA-6200', 'Tecnica De Ilustracion', 3, 4, 11), " +
                "('PSI-1050', 'Int A La Psicologia', 3, 4, 11), " +
                "('PUB-2350', 'Diseno Graf Publicitario III', 3, 4, 11), " +
                // Semestre 5
                "('CIN-3630', 'Fotografia Publicitaria', 3, 5, 11), " +
                "('COM-3220', 'Publicidad', 3, 5, 11), " +
                "('CRI-2190', 'Teoria De Las Artes Visuales', 3, 5, 11), " +
                "('PSI-2330', 'Psicologia De La Publicidad', 3, 5, 11), " +
                "('PUB-1790', 'Construccion De Modelo', 3, 5, 11), " +
                "('PUB-3320', 'Creaciones Tipograficas', 3, 5, 11), " +
                "('PUB-3380', 'Diseno Graf Publicitario IV', 4, 5, 11), " +
                // Semestre 6
                "('CIN-2670', 'Tecnologia Audiovisual', 3, 6, 11), " +
                "('CRI-4100', 'Semiologia Del Arte', 3, 6, 11), " +
                "('PUB-3060', 'Tecnicas De Impresion Grafica', 3, 6, 11), " +
                "('PUB-3300', 'Diseno Y Tecnicas De Carteles', 3, 6, 11), " +
                "('PUB-3390', 'Diseno Graf Publicitario V', 4, 6, 11), " +
                "('PUB-4300', 'Int Al Sistema Graf Por Comput', 3, 6, 11), " +
                // Semestre 7
                "('CRI-3110', 'Elaboracion De Proy De Artes', 3, 7, 11), " +
                "('PUB-4310', 'Diseno De Envases Y Embalajes', 4, 7, 11), " +
                "('PUB-4320', 'Diseno Grafico Computarizado', 3, 7, 11), " +
                "('PUB-4340', 'Diseno De Anuncio De Tv', 3, 7, 11), " +
                // Tesis de Grado (Semestre 8)
                "('PUB-7330', 'Tesis De Grado O Curso Equival', 8, 8, 11)");
        
        //  Licenciatura en Teatro (carrera_id = 12)
        stmt.execute("INSERT INTO pensum (codigo, nombre, creditos, semestre, carrera_id) VALUES " +
                // Semestre 1
                "('ART-1100', 'Taller De Educacion Estetica', 4, 1, 12), " +
                "('ART-1130', 'Historia Del Arte I', 3, 1, 12), " +
                "('DOP-1000', 'Orientacion Institucional', 1, 1, 12), " +
                "('FIL-0110', 'Int a la Filosofia', 3, 1, 12), " +
                "('HIS-0110', 'Fund de Hist Social Dominicana', 3, 1, 12), " +
                "('LET-0110', 'Lengua Espanola Basica I', 3, 1, 12), " +
                "('SOC-0100', 'Introd A Las Ciencias Sociales', 3, 1, 12), " +
                // Semestre 2
                "('ART-2100', 'Arte Y Sociedad', 3, 2, 12), " +
                "('ART-2110', 'Estetica I', 3, 2, 12), " +
                "('ART-2490', 'Espacio Escenico', 3, 2, 12), " +
                "('ART-3470', 'Diccion Y Vocalizacion I', 3, 2, 12), " +
                "('LET-0120', 'Lengua Espanola Basica II', 3, 2, 12), " +
                "('PSI-1050', 'Int A La Psicologia', 3, 2, 12), " +
                // Semestre 3
                "('ART-3400', 'Teatro Y Psicologia', 2, 3, 12), " +
                "('ART-3430', 'Historia Del Teatro I', 3, 3, 12), " +
                "('ART-3440', 'Maquillaje Y Caracterizacion', 3, 3, 12), " +
                "('ART-3480', 'Diccion Y Vocalizacion II', 3, 3, 12), " +
                "('ART-4410', 'Escenografia Y Plast Teatro I', 3, 3, 12), " +
                "('ART-4430', 'Actuacion I', 4, 3, 12), " +
                // Semestre 4
                "('ART-1170', 'His Del Arte Dominicano I', 3, 4, 12), " +
                "('ART-4420', 'Escenografia Y Plast Teatro II', 3, 4, 12), " +
                "('ART-4440', 'Actuacion II', 4, 4, 12), " +
                "('ART-4470', 'Comentario De Textos Dramat', 3, 4, 12), " +
                "('ART-4490', 'Teoria Del Teatro', 3, 4, 12), " +
                "('FIL-1240', 'Metodologia De La Inv Cientif', 3, 4, 12), " +
                // Semestre 5
                "('ART-4100', 'Semiologia Del Arte', 3, 5, 12), " +
                "('ART-4650', 'Sonido Y Musicalizacion', 3, 5, 12), " +
                "('ART-4660', 'Luminotecnia', 3, 5, 12), " +
                "('ART-5400', 'Utileria Y Regiduria', 3, 5, 12), " +
                "('ART-5410', 'Vestuario', 3, 5, 12), " +
                "('ART-5450', 'Actuacion III', 4, 5, 12), " +
                // Semestre 6
                "('ART-1450', 'Expresion Escenica I', 2, 6, 12), " +
                "('ART-3110', 'Elaboracion De Proy De Artes', 3, 6, 12), " +
                "('ART-5420', 'Teatrologia', 3, 6, 12), " +
                "('ART-5430', 'Taller De Teatro', 5, 6, 12), " +
                "('ART-5440', 'Personajes Teatrales', 3, 6, 12), " +
                "('ART-5460', 'Actuacion IV', 4, 6, 12), " +
                "('ART-5470', 'His Del Teatro Latinoamericano', 3, 6, 12), " +
                // Semestre 7
                "('ART-4110', 'Met Y Tec De La Ed Artistica', 3, 7, 12), " +
                "('ART-4400', 'Teoria Y Critica Del Teatro', 3, 7, 12), " +
                "('ART-5480', 'Expresiones Teatrales Contemp', 3, 7, 12), " +
                "('ART-5490', 'Teleteatro', 4, 7, 12), " +
                "('ART-6520', 'Actuacion V', 4, 7, 12), " +
                // Tesis de Grado (Semestre 8)
                "('ART-7440', 'Tesis De Grado O Curso Equival', 8, 8, 12)");
        
        
            // Inicio de la Facultadad de ciencias 
        
        //  Licenciatura en Biologia (carrera_id = 13)
         stmt.execute("INSERT INTO pensum (codigo, nombre, creditos, semestre, carrera_id) VALUES " +
                // Semestre 1
                "('BIO-0140', 'Biologia Basica', 3, 1, 13), " +
                "('BIO-1150', 'Orientacion Bibliografica', 2, 1, 13), " +
                "('DOP-1000', 'Orientacion Institucional', 1, 1, 13), " +
                "('FIS-0140', 'Fisica Basica', 4, 1, 13), " +
                "('GEO-1210', 'Geografia Fisica General', 3, 1, 13), " +
                "('LET-0110', 'Lengua Espanola Basica I', 3, 1, 13), " +
                "('MAT-0140', 'Matematica Basica', 4, 1, 13), " +
                "('QUI-0140', 'Quimica Basica', 3, 1, 13), " +
                // Semestre 2
                "('BIO-1160', 'Desarrollo Hist De La Biologia', 2, 2, 13), " +
                "('BIO-1340', 'Zoologia De Invertebrados', 4, 2, 13), " +
                "('EFS-0110', 'Educacion Fisica', 1, 2, 13), " +
                "('FIL-0110', 'Int a la Filosofia', 3, 2, 13), " +
                "('FIS-1110', 'Fisica General I-A', 3, 2, 13), " +
                "('FIS-2210', 'Lab De Fisica General I', 1, 2, 13), " +
                "('HIS-0110', 'Fund de Hist Social Dominicana', 3, 2, 13), " +
                "('LET-0120', 'Lengua Espanola Basica II', 3, 2, 13), " +
                "('MAT-2300', 'Algebra Superior', 5, 2, 13), " +
                // Semestre 3
                "('BIO-1200', 'Botanica Criptogamica', 4, 3, 13), " +
                "('BIO-2300', 'Zoologia De Vertebrados', 4, 3, 13), " +
                "('EST-1360', 'Bioestadistica Experimental', 4, 3, 13), " +
                "('MAT-2570', 'Calculo Y Analitica I', 5, 3, 13), " +
                "('QUI-1110', 'Quimica Inorganica', 5, 3, 13), " +
                "('SOC-0110', 'Int A Las Ciencias Sociales', 2, 3, 13), " +
                // Semestre 4
                "('BIO-1170', 'Tecnicas Aplicadas', 2, 4, 13), " +
                "('BIO-1260', 'Botanica Fanerogamica', 4, 4, 13), " +
                "('BIO-2450', 'Ecologia General I', 4, 4, 13), " +
                "('BIO-2520', 'Genetica Animal Y Vegetal', 4, 4, 13), " +
                "('MIP-1140', 'Microbio Gen Y Especial', 4, 4, 13), " +
                "('QUI-1210', 'Quimica Organica', 4, 4, 13), " +
                // Semestre 5
                "('BIO-1270', 'Botanica Sistematica', 4, 5, 13), " +
                "('BIO-2340', 'Zoologia Sistematica', 4, 5, 13), " +
                "('BIO-2350', 'Embriologia Comparada', 3, 5, 13), " +
                "('BIO-2460', 'Ecologia General II', 4, 5, 13), " +
                "('CFI-1360', 'Procesos Bio-Qui Celulares', 4, 5, 13), " +
                "('MIP-1250', 'Parasitologia General', 5, 5, 13), " +
                // Semestre 6
                "('BIO-2330', 'Histologia General', 3, 6, 13), " +
                "('BIO-3410', 'Conservacion De Rec Naturales', 4, 6, 13), " +
                "('BIO-3510', 'Citologia Animal Y Vegetal', 4, 6, 13), " +
                "('IAG-2300', 'Fisiologia Veg Gral Y Aplicada', 4, 6, 13), " +
                "('IDI-0110', 'Ingles Elemental I', 4, 6, 13), " +
                // Semestre 7
                "('BIO-3110', 'Metodos De Inv Biologicas I', 4, 7, 13), " +
                "('CFI-2160', 'Zoofisiologia I', 3, 7, 13), " +
                "('IDI-0120', 'Ingles Elemental II', 4, 7, 13), " +
                "('INF-1010', 'Int Al Procesamiento De Datos', 4, 7, 13), " +
                // Semestre 8
                "('BIO-2540', 'Paleontologia Y Evolucion', 2, 8, 13), " +
                "('BIO-3120', 'Metodos De Inv Biologicas II', 4, 8, 13), " +
                "('BIO-ZZY0', 'Asignatura Optativa', 15, 8, 13), " +
                "('CFI-2170', 'Zoofisiologia II', 3, 8, 13), " +
                "('INF-1020', 'Int A Los Computad Personales', 4, 8, 13), " +
                // Semestre 9
                "('BIO-3130', 'Biofisica', 4, 9, 13), " +
                "('BIO-3470', 'Hidrobiologia', 4, 9, 13), " +
                // Tesis de Grado (Semestre 10)
                "('BIO-5000', 'Tesis De Grado O Curso Equival', 8, 10, 13)");
         
         //  Licenciatura en Geografia (carrera_id = 14)
        stmt.execute("INSERT INTO pensum (codigo, nombre, creditos, semestre, carrera_id) VALUES " +
                // Semestre 1
                "('ARQ-1010', 'Dibujo Tecnico I-B', 4, 1, 14), " +
                "('EST-1110', 'Estadistica General', 4, 1, 14), " +
                "('GEO-1010', 'Introduccion A La Geografia', 2, 1, 14), " +
                "('GEO-1210', 'Geografia Fisica General', 3, 1, 14), " +
                "('MAT-2500', 'Analitica Y Calculo I', 5, 1, 14), " +
                "('QUI-1110', 'Quimica Inorganica', 5, 1, 14), " +
                // Semestre 2
                "('AGM-1300', 'Topografia General', 4, 2, 14), " +
                "('ECN-2110', 'Introduccion A La Economia', 4, 2, 14), " +
                "('GEO-1020', 'Des Del Pensamiento Geografico', 2, 2, 14), " +
                "('GEO-1120', 'Geologia General', 4, 2, 14), " +
                "('GEO-1180', 'Geografia De Las Aguas', 3, 2, 14), " +
                "('GEO-1610', 'Mapas Y Diagramas I', 3, 2, 14), " +
                "('GEO-2410', 'Geografia Humana', 3, 2, 14), " +
                // Semestre 3
                "('EST-2260', 'Estadistica II', 4, 3, 14), " +
                "('FIS-2440', 'Meteorologia I', 4, 3, 14), " +
                "('GEO-1140', 'Geomorfologia', 3, 3, 14), " +
                "('GEO-1400', 'Geografia Americana', 3, 3, 14), " +
                "('GEO-1620', 'Mapas Y Diagramas II', 3, 3, 14), " +
                "('GEO-2440', 'Geo Fis E Historica Isla Sd', 3, 3, 14), " +
                // Semestre 4
                "('GEO-1150', 'Climatologia', 4, 4, 14), " +
                "('GEO-1430', 'Geografia Economica', 3, 4, 14), " +
                "('GEO-1440', 'Geo Eurasia Africa Y Oceania', 3, 4, 14), " +
                "('GEO-2010', 'Metodos De Inv Geografica I', 3, 4, 14), " +
                "('GEO-2620', 'Fotogram Y Fotointerpretacion', 3, 4, 14), " +
                "('GEO-2720', 'Geografia De La Poblacion', 3, 4, 14), " +
                // Semestre 5
                "('EST-1420', 'Demografia', 3, 5, 14), " +
                "('GEO-3010', 'Metodos De Inv Geografica II', 3, 5, 14), " +
                "('IAG-1140', 'Edafologia I', 4, 5, 14), " +
                // Semestre 6
                "('BIO-2420', 'Ecologia General', 4, 6, 14), " +
                "('GEO-3740', 'Organizacion Del Espacio I', 3, 6, 14), " +
                "('GEO-ZZX0', 'Asignaturas Optativas', 6, 6, 14), " +
                "('GEO-ZZY0', 'Asignatura(S) Optativa(S)', 4, 6, 14), " +
                // Semestre 7
                "('GEO-1310', 'Biogeografia', 3, 7, 14), " +
                "('GEO-3020', 'Seminario Geografico I', 1, 7, 14), " +
                "('GEO-4740', 'Organizacion Del Espacio II', 3, 7, 14), " +
                // Semestre 8
                "('BIO-3410', 'Conservacion De Rec Naturales', 4, 8, 14), " +
                "('GEO-4020', 'Seminario Geografico II', 1, 8, 14), " +
                "('GEO-6000', 'Tesis De Grado O Curso Equival', 5, 8, 14), " +
                "('GEO-ZZX0', 'Asignaturas Optativas', 6, 8, 14), " +
                "('GEO-ZZY0', 'Asignatura(S) Optativa(S) II', 4, 8, 14)");
        
         //  Licenciatura en Fisica (carrera_id = 15)
        stmt.execute("INSERT INTO pensum (codigo, nombre, creditos, semestre, carrera_id) VALUES " +
                // Semestre 1
                "('BIO-0140', 'Biologia Basica', 3, 1, 15), " +
                "('DOP-1000', 'Orientacion Institucional', 1, 1, 15), " +
                "('EFS-0110', 'Educacion Fisica', 1, 1, 15), " +
                "('FIL-0110', 'Int a la Filosofia', 3, 1, 15), " +
                "('FIS-0140', 'Fisica Basica', 4, 1, 15), " +
                "('LET-0110', 'Lengua Espanola Basica I', 3, 1, 15), " +
                "('MAT-0140', 'Matematica Basica', 4, 1, 15), " +
                // Semestre 2
                "('FIS-1110', 'Fisica General I-A', 3, 2, 15), " +
                "('HIS-0110', 'Fund de Hist Social Dominicana', 3, 2, 15), " +
                "('LET-0120', 'Lengua Espanola Basica II', 3, 2, 15), " +
                "('MAT-2300', 'Algebra Superior', 5, 2, 15), " +
                "('QUI-0140', 'Quimica Basica', 3, 2, 15), " +
                "('SOC-0110', 'Int A Las Ciencias Sociales', 2, 2, 15), " +
                // Semestre 3
                "('FIL-1240', 'Metodologia De La Inv Cientif', 3, 3, 15), " +
                "('FIS-2110', 'Fisica General I', 3, 3, 15), " +
                "('FIS-2210', 'Lab De Fisica General I', 1, 3, 15), " +
                "('FIS-2550', 'Historia De La Fisica', 2, 3, 15), " +
                "('INF-1010', 'Int Al Procesamiento De Datos', 4, 3, 15), " +
                "('MAT-2510', 'Calculo Y Analitica I', 6, 3, 15), " +
                // Semestre 4
                "('FIS-2120', 'Fisica General II', 3, 4, 15), " +
                "('FIS-2220', 'Lab De Fisica General II', 1, 4, 15), " +
                "('INF-1020', 'Int A Los Computad Personales', 4, 4, 15), " +
                "('MAT-3500', 'Calculo Y Geom Analitica II', 6, 4, 15), " +
                "('MAT-3700', 'Calc Vect Y Algebra Matricial', 5, 4, 15), " +
                "('QUI-1110', 'Quimica Inorganica', 5, 4, 15), " +
                // Semestre 5
                "('FIL-2210', 'Teoria Del Conocimiento I', 3, 5, 15), " +
                "('FIS-2130', 'Fisica General III', 3, 5, 15), " +
                "('FIS-2230', 'Lab De Fisica General III', 1, 5, 15), " +
                "('IDI-0110', 'Ingles Elemental I', 4, 5, 15), " +
                "('INF-2120', 'Fortran', 4, 5, 15), " +
                "('MAT-3600', 'Ecuaciones Diferenciales', 5, 5, 15), " +
                // Semestre 6
                "('FIS-2140', 'Fisica General IV', 3, 6, 15), " +
                "('FIS-2240', 'Lab De Fisica General IV', 1, 6, 15), " +
                "('FIS-3410', 'Metodo De Mat Y Fisica I', 4, 6, 15), " +
                "('IDI-0120', 'Ingles Elemental II', 4, 6, 15), " +
                "('IEM-2010', 'Circuitos I', 4, 6, 15), " +
                "('IEM-2040', 'Lab De Circuitos I', 1, 6, 15), " +
                // Semestre 7
                "('FIS-3110', 'Fisica Moderna', 4, 7, 15), " +
                "('FIS-3220', 'Mecanica Analitica I', 4, 7, 15), " +
                "('FIS-4420', 'Metodo De Mat Y Fisica II', 4, 7, 15), " +
                "('IEM-3020', 'Circuitos II', 4, 7, 15), " +
                "('IEM-3060', 'Lab De Circuitos II', 1, 7, 15), " +
                // Semestre 8
                "('FIS-3230', 'Mecanica Analitica II', 4, 8, 15), " +
                "('FIS-4710', 'Mecanica Cuantica', 4, 8, 15), " +
                "('FIS-4920', 'Optica', 4, 8, 15), " +
                "('IEM-3040', 'Electronica I', 4, 8, 15), " +
                "('IEM-3050', 'Lab Electronica I', 1, 8, 15), " +
                // Semestre 9
                "('FIS-3310', 'Electromagnetismo', 4, 9, 15), " +
                "('FIS-4000', 'Lab Avanzado Para Licenciado', 4, 9, 15), " +
                "('FIS-4110', 'Fisica Estadistica', 4, 9, 15), " +
                "('FIS-Z120', 'Asignatura Optativa', 12, 9, 15), " +
                "('IEM-4050', 'Electronica II', 4, 9, 15), " +
                "('IEM-4070', 'Lab Electronica II', 1, 9, 15), " +
                // Semestre 10
                "('FIS-4010', 'Fisica Del Estado Solido', 4, 10, 15), " +
                "('FIS-4120', 'Int A La Fisica Nuclear', 4, 10, 15), " +
                // Tesis de Grado (Semestre 11)
                "('FIS-7000', 'Tesis De Grado O Curso Equival', 8, 11, 15)");
        
        // Licenciatura en Informatica (carrera_id = 16)
        stmt.execute("INSERT INTO pensum (codigo, nombre, creditos, semestre, carrera_id) VALUES " +
                // Semestre 1
                "('BIO-0140', 'Biologia Basica', 3, 1, 16), " +
                "('DOP-1000', 'Orientacion Institucional', 1, 1, 16), " +
                "('HIS-0110', 'Fund de Hist Social Dominicana', 3, 1, 16), " +
                "('INF-2060', 'Computacion Esencial', 4, 1, 16), " +
                "('INF-2070', 'Lab de Computacion Esencial', 1, 1, 16), " +
                "('LET-0110', 'Lengua Espanola Basica I', 3, 1, 16), " +
                "('SOC-0100', 'Introd A Las Ciencias Sociales', 3, 1, 16), " +
                // Semestre 2
                "('EFS-0120', 'Educacion Fisica', 2, 2, 16), " +
                "('IDI-0280', 'Ingles Elemental', 5, 2, 16), " +
                "('INF-5100', 'Introduccion a la Programacion', 4, 2, 16), " +
                "('INF-5110', 'Lab de Int a la Programacion', 1, 2, 16), " +
                "('LET-0120', 'Lengua Espanola Basica II', 3, 2, 16), " +
                "('MAT-0140', 'Matematica Basica', 4, 2, 16), " +
                // Semestre 3
                "('FIL-0110', 'Int a la Filosofia', 3, 3, 16), " +
                "('FIS-0140', 'Fisica Basica', 4, 3, 16), " +
                "('IDI-0130', 'Ingles Tecnico', 4, 3, 16), " +
                "('INF-5120', 'Lenguaje de Programacion I', 4, 3, 16), " +
                "('INF-5130', 'Lab de Lenguaje de Programac I', 1, 3, 16), " +
                "('MAT-2330', 'Analisis Lineal Y Matricial', 5, 3, 16), " +
                "('QUI-0140', 'Quimica Basica', 3, 3, 16), " +
                // Semestre 4
                "('FIS-1150', 'Fisica para Informatica', 3, 4, 16), " +
                "('FIS-1160', 'Lab de Fisica para Informatica', 1, 4, 16), " +
                "('INF-5140', 'Lenguaje de Programacion II', 4, 4, 16), " +
                "('INF-5150', 'Lab de Lenguaje de Program II', 1, 4, 16), " +
                "('INF-5260', 'Estructura De Datos', 3, 4, 16), " +
                "('MAT-3560', 'Calculo I', 5, 4, 16), " +
                // Semestre 5
                "('EST-2110', 'Estadistica Basica', 5, 5, 16), " +
                "('INF-2080', 'Organiz y Arq del Computador', 4, 5, 16), " +
                "('INF-5160', 'Lenguaje de Programacion III', 4, 5, 16), " +
                "('INF-5170', 'Lab Lenguaje de Program III', 1, 5, 16), " +
                "('MAT-3570', 'Calculo II', 5, 5, 16), " +
                // Semestre 6
                "('CON-1190', 'Contabilidad General I', 5, 6, 16), " +
                "('INF-3220', 'Algoritmos Computacionales', 5, 6, 16), " +
                "('INF-3240', 'Sistemas Operativos', 4, 6, 16), " +
                "('INF-4200', 'Base de Datos I', 5, 6, 16), " +
                // Semestre 7
                "('BIO-2400', 'Bioetica', 2, 7, 16), " +
                "('INF-3290', 'Analisis y Diseno de Sistema', 5, 7, 16), " +
                "('INF-5200', 'Base de Datos II', 5, 7, 16), " +
                "('INF-ZZA0', 'Asignatura Optativa', 3, 7, 16), " +
                "('MAT-1430', 'Matematica Financiera', 5, 7, 16), " +
                "('MAT-3920', 'Matematica Discr para Computac', 4, 7, 16), " +
                // Semestre 8
                "('INF-4250', 'Teleproceso', 4, 8, 16), " +
                "('INF-4260', 'Redes De Proc De Datos', 4, 8, 16), " +
                "('INF-5220', 'Ingenieria de Software I', 4, 8, 16), " +
                "('INF-ZZB0', 'Asignatura Optativa', 5, 8, 16), " +
                "('MAT-3940', 'Investigacion de Operaciones', 5, 8, 16), " +
                // Semestre 9
                "('INF-5230', 'Auditoria de Sist Informaticos', 4, 9, 16), " +
                "('INF-5240', 'Adm de Centros de Computos', 4, 9, 16), " +
                "('INF-5250', 'Ingenieria de Software II', 4, 9, 16), " +
                "('INF-ZZC0', 'Asignatura Optativa', 4, 9, 16), " +
                // Tesis de Grado (Semestre 10)
                "('INF-7010', 'Tesis de Grado o Curso Equival', 8, 10, 16)");
        
        //  Licenciatura en Matematicas (carrera_id = 17)
        stmt.execute("INSERT INTO pensum (codigo, nombre, creditos, semestre, carrera_id) VALUES " +
                // Semestre 1
                "('BIO-0170', 'Biologia Basica', 2, 1, 17), " +
                "('BIO-0180', 'Laboratorio de Biologia Basica', 1, 1, 17), " +
                "('FIL-0110', 'Int a la Filosofia', 3, 1, 17), " +
                "('HIS-0110', 'Fund de Hist Social Dominicana', 3, 1, 17), " +
                "('LET-0110', 'Lengua Espanola Basica I', 3, 1, 17), " +
                "('MAT-0140', 'Matematica Basica', 4, 1, 17), " +
                "('OSI-0310', 'Orientacion Instituc y Academ', 2, 1, 17), " +
                "('QUI-0140', 'Quimica Basica', 3, 1, 17), " +
                "('SOC-0100', 'Introd A Las Ciencias Sociales', 3, 1, 17), " +
                // Semestre 2
                "('EFI-0120', 'Educacion Fisica', 2, 2, 17), " +
                "('FIS-0180', 'Fisica Basica', 3, 2, 17), " +
                "('FIS-0200', 'Laboratorio de Fisica Basica', 1, 2, 17), " +
                "('LET-0120', 'Lengua Espanola Basica II', 3, 2, 17), " +
                "('MAT-1400', 'Matematica Financiera', 5, 2, 17), " +
                "('MAT-2300', 'Algebra Superior', 5, 2, 17), " +
                "('MAT-2870', 'Logica Y Teoria De Conjuntos', 3, 2, 17), " +
                // Semestre 3
                "('BIO-2400', 'Bioetica', 2, 3, 17), " +
                "('MAT-2330', 'Analisis Lineal Y Matricial', 5, 3, 17), " +
                "('MAT-2510', 'Calculo Y Analitica I', 6, 3, 17), " +
                "('MAT-3410', 'Matematica Estadistica', 3, 3, 17), " +
                "('MAT-3920', 'Matematica Discr para Computac', 4, 3, 17), " +
                // Semestre 4
                "('FIS-2110', 'Fisica General I', 3, 4, 17), " +
                "('FIS-2210', 'Lab De Fisica General I', 1, 4, 17), " +
                "('INF-2060', 'Computacion Esencial', 4, 4, 17), " +
                "('INF-2070', 'Lab de Computacion Esencial', 1, 4, 17), " +
                "('MAT-1840', 'Geometria Moderna', 3, 4, 17), " +
                "('MAT-3420', 'Matematica Estadistica II', 3, 4, 17), " +
                "('MAT-3500', 'Calculo Y Geom Analitica II', 6, 4, 17), " +
                // Semestre 5
                "('FIS-2120', 'Fisica General II', 3, 5, 17), " +
                "('FIS-2220', 'Lab De Fisica General II', 1, 5, 17), " +
                "('INF-5100', 'Introduccion a la Programacion', 4, 5, 17), " +
                "('INF-5110', 'Lab de Int a la Programacion', 1, 5, 17), " +
                "('MAT-2880', 'Topologia', 3, 5, 17), " +
                "('MAT-3430', 'Matematica Estadistica III', 3, 5, 17), " +
                "('MAT-3730', 'Calculo Vectorial', 4, 5, 17), " +
                "('MAT-3880', 'Elementos de Algebra Moderna', 4, 5, 17), " +
                // Semestre 6
                "('FIS-2150', 'Fisica General III', 5, 6, 17), " +
                "('FIS-2250', 'Lab De Fisica General III', 2, 6, 17), " +
                "('INF-5120', 'Lenguaje de Programacion I', 4, 6, 17), " +
                "('INF-5130', 'Lab de Lenguaje de Programac I', 1, 6, 17), " +
                "('MAT-3480', 'Matematica para Administracion', 3, 6, 17), " +
                "('MAT-3600', 'Ecuaciones Diferenciales', 5, 6, 17), " +
                "('MAT-3750', 'Anal De Una Variable Real I', 4, 6, 17), " +
                // Semestre 7
                "('INF-5260', 'Estructura De Datos', 3, 7, 17), " +
                "('MAT-3650', 'Analisis Numerico', 4, 7, 17), " +
                "('MAT-3760', 'Anal De Una Variable Real II', 4, 7, 17), " +
                "('MAT-3770', 'Variable Compleja', 5, 7, 17), " +
                "('MAT-ZXX0', 'Asignatura Optativa', 3, 7, 17), " +
                // Semestre 8
                "('INF-4200', 'Base de Datos I', 5, 8, 17), " +
                "('MAT-2890', 'Historia De La Matematicas', 3, 8, 17), " +
                "('MAT-4790', 'Matematica para Ciencias', 5, 8, 17), " +
                "('MAT-ZXX0', 'Asignatura Optativa', 3, 8, 17), " +
                // Semestre 9
                "('MAT-3620', 'Ecuaciones Diferenciales Parci', 4, 9, 17), " +
                "('MAT-3800', 'Fundamentos De Las Matematicas', 3, 9, 17), " +
                "('MAT-4420', 'Pasantia', 3, 9, 17), " +
                "('MAT-ZXX0', 'Asignatura Optativa', 3, 9, 17), " +
                // Tesis de Grado (Semestre 10)
                "('MAT-7010', 'Tesis de Grado o Curso Equival', 8, 10, 17)");
        
        //  Licenciatura en Microbiologia (carrera_id = 18)
        stmt.execute("INSERT INTO pensum (codigo, nombre, creditos, semestre, carrera_id) VALUES " +
                // Semestre 1
                "('BIO-0170', 'Biologia Basica', 2, 1, 18), " +
                "('BIO-0180', 'Laboratorio de Biologia Basica', 1, 1, 18), " +
                "('FIL-0110', 'Int a la Filosofia', 3, 1, 18), " +
                "('HIS-0110', 'Fund de Hist Social Dominicana', 3, 1, 18), " +
                "('LET-0110', 'Lengua Espanola Basica I', 3, 1, 18), " +
                "('MAT-0140', 'Matematica Basica', 4, 1, 18), " +
                "('OSI-0310', 'Orientacion Instituc y Academ', 2, 1, 18), " +
                "('QUI-0140', 'Quimica Basica', 3, 1, 18), " +
                "('SOC-0100', 'Introd A Las Ciencias Sociales', 3, 1, 18), " +
                // Semestre 2
                "('BIO-1110', 'Biologia General Avanzada', 4, 2, 18), " +
                "('BIO-1150', 'Orientacion Bibliografica', 2, 2, 18), " +
                "('EFI-0120', 'Educacion Fisica', 2, 2, 18), " +
                "('FIS-0180', 'Fisica Basica', 3, 2, 18), " +
                "('FIS-0200', 'Laboratorio de Fisica Basica', 1, 2, 18), " +
                "('INF-1030', 'Introduccion A La Informatica', 4, 2, 18), " +
                "('LET-0120', 'Lengua Espanola Basica II', 3, 2, 18), " +
                "('QUI-1210', 'Quimica Organica', 4, 2, 18), " +
                // Semestre 3
                "('BIO-1500', 'Biologia Molecular Y Celular', 4, 3, 18), " +
                "('BIO-2400', 'Bioetica', 2, 3, 18), " +
                "('BIO-2420', 'Ecologia General', 4, 3, 18), " +
                "('BIO-2550', 'Genetica General', 4, 3, 18), " +
                "('MIP-1000', 'Redaccion Cientifica', 3, 3, 18), " +
                "('MIP-1090', 'Microbiologia General', 4, 3, 18), " +
                "('QUI-1170', 'Quimica Inorganica Cualitativa', 3, 3, 18), " +
                // Semestre 4
                "('MIP-1300', 'Ecologia Microbiana', 3, 4, 18), " +
                "('MIP-2100', 'Bacteriologia I', 4, 4, 18), " +
                "('MIP-2160', 'Protistologia General', 4, 4, 18), " +
                "('MIP-3170', 'Virologia General', 3, 4, 18), " +
                "('MIP-4100', 'Bioseguridad', 2, 4, 18), " +
                "('MIP-4510', 'Bioquimica Microbiana', 4, 4, 18), " +
                "('MIP-4530', 'Genetica Molecular Microbiana', 4, 4, 18), " +
                // Semestre 5
                "('MIP-1250', 'Parasitologia General', 5, 5, 18), " +
                "('MIP-2110', 'Bacteriologia II', 4, 5, 18), " +
                "('MIP-2290', 'Micologia General', 4, 5, 18), " +
                "('MIP-4520', 'Bioquimica Microbiana II', 4, 5, 18), " +
                "('MIP-4580', 'Fisiologia Microbiana', 4, 5, 18), " +
                "('MIP-ZZA0', 'Asignatura Optativa', 3, 5, 18), " +
                // Semestre 6
                "('BIO-3140', 'Metod de la Invest Cs Bio I', 4, 6, 18), " +
                "('MIP-1320', 'Microbiologia Ambiental', 4, 6, 18), " +
                "('MIP-1350', 'Microb Sistematica y Evolutiva', 3, 6, 18), " +
                "('MIP-2180', 'Microbiologia Clinica', 4, 6, 18), " +
                "('MIP-4150', 'Microbiologia de Alimentos', 4, 6, 18), " +
                "('MIP-ZZE0', 'Asignatura Optativa', 3, 6, 18), " +
                "('SAP-1020', 'Epidemiologia', 4, 6, 18), " +
                // Semestre 7
                "('BIO-3150', 'Metod de la Invst en Cs Bio II', 4, 7, 18), " +
                "('MIP-3160', 'Inmunomicrobiologia', 3, 7, 18), " +
                "('MIP-3200', 'Microbiologia en la Industria', 4, 7, 18), " +
                "('MIP-4560', 'Introd a la Biotecnologia', 3, 7, 18), " +
                "('MIP-4590', 'Microbiologia de Sistemas', 4, 7, 18), " +
                "('QUI-2520', 'Quimica Bioindustrial', 4, 7, 18), " +
                // Semestre 8
                "('MIP-4140', 'Pasantia', 5, 8, 18), " +
                // Tesis de Grado (Semestre 9)
                "('MIP-5110', 'Tesis De Grado o Curso Equival', 8, 9, 18)");
        
        //  Licenciatura en Quimica (carrera_id = 19)
        stmt.execute("INSERT INTO pensum (codigo, nombre, creditos, semestre, carrera_id) VALUES " +
                // Semestre 1
                "('BIO-0170', 'Biologia Basica', 2, 1, 19), " +
                "('BIO-0180', 'Laboratorio de Biologia Basica', 1, 1, 19), " +
                "('DOP-1000', 'Orientacion Institucional', 1, 1, 19), " +
                "('EFS-0120', 'Educacion Fisica', 2, 1, 19), " +
                "('HIS-0110', 'Fund de Hist Social Dominicana', 3, 1, 19), " +
                "('LET-0110', 'Lengua Espanola Basica I', 3, 1, 19), " +
                "('MAT-0140', 'Matematica Basica', 4, 1, 19), " +
                "('QUI-0140', 'Quimica Basica', 3, 1, 19), " +
                "('SOC-0100', 'Introd A Las Ciencias Sociales', 3, 1, 19), " +
                // Semestre 2
                "('BIO-2400', 'Bioetica', 2, 2, 19), " +
                "('FIL-0110', 'Int a la Filosofia', 3, 2, 19), " +
                "('FIS-0140', 'Fisica Basica', 4, 2, 19), " +
                "('LET-0120', 'Lengua Espanola Basica II', 3, 2, 19), " +
                "('MAT-2300', 'Algebra Superior', 5, 2, 19), " +
                "('QUI-1040', 'Quimica General', 5, 2, 19), " +
                // Semestre 3
                "('FIS-2110', 'Fisica General I', 3, 3, 19), " +
                "('FIS-2210', 'Lab De Fisica General I', 1, 3, 19), " +
                "('MAT-2510', 'Calculo Y Analitica I', 6, 3, 19), " +
                "('QUI-1150', 'Quimica Inorganica I', 4, 3, 19), " +
                "('QUI-1430', 'Quimica Analitica I', 5, 3, 19), " +
                "('QUI-2220', 'Quimica Organica I', 4, 3, 19), " +
                // Semestre 4
                "('FIS-2120', 'Fisica General II', 3, 4, 19), " +
                "('FIS-2220', 'Lab De Fisica General II', 1, 4, 19), " +
                "('MAT-3500', 'Calculo Y Geom Analitica II', 6, 4, 19), " +
                "('QUI-1160', 'Quimica Inorganica II', 4, 4, 19), " +
                "('QUI-1440', 'Quimica Analitica II', 5, 4, 19), " +
                "('QUI-3260', 'Quimica Organica II', 5, 4, 19), " +
                // Semestre 5
                "('FIS-2130', 'Fisica General III', 3, 5, 19), " +
                "('FIS-2230', 'Lab De Fisica General III', 1, 5, 19), " +
                "('MAT-3600', 'Ecuaciones Diferenciales', 5, 5, 19), " +
                "('QUI-1450', 'Quimica Analitica III', 5, 5, 19), " +
                "('QUI-2320', 'Quimica Fisica I', 4, 5, 19), " +
                "('QUI-3270', 'Quimica Organica III', 5, 5, 19), " +
                // Semestre 6
                "('EST-1210', 'Estadistica Industrial', 3, 6, 19), " +
                "('QUI-3360', 'Quimica Fisica II', 5, 6, 19), " +
                "('QUI-4240', 'Quimica Biorganica', 4, 6, 19), " +
                "('QUI-4500', 'Metodos De Invest En Quimica', 4, 6, 19), " +
                "('QUI-4540', 'Procesos Quimicos Industriales', 4, 6, 19), " +
                "('QUI-ZZE0', 'Asignatura Optativa', 3, 6, 19), " +
                // Semestre 7
                "('INQ-2110', 'Balances De Materia Y Energia', 5, 7, 19), " +
                "('QUI-2570', 'Int Al Aseguramiento Calidad', 3, 7, 19), " +
                "('QUI-3370', 'Quimica Fisica III', 5, 7, 19), " +
                "('QUI-ZZC0', 'Asignatura Optativa', 2, 7, 19), " +
                // Semestre 8
                "('MIP-3150', 'Microbiologia Y Fermentacion', 4, 8, 19), " +
                "('QUI-2540', 'Quimica Ambiental', 3, 8, 19), " +
                "('QUI-3460', 'Analisis Quimico Industrial', 3, 8, 19), " +
                "('QUI-ZZA0', 'Asignatura Optativa', 4, 8, 19), " +
                // Semestre 9
                "('QUI-4590', 'Pasantia Industrial', 3, 9, 19), " +
                "('QUI-7000', 'Tesis De Grado O Curso Equival', 8, 9, 19), " +
                "('QUI-ZZA0', 'Asignatura Optativa', 4, 9, 19)");
        
         //  Tecnologo Superior en Alimentos (carrera_id = 20)
        stmt.execute("INSERT INTO pensum (codigo, nombre, creditos, semestre, carrera_id) VALUES " +
                // Semestre 1
                "('BIO-0170', 'Biologia Basica', 2, 1, 20), " +
                "('BIO-0180', 'Laboratorio de Biologia Basica', 1, 1, 20), " +
                "('DOP-1000', 'Orientacion Institucional', 1, 1, 20), " +
                "('FIL-0110', 'Int a la Filosofia', 3, 1, 20), " +
                "('HIS-0110', 'Fund de Hist Social Dominicana', 3, 1, 20), " +
                "('LET-0110', 'Lengua Espanola Basica I', 3, 1, 20), " +
                "('MAT-0140', 'Matematica Basica', 4, 1, 20), " +
                "('QUI-0140', 'Quimica Basica', 3, 1, 20), " +
                "('SOC-0100', 'Introd A Las Ciencias Sociales', 3, 1, 20), " +
                // Semestre 2
                "('EFS-0120', 'Educacion Fisica', 2, 2, 20), " +
                "('FIS-0140', 'Fisica Basica', 4, 2, 20), " +
                "('LET-0120', 'Lengua Espanola Basica II', 3, 2, 20), " +
                "('MIP-1140', 'Microbio Gen Y Especial', 4, 2, 20), " +
                "('QUI-1040', 'Quimica General', 5, 2, 20), " +
                // Semestre 3
                "('FAR-1550', 'Nutricion Y Dietetica', 3, 3, 20), " +
                "('MAT-2590', 'Calculo Y Analitica', 4, 3, 20), " +
                "('MIP-3140', 'Microbiologia De Alimentos', 4, 3, 20), " +
                "('QUI-1210', 'Quimica Organica', 4, 3, 20), " +
                "('QUI-1430', 'Quimica Analitica I', 5, 3, 20), " +
                // Semestre 4
                "('QUI-1440', 'Quimica Analitica II', 5, 4, 20), " +
                "('QUI-3540', 'Quimica De Los Alimentos I', 4, 4, 20), " +
                "('QUI-3580', 'Quimica Bioindustrial I', 3, 4, 20), " +
                "('QUI-4320', 'Quimica Fisica', 4, 4, 20), " +
                "('QUI-ZZZ0', 'Asignatura Optativa', 6, 4, 20), " +
                // Semestre 5
                "('QUI-2550', 'Proc Qui Indus Alimenticia I', 3, 5, 20), " +
                "('QUI-3600', 'Control de Calidad de los Alim', 3, 5, 20), " +
                "('QUI-4560', 'Control Quimico Y Bioambiental', 3, 5, 20), " +
                "('QUI-4600', 'Tec Quimica de los Alimentos', 3, 5, 20), " +
                "('QUI-5690', 'Pasantia Industrial', 3, 5, 20), " +
                "('QUI-ZZZ0', 'Asignatura Optativa', 6, 5, 20)");
        
        
        //Inicio de la Facultadad de Ciencias Economicas y Sociales
        
         //  Licenciatura en Administracion de Empresas (carrera_id = 21)
        stmt.execute("INSERT INTO pensum (codigo, nombre, creditos, semestre, carrera_id) VALUES " +
                "('ADM-1150', 'Teoria Administrativa', 4, 1, 21), " +
                "('BIO-0140', 'Biologia Basica', 3, 1, 21), " +
                "('DOP-1000', 'Orientacion Institucional', 1, 1, 21), " +
                "('EFS-0110', 'Educacion Fisica', 1, 1, 21), " +
                "('FIL-0110', 'Int A La Filosofia', 3, 1, 21), " +
                "('HIS-0110', 'Fund De His Social Dominicana', 3, 1, 21), " +
                "('LET-0110', 'Lengua Espanola Basica I', 3, 1, 21), " +
                "('MAT-0140', 'Matematica Basica', 4, 1, 21), " +
                "('SOC-0110', 'Int A Las Ciencias Sociales', 2, 1, 21), " +
                // Semestre 2
                "('ADM-1160', 'Proceso Administrativo', 4, 2, 21), " +
                "('CON-1190', 'Contabilidad General I', 5, 2, 21), " +
                "('DER-1160', 'Nociones De Derecho Civil', 2, 2, 21), " +
                "('FIS-0140', 'Fisica Basica', 4, 2, 21), " +
                "('INF-1030', 'Introduccion A La Informatica', 4, 2, 21), " +
                "('LET-0120', 'Lengua Espanola Basica II', 3, 2, 21), " +
                "('SOC-1100', 'Int A La Sociologia General', 4, 2, 21), " +
                // Semestre 3
                "('ADM-1170', 'Direccion Estrategica', 4, 3, 21), " +
                "('ADM-2100', 'Administracion Turistica', 3, 3, 21), " +
                "('CON-1160', 'Contabilidad General II', 4, 3, 21), " +
                "('ECN-2110', 'Introduccion A La Economia', 4, 3, 21), " +
                "('MAT-2500', 'Analitica Y Calculo I', 5, 3, 21), " +
                "('QUI-0140', 'Quimica Basica', 4, 3, 21), " +
                // Semestre 4
                "('ADM-2310', 'Organizacion Y Adm Del Estado', 3, 4, 21), " +
                "('ADM-3360', 'Organizacion Y Metodos', 4, 4, 21), " +
                "('CON-2230', 'Fund De Contabilidad De Costos', 4, 4, 21), " +
                "('ECN-2230', 'Noc De Desarrollo Economico', 3, 4, 21), " +
                "('EST-1110', 'Estadistica General', 4, 4, 21), " +
                "('PSI-1050', 'Int A La Psicologia', 3, 4, 21), " +
                "('SOC-1270', 'Metodos Y Tec De Investigacion', 3, 4, 21), " +
                // Semestre 5
                "('ADM-2450', 'Adm De Recursos Humanos I', 4, 5, 21), " +
                "('ADM-3680', 'Administracion De Ventas', 3, 5, 21), " +
                "('ECN-3370', 'Economia Dominicana', 4, 5, 21), " +
                "('EST-2230', 'Metodos Estadisticos II', 4, 5, 21), " +
                "('MAT-1400', 'Matematica Financiera', 5, 5, 21), " +
                "('MER-1100', 'Fundamentos De Mercadotecnia', 4, 5, 21), " +
                // Semestre 6
                "('ADM-2460', 'Adm De Recursos Humanos II', 4, 6, 21), " +
                "('ADM-3740', 'Produccion Y Operaciones I', 4, 6, 21), " +
                "('ADM-4580', 'Presupuesto Empresarial', 3, 6, 21), " +
                "('CON-3540', 'Contabilidad Administrativa', 3, 6, 21), " +
                "('DER-2610', 'Derecho Comercial', 3, 6, 21), " +
                "('MER-3250', 'Fun De Mercadotecnia Gerencial', 4, 6, 21), " +
                // Semestre 7
                "('ADM-2340', 'Administracion Tributaria', 3, 7, 21), " +
                "('ADM-3510', 'Administracion Financiera I', 4, 7, 21), " +
                "('ADM-3750', 'Produccion Y Operaciones II', 4, 7, 21), " +
                "('ADM-4250', 'Administracion De Seguros', 4, 7, 21), " +
                "('ADM-4510', 'Administracion Bancaria', 3, 7, 21), " +
                "('CON-4300', 'Nociones De Auditoria', 3, 7, 21), " +
                "('DER-2150', 'Fundamentos De Derecho Laboral', 3, 7, 21), " +
                // Semestre 8
                "('ADM-3550', 'Administracion Financiera II', 4, 8, 21), " +
                "('ADM-4110', 'Etica Profesional', 2, 8, 21), " +
                "('ADM-4260', 'Negocios Internacionales', 3, 8, 21), " +
                "('ADM-4740', 'Administracion De Proyectos', 4, 8, 21), " +
                "('ADM-5210', 'Sist De Informacion Gerencial', 3, 8, 21), " +
                "('ADM-5220', 'Seminario De Administracion', 3, 8, 21), " +
                "('ADM-7010', 'Tesis De Grado O Curso Equival', 8, 8, 21)");
        
         //  Licenciatura en Contabilidad (carrera_id = 22)
        stmt.execute("INSERT INTO pensum (codigo, nombre, creditos, semestre, carrera_id) VALUES " +
                "('ADM-1150', 'Teoria Administrativa', 4, 1, 22), " +
                "('BIO-0140', 'Biologia Basica', 3, 1, 22), " +
                "('DOP-1000', 'Orientacion Institucional', 1, 1, 22), " +
                "('EFS-0110', 'Educacion Fisica', 1, 1, 22), " +
                "('FIL-0110', 'Int A La Filosofia', 3, 1, 22), " +
                "('HIS-0110', 'Fund De His Social Dominicana', 3, 1, 22), " +
                "('LET-0110', 'Lengua Espanola Basica I', 3, 1, 22), " +
                "('MAT-0140', 'Matematica Basica', 4, 1, 22), " +
                "('SOC-0110', 'Int A Las Ciencias Sociales', 2, 1, 22), " +
                // Semestre 2
                "('ADM-1160', 'Proceso Administrativo', 4, 2, 22), " +
                "('CON-1190', 'Contabilidad General I', 5, 2, 22), " +
                "('DER-1160', 'Nociones De Derecho Civil', 2, 2, 22), " +
                "('FIS-0140', 'Fisica Basica', 4, 2, 22), " +
                "('INF-1030', 'Introduccion A La Informatica', 4, 2, 22), " +
                "('LET-0120', 'Lengua Espanola Basica II', 3, 2, 22), " +
                "('SOC-1100', 'Int A La Sociologia General', 4, 2, 22), " +
                // Semestre 3
                "('CON-1160', 'Contabilidad General II', 4, 3, 22), " +
                "('CON-1220', 'Laboratorio De Contabilidad I', 3, 3, 22), " +
                "('ECN-2110', 'Introduccion A La Economia', 4, 3, 22), " +
                "('EST-1110', 'Estadistica General', 4, 3, 22), " +
                "('MAT-2500', 'Analitica Y Calculo I', 5, 3, 22), " +
                "('QUI-0140', 'Quimica Basica', 4, 3, 22), " +
                // Semestre 4
                "('CON-2130', 'Contabilidad General III', 4, 4, 22), " +
                "('CON-2210', 'Contabilidad De Costos I', 4, 4, 22), " +
                "('CON-2220', 'Laboratorio De Contabilidad II', 3, 4, 22), " +
                "('DER-2610', 'Derecho Comercial', 3, 4, 22), " +
                "('EST-2230', 'Metodos Estadisticos II', 4, 4, 22), " +
                "('MAT-1400', 'Matematica Financiera', 5, 4, 22), " +
                // Semestre 5
                "('ADM-3360', 'Organizacion Y Metodos', 4, 5, 22), " +
                "('CON-2140', 'Contabilidad General IV', 4, 5, 22), " +
                "('CON-3210', 'Contabilidad De Costos II', 4, 5, 22), " +
                "('CON-3220', 'Sistemas De Contabilidad I', 3, 5, 22), " +
                "('DER-2150', 'Fundamentos De Derecho Laboral', 3, 5, 22), " +
                "('ECN-3370', 'Economia Dominicana', 4, 5, 22), " +
                "('MER-1100', 'Fundamentos De Mercadotecnia', 4, 5, 22), " +
                // Semestre 6
                "('ADM-3510', 'Administracion Financiera I', 4, 6, 22), " +
                "('ADM-4580', 'Presupuesto Empresarial', 3, 6, 22), " +
                "('CON-3150', 'Contabilidad Superior I', 4, 6, 22), " +
                "('CON-3230', 'Sistemas De Contabilidad II', 3, 6, 22), " +
                "('CON-3410', 'Contabilidad Impositiva I', 3, 6, 22), " +
                "('CON-4310', 'Auditoria I', 4, 6, 22), " +
                // Semestre 7
                "('ADM-3550', 'Administracion Financiera II', 4, 7, 22), " +
                "('CON-3160', 'Contabilidad Superior II', 4, 7, 22), " +
                "('CON-3420', 'Contabilidad Impositiva II', 3, 7, 22), " +
                "('CON-4110', 'Contabilidad Gubernamental', 3, 7, 22), " +
                "('CON-4320', 'Auditoria II', 4, 7, 22), " +
                "('CON-4510', 'Contabilidad Bancaria', 3, 7, 22), " +
                // Semestre 8
                "('ADM-4740', 'Administracion De Proyectos', 4, 8, 22), " +
                "('CON-4120', 'Contabilidad De Seguros', 3, 8, 22), " +
                "('CON-4210', 'Contabilidad Gerencial', 3, 8, 22), " +
                "('CON-4330', 'Auditoria III', 4, 8, 22), " +
                "('CON-4410', 'Etica Profesional Del Contador', 2, 8, 22), " +
                "('CON-5210', 'Seminario De Contabilidad', 3, 8, 22), " +
                "('CON-7010', 'Tesis De Grado O Curso Equival', 8, 8, 22)");
        
         //  Licenciatura en Economia (carrera_id = 23)
        stmt.execute("INSERT INTO pensum (codigo, nombre, creditos, semestre, carrera_id) VALUES " +
                "('DOP-1000', 'Orientacion Institucional', 1, 1, 23), " +
                "('ECN-2110', 'Introduccion A La Economia', 4, 1, 23), " +
                "('FIL-0110', 'Introduccion A La Filosofia', 3, 1, 23), " +
                "('FIS-0140', 'Fisica Basica', 4, 1, 23), " +
                "('HIS-0110', 'Fund De Hist Social Dominicana', 3, 1, 23), " +
                "('LET-0110', 'Lengua Espanola Basica I', 3, 1, 23), " +
                "('MAT-0140', 'Matematica Basica', 4, 1, 23), " +
                "('SOC-0110', 'Int A Las Ciencias Sociales', 2, 1, 23), " +
                // Semestre 2
                "('ADM-1130', 'Int A La Administracion', 4, 2, 23), " +
                "('BIO-0140', 'Biologia Basica', 3, 2, 23), " +
                "('CON-1190', 'Contabilidad General I', 5, 2, 23), " +
                "('ECN-1130', 'Historia Economica', 3, 2, 23), " +
                "('ECN-3150', 'Met Y Tec De Invest Economica', 3, 2, 23), " +
                "('EFS-0110', 'Educacion Fisica', 1, 2, 23), " +
                "('LET-0120', 'Lengua Espanola Basica II', 3, 2, 23), " +
                "('MAT-2500', 'Analitica Y Calculo I', 5, 2, 23), " +
                // Semestre 3
                "('CON-3240', 'Contabilidad De Costos', 4, 3, 23), " +
                "('ECN-2120', 'Microeconomia', 4, 3, 23), " +
                "('ECN-3160', 'Tecnica De Invest Economica', 3, 3, 23), " +
                "('ECN-3220', 'Economia Matematica', 4, 3, 23), " +
                "('EST-1110', 'Estadistica General', 4, 3, 23), " +
                "('QUI-0140', 'Quimica Basica', 3, 3, 23), " +
                "('SOC-1100', 'Int A La Sociologia General', 4, 3, 23), " +
                // Semestre 4
                "('ECN-2130', 'Macroeconomia I', 4, 4, 23), " +
                "('ECN-2170', 'His Del Pensamiento Economico', 3, 4, 23), " +
                "('ECN-4100', 'Microeconomia II', 4, 4, 23), " +
                "('EST-2230', 'Metodos Estadisticos II', 4, 4, 23), " +
                "('MER-1100', 'Fundamentos De Mercadotecnia', 4, 4, 23), " +
                // Semestre 5
                "('DER-1630', 'Derecho Aplicado A La Economia', 3, 5, 23), " +
                "('ECN-1200', 'Desarrollo Economico I', 3, 5, 23), " +
                "('ECN-1230', 'Cuentas Nacionales', 3, 5, 23), " +
                "('ECN-2140', 'Macroeconomia II', 4, 5, 23), " +
                "('ECN-4130', 'Economia Politica', 5, 5, 23), " +
                // Semestre 6
                "('ECN-1210', 'Desarrollo Economico II', 3, 6, 23), " +
                "('ECN-2180', 'Teoria Monetaria Y Bancaria', 4, 6, 23), " +
                "('ECN-2290', 'Programacion Y Planificacion', 4, 6, 23), " +
                "('ECN-3230', 'Econometria', 4, 6, 23), " +
                "('ECN-3370', 'Economia Dominicana', 4, 6, 23), " +
                "('GEO-1430', 'Geografia Economica', 3, 6, 23), " +
                // Semestre 7
                "('ECN-2330', 'Economia Agricola', 4, 7, 23), " +
                "('ECN-2350', 'Economia Industrial', 4, 7, 23), " +
                "('ECN-2360', 'Ecn Y Cooperac Internacional', 4, 7, 23), " +
                "('ECN-3360', 'Investigacion Ecn Operacional', 3, 7, 23), " +
                "('ECN-4310', 'Seminario De Investigacion', 3, 7, 23), " +
                "('EST-1420', 'Demografia', 3, 7, 23), " +
                // Semestre 8
                "('ECN-3190', 'Finanzas Publicas Y Pol Fiscal', 4, 8, 23), " +
                "('ECN-3260', 'Formul Y Evaluac De Proyectos', 4, 8, 23), " +
                "('ECN-3280', 'Politica Economica', 4, 8, 23), " +
                "('ECN-4380', 'Economia Dominicana Y Contemporanea', 3, 8, 23), " +
                "('ECN-7000', 'Tesis De Grado O Curso Equival', 8, 8, 23)");
        
         //  Licenciatura en Estadistica, mencion Informatica (carrera_id = 24)
        stmt.execute("INSERT INTO pensum (codigo, nombre, creditos, semestre, carrera_id) VALUES " +
                "('DOP-1000', 'Orientacion Institucional', 1, 1, 24), " +
                "('FIL-0110', 'Introduccion A La Filosofia', 3, 1, 24), " +
                "('FIS-0140', 'Fisica Basica', 4, 1, 24), " +
                "('HIS-0110', 'Fund De Hist Social Dominicana', 3, 1, 24), " +
                "('IDI-0230', 'Ingles Elemental I', 6, 1, 24), " +
                "('LET-0110', 'Lengua Espanola Basica I', 3, 1, 24), " +
                "('MAT-0140', 'Matematica Basica', 4, 1, 24), " +
                // Semestre 2
                "('BIO-0140', 'Biologia Basica', 3, 2, 24), " +
                "('ECN-1150', 'Principios De Economia', 4, 2, 24), " +
                "('EFS-0110', 'Educacion Fisica', 1, 2, 24), " +
                "('IDI-0140', 'Ingles Tecnico', 4, 2, 24), " +
                "('INF-1030', 'Introduccion A La Informatica', 4, 2, 24), " +
                "('LET-0120', 'Lengua Espanola Basica II', 3, 2, 24), " +
                "('SOC-0110', 'Int A Las Ciencias Sociales', 2, 2, 24), " +
                // Semestre 3
                "('EST-2110', 'Estadistica Basica', 5, 3, 24), " +
                "('EST-2450', 'Demografia I', 3, 3, 24), " +
                "('INF-1110', 'Informatica I', 4, 3, 24), " +
                "('MAT-2300', 'Algebra Superior', 5, 3, 24), " +
                "('QUI-0140', 'Quimica Basica', 3, 3, 24), " +
                "('SOC-1100', 'Int A La Sociologia General', 4, 3, 24), " +
                // Semestre 4
                "('ADM-1130', 'Int A La Administracion', 4, 4, 24), " +
                "('CON-1190', 'Contabilidad General I', 5, 4, 24), " +
                "('EST-3440', 'Demografia II', 3, 4, 24), " +
                "('INF-3240', 'Sistemas Operativos', 4, 4, 24), " +
                "('MAT-2510', 'Calculo Y Analitica I', 6, 4, 24), " +
                "('SOC-1270', 'Metodos Y Tec De Investigacion', 3, 4, 24), " +
                // Semestre 5
                "('EST-3540', 'Distrib De Probabilidades I', 4, 5, 24), " +
                "('INF-3150', 'Lenguajes De Programacion I', 4, 5, 24), " +
                "('INF-3260', 'Analisis De Sistemas', 4, 5, 24), " +
                "('INF-4280', 'Estruct De Datos Y Algoritmos', 4, 5, 24), " +
                "('MAT-3500', 'Calculo Y Geom Analitica II', 6, 5, 24), " +
                // Semestre 6
                "('EST-4510', 'Muestreo I', 3, 6, 24), " +
                "('EST-4530', 'Dosimacia De Hipotesis', 4, 6, 24), " +
                "('EST-4560', 'Distrib De Probabilidades II', 3, 6, 24), " +
                "('EST-5520', 'Regresion Y Correlacion', 4, 6, 24), " +
                "('INF-3160', 'Lenguajes De Programacion II', 4, 6, 24), " +
                "('INF-4230', 'Sistemas De Informacion', 4, 6, 24), " +
                // Semestre 7
                "('ADM-3610', 'Mercadotecnia General', 3, 7, 24), " +
                "('ECN-1330', 'Economia Dominicana', 4, 7, 24), " +
                "('EST-4540', 'Estadistica No Parametrica', 4, 7, 24), " +
                "('EST-5540', 'Diseno Experimental I', 4, 7, 24), " +
                "('EST-5560', 'Control Estadistico De Calidad', 4, 7, 24), " +
                "('INF-4290', 'Comunicacion De Datos', 4, 7, 24), " +
                // Semestre 8
                "('ADM-3330', 'Organizacion Y Metodos', 3, 8, 24), " +
                "('ECN-2230', 'Noc De Desarrollo Economico', 3, 8, 24), " +
                "('INF-3250', 'Manejo De Software Estadistico', 4, 8, 24), " +
                "('INF-4240', 'Base De Datos', 4, 8, 24), " +
                "('INF-5210', 'Proyecto De Informatica', 4, 8, 24), " +
                "('EST-7400', 'Tesis De Grado O Curso Equival', 8, 8, 24)");
        
        //  Licenciatura en Mercadotecnia (carrera_id = 25)
        stmt.execute("INSERT INTO pensum (codigo, nombre, creditos, semestre, carrera_id) VALUES " +
                "('BIO-0140', 'Biologia Basica', 3, 1, 25), " +
                "('DOP-1000', 'Orientacion Institucional', 1, 1, 25), " +
                "('EFS-0110', 'Educacion Fisica', 1, 1, 25), " +
                "('FIL-0110', 'Int A La Filosofia', 3, 1, 25), " +
                "('HIS-0110', 'Fund De His Social Dominicana', 3, 1, 25), " +
                "('LET-0110', 'Lengua Espanola Basica I', 3, 1, 25), " +
                "('MAT-0140', 'Matematica Basica', 4, 1, 25), " +
                "('MER-1110', 'Mercadotecnia I', 4, 1, 25), " +
                "('SOC-0110', 'Int A Las Ciencias Sociales', 2, 1, 25), " +
                // Semestre 2
                "('ADM-1150', 'Teoria Administrativa', 4, 2, 25), " +
                "('CON-1190', 'Contabilidad General I', 5, 2, 25), " +
                "('DER-1160', 'Nociones De Derecho Civil', 2, 2, 25), " +
                "('FIS-0140', 'Fisica Basica', 4, 2, 25), " +
                "('LET-0120', 'Lengua Espanola Basica II', 3, 2, 25), " +
                "('MER-1120', 'Mercadotecnia II', 4, 2, 25), " +
                "('SOC-1100', 'Int A La Sociologia General', 4, 2, 25), " +
                // Semestre 3
                "('ADM-1160', 'Proceso Administrativo', 4, 3, 25), " +
                "('CON-1160', 'Contabilidad General II', 4, 3, 25), " +
                "('ECN-2110', 'Introduccion A La Economia', 4, 3, 25), " +
                "('MAT-2500', 'Analitica Y Calculo I', 5, 3, 25), " +
                "('MER-1130', 'Mercadotecnia III', 4, 3, 25), " +
                "('QUI-0140', 'Quimica Basica', 4, 3, 25), " +
                // Semestre 4
                "('CON-2230', 'Fund De Contabilidad De Costos', 4, 4, 25), " +
                "('ECN-2230', 'Noc De Desarrollo Economico', 3, 4, 25), " +
                "('EST-1110', 'Estadistica General', 4, 4, 25), " +
                "('INF-1030', 'Introduccion A La Informatica', 4, 4, 25), " +
                "('MAT-1400', 'Matematica Financiera', 5, 4, 25), " +
                "('MER-1200', 'Ventas I', 3, 4, 25), " +
                "('MER-2200', 'Estrategia Publicitaria I', 3, 4, 25), " +
                // Semestre 5
                "('ADM-3760', 'Fund Produccion Y Operaciones', 4, 5, 25), " +
                "('ECN-3370', 'Economia Dominicana', 4, 5, 25), " +
                "('EST-2230', 'Metodos Estadisticos II', 4, 5, 25), " +
                "('MER-1210', 'Ventas II', 3, 5, 25), " +
                "('MER-2210', 'Estrategia Publicitaria II', 3, 5, 25), " +
                "('MER-3200', 'Investigacion De Mercados I', 4, 5, 25), " +
                "('SOC-1270', 'Metodos Y Tec De Investigacion', 3, 5, 25), " +
                // Semestre 6
                "('ADM-2100', 'Administracion Turistica', 3, 6, 25), " +
                "('ADM-2440', 'Fund Adm Recursos Humanos', 3, 6, 25), " +
                "('ADM-3360', 'Organizacion Y Metodos', 4, 6, 25), " +
                "('DER-2610', 'Derecho Comercial', 3, 6, 25), " +
                "('MER-1220', 'Programacion Y Pres De Ventas', 3, 6, 25), " +
                "('MER-1230', 'Promocion De Ventas', 3, 6, 25), " +
                "('MER-3210', 'Investigacion De Mercados II', 4, 6, 25), " +
                // Semestre 7
                "('ADM-3540', 'Administracion Financiera', 4, 7, 25), " +
                "('MER-1240', 'Medios Audiovisuales En Mer', 3, 7, 25), " +
                "('MER-1250', 'Canales De Distribucion', 3, 7, 25), " +
                "('MER-3220', 'Mercadotecnia Agropecuaria', 3, 7, 25), " +
                "('MER-3230', 'Desarrollo Del Producto', 3, 7, 25), " +
                "('MER-4200', 'Conducta Del Consumidor', 3, 7, 25), " +
                // Semestre 8
                "('ADM-4110', 'Etica Profesional', 2, 8, 25), " +
                "('ADM-5210', 'Sist De Informacion Gerencial', 3, 8, 25), " +
                "('MER-1260', 'Diseno De Campana', 3, 8, 25), " +
                "('MER-3240', 'Mercadotecnia Internacional', 3, 8, 25), " +
                "('MER-3260', 'Mercadotecnia Gerencial', 3, 8, 25), " +
                "('MER-5200', 'Seminario De Mercadotecnia', 3, 8, 25), " +
                "('MER-6200', 'Tesis De Grado O Curso Equival', 8, 8, 25)");
        
        //  Licenciatura en Sociologia (carrera_id = 26)
        stmt.execute("INSERT INTO pensum (codigo, nombre, creditos, semestre, carrera_id) VALUES " +
                "('ADM-1130', 'Int A La Administracion', 4, 1, 26), " +
                "('BIO-0140', 'Biologia Basica', 3, 1, 26), " +
                "('DOP-1000', 'Orientacion Institucional', 1, 1, 26), " +
                "('EFS-0110', 'Educacion Fisica', 1, 1, 26), " +
                "('FIL-0110', 'Int A La Filosofia', 3, 1, 26), " +
                "('FIS-0140', 'Fisica Basica', 4, 1, 26), " +
                "('HIS-0110', 'Fund De His Social Dominicana', 3, 1, 26), " +
                "('LET-0110', 'Lengua Espanola Basica I', 3, 1, 26), " +
                "('SOC-0110', 'Int A Las Ciencias Sociales', 2, 1, 26), " +
                // Semestre 2
                "('ECN-2110', 'Introduccion A La Economia', 4, 2, 26), " +
                "('EST-1110', 'Estadistica General', 4, 2, 26), " +
                "('LET-0120', 'Lengua Espanola Basica II', 3, 2, 26), " +
                "('MAT-0140', 'Matematica Basica', 4, 2, 26), " +
                "('PSI-1050', 'Int A La Psicologia', 3, 2, 26), " +
                "('QUI-0140', 'Quimica Basica', 3, 2, 26), " +
                "('SOC-1100', 'Int A La Sociologia General', 4, 2, 26), " +
                // Semestre 3
                "('ANT-1110', 'Antropologia General', 4, 3, 26), " +
                "('ECN-2230', 'Noc De Desarrollo Economico', 3, 3, 26), " +
                "('EST-2230', 'Metodos Estadisticos II', 4, 3, 26), " +
                "('FIL-1210', 'Logica Formal', 3, 3, 26), " +
                "('SOC-1120', 'Historia Social Dominicana', 4, 3, 26), " +
                "('SOC-1200', 'Teoria Sociologica I', 4, 3, 26), " +
                // Semestre 4
                "('DER-1610', 'Introd A Las Ciencias Juridicas', 3, 4, 26), " +
                "('EST-1420', 'Demografia', 3, 4, 26), " +
                "('PSI-1130', 'Psicologia Social', 3, 4, 26), " +
                "('SOC-1210', 'Teoria Sociologica II', 4, 4, 26), " +
                "('SOC-1270', 'Metodos Y Tec De Investigacion', 3, 4, 26), " +
                "('SOC-2110', 'Sociologia Politica', 3, 4, 26), " +
                // Semestre 5
                "('ECN-3370', 'Economia Dominicana', 4, 5, 26), " +
                "('SOC-1220', 'Teoria Sociologica III', 4, 5, 26), " +
                "('SOC-2120', 'Sociologia Urbana', 3, 5, 26), " +
                "('SOC-2130', 'Sociologia Rural', 3, 5, 26), " +
                "('SOC-2210', 'Tec De Investigacion Social I', 4, 5, 26), " +
                "('SOC-3110', 'Sociologia Del Desarrollo', 4, 5, 26), " +
                // Semestre 6
                "('SOC-1230', 'Teoria Sociologica IV', 4, 6, 26), " +
                "('SOC-2140', 'Sociologia De La Educacion', 3, 6, 26), " +
                "('SOC-2150', 'Sociologia Del Trabajo', 3, 6, 26), " +
                "('SOC-2220', 'Tec De Investigacion Social II', 4, 6, 26), " +
                "('SOC-3120', 'Planificacion Social', 4, 6, 26), " +
                "('SOC-3130', 'Estructura Social Dominicana', 4, 6, 26), " +
                // Semestre 7
                "('SOC-2160', 'Sociologia De La Cultura', 3, 7, 26), " +
                "('SOC-2170', 'Sociologia De La Familia', 3, 7, 26), " +
                "('SOC-3140', 'Movimientos Sociales', 3, 7, 26), " +
                "('SOC-3210', 'Taller De Investigacion I', 4, 7, 26), " +
                "('SOC-4110', 'Pensamiento Social Latinoamer', 3, 7, 26), " +
                // Semestre 8
                "('SOC-3150', 'Politicas Publicas Y Sociales', 3, 8, 26), " +
                "('SOC-3220', 'Taller De Investigacion II', 4, 8, 26), " +
                "('SOC-4120', 'Sociologia Contemporanea', 4, 8, 26), " +
                "('SOC-4210', 'Seminario De Sociologia', 3, 8, 26), " +
                "('SOC-7010', 'Tesis De Grado O Curso Equival', 8, 8, 26)");
        
        //  Licenciatura en Turismo y Hoteleria (carrera_id = 27)
        stmt.execute("INSERT INTO pensum (codigo, nombre, creditos, semestre, carrera_id) VALUES " +
                "('DOP-1000', 'Orientacion Institucional', 1, 1, 27), " +
                "('EFS-0110', 'Educacion Fisica', 1, 1, 27), " +
                "('FIL-0110', 'Int A La Filosofia', 3, 1, 27), " +
                "('HIS-0110', 'Fund De His Social Dominicana', 3, 1, 27), " +
                "('IDI-0230', 'Ingles Elemental I', 6, 1, 27), " +
                "('LET-0110', 'Lengua Espanola Basica I', 3, 1, 27), " +
                "('MAT-0140', 'Matematica Basica', 4, 1, 27), " +
                "('SOC-0110', 'Int A Las Ciencias Sociales', 2, 1, 27), " +
                "('TUR-1110', 'Introduccion Al Turismo', 4, 1, 27), " +
                // Semestre 2
                "('ADM-1150', 'Teoria Administrativa', 4, 2, 27), " +
                "('BIO-0140', 'Biologia Basica', 3, 2, 27), " +
                "('CON-1190', 'Contabilidad General I', 5, 2, 27), " +
                "('FIS-0140', 'Fisica Basica', 4, 2, 27), " +
                "('IDI-0240', 'Ingles Elemental II', 6, 2, 27), " +
                "('LET-0120', 'Lengua Espanola Basica II', 3, 2, 27), " +
                "('TUR-1120', 'Geografia Turistica Mundial', 3, 2, 27), " +
                "('TUR-1130', 'Patrimonio Cultural Dominicano', 3, 2, 27), " +
                // Semestre 3
                "('ADM-1160', 'Proceso Administrativo', 4, 3, 27), " +
                "('CON-1160', 'Contabilidad General II', 4, 3, 27), " +
                "('ECN-2110', 'Introduccion A La Economia', 4, 3, 27), " +
                "('IDI-1250', 'Ingles Intermedio I', 5, 3, 27), " +
                "('INF-1030', 'Introduccion A La Informatica', 4, 3, 27), " +
                "('QUI-0140', 'Quimica Basica', 3, 3, 27), " +
                "('TUR-2110', 'Geografia Turistica Dominicana', 3, 3, 27), " +
                // Semestre 4
                "('CON-2230', 'Fund De Contabilidad De Costos', 4, 4, 27), " +
                "('EST-1110', 'Estadistica General', 4, 4, 27), " +
                "('IDI-1260', 'Ingles Intermedio II', 5, 4, 27), " +
                "('MER-1100', 'Fundamentos De Mercadotecnia', 4, 4, 27), " +
                "('SOC-1270', 'Metodos Y Tec De Investigacion', 3, 4, 27), " +
                "('TUR-2120', 'Administracion Hotelera I', 4, 4, 27), " +
                "('TUR-2130', 'Alimentos Y Bebidas I', 4, 4, 27), " +
                "('TUR-2140', 'Agencias De Viajes Y Tours', 5, 4, 27), " +
                // Semestre 5
                "('ADM-2450', 'Adm De Recursos Humanos I', 4, 5, 27), " +
                "('ECN-3370', 'Economia Dominicana', 4, 5, 27), " +
                "('IDI-2270', 'Ingles Avanzado I', 4, 5, 27), " +
                "('MAT-1400', 'Matematica Financiera', 5, 5, 27), " +
                "('TUR-3110', 'Administracion Hotelera II', 4, 5, 27), " +
                "('TUR-3120', 'Alimentos Y Bebidas II', 4, 5, 27), " +
                "('TUR-3130', 'Transporte Aereo Y Maritimo', 3, 5, 27), " +
                "('TUR-3140', 'Ecoturismo Y Medio Ambiente', 4, 5, 27), " +
                // Semestre 6
                "('ADM-3510', 'Administracion Financiera I', 4, 6, 27), " +
                "('DER-2150', 'Fundamentos De Derecho Laboral', 3, 6, 27), " +
                "('IDI-2280', 'Ingles Avanzado II', 4, 6, 27), " +
                "('MER-3250', 'Fun De Mercadotecnia Gerencial', 4, 6, 27), " +
                "('TUR-3150', 'Contabilidad Hotelera', 4, 6, 27), " +
                "('TUR-3160', 'Organizacion De Eventos', 4, 6, 27), " +
                "('TUR-3170', 'Animacion Y Recreacion Turist', 4, 6, 27), " +
                "('TUR-3180', 'Legislacion Turistica', 4, 6, 27), " +
                // Semestre 7
                "('ADM-4740', 'Administracion De Proyectos', 4, 7, 27), " +
                "('IDI-3110', 'Frances Basico I', 4, 7, 27), " +
                "('TUR-4110', 'Planificacion Turistica', 4, 7, 27), " +
                "('TUR-4120', 'Mercadeo Turistico Y Hotelero', 4, 7, 27), " +
                "('TUR-4130', 'Calidad En Servicios Turisticos', 3, 7, 27), " +
                "('TUR-4140', 'Sistemas Computarizados Hotel', 4, 7, 27), " +
                "('TUR-4150', 'Pasantia Hotelera Y Turistica', 4, 7, 27), " +
                // Semestre 8
                "('ADM-4110', 'Etica Profesional', 2, 8, 27), " +
                "('IDI-3120', 'Frances Basico II', 4, 8, 27), " +
                "('TUR-4160', 'Desarrollo Sostenible Turismo', 4, 8, 27), " +
                "('TUR-4170', 'Gestion De Destinos Turisticos', 4, 8, 27), " +
                "('TUR-4180', 'Seminario De Turismo', 5, 8, 27), " +
                "('TUR-7010', 'Tesis De Grado O Curso Equival', 8, 8, 27)");
        
        
        // Inicia la Facultad de Ciencias Juridicas y Politicas
        
        //  Licenciatura en Ciencias Politicas (carrera_id = 28)
        stmt.execute("INSERT INTO pensum (codigo, nombre, creditos, semestre, carrera_id) VALUES " +
                "('BIO-0140', 'Biologia Basica', 3, 1, 28), " +
                "('DER-1110', 'Introduccion Al Derecho I', 3, 1, 28), " +
                "('DOP-1000', 'Orientacion Institucional', 1, 1, 28), " +
                "('EFS-0110', 'Educacion Fisica', 1, 1, 28), " +
                "('FIL-0110', 'Int A La Filosofia', 3, 1, 28), " +
                "('HIS-0110', 'Fund De His Social Dominicana', 3, 1, 28), " +
                "('LET-0110', 'Lengua Espanola Basica I', 3, 1, 28), " +
                "('MAT-0140', 'Matematica Basica', 4, 1, 28), " +
                "('SOC-0110', 'Int A Las Ciencias Sociales', 2, 1, 28), " +
                // Semestre 2
                "('DER-1120', 'Introduccion Al Derecho II', 3, 2, 28), " +
                "('DER-1210', 'Derecho Constitucional I', 3, 2, 28), " +
                "('ECN-2110', 'Introduccion A La Economia', 4, 2, 28), " +
                "('FIS-0140', 'Fisica Basica', 4, 2, 28), " +
                "('LET-0120', 'Lengua Espanola Basica II', 3, 2, 28), " +
                "('POL-1110', 'Introd A Las Ciencias Politicas', 4, 2, 28), " +
                "('SOC-1100', 'Int A La Sociologia General', 4, 2, 28), " +
                // Semestre 3
                "('DER-1220', 'Derecho Constitucional II', 3, 3, 28), " +
                "('EST-1110', 'Estadistica General', 4, 3, 28), " +
                "('POL-1120', 'Historia De Las Ideas Politicas I', 4, 3, 28), " +
                "('POL-1210', 'Teoria Del Estado', 4, 3, 28), " +
                "('QUI-0140', 'Quimica Basica', 3, 3, 28), " +
                "('SOC-1120', 'Historia Social Dominicana', 4, 3, 28), " +
                // Semestre 4
                "('DER-1310', 'Derecho Internacional Publico I', 3, 4, 28), " +
                "('ECN-3370', 'Economia Dominicana', 4, 4, 28), " +
                "('POL-1130', 'Historia De Las Ideas Politicas II', 4, 4, 28), " +
                "('POL-2110', 'Sistemas Politicos Comparados', 4, 4, 28), " +
                "('POL-2210', 'Partidos Politicos Y Sist Elect', 3, 4, 28), " +
                "('SOC-1270', 'Metodos Y Tec De Investigacion', 3, 4, 28), " +
                // Semestre 5
                "('ADM-2310', 'Organizacion Y Adm Del Estado', 3, 5, 28), " +
                "('DER-1320', 'Derecho Internacional Publico II', 3, 5, 28), " +
                "('DER-2410', 'Derecho Administrativo I', 3, 5, 28), " +
                "('POL-2120', 'Historia Politica Dominicana', 4, 5, 28), " +
                "('POL-2220', 'Opinion Publica Y Com Politica', 3, 5, 28), " +
                "('SOC-2110', 'Sociologia Politica', 3, 5, 28), " +
                // Semestre 6
                "('DER-2420', 'Derecho Administrativo II', 3, 6, 28), " +
                "('POL-3110', 'Relaciones Internacionales I', 4, 6, 28), " +
                "('POL-3120', 'Politicas Publicas I', 4, 6, 28), " +
                "('POL-3210', 'Geopolitica', 3, 6, 28), " +
                "('POL-3220', 'Metodologia Inv Politica', 4, 6, 28), " +
                "('SOC-3130', 'Estructura Social Dominicana', 4, 6, 28), " +
                // Semestre 7
                "('DER-3310', 'Derechos Humanos', 3, 7, 28), " +
                "('POL-3130', 'Relaciones Internacionales II', 4, 7, 28), " +
                "('POL-3140', 'Politicas Publicas II', 4, 7, 28), " +
                "('POL-4110', 'Gobernabilidad Y Democracia', 3, 7, 28), " +
                "('POL-4120', 'Politica Exterior Dominicana', 3, 7, 28), " +
                "('POL-4210', 'Gestion Municipal Y Local', 3, 7, 28), " +
                // Semestre 8
                "('POL-4130', 'Negociacion Y Resol Conflictos', 3, 8, 28), " +
                "('POL-4220', 'Integracion Regional', 3, 8, 28), " +
                "('POL-4230', 'Seminario De Ciencias Politicas', 4, 8, 28), " +
                "('POL-7010', 'Tesis De Grado O Curso Equival', 8, 8, 28)");
        
        //  Licenciatura en Derecho (carrera_id = 29)
        stmt.execute("INSERT INTO pensum (codigo, nombre, creditos, semestre, carrera_id) VALUES " +
                "('BIO-0140', 'Biologia Basica', 3, 1, 29), " +
                "('DER-1110', 'Introduccion Al Derecho I', 3, 1, 29), " +
                "('DOP-1000', 'Orientacion Institucional', 1, 1, 29), " +
                "('EFS-0110', 'Educacion Fisica', 1, 1, 29), " +
                "('FIL-0110', 'Int A La Filosofia', 3, 1, 29), " +
                "('HIS-0110', 'Fund De His Social Dominicana', 3, 1, 29), " +
                "('LET-0110', 'Lengua Espanola Basica I', 3, 1, 29), " +
                "('MAT-0140', 'Matematica Basica', 4, 1, 29), " +
                "('SOC-0110', 'Int A Las Ciencias Sociales', 2, 1, 29), " +
                // Semestre 2
                "('DER-1120', 'Introduccion Al Derecho II', 3, 2, 29), " +
                "('DER-1130', 'Derecho Romano I', 3, 2, 29), " +
                "('DER-1210', 'Derecho Constitucional I', 3, 2, 29), " +
                "('ECN-2110', 'Introduccion A La Economia', 4, 2, 29), " +
                "('FIS-0140', 'Fisica Basica', 4, 2, 29), " +
                "('LET-0120', 'Lengua Espanola Basica II', 3, 2, 29), " +
                "('SOC-1100', 'Int A La Sociologia General', 4, 2, 29), " +
                // Semestre 3
                "('DER-1140', 'Derecho Romano II', 3, 3, 29), " +
                "('DER-1150', 'Derecho Civil I', 4, 3, 29), " +
                "('DER-1220', 'Derecho Constitucional II', 3, 3, 29), " +
                "('DER-1510', 'Derecho Penal General I', 4, 3, 29), " +
                "('POL-1110', 'Introd A Las Ciencias Politicas', 4, 3, 29), " +
                "('QUI-0140', 'Quimica Basica', 3, 3, 29), " +
                // Semestre 4
                "('DER-1170', 'Derecho Civil II', 4, 4, 29), " +
                "('DER-1310', 'Derecho Internacional Publico I', 3, 4, 29), " +
                "('DER-1520', 'Derecho Penal General II', 4, 4, 29), " +
                "('DER-2110', 'Derecho Procesal Civil I', 4, 4, 29), " +
                "('DER-2410', 'Derecho Administrativo I', 3, 4, 29), " +
                "('SOC-1270', 'Metodos Y Tec De Investigacion', 3, 4, 29), " +
                // Semestre 5
                "('DER-2120', 'Derecho Procesal Civil II', 4, 5, 29), " +
                "('DER-2150', 'Fundamentos De Derecho Laboral', 3, 5, 29), " +
                "('DER-2170', 'Derecho Civil III', 4, 5, 29), " +
                "('DER-2420', 'Derecho Administrativo II', 3, 5, 29), " +
                "('DER-2510', 'Derecho Penal Especial I', 4, 5, 29), " +
                "('DER-2610', 'Derecho Comercial', 3, 5, 29), " +
                // Semestre 6
                "('DER-2180', 'Derecho Civil IV', 4, 6, 29), " +
                "('DER-2520', 'Derecho Penal Especial II', 4, 6, 29), " +
                "('DER-2620', 'Derecho Comercial II', 3, 6, 29), " +
                "('DER-3110', 'Derecho Procesal Penal I', 4, 6, 29), " +
                "('DER-3150', 'Derecho Procesal Laboral', 3, 6, 29), " +
                "('DER-3210', 'Derecho Inmobiliario I', 3, 6, 29), " +
                // Semestre 7
                "('DER-3120', 'Derecho Procesal Penal II', 4, 7, 29), " +
                "('DER-3220', 'Derecho Inmobiliario II', 3, 7, 29), " +
                "('DER-3310', 'Derechos Humanos', 3, 7, 29), " +
                "('DER-3410', 'Derecho Tributario', 3, 7, 29), " +
                "('DER-3510', 'Derecho Internacional Privado I', 3, 7, 29), " +
                "('DER-3610', 'Criminologia', 3, 7, 29), " +
                // Semestre 8
                "('DER-3520', 'Derecho Internacional Privado II', 3, 8, 29), " +
                "('DER-4110', 'Filosofia Del Derecho', 3, 8, 29), " +
                "('DER-4120', 'Etica Juridica', 2, 8, 29), " +
                "('DER-4130', 'Practica Forense', 4, 8, 29), " +
                "('DER-7010', 'Tesis De Grado O Curso Equival', 8, 8, 29)");
        
        // Inicio de la Facultad de Ciencias de la Educacion
        
        //  Licenciatura en Ciencias de la Informacion (carrera_id = 30)
        stmt.execute("INSERT INTO pensum (codigo, nombre, creditos, semestre, carrera_id) VALUES " +
                "('BIO-0140', 'Biologia Basica', 3, 1, 30), " +
                "('DOP-1000', 'Orientacion Institucional', 1, 1, 30), " +
                "('EFS-0110', 'Educacion Fisica', 1, 1, 30), " +
                "('FIL-0110', 'Int A La Filosofia', 3, 1, 30), " +
                "('FIS-0140', 'Fisica Basica', 4, 1, 30), " +
                "('HIS-0110', 'Fund De His Social Dominicana', 3, 1, 30), " +
                "('LET-0110', 'Lengua Espanola Basica I', 3, 1, 30), " +
                "('MAT-0140', 'Matematica Basica', 4, 1, 30), " +
                "('SOC-0110', 'Int A Las Ciencias Sociales', 2, 1, 30), " +
                // Semestre 2
                "('BIB-1110', 'Introd Ciencias Informacion', 3, 2, 30), " +
                "('BIB-1120', 'Historia Del Libro Y Bibliot', 3, 2, 30), " +
                "('INF-1030', 'Introduccion A La Informatica', 4, 2, 30), " +
                "('LET-0120', 'Lengua Espanola Basica II', 3, 2, 30), " +
                "('PSI-1050', 'Int A La Psicologia', 3, 2, 30), " +
                "('QUI-0140', 'Quimica Basica', 3, 2, 30), " +
                "('SOC-1100', 'Int A La Sociologia General', 4, 2, 30), " +
                // Semestre 3
                "('ADM-1130', 'Int A La Administracion', 4, 3, 30), " +
                "('BIB-1210', 'Catalogacion I', 4, 3, 30), " +
                "('BIB-1220', 'Clasificacion I', 4, 3, 30), " +
                "('BIB-1310', 'Seleccion Y Adquisicion', 3, 3, 30), " +
                "('EST-1110', 'Estadistica General', 4, 3, 30), " +
                "('SOC-1270', 'Metodos Y Tec De Investigacion', 3, 3, 30), " +
                // Semestre 4
                "('BIB-2210', 'Catalogacion II', 4, 4, 30), " +
                "('BIB-2220', 'Clasificacion II', 4, 4, 30), " +
                "('BIB-2310', 'Servicios De Referencia I', 3, 4, 30), " +
                "('BIB-2410', 'Archivistica General', 3, 4, 30), " +
                "('PED-1110', 'Fundamentos De La Educacion', 3, 4, 30), " +
                "('TEG-1110', 'Tecnologia Educativa I', 3, 4, 30), " +
                // Semestre 5
                "('BIB-2320', 'Servicios De Referencia II', 3, 5, 30), " +
                "('BIB-3110', 'Documentacion E Informacion', 3, 5, 30), " +
                "('BIB-3210', 'Indizacion Y Resumenes', 3, 5, 30), " +
                "('BIB-3410', 'Gestion Documental Y Archivos', 4, 5, 30), " +
                "('INF-4240', 'Base De Datos', 4, 5, 30), " +
                "('PED-2110', 'Didactica General', 4, 5, 30), " +
                // Semestre 6
                "('BIB-3120', 'Automatizacion De Bibliotecas', 4, 6, 30), " +
                "('BIB-3310', 'Usuarios De La Informacion', 3, 6, 30), " +
                "('BIB-4110', 'Adm Unidades De Informacion', 4, 6, 30), " +
                "('BIB-4210', 'Bibliotecologia Escolar Y Univ', 3, 6, 30), " +
                "('BIB-4410', 'Conservacion Y Restauracion', 3, 6, 30), " +
                "('PED-3110', 'Metodologia Inv Educativa', 3, 6, 30), " +
                // Semestre 7
                "('BIB-4120', 'Redes Y Sistemas Informacion', 4, 7, 30), " +
                "('BIB-4310', 'Bibliotecas Digitales', 4, 7, 30), " +
                "('BIB-4320', 'Mercadeo Servicios Informacion', 3, 7, 30), " +
                "('BIB-4510', 'Practica Profesional I', 4, 7, 30), " +
                "('PED-4110', 'Etica Profesional Docente', 2, 7, 30), " +
                // Semestre 8
                "('BIB-4520', 'Practica Profesional II', 4, 8, 30), " +
                "('BIB-5110', 'Seminario Ciencias Informacion', 3, 8, 30), " +
                "('BIB-7010', 'Tesis De Grado O Curso Equival', 8, 8, 30)");

        
        // Licenciatura en Educacion Fisica (carrera_id = 31)
        stmt.execute("INSERT INTO pensum (codigo, nombre, creditos, semestre, carrera_id) VALUES " +
                "('BIO-0140', 'Biologia Basica', 3, 1, 31), " +
                "('DOP-1000', 'Orientacion Institucional', 1, 1, 31), " +
                "('EFS-1110', 'Historia De La Educacion Fisica', 3, 1, 31), " +
                "('FIL-0110', 'Int A La Filosofia', 3, 1, 31), " +
                "('FIS-0140', 'Fisica Basica', 4, 1, 31), " +
                "('HIS-0110', 'Fund De His Social Dominicana', 3, 1, 31), " +
                "('LET-0110', 'Lengua Espanola Basica I', 3, 1, 31), " +
                "('MAT-0140', 'Matematica Basica', 4, 1, 31), " +
                "('SOC-0110', 'Int A Las Ciencias Sociales', 2, 1, 31), " +
                // Semestre 2
                "('CFI-1110', 'Anatomia Humana', 4, 2, 31), " +
                "('EFS-1120', 'Gimnasia Basica', 3, 2, 31), " +
                "('EFS-1130', 'Atletismo I', 3, 2, 31), " +
                "('LET-0120', 'Lengua Espanola Basica II', 3, 2, 31), " +
                "('PED-1110', 'Fundamentos De La Educacion', 3, 2, 31), " +
                "('PSI-1050', 'Int A La Psicologia', 3, 2, 31), " +
                "('QUI-0140', 'Quimica Basica', 3, 2, 31), " +
                // Semestre 3
                "('CFI-1210', 'Fisiologia Humana', 4, 3, 31), " +
                "('EFS-1140', 'Atletismo II', 3, 3, 31), " +
                "('EFS-1210', 'Natacion I', 3, 3, 31), " +
                "('EFS-1220', 'Baloncesto I', 3, 3, 31), " +
                "('PED-2110', 'Didactica General', 4, 3, 31), " +
                "('PSI-2110', 'Psicologia Del Desarrollo', 3, 3, 31), " +
                // Semestre 4
                "('EFS-2110', 'Fisiologia Del Ejercicio', 4, 4, 31), " +
                "('EFS-2120', 'Voleibol I', 3, 4, 31), " +
                "('EFS-2130', 'Futbol I', 3, 4, 31), " +
                "('EFS-2210', 'Natacion II', 3, 4, 31), " +
                "('PED-2210', 'Curriculo Escolar', 3, 4, 31), " +
                "('PSI-2120', 'Psicologia Del Aprendizaje', 3, 4, 31), " +
                // Semestre 5
                "('EFS-2220', 'Baloncesto II', 3, 5, 31), " +
                "('EFS-2230', 'Beisbol Y Softbol', 3, 5, 31), " +
                "('EFS-3110', 'Biomecanica Deportiva', 3, 5, 31), " +
                "('EFS-3120', 'Didactica De La Educ Fisica I', 4, 5, 31), " +
                "('EFS-3130', 'Recreacion Y Juegos', 3, 5, 31), " +
                "('PED-3120', 'Evaluacion Educativa', 3, 5, 31), " +
                // Semestre 6
                "('EFS-3140', 'Didactica De La Educ Fisica II', 4, 6, 31), " +
                "('EFS-3210', 'Voleibol II', 3, 6, 31), " +
                "('EFS-3220', 'Futbol II', 3, 6, 31), " +
                "('EFS-3230', 'Medicina Deportiva Y Primeros Aux', 3, 6, 31), " +
                "('EFS-3240', 'Teoria Entrenamiento Deportivo', 4, 6, 31), " +
                "('PED-3110', 'Metodologia Inv Educativa', 3, 6, 31), " +
                // Semestre 7
                "('EFS-4110', 'Educacion Fisica Adaptada', 3, 7, 31), " +
                "('EFS-4120', 'Adm Y Organizacion Deportiva', 3, 7, 31), " +
                "('EFS-4130', 'Practica Docente I', 4, 7, 31), " +
                "('EFS-4210', 'Artes Marciales Y Deportes Comb', 3, 7, 31), " +
                "('PED-4110', 'Etica Profesional Docente', 2, 7, 31), " +
                // Semestre 8
                "('EFS-4140', 'Practica Docente II', 5, 8, 31), " +
                "('EFS-4220', 'Seminario De Educacion Fisica', 3, 8, 31), " +
                "('EFS-7010', 'Tesis De Grado O Curso Equival', 8, 8, 31)");

        
        //  Licenciatura en Educacion Basica (carrera_id = 32)
        stmt.execute("INSERT INTO pensum (codigo, nombre, creditos, semestre, carrera_id) VALUES " +
                "('BIO-0140', 'Biologia Basica', 3, 1, 32), " +
                "('DOP-1000', 'Orientacion Institucional', 1, 1, 32), " +
                "('EFS-0110', 'Educacion Fisica', 1, 1, 32), " +
                "('FIL-0110', 'Int A La Filosofia', 3, 1, 32), " +
                "('FIS-0140', 'Fisica Basica', 4, 1, 32), " +
                "('HIS-0110', 'Fund De His Social Dominicana', 3, 1, 32), " +
                "('LET-0110', 'Lengua Espanola Basica I', 3, 1, 32), " +
                "('MAT-0140', 'Matematica Basica', 4, 1, 32), " +
                "('SOC-0110', 'Int A Las Ciencias Sociales', 2, 1, 32), " +
                // Semestre 2
                "('INF-1030', 'Introduccion A La Informatica', 4, 2, 32), " +
                "('LET-0120', 'Lengua Espanola Basica II', 3, 2, 32), " +
                "('PED-1110', 'Fundamentos De La Educacion', 3, 2, 32), " +
                "('PED-1120', 'Historia De La Educacion', 3, 2, 32), " +
                "('PSI-1050', 'Int A La Psicologia', 3, 2, 32), " +
                "('QUI-0140', 'Quimica Basica', 3, 2, 32), " +
                "('SOC-1100', 'Int A La Sociologia General', 4, 2, 32), " +
                // Semestre 3
                "('FIB-1110', 'Didactica Lengua Espanola Bas I', 4, 3, 32), " +
                "('FIB-1120', 'Didactica De La Matematica Bas I', 4, 3, 32), " +
                "('PED-2110', 'Didactica General', 4, 3, 32), " +
                "('PED-2210', 'Curriculo Escolar', 3, 3, 32), " +
                "('PSI-2110', 'Psicologia Del Desarrollo', 3, 3, 32), " +
                "('TEG-1110', 'Tecnologia Educativa I', 3, 3, 32), " +
                // Semestre 4
                "('FIB-2110', 'Didactica Lengua Espanola Bas II', 4, 4, 32), " +
                "('FIB-2120', 'Didactica De La Matematica Bas II', 4, 4, 32), " +
                "('FIB-2130', 'Didactica Ciencias Sociales I', 4, 4, 32), " +
                "('FIB-2140', 'Didactica Ciencias Naturaleza I', 4, 4, 32), " +
                "('PSI-2120', 'Psicologia Del Aprendizaje', 3, 4, 32), " +
                "('SOC-2140', 'Sociologia De La Educacion', 3, 4, 32), " +
                // Semestre 5
                "('FIB-3110', 'Didactica Ciencias Sociales II', 4, 5, 32), " +
                "('FIB-3120', 'Didactica Ciencias Naturaleza II', 4, 5, 32), " +
                "('FIB-3130', 'Literatura Infantil Y Juvenil', 3, 5, 32), " +
                "('FIB-3140', 'Educacion Artistica En Basica', 3, 5, 32), " +
                "('PED-3120', 'Evaluacion Educativa', 3, 5, 32), " +
                "('PED-3130', 'Planificacion Educativa', 3, 5, 32), " +
                // Semestre 6
                "('FIB-3210', 'Lectoescritura Inicial', 4, 6, 32), " +
                "('FIB-3220', 'Dificultades De Aprendizaje', 3, 6, 32), " +
                "('FIB-3230', 'Educacion Civica Y Valores', 3, 6, 32), " +
                "('PED-3110', 'Metodologia Inv Educativa', 3, 6, 32), " +
                "('PED-3210', 'Gestion Y Supervision Escolar', 3, 6, 32), " +
                "('PED-3220', 'Orientacion Educativa', 3, 6, 32), " +
                // Semestre 7
                "('FIB-4110', 'Practica Docente I', 5, 7, 32), " +
                "('FIB-4120', 'Taller Recursos Didacticos', 3, 7, 32), " +
                "('FIB-4130', 'Educacion Ambiental Y Salud', 3, 7, 32), " +
                "('PED-4110', 'Etica Profesional Docente', 2, 7, 32), " +
                "('PED-4120', 'Legislacion Educativa Dom', 3, 7, 32), " +
                // Semestre 8
                "('FIB-4140', 'Practica Docente II', 6, 8, 32), " +
                "('FIB-4210', 'Seminario De Educacion Basica', 3, 8, 32), " +
                "('FIB-7010', 'Tesis De Grado O Curso Equival', 8, 8, 32)");

        
         //  Licenciatura en Educación Infantil (carrera_id = 33)
        stmt.execute("INSERT INTO pensum (codigo, nombre, creditos, semestre, carrera_id) VALUES " +
                "('BIO-0140', 'Biologia Basica', 3, 1, 33), " +
                "('DOP-1000', 'Orientacion Institucional', 1, 1, 33), " +
                "('EFS-0110', 'Educacion Fisica', 1, 1, 33), " +
                "('FIL-0110', 'Int A La Filosofia', 3, 1, 33), " +
                "('FIS-0140', 'Fisica Basica', 4, 1, 33), " +
                "('HIS-0110', 'Fund De His Social Dominicana', 3, 1, 33), " +
                "('LET-0110', 'Lengua Espanola Basica I', 3, 1, 33), " +
                "('MAT-0140', 'Matematica Basica', 4, 1, 33), " +
                "('SOC-0110', 'Int A Las Ciencias Sociales', 2, 1, 33), " +
                // Semestre 2
                "('INF-1030', 'Introduccion A La Informatica', 4, 2, 33), " +
                "('LET-0120', 'Lengua Espanola Basica II', 3, 2, 33), " +
                "('PED-1110', 'Fundamentos De La Educacion', 3, 2, 33), " +
                "('PED-1120', 'Historia De La Educacion', 3, 2, 33), " +
                "('PSI-1050', 'Int A La Psicologia', 3, 2, 33), " +
                "('QUI-0140', 'Quimica Basica', 3, 2, 33), " +
                "('SOC-1100', 'Int A La Sociologia General', 4, 2, 33), " +
                // Semestre 3
                "('FIB-1310', 'Fundamentos De Educ Infantil', 4, 3, 33), " +
                "('FIB-1320', 'Desarrollo Infantil Integral', 4, 3, 33), " +
                "('PED-2110', 'Didactica General', 4, 3, 33), " +
                "('PED-2210', 'Curriculo Escolar', 3, 3, 33), " +
                "('PSI-2110', 'Psicologia Del Desarrollo', 3, 3, 33), " +
                "('TEG-1110', 'Tecnologia Educativa I', 3, 3, 33), " +
                // Semestre 4
                "('FIB-2310', 'Psicomotricidad Y Juego', 4, 4, 33), " +
                "('FIB-2320', 'Estimulacion Temprana', 4, 4, 33), " +
                "('FIB-2330', 'Expresion Plastica Infantil', 3, 4, 33), " +
                "('FIB-2340', 'Expresion Musical Y Corporal', 3, 4, 33), " +
                "('PSI-2120', 'Psicologia Del Aprendizaje', 3, 4, 33), " +
                "('SOC-2140', 'Sociologia De La Educacion', 3, 4, 33), " +
                // Semestre 5
                "('FIB-3310', 'Didactica Nivel Inicial I', 4, 5, 33), " +
                "('FIB-3320', 'Desarrollo Lenguaje Infantil', 4, 5, 33), " +
                "('FIB-3330', 'Pensamiento Logico Matematico', 4, 5, 33), " +
                "('FIB-3340', 'Literatura Infantil', 3, 5, 33), " +
                "('PED-3120', 'Evaluacion Educativa', 3, 5, 33), " +
                "('PED-3130', 'Planificacion Educativa', 3, 5, 33), " +
                // Semestre 6
                "('FIB-3410', 'Didactica Nivel Inicial II', 4, 6, 33), " +
                "('FIB-3420', 'Salud Y Nutricion Infantil', 3, 6, 33), " +
                "('FIB-3430', 'Familia Y Comunidad En Educ Inf', 3, 6, 33), " +
                "('PED-3110', 'Metodologia Inv Educativa', 3, 6, 33), " +
                "('PED-3210', 'Gestion Y Supervision Escolar', 3, 6, 33), " +
                "('PED-3220', 'Orientacion Educativa', 3, 6, 33), " +
                // Semestre 7
                "('FIB-4310', 'Practica Docente Nivel Inicial I', 5, 7, 33), " +
                "('FIB-4320', 'Taller Materiales Didacticos Inf', 3, 7, 33), " +
                "('FIB-4330', 'Atencion A La Diversidad Inf', 3, 7, 33), " +
                "('PED-4110', 'Etica Profesional Docente', 2, 7, 33), " +
                "('PED-4120', 'Legislacion Educativa Dom', 3, 7, 33), " +
                // Semestre 8
                "('FIB-4340', 'Practica Docente Nivel Inicial II', 6, 8, 33), " +
                "('FIB-4410', 'Seminario De Educacion Infantil', 3, 8, 33), " +
                "('FIB-7310', 'Tesis De Grado O Curso Equival', 8, 8, 33)");
        
        //  Licenciatura en Educacion, mencion Filosofia y Letras (carrera_id = 34)
        stmt.execute("INSERT INTO pensum (codigo, nombre, creditos, semestre, carrera_id) VALUES " +
                "('BIO-0140', 'Biologia Basica', 3, 1, 34), " +
                "('DOP-1000', 'Orientacion Institucional', 1, 1, 34), " +
                "('EFS-0110', 'Educacion Fisica', 1, 1, 34), " +
                "('FIL-0110', 'Int A La Filosofia', 3, 1, 34), " +
                "('FIS-0140', 'Fisica Basica', 4, 1, 34), " +
                "('HIS-0110', 'Fund De His Social Dominicana', 3, 1, 34), " +
                "('LET-0110', 'Lengua Espanola Basica I', 3, 1, 34), " +
                "('MAT-0140', 'Matematica Basica', 4, 1, 34), " +
                "('SOC-0110', 'Int A Las Ciencias Sociales', 2, 1, 34), " +
                // Semestre 2
                "('FIL-1110', 'Historia De La Filosofia I', 4, 2, 34), " +
                "('INF-1030', 'Introduccion A La Informatica', 4, 2, 34), " +
                "('LET-0120', 'Lengua Espanola Basica II', 3, 2, 34), " +
                "('LET-1110', 'Linguistica General I', 3, 2, 34), " +
                "('PED-1110', 'Fundamentos De La Educacion', 3, 2, 34), " +
                "('PSI-1050', 'Int A La Psicologia', 3, 2, 34), " +
                "('QUI-0140', 'Quimica Basica', 3, 2, 34), " +
                // Semestre 3
                "('FIL-1120', 'Historia De La Filosofia II', 4, 3, 34), " +
                "('FIL-1210', 'Logica Formal', 3, 3, 34), " +
                "('LET-1210', 'Gramatica Espanola I', 4, 3, 34), " +
                "('LET-1310', 'Int Estudio De La Literatura', 3, 3, 34), " +
                "('PED-2110', 'Didactica General', 4, 3, 34), " +
                "('PSI-2110', 'Psicologia Del Desarrollo', 3, 3, 34), " +
                // Semestre 4
                "('FIL-2110', 'Historia De La Filosofia III', 4, 4, 34), " +
                "('FIL-2210', 'Teoria Del Conocimiento', 3, 4, 34), " +
                "('LET-1220', 'Gramatica Espanola II', 4, 4, 34), " +
                "('LET-2310', 'Literatura Espanola I', 4, 4, 34), " +
                "('PED-2210', 'Curriculo Escolar', 3, 4, 34), " +
                "('PSI-2120', 'Psicologia Del Aprendizaje', 3, 4, 34), " +
                // Semestre 5
                "('FIL-2310', 'Etica Filosofica', 3, 5, 34), " +
                "('LET-2320', 'Literatura Hispanoamericana I', 4, 5, 34), " +
                "('LET-2410', 'Redaccion Y Estilo', 3, 5, 34), " +
                "('PED-3120', 'Evaluacion Educativa', 3, 5, 34), " +
                "('PED-3130', 'Planificacion Educativa', 3, 5, 34), " +
                "('PED-3310', 'Didactica De Lengua Y Lit I', 4, 5, 34), " +
                // Semestre 6
                "('FIL-3110', 'Filosofia Contemporanea', 4, 6, 34), " +
                "('FIL-3210', 'Filosofia De La Educacion', 3, 6, 34), " +
                "('LET-3310', 'Literatura Dominicana I', 4, 6, 34), " +
                "('PED-3110', 'Metodologia Inv Educativa', 3, 6, 34), " +
                "('PED-3320', 'Didactica De Lengua Y Lit II', 4, 6, 34), " +
                "('PED-3330', 'Didactica De La Filosofia', 3, 6, 34), " +
                // Semestre 7
                "('FIL-4110', 'Pensamiento Filosofico Latinoam', 3, 7, 34), " +
                "('LET-3320', 'Literatura Dominicana II', 4, 7, 34), " +
                "('PED-4110', 'Etica Profesional Docente', 2, 7, 34), " +
                "('PED-4310', 'Practica Docente Media I', 5, 7, 34), " +
                "('TEG-1110', 'Tecnologia Educativa I', 3, 7, 34), " +
                // Semestre 8
                "('LET-4110', 'Analisis De Textos Literarios', 3, 8, 34), " +
                "('PED-4320', 'Practica Docente Media II', 6, 8, 34), " +
                "('PED-4410', 'Seminario Filosofia Y Letras', 3, 8, 34), " +
                "('PED-7010', 'Tesis De Grado O Curso Equival', 8, 8, 34)");

        
        //  Licenciatura en Educacion Especial (carrera_id = 35)
        stmt.execute("INSERT INTO pensum (codigo, nombre, creditos, semestre, carrera_id) VALUES " +
                "('BIO-0140', 'Biologia Basica', 3, 1, 35), " +
                "('DOP-1000', 'Orientacion Institucional', 1, 1, 35), " +
                "('EFS-0110', 'Educacion Fisica', 1, 1, 35), " +
                "('FIL-0110', 'Int A La Filosofia', 3, 1, 35), " +
                "('FIS-0140', 'Fisica Basica', 4, 1, 35), " +
                "('HIS-0110', 'Fund De His Social Dominicana', 3, 1, 35), " +
                "('LET-0110', 'Lengua Espanola Basica I', 3, 1, 35), " +
                "('MAT-0140', 'Matematica Basica', 4, 1, 35), " +
                "('SOC-0110', 'Int A Las Ciencias Sociales', 2, 1, 35), " +
                // Semestre 2
                "('INF-1030', 'Introduccion A La Informatica', 4, 2, 35), " +
                "('LET-0120', 'Lengua Espanola Basica II', 3, 2, 35), " +
                "('PED-1110', 'Fundamentos De La Educacion', 3, 2, 35), " +
                "('PED-1120', 'Historia De La Educacion', 3, 2, 35), " +
                "('PSI-1050', 'Int A La Psicologia', 3, 2, 35), " +
                "('QUI-0140', 'Quimica Basica', 3, 2, 35), " +
                "('SOC-1100', 'Int A La Sociologia General', 4, 2, 35), " +
                // Semestre 3
                "('OED-1110', 'Fundamentos Educacion Especial', 4, 3, 35), " +
                "('OED-1120', 'Bases Neurobiologicas Aprend', 4, 3, 35), " +
                "('PED-2110', 'Didactica General', 4, 3, 35), " +
                "('PED-2210', 'Curriculo Escolar', 3, 3, 35), " +
                "('PSI-2110', 'Psicologia Del Desarrollo', 3, 3, 35), " +
                "('TEG-1110', 'Tecnologia Educativa I', 3, 3, 35), " +
                // Semestre 4
                "('OED-2110', 'Discapacidad Intelectual', 4, 4, 35), " +
                "('OED-2120', 'Trastornos Del Lenguaje Y Com', 4, 4, 35), " +
                "('OED-2130', 'Discapacidad Sensorial Visual', 3, 4, 35), " +
                "('OED-2140', 'Discapacidad Sensorial Auditiva', 3, 4, 35), " +
                "('PSI-2120', 'Psicologia Del Aprendizaje', 3, 4, 35), " +
                "('SOC-2140', 'Sociologia De La Educacion', 3, 4, 35), " +
                // Semestre 5
                "('OED-3110', 'Discapacidad Motora', 3, 5, 35), " +
                "('OED-3120', 'Trastornos Espectro Autista', 4, 5, 35), " +
                "('OED-3130', 'Dificultades Especificas Aprend', 4, 5, 35), " +
                "('OED-3140', 'Evaluacion Psicopedagogica', 4, 5, 35), " +
                "('PED-3120', 'Evaluacion Educativa', 3, 5, 35), " +
                "('PED-3130', 'Planificacion Educativa', 3, 5, 35), " +
                // Semestre 6
                "('OED-3210', 'Adaptaciones Curriculares', 4, 6, 35), " +
                "('OED-3220', 'Intervencion Psicopedagogica', 4, 6, 35), " +
                "('OED-3230', 'Lengua De Senas Y Braille', 3, 6, 35), " +
                "('OED-3240', 'Educacion Inclusiva', 3, 6, 35), " +
                "('PED-3110', 'Metodologia Inv Educativa', 3, 6, 35), " +
                "('PED-3220', 'Orientacion Educativa', 3, 6, 35), " +
                // Semestre 7
                "('OED-4110', 'Practica Educacion Especial I', 5, 7, 35), " +
                "('OED-4120', 'Tecnologia Asistiva En Educ', 3, 7, 35), " +
                "('OED-4130', 'Orientacion Familiar Y Comunit', 3, 7, 35), " +
                "('PED-4110', 'Etica Profesional Docente', 2, 7, 35), " +
                "('PED-4120', 'Legislacion Educativa Dom', 3, 7, 35), " +
                // Semestre 8
                "('OED-4140', 'Practica Educacion Especial II', 6, 8, 35), " +
                "('OED-4210', 'Seminario De Educacion Especial', 3, 8, 35), " +
                "('OED-7010', 'Tesis De Grado O Curso Equival', 8, 8, 35)");

        
        //  Licenciatura en Educacion, mencion Gestion Educativa (carrera_id = 59)
        stmt.execute("INSERT INTO pensum (codigo, nombre, creditos, semestre, carrera_id) VALUES " +
                "('BIO-0140', 'Biologia Basica', 3, 1, 59), " +
                "('DOP-1000', 'Orientacion Institucional', 1, 1, 59), " +
                "('EFS-0110', 'Educacion Fisica', 1, 1, 59), " +
                "('FIL-0110', 'Int A La Filosofia', 3, 1, 59), " +
                "('FIS-0140', 'Fisica Basica', 4, 1, 59), " +
                "('HIS-0110', 'Fund De His Social Dominicana', 3, 1, 59), " +
                "('LET-0110', 'Lengua Espanola Basica I', 3, 1, 59), " +
                "('MAT-0140', 'Matematica Basica', 4, 1, 59), " +
                "('SOC-0110', 'Int A Las Ciencias Sociales', 2, 1, 59), " +
                // Semestre 2
                "('ADM-1150', 'Teoria Administrativa', 4, 2, 59), " +
                "('INF-1030', 'Introduccion A La Informatica', 4, 2, 59), " +
                "('LET-0120', 'Lengua Espanola Basica II', 3, 2, 59), " +
                "('PED-1110', 'Fundamentos De La Educacion', 3, 2, 59), " +
                "('PED-1120', 'Historia De La Educacion', 3, 2, 59), " +
                "('PSI-1050', 'Int A La Psicologia', 3, 2, 59), " +
                "('QUI-0140', 'Quimica Basica', 3, 2, 59), " +
                // Semestre 3
                "('EST-1110', 'Estadistica General', 4, 3, 59), " +
                "('PED-2110', 'Didactica General', 4, 3, 59), " +
                "('PED-2210', 'Curriculo Escolar', 3, 3, 59), " +
                "('PSI-2110', 'Psicologia Del Desarrollo', 3, 3, 59), " +
                "('TEG-1110', 'Tecnologia Educativa I', 3, 3, 59), " +
                "('TEG-1210', 'Teoria De La Gestion Educativa', 4, 3, 59), " +
                // Semestre 4
                "('CON-1190', 'Contabilidad Aplicada Educacion', 4, 4, 59), " +
                "('PSI-2120', 'Psicologia Del Aprendizaje', 3, 4, 59), " +
                "('SOC-2140', 'Sociologia De La Educacion', 3, 4, 59), " +
                "('TEG-2110', 'Administracion Escolar I', 4, 4, 59), " +
                "('TEG-2210', 'Planificacion Educativa I', 4, 4, 59), " +
                "('TEG-2310', 'Legislacion Educativa Dom', 3, 4, 59), " +
                // Semestre 5
                "('PED-3120', 'Evaluacion Educativa', 3, 5, 59), " +
                "('TEG-2120', 'Administracion Escolar II', 4, 5, 59), " +
                "('TEG-2220', 'Planificacion Educativa II', 4, 5, 59), " +
                "('TEG-3110', 'Supervision Educativa I', 4, 5, 59), " +
                "('TEG-3210', 'Gestion De Recursos Humanos Ed', 3, 5, 59), " +
                // Semestre 6
                "('PED-3110', 'Metodologia Inv Educativa', 3, 6, 59), " +
                "('TEG-3120', 'Supervision Educativa II', 4, 6, 59), " +
                "('TEG-3310', 'Presupuesto Y Finanzas Escol', 4, 6, 59), " +
                "('TEG-3410', 'Politicas Publicas Educativas', 3, 6, 59), " +
                "('TEG-3510', 'Evaluacion Institucional', 4, 6, 59), " +
                // Semestre 7
                "('PED-4110', 'Etica Profesional Docente', 2, 7, 59), " +
                "('TEG-4110', 'Diseno Y Gestion Proy Educat', 4, 7, 59), " +
                "('TEG-4210', 'Calidad De La Gestion Educativa', 4, 7, 59), " +
                "('TEG-4310', 'Practica De Gestion Educativa I', 5, 7, 59), " +
                "('TEG-4410', 'Liderazgo Y Clima Organizac Ed', 3, 7, 59), " +
                // Semestre 8
                "('TEG-4320', 'Practica De Gestion Educativa II', 6, 8, 59), " +
                "('TEG-5110', 'Seminario Gestion Educativa', 3, 8, 59), " +
                "('TEG-7010', 'Tesis De Grado O Curso Equival', 8, 8, 59)");

        
        // Inicio de la Facultad de  Ciencias de la Salud
        
        //  Licenciatura en Bioanalisis (carrera_id = 36)
        stmt.execute("INSERT INTO pensum (codigo, nombre, creditos, semestre, carrera_id) VALUES " +
                "('BIO-0140', 'Biologia Basica', 3, 1, 36), " +
                "('DOP-1000', 'Orientacion Institucional', 1, 1, 36), " +
                "('EFS-0110', 'Educacion Fisica', 1, 1, 36), " +
                "('FIL-0110', 'Int A La Filosofia', 3, 1, 36), " +
                "('FIS-0140', 'Fisica Basica', 4, 1, 36), " +
                "('HIS-0110', 'Fund De His Social Dominicana', 3, 1, 36), " +
                "('LET-0110', 'Lengua Espanola Basica I', 3, 1, 36), " +
                "('MAT-0140', 'Matematica Basica', 4, 1, 36), " +
                "('SOC-0110', 'Int A Las Ciencias Sociales', 2, 1, 36), " +
                // Semestre 2
                "('BAN-1110', 'Introd Al Bioanalisis', 3, 2, 36), " +
                "('BIO-1310', 'Biologia Celular Y Molecular', 4, 2, 36), " +
                "('EST-1110', 'Estadistica General', 4, 2, 36), " +
                "('LET-0120', 'Lengua Espanola Basica II', 3, 2, 36), " +
                "('QUI-1210', 'Quimica General', 5, 2, 36), " +
                "('SAP-1050', 'Introd A La Salud Publica', 3, 2, 36), " +
                // Semestre 3
                "('BAN-1210', 'Instrumentacion De Laboratorio', 4, 3, 36), " +
                "('CFI-1110', 'Anatomia Humana', 4, 3, 36), " +
                "('CFI-1210', 'Fisiologia Humana', 4, 3, 36), " +
                "('QUI-2110', 'Quimica Organica', 5, 3, 36), " +
                "('QUI-2310', 'Quimica Analitica', 5, 3, 36), " +
                // Semestre 4
                "('BAN-2110', 'Hematologia I', 4, 4, 36), " +
                "('BAN-2210', 'Parasitologia Clinica I', 4, 4, 36), " +
                "('BAN-2310', 'Uroanalisis Y Liquidos Corp', 4, 4, 36), " +
                "('CFI-2310', 'Bioquimica General', 5, 4, 36), " +
                "('MIP-1110', 'Microbiologia General', 4, 4, 36), " +
                // Semestre 5
                "('BAN-2120', 'Hematologia II', 4, 5, 36), " +
                "('BAN-2220', 'Parasitologia Clinica II', 4, 5, 36), " +
                "('BAN-3110', 'Quimica Clinica I', 5, 5, 36), " +
                "('BAN-3210', 'Inmunologia General Y Clinica', 4, 5, 36), " +
                "('MIP-2110', 'Bacteriologia Clinica I', 4, 5, 36), " +
                // Semestre 6
                "('BAN-3120', 'Quimica Clinica II', 5, 6, 36), " +
                "('BAN-3310', 'Banco De Sangre E Inmunohemat', 4, 6, 36), " +
                "('BAN-3410', 'Micologia Y Virologia Clinica', 4, 6, 36), " +
                "('MIP-2120', 'Bacteriologia Clinica II', 4, 6, 36), " +
                "('SAP-2110', 'Epidemiologia General', 3, 6, 36), " +
                // Semestre 7
                "('BAN-4110', 'Gestion Y Control Calidad Lab', 3, 7, 36), " +
                "('BAN-4210', 'Toxicologia Clinica', 3, 7, 36), " +
                "('BAN-4310', 'Diagnostico Molecular Clinico', 4, 7, 36), " +
                "('BAN-4410', 'Bioetica Y Legislacion En Lab', 2, 7, 36), " +
                "('SAP-3110', 'Metodologia Inv En Salud', 3, 7, 36), " +
                // Semestre 8
                "('BAN-4510', 'Internado Rotatorio Bioanalisis', 10, 8, 36), " +
                "('BAN-5110', 'Seminario De Bioanalisis', 3, 8, 36), " +
                "('BAN-7010', 'Tesis De Grado O Curso Equival', 8, 8, 36)");

        
        //  Licenciatura en Imagenologia (carrera_id = 37)
        stmt.execute("INSERT INTO pensum (codigo, nombre, creditos, semestre, carrera_id) VALUES " +
                "('BIO-0140', 'Biologia Basica', 3, 1, 37), " +
                "('DOP-1000', 'Orientacion Institucional', 1, 1, 37), " +
                "('EFS-0110', 'Educacion Fisica', 1, 1, 37), " +
                "('FIL-0110', 'Int A La Filosofia', 3, 1, 37), " +
                "('FIS-0140', 'Fisica Basica', 4, 1, 37), " +
                "('HIS-0110', 'Fund De His Social Dominicana', 3, 1, 37), " +
                "('LET-0110', 'Lengua Espanola Basica I', 3, 1, 37), " +
                "('MAT-0140', 'Matematica Basica', 4, 1, 37), " +
                "('SOC-0110', 'Int A Las Ciencias Sociales', 2, 1, 37), " +
                // Semestre 2
                "('CFI-1110', 'Anatomia Humana', 4, 2, 37), " +
                "('FIS-1510', 'Fisica De Las Radiaciones', 4, 2, 37), " +
                "('IMG-1110', 'Introd A La Imagenologia', 3, 2, 37), " +
                "('INF-1030', 'Introduccion A La Informatica', 4, 2, 37), " +
                "('LET-0120', 'Lengua Espanola Basica II', 3, 2, 37), " +
                "('QUI-0140', 'Quimica Basica', 3, 2, 37), " +
                // Semestre 3
                "('CFI-1210', 'Fisiologia Humana', 4, 3, 37), " +
                "('IMG-1210', 'Anatomia Radiologica I', 4, 3, 37), " +
                "('IMG-1310', 'Proteccion Radiologica', 3, 3, 37), " +
                "('IMG-1410', 'Tecnicas Radiologicas I', 4, 3, 37), " +
                "('SAP-1050', 'Introd A La Salud Publica', 3, 3, 37), " +
                // Semestre 4
                "('IMG-2110', 'Equipos Y Procesamiento Imagen', 4, 4, 37), " +
                "('IMG-2210', 'Anatomia Radiologica II', 4, 4, 37), " +
                "('IMG-2410', 'Tecnicas Radiologicas II', 4, 4, 37), " +
                "('IMG-2510', 'Atencion Al Paciente En Imagen', 3, 4, 37), " +
                "('MED-1310', 'Fisiopatologia General', 4, 4, 37), " +
                // Semestre 5
                "('IMG-3110', 'Estudios Contrastados', 4, 5, 37), " +
                "('IMG-3210', 'Mamografia Y Densitometria', 4, 5, 37), " +
                "('IMG-3310', 'Ultrasonografia Basica I', 4, 5, 37), " +
                "('IMG-3410', 'Tomografia Computarizada I', 4, 5, 37), " +
                "('SAP-2110', 'Epidemiologia General', 3, 5, 37), " +
                // Semestre 6
                "('IMG-3320', 'Ultrasonografia Basica II', 4, 6, 37), " +
                "('IMG-3420', 'Tomografia Computarizada II', 4, 6, 37), " +
                "('IMG-3510', 'Resonancia Magnetica I', 4, 6, 37), " +
                "('IMG-3610', 'Radiologia Intervencionista', 3, 6, 37), " +
                "('SAP-3110', 'Metodologia Inv En Salud', 3, 6, 37), " +
                // Semestre 7
                "('IMG-4110', 'Resonancia Magnetica II', 4, 7, 37), " +
                "('IMG-4210', 'Medicina Nuclear Y Radioterapia', 4, 7, 37), " +
                "('IMG-4310', 'Control Calidad En Imagenologia', 3, 7, 37), " +
                "('IMG-4410', 'Bioetica Y Legislacion Imagen', 2, 7, 37), " +
                "('IMG-4510', 'Practica Clinica Imagenologia I', 5, 7, 37), " +
                // Semestre 8
                "('IMG-4520', 'Practica Clinica Imagenologia II', 6, 8, 37), " +
                "('IMG-5110', 'Seminario De Imagenologia', 3, 8, 37), " +
                "('IMG-7010', 'Tesis De Grado O Curso Equival', 8, 8, 37)");

        
        //  Licenciatura en Enfermeria (carrera_id = 38)
        stmt.execute("INSERT INTO pensum (codigo, nombre, creditos, semestre, carrera_id) VALUES " +
                "('BIO-0140', 'Biologia Basica', 3, 1, 38), " +
                "('DOP-1000', 'Orientacion Institucional', 1, 1, 38), " +
                "('EFS-0110', 'Educacion Fisica', 1, 1, 38), " +
                "('FIL-0110', 'Int A La Filosofia', 3, 1, 38), " +
                "('FIS-0140', 'Fisica Basica', 4, 1, 38), " +
                "('HIS-0110', 'Fund De His Social Dominicana', 3, 1, 38), " +
                "('LET-0110', 'Lengua Espanola Basica I', 3, 1, 38), " +
                "('MAT-0140', 'Matematica Basica', 4, 1, 38), " +
                "('SOC-0110', 'Int A Las Ciencias Sociales', 2, 1, 38), " +
                // Semestre 2
                "('CFI-1110', 'Anatomia Humana', 4, 2, 38), " +
                "('ENF-1110', 'Introd A La Enfermeria', 3, 2, 38), " +
                "('LET-0120', 'Lengua Espanola Basica II', 3, 2, 38), " +
                "('PSI-1050', 'Int A La Psicologia', 3, 2, 38), " +
                "('QUI-0140', 'Quimica Basica', 3, 2, 38), " +
                "('SAP-1050', 'Introd A La Salud Publica', 3, 2, 38), " +
                // Semestre 3
                "('CFI-1210', 'Fisiologia Humana', 4, 3, 38), " +
                "('CFI-2310', 'Bioquimica General', 5, 3, 38), " +
                "('ENF-1210', 'Fundamentos De Enfermeria I', 5, 3, 38), " +
                "('MIP-1110', 'Microbiologia General', 4, 3, 38), " +
                "('PSI-2110', 'Psicologia Del Desarrollo', 3, 3, 38), " +
                // Semestre 4
                "('ENF-2110', 'Fundamentos De Enfermeria II', 5, 4, 38), " +
                "('ENF-2210', 'Farmacologia En Enfermeria', 4, 4, 38), " +
                "('ENF-2310', 'Nutricion Y Dietoterapia', 3, 4, 38), " +
                "('MED-1310', 'Fisiopatologia General', 4, 4, 38), " +
                "('SAP-2110', 'Epidemiologia General', 3, 4, 38), " +
                // Semestre 5
                "('ENF-3110', 'Enfermeria Medico Quirurgica I', 6, 5, 38), " +
                "('ENF-3210', 'Enfermeria Comunitaria I', 4, 5, 38), " +
                "('ENF-3310', 'Bioetica Y Legislacion Enf', 2, 5, 38), " +
                "('SAP-3110', 'Metodologia Inv En Salud', 3, 5, 38), " +
                // Semestre 6
                "('ENF-3120', 'Enfermeria Medico Quirurgica II', 6, 6, 38), " +
                "('ENF-3220', 'Enfermeria Comunitaria II', 4, 6, 38), " +
                "('ENF-3410', 'Enfermeria Materno Infantil I', 5, 6, 38), " +
                "('ENF-3510', 'Enfermeria En Salud Mental', 4, 6, 38), " +
                // Semestre 7
                "('ENF-4110', 'Enfermeria Materno Infantil II', 5, 7, 38), " +
                "('ENF-4210', 'Enfermeria Pediatrica', 5, 7, 38), " +
                "('ENF-4310', 'Adm Servicios De Enfermeria', 4, 7, 38), " +
                "('ENF-4410', 'Enfermeria Geriatrica', 3, 7, 38), " +
                // Semestre 8
                "('ENF-4510', 'Internado Rotatorio Enfermeria', 10, 8, 38), " +
                "('ENF-5110', 'Seminario De Enfermeria', 3, 8, 38), " +
                "('ENF-7010', 'Tesis De Grado O Curso Equival', 8, 8, 38)");

        
        //  Licenciatura en Farmacia (carrera_id = 39)
        stmt.execute("INSERT INTO pensum (codigo, nombre, creditos, semestre, carrera_id) VALUES " +
                "('BIO-0140', 'Biologia Basica', 3, 1, 39), " +
                "('DOP-1000', 'Orientacion Institucional', 1, 1, 39), " +
                "('EFS-0110', 'Educacion Fisica', 1, 1, 39), " +
                "('FIL-0110', 'Int A La Filosofia', 3, 1, 39), " +
                "('FIS-0140', 'Fisica Basica', 4, 1, 39), " +
                "('HIS-0110', 'Fund De His Social Dominicana', 3, 1, 39), " +
                "('LET-0110', 'Lengua Espanola Basica I', 3, 1, 39), " +
                "('MAT-0140', 'Matematica Basica', 4, 1, 39), " +
                "('SOC-0110', 'Int A Las Ciencias Sociales', 2, 1, 39), " +
                // Semestre 2
                "('BIO-1310', 'Biologia Celular Y Molecular', 4, 2, 39), " +
                "('FAR-1110', 'Introd A La Farmacia', 3, 2, 39), " +
                "('LET-0120', 'Lengua Espanola Basica II', 3, 2, 39), " +
                "('MAT-2500', 'Analitica Y Calculo I', 5, 2, 39), " +
                "('QUI-1210', 'Quimica General', 5, 2, 39), " +
                "('SAP-1050', 'Introd A La Salud Publica', 3, 2, 39), " +
                // Semestre 3
                "('CFI-1110', 'Anatomia Humana', 4, 3, 39), " +
                "('CFI-1210', 'Fisiologia Humana', 4, 3, 39), " +
                "('EST-1110', 'Estadistica General', 4, 3, 39), " +
                "('QUI-2110', 'Quimica Organica I', 5, 3, 39), " +
                "('QUI-2310', 'Quimica Analitica I', 5, 3, 39), " +
                // Semestre 4
                "('CFI-2310', 'Bioquimica General', 5, 4, 39), " +
                "('FAR-2110', 'Fisicoquimica Farmaceutica', 4, 4, 39), " +
                "('MIP-1110', 'Microbiologia General', 4, 4, 39), " +
                "('QUI-2120', 'Quimica Organica II', 5, 4, 39), " +
                "('QUI-2320', 'Quimica Analitica II', 5, 4, 39), " +
                // Semestre 5
                "('FAR-2210', 'Farmacognosia I', 4, 5, 39), " +
                "('FAR-2310', 'Tecnologia Farmaceutica I', 5, 5, 39), " +
                "('FAR-2410', 'Farmacologia I', 5, 5, 39), " +
                "('FAR-2510', 'Quimica Farmaceutica I', 4, 5, 39), " +
                "('MED-1310', 'Fisiopatologia General', 4, 5, 39), " +
                // Semestre 6
                "('FAR-2220', 'Farmacognosia II', 4, 6, 39), " +
                "('FAR-2320', 'Tecnologia Farmaceutica II', 5, 6, 39), " +
                "('FAR-2420', 'Farmacologia II', 5, 6, 39), " +
                "('FAR-2520', 'Quimica Farmaceutica II', 4, 6, 39), " +
                "('FAR-3110', 'Analisis De Medicamentos I', 4, 6, 39), " +
                // Semestre 7
                "('FAR-3120', 'Analisis De Medicamentos II', 4, 7, 39), " +
                "('FAR-3210', 'Toxicologia General Y Clinica', 4, 7, 39), " +
                "('FAR-3310', 'Farmacia Clinica Y Hospital', 4, 7, 39), " +
                "('FAR-3410', 'Legislacion Y Deontologia Farm', 3, 7, 39), " +
                "('SAP-3110', 'Metodologia Inv En Salud', 3, 7, 39), " +
                // Semestre 8
                "('FAR-4110', 'Gestion Y Mercadeo Farmaceut', 3, 8, 39), " +
                "('FAR-4510', 'Internado Rotatorio Farmacia', 8, 8, 39), " +
                "('FAR-7010', 'Tesis De Grado O Curso Equival', 8, 8, 39)");

        
        //  Doctor en Medicina (carrera_id = 40)
        stmt.execute("INSERT INTO pensum (codigo, nombre, creditos, semestre, carrera_id) VALUES " +
                "('BIO-0140', 'Biologia Basica', 3, 1, 40), " +
                "('DOP-1000', 'Orientacion Institucional', 1, 1, 40), " +
                "('EFS-0110', 'Educacion Fisica', 1, 1, 40), " +
                "('FIL-0110', 'Int A La Filosofia', 3, 1, 40), " +
                "('FIS-0140', 'Fisica Basica', 4, 1, 40), " +
                "('HIS-0110', 'Fund De His Social Dominicana', 3, 1, 40), " +
                "('LET-0110', 'Lengua Espanola Basica I', 3, 1, 40), " +
                "('MAT-0140', 'Matematica Basica', 4, 1, 40), " +
                "('SOC-0110', 'Int A Las Ciencias Sociales', 2, 1, 40), " +
                // Semestre 2
                "('BIO-1310', 'Biologia Celular Y Molecular', 4, 2, 40), " +
                "('CFI-1110', 'Anatomia Humana I', 5, 2, 40), " +
                "('CFI-1130', 'Histologia Y Embriologia', 5, 2, 40), " +
                "('LET-0120', 'Lengua Espanola Basica II', 3, 2, 40), " +
                "('QUI-1210', 'Quimica General', 5, 2, 40), " +
                "('SAP-1050', 'Introd A La Salud Publica', 3, 2, 40), " +
                // Semestre 3
                "('CFI-1120', 'Anatomia Humana II', 5, 3, 40), " +
                "('CFI-1210', 'Fisiologia Humana I', 5, 3, 40), " +
                "('CFI-2310', 'Bioquimica I', 5, 3, 40), " +
                "('MIP-1110', 'Microbiologia Y Parasitologia', 5, 3, 40), " +
                "('QUI-2110', 'Quimica Organica', 5, 3, 40), " +
                // Semestre 4
                "('CFI-1220', 'Fisiologia Humana II', 5, 4, 40), " +
                "('CFI-2320', 'Bioquimica II', 5, 4, 40), " +
                "('MED-1110', 'Semiologia Medica I', 5, 4, 40), " +
                "('MED-1210', 'Anatomia Patologica I', 4, 4, 40), " +
                "('SAP-2110', 'Epidemiologia General', 3, 4, 40), " +
                // Semestre 5
                "('CFI-3110', 'Farmacologia General Y Clin I', 5, 5, 40), " +
                "('MED-1120', 'Semiologia Medica II', 5, 5, 40), " +
                "('MED-1220', 'Anatomia Patologica II', 4, 5, 40), " +
                "('MED-1310', 'Fisiopatologia General', 4, 5, 40), " +
                "('MED-2110', 'Medicina Interna I', 6, 5, 40), " +
                // Semestre 6
                "('CFI-3120', 'Farmacologia General Y Clin II', 5, 6, 40), " +
                "('MED-2120', 'Medicina Interna II', 6, 6, 40), " +
                "('MED-2210', 'Cirugia General I', 6, 6, 40), " +
                "('MED-2310', 'Pediatria Y Neonatologia I', 6, 6, 40), " +
                "('MED-2410', 'Ginecologia Y Obstetricia I', 6, 6, 40), " +
                // Semestre 7
                "('MED-2220', 'Cirugia General II', 6, 7, 40), " +
                "('MED-2320', 'Pediatria Y Neonatologia II', 6, 7, 40), " +
                "('MED-2420', 'Ginecologia Y Obstetricia II', 6, 7, 40), " +
                "('MED-3110', 'Psiquiatria Y Salud Mental', 4, 7, 40), " +
                "('MED-3210', 'Medicina Legal Y Bioetica', 3, 7, 40), " +
                "('SAP-3110', 'Metodologia Inv En Salud', 3, 7, 40), " +
                // Semestre 8
                "('MED-4510', 'Internado Rotatorio Medicina', 16, 8, 40), " +
                "('MED-7010', 'Tesis De Grado O Curso Equival', 8, 8, 40)");

        
        //  Doctor en Odontologia (carrera_id = 41)
        stmt.execute("INSERT INTO pensum (codigo, nombre, creditos, semestre, carrera_id) VALUES " +
                "('BIO-0140', 'Biologia Basica', 3, 1, 41), " +
                "('DOP-1000', 'Orientacion Institucional', 1, 1, 41), " +
                "('EFS-0110', 'Educacion Fisica', 1, 1, 41), " +
                "('FIL-0110', 'Int A La Filosofia', 3, 1, 41), " +
                "('FIS-0140', 'Fisica Basica', 4, 1, 41), " +
                "('HIS-0110', 'Fund De His Social Dominicana', 3, 1, 41), " +
                "('LET-0110', 'Lengua Espanola Basica I', 3, 1, 41), " +
                "('MAT-0140', 'Matematica Basica', 4, 1, 41), " +
                "('SOC-0110', 'Int A Las Ciencias Sociales', 2, 1, 41), " +
                // Semestre 2
                "('CFI-1110', 'Anatomia Humana General', 4, 2, 41), " +
                "('CFI-1130', 'Histologia Y Embriologia', 4, 2, 41), " +
                "('LET-0120', 'Lengua Espanola Basica II', 3, 2, 41), " +
                "('ODO-1110', 'Anatomia Dental Y Oclusion', 4, 2, 41), " +
                "('QUI-1210', 'Quimica General', 5, 2, 41), " +
                "('SAP-1050', 'Introd A La Salud Publica', 3, 2, 41), " +
                // Semestre 3
                "('CFI-1210', 'Fisiologia Humana', 4, 3, 41), " +
                "('CFI-2310', 'Bioquimica General', 5, 3, 41), " +
                "('MIP-1110', 'Microbiologia Y Parasitologia', 4, 3, 41), " +
                "('ODO-1210', 'Materiales Dentales I', 4, 3, 41), " +
                "('ODO-1310', 'Anatomia Cabeza Y Cuello', 4, 3, 41), " +
                // Semestre 4
                "('CFI-3110', 'Farmacologia General Y Odont', 4, 4, 41), " +
                "('MED-1210', 'Patologia General Y Bucal', 4, 4, 41), " +
                "('ODO-1220', 'Materiales Dentales II', 4, 4, 41), " +
                "('ODO-2110', 'Radiologia Oral Y Maxilofacial', 4, 4, 41), " +
                "('ODO-2210', 'Operatoria Dental I', 5, 4, 41), " +
                // Semestre 5
                "('ODO-2220', 'Operatoria Dental II', 5, 5, 41), " +
                "('ODO-2310', 'Periodoncia I', 4, 5, 41), " +
                "('ODO-2410', 'Endodoncia I', 4, 5, 41), " +
                "('ODO-2510', 'Cirugia Bucal I', 4, 5, 41), " +
                "('ODO-2610', 'Protesis Dental I', 5, 5, 41), " +
                // Semestre 6
                "('ODO-2320', 'Periodoncia II', 4, 6, 41), " +
                "('ODO-2420', 'Endodoncia II', 4, 6, 41), " +
                "('ODO-2520', 'Cirugia Bucal II', 4, 6, 41), " +
                "('ODO-2620', 'Protesis Dental II', 5, 6, 41), " +
                "('ODO-3110', 'Odontopediatria I', 4, 6, 41), " +
                // Semestre 7
                "('ODO-3120', 'Odontopediatria II', 4, 7, 41), " +
                "('ODO-3210', 'Ortodoncia Y Ortopedia Maxilar', 4, 7, 41), " +
                "('ODO-3310', 'Clinica Integral Odont I', 6, 7, 41), " +
                "('ODO-3410', 'Bioetica Y Odontologia Legal', 2, 7, 41), " +
                "('SAP-3110', 'Metodologia Inv En Salud', 3, 7, 41), " +
                // Semestre 8
                "('ODO-3320', 'Clinica Integral Odont II', 8, 8, 41), " +
                "('ODO-4510', 'Internado Rural Y Comunitario', 6, 8, 41), " +
                "('ODO-7010', 'Tesis De Grado O Curso Equival', 8, 8, 41)");

        
        //  Tecnico Superior en Atencion Prehospitalaria (carrera_id = 42)
        stmt.execute("INSERT INTO pensum (codigo, nombre, creditos, semestre, carrera_id) VALUES " +
                "('BIO-0140', 'Biologia Basica', 3, 1, 42), " +
                "('DOP-1000', 'Orientacion Institucional', 1, 1, 42), " +
                "('EFS-0110', 'Educacion Fisica', 1, 1, 42), " +
                "('FIL-0110', 'Int A La Filosofia', 3, 1, 42), " +
                "('HIS-0110', 'Fund De His Social Dominicana', 3, 1, 42), " +
                "('LET-0110', 'Lengua Espanola Basica I', 3, 1, 42), " +
                "('SAP-1110', 'Introd Atencion Prehospital', 3, 1, 42), " +
                // Semestre 2
                "('CFI-1110', 'Anatomia Y Fisiologia Basica', 4, 2, 42), " +
                "('LET-0120', 'Lengua Espanola Basica II', 3, 2, 42), " +
                "('SAP-1050', 'Introd A La Salud Publica', 3, 2, 42), " +
                "('SAP-1210', 'Soporte Vital Basico', 4, 2, 42), " +
                "('SAP-1310', 'Evaluacion Primaria Paciente', 4, 2, 42), " +
                // Semestre 3
                "('SAP-2120', 'Farmacologia De Emergencias', 4, 3, 42), " +
                "('SAP-2210', 'Soporte Vital Avanzado', 5, 3, 42), " +
                "('SAP-2310', 'Atencion Al Paciente Trauma', 5, 3, 42), " +
                "('SAP-2410', 'Emergencias Medicas Clinicas', 4, 3, 42), " +
                // Semestre 4
                "('SAP-3120', 'Emergencias Pediatricas Y Obst', 4, 4, 42), " +
                "('SAP-3210', 'Rescate Y Extricacion Vehicular', 4, 4, 42), " +
                "('SAP-3310', 'Gestion De Desastres Y Triage', 4, 4, 42), " +
                "('SAP-3410', 'Comunicaciones Y Despacho Med', 3, 4, 42), " +
                // Semestre 5
                "('SAP-4110', 'Bioetica Y Legislacion Prehosp', 2, 5, 42), " +
                "('SAP-4510', 'Practica Supervisada Ambulancia', 8, 5, 42), " +
                "('SAP-5010', 'Proyecto Final Tecnico', 4, 5, 42)");

        
        // Inicia la Facultad de Ciencias agronomicas y Vaterinarias
        
        
        //  Ingenieria Agronomica (carrera_id = 43)
        stmt.execute("INSERT INTO pensum (codigo, nombre, creditos, semestre, carrera_id) VALUES " +
                "('BIO-0140', 'Biologia Basica', 3, 1, 43), " +
                "('DOP-1000', 'Orientacion Institucional', 1, 1, 43), " +
                "('EFS-0110', 'Educacion Fisica', 1, 1, 43), " +
                "('FIL-0110', 'Int A La Filosofia', 3, 1, 43), " +
                "('FIS-0140', 'Fisica Basica', 4, 1, 43), " +
                "('HIS-0110', 'Fund De His Social Dominicana', 3, 1, 43), " +
                "('LET-0110', 'Lengua Espanola Basica I', 3, 1, 43), " +
                "('MAT-0140', 'Matematica Basica', 4, 1, 43), " +
                "('SOC-0110', 'Int A Las Ciencias Sociales', 2, 1, 43), " +
                // Semestre 2
                "('AGP-1110', 'Introd A La Agronomia', 3, 2, 43), " +
                "('BIO-1110', 'Botanica General', 4, 2, 43), " +
                "('LET-0120', 'Lengua Espanola Basica II', 3, 2, 43), " +
                "('MAT-2500', 'Analitica Y Calculo I', 5, 2, 43), " +
                "('QUI-1210', 'Quimica General', 5, 2, 43), " +
                "('SOC-1100', 'Int A La Sociologia General', 4, 2, 43), " +
                // Semestre 3
                "('AGM-1300', 'Topografia General', 4, 3, 43), " +
                "('BIO-2110', 'Botanica Sistematica', 4, 3, 43), " +
                "('EST-1110', 'Estadistica General', 4, 3, 43), " +
                "('FIS-2440', 'Agrometeorologia', 3, 3, 43), " +
                "('IAG-1140', 'Edafologia I', 4, 3, 43), " +
                "('QUI-2110', 'Quimica Organica', 5, 3, 43), " +
                // Semestre 4
                "('AGP-2110', 'Fisiologia Vegetal', 4, 4, 43), " +
                "('AGP-2210', 'Genetica General', 4, 4, 43), " +
                "('CFI-2310', 'Bioquimica General', 5, 4, 43), " +
                "('IAG-1150', 'Edafologia II Y Fertilidad', 4, 4, 43), " +
                "('IAG-2110', 'Mecanizacion Agricola', 4, 4, 43), " +
                // Semestre 5
                "('AGP-2310', 'Entomologia Agricola', 4, 5, 43), " +
                "('AGP-2410', 'Fitopatologia General', 4, 5, 43), " +
                "('AGP-3110', 'Fitomejoramiento', 4, 5, 43), " +
                "('EST-5540', 'Diseno Experimental Agricola', 4, 5, 43), " +
                "('IAG-2210', 'Riego Y Drenaje', 4, 5, 43), " +
                // Semestre 6
                "('AGP-3210', 'Cultivos De Ciclo Corto', 4, 6, 43), " +
                "('AGP-3310', 'Fruticultura General', 4, 6, 43), " +
                "('AGP-3410', 'Horticultura', 4, 6, 43), " +
                "('AGP-3510', 'Control De Malezas', 3, 6, 43), " +
                "('ECN-2330', 'Economia Agricola', 4, 6, 43), " +
                // Semestre 7
                "('AGP-4110', 'Cultivos Industriales Y Perennes', 4, 7, 43), " +
                "('AGP-4210', 'Manejo Integrado De Plagas', 4, 7, 43), " +
                "('AGP-4310', 'Agroecologia Y Sostenibilidad', 3, 7, 43), " +
                "('AGP-4410', 'Extension Rural', 3, 7, 43), " +
                "('IAG-3110', 'Construcciones Rurales', 3, 7, 43), " +
                // Semestre 8
                "('ADM-4740', 'Administracion Empresas Agropec', 4, 8, 43), " +
                "('AGP-4510', 'Pasantia Agronomica', 6, 8, 43), " +
                "('AGP-5110', 'Seminario De Agronomia', 3, 8, 43), " +
                "('AGP-7010', 'Tesis De Grado O Curso Equival', 8, 8, 43)");

        
        //  Licenciatura en Medicina Veterinaria (carrera_id = 44)
        stmt.execute("INSERT INTO pensum (codigo, nombre, creditos, semestre, carrera_id) VALUES " +
                "('BIO-0140', 'Biologia Basica', 3, 1, 44), " +
                "('DOP-1000', 'Orientacion Institucional', 1, 1, 44), " +
                "('EFS-0110', 'Educacion Fisica', 1, 1, 44), " +
                "('FIL-0110', 'Int A La Filosofia', 3, 1, 44), " +
                "('FIS-0140', 'Fisica Basica', 4, 1, 44), " +
                "('HIS-0110', 'Fund De His Social Dominicana', 3, 1, 44), " +
                "('LET-0110', 'Lengua Espanola Basica I', 3, 1, 44), " +
                "('MAT-0140', 'Matematica Basica', 4, 1, 44), " +
                "('SOC-0110', 'Int A Las Ciencias Sociales', 2, 1, 44), " +
                // Semestre 2
                "('BIO-1210', 'Zoologia General', 4, 2, 44), " +
                "('LET-0120', 'Lengua Espanola Basica II', 3, 2, 44), " +
                "('QUI-1210', 'Quimica General', 5, 2, 44), " +
                "('VET-1110', 'Anatomia Veterinaria I', 5, 2, 44), " +
                "('VET-1210', 'Histologia Y Embriologia Vet', 4, 2, 44), " +
                "('ZOO-1110', 'Introd A La Zootecnia', 3, 2, 44), " +
                // Semestre 3
                "('CFI-2310', 'Bioquimica Veterinaria', 5, 3, 44), " +
                "('EST-1110', 'Estadistica General', 4, 3, 44), " +
                "('QUI-2110', 'Quimica Organica', 5, 3, 44), " +
                "('VET-1120', 'Anatomia Veterinaria II', 5, 3, 44), " +
                "('VET-1310', 'Fisiologia Veterinaria I', 4, 3, 44), " +
                // Semestre 4
                "('MIP-1110', 'Microbiologia Veterinaria', 4, 4, 44), " +
                "('VET-1320', 'Fisiologia Veterinaria II', 4, 4, 44), " +
                "('VET-2110', 'Parasitologia Veterinaria', 4, 4, 44), " +
                "('VET-2210', 'Patologia General Veterinaria', 4, 4, 44), " +
                "('ZOO-2110', 'Nutricion Animal', 4, 4, 44), " +
                // Semestre 5
                "('VET-2220', 'Patologia Sistemica Veterinaria', 4, 5, 44), " +
                "('VET-2310', 'Farmacologia Veterinaria', 5, 5, 44), " +
                "('VET-2410', 'Semiologia Veterinaria', 4, 5, 44), " +
                "('VET-2510', 'Inmunologia Veterinaria', 3, 5, 44), " +
                "('ZOO-2210', 'Genetica Y Mejoramiento Animal', 4, 5, 44), " +
                // Semestre 6
                "('VET-3110', 'Enfermedades Infecciosas', 5, 6, 44), " +
                "('VET-3210', 'Enfermedades Parasitarias', 4, 6, 44), " +
                "('VET-3310', 'Cirugia Veterinaria I', 5, 6, 44), " +
                "('VET-3410', 'Reproduccion Animal E Insem', 4, 6, 44), " +
                "('VET-3510', 'Imagenologia Veterinaria', 3, 6, 44), " +
                // Semestre 7
                "('VET-3320', 'Cirugia Veterinaria II', 5, 7, 44), " +
                "('VET-4110', 'Clinica De Pequenos Animales', 5, 7, 44), " +
                "('VET-4210', 'Clinica De Grandes Animales', 5, 7, 44), " +
                "('VET-4310', 'Salud Publica Veterinaria', 4, 7, 44), " +
                "('VET-4410', 'Inspeccion De Alimentos Origen An', 4, 7, 44), " +
                // Semestre 8
                "('VET-4510', 'Internado Clinico Veterinario', 10, 8, 44), " +
                "('VET-5110', 'Seminario Medicina Veterinaria', 3, 8, 44), " +
                "('VET-7010', 'Tesis De Grado O Curso Equival', 8, 8, 44)");

        
        //  Ingenieria en Zootecnia (carrera_id = 45)
        stmt.execute("INSERT INTO pensum (codigo, nombre, creditos, semestre, carrera_id) VALUES " +
                "('BIO-0140', 'Biologia Basica', 3, 1, 45), " +
                "('DOP-1000', 'Orientacion Institucional', 1, 1, 45), " +
                "('EFS-0110', 'Educacion Fisica', 1, 1, 45), " +
                "('FIL-0110', 'Int A La Filosofia', 3, 1, 45), " +
                "('FIS-0140', 'Fisica Basica', 4, 1, 45), " +
                "('HIS-0110', 'Fund De His Social Dominicana', 3, 1, 45), " +
                "('LET-0110', 'Lengua Espanola Basica I', 3, 1, 45), " +
                "('MAT-0140', 'Matematica Basica', 4, 1, 45), " +
                "('SOC-0110', 'Int A Las Ciencias Sociales', 2, 1, 45), " +
                // Semestre 2
                "('BIO-1210', 'Zoologia General', 4, 2, 45), " +
                "('LET-0120', 'Lengua Espanola Basica II', 3, 2, 45), " +
                "('MAT-2500', 'Analitica Y Calculo I', 5, 2, 45), " +
                "('QUI-1210', 'Quimica General', 5, 2, 45), " +
                "('SOC-1100', 'Int A La Sociologia General', 4, 2, 45), " +
                "('ZOO-1110', 'Introd A La Zootecnia', 3, 2, 45), " +
                // Semestre 3
                "('CFI-2310', 'Bioquimica General', 5, 3, 45), " +
                "('EST-1110', 'Estadistica General', 4, 3, 45), " +
                "('IAG-1140', 'Suelos Y Pastos', 4, 3, 45), " +
                "('QUI-2110', 'Quimica Organica', 5, 3, 45), " +
                "('VET-1110', 'Anatomia Y Fisiologia Animal', 5, 3, 45), " +
                // Semestre 4
                "('EST-5540', 'Diseno Experimental Pecuario', 4, 4, 45), " +
                "('ZOO-2110', 'Nutricion Animal I', 4, 4, 45), " +
                "('ZOO-2210', 'Genetica Animal', 4, 4, 45), " +
                "('ZOO-2310', 'Pastos Y Forrajes', 4, 4, 45), " +
                "('ZOO-2410', 'Bioclimatologia Pecuaria', 3, 4, 45), " +
                // Semestre 5
                "('VET-2520', 'Sanidad Animal Y Profilaxis', 4, 5, 45), " +
                "('ZOO-2120', 'Nutricion Animal II Y Bromatol', 4, 5, 45), " +
                "('ZOO-2220', 'Mejoramiento Genetico Animal', 4, 5, 45), " +
                "('ZOO-3110', 'Reproduccion Animal', 4, 5, 45), " +
                "('ZOO-3210', 'Avicultura', 4, 5, 45), " +
                // Semestre 6
                "('ECN-2330', 'Economia Pecuaria', 4, 6, 45), " +
                "('IAG-3110', 'Instalaciones Pecuarias', 3, 6, 45), " +
                "('ZOO-3310', 'Produccion Bovina De Leche', 4, 6, 45), " +
                "('ZOO-3320', 'Produccion Bovina De Carne', 4, 6, 45), " +
                "('ZOO-3410', 'Porcinocultura', 4, 6, 45), " +
                // Semestre 7
                "('ZOO-4110', 'Ovinocultura Y Caprinocultura', 4, 7, 45), " +
                "('ZOO-4210', 'Apicultura Y Cunicultura', 3, 7, 45), " +
                "('ZOO-4310', 'Tecnologia De Productos Pecuarios', 4, 7, 45), " +
                "('ZOO-4410', 'Acuicultura Basica', 3, 7, 45), " +
                "('ZOO-4420', 'Extension Pecuaria', 3, 7, 45), " +
                // Semestre 8
                "('ADM-4740', 'Administracion Empresas Pecuarias', 4, 8, 45), " +
                "('ZOO-4510', 'Pasantia Zootecnica', 6, 8, 45), " +
                "('ZOO-5110', 'Seminario De Zootecnia', 3, 8, 45), " +
                "('ZOO-7010', 'Tesis De Grado O Curso Equival', 8, 8, 45)");

        
        // Inicia la Facultad de Humanidades
        
        
        //  Licenciatura en Comunicacion Social, mencion Periodismo (carrera_id = 46)
        stmt.execute("INSERT INTO pensum (codigo, nombre, creditos, semestre, carrera_id) VALUES " +
                "('BIO-0140', 'Biologia Basica', 3, 1, 46), " +
                "('DOP-1000', 'Orientacion Institucional', 1, 1, 46), " +
                "('EFS-0110', 'Educacion Fisica', 1, 1, 46), " +
                "('FIL-0110', 'Int A La Filosofia', 3, 1, 46), " +
                "('FIS-0140', 'Fisica Basica', 4, 1, 46), " +
                "('HIS-0110', 'Fund De His Social Dominicana', 3, 1, 46), " +
                "('LET-0110', 'Lengua Espanola Basica I', 3, 1, 46), " +
                "('MAT-0140', 'Matematica Basica', 4, 1, 46), " +
                "('SOC-0110', 'Int A Las Ciencias Sociales', 2, 1, 46), " +
                // Semestre 2
                "('COM-1110', 'Teoria De La Comunicacion I', 3, 2, 46), " +
                "('COM-1120', 'Historia De La Comunicacion', 3, 2, 46), " +
                "('INF-1030', 'Introduccion A La Informatica', 4, 2, 46), " +
                "('LET-0120', 'Lengua Espanola Basica II', 3, 2, 46), " +
                "('PSI-1050', 'Int A La Psicologia', 3, 2, 46), " +
                "('QUI-0140', 'Quimica Basica', 3, 2, 46), " +
                "('SOC-1100', 'Int A La Sociologia General', 4, 2, 46), " +
                // Semestre 3
                "('COM-1130', 'Teoria De La Comunicacion II', 3, 3, 46), " +
                "('COM-1210', 'Redaccion Periodistica I', 4, 3, 46), " +
                "('COM-1310', 'Fotoperiodismo', 3, 3, 46), " +
                "('ECN-2110', 'Introduccion A La Economia', 4, 3, 46), " +
                "('EST-1110', 'Estadistica General', 4, 3, 46), " +
                "('LET-2410', 'Redaccion Y Estilo', 3, 3, 46), " +
                // Semestre 4
                "('COM-2110', 'Comunicacion Y Sociedad', 3, 4, 46), " +
                "('COM-2210', 'Redaccion Periodistica II', 4, 4, 46), " +
                "('COM-2220', 'Generos Interpretativos Y Opin', 4, 4, 46), " +
                "('COM-2310', 'Periodismo Radiofonico I', 4, 4, 46), " +
                "('POL-1110', 'Introd A Las Ciencias Politicas', 4, 4, 46), " +
                "('SOC-1270', 'Metodos Y Tec De Investigacion', 3, 4, 46), " +
                // Semestre 5
                "('COM-2320', 'Periodismo Radiofonico II', 4, 5, 46), " +
                "('COM-3110', 'Periodismo Televisivo I', 4, 5, 46), " +
                "('COM-3210', 'Diseno Y Diagramacion Prensa', 3, 5, 46), " +
                "('COM-3310', 'Opinion Publica Y Propaganda', 3, 5, 46), " +
                "('COM-3410', 'Metodologia Inv Comunicacion', 4, 5, 46), " +
                // Semestre 6
                "('COM-3120', 'Periodismo Televisivo II', 4, 6, 46), " +
                "('COM-3220', 'Periodismo Digital Y Multimedia', 4, 6, 46), " +
                "('COM-3510', 'Periodismo De Investigacion', 4, 6, 46), " +
                "('COM-3610', 'Relaciones Publicas', 3, 6, 46), " +
                "('DER-3350', 'Legislacion De Prensa Y Com', 3, 6, 46), " +
                // Semestre 7
                "('COM-4110', 'Gestion De Medios Comunicacion', 3, 7, 46), " +
                "('COM-4210', 'Periodismo Especializado', 4, 7, 46), " +
                "('COM-4310', 'Etica Y Deontologia Periodist', 2, 7, 46), " +
                "('COM-4510', 'Practica Profesional Periodismo', 5, 7, 46), " +
                "('PUB-1110', 'Fundamentos De Publicidad', 3, 7, 46), " +
                // Semestre 8
                "('COM-4220', 'Taller De Produccion Informativa', 5, 8, 46), " +
                "('COM-5110', 'Seminario De Comunicacion Soc', 3, 8, 46), " +
                "('COM-7010', 'Tesis De Grado O Curso Equival', 8, 8, 46)");

        
        //  Licenciatura en Filosofia (carrera_id = 47)
        stmt.execute("INSERT INTO pensum (codigo, nombre, creditos, semestre, carrera_id) VALUES " +
                "('BIO-0140', 'Biologia Basica', 3, 1, 47), " +
                "('DOP-1000', 'Orientacion Institucional', 1, 1, 47), " +
                "('EFS-0110', 'Educacion Fisica', 1, 1, 47), " +
                "('FIL-0110', 'Int A La Filosofia', 3, 1, 47), " +
                "('FIS-0140', 'Fisica Basica', 4, 1, 47), " +
                "('HIS-0110', 'Fund De His Social Dominicana', 3, 1, 47), " +
                "('LET-0110', 'Lengua Espanola Basica I', 3, 1, 47), " +
                "('MAT-0140', 'Matematica Basica', 4, 1, 47), " +
                "('SOC-0110', 'Int A Las Ciencias Sociales', 2, 1, 47), " +
                // Semestre 2
                "('FIL-1110', 'Historia Filosofia Antigua I', 4, 2, 47), " +
                "('FIL-1210', 'Logica Formal', 3, 2, 47), " +
                "('IDI-1110', 'Griego Clasico I', 4, 2, 47), " +
                "('LET-0120', 'Lengua Espanola Basica II', 3, 2, 47), " +
                "('PSI-1050', 'Int A La Psicologia', 3, 2, 47), " +
                "('QUI-0140', 'Quimica Basica', 3, 2, 47), " +
                "('SOC-1100', 'Int A La Sociologia General', 4, 2, 47), " +
                // Semestre 3
                "('FIL-1120', 'Historia Filosofia Antigua II', 4, 3, 47), " +
                "('FIL-1220', 'Logica Simbolica', 3, 3, 47), " +
                "('FIL-1310', 'Antropologia Filosofica', 3, 3, 47), " +
                "('IDI-1120', 'Latin Clasico I', 4, 3, 47), " +
                "('LET-1310', 'Int Estudio De La Literatura', 3, 3, 47), " +
                "('SOC-1270', 'Metodos Y Tec De Investigacion', 3, 3, 47), " +
                // Semestre 4
                "('FIL-2110', 'Historia Filosofia Medieval', 4, 4, 47), " +
                "('FIL-2210', 'Teoria Del Conocimiento', 4, 4, 47), " +
                "('FIL-2310', 'Etica Filosofica I', 3, 4, 47), " +
                "('FIL-2410', 'Metafisica Y Ontologia I', 4, 4, 47), " +
                "('CRI-2110', 'Estetica Filosofica', 3, 4, 47), " +
                // Semestre 5
                "('FIL-2120', 'Historia Filosofia Moderna I', 4, 5, 47), " +
                "('FIL-2320', 'Etica Filosofica II', 3, 5, 47), " +
                "('FIL-2420', 'Metafisica Y Ontologia II', 4, 5, 47), " +
                "('FIL-3110', 'Filosofia De La Ciencia', 4, 5, 47), " +
                "('FIL-3210', 'Filosofia Politica', 3, 5, 47), " +
                // Semestre 6
                "('FIL-2130', 'Historia Filosofia Moderna II', 4, 6, 47), " +
                "('FIL-3120', 'Filosofia Contemporanea I', 4, 6, 47), " +
                "('FIL-3310', 'Filosofia Del Lenguaje', 3, 6, 47), " +
                "('FIL-3410', 'Filosofia De La Historia', 3, 6, 47), " +
                "('FIL-3510', 'Filosofia De La Religion', 3, 6, 47), " +
                // Semestre 7
                "('FIL-3130', 'Filosofia Contemporanea II', 4, 7, 47), " +
                "('FIL-4110', 'Pensamiento Filosofico Latinoam', 4, 7, 47), " +
                "('FIL-4120', 'Pensamiento Filosofico Dom', 3, 7, 47), " +
                "('FIL-4210', 'Hermeneutica Y Fenomenologia', 4, 7, 47), " +
                "('FIL-4310', 'Bioetica Filosofica', 3, 7, 47), " +
                // Semestre 8
                "('FIL-4410', 'Filosofia De La Cultura', 3, 8, 47), " +
                "('FIL-5110', 'Seminario De Filosofia', 4, 8, 47), " +
                "('FIL-7010', 'Tesis De Grado O Curso Equival', 8, 8, 47)");

        
        //  Licenciatura en Lenguas Modernas, mencion Ingles (carrera_id = 48)
        stmt.execute("INSERT INTO pensum (codigo, nombre, creditos, semestre, carrera_id) VALUES " +
                "('BIO-0140', 'Biologia Basica', 3, 1, 48), " +
                "('DOP-1000', 'Orientacion Institucional', 1, 1, 48), " +
                "('EFS-0110', 'Educacion Fisica', 1, 1, 48), " +
                "('FIL-0110', 'Int A La Filosofia', 3, 1, 48), " +
                "('FIS-0140', 'Fisica Basica', 4, 1, 48), " +
                "('HIS-0110', 'Fund De His Social Dominicana', 3, 1, 48), " +
                "('LET-0110', 'Lengua Espanola Basica I', 3, 1, 48), " +
                "('MAT-0140', 'Matematica Basica', 4, 1, 48), " +
                "('SOC-0110', 'Int A Las Ciencias Sociales', 2, 1, 48), " +
                // Semestre 2
                "('IDI-1210', 'Ingles Basico I', 6, 2, 48), " +
                "('IDI-1310', 'Fonetica Y Fonologia Inglesa I', 3, 2, 48), " +
                "('INF-1030', 'Introduccion A La Informatica', 4, 2, 48), " +
                "('LET-0120', 'Lengua Espanola Basica II', 3, 2, 48), " +
                "('LET-1110', 'Linguistica General I', 3, 2, 48), " +
                "('QUI-0140', 'Quimica Basica', 3, 2, 48), " +
                // Semestre 3
                "('IDI-1220', 'Ingles Basico II', 6, 3, 48), " +
                "('IDI-1320', 'Fonetica Y Fonologia Inglesa II', 3, 3, 48), " +
                "('IDI-1410', 'Gramatica Inglesa I', 4, 3, 48), " +
                "('IDI-2110', 'Frances Elemental I', 4, 3, 48), " +
                "('LET-1120', 'Linguistica General II', 3, 3, 48), " +
                // Semestre 4
                "('IDI-2210', 'Ingles Intermedio I', 6, 4, 48), " +
                "('IDI-2310', 'Composicion Y Redaccion Ing I', 4, 4, 48), " +
                "('IDI-2420', 'Gramatica Inglesa II', 4, 4, 48), " +
                "('IDI-2120', 'Frances Elemental II', 4, 4, 48), " +
                "('SOC-1270', 'Metodos Y Tec De Investigacion', 3, 4, 48), " +
                // Semestre 5
                "('IDI-2220', 'Ingles Intermedio II', 6, 5, 48), " +
                "('IDI-2320', 'Composicion Y Redaccion Ing II', 4, 5, 48), " +
                "('IDI-3110', 'Literatura Inglesa I', 3, 5, 48), " +
                "('IDI-3210', 'Cultura Y Civilizacion Anglosaj', 3, 5, 48), " +
                "('PED-2110', 'Didactica General', 4, 5, 48), " +
                // Semestre 6
                "('IDI-3310', 'Ingles Avanzado I', 6, 6, 48), " +
                "('IDI-3410', 'Traduccion Ingles Espanol I', 4, 6, 48), " +
                "('IDI-3510', 'Literatura Norteamericana', 3, 6, 48), " +
                "('IDI-3610', 'Linguistica Aplicada Al Ingles', 3, 6, 48), " +
                "('IDI-3710', 'Metodologia Ensenanza Ingles I', 4, 6, 48), " +
                // Semestre 7
                "('IDI-3320', 'Ingles Avanzado II', 6, 7, 48), " +
                "('IDI-3420', 'Traduccion Espanol Ingles II', 4, 7, 48), " +
                "('IDI-3720', 'Metodologia Ensenanza Ingles II', 4, 7, 48), " +
                "('IDI-4110', 'Ingles Para Fines Especificos', 3, 7, 48), " +
                "('IDI-4210', 'Practica Docente Lenguas Mod', 4, 7, 48), " +
                // Semestre 8
                "('IDI-4310', 'Interpretacion Consecutiva', 4, 8, 48), " +
                "('IDI-5110', 'Seminario De Lenguas Modernas', 3, 8, 48), " +
                "('IDI-7010', 'Tesis De Grado O Curso Equival', 8, 8, 48)");

        
        //  Licenciatura en Letras (carrera_id = 49)
        stmt.execute("INSERT INTO pensum (codigo, nombre, creditos, semestre, carrera_id) VALUES " +
                "('BIO-0140', 'Biologia Basica', 3, 1, 49), " +
                "('DOP-1000', 'Orientacion Institucional', 1, 1, 49), " +
                "('EFS-0110', 'Educacion Fisica', 1, 1, 49), " +
                "('FIL-0110', 'Int A La Filosofia', 3, 1, 49), " +
                "('FIS-0140', 'Fisica Basica', 4, 1, 49), " +
                "('HIS-0110', 'Fund De His Social Dominicana', 3, 1, 49), " +
                "('LET-0110', 'Lengua Espanola Basica I', 3, 1, 49), " +
                "('MAT-0140', 'Matematica Basica', 4, 1, 49), " +
                "('SOC-0110', 'Int A Las Ciencias Sociales', 2, 1, 49), " +
                // Semestre 2
                "('IDI-1120', 'Latin Clasico I', 4, 2, 49), " +
                "('LET-0120', 'Lengua Espanola Basica II', 3, 2, 49), " +
                "('LET-1110', 'Linguistica General I', 3, 2, 49), " +
                "('LET-1210', 'Gramatica Espanola I', 4, 2, 49), " +
                "('LET-1310', 'Int Estudio De La Literatura', 3, 2, 49), " +
                "('QUI-0140', 'Quimica Basica', 3, 2, 49), " +
                "('SOC-1100', 'Int A La Sociologia General', 4, 2, 49), " +
                // Semestre 3
                "('IDI-1130', 'Latin Clasico II', 4, 3, 49), " +
                "('LET-1120', 'Linguistica General II', 3, 3, 49), " +
                "('LET-1220', 'Gramatica Espanola II', 4, 3, 49), " +
                "('LET-1410', 'Fonetica Y Fonologia Espanola', 3, 3, 49), " +
                "('LET-2310', 'Literatura Espanola I', 4, 3, 49), " +
                "('SOC-1270', 'Metodos Y Tec De Investigacion', 3, 3, 49), " +
                // Semestre 4
                "('LET-2110', 'Morfosintaxis Espanola I', 4, 4, 49), " +
                "('LET-2210', 'Historia De La Lengua Espanola', 3, 4, 49), " +
                "('LET-2320', 'Literatura Espanola II', 4, 4, 49), " +
                "('LET-2410', 'Redaccion Y Estilo', 3, 4, 49), " +
                "('LET-2510', 'Teoria Literaria I', 3, 4, 49), " +
                // Semestre 5
                "('LET-2120', 'Morfosintaxis Espanola II', 4, 5, 49), " +
                "('LET-2520', 'Teoria Literaria II', 3, 5, 49), " +
                "('LET-3110', 'Semantica Y Lexicologia', 3, 5, 49), " +
                "('LET-3310', 'Literatura Hispanoamericana I', 4, 5, 49), " +
                "('LET-3410', 'Literatura Dominicana I', 4, 5, 49), " +
                // Semestre 6
                "('LET-3210', 'Espanol De America Y Dominicano', 4, 6, 49), " +
                "('LET-3320', 'Literatura Hispanoamericana II', 4, 6, 49), " +
                "('LET-3420', 'Literatura Dominicana II', 4, 6, 49), " +
                "('LET-3510', 'Literatura Universal I', 4, 6, 49), " +
                "('LET-3610', 'Sociolinguistica', 3, 6, 49), " +
                // Semestre 7
                "('LET-3520', 'Literatura Universal II', 4, 7, 49), " +
                "('LET-4110', 'Analisis Y Critica Literaria', 4, 7, 49), " +
                "('LET-4210', 'Taller De Creacion Literaria', 3, 7, 49), " +
                "('LET-4310', 'Edicion Y Correccion De Textos', 3, 7, 49), " +
                "('LET-4410', 'Metodologia Inv Linguistica', 3, 7, 49), " +
                // Semestre 8
                "('LET-4510', 'Seminario De Letras', 4, 8, 49), " +
                "('LET-7010', 'Tesis De Grado O Curso Equival', 8, 8, 49)");

        
        //  Licenciatura en Psicologia Clinica (carrera_id = 50)
        stmt.execute("INSERT INTO pensum (codigo, nombre, creditos, semestre, carrera_id) VALUES " +
                "('BIO-0140', 'Biologia Basica', 3, 1, 50), " +
                "('DOP-1000', 'Orientacion Institucional', 1, 1, 50), " +
                "('EFS-0110', 'Educacion Fisica', 1, 1, 50), " +
                "('FIL-0110', 'Int A La Filosofia', 3, 1, 50), " +
                "('FIS-0140', 'Fisica Basica', 4, 1, 50), " +
                "('HIS-0110', 'Fund De His Social Dominicana', 3, 1, 50), " +
                "('LET-0110', 'Lengua Espanola Basica I', 3, 1, 50), " +
                "('MAT-0140', 'Matematica Basica', 4, 1, 50), " +
                "('SOC-0110', 'Int A Las Ciencias Sociales', 2, 1, 50), " +
                // Semestre 2
                "('EST-1110', 'Estadistica General', 4, 2, 50), " +
                "('LET-0120', 'Lengua Espanola Basica II', 3, 2, 50), " +
                "('PSI-1110', 'Psicologia General I', 4, 2, 50), " +
                "('PSI-1120', 'Historia De La Psicologia', 3, 2, 50), " +
                "('QUI-0140', 'Quimica Basica', 3, 2, 50), " +
                "('SOC-1100', 'Int A La Sociologia General', 4, 2, 50), " +
                // Semestre 3
                "('EST-2230', 'Estadistica Aplicada Psicologia', 4, 3, 50), " +
                "('PSI-1130', 'Psicologia Social I', 3, 3, 50), " +
                "('PSI-1210', 'Psicologia General II', 4, 3, 50), " +
                "('PSI-1310', 'Bases Biologicas Conducta I', 4, 3, 50), " +
                "('PSI-2110', 'Psicologia Del Desarrollo I', 4, 3, 50), " +
                // Semestre 4
                "('PSI-1320', 'Bases Biologicas Conducta II', 4, 4, 50), " +
                "('PSI-2120', 'Psicologia Del Desarrollo II', 4, 4, 50), " +
                "('PSI-2210', 'Teorias De La Personalidad', 4, 4, 50), " +
                "('PSI-2310', 'Psicologia Experimental', 4, 4, 50), " +
                "('PSI-2410', 'Metodologia Inv Psicologica', 4, 4, 50), " +
                // Semestre 5
                "('PSI-2510', 'Teoria De Los Tests Y Psicomet', 4, 5, 50), " +
                "('PSI-3110', 'Psicopatologia General I', 4, 5, 50), " +
                "('PSI-3210', 'Evaluacion De La Inteligencia', 4, 5, 50), " +
                "('PSI-3310', 'Tecnicas De Entrevista Clinica', 3, 5, 50), " +
                "('PSI-3410', 'Psicologia Del Aprendizaje', 4, 5, 50), " +
                // Semestre 6
                "('PSI-3120', 'Psicopatologia General II', 4, 6, 50), " +
                "('PSI-3220', 'Evaluacion De La Personalidad', 4, 6, 50), " +
                "('PSI-3510', 'Psicologia Clinica I', 4, 6, 50), " +
                "('PSI-3610', 'Psicofarmacologia Basica', 3, 6, 50), " +
                "('PSI-3710', 'Neuropsicologia Clinica', 4, 6, 50), " +
                // Semestre 7
                "('PSI-3520', 'Psicologia Clinica II', 4, 7, 50), " +
                "('PSI-4110', 'Psicoterapia Individual', 4, 7, 50), " +
                "('PSI-4120', 'Psicoterapia De Grupo Y Familia', 4, 7, 50), " +
                "('PSI-4210', 'Psicologia De La Salud', 3, 7, 50), " +
                "('PSI-4310', 'Etica Profesional Del Psicologo', 2, 7, 50), " +
                // Semestre 8
                "('PSI-4510', 'Practica Supervisada Clinica', 8, 8, 50), " +
                "('PSI-5110', 'Seminario Psicologia Clinica', 3, 8, 50), " +
                "('PSI-7010', 'Tesis De Grado O Curso Equival', 8, 8, 50)");

        
        //  Licenciatura en Psicologia Industrial (carrera_id = 51)
        stmt.execute("INSERT INTO pensum (codigo, nombre, creditos, semestre, carrera_id) VALUES " +
                "('BIO-0140', 'Biologia Basica', 3, 1, 51), " +
                "('DOP-1000', 'Orientacion Institucional', 1, 1, 51), " +
                "('EFS-0110', 'Educacion Fisica', 1, 1, 51), " +
                "('FIL-0110', 'Int A La Filosofia', 3, 1, 51), " +
                "('FIS-0140', 'Fisica Basica', 4, 1, 51), " +
                "('HIS-0110', 'Fund De His Social Dominicana', 3, 1, 51), " +
                "('LET-0110', 'Lengua Espanola Basica I', 3, 1, 51), " +
                "('MAT-0140', 'Matematica Basica', 4, 1, 51), " +
                "('SOC-0110', 'Int A Las Ciencias Sociales', 2, 1, 51), " +
                // Semestre 2
                "('ADM-1150', 'Teoria Administrativa', 4, 2, 51), " +
                "('EST-1110', 'Estadistica General', 4, 2, 51), " +
                "('LET-0120', 'Lengua Espanola Basica II', 3, 2, 51), " +
                "('PSI-1110', 'Psicologia General I', 4, 2, 51), " +
                "('PSI-1120', 'Historia De La Psicologia', 3, 2, 51), " +
                "('SOC-1100', 'Int A La Sociologia General', 4, 2, 51), " +
                // Semestre 3
                "('EST-2230', 'Estadistica Aplicada Psicologia', 4, 3, 51), " +
                "('PSI-1130', 'Psicologia Social I', 3, 3, 51), " +
                "('PSI-1210', 'Psicologia General II', 4, 3, 51), " +
                "('PSI-1310', 'Bases Biologicas Conducta', 4, 3, 51), " +
                "('PSI-2110', 'Psicologia Del Desarrollo', 4, 3, 51), " +
                // Semestre 4
                "('ADM-2450', 'Adm De Recursos Humanos', 4, 4, 51), " +
                "('PSI-2210', 'Teorias De La Personalidad', 4, 4, 51), " +
                "('PSI-2310', 'Psicologia Experimental', 4, 4, 51), " +
                "('PSI-2410', 'Metodologia Inv Psicologica', 4, 4, 51), " +
                "('PSI-2610', 'Psicologia Industrial Y Org I', 4, 4, 51), " +
                // Semestre 5
                "('PSI-2510', 'Teoria De Los Tests Y Psicomet', 4, 5, 51), " +
                "('PSI-2620', 'Psicologia Industrial Y Org II', 4, 5, 51), " +
                "('PSI-3210', 'Evaluacion De La Inteligencia', 4, 5, 51), " +
                "('PSI-3810', 'Analisis Y Valuacion De Puestos', 4, 5, 51), " +
                "('PSI-3820', 'Reclutamiento Y Seleccion Pers', 4, 5, 51), " +
                // Semestre 6
                "('DER-2150', 'Fundamentos De Derecho Laboral', 3, 6, 51), " +
                "('PSI-3220', 'Evaluacion De La Personalidad', 4, 6, 51), " +
                "('PSI-3830', 'Capacitacion Y Des Personal', 4, 6, 51), " +
                "('PSI-3840', 'Evaluacion Del Desempeno', 4, 6, 51), " +
                "('PSI-3850', 'Comportamiento Organizacional', 4, 6, 51), " +
                // Semestre 7
                "('PSI-4310', 'Etica Profesional Del Psicologo', 2, 7, 51), " +
                "('PSI-4610', 'Salud Ocupacional Y Ergonomia', 4, 7, 51), " +
                "('PSI-4620', 'Desarrollo Organizacional', 4, 7, 51), " +
                "('PSI-4630', 'Psicologia Del Consumidor', 3, 7, 51), " +
                "('PSI-4640', 'Gestion Del Talento Humano', 4, 7, 51), " +
                // Semestre 8
                "('PSI-4520', 'Practica Supervisada Industrial', 8, 8, 51), " +
                "('PSI-5120', 'Seminario Psicologia Industrial', 3, 8, 51), " +
                "('PSI-7020', 'Tesis De Grado O Curso Equival', 8, 8, 51)");
        
                // 60. Licenciatura en Historia (carrera_id = 60)
        stmt.execute("INSERT INTO pensum (codigo, nombre, creditos, semestre, carrera_id) VALUES " +
                "('BIO-0140', 'Biologia Basica', 3, 1, 60), " +
                "('DOP-1000', 'Orientacion Institucional', 1, 1, 60), " +
                "('EFS-0110', 'Educacion Fisica', 1, 1, 60), " +
                "('FIL-0110', 'Int A La Filosofia', 3, 1, 60), " +
                "('FIS-0140', 'Fisica Basica', 4, 1, 60), " +
                "('HIS-0110', 'Fund De His Social Dominicana', 3, 1, 60), " +
                "('LET-0110', 'Lengua Espanola Basica I', 3, 1, 60), " +
                "('MAT-0140', 'Matematica Basica', 4, 1, 60), " +
                "('SOC-0110', 'Int A Las Ciencias Sociales', 2, 1, 60), " +
                // Semestre 2
                "('ANT-1110', 'Antropologia General', 4, 2, 60), " +
                "('GEO-1010', 'Introduccion A La Geografia', 3, 2, 60), " +
                "('HIS-1110', 'Introduccion A La Historia', 3, 2, 60), " +
                "('HIS-1120', 'Prehistoria Y Antiguedad', 4, 2, 60), " +
                "('LET-0120', 'Lengua Espanola Basica II', 3, 2, 60), " +
                "('QUI-0140', 'Quimica Basica', 3, 2, 60), " +
                "('SOC-1100', 'Int A La Sociologia General', 4, 2, 60), " +
                // Semestre 3
                "('ECN-2110', 'Introduccion A La Economia', 4, 3, 60), " +
                "('HIS-1210', 'Historia Medieval', 4, 3, 60), " +
                "('HIS-1310', 'Historia De America Prehisp', 4, 3, 60), " +
                "('HIS-1410', 'Historia Dominicana I', 4, 3, 60), " +
                "('SOC-1270', 'Metodos Y Tec De Investigacion', 3, 3, 60), " +
                // Semestre 4
                "('GEO-2440', 'Geografia Historica Isla SD', 3, 4, 60), " +
                "('HIS-2110', 'Historia Moderna', 4, 4, 60), " +
                "('HIS-2210', 'Historia De America Colonial', 4, 4, 60), " +
                "('HIS-2310', 'Historia Dominicana II', 4, 4, 60), " +
                "('HIS-2410', 'Teoria Y Metodo De La Historia', 4, 4, 60), " +
                // Semestre 5
                "('HIS-2120', 'Historia Contemporanea I', 4, 5, 60), " +
                "('HIS-2220', 'Historia America Indep Y XIX', 4, 5, 60), " +
                "('HIS-2320', 'Historia Dominicana III', 4, 5, 60), " +
                "('HIS-3110', 'Historiografia General', 3, 5, 60), " +
                "('HIS-3210', 'Paleografia Y Archivistica', 3, 5, 60), " +
                // Semestre 6
                "('HIS-2130', 'Historia Contemporanea II', 4, 6, 60), " +
                "('HIS-3310', 'Historia America Siglo XX', 4, 6, 60), " +
                "('HIS-3410', 'Historia Dominicana IV', 4, 6, 60), " +
                "('HIS-3510', 'Historia Del Caribe', 4, 6, 60), " +
                "('HIS-3610', 'Historiografia Dominicana', 3, 6, 60), " +
                // Semestre 7
                "('HIS-4110', 'Historia Economica Y Social Dom', 4, 7, 60), " +
                "('HIS-4210', 'Historia De Las Ideas En Amer', 3, 7, 60), " +
                "('HIS-4310', 'Patrimonio Historico Y Cultural', 3, 7, 60), " +
                "('HIS-4410', 'Tecnicas De Inv Historica', 4, 7, 60), " +
                "('PED-2110', 'Didactica De La Historia', 3, 7, 60), " +
                // Semestre 8
                "('HIS-4510', 'Historia De Las Relaciones Int', 3, 8, 60), " +
                "('HIS-5110', 'Seminario De Historia', 4, 8, 60), " +
                "('HIS-7010', 'Tesis De Grado O Curso Equival', 8, 8, 60)");

        
                // 61. Licenciatura en Antropologia (carrera_id = 61)
        stmt.execute("INSERT INTO pensum (codigo, nombre, creditos, semestre, carrera_id) VALUES " +
                "('BIO-0140', 'Biologia Basica', 3, 1, 61), " +
                "('DOP-1000', 'Orientacion Institucional', 1, 1, 61), " +
                "('EFS-0110', 'Educacion Fisica', 1, 1, 61), " +
                "('FIL-0110', 'Int A La Filosofia', 3, 1, 61), " +
                "('FIS-0140', 'Fisica Basica', 4, 1, 61), " +
                "('HIS-0110', 'Fund De His Social Dominicana', 3, 1, 61), " +
                "('LET-0110', 'Lengua Espanola Basica I', 3, 1, 61), " +
                "('MAT-0140', 'Matematica Basica', 4, 1, 61), " +
                "('SOC-0110', 'Int A Las Ciencias Sociales', 2, 1, 61), " +
                // Semestre 2
                "('ANT-1110', 'Antropologia General', 4, 2, 61), " +
                "('ANT-1210', 'Antropologia Fisica Y Biolog', 4, 2, 61), " +
                "('HIS-1110', 'Introduccion A La Historia', 3, 2, 61), " +
                "('LET-0120', 'Lengua Espanola Basica II', 3, 2, 61), " +
                "('QUI-0140', 'Quimica Basica', 3, 2, 61), " +
                "('SOC-1100', 'Int A La Sociologia General', 4, 2, 61), " +
                // Semestre 3
                "('ANT-1310', 'Arqueologia General', 4, 3, 61), " +
                "('ANT-1410', 'Antropologia Social Y Cult I', 4, 3, 61), " +
                "('EST-1110', 'Estadistica General', 4, 3, 61), " +
                "('LET-1110', 'Linguistica Antropologica', 3, 3, 61), " +
                "('SOC-1270', 'Metodos Y Tec De Investigacion', 3, 3, 61), " +
                // Semestre 4
                "('ANT-1420', 'Antropologia Social Y Cult II', 4, 4, 61), " +
                "('ANT-2110', 'Arqueologia Del Caribe Y SD', 4, 4, 61), " +
                "('ANT-2210', 'Teoria Antropologica I', 4, 4, 61), " +
                "('ANT-2310', 'Etnografia General', 4, 4, 61), " +
                "('HIS-1410', 'Historia Dominicana I', 4, 4, 61), " +
                // Semestre 5
                "('ANT-2220', 'Teoria Antropologica II', 4, 5, 61), " +
                "('ANT-2410', 'Etnologia De America', 4, 5, 61), " +
                "('ANT-2510', 'Metodos Y Tec Inv Etnografica', 4, 5, 61), " +
                "('ANT-3110', 'Antropologia Economica', 3, 5, 61), " +
                "('ANT-3210', 'Folklore Y Cultura Popular Dom', 3, 5, 61), " +
                // Semestre 6
                "('ANT-3310', 'Antropologia Politica', 3, 6, 61), " +
                "('ANT-3410', 'Antropologia De La Religion', 3, 6, 61), " +
                "('ANT-3510', 'Arqueologia De Campo Y Lab', 4, 6, 61), " +
                "('ANT-3610', 'Antropologia Urbana Y Rural', 4, 6, 61), " +
                "('ANT-3710', 'Etnologia Dominicana Y Carib', 4, 6, 61), " +
                // Semestre 7
                "('ANT-4110', 'Antropologia Aplicada Y Des', 4, 7, 61), " +
                "('ANT-4210', 'Antropologia Medica', 3, 7, 61), " +
                "('ANT-4310', 'Museografia Y Patrimonio Cult', 4, 7, 61), " +
                "('ANT-4410', 'Taller De Investigacion Antrop', 4, 7, 61), " +
                // Semestre 8
                "('ANT-4510', 'Practica De Campo Antropolog', 6, 8, 61), " +
                "('ANT-5110', 'Seminario De Antropologia', 3, 8, 61), " +
                "('ANT-7010', 'Tesis De Grado O Curso Equival', 8, 8, 61)");


        // Inicia la Facultad de Ingenieria Y Arquitectura
        
        //  Licenciatura en Agrimensura (carrera_id = 52)
        stmt.execute("INSERT INTO pensum (codigo, nombre, creditos, semestre, carrera_id) VALUES " +
                "('AGM-1110', 'Introd A La Agrimensura', 2, 1, 52), " +
                "('ARQ-1010', 'Dibujo Tecnico I', 4, 1, 52), " +
                "('DOP-1000', 'Orientacion Institucional', 1, 1, 52), " +
                "('EFS-0110', 'Educacion Fisica', 1, 1, 52), " +
                "('FIL-0110', 'Int A La Filosofia', 3, 1, 52), " +
                "('FIS-0140', 'Fisica Basica', 4, 1, 52), " +
                "('HIS-0110', 'Fund De His Social Dominicana', 3, 1, 52), " +
                "('LET-0110', 'Lengua Espanola Basica I', 3, 1, 52), " +
                "('MAT-0140', 'Matematica Basica', 4, 1, 52), " +
                // Semestre 2
                "('AGM-1210', 'Topografia I', 4, 2, 52), " +
                "('FIS-1110', 'Fisica General I', 4, 2, 52), " +
                "('GEO-1120', 'Geologia General', 4, 2, 52), " +
                "('INF-1030', 'Introduccion A La Informatica', 4, 2, 52), " +
                "('LET-0120', 'Lengua Espanola Basica II', 3, 2, 52), " +
                "('MAT-2510', 'Calculo Y Analitica I', 6, 2, 52), " +
                // Semestre 3
                "('AGM-1220', 'Topografia II', 4, 3, 52), " +
                "('AGM-1310', 'Dibujo Topografico Y Cartograf', 4, 3, 52), " +
                "('EST-1110', 'Estadistica General', 4, 3, 52), " +
                "('FIS-1120', 'Fisica General II', 4, 3, 52), " +
                "('MAT-3500', 'Calculo Y Geom Analitica II', 6, 3, 52), " +
                // Semestre 4
                "('AGM-2110', 'Topografia III', 4, 4, 52), " +
                "('AGM-2210', 'Geodesia Geometrica', 4, 4, 52), " +
                "('AGM-2310', 'Fotogrametria I', 4, 4, 52), " +
                "('DER-3210', 'Derecho Inmobiliario Y Agrario', 3, 4, 52), " +
                "('GEO-1180', 'Hidrologia Superficial', 3, 4, 52), " +
                // Semestre 5
                "('AGM-2220', 'Geodesia Fisica Y Satelital', 4, 5, 52), " +
                "('AGM-2320', 'Fotogrametria II Y Teledetecc', 4, 5, 52), " +
                "('AGM-3110', 'Agrimensura Legal I', 4, 5, 52), " +
                "('AGM-3210', 'Sistemas Informacion Geograf I', 4, 5, 52), " +
                "('CIV-2410', 'Vias De Comunicacion Y Trazado', 4, 5, 52), " +
                // Semestre 6
                "('AGM-3120', 'Agrimensura Legal II', 4, 6, 52), " +
                "('AGM-3220', 'Sistemas Informacion Geograf II', 4, 6, 52), " +
                "('AGM-3310', 'Catastro Urbano Y Rural', 4, 6, 52), " +
                "('AGM-3410', 'Ajuste De Observaciones', 4, 6, 52), " +
                "('AGM-3510', 'Tasacion De Bienes Inmuebles', 3, 6, 52), " +
                // Semestre 7
                "('AGM-4110', 'Hidrografia Y Batimetria', 4, 7, 52), " +
                "('AGM-4210', 'Topografia Aplicada A Ingenier', 4, 7, 52), " +
                "('AGM-4310', 'Urbanismo Y Ordenam Territorial', 3, 7, 52), " +
                "('AGM-4410', 'Etica Y Legislacion Agrimensura', 2, 7, 52), " +
                "('AGM-4510', 'Practica Profesional Agrimens', 4, 7, 52), " +
                // Semestre 8
                "('AGM-5110', 'Seminario De Agrimensura', 3, 8, 52), " +
                "('AGM-7010', 'Tesis De Grado O Curso Equival', 8, 8, 52)");

        
        //  Arquitectura (carrera_id = 53)
        stmt.execute("INSERT INTO pensum (codigo, nombre, creditos, semestre, carrera_id) VALUES " +
                "('ARQ-1010', 'Dibujo Tecnico I', 4, 1, 53), " +
                "('ARQ-1110', 'Introd A La Arquitectura', 3, 1, 53), " +
                "('ARQ-1210', 'Geometria Descriptiva', 4, 1, 53), " +
                "('DOP-1000', 'Orientacion Institucional', 1, 1, 53), " +
                "('EFS-0110', 'Educacion Fisica', 1, 1, 53), " +
                "('FIL-0110', 'Int A La Filosofia', 3, 1, 53), " +
                "('FIS-0140', 'Fisica Basica', 4, 1, 53), " +
                "('HIS-0110', 'Fund De His Social Dominicana', 3, 1, 53), " +
                "('LET-0110', 'Lengua Espanola Basica I', 3, 1, 53), " +
                "('MAT-0140', 'Matematica Basica', 4, 1, 53), " +
                // Semestre 2
                "('ARQ-1020', 'Expresion Grafica Arquitect I', 4, 2, 53), " +
                "('ARQ-1310', 'Diseno Arquitectonico I', 5, 2, 53), " +
                "('ARQ-1410', 'Historia De La Arquitectura I', 3, 2, 53), " +
                "('LET-0120', 'Lengua Espanola Basica II', 3, 2, 53), " +
                "('MAT-2510', 'Calculo Y Analitica I', 6, 2, 53), " +
                "('SOC-0110', 'Int A Las Ciencias Sociales', 2, 2, 53), " +
                // Semestre 3
                "('ARQ-2010', 'Expresion Grafica Arquitect II', 4, 3, 53), " +
                "('ARQ-2310', 'Diseno Arquitectonico II', 5, 3, 53), " +
                "('ARQ-2410', 'Historia De La Arquitectura II', 3, 3, 53), " +
                "('ARQ-2510', 'Materiales Y Metodos Const I', 4, 3, 53), " +
                "('CIV-1110', 'Estatica Y Resist Materiales', 4, 3, 53), " +
                // Semestre 4
                "('AGM-1300', 'Topografia Para Arquitectos', 3, 4, 53), " +
                "('ARQ-2320', 'Diseno Arquitectonico III', 5, 4, 53), " +
                "('ARQ-2420', 'Historia De La Arquitectura III', 3, 4, 53), " +
                "('ARQ-2520', 'Materiales Y Metodos Const II', 4, 4, 53), " +
                "('ARQ-2610', 'Diseno Asistido Computadora I', 4, 4, 53), " +
                // Semestre 5
                "('ARQ-3310', 'Diseno Arquitectonico IV', 5, 5, 53), " +
                "('ARQ-3410', 'Arquitectura Dominicana Y Carib', 3, 5, 53), " +
                "('ARQ-3510', 'Instalaciones En Edificios I', 4, 5, 53), " +
                "('ARQ-3610', 'Diseno Asistido Computadora II', 4, 5, 53), " +
                "('CIV-2210', 'Estructuras De Hormigon Y Acero', 4, 5, 53), " +
                // Semestre 6
                "('ARQ-3320', 'Diseno Arquitectonico V', 5, 6, 53), " +
                "('ARQ-3520', 'Instalaciones En Edificios II', 4, 6, 53), " +
                "('ARQ-3710', 'Urbanismo I', 4, 6, 53), " +
                "('ARQ-3810', 'Arquitectura Bioclimatica', 3, 6, 53), " +
                "('ARQ-3910', 'Presupuesto Y Control De Obras', 4, 6, 53), " +
                // Semestre 7
                "('ARQ-4310', 'Diseno Arquitectonico VI', 6, 7, 53), " +
                "('ARQ-4710', 'Urbanismo II Y Diseno Urbano', 4, 7, 53), " +
                "('ARQ-4810', 'Restauracion De Monumentos', 3, 7, 53), " +
                "('ARQ-4910', 'Administracion Y Supervision Obr', 4, 7, 53), " +
                "('ARQ-4920', 'Legislacion Y Etica Arquitect', 2, 7, 53), " +
                // Semestre 8
                "('ARQ-4320', 'Taller Integral De Arquitectura', 6, 8, 53), " +
                "('ARQ-5110', 'Seminario De Arquitectura', 3, 8, 53), " +
                "('ARQ-7010', 'Tesis De Grado O Curso Equival', 8, 8, 53)");

        
        //  Ingenieria Electromecanica, mencion Electrica (carrera_id = 54)
        stmt.execute("INSERT INTO pensum (codigo, nombre, creditos, semestre, carrera_id) VALUES " +
                "('ARQ-1010', 'Dibujo Tecnico I', 4, 1, 54), " +
                "('DOP-1000', 'Orientacion Institucional', 1, 1, 54), " +
                "('EFS-0110', 'Educacion Fisica', 1, 1, 54), " +
                "('FIL-0110', 'Int A La Filosofia', 3, 1, 54), " +
                "('FIS-0140', 'Fisica Basica', 4, 1, 54), " +
                "('HIS-0110', 'Fund De His Social Dominicana', 3, 1, 54), " +
                "('LET-0110', 'Lengua Espanola Basica I', 3, 1, 54), " +
                "('MAT-0140', 'Matematica Basica', 4, 1, 54), " +
                "('QUI-1210', 'Quimica General', 5, 1, 54), " +
                // Semestre 2
                "('FIS-1110', 'Fisica General I', 4, 2, 54), " +
                "('INF-1030', 'Programacion Para Ingenieros', 4, 2, 54), " +
                "('LET-0120', 'Lengua Espanola Basica II', 3, 2, 54), " +
                "('MAT-2300', 'Algebra Lineal', 4, 2, 54), " +
                "('MAT-2510', 'Calculo Y Analitica I', 6, 2, 54), " +
                "('SOC-0110', 'Int A Las Ciencias Sociales', 2, 2, 54), " +
                // Semestre 3
                "('CIV-1120', 'Mecanica Racional I Estatica', 4, 3, 54), " +
                "('FIS-1120', 'Fisica General II', 4, 3, 54), " +
                "('IEM-1110', 'Circuitos Electricos I', 5, 3, 54), " +
                "('MAT-3500', 'Calculo Y Geom Analitica II', 6, 3, 54), " +
                "('MAT-3600', 'Ecuaciones Diferenciales', 5, 3, 54), " +
                // Semestre 4
                "('CIV-1130', 'Mecanica Racional II Dinamica', 4, 4, 54), " +
                "('EST-1110', 'Probabilidad Y Estadistica Ing', 4, 4, 54), " +
                "('IEM-1120', 'Circuitos Electricos II', 5, 4, 54), " +
                "('IEM-1210', 'Termodinamica I', 4, 4, 54), " +
                "('IEM-1310', 'Electronica Basica I', 4, 4, 54), " +
                // Semestre 5
                "('IEM-2110', 'Maquinas Electricas I', 5, 5, 54), " +
                "('IEM-2210', 'Mecanica De Fluidos', 4, 5, 54), " +
                "('IEM-2310', 'Teoria Electromagnetica', 4, 5, 54), " +
                "('IEM-2410', 'Mediciones Electricas', 4, 5, 54), " +
                "('IEM-2510', 'Resistencia De Materiales', 4, 5, 54), " +
                // Semestre 6
                "('IEM-2120', 'Maquinas Electricas II', 5, 6, 54), " +
                "('IEM-3110', 'Sistemas De Potencia I', 5, 6, 54), " +
                "('IEM-3210', 'Instalaciones Electricas Ind', 4, 6, 54), " +
                "('IEM-3310', 'Sistemas De Control Automatico', 4, 6, 54), " +
                "('IEM-3410', 'Electronica De Potencia', 4, 6, 54), " +
                // Semestre 7
                "('IEM-3120', 'Sistemas De Potencia II', 5, 7, 54), " +
                "('IEM-4110', 'Subestaciones Y Lineas Transm', 4, 7, 54), " +
                "('IEM-4120', 'Protecciones Electricas', 4, 7, 54), " +
                "('IEM-4130', 'Centrales Electricas Y Energ Ren', 4, 7, 54), " +
                "('IND-3110', 'Ingenieria Economica', 3, 7, 54), " +
                // Semestre 8
                "('IEM-4140', 'Redes De Distribucion Electrica', 4, 8, 54), " +
                "('IEM-5110', 'Seminario Ingenieria Electrica', 3, 8, 54), " +
                "('IEM-7010', 'Tesis De Grado O Curso Equival', 8, 8, 54)");

        
        //  Ingenieria Electromecanica, mencion Electronica (carrera_id = 55)
        stmt.execute("INSERT INTO pensum (codigo, nombre, creditos, semestre, carrera_id) VALUES " +
                "('ARQ-1010', 'Dibujo Tecnico I', 4, 1, 55), " +
                "('DOP-1000', 'Orientacion Institucional', 1, 1, 55), " +
                "('EFS-0110', 'Educacion Fisica', 1, 1, 55), " +
                "('FIL-0110', 'Int A La Filosofia', 3, 1, 55), " +
                "('FIS-0140', 'Fisica Basica', 4, 1, 55), " +
                "('HIS-0110', 'Fund De His Social Dominicana', 3, 1, 55), " +
                "('LET-0110', 'Lengua Espanola Basica I', 3, 1, 55), " +
                "('MAT-0140', 'Matematica Basica', 4, 1, 55), " +
                "('QUI-1210', 'Quimica General', 5, 1, 55), " +
                // Semestre 2
                "('FIS-1110', 'Fisica General I', 4, 2, 55), " +
                "('INF-1030', 'Programacion Para Ingenieros', 4, 2, 55), " +
                "('LET-0120', 'Lengua Espanola Basica II', 3, 2, 55), " +
                "('MAT-2300', 'Algebra Lineal', 4, 2, 55), " +
                "('MAT-2510', 'Calculo Y Analitica I', 6, 2, 55), " +
                "('SOC-0110', 'Int A Las Ciencias Sociales', 2, 2, 55), " +
                // Semestre 3
                "('CIV-1120', 'Mecanica Racional I Estatica', 4, 3, 55), " +
                "('FIS-1120', 'Fisica General II', 4, 3, 55), " +
                "('IEM-1110', 'Circuitos Electricos I', 5, 3, 55), " +
                "('MAT-3500', 'Calculo Y Geom Analitica II', 6, 3, 55), " +
                "('MAT-3600', 'Ecuaciones Diferenciales', 5, 3, 55), " +
                // Semestre 4
                "('EST-1110', 'Probabilidad Y Estadistica Ing', 4, 4, 55), " +
                "('IEM-1120', 'Circuitos Electricos II', 5, 4, 55), " +
                "('IEM-1310', 'Electronica Analogica I', 5, 4, 55), " +
                "('IEM-1320', 'Electronica Digital I', 5, 4, 55), " +
                "('IEM-2310', 'Teoria Electromagnetica', 4, 4, 55), " +
                // Semestre 5
                "('IEM-2110', 'Maquinas Electricas', 4, 5, 55), " +
                "('IEM-2610', 'Electronica Analogica II', 5, 5, 55), " +
                "('IEM-2620', 'Electronica Digital II', 5, 5, 55), " +
                "('IEM-2630', 'Senales Y Sistemas', 4, 5, 55), " +
                "('IEM-2640', 'Instrumentacion Electronica', 4, 5, 55), " +
                // Semestre 6
                "('IEM-3310', 'Sistemas De Control Automatico', 4, 6, 55), " +
                "('IEM-3410', 'Electronica De Potencia', 4, 6, 55), " +
                "('IEM-3610', 'Microprocesadores Y Microcont', 5, 6, 55), " +
                "('IEM-3620', 'Sistemas De Comunicaciones I', 4, 6, 55), " +
                "('IEM-3630', 'Procesamiento Digital Senales', 4, 6, 55), " +
                // Semestre 7
                "('IEM-4610', 'Sistemas De Comunicaciones II', 4, 7, 55), " +
                "('IEM-4620', 'Automatizacion Industrial Y PLC', 5, 7, 55), " +
                "('IEM-4630', 'Robotica Y Sistemas Embebidos', 4, 7, 55), " +
                "('IEM-4640', 'Redes De Telecomunicaciones', 4, 7, 55), " +
                "('IND-3110', 'Ingenieria Economica', 3, 7, 55), " +
                // Semestre 8
                "('IEM-4650', 'Diseno De Sistemas Electronicos', 4, 8, 55), " +
                "('IEM-5120', 'Seminario Ingenieria Electronica', 3, 8, 55), " +
                "('IEM-7020', 'Tesis De Grado O Curso Equival', 8, 8, 55)");

        
        //  Ingenieria Civil (carrera_id = 56)
        stmt.execute("INSERT INTO pensum (codigo, nombre, creditos, semestre, carrera_id) VALUES " +
                "('ARQ-1010', 'Dibujo Tecnico I', 4, 1, 56), " +
                "('DOP-1000', 'Orientacion Institucional', 1, 1, 56), " +
                "('EFS-0110', 'Educacion Fisica', 1, 1, 56), " +
                "('FIL-0110', 'Int A La Filosofia', 3, 1, 56), " +
                "('FIS-0140', 'Fisica Basica', 4, 1, 56), " +
                "('HIS-0110', 'Fund De His Social Dominicana', 3, 1, 56), " +
                "('LET-0110', 'Lengua Espanola Basica I', 3, 1, 56), " +
                "('MAT-0140', 'Matematica Basica', 4, 1, 56), " +
                "('QUI-1210', 'Quimica General', 5, 1, 56), " +
                // Semestre 2
                "('AGM-1210', 'Topografia General', 4, 2, 56), " +
                "('FIS-1110', 'Fisica General I', 4, 2, 56), " +
                "('GEO-1120', 'Geologia Aplicada A Ingenieria', 4, 2, 56), " +
                "('INF-1030', 'Programacion Para Ingenieros', 4, 2, 56), " +
                "('LET-0120', 'Lengua Espanola Basica II', 3, 2, 56), " +
                "('MAT-2510', 'Calculo Y Analitica I', 6, 2, 56), " +
                // Semestre 3
                "('CIV-1120', 'Mecanica Racional I Estatica', 4, 3, 56), " +
                "('EST-1110', 'Probabilidad Y Estadistica Ing', 4, 3, 56), " +
                "('FIS-1120', 'Fisica General II', 4, 3, 56), " +
                "('MAT-2300', 'Algebra Lineal', 4, 3, 56), " +
                "('MAT-3500', 'Calculo Y Geom Analitica II', 6, 3, 56), " +
                // Semestre 4
                "('CIV-1130', 'Mecanica Racional II Dinamica', 4, 4, 56), " +
                "('CIV-1210', 'Resistencia De Materiales I', 4, 4, 56), " +
                "('CIV-1310', 'Materiales De Construccion', 4, 4, 56), " +
                "('IEM-2210', 'Mecanica De Fluidos', 4, 4, 56), " +
                "('MAT-3600', 'Ecuaciones Diferenciales', 5, 4, 56), " +
                // Semestre 5
                "('CIV-1220', 'Resistencia De Materiales II', 4, 5, 56), " +
                "('CIV-2110', 'Analisis Estructural I', 4, 5, 56), " +
                "('CIV-2220', 'Mecanica De Suelos I', 4, 5, 56), " +
                "('CIV-2310', 'Hidraulica General', 4, 5, 56), " +
                "('CIV-2410', 'Ingenieria De Carreteras I', 4, 5, 56), " +
                // Semestre 6
                "('CIV-2120', 'Analisis Estructural II', 4, 6, 56), " +
                "('CIV-2230', 'Mecanica De Suelos II Y Ciment', 4, 6, 56), " +
                "('CIV-2420', 'Ingenieria De Carreteras II', 4, 6, 56), " +
                "('CIV-3110', 'Hormigon Armado I', 5, 6, 56), " +
                "('CIV-3210', 'Hidrologia E Ingenieria Sanit I', 4, 6, 56), " +
                // Semestre 7
                "('CIV-3120', 'Hormigon Armado II', 5, 7, 56), " +
                "('CIV-3220', 'Acueductos Y Alcantarillados', 4, 7, 56), " +
                "('CIV-3310', 'Estructuras Metalicas Y Madera', 4, 7, 56), " +
                "('CIV-3410', 'Ingenieria De Transito Y Transp', 4, 7, 56), " +
                "('IND-3110', 'Ingenieria Economica', 3, 7, 56), " +
                // Semestre 8
                "('CIV-4110', 'Adm Y Presupuesto De Obras Civ', 4, 8, 56), " +
                "('CIV-4210', 'Ingenieria Sismica Y Puentes', 4, 8, 56), " +
                "('CIV-5110', 'Seminario De Ingenieria Civil', 3, 8, 56), " +
                "('CIV-7010', 'Tesis De Grado O Curso Equival', 8, 8, 56)");

        
        //  Ingenieria Industrial (carrera_id = 57)
        stmt.execute("INSERT INTO pensum (codigo, nombre, creditos, semestre, carrera_id) VALUES " +
                "('ARQ-1010', 'Dibujo Tecnico I', 4, 1, 57), " +
                "('DOP-1000', 'Orientacion Institucional', 1, 1, 57), " +
                "('EFS-0110', 'Educacion Fisica', 1, 1, 57), " +
                "('FIL-0110', 'Int A La Filosofia', 3, 1, 57), " +
                "('FIS-0140', 'Fisica Basica', 4, 1, 57), " +
                "('HIS-0110', 'Fund De His Social Dominicana', 3, 1, 57), " +
                "('LET-0110', 'Lengua Espanola Basica I', 3, 1, 57), " +
                "('MAT-0140', 'Matematica Basica', 4, 1, 57), " +
                "('QUI-1210', 'Quimica General', 5, 1, 57), " +
                // Semestre 2
                "('FIS-1110', 'Fisica General I', 4, 2, 57), " +
                "('IND-1110', 'Introd A La Ingenieria Ind', 3, 2, 57), " +
                "('INF-1030', 'Programacion Para Ingenieros', 4, 2, 57), " +
                "('LET-0120', 'Lengua Espanola Basica II', 3, 2, 57), " +
                "('MAT-2300', 'Algebra Lineal', 4, 2, 57), " +
                "('MAT-2510', 'Calculo Y Analitica I', 6, 2, 57), " +
                // Semestre 3
                "('CON-1190', 'Contabilidad General Y Costos', 4, 3, 57), " +
                "('ECN-2110', 'Economia Para Ingenieros', 4, 3, 57), " +
                "('EST-1110', 'Probabilidad Y Estadistica I', 4, 3, 57), " +
                "('FIS-1120', 'Fisica General II', 4, 3, 57), " +
                "('MAT-3500', 'Calculo Y Geom Analitica II', 6, 3, 57), " +
                // Semestre 4
                "('CIV-1120', 'Mecanica Racional Y Materiales', 4, 4, 57), " +
                "('EST-2230', 'Estadistica Inferencial Ing', 4, 4, 57), " +
                "('IND-2110', 'Ingenieria De Metodos I', 4, 4, 57), " +
                "('IND-2210', 'Procesos De Manufactura I', 4, 4, 57), " +
                "('MAT-3600', 'Ecuaciones Diferenciales', 5, 4, 57), " +
                // Semestre 5
                "('IND-2120', 'Ingenieria De Metodos II Y Erg', 4, 5, 57), " +
                "('IND-2220', 'Procesos De Manufactura II', 4, 5, 57), " +
                "('IND-2310', 'Investigacion De Operaciones I', 4, 5, 57), " +
                "('IND-3110', 'Ingenieria Economica', 4, 5, 57), " +
                "('IND-3210', 'Control Estadistico Calidad', 4, 5, 57), " +
                // Semestre 6
                "('IND-2320', 'Investigacion De Operaciones II', 4, 6, 57), " +
                "('IND-3220', 'Gestion De Calidad Total', 4, 6, 57), " +
                "('IND-3310', 'Planificacion Y Control Prod I', 4, 6, 57), " +
                "('IND-3410', 'Seguridad E Higiene Industrial', 3, 6, 57), " +
                "('IND-3510', 'Diseno De Plantas Industriales', 4, 6, 57), " +
                // Semestre 7
                "('IND-3320', 'Planificacion Y Control Prod II', 4, 7, 57), " +
                "('IND-4110', 'Logistica Y Cadena Suministro', 4, 7, 57), " +
                "('IND-4210', 'Simulacion De Sistemas Ind', 4, 7, 57), " +
                "('IND-4310', 'Formulacion Y Eval Proyectos', 4, 7, 57), " +
                "('IND-4410', 'Mantenimiento Industrial', 3, 7, 57), " +
                // Semestre 8
                "('IND-4510', 'Gerencia De Operaciones', 4, 8, 57), " +
                "('IND-5110', 'Seminario Ingenieria Industrial', 3, 8, 57), " +
                "('IND-7010', 'Tesis De Grado O Curso Equival', 8, 8, 57)");

        
        //  Ingenieria Quimica (carrera_id = 58)
        stmt.execute("INSERT INTO pensum (codigo, nombre, creditos, semestre, carrera_id) VALUES " +
                "('ARQ-1010', 'Dibujo Tecnico I', 4, 1, 58), " +
                "('DOP-1000', 'Orientacion Institucional', 1, 1, 58), " +
                "('EFS-0110', 'Educacion Fisica', 1, 1, 58), " +
                "('FIL-0110', 'Int A La Filosofia', 3, 1, 58), " +
                "('FIS-0140', 'Fisica Basica', 4, 1, 58), " +
                "('HIS-0110', 'Fund De His Social Dominicana', 3, 1, 58), " +
                "('LET-0110', 'Lengua Espanola Basica I', 3, 1, 58), " +
                "('MAT-0140', 'Matematica Basica', 4, 1, 58), " +
                "('QUI-1210', 'Quimica General I', 5, 1, 58), " +
                // Semestre 2
                "('FIS-1110', 'Fisica General I', 4, 2, 58), " +
                "('INF-1030', 'Programacion Para Ingenieros', 4, 2, 58), " +
                "('LET-0120', 'Lengua Espanola Basica II', 3, 2, 58), " +
                "('MAT-2300', 'Algebra Lineal', 4, 2, 58), " +
                "('MAT-2510', 'Calculo Y Analitica I', 6, 2, 58), " +
                "('QUI-1220', 'Quimica General II', 5, 2, 58), " +
                // Semestre 3
                "('EST-1110', 'Probabilidad Y Estadistica Ing', 4, 3, 58), " +
                "('FIS-1120', 'Fisica General II', 4, 3, 58), " +
                "('IQU-1110', 'Balance De Materia Y Energia', 4, 3, 58), " +
                "('MAT-3500', 'Calculo Y Geom Analitica II', 6, 3, 58), " +
                "('QUI-2110', 'Quimica Organica I', 5, 3, 58), " +
                // Semestre 4
                "('IQU-1210', 'Termodinamica Quimica I', 4, 4, 58), " +
                "('MAT-3600', 'Ecuaciones Diferenciales', 5, 4, 58), " +
                "('QUI-2120', 'Quimica Organica II', 5, 4, 58), " +
                "('QUI-2310', 'Quimica Analitica Cuantitativa', 5, 4, 58), " +
                "('QUI-2410', 'Fisicoquimica I', 4, 4, 58), " +
                // Semestre 5
                "('IQU-1220', 'Termodinamica Quimica II', 4, 5, 58), " +
                "('IQU-2110', 'Fenomenos De Transporte I', 4, 5, 58), " +
                "('IQU-2210', 'Operaciones Unitarias I', 4, 5, 58), " +
                "('QUI-2320', 'Analisis Instrumental', 4, 5, 58), " +
                "('QUI-2420', 'Fisicoquimica II', 4, 5, 58), " +
                // Semestre 6
                "('IQU-2120', 'Fenomenos De Transporte II', 4, 6, 58), " +
                "('IQU-2220', 'Operaciones Unitarias II', 4, 6, 58), " +
                "('IQU-2310', 'Cinetica Quimica Y Catalisis', 4, 6, 58), " +
                "('IQU-2410', 'Ingenieria De Las Reacciones I', 4, 6, 58), " +
                "('IQU-2510', 'Tecnologia De Alimentos Y Biot', 4, 6, 58), " +
                // Semestre 7
                "('IND-3110', 'Ingenieria Economica', 3, 7, 58), " +
                "('IQU-2230', 'Operaciones Unitarias III', 4, 7, 58), " +
                "('IQU-2420', 'Ingenieria De Las Reacciones II', 4, 7, 58), " +
                "('IQU-3110', 'Control E Instrumentacion Proc', 4, 7, 58), " +
                "('IQU-3210', 'Ingenieria Ambiental Y Seguridad', 4, 7, 58), " +
                // Semestre 8
                "('IQU-4110', 'Diseno De Plantas Quimicas', 5, 8, 58), " +
                "('IQU-5110', 'Seminario De Ingenieria Quimica', 3, 8, 58), " +
                "('IQU-7010', 'Tesis De Grado O Curso Equival', 8, 8, 58)");
   
        
        }
    
}
