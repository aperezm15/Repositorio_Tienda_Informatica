package adapters;


import model.newpackage.*;
import ports.ProductoRepositoryPort;
import java.sql.*;
import java.util.List;

public class MySQLProductoRepositoryAdapter implements ProductoRepositoryPort {


    private static final String JDBC_URL = "jdbc:mysql://25.50.109.111:3306/tienda_informatica_final?useSSL=false&serverTimezone=UTC";
    private static final String USER = "root2";
    private static final String PASSWORD = ""; 

    @Override
    public int guardarProducto(Producto producto) {
        int idDetalleAltaTecnologia = 0;
        int idProductoGenerado = 0;
        
        try (Connection conn = DriverManager.getConnection(JDBC_URL, USER, PASSWORD)) {
            conn.setAutoCommit(false); 

            // PASO 1: INSERTAR FABRICANTE Y DETALLE (Si es Producto de Alta Tecnología)

            DetalleAltaTecnologia detalle = producto.getDetalleAltaTecnologia();
            
            if (detalle != null) {
                // Insertar Empresa Fabricante
                EmpresaFabricante fabricante = detalle.getFabricante();
                int idFabricanteGenerado = 0;
                
                String sqlFabricante = "INSERT INTO empresa_fabricante (nombre, numero_empleados) VALUES (?, ?)";
                try (PreparedStatement psFab = conn.prepareStatement(sqlFabricante, Statement.RETURN_GENERATED_KEYS)) {
                    psFab.setString(1, fabricante.getNombre());
                    psFab.setInt(2, fabricante.getNumeroEmpleados());
                    psFab.executeUpdate();
                    
                    try (ResultSet rs = psFab.getGeneratedKeys()) {
                        if (rs.next()) {
                            idFabricanteGenerado = rs.getInt(1);
                        }
                    }
                }
                
                // Insertar Detalle de Alta Tecnología, usando el ID del fabricante
                String sqlDetalle = "INSERT INTO detalle_alta_tecnologia (pais_origen, fecha_fabricacion, Empresa_fabricante_idEmpresa_fabricante) VALUES (?, ?, ?)";
                try (PreparedStatement psDetalle = conn.prepareStatement(sqlDetalle, Statement.RETURN_GENERATED_KEYS)) {
                    psDetalle.setString(1, detalle.getPaisOrigen());
                    psDetalle.setDate(2, Date.valueOf(detalle.getFechaFabricacion())); 
                    psDetalle.setInt(3, idFabricanteGenerado); 
                    psDetalle.executeUpdate();
                    
                    try (ResultSet rs = psDetalle.getGeneratedKeys()) {
                        if (rs.next()) {
                            idDetalleAltaTecnologia = rs.getInt(1); // Guardamos el ID para el Producto
                        }
                    }
                }
            }


            // -------------------------------------------------------------------
            // PASO 2: INSERTAR el Producto
            // -------------------------------------------------------------------
            String sqlProducto = "INSERT INTO producto (nombre_producto, modelo, descripcion, Detalle_alta_tecnologia_idDetalle_alta_tecnologia, categoria_producto_idcategoria) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement psProducto = conn.prepareStatement(sqlProducto, Statement.RETURN_GENERATED_KEYS)) {

                psProducto.setString(1, producto.getNombreProducto());
                psProducto.setString(2, producto.getModelo());
                psProducto.setString(3, producto.getDescripcion());
                
                // Si es Alta Tecnología (idDetalleAltaTecnologia > 0), se usa el ID generado.
                if (idDetalleAltaTecnologia > 0) {
                     psProducto.setInt(4, idDetalleAltaTecnologia); 
                } else {
                     psProducto.setNull(4, java.sql.Types.INTEGER);
                }
                
                psProducto.setInt(5, producto.getCategoriaProductoId()); 

                if (psProducto.executeUpdate() > 0) {
                    try (ResultSet rs = psProducto.getGeneratedKeys()) {
                        if (rs.next()) {
                            idProductoGenerado = rs.getInt(1);
                        }
                    }
                }
            }

            conn.commit();
            return idProductoGenerado;

        } catch (SQLException e) {
             try {

                Connection conn = DriverManager.getConnection(JDBC_URL, USER, PASSWORD);
                if (conn != null) conn.rollback();
            } catch (SQLException ex) {

            }
           
            e.printStackTrace();
            return 0;
        }
    }


    @Override
    public Producto buscarProductoPorId(int id) {
        return null; 
    }
    // ...

    @Override
    public List<Producto> obtenerTodosLosProductos() {

            System.err.println("❌ ERROR FATAL AL OBTENER TODOS LOS PRODUCTOS: Método no implementado");
        throw new UnsupportedOperationException("Unimplemented method 'obtenerTodosLosProductos'");
    }

    @Override
    public boolean actualizarProducto(Producto producto) {

        throw new UnsupportedOperationException("Unimplemented method 'actualizarProducto'");
    }

    @Override
    public boolean eliminarProducto(int id) {

        System.err.println("❌ ERROR FATAL AL ELIMINAR PRODUCTO: Método no implementado");
        throw new UnsupportedOperationException("Unimplemented method 'eliminarProducto'");
    }
}