package br.com.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class PrecoItemEstoque {
    private Integer idPrecoItemEstoque;
    private Integer idEstoque;
    private BigDecimal valorCompra;
    private BigDecimal valorVenda;
    private LocalDateTime dataPreco;

    public Integer getIdPrecoItemEstoque() { return idPrecoItemEstoque; }
    public void setIdPrecoItemEstoque(Integer idPrecoItemEstoque) { this.idPrecoItemEstoque = idPrecoItemEstoque; }
    public Integer getIdEstoque() { return idEstoque; }
    public void setIdEstoque(Integer idEstoque) { this.idEstoque = idEstoque; }
    public BigDecimal getValorCompra() { return valorCompra; }
    public void setValorCompra(BigDecimal valorCompra) { this.valorCompra = valorCompra; }
    public BigDecimal getValorVenda() { return valorVenda; }
    public void setValorVenda(BigDecimal valorVenda) { this.valorVenda = valorVenda; }
    public LocalDateTime getDataPreco() { return dataPreco; }
    public void setDataPreco(LocalDateTime dataPreco) { this.dataPreco = dataPreco; }
}