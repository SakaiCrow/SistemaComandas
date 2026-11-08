package br.com.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexao {

    private static final String DEFAULT_URL =
        "jdbc:mysql://localhost:3306/sistema_comandas?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=America/Sao_Paulo&useUnicode=true&characterEncoding=UTF-8";
    private static final String DEFAULT_USER = "root";
    private static final String DEFAULT_PASS = "senha";

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("Driver JDBC do MySQL não encontrado: " + e.getMessage());
        }
    }

    public static Connection conectar() throws SQLException {
        String envUrl = System.getenv("DB_URL");
        String url = (envUrl != null && !envUrl.isBlank()) ? envUrl : System.getProperty("db.url", DEFAULT_URL);

        String envUser = System.getenv("DB_USER");
        String user = (envUser != null && !envUser.isBlank()) ? envUser : System.getProperty("db.user", DEFAULT_USER);

        String envPass = System.getenv("DB_PASSWORD");
        String pass = (envPass != null) ? envPass : System.getProperty("db.password", DEFAULT_PASS);

        return DriverManager.getConnection(url, user, pass);
    }

    public static boolean testarConexao() {
        try (Connection conn = conectar()) {
            return conn != null && !conn.isClosed();
        } catch (SQLException e) {
            return false;
        }
    }
}
