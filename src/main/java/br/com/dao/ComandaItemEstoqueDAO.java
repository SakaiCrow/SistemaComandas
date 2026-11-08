package br.com.dao;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class ComandaItemEstoqueDAO {

    public List<Map<String, Object>> listarItensComanda() throws SQLException {
        String sql = "SELECT ci.idComandaItem, ci.idComanda, co.numeroMesa, p.nome AS produto, ci.quantidade, "
                   + "ci.unidadeMedida, ci.precovenda, (ci.quantidade * ci.precovenda) AS subtotal, "
                   + "ci.dataPedido, ci.dataEntrega "
                   + "FROM comandaItemEstoque ci "
                   + "JOIN comanda co ON co.idComanda = ci.idComanda "
                   + "JOIN estoque e ON e.idEstoque = ci.idEstoque "
                   + "JOIN produtos p ON p.idProduto = e.idProduto "
                   + "ORDER BY ci.idComanda, ci.dataPedido";
        return DbHelper.query(sql);
    }

    public int adicionarItem(Map<String, Object> item) throws SQLException {
        String sqlInsert = "INSERT INTO comandaItemEstoque(idComanda, idEstoque, precovenda, quantidade, unidadeMedida) VALUES(?,?,?,?,?)";
        int idItem = DbHelper.executeInsert(sqlInsert,
            item.get("idComanda"), item.get("idEstoque"), item.get("precovenda"),
            item.get("quantidade"), item.get("unidadeMedida")
        );

        if (idItem > 0 && item.get("idComanda") != null && item.get("precovenda") != null && item.get("quantidade") != null) {
            String sqlUpdate = "UPDATE comanda SET valor = COALESCE(valor, 0) + (? * ?) WHERE idComanda = ?";
            DbHelper.executeUpdate(sqlUpdate, item.get("precovenda"), item.get("quantidade"), item.get("idComanda"));
        }

        return idItem;
    }
}
