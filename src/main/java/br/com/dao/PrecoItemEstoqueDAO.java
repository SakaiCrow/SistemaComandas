package br.com.dao;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class PrecoItemEstoqueDAO {

    public List<Map<String, Object>> listarHistoricoPrecos() throws SQLException {
        String sql = "SELECT p.nome AS produto, e.idEstoque, e.quantidade, pi.valorCompra, pi.valorVenda, pi.dataPreco "
                   + "FROM estoque e "
                   + "JOIN produtos p ON p.idProduto = e.idProduto "
                   + "JOIN precoItemEstoque pi ON pi.idEstoque = e.idEstoque "
                   + "ORDER BY p.nome, pi.dataPreco DESC";
        return DbHelper.query(sql);
    }

    public int inserirPreco(int idEstoque, BigDecimal valorCompra, BigDecimal valorVenda) throws SQLException {
        String sql = "INSERT INTO precoItemEstoque(idEstoque, valorCompra, valorVenda) VALUES(?,?,?)";
        return DbHelper.executeInsert(sql, idEstoque, valorCompra, valorVenda);
    }
}
