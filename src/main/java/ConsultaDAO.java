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
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class ConsultaDAO {

    // idInternamiento puede ser null (consulta externa, sin internamiento asociado)
    public static void guardar(String idPaciente, int idMedico, Integer idInternamiento,
            String diagnostico, String observaciones) throws SQLException {

        String sql = "INSERT INTO consultas "
                + "(id_paciente, id_medico, id_internamiento, diagnostico, observaciones, fecha_consulta) "
                + "VALUES (?,?,?,?,?,?)";

        try (Connection con = Conexion.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, idPaciente);
            ps.setInt(2, idMedico);
            if (idInternamiento == null) {
                ps.setNull(3, java.sql.Types.INTEGER);
            } else {
                ps.setInt(3, idInternamiento);
            }
            ps.setString(4, diagnostico);
            ps.setString(5, observaciones);
            ps.setTimestamp(6, Timestamp.valueOf(java.time.LocalDateTime.now()));
            ps.executeUpdate();
        }
    }

    public static void actualizar(int idConsulta, String diagnostico, String observaciones) throws SQLException {
        // Solo se permite corregir diagnóstico/observaciones; paciente, médico y fecha no se editan.
        String sql = "UPDATE consultas SET diagnostico=?, observaciones=? WHERE id_consulta=?";

        try (Connection con = Conexion.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, diagnostico);
            ps.setString(2, observaciones);
            ps.setInt(3, idConsulta);
            ps.executeUpdate();
        }
    }

    public static void eliminar(int idConsulta) throws SQLException {
        String sql = "DELETE FROM consultas WHERE id_consulta=?";
        try (Connection con = Conexion.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idConsulta);
            ps.executeUpdate();
        }
    }

    public static List<Object[]> listarTodos() throws SQLException {
        String sql = "SELECT c.id_consulta, "
                + "p.nombre || ' ' || p.apellido_paterno AS paciente, "
                + "m.nombre || ' ' || m.apellido_paterno AS medico, "
                + "c.fecha_consulta, c.diagnostico "
                + "FROM consultas c "
                + "JOIN pacientes p ON p.id_paciente = c.id_paciente "
                + "JOIN medicos m ON m.id_medico = c.id_medico "
                + "ORDER BY c.id_consulta DESC";

        List<Object[]> lista = new ArrayList<>();
        try (Connection con = Conexion.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(new Object[]{
                    rs.getInt("id_consulta"),
                    rs.getString("paciente"),
                    rs.getString("medico"),
                    rs.getTimestamp("fecha_consulta"),
                    rs.getString("diagnostico")
                });
            }
        }
        return lista;
    }

    // Internamientos activos de un paciente (para el combo "Internamiento asociado")
    public static List<Object[]> listarInternamientosActivos(String idPaciente) throws SQLException {
        String sql = "SELECT id_internamiento, fecha_entrada FROM internamientos "
                + "WHERE id_paciente = ? AND estado = 'activo' ORDER BY id_internamiento DESC";

        List<Object[]> lista = new ArrayList<>();
        try (Connection con = Conexion.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, idPaciente);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(new Object[]{
                        rs.getInt("id_internamiento"),
                        rs.getDate("fecha_entrada")
                    });
                }
            }
        }
        return lista;
    }
}