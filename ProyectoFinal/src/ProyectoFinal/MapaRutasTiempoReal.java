package ProyectoFinal;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class MapaRutasTiempoReal extends JFrame {
    
    private MapaVisualizacion mapa;
    private JComboBox<String> comboCamiones;
    private JButton btnIniciarRuta;
    private JButton btnDetener;
    private JLabel lblEstado;
    private Timer animacionTimer;
    private int idCamionEspecifico = -1;
    
    // Posición de la central (esquina superior izquierda)
    private static final int CENTRAL_FILA = 0;
    private static final int CENTRAL_COLUMNA = 0;
    
    // Constructor sin parámetros (modo manual)
    public MapaRutasTiempoReal() {
        this(-1, null);
    }
    
    // Constructor con camión específico (modo automático) - PÚBLICO
    public MapaRutasTiempoReal(int idCamion, String placaCamion) {
        this.idCamionEspecifico = idCamion;
        
        setTitle("Mapa de Rutas en Tiempo Real" + 
                (placaCamion != null ? " - Camión: " + placaCamion : ""));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        
        // Crear el mapa de visualización
        mapa = new MapaVisualizacion();
        
        // Panel de controles
        JPanel panelControles = new JPanel();
        panelControles.setBackground(new Color(0, 0, 153));
        panelControles.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JLabel lblCamion = new JLabel("Seleccionar Camión:");
        lblCamion.setForeground(Color.WHITE);
        lblCamion.setFont(new Font("Century Gothic", Font.BOLD, 12));
        
        comboCamiones = new JComboBox<>();
        comboCamiones.setFont(new Font("Century Gothic", Font.PLAIN, 12));
        cargarCamionesConRutas();
        
        btnIniciarRuta = new JButton("🚚 Iniciar Ruta");
        btnIniciarRuta.setBackground(new Color(34, 139, 34));
        btnIniciarRuta.setForeground(Color.WHITE);
        btnIniciarRuta.setFont(new Font("Century Gothic", Font.BOLD, 12));
        btnIniciarRuta.addActionListener(e -> iniciarRuta());
        
        btnDetener = new JButton("⏹ Detener");
        btnDetener.setBackground(new Color(220, 53, 69));
        btnDetener.setForeground(Color.WHITE);
        btnDetener.setFont(new Font("Century Gothic", Font.BOLD, 12));
        btnDetener.setEnabled(false);
        btnDetener.addActionListener(e -> detenerAnimacion());
        
        lblEstado = new JLabel("Esperando...");
        lblEstado.setForeground(Color.WHITE);
        lblEstado.setFont(new Font("Century Gothic", Font.PLAIN, 12));
        
        panelControles.add(lblCamion);
        panelControles.add(comboCamiones);
        panelControles.add(btnIniciarRuta);
        panelControles.add(btnDetener);
        panelControles.add(lblEstado);
        
        add(panelControles, BorderLayout.NORTH);
        add(new JScrollPane(mapa), BorderLayout.CENTER);
        
        setSize(800, 700);
        setLocationRelativeTo(null);
        
        // Si se especificó un camión, iniciar automáticamente
        if (idCamionEspecifico != -1) {
            SwingUtilities.invokeLater(() -> {
                iniciarRutaAutomatica(idCamionEspecifico);
            });
        }
    }
    
    private void cargarCamionesConRutas() {
        CRutas r = new CRutas();
        try {
            Statement stmt = r.conexion.createStatement();
            String consulta = "SELECT DISTINCT camiones.matricula, camiones.ID_camion " +
                            "FROM rutas_asignadas " +
                            "JOIN camiones ON camiones.ID_camion = rutas_asignadas.R_camion " +
                            "ORDER BY camiones.matricula";
            ResultSet rs = stmt.executeQuery(consulta);
            
            while (rs.next()) {
                String item = rs.getString("matricula") + " (ID: " + rs.getInt("ID_camion") + ")";
                comboCamiones.addItem(item);
            }
            
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            System.err.println("Error al cargar camiones: " + e.getMessage());
        }
    }
    
    private void iniciarRuta() {
        if (comboCamiones.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Selecciona un camión primero");
            return;
        }
        
        // Extraer ID del camión del ComboBox
        String seleccion = comboCamiones.getSelectedItem().toString();
        int idCamion = Integer.parseInt(seleccion.split("ID: ")[1].replace(")", ""));
        
        iniciarRutaAutomatica(idCamion);
    }
    
    private void iniciarRutaAutomatica(int idCamion) {
        // Limpiar mapa
        mapa.limpiar();
        
        // Establecer posición inicial (central)
        mapa.setPosicionCamion(CENTRAL_FILA, CENTRAL_COLUMNA);
        
        // Cargar clientes del camión
        List<ClienteDestino> clientes = cargarClientesPorCamion(idCamion);
        
        if (clientes.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No hay clientes asignados a este camión");
            return;
        }
        
        // Marcar clientes en el mapa
        for (ClienteDestino cliente : clientes) {
            mapa.agregarCliente(cliente.fila, cliente.columna, cliente.nombre);
        }
        
        // Calcular y mostrar ruta
        calcularYMostrarRuta(clientes);
        
        btnIniciarRuta.setEnabled(false);
        btnDetener.setEnabled(true);
        lblEstado.setText("Ruta activa - " + clientes.size() + " cliente(s)");
    }
    
    private List<ClienteDestino> cargarClientesPorCamion(int idCamion) {
        List<ClienteDestino> clientes = new ArrayList<>();
        CRutas r = new CRutas();
        
        try {
            Statement stmt = r.conexion.createStatement();
            String consulta = "SELECT clientes.coordx, clientes.coordy, " +
                            "CONCAT(clientes.nombre_cliente, ' ', clientes.ap_pat_cliente) as nombre " +
                            "FROM rutas_asignadas " +
                            "JOIN pedidos ON pedidos.ID_pedido = rutas_asignadas.R_pedido " +
                            "JOIN clientes ON clientes.ID_cliente = pedidos.R_cliente " +
                            "WHERE rutas_asignadas.R_camion = " + idCamion;
            
            ResultSet rs = stmt.executeQuery(consulta);
            
            while (rs.next()) {
                ClienteDestino cliente = new ClienteDestino();
                cliente.fila = rs.getInt("coordx");
                cliente.columna = rs.getInt("coordy");
                cliente.nombre = rs.getString("nombre");
                clientes.add(cliente);
            }
            
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            System.err.println("Error al cargar clientes: " + e.getMessage());
        }
        
        return clientes;
    }
    
    private void calcularYMostrarRuta(List<ClienteDestino> clientes) {
        AlgoritmoAEstrella aEstrella = new AlgoritmoAEstrella(20, 20, mapa.getMapa());
        
        List<Cuadricula> rutaCompleta = new ArrayList<>();
        int filaActual = CENTRAL_FILA;
        int columnaActual = CENTRAL_COLUMNA;
        
        // Calcular ruta desde central hasta cada cliente
        for (ClienteDestino cliente : clientes) {
            Cuadricula inicio = mapa.obtenerCuadricula(filaActual, columnaActual);
            Cuadricula destino = mapa.obtenerCuadricula(cliente.fila, cliente.columna);
            
            if (destino != null && destino.esObstaculo()) {
                // Buscar ruta a la casa
                List<Cuadricula> segmento = aEstrella.encontrarCamino(inicio, destino);
                
                if (segmento != null) {
                    rutaCompleta.addAll(segmento);
                    // Actualizar posición actual (última posición del camino)
                    if (segmento.size() > 0) {
                        Cuadricula ultima = segmento.get(segmento.size() - 1);
                        filaActual = ultima.getFila();
                        columnaActual = ultima.getColumna();
                    }
                }
            }
        }
        
        // Mostrar ruta completa
        mapa.establecerRuta(rutaCompleta);
        
        // Iniciar animación
        animarCamion(rutaCompleta);
    }
    
    private void animarCamion(List<Cuadricula> ruta) {
        if (ruta == null || ruta.isEmpty()) {
            return;
        }
        
        final int[] indice = {0};
        
        animacionTimer = new Timer(300, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (indice[0] < ruta.size()) {
                    Cuadricula actual = ruta.get(indice[0]);
                    mapa.setPosicionCamion(actual.getFila(), actual.getColumna());
                    lblEstado.setText("Entregando... Paso " + (indice[0] + 1) + "/" + ruta.size());
                    indice[0]++;
                } else {
                    animacionTimer.stop();
                    lblEstado.setText("✅ Entregas completadas");
                    btnIniciarRuta.setEnabled(true);
                    btnDetener.setEnabled(false);
                    
                    // Si es modo automático, cerrar después de un momento
                    if (idCamionEspecifico != -1) {
                        Timer cerrarTimer = new Timer(2000, evt -> {
                            JOptionPane.showMessageDialog(MapaRutasTiempoReal.this, 
                                "Todas las entregas han sido completadas.\n" +
                                "El camión regresa a la central.", 
                                "Ruta Finalizada", 
                                JOptionPane.INFORMATION_MESSAGE);
                            dispose();
                        });
                        cerrarTimer.setRepeats(false);
                        cerrarTimer.start();
                    } else {
                        JOptionPane.showMessageDialog(MapaRutasTiempoReal.this, 
                            "Todas las entregas han sido completadas", 
                            "Ruta Finalizada", 
                            JOptionPane.INFORMATION_MESSAGE);
                    }
                }
            }
        });
        
        animacionTimer.start();
    }
    
    private void detenerAnimacion() {
        if (animacionTimer != null && animacionTimer.isRunning()) {
            animacionTimer.stop();
            lblEstado.setText("⏸ Ruta detenida");
            btnIniciarRuta.setEnabled(true);
            btnDetener.setEnabled(false);
        }
    }
    
    // Clase interna para almacenar datos de clientes
    private static class ClienteDestino {
        int fila;
        int columna;
        String nombre;
    }
    
    // Clase interna para el panel de visualización del mapa
    private class MapaVisualizacion extends JPanel {
        private int filas = 20;
        private int columnas = 20;
        private int tamanioCelda = 30;
        private Cuadricula[][] mapa;
        private Cuadricula posicionCamion;
        private List<Cuadricula> ruta;
        private List<ClienteMarcado> clientes;
        
        private static final Color COLOR_CALLE = new Color(200, 200, 200);
        private static final Color COLOR_CASA = Color.BLACK;
        private static final Color COLOR_CAMION = new Color(0, 150, 255);
        private static final Color COLOR_CLIENTE = new Color(255, 0, 0);
        private static final Color COLOR_RUTA = new Color(144, 238, 144);
        private static final Color COLOR_CENTRAL = new Color(255, 165, 0);
        
        public MapaVisualizacion() {
            this.mapa = new Cuadricula[filas][columnas];
            this.clientes = new ArrayList<>();
            this.ruta = new ArrayList<>();
            
            // Inicializar mapa
            int idActual = 0;
            for (int i = 0; i < filas; i++) {
                for (int j = 0; j < columnas; j++) {
                    mapa[i][j] = new Cuadricula(idActual++, i, j);
                }
            }
            
            // Crear casas (obstáculos)
            crearCroquis();
            
            setPreferredSize(new Dimension(columnas * tamanioCelda, filas * tamanioCelda));
            setBackground(new Color(34, 139, 34));
        }
        
        private void crearCroquis() {
            // Manzanas
            crearManzana(2, 2, 3, 4);
            crearManzana(2, 8, 3, 4);
            crearManzana(2, 14, 3, 4);
            crearManzana(7, 2, 4, 4);
            crearManzana(7, 8, 4, 5);
            crearManzana(7, 15, 4, 3);
            crearManzana(13, 2, 4, 3);
            crearManzana(13, 7, 4, 4);
            crearManzana(13, 13, 4, 5);
            
            // Casas individuales
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
        
        public void setPosicionCamion(int fila, int columna) {
            this.posicionCamion = obtenerCuadricula(fila, columna);
            repaint();
        }
        
        public void agregarCliente(int fila, int columna, String nombre) {
            ClienteMarcado cliente = new ClienteMarcado();
            cliente.cuadricula = obtenerCuadricula(fila, columna);
            cliente.nombre = nombre;
            clientes.add(cliente);
            repaint();
        }
        
        public void establecerRuta(List<Cuadricula> ruta) {
            this.ruta = ruta;
            repaint();
        }
        
        public void limpiar() {
            clientes.clear();
            ruta.clear();
            posicionCamion = null;
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
                    
                    // Color base
                    if (cuadricula.esObstaculo()) {
                        g.setColor(COLOR_CASA);
                    } else if (ruta.contains(cuadricula)) {
                        g.setColor(COLOR_RUTA);
                    } else {
                        g.setColor(COLOR_CALLE);
                    }
                    
                    g.fillRect(x, y, tamanioCelda, tamanioCelda);
                    
                    // Dibujar central
                    if (i == CENTRAL_FILA && j == CENTRAL_COLUMNA) {
                        g.setColor(COLOR_CENTRAL);
                        g.fillRect(x + 2, y + 2, tamanioCelda - 4, tamanioCelda - 4);
                        g.setColor(Color.BLACK);
                        g.drawString("C", x + 10, y + 20);
                    }
                }
            }
            
            // Dibujar clientes
            for (ClienteMarcado cliente : clientes) {
                if (cliente.cuadricula != null) {
                    int x = cliente.cuadricula.getColumna() * tamanioCelda;
                    int y = cliente.cuadricula.getFila() * tamanioCelda;
                    
                    g.setColor(COLOR_CLIENTE);
                    g.fillOval(x + 5, y + 5, tamanioCelda - 10, tamanioCelda - 10);
                    g.setColor(Color.WHITE);
                    g.drawString("📦", x + 8, y + 22);
                }
            }
            
            // Dibujar camión
            if (posicionCamion != null) {
                int x = posicionCamion.getColumna() * tamanioCelda;
                int y = posicionCamion.getFila() * tamanioCelda;
                
                g.setColor(COLOR_CAMION);
                g.fillRoundRect(x + 3, y + 3, tamanioCelda - 6, tamanioCelda - 6, 5, 5);
                g.setColor(Color.WHITE);
                g.drawString("🚚", x + 7, y + 21);
            }
            
            // Dibujar grid
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
        }
    }
}