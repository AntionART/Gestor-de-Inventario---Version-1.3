package MODELO;

public class Producto {
 
   private String nombre;
   private int cantidad;
   private Double precio;
   private Double total;
//Datos recibidos de Controlador_Frm_Registrar_Producto.java
    public Producto(String nombre, int cantidad, Double precio) {
        this.nombre = nombre;
        this.cantidad = cantidad;
        this.precio = precio;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public Double getPrecio() {
        return precio;
    }

    public void setPrecio(Double precio) {
        this.precio = precio;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }
    public Double Total(){
            Double total=cantidad*precio;
            return total;
    }
   
}
