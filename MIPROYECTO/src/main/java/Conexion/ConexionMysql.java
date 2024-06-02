package Conexion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionMysql {
    Connection cn;

    public Connection conectar() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            cn = DriverManager.getConnection("jdbc:mysql://localhost/miproyecto", "root", "");
            System.out.println("Conexion Exitosa");
        } catch (ClassNotFoundException e) {
            System.out.println("Error al cargar el controlador JDBC: " + e);
        } catch (SQLException e) {
            System.out.println("Error de conexión a la base de datos: " + e);
        }
        return cn;
    }
}
