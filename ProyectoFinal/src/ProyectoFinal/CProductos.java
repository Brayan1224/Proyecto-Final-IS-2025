/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ProyectoFinal;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author braya
 */
public class CProductos extends ConexionMySQL implements CRUD{
    
    public int id;
    
    public void capturar_id_producto(int id){
        this.id = id;
    }
    
    
    public int vender(int cantidad){
        try{
            try (Statement stmt = conexion.createStatement()) {
                String consulta = "select piezas_disponibles from productos where ID_producto = " + this.id;
                try (ResultSet rs = stmt.executeQuery(consulta)) {
                    if (rs.next()) {
                        int cantidad_producto = rs.getInt("piezas_disponibles");
                        cantidad_producto -= cantidad;
                        if(cantidad_producto >= 0){
                            actualizar("piezas_disponibles", cantidad_producto+"", 2);
                            return 1;
                        }else{
                            System.out.println("Cantidad de producto no disponible");
                        }
                    } else {
                        System.out.println("Producto no encontrado.");
                    }
                }
            }
        }catch(SQLException e){
            System.out.println("No se pudo acceder a la tabla");
        }
        return 0;
    }
    
    public void entrada(int cantidad){
        try{
            try (Statement stmt = conexion.createStatement()) {
                String consulta = "select piezas_disponibles from productos where ID_producto = " + this.id;
                try (ResultSet rs = stmt.executeQuery(consulta)) {
                    if (rs.next()) {
                        int cantidad_producto = rs.getInt("piezas_disponibles");
                        cantidad_producto += cantidad;
                        actualizar("piezas_disponibles", cantidad_producto+"", 2);
                    } else {
                        System.out.println("Producto no encontrado.");
                    }
                }
            }
        }catch(SQLException e){
            System.out.println("No se pudo acceder a la tabla");
        }
    }
    
    @Override
    public void leer(){
        try{
            try (Statement stmt = conexion.createStatement()) {
                String consulta = "select * from productos";
                try (ResultSet rs = stmt.executeQuery(consulta)) {
                    while (rs.next()) {
                        System.out.println("ID: " + rs.getInt("ID_producto") +
                                ", Nombre: " + rs.getString("descripcion_producto") +
                                " Cantidad: " + rs.getInt("piezas_disponibles") +
                                " Precio : " + rs.getFloat("precio"));
                    }
                }
            }
        }catch(SQLException e){
            System.out.println("No se pudo acceder a la tabla");
        }
    }

    @Override
    public void crear() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void actualizar(String columna, String valor, int tipo_dato) {
        try {
            String consulta = "";
            if(tipo_dato == 1){
                consulta = "UPDATE productos SET " + columna + " = \"" + valor + "\" WHERE ID_producto = " + this.id;
            }
            if(tipo_dato == 2){
                consulta = "UPDATE productos SET " + columna + " = " + valor + " WHERE ID_producto = " + this.id;
            }
            try (PreparedStatement ps = conexion.prepareStatement(consulta)) {
                int filasActualizadas = ps.executeUpdate();
                System.out.println("Filas actualizadas: " + filasActualizadas);
            }
        } catch (SQLException e) {
        }
    }

    @Override
    public void eliminar() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
}