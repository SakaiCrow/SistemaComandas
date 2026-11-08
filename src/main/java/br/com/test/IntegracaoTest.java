package br.com.test;

import br.com.dao.ClienteDAO;
import br.com.dao.ComandaDAO;
import br.com.dao.Conexao;
import br.com.dao.DashboardDAO;
import br.com.dao.EstoqueDAO;
import br.com.dao.ProdutoDAO;

import java.math.BigDecimal;
import java.util.Map;

public class IntegracaoTest {

    public static void main(String[] args) {
        System.out.println("=== INICIANDO TESTE DE INTEGRAÇÃO DO SISTEMA DE COMANDAS ===");

        boolean dbOk = Conexao.testarConexao();
        System.out.println("1. Conexão com o banco: " + (dbOk ? "OK (Conectado)" : "FALHA"));
        if (!dbOk) {
            System.err.println("Não foi possível conectar ao banco. Abortando teste.");
            return;
        }

        try {
            ClienteDAO clienteDAO = new ClienteDAO();
            ProdutoDAO produtoDAO = new ProdutoDAO();
            EstoqueDAO estoqueDAO = new EstoqueDAO();
            ComandaDAO comandaDAO = new ComandaDAO();
            DashboardDAO dashboardDAO = new DashboardDAO();

            System.out.println("2. Testando contagem inicial:");
            System.out.println("   - Clientes: " + clienteDAO.contar());
            System.out.println("   - Produtos: " + produtoDAO.contar());
            System.out.println("   - Comandas Abertas: " + comandaDAO.contarAbertas());

            System.out.println("3. Testando Dashboard DAO:");
            Map<String, Object> dash = dashboardDAO.carregarDashboard();
            System.out.println("   - Dashboard carregado com sucesso: " + dash.keySet());

            System.out.println("4. Testando inserção de Produto de teste:");
            int idProduto = produtoDAO.inserir(Map.of(
                    "nome", "Hambúrguer Artesanal Teste",
                    "categoria", "Lanches",
                    "ingredientes", "Pão brioche, blend 180g, queijo cheddar, bacon"
            ));
            System.out.println("   - Produto inserido com ID: " + idProduto);

            System.out.println("5. Testando inserção de Estoque e Preço para o produto:");
            int idEstoque = estoqueDAO.inserir(Map.of(
                    "idProduto", idProduto,
                    "quantidade", new BigDecimal("10.000"),
                    "lote", "LOTE-2026-001",
                    "valorCompra", new BigDecimal("12.50"),
                    "valorVenda", new BigDecimal("32.00")
            ));
            System.out.println("   - Estoque inserido com ID: " + idEstoque);

            System.out.println("6. Testando inserção de Cliente de teste:");
            int idCliente = clienteDAO.inserir(Map.of(
                    "nome", "Cliente Teste",
                    "email", "teste@exemplo.com",
                    "telefone", "(11) 98888-7777"
            ));
            System.out.println("   - Cliente inserido com ID: " + idCliente);

            System.out.println("7. Testando Abertura de Comanda:");
            int idComanda = comandaDAO.abrirComanda(Map.of(
                    "numeroComanda", "CMD-001",
                    "numeroMesa", "Mesa 05",
                    "idCliente", idCliente,
                    "valor", new BigDecimal("0.00"),
                    "desconto", new BigDecimal("0.00")
            ));
            System.out.println("   - Comanda aberta com ID: " + idComanda);

            System.out.println("8. Testando Fechamento de Comanda:");
            int fechada = comandaDAO.fecharComanda(idComanda, new BigDecimal("32.00"), "Pix");
            System.out.println("   - Comanda fechada (" + fechada + " registro atualizado)");

            System.out.println("9. Limpando dados de teste...");
            comandaDAO.excluir(idComanda);
            clienteDAO.excluir(idCliente);
            estoqueDAO.excluir(idEstoque);
            produtoDAO.excluir(idProduto);

            System.out.println("=== TODOS OS TESTES FORAM CONCLUÍDOS COM SUCESSO! ===");

        } catch (Exception e) {
            System.err.println("Erro durante os testes de integração: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
