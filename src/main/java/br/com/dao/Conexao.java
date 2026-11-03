package br.com.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexao {

    private static final String URL =
        "jdbc:mysql://localhost:3306/sistema_comandas";

    private static final String USUARIO = "root";
    private static final String SENHA = "senha";

    public static Connection conectar() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new SQLException(
                "Driver JDBC do MySQL não encontrado. Verifique o Connector/J.",
                e
            );
        }

        return DriverManager.getConnection(URL, USUARIO, SENHA);
    }
}

