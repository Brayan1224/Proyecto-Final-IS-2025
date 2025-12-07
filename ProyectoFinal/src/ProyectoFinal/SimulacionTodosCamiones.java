package ProyectoFinal;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SimulacionTodosCamiones extends JFrame {
    
    private MapaMultiCamiones mapa;
    private JButton btnIniciar;
    private JButton btnPausar;
    private JButton btnDetener;
    private JLabel lblEstado;
    private JTextArea txtLog;
    private Timer animacionTimer;
    private List<CamionEnRuta> camionesActivos;
    private boolean pausado = false;
    
    // Posición de la central
    private static final int CENTRAL_FILA = 0;
    private static final int CENTRAL_COLUMNA = 0;
    
    public SimulacionTodosCamiones() {
        setTitle("Simulación de Todos los Repartos");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        
        camionesActivos = new ArrayList<>();
        
        // Panel superior con controles
        JPanel panelControles = new JPanel(new FlowLayout());
        panelControles.setBackground(new Color(0, 0, 153));
        panelControles.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        btnIniciar = new JButton("▶ Iniciar Simulación");
        btnIniciar.setBackground(new Color(34, 139, 34));
        btnIniciar.setForeground(Color.WHITE);
        btnIniciar.setFont(new Font("Century Gothic", Font.BOLD, 12));
        btnIniciar.addActionListener(e -> iniciarSimulacion());
        
        btnPausar = new JButton("⏸ Pausar");
        btnPausar.setBackground(new Color(255, 193, 7));
        btnPausar.setForeground(Color.BLACK);
        btnPausar.setFont(new Font("Century Gothic", Font.BOLD, 12));
        btnPausar.setEnabled(false);
        btnPausar.addActionListener(e -> pausarReanudar());
        
        btnDetener = new JButton("⏹ Detener");
        btnDetener.setBackground(new Color(220, 53, 69));
        btnDetener.setForeground(Color.WHITE);
        btnDetener.setFont(new Font("Century Gothic", Font.BOLD, 12));
        btnDetener.setEnabled(false);
        btnDetener.addActionListener(e -> detenerSimulacion());
        
        lblEstado = new JLabel("Esperando inicio...");
        lblEstado.setForeground(Color.WHITE);
        lblEstado.setFont(new Font("Century Gothic", Font.BOLD, 14));
        
        panelControles.add(btnIniciar);
        panelControles.add(btnPausar);
        panelControles.add(btnDetener);
        panelControles.add(Box.createHorizontalStrut(20));
        panelControles.add(lblEstado);
        
        // Mapa
        mapa = new MapaMultiCamiones();
        JScrollPane scrollMapa = new JScrollPane(mapa);
        scrollMapa.setPreferredSize(new Dimension(650, 650));
        
        // Panel de log
        JPanel panelLog = new JPanel(new BorderLayout());
        panelLog.setBorder(BorderFactory.createTitledBorder("Registro de Entregas"));
        panelLog.setPreferredSize(new Dimension(300, 650));
        
        txtLog = new JTextArea();
        txtLog.setEditable(false);
        txtLog.setFont(new Font("Monospaced", Font.PLAIN, 11));
        JScrollPane scrollLog = new JScrollPane(txtLog);
        panelLog.add(scrollLog, BorderLayout.CENTER);
        
        // Layout principal
        add(panelControles, BorderLayout.NORTH);
        add(scrollMapa, BorderLayout.CENTER);
        add(panelLog, BorderLayout.EAST);
        
        pack();
        setLocationRelativeTo(null);
    }
    
    private void iniciarSimulacion() {
        // Limpiar mapa y log
        mapa.limpiar();
        txtLog.setText("");
        camionesActivos.clear();
        
        agregarLog("=== INICIANDO SIMULACIÓN GENERAL ===\n");
        
        // Cargar todos los camiones con rutas
        Map<Integer, List<ClienteDestino>> camionesYClientes = cargarTodosLosCamiones();
        
        if (camionesYClientes.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "No hay repartos pendientes", 
                "Aviso", 
                JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        agregarLog("Total de camiones: " + camionesYClientes.size() + "\n\n");
        
        // Crear cada camión con su ruta
        AlgoritmoAEstrella aEstrella = new AlgoritmoAEstrella(20, 20, mapa.getMapa());
        
        for (Map.Entry<Integer, List<ClienteDestino>> entry : camionesYClientes.entrySet()) {
            int idCamion = entry.getKey();
            List<ClienteDestino> clientes = entry.getValue();
            
            agregarLog("🚚 Camión " + obtenerMatriculaCamion(idCamion) + " - " + clientes.size() + " cliente(s)\n");
            
            // Calcular ruta completa optimizada para este camión
            List<Cuadricula> rutaCompleta = calcularRutaCompleta(aEstrella, clientes);
            
            if (!rutaCompleta.isEmpty()) {
                CamionEnRuta camion = new CamionEnRuta();
                camion.idCamion = idCamion;
                camion.matricula = obtenerMatriculaCamion(idCamion);
                camion.ruta = rutaCompleta;
                camion.indiceActual = 0;
                camion.clientes = clientes;
                camion.clientesEntregados = 0;
                camion.color = generarColorCamion(camionesActivos.size());
                
                camionesActivos.add(camion);
                mapa.agregarCamion(camion);
                
                // Agregar clientes al mapa (ya están en orden optimizado)
                for (ClienteDestino cliente : clientes) {
                    mapa.agregarCliente(cliente.fila, cliente.columna, cliente.nombre, camion.color);
                }
                
                agregarLog("\n");
            }
        }
        
        // Iniciar animación
        btnIniciar.setEnabled(false);
        btnPausar.setEnabled(true);
        btnDetener.setEnabled(true);
        lblEstado.setText("🚚 Repartos en progreso - " + camionesActivos.size() + " camión(es)");
        
        animarTodosCamiones();
    }
    
    private Map<Integer, List<ClienteDestino>> cargarTodosLosCamiones() {
        Map<Integer, List<ClienteDestino>> resultado = new HashMap<>();
        CRutas r = new CRutas();
        
        try {
            Statement stmt = r.conexion.createStatement();
            String consulta = "SELECT DISTINCT camiones.ID_camion, camiones.matricula, " +
                            "clientes.coordx, clientes.coordy, " +
                            "CONCAT(clientes.nombre_cliente, ' ', clientes.ap_pat_cliente) as nombre " +
                            "FROM rutas_asignadas " +
                            "JOIN camiones ON camiones.ID_camion = rutas_asignadas.R_camion " +
                            "JOIN pedidos ON pedidos.ID_pedido = rutas_asignadas.R_pedido " +
                            "JOIN clientes ON clientes.ID_cliente = pedidos.R_cliente " +
                            "ORDER BY camiones.ID_camion";
            
            ResultSet rs = stmt.executeQuery(consulta);
            
            while (rs.next()) {
                int idCamion = rs.getInt("ID_camion");
                
                ClienteDestino cliente = new ClienteDestino();
                cliente.fila = rs.getInt("coordx");
                cliente.columna = rs.getInt("coordy");
                cliente.nombre = rs.getString("nombre");
                
                resultado.computeIfAbsent(idCamion, k -> new ArrayList<>()).add(cliente);
            }
            
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            System.err.println("Error al cargar camiones: " + e.getMessage());
        }
        
        return resultado;
    }
    
    private String obtenerMatriculaCamion(int idCamion) {
        CRutas r = new CRutas();
        try {
            Statement stmt = r.conexion.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT matricula FROM camiones WHERE ID_camion = " + idCamion);
            if (rs.next()) {
                return rs.getString("matricula");
            }
        } catch (SQLException e) {
            System.err.println("Error: " + e.getMessage());
        }
        return "CAM-" + idCamion;
    }
    
    private List<Cuadricula> calcularRutaCompleta(AlgoritmoAEstrella aEstrella, List<ClienteDestino> clientes) {
        List<Cuadricula> rutaCompleta = new ArrayList<>();
        
        if (clientes.isEmpty()) {
            return rutaCompleta;
        }
        
        // Optimizar orden de visita usando algoritmo del vecino más cercano
        List<ClienteDestino> clientesOrdenados = ordenarClientesPorCercania(clientes);
        
        int filaActual = CENTRAL_FILA;
        int columnaActual = CENTRAL_COLUMNA;
        
        agregarLog("   Orden de entrega optimizado:\n");
        int orden = 1;
        
        for (ClienteDestino cliente : clientesOrdenados) {
            Cuadricula inicio = mapa.obtenerCuadricula(filaActual, columnaActual);
            Cuadricula destino = mapa.obtenerCuadricula(cliente.fila, cliente.columna);
            
            if (destino != null && destino.esObstaculo()) {
                List<Cuadricula> segmento = aEstrella.encontrarCamino(inicio, destino);
                
                if (segmento != null && !segmento.isEmpty()) {
                    rutaCompleta.addAll(segmento);
                    Cuadricula ultima = segmento.get(segmento.size() - 1);
                    filaActual = ultima.getFila();
                    columnaActual = ultima.getColumna();
                    
                    agregarLog("   " + orden + ". " + cliente.nombre + 
                              " (distancia: " + segmento.size() + " pasos)\n");
                    orden++;
                }
            }
        }
        
        // Actualizar lista original con el orden optimizado
        clientes.clear();
        clientes.addAll(clientesOrdenados);
        
        return rutaCompleta;
    }
    
    // Algoritmo del vecino más cercano para optimizar ruta
    private List<ClienteDestino> ordenarClientesPorCercania(List<ClienteDestino> clientes) {
        if (clientes.size() <= 1) {
            return new ArrayList<>(clientes);
        }
        
        List<ClienteDestino> ordenados = new ArrayList<>();
        List<ClienteDestino> pendientes = new ArrayList<>(clientes);
        
        int filaActual = CENTRAL_FILA;
        int columnaActual = CENTRAL_COLUMNA;
        
        // Mientras haya clientes pendientes
        while (!pendientes.isEmpty()) {
            ClienteDestino masCercano = null;
            double distanciaMinima = Double.MAX_VALUE;
            
            // Buscar el cliente más cercano a la posición actual
            for (ClienteDestino cliente : pendientes) {
                double distancia = calcularDistanciaManhattan(
                    filaActual, columnaActual, 
                    cliente.fila, cliente.columna
                );
                
                if (distancia < distanciaMinima) {
                    distanciaMinima = distancia;
                    masCercano = cliente;
                }
            }
            
            if (masCercano != null) {
                ordenados.add(masCercano);
                pendientes.remove(masCercano);
                filaActual = masCercano.fila;
                columnaActual = masCercano.columna;
            }
        }
        
        return ordenados;
    }
    
    // Calcular distancia Manhattan entre dos puntos
    private double calcularDistanciaManhattan(int fila1, int col1, int fila2, int col2) {
        return Math.abs(fila1 - fila2) + Math.abs(col1 - col2);
    }
    
    private void animarTodosCamiones() {
        animacionTimer = new Timer(400, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (pausado) return;
                
                boolean algunoActivo = false;
                
                for (CamionEnRuta camion : camionesActivos) {
                    if (camion.indiceActual < camion.ruta.size()) {
                        algunoActivo = true;
                        
                        Cuadricula posicionActual = camion.ruta.get(camion.indiceActual);
                        camion.posicionActual = posicionActual;
                        
                        // Verificar si llegó a un cliente
                        for (ClienteDestino cliente : camion.clientes) {
                            if (posicionActual.getFila() == cliente.fila && 
                                posicionActual.getColumna() == cliente.columna) {
                                if (!cliente.entregado) {
                                    cliente.entregado = true;
                                    camion.clientesEntregados++;
                                    agregarLog("✅ " + camion.matricula + " entregó a " + cliente.nombre + "\n");
                                }
                            }
                        }
                        
                        camion.indiceActual++;
                    }
                }
                
                mapa.repaint();
                
                if (!algunoActivo) {
                    animacionTimer.stop();
                    finalizarSimulacion();
                }
            }
        });
        
        animacionTimer.start();
    }
    
    private void pausarReanudar() {
        pausado = !pausado;
        if (pausado) {
            btnPausar.setText("▶ Reanudar");
            lblEstado.setText("⏸ Simulación pausada");
        } else {
            btnPausar.setText("⏸ Pausar");
            lblEstado.setText("🚚 Repartos en progreso");
        }
    }
    
    private void detenerSimulacion() {
        if (animacionTimer != null && animacionTimer.isRunning()) {
            animacionTimer.stop();
        }
        
        btnIniciar.setEnabled(true);
        btnPausar.setEnabled(false);
        btnDetener.setEnabled(false);
        lblEstado.setText("⏹ Simulación detenida");
        agregarLog("\n⏹ SIMULACIÓN DETENIDA MANUALMENTE\n");
    }
    
    private void finalizarSimulacion() {
        btnIniciar.setEnabled(true);
        btnPausar.setEnabled(false);
        btnDetener.setEnabled(false);
        lblEstado.setText("✅ Simulación completada");
        
        agregarLog("\n=== SIMULACIÓN COMPLETADA ===\n");
        int totalEntregas = 0;
        for (CamionEnRuta camion : camionesActivos) {
            agregarLog("🚚 " + camion.matricula + ": " + camion.clientesEntregados + " entregas\n");
            totalEntregas += camion.clientesEntregados;
        }
        agregarLog("TOTAL: " + totalEntregas + " entregas realizadas\n");
        
        JOptionPane.showMessageDialog(this,
            "Simulación completada!\n" +
            totalEntregas + " entregas realizadas por " + camionesActivos.size() + " camión(es)",
            "Finalizado",
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void agregarLog(String mensaje) {
        txtLog.append(mensaje);
        txtLog.setCaretPosition(txtLog.getDocument().getLength());
    }
    
    private Color generarColorCamion(int indice) {
        Color[] colores = {
            new Color(0, 123, 255),    // Azul
            new Color(40, 167, 69),    // Verde
            new Color(255, 193, 7),    // Amarillo
            new Color(220, 53, 69),    // Rojo
            new Color(111, 66, 193),   // Púrpura
            new Color(23, 162, 184),   // Cian
            new Color(255, 87, 34),    // Naranja
            new Color(156, 39, 176)    // Magenta
        };
        return colores[indice % colores.length];
    }
    
    // Clases internas
    private static class ClienteDestino {
        int fila;
        int columna;
        String nombre;
        boolean entregado = false;
    }
    
    private static class CamionEnRuta {
        int idCamion;
        String matricula;
        List<Cuadricula> ruta;
        int indiceActual;
        Cuadricula posicionActual;
        List<ClienteDestino> clientes;
        int clientesEntregados;
        Color color;
    }
    
    // Mapa de visualización
    private class MapaMultiCamiones extends JPanel {
        private int filas = 20;
        private int columnas = 20;
        private int tamanioCelda = 30;
        private Cuadricula[][] mapa;
        private List<CamionEnRuta> camiones;
        private List<ClienteMarcado> clientes;
        
        private static final Color COLOR_CALLE = new Color(200, 200, 200);
        private static final Color COLOR_CASA = Color.BLACK;
        private static final Color COLOR_CENTRAL = new Color(255, 165, 0);
        
        public MapaMultiCamiones() {
            this.mapa = new Cuadricula[filas][columnas];
            this.camiones = new ArrayList<>();
            this.clientes = new ArrayList<>();
            
            // Inicializar mapa
            int idActual = 0;
            for (int i = 0; i < filas; i++) {
                for (int j = 0; j < columnas; j++) {
                    mapa[i][j] = new Cuadricula(idActual++, i, j);
                }
            }
            
            crearCroquis();
            
            setPreferredSize(new Dimension(columnas * tamanioCelda, filas * tamanioCelda));
            setBackground(new Color(34, 139, 34));
        }
        
        private void crearCroquis() {
            crearManzana(2, 2, 3, 4);
            crearManzana(2, 8, 3, 4);
            crearManzana(2, 14, 3, 4);
            crearManzana(7, 2, 4, 4);
            crearManzana(7, 8, 4, 5);
            crearManzana(7, 15, 4, 3);
            crearManzana(13, 2, 4, 3);
            crearManzana(13, 7, 4, 4);
            crearManzana(13, 13, 4, 5);
            crearCasa(1, 7);
            crearCasa(1, 13);
            crearCasa(6, 6);
            crearCasa(12, 6);
            crearCasa(18, 8);
            crearCasa(18, 14);
        }
        
        private void crearManzana(int filaInicio, int colInicio, int alto, int ancho) {
            for (int i = filaInicio; i < filaInicio + alto && i < filas; i++) {
                for (int j = colInicio; j < colInicio + ancho && j < columnas; j++) {
                    mapa[i][j].setTipo(1);
                }
            }
        }
        
        private void crearCasa(int fila, int col) {
            if (fila < filas && col < columnas) {
                mapa[fila][col].setTipo(1);
            }
        }
        
        public Cuadricula[][] getMapa() {
            return mapa;
        }
        
        public Cuadricula obtenerCuadricula(int fila, int col) {
            if (fila >= 0 && fila < filas && col >= 0 && col < columnas) {
                return mapa[fila][col];
            }
            return null;
        }
        
        public void agregarCamion(CamionEnRuta camion) {
            camiones.add(camion);
        }
        
        public void agregarCliente(int fila, int columna, String nombre, Color color) {
            ClienteMarcado cliente = new ClienteMarcado();
            cliente.cuadricula = obtenerCuadricula(fila, columna);
            cliente.nombre = nombre;
            cliente.color = color;
            clientes.add(cliente);
        }
        
        public void limpiar() {
            camiones.clear();
            clientes.clear();
            repaint();
        }
        
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            // Dibujar cuadrículas
            for (int i = 0; i < filas; i++) {
                for (int j = 0; j < columnas; j++) {
                    Cuadricula cuadricula = mapa[i][j];
                    int x = j * tamanioCelda;
                    int y = i * tamanioCelda;
                    
                    if (cuadricula.esObstaculo()) {
                        g.setColor(COLOR_CASA);
                    } else {
                        g.setColor(COLOR_CALLE);
                    }
                    
                    g.fillRect(x, y, tamanioCelda, tamanioCelda);
                    
                    // Central
                    if (i == CENTRAL_FILA && j == CENTRAL_COLUMNA) {
                        g.setColor(COLOR_CENTRAL);
                        g.fillRect(x + 2, y + 2, tamanioCelda - 4, tamanioCelda - 4);
                        g.setColor(Color.BLACK);
                        g.setFont(new Font("Arial", Font.BOLD, 10));
                        g.drawString("C", x + 11, y + 19);
                    }
                }
            }
            
            // Dibujar clientes
            for (ClienteMarcado cliente : clientes) {
                if (cliente.cuadricula != null) {
                    int x = cliente.cuadricula.getColumna() * tamanioCelda;
                    int y = cliente.cuadricula.getFila() * tamanioCelda;
                    
                    g.setColor(cliente.color);
                    g.fillOval(x + 8, y + 8, tamanioCelda - 16, tamanioCelda - 16);
                }
            }
            
            // Dibujar camiones
            for (CamionEnRuta camion : camiones) {
                if (camion.posicionActual != null) {
                    int x = camion.posicionActual.getColumna() * tamanioCelda;
                    int y = camion.posicionActual.getFila() * tamanioCelda;
                    
                    g.setColor(camion.color);
                    g.fillRoundRect(x + 3, y + 3, tamanioCelda - 6, tamanioCelda - 6, 8, 8);
                    g.setColor(Color.WHITE);
                    g.setFont(new Font("Arial", Font.BOLD, 14));
                    g.drawString("🚚", x + 7, y + 21);
                }
            }
            
            // Grid
            g.setColor(new Color(150, 150, 150, 100));
            for (int i = 0; i <= filas; i++) {
                g.drawLine(0, i * tamanioCelda, columnas * tamanioCelda, i * tamanioCelda);
            }
            for (int j = 0; j <= columnas; j++) {
                g.drawLine(j * tamanioCelda, 0, j * tamanioCelda, filas * tamanioCelda);
            }
        }
        
        private class ClienteMarcado {
            Cuadricula cuadricula;
            String nombre;
            Color color;
        }
    }
}