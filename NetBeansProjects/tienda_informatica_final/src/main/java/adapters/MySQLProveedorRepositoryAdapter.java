package adapters;

import ports.ProveedorRepositoryPort;
import model.Proveedor; // Usamos 'model.Proveedor'
import model.Direccion; // Usamos 'model.Direccion'
import java.sql.*;
import java.util.List;

/**
 * Adaptador de Infraestructura para Proveedores.
 * Implementa el Puerto ProveedorRepositoryPort usando JDBC para MySQL.
 */
public class MySQLProveedorRepositoryAdapter implements ProveedorRepositoryPort {

    // ⚠️ Reemplaza con tus datos de conexión si son diferentes
    private static final String JDBC_URL = "jdbc:mysql://25.50.109.111:3306/tienda_informatica_final?useSSL=false&serverTimezone=UTC";
    private static final String USER = "root2";
    private static final String PASSWORD = "";

    @Override
    public int guardarProveedor(Proveedor proveedor) {
        int idGenerado = 0;
        Direccion direccion = proveedor.getDireccion();
        Connection conn = null;

        try {
            conn = DriverManager.getConnection(JDBC_URL, USER, PASSWORD);
            conn.setAutoCommit(false); // 🚀 INICIAR TRANSACCIÓN

            // -------------------------------------------------------------------
            // PASO 1: INSERTAR DIRECCION Y OBTENER ID
            // -------------------------------------------------------------------
            String sqlInsertDireccion = "INSERT INTO direccion (pais, ciudad, calle, barrio) VALUES (?, ?, ?, ?)";
            
            try (PreparedStatement psDir = conn.prepareStatement(sqlInsertDireccion, Statement.RETURN_GENERATED_KEYS)) {
                psDir.setString(1, direccion.getPais());
                psDir.setString(2, direccion.getCiudad());
                psDir.setString(3, direccion.getCalle());
                psDir.setString(4, direccion.getBarrio());

                psDir.executeUpdate();
                
                try (ResultSet rsDir = psDir.getGeneratedKeys()) {
                    if (rsDir.next()) {
                        int idDireccionGenerado = rsDir.getInt(1);
                        direccion.setIdDireccion(idDireccionGenerado); // Actualizar el objeto Direccion
                    } else {
                        throw new SQLException("Fallo al obtener ID de Dirección.");
                    }
                }
            }
            
            // -------------------------------------------------------------------
            // PASO 2: INSERTAR PROVEEDOR USANDO EL ID DE DIRECCION
            // -------------------------------------------------------------------
            // Asumiendo que la FK se llama 'Direccion_idDireccion'
            String sqlInsertProveedor = "INSERT INTO proveedor (nif, nombre, Direccion_idDireccion) VALUES (?, ?, ?)";

            try (PreparedStatement psProv = conn.prepareStatement(sqlInsertProveedor, Statement.RETURN_GENERATED_KEYS)) {
                psProv.setString(1, proveedor.getNif());
                psProv.setString(2, proveedor.getNombre());
                psProv.setInt(3, direccion.getIdDireccion()); // Usar el ID generado

                if (psProv.executeUpdate() > 0) {
                    try (ResultSet rsProv = psProv.getGeneratedKeys()) {
                        if (rsProv.next()) {
                            idGenerado = rsProv.getInt(1);
                            proveedor.setIdProveedor(idGenerado);
                        }
                    }
                } else {
                    throw new SQLException("Fallo al insertar Proveedor.");
                }
            }

            conn.commit(); // ✅ CONFIRMAR LA TRANSACCIÓN
            System.out.println("LOG: Proveedor " + proveedor.getNombre() + " registrado con ID: " + idGenerado);

        } catch (SQLException e) {
            System.err.println("❌ ERROR FATAL AL GUARDAR PROVEEDOR (ROLLBACK): " + e.getMessage());
            if (conn != null) {
                try {
                    conn.rollback(); // ↩️ REVERTIR LA TRANSACCIÓN
                } catch (SQLException ex) {
                    System.err.println("Error en rollback: " + ex.getMessage());
                }
            }
            e.printStackTrace();
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return idGenerado;
    }

    @Override
    public Proveedor buscarProveedorPorId(int id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'buscarProveedorPorId'");
    }

    @Override
    public List<Proveedor> obtenerTodosLosProveedores() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'obtenerTodosLosProveedores'");
    }

    @Override
    public boolean actualizarProveedor(Proveedor proveedor) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'actualizarProveedor'");
    }

    @Override
    public boolean eliminarProveedor(int id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'eliminarProveedor'");
    }

    // ... (El resto de métodos pendientes del CRUD) ...
}