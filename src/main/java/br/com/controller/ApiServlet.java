package br.com.controller;

import br.com.dao.CartaoDAO;
import br.com.dao.ClienteDAO;
import br.com.dao.ComandaDAO;
import br.com.dao.ComandaItemEstoqueDAO;
import br.com.dao.Conexao;
import br.com.dao.DashboardDAO;
import br.com.dao.EncomendaDAO;
import br.com.dao.EncomendaItemDAO;
import br.com.dao.EstoqueDAO;
import br.com.dao.PrecoItemEstoqueDAO;
import br.com.dao.ProdutoDAO;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/api/*")
public class ApiServlet extends HttpServlet {

    private final Gson gson = new GsonBuilder()
            .serializeNulls()
            .setDateFormat("yyyy-MM-dd HH:mm:ss")
            .create();

    private final ClienteDAO clienteDAO = new ClienteDAO();
    private final ProdutoDAO produtoDAO = new ProdutoDAO();
    private final EstoqueDAO estoqueDAO = new EstoqueDAO();
    private final PrecoItemEstoqueDAO precoItemEstoqueDAO = new PrecoItemEstoqueDAO();
    private final CartaoDAO cartaoDAO = new CartaoDAO();
    private final ComandaDAO comandaDAO = new ComandaDAO();
    private final ComandaItemEstoqueDAO comandaItemEstoqueDAO = new ComandaItemEstoqueDAO();
    private final EncomendaDAO encomendaDAO = new EncomendaDAO();
    private final EncomendaItemDAO encomendaItemDAO = new EncomendaItemDAO();
    private final DashboardDAO dashboardDAO = new DashboardDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String[] parts = getPathParts(req);
        if (parts.length == 0) {
            sendJson(resp, HttpServletResponse.SC_BAD_REQUEST, Map.of("erro", "Recurso não especificado."));
            return;
        }

        String resource = parts[0];

        try {
            switch (resource) {
                case "health":
                    boolean ok = Conexao.testarConexao();
                    sendJson(resp, HttpServletResponse.SC_OK, Map.of(
                            "ok", ok,
                            "database", ok ? "connected" : "disconnected"
                    ));
                    break;

                case "dashboard":
                    sendJson(resp, HttpServletResponse.SC_OK, dashboardDAO.carregarDashboard());
                    break;

                case "clientes":
                    sendJson(resp, HttpServletResponse.SC_OK, clienteDAO.listarTodos());
                    break;

                case "produtos":
                    sendJson(resp, HttpServletResponse.SC_OK, produtoDAO.listarTodos());
                    break;

                case "estoque":
                    sendJson(resp, HttpServletResponse.SC_OK, estoqueDAO.listarEstoque());
                    break;

                case "precos":
                    sendJson(resp, HttpServletResponse.SC_OK, precoItemEstoqueDAO.listarHistoricoPrecos());
                    break;

                case "comandas":
                    sendJson(resp, HttpServletResponse.SC_OK, comandaDAO.listarComandas());
                    break;

                case "itens-comanda":
                    sendJson(resp, HttpServletResponse.SC_OK, comandaItemEstoqueDAO.listarItensComanda());
                    break;

                case "encomendas":
                    sendJson(resp, HttpServletResponse.SC_OK, encomendaDAO.listarEncomendas());
                    break;

                case "itens-encomenda":
                    sendJson(resp, HttpServletResponse.SC_OK, encomendaItemDAO.listarItensEncomenda());
                    break;

                case "cartoes":
                    sendJson(resp, HttpServletResponse.SC_OK, cartaoDAO.listarTodos());
                    break;

                case "estoque-baixo":
                    sendJson(resp, HttpServletResponse.SC_OK, estoqueDAO.listarEstoqueBaixo());
                    break;

                case "vencimentos":
                    sendJson(resp, HttpServletResponse.SC_OK, estoqueDAO.listarVencimentos());
                    break;

                case "historico-estoque":
                    sendJson(resp, HttpServletResponse.SC_OK, estoqueDAO.listarHistoricoEntradas());
                    break;

                case "relatorio-vendas":
                    sendJson(resp, HttpServletResponse.SC_OK, comandaDAO.relatorioVendasHoje());
                    break;

                default:
                    sendJson(resp, HttpServletResponse.SC_NOT_FOUND, Map.of("erro", "Recurso '" + resource + "' não encontrado."));
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
            sendJson(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, Map.of("erro", safeMessage(e)));
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String[] parts = getPathParts(req);
        if (parts.length == 0) {
            sendJson(resp, HttpServletResponse.SC_BAD_REQUEST, Map.of("erro", "Recurso não especificado."));
            return;
        }

        String resource = parts[0];
        Map<String, Object> body = readBody(req);

        try {
            // Rota especial: POST /api/comandas/{id}/fechar
            if ("comandas".equals(resource) && parts.length >= 3 && "fechar".equals(parts[2])) {
                int idComanda = Integer.parseInt(parts[1]);
                int affected = comandaDAO.fecharComanda(idComanda, body.get("valorPago"), body.get("metodoPagamento"));
                if (affected == 0) {
                    sendJson(resp, HttpServletResponse.SC_BAD_REQUEST, Map.of("erro", "Comanda não encontrada ou não está aberta."));
                } else {
                    sendJson(resp, HttpServletResponse.SC_OK, Map.of("mensagem", "Comanda fechada com sucesso."));
                }
                return;
            }

            int id = 0;
            switch (resource) {
                case "clientes":
                    id = clienteDAO.inserir(body);
                    break;

                case "produtos":
                    id = produtoDAO.inserir(body);
                    break;

                case "cartoes":
                    Object obs = body.get("observacao");
                    id = cartaoDAO.inserir(obs != null ? obs.toString() : "");
                    break;

                case "itens-comanda":
                    id = comandaItemEstoqueDAO.adicionarItem(body);
                    break;

                case "estoque":
                    id = estoqueDAO.inserir(body);
                    break;

                case "encomendas":
                    id = encomendaDAO.inserir(body);
                    break;

                case "comandas":
                    id = comandaDAO.abrirComanda(body);
                    break;

                default:
                    sendJson(resp, HttpServletResponse.SC_BAD_REQUEST, Map.of("erro", "Cadastro não suportado para o recurso: " + resource));
                    return;
            }

            sendJson(resp, HttpServletResponse.SC_CREATED, Map.of("id", id, "mensagem", "Registro cadastrado com sucesso."));

        } catch (Exception e) {
            e.printStackTrace();
            sendJson(resp, HttpServletResponse.SC_BAD_REQUEST, Map.of("erro", safeMessage(e)));
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String[] parts = getPathParts(req);
        if (parts.length < 2) {
            sendJson(resp, HttpServletResponse.SC_BAD_REQUEST, Map.of("erro", "Identificador do registro não fornecido."));
            return;
        }

        String resource = parts[0];
        int id;
        try {
            id = Integer.parseInt(parts[1]);
        } catch (NumberFormatException e) {
            sendJson(resp, HttpServletResponse.SC_BAD_REQUEST, Map.of("erro", "ID inválido: " + parts[1]));
            return;
        }

        Map<String, Object> body = readBody(req);

        try {
            // Rota especial: PUT /api/encomendas/{id}/status
            if ("encomendas".equals(resource) && parts.length >= 3 && "status".equals(parts[2])) {
                String status = (body.get("status") != null) ? body.get("status").toString() : "";
                int n = encomendaDAO.atualizarStatus(id, status);
                sendJson(resp, HttpServletResponse.SC_OK, Map.of("atualizados", n));
                return;
            }

            int n = 0;
            switch (resource) {
                case "clientes":
                    n = clienteDAO.atualizar(id, body);
                    break;

                case "produtos":
                    n = produtoDAO.atualizar(id, body);
                    break;

                case "encomendas":
                    n = encomendaDAO.atualizar(id, body);
                    break;

                default:
                    sendJson(resp, HttpServletResponse.SC_BAD_REQUEST, Map.of("erro", "Edição não suportada para o recurso: " + resource));
                    return;
            }

            sendJson(resp, HttpServletResponse.SC_OK, Map.of("atualizados", n));

        } catch (Exception e) {
            e.printStackTrace();
            sendJson(resp, HttpServletResponse.SC_BAD_REQUEST, Map.of("erro", safeMessage(e)));
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String[] parts = getPathParts(req);
        if (parts.length < 2) {
            sendJson(resp, HttpServletResponse.SC_BAD_REQUEST, Map.of("erro", "Identificador do registro não fornecido."));
            return;
        }

        String resource = parts[0];
        int id;
        try {
            id = Integer.parseInt(parts[1]);
        } catch (NumberFormatException e) {
            sendJson(resp, HttpServletResponse.SC_BAD_REQUEST, Map.of("erro", "ID inválido: " + parts[1]));
            return;
        }

        try {
            int n = 0;
            switch (resource) {
                case "clientes":
                    n = clienteDAO.excluir(id);
                    break;

                case "produtos":
                    n = produtoDAO.excluir(id);
                    break;

                case "cartoes":
                    n = cartaoDAO.excluir(id);
                    break;

                default:
                    sendJson(resp, HttpServletResponse.SC_BAD_REQUEST, Map.of("erro", "Exclusão não permitida para o recurso: " + resource));
                    return;
            }

            sendJson(resp, HttpServletResponse.SC_OK, Map.of("excluidos", n));

        } catch (Exception e) {
            e.printStackTrace();
            sendJson(resp, HttpServletResponse.SC_BAD_REQUEST, Map.of("erro", safeMessage(e)));
        }
    }

    private String[] getPathParts(HttpServletRequest req) {
        String pathInfo = req.getPathInfo();
        if (pathInfo == null || pathInfo.trim().isEmpty() || "/".equals(pathInfo.trim())) {
            return new String[0];
        }
        if (pathInfo.startsWith("/")) {
            pathInfo = pathInfo.substring(1);
        }
        return pathInfo.split("/");
    }

    private Map<String, Object> readBody(HttpServletRequest req) {
        try (BufferedReader reader = req.getReader()) {
            Type type = new TypeToken<Map<String, Object>>() {}.getType();
            Map<String, Object> map = gson.fromJson(reader, type);
            return (map != null) ? map : new LinkedHashMap<>();
        } catch (Exception e) {
            return new LinkedHashMap<>();
        }
    }

    private void sendJson(HttpServletResponse resp, int statusCode, Object data) throws IOException {
        resp.setStatus(statusCode);
        resp.setContentType("application/json;charset=UTF-8");
        resp.getWriter().write(gson.toJson(data));
        resp.getWriter().flush();
    }

    private String safeMessage(Exception e) {
        Throwable cause = e;
        while (cause.getCause() != null) {
            cause = cause.getCause();
        }
        return cause.getMessage() != null ? cause.getMessage() : "Erro interno no servidor";
    }
}
