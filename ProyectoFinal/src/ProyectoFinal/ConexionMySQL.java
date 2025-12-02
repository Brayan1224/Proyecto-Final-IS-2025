/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ProyectoFinal;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author braya
 */
public abstract class ConexionMySQL {
    
    // Configuración de la conexión
    private String url = "jdbc:mysql://localhost:3306/epura";
    private String usuario = "root"; 
    private String contraseña = "server"; 
    public Connection conexion = null;
    
    public ConexionMySQL(){
        try {
            // Establecer conexión
            conexion = DriverManager.getConnection(url, usuario, contraseña);
        } catch (SQLException e) {
            System.out.println("Error al conectar: " + e.getMessage());
        }
    }
    
    public void cerrar_conexion(){
        try {
            if (conexion != null) {
                conexion.close();
            }
        } catch (SQLException ex) {
        }
    }
}