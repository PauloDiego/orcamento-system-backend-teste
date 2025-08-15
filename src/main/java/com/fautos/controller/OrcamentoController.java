package com.fautos.controller;

import com.fautos.dto.OrcamentoRequest;
import com.fautos.service.OrcamentoService;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


import java.io.ByteArrayOutputStream;

@RestController
@RequestMapping("/api/orcamentos")
@CrossOrigin(origins = "https://fautos-orcamentos.netlify.app")
public class OrcamentoController {

    @Autowired
    private OrcamentoService orcamentoService;

    @PostMapping("/gerar-pdf")
    public ResponseEntity<byte[]> gerarPdfOrcamento(@RequestBody OrcamentoRequest request) {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            PdfWriter writer = new PdfWriter(outputStream);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);

            // 1️⃣ Informações do cliente
            document.add(new Paragraph("===== ORÇAMENTO =====").setBold());
            document.add(new Paragraph("Cliente: " + request.getCliente().getNome()));
            document.add(new Paragraph("Telefone: " + request.getCliente().getTelefone()));
            document.add(new Paragraph("Veículo: " + request.getCliente().getModeloVeiculo()));
            document.add(new Paragraph("Placa: " + request.getCliente().getPlacaVeiculo()));
            document.add(new Paragraph("Forma de Pagamento: " + request.getCliente().getFormaPagamento()));
            document.add(new Paragraph("\n"));

            // 2️⃣ Serviços
            if(request.getServicos() != null && !request.getServicos().isEmpty()) {
                document.add(new Paragraph("----- SERVIÇOS -----").setBold());
                for (OrcamentoRequest.ServicoDTO servico : request.getServicos()) {
                    document.add(new Paragraph(
                            servico.getQuantidade() + "x " + servico.getDescricao() + " - R$ " + servico.getValor()
                    ));
                }
                document.add(new Paragraph("\n"));
            }

            // 3️⃣ Peças
            if(request.getPecas() != null && !request.getPecas().isEmpty()) {
                document.add(new Paragraph("----- PEÇAS -----").setBold());
                for (OrcamentoRequest.PecaDTO peca : request.getPecas()) {
                    document.add(new Paragraph(
                            peca.getNome() + " - R$ " + peca.getValor()
                    ));
                }
                document.add(new Paragraph("\n"));
            }

            // 4️⃣ Fechar PDF
            document.close();
            byte[] pdfBytes = outputStream.toByteArray();

            // 5️⃣ Headers para download
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", "orcamento.pdf");

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(pdfBytes);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

}