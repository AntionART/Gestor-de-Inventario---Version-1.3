package CONTROLADOR;

import MODELO.ListarProductos;
import MODELO.Producto;
import MODELO.Registro;
import VISTA.frm_RegistrarProducto;
import VISTA.frm_ProductosAgrupados;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType0Font;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

public class Controlador_Frm_Registrar_Producto implements ActionListener, ListSelectionListener {

    private frm_RegistrarProducto frm_rp;
    private String projectPath;

    public Controlador_Frm_Registrar_Producto(frm_RegistrarProducto frm_rp) {
        this.frm_rp = frm_rp;
        this.frm_rp.btnguardar.addActionListener(this);
        this.frm_rp.TablaProductos.getSelectionModel().addListSelectionListener(this);
        this.frm_rp.btn_actualizar.addActionListener(this);
        this.frm_rp.btn_cancelar.addActionListener(this);
        this.frm_rp.btn_eliminar.addActionListener(this);
        this.frm_rp.btn_agrupados.addActionListener(this);
        this.frm_rp.btn_excel.addActionListener(this);
        this.frm_rp.btn_pdf.addActionListener(this);
        this.frm_rp.btn_mostrarGrafico.addActionListener(this); // Añadir listener para el botón de gráfico
        this.projectPath = System.getProperty("user.dir");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == frm_rp.btnguardar) {
            try {
                String nombre = frm_rp.txtnombre.getText();
                int cantidad = Integer.parseInt(frm_rp.txtcantidad.getText());
                double precio = Double.parseDouble(frm_rp.txtprecio.getText());
                Producto P = new Producto(nombre, cantidad, precio);
                Registro R = new Registro();
                R.registrarbd(P);
                ListarProductos lp = new ListarProductos();
                lp.MostrarTable(frm_rp.TablaProductos);
                limpiarentradas();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frm_rp, "Por favor, ingrese valores válidos.");
            }
        } else if (e.getSource() == frm_rp.btn_actualizar) {
            try {
                int id = Integer.parseInt(frm_rp.txtid.getText());
                String nombre = frm_rp.txtnombre.getText();
                int cantidad = Integer.parseInt(frm_rp.txtcantidad.getText());
                double precio = Double.parseDouble(frm_rp.txtprecio.getText());
                Producto producto = new Producto(nombre, cantidad, precio);
                Registro r = new Registro();
                r.actualizarbd(producto, id);
                limpiarentradas();
                ListarProductos LP = new ListarProductos();
                LP.MostrarTable(frm_rp.TablaProductos);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frm_rp, "Por favor, ingrese valores válidos.");
            }
        } else if (e.getSource() == frm_rp.btn_cancelar) {
            frm_rp.btn_actualizar.setEnabled(false);
            frm_rp.btn_cancelar.setEnabled(false);
            frm_rp.btnguardar.setEnabled(true);
            limpiarentradas();
        } else if (e.getSource() == frm_rp.btn_eliminar) {
            int filaObtenida = frm_rp.TablaProductos.getSelectedRow();
            if (filaObtenida >= 0) {
                TableModel modelo = frm_rp.TablaProductos.getModel();
                Object id = modelo.getValueAt(filaObtenida, 0);

                int opcion = JOptionPane.showConfirmDialog(null, "¿Desea eliminar este producto?", "Eliminar producto", JOptionPane.YES_OPTION);
                if (opcion == JOptionPane.YES_OPTION) {
                    Registro R = new Registro();
                    R.eliminarbd((int) id);

                    ListarProductos lp = new ListarProductos();
                    lp.MostrarTable(frm_rp.TablaProductos);

                    limpiarentradas();

                    JOptionPane.showMessageDialog(null, "REGISTRO ELIMINADO CORRECTAMENTE");
                }
            } else {
                JOptionPane.showMessageDialog(null, "Seleccione un producto para eliminar.");
            }
        } else if (e.getSource() == frm_rp.btn_agrupados) {
            frm_ProductosAgrupados productosAgrupados = new frm_ProductosAgrupados(null, true);
            ListarProductos listarProductos = new ListarProductos();
            listarProductos.MostrarTableAgrupada(productosAgrupados.TablaProductos);
            productosAgrupados.setVisible(true);
        } else if (e.getSource() == frm_rp.btn_excel) {
            exportToExcel(frm_rp.TablaProductos, "productos.xlsx");
        } else if (e.getSource() == frm_rp.btn_pdf) {
            exportToPDF(frm_rp.TablaProductos, "productos.pdf");
        } else if (e.getSource() == frm_rp.btn_mostrarGrafico) {
            mostrarGrafico();
        }
    }

    private void mostrarGrafico() {
        List<Producto> productos = obtenerListaDeProductos();
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        for (Producto producto : productos) {
            dataset.addValue(producto.getCantidad(), "Cantidad", producto.getNombre());
            dataset.addValue(producto.getPrecio(), "Precio", producto.getNombre());
            dataset.addValue(producto.getTotal(), "Total", producto.getNombre());
        }

        JFreeChart barChart = ChartFactory.createBarChart(
                "Productos Inventario",
                "Producto",
                "Valor",
                dataset,
                PlotOrientation.VERTICAL,
                true, true, false);

        JFrame graficoFrame = new JFrame("Gráfico de Productos");
        graficoFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        graficoFrame.add(new ChartPanel(barChart));
        graficoFrame.pack();
        graficoFrame.setLocationRelativeTo(null);
        graficoFrame.setVisible(true);
    }

    private List<Producto> obtenerListaDeProductos() {
        ListarProductos listarProductos = new ListarProductos();
        return listarProductos.obtenerProductos(); // Implementa este método en ListarProductos
    }

    private void limpiarentradas() {
        frm_rp.txtid.setText("");
        frm_rp.txtnombre.setText("");
        frm_rp.txtcantidad.setText("");
        frm_rp.txtprecio.setText("");
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {
        if (!e.getValueIsAdjusting()) {
            if (e.getSource() == frm_rp.TablaProductos.getSelectionModel()) {
                int filaObtenida = frm_rp.TablaProductos.getSelectedRow();

                if (filaObtenida >= 0) {
                    TableModel modelo = frm_rp.TablaProductos.getModel();
                    Object id = modelo.getValueAt(filaObtenida, 0);
                    Object nombre = modelo.getValueAt(filaObtenida, 1);
                    Object cantidad = modelo.getValueAt(filaObtenida, 2);
                    Object precio = modelo.getValueAt(filaObtenida, 3);

                    frm_rp.txtid.setText(id.toString());
                    frm_rp.txtnombre.setText(nombre.toString());
                    frm_rp.txtcantidad.setText(cantidad.toString());
                    frm_rp.txtprecio.setText(precio.toString());

                    frm_rp.btnguardar.setEnabled(false);
                    frm_rp.btn_actualizar.setEnabled(true);
                    frm_rp.btn_cancelar.setEnabled(true);
                }
            }
        }
    }

    public void exportToExcel(JTable table, String productosxlsx) {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Productos");

        TableModel model = table.getModel();

        Row headerRow = sheet.createRow(0);
        for (int i = 0; i < model.getColumnCount(); i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(model.getColumnName(i));
        }

        for (int i = 0; i < model.getRowCount(); i++) {
            Row row = sheet.createRow(i + 1);
            for (int j = 0; j < model.getColumnCount(); j++) {
                Cell cell = row.createCell(j);
                cell.setCellValue(model.getValueAt(i, j).toString());
            }
        }

        String excelFolderPath = projectPath + File.separator + "Excel";
        File excelFolder = new File(excelFolderPath);
        if (!excelFolder.exists()) {
            excelFolder.mkdir();
        }

        String excelFilePath = excelFolderPath + File.separator + productosxlsx;

        try (FileOutputStream fileOut = new FileOutputStream(excelFilePath)) {
            workbook.write(fileOut);
            workbook.close();
            JOptionPane.showMessageDialog(null, "Datos exportados a Excel correctamente.");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error al exportar datos a Excel: " + e.getMessage());
        }
    }

    public void exportToPDF(JTable table, String productospdf) {
        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage();
            document.addPage(page);
            try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                TableModel model = table.getModel();

                float margin = 50;
                float yStart = page.getMediaBox().getHeight() - margin;
                float tableWidth = page.getMediaBox().getWidth() - (2 * margin);
                float yPosition = yStart;
                float rowHeight = 20;

                // Cargar la fuente Helvetica
                String fontFile = "fonts" + File.separator + "Helvetica.ttf";
                PDType0Font.load(document, new File(fontFile));

                // Establecer la fuente Helvetica en tamaño 12
                contentStream.setFont(PDType0Font.load(document, new File(fontFile)), 12);

                for (int i = 0; i < model.getColumnCount(); i++) {
                    float columnWidth = tableWidth / model.getColumnCount();
                    String text = model.getColumnName(i);
                    contentStream.beginText();
                    contentStream.newLineAtOffset(margin + (i * columnWidth), yPosition - 15);
                    contentStream.showText(text);
                    contentStream.endText();
                }

                yPosition -= rowHeight;

                // Cambiar la fuente a Helvetica para los datos de la tabla
                contentStream.setFont(PDType0Font.load(document, new File(fontFile)), 12);

                for (int i = 0; i < model.getRowCount(); i++) {
                    for (int j = 0; j < model.getColumnCount(); j++) {
                        String text = model.getValueAt(i, j).toString();
                        float columnWidth = tableWidth / model.getColumnCount();
                        contentStream.beginText();
                        contentStream.newLineAtOffset(margin + (j * columnWidth), yPosition);
                        contentStream.showText(text);
                        contentStream.endText();
                    }
                    yPosition -= rowHeight;
                }
            }

            String pdfFolderPath = projectPath + File.separator + "PDF";
            File pdfFolder = new File(pdfFolderPath);
            if (!pdfFolder.exists()) {
                pdfFolder.mkdirs(); // Esto crea la carpeta si no existe
            }

            String pdfFilePath = pdfFolderPath + File.separator + productospdf;
            document.save(new File(pdfFilePath));
            JOptionPane.showMessageDialog(null, "Datos exportados a PDF correctamente.");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error al exportar datos a PDF: " + e.getMessage());
        }
    }
}
