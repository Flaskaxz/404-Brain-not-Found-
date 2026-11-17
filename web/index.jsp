<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Catálogo UASD</title>
    <style>
        /* Estilos base y tipografía */
            body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            margin: 0;
            padding: 0;
            color: #333;
            min-height: 100vh;
            position: relative;
            background-color: #f4f6f9;
        }

                /* Contenedor de la imagen de fondo con efectos modernos */
        .background-overlay {
            position: fixed;
            top: 0;
            left: 0;
            width: 100%;
            height: 100%;
            /* Puedes usar esta imagen genérica de biblioteca o reemplazarla por una local */
            background-image: url('https://unsplash.com');
            background-size: cover;
            background-position: center;
            background-repeat: no-repeat;
            filter: blur(6px); /* Efecto desenfoque para dar profundidad */
            opacity: 0.12; /* Muy suave para que los paneles sigan siendo los protagonistas */
            z-index: -1; /* Se mantiene detrás de todo el contenido */
            animation: backgroundFade 10s ease-in-out infinite alternate;
        }

        /* Animación sutil de zoom para el fondo */
        @keyframes backgroundFade {
            0% { transform: scale(1); }
            100% { transform: scale(1.05); }
        }

        /* Ajuste para asegurar que las cajas resalten sobre el fondo */
        .panel-box {
            flex: 1;
            min-height: 450px;
            background: rgba(255, 255, 255, 0.95); /* Ligeramente translúcido */
            border: 1px solid rgba(224, 224, 224, 0.8);
            border-radius: 12px; /* Bordes un poco más suaves */
            padding: 20px;
            box-shadow: 0 8px 24px rgba(0,0,0,0.06); /* Sombra más envolvente */
            backdrop-filter: blur(4px); /* Filtro de cristal templado */
        }


        /* BARRA DE NAVEGACIÓN INSTITUCIONAL (ESTILO UASD) */
        .navbar-uasd {
            background-color: #002D62; /* Azul Marino Oficial */
            color: #ffffff;
            padding: 15px 30px;
            display: flex;
            justify-content: space-between;
            align-items: center;
            box-shadow: 0 4px 10px rgba(0,0,0,0.15);
            border-bottom: 4px solid #D4AF37; /* Detalle Dorado */
        }
        .navbar-brand {
            display: flex;
            align-items: center;
            gap: 12px;
        }
        .navbar-logo-text {
            font-size: 1.4rem;
            font-weight: bold;
            letter-spacing: 1px;
            margin: 0;
        }
        .navbar-subtext {
            font-size: 0.85rem;
            color: #D4AF37;
            margin: 0;
            text-transform: uppercase;
            letter-spacing: 0.5px;
        }
        .navbar-links {
            font-size: 0.95rem;
            font-weight: 500;
            opacity: 0.9;
        }

        /* Contenedor del contenido principal */
        .main-content {
            padding: 40px 20px;
        }
        h2.page-title {
            text-align: center;
            color: #002D62;
            margin-bottom: 35px;
            font-weight: bold;
        }
        
        /* Contenedor principal en formato de 3 columnas */
        .contenedor-paneles {
            display: flex;
            gap: 20px;
            max-width: 1200px;
            margin: 0 auto;
        }
        
        /* Caja de cada panel individual */
        .panel-box {
            flex: 1;
            min-height: 450px;
            background: #ffffff;
            border: 1px solid #e0e0e0;
            border-radius: 8px;
            padding: 20px;
            box-shadow: 0 4px 6px rgba(0,0,0,0.05);
        }
        
        h5 {
            margin-top: 0;
            margin-bottom: 15px;
            padding-bottom: 10px;
            border-bottom: 2px solid #002D62;
            color: #495057;
            font-size: 1.1rem;
        }
        
        /* Contenedor de listas y botones interactivos */
        .list-group {
            display: flex;
            flex-direction: column;
            gap: 8px;
        }
        
        .list-group-item {
            padding: 12px 15px;
            background-color: #fff;
            border: 1px solid #dee2e6;
            border-radius: 6px;
            text-align: left;
            cursor: pointer;
            transition: all 0.2s ease;
            font-size: 0.95rem;
            width: 100%;
            box-sizing: border-box;
            font-family: inherit;
        }
        
        .list-group-item:hover {
            background-color: #f8f9fa;
            border-color: #b5b5b5;
        }
        
        /* Estado cuando un botón de facultad o escuela está seleccionado */
        .list-group-item.active {
            background-color: #002D62;
            color: white;
            border-color: #002D62;
        }
        
        /* Bloque estático exclusivo para mostrar las carreras finales */
        .carrera-item {
            padding: 12px 15px;
            background-color: #f8f9fa;
            border-left: 4px solid #D4AF37; /* Borde Dorado en las carreras */
            border-top: 1px solid #dee2e6;
            border-right: 1px solid #dee2e6;
            border-bottom: 1px solid #dee2e6;
            border-radius: 0 6px 6px 0;
            font-size: 0.9rem;
        }
        
        .text-muted {
            color: #6c757d;
            font-style: italic;
            text-align: center;
            margin-top: 30px;
            font-size: 0.9rem;
        }
    
     @keyframes fadeInSlot {
            from { 
                opacity: 0; 
                transform: translateY(8px); 
            }
            to { 
                opacity: 1; 
                transform: translateY(0); 
            }
        }
        
        .list-group-item, .carrera-item {
            animation-name: fadeInSlot;
            animation-duration: 0.3s;
            animation-timing-function: ease-out;
            animation-fill-mode: forwards;
        }
    
     .text-muted {
            color: #6c757d;
            font-style: italic;
            text-align: center;
            margin-top: 30px;
            font-size: 0.9rem;
        }

        /* --- AQUÍ ADENTRO DE LOS ESTILOS ES DONDE VA TU BLOQUE SELECCIONADO --- */
        .background-overlay {
            position: fixed;
            top: 0;
            left: 0;
            width: 100%;
            height: 100%;
            /* Apuntamos a tu nuevo archivo GIF local */
            background-image: url('fondo.gif');
            background-size: cover;
            background-position: center;
            background-repeat: no-repeat;
            filter: blur(2px); /* Difumina un poco el GIF para que no distraiga la lectura */
            opacity: 0.35; /* Manténlo bien bajito (10%) para que las letras se sigan leyendo perfecto */
            z-index: -1; /* Se queda al fondo de todo */
        }
    
            /* PANTALLA DE BIENVENIDA (SPLASH SCREEN) */
        .splash-screen {
            position: fixed;
            top: 0;
            left: 0;
            width: 100%;
            height: 100vh;
            background-color: #002D62; /* Azul Marino UASD */
            z-index: 9999; /* Se posiciona por encima de absolutamente todo */
            display: flex;
            flex-direction: column;
            justify-content: center;
            align-items: center;
            color: white;
            transition: opacity 0.8s ease, visibility 0.8s;
        }

        /* Animación de pulso para el texto o logo */
        .splash-logo-text {
            font-size: 3rem;
            font-weight: bold;
            letter-spacing: 3px;
            margin: 0;
            color: #ffffff;
            animation: pulseText 2s infinite ease-in-out;
        }
        .splash-subtext {
            font-size: 1.1rem;
            color: #D4AF37; /* Dorado */
            margin-top: 10px;
            text-transform: uppercase;
            letter-spacing: 2px;
        }

        /* Spinner o círculo de carga institucional */
        .spinner-uasd {
            width: 50px;
            height: 50px;
            border: 5px solid rgba(255, 255, 255, 0.2);
            border-top: 5px solid #D4AF37;
            border-radius: 50%;
            margin-top: 30px;
            animation: spin 1s linear infinite;
        }

        @keyframes spin {
            0% { transform: rotate(0deg); }
            100% { transform: rotate(360deg); }
        }

        @keyframes pulseText {
            0% { transform: scale(0.98); opacity: 0.8; }
            50% { transform: scale(1.02); opacity: 1; }
            100% { transform: scale(0.98); opacity: 0.8; }
        }

        /* Clase contenedora para ocultar la pantalla suavemente */
        .splash-hidden {
            opacity: 0;
            visibility: hidden;
            pointer-events: none;
        }

        /* Fondo oscuro detrás de la ventana */
