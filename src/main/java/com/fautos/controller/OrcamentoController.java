package com.fautos.controller;

import com.fautos.dto.OrcamentoRequest;
import com.fautos.service.OrcamentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orcamentos")
@CrossOrigin(origins = "https://fautos-orcamentos.netlify.app")
public class OrcamentoController {

    @Autowired
    private OrcamentoService orcamentoService;

    @PostMapping("/gerar-pdf")
    public ResponseEntity<byte[]> gerarPdfOrcamento(@RequestBody OrcamentoRequest request) {
        try {
            byte[] pdfBytes = orcamentoService.criarOrcamentoEGerarPdf(request);
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", "orcamento.pdf");
            
            return ResponseEntity.ok()
                .headers(headers)
                .body(pdfBytes);
                
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}