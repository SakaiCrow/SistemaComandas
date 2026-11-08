package br.com.dao;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class EncomendaDAO {

    public List<Map<String, Object>> listarEncomendas() throws SQLException {
        String sql = "SELECT e.idEncomenda, c.nome AS cliente, c.telefone, e.dtPedido, e.dtEntrega, "
                   + "e.formaEntrega, e.formaPagto, e.valorEncomenda, e.valorPagto, "
                   + "(e.valorEncomenda - e.valorPagto) AS valorRestante, e.status "
                   + "FROM encomenda e "
                   + "JOIN cliente c ON c.idCliente = e.idCliente "
                   + "ORDER BY e.dtEntrega";
        return DbHelper.query(sql);
    }

    public List<Map<String, Object>> listarPendentes(int limit) throws SQLException {
        String sql = "SELECT e.idEncomenda, c.nome AS cliente, c.telefone, e.dtEntrega, "
                   + "e.formaEntrega, e.valorEncomenda, e.valorPagto, e.status "
                   + "FROM encomenda e "
                   + "JOIN cliente c ON c.idCliente = e.idCliente "
                   + "WHERE e.status NOT IN ('Concluída', 'Cancelada') "
                   + "ORDER BY e.dtEntrega LIMIT " + limit;
        return DbHelper.query(sql);
    }

    public int inserir(Map<String, Object> e) throws SQLException {
        String sql = "INSERT INTO encomenda(idCliente, dtEntrega, formaEntrega, formaPagto, valorPagto, valorFrete, valorEncomenda, status) "
                   + "VALUES(?,?,?,?,?,?,?,?)";
        return DbHelper.executeInsert(sql,
            e.get("idCliente"), e.get("dtEntrega"), e.get("formaEntrega"),
            e.get("formaPagto"), e.get("valorPagto"), e.get("valorFrete"),
            e.get("valorEncomenda"), e.get("status")
        );
    }

    public int atualizar(int idEncomenda, Map<String, Object> e) throws SQLException {
        String sql = "UPDATE encomenda SET idCliente=?, dtEntrega=?, formaEntrega=?, formaPagto=?, valorPagto=?, valorFrete=?, valorEncomenda=?, status=? "
                   + "WHERE idEncomenda=?";
        return DbHelper.executeUpdate(sql,
            e.get("idCliente"), e.get("dtEntrega"), e.get("formaEntrega"),
            e.get("formaPagto"), e.get("valorPagto"), e.get("valorFrete"),
            e.get("valorEncomenda"), e.get("status"), idEncomenda
        );
    }

    public int atualizarStatus(int idEncomenda, String status) throws SQLException {
        String sql = "UPDATE encomenda SET status=? WHERE idEncomenda=?";
        return DbHelper.executeUpdate(sql, status, idEncomenda);
    }

    public long contarPendentes() throws SQLException {
        Object val = DbHelper.scalar("SELECT COUNT(*) FROM encomenda WHERE status NOT IN ('Concluída', 'Cancelada')");
        return (val instanceof Number) ? ((Number) val).longValue() : 0L;
    }
}
