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
public class CClientes extends ConexionMySQL implements CRUD{
    
    public int id;
    public String nombre;
    public String ap_paterno;
    public String ap_materno;
    public String telefono;
    public String cp;
    public String calles;
    public int ruta;
    public int tipo_cliente;
    public int coordx;
    public int coordy;
    public int R_usuario;
    
    public void registrar_cliente(String nombre, String ap_paterno, String ap_materno, String telefono,
            String cp, String calles, int ruta, int tipo_cliente, int x, int y, int R_usuario){
        this.nombre = nombre;
        this.ap_paterno = ap_paterno;
        this.ap_materno = ap_materno;
        this.telefono = telefono;
        this.cp = cp;
        this.calles = calles;
        this.ruta = ruta;
        this.tipo_cliente = tipo_cliente;
        this.coordx = x;
        this.coordy = y;
        this.R_usuario = R_usuario;
        crear();
    }
    
    public void capturar_id_cliente(int id){
        this.id = id;
    }

    @Override
    public void crear() {
        try {
            String consulta = "INSERT INTO clientes(nombre_cliente, ap_pat_cliente, ap_mat_cliente, telefono_cliente, cp, calles, R_ruta, R_tipo, coordx, coordy, R_usuario) "
                             + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement ps = conexion.prepareStatement(consulta)) {
                ps.setString(1, this.nombre);
                ps.setString(2, this.ap_paterno);
                ps.setString(3, this.ap_materno);
                ps.setString(4, this.telefono);
                ps.setString(5, this.cp);
                ps.setString(6, this.calles);
                ps.setInt(7, this.ruta);
                ps.setInt(8, this.tipo_cliente);
                ps.setInt(9, this.coordx);
                ps.setInt(10, this.coordy);
                ps.setInt(11, this.R_usuario);
                int filasInsertadas = ps.executeUpdate();
                System.out.println("Filas insertadas: " + filasInsertadas);
            }
        } catch (SQLException e) {
        }
    }

    @Override
    public void leer() {
        try{
            try (Statement stmt = conexion.createStatement()) {
                String consulta = "select * from clientes";
                try (ResultSet rs = stmt.executeQuery(consulta)) {
                    while (rs.next()) {
                        System.out.println("ID: " + rs.getInt("ID_cliente") +
                                ", Nombre: " + rs.getString("nombre_cliente") +
                                " " + rs.getString("ap_pat_cliente") +
                                " " + rs.getString("ap_mat_cliente"));
                    }
                }
            }
        }catch(SQLException e){
            System.out.println("No se pudo acceder a la tabla");
        }
    }

    @Override
    public void actualizar(String columna, String valor, int tipo_dato) {
        try {
            String consulta = "";
            if(tipo_dato == 1){
                consulta = "UPDATE clientes SET " + columna + " = \"" + valor + "\" WHERE ID_cliente = " + this.id;
            }
            if(tipo_dato == 2){
                consulta = "UPDATE clientes SET " + columna + " = " + valor + " WHERE ID_cliente = " + this.id;
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
        try {
            String consulta = "DELETE FROM clientes WHERE ID_cliente = ?";
            try (PreparedStatement ps = conexion.prepareStatement(consulta)) {
                ps.setInt(1, this.id);
                int filasEliminadas = ps.executeUpdate();
                System.out.println("Filas eliminadas: " + filasEliminadas);
            }
        } catch (SQLException e) {
        }
    }
    
}