/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ProyectoFinal;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 *
 * @author braya
 */
public class CPedidos extends ConexionMySQL implements CRUD{
    
    public int id;
    public String fechaPedido;
    public String fechaEntrega;
    public int R_cliente;
    public int R_status;
    
    public void capturar_id_pedido(int id){
        this.id = id;
    }
    
    public void generarPedido(String fechaPedido, String fechaEntrega, int R_cliente, int R_status, ArrayList<Integer> productos, ArrayList<Integer> cantidades){
        this.fechaPedido = fechaPedido;
        this.fechaEntrega = fechaEntrega;
        this.R_cliente = R_cliente;
        this.R_status = R_status;
        crear();
        try {
            if(productos.size() == cantidades.size()){
                String consulta = "INSERT INTO detalles_pedido(R_pedido, R_producto, cantidad) "
                             + "VALUES ";
                int i = 0;
                for (; i < productos.size()-1; i++){
                    consulta += "(" + this.id + ", " + productos.get(i) + ", " + cantidades.get(i) + "), ";
                }
                consulta += "(" + this.id + ", " + productos.get(i) + ", " + cantidades.get(i) + ");";
                System.out.println(consulta);
                try (PreparedStatement ps = conexion.prepareStatement(consulta)) {
                    int filasInsertadas = ps.executeUpdate();
                    System.out.println("Filas insertadas: " + filasInsertadas);
                }
            }
        } catch (SQLException e) {
        }
    }
    
    public void generarTicket(){
        try{
            try (Statement stmt = conexion.createStatement()) {
                String consulta = "select concat(clientes.nombre_cliente, \" \", clientes.ap_pat_cliente, \" \", clientes.ap_mat_cliente) as cliente, "
                        + "pedidos.fecha_pedido as fecha_pedido, pedidos.fecha_entrega as fecha_entrega "
                        + "from clientes join pedidos on pedidos.R_cliente = clientes.ID_cliente "
                        + "where pedidos.ID_pedido = " + this.id;
                ResultSet rs = stmt.executeQuery(consulta);
                if (rs.next()) {
                    System.out.println("Nombre del cliente: " + rs.getString("cliente") +
                            ", Fecha Pedido: " + rs.getString("fecha_pedido") +
                            ", Fecha Entrega: " + rs.getString("fecha_entrega"));
                }
                consulta = "select productos.descripcion_producto as producto, productos.precio as precio_unitario,"
                        + " detalles_pedido.cantidad as cantidad, sum(productos.precio*detalles_pedido.cantidad) as total "
                        + "from detalles_pedido join productos on detalles_pedido.R_producto = productos.ID_producto "
                        + "where detalles_pedido.R_pedido = " + this.id + " group by producto, precio_unitario, cantidad";
                rs = stmt.executeQuery(consulta);
                float sumaTotal = 0;
                while (rs.next()) {
                    System.out.println("Producto: " + rs.getString("producto") +
                            ", Precio Unitario: " + rs.getFloat("precio_unitario") +
                            ", Cantidad: " + rs.getInt("cantidad") +
                            ", Total: " + rs.getFloat("total"));
                    sumaTotal += rs.getFloat("total");
                }
                System.out.println("Total de Pedido: " + sumaTotal);
                rs.close();
            }
        }catch(SQLException e){
            System.out.println("No se pudo acceder a la tabla");
        }
    }

    @Override
    public void crear() {
        try {
            String consulta = "INSERT INTO pedidos(fecha_pedido, fecha_entrega, R_cliente, R_status) "
                             + "VALUES (?, ?, ?, ?)";
            try (PreparedStatement ps = conexion.prepareStatement(consulta, Statement.RETURN_GENERATED_KEYS)) {
                ps.setString(1, this.fechaPedido);
                ps.setString(2, this.fechaEntrega);
                ps.setInt(3, this.R_cliente);
                ps.setInt(4, this.R_status);
                int filasInsertadas = ps.executeUpdate();
                System.out.println("Filas insertadas: " + filasInsertadas);
                try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        this.id = generatedKeys.getInt(1);
                        System.out.println("ID generado: " + this.id);
                    }
                }
            }
        } catch (SQLException e) {
        }
    }

    @Override
    public void leer() {
        try{
            try (Statement stmt = conexion.createStatement()) {
                String consulta = "select pedidos.ID_pedido as pedido, fecha_pedido, fecha_entrega, status_pedidos.status_pedido as status, " +
                        "concat(clientes.nombre_cliente, \" \", clientes.ap_pat_cliente, \" \", clientes.ap_mat_cliente) as cliente " +
                        "from pedidos join clientes on pedidos.R_cliente = clientes.ID_cliente join status_pedidos on " +
                        "status_pedidos.ID_status = pedidos.R_status order by pedido";
                try (ResultSet rs = stmt.executeQuery(consulta)) {
                    while (rs.next()) {
                        System.out.println("ID: " + rs.getInt("pedido") +
                                ", Nombre: " + rs.getString("fecha_pedido") +
                                " " + rs.getString("fecha_entrega") +
                                " " + rs.getString("status") +
                                " " + rs.getString("cliente"));
                    }
                    // Cerrar conexión
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
                consulta = "UPDATE pedidos SET " + columna + " = \"" + valor + "\" WHERE ID_pedido = " + this.id;
            }
            if(tipo_dato == 2){
                consulta = "UPDATE pedidos SET " + columna + " = " + valor + " WHERE ID_pedido = " + this.id;
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
            String consulta = "DELETE FROM detalles_pedido WHERE R_pedido = ?";
            PreparedStatement ps = conexion.prepareStatement(consulta);
            ps.setInt(1, this.id);
            int filasEliminadas = ps.executeUpdate();
            System.out.println("Filas eliminadas: " + filasEliminadas);
            consulta = "DELETE FROM pedidos WHERE ID_pedido = ?";
            ps = conexion.prepareStatement(consulta);
            ps.setInt(1, this.id);
            filasEliminadas = ps.executeUpdate();
            System.out.println("Filas eliminadas: " + filasEliminadas);
            ps.close();
        } catch (SQLException e) {
        }
    }
    
}