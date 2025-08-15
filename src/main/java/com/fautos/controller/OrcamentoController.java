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

            // Conteúdo do PDF usando os getters corretos
            document.add(new Paragraph("Orçamento de: " + request.getCliente().getNome()));
            document.add(new Paragraph("Telefone: " + request.getCliente().getTelefone()));
            document.add(new Paragraph("Veículo: " + request.getCliente().getModeloVeiculo()));
            document.add(new Paragraph("Placa: " + request.getCliente().getPlacaVeiculo()));

            document.close(); // fecha o PDF

            byte[] pdfBytes = outputStream.toByteArray();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", "orcamento.pdf");

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(pdfBytes);

        } catch (Exception e) {
            e.printStackTrace(); // loga erro no Railway
            return ResponseEntity.internalServerError().build();
        }
    }
}