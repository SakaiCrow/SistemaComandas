package br.com.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Encomenda {
    private Integer idEncomenda;
    private Integer idCliente;
    private LocalDateTime dtPedido;
    private LocalDateTime dtEntrega;
    private String formaEntrega;
    private String formaPagto;
    private BigDecimal valorPagto;
    private BigDecimal valorFrete;
    private BigDecimal valorEncomenda;

    public Integer getIdEncomenda() { return idEncomenda; }
    public void setIdEncomenda(Integer idEncomenda) { this.idEncomenda = idEncomenda; }
    public Integer getIdCliente() { return idCliente; }
    public void setIdCliente(Integer idCliente) { this.idCliente = idCliente; }
    public LocalDateTime getDtPedido() { return dtPedido; }
    public void setDtPedido(LocalDateTime dtPedido) { this.dtPedido = dtPedido; }
    public LocalDateTime getDtEntrega() { return dtEntrega; }
    public void setDtEntrega(LocalDateTime dtEntrega) { this.dtEntrega = dtEntrega; }
    public String getFormaEntrega() { return formaEntrega; }
    public void setFormaEntrega(String formaEntrega) { this.formaEntrega = formaEntrega; }
    public String getFormaPagto() { return formaPagto; }
    public void setFormaPagto(String formaPagto) { this.formaPagto = formaPagto; }
    public BigDecimal getValorPagto() { return valorPagto; }
    public void setValorPagto(BigDecimal valorPagto) { this.valorPagto = valorPagto; }
    public BigDecimal getValorFrete() { return valorFrete; }
    public void setValorFrete(BigDecimal valorFrete) { this.valorFrete = valorFrete; }
    public BigDecimal getValorEncomenda() { return valorEncomenda; }
    public void setValorEncomenda(BigDecimal valorEncomenda) { this.valorEncomenda = valorEncomenda; }
}
