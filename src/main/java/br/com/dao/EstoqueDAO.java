package br.com.dao;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class EstoqueDAO {

    public List<Map<String, Object>> listarEstoque() throws SQLException {
        String sql = "SELECT e.idEstoque, p.nome AS produto, p.categoria, e.quantidade, e.validade, e.lote, e.marca, e.fornecedor, pi.valorCompra, pi.valorVenda "
                   + "FROM estoque e "
                   + "JOIN produtos p ON p.idProduto = e.idProduto "
                   + "LEFT JOIN precoItemEstoque pi ON pi.idPrecoItemEstoque = (SELECT MAX(p2.idPrecoItemEstoque) FROM precoItemEstoque p2 WHERE p2.idEstoque = e.idEstoque) "
                   + "ORDER BY p.nome";
        return DbHelper.query(sql);
    }

    public int inserir(Map<String, Object> e) throws SQLException {
        String sql = "INSERT INTO estoque(idProduto, quantidade, validade, lote, marca, modelo, fabricante, fornecedor, notaEntrada, peso, volume) "
                   + "VALUES(?,?,?,?,?,?,?,?,?,?,?)";
        int idEstoque = DbHelper.executeInsert(sql,
            e.get("idProduto"), e.get("quantidade"), e.get("validade"),
            e.get("lote"), e.get("marca"), e.get("modelo"), e.get("fabricante"),
            e.get("fornecedor"), e.get("notaEntrada"), e.get("peso"), e.get("volume")
        );

        if (idEstoque > 0 && e.get("valorCompra") != null && e.get("valorVenda") != null) {
            String sqlPreco = "INSERT INTO precoItemEstoque(idEstoque, valorCompra, valorVenda) VALUES(?,?,?)";
            DbHelper.executeInsert(sqlPreco, idEstoque, e.get("valorCompra"), e.get("valorVenda"));
        }

        return idEstoque;
    }

    public int excluir(int idEstoque) throws SQLException {
        DbHelper.executeUpdate("DELETE FROM precoItemEstoque WHERE idEstoque = ?", idEstoque);
        return DbHelper.executeUpdate("DELETE FROM estoque WHERE idEstoque = ?", idEstoque);
    }

    public List<Map<String, Object>> listarEstoqueBaixo() throws SQLException {
        String sql = "SELECT p.idProduto, p.nome AS produto, COALESCE(SUM(e.quantidade), 0) AS quantidadeEstoque "
                   + "FROM produtos p "
                   + "LEFT JOIN estoque e ON e.idProduto = p.idProduto "
                   + "GROUP BY p.idProduto, p.nome "
                   + "HAVING COALESCE(SUM(e.quantidade), 0) < 5 "
                   + "ORDER BY quantidadeEstoque";
        return DbHelper.query(sql);
    }

    public List<Map<String, Object>> listarVencimentos() throws SQLException {
        String sql = "SELECT p.nome AS produto, e.idEstoque, e.lote, e.quantidade, e.validade, DATEDIFF(e.validade, CURDATE()) AS diasParaVencer "
                   + "FROM estoque e "
                   + "INNER JOIN produtos p ON p.idProduto = e.idProduto "
                   + "WHERE e.validade IS NOT NULL "
                   + "  AND e.validade BETWEEN CURDATE() AND CURDATE() + INTERVAL 30 DAY "
                   + "  AND e.quantidade > 0 "
                   + "ORDER BY e.validade";
        return DbHelper.query(sql);
    }

    public List<Map<String, Object>> listarHistoricoEntradas() throws SQLException {
        String sql = "SELECT p.nome AS produto, e.quantidade, e.dtEntrada, e.fornecedor, e.notaEntrada, e.lote, e.validade "
                   + "FROM estoque e "
                   + "INNER JOIN produtos p ON p.idProduto = e.idProduto "
                   + "ORDER BY e.dtEntrada DESC";
        return DbHelper.query(sql);
    }

    public long contarEstoqueBaixo() throws SQLException {
        String sql = "SELECT COUNT(*) FROM (SELECT p.idProduto FROM produtos p LEFT JOIN estoque e ON e.idProduto=p.idProduto GROUP BY p.idProduto HAVING COALESCE(SUM(e.quantidade),0) < 5) x";
        Object val = DbHelper.scalar(sql);
        return (val instanceof Number) ? ((Number) val).longValue() : 0L;
    }
}
