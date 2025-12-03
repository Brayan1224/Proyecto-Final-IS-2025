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
public class CUsuarios extends ConexionMySQL{
    public int ID_usuario;
    public String usuario;
    public String contrasenia;
    public int rol;
    
    public void capturarUsuario(String usuario, String contrasenia){
        this.usuario = usuario;
        this.contrasenia = contrasenia;
    }
    
    public int iniciarSesion(String usuario, String contrasenia){
        capturarUsuario(usuario, contrasenia);
        return verificarUsuario();
    }
    
    private int verificarUsuario() {
        try {
            String consulta = "SELECT ID_usuario, rol FROM usuarios WHERE usuario = \"" + usuario
                    + "\" AND contrasenia = \"" + contrasenia + "\"";

            try (Statement stmt = conexion.createStatement();
                 ResultSet rs = stmt.executeQuery(consulta)) {

                if (rs.next()) {
                    this.ID_usuario = rs.getInt("ID_usuario");
                    this.rol = rs.getInt("rol");
                    return 1;
                } else {
                    System.out.println("Usuario no encontrado.");
                }
            }

        } catch (SQLException e) {
            System.out.println("No se pudo acceder a la tabla");
        }

        return 0;
    }

    public void agregarUsuario(int rol) {
        try {
            String consulta = "INSERT INTO usuarios(usuario, contrasenia, rol) "
                             + "VALUES (?, ?, ?)";

            //pedir que retorne las llaves generadas
            try (PreparedStatement ps = conexion.prepareStatement(consulta, Statement.RETURN_GENERATED_KEYS)) {

                ps.setString(1, this.usuario);
                ps.setString(2, this.contrasenia);
                ps.setInt(3, rol);

                int filasInsertadas = ps.executeUpdate();
                System.out.println("Filas insertadas: " + filasInsertadas);

                //obtener el ID_usuario generado
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        this.ID_usuario = rs.getInt(1);
                        System.out.println("ID_usuario generado: " + this.ID_usuario);
                    }
                }
            }

        } catch (SQLException e) {
            System.out.println("Error al insertar usuario: " + e.getMessage());
        }
    }

    
}