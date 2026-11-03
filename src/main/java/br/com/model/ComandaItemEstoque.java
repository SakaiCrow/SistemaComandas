package br.com.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class ComandaItemEstoque {
    private Integer idComandaItem;
    private Integer idComanda;
    private Integer idEstoque;
    private BigDecimal precoVenda;
    private BigDecimal quantidade;
    private String unidadeMedida;
    private LocalDateTime dataPedido;
    private LocalDateTime dataEntrega;

    public Integer getIdComandaItem() { return idComandaItem; }
    public void setIdComandaItem(Integer idComandaItem) { this.idComandaItem = idComandaItem; }
    public Integer getIdComanda() { return idComanda; }
    public void setIdComanda(Integer idComanda) { this.idComanda = idComanda; }
    public Integer getIdEstoque() { return idEstoque; }
    public void setIdEstoque(Integer idEstoque) { this.idEstoque = idEstoque; }
    public BigDecimal getPrecoVenda() { return precoVenda; }
    public void setPrecoVenda(BigDecimal precoVenda) { this.precoVenda = precoVenda; }
    public BigDecimal getQuantidade() { return quantidade; }
    public void setQuantidade(BigDecimal quantidade) { this.quantidade = quantidade; }
    public String getUnidadeMedida() { return unidadeMedida; }
    public void setUnidadeMedida(String unidadeMedida) { this.unidadeMedida = unidadeMedida; }
    public LocalDateTime getDataPedido() { return dataPedido; }
    public void setDataPedido(LocalDateTime dataPedido) { this.dataPedido = dataPedido; }
    public LocalDateTime getDataEntrega() { return dataEntrega; }
    public void setDataEntrega(LocalDateTime dataEntrega) { this.dataEntrega = dataEntrega; }
}
