package br.com.dao;

import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map;

public class DashboardDAO {

    private final ClienteDAO clienteDAO = new ClienteDAO();
    private final ProdutoDAO produtoDAO = new ProdutoDAO();
    private final ComandaDAO comandaDAO = new ComandaDAO();
    private final EncomendaDAO encomendaDAO = new EncomendaDAO();
    private final EstoqueDAO estoqueDAO = new EstoqueDAO();

    public Map<String, Object> carregarDashboard() throws SQLException {
        Map<String, Object> dashboard = new LinkedHashMap<>();
        dashboard.put("clientes", clienteDAO.contar());
        dashboard.put("produtos", produtoDAO.contar());
        dashboard.put("comandasAbertas", comandaDAO.contarAbertas());
        dashboard.put("encomendasPendentes", encomendaDAO.contarPendentes());
        dashboard.put("vendasHoje", comandaDAO.totalVendasHoje());
        dashboard.put("estoqueBaixo", estoqueDAO.contarEstoqueBaixo());
        dashboard.put("comandas", comandaDAO.listarAbertas(8));
        dashboard.put("encomendas", encomendaDAO.listarPendentes(8));
        return dashboard;
    }
}