.modal-overlay {
    display: none; /* Oculto por defecto */
    position: fixed;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    background: rgba(0, 20, 45, 0.75);
    backdrop-filter: blur(4px);
    z-index: 2000;
    justify-content: center;
    align-items: center;
    padding: 20px;
    box-sizing: border-box;
}

/* Caja principal de la ventana */
.modal-content {
    background: #ffffff;
    width: 100%;
    max-width: 850px;
    max-height: 85vh;
    border-radius: 12px;
    box-shadow: 0 15px 40px rgba(0,0,0,0.3);
    border-top: 6px solid #D4AF37;
    display: flex;
    flex-direction: column;
    overflow: hidden;
    animation: fadeInSlot 0.25s ease-out;
}

.modal-header {
    background-color: #002D62;
    color: white;
    padding: 18px 25px;
    display: flex;
    justify-content: space-between;
    align-items: center;
}

.modal-close {
    background: none;
    border: none;
    color: #D4AF37;
    font-size: 1.8rem;
    font-weight: bold;
    cursor: pointer;
    line-height: 1;
}

.modal-body {
    padding: 25px;
    overflow-y: auto; /* Permite hacer scroll si el pensum es muy largo */
}

    
    </style>
</head>
<body>

    <!-- 1. Pantalla de Bienvenida (Splash Screen) -->
    <div id="splash" class="splash-screen">
        <h1 class="splash-logo-text">UASD</h1>
        <p class="splash-subtext">DESIGNED BY 404: BRAIN NOT FOUND</p>
        <div class="spinner-uasd"></div>
    </div>

    <!-- 2. Capa de fondo animada (GIF Matrix) -->
    <div class="background-overlay"></div>
    
    <!-- 3. BARRA DE NAVEGACIÓN SUPERIOR (Cerrada correctamente en su sitio) -->
    <nav class="navbar-uasd">
        <div class="navbar-brand">
            <div style="background-color: #D4AF37; width: 8px; height: 35px; border-radius: 2px;"></div>
            <div>
                <p class="navbar-logo-text">UASD</p>
                <p style="font-size: 0.65rem; color: #aaa; margin: 0;">DESIGNED BY 404: BRAIN NOT FOUND</p>
            </div>
        </div>
        <div class="navbar-links">
            <span>Catalogo Academico Oficial</span>
        </div>
    </nav> <!-- AQUÍ SE CIERRA LA BARRA AZUL -->

    <!-- 4. CONTENIDO PRINCIPAL (Comienza limpio debajo de la barra azul) -->
    <div class="main-content">
        <h2 class="page-title">Catálogo de Carreras Disponibles</h2>
        
        
        <div class="contenedor-paneles">
            <!-- Panel 1: Facultades (Carga del Servidor) -->
            <div class="panel-box">
                <h5> Seleccione Facultad</h5>
                <div class="list-group">
                    <%
                        java.util.List<com.uasd.model.Item> facs = (java.util.List<com.uasd.model.Item>) request.getAttribute("facultades");
                        if(facs != null) {
                            for(com.uasd.model.Item f : facs) {
                    %>
                        <button onclick="cargarEscuelas(<%= f.getId() %>, this)" class="list-group-item btn-facultad">
                            <%= f.getNombre() %>
                        </button>
                    <% 
                            }
                        } 
                    %>
                </div>
            </div>         


            <!-- Panel 2: Escuelas (Carga Dinámica AJAX) -->
            <div class="panel-box">
                <h5>  Escuelas</h5>
                <div id="panel-escuelas" class="list-group">
                    <p class="text-muted">Seleccione una facultad para ver sus escuelas.</p>
                </div>
            </div>

            <!-- Panel 3: Carreras Finales -->
            <div class="panel-box">
                <h5> Carreras</h5>
                <div id="panel-carreras" class="list-group">
                    <p class="text-muted">Seleccione una escuela para listar las carreras.</p>
                </div>
            </div>
        </div>
    </div>
                
     <!-- Ventana Emergente (Modal) para el Pensum -->
