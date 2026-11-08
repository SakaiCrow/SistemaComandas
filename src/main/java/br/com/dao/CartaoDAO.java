package br.com.dao;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class CartaoDAO {

    public List<Map<String, Object>> listarTodos() throws SQLException {
        String sql = "SELECT idCartao, observacao FROM cartao ORDER BY idCartao";
        return DbHelper.query(sql);
    }

    public int inserir(String observacao) throws SQLException {
        String sql = "INSERT INTO cartao(observacao) VALUES(?)";
        return DbHelper.executeInsert(sql, observacao);
    }

    public int excluir(int idCartao) throws SQLException {
        String sql = "DELETE FROM cartao WHERE idCartao = ?";
        return DbHelper.executeUpdate(sql, idCartao);
    }
}
