package br.com.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class EncomendaItem {
    private Integer idEncomendaItem;
    private Integer idEncomenda;
    private Integer idProduto;
    private BigDecimal valorVenda;
    private BigDecimal quantidade;
    private LocalDateTime dtSaida;
    private String motivoSaida;
    private String status;
    private LocalDateTime dtEntrega;
    private LocalDate validade;
    private String lote;
    private BigDecimal peso;
    private BigDecimal volume;

    public Integer getIdEncomendaItem() { return idEncomendaItem; }
    public void setIdEncomendaItem(Integer idEncomendaItem) { this.idEncomendaItem = idEncomendaItem; }
    public Integer getIdEncomenda() { return idEncomenda; }
    public void setIdEncomenda(Integer idEncomenda) { this.idEncomenda = idEncomenda; }
    public Integer getIdProduto() { return idProduto; }
    public void setIdProduto(Integer idProduto) { this.idProduto = idProduto; }
    public BigDecimal getValorVenda() { return valorVenda; }
    public void setValorVenda(BigDecimal valorVenda) { this.valorVenda = valorVenda; }
    public BigDecimal getQuantidade() { return quantidade; }
    public void setQuantidade(BigDecimal quantidade) { this.quantidade = quantidade; }
    public LocalDateTime getDtSaida() { return dtSaida; }
    public void setDtSaida(LocalDateTime dtSaida) { this.dtSaida = dtSaida; }
    public String getMotivoSaida() { return motivoSaida; }
    public void setMotivoSaida(String motivoSaida) { this.motivoSaida = motivoSaida; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public LocalDateTime getDtEntrega() { return dtEntrega; }
    public void setDtEntrega(LocalDateTime dtEntrega) { this.dtEntrega = dtEntrega; }
    public LocalDate getValidade() { return validade; }
    public void setValidade(LocalDate validade) { this.validade = validade; }
    public String getLote() { return lote; }
    public void setLote(String lote) { this.lote = lote; }
    public BigDecimal getPeso() { return peso; }
    public void setPeso(BigDecimal peso) { this.peso = peso; }
    public BigDecimal getVolume() { return volume; }
    public void setVolume(BigDecimal volume) { this.volume = volume; }
}