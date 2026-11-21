# CatalogoCarrerasUASD1 - versión mejorada

Aplicación de escritorio Java Swing para consultar el catálogo académico UASD.

## Mejoras
- Interfaz personalizada con jerarquía visual, paneles y colores institucionales.
- Menú organizado en: Catálogo, Filtros y organización, Vista, Ayuda y Sesión.
- Buscador por código, carrera, escuela y facultad.
- Filtros combinables por facultad y escuela.
- Ordenamiento por facultad, escuela, carrera, código o créditos.
- Orden ascendente/descendente.
- Vista especial de todas las carreras con los dos escudos UASD.
- La primera opción de cada facultad es "Todas las carreras de esta facultad".
- Columna de créditos en la tabla.
- Los créditos se muestran como "No registrado" cuando la base de datos contiene 0, para no inventar datos académicos.
- Mantiene los logos oficiales incluidos por el usuario y conserva su proporción al redimensionarlos.

## Datos
La base incluida contiene 9 facultades, 50 escuelas y 58 carreras.

->**informacion agregada a la base de datos** : 
Se agrega la facultade de arte con las carreras: 
Licenciatura en artes plásticas (30320 - P-A-PLA)
Licenciatura en artes plásticas mención escultura (30323 - P-ARPE)
Licenciatura en artes plasticas mencion pintura (30322 - P- ARPP)
Licenciatura en cinematografía y audiovisuales mención cine (303 61 - P-CINC)
Licenciatura en cinematografía y audiovisuales mención televisión (30362 - P-CINT)
Tecnico en fotografia y medios audiovisuales (30363 - P-FOTA)
Licenciatura en historia y crítica del arte
Licenciatura en artes industriales mención diseño artesanal (30372 - P-AIDA) 
Licenciatura en artes industriales mención diseño industrial (30373 - P-AIDI) 
Licenciatura en artes industriales mención diseño de modas (30371 - P-AIDM) 
Licenciatura en música mención teoría y educación musical (30355 - P-MUST)
Licenciatura en música (30350 - P-MUS)
Licenciatura en publicidad mención diseño (30332 - P-ARD)
Licenciatura en publicidad mención ilustración (30331 - P-ARPI)
Licenciatura en publicidad mención creatividad y gerencia (30333 - P-UBC)
Licenciatura en teatro (3040 - P-TEAA)	
Licenciatura en teatro mención actuación (3041 - P-TEEA)	
Licenciatura en teatro mención dramaturgia (30342 - P-TEDR)	
Licenciatura en teatro mención en dirección (30343 - P-TEAI)

Se agrega la facultad de ciencias con las carreras: 
Licenciatura en biología (40201- P-biol)
Licenciatura en geografía (40302 - P-geog)
Licenciatura en geografía mención Recursos naturales y ecotur (40303 - P-ecoot)
Licenciatura en geografía mención representación espacial (40303 - P-geor)
Licenciatura en física (40401 - P-fisi)
Licenciatura en informática (40601 - P-info)
Licenciatura en matemáticas (40603 - P-mate)
Licenciatura en microbiología (40701 - P-mirc)
Licenciatura en química (40801 - P-quim)
Tecnólogo superior en alimentos (40803 - P-tasl)

## Importante sobre créditos
La base de datos recibida para esta versión contiene la columna `creditos`, pero las 58 carreras tienen valor 0. Por eso la aplicación ya está preparada para mostrarlos, pero presenta "No registrado" hasta que se incorporen los valores oficiales correspondientes.

## Acceso de prueba
Usuario: `admin`
Clave: `1234`
