package ProyectoFinal;

import java.awt.Image;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.table.DefaultTableModel;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import java.io.FileOutputStream;
import javax.swing.JOptionPane;

public class PedidosVendedor extends javax.swing.JFrame {
    
    public int id;
    
    public void mostrar(String filtros){
        CClientes c = new CClientes();
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("No. Pedido");
        model.addColumn("Fecha del Pedido");
        model.addColumn("Fecha de Entrega");
        model.addColumn("Status");
        model.addColumn("Nombre del Cliente");
        visor.setModel(model);
        String datos[] = new String[5];
        try{
            try (Statement stmt = c.conexion.createStatement()) {
                String consulta = "select pedidos.ID_pedido as pedido, fecha_pedido, fecha_entrega, status_pedidos.status_pedido as status, " +
                        "concat(clientes.nombre_cliente, \" \", clientes.ap_pat_cliente, \" \", clientes.ap_mat_cliente) as cliente " +
                        "from pedidos join clientes on pedidos.R_cliente = clientes.ID_cliente join status_pedidos on " +
                        "status_pedidos.ID_status = pedidos.R_status " + filtros + " order by pedidos.ID_pedido";
                try (ResultSet rs = stmt.executeQuery(consulta)) {
                    while (rs.next()) {
                        datos[0] = rs.getString("pedido");
                        datos[1] = rs.getString("fecha_pedido");
                        datos[2] = rs.getString("fecha_entrega");
                        datos[3] = rs.getString("status");
                        datos[4] = rs.getString("cliente");
                        model.addRow(datos);
                    }
                }
            }
        }catch(SQLException e){
            System.out.println("No se pudo acceder a la tabla");
        }
    }
    
    private static PdfPCell crearCelda(String contenido, Font font) {
        PdfPCell celda = new PdfPCell(new Phrase(contenido, font));
        celda.setHorizontalAlignment(Element.ALIGN_CENTER);
        celda.setVerticalAlignment(Element.ALIGN_MIDDLE);
        celda.setBorder(Rectangle.NO_BORDER);
        return celda;
    }

    public PedidosVendedor() {
        initComponents();
        this.setLocationRelativeTo(this);
        
        SetImageLabel(jLabel1,"src\\imagenes\\logotip.png");
        SetImageLabel(jLabel2,"src\\imagenes\\mandala.png");
        mostrar("");
    }
    
