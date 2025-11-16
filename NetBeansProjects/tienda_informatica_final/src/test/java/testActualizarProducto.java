
import adapters.*;
import model.*;

import ports.ProductoRepositoryPort;
import java.time.LocalDate;

public class testActualizarProducto {

    public static void main(String[] args) {
        
        ProductoRepositoryPort repository = new MySQLProductoRepositoryAdapter();

        // ----------------------------------------------------
        // FASE 1: LECTURA DEL PRODUCTO ORIGINAL (Para obtener todos los IDs)
        // ----------------------------------------------------
        final int ID_A_MODIFICAR = 2; // ⚠️ Usar un ID de un producto que sepas que es Alta Tecnología y ya existe
        System.out.println("--- 1. CONSULTANDO PRODUCTO ORIGINAL ---");
        Producto productoOriginal = repository.buscarProductoPorId(ID_A_MODIFICAR);
        
        if (productoOriginal == null) {
            System.err.println("ERROR: No se encontró el producto con ID " + ID_A_MODIFICAR + ". La prueba no puede continuar.");
            return;
        }
        
        System.out.println("Original: " + productoOriginal.getNombreProducto() + ", Fabricante: " + productoOriginal.getDetalleAltaTecnologia().getFabricante().getNombre());
        
        // ----------------------------------------------------
        // FASE 2: PREPARAR DATOS DE ACTUALIZACIÓN
        // ----------------------------------------------------
        System.out.println("\n--- 2. PREPARANDO ACTUALIZACIÓN ---");
        
        // 1. Obtener los objetos existentes para reutilizar los IDs
        DetalleAltaTecnologia detalleExistente = productoOriginal.getDetalleAltaTecnologia();
        EmpresaFabricante fabricanteExistente = detalleExistente.getFabricante();

        // 2. Modificar los campos del Fabricante
        fabricanteExistente.setNombre("AMD Corporation (MODIFICADO)");
        fabricanteExistente.setNumeroEmpleados(30000); // Aumento de empleados
        
        // 3. Modificar los campos del Detalle
        detalleExistente.setPaisOrigen("Estados Unidos (MODIFICADO)");
        detalleExistente.setFechaFabricacion(LocalDate.of(2025, 12, 31));
        
        // 4. Modificar los campos del Producto
        productoOriginal.setNombreProducto("Procesador AMD Ryzen 9 7950X (MODIFICADO)");
        productoOriginal.setModelo("Ryzen 9 7950X");
        productoOriginal.setDescripcion("Procesador de alto rendimiento AMD Ryzen 9 de 13ª generación con overclocking mejorado.");
        // Mantener el mismo ID de Producto y Categoría
        
        // ----------------------------------------------------
        // FASE 3: EJECUTAR ACTUALIZACIÓN
        // ----------------------------------------------------
        System.out.println("\n--- 3. EJECUTANDO ACTUALIZACIÓN ---");
        boolean actualizado = repository.actualizarProducto(productoOriginal);

        if (actualizado) {
            System.out.println("✅ ACTUALIZACIÓN EXITOSA. Verificando... ");

            // ----------------------------------------------------
            // FASE 4: VERIFICACIÓN (R)
            // ----------------------------------------------------
            Producto productoActualizado = repository.buscarProductoPorId(ID_A_MODIFICAR);
            
            System.out.println("\n--- 4. DATOS RECUPERADOS ---");
            System.out.println("Nombre Actualizado: " + productoActualizado.getNombreProducto());
            System.out.println("Nuevo País de Origen: " + productoActualizado.getDetalleAltaTecnologia().getPaisOrigen());
            System.out.println("Nuevo Fabricante: " + productoActualizado.getDetalleAltaTecnologia().getFabricante().getNombre());
            
        } else {
            System.err.println("❌ ACTUALIZACIÓN FALLIDA. Revise el error SQL detallado.");
        }
    }
}