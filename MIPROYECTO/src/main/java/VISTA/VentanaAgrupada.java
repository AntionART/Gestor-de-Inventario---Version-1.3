package VISTA;

import javax.swing.*;
import java.awt.*;

public class VentanaAgrupada extends JFrame {

    private JTable tablaAgrupada;

    public VentanaAgrupada(Object[][] data, String[] columnNames) {
        super("Listado Agrupado");

        // Configuración básica de la ventana
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Creación de la tabla con los datos agrupados
        tablaAgrupada = new JTable(data, columnNames);
        JScrollPane scrollPane = new JScrollPane(tablaAgrupada);

        // Agregar la tabla al contenido de la ventana
        getContentPane().add(scrollPane, BorderLayout.CENTER);
    }
}