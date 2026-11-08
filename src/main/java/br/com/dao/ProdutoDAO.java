package br.com.dao;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class ProdutoDAO {

    public List<Map<String, Object>> listarTodos() throws SQLException {
        String sql = "SELECT idProduto, nome, categoria, ingredientes FROM produtos ORDER BY nome";
        return DbHelper.query(sql);
    }

    public Map<String, Object> buscarPorId(int id) throws SQLException {
        String sql = "SELECT idProduto, nome, categoria, ingredientes FROM produtos WHERE idProduto = ?";
        List<Map<String, Object>> res = DbHelper.query(sql, id);
        return res.isEmpty() ? null : res.get(0);
    }

    public int inserir(Map<String, Object> p) throws SQLException {
        String sql = "INSERT INTO produtos(nome, categoria, ingredientes) VALUES(?,?,?)";
        return DbHelper.executeInsert(sql, p.get("nome"), p.get("categoria"), p.get("ingredientes"));
    }

    public int atualizar(int id, Map<String, Object> p) throws SQLException {
        String sql = "UPDATE produtos SET nome=?, categoria=?, ingredientes=? WHERE idProduto=?";
        return DbHelper.executeUpdate(sql, p.get("nome"), p.get("categoria"), p.get("ingredientes"), id);
    }

    public int excluir(int id) throws SQLException {
        String sql = "DELETE FROM produtos WHERE idProduto = ?";
        return DbHelper.executeUpdate(sql, id);
    }

    public long contar() throws SQLException {
        Object val = DbHelper.scalar("SELECT COUNT(*) FROM produtos");
        return (val instanceof Number) ? ((Number) val).longValue() : 0L;
    }
}
