package ProyectoFinal;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.text.DecimalFormat;
import org.jfree.chart.*;
import org.jfree.chart.plot.*;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;

public class EstadisticasVentasMes extends JFrame {
    
    private CRutas conexion;
    private JComboBox<String> comboMes;
    private JComboBox<String> comboAnio;
    private JPanel panelGraficos;
    private JPanel panelEstadisticas;
    
    // Labels para estadísticas
    private JLabel lblTotalVentas;
    private JLabel lblTotalPedidos;
    private JLabel lblProductosVendidos;
    private JLabel lblTicketPromedio;
    
    private DecimalFormat formatoMoneda = new DecimalFormat("$#,##0.00");
    
    private String[] meses = {
        "Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio",
        "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"
    };
    
    public EstadisticasVentasMes() {
        conexion = new CRutas();
        
        setTitle("Estadísticas de Ventas por Mes");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
        setSize(1400, 900);
        getContentPane().setBackground(new Color(240, 240, 245));
        
        // Panel superior con controles - AHORA FIJO
        add(crearPanelSuperior(), BorderLayout.NORTH);
        
        // Panel central que contiene estadísticas y gráficos
        JPanel panelCentral = new JPanel(new BorderLayout(10, 10));
        panelCentral.setBackground(new Color(240, 240, 245));
        
        // Panel de estadísticas numéricas
        panelEstadisticas = crearPanelEstadisticas();
        panelCentral.add(panelEstadisticas, BorderLayout.NORTH);
        
        // Panel de gráficos con scroll
        panelGraficos = new JPanel(new GridLayout(2, 2, 15, 15));
        panelGraficos.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panelGraficos.setBackground(new Color(240, 240, 245));
        
        JScrollPane scrollGraficos = new JScrollPane(panelGraficos);
        scrollGraficos.setBorder(null);
        scrollGraficos.setPreferredSize(new Dimension(0, 600)); // Altura fija para gráficos
        
        panelCentral.add(scrollGraficos, BorderLayout.CENTER);
        
        add(panelCentral, BorderLayout.CENTER);
        
        // Cargar datos del mes actual
        cargarMesActual();
        actualizarEstadisticas();
        
        setLocationRelativeTo(null);
    }
    
