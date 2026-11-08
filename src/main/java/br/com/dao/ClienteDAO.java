package br.com.dao;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class ClienteDAO {

    public List<Map<String, Object>> listarTodos() throws SQLException {
        String sql = "SELECT idCliente, nome, email, cpf, telefone, cep, logradouro, numero, complemento, unidade, bairro, cidade, uf, dtCadastro FROM cliente ORDER BY nome";
        return DbHelper.query(sql);
    }

    public Map<String, Object> buscarPorId(int id) throws SQLException {
        String sql = "SELECT idCliente, nome, email, cpf, telefone, cep, logradouro, numero, complemento, unidade, bairro, cidade, uf, dtCadastro FROM cliente WHERE idCliente = ?";
        List<Map<String, Object>> res = DbHelper.query(sql, id);
        return res.isEmpty() ? null : res.get(0);
    }

    public int inserir(Map<String, Object> c) throws SQLException {
        String sql = "INSERT INTO cliente(nome, email, cpf, telefone, cep, logradouro, numero, complemento, unidade, bairro, cidade, uf) VALUES(?,?,?,?,?,?,?,?,?,?,?,?)";
        return DbHelper.executeInsert(sql,
            c.get("nome"), c.get("email"), c.get("cpf"), c.get("telefone"),
            c.get("cep"), c.get("logradouro"), c.get("numero"), c.get("complemento"),
            c.get("unidade"), c.get("bairro"), c.get("cidade"), c.get("uf")
        );
    }

    public int atualizar(int id, Map<String, Object> c) throws SQLException {
        String sql = "UPDATE cliente SET nome=?, email=?, cpf=?, telefone=?, cep=?, logradouro=?, numero=?, complemento=?, unidade=?, bairro=?, cidade=?, uf=? WHERE idCliente=?";
        return DbHelper.executeUpdate(sql,
            c.get("nome"), c.get("email"), c.get("cpf"), c.get("telefone"),
            c.get("cep"), c.get("logradouro"), c.get("numero"), c.get("complemento"),
            c.get("unidade"), c.get("bairro"), c.get("cidade"), c.get("uf"), id
        );
    }

    public int excluir(int id) throws SQLException {
        String sql = "DELETE FROM cliente WHERE idCliente = ?";
        return DbHelper.executeUpdate(sql, id);
    }

    public long contar() throws SQLException {
        Object val = DbHelper.scalar("SELECT COUNT(*) FROM cliente");
        return (val instanceof Number) ? ((Number) val).longValue() : 0L;
    }
}
