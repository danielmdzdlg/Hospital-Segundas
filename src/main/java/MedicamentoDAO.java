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

public class MedicamentoDAO {

    public static List<Object[]> listarTodos() throws SQLException {
        String sql = "SELECT id_medicamento, nombre, presentacion, concentracion "
                + "FROM medicamentos ORDER BY nombre";

        List<Object[]> lista = new ArrayList<>();
        try (Connection con = Conexion.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(new Object[]{
                    rs.getInt("id_medicamento"),
                    rs.getString("nombre"),
                    rs.getString("presentacion"),
                    rs.getString("concentracion")
                });
            }
        }
        return lista;
    }
}