package com.fautos.service;

import com.fautos.dto.OrcamentoRequest;
import com.fautos.model.*;
import com.fautos.repository.OrcamentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrcamentoService {

    @Autowired
    private OrcamentoRepository orcamentoRepository;

    @Autowired
    private PdfService pdfService;

    public byte[] criarOrcamentoEGerarPdf(OrcamentoRequest request) {
        // Criar entidade Cliente
        Cliente cliente = new Cliente(
            request.getCliente().getNome(),
            request.getCliente().getEndereco(),
            request.getCliente().getBairro(),
            request.getCliente().getTelefone(),
            request.getCliente().getFormaPagamento(),
            request.getCliente().getModeloVeiculo(),
            request.getCliente().getPlacaVeiculo()
        );

        // Criar entidade Orçamento
        Orcamento orcamento = new Orcamento();
        orcamento.setCliente(cliente);

        // Criar serviços
        List<Servico> servicos = request.getServicos().stream()
            .map(s -> {
                Servico servico = new Servico(s.getQuantidade(), s.getDescricao(), s.getValor());
                servico.setOrcamento(orcamento);
                return servico;
            })
            .collect(Collectors.toList());
        orcamento.setServicos(servicos);

        // Criar peças
        List<Peca> pecas = request.getPecas().stream()
            .map(p -> {
                Peca peca = new Peca(p.getNome(), p.getValor());
                peca.setOrcamento(orcamento);
                return peca;
            })
            .collect(Collectors.toList());
        orcamento.setPecas(pecas);

        // Calcular valor total
        double valorTotal = servicos.stream().mapToDouble(Servico::getValor).sum();
        orcamento.setValorTotal(valorTotal);

        // Salvar no banco
        orcamentoRepository.save(orcamento);

        // Gerar PDF
        return pdfService.gerarPdfOrcamento(orcamento);
    }
}