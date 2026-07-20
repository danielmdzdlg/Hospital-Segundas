/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author danim
 */
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class RecetaDAO {

    // filaMedicamentos: cada elemento es {idMedicamento (Integer), dosis, frecuencia, duracion}
    public static void guardarRecetaCompleta(int idConsulta, String indicacionesGenerales,
            List<Object[]> filaMedicamentos) throws SQLException {

        if (filaMedicamentos.isEmpty()) {
            throw new SQLException("Agrega al menos un medicamento antes de guardar.");
        }

        try (Connection con = Conexion.obtenerConexion()) {
            con.setAutoCommit(false);
            try {
                int idReceta;
                String sqlReceta = "INSERT INTO recetas (id_consulta, fecha_emision, indicaciones_generales) "
                        + "VALUES (?,?,?)";
                try (PreparedStatement ps = con.prepareStatement(sqlReceta, Statement.RETURN_GENERATED_KEYS)) {
                    ps.setInt(1, idConsulta);
                    ps.setTimestamp(2, Timestamp.valueOf(java.time.LocalDateTime.now()));
                    ps.setString(3, indicacionesGenerales);
                    ps.executeUpdate();

                    try (ResultSet rs = ps.getGeneratedKeys()) {
                        if (!rs.next()) {
                            throw new SQLException("No se pudo obtener el ID de la receta generada.");
                        }
                        idReceta = rs.getInt(1);
                    }
                }

                String sqlDetalle = "INSERT INTO receta_detalle "
                        + "(id_receta, id_medicamento, dosis, frecuencia, duracion) VALUES (?,?,?,?,?)";
                try (PreparedStatement ps = con.prepareStatement(sqlDetalle)) {
                    for (Object[] fila : filaMedicamentos) {
                        ps.setInt(1, idReceta);
                        ps.setInt(2, (int) fila[0]);
                        ps.setString(3, (String) fila[1]);
                        ps.setString(4, (String) fila[2]);
                        ps.setString(5, (String) fila[3]);
                        ps.addBatch();
                    }
                    ps.executeBatch();
                }

                con.commit();
            } catch (SQLException e) {
                con.rollback();
                throw e;
            } finally {
                con.setAutoCommit(true);
            }
        }
    }

    public static List<Object[]> listarConsultasSinReceta() throws SQLException {
        // Consultas que todavía no tienen receta emitida
        String sql = "SELECT c.id_consulta, "
                + "p.nombre || ' ' || p.apellido_paterno AS paciente, c.fecha_consulta "
                + "FROM consultas c "
                + "JOIN pacientes p ON p.id_paciente = c.id_paciente "
                + "WHERE c.id_consulta NOT IN (SELECT id_consulta FROM recetas) "
                + "ORDER BY c.id_consulta DESC";

        List<Object[]> lista = new ArrayList<>();
        try (Connection con = Conexion.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(new Object[]{
                    rs.getInt("id_consulta"),
                    rs.getString("paciente"),
                    rs.getTimestamp("fecha_consulta")
                });
            }
        }
        return lista;
    }
}