    private JPanel crearPanelSuperior() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(0, 0, 102));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));
        panel.setPreferredSize(new Dimension(0, 80)); // Altura fija
        
        // Título
        JLabel lblTitulo = new JLabel("Estadísticas de Ventas Mensuales");
        lblTitulo.setFont(new Font("Century Gothic", Font.BOLD, 28));
        lblTitulo.setForeground(Color.WHITE);
        
        // Panel de filtros
        JPanel panelFiltros = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        panelFiltros.setOpaque(false);
        
        JLabel lblMes = new JLabel("Mes:");
        lblMes.setForeground(Color.WHITE);
        lblMes.setFont(new Font("Century Gothic", Font.BOLD, 14));
        
        comboMes = new JComboBox<>(meses);
        comboMes.setFont(new Font("Century Gothic", Font.PLAIN, 13));
        comboMes.setPreferredSize(new Dimension(130, 30));
        
        JLabel lblAnio = new JLabel("Año:");
        lblAnio.setForeground(Color.WHITE);
        lblAnio.setFont(new Font("Century Gothic", Font.BOLD, 14));
        
        // Generar años (actual y 5 años atrás)
        String[] anios = new String[6];
        int anioActual = java.util.Calendar.getInstance().get(java.util.Calendar.YEAR);
        for (int i = 0; i < 6; i++) {
            anios[i] = String.valueOf(anioActual - i);
        }
        
        comboAnio = new JComboBox<>(anios);
        comboAnio.setFont(new Font("Century Gothic", Font.PLAIN, 13));
        comboAnio.setPreferredSize(new Dimension(100, 30));
        
        JButton btnConsultar = new JButton("Consultar");
        btnConsultar.setBackground(new Color(40, 167, 69));
        btnConsultar.setForeground(Color.WHITE);
        btnConsultar.setFont(new Font("Century Gothic", Font.BOLD, 13));
        btnConsultar.setFocusPainted(false);
        btnConsultar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnConsultar.addActionListener(e -> actualizarEstadisticas());
        
        panelFiltros.add(lblMes);
        panelFiltros.add(comboMes);
        panelFiltros.add(Box.createHorizontalStrut(10));
        panelFiltros.add(lblAnio);
        panelFiltros.add(comboAnio);
        panelFiltros.add(Box.createHorizontalStrut(10));
        panelFiltros.add(btnConsultar);
        
        panel.add(lblTitulo, BorderLayout.WEST);
        panel.add(panelFiltros, BorderLayout.EAST);
        
        return panel;
    }
    
    private JPanel crearPanelEstadisticas() {
        JPanel panel = new JPanel(new GridLayout(1, 4, 20, 0));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.setBackground(new Color(240, 240, 245));
        panel.setPreferredSize(new Dimension(0, 130)); // Altura fija
        
        // Tarjeta 1: Total Ventas
        JPanel tarjeta1 = crearTarjetaEstadistica(
            "💰", "Total Ventas", "$0.00", 
            new Color(40, 167, 69), new Color(220, 255, 230)
        );
        lblTotalVentas = obtenerLabelValor(tarjeta1);
        
        // Tarjeta 2: Total Pedidos
        JPanel tarjeta2 = crearTarjetaEstadistica(
            "📦", "Total Pedidos", "0", 
            new Color(0, 123, 255), new Color(220, 240, 255)
        );
        lblTotalPedidos = obtenerLabelValor(tarjeta2);
        
        // Tarjeta 3: Productos Vendidos
        JPanel tarjeta3 = crearTarjetaEstadistica(
            "📊", "Productos Vendidos", "0", 
            new Color(111, 66, 193), new Color(240, 230, 255)
        );
        lblProductosVendidos = obtenerLabelValor(tarjeta3);
        
        // Tarjeta 4: Ticket Promedio
        JPanel tarjeta4 = crearTarjetaEstadistica(
            "💵", "Ticket Promedio", "$0.00", 
            new Color(255, 193, 7), new Color(255, 250, 220)
        );
        lblTicketPromedio = obtenerLabelValor(tarjeta4);
        
        panel.add(tarjeta1);
        panel.add(tarjeta2);
        panel.add(tarjeta3);
        panel.add(tarjeta4);
        
        return panel;
    }
    
    private JPanel crearTarjetaEstadistica(String icono, String titulo, String valor, Color colorIcono, Color colorFondo) {
        JPanel tarjeta = new JPanel(new BorderLayout(10, 10));
        tarjeta.setBackground(Color.WHITE);
        tarjeta.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(230, 230, 230), 1),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        
        // Panel izquierdo con icono
        JPanel panelIcono = new JPanel(new GridBagLayout());
        panelIcono.setBackground(colorFondo);
        panelIcono.setPreferredSize(new Dimension(70, 70));
        panelIcono.setBorder(BorderFactory.createLineBorder(colorIcono, 2));
        
        JLabel lblIcono = new JLabel(icono);
        lblIcono.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 32));
        panelIcono.add(lblIcono);
        
        // Panel derecho con datos
        JPanel panelDatos = new JPanel();
        panelDatos.setLayout(new BoxLayout(panelDatos, BoxLayout.Y_AXIS));
        panelDatos.setOpaque(false);
        
        JLabel lblTitulo = new JLabel(titulo);
        lblTitulo.setFont(new Font("Century Gothic", Font.PLAIN, 13));
        lblTitulo.setForeground(new Color(120, 120, 120));
        
        JLabel lblValor = new JLabel(valor);
        lblValor.setFont(new Font("Century Gothic", Font.BOLD, 28));
        lblValor.setForeground(colorIcono);
        
        panelDatos.add(lblTitulo);
        panelDatos.add(Box.createVerticalStrut(5));
        panelDatos.add(lblValor);
        
        tarjeta.add(panelIcono, BorderLayout.WEST);
        tarjeta.add(panelDatos, BorderLayout.CENTER);
        
        return tarjeta;
    }
    
    private JLabel obtenerLabelValor(JPanel tarjeta) {
        JPanel panelDatos = (JPanel) tarjeta.getComponent(1);
        return (JLabel) panelDatos.getComponent(2);
    }
    
    private void cargarMesActual() {
        java.util.Calendar cal = java.util.Calendar.getInstance();
        comboMes.setSelectedIndex(cal.get(java.util.Calendar.MONTH));
        comboAnio.setSelectedIndex(0);
    }
    
    private void actualizarEstadisticas() {
        int mes = comboMes.getSelectedIndex() + 1;
        int anio = Integer.parseInt((String) comboAnio.getSelectedItem());
        
        // Actualizar tarjetas numéricas
        actualizarTarjetas(mes, anio);
        
        // Actualizar gráficos
        panelGraficos.removeAll();
        panelGraficos.add(crearGraficoTopProductos(mes, anio));
        panelGraficos.add(crearGraficoComparativoMeses(anio));
        panelGraficos.add(crearGraficoCategorias(mes, anio));
        
        panelGraficos.revalidate();
        panelGraficos.repaint();
    }
    
    private void actualizarTarjetas(int mes, int anio) {
        try {
            Statement stmt = conexion.conexion.createStatement();
            
            // Consulta principal - calcular total desde detalle_pedido
            String sql = String.format(
                "SELECT COUNT(DISTINCT p.ID_pedido) as total_pedidos, " +
                "COALESCE(SUM(dp.cantidad * pr.precio), 0) as total_ventas, " +
                "COALESCE(SUM(dp.cantidad), 0) as total_productos " +
                "FROM pedidos p " +
                "LEFT JOIN detalles_pedido dp ON p.ID_pedido = dp.R_pedido " +
                "LEFT JOIN productos pr ON pr.ID_producto = dp.R_producto " +
                "WHERE MONTH(p.fecha_pedido) = %d AND YEAR(p.fecha_pedido) = %d",
                mes, anio
            );
            
            ResultSet rs = stmt.executeQuery(sql);
            
            if (rs.next()) {
                int totalPedidos = rs.getInt("total_pedidos");
                double totalVentas = rs.getDouble("total_ventas");
                int totalProductos = rs.getInt("total_productos");
                
                lblTotalPedidos.setText(String.valueOf(totalPedidos));
                lblTotalVentas.setText(formatoMoneda.format(totalVentas));
                lblProductosVendidos.setText(String.valueOf(totalProductos));
                
                if (totalPedidos > 0) {
                    lblTicketPromedio.setText(formatoMoneda.format(totalVentas / totalPedidos));
                } else {
                    lblTicketPromedio.setText("$0.00");
                }
            }
            
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            System.err.println("Error al actualizar tarjetas: " + e.getMessage());
            JOptionPane.showMessageDialog(this, 
                "Error al cargar datos: " + e.getMessage(), 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private ChartPanel crearGraficoTopProductos(int mes, int anio) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        
        try {
            Statement stmt = conexion.conexion.createStatement();
            String sql = String.format(
                "SELECT pr.descripcion_producto, SUM(dp.cantidad) as total " +
                "FROM detalles_pedido dp " +
                "JOIN productos pr ON pr.ID_producto = dp.R_producto " +
                "JOIN pedidos p ON p.ID_pedido = dp.R_pedido " +
                "WHERE MONTH(p.fecha_pedido) = %d AND YEAR(p.fecha_pedido) = %d " +
                "GROUP BY pr.ID_producto " +
                "ORDER BY total DESC LIMIT 10",
                mes, anio
            );
            
            ResultSet rs = stmt.executeQuery(sql);
            
            while (rs.next()) {
                String producto = rs.getString("descripcion_producto");
                int cantidad = rs.getInt("total");
                dataset.addValue(cantidad, "Cantidad", producto);
            }
            
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            System.err.println("Error: " + e.getMessage());
        }
        
        JFreeChart chart = ChartFactory.createBarChart(
            "Top 10 Productos Más Vendidos",
            "Producto",
            "Cantidad Vendida",
            dataset,
            PlotOrientation.HORIZONTAL,
            false,
            true,
            false
        );
        
        customizarGraficoBarra(chart, new Color(0, 123, 255));
        
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setBackground(Color.WHITE);
        return chartPanel;
    }
    
    private ChartPanel crearGraficoComparativoMeses(int anio) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        
        try {
            Statement stmt = conexion.conexion.createStatement();
            String sql = String.format(
                "SELECT MONTH(p.fecha_pedido) as mes, " +
                "COALESCE(SUM(dp.cantidad * pr.precio), 0) as total_ventas " +
                "FROM pedidos p " +
                "JOIN detalles_pedido dp ON p.ID_pedido = dp.R_pedido " +
                "JOIN productos pr ON pr.ID_producto = dp.R_producto " +
                "WHERE YEAR(p.fecha_pedido) = %d " +
                "GROUP BY MONTH(p.fecha_pedido) " +
                "ORDER BY mes",
                anio
            );
            
            ResultSet rs = stmt.executeQuery(sql);
            
            while (rs.next()) {
                int mes = rs.getInt("mes");
                double ventas = rs.getDouble("total_ventas");
                dataset.addValue(ventas, "Ventas", meses[mes - 1]);
            }
            
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            System.err.println("Error: " + e.getMessage());
        }
        
        JFreeChart chart = ChartFactory.createBarChart(
            "Comparativo de Ventas por Mes (" + anio + ")",
            "Mes",
            "Ventas ($)",
            dataset,
            PlotOrientation.VERTICAL,
            false,
            true,
            false
        );
        
        customizarGraficoBarra(chart, new Color(111, 66, 193));
        
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setBackground(Color.WHITE);
        return chartPanel;
    }
    
    private ChartPanel crearGraficoCategorias(int mes, int anio) {
        DefaultPieDataset dataset = new DefaultPieDataset();
        
        try {
            Statement stmt = conexion.conexion.createStatement();
            String sql = String.format(
                "SELECT c.nombre_categoria, SUM(dp.cantidad) as total " +
                "FROM detalles_pedido dp " +
                "JOIN productos pr ON pr.ID_producto = dp.R_producto " +
                "JOIN categorias c ON c.ID_categoria = pr.R_categoria " +
                "JOIN pedidos p ON p.ID_pedido = dp.R_pedido " +
                "WHERE MONTH(p.fecha_pedido) = %d AND YEAR(p.fecha_pedido) = %d " +
                "GROUP BY c.ID_categoria, c.nombre_categoria " +
                "ORDER BY total DESC",
                mes, anio
            );
            
            ResultSet rs = stmt.executeQuery(sql);
            
            while (rs.next()) {
                String categoria = rs.getString("nombre_categoria");
                int cantidad = rs.getInt("total");
                dataset.setValue(categoria + " (" + cantidad + ")", cantidad);
            }
            
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            System.err.println("Error: " + e.getMessage());
        }
        
        JFreeChart chart = ChartFactory.createPieChart(
            "Ventas por Categoría",
            dataset,
            true,
            true,
            false
        );
        
        chart.setBackgroundPaint(Color.WHITE);
        PiePlot plot = (PiePlot) chart.getPlot();
        plot.setBackgroundPaint(Color.WHITE);
        plot.setLabelFont(new Font("Century Gothic", Font.PLAIN, 11));
        plot.setOutlineVisible(false);
        
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setBackground(Color.WHITE);
        return chartPanel;
    }
    
    private void customizarGraficoBarra(JFreeChart chart, Color color) {
        chart.setBackgroundPaint(Color.WHITE);
        chart.getTitle().setFont(new Font("Century Gothic", Font.BOLD, 15));
        
        CategoryPlot plot = chart.getCategoryPlot();
        plot.setBackgroundPaint(Color.WHITE);
        plot.setRangeGridlinePaint(new Color(220, 220, 220));
        plot.setOutlineVisible(false);
        
        BarRenderer renderer = (BarRenderer) plot.getRenderer();
        renderer.setSeriesPaint(0, color);
        renderer.setDrawBarOutline(false);
        renderer.setShadowVisible(false);
        
        plot.getDomainAxis().setLabelFont(new Font("Century Gothic", Font.PLAIN, 12));
        plot.getRangeAxis().setLabelFont(new Font("Century Gothic", Font.PLAIN, 12));
        plot.getDomainAxis().setTickLabelFont(new Font("Century Gothic", Font.PLAIN, 10));
        plot.getRangeAxis().setTickLabelFont(new Font("Century Gothic", Font.PLAIN, 10));
    }
    
    private void customizarGraficoLinea(JFreeChart chart, Color color) {
        chart.setBackgroundPaint(Color.WHITE);
        chart.getTitle().setFont(new Font("Century Gothic", Font.BOLD, 15));
        
        CategoryPlot plot = chart.getCategoryPlot();
        plot.setBackgroundPaint(Color.WHITE);
        plot.setRangeGridlinePaint(new Color(220, 220, 220));
        plot.setOutlineVisible(false);
        
        LineAndShapeRenderer renderer = (LineAndShapeRenderer) plot.getRenderer();
        renderer.setSeriesPaint(0, color);
        renderer.setSeriesStroke(0, new BasicStroke(3.0f));
        renderer.setSeriesShapesVisible(0, true);
        
        plot.getDomainAxis().setLabelFont(new Font("Century Gothic", Font.PLAIN, 12));
        plot.getRangeAxis().setLabelFont(new Font("Century Gothic", Font.PLAIN, 12));
        plot.getDomainAxis().setTickLabelFont(new Font("Century Gothic", Font.PLAIN, 10));
        plot.getRangeAxis().setTickLabelFont(new Font("Century Gothic", Font.PLAIN, 10));
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            EstadisticasVentasMes estadisticas = new EstadisticasVentasMes();
            estadisticas.setVisible(true);
        });
    }
}