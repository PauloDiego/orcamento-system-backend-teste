package com.fautos.dto;

import java.util.List;

public class OrcamentoRequest {
    private ClienteDTO cliente;
    private List<ServicoDTO> servicos;
    private List<PecaDTO> pecas;

    // Construtores
    public OrcamentoRequest() {}

    // Getters e Setters
    public ClienteDTO getCliente() { return cliente; }
    public void setCliente(ClienteDTO cliente) { this.cliente = cliente; }

    public List<ServicoDTO> getServicos() { return servicos; }
    public void setServicos(List<ServicoDTO> servicos) { this.servicos = servicos; }

    public List<PecaDTO> getPecas() { return pecas; }
    public void setPecas(List<PecaDTO> pecas) { this.pecas = pecas; }

    // Classes internas
    public static class ClienteDTO {
        private String nome;
        private String endereco;
        private String bairro;
        private String telefone;
        private String formaPagamento;
        private String modeloVeiculo;
        private String placaVeiculo;

        // Getters e Setters
        public String getNome() { return nome; }
        public void setNome(String nome) { this.nome = nome; }

        public String getEndereco() { return endereco; }
        public void setEndereco(String endereco) { this.endereco = endereco; }

        public String getBairro() { return bairro; }
        public void setBairro(String bairro) { this.bairro = bairro; }

        public String getTelefone() { return telefone; }
        public void setTelefone(String telefone) { this.telefone = telefone; }

        public String getFormaPagamento() { return formaPagamento; }
        public void setFormaPagamento(String formaPagamento) { this.formaPagamento = formaPagamento; }

        public String getModeloVeiculo() { return modeloVeiculo; }
        public void setModeloVeiculo(String modeloVeiculo) { this.modeloVeiculo = modeloVeiculo; }

        public String getPlacaVeiculo() { return placaVeiculo; }
        public void setPlacaVeiculo(String placaVeiculo) { this.placaVeiculo = placaVeiculo; }
    }

    public static class ServicoDTO {
        private Integer quantidade;
        private String descricao;
        private Double valor;

        // Getters e Setters
        public Integer getQuantidade() { return quantidade; }
        public void setQuantidade(Integer quantidade) { this.quantidade = quantidade; }

        public String getDescricao() { return descricao; }
        public void setDescricao(String descricao) { this.descricao = descricao; }

        public Double getValor() { return valor; }
        public void setValor(Double valor) { this.valor = valor; }
    }

    public static class PecaDTO {
        private String nome;
        private Double valor;

        // Getters e Setters
        public String getNome() { return nome; }
        public void setNome(String nome) { this.nome = nome; }

        public Double getValor() { return valor; }
        public void setValor(Double valor) { this.valor = valor; }
    }
}