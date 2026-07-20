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
import java.util.ArrayList;
import java.util.List;

public class MedicoDAO {

    public static void guardar(String nombre, String apPaterno, String apMaterno,
            String cedula, String especialidad, String hospital,
            String telefono, String correo) throws SQLException {

        String sql = "INSERT INTO medicos (nombre, apellido_paterno, apellido_materno, "
                + "cedula, especialidad, hospital, telefono, correo) "
                + "VALUES (?,?,?,?,?,?,?,?)";

        try (Connection con = Conexion.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, nombre);
            ps.setString(2, apPaterno);
            ps.setString(3, apMaterno);
            ps.setString(4, cedula);
            ps.setString(5, especialidad);
            ps.setString(6, hospital);
            ps.setString(7, telefono);
            ps.setString(8, correo);
            ps.executeUpdate();
        }
    }

    public static void actualizar(int idMedico, String nombre, String apPaterno, String apMaterno,
            String cedula, String especialidad, String hospital,
            String telefono, String correo) throws SQLException {

        String sql = "UPDATE medicos SET nombre=?, apellido_paterno=?, apellido_materno=?, "
                + "cedula=?, especialidad=?, hospital=?, telefono=?, correo=? "
                + "WHERE id_medico=?";

        try (Connection con = Conexion.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, nombre);
            ps.setString(2, apPaterno);
            ps.setString(3, apMaterno);
            ps.setString(4, cedula);
            ps.setString(5, especialidad);
            ps.setString(6, hospital);
            ps.setString(7, telefono);
            ps.setString(8, correo);
            ps.setInt(9, idMedico);
            ps.executeUpdate();
        }
    }

    public static void eliminar(int idMedico) throws SQLException {
        String sql = "DELETE FROM medicos WHERE id_medico=?";
        try (Connection con = Conexion.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idMedico);
            ps.executeUpdate();
        }
    }

    public static List<Object[]> listarTodos() throws SQLException {
        String sql = "SELECT id_medico, nombre, apellido_paterno, apellido_materno, "
                + "cedula, especialidad, hospital, telefono, correo "
                + "FROM medicos ORDER BY id_medico";

        List<Object[]> lista = new ArrayList<>();
        try (Connection con = Conexion.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(new Object[]{
                    rs.getInt("id_medico"),
                    rs.getString("nombre"),
                    rs.getString("apellido_paterno"),
                    rs.getString("apellido_materno"),
                    rs.getString("cedula"),
                    rs.getString("especialidad"),
                    rs.getString("hospital"),
                    rs.getString("telefono"),
                    rs.getString("correo")
                });
            }
        }
        return lista;
    }
}