package ProyectoFinal;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.function.Consumer;

public class MapaCroquis extends JPanel {
    private int filas = 20;
    private int columnas = 20;
    private int tamanioCelda = 30;
    private Cuadricula[][] mapa;
    private Cuadricula cuadriculaSeleccionada;
    private Cuadricula casaDestino;
    private Consumer<Cuadricula> onCasaGuardada; // Callback
    
    private static final Color COLOR_CALLE = new Color(200, 200, 200);
    private static final Color COLOR_CASA = Color.BLACK;
    private static final Color COLOR_SELECCIONADA = Color.YELLOW;
    private static final Color COLOR_DESTINO = new Color(255, 0, 0);
    
    public MapaCroquis() {
        this.mapa = new Cuadricula[filas][columnas];
        this.cuadriculaSeleccionada = null;
        this.casaDestino = null;
        
        // Inicializar cada cuadrícula
        int idActual = 0;
        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                mapa[i][j] = new Cuadricula(idActual++, i, j);
            }
        }
        
        // Crear casas (obstáculos) simulando un vecindario
        crearCroquis();
        
        setPreferredSize(new Dimension(columnas * tamanioCelda, filas * tamanioCelda));
        setBackground(new Color(34, 139, 34)); // áreas verdes
        
        // Interacción con el mouse
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int fila = e.getY() / tamanioCelda;
                int col = e.getX() / tamanioCelda;
                
                if (fila < filas && col < columnas) {
                    Cuadricula cuadricula = mapa[fila][col];
                    
                    if (SwingUtilities.isLeftMouseButton(e)) {
                        cuadriculaSeleccionada = cuadricula;
                        System.out.println("Seleccionada: " + cuadricula);
                        repaint();
                    }
                }
            }
        });
    }
    
    private void crearCroquis() {
        // Crear manzanas de casas (bloques con espacio para calles)
        
        // Manzana 1 (superior izquierda)
        crearManzana(2, 2, 3, 4);
        
        // Manzana 2 (superior centro)
        crearManzana(2, 8, 3, 4);
        
        // Manzana 3 (superior derecha)
        crearManzana(2, 14, 3, 4);
        
        // Manzana 4 (centro izquierda)
        crearManzana(7, 2, 4, 4);
        
        // Manzana 5 (centro centro) - más grande
        crearManzana(7, 8, 4, 5);
        
        // Manzana 6 (centro derecha)
        crearManzana(7, 15, 4, 3);
        
        // Manzana 7 (inferior izquierda)
        crearManzana(13, 2, 4, 3);
        
        // Manzana 8 (inferior centro)
        crearManzana(13, 7, 4, 4);
        
        // Manzana 9 (inferior derecha)
        crearManzana(13, 13, 4, 5);
        
        // Casas individuales dispersas
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
                mapa[i][j].setTipo(1); // Obstáculo (casa)
            }
        }
    }
    
    private void crearCasa(int fila, int col) {
        if (fila < filas && col < columnas) {
            mapa[fila][col].setTipo(1);
        }
    }
    
    public void establecerCasaDestino(Cuadricula cuadricula) {
        if (cuadricula != null && cuadricula.esObstaculo()) {
            this.casaDestino = cuadricula;
            repaint();
        }
    }
    
    public Cuadricula getCasaDestino() {
        return casaDestino;
    }
    
    public Cuadricula getCuadriculaSeleccionada() {
        return cuadriculaSeleccionada;
    }
    
    public void setOnCasaGuardada(Consumer<Cuadricula> callback) {
        this.onCasaGuardada = callback;
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Dibujar cada cuadrícula
        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                Cuadricula cuadricula = mapa[i][j];
                int x = j * tamanioCelda;
                int y = i * tamanioCelda;
                
                // Determinar color base
                if (cuadricula.esObstaculo() && cuadricula != casaDestino) {
                    // Dibujar casa (cuadro negro)
                    g.setColor(COLOR_CASA);
                    g.fillRect(x, y, tamanioCelda, tamanioCelda);
                    
                    // Si está seleccionada, dibujar borde
                    if (cuadricula == cuadriculaSeleccionada) {
                        g.setColor(COLOR_SELECCIONADA);
                        g2d.setStroke(new BasicStroke(3));
                        g.drawRect(x + 1, y + 1, tamanioCelda - 2, tamanioCelda - 2);
                        g2d.setStroke(new BasicStroke(1));
                    }
                } else if (cuadricula == casaDestino) {
                    // Dibujar casa destino (cuadro rojo)
                    g.setColor(COLOR_DESTINO);
                    g.fillRect(x, y, tamanioCelda, tamanioCelda);
                } else {
                    // Dibujar calle (cuadro gris)
                    g.setColor(COLOR_CALLE);
                    g.fillRect(x, y, tamanioCelda, tamanioCelda);
                }
            }
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
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Croquis del Vecindario 20x20");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            
            MapaCroquis mapa = new MapaCroquis();
            
            // Label para mostrar el número de casa seleccionado
            JLabel lblNumeroCasa = new JLabel("<html><b>Casa seleccionada:</b> Ninguna</html>");
            lblNumeroCasa.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
            lblNumeroCasa.setFont(new Font("Arial", Font.PLAIN, 14));
            
            // Panel de botones
            JPanel panelBotones = new JPanel(new FlowLayout());
            panelBotones.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            
            JButton btnGuardar = new JButton("💾 Guardar Casa Seleccionada");
            btnGuardar.setBackground(new Color(34, 139, 34));
            btnGuardar.setForeground(Color.WHITE);
            btnGuardar.setFont(new Font("Arial", Font.BOLD, 14));
            btnGuardar.addActionListener(e -> {
                Cuadricula sel = mapa.getCuadriculaSeleccionada();
                if (sel != null && sel.esObstaculo()) {
                    int numeroCasa = sel.getId();
                    mapa.establecerCasaDestino(sel);
                    lblNumeroCasa.setText("<html><b>Casa seleccionada:</b> #" + numeroCasa + 
                                         " [Fila: " + sel.getFila() + ", Columna: " + sel.getColumna() + "]</html>");
                    System.out.println("Casa guardada - Número: " + numeroCasa);
                    System.out.println("Detalles: " + sel);
                    
                    // Llamar al callback si existe
                    if (mapa.onCasaGuardada != null) {
                        mapa.onCasaGuardada.accept(sel);
                        frame.dispose(); // Cerrar ventana
                    } else {
                        JOptionPane.showMessageDialog(frame, 
                            "Casa guardada exitosamente! \n\n" +
                            "Número de casa: " + numeroCasa + "\n" +
                            "Posición: [Fila " + sel.getFila() + ", Columna " + sel.getColumna() + "]",
                            "Casa Guardada", 
                            JOptionPane.INFORMATION_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(frame, 
                        "Por favor, selecciona una CASA (cuadro negro)\n" +
                        "Haz clic izquierdo sobre una casa primero.",
                        "Aviso", 
                        JOptionPane.WARNING_MESSAGE);
                }
            });
            
            panelBotones.add(btnGuardar);
            
            JLabel lblInstrucciones = new JLabel(
                "<html><center><b>Croquis del Vecindario 20x20</b><br>" +
                "⬛ Casas (negro) | 🟥 Casa destino guardada (rojo) | ⬜ Calles (gris)<br>" +
                "Clic izquierdo en una casa para seleccionarla, luego presiona 'Guardar'</center></html>"
            );
            lblInstrucciones.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
            
            JPanel panelNorte = new JPanel(new BorderLayout());
            panelNorte.add(lblInstrucciones, BorderLayout.NORTH);
            panelNorte.add(lblNumeroCasa, BorderLayout.SOUTH);
            
            frame.setLayout(new BorderLayout());
            frame.add(panelNorte, BorderLayout.NORTH);
            frame.add(mapa, BorderLayout.CENTER);
            frame.add(panelBotones, BorderLayout.SOUTH);
            
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
    
    // Método estático para abrir el mapa y obtener resultado
    public static void abrirYSeleccionar(Consumer<Cuadricula> callback) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Croquis del Vecindario 20x20 - Seleccionar Casa");
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            
            MapaCroquis mapa = new MapaCroquis();
            mapa.setOnCasaGuardada(callback);
            
            JLabel lblNumeroCasa = new JLabel("<html><b>Casa seleccionada:</b> Ninguna</html>");
            lblNumeroCasa.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
            lblNumeroCasa.setFont(new Font("Arial", Font.PLAIN, 14));
            
            JPanel panelBotones = new JPanel(new FlowLayout());
            panelBotones.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            
            JButton btnGuardar = new JButton("💾 Guardar Casa y Cerrar");
            btnGuardar.setBackground(new Color(34, 139, 34));
            btnGuardar.setForeground(Color.WHITE);
            btnGuardar.setFont(new Font("Arial", Font.BOLD, 14));
            btnGuardar.addActionListener(e -> {
                Cuadricula sel = mapa.getCuadriculaSeleccionada();
                if (sel != null && sel.esObstaculo()) {
                    mapa.establecerCasaDestino(sel);
                    callback.accept(sel);
                    frame.dispose();
                } else {
                    JOptionPane.showMessageDialog(frame, 
                        "Por favor, selecciona una CASA (cuadro negro)",
                        "Aviso", 
                        JOptionPane.WARNING_MESSAGE);
                }
            });
            
            panelBotones.add(btnGuardar);
            
            JLabel lblInstrucciones = new JLabel(
                "<html><center><b>Selecciona una Casa</b><br>" +
                "⬛ Casas (negro) | ⬜ Calles (gris)<br>" +
                "Haz clic en una casa y presiona 'Guardar'</center></html>"
            );
            lblInstrucciones.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
            
            JPanel panelNorte = new JPanel(new BorderLayout());
            panelNorte.add(lblInstrucciones, BorderLayout.NORTH);
            panelNorte.add(lblNumeroCasa, BorderLayout.SOUTH);
            
            frame.setLayout(new BorderLayout());
            frame.add(panelNorte, BorderLayout.NORTH);
            frame.add(mapa, BorderLayout.CENTER);
            frame.add(panelBotones, BorderLayout.SOUTH);
            
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}