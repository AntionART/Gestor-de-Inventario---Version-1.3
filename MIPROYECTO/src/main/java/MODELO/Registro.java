package MODELO;

import Conexion.ConexionMysql;
import java.sql.Connection;
import java.sql.PreparedStatement;
import javax.swing.JOptionPane;

public class Registro {
    
    // Declaración de variables
    ConexionMysql con = new ConexionMysql(); // Instancia de la clase ConexionMysql
    Connection cn = con.conectar(); // Conexión a la base de datos
    
    // Método para registrar un Producto en la base de datos
    public void registrarbd(Producto p) {
        try {
            // Preparar la consulta SQL para insertar un nuevo producto en la tabla 'producto'
            PreparedStatement ps = cn.prepareStatement("INSERT INTO producto (nombre, cantidad, precio, total) VALUES (?,?,?,?)");
            
            // Establecer los valores de los parámetros en la consulta SQL
            ps.setString(1, p.getNombre()); // Nombre del producto
            ps.setInt(2, p.getCantidad()); // Cantidad del producto
            ps.setDouble(3, p.getPrecio()); // Precio del producto
            ps.setDouble(4, p.Total()); // Total del producto
            
            // Ejecutar la consulta SQL para insertar el producto en la base de datos
            ps.executeUpdate();
        } catch (Exception e) {
            // Manejar cualquier excepción que pueda ocurrir durante la inserción del producto
            JOptionPane.showMessageDialog(null, "ERROR AL REGISTRAR DATOS" + e);
        }
    }
    public void actualizarbd (Producto p,int id) {
        try {
            String consulta="UPDATE producto SET nombre=?, cantidad=?, precio=?,total=? WHERE id="+id+"";
            PreparedStatement ps=cn.prepareStatement (consulta);
            ps.setString(1, p.getNombre());
            ps.setInt(2, p.getCantidad());
            ps.setDouble(3, p.getPrecio());
            ps.setDouble(4, p.Total());
            ps.executeUpdate();
            JOptionPane.showMessageDialog(null, "ACTUALIZACIÓN EXITOSA");

        }catch (Exception e) {
            JOptionPane.showMessageDialog(null, "ERROR AL ACTUALIZAR EL REGISTRO"+ e);
        }
    }
    
    public void eliminarbd (int id) {
        try {
            String consulta="DELETE FROM producto WHERE id="+id+"";
            PreparedStatement ps=cn.prepareStatement (consulta);
            ps.execute();
            System.out.println("El producto se elimino correctamente");
        }catch (Exception e) {
            JOptionPane.showMessageDialog (null, "Error al eliminar un producto"+e);
        }
    } 
}
