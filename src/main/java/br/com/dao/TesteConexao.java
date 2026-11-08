package br.com.dao;

import java.sql.Connection;
import java.sql.SQLException;

public class TesteConexao {

    public static void main(String[] args) {
        System.out.println("Tentando conectar ao banco de dados MySQL...");
        try (Connection conexao = Conexao.conectar()) {
            if (conexao != null && !conexao.isClosed()) {
                System.out.println("Conexão com o MySQL realizada com sucesso no banco: " + conexao.getCatalog());
            }
        } catch (SQLException e) {
            System.err.println("Erro ao conectar com o MySQL: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
