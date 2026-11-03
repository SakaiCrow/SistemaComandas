package br.com.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class Estoque {
    private Integer idEstoque;
    private Integer idProduto;
    private BigDecimal quantidade;
    private LocalDateTime dtEntrada;
    private LocalDateTime dtSaida;
    private String motivoSaida;
    private LocalDate validade;
    private String lote;
    private BigDecimal peso;
    private BigDecimal volume;
    private String marca;
    private String modelo;
    private String fabricante;
    private String fornecedor;
    private String notaEntrada;

    public Integer getIdEstoque() { return idEstoque; }
    public void setIdEstoque(Integer idEstoque) { this.idEstoque = idEstoque; }
    public Integer getIdProduto() { return idProduto; }
    public void setIdProduto(Integer idProduto) { this.idProduto = idProduto; }
    public BigDecimal getQuantidade() { return quantidade; }
    public void setQuantidade(BigDecimal quantidade) { this.quantidade = quantidade; }
    public LocalDateTime getDtEntrada() { return dtEntrada; }
    public void setDtEntrada(LocalDateTime dtEntrada) { this.dtEntrada = dtEntrada; }
    public LocalDateTime getDtSaida() { return dtSaida; }
    public void setDtSaida(LocalDateTime dtSaida) { this.dtSaida = dtSaida; }
    public String getMotivoSaida() { return motivoSaida; }
    public void setMotivoSaida(String motivoSaida) { this.motivoSaida = motivoSaida; }
    public LocalDate getValidade() { return validade; }
    public void setValidade(LocalDate validade) { this.validade = validade; }
    public String getLote() { return lote; }
    public void setLote(String lote) { this.lote = lote; }
    public BigDecimal getPeso() { return peso; }
    public void setPeso(BigDecimal peso) { this.peso = peso; }
    public BigDecimal getVolume() { return volume; }
    public void setVolume(BigDecimal volume) { this.volume = volume; }
    public String getMarca() { return marca; }
    public void setMarca(String marca) { this.marca = marca; }
    public String getModelo() { return modelo; }
    public void setModelo(String modelo) { this.modelo = modelo; }
    public String getFabricante() { return fabricante; }
    public void setFabricante(String fabricante) { this.fabricante = fabricante; }
    public String getFornecedor() { return fornecedor; }
    public void setFornecedor(String fornecedor) { this.fornecedor = fornecedor; }
    public String getNotaEntrada() { return notaEntrada; }
    public void setNotaEntrada(String notaEntrada) { this.notaEntrada = notaEntrada; }
}
