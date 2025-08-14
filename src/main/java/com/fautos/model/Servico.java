package com.fautos.model;

import jakarta.persistence.*;

@Entity
@Table(name = "servicos")
public class Servico {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private Integer quantidade;
    private String descricao;
    private Double valor;
    
    @ManyToOne
    @JoinColumn(name = "orcamento_id")
    private Orcamento orcamento;

    // Construtores
    public Servico() {}

    public Servico(Integer quantidade, String descricao, Double valor) {
        this.quantidade = quantidade;
        this.descricao = descricao;
        this.valor = valor;
    }

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Integer getQuantidade() { return quantidade; }
    public void setQuantidade(Integer quantidade) { this.quantidade = quantidade; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public Double getValor() { return valor; }
    public void setValor(Double valor) { this.valor = valor; }

    public Orcamento getOrcamento() { return orcamento; }
    public void setOrcamento(Orcamento orcamento) { this.orcamento = orcamento; }
}