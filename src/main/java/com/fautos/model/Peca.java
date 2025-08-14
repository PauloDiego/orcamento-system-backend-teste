package com.fautos.model;

import jakarta.persistence.*;

@Entity
@Table(name = "pecas")
public class Peca {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String nome;
    private Double valor;
    
    @ManyToOne
    @JoinColumn(name = "orcamento_id")
    private Orcamento orcamento;

    // Construtores
    public Peca() {}

    public Peca(String nome, Double valor) {
        this.nome = nome;
        this.valor = valor;
    }

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public Double getValor() { return valor; }
    public void setValor(Double valor) { this.valor = valor; }

    public Orcamento getOrcamento() { return orcamento; }
    public void setOrcamento(Orcamento orcamento) { this.orcamento = orcamento; }
}