<div id="modal-pensum" class="modal-overlay" onclick="cerrarModal(event)">
    <div class="modal-content" onclick="event.stopPropagation()">
        <div class="modal-header">
            <h3 id="modal-titulo-carrera" style="margin:0; font-size:1.2rem;">Pensum Académico</h3>
            <button class="modal-close" onclick="cerrarModalDirecto()">&times;</button>
        </div>
        <div id="modal-cuerpo-pensum" class="modal-body">
            <!-- Aquí se cargan los semestres y materias -->
        </div>
    </div>
</div>
      
    <!-- JavaScript Dinámico (Fetch API) -->
    <script>
        function cargarEscuelas(facultadId, elemento) {
            document.querySelectorAll('.btn-facultad').forEach(b => b.classList.remove('active'));
            elemento.classList.add('active');
            document.getElementById('panel-carreras').innerHTML = '<p class="text-muted">Seleccione una escuela para listar las carreras.</p>';

            fetch('api?tipo=escuelas&id=' + facultadId)
                .then(function(res) { 
                    return res.json(); 
                })
                .then(function(data) {
                    let html = '';
                    if (data.length === 0) {
                        html = '<p class="text-muted">No hay escuelas registradas.</p>';
                    } else {
                        data.forEach(function(escuela) {
                            let nombre = escuela.nombre || "Escuela Academica";
                            html += '<button onclick="cargarCarreras(' + escuela.id + ', this)" class="list-group-item btn-escuela">' + nombre + '</button>';
                        });
                    }
                    document.getElementById('panel-escuelas').innerHTML = html;
                })
                .catch(function(err) { 
                    console.error("Error escuelas:", err); 
                });
        }

        function cargarCarreras(escuelaId, elemento) {
            document.querySelectorAll('.btn-escuela').forEach(b => b.classList.remove('active'));
            elemento.classList.add('active');

            fetch('api?tipo=carreras&id=' + escuelaId)
                .then(function(res) { 
                    return res.json(); 
                })
                .then(function(data) {
                    let html = '';
                    if (data.length === 0) {
                        html = '<p class="text-muted">No hay carreras asignadas.</p>';
                    } else {
                        data.forEach(function(carrera) {
                            let nombre = carrera.nombre || "Carrera Academica";
                           html += '<button onclick="abrirPensum(' + carrera.id + ', \'' + nombre.replace(/'/g, "\\'") + '\')" class="list-group-item carrera-item">' + nombre + '</button>';

                        });
                    }
                    document.getElementById('panel-carreras').innerHTML = html;
                })
                .catch(function(err) { 
                    console.error("Error carreras:", err); 
                });
        }
        
        
        
            // FUNCIÓN PARA RETIRAR LA PANTALLA DE BIENVENIDA AUTOMÁTICAMENTE
        window.addEventListener('DOMContentLoaded', function() {
            // El número 3000 representa 5 segundos
            setTimeout(function() {
                let splash = document.getElementById('splash');
                if (splash) {
                    splash.classList.add('splash-hidden');
                }
            }, 5000); 
        });

        function abrirPensum(carreraId, nombreCarrera) {
        document.getElementById('modal-titulo-carrera').innerText = 'Pensum: ' + nombreCarrera;
        document.getElementById('modal-cuerpo-pensum').innerHTML = '<p class="text-muted">Cargando asignaturas...</p>';
        document.getElementById('modal-pensum').style.display = 'flex';

        fetch('api?tipo=pensum&id=' + carreraId)
         .then(res => res.json())
         .then(data => {
            let html = '';
            if (data.length === 0) {
                html = '<p class="text-muted">Pensum no disponible aún para esta carrera.</p>';
            } else {
                let semestreActual = 0;
                data.forEach(mat => {
                    if (mat.semestre !== semestreActual) {
                        semestreActual = mat.semestre;
                        html += '<div style="background:#002D62; color:#D4AF37; padding:8px 14px; font-weight:bold; border-radius:6px; margin-top:15px; margin-bottom:8px;">Semestre ' + semestreActual + '</div>';
                    }
                    html += '<div class="carrera-item" style="display:flex; justify-content:space-between; margin-bottom:6px;">'
                          + '<span><strong>' + mat.codigo + '</strong> - ' + mat.nombre + '</span>'
                          + '<span style="color:#002D62; font-weight:bold;">' + mat.creditos + ' CR</span>'
                          + '</div>';
                });
            }
            document.getElementById('modal-cuerpo-pensum').innerHTML = html;
        })
        .catch(err => console.error("Error pensum:", err));
}

function cerrarModalDirecto() {
    document.getElementById('modal-pensum').style.display = 'none';
}

function cerrarModal(event) {
    if (event.target.id === 'modal-pensum') {
        cerrarModalDirecto();
    }
}

 window.addEventListener('DOMContentLoaded', function() {
            setTimeout(function() {
                let splash = document.getElementById('splash');
                if (splash) {
                    splash.classList.add('splash-hidden');
                }
            }, 5000); 
        });


    </script>
</body>
</html>
