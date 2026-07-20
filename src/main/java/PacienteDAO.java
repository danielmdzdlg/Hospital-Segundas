/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author danim
 */
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PacienteDAO {

    public static boolean existe(String idPaciente) throws SQLException {
        String sql = "SELECT 1 FROM pacientes WHERE id_paciente = ?";
        try (Connection con = Conexion.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, idPaciente);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    public static void guardar(String idPaciente, String nombre, String apPaterno, String apMaterno,
            String genero, Date fechaNacimiento, double peso, double altura,
            String tipoSangre, String telefono, String correo) throws SQLException {

        String sql = "INSERT INTO pacientes (id_paciente, nombre, apellido_paterno, apellido_materno, "
                + "genero, fecha_nacimiento, peso, altura, tipo_sangre, telefono, correo) "
                + "VALUES (?,?,?,?,?,?,?,?,?,?,?)";

        try (Connection con = Conexion.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, idPaciente);
            ps.setString(2, nombre);
            ps.setString(3, apPaterno);
            ps.setString(4, apMaterno);
            ps.setString(5, genero);
            ps.setDate(6, fechaNacimiento);
            ps.setDouble(7, peso);
            ps.setDouble(8, altura);
            ps.setString(9, tipoSangre);
            ps.setString(10, telefono);
            ps.setString(11, correo);
            ps.executeUpdate();
        }
    }

    public static void actualizar(String idPaciente, String nombre, String apPaterno, String apMaterno,
            String genero, Date fechaNacimiento, double peso, double altura,
            String tipoSangre, String telefono, String correo) throws SQLException {

        String sql = "UPDATE pacientes SET nombre=?, apellido_paterno=?, apellido_materno=?, "
                + "genero=?, fecha_nacimiento=?, peso=?, altura=?, tipo_sangre=?, telefono=?, correo=? "
                + "WHERE id_paciente=?";

        try (Connection con = Conexion.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, nombre);
            ps.setString(2, apPaterno);
            ps.setString(3, apMaterno);
            ps.setString(4, genero);
            ps.setDate(5, fechaNacimiento);
            ps.setDouble(6, peso);
            ps.setDouble(7, altura);
            ps.setString(8, tipoSangre);
            ps.setString(9, telefono);
            ps.setString(10, correo);
            ps.setString(11, idPaciente);
            ps.executeUpdate();
        }
    }

    public static void eliminar(String idPaciente) throws SQLException {
        String sql = "DELETE FROM pacientes WHERE id_paciente=?";
        try (Connection con = Conexion.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, idPaciente);
            ps.executeUpdate();
        }
    }

    public static List<Object[]> listarTodos() throws SQLException {
        String sql = "SELECT id_paciente, nombre, apellido_paterno, apellido_materno, genero, "
                + "fecha_nacimiento, peso, altura, tipo_sangre, telefono, correo "
                + "FROM pacientes ORDER BY id_paciente";

        List<Object[]> lista = new ArrayList<>();
        try (Connection con = Conexion.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(new Object[]{
                    rs.getString("id_paciente"),
                    rs.getString("nombre"),
                    rs.getString("apellido_paterno"),
                    rs.getString("apellido_materno"),
                    rs.getString("genero"),
                    rs.getDate("fecha_nacimiento"),
                    rs.getDouble("peso"),
                    rs.getDouble("altura"),
                    rs.getString("tipo_sangre"),
                    rs.getString("telefono"),
                    rs.getString("correo")
                });
            }
        }
        return lista;
    }

    public static List<Object[]> buscar(String texto) throws SQLException {
        String sql = "SELECT id_paciente, nombre, apellido_paterno, apellido_materno, genero, "
                + "fecha_nacimiento, peso, altura, tipo_sangre, telefono, correo "
                + "FROM pacientes "
                + "WHERE id_paciente ILIKE ? OR nombre ILIKE ? OR apellido_paterno ILIKE ? "
                + "ORDER BY id_paciente";

        List<Object[]> lista = new ArrayList<>();
        try (Connection con = Conexion.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            String patron = "%" + texto + "%";
            ps.setString(1, patron);
            ps.setString(2, patron);
            ps.setString(3, patron);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(new Object[]{
                        rs.getString("id_paciente"),
                        rs.getString("nombre"),
                        rs.getString("apellido_paterno"),
                        rs.getString("apellido_materno"),
                        rs.getString("genero"),
                        rs.getDate("fecha_nacimiento"),
                        rs.getDouble("peso"),
                        rs.getDouble("altura"),
                        rs.getString("tipo_sangre"),
                        rs.getString("telefono"),
                        rs.getString("correo")
                    });
                }
            }
        }
        return lista;
    }
}