    @SuppressWarnings("unchecked")
    
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        visor = new javax.swing.JTable();
        jLabel3 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox<>();
        jTextField1 = new javax.swing.JTextField();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(0, 0, 153));

        jPanel2.setBackground(new java.awt.Color(0, 0, 102));

        jLabel4.setFont(new java.awt.Font("Century Gothic", 1, 36)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("PEDIDOS");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 131, Short.MAX_VALUE)
                .addComponent(jLabel4)
                .addGap(130, 130, 130)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap(22, Short.MAX_VALUE))))
        );

        jButton1.setBackground(new java.awt.Color(204, 204, 255));
        jButton1.setFont(new java.awt.Font("Century Gothic", 0, 12)); // NOI18N
        jButton1.setText(" ← Regresar");
        jButton1.setActionCommand("← Regresar");
        jButton1.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton3.setBackground(new java.awt.Color(204, 204, 255));
        jButton3.setFont(new java.awt.Font("Century Gothic", 0, 12)); // NOI18N
        jButton3.setText("Imprimir Ticket");
        jButton3.setActionCommand("← Regresar");
        jButton3.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        visor.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(visor);

        jLabel3.setFont(new java.awt.Font("Century Gothic", 1, 14)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Status del pedido:");

        jLabel5.setFont(new java.awt.Font("Century Gothic", 1, 14)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("Nombre del Cliente: ");

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Seleccionar", "Sin Asignar", "Asignado", "Entregado" }));

        jButton4.setBackground(new java.awt.Color(204, 204, 255));
        jButton4.setFont(new java.awt.Font("Century Gothic", 0, 12)); // NOI18N
        jButton4.setText("Buscar");
        jButton4.setActionCommand("← Regresar");
        jButton4.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jButton5.setBackground(new java.awt.Color(204, 204, 255));
        jButton5.setFont(new java.awt.Font("Century Gothic", 0, 12)); // NOI18N
        jButton5.setText("Limpiar");
        jButton5.setActionCommand("← Regresar");
        jButton5.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        jButton6.setBackground(new java.awt.Color(204, 204, 255));
        jButton6.setFont(new java.awt.Font("Century Gothic", 0, 12)); // NOI18N
        jButton6.setText("Asignar Rutas");
        jButton6.setActionCommand("← Regresar");
        jButton6.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(50, 50, 50)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel3)
                                .addGap(85, 85, 85)
                                .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 154, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(61, 61, 61)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButton4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton5, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(44, 44, 44)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 549, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(29, 29, 29)
                                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(jLabel5)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton5))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton4))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 376, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton6)
                    .addComponent(jButton3))
                .addContainerGap(51, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        
        //MainVendedores newframe = new MainVendedores();
        //newframe.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        CPedidos p = new CPedidos();
        int fila = visor.getSelectedRow();
        if (fila == -1){
            JOptionPane.showMessageDialog(this, "Seleccione una fila");
            return;
        }else{
            this.id = Integer.parseInt((String)visor.getValueAt(fila, 0));
            System.out.println(this.id);
        }
        try {
            // Ruta del archivo PDF
            String ruta = "ticket_venta_5_5_12.pdf";

            // Crear documento
            Document document = new Document(PageSize.A7); // Tamaño tipo ticket
            PdfWriter.getInstance(document, new FileOutputStream(ruta));

            // Abrir el documento
            document.open();

            // Fuente personalizada
            Font tituloFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12);
            Font contenidoFont = FontFactory.getFont(FontFactory.HELVETICA, 8);

            // Encabezado
            Paragraph encabezado = new Paragraph("Epura\n\n", tituloFont);
            encabezado.setAlignment(Element.ALIGN_CENTER);
            document.add(encabezado);
            try{
                try (Statement stmt = p.conexion.createStatement()) {
                    String consulta = "select concat(clientes.nombre_cliente, \" \", clientes.ap_pat_cliente, \" \", clientes.ap_mat_cliente) as cliente, "
                            + "pedidos.fecha_pedido as fecha_pedido, pedidos.fecha_entrega as fecha_entrega "
                            + "from clientes join pedidos on pedidos.R_cliente = clientes.ID_cliente "
                            + "where pedidos.ID_pedido = " + this.id;
                    ResultSet rs = stmt.executeQuery(consulta);
                    if (rs.next()) {
                        // Información del ticket
                        document.add(new Paragraph("Nombre del cliente: " + rs.getString("cliente"), contenidoFont));
                        document.add(new Paragraph("Fecha Pedido: " + rs.getString("fecha_pedido"), contenidoFont));
                        document.add(new Paragraph("Fecha Entrega: " + rs.getString("fecha_entrega") + "\n\n", contenidoFont));
                    }
                    String consulta1 = "select productos.descripcion_producto as producto, productos.precio as precio_unitario,"
                            + " detalles_pedido.cantidad as cantidad, sum(productos.precio*detalles_pedido.cantidad) as total "
                            + "from detalles_pedido join productos on detalles_pedido.R_producto = productos.ID_producto "
                            + "where detalles_pedido.R_pedido = " + this.id + " group by producto, precio_unitario, cantidad";
                    rs = stmt.executeQuery(consulta1);
                    float sumaTotal = 0;
                    // Detalles de la compra
                    PdfPTable tabla = new PdfPTable(4); // 4 columnas
                    tabla.setWidthPercentage(100);
                    tabla.setWidths(new float[]{2, 5, 2, 3}); // Ancho de columnas

                    // Agregar encabezados
                    tabla.addCell(crearCelda("Cant.", contenidoFont));
                    tabla.addCell(crearCelda("Descripción", contenidoFont));
                    tabla.addCell(crearCelda("P.Unit.", contenidoFont));
                    tabla.addCell(crearCelda("Total", contenidoFont));
                    float t;
                    while (rs.next()) {
                        t = rs.getFloat("precio_unitario")*rs.getInt("cantidad");
                        tabla.addCell(crearCelda(rs.getInt("cantidad")+"", contenidoFont));
                        tabla.addCell(crearCelda(rs.getString("producto"), contenidoFont));
                        tabla.addCell(crearCelda(rs.getFloat("precio_unitario")+"", contenidoFont));
                        tabla.addCell(crearCelda(t+"", contenidoFont));
                        sumaTotal  += t;
                    }
                    document.add(tabla);
                    // Total
                    Paragraph total = new Paragraph("\nTotal: "+sumaTotal, tituloFont);
                    total.setAlignment(Element.ALIGN_RIGHT);
                    document.add(total);
                    
                    // Cerrar documento
                    document.close();
                    rs.close();
                    JOptionPane.showMessageDialog(this, "Ticket generado con exito");
                }
            }catch(SQLException e){
                System.out.println("No se pudo acceder a la tabla");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        String nombre = jTextField1.getText();
        String filtro = " where clientes.nombre_cliente like '%" + nombre + "%'";
        int status = jComboBox1.getSelectedIndex();
        if (status != 0){
            filtro += " and pedidos.R_status = " + status;
        }
        mostrar(filtro);
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        jTextField1.setText("");
        jComboBox1.setSelectedIndex(0);
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        CRutas r = new CRutas();
        r.asignarRutas();
        JOptionPane.showMessageDialog(this, "Rutas asignadas con exito");
    }//GEN-LAST:event_jButton6ActionPerformed

    public static void main(String args[]) {

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new PedidosVendedor().setVisible(true);
            }
        });
    }
    
    private void SetImageLabel(JLabel labelName, String root){
        ImageIcon image = new ImageIcon(root);
        Icon icon = new ImageIcon(image.getImage().getScaledInstance(labelName.getWidth(),labelName.getHeight(),Image.SCALE_DEFAULT));
        labelName.setIcon(icon);
        this.repaint();
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTable visor;
    // End of variables declaration//GEN-END:variables
}
