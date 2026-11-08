package br.com.dao;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class EncomendaItemDAO {

    public List<Map<String, Object>> listarItensEncomenda() throws SQLException {
        String sql = "SELECT ei.idEncomendaItem, ei.idEncomenda, p.nome AS produto, ei.quantidade, "
                   + "ei.valorVenda, (ei.quantidade * ei.valorVenda) AS subtotal, ei.status, ei.dtEntrega "
                   + "FROM encomendaItens ei "
                   + "JOIN produtos p ON p.idProduto = ei.idProduto "
                   + "ORDER BY ei.idEncomenda, p.nome";
        return DbHelper.query(sql);
    }

    public int adicionarItem(Map<String, Object> item) throws SQLException {
        String sql = "INSERT INTO encomendaItens(idEncomenda, idProduto, valorVenda, quantidade, unidadeMedida, status, dtEntrega) "
                   + "VALUES(?,?,?,?,?,?,?)";
        return DbHelper.executeInsert(sql,
            item.get("idEncomenda"), item.get("idProduto"), item.get("valorVenda"),
            item.get("quantidade"), item.get("unidadeMedida"), item.get("status"),
            item.get("dtEntrega")
        );
    }
}
