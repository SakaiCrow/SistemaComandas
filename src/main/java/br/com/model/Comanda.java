package br.com.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Comanda {
    private Integer idComanda;
    private LocalDateTime dtAbertura;
    private LocalDateTime dtFechamento;
    private BigDecimal valor;
    private BigDecimal desconto;
    private String promocao;
    private BigDecimal valorPago;
    private String metodoPagamento;

    public Integer getIdComanda() { return idComanda; }
    public void setIdComanda(Integer idComanda) { this.idComanda = idComanda; }
    public LocalDateTime getDtAbertura() { return dtAbertura; }
    public void setDtAbertura(LocalDateTime dtAbertura) { this.dtAbertura = dtAbertura; }
    public LocalDateTime getDtFechamento() { return dtFechamento; }
    public void setDtFechamento(LocalDateTime dtFechamento) { this.dtFechamento = dtFechamento; }
    public BigDecimal getValor() { return valor; }
    public void setValor(BigDecimal valor) { this.valor = valor; }
    public BigDecimal getDesconto() { return desconto; }
    public void setDesconto(BigDecimal desconto) { this.desconto = desconto; }
    public String getPromocao() { return promocao; }
    public void setPromocao(String promocao) { this.promocao = promocao; }
    public BigDecimal getValorPago() { return valorPago; }
    public void setValorPago(BigDecimal valorPago) { this.valorPago = valorPago; }
    public String getMetodoPagamento() { return metodoPagamento; }
    public void setMetodoPagamento(String metodoPagamento) { this.metodoPagamento = metodoPagamento; }
}
