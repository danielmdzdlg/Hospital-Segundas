/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author danim
 */

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class Conexion {

    private static final String HOST = "localhost";
    private static final String PUERTO = "5432";
    private static final String BASE_DATOS = "Topicos";
    private static final String ESQUEMA = "hospital";
    private static final String USUARIO = "postgres";
    private static final String PASSWORD = "1234";

    private static final String URL =
            "jdbc:postgresql://" + HOST + ":" + PUERTO + "/" + BASE_DATOS;

    public static Connection obtenerConexion() throws SQLException {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new SQLException("No se encontró el driver de PostgreSQL. "
                    + "Revisa que la dependencia esté en el pom.xml.", e);
        }

        Properties props = new Properties();
        props.setProperty("user", USUARIO);
        props.setProperty("password", PASSWORD);
        // Fuerza el search_path al esquema Hospital en cada conexión,
        // así no dependemos de que la sesión de PostgreSQL lo recuerde.
        props.setProperty("currentSchema", ESQUEMA);

        return DriverManager.getConnection(URL, props);
    }
}