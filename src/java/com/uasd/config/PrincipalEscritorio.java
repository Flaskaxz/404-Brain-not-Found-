package com.uasd.config;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import java.awt.Dimension;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class PrincipalEscritorio extends JFrame {

    public PrincipalEscritorio() {
        setTitle("Sistema de Catalogo Academico - UASD");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(400, 250)); 
        setLocationRelativeTo(null); 

        try {
            // 1. AUTODESPLIEGUE: Forzamos la sincronizacion del proyecto dentro de Tomcat
            String rutaOrigen = System.getProperty("user.dir") + File.separator + "WebCatalogo.war";
            // Ruta por defecto del despliegue automatico de Tomcat de fabrica
            String rutaDestino = "C:" + File.separator + "Program Files" + File.separator 
                    + "Apache Software Foundation" + File.separator + "Tomcat 9.0" 
                    + File.separator + "webapps" + File.separator + "WebCatalogo.war";
            
            File origen = new File(rutaOrigen);
            File destino = new File(rutaDestino);
            
            if (origen.exists() && destino.getParentFile().exists()) {
                Files.copy(origen.toPath(), destino.toPath(), StandardCopyOption.REPLACE_EXISTING);
                System.out.println(">>> Proyecto sincronizado en Tomcat webapps.");
            }

            // 2. Esperamos 4 segundos a que Tomcat descomprima el archivo y levante la url
            Thread.sleep(4000);
            
            String url = "http://localhost:8080/WebCatalogo/index";
            java.awt.Desktop.getDesktop().browse(new java.net.URI(url));
            
            // Cerramos la pasarela y queda tu catalogo de cristal abierto en el navegador
            System.exit(0);
        } catch (Exception e) {
            System.out.println("Aviso en pasarela: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        System.out.println(">>> Iniciando pasarela de escritorio UASD...");

        Thread hiloServidor = new Thread(() -> {
            try {
                com.uasd.config.ConexionDB.getConnection();
                System.out.println(">>> Base de datos SQLite acoplada.");
            } catch (Exception e) {
                System.out.println("Aviso en base de datos: " + e.getMessage());
            }
        });
        hiloServidor.setDaemon(true); 
        hiloServidor.start();

        SwingUtilities.invokeLater(() -> {
            PrincipalEscritorio ventana = new PrincipalEscritorio();
            ventana.setVisible(true);
        });
    }
}
