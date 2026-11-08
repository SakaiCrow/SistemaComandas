package br.com.dao;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class ComandaDAO {

    public List<Map<String, Object>> listarComandas() throws SQLException {
        String sql = "SELECT co.idComanda, co.numeroComanda, co.numeroMesa, COALESCE(c.nome, co.nomeClienteProvisorio) AS cliente, "
                   + "co.dtAbertura, co.dtFechamento, co.valor, co.desconto, co.valorPago, co.metodoPagamento, co.status "
                   + "FROM comanda co "
                   + "LEFT JOIN cliente c ON c.idCliente = co.idCliente "
                   + "ORDER BY co.dtAbertura DESC";
        return DbHelper.query(sql);
    }

    public List<Map<String, Object>> listarAbertas(int limit) throws SQLException {
        String sql = "SELECT co.idComanda, co.numeroComanda, co.numeroMesa, COALESCE(c.nome, co.nomeClienteProvisorio) AS cliente, "
                   + "co.dtAbertura, co.valor, co.desconto, co.valorPago, co.metodoPagamento, co.status "
                   + "FROM comanda co "
                   + "LEFT JOIN cliente c ON c.idCliente = co.idCliente "
                   + "WHERE co.status = 'Aberta' "
                   + "ORDER BY co.dtAbertura DESC LIMIT " + limit;
        return DbHelper.query(sql);
    }

    public int abrirComanda(Map<String, Object> c) throws SQLException {
        String sql = "INSERT INTO comanda(numeroComanda, numeroMesa, nomeClienteProvisorio, idCliente, valor, desconto, promocao, status) "
                   + "VALUES(?,?,?,?,?,?,?,'Aberta')";
        return DbHelper.executeInsert(sql,
            c.get("numeroComanda"), c.get("numeroMesa"), c.get("nomeClienteProvisorio"),
            c.get("idCliente"), c.get("valor"), c.get("desconto"), c.get("promocao")
        );
    }

    public int fecharComanda(int idComanda, Object valorPago, Object metodoPagamento) throws SQLException {
        String sql = "UPDATE comanda SET status='Fechada', dtFechamento=NOW(), valorPago=?, metodoPagamento=? "
                   + "WHERE idComanda=? AND status='Aberta'";
        return DbHelper.executeUpdate(sql, valorPago, metodoPagamento, idComanda);
    }

    public int excluir(int idComanda) throws SQLException {
        String sql = "DELETE FROM comanda WHERE idComanda = ?";
        return DbHelper.executeUpdate(sql, idComanda);
    }

    public long contarAbertas() throws SQLException {
        Object val = DbHelper.scalar("SELECT COUNT(*) FROM comanda WHERE status = 'Aberta'");
        return (val instanceof Number) ? ((Number) val).longValue() : 0L;
    }

    public BigDecimal totalVendasHoje() throws SQLException {
        String sql = "SELECT COALESCE(SUM(valorPago), 0) FROM comanda WHERE status = 'Fechada' AND dtFechamento >= CURDATE() AND dtFechamento < CURDATE() + INTERVAL 1 DAY";
        Object val = DbHelper.scalar(sql);
        if (val instanceof BigDecimal) return (BigDecimal) val;
        if (val instanceof Number) return BigDecimal.valueOf(((Number) val).doubleValue());
        return BigDecimal.ZERO;
    }

    public List<Map<String, Object>> relatorioVendasHoje() throws SQLException {
        String sql = "SELECT metodoPagamento, COUNT(*) AS quantidadeComandas, SUM(valorPago) AS totalRecebido "
                   + "FROM comanda "
                   + "WHERE status = 'Fechada' AND dtFechamento >= CURDATE() AND dtFechamento < CURDATE() + INTERVAL 1 DAY "
                   + "GROUP BY metodoPagamento ORDER BY totalRecebido DESC";
        return DbHelper.query(sql);
    }
}
