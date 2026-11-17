package com.uasd.controller;

import com.uasd.config.ConexionDB;
import com.uasd.model.Item;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet(name = "ApiServlet", urlPatterns = {"/api"})
public class ApiServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        request.setCharacterEncoding("UTF-8");
        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        response.setContentType("application/json;charset=UTF-8");
        
        String tipo = request.getParameter("tipo");
        String idStr = request.getParameter("id");
        String json = "[]";
        
        List<Item> lista = new ArrayList<>();
        if (tipo != null && idStr != null) {
            int id = Integer.parseInt(idStr);
            
            if (tipo.equals("pensum")) {
                String queryPensum = "SELECT codigo, nombre, creditos, semestre FROM pensum WHERE carrera_id = ? ORDER BY semestre ASC, id ASC";
                StringBuilder pb = new StringBuilder("[");
                
                try (Connection conn = ConexionDB.getConnection();
                     PreparedStatement ps = conn.prepareStatement(queryPensum)) {
                    ps.setInt(1, id);
                    try (ResultSet rs = ps.executeQuery()) {
                        boolean primero = true;
                        while (rs.next()) {
                            if (!primero) pb.append(",");
                            primero = false;
                            
                            String nombreLimpio = rs.getString("nombre").replace("\"", "\\\"");
                            pb.append("{")
                              .append("\"codigo\":\"").append(rs.getString("codigo")).append("\",")
                              .append("\"nombre\":\"").append(nombreLimpio).append("\",")
                              .append("\"creditos\":").append(rs.getInt("creditos")).append(",")
                              .append("\"semestre\":").append(rs.getInt("semestre"))
                              .append("}");
                        }
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                pb.append("]");
                json = pb.toString();
                
            } else {
                String query = "";
                if (tipo.equals("escuelas")) {
                    query = "SELECT id, nombre FROM escuela WHERE facultad_id = ?";
                } else if (tipo.equals("carreras")) {
                    query = "SELECT id, nombre FROM carrera WHERE escuela_id = ?";
                }
                
                try (Connection conn = ConexionDB.getConnection();
                     PreparedStatement ps = conn.prepareStatement(query)) {
                    ps.setInt(1, id);
                    try (ResultSet rs = ps.executeQuery()) {
                        while (rs.next()) {
                            lista.add(new Item(rs.getInt("id"), rs.getString("nombre")));
                        }
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                
                // REEMPLAZA EL BLOQUE DE GSON POR ESTA CONVERSIÓN MANUAL INMUNE A FALLOS:
                StringBuilder jsonBuilder = new StringBuilder();
                jsonBuilder.append("[");
                for (int i = 0; i < lista.size(); i++) {
                    Item item = lista.get(i);
                    jsonBuilder.append("{");
                    jsonBuilder.append("\"id\":").append(item.getId()).append(",");
                    // Limpiamos comillas por si acaso para no romper el JSON
                    String nombreLimpio = item.getNombre().replace("\"", "\\\"");
                    jsonBuilder.append("\"nombre\":\"").append(nombreLimpio).append("\"");
                    jsonBuilder.append("}");
                    if (i < lista.size() - 1) {
                        jsonBuilder.append(",");
                    }
                }
                jsonBuilder.append("]");
                json = jsonBuilder.toString();
            }
        }
        
        // Imprime en la consola del Servidor para verificarlo
        System.out.println(">>> JSON MANUAL ENVIADO: " + json);
        
        try (PrintWriter out = response.getWriter()) {
            out.print(json);
            out.flush();
        }
    }
}
