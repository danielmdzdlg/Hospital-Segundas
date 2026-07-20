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
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class InternamientoDAO {

    // Registra la entrada. Fecha y hora se toman del sistema, NO se reciben como parámetro.
    public static void registrarEntrada(String idPaciente, int idMedicoResponsable,
            String tipoPaciente, String motivoEntrada, String observacionesEntrada) throws SQLException {

        LocalDateTime ahora = LocalDateTime.now();

        String sql = "INSERT INTO internamientos "
                + "(id_paciente, id_medico_responsable, tipo_paciente, fecha_entrada, hora_entrada, "
                + "motivo_entrada, observaciones_entrada, estado) "
                + "VALUES (?,?,?,?,?,?,?,'activo')";

        try (Connection con = Conexion.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, idPaciente);
            ps.setInt(2, idMedicoResponsable);
            ps.setString(3, tipoPaciente);
            ps.setDate(4, java.sql.Date.valueOf(ahora.toLocalDate()));
            ps.setTime(5, java.sql.Time.valueOf(ahora.toLocalTime().withNano(0)));
            ps.setString(6, motivoEntrada);
            ps.setString(7, observacionesEntrada);
            ps.executeUpdate();
        }
    }

    // Registra la salida de un internamiento ya existente (por su ID). Hora automática también.
    public static void registrarSalida(int idInternamiento, String motivoSalida,
            String observacionesSalida) throws SQLException {

        LocalDateTime ahora = LocalDateTime.now();

        String sql = "UPDATE internamientos SET fecha_salida=?, hora_salida=?, "
                + "motivo_salida=?, observaciones_salida=?, estado='dado_de_alta' "
                + "WHERE id_internamiento=?";

        try (Connection con = Conexion.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setDate(1, java.sql.Date.valueOf(ahora.toLocalDate()));
            ps.setTime(2, java.sql.Time.valueOf(ahora.toLocalTime().withNano(0)));
            ps.setString(3, motivoSalida);
            ps.setString(4, observacionesSalida);
            ps.setInt(5, idInternamiento);
            ps.executeUpdate();
        }
    }

    // Solo permite corregir motivo/observaciones de entrada mientras sigue activo.
    // Fecha/hora nunca se editan a mano.
    public static void actualizarDatosEntrada(int idInternamiento, String motivoEntrada,
            String observacionesEntrada) throws SQLException {

        String sql = "UPDATE internamientos SET motivo_entrada=?, observaciones_entrada=? "
                + "WHERE id_internamiento=? AND estado='activo'";

        try (Connection con = Conexion.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, motivoEntrada);
            ps.setString(2, observacionesEntrada);
            ps.setInt(3, idInternamiento);
            ps.executeUpdate();
        }
    }

    public static List<Object[]> listarTodos() throws SQLException {
        String sql = "SELECT i.id_internamiento, "
                + "p.nombre || ' ' || p.apellido_paterno AS paciente, "
                + "i.fecha_entrada, i.hora_entrada, i.fecha_salida, i.hora_salida, i.estado "
                + "FROM internamientos i "
                + "JOIN pacientes p ON p.id_paciente = i.id_paciente "
                + "ORDER BY i.id_internamiento DESC";

        List<Object[]> lista = new ArrayList<>();
        try (Connection con = Conexion.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(new Object[]{
                    rs.getInt("id_internamiento"),
                    rs.getString("paciente"),
                    rs.getDate("fecha_entrada"),
                    rs.getTime("hora_entrada"),
                    rs.getDate("fecha_salida"),
                    rs.getTime("hora_salida"),
                    rs.getString("estado")
                });
            }
        }
        return lista;
    }
}