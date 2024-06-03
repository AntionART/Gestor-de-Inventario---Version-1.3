package MODELO;

import Conexion.ConexionMysql;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ListarProductos {

    // Método para mostrar la tabla completa de productos
    public void MostrarTable(JTable tabla) {
        // Se crea una instancia de la clase ConexionMysql
        ConexionMysql con = new ConexionMysql();
        // Se llama al método conectar() para obtener la conexión
        Connection cn = con.conectar();
        
        // Se crea un nuevo modelo de tabla
        DefaultTableModel modelo = new DefaultTableModel();
        // Se agregan las columnas al modelo
        modelo.addColumn("ID");
        modelo.addColumn("Nombre");
        modelo.addColumn("Cantidad");
        modelo.addColumn("Precio");
        modelo.addColumn("Total");
        
        // Se define la consulta SQL para obtener los productos
        String consultasql = "SELECT * FROM producto"; // Selecciona todas las columnas de la tabla
        
        try {
            // Se crea un Statement para ejecutar la consulta
            Statement st = cn.createStatement();
            // Se ejecuta la consulta y se obtiene el resultado
            ResultSet rs = st.executeQuery(consultasql);
            
            // Se itera sobre el resultado y se agregan las filas al modelo
            while (rs.next()) {
                // Se obtienen los valores de las columnas en la fila actual
                int id = rs.getInt(1);
                String nombre = rs.getString(2);
                int cantidad = rs.getInt(3);
                double precio = rs.getDouble(4);
                double total = rs.getDouble(5);
                
                // Se agrega una nueva fila al modelo con los valores obtenidos
                modelo.addRow(new Object[]{id, nombre, cantidad, precio, total});
            }
            
            // Se cierra el ResultSet y el Statement
            rs.close();
            st.close();
            
            // Se asigna el modelo a la tabla
            tabla.setModel(modelo);
        } catch (Exception e) {
            // Se maneja cualquier excepción que pueda ocurrir durante la consulta
            System.out.println("ERROR AL LISTAR LOS DATOS: " + e);
        }
    }
    
    // Método para mostrar la tabla de productos agrupados por nombre
    public void MostrarTableAgrupada(JTable tabla) {
        ConexionMysql con = new ConexionMysql();
        Connection cn = con.conectar();

        DefaultTableModel modelo = new DefaultTableModel();
        modelo.addColumn("Nombre");
        modelo.addColumn("Cantidad Total");
        modelo.addColumn("Precio Promedio");
        
        String query = "SELECT nombre, SUM(cantidad) AS cantidad_total, AVG(precio) AS precio_promedio FROM producto GROUP BY nombre";
        
        try {
            PreparedStatement stmt = cn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                String nombre = rs.getString("nombre");
                int cantidadTotal = rs.getInt("cantidad_total");
                double precioPromedio = rs.getDouble("precio_promedio");
                
                modelo.addRow(new Object[]{nombre, cantidadTotal, precioPromedio});
            }
            
            rs.close();
            stmt.close();
            
            tabla.setModel(modelo);
        } catch (SQLException e) {
            System.out.println("ERROR AL AGRUPAR LOS DATOS: " + e);
        }
    }

    // Método para obtener la lista de productos
    public List<Producto> obtenerProductos() {
        List<Producto> productos = new ArrayList<>();
        ConexionMysql con = new ConexionMysql();
        Connection cn = con.conectar();

        String sql = "SELECT nombre, cantidad, precio FROM producto";
        
        try (PreparedStatement stmt = cn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String nombre = rs.getString("nombre");
                int cantidad = rs.getInt("cantidad");
                double precio = rs.getDouble("precio");
                Producto producto = new Producto(nombre, cantidad, precio);
                productos.add(producto);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return productos;
    }
}