 

import model.DetalleAltaTecnologia;
import model.EmpresaFabricante;
import model.Producto;
import model.*;
import ports.ProductoRepositoryPort;

import java.time.LocalDate;

import adapters.MySQLProductoRepositoryAdapter;

public class testProducto {

    public static void main(String[] args) {

        EmpresaFabricante fabricante = new EmpresaFabricante(
            0,
            "Asus Technologies",
            50000 
        );


        DetalleAltaTecnologia detalle = new DetalleAltaTecnologia(
            0,
            "Estados Unidos",
            LocalDate.of(2025, 10, 20),
            0
        );

        detalle.setFabricante(fabricante); 

        Producto monitor = new Producto(
            0,
            "Monitor Gamer ASUS ROG Swift 27\"",
            "ROG Swift 27\"",
            "Monitor gaming de alta resolución y tasa de refresco.",

            null, 
            3 // ID de Categoria 
        );

        monitor.setDetalleAltaTecnologia(detalle);


        MySQLProductoRepositoryAdapter adapter = new MySQLProductoRepositoryAdapter();


        int idProductoGenerado = adapter.guardarProducto(monitor);

        if (idProductoGenerado > 0) {
            System.out.println("✅ Monitor (Producto de Alta Tecnología) guardado exitosamente con ID: " + idProductoGenerado);
        } else {
            System.out.println("❌ Error al guardar el producto. Revisa la consola para la excepción SQL detallada.");
        }
    }
}