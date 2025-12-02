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
public class CRutas extends ConexionMySQL{
    
    public int id_camion, capacidad_maxg, capacidad_maxml, pedidoid, cant_garrafones, capacidad_ml;
    
    public void entregarProductos(){
        restaurarCapacidadesCamiones();
        truncarRutasAsignadas();
        actualizarStatusPedidos();
    }
    
    public void asignarRutas() {
        try {
            // Consulta de pedidos pendientes
            String consultaPedidos = 
                "SELECT " +
                "    detalles_pedido.R_pedido AS pedidoid, " +
                "    SUM(CASE WHEN detalles_pedido.R_producto = 1 THEN detalles_pedido.cantidad ELSE 0 END) AS cantidad_garrafones, " +
                "    SUM(CASE WHEN detalles_pedido.R_producto != 1 THEN detalles_pedido.cantidad * productos.capacidad ELSE 0 END) AS capacidad_total " +
                "FROM detalles_pedido " +
                "JOIN productos ON detalles_pedido.R_producto = productos.ID_producto " +
                "JOIN pedidos ON pedidos.ID_pedido = detalles_pedido.R_pedido " +
                "WHERE pedidos.R_status = 1 " +
                "GROUP BY detalles_pedido.R_pedido";

            // Consulta de camiones
            String consultaCamiones = 
                "SELECT ID_camion, capacidad_maxima_gact, capacidad_maxima_mlact FROM camiones";

            // Consultas para asignar ruta y actualizar capacidades
            String consultaAsignarRuta = 
                "INSERT INTO rutas_asignadas (R_camion, R_pedido) VALUES (?, ?)";
            String consultaActualizarCamion = 
                "UPDATE camiones SET capacidad_maxima_gact = ?, capacidad_maxima_mlact = ? WHERE ID_camion = ?";
            String consultaActualizarPedido = 
                "UPDATE pedidos SET R_status = 2 WHERE ID_pedido = ?"; // Actualiza estatus del pedido a 2

            try (
                Statement stmtPedidos = conexion.createStatement();
                ResultSet rsPedidos = stmtPedidos.executeQuery(consultaPedidos);
                PreparedStatement psAsignarRuta = conexion.prepareStatement(consultaAsignarRuta);
                PreparedStatement psActualizarCamion = conexion.prepareStatement(consultaActualizarCamion);
                PreparedStatement psActualizarPedido = conexion.prepareStatement(consultaActualizarPedido);
            ) {
                while (rsPedidos.next()) {
                    int pedidoId = rsPedidos.getInt("pedidoid");
                    int cantidadGarrafones = rsPedidos.getInt("cantidad_garrafones");
                    int capacidadTotal = rsPedidos.getInt("capacidad_total");

                    // Consultar camiones disponibles
                    try (Statement stmtCamiones = conexion.createStatement();
                         ResultSet rsCamiones = stmtCamiones.executeQuery(consultaCamiones)) {

                        while (rsCamiones.next()) {
                            int idCamion = rsCamiones.getInt("ID_camion");
                            int capacidadGarrafonesActual = rsCamiones.getInt("capacidad_maxima_gact");
                            int capacidadMlActual = rsCamiones.getInt("capacidad_maxima_mlact");

                            if (capacidadGarrafonesActual >= cantidadGarrafones && capacidadMlActual >= capacidadTotal) {
                                // Asignar ruta
                                psAsignarRuta.setInt(1, idCamion);
                                psAsignarRuta.setInt(2, pedidoId);
                                psAsignarRuta.executeUpdate();

                                // Actualizar capacidad del camión
                                int nuevaCapacidadGarrafones = capacidadGarrafonesActual - cantidadGarrafones;
                                int nuevaCapacidadMl = capacidadMlActual - capacidadTotal;
                                psActualizarCamion.setInt(1, nuevaCapacidadGarrafones);
                                psActualizarCamion.setInt(2, nuevaCapacidadMl);
                                psActualizarCamion.setInt(3, idCamion);
                                psActualizarCamion.executeUpdate();

                                // Actualizar estatus del pedido
                                psActualizarPedido.setInt(1, pedidoId);
                                psActualizarPedido.executeUpdate();

                                break; // Camión asignado, salir del bucle de camiones
                            }
                        }
                    }
                }
            }
        } catch (SQLException e) { // Para identificar errores con más detalle
            // Para identificar errores con más detalle
            System.out.println("No se pudo asignar rutas.");
        }
    }

    
    public void restaurarCapacidadesCamiones() {
    String consulta = "UPDATE camiones " +
                      "SET capacidad_maxima_gact = capacidad_maxima_garrafones, " +
                      "capacidad_maxima_mlact = capacidad_maxima_ml";

        try (Statement stmt = conexion.createStatement()) {
            int filasActualizadas = stmt.executeUpdate(consulta);
            System.out.println("Capacidades restauradas en " + filasActualizadas + " camiones.");
        } catch (SQLException e) {
            System.out.println("Error al restaurar las capacidades de los camiones.");
        }
    }
    
    public void truncarRutasAsignadas() {
        String consulta = "truncate table rutas_asignadas";

        try (Statement stmt = conexion.createStatement()) {
            stmt.executeUpdate(consulta);
            System.out.println("Todos los registros de la tabla rutas_asignadas han sido eliminados.");
        } catch (SQLException e) {
            System.out.println("Error al truncar la tabla rutas_asignadas.");
        }
    }
    
    public void actualizarStatusPedidos() {
        String consulta = "UPDATE pedidos SET R_status = 3 WHERE R_status = 2";

        try (PreparedStatement ps = conexion.prepareStatement(consulta)) {
            int filasActualizadas = ps.executeUpdate(); // Ejecuta la actualización
            System.out.println("Se actualizaron " + filasActualizadas + " filas.");
        } catch (SQLException e) {
            System.out.println("Error al actualizar los estados de los pedidos: " + e.getMessage());
        }
    }


}