package br.com.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Comanda {
    private Integer idComanda;
    private String numeroComanda;
    private String numeroMesa;
    private String nomeClienteProvisorio;
    private Integer idCliente;
    private LocalDateTime dtAbertura;
    private LocalDateTime dtFechamento;
    private BigDecimal valor;
    private BigDecimal desconto;
    private String promocao;
    private BigDecimal valorPago;
    private String metodoPagamento;
    private String status;

    public Comanda() {}

    public Integer getIdComanda() { return idComanda; }
    public void setIdComanda(Integer idComanda) { this.idComanda = idComanda; }

    public String getNumeroComanda() { return numeroComanda; }
    public void setNumeroComanda(String numeroComanda) { this.numeroComanda = numeroComanda; }

    public String getNumeroMesa() { return numeroMesa; }
    public void setNumeroMesa(String numeroMesa) { this.numeroMesa = numeroMesa; }

    public String getNomeClienteProvisorio() { return nomeClienteProvisorio; }
    public void setNomeClienteProvisorio(String nomeClienteProvisorio) { this.nomeClienteProvisorio = nomeClienteProvisorio; }

    public Integer getIdCliente() { return idCliente; }
    public void setIdCliente(Integer idCliente) { this.idCliente = idCliente; }

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

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
