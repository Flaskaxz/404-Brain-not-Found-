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

## Importante sobre créditos
La base de datos recibida para esta versión contiene la columna `creditos`, pero las 58 carreras tienen valor 0. Por eso la aplicación ya está preparada para mostrarlos, pero presenta "No registrado" hasta que se incorporen los valores oficiales correspondientes.

## Acceso de prueba
Usuario: `admin`
Clave: `1234`
